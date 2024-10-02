package com.webapp.actions.secure.vendor.orders;

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
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.OrderItemModel;
import com.webapp.models.OrderModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/view-order")
public class ViewOrderAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getViewOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.ORDER_ID) String orderId,
		@QueryParam(FieldConstants.TYPE) String type
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(orderId);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) && !orderModel.getOrderReceivedAgainstVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		data.put(FieldConstants.ORDER_ID, orderModel.getOrderId());
		data.put(ProjectConstants.STATUS_TYPE, type);

		data.put("orderShortId", orderModel.getOrderShortId());
		data.put("orderCreationTime", DateUtils.dbTimeStampToSesionDate(orderModel.getOrderCreationTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		data.put("vendorName", orderModel.getVendorName());
		data.put("vendorAddress", orderModel.getVendorAddress()+ ", Phone No: " +orderModel.getVendorPhoneNo() );
		data.put("customerName", orderModel.getCustomerName());
		data.put("customerPhoneNo", MyHubUtils.formatPhoneNumber(orderModel.getCustomerPhoneNoCode(), orderModel.getCustomerPhoneNo()));
		data.put("promoCode", StringUtils.validString(orderModel.getPromoCode()) ? orderModel.getPromoCode() : ProjectConstants.NOT_AVAILABLE);
		data.put("orderDeliveryAddress", MyHubUtils.getTrimmedTo(orderModel.getOrderDeliveryAddress()));
		data.put("orderDeliveryStatus", OrderUtils.getOrderStatusDisplayLabels(orderModel.getOrderDeliveryStatus()));
		data.put("paymentMode", OrderUtils.getPaymentModeString(orderModel.getPaymentMode()));
		data.put("paymentStatus", StringUtils.capatalize(orderModel.getPaymentStatus()));
		data.put("endOtp", orderModel.getEndOtp());

		data.put("storeAddressLat", orderModel.getStoreAddressLat());
		data.put("storeAddressLng", orderModel.getStoreAddressLng());
		data.put("orderDeliveryLat", orderModel.getOrderDeliveryLat());
		data.put("orderDeliveryLng", orderModel.getOrderDeliveryLng());

		data.put("orderNumberOfItems", StringUtils.valueOf(orderModel.getOrderNumberOfItems()));
		data.put("orderDeliveryDistance", MyHubUtils.getKilometerString(orderModel.getOrderDeliveryDistance()));
		data.put("orderTotal", MyHubUtils.getAmountWithCurrency(adminSettingsModel, orderModel.getOrderTotal()));
		data.put("orderDeliveryCharges", MyHubUtils.getAmountWithCurrency(adminSettingsModel, orderModel.getOrderDeliveryCharges()));
		data.put("orderPromoCodeDiscount", MyHubUtils.getAmountWithCurrency(adminSettingsModel, orderModel.getOrderPromoCodeDiscount()));
		data.put("orderCharges", MyHubUtils.getAmountWithCurrency(adminSettingsModel, orderModel.getOrderCharges()));
		
		data.put("storeName", orderModel.getStoreName());
		data.put("storeAddress", orderModel.getStoreAddress());

		String orderStatusFilterOptions = DropDownUtils.getOrderStatusForOrdersToBeChangedByAdmin(orderModel, orderModel.getOrderDeliveryStatus(), orderModel.isDelieveryManagedByVendorDriver(), true);
		if (orderStatusFilterOptions == null) {
			data.put("showChangeStatus", false + "");
		} else {
			data.put("showChangeStatus", true + "");
			data.put("orderStatusFilterOptions", orderStatusFilterOptions);
		}

		String driverId = orderModel.getDriverId();
		if (driverId != null && !ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(driverId)) {
			DriverInfoModel driverInfo = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);
			data.put("driverName", driverInfo.getFullName());
			data.put("driverPhone", MyHubUtils.formatFullName(driverInfo.getPhoneNoCode(), driverInfo.getPhoneNo()));
		} else {
			data.put("driverName", ProjectConstants.NOT_AVAILABLE);
			data.put("driverPhone", ProjectConstants.NOT_AVAILABLE);
		}

		data.put(FieldConstants.IS_SELF_DELIVERY_WITHIN_X_KM, orderModel.isSelfDeliveryWithinXKm() ? ProjectConstants.YES : ProjectConstants.NO);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.VIEW_ORDER_DETAILS_URL + "?orderId=" + orderId + "&type=" + type);
		return loadView(UrlConstants.JSP_URLS.VIEW_ORDERS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getViewOrdersList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String orderId = dtu.getRequestParameter(FieldConstants.ORDER_ID);

		double numberSearch = -1;
		try {
			numberSearch = Double.parseDouble(dtu.getGlobalSearchString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		int total = OrderItemModel.getOrderItemCount(orderId);
		List<OrderItemModel> orderItemList = OrderItemModel.getOrderItemSearch(dtu.getGlobalSearchStringWithPercentage(), orderId, dtu.getStartInt(), dtu.getLengthInt(), numberSearch);

		for (OrderItemModel orderItemModel : orderItemList) {
			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(orderItemModel.getOrderItemId());
			dtuInnerJsonArray.put(NewThemeUiUtils.getProductDetailsDiv(request, orderItemModel, adminSettingsModel));
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = OrderItemModel.getOrderItemSearchCount(dtu.getGlobalSearchStringWithPercentage(), orderId, numberSearch);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/vendor/order-delivery-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response postSurgeSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.ORDER_ID) String orderId,
		@QueryParam(FieldConstants.ORDER_STATUS_FILTER) String orderStatusFilter,
		@QueryParam(FieldConstants.TYPE) String type
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		Map<String, Object> output = new HashMap<>();

		OrderModel currentDbOrderModel = OrderModel.getOrderDetailsByOrderId(orderId);

		String vendorId = null;
		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loginSessionMap.get(LoginUtils.USER_ID), false);

			if (!vendorStoreIdList.contains(currentDbOrderModel.getVendorStoreId())) {
				output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
				output.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin("errorOrderStatusChangePermissions"), orderStatusFilter));
				return sendDataResponse(output);
			}

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap.get(LoginUtils.USER_ID));
		}

		// status can be changed by either
		// 1. super admin
		// 2. admin
		// 3. vendor against whom the order is placed.
		// 4. sub vendor with the store access
		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || loginSessionMap.get(LoginUtils.USER_ID).equalsIgnoreCase(currentDbOrderModel.getOrderReceivedAgainstVendorId())
					|| (vendorId != null && vendorId.equalsIgnoreCase(currentDbOrderModel.getOrderReceivedAgainstVendorId()))) {

			OrderModel inputOrderModel = OrderUtils.setInputOrderModel(orderId, orderStatusFilter);

			output = OrderUtils.updateOrderDeliveryStatusByVendorOrAdminViaAdminPanelOrApi(inputOrderModel, loginSessionMap.get(LoginUtils.USER_ID));
			output.put("url", WebappPropertyUtils.BASE_PATH + UrlConstants.PAGE_URLS.VIEW_ORDER_DETAILS_URL + "?orderId=" + orderId + "&type=" + type);

			return sendDataResponse(output);

		} else {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, BusinessAction.messageForKeyAdmin("errorOrderStatusChangePermissions"));
			return sendDataResponse(output);
		}
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VIEW_ORDERS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}