package com.webapp.actions.api;

import java.sql.SQLException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.UnifiedHistoryUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.UnifiedHistoryModel;
import com.webapp.models.UserModel;

@Path("/api/unified-history")
public class UnifiedHistoryAction extends BusinessApiAction {

	@Path("/{type}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getUnifiedHistory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("type") String type,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("vendorOrderManagement") String vendorOrderManagement,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		if (vendorOrderManagement != null && !"".equalsIgnoreCase(vendorOrderManagement) && vendorOrderManagement.equalsIgnoreCase("-1")) {
			vendorOrderManagement = null;
		}

		List<String> statusList = UnifiedHistoryUtils.getStatusListAsPerType(type);

		UserModel loggedInUserModel = UserModel.getUserActiveDeativeDetailsById(loggedInUserId);

		List<UnifiedHistoryModel> unifiedHistoryList = UnifiedHistoryModel.getUnifiedHistoryList(loggedInUserId, start, length, loggedInUserModel.getRoleId(), "%" + searchKey + "%", statusList, vendorOrderManagement);

		Map<String, List<UnifiedHistoryModel>> finalOutput = new HashMap<>();
		finalOutput.put("unifiedHistoryList", unifiedHistoryList);

		return sendDataResponse(finalOutput);
	}
}