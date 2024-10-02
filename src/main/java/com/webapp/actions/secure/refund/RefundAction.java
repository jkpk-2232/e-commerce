package com.webapp.actions.secure.refund;

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
import com.utils.myhub.TimeZoneUtils;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.InvoiceModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/refund")
public class RefundAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getRefundAction(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REFUND_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		return loadView(UrlConstants.JSP_URLS.REFUND_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRefundActionList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REFUND_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = InvoiceModel.getTotalInvoicesByUserId(null, dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);
		List<InvoiceModel> invoiceList = InvoiceModel.getInvoiceListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), null, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (InvoiceModel invoiceModel : invoiceList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(invoiceModel.getTourId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(invoiceModel.getUserTourId());
			dtuInnerJsonArray.put(invoiceModel.getDriverFullName() != null ? invoiceModel.getDriverFullName() : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(invoiceModel.getPassengerFullName() != null ? invoiceModel.getPassengerFullName() : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(invoiceModel.getBookingType());

			if (invoiceModel.isRentalBooking()) {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelRental"));
			} else {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelTaxi"));
			}

			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(invoiceModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));

			dtuInnerJsonArray.put(StringUtils.valueOf(invoiceModel.getCharges()));

			if (invoiceModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {
				dtuInnerJsonArray.put(ProjectConstants.C_CASH);
			} else {
				dtuInnerJsonArray.put(ProjectConstants.C_CARD);
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + invoiceModel.getTourId() + "&tourType=refund")));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? invoiceList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.REFUND_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}