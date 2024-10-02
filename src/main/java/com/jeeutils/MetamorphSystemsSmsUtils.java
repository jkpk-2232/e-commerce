package com.jeeutils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.utils.myhub.WebappPropertyUtils;
import com.webapp.actions.BusinessAction;

public class MetamorphSystemsSmsUtils extends BusinessAction {

	private static Logger logger = Logger.getLogger(MetamorphSystemsSmsUtils.class);

	public static boolean sendSmsToSingleUser(String msg, String toMobile, String templateId) {

		if (!WebappPropertyUtils.RANDOM_CODE_PASS) {
			return true;
		}

		try {

			URL obj;

			String params = "username=" + WebappPropertyUtils.METAMORPH_USER_NAME + "&password=" + WebappPropertyUtils.METAMORPH_PASSWORD + "&from=" + WebappPropertyUtils.METAMORPH_FROM + "&to=" + toMobile + "&message=" + URLEncoder.encode(msg, "UTF-8")
						+ "&sms_type=2&template_id=" + templateId;

			String url = "https://www.metamorphsystems.com/index.php/api/bulk-sms?" + params;

			System.out.println("url\t" + url);

			obj = new URL(url);

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
			System.out.println("Exception\t" + e);
			logger.info("\n\n\n\n\t ================> error sms sending ... \t" + e);
		}

		return true;
	}
}