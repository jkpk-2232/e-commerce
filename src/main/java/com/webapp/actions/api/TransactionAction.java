package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverTransactionHistoryModel;
import com.webapp.models.TransactionHistoryModel;

@Path("/api/transaction")
public class TransactionAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStateByCountryId(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length
		) throws SQLException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);
		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		List<TransactionHistoryModel> transactionHistoryModel = TransactionHistoryModel.getTransactionHistoryList(userId, start, length);

		return sendDataResponse(transactionHistoryModel);
	}

	@Path("/driver/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverTransactionHistory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length
		) throws SQLException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);
		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		List<DriverTransactionHistoryModel> transactionHistoryModel = DriverTransactionHistoryModel.getTransactionHistoryList(userId, start, length);

		return sendDataResponse(transactionHistoryModel);
	}
}