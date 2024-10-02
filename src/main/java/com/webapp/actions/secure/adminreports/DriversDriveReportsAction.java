package com.webapp.actions.secure.adminreports;

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
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-drivers-drive-reports")
public class DriversDriveReportsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response driversDriverReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.DISTANCE_TYPE, messageForKeyAdmin("labelDriverKmReports"));

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_DRIVER_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response driversDriverReportList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String vendorId = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = UserModel.getTotalUserCount(UserRoles.DRIVER_ROLE_ID, 0, 0, null, assignedRegionList, vendorId);

		List<UserModel> userModelList = UserModel.getDriverKmReportAdminListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), ProjectConstants.TourStatusConstants.ENDED_TOUR,
					dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, vendorId);

		int count = dtu.getStartInt();

		for (UserModel userModel : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
			dtuInnerJsonArray.put(userModel.getEmail());
			dtuInnerJsonArray.put(StringUtils.valueOf(userModel.getDriverDistanceTotal() / adminSettings.getDistanceUnits()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.DRIVER_KM_DETAIL_REPORTS_URL + "?driverId=" + userModel.getUserId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_DRIVER_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}