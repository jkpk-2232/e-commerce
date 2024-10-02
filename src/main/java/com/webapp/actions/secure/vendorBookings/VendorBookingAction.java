package com.webapp.actions.secure.vendorBookings;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.TourModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/vendor-bookings")
public class VendorBookingAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadVendorBookingList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.VENDOR_BOOKINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String statusFilterOptions = DropDownUtils.getToutStatusFilterList(ProjectConstants.ALL_USERS_ID);
		data.put(FieldConstants.STATUS_FILTER_OPTIONS, statusFilterOptions);

		String surgeFilterOptions = DropDownUtils.getOptionsForSurgeFilter(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, true);
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_BOOKING_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.VENDOR_BOOKINGS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorBookingList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.VENDOR_BOOKINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String statusFilter = dtu.getRequestParameter(FieldConstants.STATUS_FILTER);
		String surgeFilter = dtu.getRequestParameter(FieldConstants.SURGE_FILTER);

		String[] statusArray = TourUtils.getStatusArray(statusFilter);
		double[] surgeArray = TourUtils.getSurgeArray(surgeFilter);
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = TourModel.getVendorsAllTourListCount(dtu.getGlobalSearchStringWithPercentage(), statusArray, dtu.getStartDatelong(), dtu.getEndDatelong(), surgeArray, assignedRegionList, loginSessionMap.get(LoginUtils.USER_ID));
		List<TourModel> tourList = TourModel.getVendorsAllTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), statusArray, dtu.getStartDatelong(), dtu.getEndDatelong(), surgeArray, assignedRegionList,
					loginSessionMap.get(LoginUtils.USER_ID));

		String timeZone = TimeZoneUtils.getTimeZone();

		for (TourModel tourModel : tourList) {

			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(tourModel.getTourId());
			dtuInnerJsonArray.put(tourModel.getUserTourId());

			if (StringUtils.validString(tourModel.getFirstName())) {
				dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getFirstName(), tourModel.getLastName()));
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			// Passengre vendor is equal to the logged in vendor, the show passenger details
			if (tourModel.getPassengerVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
				dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.BUSINESS_OWNER_BOOKING)) {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelCorporateOwnersBooking"));
			} else {
				dtuInnerJsonArray.put(tourModel.getBookingType());
			}

			if (tourModel.isRentalBooking()) {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelRental"));
			} else {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelTaxi"));
			}

			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if (tourModel.getInvoiceId() != null) {

				dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getCharges()));

				if (tourModel.getSurgePrice() <= 1) {
					dtuInnerJsonArray.put(StringUtils.valueOf(1) + "x");
				} else {
					dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getSurgePrice()) + "x");
				}

				if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
					dtuInnerJsonArray.put(ProjectConstants.CREDITS_STRING);
				} else {
					if (tourModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {
						dtuInnerJsonArray.put(StringUtils.valueOf(tourModel.getFinalAmountCollected()));
						dtuInnerJsonArray.put(ProjectConstants.C_CASH);
					} else {
						dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
						dtuInnerJsonArray.put(ProjectConstants.C_CARD);
					}
				}
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(TourUtils.getTourStatus(tourModel.getStatus()));

			if (StringUtils.validString(tourModel.getCarType())) {
				dtuInnerJsonArray.put(tourModel.getCarType());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + tourModel.getTourId() + "&tourType=bookings")));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? tourList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VENDOR_BOOKINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}