package com.webapp.actions.secure.driver.account;

import java.io.IOException;
import java.sql.SQLException;
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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.DriverVendorsModel;
import com.webapp.models.EncashRequestsModel;
import com.webapp.models.UserAccountLogDetailsModel;
import com.webapp.models.UserAccountLogsModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserProfileModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-drivers/account")
public class ManageDriverAccountAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadDriverAccountList(
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

		String encashRequestStatusOptions = DropDownUtils.getEncashRequestStatusOptions(ProjectConstants.ENCASH_REQUEST_SEND_PENDING_FOR_APPROVAL_ID);
		data.put(FieldConstants.ENCASH_REQUEST_STATUS_OPTIONS, encashRequestStatusOptions);

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_DRIVER_ACCOUNT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_ACCOUNT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverAccountList(
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

		int total = UserAccountModel.getTotalUserAccountCount(UserRoles.DRIVER_ROLE_ID, assignedRegionList);
		List<UserAccountModel> userAccountModelList = UserAccountModel.getUserAccountListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), assignedRegionList, UserRoles.DRIVER_ROLE_ID);

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

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelView"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_URL + "?userId=" + userAccountModel.getUserId() + "&frm=drac"),
						UrlConstants.JSP_URLS.MANAGE_DRIVER_ACCOUNT_LOGS_ICON));

			btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, userAccountModel.getUserId(), "recharge"));

			btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, userAccountModel.getUserId(), "encash"));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = UserAccountModel.getFilteredUserAccountCount(dtu.getGlobalSearchStringWithPercentage(), assignedRegionList, UserRoles.DRIVER_ROLE_ID);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@GET
	@Path("/details")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.USER_ID) String userId
		) throws IOException, SQLException, ServletException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasAccountsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		Map<String, Object> outPutMap = new HashMap<>();

		UserAccountModel userAccountModel = UserAccountModel.getUserWithAccountDetails(userId.trim());

		if (userAccountModel == null) {
			outPutMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outPutMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorUserNotFound"));
			return sendDataResponse(outPutMap);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		outPutMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outPutMap.put("name", userAccountModel.getFirstName());
		outPutMap.put("emailAddress", userAccountModel.getEmail());
		outPutMap.put("phoneNumber", MyHubUtils.formatFullName(userAccountModel.getPhoneNoCode(), userAccountModel.getPhoneNo()));
		outPutMap.put("currentBalance", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getCurrentBalance()));
		outPutMap.put("holdBalance", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getHoldBalance()));
		outPutMap.put("approvedBalance", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getApprovedBalance()));
		outPutMap.put("totalBalance", adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getTotalBalance()));

		DriverVendorsModel driverVendorsModel = DriverVendorsModel.getDriverVendorDetailsByDriverId(userId.trim());

		if (driverVendorsModel != null && !UserRoleUtils.isSuperAdminAndAdminRole(driverVendorsModel.getVendorRoleId())) {
			outPutMap.put(FieldConstants.VENDOR_NAME, driverVendorsModel.getVendorName());
		} else {
			outPutMap.put(FieldConstants.VENDOR_NAME, messageForKeyAdmin("labelAdmin"));
		}

		return sendDataResponse(outPutMap);
	}

	@POST
	@Path("/recharge")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response rechargeDriverAccount(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.USER_ID) String userId,
		@FormParam(FieldConstants.RECHARGE_AMOUNT) String rechargeAmount
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasAccountsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.RECHARGE_AMOUNT, rechargeAmount);

		if (hasErrorsForEnglish()) {
			return sendDataResponse(data);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		double amount = Double.parseDouble(rechargeAmount);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			UserAccountModel loggedInUserAccountModel = UserAccountModel.getAccountBalanceDetailsByUserId(loginSessionMap.get(LoginUtils.USER_ID));

			if (loggedInUserModelViaSession.isVendorDriverSubscriptionAppliedInBookingFlow()) {
				// if driver subscription flag is on, then skip the balance check for recharging
				// driver.
			} else {
				if (loggedInUserAccountModel == null || loggedInUserAccountModel.getCurrentBalance() < amount) {
					data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
					data.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorVendorNotHaveSufficientBalance"));
					return sendDataResponse(data);
				}
			}
		}

		String remarkRechargeByAdminVendor;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			remarkRechargeByAdminVendor = String.format(messageForKeyAdmin("remarkRechargeByAdminVendor3"), loggedInUserModelViaSession.getFirstName());
		} else {
			remarkRechargeByAdminVendor = String.format(messageForKeyAdmin("remarkRechargeByAdminVendor1"), loggedInUserModelViaSession.getFirstName());
		}

		UserAccountModel.updateUserAccountAndCreateLog(userId, amount, ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT, remarkRechargeByAdminVendor, remarkRechargeByAdminVendor, loginSessionMap.get(LoginUtils.USER_ID), true);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			String vTransactionType = ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT;
			String vDescription = "Driver Account recharge.";
			String vRemark = "Driver account recharge.";
			String vTransactionBy = loginSessionMap.get(LoginUtils.USER_ID);
			boolean vIsAccountRecharge = false;

			UserAccountModel.updateUserAccountAndCreateLog(loginSessionMap.get(LoginUtils.USER_ID), amount, vTransactionType, vDescription, vRemark, vTransactionBy, vIsAccountRecharge);
		}

		String notificationMessage = String.format(messageForKeyAdmin("pushMessageAccountRecharge1"), adminSettings.getCurrencySymbol(), StringUtils.valueOf(amount));
		MyHubNotificationUtils.sendPushNotificationToUser(userId, notificationMessage);

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(userId);
		String successAccountRechargeMessage = String.format(messageForKeyAdmin("successAccountRecharge1"), userProfileModel.getFirstName(), adminSettings.getCurrencySymbol(), StringUtils.valueOf(amount));

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		data.put(ProjectConstants.STATUS_MESSAGE, successAccountRechargeMessage);

		return sendDataResponse(data);
	}

	@POST
	@Path("/encash")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response sendEncashRequest(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.USER_ID) String userId,
		@FormParam("encashRequestStatus") String encashRequestStatus,
		@FormParam("requestedAmount") String requestedAmount,
		@FormParam("encashRemark") String encashRemark
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasAccountsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put("requestedAmount", requestedAmount);
		data.put("encashRemark", encashRemark);
		data.put("encashRequestStatus", encashRequestStatus);

		if (hasErrorsForEnglishForEncash(encashRemark)) {
			return sendDataResponse(data);
		}

		//@formatter:off
		if((encashRequestStatus != null) && ((ProjectConstants.ENCASH_REQUEST_SEND_PENDING_FOR_APPROVAL_ID.equals(encashRequestStatus)) 
				|| (ProjectConstants.ENCASH_REQUEST_SEND_PENDING_FOR_TRANSFER_ID.equals(encashRequestStatus)) 
				|| (ProjectConstants.ENCASH_REQUEST_SEND_DIRECT_TRANSFER_ID.equals(encashRequestStatus)))) {
		//@formatter:on

		} else {

			data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			data.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorInvalidEncashRequest"));
			return sendDataResponse(data);
		}

		UserAccountModel userPreviousAccountDetails = UserAccountModel.getAccountBalanceDetailsByUserId(userId);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		double rAmount = StringUtils.doubleValueOf(requestedAmount);

		if ((userPreviousAccountDetails != null) && (rAmount > 0)) {

			if (rAmount > userPreviousAccountDetails.getCurrentBalance()) {

				data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
				data.put(ProjectConstants.STATUS_MESSAGE, String.format(messageForKeyAdmin("errorEncashRequestAmountNotGreater"), adminSettingsModel.getCurrencySymbol(), userPreviousAccountDetails.getCurrentBalance()));
				return sendDataResponse(data);
			}

		} else {

			data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			data.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorInvalidEncashRequest"));
			return sendDataResponse(data);
		}

		// Add encash request
		EncashRequestsModel encashRequest = new EncashRequestsModel();
		encashRequest.setUserId(userId);
		encashRequest.setRequestedAmount(rAmount);
		encashRequest.setRequestedDate(DateUtils.nowAsGmtMillisec());
		encashRequest.setHoldDate(0);
		encashRequest.setApprovedDate(0);
		encashRequest.setRejectedDate(0);
		encashRequest.setTransferDate(0);

		UserAccountModel userAccountModel = new UserAccountModel();
		userAccountModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
		userAccountModel.setUserId(userId);
		userAccountModel.setCurrentBalance(userPreviousAccountDetails.getCurrentBalance() - rAmount);
		userAccountModel.setHoldBalance(userPreviousAccountDetails.getHoldBalance());
		userAccountModel.setApprovedBalance(userPreviousAccountDetails.getApprovedBalance());

		String logTransactionStatus = ProjectConstants.ENCASH_REQUEST_STATUS_HOLD;
		String holdEncashRequestRemark = "";
		String successEncashRequestAccepted = "";

		if (ProjectConstants.ENCASH_REQUEST_SEND_PENDING_FOR_TRANSFER_ID.equals(encashRequestStatus)) {

			encashRequest.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_APPROVED);
			encashRequest.setApprovedDate(encashRequest.getRequestedDate());

			userAccountModel.setApprovedBalance(userPreviousAccountDetails.getApprovedBalance() + rAmount);

			logTransactionStatus = ProjectConstants.ENCASH_REQUEST_STATUS_APPROVED;

			holdEncashRequestRemark = messageForKeyAdmin("remarkApproveEncashRequest");

			successEncashRequestAccepted = messageForKeyAdmin("successEncashRequestApprovedSuccessfully");

		} else if (ProjectConstants.ENCASH_REQUEST_SEND_DIRECT_TRANSFER_ID.equals(encashRequestStatus)) {

			encashRequest.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED);
			encashRequest.setTransferDate(encashRequest.getRequestedDate());

			logTransactionStatus = ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED;

			holdEncashRequestRemark = messageForKeyAdmin("remarkTransferredEncashRequest");

			successEncashRequestAccepted = messageForKeyAdmin("successEncashRequestTransferredSuccessfully");

		} else {

			encashRequest.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
			encashRequest.setHoldDate(encashRequest.getRequestedDate());

			userAccountModel.setHoldBalance(userPreviousAccountDetails.getHoldBalance() + rAmount);

			logTransactionStatus = ProjectConstants.ENCASH_REQUEST_STATUS_HOLD;

			holdEncashRequestRemark = messageForKeyAdmin("remarkHoldEncashRequest");

			successEncashRequestAccepted = messageForKeyAdmin("successEncashRequestAccepted");
		}

		if (StringUtils.validString(encashRemark)) {
			holdEncashRequestRemark = encashRemark;
		}

		String encashRequestId = encashRequest.addEncashRequest(loginSessionMap.get(LoginUtils.USER_ID));

		userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());
		userAccountModel.updateAccountBalanceDetails(loginSessionMap.get(LoginUtils.USER_ID));

		// Add account log
		UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
		userAccountLogsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
		userAccountLogsModel.setUserId(userId);
		userAccountLogsModel.setEncashRequestId(encashRequestId);
		userAccountLogsModel.setDescription(holdEncashRequestRemark);
		userAccountLogsModel.setRemark(holdEncashRequestRemark);
		userAccountLogsModel.setCreditedAmount(0);
		userAccountLogsModel.setDebitedAmount(rAmount);
		userAccountLogsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
		userAccountLogsModel.setTransactionStatus(logTransactionStatus);
		userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
		userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
		userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
		userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());

		String userAccountLogId = userAccountLogsModel.insertUserAccountLog(loginSessionMap.get(LoginUtils.USER_ID));

		if (userAccountLogId != null) {

			// Add account log details
			UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
			userAccountLogDetailsModel.setUserAccountLogId(userAccountLogId);
			userAccountLogDetailsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
			userAccountLogDetailsModel.setUserId(userId);
			userAccountLogDetailsModel.setDescription(holdEncashRequestRemark);
			userAccountLogDetailsModel.setRemark(holdEncashRequestRemark);
			userAccountLogDetailsModel.setCreditedAmount(0);
			userAccountLogDetailsModel.setDebitedAmount(rAmount);
			userAccountLogDetailsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
			userAccountLogDetailsModel.setTransactionStatus(logTransactionStatus);
			userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
			userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
			userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
			userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

			userAccountLogDetailsModel.insertUserAccountLogDetails(loginSessionMap.get(LoginUtils.USER_ID));
		}

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		data.put(ProjectConstants.STATUS_MESSAGE, successEncashRequestAccepted);

		return sendDataResponse(data);
	}

	public boolean hasErrorsForEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping("rechargeAmount", messageForKeyAdmin("labelAmount"), new RequiredValidationRule());
		validator.addValidationMapping("rechargeAmount", messageForKeyAdmin("labelAmount"), new MinMaxValueValidationRule(1, 1000000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	public boolean hasErrorsForEnglishForEncash(String remark) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping("requestedAmount", messageForKeyAdmin("labelRequestedAmount"), new RequiredValidationRule());
		validator.addValidationMapping("requestedAmount", messageForKeyAdmin("labelRequestedAmount"), new MinMaxValueValidationRule(1, 1000000));

		if (StringUtils.validString(remark)) {
			validator.addValidationMapping("encashRemark", messageForKeyAdmin("labelRemark"), new MinMaxLengthValidationRule(1, 20));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_ACCOUNT_JS, UrlConstants.JS_URLS.DRIVER_VENDOR_ACCOUNT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}