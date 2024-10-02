package com.webapp.models;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeeutils.S3Utils;
import com.utils.CommonUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.UploadAction;

public class ProcessCsvThread extends Thread {

	private static Logger logger = Logger.getLogger(ProcessCsvThread.class);

	private String tourId;

	private String bucket;

	private String tempStoragePath;

	public ProcessCsvThread(String tourId, String bucket, String tempStoragePath) {

		this.tourId = tourId;
		this.bucket = bucket;
		this.tempStoragePath = tempStoragePath;
		this.start();
	}

	@Override
	public void run() {

		String fileName = tourId + ".csv";

		List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();

		try {

			File fileToWrite = new File(tempStoragePath + fileName);

			S3Utils.downloadFromBucket(bucket, fileName, fileToWrite);

			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			br = new BufferedReader(new FileReader(fileToWrite));

			long count = 0;

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
			Map<String, Object> location = new HashMap<String, Object>();

			while ((line = br.readLine()) != null) {
				count++;
				// use comma as separator
				String[] latLong = line.split(cvsSplitBy);
				Map<String, Object> csvMap = new HashMap<String, Object>();
				if (count == 1) {
					csvMap.put("time", latLong[0]);
					csvMap.put("latitude", latLong[1]);
					csvMap.put("longitude", latLong[2]);

					location.put("prelatitude", latLong[1]);
					location.put("prelongitude", latLong[2]);
					location.put("nextlatitude", latLong[1]);
					location.put("nextlongitude", latLong[2]);

					locationList.add(csvMap);
				} else {
					location.put("prelatitude", location.get("nextlatitude"));
					location.put("prelongitude", location.get("nextlongitude"));
					location.put("nextlatitude", latLong[1]);
					location.put("nextlongitude", latLong[2]);

					double driverDistance = CommonUtils.distance(Double.parseDouble(location.get("prelatitude").toString()), Double.parseDouble(location.get("prelongitude").toString()), Double.parseDouble(location.get("nextlatitude").toString()),
								Double.parseDouble(location.get("nextlongitude").toString()), 'K');

					if (driverDistance * 1000 < 300) {

						csvMap.put("time", latLong[0]);
						csvMap.put("latitude", latLong[1]);
						csvMap.put("longitude", latLong[2]);
						locationList.add(csvMap);
					}
				}

				double driverDistance = CommonUtils.distance(Double.parseDouble(location.get("prelatitude").toString()), Double.parseDouble(location.get("prelongitude").toString()), Double.parseDouble(location.get("nextlatitude").toString()),
							Double.parseDouble(location.get("nextlongitude").toString()), 'K');
				if (driverDistance * adminSettingsModel.getDistanceUnits() > 300) {
					locationList.addAll(getInbetweenLocations(location.get("prelatitude").toString(), location.get("prelongitude").toString(), location.get("nextlatitude").toString(), location.get("nextlongitude").toString()));
				}
			}

			br.close();

			File csvFileWrite = new File(tempStoragePath + "/" + "temp_" + fileName);
			writeCsvFile(csvFileWrite, fileToWrite, locationList);

			if (locationList.size() > 1) {

				Map<String, Object> destiNationMap = locationList.get(locationList.size() - 1);

				String destLati = destiNationMap.get("latitude").toString();
				String destLongi = destiNationMap.get("longitude").toString();
				String destiGeoLocation = "ST_GeographyFromText('SRID=4326;POINT(" + destLongi + "  " + destLati + ")')";

				String latlng = destLati + "," + destLongi;

				String destiAddress = CommonUtils.getAddressFromLatLong(latlng);

				Map<String, Object> sourceMap = locationList.get(0);

				String sourceLati = sourceMap.get("latitude").toString();
				String sourceLongi = sourceMap.get("longitude").toString();
				String sourceGeoLocation = "ST_GeographyFromText('SRID=4326;POINT(" + sourceLongi + "  " + sourceLati + ")')";

				latlng = sourceLati + "," + sourceLongi;

				String sourceAddress = CommonUtils.getAddressFromLatLong(latlng);

				TourModel tour = new TourModel();
				tour.setTourId(tourId);

				TourModel tourModel = TourModel.getTourDetailsByTourId(this.tourId);

				if (!tourModel.isAirportFixedFareApplied()) {
					tour.setDestinationGeolocation(destiGeoLocation);
					tour.setDestinationAddress(destiAddress);
					tour.setSourceAddress(sourceAddress);
					tour.setSourceGeolocation(sourceGeoLocation);
					tour.updateTourAddress();
					uploadStaticMapImageInBucket(sourceLati, sourceLongi, destLati, destLongi);
				} else {
					uploadStaticMapImageInBucket(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());
				}
			}

		} catch (Exception e) {

			logger.error("\n\n\t" + e);
		}

	}

	private List<Map<String, Object>> getInbetweenLocations(String prelatitude, String prelongitude, String nextlatitude, String nextlongitude) {

		List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();

		String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + prelatitude + "," + prelongitude + "&destination=" + nextlatitude + "," + nextlongitude + "&sensor=false&key=AIzaSyDvH4SjvJ0yderdVozYBhiWMEzOKuJkaCw";
		HttpClient client = new HttpClient();

		GetMethod method = new GetMethod(url);

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

			}

			byte[] responseBody = method.getResponseBody();

			String jsonText = new String(responseBody);
			JSONObject json = new JSONObject(jsonText);
			JSONArray jsonArray = json.getJSONArray("routes");
			json = jsonArray.getJSONObject(0);
			jsonArray = json.getJSONArray("legs");
			json = jsonArray.getJSONObject(0);
			jsonArray = json.getJSONArray("steps");

			for (int i = 0; i < jsonArray.length(); i++) {
				json = jsonArray.getJSONObject(i);
				JSONObject endLocation = json.getJSONObject("end_location");
				Map<String, Object> location = new HashMap<String, Object>();
				location.put("latitude", endLocation.getString("lat"));
				location.put("longitude", endLocation.getString("lng"));
				location.put("time", -1);
				locationList.add(location);
			}

		} catch (Exception e) {

		}

		return locationList;
	}

	private void writeCsvFile(File file, File newfile, List<Map<String, Object>> locationList) {

		try {

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (Map<String, Object> outPut : locationList) {
				String line = outPut.get("time") + "," + outPut.get("latitude") + "," + outPut.get("longitude");
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			if (file.renameTo(newfile)) {

				try {
					UploadAction imageUpload = new UploadAction();
					imageUpload.uploadImage(this.tourId + ".csv", this.bucket, this.tempStoragePath);
				} catch (Exception e) {
					e.printStackTrace();

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void uploadStaticMapImageInBucket(String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude) throws IOException {

		String sourceLatiLong = sourceLatitude + "," + sourceLongitude;
		String destinationLatLong = destinationLatitude + "," + destinationLongitude;

		String params = "&origin=" + sourceLatiLong + "&destination=" + destinationLatLong + "&sensor=false&key=" + ProjectConstants.GOOGLE_PLACE_KEY;

		String urlString = "https://maps.googleapis.com/maps/api/directions/json?" + params;

		String dataString = "";

		InputStream inputStream = null;
		HttpURLConnection connection = null;

		try {

			URL url = new URL(urlString);

			connection = (HttpURLConnection) url.openConnection();

			connection.connect();

			inputStream = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			StringBuffer sb = new StringBuffer();

			String line = "";

			while ((line = br.readLine()) != null) {

				sb.append(line);
			}

			dataString = sb.toString();

			br.close();

		} catch (Throwable t) {

			t.printStackTrace();

		} finally {

			inputStream.close();
			connection.disconnect();
		}

		JSONObject polyLine = null;

		try {

			JSONObject jObject = new JSONObject(dataString);

			org.json.JSONArray jsonRouteArray = jObject.getJSONArray("routes");

			JSONObject jsonRoute = jsonRouteArray.getJSONObject(0);

			polyLine = (JSONObject) jsonRoute.get("overview_polyline");

		} catch (Exception e) {

			e.printStackTrace();
		}

		String imageSize = ProjectConstants.STATIC_MAP_IMG_SIZE;
		String sourceMarker = "color:0x009641%7Clabel:S%7C" + sourceLatiLong;
		String destinationMarker = "color:0xea0e2b%7Clabel:D%7C" + destinationLatLong;

		String staticMapParams = "&size=" + imageSize + "&markers=" + sourceMarker + "&markers=" + destinationMarker + "&path=weight:5%7Ccolor:blue%7Cenc:" + polyLine.get("points");

		BufferedImage br = ImageIO.read(new URL("https://maps.googleapis.com/maps/api/staticmap?" + staticMapParams));

		String staticMapImgFileName = ProjectConstants.STATIC_MAP_IMG_PRE_STRING + this.tourId + ".png";

		File outputfile = new File(this.tempStoragePath + staticMapImgFileName);

		ImageIO.write(br, "png", outputfile);

		try {

			UploadAction imageUpload = new UploadAction();
			imageUpload.uploadImage(staticMapImgFileName, this.bucket, this.tempStoragePath);

		} catch (Exception e) {

			e.printStackTrace();
		}

		String staticMapImgUrl = "/getimage.do?imageId=" + staticMapImgFileName;

		int updateStatus = InvoiceModel.updateStaticMapImgUrlByTourId(this.tourId, staticMapImgUrl);

		if (updateStatus > 0) {

		}

		if (outputfile != null) {

			if (outputfile.exists()) {

				outputfile.delete();
			}
		}
	}

}