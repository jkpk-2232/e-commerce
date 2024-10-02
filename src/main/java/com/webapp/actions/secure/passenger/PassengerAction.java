package com.webapp.actions.secure.passenger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.DriverReferralCodeLogModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-passengers")
public class PassengerAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadPassengerList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String statusOptions = DropDownUtils.getStatusList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.STATUS_OPTIONS, statusOptions);

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_PASSENGER_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PASSENGER_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPassengerUserList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String status = dtu.getRequestParameter(FieldConstants.STATUS);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		String statusCheck = null;
		if (status.equalsIgnoreCase(ProjectConstants.ACTIVE_ID)) {
			statusCheck = ProjectConstants.ACTIVE_ID;
		} else if (status.equalsIgnoreCase(ProjectConstants.DEACTIVE_ID)) {
			statusCheck = ProjectConstants.DEACTIVE_ID;
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		int total = UserModel.getTotalPassengerUserCount(UserRoles.PASSENGER_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), null, null, vendorId, statusCheck);
		List<UserModel> userModelList = UserModel.getUserListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.PASSENGER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), null, vendorId, statusCheck);

		int count = dtu.getStartInt();
		StringBuilder referrerDetails = new StringBuilder();

		for (UserModel userProfile : userModelList) {
			
			count++;
			btnGroupStr = new StringBuilder();
			referrerDetails = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfile.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userProfile.getFirstName(), userProfile.getLastName()));
			dtuInnerJsonArray.put(userProfile.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userProfile.getPhoneNoCode(), userProfile.getPhoneNo()));
			
			if (userProfile.getPhoneNum() != null) {
				dtuInnerJsonArray.put("KP E-MART");
			} else {
				dtuInnerJsonArray.put("My Hub");
			}

			referrerDetails.append("<a class='referrerDetailsSpan' id='").append(userProfile.getUserId()).append("'>");

			if (userProfile.getDriverReferralCodeLogId() != null && userProfile.getDriverReferralDriverName() != null) {
				referrerDetails.append(userProfile.getDriverReferralDriverName());
			} else {
				referrerDetails.append(messageForKeyAdmin("labelAttachDriver"));
			}

			referrerDetails.append("</a>");

			dtuInnerJsonArray.put(referrerDetails);

			if (userProfile.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewTours"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.PASSENGER_TOURS_URL + "?passengerId=" + userProfile.getUserId())));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelViewSubscribedDrivers"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_SUBSCRIBERS_URL + "?subscriberUserId=" + userProfile.getUserId()),
						UrlConstants.JSP_URLS.MANAGE_SUBSCRIBERS_ICON));

			if (userProfile.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.getUserId(), "customer", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL), "userDeactivate"));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.getUserId(), "customer", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL), "userReactivate"));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/passenger-info")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPassengerInfo(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.USER_ID) String userId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();

		if (!UserRoleUtils.isSuperAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("labelUnauthorized"));
			return sendDataResponse(outputMap);
		}

		UserProfileModel userModel = UserProfileModel.getUserAccountDetailsById(userId);

		if (userModel == null) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("labelNoUserFound"));
			return sendDataResponse(outputMap);
		}

		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outputMap.put("passengerName", MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
		outputMap.put("passengerEmail", userModel.getEmail());
		outputMap.put("passengerPhone", MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
		outputMap.put("passengerCredits", df.format(0));

		return sendDataResponse(outputMap);
	}

	@Path("/driver-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadDriverList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PASSENGER_ID) String passengerId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();

		String alreadyAttachedDriver = "";

		DriverReferralCodeLogModel driverReferralCodeLogModel = DriverReferralCodeLogModel.getDriverReferralCodeLogByPassengerId(passengerId);

		if (driverReferralCodeLogModel != null) {
			alreadyAttachedDriver = driverReferralCodeLogModel.getDriverId();
		}

		Map<String, Object> innerMap = new HashMap<String, Object>();
		List<UserModel> driverModelList = UserModel.getActiveUserListForSearch(0, 0, "", UserRoles.DRIVER_ROLE_ID, "%%", 0, 0, null);

		if (driverModelList.size() > 0) {

			List<Map<String, Object>> driverListMap = new ArrayList<Map<String, Object>>();

			for (UserModel userModel : driverModelList) {

				if (!alreadyAttachedDriver.equals(userModel.getUserId())) {

					innerMap = new HashMap<String, Object>();

					innerMap.put("driverId", userModel.getUserId());
					innerMap.put("driverName", userModel.getFirstName() + " " + userModel.getLastName());
					innerMap.put("driverEmail", userModel.getEmail());

					driverListMap.add(innerMap);
				}
			}

			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
			outputMap.put("driverList", driverListMap);
		}

		return sendDataResponse(outputMap);
	}

	@Path("/update-referrer")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateReferrer(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PASSENGER_ID) String passengerId,
		@FormParam(FieldConstants.DRIVER_ID) String driverId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> outputMap = new HashMap<>();

		if (!StringUtils.validString(driverId)) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorSelectDriverFromList"));
			return sendDataResponse(outputMap);
		}

		if (!StringUtils.validString(passengerId)) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorInvalidPassengerId"));
			return sendDataResponse(outputMap);
		}

		DriverReferralCodeLogModel driverReferralCodeLogModel = DriverReferralCodeLogModel.getDriverReferralCodeLogByPassengerId(passengerId);

		if (driverReferralCodeLogModel != null) {
			driverReferralCodeLogModel.deleteDriverReferralCodeLogByPassengerId(loginSessionMap.get(LoginUtils.USER_ID));
		}

		driverReferralCodeLogModel = new DriverReferralCodeLogModel();
		driverReferralCodeLogModel.setDriverId(driverId);
		driverReferralCodeLogModel.setPassengerId(passengerId);
		driverReferralCodeLogModel.addDriverReferralCodeLog(loginSessionMap.get(LoginUtils.USER_ID));

		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successMsgUpdateReferrerSuccessfully"));
		return sendDataResponse(outputMap);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PASSENGER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}