package com.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.utils.myhub.WebappPropertyUtils;

public class HttpURLConnectionUtils {

	private static Logger logger = Logger.getLogger(HttpURLConnectionUtils.class);

	public static Map<String, Object> sendGET(String url) {

		HttpURLConnection connection = null;
		
		try {
			URL urlObj = new URL(url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("api-token", WebappPropertyUtils.LED_API_TOKEN);
			connection.setRequestProperty("service-name", WebappPropertyUtils.LED_SERVICE_NAME);
			
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {

				ObjectMapper objectMapper = new ObjectMapper();

				// Read the InputStream and parse it into a Map
				Map<String, Object> jsonMap = objectMapper.readValue(connection.getInputStream(), Map.class);
				
				return jsonMap;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return null;
	}

	public static Response sendPOST(String url, Map<String, Object> requestObject) {
		
		HttpURLConnection connection = null;
		//BufferedReader reader = null;
	       //OutputStream outputStream = null;

		try {
			URL postUrl = new URL(url);
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("api-token", WebappPropertyUtils.LED_API_TOKEN);
			connection.setRequestProperty("service-name", WebappPropertyUtils.LED_SERVICE_NAME);
			connection.setDoOutput(true);
			ObjectMapper objectMapper = new ObjectMapper();
			String jacksonData = objectMapper.writeValueAsString(requestObject);
			
			try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
				wr.writeBytes(jacksonData);
				wr.flush();
			}

			int responseCode = connection.getResponseCode();
			/*
			if (responseCode == HttpURLConnection.HTTP_OK) {
		                // If the response is successful, read the response body
		                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		                String line;
		                StringBuilder response = new StringBuilder();
		                while ((line = reader.readLine()) != null) {
		                    response.append(line);
		                }
		                System.out.println("Response: " + response.toString());
		            } else {
		                System.out.println("HTTP error code: " + responseCode);
		            }
			*/
			

		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			/*
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			*/
		            
		}

		return null;
	}
	
	
	public static List<Object> sendGETArrayObject(String url)  {

		HttpURLConnection connection = null;
		
		try {
			URL urlObj = new URL(url);
			connection = (HttpURLConnection) urlObj.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("api-token", WebappPropertyUtils.LED_API_TOKEN);
			connection.setRequestProperty("service-name", WebappPropertyUtils.LED_SERVICE_NAME);
			
			
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {

				ObjectMapper objectMapper = new ObjectMapper();

				// Read the InputStream and parse it into a Map
				List<Object> jsonMap = objectMapper.readValue(connection.getInputStream(), List.class);

				return jsonMap;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			if (connection != null) {
				connection.disconnect();
			}
		}
		
		return null;
	}

}
