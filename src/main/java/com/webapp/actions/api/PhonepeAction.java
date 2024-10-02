package com.webapp.actions.api;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import com.jeeutils.DateUtils;
import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.OrderModel;
import com.webapp.models.PaymentOrdersModel;
import com.webapp.models.PhonepePaymentModel;
import com.webapp.models.PhonepeRequestParameters;
import com.webapp.models.PhonepeResponseModel;
import com.webapp.models.PhonepeStaticQrResponseModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorStoreModel;

@Path("/api/phonepe")
public class PhonepeAction extends BusinessApiAction {

	@Path("/get-transactionId")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTransactionId(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam(FieldConstants.ORDER_ID) String orderId
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		OrderModel orderDetails = OrderModel.getOrderDetailsByOrderId(orderId);

		PhonepePaymentModel phonepePaymentModel = new PhonepePaymentModel();
		phonepePaymentModel.setAmount(orderDetails.getOrderCharges());
		phonepePaymentModel.setVendorId(headerVendorId);
		phonepePaymentModel.setMerchantTransactionId(orderId);
		phonepePaymentModel.setUserId(loggedInuserId);

		phonepePaymentModel = phonepePaymentModel.insertPhonepePayment(loggedInuserId);

		Map<String, Object> finalOutput = new HashMap<>();

		finalOutput.put("merchantTransactionId", phonepePaymentModel.getMerchantTransactionId());

		return sendDataResponse(finalOutput);
	}

	@Path("/call-back")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public void phonepeCallback(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("X-VERIFY") String verifyHeader,
		PhonepeResponseModel phonepeResponseModel
		) throws SQLException {
	//@formatter:on

		if (phonepeResponseModel != null) {
			byte[] decodedBytes = Base64.getDecoder().decode(phonepeResponseModel.getResponse());

			ObjectMapper mapper = new ObjectMapper();
			try {
				PhonepeResponseModel phonepeResp = mapper.readValue(decodedBytes, PhonepeResponseModel.class);

				PhonepePaymentModel phonepePaymentModel = PhonepePaymentModel.getPhonepePaymentDetailsByMerchantTransactionId(phonepeResp.getData().getMerchantTransactionId());

				if (phonepePaymentModel != null) {

					phonepePaymentModel.setCode(phonepeResp.getCode());
					phonepePaymentModel.setMessage(phonepeResp.getMessage());
					phonepePaymentModel.setState(phonepeResp.getData().getState());
					phonepePaymentModel.setResponseCode(phonepeResp.getData().getResponseCode());
					phonepePaymentModel.setTransactionId(phonepeResp.getData().getTransactionId());

					if (phonepeResp.getData().getPaymentInstrument() != null) {

						phonepePaymentModel.setPaymentInstrumentType(phonepeResp.getData().getPaymentInstrument().getType());
						phonepePaymentModel.setUtr(phonepeResp.getData().getPaymentInstrument().getUtr());
						phonepePaymentModel.setCardType(phonepeResp.getData().getPaymentInstrument().getCardType());
						phonepePaymentModel.setPgTransactionId(phonepeResp.getData().getPaymentInstrument().getPgTransactionId());
						phonepePaymentModel.setBankTransactionId(phonepeResp.getData().getPaymentInstrument().getBankTransactionId());
						phonepePaymentModel.setPgAuthorizationCode(phonepeResp.getData().getPaymentInstrument().getPgAuthorizationCode());
						phonepePaymentModel.setArn(phonepeResp.getData().getPaymentInstrument().getArn());
						phonepePaymentModel.setBankId(phonepeResp.getData().getPaymentInstrument().getBankId());
						phonepePaymentModel.setPgServiceTransactionId(phonepeResp.getData().getPaymentInstrument().getPgServiceTransactionId());
					}

					phonepePaymentModel.updatePhonepePaymentDetails();

					if ("PAYMENT_SUCCESS".equals(phonepeResp.getCode())) {

						OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(phonepeResp.getData().getMerchantTransactionId());

						if (orderModel != null) {

							orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
							orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
							orderModel.setPaymentMode(ProjectConstants.CASH_ID);
							orderModel.updatePaymentAndOrderStatusForKPMart(orderModel.getCreatedBy());

							//CustomisedOrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);
							OrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);

						}

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Path("/check-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response checkStatus (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("merchantTransactionId") String merchantTransactionId
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		String urlEndPoint = WebappPropertyUtils.PHONEPE_BASE_URL + WebappPropertyUtils.MERCHANT_ID + '/' + merchantTransactionId;

		HttpURLConnection connection = null;
		Map<String, Object> outputMap = new HashMap<>();
		outputMap.put("type", "");
		outputMap.put("messages", new ArrayList<>());

		URL url;
		try {
			String sha256Hashed = Hashing.sha256().hashString("/pg/v1/status/" + WebappPropertyUtils.MERCHANT_ID + "/" + merchantTransactionId + WebappPropertyUtils.SALT_KEY, StandardCharsets.UTF_8).toString();

			url = new URL(urlEndPoint);

			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-VERIFY", sha256Hashed + "###" + WebappPropertyUtils.SALT_INDEX);
			connection.setRequestProperty("X-MERCHANT-ID", WebappPropertyUtils.MERCHANT_ID);

			int responseCode = connection.getResponseCode();

			ObjectMapper mapper = new ObjectMapper();

			if (responseCode == HttpURLConnection.HTTP_OK) {

				PhonepeResponseModel phonepeResp = mapper.readValue(connection.getInputStream(), PhonepeResponseModel.class);

				if (phonepeResp != null) {

					PhonepePaymentModel phonepePaymentModel = PhonepePaymentModel.getPhonepePaymentDetailsByMerchantTransactionId(phonepeResp.getData().getMerchantTransactionId());

					if (phonepePaymentModel != null) {

						phonepePaymentModel.setCode(phonepeResp.getCode());
						phonepePaymentModel.setMessage(phonepeResp.getMessage());
						phonepePaymentModel.setState(phonepeResp.getData().getState());
						phonepePaymentModel.setResponseCode(phonepeResp.getData().getResponseCode());
						phonepePaymentModel.setTransactionId(phonepeResp.getData().getTransactionId());

						if (phonepeResp.getData().getPaymentInstrument() != null) {

							phonepePaymentModel.setPaymentInstrumentType(phonepeResp.getData().getPaymentInstrument().getType());
							phonepePaymentModel.setUtr(phonepeResp.getData().getPaymentInstrument().getUtr());
							phonepePaymentModel.setCardType(phonepeResp.getData().getPaymentInstrument().getCardType());
							phonepePaymentModel.setPgTransactionId(phonepeResp.getData().getPaymentInstrument().getPgTransactionId());
							phonepePaymentModel.setBankTransactionId(phonepeResp.getData().getPaymentInstrument().getBankTransactionId());
							phonepePaymentModel.setPgAuthorizationCode(phonepeResp.getData().getPaymentInstrument().getPgAuthorizationCode());
							phonepePaymentModel.setArn(phonepeResp.getData().getPaymentInstrument().getArn());
							phonepePaymentModel.setBankId(phonepeResp.getData().getPaymentInstrument().getBankId());
							phonepePaymentModel.setPgServiceTransactionId(phonepeResp.getData().getPaymentInstrument().getPgServiceTransactionId());
							phonepePaymentModel.setBrn(phonepeResp.getData().getPaymentInstrument().getBrn());
							phonepePaymentModel.setResponseCodeDescription(phonepeResp.getData().getPaymentInstrument().getResponseCodeDescription());
						}

						phonepePaymentModel.updatePhonepePaymentDetails();

						if ("PAYMENT_SUCCESS".equals(phonepeResp.getCode())) {

							OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(phonepeResp.getData().getMerchantTransactionId());
							if (orderModel != null) {
								/*
								 * orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.
								 * ORDER_PAYMENT_SUCCESS);
								 * orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.
								 * ORDER_STATUS_NEW);
								 * orderModel.updatePaymentAndOrderStatusForCCavenuePayment(loggedInuserId);
								 */
								orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
								orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
								orderModel.setPaymentMode(ProjectConstants.CASH_ID);
								orderModel.updatePaymentAndOrderStatusForKPMart(loggedInuserId);

								// CustomisedOrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);
								OrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);

								return sendSuccessMessage(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
							} else
								return sendSuccessMessage("Order not forund orderId:" + phonepeResp.getData().getMerchantTransactionId());

						} else {
							return sendSuccessMessage(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_FAILED);
						}

					} else

						return sendBussinessError("Not found record based on merchantTransactionId");

				} else {

					return sendBussinessError("Phonepe Response is empty");
				}

			} else {

				return sendBussinessError("Not reached phonepe check status Api");
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return sendDataResponse(outputMap);
	}
	
	
	@Path("/get-static-qr-transaction-list")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStaticQRTransactionList (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@HeaderParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		PhonepeRequestParameters phonepeRequestParameters
		) {
	//@formatter:on	
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 
		 Map<String, Object> finalList = new HashMap<>();
		 
		 finalList.put("status", false);
		 finalList.put("code", "");
		 finalList.put("message", "");
		 finalList.put("data", new ArrayList<>());
		 
		 HttpURLConnection connection = null;
	        
		try {
			
			UserProfileModel userProfile =  UserProfileModel.getAdminUserAccountDetailsById(headerVendorId);
			
			if (userProfile != null) {
				
				VendorStoreModel vendorStore = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
				
				if (vendorStore != null) {
					
					if (userProfile.getPhonepeMerchantId() != null && userProfile.getSaltIndex() != null && userProfile.getSaltKey() != null) {

						if (vendorStore.getPhonepeStoreId() != null) {
							JSONObject jsonObject = new JSONObject();
							
							jsonObject.put("size", phonepeRequestParameters.getSize());
							jsonObject.put("merchantId", userProfile.getPhonepeMerchantId());
							jsonObject.put("storeId", vendorStore.getPhonepeStoreId());
							
							if (phonepeRequestParameters.getStartTimestamp() > 0) {
								jsonObject.put("startTimestamp", phonepeRequestParameters.getStartTimestamp());
							}
							if (phonepeRequestParameters.getAmount() > 0) {
								jsonObject.put("amount", phonepeRequestParameters.getAmount() * 100 );
							}
							
							if (phonepeRequestParameters.getLast4Digits() != null) {
								jsonObject.put("last4Digits", phonepeRequestParameters.getLast4Digits());
							}
							
							if (phonepeRequestParameters.getQrcodeId() != null) {
								jsonObject.put("qrCodeId", phonepeRequestParameters.getQrcodeId());
							}
							
							if (phonepeRequestParameters.getQrcodeId() != null) {
								jsonObject.put("terminalId", phonepeRequestParameters.getTerminalId());
							}
							
							String base64Payload = Base64.getEncoder().encodeToString(jsonObject.toString().getBytes());
							
							String sha256Hashed = Hashing.sha256().hashString(base64Payload + "/v3/qr/transaction/list" + userProfile.getSaltKey() , StandardCharsets.UTF_8).toString();
							
							URL url = new URL(WebappPropertyUtils.getWebAppProperty("phonepe_static_qr_base_Url") + "/v3/qr/transaction/list");
						       connection = (HttpURLConnection) url.openConnection();
						       connection.setRequestMethod("POST");
						       connection.setRequestProperty("Content-Type", "application/json");
						       connection.setRequestProperty("X-VERIFY", sha256Hashed + "###" + userProfile.getSaltKey());
						       connection.setDoOutput(true);
						       
						       String jsonInputString = "{\"request\":\"" + base64Payload + "\"}";
						       
							try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
								wr.writeBytes(jsonInputString);
								wr.flush();
							}
							
							int responseCode = connection.getResponseCode();

							if (responseCode == HttpURLConnection.HTTP_OK) {
								PhonepeStaticQrResponseModel phonepeStaticQrResp = objectMapper.readValue(connection.getInputStream(), PhonepeStaticQrResponseModel.class);
								if ("true".equals(phonepeStaticQrResp.getSuccess())) {
									System.out.println("condition true");
									finalList.replace("status", true);
									finalList.replace("code", phonepeStaticQrResp.getCode());
									finalList.replace("message", phonepeStaticQrResp.getMessage());
									finalList.replace("data", phonepeStaticQrResp.getData().getTransactions());

								} else {
									finalList.replace("status", false);
									finalList.replace("code", phonepeStaticQrResp.getCode());
									finalList.replace("message", phonepeStaticQrResp.getMessage());
									finalList.replace("data", phonepeStaticQrResp.getData().getTransactions());
								} 
							}else {
								return sendBussinessError("Not reached phonepe transaction list Api");
							}
						            
						} else {
							return sendBussinessError(messageForKey("errorPhonepeStoreIdDetails", request));
						}
						
					}else {
						return sendBussinessError(messageForKey("errorPhonepeConfiguration", request));
					}
					
				} else {
					return sendBussinessError("Not a valid vendor store.");
				}
				
			}else {
				return sendBussinessError(messageForKey("errorInvalidVendor", request));
			}
		            
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	        
		return sendDataResponse(finalList);
	}
	
	
	@Path("/get-metadata")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMetadata (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		PhonepeRequestParameters phonepeRequestParameters
		) {
		
		
		 ObjectMapper objectMapper = new ObjectMapper();
		 
		 Map<String, Object> finalList = new HashMap<>();
		 
		 finalList.put("status", false);
		 finalList.put("message", "");
		 finalList.put("code", "" );
		 finalList.put("data", "" );
	        
		 HttpURLConnection connection = null;
		try {
			UserProfileModel userProfile =  UserProfileModel.getAdminUserAccountDetailsById(headerVendorId);
			
			if (userProfile != null) {
				
				JSONObject jsonObject = new JSONObject();
				
				jsonObject.put("merchantId", userProfile.getPhonepeMerchantId());
				jsonObject.put("phonepeTransactionId", phonepeRequestParameters.getPhonepeTransactionId());//"T2406051452055182757343"
				jsonObject.put("schemaVersionNumber", WebappPropertyUtils.getWebAppProperty("schema_version_number"));
				
				JSONObject metaData = new JSONObject();
				
				metaData.put("BILLNUMBER", phonepeRequestParameters.getBillNumber());
				jsonObject.put("metadata", metaData);
				
				String jsonPayload = jsonObject.toString();
				String base64Payload = Base64.getEncoder().encodeToString(jsonPayload.getBytes());
				
				String sha256Hashed = Hashing.sha256().hashString(base64Payload + "/v1/merchant/transaction/metadata" + userProfile.getSaltKey(), StandardCharsets.UTF_8).toString();
				
				URL url = new URL( WebappPropertyUtils.getWebAppProperty("phonepe_static_qr_base_Url") + "/v1/merchant/transaction/metadata");
				
			            connection = (HttpURLConnection) url.openConnection();
			            connection.setRequestMethod("POST");
			            connection.setRequestProperty("Content-Type", "application/json");
			            connection.setRequestProperty("x-verify", sha256Hashed + "###" + userProfile.getSaltIndex());
			            connection.setDoOutput(true);

			            String jsonInputString = "{\"request\":\"" + base64Payload + "\"}";
			        
			            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
						wr.writeBytes(jsonInputString);
						wr.flush();
					}
			            
			            int responseCode = connection.getResponseCode();
			            
			            if (responseCode == HttpURLConnection.HTTP_OK) {
			       	     PhonepeStaticQrResponseModel phonepeStaticQrResp = objectMapper.readValue(connection.getInputStream(), PhonepeStaticQrResponseModel.class);
			       	     
			       	     if ("true".equals(phonepeStaticQrResp.getSuccess())) {
			       		     finalList.replace("status", true);
			       		     finalList.replace("message", phonepeStaticQrResp.getMessage());
			       		     finalList.replace("code", phonepeStaticQrResp.getCode());
			       		     finalList.replace("data", phonepeStaticQrResp.getData());
			       	     }else {
			       		     finalList.replace("message", phonepeStaticQrResp.getMessage());
			       		     finalList.replace("code", phonepeStaticQrResp.getCode());
			       		     finalList.replace("data", phonepeStaticQrResp.getData());
			       		     finalList.replace("status", false);
			       	     }
			            } else {
			       	     return sendBussinessError("Not reached phonepe metadata Api");
			            }
			            
			} else {
				return sendBussinessError(messageForKey("errorInvalidVendor", request));
			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	        
		return sendDataResponse(finalList);
	}
	
	@Path("/check-status-new")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response checkStatusNew (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("merchantTransactionId") String merchantTransactionId,
		@QueryParam("orderType") String orderType
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}
		
		String merchantId = null;
		String saltKey = null;
		String saltIndex = null;
		
		if (ProjectConstants.OrderDeliveryConstants.ORDER.equals(orderType)) {
			 merchantId = WebappPropertyUtils.MERCHANT_ID;
			 saltKey = WebappPropertyUtils.SALT_KEY;
			 saltIndex = WebappPropertyUtils.SALT_INDEX;
		} else {
			merchantId = WebappPropertyUtils.DRIVER_MERCHANT_ID;
			saltKey = WebappPropertyUtils.DRIVER_SALT_KEY;
			saltIndex = WebappPropertyUtils.DRIVER_SALT_INDEX;
		}
		

		String urlEndPoint = WebappPropertyUtils.PHONEPE_BASE_URL + merchantId + '/' + merchantTransactionId;

		HttpURLConnection connection = null;
		Map<String, Object> outputMap = new HashMap<>();
		outputMap.put("type", "");
		outputMap.put("messages", new ArrayList<>());

		URL url;
		try {
			String sha256Hashed = Hashing.sha256().hashString("/pg/v1/status/" + merchantId + "/" + merchantTransactionId + saltKey, StandardCharsets.UTF_8).toString();

			url = new URL(urlEndPoint);

			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-VERIFY", sha256Hashed + "###" + saltIndex);
			connection.setRequestProperty("X-MERCHANT-ID", merchantId);

			int responseCode = connection.getResponseCode();

			ObjectMapper mapper = new ObjectMapper();

			if (responseCode == HttpURLConnection.HTTP_OK) {

				PhonepeResponseModel phonepeResp = mapper.readValue(connection.getInputStream(), PhonepeResponseModel.class);

				if (phonepeResp != null) {

					PaymentOrdersModel paymentOrderModel = PaymentOrdersModel.getPaymentDetailsByOrderId(merchantTransactionId);

					if (paymentOrderModel != null) {
						
						
						if ("PAYMENT_SUCCESS".equals(phonepeResp.getCode())) {
							
							 
							paymentOrderModel.setPaymentStatus(phonepeResp.getCode());
							paymentOrderModel.setUpdatedBy(loggedInuserId);
							paymentOrderModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
								
							paymentOrderModel.updatePaymentStatus();
							
							PhonepePaymentModel phonepePaymentModel = new PhonepePaymentModel();
							
							phonepePaymentModel.setPaymentOrderId(paymentOrderModel.getPaymentOrderId());
							phonepePaymentModel.setMerchantTransactionId(merchantTransactionId);
							phonepePaymentModel.setUserId(loggedInuserId);
							phonepePaymentModel.setAmount(paymentOrderModel.getAmount());
							phonepePaymentModel.setCode(phonepeResp.getCode());
							phonepePaymentModel.setMessage(phonepeResp.getMessage());
							phonepePaymentModel.setState(phonepeResp.getData().getState());
							phonepePaymentModel.setResponseCode(phonepeResp.getData().getResponseCode());
							phonepePaymentModel.setTransactionId(phonepeResp.getData().getTransactionId());
							
							if (phonepeResp.getData().getPaymentInstrument() != null) {

								phonepePaymentModel.setPaymentInstrumentType(phonepeResp.getData().getPaymentInstrument().getType());
								phonepePaymentModel.setUtr(phonepeResp.getData().getPaymentInstrument().getUtr());
								phonepePaymentModel.setCardType(phonepeResp.getData().getPaymentInstrument().getCardType());
								phonepePaymentModel.setPgTransactionId(phonepeResp.getData().getPaymentInstrument().getPgTransactionId());
								phonepePaymentModel.setBankTransactionId(phonepeResp.getData().getPaymentInstrument().getBankTransactionId());
								phonepePaymentModel.setPgAuthorizationCode(phonepeResp.getData().getPaymentInstrument().getPgAuthorizationCode());
								phonepePaymentModel.setArn(phonepeResp.getData().getPaymentInstrument().getArn());
								phonepePaymentModel.setBankId(phonepeResp.getData().getPaymentInstrument().getBankId());
								phonepePaymentModel.setPgServiceTransactionId(phonepeResp.getData().getPaymentInstrument().getPgServiceTransactionId());
								phonepePaymentModel.setBrn(phonepeResp.getData().getPaymentInstrument().getBrn());
								phonepePaymentModel.setResponseCodeDescription(phonepeResp.getData().getPaymentInstrument().getResponseCodeDescription());
							}
							
							if (ProjectConstants.OrderDeliveryConstants.ORDER.equals(paymentOrderModel.getOrderType())) {
								
								OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(paymentOrderModel.getOrderRefId());
								if (orderModel != null) {
									
									phonepePaymentModel.setVendorId(orderModel.getOrderReceivedAgainstVendorId());
									phonepePaymentModel.insertPayment();
									
									orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
									orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
									orderModel.setPaymentMode(ProjectConstants.CASH_ID);
									orderModel.updatePaymentAndOrderStatusForKPMart(loggedInuserId);

									// CustomisedOrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);
									String orderSuccessMessage =  String.format(BusinessAction.messageForKeyAdmin("successOrderPlaced"), orderModel.getOrderShortId()); 
									MyHubNotificationUtils.sendFCMPushNotificationToUser(orderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER, orderSuccessMessage);
									OrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);

									return sendSuccessMessage(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
								} else
									return sendSuccessMessage("Order not forund orderId:" + phonepeResp.getData().getMerchantTransactionId());
							}else {
								
								String vendorId = MultiTenantUtils.getVendorIdByUserId(paymentOrderModel.getUserId());
								
								phonepePaymentModel.setVendorId(vendorId);
								phonepePaymentModel.insertPayment();
								
								SubscriptionPackageModel spm = SubscriptionPackageModel.getSubscriptionPackageDetailsById(paymentOrderModel.getOrderRefId());
								boolean status = DriverSubscriptionUtils.processDriverSubscription(spm, loggedInuserId, vendorId, ProjectConstants.PAYMENT_TYPE_PHONEPE);
								
								String pushMessage = "";

								if (status) {
									pushMessage = String.format(BusinessAction.messageForKeyAdmin("successPhonepeDriverSubscriptionPaymentSuccess", null), spm.getPackageName(), spm.getPrice());
								} else {
									pushMessage = String.format(BusinessAction.messageForKeyAdmin("errorPhonepeDriverSubscriptionPayment", null), spm.getPackageName());
								}
								
								ApnsDeviceModel apnsDeviceOfDriver = ApnsDeviceModel.getDeviseByUserId(loggedInuserId);

								ApnsMessageModel apnsMessage = new ApnsMessageModel();
								apnsMessage.setMessage(pushMessage);
								apnsMessage.setMessageType("push");
								apnsMessage.setToUserId(loggedInuserId);
								apnsMessage.insertPushMessage();

								if (apnsDeviceOfDriver != null) {
									apnsDeviceOfDriver.sendNotification("1", "Push", pushMessage, ProjectConstants.PAYMENT_CONFIRMATION, WebappPropertyUtils.getWebAppProperty("certificatePath"));
								}
								
								Map<String, Object> finalOutput = new HashMap<>();
								
								finalOutput.put("referenceNo", phonepePaymentModel.getTransactionId());
								finalOutput.put("orderId", phonepePaymentModel.getMerchantTransactionId());
								finalOutput.put("paymentMode", "Phonepe");
								finalOutput.put("amount", phonepePaymentModel.getAmount());
								
								return sendDataResponse(finalOutput);
							}
							
							
						} else {
							
							return sendBussinessError(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_FAILED);
						}


					} else

						return sendBussinessError("Not found record based on merchantTransactionId");

				} else {

					return sendBussinessError("Phonepe Response is empty");
				}

			} else {

				return sendBussinessError("Not reached phonepe check status Api");
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return sendDataResponse(outputMap);
	}
	
	
	
	@Path("/call-back-new")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public void phonepeCallbackNew(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("X-VERIFY") String verifyHeader,
		PhonepeResponseModel phonepeResponseModel
		) throws SQLException {
	//@formatter:on

		if (phonepeResponseModel != null) {
			byte[] decodedBytes = Base64.getDecoder().decode(phonepeResponseModel.getResponse());

			ObjectMapper mapper = new ObjectMapper();
			try {
				PhonepeResponseModel phonepeResp = mapper.readValue(decodedBytes, PhonepeResponseModel.class);

				if ("PAYMENT_SUCCESS".equals(phonepeResp.getCode())) {
					
					PaymentOrdersModel paymentOrderModel = PaymentOrdersModel.getPaymentDetailsByOrderId(phonepeResp.getData().getMerchantTransactionId());
					
					 
						paymentOrderModel.setPaymentStatus(phonepeResp.getCode());
						paymentOrderModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
						
						paymentOrderModel.updatePaymentStatus();
					
					
					PhonepePaymentModel phonepePaymentModel = new PhonepePaymentModel();
					
					phonepePaymentModel.setPaymentOrderId(paymentOrderModel.getPaymentOrderId());
					phonepePaymentModel.setMerchantTransactionId(phonepeResp.getData().getMerchantTransactionId());
					phonepePaymentModel.setUserId(paymentOrderModel.getCreatedBy());
					phonepePaymentModel.setAmount(paymentOrderModel.getAmount());
					phonepePaymentModel.setCode(phonepeResp.getCode());
					phonepePaymentModel.setMessage(phonepeResp.getMessage());
					phonepePaymentModel.setState(phonepeResp.getData().getState());
					phonepePaymentModel.setResponseCode(phonepeResp.getData().getResponseCode());
					phonepePaymentModel.setTransactionId(phonepeResp.getData().getTransactionId());
					
					if (phonepeResp.getData().getPaymentInstrument() != null) {

						phonepePaymentModel.setPaymentInstrumentType(phonepeResp.getData().getPaymentInstrument().getType());
						phonepePaymentModel.setUtr(phonepeResp.getData().getPaymentInstrument().getUtr());
						phonepePaymentModel.setCardType(phonepeResp.getData().getPaymentInstrument().getCardType());
						phonepePaymentModel.setPgTransactionId(phonepeResp.getData().getPaymentInstrument().getPgTransactionId());
						phonepePaymentModel.setBankTransactionId(phonepeResp.getData().getPaymentInstrument().getBankTransactionId());
						phonepePaymentModel.setPgAuthorizationCode(phonepeResp.getData().getPaymentInstrument().getPgAuthorizationCode());
						phonepePaymentModel.setArn(phonepeResp.getData().getPaymentInstrument().getArn());
						phonepePaymentModel.setBankId(phonepeResp.getData().getPaymentInstrument().getBankId());
						phonepePaymentModel.setPgServiceTransactionId(phonepeResp.getData().getPaymentInstrument().getPgServiceTransactionId());
						phonepePaymentModel.setBrn(phonepeResp.getData().getPaymentInstrument().getBrn());
						phonepePaymentModel.setResponseCodeDescription(phonepeResp.getData().getPaymentInstrument().getResponseCodeDescription());
					}
					
					
					
					if (ProjectConstants.OrderDeliveryConstants.ORDER.equals(paymentOrderModel.getOrderType())) {
						
						OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(phonepeResp.getData().getMerchantTransactionId());

						if (orderModel != null) {
							
							phonepePaymentModel.setVendorId(orderModel.getOrderReceivedAgainstVendorId());
							phonepePaymentModel.insertPayment();
							
							orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
							orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
							orderModel.setPaymentMode(ProjectConstants.CASH_ID);
							orderModel.updatePaymentAndOrderStatusForKPMart(orderModel.getCreatedBy());

							OrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);

						}
						
					}else {
						String vendorId = MultiTenantUtils.getVendorIdByUserId(paymentOrderModel.getUserId());
						
						phonepePaymentModel.setVendorId(vendorId);
						phonepePaymentModel.insertPayment();
						
						SubscriptionPackageModel spm = SubscriptionPackageModel.getSubscriptionPackageDetailsById(paymentOrderModel.getOrderRefId());
						boolean status = DriverSubscriptionUtils.processDriverSubscription(spm, paymentOrderModel.getUserId(), vendorId, ProjectConstants.PAYMENT_TYPE_PHONEPE);
						
						String pushMessage = "";

						if (status) {
							pushMessage = String.format(BusinessAction.messageForKeyAdmin("successPhonepeDriverSubscriptionPaymentSuccess", null), spm.getPackageName(), spm.getPrice());
						} else {
							pushMessage = String.format(BusinessAction.messageForKeyAdmin("errorPhonepeDriverSubscriptionPayment", null), spm.getPackageName());
						}
						
						ApnsDeviceModel apnsDeviceOfDriver = ApnsDeviceModel.getDeviseByUserId(paymentOrderModel.getUserId());

						ApnsMessageModel apnsMessage = new ApnsMessageModel();
						apnsMessage.setMessage(pushMessage);
						apnsMessage.setMessageType("push");
						apnsMessage.setToUserId(paymentOrderModel.getUserId());
						apnsMessage.insertPushMessage();

						if (apnsDeviceOfDriver != null) {
							apnsDeviceOfDriver.sendNotification("1", "Push", pushMessage, ProjectConstants.PAYMENT_CONFIRMATION, WebappPropertyUtils.getWebAppProperty("certificatePath"));
						}
					}
					
				}

				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
}	