package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.FieldConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CartModel;

@Path("/api/cart")
public class CartAction extends BusinessApiAction {
	
	
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addCart(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			CartModel cartModel
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
		
		cartModel.setVendorId(headerVendorId);
		cartModel.setUserId(loggedInuserId);
		
		CartModel.addCart(cartModel);
		
		return sendSuccessMessage(messageForKey("successAddCartItem", request));
	}
	
	@Path("/add-multiple-cart-items")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addMultipleCartItems(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			List<CartModel> cartModelList
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
		
		for (CartModel cartModel : cartModelList) {
			cartModel.setVendorId(headerVendorId);
			cartModel.setUserId(loggedInuserId);
			cartModel.setCartId(UUIDGenerator.generateUUID());
			cartModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			cartModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			cartModel.setCreatedBy(cartModel.getUserId());
			cartModel.setUpdatedBy(cartModel.getUserId());
		}
		
		
		CartModel.addMultipleCartItems(cartModelList);
		
		return sendSuccessMessage(messageForKey("successAddCartItem", request));
	}
	
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCartDetailsByUser(
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
		
		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}
		
		Map<String, List<CartModel>> finalResponse = new HashMap<>();
		
		List<CartModel> cartModelList = CartModel.getCartDetailsByUser(loggedInuserId);
		
		finalResponse.put("cartItemList", cartModelList);
		
		return sendDataResponse(finalResponse);
	}
	
	
	@PUT
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateCart(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			CartModel cartModel
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
		
		cartModel.setUserId(loggedInuserId);
		
		CartModel.updateCart(cartModel);
		
		return sendSuccessMessage(messageForKey("successEditCartItem", request));
	}
	
	
	
	@Path("/{cartId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteCartByCartId(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam(FieldConstants.CART_ID) String cartId
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
		
		 CartModel.deleteCartItemByCartId(cartId);
		
		 return sendSuccessMessage(messageForKey("successCartItemDeletedSuccess", request));
	}
	
	
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteTotalCartItems (
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
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

		CartModel.deleteTotalCartItemsByVendorAndStoreAndUser(headerVendorId , vendorStoreId, loggedInUserId);

		return sendSuccessMessage(messageForKey("successCartItemDeletedSuccess", request));
	}
	
	@Path("/cartDetails-new")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getNewCartDetailsByUser(
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
		
		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}
		
		Map<String, List<CartModel>> finalResponse = new HashMap<>();
		
		List<CartModel> cartModelList = CartModel.getNewCartDetailsByUser(loggedInuserId);
		
		finalResponse.put("cartItemList", cartModelList);
		
		return sendDataResponse(finalResponse);
	}

}
