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
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.TourModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-driver-bookings")
public class DriverBookingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverBookings(
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

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_BOOKINGS_JSP);
	}

	@Path("/driver-payable-info")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverPayableInfo(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId,
		@QueryParam(FieldConstants.START_DATE) String startDate,
		@QueryParam(FieldConstants.END_DATE) String endDate
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
		long endDatelong = DateUtils.getStartOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		double totalAdminSettlementAmount = InvoiceModel.getTotalAdminSettlementAmount(startDatelong, endDatelong, driverId, assignedRegionList);

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outputMap.put(FieldConstants.SETTLEMENT_AMOUNT, MyHubUtils.getDriverSettlementString(adminSettings, totalAdminSettlementAmount));

		return sendDataResponse(outputMap);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverBookingsList(
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

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		long userTourId = -1;

		try {
			userTourId = Long.parseLong(dtu.getGlobalSearchString());
		} catch (Exception e) {
			userTourId = -1;
		}

		int total = TourModel.getDriverTourCountByDriverId(dtu.getStartDatelong(), dtu.getEndDatelong(), driverId, assignedRegionList);
		List<TourModel> tourModelList = TourModel.getDriverTourListBySearchByDriverId(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), driverId, dtu.getGlobalSearchStringWithPercentage(), userTourId, dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);

		int count = dtu.getStartInt();

		String usedCredits;
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		for (TourModel tourModel : tourModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(tourModel.getTourId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(tourModel.getUserTourId());

			// Passengre vendor is equal to the logged in vendor, the show passenger details
			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				if (tourModel.getPassengerVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
					dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
				} else {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				}
			} else {
				dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			}

			if (tourModel.getInvoiceId() != null) {

				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getTotal()));
				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getPromoDiscount()));
				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getTotalTaxAmount()));

				usedCredits = StringUtils.valueOf(tourModel.getUsedCredits());

				if (usedCredits.contains("-")) {

					if (tourModel.getUsedCredits() != 0) {
						usedCredits = usedCredits.replace("-", "+");
					}

				} else {

					if (tourModel.getUsedCredits() != 0) {
						usedCredits = "-" + usedCredits;
					}
				}

				dtuInnerJsonArray.put(usedCredits);
				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getFinalAmountCollected()));
				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getPercentage()));
				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getDriverAmount()));
				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getMarkupFare()));

				if (tourModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.CARD)) {
					dtuInnerJsonArray.put(ProjectConstants.C_CARD);
					dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(tourModel.getAdminSettlementAmount()) + " (Pay))")));
				} else {
					dtuInnerJsonArray.put(ProjectConstants.C_CASH);
					dtuInnerJsonArray.put(MyHubUtils.getDriverSettlementString(adminSettings, tourModel.getAdminSettlementAmount()));
				}

			} else {

				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + tourModel.getTourId() + "&tourType=bookings")));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = TourModel.getDriverTourListBySearchByDriverIdCount(driverId, dtu.getGlobalSearchStringWithPercentage(), userTourId, dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_BOOKINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}