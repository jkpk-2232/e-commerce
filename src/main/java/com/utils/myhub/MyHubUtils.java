package com.utils.myhub;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jeeutils.StringUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

public class MyHubUtils {

	public static List<String> splitStringByCommaList(String commaSeparatedString) {

		if (!StringUtils.validString(commaSeparatedString)) {
			return null;
		}

		return Arrays.asList(commaSeparatedString.split(","));
	}

	public static String[] splitStringByCommaArray(String commaSeparatedString) {

		if (!StringUtils.validString(commaSeparatedString)) {
			return null;
		}

		return commaSeparatedString.split(",");
	}

	public static String getSearchStringFormat(String tempKey) {

		if (tempKey == null) {
			tempKey = "";
		}

		return ("%").concat(tempKey).concat("%");
	}

	public static String formatPhoneNumber(String phoneNoCode, String phoneNo) {
		return (phoneNoCode.concat("-").concat(phoneNo));
	}

	public static String formatFullName(String firstName, String lastName) {
		return (firstName.concat(" ").concat(lastName));
	}

	public static String getKilometerString(double meters) {
		return BusinessAction.df.format(meters / ProjectConstants.KM_UNITS) + " " + ProjectConstants.KM;
	}

	public static long convertMillisToSeconds(long milliSeconds) {
		return (milliSeconds / 1000);
	}

	public static long convertMillisToSeconds(String milliSeconds) {

		long milliSecondsLong = 0;

		try {
			milliSecondsLong = Long.parseLong(milliSeconds);
		} catch (Exception e) {
			e.printStackTrace();
			milliSecondsLong = 0;
		}

		return (milliSecondsLong / 1000);
	}

	public static long convertSecondsToMillis(long seconds) {
		return (seconds * 1000);
	}

	public static long convertSecondsToMillis(String seconds) {

		long secondsLong = 0;

		try {
			secondsLong = Long.parseLong(seconds);
		} catch (Exception e) {
			e.printStackTrace();
			secondsLong = 0;
		}

		return (secondsLong * 1000);
	}

	public static int searchNumericFormat(String searchKey) {

		try {
			return Integer.parseInt(searchKey);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return -1;
	}

	public static String removeWhiteSpaces(String temp) {
		return temp.replaceAll("\\s", "");
	}

	public static String generatePassword() {

		if (!WebappPropertyUtils.RANDOM_CODE_PASS) {
			return "123456";
		}

		final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String intChar = "0123456789";

		Random r = new Random();

		String pass = "";

		while (pass.length() < 6) {

			int rPick = r.nextInt(2);

			if (rPick == 0) {

				int spot = r.nextInt(9);
				pass += intChar.charAt(spot);

			} else if (rPick == 1) {

				int spot = r.nextInt(25);

				Pattern pattern = Pattern.compile("[A-Z]");

				Matcher match = pattern.matcher(pass);

				if (!match.find()) {
					pass += uCase.charAt(spot);
				}
			}
		}

		return pass;
	}

	public static String generateVerificationCode() {

		if (!WebappPropertyUtils.RANDOM_CODE_PASS) {
			return "1234";
		}

		final String intChar = "123456789";

		Random r = new Random();

		String verificationCode = "";

		while (verificationCode.length() < 4) {

			int spot = r.nextInt(9);

			verificationCode += intChar.charAt(spot);
		}

		return verificationCode;
	}

	public static String getTrimmedTo(String string) {
		return getTrimmedTo(string, ProjectConstants.DEFAULT_TRIM_LENGTH);
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

	public static String convertMinutesToHours(long minutesInLong) {

		long hours = minutesInLong / 60; // since both are ints, you get an int
		long minutes = minutesInLong % 60;

		String format = hours + ":" + minutes;

		return format;
	}

	public static String convertMinutesToDays(long minutesInLong) {

		String days = String.valueOf(minutesInLong / (60 * 24));

		return days;
	}

	public static long convertHoursToMinutes(String minutesInString, long days) {

		if (minutesInString == null || "".equalsIgnoreCase(minutesInString)) {
			return 0;
		}

		String hours = minutesInString.substring(0, minutesInString.indexOf(":"));
		String minutes = minutesInString.substring(minutesInString.indexOf(":"));
		minutes = minutes.substring(1);

		long hoursInLong = Long.parseLong(hours.trim()) * 60;

		long minutesInLong = Long.parseLong(minutes.trim()) + hoursInLong + (days * 1440);
		return minutesInLong;
	}

	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {

		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		double meterConversion = 1609;

		double distance = dist * meterConversion * 0.000621371;
		BigDecimal bd = new BigDecimal(distance);
		bd = bd.setScale(2, RoundingMode.HALF_UP);

		return bd.doubleValue();
	}

	public static String formatPlateNumber(String plateNumber) {

		StringBuilder processString = new StringBuilder(plateNumber);
		StringBuilder outputString = new StringBuilder();

		int spaceCout = 1;

		char[] processStringArr = processString.toString().toCharArray();

		for (char c : processStringArr) {

			if (spaceCout == 1) {
				if (Character.isDigit(c)) {
					outputString.append(" ");
					spaceCout++;
				} else {
					outputString.append(c);
				}
			}

			if (spaceCout == 2) {
				if (Character.isDigit(c)) {
					outputString.append(c);
				} else {
					outputString.append(" ");
					spaceCout++;
				}
			}

			if (spaceCout == 3) {
				if (Character.isDigit(c)) {
					outputString.append(" ");
					spaceCout++;
				} else {
					outputString.append(c);
				}
			}

			if (spaceCout > 3) {
				outputString.append(c);
			}
		}

		return outputString.toString();
	}

	public static String getDistanceInProjectUnitFromMeters(double distanceInMeters, AdminSettingsModel adminSettings) {
		return StringUtils.valueOf(distanceInMeters / adminSettings.getDistanceUnits());
	}

	public static double getDistanceInProjectUnitFromMeters(double distanceInMeters) {

		double distanceInProjectUnit;

		distanceInProjectUnit = MyHubUtils.getKilometerFromMeters(distanceInMeters);

		return distanceInProjectUnit;
	}

	public static double getKilometerFromMeters(double distanceInMeters) {

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		double distanceInKm;

		distanceInKm = distanceInMeters / adminSettings.getDistanceUnits();

		return distanceInKm;
	}

	public static double getMilesFromMeters(double distanceInMeters) {

		double distanceInMiles;

		distanceInMiles = distanceInMeters / 1609.344;

		return distanceInMiles;
	}

	public static String getDriverSettlementString(AdminSettingsModel adminSettings, double totalAdminSettlementAmount) {
		if (totalAdminSettlementAmount > 0) {
			// Receivable amount
			return NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, (adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(totalAdminSettlementAmount) + " (Collect)"));
		} else if (totalAdminSettlementAmount < 0) {
			// Payable amount
			return NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(0 - (totalAdminSettlementAmount)) + " (Pay)"));
		} else {
			// No settlement amount
			return NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.PRIMARY, (adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(totalAdminSettlementAmount) + " (No Settlement)"));
		}
	}

	public static String getDriverSettlement(AdminSettingsModel adminSettings, double totalAdminSettlementAmount) {
		if (totalAdminSettlementAmount > 0) {
			// Receivable amount
			return ((adminSettings.getCurrencySymbol() + StringUtils.valueOf(totalAdminSettlementAmount) + " (Collect)"));
		} else if (totalAdminSettlementAmount < 0) {
			// Payable amount
			return ((adminSettings.getCurrencySymbol() + StringUtils.valueOf(0 - (totalAdminSettlementAmount)) + " (Pay)"));
		} else {
			// No settlement amount
			return ((adminSettings.getCurrencySymbol() + StringUtils.valueOf(totalAdminSettlementAmount) + " (No Settlement)"));
		}
	}

	public static String getAmountWithCurrency(AdminSettingsModel adminSettings, double temp) {
		return (adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(temp));
	}

	public static List<String> convertStringToList(String temp) {

		if (!StringUtils.validString(temp)) {
			return null;
		}

		List<String> convertedRegionsList = Stream.of(temp.split(",", -1)).collect(Collectors.toList());

		return convertedRegionsList;
	}

	public static String generateSixDigVerificationCode(int length) {

		final String AB = "0123456789";

		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {

			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}

		return sb.toString();
	}

}