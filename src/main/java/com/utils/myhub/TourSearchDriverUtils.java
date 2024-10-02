package com.utils.myhub;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.FrameworkConstants;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.secure.manualbookings.ManualBookingsAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.OrderModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserModel;
import com.webapp.models.WebSocketClient;

public class TourSearchDriverUtils {

	private static Logger logger = Logger.getLogger(TourSearchDriverUtils.class);

	private static String getNearestDriver(TourModel tourModel, String vendorType) {

		String tourId = tourModel.getTourId();
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String maxRadius = adminSettingsModel.getRadiusString();

		if (tourModel.isSurgePriceApplied() && (!tourModel.isRideLater()) && tourModel.getSurgeRadius() != 0) {
			maxRadius = String.valueOf(tourModel.getSurgeRadius() * adminSettingsModel.getDistanceUnits());
		}

		String distance = GeoLocationUtil.getDistanceQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION);
		String latAndLong = GeoLocationUtil.getLatLngQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION, maxRadius);

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("tourId", tourId);
		inputMap.put("carTypeId", tourModel.getCarTypeId());
		inputMap.put("searchKey", null);
		inputMap.put("startOffSet", 0);
		inputMap.put("recordOffset", 1);
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("multicityCityRegionId", tourModel.getMulticityCityRegionId());
		inputMap.put("multicityCountryId", tourModel.getMulticityCountryId());
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("transmissionTypeIdList", getDriveTransmissionListFromType(tourModel.getTransmissionTypeId(), tourModel.getCarTypeId()));
		inputMap.put("passengerId", tourModel.getPassengerId());

		String vendorId = null;
		if (vendorType.equalsIgnoreCase("1")) {
			// search specific vendor
			// inputMap.put("vendorId",
			// MultiTenantUtils.getVendorIdByUserId(tourModel.getPassengerId()));
			vendorId = tourModel.getPassengerVendorId() != null ? tourModel.getPassengerVendorId() : MultiTenantUtils.getVendorIdByUserId(tourModel.getPassengerId());
			inputMap.put("vendorId", vendorId);
		} else if (vendorType.equalsIgnoreCase("2")) {
			// search default vendor
			vendorId = WebappPropertyUtils.DEFAULT_VENDOR_ID;
			inputMap.put("vendorId", vendorId);
		} else {
			// search all the vendors
			inputMap.put("vendorId", null);
		}

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, vendorId, DateUtils.nowAsGmtMillisec());

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		inputMap.put("minimumWalletAmount", minimumWalletAmount);
		inputMap.put("searchSubscribedDriver", false);

		DriverGeoLocationModel driver = DriverGeoLocationModel.getNearestSingledriver(inputMap);

		String driverId = null;

		if (driver != null) {
			driverId = driver.getDriverId();
		}

		return driverId;
	}

	private static String getFavouriteNearestDriver(TourModel tourModel, String vendorType) {

		String tourId = tourModel.getTourId();
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String maxRadius = adminSettingsModel.getRadiusString();

		if (tourModel.isSurgePriceApplied() && (!tourModel.isRideLater()) && tourModel.getSurgeRadius() != 0) {
			maxRadius = String.valueOf(tourModel.getSurgeRadius() * adminSettingsModel.getDistanceUnits());
		}

		String distance = GeoLocationUtil.getDistanceQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION);
		String latAndLong = GeoLocationUtil.getLatLngQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION, maxRadius);

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("tourId", tourId);
		inputMap.put("carTypeId", tourModel.getCarTypeId());
		inputMap.put("searchKey", null);
		inputMap.put("startOffSet", 0);
		inputMap.put("recordOffset", 1);
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("passengerId", tourModel.getPassengerId());
		inputMap.put("multicityCityRegionId", tourModel.getMulticityCityRegionId());
		inputMap.put("multicityCountryId", tourModel.getMulticityCountryId());
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("transmissionTypeIdList", getDriveTransmissionListFromType(tourModel.getTransmissionTypeId(), tourModel.getCarTypeId()));

		String vendorId = null;
		if (vendorType.equalsIgnoreCase("1")) {
			// search specific vendor
			// inputMap.put("vendorId",
			// MultiTenantUtils.getVendorIdByUserId(tourModel.getPassengerId()));
			vendorId = tourModel.getPassengerVendorId() != null ? tourModel.getPassengerVendorId() : MultiTenantUtils.getVendorIdByUserId(tourModel.getPassengerId());
			inputMap.put("vendorId", vendorId);
		} else if (vendorType.equalsIgnoreCase("2")) {
			// search default vendor
			vendorId = WebappPropertyUtils.DEFAULT_VENDOR_ID;
			inputMap.put("vendorId", vendorId);
		} else {
			// search all the vendors
			inputMap.put("vendorId", null);
		}

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, vendorId, DateUtils.nowAsGmtMillisec());

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		inputMap.put("minimumWalletAmount", minimumWalletAmount);

		DriverGeoLocationModel driver = DriverGeoLocationModel.getFavouriteNearestSingledriver(inputMap);

		String driverId = null;

		if (driver != null) {

			driverId = driver.getDriverId();
		}

		return driverId;
	}

	public static String assignDriver(TourModel tourModel, AdminSettingsModel adminSettingsModel) {

		String driverId = null;

		/*
		 * While searching driver, 1st search the driver attached to the passenger
		 * vendor. If not found then search driver of the default vendor. If not found
		 * then trip can be assigned to any driver from any vendor
		 */

		/*
		 * Search for drivers with associated vendor 1st
		 */

		driverId = getFavouriteNearestDriver(tourModel, "1");

		if (driverId == null) {
			driverId = getNearestDriver(tourModel, "1");
		}

		/*
		 * Search for drivers with associated default vendor
		 */

		if (driverId == null) {
			driverId = getFavouriteNearestDriver(tourModel, "2");
		}

		if (driverId == null) {
			driverId = getNearestDriver(tourModel, "2");
		}

		/*
		 * Search for drivers with all of the rest vendors
		 */

		if (driverId == null) {
			driverId = getFavouriteNearestDriver(tourModel, "0");
		}

		if (driverId == null) {
			driverId = getNearestDriver(tourModel, "0");
		}

		if (driverId != null && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)
					&& !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

			String freeDriverId = null;

			if (!tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {
				freeDriverId = tourModel.getDriverId();
			}

			DriverTourStatusUtils.updateDriverTourStatus(driverId, ProjectConstants.DRIVER_HIRED_STATUS);

			if ((ProjectConstants.Fifth_Vehicle_ID.equals(tourModel.getCarTypeId())) && (adminSettingsModel.isAutoAssign())) {

				tourAutoAssignToDriver(tourModel.getTourId(), driverId);

			} else if ((!ProjectConstants.Fifth_Vehicle_ID.equals(tourModel.getCarTypeId())) && (adminSettingsModel.isCarServiceAutoAssign())) {

				tourAutoAssignToDriver(tourModel.getTourId(), driverId);

			} else {

				TourUtils.assignTourDriver(tourModel.getTourId(), driverId, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR, driverId);

				DriverTourRequestUtils.createDriverTourRequest(tourModel.getTourId(), driverId, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);
			}

			if (freeDriverId != null) {
				freeDriver(freeDriverId);
			}

			return driverId;

		} else {
			return null;
		}
	}

	public static void freeDriver(String driverId) {
		DriverTourStatusUtils.updateDriverTourStatus(driverId, ProjectConstants.DRIVER_FREE_STATUS);
	}

	public static void sendDriverNotification(String driverId, int tripType, OrderModel orderModel) {

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);

		TourModel tourModel = null;
		String carTypeId = null;

		if (tripType == ProjectConstants.TRIP_TYPE_TOUR_ID) {
			tourModel = TourModel.getCurrentTourByDriverId(driverId);
			carTypeId = tourModel.getCarTypeId();
		} else if (tripType == ProjectConstants.TRIP_TYPE_ORDER_ID) {
			carTypeId = orderModel.getCarTypeId();
		}

		CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(carTypeId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> outPutMap = formHashMap(tripType, tourModel, carTypeModel, adminSettingsModel, orderModel);

		JSONObject jsonMessage = new JSONObject(outPutMap);

		logger.info("\n\n\n\n\n\tRequest message to driver\t" + jsonMessage);

		String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.NGB_SOCKET + jsonMessage.toString();

		WebSocketClient.sendDriverNotification(messge, driverId, apnsDevice.getApiSessionKey());

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();
		String message = BusinessAction.messageForKeyAdmin("successNewJobMessage", adminSmsSendingModel.getLanguage());

		UserModel userModel = UserModel.getUserAccountDetailsById(driverId);

		if (tripType == ProjectConstants.TRIP_TYPE_TOUR_ID) {

			// Send push to driver only when auto assign on ------------------------------
			if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) && (!tourModel.isAcknowledged())) {
				ApnsDeviceModel driverAapnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);

				String driverPushMsg = BusinessAction.messageForKeyAdmin("driverPushMsgForManualBookingAssign", FrameworkConstants.LANGUAGE_ENGLISH);

				ApnsMessageModel driverApnsMessage = new ApnsMessageModel();
				driverApnsMessage.setMessage(driverPushMsg);
				driverApnsMessage.setMessageType("push");
				driverApnsMessage.setToUserId(driverId);
				driverApnsMessage.insertPushMessage();

				if (driverAapnsDevice != null) {
					driverAapnsDevice.sendNotification("1", "Push", driverPushMsg, ProjectConstants.NOTIFICATION_TYPE_MB_NEW_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
				}
			}
			// ----------------------------------------------------

			//@formatter:off
			if ((tourModel.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
					&& tourModel.getCardOwner() != null && tourModel.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
						|| (tourModel.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
						&& tourModel.getCardOwner() != null && tourModel.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
			//@formatter:on

				UserModel boModel = UserModel.getUserAccountDetailsById(tourModel.getCreatedBy());

				if (boModel != null) {

					String p_message = String.format(BusinessAction.messageForKeyAdmin("successPassengerOwnerBookCarMessage_1", tourModel.getLanguage()), tourModel.getpFirstName() + " " + tourModel.getpLastName(),
								boModel.getFirstName() + " " + boModel.getLastName());

					if (adminSmsSendingModel.ispBookByOwner()) {

						MetamorphSystemsSmsUtils.sendSmsToSingleUser(p_message, tourModel.getpPhoneCode() + tourModel.getpPhone(), ProjectConstants.SMSConstants.SMS_BOOKING_TEMPLATE_ID);

						// SmsUtils.sendSms(p_message, tourModel.getpPhone(),
						// tourModel.getpPhoneCode());
					}
				}
			}
		}

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(driverId);
		apnsMessage.insertPushMessage();

		if (adminSmsSendingModel.isdBookingRequest()) {

			MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userModel.getPhoneNoCode() + userModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_NEW_JOB_TEMPLATE_ID);

			// SmsUtils.sendSms(message, userModel.getPhoneNo(),
			// userModel.getPhoneNoCode());
		}
	}

	public static void tourAutoAssignToDriver(String tourId, String driverId) {

		TourModel tour = new TourModel();
		tour.setTourId(tourId);
		tour.setAcknowledged(false);
		tour.updateRideLaterTourAcknowledgeByTourId();

		TourUtils.assignTourDriver(tourId, driverId, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST, driverId);

		MultiTenantUtils.changeTourFareParametersWithVendorFare(tourId, driverId);

		DriverTourRequestUtils.createDriverTourRequest(tourId, driverId, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);

		// send passenger socket
		// This method used only when driver service type and auto assign on
		ManualBookingsAction.sendNotificationToPassenger(tourId);
	}

	public static List<String> getDriveTransmissionListFromType(String transmissionType, String carTypeId) {

		List<String> result = new ArrayList<String>();
		if (!carTypeId.equals("5")) {
			return null;
		}

		if (transmissionType == null) {
			return null;
		}

		if (transmissionType.trim().equals("")) {
			return null;
		}

		result.add(transmissionType);
		result.add(ProjectConstants.TRANSMISSION_TYPE_BOTH_ID);

		return result;
	}

	public static void updateTourStatusAsPending(String tourId, String passangerId) {

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

			TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, ProjectConstants.DEFAULT_DRIVER_ID);

			PromoCodeUtils.promoCodeCancel(tourModel, true);

			String message = BusinessAction.messageForKeyAdmin("errorNoCarAvailable");

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(passangerId);

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(passangerId);
			apnsMessage.insertPushMessage();

			TourModel tour1 = new TourModel();
			tour1.setTourId(tourId);
			tour1.setCharges(0);
			tour1.updateCharges();

			if (apnsDevice != null) {
				apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}
		}
	}

	public static void updateTourStatusExpired(TourModel tourModel) {

		TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, ProjectConstants.DEFAULT_DRIVER_ID);

		PromoCodeUtils.promoCodeCancel(tourModel, true);

		if (!tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {
			DriverTourStatusUtils.updateDriverTourStatus(tourModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);
		}

		String message = BusinessAction.messageForKeyAdmin("errorNoCarAvailable");

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(tourModel.getPassengerId());
		apnsMessage.insertPushMessage();

		TourModel tour1 = new TourModel();
		tour1.setTourId(tourModel.getTourId());
		tour1.setCharges(0);
		tour1.updateCharges();

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	private static Map<String, Object> formHashMap(int tripType, TourModel tourModel, CarTypeModel carTypeModel, AdminSettingsModel adminSettingsModel, OrderModel orderModel) {

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		if (carTypeModel != null) {
			outPutMap.put("carType", carTypeModel.getCarType());
		} else {
			outPutMap.put("carType", "");
		}

		if (tripType == ProjectConstants.TRIP_TYPE_TOUR_ID && tourModel != null) {

			outPutMap.put("tourId", tourModel.getTourId());
			outPutMap.put("userTourId", tourModel.getUserTourId());
			outPutMap.put("passengerId", tourModel.getPassengerId());
			outPutMap.put("driverId", tourModel.getDriverId());
			outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));
			outPutMap.put("firstName", tourModel.getpFirstName());
			outPutMap.put("lastName", tourModel.getpLastName());
			outPutMap.put("email", tourModel.getpEmail());
			outPutMap.put("phone", tourModel.getpPhone());
			outPutMap.put("phoneCode", tourModel.getpPhoneCode());
			outPutMap.put("photoUrl", tourModel.getpPhotoUrl());
			outPutMap.put("isAcknowledged", tourModel.isAcknowledged());
			outPutMap.put("carTypeId", tourModel.getCarTypeId());
			outPutMap.put("paymentType", tourModel.getPaymentType());

			outPutMap.put("sourceLatitude", tourModel.getsLatitude());
			outPutMap.put("sourceLongitude", tourModel.getsLongitude());
			outPutMap.put("destinationLatitude", tourModel.getdLatitude());
			outPutMap.put("destinationLongitude", tourModel.getdLongitude());

			outPutMap.put("sourceAddress", tourModel.getSourceAddress());
			outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
			outPutMap.put("status", tourModel.getStatus());
			outPutMap.put("updatedAt", tourModel.getUpdatedAt());

			outPutMap.put("initialFare", tourModel.getInitialFare());
			outPutMap.put("perKmFare", tourModel.getPerKmFare());
			outPutMap.put("perMinuteFare", tourModel.getPerMinuteFare());
			outPutMap.put("bookingFees", tourModel.getBookingFees());
			outPutMap.put("minimumFare", tourModel.getMinimumFare());
			outPutMap.put("discount", tourModel.getDiscount());
			outPutMap.put("cancellationCharges", CancellationChargeModel.getAdminCancellationCharges().getCharge());
			outPutMap.put("jobExpireTime", ProjectConstants.JOB_EXPIRE_TIME);

			double rating = 0.0;
			List<DriverTripRatingsModel> ratingList = DriverTripRatingsModel.getAllPassangerRatings(tourModel.getPassengerId());

			for (DriverTripRatingsModel paRating : ratingList) {
				rating = rating + paRating.getRate();
			}

			DecimalFormat df = new DecimalFormat("#.#");

			if (rating != 0.0) {
				rating = (double) rating / ratingList.size();
				outPutMap.put("rating", df.format(rating));
			} else {
				outPutMap.put("rating", -1);
			}

			if (tourModel.getCharges() > tourModel.getMinimumFare()) {
				tourModel.setCharges(tourModel.getCharges());
			} else {
				tourModel.setCharges(tourModel.getMinimumFare());
			}

			outPutMap.put("promoCodeApplied", tourModel.isPromoCodeApplied());
			outPutMap.put("total", tourModel.getTotal());
			outPutMap.put("promoDiscount", tourModel.getPromoDiscount());
			outPutMap.put("usedCredits", tourModel.getUsedCredits());
			outPutMap.put("isSurgePriceApplied", tourModel.isSurgePriceApplied());

			if (tourModel.isSurgePriceApplied()) {

				outPutMap.put("surgePriceId", tourModel.getSurgePriceId());
				outPutMap.put("surgePrice", tourModel.getSurgePrice());
				outPutMap.put("totalWithSurge", tourModel.getTotalWithSurge());

			} else {

				outPutMap.put("surgePriceId", "-1");
				outPutMap.put("surgePrice", 1);
				outPutMap.put("totalWithSurge", 0);
			}

			if (tourModel.isPromoCodeApplied()) {

				PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

				if (promoCodeModel != null) {

					outPutMap.put("promoCodeId", promoCodeModel.getPromoCodeId());
					outPutMap.put("promoCode", promoCodeModel.getPromoCode());
					outPutMap.put("usage", promoCodeModel.getUsage());
					outPutMap.put("usageCount", promoCodeModel.getUsageCount());
					outPutMap.put("mode", promoCodeModel.getMode());
					outPutMap.put("promoCodeDiscount", promoCodeModel.getDiscount());
					outPutMap.put("startDate", promoCodeModel.getStartDate());
					outPutMap.put("usedCount", promoCodeModel.getUsedCount());
					outPutMap.put("endDate", promoCodeModel.getEndDate());

				} else {

					outPutMap.put("promoCodeId", "-");
					outPutMap.put("promoCode", "-");
					outPutMap.put("usage", "-");
					outPutMap.put("usageCount", "-");
					outPutMap.put("mode", "-");
					outPutMap.put("promoCodeDiscount", "-");
					outPutMap.put("startDate", "-");
					outPutMap.put("usedCount", "-");
					outPutMap.put("endDate", "-");
				}

			} else {

				outPutMap.put("promoCodeId", "-");
				outPutMap.put("promoCode", "-");
				outPutMap.put("usage", "-");
				outPutMap.put("usageCount", "-");
				outPutMap.put("mode", "-");
				outPutMap.put("promoCodeDiscount", "-");
				outPutMap.put("startDate", "-");
				outPutMap.put("usedCount", "-");
				outPutMap.put("endDate", "-");
			}

			outPutMap.put("distance", tourModel.getDistance());
			outPutMap.put("charges", tourModel.getCharges());

			outPutMap.put("isRentalBooking", tourModel.isRentalBooking());

			Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

			if (tourModel.isRentalBooking()) {

				outPutMap.put("rentalPackageId", tourModel.getRentalPackageId());
				outPutMap.put("rentalPackageTime", tourModel.getRentalPackageTime());

				rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
				rentalPackageDetails.put("baseFare", BusinessApiAction.df_new.format(tourModel.getInitialFare()));
				rentalPackageDetails.put("additionalPerKmFare", BusinessApiAction.df_new.format(tourModel.getPerKmFare()));
				rentalPackageDetails.put("additionalPerMinuteFare", BusinessApiAction.df_new.format(tourModel.getPerMinuteFare()));

				if (tourModel.getRentalPackageTime() == 1) {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

				} else {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
				}

				rentalPackageDetails.put("packageDistance", BusinessApiAction.df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

				outPutMap.put("rentalPackageDetails", rentalPackageDetails);

			} else {

				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", rentalPackageDetails);
			}

			outPutMap.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
			outPutMap.put("airportBookingType", tourModel.getAirportBookingType());

			tourModel.updateCharges();

			CourierUtils.formCourierData(tourModel, outPutMap);
			OrderUtils.formOrdersNGBData(null, outPutMap);

		} else {
			outPutMap.put("tourId", "");
			outPutMap.put("userTourId", "");
			outPutMap.put("passengerId", "");
			outPutMap.put("driverId", "");
			outPutMap.put("dateTime", "");
			outPutMap.put("firstName", "");
			outPutMap.put("lastName", "");
			outPutMap.put("email", "");
			outPutMap.put("phone", "");
			outPutMap.put("phoneCode", "");
			outPutMap.put("photoUrl", "");
			outPutMap.put("isAcknowledged", "");
			outPutMap.put("carTypeId", "");
			outPutMap.put("paymentType", "");

			outPutMap.put("sourceLatitude", 0);
			outPutMap.put("sourceLongitude", 0);
			outPutMap.put("destinationLatitude", 0);
			outPutMap.put("destinationLongitude", 0);

			outPutMap.put("sourceAddress", "");
			outPutMap.put("destinationAddress", "");
			outPutMap.put("status", "");
			outPutMap.put("updatedAt", 0);

			outPutMap.put("initialFare", 0);
			outPutMap.put("perKmFare", 0);
			outPutMap.put("perMinuteFare", 0);
			outPutMap.put("bookingFees", 0);
			outPutMap.put("minimumFare", 0);
			outPutMap.put("discount", 0);
			outPutMap.put("cancellationCharges", 0);
			outPutMap.put("jobExpireTime", ProjectConstants.JOB_EXPIRE_TIME);

			outPutMap.put("rating", -1);

			outPutMap.put("promoCodeApplied", false);
			outPutMap.put("total", 0);
			outPutMap.put("promoDiscount", 0);
			outPutMap.put("usedCredits", 0);
			outPutMap.put("isSurgePriceApplied", false);

			outPutMap.put("surgePriceId", "-1");
			outPutMap.put("surgePrice", 1);
			outPutMap.put("totalWithSurge", 0);

			outPutMap.put("promoCodeId", "-");
			outPutMap.put("promoCode", "-");
			outPutMap.put("usage", "-");
			outPutMap.put("usageCount", "-");
			outPutMap.put("mode", "-");
			outPutMap.put("promoCodeDiscount", "-");
			outPutMap.put("startDate", "-");
			outPutMap.put("usedCount", "-");
			outPutMap.put("endDate", "-");

			outPutMap.put("distance", 0);
			outPutMap.put("charges", 0);

			outPutMap.put("rentalPackageId", "-1");
			outPutMap.put("rentalPackageTime", 0);
			outPutMap.put("rentalPackageDetails", new HashMap<String, Object>());

			outPutMap.put("isAirportFixedFareApplied", false);
			outPutMap.put("airportBookingType", "");

			CourierUtils.formCourierData(null, outPutMap);
		}

		if (tripType == ProjectConstants.TRIP_TYPE_ORDER_ID && orderModel != null) {
			OrderUtils.formOrdersNGBData(orderModel, outPutMap);
		}

		outPutMap.put("tripType", tripType);

		return outPutMap;
	}

	public static void updateDriverProcessingViaCronTime(TourModel tourModel) {
		tourModel.setDriverProcessingViaCronTime(DateUtils.nowAsGmtMillisec());
		tourModel.updateDriverProcessingViaCronTime();
	}
}
