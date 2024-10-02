package com.webapp.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.utils.EncryptionUtils;

public class TestAction {

	public static Logger logger = Logger.getLogger(TestAction.class);

	public static void main(String[] args) throws IOException {

//		AwsSesEmailUtils.sendTextMail("david.m@bilwamindia.in", "Test SES MyHubs", "Test SES MyHubs", null);

		try {

			String METAMORPH_USER_NAME = "myhubtech";
			String METAMORPH_PASSWORD = "Meta@123";
			String METAMORPH_FROM = "MYHUB";
			String toMobile = "+918149199317";
			String msg = "The passenger has change in plans. TRIP CANCELED! @MyHub";
			String templateId = "1707169700766627360";

			String params = "username=" + METAMORPH_USER_NAME + "&password=" + METAMORPH_PASSWORD + "&from=" + METAMORPH_FROM + "&to=" + toMobile + "&message=" + URLEncoder.encode(msg, "UTF-8") + "&sms_type=2&template_id=" + templateId;

			String url = "https://www.metamorphsystems.com/index.php/api/bulk-sms?" + params;

			System.out.println("url\t" + url);

			URL obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();

			System.out.println("responseCode\t" + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String inputLine;

			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {

				response.append(inputLine);
			}

			in.close();

			System.out.println("response\t" + response);
			logger.info("\n\n\n\n\tresponseCode\t" + responseCode);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(EncryptionUtils.decryptByte("7QygYtVWCPSQCW5AnblK8g=="));
	}
}