package com.webapp.actions.secure.whmgmt.users;

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
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.BusinessInterestedUsersModel;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-warehouse-users")
public class ManageWarehouseUsersAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadWarehouseUsersList(
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
		
		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_WAREHOUSE_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_WAREHOUSE_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getWarehouseUsersList(
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

		int total = UserModel.getTotalPassengerUserCount(UserRoles.WAREHOUSE_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), null, null, null, null);

		List<UserModel> userModelList = UserModel.getUserListForSearch(dtu.getStartInt(), dtu.getLengthInt(), null, UserRoles.WAREHOUSE_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), null, null, null);
		
		int count = dtu.getStartInt();

		for (UserModel userProfile : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfile.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userProfile.getFirstName(), userProfile.getLastName()));
			dtuInnerJsonArray.put(userProfile.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userProfile.getPhoneNoCode(), userProfile.getPhoneNo()));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(userProfile.getCreatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DISPLAY_DATE_TIME_FORMAT));

			if (userProfile.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			/*
			 * btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.
			 * OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewTours"),
			 * UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.PASSENGER_TOURS_URL +
			 * "?passengerId=" + userProfile.getUserId())));
			 * 
			 * btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(
			 * messageForKeyAdmin("labelViewSubscribedDrivers"),
			 * UrlConstants.getUrl(request,
			 * UrlConstants.PAGE_URLS.MANAGE_DRIVER_SUBSCRIBERS_URL + "?subscriberUserId=" +
			 * userProfile.getUserId()), UrlConstants.JSP_URLS.MANAGE_SUBSCRIBERS_ICON));
			 * 
			 * if (userProfile.isActive()) {
			 * btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.
			 * getUserId(), "customer", UrlConstants.getUrl(request,
			 * UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL), "userDeactivate")); } else {
			 * btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfile.
			 * getUserId(), "customer", UrlConstants.getUrl(request,
			 * UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL), "userReactivate")); }
			 * 
			 * dtuInnerJsonArray.put(btnGroupStr);
			 */

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_WAREHOUSE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
