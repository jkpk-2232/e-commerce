package com.webapp.actions.api;

import java.io.IOException;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DeliveryAddressModel;

@Path("/api/delivery-address")
public class DeliveryAddressAction extends BusinessApiAction {

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addDeliveryAddress(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		DeliveryAddressModel deliveryAddressModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = deliveryAddressModelValidation(deliveryAddressModel, request);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		if (deliveryAddressModel.getAddressType().equalsIgnoreCase(ProjectConstants.DELIVERY_ADDRESS.DELIVERY_ADDRESS_OTHER_ID)) {
			if (deliveryAddressModel.getIsDefault()) {
				DeliveryAddressModel.updateIsDefaultByUserId(loggedInuserId, false);
				deliveryAddressModel.insertDeliveryAddress(loggedInuserId);
			} else {
				deliveryAddressModel.insertDeliveryAddress(loggedInuserId);
			}
			
		} else {
			
			if (deliveryAddressModel.getIsDefault()) {
				DeliveryAddressModel.updateIsDefaultByUserId(loggedInuserId, false);
				
				DeliveryAddressModel currentDeliveryAddressModel = DeliveryAddressModel.getDeliveryAddressByAddressTypeAndUserId(loggedInuserId, deliveryAddressModel.getAddressType());

				if (currentDeliveryAddressModel == null) {
					deliveryAddressModel.insertDeliveryAddress(loggedInuserId);
				} else {
					deliveryAddressModel.setDeliveryAddressId(currentDeliveryAddressModel.getDeliveryAddressId());
					deliveryAddressModel.updateDeliveryAddress(loggedInuserId);
				}
				
			} else {
				
				DeliveryAddressModel currentDeliveryAddressModel = DeliveryAddressModel.getDeliveryAddressByAddressTypeAndUserId(loggedInuserId, deliveryAddressModel.getAddressType());

				if (currentDeliveryAddressModel == null) {
					deliveryAddressModel.insertDeliveryAddress(loggedInuserId);
				} else {
					deliveryAddressModel.setDeliveryAddressId(currentDeliveryAddressModel.getDeliveryAddressId());
					deliveryAddressModel.updateDeliveryAddress(loggedInuserId);
				}

			}

			
		}

		return sendSuccessMessage(messageForKey("successDeliveryAddressAdded", request));
	}

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDeliveryAddressList(
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

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, List<DeliveryAddressModel>> finalList = new HashMap<>();

		List<DeliveryAddressModel> deliveryAddressList = DeliveryAddressModel.getDeliveryAddressList(loggedInuserId, start, length, "%" + searchKey + "%");
		finalList.put("deliveryAddressList", deliveryAddressList);

		return sendDataResponse(finalList);
	}

	@Path("/{deliveryAddressId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteDeliveryAddress(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("deliveryAddressId") String deliveryAddressId
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

		DeliveryAddressModel dam = new DeliveryAddressModel();
		dam.setDeliveryAddressId(deliveryAddressId);
		dam.deleteDeliveryAddress(loggedInuserId);

		return sendSuccessMessage(messageForKey("successDeliveryAddressDeleted", request));
	}

	private List<String> deliveryAddressModelValidation(DeliveryAddressModel deliveryAddressModel, HttpServletRequest request) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping("addressType", "Address Type", new RequiredValidationRule());
		validator.addValidationMapping("addressLat", "Address Lat", new RequiredValidationRule());
		validator.addValidationMapping("addressLng", "Address Lng", new RequiredValidationRule());
		validator.addValidationMapping("address", "Address", new MinMaxLengthValidationRule(1, 250));

		errorMessages = validator.validate(deliveryAddressModel);

		return errorMessages;
	}
}