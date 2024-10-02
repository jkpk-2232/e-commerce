package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.DriverSubscriptionUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.OrderModel;
import com.webapp.models.PaymentOrdersModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.UserModel;

@Path("/api/payment-order")
public class PaymentOrdersAction extends BusinessApiAction {
	
	
	@Path("/get-transactionId")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTransactionId(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		PaymentOrdersModel paymentOrderModel
		) throws SQLException {
		
		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}
		
		String shortId = null;
		boolean isDuplicateOrderId = false;
		int counter = 0;
		int orderIdLength = 4;
		
		UserModel userModel = UserModel.getUserAccountDetailsById(loggedInUserId);
		
		paymentOrderModel.setFirstName(userModel.getFirstName());
		paymentOrderModel.setLastName(userModel.getLastName());
		paymentOrderModel.setMobileNumber(userModel.getPhoneNo());
		paymentOrderModel.setUserId(loggedInUserId);
		paymentOrderModel.setPaymentStatus("PAYMENT_PENDING");
		
		if (ProjectConstants.OrderDeliveryConstants.ORDER.equalsIgnoreCase(paymentOrderModel.getOrderType())) {
			
			OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(paymentOrderModel.getOrderRefId());

			shortId = orderModel.getOrderShortId();
			
		}else {
			SubscriptionPackageModel spm = SubscriptionPackageModel.getSubscriptionPackageDetailsById(paymentOrderModel.getOrderRefId());
			shortId = spm.getShortSubscriptionId();
			
			boolean isEligibleForNewSubscription = DriverSubscriptionUtils.isEligibleForNewSubscription(paymentOrderModel.getOrderRefId(), loggedInUserId, headerVendorId);

			if (!isEligibleForNewSubscription) {
				return sendBussinessError(messageForKey("errorMoreThanOnePackageSubscribed", request));
			}
		}
		
		String orderIdNew = getNewOrderId(shortId, orderIdLength, paymentOrderModel.getOrderType());
		
		do {

			boolean isOrderIdExist = paymentOrderModel.isOrderIdExists(orderIdNew);

			if (isOrderIdExist) {

				isDuplicateOrderId = true;
				orderIdNew = getNewOrderId(shortId, orderIdLength, paymentOrderModel.getOrderType());

				if (counter == 5) {

					counter = 0;
					orderIdLength--;
					orderIdNew = getNewOrderId(shortId, orderIdLength, paymentOrderModel.getOrderType());
				}

				counter++;

			} else {

				isDuplicateOrderId = false;

				paymentOrderModel.setOrderId(orderIdNew);

				paymentOrderModel = paymentOrderModel.insertPaymentOrder(loggedInUserId);
			}

		} while (isDuplicateOrderId);
		
		
		Map<String, Object> finalOutput = new HashMap<>();
		
		finalOutput.put("merchantTransactionId", paymentOrderModel.getOrderId());

		return sendDataResponse(finalOutput);
	}
	
	private String getNewOrderId(String shortId, int orderIdLength, String paymentRequestType) {

		String orderIdNew = "";

		if (paymentRequestType.equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER)) {
			orderIdNew = ProjectConstants.CCAVENUE_DEFAULT_DELIVERY_ORDER_ID + shortId + "-" + generateOrderId(orderIdLength);
		} else {
			orderIdNew = ProjectConstants.CCAVENUE_DEFAULT_SUBSCRIPTION_ORDER_ID + shortId + "-" + generateOrderId(orderIdLength);
		}

		return orderIdNew;
	}

}
