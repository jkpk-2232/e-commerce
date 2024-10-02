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
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.TourReferrerBenefitModel;

@Path("/driver/refer-benefit/trips-reports")
public class DriverReferBenefitTripsReportAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response diverReferBenefitTripReports(
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

		return loadView(UrlConstants.JSP_URLS.DRIVER_REFER_BENEFITS_TRIP_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response diverReferBenefitTripReportsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String driverId = dtu.getRequestParameter(FieldConstants.DRIVER_ID);

		String timeZone = TimeZoneUtils.getTimeZone();

		double totalDriverBenefits = TourReferrerBenefitModel.getTotalDriverBenefitByDriverId(driverId);

		int total = TourReferrerBenefitModel.getTotalTourReferrerBenefitCountForSearch(driverId, dtu.getStartDatelong(), dtu.getEndDatelong());
		List<TourReferrerBenefitModel> tourReferrerBenefitModelList = TourReferrerBenefitModel.getTourReferrerBenefitListForSearch(driverId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(),
					dtu.getEndDatelong());

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		int count = dtu.getStartInt();

		for (TourReferrerBenefitModel tourReferrerBenefitModel : tourReferrerBenefitModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getTourReferrerBenefitId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getReferrerDriverBenefit());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getUserTourId());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getDriverName());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getDriverPhoneNumber());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getDriverEmail());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getPassengerName());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getPassengerPhoneNumber());
			dtuInnerJsonArray.put(tourReferrerBenefitModel.getPassengerEmail());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(tourReferrerBenefitModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? tourReferrerBenefitModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		dtuJsonObject.put(FieldConstants.BENEFIT_AMOUNT, adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(totalDriverBenefits));

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_REFER_BENEFITS_TRIP_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}