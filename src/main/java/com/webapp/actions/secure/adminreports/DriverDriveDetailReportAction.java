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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserModel;

@Path("/driver-km-detail-reports")
public class DriverDriveDetailReportAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off 
	public Response getDriverKmDetailReports(
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

		UserModel driverModel = UserModel.getUserActiveDeativeDetailsById(driverId);

		if (driverModel != null) {
			data.put(FieldConstants.DRIVER_NAME, MyHubUtils.formatFullName(driverModel.getFirstName(), driverModel.getLastName()));
		} else {
			data.put(FieldConstants.DRIVER_NAME, ProjectConstants.NOT_AVAILABLE);
		}

		return loadView(UrlConstants.JSP_URLS.DRIVER_DRIVE_DETAIL_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverKmDetailReportsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		DatatableUtils dtu = new DatatableUtils(request);
		String driverId = dtu.getRequestParameter(FieldConstants.DRIVER_ID);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String[] statusArray = { ProjectConstants.TourStatusConstants.ENDED_TOUR };

		int total = TourModel.getDriverAllTourListCount(driverId, dtu.getGlobalSearchStringWithPercentage(), statusArray, dtu.getStartDatelong(), dtu.getEndDatelong());
		List<TourModel> driverDutyLogsModelList = TourModel.getDriverAllTourListBySearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), statusArray, dtu.getStartDatelong(), dtu.getEndDatelong(),
					assignedRegionList);
		List<TourModel> tourModelList = TourModel.getDriverAllTourListBySearch(driverId, 0, 0, dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), statusArray, dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);

		int count = dtu.getStartInt();
		double totalDistance = 0d;

		for (TourModel tourModel : tourModelList) {
			totalDistance += tourModel.getDistance();
		}

		for (TourModel driverTours : driverDutyLogsModelList) {

			count++;
			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverTours.getTourId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(driverTours.getUserTourId());
			dtuInnerJsonArray.put(driverTours.getSourceAddress());
			dtuInnerJsonArray.put(driverTours.getDestinationAddress());
			dtuInnerJsonArray.put(String.valueOf(driverTours.getDistance() / adminSettings.getDistanceUnits()));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? driverDutyLogsModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		dtuJsonObject.put("totalDistance", MyHubUtils.formatFullName(StringUtils.valueOf(totalDistance / adminSettings.getDistanceUnits()), adminSettings.getDistanceType()));

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_DRIVE_DETAIL_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}