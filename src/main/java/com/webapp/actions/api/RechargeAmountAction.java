package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.RechargeAmountModel;
import com.webapp.models.TransactionHistoryModel;
import com.webapp.models.UserProfileModel;

@Path("/api/recharge-amount")
public class RechargeAmountAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAllRecharges(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<RechargeAmountModel> rechargeLists = new ArrayList<RechargeAmountModel>();

		rechargeLists = RechargeAmountModel.getAllRechargeAmountList();

		Map<String, Object> output = new HashMap<String, Object>();

		output.put("rechargeLists", rechargeLists);

		return sendDataResponse(output);
	}

	@Path("/{rechargeAmountId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAllCars(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("rechargeAmountId") String rechargeAmountId
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		RechargeAmountModel rechargeModel = RechargeAmountModel.getRechargeAmountById(rechargeAmountId);

		if (rechargeModel != null) {

			UserProfileModel user = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);
			user.setCredit(rechargeModel.getAmount());
			user.updateUserCredits();

			TransactionHistoryModel thm = new TransactionHistoryModel();

			thm.setUserId(loggedInuserId);
			thm.setAmount(rechargeModel.getAmount());
			thm.setPaymentTypeId("2");
			thm.setStatus(ProjectConstants.ADMIN_STATUS_SUCCESS);

			thm.addTransactionHistory(loggedInuserId);

			return sendSuccessMessage("Recharge successfull");
		}

		return sendBussinessError("Invalid recharge amount");
	}

}