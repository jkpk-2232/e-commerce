package com.webapp.actions.secure.vendor;

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
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.TourModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/vendor/tours")
public class VendorTripAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorTrip(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.VENDOR_ID, vendorId);

		return loadView(UrlConstants.JSP_URLS.VENDOR_TOURS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorTripList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = TourModel.getVendorsAllTourListCount(dtu.getGlobalSearchStringWithPercentage(), null, dtu.getStartDatelong(), dtu.getEndDatelong(), null, assignedRegionList, vendorId);
		List<TourModel> tourList = TourModel.getVendorsAllTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), null, dtu.getGlobalSearchStringWithPercentage(), null, dtu.getStartDatelong(), dtu.getEndDatelong(), null, assignedRegionList, vendorId);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (TourModel tourModel : tourList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(tourModel.getTourId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(tourModel.getUserTourId());
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			dtuInnerJsonArray.put(tourModel.getBookingType());

			if (tourModel.isRentalBooking()) {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelRental"));
			} else {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelTaxi"));
			}

			dtuInnerJsonArray.put(tourModel.getStatus());

			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));

			if (tourModel.getInvoiceId() != null) {

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
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + tourModel.getTourId() + "&tourType=vendor")));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? tourList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VENDOR_TOURS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}