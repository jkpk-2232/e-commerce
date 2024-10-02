package com.webapp.actions.secure.vendor.orders;

import java.io.IOException;
import java.util.ArrayList;
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
import com.utils.myhub.OrderUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.OrderModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-orders-new")
public class ManageOrdersNewAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getOrdersNew(
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

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		String orderStatusFilterOptions = DropDownUtils.getOrderStatusList(true, ProjectConstants.OrderDeliveryConstants.ORDERS_NEW_TAB, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.ORDER_STATUS_FILTER_OPTIONS, orderStatusFilterOptions);

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.OrderDeliveryConstants.ORDERS_NEW_TAB);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ORDERS_NEW_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrdersNewList(
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

		String type = dtu.getRequestParameter(ProjectConstants.STATUS_TYPE);
		List<String> orderStatus = OrderUtils.getOrderStatusListAsPerOrderType(type);

		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		List<String> vendorStoreIdList = new ArrayList<>();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceId = vscm != null ? vscm.getServiceId() : null;
			vendorStoreIdList = null;
		} else if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceId = vscm != null ? vscm.getServiceId() : null;

			vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loginSessionMap.get(LoginUtils.USER_ID), false);

		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
			serviceId = DropDownUtils.parserForAllOptions(serviceId);
			vendorStoreIdList = null;
		}

		String orderStatusFilter = dtu.getRequestParameter(FieldConstants.ORDER_STATUS_FILTER);
		orderStatusFilter = DropDownUtils.parserForAllOptions(orderStatusFilter);

		String vendorOrderManagement = dtu.getRequestParameter(FieldConstants.VENDOR_ORDER_MANAGEMENT);
		vendorOrderManagement = DropDownUtils.parserForAllOptions(vendorOrderManagement);

		int orderShortIdSearch = MyHubUtils.searchNumericFormat(dtu.getGlobalSearchString());

		int total = OrderModel.getOrdersCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId, serviceId, null, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, vendorStoreIdList);
		List<OrderModel> orderList = OrderModel.getOrdersSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), null, vendorId, serviceId, null, orderStatus, orderShortIdSearch,
					orderStatusFilter, vendorOrderManagement, vendorStoreIdList);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (OrderModel orderModel : orderList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(orderModel.getOrderId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(orderModel.getOrderShortId());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(orderModel.getOrderCreationTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(orderModel.getCustomerName());
			dtuInnerJsonArray.put(orderModel.getOrderDeliveryAddress());
			dtuInnerJsonArray.put(orderModel.getVendorName());
			dtuInnerJsonArray.put(df.format(orderModel.getOrderTotal()));
			dtuInnerJsonArray.put(df.format(orderModel.getOrderDeliveryCharges()));
			dtuInnerJsonArray.put(df.format(orderModel.getOrderCharges()));
			dtuInnerJsonArray.put(orderModel.getOrderNumberOfItems());
			dtuInnerJsonArray.put(OrderUtils.getOrderStatusDisplayLabels(orderModel.getOrderDeliveryStatus()));
			dtuInnerJsonArray.put(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.VIEW_ORDER_DETAILS_URL + "?orderId=" + orderModel.getOrderId() + "&type=" + type)));
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = OrderModel.getOrdersSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId, serviceId, null, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, vendorStoreIdList);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ORDERS_NEW_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}