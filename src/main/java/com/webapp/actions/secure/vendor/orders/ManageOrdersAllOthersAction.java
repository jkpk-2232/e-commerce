package com.webapp.actions.secure.vendor.orders;

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

import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;

@Path("/manage-orders-all-others")
public class ManageOrdersAllOthersAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getOrdersAllOthers(
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

		String orderStatusFilterOptions = DropDownUtils.getOrderStatusList(true, ProjectConstants.OrderDeliveryConstants.ORDERS_ALL_OTHERS_TAB, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.ORDER_STATUS_FILTER_OPTIONS, orderStatusFilterOptions);

		String vendorOrderManagementOptions = DropDownUtils.getVendorOrderManagementOptionsList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.VENDOR_ORDER_MANAGEMENT_OPTIONS, vendorOrderManagementOptions);

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.OrderDeliveryConstants.ORDERS_ALL_OTHERS_TAB);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ORDERS_ALL_OTHERS_JSP);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ORDERS_ALL_OTHERS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}