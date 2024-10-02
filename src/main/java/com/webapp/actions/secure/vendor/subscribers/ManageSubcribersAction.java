package com.webapp.actions.secure.vendor.subscribers;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.VendorSubscriberModel;

@Path("/manage-subscribers")
public class ManageSubcribersAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadManageSubcribersGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SUBCRIBERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(true, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, assignedRegionList, null);
		data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		return loadView(UrlConstants.JSP_URLS.MANAGE_SUBSCRIBERS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadManageSubcribersListGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SUBCRIBERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		} else if(UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		String vendorStoreId = dtu.getRequestParameter(FieldConstants.VENDOR_STORE_ID);
		vendorStoreId = DropDownUtils.parserForAllOptions(vendorStoreId);

		int total = VendorSubscriberModel.getVendorSubscriberCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId, vendorStoreId);
		List<VendorSubscriberModel> vendorSubscriberList = VendorSubscriberModel.getVendorSubscriberSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), vendorId, vendorStoreId);

		int count = dtu.getStartInt();

		for (VendorSubscriberModel vendorSubscriberModel : vendorSubscriberList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorSubscriberModel.getVendorSubscriberId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorSubscriberModel.getVendorName());
			dtuInnerJsonArray.put(vendorSubscriberModel.getVendorBrandName());
			dtuInnerJsonArray.put(vendorSubscriberModel.getStoreName());
			dtuInnerJsonArray.put(vendorSubscriberModel.getSubscriberName());
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = VendorSubscriberModel.getVendorSubscriberSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId, vendorStoreId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_SUBSCRIBERS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}