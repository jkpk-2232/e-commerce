package com.webapp.actions.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CcavenueResponseLogModel;
import com.webapp.models.CcavenueRsaOrderModel;
import com.webapp.models.CcavenueRsaRequestModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.OrderModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.TourModel;

@Path("/api/ccavenue")
public class CcavenueAction extends BusinessApiAction {

	public static final String TESTING_ENV = "TEST";
	public static final String TESTING_GET_RSA_URL = WebappPropertyUtils.getWebAppProperty("testing_get_rsa_url");

	public static final String LIVE_ENV = "LIVE";
	public static final String LIVE_GET_RSA_URL = WebappPropertyUtils.getWebAppProperty("live_get_rsa_url");

	public static final String ACCESS_CODE_KEY = "access_code";
	public static final String ORDER_ID_KEY = "order_id";

	@Path("/get-rsa")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRsaFromCcavenue(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		CcavenueRsaRequestModel ccavenueRsaRequest
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		String eventId = null;
		String shortId = null;
		String paymentRequestType = ccavenueRsaRequest.getPaymentRequestType();

		if (paymentRequestType.equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_SUBSCRIPTION_ID)) {

			String subscriptionId = ccavenueRsaRequest.getOrderId();
			SubscriptionPackageModel spm = SubscriptionPackageModel.getSubscriptionPackageDetailsById(subscriptionId);

			eventId = subscriptionId;
			shortId = spm.getShortSubscriptionId();

			boolean isEligibleForNewSubscription = DriverSubscriptionUtils.isEligibleForNewSubscription(subscriptionId, loggedInUserId, headerVendorId);

			if (!isEligibleForNewSubscription) {
				return sendBussinessError(messageForKey("errorMoreThanOnePackageSubscribed", request));
			}

		} else if (paymentRequestType.equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_ORDER_ID)) {

			String orderId = ccavenueRsaRequest.getOrderId();
			OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(orderId);

			eventId = orderId;
			shortId = orderModel.getOrderShortId();

		} else {

			String tourId = ccavenueRsaRequest.getOrderId();
			TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

			eventId = tourId;
			shortId = tourModel.getUserTourId();
		}

		String ccavenueRsaOrderId = "-1";
		boolean isDuplicateOrderId = false;
		int counter = 0;
		int orderIdLength = 4;

		String orderIdNew = getNewOrderId(shortId, orderIdLength, paymentRequestType);

		do {

			boolean isOrderIdExist = CcavenueRsaOrderModel.isOrderIdExists(orderIdNew);

			if (isOrderIdExist) {

				isDuplicateOrderId = true;
				orderIdNew = getNewOrderId(shortId, orderIdLength, paymentRequestType);

				if (counter == 5) {

					counter = 0;
					orderIdLength--;
					orderIdNew = getNewOrderId(shortId, orderIdLength, paymentRequestType);
				}

				counter++;

			} else {

				isDuplicateOrderId = false;

				CcavenueRsaOrderModel ccavenueRsaOrderModel = new CcavenueRsaOrderModel();
				ccavenueRsaOrderModel.setPaymentRequestType(paymentRequestType);
				ccavenueRsaOrderModel.setUserId(loggedInUserId);
				ccavenueRsaOrderModel.setOrderId(orderIdNew);

				if (paymentRequestType.equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_SUBSCRIPTION_ID)) {
					ccavenueRsaOrderModel.setSubscriptionId(eventId);
				} else if (paymentRequestType.equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_ORDER_ID)) {
					ccavenueRsaOrderModel.setDeliveryOrderId(eventId);
				} else {
					ccavenueRsaOrderModel.setTourId(eventId);
				}

				ccavenueRsaOrderId = ccavenueRsaOrderModel.insertCcavenueRsaOrderDetails(loggedInUserId);
			}

		} while (isDuplicateOrderId);

		StringBuffer vRequest = new StringBuffer("");
		vRequest.append(ACCESS_CODE_KEY + "=" + ccavenueRsaRequest.getAccessCode() + "&");
		vRequest.append(ORDER_ID_KEY + "=" + orderIdNew);

		URL vUrl = null;
		DataOutputStream vPrintout = null;
		DataInputStream vInput = null;
		StringBuffer vStringBuffer = null;

		HttpsURLConnection connection = null;

		String rsaKey = "";
		Map<String, Object> output = new HashMap<String, Object>();

		try {

			System.setProperty("jsse.enableSNIExtension", "false");

			if (LIVE_ENV.equalsIgnoreCase((ccavenueRsaRequest.getEnvironment()).trim())) {
				vUrl = new URL(LIVE_GET_RSA_URL);
			} else {
				vUrl = new URL(TESTING_GET_RSA_URL);
			}

			connection = (HttpsURLConnection) vUrl.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");

			X509TrustManager tm = new X509TrustManager() {

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			SSLContext sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());

			connection.setSSLSocketFactory(sc.getSocketFactory());

			vPrintout = new DataOutputStream(connection.getOutputStream());
			vPrintout.writeBytes(vRequest.toString());
			vPrintout.flush();
			vPrintout.close();

			connection.connect();

			int code = connection.getResponseCode();

			if (code == 200) {

				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				vStringBuffer = new StringBuffer();
				String vRespData;

				while ((vRespData = bufferedreader.readLine()) != null)
					if (vRespData.length() != 0) {

						vStringBuffer.append(vRespData.trim() + "\n");
					}

				bufferedreader.close();
				bufferedreader = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (vInput != null)
				vInput.close();
			if (connection != null)
				connection = null;
		}

		response.setContentType("text/html");

		if (vStringBuffer != null && vStringBuffer.length() > 0) {

			rsaKey = vStringBuffer.substring(0, vStringBuffer.length() - 1);

			output.put("rsaKey", rsaKey);

			CcavenueRsaRequestModel ccavenueRsaRequestModel = new CcavenueRsaRequestModel();

			ccavenueRsaRequestModel.setUserId(loggedInUserId);
			ccavenueRsaRequestModel.setCcavenueRsaOrderId(ccavenueRsaOrderId);
			ccavenueRsaRequestModel.setRsaKey(rsaKey);
			ccavenueRsaRequestModel.setPaymentRequestType(ccavenueRsaRequest.getPaymentRequestType());

			ccavenueRsaRequestModel.insertCcavenueRsaRequestDetails(loggedInUserId);

		} else {

			output.put("rsaKey", rsaKey);
		}

		output.put("orderId", orderIdNew);

		return sendDataResponse(output);
	}

	private String getNewOrderId(String shortId, int orderIdLength, String paymentRequestType) {

		String orderIdNew = "";

		if (paymentRequestType.equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_SUBSCRIPTION_ID)) {
			orderIdNew = ProjectConstants.CCAVENUE_DEFAULT_SUBSCRIPTION_ORDER_ID + shortId + "-" + generateOrderId(orderIdLength);
		} else if (paymentRequestType.equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_ORDER_ID)) {
			orderIdNew = ProjectConstants.CCAVENUE_DEFAULT_DELIVERY_ORDER_ID + shortId + "-" + generateOrderId(orderIdLength);
		} else {
			orderIdNew = ProjectConstants.CCAVENUE_DEFAULT_ORDER_ID + shortId + "-" + generateOrderId(orderIdLength);
		}

		return orderIdNew;
	}

	@Path("/transaction")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTransactionDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@QueryParam("tripId") String tripId,
		@QueryParam("subscriptionOrderId") String subscriptionOrderId,
		@QueryParam("deliveryOrderId") String deliveryOrderId
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		CcavenueResponseLogModel ccavenueResponseLogModel = null;

		if (tripId != null) {
			ccavenueResponseLogModel = CcavenueResponseLogModel.getCcavenueResponseLogDetailsByTripId(tripId);
		} else if (subscriptionOrderId != null) {
			ccavenueResponseLogModel = CcavenueResponseLogModel.getCcavenueResponseLogDetailsBySubscriptionId(subscriptionOrderId, loggedInuserId);
		} else {
			ccavenueResponseLogModel = CcavenueResponseLogModel.getCcavenueResponseLogDetailsByDeliveryOrderId(deliveryOrderId, loggedInuserId);
		}

		if (ccavenueResponseLogModel != null) {
			return sendDataResponse(ccavenueResponseLogModel);
		} else {
			return sendBussinessError(messageForKey("errorNoTransactionAvailableInvoice", request));
		}
	}

	@Path("/payment/pending")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response pendingPaymentDetails(
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

		Map<String, Object> output = new HashMap<String, Object>();

		InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(loggedInuserId);

		if (invoiceModel != null) {

			output.put("tourId", invoiceModel.getTourId());
			output.put("userTourId", invoiceModel.getUserTourId());
			output.put("sourceAddress", invoiceModel.getSourceAddess());
			output.put("destinationAddress", invoiceModel.getDestiAddess());
			output.put("finalAmountCollected", invoiceModel.getFinalAmountCollected());
			output.put("isPaymentPaid", invoiceModel.isPaymentPaid());
			output.put("dPhotoUrl", invoiceModel.getPhotoUrl());
			output.put("pPhoneNo", invoiceModel.getpPhone());
			output.put("pPhoneNoCode", invoiceModel.getpPhoneCode());
			output.put("pEmail", invoiceModel.getpEmail());
			output.put("dateTime", invoiceModel.getCreatedAt());

		} else {

			output.put("tourId", "-1");
			output.put("userTourId", "-1");
			output.put("sourceAddress", "-1");
			output.put("destinationAddress", "-1");
			output.put("finalAmountCollected", "-1");
			output.put("isPaymentPaid", "-1");
			output.put("dPhotoUrl", "");
			output.put("pPhoneNo", "-1");
			output.put("pPhoneNoCode", "-1");
			output.put("pEmail", "-1");
			output.put("dateTime", 0);
		}

		return sendDataResponse(output);
	}
}