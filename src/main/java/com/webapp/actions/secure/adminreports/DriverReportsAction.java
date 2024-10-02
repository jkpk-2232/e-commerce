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

@Path("/manage-driver-reports")
public class DriverReportsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverReports(
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

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_REPORTS_JSP);
	}

//	@Path("/driver-payable-info")
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getDriverPayableInfo(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response,
//		@QueryParam("startDate") String startDate,
//		@QueryParam("endDate") String endDate
//		) throws IOException, SQLException, ServletException {
//	//@formatter:on
//
//		preprocessRequestNewTheme(request, response);
//
//		if (loginSessionMap == null) {
//			return logoutUser();
//		}
//
//		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
//
//		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
//		long endDatelong = DateUtils.getStartOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
//
//		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
//
//		double totalAdminSettlementAmount = InvoiceModel.getTotalAdminSettlementAmount(startDatelong, endDatelong, null, assignedRegionList);
//
//		Map<String, Object> outputMap = new HashMap<String, Object>();
//		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
//		outputMap.put(FieldConstants.SETTLEMENT_AMOUNT, MyHubUtils.getDriverSettlementString(adminSettings, totalAdminSettlementAmount));
//
//		return sendDataResponse(outputMap);
//	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverReportsList(
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

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		String vendorId = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		int total = UserModel.getTotalUserCount(UserRoles.DRIVER_ROLE_ID, 0, 0, null, assignedRegionList, vendorId);
		// db call for getting the list of drivers
		List<UserModel> userModelList = UserModel.getDriverReportAdminListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(),
					assignedRegionList, vendorId);

		// db call for getting the settlement amount for drivers
		List<UserModel> userModelListForAdminSettelmentAmt = UserModel.getDriverReportAdminListForSearch(0, 0, dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList,
					vendorId);

		double totalAdminSettlementAmount = 0;

		for (UserModel userModel : userModelListForAdminSettelmentAmt) {
			totalAdminSettlementAmount += userModel.getAdminSettlementAmount();
		}

		String totalAdminSettlementAmountString = MyHubUtils.getDriverSettlementString(adminSettings, totalAdminSettlementAmount);

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
			dtuInnerJsonArray.put(StringUtils.valueOf(userModel.getInvoiceTotal()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userModel.getDriverReceivableAmount()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userModel.getDriverAmountTotal()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userModel.getMarkupFare()));
			dtuInnerJsonArray.put(MyHubUtils.getDriverSettlementString(adminSettings, userModel.getAdminSettlementAmount()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_BOOKINGS_URL + "?driverId=" + userModel.getUserId())));
			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = UserModel.getDriverReportAdminListForSearchCount(UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, vendorId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		dtuJsonObject.put(FieldConstants.TOTAL_ADMIN_SETTLEMENT_AMOUNT, totalAdminSettlementAmountString);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}