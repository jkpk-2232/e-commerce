package com.jeeutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;

public class DateUtils {

	protected static Logger logger = Logger.getLogger(DateUtils.class);

	// --------------------------------------------------

	public static final String TIMEZONE_GMT = "GMT";

	// The following 2 are used for datatable time display and parsing at java level
	public static final String DATATABLE_DATE_FORMAT_PARSE = "yyyy-MM-dd";
	public static final String DATATABLE_DATE_FORMAT_DISPALY = "YYYY-MM-DD";

	public static final String DISPLAY_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static final String DATE_FORMAT_FOR_VIEW = "dd/MM/yyyy";

	public static final String DATE_FORMAT_FOR_VIEW_FOR_12_HOURS = "dd/MM/yyyy hh:mm a";

	// --------------------------------------------------

	public static final String Date_Picker_Format = "MM-dd-yyyy";

	public static final String Date_Picker_Format_Datatable = "MM/dd/yyyy";

	public static final String TIME_FORMAT_STR = "hh:mm a";

	public static final String DATE_TIME_PICKER_FORMAT_FOR_12_HOURS = "MM-dd-yyyy hh:mm a";

	public static final String Ride_Later_DateTime_Format = "MM-dd-yyyy HH:mm:ss";

	public static final String Ride_Later_DateTime_Format_FOR_PICKER = "dd/MM/yyyy HH:mm:ss";

	public static final String TIME_FORMAT = "HH:mm";
	
	public static final String DATE_TIME_WITH_ZERO_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

	public static long getDateFromString(String dateStr, String dateFormatString) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parsedDate.getTime();
	}

	public static String getDatePickerFormatedWithoutZone1(Date dateValue) {

		if (dateValue == null) {
			return null;
		}

		String dateValuStr = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Picker_Format_Datatable);

		try {
			dateValuStr = dateFormat.format(dateValue);
		} catch (NullPointerException e) {
			logger.debug("Date Parse Faied : ", e);
		}

		return dateValuStr;

	}

	public static long nowAsGmtMillisec() {
		return new Date().getTime();
	}

	public static String dbTimeStampToSesionDate(long time, String timeZone, String sessionDateFormatStr) {

		Date gmtDate = new Date(time);

		SimpleDateFormat currentDateFormat = new SimpleDateFormat(sessionDateFormatStr);

		currentDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

		String dateStr = currentDateFormat.format(gmtDate);

		return dateStr;
	}

	public static String getDateFromMilliSecond(long milliSec, String format) {

		Date currentDate = new Date(milliSec);

		DateFormat df = new SimpleDateFormat(Date_Picker_Format);

		String date = df.format(currentDate);

		return date;
	}

	public static long getStartOfDayDatatable(String dateStr, String timeZone) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Picker_Format_Datatable);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsedDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTimeInMillis();
	}

	public static long getEndOfDayDatatable(String dateStr, String timeZone) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Picker_Format_Datatable);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsedDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTimeInMillis();
	}

	public static long getDateFromStringForRideLater(String dateStr, String timeZone) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Ride_Later_DateTime_Format_FOR_PICKER);

		dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parsedDate.getTime();
	}

	public static String getRideLaterDateFromMilliSecond(long milliSec, String timeZone) {

		Date currentDate = new Date(milliSec);

		DateFormat df = new SimpleDateFormat(Ride_Later_DateTime_Format_FOR_PICKER);

		df.setTimeZone(TimeZone.getTimeZone(timeZone));

		String date = df.format(currentDate);

		return date;
	}

	public static long getStartDayOfYearInMillis(int year) {

		Calendar calender = Calendar.getInstance();

		calender.set(year, 0, 1, 00, 00, 00);

		calender.set(Calendar.MILLISECOND, 0);

		return calender.getTimeInMillis();
	}

	public static long getLastDayOfYearInMillis(int year) {

		Calendar calender = Calendar.getInstance();

		calender.set(year, 11, 31, 23, 59, 59);

		calender.set(Calendar.MILLISECOND, 999);

		return calender.getTimeInMillis();
	}

	public static long getTimeInMillisForSurge(int hour, int minute, boolean isNextDay, String timeZone) {

		String[] dateArr;

		if (isNextDay) {

			dateArr = ProjectConstants.SURGE_NEXT_DAY_DATE.split("/");
		} else {

			dateArr = ProjectConstants.SURGE_FIRST_DAY_DATE.split("/");
		}

		Calendar calender = Calendar.getInstance();

		calender.clear();

		calender.setTimeZone(TimeZone.getTimeZone(timeZone));

		calender.set(Calendar.YEAR, Integer.parseInt(dateArr[2]));
		calender.set(Calendar.MONTH, Integer.parseInt(dateArr[1]));
		calender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[0]));
		calender.set(Calendar.HOUR_OF_DAY, hour);
		calender.set(Calendar.MINUTE, minute);
		calender.set(Calendar.MILLISECOND, 0);

		return calender.getTimeInMillis();
	}

	public static String getHoursAndMinutesStringFromMillis(long timeInMilli, String timeZone) {

		Calendar calender = Calendar.getInstance();

		calender.clear();

		calender.setTimeZone(TimeZone.getTimeZone(timeZone));

		calender.setTimeInMillis(timeInMilli);

		return calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);

	}

	public static long getStartOfDayDatatableUpdated(String dateStr, String formatStr, String timeZone) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsedDate);
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTimeInMillis();
	}

	public static long getEndOfDayDatatableUpdated(String dateStr, String formatStr, String timeZone) {

		Date parsedDate = null;

		if (dateStr == null) {

			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsedDate);
		calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTimeInMillis();
	}

	public static String getDateInFormatFromMilliSecond(long milliSec, String format) {

		Date currentDate = new Date(milliSec);

		DateFormat df = new SimpleDateFormat(format);

		String date = df.format(currentDate);

		return date;
	}

	public static long getDateFromString1(String dateStr) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Picker_Format_Datatable);
		// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parsedDate.getTime();
	}

	public static long getStartOfDayLong(Instant todayInstantObject) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(todayInstantObject.toEpochMilli());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getEndOfDayLong(Instant todayInstantObject) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(todayInstantObject.toEpochMilli());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	public static long getStartOfDayLong(String dateStr) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Picker_Format_Datatable);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsedDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	public static long getEndOfDayLong(String dateStr) {

		Date parsedDate = null;

		if (dateStr == null) {
			return -1;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Date_Picker_Format_Datatable);

		try {
			parsedDate = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsedDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	public static long getHoursIntoMillis(String hoursStrings, String timeZone) {

		long timeInMillis = 0;

		try {

			String[] timeArray = hoursStrings.split(":");

			String dummyHour = timeArray[0];
			String dummyMinute = timeArray[1];

			long hourMillis = Integer.parseInt(dummyHour) * 60 * 60 * 1000;
			long minuteMillis = Integer.parseInt(dummyMinute) * 60 * 1000;

			timeInMillis = hourMillis + minuteMillis;

			long offsetInMillis = getTimeZoneOffset();
			timeInMillis = timeInMillis - offsetInMillis;

		} catch (Exception e) {

		}

		return timeInMillis;
	}

	public static long getTimeZoneOffset() {
		TimeZone tz = TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE);
		long offsetInMillis = tz.getOffset(new Date().getTime());
		return offsetInMillis;
	}

	public static String getDateFromMilliSecond2(long milliSec, String format) {

		Date currentDate = new Date(milliSec);
		DateFormat df = new SimpleDateFormat(format);
		String date = df.format(currentDate);

		return date;
	}

	public static Instant getNowInstant() {
		return Instant.now();
	}

	public static Instant getXDaysInstantInFuture(int numberOfDays) {
		return Instant.now().plus(numberOfDays, ChronoUnit.DAYS);
	}

//	public static DayOfWeek getDayOfWeek(Instant todayInstantObject) {
//		return todayInstantObject.atZone(ZoneId.of(Action.CLIENT_TIMEZONE)).getDayOfWeek();
//	}

	public static String getDayOfWeek(Instant todayInstantObject) {

		// Monday, Tuesday, Wednesday, Thursday, Friday, Saturday and Sunday
		// 1,2,3,4,5,6,7

		DayOfWeek currentDayOfWeekValue = todayInstantObject.atZone(ZoneId.of(WebappPropertyUtils.CLIENT_TIMEZONE)).getDayOfWeek();

		switch (String.valueOf(currentDayOfWeekValue.getValue())) {

		case "1":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.MONDAY_NO);
		case "2":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.TUESDAY_NO);
		case "3":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.WEDNESDAY_NO);
		case "4":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.THURSDAY_NO);
		case "5":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.FRIDAY_NO);
		case "6":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.SATURDAY_NO);
		case "7":
			return String.valueOf(ProjectConstants.DAY_OF_WEEK.SUNDAY_NO);
		}

		return null;
	}

	public static long getVendorStoreTimingInGmt(long startOfDayLong, long hourMinsMillis) {

		logger.info("startOfDayLong         \t" + startOfDayLong);
		logger.info("hourMinsMillis         \t" + hourMinsMillis);
		long temp = startOfDayLong + hourMinsMillis;
		logger.info("temp without offset    \t" + Instant.ofEpochMilli(temp).toString());
		temp = temp + DateUtils.getTimeZoneOffset();
		logger.info("temp with offset       \t" + Instant.ofEpochMilli(temp).toString());

		return temp;
	}

	public static long atStartOfDay(Instant instantInput) {
		LocalDateTime localDateTime = dateToLocalDateTime(instantInput);
		LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return localDateTimeToDate(startOfDay).toEpochMilli();
	}

	public static long atEndOfDay(Instant instantInput) {
		LocalDateTime localDateTime = dateToLocalDateTime(instantInput);
		LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return localDateTimeToDate(endOfDay).toEpochMilli();
	}

	private static LocalDateTime dateToLocalDateTime(Instant instantInput) {
		return LocalDateTime.ofInstant(instantInput, ZoneId.of(WebappPropertyUtils.CLIENT_TIMEZONE));
	}

	private static Instant localDateTimeToDate(LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.of(WebappPropertyUtils.CLIENT_TIMEZONE)).toInstant();
	}

	public static long getStartOfWeek() {

		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(DateUtils.TIMEZONE_GMT));
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		return cal.getTimeInMillis();
	}

	public static long getEndOfWeek() {

		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone(DateUtils.TIMEZONE_GMT));
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		// start of the next week
		cal.add(Calendar.WEEK_OF_YEAR, 1);

		return cal.getTimeInMillis() - 1;
	}

	public static long getStartOfMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.setTimeZone(TimeZone.getTimeZone(DateUtils.TIMEZONE_GMT));
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of the month
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTimeInMillis();
	}

	public static long getEndOfMonth() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.setTimeZone(TimeZone.getTimeZone(DateUtils.TIMEZONE_GMT));
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of the month
		cal.set(Calendar.DAY_OF_MONTH, 1);

		// get start of the next month
		cal.add(Calendar.MONTH, 1);

		return cal.getTimeInMillis() - 1;
	}

	public static int getCurrentYear() {
		return Calendar.getInstance(TimeZone.getDefault()).get(Calendar.YEAR);
	}

	public static long getPreviousMonth() {
		// Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.add(Calendar.MONTH, -1);
		// cal.roll(Calendar.MONTH, false);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static long getPreviousThreeMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.add(Calendar.MONTH, -3);
		// cal.roll(cal.get(Calendar.MONTH) - 1 , false);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static long getLastSevenDays() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		// cal.roll(cal.get(Calendar.MONTH) - 3 , false);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	public static String getDateTimeStringwithZeroTimeFormat(String dateTimeString, String inputFormat, String outputFormat) {

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);

		OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString, inputFormatter);

		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

		String outputDateString = offsetDateTime.format(outputFormatter);
		
		return outputDateString;
	}
}