package com.jeeutils;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final String MOBILE_PATTERN = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";

	private static final String ALPHA_CHARECTER = "^[a-zA-Z]*$";

	private static final String alphaDashCharsRegExp = "^[A-Z a-z0-9_-]+$";

	public static boolean isValidEmail(String email) {

		boolean result = false;

		Pattern p = Pattern.compile(EMAIL_PATTERN);

		Matcher matcher = p.matcher(email);

		result = matcher.matches();

		return result;
	}

	public static boolean isValidString(String str) {

		boolean result = false;

		String newStr = str.toLowerCase();

		StringTokenizer strTokenizer = new StringTokenizer(newStr);

		while (strTokenizer.hasMoreTokens()) {

			String tokenStr = strTokenizer.nextToken();

			for (int i = 0; i < tokenStr.length(); i++) {

				if (!Character.isLetter(tokenStr.charAt(i))) {
					result = false;
					break;
				} else {
					result = true;
				}
			}
		}

		return result;
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {

		boolean result = false;

		Pattern pattern = Pattern.compile("\\d{3}-\\d{8}");

		Matcher matcher = pattern.matcher(phoneNumber);

		if (matcher.matches()) {
			result = true;
		}

		return result;
	}

	public static boolean isVallidMobileNo(String mobileNumber) {

		boolean result = false;

		Pattern p = Pattern.compile(MOBILE_PATTERN);

		Matcher matcher = p.matcher(mobileNumber);

		result = matcher.matches();

		return result;
	}

	public static boolean isNumber(String str) {

		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			if (!Character.isDigit(str.charAt(i)))
				return false;
		}

		return true;

	}

	public static boolean isAlphaCharecters(String string) {

		boolean result = false;

		Pattern p = Pattern.compile(ALPHA_CHARECTER);

		Matcher matcher = p.matcher(string);

		result = matcher.matches();

		return result;
	}

	public static boolean isAlphaNumeric(String value) {

		boolean result = true;

		Pattern pattern = Pattern.compile(alphaDashCharsRegExp, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(value.toString());

		if (!matcher.matches()) {
			result = false;
		}

		return result;
	}
}
