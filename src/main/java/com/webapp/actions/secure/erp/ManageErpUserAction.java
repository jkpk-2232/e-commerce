package com.webapp.actions.secure.erp;

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
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-erp-users")
public class ManageErpUserAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getErpUser (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on
		
		preprocessRequestNewTheme(request, response);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_ERP_USER_URL);
		
		return loadView(UrlConstants.JSP_URLS.MANAGE_ERP_USER_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getErpUsersList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		String ownUserId = null;
		
		if (!UserRoleUtils.isSuperAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			ownUserId = loginSessionMap.get(LoginUtils.USER_ID);
		}
		
		int total = UserModel.getErpTotalUserCount(UserRoles.ERP_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), ownUserId, assignedRegionList);
		List<UserModel> userModelList = UserModel.getErpListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.ERP_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, ownUserId);
		
		int count = dtu.getStartInt();

		for (UserModel userProfileModel : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfileModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(userProfileModel.getVendorBrandName());
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));
			dtuInnerJsonArray.put(userProfileModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userProfileModel.getPhoneNoCode(), userProfileModel.getPhoneNo()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_ERP_USER_URL + "?userId=" + userProfileModel.getUserId())));
			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelBrandAssociation"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL + "?vendorId=" + userProfileModel.getUserId()),
						UrlConstants.JSP_URLS.CAMPANY_ICON));
			

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ERP_USERS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
