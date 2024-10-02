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
import com.webapp.models.DriverReferralCodeLogModel;
import com.webapp.models.UserModel;

@Path("/driver/refer-benefit/details-reports")
public class DriverReferBenefitDetailsReportAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverReferBenefitReports(
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

		return loadView(UrlConstants.JSP_URLS.DRIVER_REFER_BENEFITS_DETAIL_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverReferBenefitReportsList(
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

		DatatableUtils dtu = new DatatableUtils(request);
		String driverId = dtu.getRequestParameter(FieldConstants.DRIVER_ID);

		String timeZone = TimeZoneUtils.getTimeZone();

		int total = DriverReferralCodeLogModel.getTotalDriverReferralLogsCount(driverId, dtu.getStartDatelong(), dtu.getEndDatelong());

		List<DriverReferralCodeLogModel> driverReferralCodeLogModelList = DriverReferralCodeLogModel.getDriverReferralLogsListForSearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(),
					dtu.getEndDatelong());

		int count = dtu.getStartInt();

		for (DriverReferralCodeLogModel driverReferralCodeLogModel : driverReferralCodeLogModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverReferralCodeLogModel.getDriverReferralCodeLogId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(driverReferralCodeLogModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(driverReferralCodeLogModel.getPassengerName());
			dtuInnerJsonArray.put(driverReferralCodeLogModel.getPassengerPhoneNumber());
			dtuInnerJsonArray.put(driverReferralCodeLogModel.getPassengerEmail());

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? driverReferralCodeLogModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_REFER_BENEFITS_DETAIL_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}