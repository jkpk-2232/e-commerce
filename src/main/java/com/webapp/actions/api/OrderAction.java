package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.OrderModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorStoreModel;

@Path("/api/orders")
public class OrderAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("orderShortId") int orderShortId
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

		Map<String, List<OrderModel>> finalList = new HashMap<>();

		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

		List<String> statusNotToBeConsidered = Arrays.asList(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING);

		List<OrderModel> orderList = OrderModel.getOrdersByUserId(loggedInUserId, start, length, orderShortId, loggedInUserModel.getRoleId(), statusNotToBeConsidered);
		finalList.put("orderList", orderList);

		return sendDataResponse(finalList);
	}

	@Path("/estimate")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrdersEstimate(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		OrderModel orderModel
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

		Map<String, Object> estimateFareMap = OrderUtils.getOrdersEstimatedFareAndPlaceOrder(orderModel, true, loggedInUserId);

		if (estimateFareMap.containsKey(ProjectConstants.STATUS_TYPE)) {

			if (estimateFareMap.containsKey("productInventoryCountErrorMap")) {

				estimateFareMap.put("errorCode", 400);
				estimateFareMap.put("errorType", "OutOfStock");
				estimateFareMap.put("message", messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
				return sendDataResponse(estimateFareMap);
			}

			return sendBussinessError(messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
		}

		return sendDataResponse(estimateFareMap);
	}

	@Path("/place-order")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response placeOrder(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		OrderModel orderModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> estimateFareMap = OrderUtils.getOrdersEstimatedFareAndPlaceOrder(orderModel, false, loggedInUserId);

		if (estimateFareMap.containsKey(ProjectConstants.STATUS_TYPE)) {

			if (estimateFareMap.containsKey("productInventoryCountErrorMap")) {

				estimateFareMap.put("errorCode", 400);
				estimateFareMap.put("errorType", "OutOfStock");
				estimateFareMap.put("message", messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
				return sendDataResponse(estimateFareMap);
			}

			return sendBussinessError(messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
		}

		if (orderModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.ONLINE_ID)) {

			OrderModel outPutOrderModel = OrderModel.getOrderDetailsByOrderId(estimateFareMap.get("orderId").toString());
			return sendDataResponse(outPutOrderModel);

		} else {
			return sendSuccessMessage(String.format(messageForKey("successOrderPlaced", request), estimateFareMap.containsKey("orderShortId") ? estimateFareMap.get("orderShortId") : ProjectConstants.NOT_AVAILABLE));
		}
	}

	@Path("/cancel/{orderId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response cancelOrder(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("orderId") String orderId
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

		Map<String, Object> cancelOrderMap = OrderUtils.cancelOrder(loggedInUserId, orderId);

		if (cancelOrderMap.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(cancelOrderMap.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendSuccessMessage(cancelOrderMap.get(ProjectConstants.STATUS_MESSAGE).toString());
		}
	}

	@Path("/driver-job-status/{orderId}/{status}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response orderJobStatusUpdateByDriver(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("orderId") String orderId,
		@PathParam("status") String status
		) throws SQLException {
	//@formatter:on

		String loggedInDriverUserId = checkValidSession(sessionKeyHeader);
		if (loggedInDriverUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		// status = ProjectConstants.ACCEPTED_REQUEST
		// status = ProjectConstants.REJECTED_TOUR
		// to be called by driver only.
		Map<String, Object> output = OrderUtils.updateOrderJobAssignedStatusByDriverOnly(orderId, status, loggedInDriverUserId);

		if (output.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(output.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendSuccessMessage(output.get(ProjectConstants.STATUS_MESSAGE).toString());
		}
	}

	@Path("/order-delivery-status")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response orderStatusUpdateByVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("otpDelivery") String otpDelivery,
		@QueryParam("endOtp") String endOtp,
		OrderModel inputOrderModel
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

		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

		Map<String, Object> output = new HashMap<>();

		if (loggedInUserModel.getRoleId().equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID)) {
			output = OrderUtils.updateOrderDeliveryStatusByDriverApiOnly(inputOrderModel, loggedInUserId, otpDelivery, endOtp);
		} else if (UserRoleUtils.isVendorAndSubVendorRole(loggedInUserModel.getRoleId())) {
			output = OrderUtils.updateOrderDeliveryStatusByVendorOrAdminViaAdminPanelOrApi(inputOrderModel, loggedInUserId);
		} else {
			return sendBussinessError(messageForKey("errorNotAuthorized", request));
		}

		if (output.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(output.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendSuccessMessage(output.get(ProjectConstants.STATUS_MESSAGE).toString());
		}
	}

	@Path("/{orderId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response orderDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("orderId") String orderId
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

		Map<String, Object> output = new HashMap<>();

		OrderModel orderModel = OrderModel.getOrderDetailsByOrderIdWithOrderItems(orderId);
		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(orderModel.getVendorStoreId());

		if (orderModel.getDriverId() != null && !ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(orderModel.getDriverId())) {

			DriverInfoModel driverInfo = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(orderModel.getDriverId());
			output.put("driverName", driverInfo.getFullName());
			output.put("driverPhone", driverInfo.getPhoneNoCode() + "-" + driverInfo.getPhoneNo());
		} else {
			output.put("driverName", null);
			output.put("driverPhone", null);
		}

		output.put("orderModel", orderModel);
		output.put("customerName", orderModel.getCustomerName());
		output.put("customerPhoneNo", orderModel.getCustomerPhoneNo());
		output.put("customerPhoneNoCode", orderModel.getCustomerPhoneNoCode());
		output.put("vendorStoreModel", vendorStoreModel);

		return sendDataResponse(output);
	}

	@Path("/delivered-orders/{startDate}/{endDate}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDeliveredOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("startDate") String startDate,
		@PathParam("endDate") String endDate
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

		List<String> orderDeliveryStatusList = new ArrayList<>();
		orderDeliveryStatusList.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED);
		orderDeliveryStatusList.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_OFFLINE);

		List<OrderModel> orderModelList = OrderUtils.getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt(headerVendorId, orderDeliveryStatusList, startDate, endDate);

		return sendDataResponse(orderModelList);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrdersListByUser(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader
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

		Map<String, Object> outPut = new HashMap<>();

		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

		List<String> statusNotToBeConsidered = Arrays.asList(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING);

		List<OrderModel> orderList = OrderModel.getOrdersListByUserId(loggedInUserId, -1, loggedInUserModel.getRoleId(), statusNotToBeConsidered);

		int total = OrderModel.getOrdersCountByUserId(loggedInUserId, loggedInUserModel.getRoleId(), statusNotToBeConsidered);

		outPut.put("orderList", orderList);
		outPut.put("totalCount", total);

		return sendDataResponse(outPut);
	}

	@Path("/place-store-orders")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response storeOrdersPlaced(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		List<OrderModel> orderModelList
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		Map<String, Object> outPut = new HashMap<>();

		orderModelList = OrderUtils.placeStoreOrders(orderModelList, loggedInUserId);

		outPut.put("orderList", orderModelList);

		return sendDataResponse(outPut);
	}
}