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
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.VendorMonthlySubscriptionHistoryModel;

@Path("/manage-vendor-monthly-subscription-history")
public class ManageVendorMonthlySubscriptionHistoryAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorMonthlySubscriptionHistory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!(loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions(StringUtils.validString(vendorId) ? vendorId : "0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorMonthlySubscriptionHistoryList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!(loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		int total = VendorMonthlySubscriptionHistoryModel.getVendorMonthlySubscriptionHistoryCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId);
		List<VendorMonthlySubscriptionHistoryModel> vendorMonthlySubscriptionHistoryList = VendorMonthlySubscriptionHistoryModel.getVendorMonthlySubscriptionHistorySearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(),
					dtu.getStartInt(), dtu.getLengthInt(), vendorId);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (VendorMonthlySubscriptionHistoryModel vendorMonthlySubscriptionHistoryModel : vendorMonthlySubscriptionHistoryList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorMonthlySubscriptionHistoryModel.getVendorMonthlySubscriptionHistoryId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorMonthlySubscriptionHistoryModel.getVendorName());
			dtuInnerJsonArray.put(vendorMonthlySubscriptionHistoryModel.getVendorMonthlySubscriptionHistorySerialId());
			dtuInnerJsonArray.put(vendorMonthlySubscriptionHistoryModel.getPaymentType());
			dtuInnerJsonArray.put(StringUtils.validString(vendorMonthlySubscriptionHistoryModel.getTransactionId()) ? vendorMonthlySubscriptionHistoryModel.getTransactionId() : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(StringUtils.valueOf(vendorMonthlySubscriptionHistoryModel.getVendorMonthlySubscriptionFee()));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(vendorMonthlySubscriptionHistoryModel.getStartDateTime(), timeZone, DateUtils.Ride_Later_DateTime_Format_FOR_PICKER));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(vendorMonthlySubscriptionHistoryModel.getEndDateTime(), timeZone, DateUtils.Ride_Later_DateTime_Format_FOR_PICKER));
			dtuInnerJsonArray.put(vendorMonthlySubscriptionHistoryModel.isFreeSubscriptionEntry() ? ProjectConstants.VENDOR_MONTHLY_SUBSCRIPTION_SUBSCRIPTION_TYPE.FREE : ProjectConstants.VENDOR_MONTHLY_SUBSCRIPTION_SUBSCRIPTION_TYPE.PAID);
			dtuInnerJsonArray.put(vendorMonthlySubscriptionHistoryModel.isVendorSubscriptionCurrentActive() ? ProjectConstants.VENDOR_MONTHLY_SUBSCRIPTION_STATUS_ACTIVE : ProjectConstants.VENDOR_MONTHLY_SUBSCRIPTION_STATUS_EXPIRED);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? vendorMonthlySubscriptionHistoryList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}