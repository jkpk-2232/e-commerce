package com.webapp.actions.api.revised;

import java.io.IOException;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.UUIDGenerator;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.DriverReferralCodeLogModel;
import com.webapp.models.DriverVendorsModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.ProcessCsvThread;
import com.webapp.models.ProcessInvoiceEmailThread;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TaxModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourReferrerBenefitModel;
import com.webapp.models.TourTaxModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UtilizedUserPromoCodeModel;

@Path("/api/driver")
public class DriverAction extends BusinessApiAction {

	private static final String TOUR_ID = "tourId";
	private static final String TOUR_ID_LABEL = "Tour Id";

	private static final String DISTANCE = "distance";
	private static final String DISTANCE_LABEL = "Distance";

	private static final String WAITING_TIME = "waitingTime";
	private static final String WAITING_TIME_LABEL = "Waiting Time";

	@Path("/ended_revised/ended")
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response endedTourRevisedV5(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			InvoiceModel invoice
			) throws SQLException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = invoiceModelValidation(invoice);

		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		df.setRoundingMode(RoundingMode.DOWN);

		TourModel tourDetails = TourModel.getTourDetailsByTourId(invoice.getTourId());
		String passengerId = tourDetails.getPassengerId();

		UserModel passengerModel = UserModel.getUserAccountDetailsById(passengerId);

		double userAvailabelCredits = 0;

		if (passengerModel != null) {

			userAvailabelCredits = passengerModel.getCredit();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();

		TourTimeModel tourTimeModel = TourTimeModel.getTourTimesDetailsByTourId(invoice.getTourId());

		if (((DateUtils.nowAsGmtMillisec() - tourTimeModel.getStartTime())) < invoice.getWaitingTime()) {

			outputMap.put("message", messageForKey("errorWaitingTime", request));

			return sendDataResponse(outputMap);
		}

		if (!tourDetails.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			long currentTime = DateUtils.nowAsGmtMillisec();

			if (tourTimeModel.getEndTime() == 0) {

				tourTimeModel.setEndTime(currentTime);
				updateTourEndTime(invoice);

			}

			invoice.setDestiAddess(tourDetails.getDestinationAddress());
			invoice.setInitialFare(tourDetails.getInitialFare());
			invoice.setPerKmFare(tourDetails.getPerKmFare());
			invoice.setPerMinuteFare(tourDetails.getPerMinuteFare());
			invoice.setBookingFees(tourDetails.getBookingFees());
			invoice.setDiscount(tourDetails.getDiscount());
			invoice.setMarkupFare(tourDetails.getMarkupFare());

			FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();

			double arrivedWaitingTime = 0;

			Map<String, Object> totalMap = new HashMap<String, Object>();

			List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

			boolean isDriverReferralApplied = true;
			double referrerDriverPercentage = adminSettingsModel.getDriverReferralBenefit();

			// Check speed and update distance ------------------------------------
			double tripDuration = currentTime - tourTimeModel.getStartTime();

			double avgSpeed = ((invoice.getDistance() / (tripDuration / 1000)) * 3.6);

			if (avgSpeed > adminSettingsModel.getAverageSpeed()) {

				if (tourDetails.isRentalBooking()) {
					invoice.setDistance(tourDetails.getFreeDistance());
				} else {
					invoice.setDistance(tourDetails.getDistance());
				}

			}
			// ---------------------------------------------------------------------

			if (tourDetails.isRentalBooking()) {

				if (tourTimeModel.getArrivedWaitingTime() < tourTimeModel.getBookingTime()) {

					double totalTime = currentTime - (tourTimeModel.getBookingTime() + (freeWaitingTimeModel.getWaitingTime() * (60 * 1000)));

					if ((totalTime > tourDetails.getRentalPackageTime()) && (totalTime > 0)) {
						invoice.setDuration(totalTime);
					} else {
						invoice.setDuration(currentTime - tourTimeModel.getStartTime());
					}

				} else if (tourTimeModel.getArrivedWaitingTime() > tourTimeModel.getBookingTime()) {

					double totalTime = currentTime - (tourTimeModel.getArrivedWaitingTime() + (freeWaitingTimeModel.getWaitingTime() * (60 * 1000)));

					if ((totalTime > tourDetails.getRentalPackageTime()) && (totalTime > 0)) {
						invoice.setDuration(totalTime);
					} else {
						invoice.setDuration(currentTime - tourTimeModel.getStartTime());
					}

				} else {

					invoice.setDuration(currentTime - tourTimeModel.getStartTime());
				}

				invoice.setArrivedWaitingTime(0);

				invoice.setWaitingTime(0);

				totalMap = calculateFareForRentalBooking(invoice, tourDetails, arrivedWaitingTime, userAvailabelCredits, invoice.getTollAmount(), taxModelList, isDriverReferralApplied, referrerDriverPercentage);

			} else {

				arrivedWaitingTime = (tourTimeModel.getStartTime() - tourTimeModel.getArrivedWaitingTime()) - (freeWaitingTimeModel.getWaitingTime() * (60 * 1000));

				arrivedWaitingTime = arrivedWaitingTime > 0 ? arrivedWaitingTime : 0;

				invoice.setArrivedWaitingTime(arrivedWaitingTime);

				invoice.setDuration(currentTime - tourTimeModel.getStartTime());

				invoice.setWaitingTime(0);

				if (tourDetails.isAirportFixedFareApplied()) {
					invoice.setDistance(tourDetails.getDistance());
				}

				totalMap = calculateFare(invoice, tourDetails, arrivedWaitingTime, userAvailabelCredits, invoice.getTollAmount(), taxModelList, isDriverReferralApplied, referrerDriverPercentage);
			}

			double baseFare = (Double) totalMap.get("baseFare"); // intial fare +booking fees
			double initialFare = (Double) totalMap.get("initialFare");
			double bookingFees = (Double) totalMap.get("bookingFees");
			double timeFare = (Double) totalMap.get("timeFare");
			double distanceFare = (Double) totalMap.get("distanceFare");
			double usedCredits = (Double) totalMap.get("usedCredits");

			double total = (Double) totalMap.get("total");
			double charges = (Double) totalMap.get("charges");
			double subTotal = (Double) totalMap.get("total");

			double arrivedWaitingTimeFare = (Double) totalMap.get("arrivedWaitingTimeFare");
			double finalAmountForTaxCalculation = (Double) totalMap.get("finalAmountForTaxCalculation");
			double totalTaxAmount = (Double) totalMap.get("totalTaxAmount");

			double referrerDriverAmount = (Double) totalMap.get("referrerDriverAmount");

			invoice.setCharges(charges);
			invoice.setTotal(total);
			invoice.setSubTotal(total);

			double finalAmountCollected = (Double) totalMap.get("finalAmountCollected");
			double driverAmount = (Double) totalMap.get("driverAmount");
			double availabelCredits = (Double) totalMap.get("availabelCredits");
			double promoDiscountAmount = (Double) totalMap.get("promoDiscountAmount");
			double minimumFare = (Double) totalMap.get("minimumFare");
			double tollAmount = (Double) totalMap.get("tollAmount");

			double totalWithSurge = (Double) totalMap.get("totalWithSurge");
			double surgeFare = (Double) totalMap.get("surgeFare");

			double distBeforeSpecificKmInMeters = (Double) totalMap.get("distBeforeSpecificKmInMeters");
			double distAfterSpecificKmInMeters = (Double) totalMap.get("distAfterSpecificKmInMeters");
			double distanceFareBeforeSpecificKm = (Double) totalMap.get("distanceFareBeforeSpecificKm");
			double distanceFareAfterSpecificKm = (Double) totalMap.get("distanceFareAfterSpecificKm");

			double totalForDemandSupplier = (Double) totalMap.get("totalForDemandSupplier");
			double driverAmountForDemandSupplier = (Double) totalMap.get("driverAmountForDemandSupplier");

			invoice.setBaseFare(baseFare);
			invoice.setInitialFare(initialFare);
			invoice.setBookingFees(bookingFees);
			invoice.setPercentage(tourDetails.getPercentage());
			invoice.setDriverAmount(driverAmount);
			invoice.setDistanceFare(distanceFare);
			invoice.setTimeFare(timeFare);
			invoice.setUsedCredits(usedCredits);
			invoice.setFinalAmountCollected(finalAmountCollected);

			invoice.setUpdatedAmountCollected(finalAmountCollected);

			invoice.setTotalWithSurge(totalWithSurge);
			invoice.setSurgeFare(surgeFare);

			invoice.setTotalTaxAmount(totalTaxAmount);
			invoice.setArrivedWaitingTimeFare(arrivedWaitingTimeFare);

			invoice.setDistanceBeforeSpecificKm(distBeforeSpecificKmInMeters);
			invoice.setDistanceAfterSpecificKm(distAfterSpecificKmInMeters);
			invoice.setDistanceFareBeforeSpecificKm(distanceFareBeforeSpecificKm);
			invoice.setDistanceFareAfterSpecificKm(distanceFareAfterSpecificKm);

			tourDetails.setFinalAmountCollected(finalAmountCollected);

			if (taxModelList != null && finalAmountForTaxCalculation > 0) {
				updateTaxDetails(taxModelList, finalAmountForTaxCalculation, tourDetails.getTourId(), loggedInuserId);
			}

			addReferralLogs(tourDetails, isDriverReferralApplied, referrerDriverPercentage, referrerDriverAmount, loggedInuserId);

			if (tourDetails.isPromoCodeApplied()) {
				invoice.setPromoCodeApplied(true);
				invoice.setPromoCodeId(tourDetails.getPromoCodeId());
			}

			//@formatter:off
			if ((tourDetails.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING))) {
			//@formatter:on

				TourUtils.updateTourStatusByTourId(tourDetails.getTourId(), tourDetails.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

				updateDriverTourStatus(loggedInuserId);

				invoice.setPromoDiscount(promoDiscountAmount);
				invoice.setUsedCredits(usedCredits);

				generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.CASH, charges, total);

				processCsvThread(invoice);

				sendSMS(tourDetails);

				sendEmail(tourDetails);

				outputMap.put("paymentStatusCode", 200);
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				outputMap.put("invoiceTotal", total);
				outputMap.put("subTotal", subTotal);
				outputMap.put("total", charges);
				outputMap.put("charges", total);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);
				outputMap.put("message", messageForKey("successTripEnded", request));

				UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();

				utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
				utilizedUserPromoCodeModel.setUserId(tourDetails.getPassengerId());
				utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);

				MultiTenantUtils.processDemandSupplierAmount(invoice.getTourId(), totalForDemandSupplier, driverAmountForDemandSupplier);

				return sendDataResponse(outputMap);
			}

			if (finalAmountCollected == 0) {

				TourUtils.updateTourStatusByTourId(tourDetails.getTourId(), tourDetails.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

				updateDriverTourStatus(loggedInuserId);

				invoice.setPromoDiscount(promoDiscountAmount);
				invoice.setUsedCredits(usedCredits);

				UserProfileModel userProfile = new UserProfileModel();

				userProfile.setUserId(tourDetails.getPassengerId());
				userProfile.setCredit(availabelCredits);
				userProfile.updateUserCredits();

				List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();
				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
				apnsMessage.setCreatedAt(DateUtils.nowAsGmtMillisec());
				apnsMessage.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				apnsMessage.setToUserId(passengerId);
				apnsMessage.setMessageType("push");

				String message = "";

				message = messageForKey("labelForTripId", request) + " " + tourDetails.getUserTourId() + ", " + messageForKey("labelAmountCashCurrency", request) + df.format(usedCredits) + " " + messageForKey("labelHasBeenDebitedFromCredits", request);
				apnsMessage.setMessage(message);

				if (ProjectConstants.CASH_ID.equals(tourDetails.getPaymentType())) {
					generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.CASH, charges, total);
				}

				if (ProjectConstants.CARD_ID.equals(tourDetails.getPaymentType())) {
					generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.CARD, charges, total);
				}

				if (ProjectConstants.WALLET_ID.equals(tourDetails.getPaymentType())) {
					generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.WALLET, charges, total);
				}

				processCsvThread(invoice);

				sendSMS(tourDetails);

				sendEmail(tourDetails);

				apnsMessageList.add(apnsMessage);

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(passengerId);

				ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);

				if (apnsDevice != null) {
					apnsDevice.sendNotification("1", "Push", message, ProjectConstants.WALLET_DEPOSIT, WebappPropertyUtils.getWebAppProperty("certificatePath"));
				}

				outputMap.put("paymentStatusCode", 200);
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				outputMap.put("invoiceTotal", total);
				outputMap.put("subTotal", subTotal);
				outputMap.put("total", charges);
				outputMap.put("charges", total);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);
				outputMap.put("message", messageForKey("successTripEnded", request));

				UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();

				utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
				utilizedUserPromoCodeModel.setUserId(tourDetails.getPassengerId());
				utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);

				MultiTenantUtils.processDemandSupplierAmount(invoice.getTourId(), totalForDemandSupplier, driverAmountForDemandSupplier);

				return sendDataResponse(outputMap);
			}

			switch (tourDetails.getPaymentType()) {

			case ProjectConstants.CASH_ID:

				TourUtils.updateTourStatusByTourId(tourDetails.getTourId(), tourDetails.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

				updateDriverTourStatus(loggedInuserId);

				invoice.setPromoDiscount(promoDiscountAmount);
				invoice.setUsedCredits(usedCredits);

				double extraCredits = 0;

				List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();
				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
				apnsMessage.setCreatedAt(DateUtils.nowAsGmtMillisec());
				apnsMessage.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				apnsMessage.setToUserId(passengerId);
				apnsMessage.setMessageType("push");

				String message = "";

				if (invoice.getCashCollected() > invoice.getCashToBeCollected()) {

					extraCredits = invoice.getCashCollected() - invoice.getCashToBeCollected();
					extraCredits = roundToDecimalOnly(extraCredits, 2);

					message = messageForKey("labelForTripId", request) + " " + tourDetails.getUserTourId() + ", " + messageForKey("labelAmountCash", request) + " " + extraCredits + " " + messageForKey("labelHasBeenCredited", request);
					apnsMessage.setMessage(message);

				} else {

					extraCredits = invoice.getCashCollected() - invoice.getCashToBeCollected();
					extraCredits = roundToDecimalOnly(extraCredits, 2);

					String amountString = "";

					if (usedCredits < 0) {

						amountString = "" + df_new.format(usedCredits);
						amountString = amountString.replaceFirst("-", "");
						amountString = "- " + adminSettingsModel.getCurrencySymbol() + amountString;

					} else {

						amountString = adminSettingsModel.getCurrencySymbol() + df_new.format(usedCredits);
					}

					message = messageForKey("labelForTripId", request) + " " + tourDetails.getUserTourId() + ", " + messageForKey("labelAmountCash", request) + " " + amountString + " " + messageForKey("labelHasBeenDebitedFromCredits", request);

					apnsMessage.setMessage(message);
				}

				apnsMessageList.add(apnsMessage);

				if (usedCredits != 0) {

					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(passengerId);

					ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);

					if (apnsDevice != null) {
						apnsDevice.sendNotification("1", "Push", message, ProjectConstants.WALLET_DEPOSIT, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					}
				}

				generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.CASH, charges, total);

				processCsvThread(invoice);

				sendSMS(tourDetails);

				sendEmail(tourDetails);

				outputMap.put("paymentStatusCode", 201);
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				outputMap.put("invoiceTotal", total);
				outputMap.put("subTotal", subTotal);
				outputMap.put("total", total);
				outputMap.put("charges", charges);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);
				outputMap.put("message", messageForKey("successTripEnded", request));

				List<Map<String, Object>> taxOutPutMapList = new ArrayList<Map<String, Object>>();

				List<TourTaxModel> tourTaxModelList = TourTaxModel.getTourTaxListByTourId(tourDetails.getTourId());

				for (TourTaxModel tourTaxModel : tourTaxModelList) {

					Map<String, Object> taxOutPutMap = new HashMap<String, Object>();

					taxOutPutMap.put("tourTaxId", tourTaxModel.getTourTaxId());
					taxOutPutMap.put("taxId", tourTaxModel.getTaxId());
					taxOutPutMap.put("taxName", tourTaxModel.getTaxName());
					taxOutPutMap.put("taxPercentage", tourTaxModel.getTaxPercentage());
					taxOutPutMap.put("taxAmount", tourTaxModel.getTaxAmount());

					taxOutPutMapList.add(taxOutPutMap);
				}

				outputMap.put("taxList", taxOutPutMapList);
				outputMap.put("totalTaxAmount", totalTaxAmount);

				UserProfileModel userProfile = new UserProfileModel();

				userProfile.setUserId(tourDetails.getPassengerId());
				userProfile.setCredit(availabelCredits + extraCredits);
				userProfile.updateUserCredits();

				UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();

				utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
				utilizedUserPromoCodeModel.setUserId(tourDetails.getPassengerId());
				utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);

				MultiTenantUtils.processDemandSupplierAmount(invoice.getTourId(), totalForDemandSupplier, driverAmountForDemandSupplier);
				
				ApnsDeviceModel passengerDevice = ApnsDeviceModel.getDeviseByUserId(passengerId);
				if(passengerDevice != null) {
					passengerDevice.sendFCMNotification("1", "Push", ProjectConstants.TourStatusConstants.ENDED_TOUR, message);
				}
				
				
				return sendDataResponse(outputMap);

			case ProjectConstants.CARD_ID:

				TourUtils.updateTourStatusByTourId(tourDetails.getTourId(), tourDetails.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

				updateDriverTourStatus(loggedInuserId);

				invoice.setPromoDiscount(promoDiscountAmount);
				invoice.setUsedCredits(usedCredits);

				generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.CARD, charges, total);

				String amountString = "";
				if (usedCredits < 0) {

					amountString = "" + df_new.format(usedCredits);
					amountString = amountString.replaceFirst("-", "");
					amountString = "- " + adminSettingsModel.getCurrencySymbol() + amountString;

				} else {

					amountString = adminSettingsModel.getCurrencySymbol() + df_new.format(usedCredits);
				}

				message = messageForKey("labelForTripId", request) + " " + tourDetails.getUserTourId() + ", " + messageForKey("labelAmountCash", request) + amountString + " " + messageForKey("labelHasBeenDebitedFromCredits", request);

				if (usedCredits != 0) {

					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

					apnsMessage = new ApnsMessageModel();
					apnsMessage.setMessage(message);
					apnsMessage.setMessageType("push");
					apnsMessage.setToUserId(tourDetails.getPassengerId());
					apnsMessage.insertPushMessage();

					if (apnsDevice != null) {
						apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.ENDED_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					}
				}

				processCsvThread(invoice);

				sendSMS(tourDetails);

				sendEmail(tourDetails);

				userProfile = new UserProfileModel();
				userProfile.setUserId(tourDetails.getPassengerId());
				userProfile.setCredit(availabelCredits);
				userProfile.updateUserCredits();

				utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();
				utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
				utilizedUserPromoCodeModel.setUserId(tourDetails.getPassengerId());
				utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);

				outputMap.put("paymentStatusCode", 200);
				outputMap.put("message", messageForKey("successTripEnded", request));
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				outputMap.put("invoiceTotal", total);
				outputMap.put("subTotal", subTotal);
				outputMap.put("total", total);
				outputMap.put("charges", charges);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);

				taxOutPutMapList = new ArrayList<Map<String, Object>>();

				tourTaxModelList = TourTaxModel.getTourTaxListByTourId(tourDetails.getTourId());

				for (TourTaxModel tourTaxModel : tourTaxModelList) {

					Map<String, Object> taxOutPutMap = new HashMap<String, Object>();

					taxOutPutMap.put("tourTaxId", tourTaxModel.getTourTaxId());
					taxOutPutMap.put("taxId", tourTaxModel.getTaxId());
					taxOutPutMap.put("taxName", tourTaxModel.getTaxName());
					taxOutPutMap.put("taxPercentage", tourTaxModel.getTaxPercentage());
					taxOutPutMap.put("taxAmount", tourTaxModel.getTaxAmount());

					taxOutPutMapList.add(taxOutPutMap);
				}

				outputMap.put("taxList", taxOutPutMapList);
				outputMap.put("totalTaxAmount", totalTaxAmount);

				MultiTenantUtils.processDemandSupplierAmount(invoice.getTourId(), totalForDemandSupplier, driverAmountForDemandSupplier);

				return sendDataResponse(outputMap);

			case ProjectConstants.WALLET_ID:

				TourUtils.updateTourStatusByTourId(tourDetails.getTourId(), tourDetails.getDriverId(), ProjectConstants.TourStatusConstants.ENDED_TOUR);

				updateDriverTourStatus(loggedInuserId);

				invoice.setPromoDiscount(promoDiscountAmount);
				invoice.setUsedCredits(usedCredits);

				generateInvoiceNew(invoice, tourDetails, loggedInuserId, ProjectConstants.WALLET, charges, total);

				processCsvThread(invoice);

				sendSMS(tourDetails);

				sendEmail(tourDetails);

				userProfile = new UserProfileModel();
				userProfile.setUserId(tourDetails.getPassengerId());
				userProfile.setCredit(availabelCredits);
				userProfile.updateUserCredits();

				utilizedUserPromoCodeModel = new UtilizedUserPromoCodeModel();
				utilizedUserPromoCodeModel.setPromoCodeId(invoice.getPromoCodeId());
				utilizedUserPromoCodeModel.setUserId(tourDetails.getPassengerId());
				utilizedUserPromoCodeModel.addUtilizedUserPromoCodeList(loggedInuserId);

				outputMap.put("paymentStatusCode", 200);
				outputMap.put("amountToCollect", finalAmountCollected);
				outputMap.put("finalAmountCollected", finalAmountCollected);
				outputMap.put("promoCodeDiscount", promoDiscountAmount);
				outputMap.put("usedCredits", usedCredits);
				outputMap.put("walletNegative", availabelCredits);
				outputMap.put("availabelCredits", availabelCredits);
				outputMap.put("invoiceTotal", total);
				outputMap.put("subTotal", subTotal);
				outputMap.put("total", total);
				outputMap.put("charges", charges);
				outputMap.put("minimumFare", minimumFare);
				outputMap.put("tollAmount", tollAmount);
				outputMap.put("message", messageForKey("successCardPayment", request));

				MultiTenantUtils.processDemandSupplierAmount(invoice.getTourId(), totalForDemandSupplier, driverAmountForDemandSupplier);

				return sendDataResponse(outputMap);

			default:

				return sendBussinessError(messageForKey("errorFailedToEndTrip", request));
			}
		}

		return sendSuccessMessage(messageForKey("successTripAlreadyEnded", request));
	}

	public static Map<String, Object> calculateFare(InvoiceModel inputInvoiceModel, TourModel tourModel, double arrivedWaitingTime, double userAvailabelCredits, double tollAmount, List<TaxModel> taxModelList, boolean isDriverReferralApplied, double referrerDriverPercentage) {

		/*****************
		 * fare calculation
		 * 
		 * total = basefare + timefare + distancefare charges = total - promodiscount
		 * finalamountcollected = charges -credits
		 * 
		 **********/

		Map<String, Object> outputMap = new HashMap<String, Object>();

		double baseFare = 0.0;
		double initialFare = 0.0;
		double bookingFees = 0.0;
		double timeFare = 0.0;
		double distanceFare = 0.0;
		double usedCredits = 0.0;
		double total = 0.0;
		double charges = 0.0;
		double finalAmountCollected = 0.0;
		double driverAmount = 0.0;
		double availabelCredits = 0.0;
		double timeInMin = 0.0;
		double promoDiscountAmount = 0.0;
		double minimumFare = 0.0;
		double totalWithSurge = 0.0;
		double surgeFare = 0.0;
		double finalAmountForTaxCalculation = 0.0;
		double totalTaxAmount = 0.0;
		double arrivedWaitingTimeFare = 0.0;

		double referrerDriverAmount = 0.0;

		double distBeforeSpecificKmInMeters = 0.0;
		double distAfterSpecificKmInMeters = 0.0;
		double distanceFareBeforeSpecificKm = 0.0;
		double distanceFareAfterSpecificKm = 0.0;

		minimumFare = tourModel.getMinimumFare();

		initialFare = tourModel.getInitialFare();

		bookingFees = tourModel.getBookingFees();

		arrivedWaitingTime = millisToMin(inputInvoiceModel.getArrivedWaitingTime());

		timeInMin = millisToMin(inputInvoiceModel.getDuration());

		availabelCredits = userAvailabelCredits;

		double totalDistanceInMeter = inputInvoiceModel.getDistance() - tourModel.getFreeDistance();

		if (totalDistanceInMeter < 0) {

			totalDistanceInMeter = 0;
		}

		double distBeforeSpecificKmInProjectUnit = 0;
		double distAfterSpecificKmInProjectUnit = 0;

		if (totalDistanceInMeter > tourModel.getKmToIncreaseFare()) {

			distBeforeSpecificKmInMeters = tourModel.getKmToIncreaseFare();
			distAfterSpecificKmInMeters = (totalDistanceInMeter - tourModel.getKmToIncreaseFare());

		} else {

			distBeforeSpecificKmInMeters = totalDistanceInMeter;
		}

		if (distBeforeSpecificKmInMeters > 0) {
			distBeforeSpecificKmInProjectUnit = getDistanceInProjectUnitFromMeters(distBeforeSpecificKmInMeters);
		} else {
			distBeforeSpecificKmInMeters = 0;
		}

		if (distAfterSpecificKmInMeters > 0) {
			distAfterSpecificKmInProjectUnit = getDistanceInProjectUnitFromMeters(distAfterSpecificKmInMeters);
		} else {
			distAfterSpecificKmInMeters = 0;
		}

		distanceFareBeforeSpecificKm = (distBeforeSpecificKmInProjectUnit > 0 ? distBeforeSpecificKmInProjectUnit : 0) * tourModel.getPerKmFare();

		distanceFareAfterSpecificKm = (distAfterSpecificKmInProjectUnit > 0 ? distAfterSpecificKmInProjectUnit : 0) * tourModel.getFareAfterSpecificKm();

		distanceFareBeforeSpecificKm = roundUpDecimalValueWithDownMode(distanceFareBeforeSpecificKm, 2);

		distanceFareAfterSpecificKm = roundUpDecimalValueWithDownMode(distanceFareAfterSpecificKm, 2);

		distanceFare = distanceFareBeforeSpecificKm + distanceFareAfterSpecificKm;

		baseFare = initialFare;

		timeFare = timeInMin * tourModel.getPerMinuteFare();

		timeFare = roundUpDecimalValueWithDownMode(timeFare, 2);

		arrivedWaitingTimeFare = arrivedWaitingTime * tourModel.getPerMinuteFare();

		arrivedWaitingTimeFare = roundUpDecimalValueWithDownMode(arrivedWaitingTimeFare, 2);

		total = baseFare + timeFare + arrivedWaitingTimeFare + distanceFare + tourModel.getMarkupFare();

		if (tourModel.isSurgePriceApplied()) {

			totalWithSurge = total * tourModel.getSurgePrice();

			totalWithSurge = roundUpDecimalValueWithDownMode(totalWithSurge, 2);

			surgeFare = totalWithSurge - total;

			if (surgeFare <= 0) {

				surgeFare = 0;
			}

			total = totalWithSurge;

		}

		promoDiscountAmount = promocodeAmount(tourModel, total);

		promoDiscountAmount = roundUpDecimalValueWithDownMode(promoDiscountAmount, 2);

		if (promoDiscountAmount >= total) {

			promoDiscountAmount = total;

		}

		charges = total - promoDiscountAmount;

		charges = charges + tollAmount;

		charges = roundUpDecimalValueWithDownMode(charges, 2);

		finalAmountForTaxCalculation = charges;

		if (taxModelList != null) {

			for (TaxModel taxModel : taxModelList) {

				double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

				taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2);

				totalTaxAmount = totalTaxAmount + taxAmount;
			}
		}

		totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2);

		charges = charges + totalTaxAmount;

		charges = roundUpDecimalValueWithDownMode(charges, 2);

		if (availabelCredits < 0) {

			// IF AVAIALABLE CREDITS IS NEAGATIVE THEN MAKE USED CREDITS NEAGTIVE TO GET
			// ADDED IN FARE
			usedCredits = availabelCredits;
			availabelCredits = 0;

		} else if (charges >= availabelCredits) {

			// IF AVAIALABLE CREDITS IS LESS THAN CHARGES THEN ALL AVAILABLE CREDITS WILL BE
			// USED
			usedCredits = availabelCredits;
			availabelCredits = 0;

		} else if (charges < availabelCredits) {

			// IF AVAIALABLE CREDITS IS GREATER THAN CHARGES PAY ALL CHARGES FROM AVAIALBE
			// CREDIT
			// AND MINUS THIS USED CREDITS FROM AVAIALBLE CREDITS
			usedCredits = charges;
			availabelCredits = availabelCredits - usedCredits;
		}

		finalAmountCollected = charges - usedCredits;

		finalAmountCollected = roundUpDecimalValueWithUpMode(finalAmountCollected, 0);

		total = total - tourModel.getMarkupFare();

		driverAmount = (total) * (tourModel.getPercentage() / 100);

		double totalForDemandSupplier = total;
		double driverAmountForDemandSupplier = driverAmount;

		driverAmount = driverAmount + tollAmount;

		driverAmount = roundUpDecimalValueWithDownMode(driverAmount, 2);

		total = total + tourModel.getMarkupFare();

		if (isDriverReferralApplied) {

			referrerDriverAmount = (total) * (referrerDriverPercentage / 100);
			referrerDriverAmount = roundUpDecimalValueWithDownMode(referrerDriverAmount, 2);
		}

		outputMap.put("initialFare", initialFare);
		outputMap.put("bookingFees", bookingFees);
		outputMap.put("baseFare", baseFare);
		outputMap.put("distanceFare", distanceFare);
		outputMap.put("timeFare", timeFare);
		outputMap.put("total", total);
		outputMap.put("charges", charges);
		outputMap.put("finalAmountCollected", finalAmountCollected);
		outputMap.put("promoDiscountAmount", promoDiscountAmount);
		outputMap.put("availabelCredits", availabelCredits);
		outputMap.put("usedCredits", usedCredits);
		outputMap.put("driverAmount", driverAmount);
		outputMap.put("minimumFare", minimumFare);
		outputMap.put("tollAmount", tollAmount);
		outputMap.put("totalWithSurge", totalWithSurge);
		outputMap.put("surgeFare", surgeFare);
		outputMap.put("arrivedWaitingTimeFare", arrivedWaitingTimeFare);
		outputMap.put("finalAmountForTaxCalculation", finalAmountForTaxCalculation);
		outputMap.put("totalTaxAmount", totalTaxAmount);
		outputMap.put("referrerDriverAmount", referrerDriverAmount);
		outputMap.put("distBeforeSpecificKmInMeters", distBeforeSpecificKmInMeters);
		outputMap.put("distAfterSpecificKmInMeters", distAfterSpecificKmInMeters);
		outputMap.put("distanceFareBeforeSpecificKm", distanceFareBeforeSpecificKm);
		outputMap.put("distanceFareAfterSpecificKm", distanceFareAfterSpecificKm);
		outputMap.put("totalForDemandSupplier", totalForDemandSupplier);
		outputMap.put("driverAmountForDemandSupplier", driverAmountForDemandSupplier);

		return outputMap;
	}

	public static Map<String, Object> calculateFareForRentalBooking(InvoiceModel inputInvoiceModel, TourModel tourModel, double arrivedWaitingTime, double userAvailabelCredits, double tollAmount, List<TaxModel> taxModelList, boolean isDriverReferralApplied,
				double referrerDriverPercentage) {

		Map<String, Object> outputMap = new HashMap<String, Object>();

		double baseFare = 0.0;
		double initialFare = tourModel.getInitialFare();
		double bookingFees = tourModel.getBookingFees();
		double timeFare = 0.0;
		double distanceFare = 0.0;
		double usedCredits = 0.0;
		double total = 0.0;
		double charges = 0.0;
		double finalAmountCollected = 0.0;
		double driverAmount = 0.0;
		double availabelCredits = 0.0;
		double timeInMin = 0.0;
		double distanceInProjectUnit = 0.0;
		double promoDiscountAmount = 0.0;
		double minimumFare = tourModel.getMinimumFare();
		double totalWithSurge = 0.0;
		double surgeFare = 0.0;
		double finalAmountForTaxCalculation = 0.0;
		double totalTaxAmount = 0.0;
		double arrivedWaitingTimeFare = 0.0;

		double referrerDriverAmount = 0.0;

		double distBeforeSpecificKmInMeters = 0.0;
		double distAfterSpecificKmInMeters = 0.0;
		double distanceFareBeforeSpecificKm = 0.0;
		double distanceFareAfterSpecificKm = 0.0;

		baseFare = initialFare;
		timeInMin = millisToMin(inputInvoiceModel.getDuration());

		double rentalPackageTimeInMin = tourModel.getRentalPackageTime() * 60;

		if (timeInMin > rentalPackageTimeInMin) {

			timeFare = (timeInMin - rentalPackageTimeInMin) * tourModel.getPerMinuteFare();

			timeFare = roundUpDecimalValueWithDownMode(timeFare, 2);
		}

		double totalDistanceInMeter = inputInvoiceModel.getDistance() - tourModel.getFreeDistance();

		if (totalDistanceInMeter < 0) {

			totalDistanceInMeter = 0;
		}

		if (totalDistanceInMeter > 0) {

			distanceInProjectUnit = getDistanceInProjectUnitFromMeters(totalDistanceInMeter);

			distanceFare = (distanceInProjectUnit > 0 ? distanceInProjectUnit : 0) * tourModel.getPerKmFare();

			distanceFare = roundUpDecimalValueWithDownMode(distanceFare, 2);
		}

		total = baseFare + timeFare + distanceFare + tourModel.getMarkupFare();

		promoDiscountAmount = promocodeAmount(tourModel, total);

		promoDiscountAmount = roundUpDecimalValueWithDownMode(promoDiscountAmount, 2);

		if (promoDiscountAmount >= total) {

			promoDiscountAmount = total;

		}

		charges = total - promoDiscountAmount;

		charges = charges + tollAmount;

		charges = roundUpDecimalValueWithDownMode(charges, 2);

		finalAmountForTaxCalculation = charges;

		if (taxModelList != null) {

			for (TaxModel taxModel : taxModelList) {

				double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

				taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2);

				totalTaxAmount = totalTaxAmount + taxAmount;
			}
		}

		totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2);

		charges = charges + totalTaxAmount;

		charges = roundUpDecimalValueWithDownMode(charges, 2);

		if (availabelCredits < 0) {

			// IF AVAIALABLE CREDITS IS NEAGATIVE THEN MAKE USED CREDITS NEAGTIVE TO GET
			// ADDED IN FARE
			usedCredits = availabelCredits;
			availabelCredits = 0;

		} else if (charges >= availabelCredits) {

			// IF AVAIALABLE CREDITS IS LESS THAN CHARGES THEN ALL AVAILABLE CREDITS WILL BE
			// USED
			usedCredits = availabelCredits;
			availabelCredits = 0;

		} else if (charges < availabelCredits) {

			// IF AVAIALABLE CREDITS IS GREATER THAN CHARGES PAY ALL CHARGES FROM AVAIALBE
			// CREDIT
			// AND MINUS THIS USED CREDITS FROM AVAIALBLE CREDITS
			usedCredits = charges;
			availabelCredits = availabelCredits - usedCredits;
		}

		finalAmountCollected = charges - usedCredits;

		finalAmountCollected = roundUpDecimalValueWithUpMode(finalAmountCollected, 0);

		total = total - tourModel.getMarkupFare();

		driverAmount = (total) * (tourModel.getPercentage() / 100);

		double totalForDemandSupplier = total;
		double driverAmountForDemandSupplier = driverAmount;

		driverAmount = driverAmount + tollAmount;

		driverAmount = roundUpDecimalValueWithDownMode(driverAmount, 2);

		total = total + tourModel.getMarkupFare();

		if (isDriverReferralApplied) {

			referrerDriverAmount = (total) * (referrerDriverPercentage / 100);

			referrerDriverAmount = roundUpDecimalValueWithDownMode(referrerDriverAmount, 2);
		}

		outputMap.put("initialFare", initialFare);
		outputMap.put("bookingFees", bookingFees);
		outputMap.put("baseFare", baseFare);
		outputMap.put("distanceFare", distanceFare);
		outputMap.put("timeFare", timeFare);
		outputMap.put("total", total);
		outputMap.put("charges", charges);
		outputMap.put("finalAmountCollected", finalAmountCollected);
		outputMap.put("promoDiscountAmount", promoDiscountAmount);
		outputMap.put("availabelCredits", availabelCredits);
		outputMap.put("usedCredits", usedCredits);
		outputMap.put("driverAmount", driverAmount);
		outputMap.put("minimumFare", minimumFare);
		outputMap.put("tollAmount", tollAmount);
		outputMap.put("totalWithSurge", totalWithSurge);
		outputMap.put("surgeFare", surgeFare);
		outputMap.put("arrivedWaitingTimeFare", arrivedWaitingTimeFare);
		outputMap.put("finalAmountForTaxCalculation", finalAmountForTaxCalculation);
		outputMap.put("totalTaxAmount", totalTaxAmount);
		outputMap.put("referrerDriverAmount", referrerDriverAmount);
		outputMap.put("distBeforeSpecificKmInMeters", distBeforeSpecificKmInMeters);
		outputMap.put("distAfterSpecificKmInMeters", distAfterSpecificKmInMeters);
		outputMap.put("distanceFareBeforeSpecificKm", distanceFareBeforeSpecificKm);
		outputMap.put("distanceFareAfterSpecificKm", distanceFareAfterSpecificKm);
		outputMap.put("totalForDemandSupplier", totalForDemandSupplier);
		outputMap.put("driverAmountForDemandSupplier", driverAmountForDemandSupplier);

		return outputMap;
	}

	public static double promocodeAmount(TourModel tourModel, double total) {

		double promoCodeDiscountAmount = 0;

		if (tourModel.isPromoCodeApplied()) {

			PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

			if (promoCodeModel.getMode().equals(ProjectConstants.PERCENTAGE_ID)) {
				promoCodeDiscountAmount = (total / 100) * promoCodeModel.getDiscount();
			} else {
				promoCodeDiscountAmount = promoCodeModel.getDiscount();
			}
		}

		return promoCodeDiscountAmount;
	}

	public static void updateTourEndTime(InvoiceModel invoice) {

		TourTimeModel tourTimeModel = new TourTimeModel();
		tourTimeModel.setTourId(invoice.getTourId());
		tourTimeModel.setEndTime(DateUtils.nowAsGmtMillisec());
		tourTimeModel.updateTourEndTime();
	}

	public static void generateInvoiceNew(InvoiceModel invoice, TourModel tourModel, String loggedInuserId, String paymentMode, double charges, double total) {

		invoice.setCharges(charges);
		invoice.setTotal(total);
		invoice.setSubTotal(total);
		invoice.setPaymentMode(paymentMode);

		if (((ProjectConstants.CARD).equalsIgnoreCase(paymentMode)) && (invoice.getFinalAmountCollected() > 0)) {
			invoice.setPaymentPaid(false);
		} else {
			invoice.setPaymentPaid(true);
		}

		invoice.generateInvoiceNew(loggedInuserId);

		updateUserAccountAndLogAtEndTrip(invoice, tourModel, loggedInuserId, loggedInuserId);
	}

	private void sendEmail(TourModel tourDetils) {

		new ProcessInvoiceEmailThread(tourDetils.getTourId());
	}

	private void sendSMS(TourModel tourDetils) {

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		String message = sendNotification(tourDetils);

		UserModel userB = UserModel.getUserAccountDetailsById(tourDetils.getCreatedBy());

		//@formatter:off
		if ((tourDetils.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
				&& tourDetils.getCardOwner() != null && tourDetils.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
					|| (tourDetils.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
					&& tourDetils.getCardOwner() != null && tourDetils.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
		//@formatter:on

			String bo_message = BusinessAction.messageForKeyAdmin("successTripCompleted", tourDetils.getLanguage());

			if (adminSmsSendingModel.isBoInvoice()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(bo_message, tourDetils.getpPhoneCode() + tourDetils.getpPhone(), ProjectConstants.SMSConstants.SMS_EINVOICE_GENERATED_TEMPLATE_ID);
			}

			String messageToAdmin = BusinessAction.messageForKeyAdmin("successTripCompletedMsgForAdmin", tourDetils.getLanguage());

			if (userB != null) {

				if (adminSmsSendingModel.isBoInvoice()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(messageToAdmin, userB.getPhoneNoCode() + userB.getPhoneNo(), ProjectConstants.SMSConstants.SMS_INVOICE_TEMPLATE_ID);
				}
			}

		} else {

			if (adminSmsSendingModel.ispInvoice()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetils.getpPhoneCode() + tourDetils.getpPhone(), ProjectConstants.SMSConstants.SMS_EINVOICE_GENERATED_TEMPLATE_ID);
			}
		}
	}

	public static void processCsvThread(InvoiceModel invoice) {

		String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

		String tempPath = WebappPropertyUtils.getWebAppProperty("tempDir");

		new ProcessCsvThread(invoice.getTourId(), bucket, tempPath);
	}

	private String sendNotification(TourModel tourDetils) {

		String message = String.format(BusinessAction.messageForKeyAdmin("successTripCompleted", tourDetils.getLanguage()));

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetils.getPassengerId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(tourDetils.getPassengerId());
		apnsMessage.insertPushMessage();

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.ENDED_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}

		return message;
	}

	public static void updateDriverTourStatus(String loggedInuserId) {
		DriverTourStatusUtils.updateDriverTourStatus(loggedInuserId, ProjectConstants.DRIVER_FREE_STATUS);
	}

	private List<String> invoiceModelValidation(InvoiceModel invoiceModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(TOUR_ID, TOUR_ID_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DISTANCE, DISTANCE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(WAITING_TIME, WAITING_TIME_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(invoiceModel);

		return errorMessages;
	}

	private void updateTaxDetails(List<TaxModel> taxModelList, double finalAmountForTaxCalculation, String tourId, String userId) {

		List<TourTaxModel> tourTaxModelList = new ArrayList<TourTaxModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		for (TaxModel taxModel : taxModelList) {

			double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

			taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2); // roundOff(taxAmount, true, true, RoundingMode.DOWN, 2);

			TourTaxModel tourTaxModel = new TourTaxModel();

			tourTaxModel.setTourTaxId(UUIDGenerator.generateUUID());
			tourTaxModel.setTourId(tourId);
			tourTaxModel.setTaxId(taxModel.getTaxId());
			tourTaxModel.setTaxPercentage(taxModel.getTaxPercentage());
			tourTaxModel.setTaxAmount(taxAmount > 0 ? taxAmount : 0);

			tourTaxModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			tourTaxModel.setCreatedAt(currentTime);
			tourTaxModel.setCreatedBy(userId);
			tourTaxModel.setUpdatedAt(currentTime);
			tourTaxModel.setUpdatedBy(userId);

			tourTaxModelList.add(tourTaxModel);
		}

		if (tourTaxModelList.size() > 0) {

			TourTaxModel.insertTourTaxBatch(tourTaxModelList);
		}
	}

	private void sendReferralBenefitPushToReferredDriver(TourModel tour, String referredDriverId, double referrerDriverAmount) {

		//@formatter:off
		String message = BusinessAction.messageForKeyAdmin("msgReferralBenefitPushToDriver1", tour.getLanguage())
				+" " + tour.getpFirstName() + " "
				+BusinessAction.messageForKeyAdmin("msgReferralBenefitPushToDriver2", tour.getLanguage())
				+" "+ BusinessAction.messageForKeyAdmin("labelCurrency", tour.getLanguage()) + referrerDriverAmount +" "
				+BusinessAction.messageForKeyAdmin("msgReferralBenefitPushToDriver3", tour.getLanguage());
		//@formatter:on

		List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();
		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
		apnsMessage.setCreatedAt(DateUtils.nowAsGmtMillisec());
		apnsMessage.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		apnsMessage.setToUserId(referredDriverId);
		apnsMessage.setMessageType("push");
		apnsMessage.setMessage(message);

		apnsMessageList.add(apnsMessage);

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(referredDriverId);

		ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", message, ProjectConstants.WALLET_DEPOSIT, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	private void addReferralLogs(TourModel tourDetails, boolean isDriverReferralApplied, double referrerDriverPercentage, double referrerDriverAmount, String loggedInUserId) {

		UserModel passengerModel = UserModel.getUserAccountDetailsById(tourDetails.getPassengerId());

		if (passengerModel.getRoleId().equals(UserRoles.PASSENGER_ROLE_ID)) {

			DriverReferralCodeLogModel referralModel = DriverReferralCodeLogModel.getDriverReferralCodeLogByPassengerId(tourDetails.getPassengerId());

			if (referralModel == null) {

				boolean isFirstTour = TourModel.checkPassengerCurrentTourAsFirstTour(tourDetails.getTourId(), tourDetails.getPassengerId(), tourDetails.getCreatedAt());

				if (isFirstTour) {

					DriverReferralCodeLogModel driverReferralModel = new DriverReferralCodeLogModel();
					driverReferralModel.setDriverId(tourDetails.getDriverId());
					driverReferralModel.setPassengerId(tourDetails.getPassengerId());

					driverReferralModel.addDriverReferralCodeLog(tourDetails.getDriverId());

					// Its for passenger flow
					// ReferralCodeLogsModel referralCodeLogsModel = new ReferralCodeLogsModel();
					// referralCodeLogsModel.setSenderId(tourDetils.getDriverId());
					// referralCodeLogsModel.setReceiverId(tourDetils.getPassengerId());
					//
					// referralCodeLogsModel.addReferralCodeLogs(tourDetils.getDriverId(),
					// ProjectConstants.DRIVER_ROLE_ID);
				}
			}

			DriverReferralCodeLogModel driverReferralCodeLogModel = DriverReferralCodeLogModel.getDriverReferralCodeLogByPassengerId(tourDetails.getPassengerId());

			if (driverReferralCodeLogModel != null) {

				if (isDriverReferralApplied && referrerDriverAmount > 0) {

					TourReferrerBenefitModel tourReferrerBenefitModel = new TourReferrerBenefitModel();

					tourReferrerBenefitModel.setDriverReferralCodeLogId(driverReferralCodeLogModel.getDriverReferralCodeLogId());
					tourReferrerBenefitModel.setTourId(tourDetails.getTourId());
					tourReferrerBenefitModel.setReferrerDriverPercentage(referrerDriverPercentage);
					tourReferrerBenefitModel.setReferrerDriverBenefit(referrerDriverAmount);

					if (driverReferralCodeLogModel.getDriverId().equals(tourDetails.getDriverId())) {

						tourReferrerBenefitModel.setTourReferrerType(ProjectConstants.TOUR_REFERRER_TYPE_OWN);

					} else {

						tourReferrerBenefitModel.setTourReferrerType(ProjectConstants.TOUR_REFERRER_TYPE_OTHER);
					}

					tourReferrerBenefitModel.addTourReferrerBenefit(tourDetails.getDriverId());

					sendReferralBenefitPushToReferredDriver(tourDetails, driverReferralCodeLogModel.getDriverId(), referrerDriverAmount);

					//@formatter:off
					String remarkTripReferralBonus = BusinessAction.messageForKeyAdmin("remarkTripReferralBonus1", tourDetails.getLanguage()) + " " 
							+ tourDetails.getUserTourId() 
							+ BusinessAction.messageForKeyAdmin("remarkTripReferralBonus2", tourDetails.getLanguage());
					//@formatter:on

					String userId = driverReferralCodeLogModel.getDriverId();
					double amount = referrerDriverAmount;
					String transactionType = ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT;
					String remark = remarkTripReferralBonus;
					String description = remarkTripReferralBonus;
					String transactionBy = loggedInUserId;
					boolean isAccountRecharge = false;

					UserAccountModel.updateUserAccountAndCreateLog(userId, amount, transactionType, description, remark, transactionBy, isAccountRecharge);

				}
			}
		}
	}

	public static void updateUserAccountAndLogAtEndTrip(InvoiceModel invoiceModel, TourModel tourModel, String driverId, String loggedInUserId) {

		String transactionType = "";
		double logAmount = 0.0;
		String remark = "";
		String transactionBy = loggedInUserId;
		boolean isAccountRecharge = false;
		boolean isNeedToUpdateOrAddAC = false;
		double driverAmount = invoiceModel.getDriverAmount();

		if (ProjectConstants.VENDOR_BOOKING.equals(tourModel.getBookingType())) {

			DriverVendorsModel driverVendorsModel = DriverVendorsModel.getDriverVendorDetailsByDriverId(driverId);

			if (driverVendorsModel != null && tourModel.getMarkupFare() > 0) {

				//@formatter:off
				String remarkVendorMarkup = BusinessAction.messageForKeyAdmin("remarkVendorMarkup1", tourModel.getLanguage()) + " " 
						+ tourModel.getUserTourId() 
						+ BusinessAction.messageForKeyAdmin("remarkVendorMarkup2", tourModel.getLanguage());
				//@formatter:on

				String vendorId = driverVendorsModel.getVendorId();
				double vendorAmount = tourModel.getMarkupFare();
				String vendorTransactionType = ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT;
				String vendorDescription = remarkVendorMarkup;
				String vendorRemark = remarkVendorMarkup;

				UserAccountModel.updateUserAccountAndCreateLog(vendorId, vendorAmount, vendorTransactionType, vendorDescription, vendorRemark, transactionBy, isAccountRecharge);
			}

		}

		if (invoiceModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {

			if (invoiceModel.getFinalAmountCollected() > driverAmount) {

				isNeedToUpdateOrAddAC = true;

				transactionType = ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT;
				logAmount = Double.parseDouble(df.format(invoiceModel.getFinalAmountCollected() - driverAmount));

				//@formatter:off
				remark = BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit1", tourModel.getLanguage()) + " " 
						+ tourModel.getUserTourId() + ", " + logAmount + " "
						+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit3", tourModel.getLanguage()) + " "
						+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit4", tourModel.getLanguage()) + " "
						+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit5", tourModel.getLanguage());
				//@formatter:on

			} else if (invoiceModel.getFinalAmountCollected() < driverAmount) {

				isNeedToUpdateOrAddAC = true;

				transactionType = ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT;
				logAmount = Double.parseDouble(df.format(driverAmount - invoiceModel.getFinalAmountCollected()));

				//@formatter:off
				remark = BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit1", tourModel.getLanguage()) + " " 
						+ tourModel.getUserTourId() + ", " + logAmount + " "
						+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit2", tourModel.getLanguage()) + " "
						+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit4", tourModel.getLanguage()) + " "
						+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit5", tourModel.getLanguage());
				//@formatter:on
			}

		} else {

			isNeedToUpdateOrAddAC = true;

			transactionType = ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT;
			logAmount = Double.parseDouble(df.format(driverAmount));

			//@formatter:off
			remark = BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit1", tourModel.getLanguage()) + " " 
					+ tourModel.getUserTourId() + ", " + logAmount + " "
					+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit2", tourModel.getLanguage()) + " "
					+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit4", tourModel.getLanguage()) + " "
					+ BusinessAction.messageForKeyAdmin("remarkTripEndDebitCredit6", tourModel.getLanguage());
			//@formatter:on
		}

		String description = remark;

		if (isNeedToUpdateOrAddAC) {

			UserAccountModel.updateUserAccountAndCreateLog(driverId, logAmount, transactionType, description, remark, transactionBy, isAccountRecharge);

			if (ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(transactionType)) {

				UserAccountModel userAccountDetails = UserAccountModel.getAccountBalanceDetailsByUserId(driverId.trim());

				DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

				if ((userAccountDetails.getCurrentBalance() - logAmount) <= driverWalletSettingsModel.getNotifyAmount()) {

					// Send push notification to driver

					//@formatter:off
					String notifyAlert1 = BusinessAction.messageForKeyAdmin("driverWalletNotifyAlert11", tourModel.getLanguage()) + " " 
							+ df.format(userAccountDetails.getCurrentBalance()) 
							+ BusinessAction.messageForKeyAdmin("driverWalletNotifyAlert12", tourModel.getLanguage()) + " " 
							+ df.format(driverWalletSettingsModel.getMinimumAmount()) + ".";
					
					String notifyAlert2 = BusinessAction.messageForKeyAdmin("driverWalletNotifyAlert21", tourModel.getLanguage()) + " " 
							+ df.format(userAccountDetails.getCurrentBalance()) 
							+ BusinessAction.messageForKeyAdmin("driverWalletNotifyAlert22", tourModel.getLanguage()) + " " 
							+ df.format(driverWalletSettingsModel.getMinimumAmount()) 
							+ BusinessAction.messageForKeyAdmin("driverWalletNotifyAlert23", tourModel.getLanguage());
					//@formatter:on

					String pushMessage = "";

					if ((userAccountDetails.getCurrentBalance() - logAmount) <= driverWalletSettingsModel.getMinimumAmount()) {
						pushMessage = notifyAlert2;
					} else if ((userAccountDetails.getCurrentBalance() - logAmount) <= driverWalletSettingsModel.getNotifyAmount()) {
						pushMessage = notifyAlert1;
					}

					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);

					ApnsMessageModel apnsMessage = new ApnsMessageModel();
					apnsMessage.setMessage(pushMessage);
					apnsMessage.setMessageType("push");
					apnsMessage.setToUserId(driverId);
					apnsMessage.insertPushMessage();

					if (apnsDevice != null) {
						apnsDevice.sendNotification("1", "Push", pushMessage, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					}
				}
			}

		}
	}

}