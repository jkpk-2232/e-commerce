package com.webapp.actions.secure.vendor.appointments;

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
import com.utils.myhub.AppointmentUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AppointmentModel;
import com.webapp.models.OrderItemModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/view-appointment")
public class ViewAppointmentAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getViewOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.APPOINTMENT_ID) String appointmentId,
		@QueryParam(FieldConstants.TYPE) String type
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		AppointmentModel appointmentModel = AppointmentModel.getAppointmentDetailsByAppointmentId(appointmentId);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) && !appointmentModel.getAppointmentReceivedAgainstVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		data.put(FieldConstants.APPOINTMENT_ID, appointmentId);
		data.put(ProjectConstants.STATUS_TYPE, type);

		data.put("appointmentShortId", appointmentModel.getAppointmentShortId());
		data.put("appointmentCreationTime", DateUtils.dbTimeStampToSesionDate(appointmentModel.getAppointmentCreationTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		data.put("appointmentTime", DateUtils.dbTimeStampToSesionDate(appointmentModel.getAppointmentTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		data.put("customerName", appointmentModel.getCustomerName());
		data.put("customerPhoneNo", MyHubUtils.formatPhoneNumber(appointmentModel.getCustomerPhoneNoCode(), appointmentModel.getCustomerPhoneNo()));
		data.put("promoCode", StringUtils.validString(appointmentModel.getPromoCode()) ? appointmentModel.getPromoCode() : ProjectConstants.NOT_AVAILABLE);
		data.put("appointmentStatus", AppointmentUtils.getAppointmentStatusDisplayLabels(appointmentModel.getAppointmentStatus()));
		data.put("paymentMode", OrderUtils.getPaymentModeString(appointmentModel.getPaymentMode()));
		data.put("paymentStatus", StringUtils.capatalize(appointmentModel.getPaymentStatus()));
		data.put("endOtp", appointmentModel.getEndOtp());
		data.put("appointmentNumberOfItems", StringUtils.valueOf(appointmentModel.getAppointmentNumberOfItems()));

		data.put("storeAddressLat", appointmentModel.getStoreAddressLat());
		data.put("storeAddressLng", appointmentModel.getStoreAddressLng());

		data.put("appointmentNumberOfItems", StringUtils.valueOf(appointmentModel.getAppointmentNumberOfItems()));
		data.put("appointmentTotal", MyHubUtils.getAmountWithCurrency(adminSettingsModel, appointmentModel.getAppointmentTotal()));
		data.put("appointmentPromoCodeDiscount", MyHubUtils.getAmountWithCurrency(adminSettingsModel, appointmentModel.getAppointmentPromoCodeDiscount()));
		data.put("appointmentCharges", MyHubUtils.getAmountWithCurrency(adminSettingsModel, appointmentModel.getAppointmentCharges()));

		String appointmentStatusFilterOptions = DropDownUtils.getAppointmentStatusForOrdersToBeChangedByAdmin(appointmentModel, appointmentModel.getAppointmentStatus(), true);
		if (appointmentStatusFilterOptions == null) {
			data.put("showChangeStatus", false + "");
		} else {
			data.put("showChangeStatus", true + "");
			data.put("appointmentStatusFilterOptions", appointmentStatusFilterOptions);
		}

		data.put("vendorName", appointmentModel.getVendorName());

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.VIEW_APPOINMENTS_DETAILS_URL + "?appointmentId=" + appointmentId + "&type=" + type);
		return loadView(UrlConstants.JSP_URLS.VIEW_APPOINTMENT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getViewAppointmentsItemsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String appointmentId = dtu.getRequestParameter(FieldConstants.APPOINTMENT_ID);

		double numberSearch = -1;
		try {
			numberSearch = Double.parseDouble(dtu.getGlobalSearchString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		int total = OrderItemModel.getOrderItemCount(appointmentId);
		List<OrderItemModel> orderItemList = OrderItemModel.getOrderItemSearch(dtu.getGlobalSearchStringWithPercentage(), appointmentId, dtu.getStartInt(), dtu.getLengthInt(), numberSearch);

		for (OrderItemModel orderItemModel : orderItemList) {
			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(orderItemModel.getOrderItemId());
			dtuInnerJsonArray.put(NewThemeUiUtils.getProductDetailsDiv(request, orderItemModel, adminSettingsModel));
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = OrderItemModel.getOrderItemSearchCount(dtu.getGlobalSearchStringWithPercentage(), appointmentId, numberSearch);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/vendor/appointment-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response postSurgeSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.APPOINTMENT_ID) String appointmentId,
		@QueryParam(FieldConstants.APPOINTMENT_STATUS_FILTER) String appointmentStatusFilter,
		@QueryParam(FieldConstants.TYPE) String type
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap.get(LoginUtils.USER_ID));
		}

		AppointmentModel currentDbAppointmentModel = AppointmentModel.getAppointmentDetailsByAppointmentId(appointmentId);

		Map<String, Object> output = new HashMap<>();

		// status can be changed by either
		// 1. super admin
		// 2. admin
		// 3. vendor against whom the order is placed.

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || loginSessionMap.get(LoginUtils.USER_ID).equalsIgnoreCase(currentDbAppointmentModel.getAppointmentReceivedAgainstVendorId())
					|| (vendorId != null && vendorId.equalsIgnoreCase(currentDbAppointmentModel.getAppointmentReceivedAgainstVendorId()))) {

			AppointmentModel inputAppointmentModel = AppointmentUtils.setInputAppointmentModel(appointmentId, appointmentStatusFilter);

			output = AppointmentUtils.updateAppointmentStatusByVendorOrAdminViaAdminPanelOrApi(inputAppointmentModel, loginSessionMap.get(LoginUtils.USER_ID), true);
			output.put("url", WebappPropertyUtils.BASE_PATH + UrlConstants.PAGE_URLS.VIEW_APPOINMENTS_DETAILS_URL + "?appointmentId=" + appointmentId + "&type=" + type);

			return sendDataResponse(output);
		} else {

			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, BusinessAction.messageForKeyAdmin("errorAppointmentStatusChangePermissions"));
			return sendDataResponse(output);
		}
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VIEW_APPOINTMENT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}