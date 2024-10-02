package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jeeutils.FrameworkConstants;
import com.jeeutils.StringUtils;
import com.webapp.ProjectConstants;
import com.webapp.models.AdminSettingsModel;

public class CommonUtils {

	protected static Logger logger = Logger.getLogger(CommonUtils.class);

	private static final String GEOCODE_JSON_REQUEST_URL = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&key=" + ProjectConstants.GOOGLE_PLACE_KEY;

	private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());

	public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {

		double theta = lon1 - lon2;

		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	public static double calculateETA(double distance, double distanceUnits) {
		return StringUtils.doubleValueOf(StringUtils.valueOf((distance * distanceUnits) / (5 * 60)));
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static String getAddressFromLatLong(String latlng) {

		String address = "";

		try {

			StringBuilder urlBuilder = new StringBuilder(GEOCODE_JSON_REQUEST_URL);

			if (StringUtils.validString(latlng)) {
				urlBuilder.append("&latlng=").append(URLEncoder.encode(latlng, "UTF-8")).append("&key=").append(ProjectConstants.GOOGLE_PLACE_KEY);
			}

			final GetMethod getMethod = new GetMethod(urlBuilder.toString());

			try {
				httpClient.executeMethod(getMethod);

				byte[] responseBody = getMethod.getResponseBody();

				String jsonText = new String(responseBody);

				JSONObject outputData = new JSONObject(jsonText);
				JSONArray jsonArray = outputData.getJSONArray("results");

				outputData = jsonArray.getJSONObject(0);

				address = outputData.getString("formatted_address");

			} finally {
				getMethod.releaseConnection();
			}

		} catch (Exception e) {
			logger.error("Exception occured during getting address : ", e);
		}

		return address;
	}

	public static double getInbetweenLocations(String prelatitude, String prelongitude, String nextlatitude, String nextlongitude) {

		double ditMiles = 0.0;

		String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + prelatitude + "," + prelongitude + "&destination=" + nextlatitude + "," + nextlongitude + "&sensor=false&key=" + ProjectConstants.GOOGLE_PLACE_KEY;

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

			JSONObject distance = json.getJSONObject("distance");

			if (distance.getString("text").contains("km")) {
				String kmDistanc = distance.getString("text").replace("km", "");
				ditMiles = Double.parseDouble(kmDistanc);
			} else {
				String kmDistanc = distance.getString("text").replace("mi", "");
				ditMiles = Double.parseDouble(kmDistanc) * 1.60934;
			}

		} catch (Exception e) {
		}

		return ditMiles;
	}

	public static double getDistanceMatrixInbetweenLocations(String pickUpLocation, String destLocation) {

		double distanceInMeters = 0.0;

		double distanceInKm = 0.0;

		pickUpLocation = pickUpLocation.trim();

		destLocation = destLocation.trim();

		try {
			pickUpLocation = URLEncoder.encode(pickUpLocation, "UTF-8");
			destLocation = URLEncoder.encode(destLocation, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + pickUpLocation + "&destinations=" + destLocation + "&key=" + ProjectConstants.GOOGLE_PLACE_KEY + "&language=" + FrameworkConstants.LANGUAGE_ENGLISH;

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

			JSONObject distance = json.getJSONObject("distance");

			distanceInMeters = Double.parseDouble(distance.getString("value"));

			distanceInKm = (distanceInMeters / 1000);

		} catch (Exception e) {
			logger.info("\n\n\n\n\tException occurred while fetching distance.");
		}

		return distanceInKm;
	}

	public static Map<String, Double> getDistanceMatrixInbetweenLocations(String prelatitude, String prelongitude, String nextlatitude, String nextlongitude) {

		Map<String, Double> output = new HashMap<String, Double>();

		double distanceInMeters = 0.0;
		double distanceInKm = 0.0;
		double durationInSec = 0.0;
		double durationInMin = 0.0;

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + prelatitude + "," + prelongitude + "&destinations=" + nextlatitude + "," + nextlongitude + "&sensor=true&mode=driving&key=" + ProjectConstants.GOOGLE_PLACE_KEY + "&language="
					+ FrameworkConstants.LANGUAGE_ENGLISH;

		HttpClient client = new HttpClient();

		GetMethod method = new GetMethod(url);

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try {

			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
			}

			BufferedReader br = null;

			br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));

			String readLine;
			String outPut = "";

			while (((readLine = br.readLine()) != null)) {
				outPut = outPut + readLine;

			}

			JSONObject json = new JSONObject(outPut);

			JSONArray jsonArray = json.getJSONArray("rows");

			json = jsonArray.getJSONObject(0);

			JSONArray jsonArray1 = json.getJSONArray("elements");

			json = jsonArray1.getJSONObject(0);

			distanceInMeters = Double.parseDouble(json.getJSONObject("distance").get("value").toString());

			durationInSec = Double.parseDouble(json.getJSONObject("duration").get("value").toString());

			distanceInKm = (distanceInMeters / adminSettingsModel.getDistanceUnits());

			durationInMin = (durationInSec / 60);

		} catch (Exception e) {
			logger.info("\n\n\n\n\tException occurred while fetching distance." + e);
		}

		output.put("distanceInMeters", distanceInMeters);
		output.put("distanceInKm", distanceInKm);
		output.put("durationInMin", durationInMin);

		return output;
	}

	public static Map<String, String> getLatitudeLongitudeByPlaceId(String placeId) {

		Map<String, String> output = new HashMap<String, String>();

		String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=" + placeId + "&key=" + ProjectConstants.GOOGLE_PLACE_KEY;

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

			JSONArray jsonArray = json.getJSONArray("results");

			json = jsonArray.getJSONObject(0);

			String latitude = json.getJSONObject("geometry").getJSONObject("location").get("lat").toString();

			String longitude = json.getJSONObject("geometry").getJSONObject("location").get("lng").toString();

			output.put("latitude", latitude);
			output.put("longitude", longitude);

		} catch (Exception e) {
			logger.info("\n\n\n\n\tException occurred while fetching latitude longitude using place id.");
			e.printStackTrace();
		}

		return output;
	}

	public static Map<String, Double> getDistanceMatrixInbetweenLocations(String prelatitude, String prelongitude, String nextlatitude, String nextlongitude, double distanceUnits) {

		Map<String, Double> output = new HashMap<String, Double>();

		double distanceInMeters = 0.0;
		double distanceInKm = 0.0;
		double durationInSec = 0.0;
		double durationInMin = 0.0;

		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + prelatitude + "," + prelongitude + "&destinations=" + nextlatitude + "," + nextlongitude + "&sensor=true&mode=driving&key=" + ProjectConstants.GOOGLE_PLACE_KEY + "&language="
					+ FrameworkConstants.LANGUAGE_ENGLISH;

		HttpClient client = new HttpClient();

		GetMethod method = new GetMethod(url);

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try {

			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
			}

			BufferedReader br = null;

			br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));

			String readLine;
			String outPut = "";

			while (((readLine = br.readLine()) != null)) {
				outPut = outPut + readLine;
			}

			JSONObject json = new JSONObject(outPut);

			JSONArray jsonArray = json.getJSONArray("rows");

			json = jsonArray.getJSONObject(0);

			JSONArray jsonArray1 = json.getJSONArray("elements");

			json = jsonArray1.getJSONObject(0);

			distanceInMeters = Double.parseDouble(json.getJSONObject("distance").get("value").toString());

			durationInSec = Double.parseDouble(json.getJSONObject("duration").get("value").toString());

			distanceInKm = (distanceInMeters / distanceUnits);

			durationInMin = (durationInSec / 60);

		} catch (Exception e) {
			logger.info("\n\n\n\n\tException occurred while fetching distance.");
			e.printStackTrace();
		}

		output.put("distanceInKm", distanceInKm);
		output.put("durationInMin", durationInMin);

		return output;
	}

	public static void main(String[] args) {
		System.out.println(distance(18.587256, 73.738203, 18.559653, 73.779903, 'K'));
	}

}