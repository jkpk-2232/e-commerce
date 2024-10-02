package com.webapp.actions.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.EncashRequestsModel;
import com.webapp.models.UserAccountLogDetailsModel;
import com.webapp.models.UserAccountLogsModel;
import com.webapp.models.UserAccountModel;

@Path("/api/driver/account")
public class DriverAccountAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	// @formatter:off
	public Response getDriverAccountDetails(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@HeaderParam("x-language-code") String lang, 
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@QueryParam("offset") int offset,
			@QueryParam("length") int length
			) throws IOException {
	// @formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		if (offset <= 0) {
			offset = 0;
		}

		if (length <= 0) {
			length = ProjectConstants.LIST_LIMIT;
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		Map<String, Object> accountDetails = new HashMap<String, Object>();

		UserAccountModel userAccountModel = UserAccountModel.getAccountBalanceDetailsByUserId(loggedInUserId);

		if (userAccountModel != null) {
			accountDetails.put("currentBalance", userAccountModel.getCurrentBalance());
		} else {
			accountDetails.put("currentBalance", 0);
		}

		List<Map<String, Object>> accountLogsList = new ArrayList<>();

		List<UserAccountLogsModel> userAccountLogsModelList = UserAccountLogsModel.getUserAccountLogsListByUserId(loggedInUserId, offset, length);

		if (userAccountLogsModelList != null) {

			for (UserAccountLogsModel userAccountLogsModel : userAccountLogsModelList) {

				Map<String, Object> userAccountLogsMap = new HashMap<String, Object>();
				userAccountLogsMap.put("userAccountLogId", userAccountLogsModel.getUserAccountLogId());
				userAccountLogsMap.put("description", userAccountLogsModel.getDescription());
				userAccountLogsMap.put("remark", userAccountLogsModel.getRemark());
				userAccountLogsMap.put("creditedAmount", Double.parseDouble(df.format(userAccountLogsModel.getCreditedAmount())));
				userAccountLogsMap.put("debitedAmount", Double.parseDouble(df.format(userAccountLogsModel.getDebitedAmount())));
				userAccountLogsMap.put("transactionType", userAccountLogsModel.getTransactionType());
				userAccountLogsMap.put("transactionStatus", userAccountLogsModel.getTransactionStatus());
				userAccountLogsMap.put("currentBalance", Double.parseDouble(df.format(userAccountLogsModel.getCurrentBalance())));
				userAccountLogsMap.put("holdBalance", Double.parseDouble(df.format(userAccountLogsModel.getHoldBalance())));
				userAccountLogsMap.put("approvedBalance", Double.parseDouble(df.format(userAccountLogsModel.getApprovedBalance())));
				userAccountLogsMap.put("totalBalance", Double.parseDouble(df.format(userAccountLogsModel.getTotalBalance())));
				userAccountLogsMap.put("date", userAccountLogsModel.getCreatedAt());

				accountLogsList.add(userAccountLogsMap);
			}
		}

		outPutMap.put("accountDetails", accountDetails);
		outPutMap.put("accountLogsList", accountLogsList);

		return sendDataResponse(outPutMap);
	}

	@Path("/encash")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response userRegistration(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			EncashRequestsModel encashRequestsModel
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		UserAccountModel userPreviousAccountDetails = UserAccountModel.getAccountBalanceDetailsByUserId(loggedInUserId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		if ((userPreviousAccountDetails != null) && (encashRequestsModel.getRequestedAmount() > 0)) {

			if (encashRequestsModel.getRequestedAmount() > userPreviousAccountDetails.getCurrentBalance()) {
				return sendBussinessError(messageForKey("errorEncashRequestAmountNotGreater", request) + " " + adminSettingsModel.getCurrencySymbol() + "" + userPreviousAccountDetails.getCurrentBalance() + "/-");
			}

		} else {

			return sendBussinessError(messageForKey("errorInvalidEncashRequest", request));
		}

		String holdEncashRequestRemark = messageForKey("remarkHoldEncashRequest", request);

		if ((encashRequestsModel.getRemark() != null) && (!"".equals(encashRequestsModel.getRemark().trim()))) {
			holdEncashRequestRemark = encashRequestsModel.getRemark();
		}

		//Add encash request
		EncashRequestsModel encashRequest = new EncashRequestsModel();
		encashRequest.setUserId(loggedInUserId);
		encashRequest.setRequestedAmount(encashRequestsModel.getRequestedAmount());
		encashRequest.setRequestedDate(DateUtils.nowAsGmtMillisec());
		encashRequest.setStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
		encashRequest.setHoldDate(encashRequest.getRequestedDate());
		encashRequest.setApprovedDate(0);
		encashRequest.setRejectedDate(0);
		encashRequest.setTransferDate(0);

		String encashRequestId = encashRequest.addEncashRequest(loggedInUserId);

		//Update account
		UserAccountModel userAccountModel = new UserAccountModel();
		userAccountModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
		userAccountModel.setUserId(loggedInUserId);
		userAccountModel.setCurrentBalance(userPreviousAccountDetails.getCurrentBalance() - encashRequestsModel.getRequestedAmount());
		userAccountModel.setHoldBalance(userPreviousAccountDetails.getHoldBalance() + encashRequestsModel.getRequestedAmount());
		userAccountModel.setApprovedBalance(userPreviousAccountDetails.getApprovedBalance());
		userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());

		userAccountModel.updateAccountBalanceDetails(loggedInUserId);

		//Add account log
		UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
		userAccountLogsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
		userAccountLogsModel.setUserId(loggedInUserId);
		userAccountLogsModel.setEncashRequestId(encashRequestId);
		userAccountLogsModel.setDescription(holdEncashRequestRemark);
		userAccountLogsModel.setRemark(holdEncashRequestRemark);
		userAccountLogsModel.setCreditedAmount(0);
		userAccountLogsModel.setDebitedAmount(encashRequestsModel.getRequestedAmount());
		userAccountLogsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
		userAccountLogsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
		userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
		userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
		userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
		userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());

		String userAccountLogId = userAccountLogsModel.insertUserAccountLog(loggedInUserId);

		if (userAccountLogId != null) {

			//Add account log details
			UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
			userAccountLogDetailsModel.setUserAccountLogId(userAccountLogId);
			userAccountLogDetailsModel.setUserAccountId(userPreviousAccountDetails.getUserAccountId());
			userAccountLogDetailsModel.setUserId(loggedInUserId);
			userAccountLogDetailsModel.setDescription(holdEncashRequestRemark);
			userAccountLogDetailsModel.setRemark(holdEncashRequestRemark);
			userAccountLogDetailsModel.setCreditedAmount(0);
			userAccountLogDetailsModel.setDebitedAmount(encashRequestsModel.getRequestedAmount());
			userAccountLogDetailsModel.setTransactionType(ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT);
			userAccountLogDetailsModel.setTransactionStatus(ProjectConstants.ENCASH_REQUEST_STATUS_HOLD);
			userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
			userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
			userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
			userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

			userAccountLogDetailsModel.insertUserAccountLogDetails(loggedInUserId);
		}

		//Check account balance and send push message.
		UserAccountModel userAccount = UserAccountModel.getAccountBalanceDetailsByUserId(loggedInUserId.trim());

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		if (userAccount.getCurrentBalance() <= driverWalletSettingsModel.getNotifyAmount()) {

			//@formatter:off
			String notifyAlert1 = messageForKey("driverWalletNotifyAlert11", request)  + " " + df.format(userAccount.getCurrentBalance()) 
					+ messageForKey("driverWalletNotifyAlert12", request) + " " + df.format(driverWalletSettingsModel.getMinimumAmount()) + ".";
			
			String notifyAlert2 = messageForKey("driverWalletNotifyAlert21", request) + " " + df.format(userAccount.getCurrentBalance())
					+ messageForKey("driverWalletNotifyAlert22", request)  + " " + df.format(driverWalletSettingsModel.getMinimumAmount()) 
					+ messageForKey("driverWalletNotifyAlert23", request);
			//@formatter:on

			String pushMessage = "";

			if (userAccount.getCurrentBalance() <= driverWalletSettingsModel.getMinimumAmount()) {
				pushMessage = notifyAlert2;
			} else if (userAccount.getCurrentBalance() <= driverWalletSettingsModel.getNotifyAmount()) {
				pushMessage = notifyAlert1;
			}

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(loggedInUserId);

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(pushMessage);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(loggedInUserId);
			apnsMessage.insertPushMessage();

			if (apnsDevice != null) {
				apnsDevice.sendNotification("1", "Push", pushMessage, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}
		}

		return sendSuccessMessage(messageForKey("successEncashRequestAccepted", request));
	}

}