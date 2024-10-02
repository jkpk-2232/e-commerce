package com.jeeutils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

	public static String requestAttributeValue(HttpServletRequest request, String attributeName) {

		Object attributeObj = request.getAttribute(attributeName);

		if (attributeObj == null) {
			return "";
		} else {
			return attributeObj.toString();
		}
	}

	public static long requestLongAttributeValue(HttpServletRequest request, String attributeName) {

		Object attributeObj = request.getAttribute(attributeName);

		if (attributeObj == null) {

			return -1;

		} else {

			String strValue = attributeObj.toString();

			long longValue = -1;

			try {
				longValue = Long.parseLong(strValue);
			} catch (Exception e) {
				// Do Nothing
			}

			return longValue;
		}
	}

}