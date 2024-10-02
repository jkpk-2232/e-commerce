package com.webapp.actions.secure.adminreports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.DriverLoggedInTimeModel;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/driver/loggedin/time-report")
public class DriverLoggedInTimeReportAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverLoggedInTimeReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.DRIVER_ID, driverId);

		UserModel driverModel = UserModel.getUserAccountDetailsById(driverId);

		if (driverModel != null) {
			data.put(FieldConstants.DRIVER_NAME, MyHubUtils.formatFullName(driverModel.getFirstName(), driverModel.getLastName()));
			data.put(FieldConstants.EMAIL, driverModel.getEmail());
			data.put(FieldConstants.PHONE_NO, MyHubUtils.formatPhoneNumber(driverModel.getPhoneNoCode(), driverModel.getPhoneNo()));
		} else {
			data.put(FieldConstants.DRIVER_NAME, ProjectConstants.NOT_AVAILABLE);
			data.put(FieldConstants.EMAIL, ProjectConstants.NOT_AVAILABLE);
			data.put(FieldConstants.PHONE_NO, ProjectConstants.NOT_AVAILABLE);
		}

		data.put("labelDriverLoggedInTimeReport", messageForKeyAdmin("labelDriverLoggedInTimeReport"));

		data.put("labelDriverName", messageForKeyAdmin("labelDriverName") + ":");
		data.put("labelPhone", messageForKeyAdmin("labelPhone") + ":");
		data.put("labelEmail", messageForKeyAdmin("labelEmail") + ":");
		data.put("labelTotalLoggedInHours", messageForKeyAdmin("labelTotalLoggedInHours") + ":");

		data.put("labelSearch", messageForKeyAdmin("labelSearch"));
		data.put("labelFromDate", messageForKeyAdmin("labelFromDate"));
		data.put("labelToDate", messageForKeyAdmin("labelToDate"));
		data.put("labelExport", messageForKeyAdmin("labelExport"));

		data.put("labelId", messageForKeyAdmin("labelId"));
		data.put("labelSrNo", messageForKeyAdmin("labelSrNo"));
		data.put("labelLoggedInHours", messageForKeyAdmin("labelLoggedInHours"));
		data.put("labelDate", messageForKeyAdmin("labelDate"));
		data.put("labelAction", messageForKeyAdmin("labelAction"));

		return loadView(UrlConstants.JSP_URLS.DRIVER_LOGGED_IN_TIME_REPORT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverLoggedInTimeReportList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String driverId = dtu.getRequestParameter(FieldConstants.DRIVER_ID);

		String currentDateStr = DateUtils.getDateInFormatFromMilliSecond(DateUtils.nowAsGmtMillisec(), DateUtils.DATATABLE_DATE_FORMAT_PARSE);

		int total = DriverLoggedInTimeModel.getTotalDriverLoggedInTimeLogCount(driverId, dtu.getStartDatelong(), dtu.getEndDatelong());
		List<DriverLoggedInTimeModel> driverLoggedInTimeModelList = DriverLoggedInTimeModel.getDriverLoggedInTimeLogListForSearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getStartDatelong(), dtu.getEndDatelong());

		UserModel userDetails = UserModel.getDriverDetailsForLoggedInTimeReportById(driverId);

		long extraLoggedInTime = 0;

		if (userDetails != null) {

			long currentTime = DateUtils.nowAsGmtMillisec();

			if (userDetails.isOnDuty()) {

				if (currentTime > userDetails.getUpdatedAt()) {
					extraLoggedInTime = currentTime - userDetails.getUpdatedAt() > 0 ? (currentTime - userDetails.getUpdatedAt()) : 0;
				}
			}
		}

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (DriverLoggedInTimeModel driverLoggedInTimeModel : driverLoggedInTimeModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverLoggedInTimeModel.getDriverLoggedInTimeId());
			dtuInnerJsonArray.put(count);

			long loggedInTime = driverLoggedInTimeModel.getLoggedInTime();

			if ((count == 1) && (currentDateStr.equals(dtu.getEndDate()))) {
				loggedInTime = loggedInTime + extraLoggedInTime;
			}

			if (loggedInTime > 0) {

				long secondsInMilli = 1000;
				long minutesInMilli = secondsInMilli * 60;
				long hoursInMilli = minutesInMilli * 60;

				long elapsedHours = loggedInTime / hoursInMilli;
				loggedInTime = loggedInTime % hoursInMilli;

				long elapsedMinutes = loggedInTime / minutesInMilli;
				loggedInTime = loggedInTime % minutesInMilli;

				long elapsedSeconds = loggedInTime / secondsInMilli;

				dtuInnerJsonArray.put(ProjectConstants.dfTwoDigit.format(elapsedHours) + ":" + ProjectConstants.dfTwoDigit.format(elapsedMinutes) + ":" + ProjectConstants.dfTwoDigit.format(elapsedSeconds));

			} else {

				dtuInnerJsonArray.put(0);
			}

			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(driverLoggedInTimeModel.getDateTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.DRIVER_DUTY_REPORTS_URL + "?driverId=" + driverId + "&dt=" + driverLoggedInTimeModel.getDateTime())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = driverLoggedInTimeModelList.size();
		} else {
			filterCount = total;
		}

		long totalLoggedInTime = DriverLoggedInTimeModel.getTotalLoggedInTimeByDriverIdandDate(driverId, dtu.getStartDatelong(), dtu.getEndDatelong());

		String totalLoggedInTimeStr = "";

		if (totalLoggedInTime > 0) {

			long secondsInMilli = 1000;
			long minutesInMilli = secondsInMilli * 60;
			long hoursInMilli = minutesInMilli * 60;

			long elapsedHours = totalLoggedInTime / hoursInMilli;
			totalLoggedInTime = totalLoggedInTime % hoursInMilli;

			long elapsedMinutes = totalLoggedInTime / minutesInMilli;
			totalLoggedInTime = totalLoggedInTime % minutesInMilli;

			long elapsedSeconds = totalLoggedInTime / secondsInMilli;

			totalLoggedInTimeStr = ProjectConstants.dfTwoDigit.format(elapsedHours) + ":" + ProjectConstants.dfTwoDigit.format(elapsedMinutes) + ":" + ProjectConstants.dfTwoDigit.format(elapsedSeconds);
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);
		dtuJsonObject.put("totalLoggedInTimeStr", totalLoggedInTimeStr);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_LOGGED_IN_TIME_REPORT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}