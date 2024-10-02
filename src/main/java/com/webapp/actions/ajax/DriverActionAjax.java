package com.webapp.actions.ajax;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.revised.DriverAction;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UtilizedUserPromoCodeModel;
import com.webapp.models.WebSocketClient;

@Path("/ajax/admin/revised_v4")
public class DriverActionAjax extends BusinessApiAction {

	Logger logger = Logger.getLogger(DriverActionAjax.class);

	@GET
	@Path("/fare")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response endTour(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
	        @Context ServletContext context,
		@QueryParam("tourId") String tourId,
		@QueryParam("distance") double distance,
		@QueryParam("tollDropAmt") double tollDropAmt,
		@QueryParam("tourTimeAdmin") double tourTimeAdmin
		) throws IOException {
	//@formatter:on

		Map<String, Object> outputMap = new HashMap<String, Object>();

		if (tourId == null) {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("requiredTourId", request));
			return sendDataResponse(outputMap);
		}

		if (tourTimeAdmin == 0 && distance == 0) {
			outputMap.put("status", 207);
			outputMap.put("messageTime", messageForKey("requiredTourTime", request));
			outputMap.put("messageDistance", messageForKey("requiredDistance", request));
			return sendDataResponse(outputMap);
		}

		if (distance == 0) {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("requiredDistance", request));
			return sendDataResponse(outputMap);
		}

		if (tourTimeAdmin == 0) {
			outputMap.put("status", 206);
			outputMap.put("message", messageForKey("requiredTourTime", request));
			return sendDataResponse(outputMap);
		}

		InvoiceModel invoice = new InvoiceModel();

		invoice.setTourId(tourId);
		invoice.setDistance(distance * 1000);
		invoice.setTollAmount(tollDropAmt);

		TourModel tourDetils = TourModel.getTourDetailsByTourId(invoice.getTourId());

		TourTimeModel tourTimeModel = TourTimeModel.getTourTimesDetailsByTourId(invoice.getTourId());

		if (tourTimeModel == null) {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("tourNotValid", request));
			return sendDataResponse(outputMap);
		}

		double tourTime = DateUtils.nowAsGmtMillisec() - tourTimeModel.getStartTime();

		tourTimeAdmin = tourTimeAdmin * (60 * 1000);

		if (tourTimeAdmin > tourTime) {
			outputMap.put("status", 206);
			outputMap.put("message", messageForKey("invalidTourTime", request));
			return sendDataResponse(outputMap);
		}

		tourTime = tourTimeAdmin;

		if (!tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

			FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();
			double arrivedWaitingTime = (tourTimeModel.getStartTime() - tourTimeModel.getArrivedWaitingTime()) - (freeWaitingTimeModel.getWaitingTime() * (60 * 1000));

			arrivedWaitingTime = arrivedWaitingTime > 0 ? arrivedWaitingTime : 0;

			/**
			 * Setting arrived waiting time to waiting time in tour as time fare calculated
			 * on tour time tour waiting time will not be calculated
			 **/

			invoice.setWaitingTime(arrivedWaitingTime);

			double userAvailabelCredits = 0;// as credit considered to be no credits

			Map<String, Object> totalMap = DriverAction.calculateFare(invoice, tourDetils, arrivedWaitingTime, userAvailabelCredits, invoice.getTollAmount(), null, false, 0);

			double finalAmountCollected = (Double) totalMap.get("finalAmountCollected");
			double charges = (Double) totalMap.get("charges");
			double tollAmount = (Double) totalMap.get("tollAmount");
			double usedCredits = (Double) totalMap.get("usedCredits");
			double promoDiscountAmount = (Double) totalMap.get("promoDiscountAmount");

			outputMap.put("total", df.format(finalAmountCollected));
			outputMap.put("charges", df.format(charges));
			outputMap.put("tollAmount", df.format(tollAmount));
			outputMap.put("usedCredits", df.format(usedCredits));
			outputMap.put("promoDiscount", df.format(promoDiscountAmount));

		} else {

			outputMap.put("message", messageForKey("notabletoCalculatefare", request));
		}

		return sendDataResponse(outputMap);
	}

	@GET
	@Path("/ended")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response confrimDropNewTour(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@Context ServletContext context,
		@QueryParam("tourId") String tourId,
		@QueryParam("distance") double distance,
		@QueryParam("tollDropAmt") double tollDropAmt,
		@QueryParam("paymentType") String paymentType,
		@QueryParam("tourTimeAdmin") double tourTimeAdmin
		) throws IOException, SQLException {
	//@formatter:on

		Map<String, Object> outputMap = new HashMap<String, Object>();

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);

		String loggedInuserId = userInfo.get("user_id").toString();

		if (tourId == null) {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("requiredTourId", request));
			return sendDataResponse(outputMap);
		}

		if (tourTimeAdmin == 0 && distance == 0) {
			outputMap.put("status", 207);
			outputMap.put("messageTime", messageForKey("requiredTourTime", request));
			outputMap.put("messageDistance", messageForKey("requiredDistance", request));
			return sendDataResponse(outputMap);
		}

		if (distance == 0) {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("requiredDistance", request));
			return sendDataResponse(outputMap);
		}

		if (tourTimeAdmin == 0) {
			outputMap.put("status", 206);
			outputMap.put("message", messageForKey("requiredTourTime", request));
			return sendDataResponse(outputMap);
		}

		InvoiceModel invoice = new InvoiceModel();
		invoice.setTourId(tourId);
		invoice.setDistance(distance * 1000);
		invoice.setTollAmount(tollDropAmt);
		invoice.setCashReceived(true);

		if (paymentType.equals("byCash")) {
			invoice.setCashReceived(true);
		} else {
			invoice.setCashReceived(false);
		}

		TourModel tourDetils = TourModel.getTourDetailsByTourId(invoice.getTourId());

		String passengerId = tourDetils.getPassengerId();

		UserModel passengerModel = UserModel.getUserAccountDetailsById(passengerId);

		double userAvailabelCredits = 0;

		if (passengerModel != null) {

			userAvailabelCredits = passengerModel.getCredit();
		}

		TourTimeModel tourTimeModel = TourTimeModel.getTourTimesDetailsByTourId(invoice.getTourId());

		// updating end time at first and comment all below updateTourEndTime method
		// used below
		long currentTime = DateUtils.nowAsGmtMillisec();

		// min to millis
		tourTimeAdmin = tourTimeAdmin * 1000;

		double tourTime = currentTime - tourTimeModel.getStartTime();

		if (tourTimeAdmin > tourTime) {
			outputMap.put("status", 206);
			outputMap.put("message", messageForKey("invalidTourTime", request));
			return sendDataResponse(outputMap);
		}

		tourTime = tourTimeAdmin;
		if (tourTimeModel.getEndTime() == 0) {
			tourTimeModel.setEndTime(currentTime);
			DriverAction.updateTourEndTime(invoice);
		}

		if (!tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

			invoice.setDestiAddess(tourDetils.getDestinationAddress());
			invoice.setInitialFare(tourDetils.getInitialFare());
			invoice.setPerKmFare(tourDetils.getPerKmFare());
			invoice.setPerMinuteFare(tourDetils.getPerMinuteFare());
			invoice.setBookingFees(tourDetils.getBookingFees());
			invoice.setDiscount(tourDetils.getDiscount());

			long statusPayment = 0;

			FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();
			double arrivedWaitingTime = (tourTimeModel.getStartTime() - tourTimeModel.getArrivedWaitingTime()) - (freeWaitingTimeModel.getWaitingTime() * (60 * 1000));

			arrivedWaitingTime = arrivedWaitingTime > 0 ? arrivedWaitingTime : 0;

			/**
			 * Setting arrived waiting time to waiting time in tour as time fare calculated
			 * on tour time tour waiting time will not be calculated
			 **/

			invoice.setWaitingTime(arrivedWaitingTime);

			Map<String, Object> totalMap = new HashMap<String, Object>();

			totalMap = DriverAction.calculateFare(invoice, tourDetils, arrivedWaitingTime, userAvailabelCredits, invoice.getTollAmount(), null, false, 0);

			/*****************
			 * fare calculation
			 * 
			 * total = basefare + timefare + distancefare charges = total - promodiscount
			 * finalamountcollected = charges -credits
			 * 
			 * total are stored in invoice.setTotal(total) charges are stored in
			 * invoice.setCharges(charges) which used for report calculation
			 * finalamountcollected are stored in invoice.setFinalAmountCollected
			 **********/

			// double baseFare = (Double) totalMap.get("baseFare"); //intial fare +booking
			// fees
			// double initialFare = (Double) totalMap.get("initialFare");
			// double bookingFees = (Double) totalMap.get("bookingFees");
			double timeFare = (Double) totalMap.get("timeFare");
			double distanceFare = (Double) totalMap.get("distanceFare");
			double usedCredits = (Double) totalMap.get("usedCredits");
			double total = (Double) totalMap.get("total");
			double charges = (Double) totalMap.get("charges");
			double finalAmountCollected = (Double) totalMap.get("finalAmountCollected");
			double driverAmount = (Double) totalMap.get("driverAmount");
			double availabelCredits = (Double) totalMap.get("availabelCredits");
			double promoDiscountAmount = (Double) totalMap.get("promoDiscountAmount");
			double minimumFare = (Double) totalMap.get("minimumFare");
			double tollAmount = (Double) totalMap.get("tollAmount");

			invoice.setPercentage(tourDetils.getPercentage());
			invoice.setDriverAmount(driverAmount);
			invoice.setDistanceFare(distanceFare);
			invoice.setTimeFare(timeFare);
			invoice.setCharges(charges);
			invoice.setTotal(total);
			invoice.setUsedCredits(usedCredits);
			invoice.setFinalAmountCollected(finalAmountCollected);

			if (tourDetils.isPromoCodeApplied()) {
				invoice.setPromoCodeApplied(true);
				invoice.setPromoCodeId(tourDetils.getPromoCodeId());
				invoice.setPromoDiscount(promoDiscountAmount);
			}

			if (!invoice.isCashReceived() && tourDetils.isCardBooking()) {

				statusPayment = 200;

				if (statusPayment != 200) {
					logger.info("\n\n card payment failed= ");
					outputMap.put("paymentStatusCode", 400);
					outputMap.put("amountToCollect", finalAmountCollected);
					outputMap.put("finalAmountCollected", finalAmountCollected);
					outputMap.put("promoCodeDiscount", promoDiscountAmount);
					outputMap.put("usedCredits", usedCredits);
					outputMap.put("walletNegative", availabelCredits);
					outputMap.put("availabelCredits", availabelCredits);
					outputMap.put("invoiceTotal", total);
					outputMap.put("total", total);
					outputMap.put("charges", charges);
					outputMap.put("minimumFare", minimumFare);
					outputMap.put("tollAmount", tollAmount);
					outputMap.put("message", messageForKey("errorPaymentFailedAdmin", request));

					return sendDataResponse(outputMap);
				}

				if (statusPayment == 200) {

					TourUtils.updateTourStatusByTourId(tourId, tourDetils.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

					DriverAction.updateTourEndTime(invoice);

					// make driver free
					DriverAction.updateDriverTourStatus(loggedInuserId);

					// generate invoice
					DriverAction.generateInvoiceNew(invoice, tourDetils, loggedInuserId, ProjectConstants.CARD, charges, total);
					// process csv
					DriverAction.processCsvThread(invoice);

					// send Email
					// DriverAction.sendEmail(tourDetils);

					UserProfileModel userProfile = new UserProfileModel();

					userProfile.setUserId(tourDetils.getPassengerId());
					userProfile.setCredit(availabelCredits);
					userProfile.updateUserCredits();

					if (tourDetils.isPromoCodeApplied()) {
						UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();

						utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
						utilizedUserPromoCodeModel.setUserId(tourDetils.getPassengerId());
						utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);
					}

					outputMap.put("paymentStatusCode", 200);
					outputMap.put("amountToCollect", finalAmountCollected);
					outputMap.put("finalAmountCollected", finalAmountCollected);
					outputMap.put("promoCodeDiscount", promoDiscountAmount);
					outputMap.put("usedCredits", usedCredits);
					outputMap.put("walletNegative", availabelCredits);
					outputMap.put("availabelCredits", availabelCredits);
					// outputMap.put("remainingPromoDiscount", remainingPromoDiscount);
					outputMap.put("invoiceTotal", total);
					outputMap.put("total", total);
					outputMap.put("charges", charges);
					outputMap.put("minimumFare", minimumFare);
					outputMap.put("tollAmount", tollAmount);

					// send SMS
					sendSMS(tourDetils, true, outputMap);

					outputMap.put("message", messageForKey("successCardPayment", request));

					return sendDataResponse(outputMap);

				} else {

					outputMap.put("paymentStatusCode", 400);
					outputMap.put("amountToCollect", finalAmountCollected);
					outputMap.put("finalAmountCollected", finalAmountCollected);
					outputMap.put("promoCodeDiscount", promoDiscountAmount);
					outputMap.put("usedCredits", usedCredits);
					outputMap.put("walletNegative", availabelCredits);
					outputMap.put("availabelCredits", availabelCredits);
					// outputMap.put("remainingPromoDiscount", remainingPromoDiscount);
					outputMap.put("invoiceTotal", total);
					outputMap.put("total", total);
					outputMap.put("charges", charges);
					outputMap.put("minimumFare", minimumFare);
					outputMap.put("tollAmount", tollAmount);
					outputMap.put("message", messageForKey("errorPaymentFailedAdmin", request));

					return sendDataResponse(outputMap);
				}

			} else if (!invoice.isCashReceived() && !tourDetils.isCardBooking()) {

				outputMap.put("paymentStatusCode", 201);
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				outputMap.put("invoiceTotal", total);
				outputMap.put("total", total);
				outputMap.put("charges", charges);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);
				outputMap.put("message", messageForKey("cabChargeCollectAdmin", request));

				return sendDataResponse(outputMap);

			} else if (invoice.isCashReceived()) {

				TourUtils.updateTourStatusByTourId(tourId, tourDetils.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

				// update tour end time
				DriverAction.updateTourEndTime(invoice);

				// make driver free
				DriverAction.updateDriverTourStatus(loggedInuserId);

				// generate invoice
				DriverAction.generateInvoiceNew(invoice, tourDetils, loggedInuserId, ProjectConstants.CASH, charges, total);
				// process csv
				DriverAction.processCsvThread(invoice);

				// send Email
				// DriverAction.sendEmail(tourDetils);

				outputMap.put("paymentStatusCode", 200);
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				// outputMap.put("remainingPromoDiscount", remainingPromoDiscount);
				outputMap.put("invoiceTotal", total);
				outputMap.put("total", total);
				outputMap.put("charges", charges);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);

				// send SMS
				sendSMS(tourDetils, true, outputMap);

				outputMap.put("message", messageForKey("successCashTripEndedAdmin", request));

				UserProfileModel userProfile = new UserProfileModel();

				userProfile.setUserId(tourDetils.getPassengerId());
				userProfile.setCredit(availabelCredits);
				userProfile.updateUserCredits();

				if (tourDetils.isPromoCodeApplied()) {
					UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();

					utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
					utilizedUserPromoCodeModel.setUserId(tourDetils.getPassengerId());
					utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);
				}

				return sendDataResponse(outputMap);
			} else {
				outputMap.put("message", messageForKey("errorFailedToEndTrip", request));
				return sendDataResponse(outputMap);
			}
		} else {

			outputMap.put("message", messageForKey("notabletoCalculatefare", request));
		}

		return sendDataResponse(outputMap);
	}

	@GET
	@Path("/canceled")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response confrimToCancelTour(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@Context ServletContext context,
		@QueryParam("tourId") String tourId
		) throws IOException,  SQLException {
	//@formatter:on

		Map<String, Object> outputMap = new HashMap<String, Object>();

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		TourModel tourDetils = TourModel.getTourDetailsByTourId(tourId);

		if (tourDetils == null) {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("errorInvalidTrip", request));
			return sendDataResponse(outputMap);
		}

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		if (!tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && !tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {

			if (tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_ADMIN, loggedInUserId);

				// make driver free
				DriverTourStatusUtils.updateDriverTourStatus(tourDetils.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

				// update charges to 0
				tourDetils.setCharges(0);
				tourDetils.updateCharges();

				return sendSuccessMessage(messageForKey("successTripCancelled", request));

			} else if (tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) || tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)
						|| tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING) || tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR)
						|| tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR) || tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR)
						|| tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST) || tourDetils.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST)) {

				// update charges to 0
				TourModel tourDetils2 = new TourModel();
				tourDetils2.setTourId(tourId);
				tourDetils2.setCharges(0);
				tourDetils2.updateCharges();

				TourUtils.updateTourStatusByTourId(tourId, tourDetils.getDriverId(), ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_ADMIN);

				String message = BusinessAction.messageForKeyAdmin("errorAdminCancelledTrip", tourDetils.getLanguage());
				String bo_message = BusinessAction.messageForKeyAdmin("errorAdminCancelledTrip", adminSmsSendingModel.getLanguage());

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetils.getPassengerId());

				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setMessage(message);
				apnsMessage.setMessageType("push");
				apnsMessage.setToUserId(tourDetils.getPassengerId());
				apnsMessage.insertPushMessage();

				if (adminSmsSendingModel.ispCancelledByDriver()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetils.getpPhoneCode() + tourDetils.getpPhone(), null);
				}

				UserModel user = UserModel.getUserAccountDetailsById(tourDetils.getCreatedBy());

				//@formatter:off
					if ((tourDetils.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
							&& tourDetils.getCardOwner() != null && tourDetils.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
								|| (tourDetils.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
								&& tourDetils.getCardOwner() != null && tourDetils.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
					//@formatter:on

					if (user != null) {
						MetamorphSystemsSmsUtils.sendSmsToSingleUser(bo_message, user.getPhoneNoCode() + user.getPhoneNo(), null);
					}
				}

				if (apnsDevice != null) {
					apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_ADMIN, WebappPropertyUtils.getWebAppProperty("certificatePath"));
				}

				if (!tourDetils.getDriverId().equals("-1")) {

					sendDriverNotification(tourDetils.getDriverId(), messageForKey("successTripCancelledByAdmin", request));

					// make driver free
					DriverTourStatusUtils.updateDriverTourStatus(tourDetils.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);
				}

				UserModel userDriver = UserModel.getUserAccountDetailsById(tourDetils.getDriverId());

				String messageDriver = BusinessAction.messageForKeyAdmin("errorAdminCancelledTrip", adminSmsSendingModel.getLanguage());

				if (adminSmsSendingModel.isdCancelledByPassengerBusinessO()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(messageDriver, userDriver.getPhoneNoCode() + userDriver.getPhoneNo(), null);
				}

				outputMap.put("status", 200);
				outputMap.put("message", messageForKey("successTripCancelled", request));
				return sendDataResponse(outputMap);

			} else {
				outputMap.put("status", 205);
				outputMap.put("message", messageForKey("errorFailedToCancelTrip", request));
				return sendDataResponse(outputMap);
			}

		} else {
			outputMap.put("status", 205);
			outputMap.put("message", messageForKey("errorFailedToCancelAlreadyCanceled", request));
			return sendDataResponse(outputMap);
		}

	}

	private void sendDriverNotification(String driverId, String message) {

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);
		String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.CBA_SOCKET;
		WebSocketClient.sendDriverNotification(messge, driverId, apnsDevice.getApiSessionKey());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(driverId);
		apnsMessage.insertPushMessage();
	}

	private void sendSMS(TourModel tourDetils, boolean adminTripEnd, Map<String, Object> outputMap) {

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		// send notification
		String message = sendNotification(tourDetils, adminTripEnd, outputMap);

		UserModel userB = UserModel.getUserAccountDetailsById(tourDetils.getCreatedBy());

		//@formatter:off
		if ((tourDetils.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
				&& tourDetils.getCardOwner() != null && tourDetils.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
					|| (tourDetils.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
					&& tourDetils.getCardOwner() != null && tourDetils.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
		//@formatter:on

			String bo_message = BusinessAction.messageForKeyAdmin("successTripCompleted", tourDetils.getLanguage());

			if (adminSmsSendingModel.isBoInvoice()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(bo_message, tourDetils.getPhoneNoCode() + tourDetils.getpPhone(), ProjectConstants.SMSConstants.SMS_EINVOICE_GENERATED_TEMPLATE_ID);
			}

			if (userB != null) {
				if (adminSmsSendingModel.isBoInvoice()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(bo_message, userB.getPhoneNoCode() + userB.getPhoneNo(), ProjectConstants.SMSConstants.SMS_EINVOICE_GENERATED_TEMPLATE_ID);
				}
			}

		} else {

			if (adminSmsSendingModel.ispInvoice()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetils.getpPhoneCode() + tourDetils.getpPhone(), ProjectConstants.SMSConstants.SMS_EINVOICE_GENERATED_TEMPLATE_ID);
			}
		}
	}

	private String sendNotification(TourModel tourDetils, boolean adminTripEnd, Map<String, Object> outputMap) {

		String message = BusinessAction.messageForKeyAdmin("successTripCompleted", tourDetils.getLanguage());

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetils.getPassengerId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(tourDetils.getPassengerId());
		apnsMessage.insertPushMessage();

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.ENDED_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}

		if (adminTripEnd) {

			outputMap.put("tourId", tourDetils.getTourId());
			outputMap.put("status", ProjectConstants.TourStatusConstants.ENDED_TOUR);

			tourDetils.setStatus(ProjectConstants.TourStatusConstants.ENDED_TOUR);

			ApnsDeviceModel apnsDeviceDriver = ApnsDeviceModel.getDeviseByUserId(tourDetils.getDriverId());

			JSONObject jsonMessage = new JSONObject(tourDetils);

			String messge = "TOUR`1`" + apnsDeviceDriver.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.AET_SOCKET + jsonMessage;

			WebSocketClient.sendDriverNotification(messge, tourDetils.getDriverId(), apnsDeviceDriver.getApiSessionKey());
		}

		return message;
	}

}