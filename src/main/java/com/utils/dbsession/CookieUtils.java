package com.utils.dbsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CookieUtils {

	public static final String DELETED_COOKIE_VALUE = "finished";

	protected static Logger logger = Logger.getLogger(CookieUtils.class);

	public static Cookie getCookie(String name, HttpServletRequest request) {

		Cookie cookie = null;

		Cookie[] cookies = request.getCookies();

		for (int i = 0; cookies != null && i < cookies.length; i++) {

			if (cookies[i].getName().equals(name)) {
				cookie = cookies[i];
			}
		}

		return cookie;
	}

	public static void setCookie(Cookie cookie, HttpServletRequest request, HttpServletResponse response) {
		response.addCookie(cookie);
	}

	public static void deleteCookie(Cookie cookie, HttpServletRequest request, HttpServletResponse response) {
		cookie.setValue(DELETED_COOKIE_VALUE);
		cookie.setMaxAge(0);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}
}