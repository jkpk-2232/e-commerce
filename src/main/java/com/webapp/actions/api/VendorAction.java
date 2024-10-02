package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.OrderUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.InputModel;

@Path("/api/vendors")
public class VendorAction extends BusinessApiAction {

	@Path("/dashboard")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorDashboard(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		InputModel inputModel
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> outputMap = OrderUtils.getVendorDashboardData(inputModel, loggedInuserId);

		return sendDataResponse(outputMap);
	}
}