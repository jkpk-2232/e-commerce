package com.webapp.actions.secure.driver.account;

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
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.UserAccountLogsModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserProfileModel;

@Path("/manage-drivers/account-logs")
public class ManageDriverAccountLogsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadDriverAccountLogs(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.USER_ID) String userId, 
		@QueryParam("frm") String from
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasAccountsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (!StringUtils.validString(userId)) {

			String url = UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_URL;

			if ("vac".equalsIgnoreCase(from)) {
				url = UrlConstants.PAGE_URLS.MANAGE_VENDOR_ACCOUNT_URL;
			} else if ("vdrac".equalsIgnoreCase(from)) {
				url = UrlConstants.PAGE_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_URL;
			}

			return redirectToPage(url);
		}

		UserProfileModel userProfile = UserProfileModel.getUserAccountDetailsById(userId.trim());

		if (userProfile == null) {

			String url = UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_URL;

			if ("vac".equalsIgnoreCase(from)) {
				url = UrlConstants.PAGE_URLS.MANAGE_VENDOR_ACCOUNT_URL;
			} else if ("vdrac".equalsIgnoreCase(from)) {
				url = UrlConstants.PAGE_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_URL;
			}

			return redirectToPage(url);
		}

		if ("drac".equalsIgnoreCase(from)) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			data.put("urlCallFrom", "driverAccount");

		} else if ("vac".equalsIgnoreCase(from)) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_ACCOUNT_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			data.put("urlCallFrom", "vendorAccount");

		} else if ("vdrac".equalsIgnoreCase(from)) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			data.put("urlCallFrom", "vendorDriverAccount");

		} else {

			data.put("urlCallFrom", "driverAccount");
		}

		UserAccountModel userAccountModel = UserAccountModel.getUserWithAccountDetails(userId.trim());
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		data.put("userIdHidden", userId);
		data.put("dName", MyHubUtils.formatFullName(userAccountModel.getFirstName(), userAccountModel.getLastName()));
		data.put("dEmail", userAccountModel.getEmail());
		data.put("dPhoneNumber", MyHubUtils.formatPhoneNumber(userAccountModel.getPhoneNoCode(), userAccountModel.getPhoneNo()));
		data.put("dCurrentBalence", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getCurrentBalance()));
		data.put("dHoldBalence", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getHoldBalance()));
		data.put("dApprovedBalence", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getApprovedBalance()));

		if ("drac".equalsIgnoreCase(from)) {

			boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_DRIVER_ACCOUNT_ID);
			data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		} else if ("vac".equalsIgnoreCase(from)) {

			boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_VENDOR_ACCOUNT_ID);
			data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		} else if ("vdrac".equalsIgnoreCase(from)) {

			boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID);
			data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		} else {

			data.put(FieldConstants.IS_EXPORT_ACCESS, ProjectConstants.TRUE_STRING);
		}

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverAccountlogsList(
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

		DatatableUtils dtu = new DatatableUtils(request);
		String userId = dtu.getRequestParameter(FieldConstants.USER_ID);

		int total = UserAccountLogsModel.getTotalUserAccountLogsCount(userId, dtu.getStartDatelong(), dtu.getEndDatelong());
		List<UserAccountLogsModel> userAccountLogsModelList = UserAccountLogsModel.getUserAccountLogsListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), userId, dtu.getStartDatelong(), dtu.getEndDatelong());

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (UserAccountLogsModel userAccountLogsModel : userAccountLogsModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userAccountLogsModel.getUserAccountLogId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(userAccountLogsModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(userAccountLogsModel.getRemark());

			if (userAccountLogsModel.getCreditedAmount() == 0) {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			} else {
				dtuInnerJsonArray.put(StringUtils.valueOf(userAccountLogsModel.getCreditedAmount()));
			}

			if (userAccountLogsModel.getDebitedAmount() == 0) {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			} else {
				dtuInnerJsonArray.put(StringUtils.valueOf(userAccountLogsModel.getDebitedAmount()));
			}

			//@formatter:off
			if (ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT.equalsIgnoreCase(userAccountLogsModel.getTransactionStatus()) 
					|| ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(userAccountLogsModel.getTransactionStatus())
					|| ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(userAccountLogsModel.getTransactionStatus())) {
			//@formatter:on
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			} else {
				dtuInnerJsonArray.put(userAccountLogsModel.getTransactionStatus());
			}

			dtuInnerJsonArray.put(userAccountLogsModel.getTransactionType());
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountLogsModel.getCurrentBalance()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountLogsModel.getHoldBalance()));
			dtuInnerJsonArray.put(StringUtils.valueOf(userAccountLogsModel.getApprovedBalance()));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = UserAccountLogsModel.getFilteredUserAccountLogsCount(dtu.getGlobalSearchStringWithPercentage(), userId, dtu.getStartDatelong(), dtu.getEndDatelong());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}