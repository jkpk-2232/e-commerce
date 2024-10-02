package com.webapp.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.jeeutils.FrameworkConstants;
import com.jeeutils.MessagesUitls;
import com.jeeutils.models.Message;
import com.utils.LoginUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.models.AdminSettingsModel;

public class BusinessApiAction extends Action {

	public static Logger logger = Logger.getLogger(BusinessApiAction.class);

	protected List<String> errorMessages = new ArrayList<String>();

	public static final String TECHNICAL_ERROR_TYPE = "ERROR-TECHNICAL";

	public static final String BUSSINES_ERROR_TYPE = "ERROR-BUSSINESS";

	protected static final String SUCCESS_MESSAGE_TYPE = "SUCCESS";

	private String sessionKey = "";

	protected ServletContext applicationContext;

	protected static final long serialVersionUID = 1897776556453232L;

	public String getSessionKey() {
		return sessionKey;
	}

	public String checkValidSession(String sessionKey) {
		String userId = LoginUtils.isSessionExists(sessionKey);
		return userId;
	}

	private Message ouputMessage(String msgType, String message) {
		Message messageModel = new Message();
		messageModel.setType(msgType);
		List<String> messages = new ArrayList<String>();
		messages.add(message);
		messageModel.setMessages(messages);
		return messageModel;
	}

	private Message ouputMessage(String msgType, List<String> errors) {
		Message messageModel = new Message();
		messageModel.setType(msgType);
		messageModel.setMessages(errors);
		return messageModel;
	}

	public static String messageForKey(String key, HttpServletRequest request) {

		if (!isLocalizationEnabled()) {
			return null;
		}

		String language = null;

		language = getUserLanguage(request);

		return MessagesUitls.messageForKey(key, language);
	}

	public static String getUserLanguage(HttpServletRequest request) {

		if (request != null) {

			String langCode = getHeaderLanguageCode(request);

			if (langCode != null) {

				return checkValidLanguageIfNotValidSetToDefault(langCode);
			}
		}

		return FrameworkConstants.LANGUAGE_ENGLISH;
	}

	public static String getHeaderLanguageCode(HttpServletRequest request) {

		return request.getHeader("x-language-code");
	}

	public static String checkValidLanguageIfNotValidSetToDefault(String langCode) {

		for (int i = 0; i < FrameworkConstants.SUPPORT_LANGUAGE.length; i++) {

			if (langCode.equals(FrameworkConstants.SUPPORT_LANGUAGE[i])) {

				return langCode;
			}
		}

		return FrameworkConstants.LANGUAGE_ENGLISH;
	}

	private static boolean isLocalizationEnabled() {

		String localizationEnabled = WebappPropertyUtils.getWebAppProperty(FrameworkConstants.LOCALIZATION_ENABLED);

		if (localizationEnabled == null) {
			localizationEnabled = "no";
		}

		if (localizationEnabled.equalsIgnoreCase(FrameworkConstants.LOCALIZATION_ENABLED_YES)) {
			return true;
		} else {
			return false;
		}
	}

	public static String convertVerificationCode(String verificationCode) {
		return verificationCode;
	}

	public static double getKilometerFromMeters(double distanceInMeters) {

		double distanceInKm;

		distanceInKm = distanceInMeters / 1000;

		return distanceInKm;
	}

	public static double getMilesFromMeters(double distanceInMeters) {

		double distanceInMiles;

		distanceInMiles = distanceInMeters / 1609.344;

		return distanceInMiles;
	}

	public static double getDistanceInProjectUnitFromMeters(double distanceInMeters) {

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		double distanceInProjectUnit = distanceInMeters;

		distanceInProjectUnit = distanceInProjectUnit / adminSettingsModel.getDistanceUnits();

		return distanceInProjectUnit;
	}

	public static double millisToMin(double milliseconds) {

		double min = (milliseconds / (1000 * 60));

		return min;
	}

	public static double roundOff(double amount, boolean isRoundOff, boolean decimalOnly, RoundingMode rm, int noOfDigitsAfterDecimal) {

		if (decimalOnly && isRoundOff) {

			return roundToDecimalOnly(amount, noOfDigitsAfterDecimal);

		} else if (isRoundOff) {

			return roundToDecimal(amount, rm, noOfDigitsAfterDecimal);

		} else {

			return amount;
		}
	}

	public static double roundToDecimal(double amount, RoundingMode rm, int noOfDigitsAfterDecimal) {

		BigDecimal bigDecimal = new BigDecimal(amount);
		bigDecimal = bigDecimal.setScale(noOfDigitsAfterDecimal, rm);
		return bigDecimal.doubleValue();
	}

	public static double roundToDecimalOnly(double amount, int noOfDigitsAfterDecimal) {

		double formatedValue;

		formatedValue = Double.parseDouble(df.format(amount));

		return formatedValue;
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

	public Response sendUnauthorizedRequestError() {
		return Response.status(Status.UNAUTHORIZED).entity(ouputMessage(BUSSINES_ERROR_TYPE, "Please Login again. Session expired")).build();
	}

	public Response sendUnexpectedError() {
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ouputMessage(TECHNICAL_ERROR_TYPE, "Unexpected error occured, please try again after some time.")).build();
	}

	public Response sendBussinessError(List<String> errors) {
		return Response.status(Status.BAD_REQUEST).entity(ouputMessage(BUSSINES_ERROR_TYPE, errors)).build();
	}

	public Response sendBussinessError(String message) {
		return Response.status(Status.BAD_REQUEST).entity(ouputMessage(BUSSINES_ERROR_TYPE, message)).build();
	}

	public Response sendTechnicalError(List<String> errors) {
		return Response.status(Status.BAD_REQUEST).entity(ouputMessage(TECHNICAL_ERROR_TYPE, errors)).build();
	}

	public Response sendTechnicalError(String message) {
		return Response.status(Status.BAD_REQUEST).entity(ouputMessage(TECHNICAL_ERROR_TYPE, message)).build();
	}

	public Response sendSuccessMessage(String message) {
		return Response.ok().entity(ouputMessage(SUCCESS_MESSAGE_TYPE, message)).build();
	}

	public Response sendDataResponse(Object obj) {
		return Response.ok(obj).build();
	}

	public static String generateOrderId(int length) {

		final String AB = "0123456789";

		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {

			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}

		return sb.toString();
	}

	public static double roundUpDecimalValueWithDownMode(double value, int numberOfDigitsAfterDecimalPoint) {

		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_DOWN);
		return bigDecimal.doubleValue();
	}

	public static double roundUpDecimalValueWithUpMode(double value, int numberOfDigitsAfterDecimalPoint) {

		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_UP);
		return bigDecimal.doubleValue();
	}

	public static List<Map<String, Object>> calculateLoggedInTimeDayWise(long startDateTimeInMillies, long endDateTimeInMillies) {

		List<Map<String, Object>> loggedInTimeList = new ArrayList<Map<String, Object>>();

		Calendar calStartDateTime = Calendar.getInstance();
		calStartDateTime.setTimeInMillis(startDateTimeInMillies);

		Calendar calEndDateTime = Calendar.getInstance();
		calEndDateTime.setTimeInMillis(endDateTimeInMillies);

		if ((calStartDateTime.get(Calendar.YEAR) == calEndDateTime.get(Calendar.YEAR)) && (calStartDateTime.get(Calendar.DAY_OF_YEAR) == calEndDateTime.get(Calendar.DAY_OF_YEAR))) {

			// Same day
			Map<String, Object> innerMap = new HashMap<String, Object>();

			calEndDateTime.set(Calendar.HOUR_OF_DAY, 16);
			calEndDateTime.set(Calendar.MINUTE, 0);
			calEndDateTime.set(Calendar.SECOND, 0);
			calEndDateTime.set(Calendar.MILLISECOND, 0);

			long difference = endDateTimeInMillies - startDateTimeInMillies;

			innerMap.put("dateTimeInMillies", calEndDateTime.getTimeInMillis());
			innerMap.put("loggedInTimeInMillies", difference);

			loggedInTimeList.add(innerMap);

		} else {

			// More the one day
			long totalDifference = endDateTimeInMillies - startDateTimeInMillies;

			// Calculate current day hours
			Calendar currentDayCal = Calendar.getInstance();
			currentDayCal.setTimeInMillis(endDateTimeInMillies);
			currentDayCal.set(Calendar.HOUR_OF_DAY, 0);
			currentDayCal.set(Calendar.MINUTE, 0);
			currentDayCal.set(Calendar.SECOND, 0);
			currentDayCal.set(Calendar.MILLISECOND, 1);

			long currentDayDifference = endDateTimeInMillies - currentDayCal.getTimeInMillis();

			Map<String, Object> currentDayMap = new HashMap<String, Object>();

			calEndDateTime.set(Calendar.HOUR_OF_DAY, 16);
			calEndDateTime.set(Calendar.MINUTE, 0);
			calEndDateTime.set(Calendar.SECOND, 0);
			calEndDateTime.set(Calendar.MILLISECOND, 0);

			currentDayMap.put("dateTimeInMillies", calEndDateTime.getTimeInMillis());
			currentDayMap.put("loggedInTimeInMillies", currentDayDifference);

			loggedInTimeList.add(currentDayMap);

			// Calculate previous days hours
			if (totalDifference > currentDayDifference) {

				long remainingDifference = totalDifference - currentDayDifference;

				boolean conditionFlag = true;
				long oneDayInMillies = 24 * ProjectConstants.ONE_HOUR_MILLISECONDS_LONG;
				int i = 1;

				do {
					Map<String, Object> innerMap = new HashMap<String, Object>();

					Calendar calForPreviousDay = Calendar.getInstance();
					calForPreviousDay.clear();
					calForPreviousDay.setTimeInMillis(endDateTimeInMillies);

					if (remainingDifference <= oneDayInMillies) {
						conditionFlag = false;
						calForPreviousDay.set(Calendar.DAY_OF_YEAR, (calEndDateTime.get(Calendar.DAY_OF_YEAR) - i));
						innerMap.put("loggedInTimeInMillies", remainingDifference);
					} else {
						calForPreviousDay.set(Calendar.DAY_OF_YEAR, (calEndDateTime.get(Calendar.DAY_OF_YEAR) - i));
						remainingDifference = remainingDifference - oneDayInMillies;
						innerMap.put("loggedInTimeInMillies", oneDayInMillies);
					}

					calForPreviousDay.set(Calendar.HOUR_OF_DAY, 16);
					calForPreviousDay.set(Calendar.MINUTE, 0);
					calForPreviousDay.set(Calendar.SECOND, 0);
					calForPreviousDay.set(Calendar.MILLISECOND, 0);

					innerMap.put("dateTimeInMillies", calForPreviousDay.getTimeInMillis());

					loggedInTimeList.add(innerMap);

					i++;

				} while (conditionFlag);
			}
		}

		return loggedInTimeList;
	}

	public static Map<String, Object> airPortbooking(String sLatitude, String sLongitude, String dLatitude, String dLongitude, String carTypeId) {

		boolean isAirportBooking = false;

		Map<String, Object> airportRegionMap = new HashMap<String, Object>();

		airportRegionMap = TourUtils.checkBookingForAirportPickupOrDrop(sLatitude, sLongitude, dLatitude, dLongitude);

		if ((boolean) airportRegionMap.get("isAirportPickUp") || (boolean) airportRegionMap.get("isAirportDrop")) {
			isAirportBooking = true;
		}

		airportRegionMap.put("isAirportBooking", isAirportBooking);

		return airportRegionMap;
	}

}