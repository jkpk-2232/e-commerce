package com.webapp.actions.secure.ccavenue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.SendEmailThread;
import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CcavenueResponseLogModel;
import com.webapp.models.CcavenueRsaRequestModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.TourModel;

@Path("/ccavenue/response")
public class CcavenueResponseHandlerAction extends BusinessAction {

	private static Logger logger = Logger.getLogger(CcavenueResponseHandlerAction.class);

	public static final String ENCRYPTION_KEY = WebappPropertyUtils.getWebAppProperty("encryption_key");

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getResponseHandler(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res) 
					throws ServletException, IOException {
	//@formatter:on
		preprocessRequest(req, res);

		data.put("message", "Payment failed.");

		data.put("labelClose", "Close");

		return loadView("/secure/ccavenue/response-handler.jsp");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Produces(MediaType.TEXT_HTML)
	// @formatter:off
	public Response postResponseHandler(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	// @formatter:on

		preprocessRequest(request, response);

		logger.info("\n\n\n\tRequest\t" + request.toString());

		String bodyString = getBody(request);

		logger.info("\n\n\n\tbodyString\t" + bodyString);

		String encResp = (bodyString.split("&")[0]).split("=")[1];

		logger.info("\t encResp\t" + encResp);

		String decResp = decrypt(encResp);

		logger.info("\t decResp\t" + decResp);

		StringTokenizer tokenizer = new StringTokenizer(decResp, "&");
		Hashtable hs = new Hashtable();

		String pair = null;
		String pname = null;
		String pvalue = null;

		while (tokenizer.hasMoreTokens()) {
			pair = (String) tokenizer.nextToken();
			if (pair != null) {
				StringTokenizer strTok = new StringTokenizer(pair, "=");
				pname = "";
				pvalue = "";
				if (strTok.hasMoreTokens()) {
					pname = (String) strTok.nextToken();
					if (strTok.hasMoreTokens())
						pvalue = (String) strTok.nextToken();
					hs.put(pname, pvalue);
				}
			}
		}

		logger.info("\t hs\t" + hs);

		String orderId = null;
		CcavenueResponseLogModel ccavenueResponseLogModel = new CcavenueResponseLogModel();
		Enumeration enumeration = hs.keys();

		while (enumeration.hasMoreElements()) {

			pname = "" + enumeration.nextElement();
			pvalue = "" + hs.get(pname);

			logger.info("\t pname\t" + pname + "\t pvalue" + pvalue);

			if ("order_id".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					// ccavenueResponseLogModel.setTourId(pvalue);
					orderId = pvalue;
				}
			} else if ("tracking_id".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setTrackingId(pvalue);
				}
			} else if ("amount".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setAmount(Double.parseDouble(pvalue));
				}
			} else if ("order_status".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setOrderStatus(pvalue);
				}
			} else if ("failure_message".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setFailureMessage(pvalue);
				}
			} else if ("payment_mode".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setPaymentMode(pvalue);
				}
			} else if ("bank_ref_no".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setBankRefNo(pvalue);
				}
			} else if ("retry".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setRetriedPayment(pvalue);
				}
			} else if ("response_code".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setBankResponseCode(pvalue);
				}
			} else if ("card_name".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setCardName(pvalue);
				}
			} else if ("currency".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setCurrency(pvalue);
				}
			} else if ("billing_country".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setBillingCountry(pvalue);
				}
			} else if ("billing_tel".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setBillingTel(pvalue);
				}
			} else if ("billing_email".equalsIgnoreCase(pname)) {
				if (pvalue != null && (!"".equals(pvalue))) {
					ccavenueResponseLogModel.setBillingEmail(pvalue);
				}
			}
		}

		logger.info("\t ccavenueResponseLogModel\t" + ccavenueResponseLogModel.toString());

		String statusString = "-1";

		if ((ccavenueResponseLogModel.getOrderStatus() != null) && (!"".equals(ccavenueResponseLogModel.getOrderStatus()))) {

			logger.info("\t orderId\t" + orderId);

			CcavenueRsaRequestModel ccavenueRsaRequestModel = CcavenueRsaRequestModel.getCcavenueRsaRequestByOrderId(orderId);

			if (ccavenueRsaRequestModel != null) {

				logger.info("\t ccavenueRsaRequestModel\t" + ccavenueRsaRequestModel.toString());

				logger.info("\t ccavenueRsaRequestModel.getPaymentRequestType()\t" + ccavenueRsaRequestModel.getPaymentRequestType());

				if (ccavenueRsaRequestModel.getPaymentRequestType().equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_SUBSCRIPTION_ID)) {
					ccavenueResponseLogModel.setSubscriptionId(ccavenueRsaRequestModel.getSubscriptionId());
				} else if (ccavenueRsaRequestModel.getPaymentRequestType().equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_ORDER_ID)) {
					ccavenueResponseLogModel.setDeliveryOrderId(ccavenueRsaRequestModel.getDeliveryOrderId());
				} else {
					ccavenueResponseLogModel.setTourId(ccavenueRsaRequestModel.getTourId());
				}

				ccavenueResponseLogModel.setCcavenueRsaRequestId(ccavenueRsaRequestModel.getCcavenueRsaRequestId());
			} else {
				logger.info("\t ccavenueRsaRequestModel is null\t");
			}

			logger.info("\t ccavenueResponseLogModel.getOrderStatus()\t" + ccavenueResponseLogModel.getOrderStatus());

			if ("Success".equalsIgnoreCase(ccavenueResponseLogModel.getOrderStatus())) {

				statusString = "Success";

			} else if ("Failure".equalsIgnoreCase(ccavenueResponseLogModel.getOrderStatus())) {

				statusString = "Failure";

			} else if ("Aborted".equalsIgnoreCase(ccavenueResponseLogModel.getOrderStatus())) {

				statusString = "Aborted";
			}

			ccavenueResponseLogModel.setUserId(ccavenueRsaRequestModel.getUserId());
			ccavenueResponseLogModel.setOrderId(orderId);
			ccavenueResponseLogModel.insertCcavenueResponseLog("1");

			if (statusString.equalsIgnoreCase("Success")) {

				if (ccavenueRsaRequestModel.getPaymentRequestType().equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_SUBSCRIPTION_ID)) {
					updateSubscription(ccavenueResponseLogModel);
				} else if (ccavenueRsaRequestModel.getPaymentRequestType().equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_ORDER_ID)) {
					OrderUtils.updateDeliveryOrder(ccavenueResponseLogModel);
				} else {
					updateTour(ccavenueResponseLogModel);
				}
			}

			logger.info("\t ccavenueResponseLogModel\t" + ccavenueResponseLogModel.toString());
		}

		logger.info("\t statusString\t" + statusString);

		return sendDataResponse(statusString);
	}

	private void updateSubscription(CcavenueResponseLogModel ccavenueResponseLogModel) {

		logger.info("\n\n\n\tccavenueResponseLogModel updateSubscription\t" + ccavenueResponseLogModel.toString());

		SubscriptionPackageModel spm = SubscriptionPackageModel.getSubscriptionPackageDetailsById(ccavenueResponseLogModel.getSubscriptionId());
		boolean status = DriverSubscriptionUtils.processDriverSubscription(spm, ccavenueResponseLogModel.getUserId(), MultiTenantUtils.getVendorIdByUserId(ccavenueResponseLogModel.getUserId()), ProjectConstants.PAYMENT_TYPE_CCAVENUE);

		String pushMessage = "";

		if (status) {
			pushMessage = String.format(BusinessAction.messageForKeyAdmin("successCCAvenueDriverSubscriptionPaymentSuccess", null), spm.getPackageName(), spm.getPrice());
		} else {
			pushMessage = String.format(BusinessAction.messageForKeyAdmin("errorCCAvenueDriverSubscriptionPayment", null), spm.getPackageName());
		}

		ApnsDeviceModel apnsDeviceOfDriver = ApnsDeviceModel.getDeviseByUserId(ccavenueResponseLogModel.getUserId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(pushMessage);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(ccavenueResponseLogModel.getUserId());
		apnsMessage.insertPushMessage();

		if (apnsDeviceOfDriver != null) {
			apnsDeviceOfDriver.sendNotification("1", "Push", pushMessage, ProjectConstants.PAYMENT_CONFIRMATION, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	private void updateTour(CcavenueResponseLogModel ccavenueResponseLogModel) {

		logger.info("\t ccavenueResponseLogModel\t" + ccavenueResponseLogModel.toString());

		int updateStatus = InvoiceModel.updatePaymentPaidStatus(ccavenueResponseLogModel.getTourId(), true, false, null);

		logger.info("\t updateStatus\t" + updateStatus);

		if (updateStatus > 0) {
			sendEmail(ccavenueResponseLogModel.getTourId());
		}

		TourModel tourDetails = TourModel.getTourDetailsByTourId(ccavenueResponseLogModel.getTourId());

		if (tourDetails != null) {

			logger.info("\t updateStatus send sms\t" + updateStatus);

			AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

			String pushMessage = BusinessAction.messageForKeyAdmin("pushMsgForPaymentConfirmation1", tourDetails.getLanguage()) + " " + tourDetails.getUserTourId() + BusinessAction.messageForKeyAdmin("pushMsgForPaymentConfirmation2", tourDetails.getLanguage());

			String smsText = String.format(BusinessAction.messageForKeyAdmin("smsTextForPaymentConfirmation1", tourDetails.getLanguage()), tourDetails.getUserTourId());

			ApnsDeviceModel apnsDeviceOfDriver = ApnsDeviceModel.getDeviseByUserId(tourDetails.getDriverId());

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(pushMessage);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(tourDetails.getDriverId());
			apnsMessage.insertPushMessage();

			if (apnsDeviceOfDriver != null) {
				apnsDeviceOfDriver.sendNotification("1", "Push", pushMessage, ProjectConstants.PAYMENT_CONFIRMATION, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}

			if (adminSmsSendingModel.isdPaymentReceived()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(smsText, tourDetails.getPhoneNoCode().trim() + tourDetails.getPhoneNo().trim(), ProjectConstants.SMSConstants.SMS_PAYMENT_RECEIVED_TEMPLATE_ID);
			}
		} else {
			logger.info("\t tourDetails null");
		}
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/ccavenue/response-handler.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	private void sendEmail(String tourId) {

		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

		InvoiceModel invoiceModel = InvoiceModel.getInvoiceByTourId(tourDetails.getTourId());

		if (tourDetails.getpEmail() != null) {

			String messasge = getInvoiceMessageNewTemplate(tourDetails, invoiceModel, tourDetails.getLanguage());
			new SendEmailThread(tourDetails.getpEmail(), BusinessAction.messageForKeyAdmin("labelInvoiceDetials", tourDetails.getLanguage()), messasge);
		}
	}

	public String decrypt(String hexCipherText) {
		try {

			SecretKeySpec skey = new SecretKeySpec(getMD5(ENCRYPTION_KEY), "AES");

			byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

			Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);

			return new String(dcipher.doFinal(hexToByte(hexCipherText)), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private byte[] getMD5(String input) {

		try {
			byte[] bytesOfMessage = input.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(bytesOfMessage);
		} catch (Exception e) {
			return null;
		}
	}

	public byte[] hexToByte(String hexString) {

		int len = hexString.length();

		byte[] ba = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			ba[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
		}

		return ba;
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;

		StringBuilder stringBuilder = new StringBuilder();

		BufferedReader bufferedReader = null;

		try {

			InputStream inputStream = request.getInputStream();

			if (inputStream != null) {

				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				char[] charBuffer = new char[128];

				int bytesRead = -1;

				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}

			} else {
				stringBuilder.append("");
			}

		} catch (IOException ex) {
			throw ex;
		} finally {

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

}