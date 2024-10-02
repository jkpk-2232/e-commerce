package com.webapp.actions.secure.adminreports;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.DriverLoggedInTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-driver-duty-reports")
public class DriverDutyReportsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverDutyReports(
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

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		String onOffStatusOptions = DropDownUtils.getDriverOnOffSearchList("0");
		data.put(FieldConstants.ON_OFF_STATUS_OPTIONS, onOffStatusOptions);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_DUTY_REPORTS_JSP);
	}

	@Path("/driver-duty-info")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMessageList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.START_DATE) String startDate,
		@QueryParam(FieldConstants.END_DATE) String endDate
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		double allDriverTotalPayableAmount = InvoiceModel.getAllDriverTotalPayableAmount(startDatelong, endDatelong, null);

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outputMap.put(FieldConstants.ALL_DRIVER_TOTAL_PAYABLE_AMOUNT, adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(allDriverTotalPayableAmount));
		return sendDataResponse(outputMap);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverDutyReportsList(
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
		String onOffStatus = dtu.getRequestParameter(FieldConstants.ON_OFF_STATUS);

		String onOffStatusForCheck = null;
		boolean onOffStatusBoolean = true;

		if (onOffStatus.equals("1")) {
			onOffStatusForCheck = "YES";
			onOffStatusBoolean = true;
		} else if (onOffStatus.equals("2")) {
			onOffStatusForCheck = "YES";
			onOffStatusBoolean = false;
		}

		String vendorId = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		int total = UserModel.getTotalDriverUserCount(UserRoles.DRIVER_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), null, onOffStatusForCheck, onOffStatusBoolean, vendorId);
		List<UserModel> userModelList = UserModel.getDriverListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), onOffStatusForCheck,
					onOffStatusBoolean, vendorId);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);
		long idealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();
		long currentTime = DateUtils.nowAsGmtMillisec();

		for (UserModel userModel : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			dtuInnerJsonArray.put(userModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));

			if (userModel.isOnDuty()) {

				dtuInnerJsonArray.put(ProjectConstants.ON_DUTY);

				if (userModel.getCreatedAt() > idealTimeInMillis) {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				} else {
					dtuInnerJsonArray.put(ProjectConstants.IDEAL_STRING);
				}

			} else {

				dtuInnerJsonArray.put(ProjectConstants.OFF_DUTY);
				dtuInnerJsonArray.put("-");
			}

			if (userModel.getCreatedAt() <= 0) {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			} else {
				dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(userModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			}

			long totalLoggedInTime = DriverLoggedInTimeModel.getTotalLoggedInTimeByDriverIdandDate(userModel.getUserId(), dtu.getStartDatelong(), dtu.getEndDatelong());

			if (userModel.isOnDuty() && currentTime > userModel.getUpdatedAt()) {
				totalLoggedInTime = totalLoggedInTime + (currentTime - userModel.getUpdatedAt() > 0 ? (currentTime - userModel.getUpdatedAt()) : 0);
			}

			if (totalLoggedInTime > 0) {

				long secondsInMilli = 1000;
				long minutesInMilli = secondsInMilli * 60;
				long hoursInMilli = minutesInMilli * 60;

				long elapsedHours = totalLoggedInTime / hoursInMilli;
				totalLoggedInTime = totalLoggedInTime % hoursInMilli;

				long elapsedMinutes = totalLoggedInTime / minutesInMilli;
				totalLoggedInTime = totalLoggedInTime % minutesInMilli;

				long elapsedSeconds = totalLoggedInTime / secondsInMilli;

				dtuInnerJsonArray.put(ProjectConstants.dfTwoDigit.format(elapsedHours) + ":" + ProjectConstants.dfTwoDigit.format(elapsedMinutes) + ":" + ProjectConstants.dfTwoDigit.format(elapsedSeconds));

			} else {

				dtuInnerJsonArray.put(0);
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_LOGGED_IN_TIME_REPORT_URL + "?driverId=" + userModel.getUserId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_DUTY_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}