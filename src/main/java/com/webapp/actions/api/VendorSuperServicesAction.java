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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ServiceModel;

@Path("/api/vendor-super-services")
public class VendorSuperServicesAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorSuperServices(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			String loggedInuserId = checkValidSession(sessionKeyHeader);
			if (loggedInuserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		List<ServiceModel> serviceList = ServiceModel.getServiceSearch(0, 0, "%" + searchKey + "%", start, length, "1", "s.service_priority ASC", null);

		return sendDataResponse(serviceList);
	}

	@Path("/{serviceTypeId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorSuperServicesBySuperServiceType(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("serviceTypeId") String serviceTypeId,
		@PathParam("start") int start,
		@PathParam("length") int length,
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

		List<ServiceModel> serviceList = ServiceModel.getServiceSearch(0, 0, "%" + searchKey + "%", start, length, "1", "s.service_priority ASC", serviceTypeId);

		return sendDataResponse(serviceList);
	}
}