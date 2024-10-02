package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.CustomisedOrderUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CustomisedOrderModel;
import com.webapp.models.UserProfileModel;

@Path("/api/kp-mart/offline-orders")
public class KPMartOfflineOrderAction extends BusinessApiAction {

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response offlineOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@HeaderParam("x-vendor-store-id") String vendorStoreId,
		Map<String, List<CustomisedOrderModel> >  orderMapObject
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

		List<CustomisedOrderModel> customisedOrderList = orderMapObject.get("orders_list");
		Map<String, Object> outPut = new HashMap<>();
		
		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);
		
		String vendorId = loggedInUserId;
		
		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

			vendorId = UserRoleUtils.getParentVendorId(loggedInUserId);
		} 

		List<String> successOrderIds = CustomisedOrderUtils.offlineOrders(vendorId, vendorStoreId, customisedOrderList, loggedInUserId);

		outPut.put("orderIdList", successOrderIds);

		return sendDataResponse(outPut);
	}

}
