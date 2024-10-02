package com.webapp.actions.secure.driver;

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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.EncryptionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DriverSubscriptionUtils;
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

@Path("/manage-drivers")
public class DriverAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadDrivers(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String statusOptions = DropDownUtils.getStatusList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.STATUS_OPTIONS, statusOptions);

		String approvelOptions = DropDownUtils.getApprovelSearchList("0");
		data.put(FieldConstants.APPROVEL_OPTIONS, approvelOptions);

		if (!UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

			String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_DRIVER_URL);
		data.put(FieldConstants.OTHER_PAGE_URL, UrlConstants.PAGE_URLS.DRIVER_SUBSCRIPTION_EXTENSTION_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadDriversList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String status = dtu.getRequestParameter(FieldConstants.STATUS);
		String approvel = dtu.getRequestParameter(FieldConstants.APPROVEL);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String statusCheck = null;
		boolean statusCheckBoolean = false;

		if (status.equalsIgnoreCase(ProjectConstants.ACTIVE_ID)) {
			statusCheck = ProjectConstants.ACTIVE_ID;
			statusCheckBoolean = true;
		} else if (status.equalsIgnoreCase(ProjectConstants.DEACTIVE_ID)) {
			statusCheck = ProjectConstants.DEACTIVE_ID;
			statusCheckBoolean = false;
		}

		String approvelCheck = null;
		boolean approvelStatus = true;

		if (approvel.equalsIgnoreCase("true")) {
			approvelCheck = "YES";
			approvelStatus = true;
		} else if (approvel.equalsIgnoreCase("false")) {
			approvelCheck = "YES";
			approvelStatus = false;
		}

		int total = UserModel.getTotalUserCount(UserRoles.DRIVER_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), null, assignedRegionList, vendorId);
		List<UserModel> userModelList = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			userModelList = UserModel.getVendorDriverList(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, statusCheck, statusCheckBoolean, approvelCheck,
						approvelStatus, loginSessionMap.get(LoginUtils.USER_ID));

		} else {

			userModelList = UserModel.getDriverList(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, statusCheck, statusCheckBoolean, approvelCheck,
						approvelStatus, vendorId, null);
		}

		long tempTimeNow = DateUtils.nowAsGmtMillisec();
		int count = dtu.getStartInt();
		String currentSubscriptionPackageId;

		for (UserModel userProfile : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfile.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userProfile.getFirstName(), userProfile.getLastName()));
			dtuInnerJsonArray.put(userProfile.getEmail());
			dtuInnerJsonArray.put(EncryptionUtils.decryptByte(userProfile.getPassword()));
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userProfile.getPhoneNoCode(), userProfile.getPhoneNo()));

			if (StringUtils.validString(userProfile.getDriverLicenseCardNumber())) {
				dtuInnerJsonArray.put(userProfile.getDriverLicenseCardNumber());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(userProfile.isApprovelStatus() ? ProjectConstants.APPROVED : ProjectConstants.NOT_APPROVED);

			if (StringUtils.validString(userProfile.getVendorName())) {
				dtuInnerJsonArray.put(userProfile.getVendorName());
			} else {
				dtuInnerJsonArray.put(messageForKeyAdmin("labelAdmin"));
			}

			dtuInnerJsonArray.put(userProfile.getAgentNumber());

			if (userProfile.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(userProfile.getUserId(), null, tempTimeNow);

			if (currentSubscriptionPackageId != null) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (messageForKeyAdmin("labelVendorMonthlySubscriptionStatus") + " - " + messageForKeyAdmin("labelActive"))));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, (messageForKeyAdmin("labelVendorMonthlySubscriptionStatus") + " - " + messageForKeyAdmin("labelDeactive"))));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_DRIVER_URL + "?driverId=" + userProfile.getUserId())));
			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewTours"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.DRIVER_TOURS_URL + "?driverId=" + userProfile.getUserId())));

			if (userProfile.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.getUserId(), "driver", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL), "userDeactivate"));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.getUserId(), "driver", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL), "userReactivate"));
			}

			if (!userProfile.isApprovelStatus()) {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.getUserId(), "driver", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL), "userApproved"));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelSubscriptionHistory"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_URL + "?driverId=" + userProfile.getUserId()),
						UrlConstants.JSP_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_ICON));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelSubscribePackage"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.SUBSCRIBE_PACKAGE_URL + "?driverId=" + userProfile.getUserId()),
						UrlConstants.JSP_URLS.SUBSCRIBE_PACKAGE_ICON));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelTransactionHistory"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_URL + "?driverId=" + userProfile.getUserId()),
						UrlConstants.JSP_URLS.MANAGE_DRIVER_TRANSACTION_HISTORY_REPORTS_ICON));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (status.equalsIgnoreCase(ProjectConstants.ACTIVE_ID)) {
				filterCount = UserModel.getVendorsTotalActiveUserCountBySearch(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, approvelCheck, approvelStatus,
							loginSessionMap.get(LoginUtils.USER_ID));
			} else if (status.equalsIgnoreCase(ProjectConstants.DEACTIVE_ID)) {
				filterCount = UserModel.getVendorsTotalDeactiveUserCountBySearch(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, approvelCheck, approvelStatus,
							loginSessionMap.get(LoginUtils.USER_ID));
			} else {
				filterCount = UserModel.getVendorsTotalUserCountBySearch(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, approvelCheck, approvelStatus,
							loginSessionMap.get(LoginUtils.USER_ID));
			}

		} else {

			if (status.equalsIgnoreCase(ProjectConstants.ACTIVE_ID)) {
				filterCount = UserModel.getTotalActiveDriverCountBySearch(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, approvelCheck, approvelStatus, vendorId, null);
			} else if (status.equalsIgnoreCase(ProjectConstants.DEACTIVE_ID)) {
				filterCount = UserModel.getTotalDeactiveDriverCountBySearch(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, approvelCheck, approvelStatus, vendorId, null);
			} else {
				filterCount = UserModel.getTotalDriverCountBySearch(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, approvelCheck, approvelStatus, vendorId, null);
			}
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}