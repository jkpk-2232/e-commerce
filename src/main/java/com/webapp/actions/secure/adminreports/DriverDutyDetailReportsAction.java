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
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.DriverDutyLogsModel;
import com.webapp.models.UserModel;

@Path("/driver-duty-reports")
public class DriverDutyDetailReportsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverDutyDetailsReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId,
		@QueryParam(FieldConstants.DT) String logsDate
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (!StringUtils.validString(logsDate) || !logsDate.matches("[0-9]+")) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DRIVER_LOGGED_IN_TIME_REPORT_URL + "?driverId=" + driverId);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		String onOffStatusOptions = DropDownUtils.getDriverOnOffSearchList("0");
		data.put(FieldConstants.ON_OFF_STATUS_OPTIONS, onOffStatusOptions);

		data.put(FieldConstants.DRIVER_ID, driverId);
		data.put(FieldConstants.LOGS_DATE, logsDate);

		UserModel driverModel = UserModel.getUserActiveDeativeDetailsById(driverId);

		if (driverModel != null) {

			String defaultDateStr = DateUtils.dbTimeStampToSesionDate(Long.parseLong(logsDate), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW);

			data.put(FieldConstants.DRIVER_NAME, MyHubUtils.formatFullName(driverModel.getFirstName(), driverModel.getLastName()));
			data.put(FieldConstants.EMAIL, driverModel.getEmail());
			data.put(FieldConstants.PHONE_NO, MyHubUtils.formatPhoneNumber(driverModel.getPhoneNoCode(), driverModel.getPhoneNo()));
			data.put(FieldConstants.LOGS_DATE_FOR_HEADER, defaultDateStr);

		} else {

			data.put(FieldConstants.DRIVER_NAME, ProjectConstants.NOT_AVAILABLE);
			data.put(FieldConstants.EMAIL, ProjectConstants.NOT_AVAILABLE);
			data.put(FieldConstants.PHONE_NO, ProjectConstants.NOT_AVAILABLE);
			data.put(FieldConstants.LOGS_DATE_FOR_HEADER, ProjectConstants.NOT_AVAILABLE);
		}

		return loadView(UrlConstants.JSP_URLS.DRIVER_DUTY_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverDutyDetailsReportList(
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
		String onOffStatus = dtu.getRequestParameter(FieldConstants.ON_OFF_STATUS);

		int total = DriverDutyLogsModel.getTotalDutyReportCount(driverId, dtu.getStartDatelong(), dtu.getEndDatelong());

		List<DriverDutyLogsModel> driverDutyLogsModelList = null;

		if (onOffStatus.equals("1")) {
			driverDutyLogsModelList = DriverDutyLogsModel.getDriverOnOffDutyReportAdminListForSearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getStartDatelong(), dtu.getEndDatelong(), true);
		} else if (onOffStatus.equals("2")) {
			driverDutyLogsModelList = DriverDutyLogsModel.getDriverOnOffDutyReportAdminListForSearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getStartDatelong(), dtu.getEndDatelong(), false);
		} else {
			driverDutyLogsModelList = DriverDutyLogsModel.getDriverDutyReportAdminListForSearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getStartDatelong(), dtu.getEndDatelong());
		}

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (DriverDutyLogsModel driverDutyLogsModel : driverDutyLogsModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverDutyLogsModel.getDriverDutyStatusLogsId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(driverDutyLogsModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if (driverDutyLogsModel.isDutyStatus()) {
				dtuInnerJsonArray.put(ProjectConstants.ON_DUTY);
			} else {
				dtuInnerJsonArray.put(ProjectConstants.OFF_DUTY);
			}

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (onOffStatus.equals("1")) {
			filterCount = DriverDutyLogsModel.getDriverOnOffDutyReportAdminListForSearchCount(driverId, dtu.getStartDatelong(), dtu.getEndDatelong(), true);
		} else if (onOffStatus.equals("2")) {
			filterCount = DriverDutyLogsModel.getDriverOnOffDutyReportAdminListForSearchCount(driverId, dtu.getStartDatelong(), dtu.getEndDatelong(), false);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_DUTY_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}