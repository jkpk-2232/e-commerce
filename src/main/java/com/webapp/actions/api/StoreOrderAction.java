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
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.OrderModel;

@Path("/api/vendor-store-orders")
public class StoreOrderAction extends BusinessApiAction {
	
	
	@Path("/{vendorId}/{vendorStoreId}/{type}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrderListBasedOnStoreAndVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("vendorStoreId") String vendorStoreId,
		@PathParam("type") String type,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("vendorOrderManagement") String vendorOrderManagement,
		@QueryParam("searchKey") String searchKey
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

		if (vendorOrderManagement != null && !"".equalsIgnoreCase(vendorOrderManagement) && vendorOrderManagement.equalsIgnoreCase("-1")) {
			vendorOrderManagement = null;
		}

		int orderShortIdSearch = MyHubUtils.searchNumericFormat(searchKey);

		List<String> orderStatus = OrderUtils.getOrderStatusListAsPerOrderType(type);

		List<OrderModel> orderList = OrderModel.getOrderListBasedOnStoreAndVendorAndSearchAPI("%" + searchKey + "%", start, length, vendorId, vendorStoreId, orderStatus, orderShortIdSearch, vendorOrderManagement);

		Map<String, List<OrderModel>> finalList = new HashMap<>();
		finalList.put("orderList", orderList);
		return sendDataResponse(finalList);
	}
	

}
