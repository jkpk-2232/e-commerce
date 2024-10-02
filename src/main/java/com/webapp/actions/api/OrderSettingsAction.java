package com.webapp.actions.api;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.OrderSettingModel;
import com.webapp.models.ServiceModel;

@Path("/api/order-settings")
public class OrderSettingsAction extends BusinessApiAction {

	@Path("/{serviceId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrderSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("serviceId") String serviceId
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

		ServiceModel serviceModel = ServiceModel.getServiceDetailsByServiceId(serviceId);
		if (serviceModel == null) {
			return sendBussinessError(messageForKey("errorServiceNotFound", request));
		}

		OrderSettingModel orderSettingsModel = OrderSettingModel.getOrderSettingDetailsByServiceId(serviceId);
		if (orderSettingsModel == null) {
			return sendBussinessError(messageForKey("errorOrderSettingsNotFound", request));
		}

		return sendDataResponse(orderSettingsModel);
	}
}