package com.webapp.actions.secure.encashrequests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.EncashRequestsModel;
import com.webapp.models.UserAccountLogDetailsModel;
import com.webapp.models.UserAccountLogsModel;
import com.webapp.models.UserAccountModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/encash-requests/approved")
public class ManageApprovedEncashRequestAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadApprovedEncashRequests(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPROVED_ENCASH_REQUEST_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_APPROVED_ENCASH_REQUEST_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_APPROVED_ENCASH_REQUEST_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadApprovedEncashRequestsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPROVED_ENCASH_REQUEST_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		int total = EncashRequestsModel.getTotalCountOfEncashRequestForSearchByStatus(ProjectConstants.ENCASH_REQUEST_STATUS_APPROVED, dtu.getStartDatelong(), dtu.getEndDatelong());

		List<EncashRequestsModel> encashRequestsModelList = EncashRequestsModel.getEncashRequestForSearchByStatus(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), ProjectConstants.ENCASH_REQUEST_STATUS_APPROVED,
					dtu.getStartDatelong(), dtu.getEndDatelong());

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (EncashRequestsModel encashRequestsModel : encashRequestsModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(encashRequestsModel.getEncashRequestId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(encashRequestsModel.getFirstName());
			dtuInnerJsonArray.put(encashRequestsModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(encashRequestsModel.getPhoneNoCode(), encashRequestsModel.getPhoneNo()));
			dtuInnerJsonArray.put(StringUtils.valueOf(encashRequestsModel.getRequestedAmount()));
			dtuInnerJsonArray.put(StringUtils.valueOf(encashRequestsModel.getCurrentBalance()));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(encashRequestsModel.getApprovedDate(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if (StringUtils.validString(encashRequestsModel.getRemark())) {
				dtuInnerJsonArray.put(encashRequestsModel.getRemark());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, encashRequestsModel.getStatus()));

			btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, encashRequestsModel.getEncashRequestId(), "transfer"));
			btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, encashRequestsModel.getEncashRequestId(), "reject"));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = EncashRequestsModel.getFilteredCountOfEncashRequestForSearchByStatus(ProjectConstants.ENCASH_REQUEST_STATUS_APPROVED, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@GET
	@Path("/transfer")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response transferRequest(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("encashRequestIds") String encashRequestIds,
		@QueryParam("requestStatus") String requestStatus,
		@QueryParam("approveRejectRemark") String approveRejectRemark
		) throws IOException, ServletException {
	//@formatter:on		

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPROVED_ENCASH_REQUEST_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		Map<String, Object> output = new HashMap<String, Object>();

		if (!StringUtils.validString(requestStatus)) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorFaildToTransferEncashRequest"));
			return sendDataResponse(output);
		}

		if (!StringUtils.validString(encashRequestIds)) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorFaildToTransferEncashRequest"));
			return sendDataResponse(output);
		}

		String[] encashRequestIdArray = MyHubUtils.splitStringByCommaArray(encashRequestIds);

		String transferEncashRequestRemark = messageForKeyAdmin("remarkTransferredEncashRequest");
		String rejectEncashRequestRemark = String.format(messageForKeyAdmin("remarkRejectRequest"), loggedInUserModelViaSession.getFullName());
		String creditEncashRequestRemark = messageForKeyAdmin("remarkCreditAfterRejectRequest");

		if ((approveRejectRemark != null) && (!"".equals(approveRejectRemark.trim())) && (!"undefined".equalsIgnoreCase(approveRejectRemark.trim()))) {
			transferEncashRequestRemark = approveRejectRemark;
			rejectEncashRequestRemark = approveRejectRemark;
		}

		for (String encashRequestId : encashRequestIdArray) {

			EncashRequestsModel encashRequestModel = EncashRequestsModel.getEncashRequestDetailsById(encashRequestId);

			if (encashRequestModel != null) {

				UserAccountModel userPreviousAccountDetails = UserAccountModel.getAccountBalanceDetailsByUserId(encashRequestModel.getUserId());

				if (userPreviousAccountDetails != null) {

					if (ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(requestStatus)) {

						UserAccountModel userAccountModel = new UserAccountModel();
						userAccountModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
						userAccountModel.setUserId(encashRequestModel.getUserId());
						userAccountModel.setCurrentBalance(userPreviousAccountDetails.getCurrentBalance());
						userAccountModel.setHoldBalance(userPreviousAccountDetails.getHoldBalance());
						userAccountModel.setApprovedBalance(userPreviousAccountDetails.getApprovedBalance() - encashRequestModel.getRequestedAmount());
						userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());
						userAccountModel.updateAccountBalanceDetails(loginSessionMap.get(LoginUtils.USER_ID));

						encashRequestModel.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED);
						encashRequestModel.setTransferDate(DateUtils.nowAsGmtMillisec());
						encashRequestModel.updateEncashRequestStatus(loginSessionMap.get(LoginUtils.USER_ID));

						UserAccountLogsModel previousUserAccountLog = UserAccountLogsModel.getUserAccountLogDetailsByEncashRequestId(encashRequestId);

						if (previousUserAccountLog != null) {

							UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
							userAccountLogsModel.setDescription(transferEncashRequestRemark);
							userAccountLogsModel.setRemark(transferEncashRequestRemark);
							userAccountLogsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED);
							userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
							userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
							userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
							userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());
							userAccountLogsModel.setEncashRequestId(encashRequestId);

							int updateStatus = userAccountLogsModel.updateUserAccountLogByEncashRequestId(loginSessionMap.get(LoginUtils.USER_ID));

							if (updateStatus > 0) {

								UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
								userAccountLogDetailsModel.setUserAccountLogId(previousUserAccountLog.getUserAccountLogId());
								userAccountLogDetailsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
								userAccountLogDetailsModel.setUserId(encashRequestModel.getUserId());
								userAccountLogDetailsModel.setDescription(transferEncashRequestRemark);
								userAccountLogDetailsModel.setRemark(transferEncashRequestRemark);
								userAccountLogDetailsModel.setCreditedAmount(0);
								userAccountLogDetailsModel.setDebitedAmount(encashRequestModel.getRequestedAmount());
								userAccountLogDetailsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
								userAccountLogDetailsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED);
								userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
								userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
								userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
								userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

								userAccountLogDetailsModel.insertUserAccountLogDetails(loginSessionMap.get(LoginUtils.USER_ID));
							}
						}

					} else {

						UserAccountModel userAccountModel = new UserAccountModel();
						userAccountModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
						userAccountModel.setUserId(encashRequestModel.getUserId());
						userAccountModel.setCurrentBalance(userPreviousAccountDetails.getCurrentBalance() + encashRequestModel.getRequestedAmount());
						userAccountModel.setHoldBalance(userPreviousAccountDetails.getHoldBalance());
						userAccountModel.setApprovedBalance(userPreviousAccountDetails.getApprovedBalance() - encashRequestModel.getRequestedAmount());
						userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());
						userAccountModel.updateAccountBalanceDetails(loginSessionMap.get(LoginUtils.USER_ID));

						encashRequestModel.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED);
						encashRequestModel.setRejectedDate(DateUtils.nowAsGmtMillisec());
						encashRequestModel.updateEncashRequestStatus(loginSessionMap.get(LoginUtils.USER_ID));

						UserAccountLogsModel previousUserAccountLog = UserAccountLogsModel.getUserAccountLogDetailsByEncashRequestId(encashRequestId);

						if (previousUserAccountLog != null) {

							UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
							userAccountLogsModel.setDescription(rejectEncashRequestRemark);
							userAccountLogsModel.setRemark(rejectEncashRequestRemark);
							userAccountLogsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED);
							userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
							userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
							userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
							userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());
							userAccountLogsModel.setEncashRequestId(encashRequestId);

							int updateStatus = userAccountLogsModel.updateUserAccountLogByEncashRequestId(loginSessionMap.get(LoginUtils.USER_ID));

							if (updateStatus > 0) {

								UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
								userAccountLogDetailsModel.setUserAccountLogId(previousUserAccountLog.getUserAccountLogId());
								userAccountLogDetailsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
								userAccountLogDetailsModel.setUserId(encashRequestModel.getUserId());
								userAccountLogDetailsModel.setDescription(rejectEncashRequestRemark);
								userAccountLogDetailsModel.setRemark(rejectEncashRequestRemark);
								userAccountLogDetailsModel.setCreditedAmount(0);
								userAccountLogDetailsModel.setDebitedAmount(encashRequestModel.getRequestedAmount());
								userAccountLogDetailsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
								userAccountLogDetailsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED);
								userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
								userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
								userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
								userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

								userAccountLogDetailsModel.insertUserAccountLogDetails(loginSessionMap.get(LoginUtils.USER_ID));
							}
						}

						UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
						userAccountLogsModel.setUserAccountId(previousUserAccountLog.getUserAccountLogId());
						userAccountLogsModel.setUserId(encashRequestModel.getUserId());
						userAccountLogsModel.setDescription(creditEncashRequestRemark);
						userAccountLogsModel.setRemark(creditEncashRequestRemark);
						userAccountLogsModel.setCreditedAmount(encashRequestModel.getRequestedAmount());
						userAccountLogsModel.setDebitedAmount(0);
						userAccountLogsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT);
						userAccountLogsModel.setTransactionStatus(ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT);
						userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
						userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
						userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
						userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());
						userAccountLogsModel.setTransactionBy(loginSessionMap.get(LoginUtils.USER_ID));
						userAccountLogsModel.setAccountRecharge(false);

						String userAccountLogId = userAccountLogsModel.insertUserAccountLog(loginSessionMap.get(LoginUtils.USER_ID));

						if (userAccountLogId != null) {

							UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
							userAccountLogDetailsModel.setUserAccountLogId(userAccountLogId);
							userAccountLogDetailsModel.setUserAccountId(previousUserAccountLog.getUserAccountLogId());
							userAccountLogDetailsModel.setUserId(encashRequestModel.getUserId());
							userAccountLogDetailsModel.setDescription(creditEncashRequestRemark);
							userAccountLogDetailsModel.setRemark(creditEncashRequestRemark);
							userAccountLogDetailsModel.setCreditedAmount(encashRequestModel.getRequestedAmount());
							userAccountLogDetailsModel.setDebitedAmount(0);
							userAccountLogDetailsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT);
							userAccountLogDetailsModel.setTransactionStatus(ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT);
							userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
							userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
							userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
							userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

							userAccountLogDetailsModel.insertUserAccountLogDetails(loginSessionMap.get(LoginUtils.USER_ID));
						}
					}
				}
			}
		}

		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		if (ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(requestStatus)) {
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successEncashRequestTransferredSuccessfully"));
		} else {
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successEncashRequestRejectedSuccessfully"));
		}

		return sendDataResponse(output);
	}

	@GET
	@Path("/details")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getEncashRequestDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("encashRequestId") String encashRequestId
		) throws IOException, SQLException, ServletException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPROVED_ENCASH_REQUEST_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		EncashRequestsModel encashRequestModel = EncashRequestsModel.getEncashRequestDetailsById(encashRequestId);

		Map<String, Object> outPutMap = new HashMap<>();

		if (encashRequestModel == null) {
			outPutMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outPutMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorUserNotFound"));
			return sendDataResponse(outPutMap);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		outPutMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outPutMap.put(FieldConstants.FIRST_NAME, MyHubUtils.formatFullName(encashRequestModel.getFirstName(), encashRequestModel.getLastName()));
		outPutMap.put(FieldConstants.EMAIL, encashRequestModel.getEmail());
		outPutMap.put(FieldConstants.PHONE_NUMBER, MyHubUtils.formatPhoneNumber(encashRequestModel.getPhoneNoCode(), encashRequestModel.getPhoneNo()));
		outPutMap.put(FieldConstants.REQUESTED_AMOUNT, adminSettings.getCurrencySymbol() + StringUtils.valueOf(encashRequestModel.getRequestedAmount()));
		outPutMap.put(FieldConstants.REMAINING_BALANCE, adminSettings.getCurrencySymbol() + StringUtils.valueOf(encashRequestModel.getCurrentBalance()));

		return sendDataResponse(outPutMap);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_APPROVED_ENCASH_REQUEST_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}