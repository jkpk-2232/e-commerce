package com.webapp.actions.secure.vendor.account;

import java.io.IOException;
import java.util.Arrays;
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
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.EncashRequestsModel;
import com.webapp.models.UserAccountLogDetailsModel;
import com.webapp.models.UserAccountLogsModel;
import com.webapp.models.UserAccountModel;

@Path("/vendor/my-account")
public class VendorMyAccountAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadVendorAccount(
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

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		UserAccountModel userAccountModel = UserAccountModel.getUserWithAccountDetails(loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.USER_ID, loginSessionMap.get(LoginUtils.USER_ID));
		data.put(FieldConstants.FIRST_NAME, MyHubUtils.formatFullName(userAccountModel.getFirstName(), userAccountModel.getLastName()));
		data.put(FieldConstants.EMAIL_ADDRESS, userAccountModel.getEmail());
		data.put(FieldConstants.PHONE_NUMBER, MyHubUtils.formatPhoneNumber(userAccountModel.getPhoneNoCode(), userAccountModel.getPhoneNo()));
		data.put(FieldConstants.CURRENT_BALANCE, adminSettings.getCurrencySymbol() + StringUtils.valueOf(userAccountModel.getCurrentBalance()));

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.VENDOR_MY_ACCOUNT_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_MY_ACCOUNT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorAccountLogsList(
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
			btnGroupStr = new StringBuilder();

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

	@POST
	@Path("/encash")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response sendEncashRequest(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam("requestedAmount") String requestedAmount,
		@FormParam("encashRemark") String encashRemark
		) throws ServletException, IOException{
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

		if (hasErrorsForEnglish(encashRemark)) {
			return sendDataResponse(data);
		}

		UserAccountModel userPreviousAccountDetails = UserAccountModel.getAccountBalanceDetailsByUserId(loginSessionMap.get(LoginUtils.USER_ID));

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

		String holdEncashRequestRemark = messageForKeyAdmin("remarkHoldEncashRequest");

		if ((encashRemark != null) && (!"".equals(encashRemark.trim()))) {
			holdEncashRequestRemark = encashRemark;
		}

		EncashRequestsModel encashRequest = new EncashRequestsModel();
		encashRequest.setUserId(loginSessionMap.get(LoginUtils.USER_ID));
		encashRequest.setRequestedAmount(rAmount);
		encashRequest.setRequestedDate(DateUtils.nowAsGmtMillisec());
		encashRequest.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
		encashRequest.setHoldDate(encashRequest.getRequestedDate());
		encashRequest.setApprovedDate(0);
		encashRequest.setRejectedDate(0);
		encashRequest.setTransferDate(0);

		String encashRequestId = encashRequest.addEncashRequest(loginSessionMap.get(LoginUtils.USER_ID));

		UserAccountModel userAccountModel = new UserAccountModel();
		userAccountModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
		userAccountModel.setUserId(loginSessionMap.get(LoginUtils.USER_ID));
		userAccountModel.setCurrentBalance(userPreviousAccountDetails.getCurrentBalance() - rAmount);
		userAccountModel.setHoldBalance(userPreviousAccountDetails.getHoldBalance() + rAmount);
		userAccountModel.setApprovedBalance(userPreviousAccountDetails.getApprovedBalance());
		userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());

		userAccountModel.updateAccountBalanceDetails(loginSessionMap.get(LoginUtils.USER_ID));

		UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
		userAccountLogsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
		userAccountLogsModel.setUserId(loginSessionMap.get(LoginUtils.USER_ID));
		userAccountLogsModel.setEncashRequestId(encashRequestId);
		userAccountLogsModel.setDescription(holdEncashRequestRemark);
		userAccountLogsModel.setRemark(holdEncashRequestRemark);
		userAccountLogsModel.setCreditedAmount(0);
		userAccountLogsModel.setDebitedAmount(rAmount);
		userAccountLogsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
		userAccountLogsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
		userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
		userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
		userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
		userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());

		String userAccountLogId = userAccountLogsModel.insertUserAccountLog(loginSessionMap.get(LoginUtils.USER_ID));

		if (userAccountLogId != null) {

			UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
			userAccountLogDetailsModel.setUserAccountLogId(userAccountLogId);
			userAccountLogDetailsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
			userAccountLogDetailsModel.setUserId(loginSessionMap.get(LoginUtils.USER_ID));
			userAccountLogDetailsModel.setDescription(holdEncashRequestRemark);
			userAccountLogDetailsModel.setRemark(holdEncashRequestRemark);
			userAccountLogDetailsModel.setCreditedAmount(0);
			userAccountLogDetailsModel.setDebitedAmount(rAmount);
			userAccountLogDetailsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
			userAccountLogDetailsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
			userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
			userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
			userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
			userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

			userAccountLogDetailsModel.insertUserAccountLogDetails(loginSessionMap.get(LoginUtils.USER_ID));
		}

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		data.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successEncashRequestAccepted"));

		return sendDataResponse(data);
	}

	public boolean hasErrorsForEnglish(String remark) {

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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_MY_ACCOUNT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}