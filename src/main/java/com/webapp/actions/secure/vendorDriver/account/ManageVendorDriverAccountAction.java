package com.webapp.actions.secure.vendorDriver.account;

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
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserAccountModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-vendor-drivers/account")
public class ManageVendorDriverAccountAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorDriverAccountList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasAccountsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadVendorDriverAccountList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasAccountsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		DatatableUtils dtu = new DatatableUtils(request);

		int total = UserAccountModel.getTotalVendorUserAccountCount(UserRoles.DRIVER_ROLE_ID, assignedRegionList, loginSessionMap.get(LoginUtils.USER_ID));
		List<UserAccountModel> userAccountModelList = UserAccountModel.getVendorUserAccountListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), assignedRegionList, UserRoles.DRIVER_ROLE_ID,
					loginSessionMap.get(LoginUtils.USER_ID));

		int count = dtu.getStartInt();

		for (UserAccountModel userAccountModel : userAccountModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userAccountModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(userAccountModel.getFirstName());
			dtuInnerJsonArray.put(userAccountModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userAccountModel.getPhoneNoCode(), userAccountModel.getPhoneNo()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountModel.getCurrentBalance()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountModel.getHoldBalance()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountModel.getApprovedBalance()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountModel.getTotalBalance()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelView"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_URL + "?userId=" + userAccountModel.getUserId() + "&frm=vdrac"),
						UrlConstants.JSP_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_ICON));

			btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, userAccountModel.getUserId(), "recharge"));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = UserAccountModel.getFilteredVendorUserAccountCount(dtu.getGlobalSearchStringWithPercentage(), assignedRegionList, UserRoles.DRIVER_ROLE_ID, loginSessionMap.get(LoginUtils.USER_ID));
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_JS, UrlConstants.JS_URLS.DRIVER_VENDOR_ACCOUNT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}