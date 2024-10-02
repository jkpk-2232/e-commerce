package com.webapp.actions.secure.history;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserProfileModel;

@Path("/manage-history-active")
public class ManageHistoryActiveAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);
		if (userProfileModel == null) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		if (!UrlAccessesModel.hasUserUrlAccess(ManageHistoryNewAction.MANAGE_HISTORY_URL, loggedInUserId)) {
			return loadView("/error.jsp");
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(userProfileModel);

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put("vendorIdOptions", vendorIdOptions);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, "-1");
		data.put("serviceIdOptions", serviceIdOptions);

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.OrderDeliveryConstants.ORDERS_ACTIVE_TAB);

		String orderStatusFilterOptions = DropDownUtils.getOrderStatusList(true, ProjectConstants.OrderDeliveryConstants.ORDERS_ACTIVE_TAB, "-1");
		data.put("orderStatusFilterOptions", orderStatusFilterOptions);

		String vendorOrderManagementOptions = DropDownUtils.getVendorOrderManagementOptionsList("-1");
		data.put("vendorOrderManagementOptions", vendorOrderManagementOptions);

		return loadView("/secure/history/manage-history-active.jsp");
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/history/manage-history-active.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}