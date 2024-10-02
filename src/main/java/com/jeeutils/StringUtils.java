package com.jeeutils;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class StringUtils {

	public static String[] split(String toSplit, String separator) {

		StringTokenizer tokenizer = new StringTokenizer(toSplit, separator);

		int count = tokenizer.countTokens();

		String[] tokens = new String[count];

		for (int i = 0; i < count; i++) {
			tokens[i] = tokenizer.nextToken();
		}

		return tokens;
	}

	public static String capatalize(String str) {

		if (str.length() == 0) {
			return str;
		}

		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static String removePrefix(String str, String prefix) {

		if (str.length() == prefix.length()) {
			return "";
		}

		return str.substring(prefix.length());
	}

	public static String getTrimmedTo(String string, int trimLen) {

		String trimString = string;

		int upperletterCounter = 0;

		if (string.length() > trimLen) {

			trimString = string.substring(0, trimLen);

			if (trimString.length() > 0) {

				char chars[] = trimString.toCharArray();

				for (char c : chars) {

					if (Character.isUpperCase(c)) {
						upperletterCounter++;
					}
				}
			}
			if (upperletterCounter > 0) {
				trimLen = trimLen - (upperletterCounter / 2) - 3;
				trimString = trimString.substring(0, trimLen);
			} else {
				trimString = trimString.substring(0, trimLen - 3);
			}

			trimString += "...";
		}

		return trimString;
	}

	public static String getExtension(String fileName) {

		int lastIndexOfDot = fileName.lastIndexOf('.');

		if (lastIndexOfDot == -1) {
			return "";
		}

		String extension = fileName.substring(lastIndexOfDot);

		return extension;
	}

	public static boolean validString(String tempStr) {
		return tempStr != null && !tempStr.trim().isEmpty();
	}

	public static String valueOf(int temp) {
		return String.valueOf(df_long.format(temp));
	}

	public static String valueOf(long temp) {
		return String.valueOf(df_long.format(temp));
	}

	public static String valueOf(double temp) {
		return String.valueOf(df_double.format(temp));
	}

	public static String valueOfDfNew(double temp) {
		return String.valueOf(df_new.format(temp));
	}

	private static final DecimalFormat df_new = new DecimalFormat("#######0.00");
	private static final DecimalFormat df_double = new DecimalFormat("##.##");
	private static final DecimalFormat df_long = new DecimalFormat("##");

	public static double doubleValueOf(String temp) {
		return Double.parseDouble(df_double.format(Double.parseDouble(temp)));
	}

	public static long longValueOf(String temp) {
		return Long.parseLong(df_long.format(Long.parseLong(temp)));
	}

	public static int intValueOf(String temp) {
		return Integer.parseInt(df_long.format(Integer.parseInt(temp)));
	}

	public static boolean booleanValueOf(String temp) {
		return Boolean.parseBoolean(temp);
	}
}
