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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-sub-vendors")
public class ManageSubVendorAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getSubVendors(
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

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		if (!UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			String vendorIdOptions = DropDownUtils.getVendorFilterListOptions(StringUtils.validString(vendorId) ? vendorId : ProjectConstants.ALL_ID, UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_SUB_VENDOR_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_SUB_VENDOR_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorsList(
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

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = UserModel.getTotalOperatorCountByBusinessOwnerId(vendorId, assignedRegionList, dtu.getStartDatelong(), dtu.getEndDatelong(), loginSessionMap.get(LoginUtils.USER_ID));
		List<UserModel> userModelList = UserModel.getBusinessOpertorListForSearch(vendorId, dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.SUB_VENDOR_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), assignedRegionList, dtu.getStartDatelong(),
					dtu.getEndDatelong(), loginSessionMap.get(LoginUtils.USER_ID));

		int count = dtu.getStartInt();

		for (UserModel userProfileModel : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfileModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));
			dtuInnerJsonArray.put(userProfileModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userProfileModel.getPhoneNoCode(), userProfileModel.getPhoneNo()));

			if (userProfileModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (messageForKeyAdmin("labelStatus") + " - " + messageForKeyAdmin("labelActive"))));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, (messageForKeyAdmin("labelStatus") + " - " + messageForKeyAdmin("labelDeactive"))));
			}

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_SUB_VENDOR_URL + "?userId=" + userProfileModel.getUserId())));
			}

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfileModel.getUserId(), "sub vendor", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SUB_VENDOR_URL), userProfileModel.isActive() ? "userDeactivate" : "userReactivate"));
			}

			if (btnGroupStr.length() <= 0) {
				btnGroupStr.append(messageForKey("labelAccessDenied"));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_SUB_VENDOR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}