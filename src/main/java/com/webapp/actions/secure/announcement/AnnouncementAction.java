package com.webapp.actions.secure.announcement;

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
import javax.ws.rs.Consumes;
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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AnnouncementModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.UserProfileModel;

@Path("/announcements")
public class AnnouncementAction extends BusinessAction {

	private static final String[] ANNOUNCEMENT_ROLE_IDS = { UserRoles.PASSENGER_ROLE_ID, UserRoles.DRIVER_ROLE_ID };

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAnnouncement(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String userRoleOptions = DropDownUtils.getUserRoleListForBroadcastByRoleIds(UserRoles.PASSENGER_ROLE_ID, ANNOUNCEMENT_ROLE_IDS);
		data.put(FieldConstants.USER_ROLE_OPTIONS, userRoleOptions);

		String driverOptions = DropDownUtils.getDriverListByRoleIdOptionsAndApplicationStatus(null, UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, assignedRegionList);
		data.put(FieldConstants.DRIVER_LIST_OPTIONS, driverOptions);

		String passengerOptions = DropDownUtils.getUserListByRoleIdOptions(null, UserRoles.PASSENGER_ROLE_ID);
		data.put(FieldConstants.PASSENGER_LIST_OPTIONS, passengerOptions);

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(null, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		data.put(FieldConstants.IS_DISPLAY_PASSENGER_LIST, "YES");
		data.put(FieldConstants.DRIVER_ROLE_ID, UserRoles.DRIVER_ROLE_ID);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.ANNOUNCEMENT_URL);

		return loadView(UrlConstants.JSP_URLS.ANNOUNCEMENT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response getAnnouncementList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.USER_ROLE) String userRole,
		@FormParam(FieldConstants.PASSENGER_LIST) List<String> passengerList,
		@FormParam(FieldConstants.REGION_LIST) List<String> regionList,
		@FormParam(FieldConstants.DRIVER_LIST) List<String> driverList,
		@FormParam(FieldConstants.ANNOUNCEMENT_MESSAGE) String announcementMessage
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.USER_ROLE, "");
		data.put(FieldConstants.PASSENGER_LIST, "");
		data.put(FieldConstants.REGION_LIST, "");
		data.put(FieldConstants.DRIVER_LIST, "");

		String userRoleOptions = DropDownUtils.getUserRoleListForBroadcastByRoleIds(userRole, ANNOUNCEMENT_ROLE_IDS);
		data.put(FieldConstants.USER_ROLE_OPTIONS, userRoleOptions);

		String passengerOptions = DropDownUtils.getUserListByRoleIdOptions(passengerList, UserRoles.PASSENGER_ROLE_ID);
		data.put(FieldConstants.PASSENGER_LIST_OPTIONS, passengerOptions);

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String driverListOptions = DropDownUtils.getDriverListByRoleIdOptionsAndApplicationStatus(driverList, UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, assignedRegionList);
		data.put(FieldConstants.DRIVER_LIST_OPTIONS, driverListOptions);

		data.put(FieldConstants.ANNOUNCEMENT_MESSAGE, announcementMessage);

		if (hasErrors(userRole, passengerList, regionList, driverList)) {

			if (UserRoleUtils.isPassengerRole(userRole)) {
				data.put(FieldConstants.IS_DISPLAY_PASSENGER_LIST, "YES");
			} else {
				data.put(FieldConstants.IS_DISPLAY_PASSENGER_LIST, "NO");
			}

			data.put(FieldConstants.DRIVER_ROLE_ID, UserRoles.DRIVER_ROLE_ID);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.ANNOUNCEMENT_URL);

			return loadView(UrlConstants.JSP_URLS.ANNOUNCEMENT_JSP);
		}

		AnnouncementModel announcementModel = new AnnouncementModel();
		announcementModel.setAnnouncementMessage(announcementMessage);
		announcementModel.addAnnouncementtMessage();

		List<String> passengerIdList = new ArrayList<>();

		if (passengerList.size() > 0) {
			for (String passengerId : passengerList) {
				if (ProjectConstants.All.equals(passengerId)) {
					passengerIdList = new ArrayList<>();
					List<UserProfileModel> usersList = UserProfileModel.getAllUsersByRoleId(UserRoles.PASSENGER_ROLE_ID);
					for (UserProfileModel user : usersList) {
						passengerIdList.add(user.getUserId());
					}
					break;
				} else {
					passengerIdList.add(passengerId);
				}
			}
		}

		LOGGER.info("\n\n\n\tpassengerIdList size :: " + passengerIdList.size());
		LOGGER.info("\n\n\n\tpassengerIdList :: " + passengerIdList);

		List<String> driverIdList = new ArrayList<>();

		if (driverList.size() > 0) {
			for (String driverId : driverList) {
				if (ProjectConstants.All.equals(driverId)) {
					driverIdList = new ArrayList<>();
					List<DriverInfoModel> driverUserList = DriverInfoModel.getDriverListByMulticityRegionIds(UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, regionList.toArray(new String[0]));
					for (DriverInfoModel driver : driverUserList) {
						driverIdList.add(driver.getUserId());
					}
					break;
				} else {
					driverIdList.add(driverId);
				}
			}
		}

		LOGGER.info("\n\n\n\tdriverIdList size :: " + driverIdList.size());
		LOGGER.info("\n\n\n\tdriverIdList :: " + driverIdList);

		MyHubNotificationUtils.sendNotificationForAnnouncements(announcementMessage, userRole, passengerIdList, driverIdList);

		return redirectToPage(UrlConstants.PAGE_URLS.ANNOUNCEMENT_URL);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAnnouncementDetailsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		int total = AnnouncementModel.getAnnouncementCount(dtu.getStartDatelong(), dtu.getEndDatelong());
		List<AnnouncementModel> announcemnentModelList = AnnouncementModel.getAnnouncementListForSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage());

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (AnnouncementModel announcemnentList : announcemnentModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(announcemnentList.getAnnouncementId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(announcemnentList.getAnnouncementMessage());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(announcemnentList.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(announcemnentList.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = AnnouncementModel.getTotalAnnouncementCountBySearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/user-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAllUsersByRoleId(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.ROLE_ID) String roleId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<UserProfileModel> userList = UserProfileModel.getAllUsersByRoleId(roleId);

		Map<String, Object> userListMap = new HashMap<String, Object>();

		for (UserProfileModel userProfileModel : userList) {
			userListMap.put(userProfileModel.getUserId(), userProfileModel.getFullName());
		}

		return sendDataResponse(userListMap);
	}

	@Path("/driver-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverListRegionWise(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.REGION_IDS) String regionIds
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String[] multicityRegionIds = MyHubUtils.splitStringByCommaArray(regionIds);
		String driverListOptions = DropDownUtils.getDriverListByMulticityRegionIdsForMultiselectOptions(null, multicityRegionIds);

		Map<String, Object> outputMap = new HashMap<String, Object>();

		if (StringUtils.validString(driverListOptions)) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
			outputMap.put(FieldConstants.DRIVER_LIST_OPTIONS, driverListOptions);
		} else {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(FieldConstants.DRIVER_LIST_OPTIONS, null);
		}

		return sendDataResponse(outputMap);
	}

	public boolean hasErrors(String userRoleList, List<String> passengerList, List<String> regionList, List<String> driverList) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.ANNOUNCEMENT_MESSAGE, messageForKeyAdmin("labelMessage"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ANNOUNCEMENT_MESSAGE, messageForKeyAdmin("labelMessage"), new MinMaxLengthValidationRule(1, 200));

		if (UserRoleUtils.isPassengerRole(userRoleList)) {
			validator.addValidationMapping(FieldConstants.PASSENGER_LIST, messageForKeyAdmin("labelPassenger"), new RequiredListValidationRule(passengerList.size()));
		} else {
			validator.addValidationMapping(FieldConstants.REGION_LIST, messageForKeyAdmin("labelRegion"), new RequiredListValidationRule(regionList.size()));
			validator.addValidationMapping(FieldConstants.DRIVER_LIST, messageForKeyAdmin("labelDriver"), new RequiredListValidationRule(driverList.size()));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ANNOUNCEMENT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}