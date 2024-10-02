package com.webapp.actions.api;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.VendorSubscriberModel;

@Path("/api/vendor-store-subscriber")
public class VendorSubscriberAction extends BusinessApiAction {

	@Path("/{vendorStoreId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorStoreSubscriber(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorStoreId") String vendorStoreId
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

		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
		if (vendorStoreModel == null) {
			return sendBussinessError(messageForKey("errorInvalidStore", request));
		}

		boolean isUserSubscribedToVendorStore = VendorSubscriberModel.isUserSubscribedToVendorStore(vendorStoreId, loggedInuserId);
		if (isUserSubscribedToVendorStore) {
			return sendBussinessError(messageForKey("errorAlreadyVendorSubscribed", request));
		}

		VendorSubscriberModel vsm = new VendorSubscriberModel();
		vsm.setVendorId(vendorStoreModel.getVendorId());
		vsm.setVendorStoreId(vendorStoreId);
		vsm.setUserId(loggedInuserId);
		vsm.insertVendorSubscriber(loggedInuserId);

		return sendSuccessMessage(messageForKey("successVendorSubscribed", request));
	}

	@Path("/{vendorStoreId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorUnSubscriber(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorStoreId") String vendorStoreId
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

		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
		if (vendorStoreModel == null) {
			return sendBussinessError(messageForKey("errorInvalidStore", request));
		}

		boolean isUserSubscribedToVendor = VendorSubscriberModel.isUserSubscribedToVendorStore(vendorStoreId, loggedInuserId);
		if (!isUserSubscribedToVendor) {
			return sendBussinessError(messageForKey("errorNotVendorSubscribed", request));
		}

		VendorSubscriberModel vsm = new VendorSubscriberModel();
		vsm.setVendorStoreId(vendorStoreId);
		vsm.setUserId(loggedInuserId);
		vsm.deleteVendorSubscriber();

		return sendSuccessMessage(messageForKey("successVendorUnSubscribed", request));
	}
}