package com.jeeutils;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class URLUtils {

	public static String getBaseUrl(HttpServletRequest request) {

		String url = null;

		try {
			URL reconstructedURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "" + request.getContextPath());
			url = reconstructedURL.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			url = null;
		}

		return url;
	}

}