package com.webapp.actions.secure.subscription.packages;

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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.DriverSubscriptionPackageHistoryModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-driver-subscription-package-reports")
public class ManageDriverSubscriptionPackageReportsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverSubscriptionPackageReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		String driverVendorId = StringUtils.validString(driverId) ? MultiTenantUtils.getVendorIdByUserId(driverId) : ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE;

		String vendorIdOptions = DropDownUtils.getUserAccessWiseVendorList(driverVendorId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		data.put(FieldConstants.DRIVER_ID, driverId);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverSubscriptionPackageReportsList(
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

		String driverId = dtu.getRequestParameter(FieldConstants.DRIVER_ID);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {
			vendorId = DropDownUtils.parserForAllOptions(vendorId);
		}

		if (driverId.isEmpty()) {
			driverId = null;
		}

		String[] vendorIds = DropDownUtils.getUserAccessWiseVendorListDatatable(vendorId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));

		int total = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorIds, driverId);
		List<DriverSubscriptionPackageHistoryModel> driverSubscriptionPackageHistoryList = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getGlobalSearchStringWithPercentage(),
					dtu.getStartDatelong(), dtu.getEndDatelong(), vendorIds, driverId);

		int count = dtu.getStartInt();
		long timeNow = DateUtils.nowAsGmtMillisec();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : driverSubscriptionPackageHistoryList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getDriverName());
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getVendorName());
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getPackageName());
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getDurationDays());
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getPrice());
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getCarType());
			dtuInnerJsonArray.put(driverSubscriptionPackageHistoryModel.getOrderId());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(driverSubscriptionPackageHistoryModel.getPackageStartTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(driverSubscriptionPackageHistoryModel.getPackageEndTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if (driverSubscriptionPackageHistoryModel.getPackageStartTime() <= timeNow && timeNow <= driverSubscriptionPackageHistoryModel.getPackageEndTime()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else if (driverSubscriptionPackageHistoryModel.getPackageStartTime() >= timeNow) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.PRIMARY, messageForKeyAdmin("labelNotYetActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelExpiredPackage")));
			}

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? driverSubscriptionPackageHistoryList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}