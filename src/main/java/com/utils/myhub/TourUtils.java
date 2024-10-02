package com.utils.myhub;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.utils.CommonUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.AirportRegionModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarDriversModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.EstimateFareModel;
import com.webapp.models.PassangerNotificationThread;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.RideLaterSettingsModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

public class TourUtils {

	public static List<String> getStatusListForToursProcessingCronJob() {
		// Cron will pull in "new" tours jobs
		return Arrays.asList(ProjectConstants.TourStatusConstants.NEW_TOUR, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);
	}

	public static String[] getStatusArray(String statusFilter) {

		String[] statusArray;

		if (ProjectConstants.TourStatusConstants.NEW_TOUR_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.NEW_TOUR;
		} else if (ProjectConstants.TourStatusConstants.STARTED_TOUR_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.STARTED_TOUR;
		} else if (ProjectConstants.TourStatusConstants.ENDED_TOUR_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.ENDED_TOUR;
		} else if (ProjectConstants.TourStatusConstants.ASSIGNED_TOUR_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.ASSIGNED_TOUR;
		} else if (ProjectConstants.TourStatusConstants.EXPIRED_TOUR_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.EXPIRED_TOUR;
		} else if (ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST;
		} else if (ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER;
		} else if (ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER;
		} else if (ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE;
		} else if (ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR_ID.equals(statusFilter)) {
			statusArray = new String[1];
			statusArray[0] = ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR;
		} else {
			statusArray = new String[10];
			statusArray[0] = ProjectConstants.TourStatusConstants.NEW_TOUR;
			statusArray[1] = ProjectConstants.TourStatusConstants.STARTED_TOUR;
			statusArray[2] = ProjectConstants.TourStatusConstants.ENDED_TOUR;
			statusArray[3] = ProjectConstants.TourStatusConstants.ASSIGNED_TOUR;
			statusArray[4] = ProjectConstants.TourStatusConstants.EXPIRED_TOUR;
			statusArray[5] = ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST;
			statusArray[6] = ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER;
			statusArray[7] = ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER;
			statusArray[8] = ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE;
			statusArray[9] = ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR;
		}

		return statusArray;
	}

	public static String getTourStatus(String status) {

		String tourStatus;

		if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelCancelledByDriver");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelCancelledByPassenger");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelPassengerUnavailable");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelDriverArrivedAndWaiting");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.STARTED_TOUR)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelTourStarted");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelTourEnded");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelTourAssigned");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelTourExpired");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.NEW_TOUR)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelNewTour");
		} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelTourAccepted");
		} else {
			tourStatus = BusinessAction.messageForKeyAdmin("labelTourStatusNotAvailable");
		}

		if (tourStatus.contains(".")) {
			tourStatus = tourStatus.replace(".", "");
		}

		return tourStatus;
	}

	public static String getRideLaterTourStatus(TourModel tourModel) {

		String tourStatus = ProjectConstants.NOT_AVAILABLE;

		if (ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelRLNew");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelRLAssigned");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelRLReassigned");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelRLAccepted");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = BusinessAction.messageForKeyAdmin("labelRLPending");
		} else {
			tourStatus = tourModel.getStatus();
		}

		return tourStatus;
	}

	public static boolean rideLaterDriverAssignmentCriteriaStatus(TourModel tourModel) {

		return tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST)
					|| tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR);
	}

	public static double[] getSurgeArray(String surgeFilter) {

		double[] surgeArray;

		if (surgeFilter == null || "-1".equals(surgeFilter) || "".equals(surgeFilter) || "1".equals(surgeFilter)) {

			surgeArray = new double[14];

			surgeArray[0] = 0;

			for (double surge = 1, i = 1; surge <= 4.0; surge = surge + 0.25, i++) {

				surgeArray[((int) i)] = surge;
			}
		} else {
			surgeArray = new double[1];

			for (double surge = 1.25; surge <= 4.0; surge = surge + 0.25) {

				if ((Double.parseDouble(surgeFilter)) == surge) {

					surgeArray[0] = surge;
				}
			}
		}

		return surgeArray;
	}

	public static void assignTourDriver(String tourId, String driverId, String status, String loggedInUserId) {

		TourModel tourModel = new TourModel();
		tourModel.setTourId(tourId);
		tourModel.setDriverId(driverId);
		tourModel.setStatus(status);

		tourModel.assignTourDriver(loggedInUserId);
	}

	public static void updateTourCriticalStatus(TourModel tour, boolean status) {
		TourModel criticalTour = new TourModel();
		criticalTour.setTourId(tour.getTourId());
		criticalTour.setCriticalTourRideLater(status);
		criticalTour.updateTourStatusCritical();
	}

	public static void updateTourStatusByTourId(String tourId, String driverId, String status) {
		TourModel tour = new TourModel();
		tour.setTourId(tourId);
		tour.setDriverId(driverId);
		tour.setStatus(status);
		tour.updateTourStatusByTourId();
	}

	public static void updateTourCarIdByTourId(String tourId, String driverId) {

		CarDriversModel carDriversModel = CarDriversModel.getCarDriverDetailsByDriverId(driverId);

		if (carDriversModel != null) {
			TourModel tour1 = new TourModel();
			tour1.setTourId(tourId);
			tour1.setDriverId(driverId);
			tour1.setCarId(carDriversModel.getCarId());

			tour1.updateTourCarIdByTourId();
		}
	}

	public static void updateTourAcceptedTime(String tourId) {
		TourTimeModel tourTimeModel = new TourTimeModel();
		tourTimeModel.setTourId(tourId);
		tourTimeModel.setAcceptTime(DateUtils.nowAsGmtMillisec());
		tourTimeModel.updateTourAcceptedTime();
	}

	public static void updateTakeBookingByDriverParams(String loggedInuserId, TourModel tourModel) {
		tourModel.setTakeBookingByDriver(true);
		tourModel.setTakeBookingDriverId(loggedInuserId);
		tourModel.setTakeBookingByDriverTime(DateUtils.nowAsGmtMillisec());
		tourModel.updateTakeBookingByDriverParams();
	}

	public static List<String> getTourStatusListAsPerType(String type) {

		List<String> statusList = new ArrayList<>();

		switch (type) {

		case ProjectConstants.OrderDeliveryConstants.ORDERS_NEW_TAB:
			statusList.add(ProjectConstants.TourStatusConstants.NEW_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING);
			break;

		case ProjectConstants.OrderDeliveryConstants.ORDERS_ACTIVE_TAB:
			statusList.add(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST);
			statusList.add(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.STARTED_TOUR);
			break;

		case ProjectConstants.OrderDeliveryConstants.ORDERS_ALL_OTHERS_TAB:
			statusList.add(ProjectConstants.TourStatusConstants.ENDED_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER);
			statusList.add(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE);
			statusList.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
			statusList.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_ADMIN);
			statusList.add(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_EXPIRED_TOUR);
			break;

		default:
			break;
		}

		return statusList;
	}

	public static Map<String, Object> checkBookingForAirportPickupOrDrop(String sLat, String sLng, String dLat, String dLng) {

		Map<String, Object> airportPickupOrDropMap = new HashMap<String, Object>();

		AirportRegionModel sAirportRegionModel = TourUtils.getAirportRegionModelByLatLng(sLat, sLng);
		AirportRegionModel dAirportRegionModel = TourUtils.getAirportRegionModelByLatLng(dLat, dLng);
		AirportRegionModel airportRegionModel = null;

		airportPickupOrDropMap.put("isAirportPickUp", false);
		airportPickupOrDropMap.put("isAirportDrop", false);

		if (sAirportRegionModel != null) {

			airportPickupOrDropMap.put("isAirportPickUp", true);
			airportRegionModel = sAirportRegionModel;
		}

		if (dAirportRegionModel != null) {

			airportPickupOrDropMap.put("isAirportDrop", true);
			airportRegionModel = dAirportRegionModel;
		}

		if (sAirportRegionModel != null && dAirportRegionModel != null) {
			airportRegionModel = (sAirportRegionModel.getCreatedAt() < dAirportRegionModel.getCreatedAt()) ? sAirportRegionModel : dAirportRegionModel;
		}

		airportPickupOrDropMap.put("airport", airportRegionModel);

		return airportPickupOrDropMap;
	}

	public static AirportRegionModel getAirportRegionModelByLatLng(String lat, String lng) {

		AirportRegionModel airportRegionModel = null;

		if (((lat != null) && (!"".equals(lat))) && ((lng != null) && (!"".equals(lng)))) {

			String latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + lat + " " + lng + ")'), area_polygon)";
			airportRegionModel = AirportRegionModel.getAirportRegionContainingLatLng(latAndLong);
		}

		return airportRegionModel;
	}

	public static CitySurgeModel getApplicableRadiusSurge(EstimateFareModel estimateFareModel, String regionId, AdminSettingsModel adminSettingsModel) {

		CitySurgeModel applicableCitySurgeModel = null;

		// String constant = "\"WGS 84\"";
		// String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry,
		// ST_GeomFromText('POINT(" + estimateFareModel.getsLongitude() + " " +
		// estimateFareModel.getsLatitude() + ")',4326), 'SPHEROID[" + constant +
		// ",6378137,298.257223563]')As numeric),2)";
		String distance = GeoLocationUtil.getDistanceQuery(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION);

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + estimateFareModel.getsLongitude() + " " + estimateFareModel.getsLatitude() + ")'),  " + adminSettingsModel.getRadiusString() + ")";

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		// Not considered Driver subscription validity
		List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(latAndLong, distance, regionId, Arrays.asList(estimateFareModel.getCarTypeId()), null, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, null, 0, adminSettingsModel);

		if (!carLocation.isEmpty()) {
			return applicableCitySurgeModel;
		}

		List<CitySurgeModel> citySurgeModelList = CitySurgeModel.getCitySurgeByRegionIdAndRadiusOrder(regionId, ProjectConstants.SORT_ASC);

		if (citySurgeModelList != null) {

			for (CitySurgeModel citySurgeModel : citySurgeModelList) {

				String latAndLong1 = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + estimateFareModel.getsLongitude() + " " + estimateFareModel.getsLatitude() + ")'),  "
							+ String.valueOf(citySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits()) + ")";

				List<DriverGeoLocationModel> carLocation1 = DriverGeoLocationModel.getNearByCarList(latAndLong1, distance, regionId, Arrays.asList(estimateFareModel.getCarTypeId()), null, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, null, 0,
							adminSettingsModel);

				if (!carLocation1.isEmpty()) {
					applicableCitySurgeModel = citySurgeModel;
					break;
				}
			}
		}

		return applicableCitySurgeModel;
	}

	public static int getDriverAverageRatings(String driverId) {

		List<DriverTripRatingsModel> driverTripRatingsModelList = DriverTripRatingsModel.getDriversTripRatingsList(driverId);

		int driverAvgRate = 0;

		if (!driverTripRatingsModelList.isEmpty()) {

			int size = driverTripRatingsModelList.size();
			int rate = 0;

			for (DriverTripRatingsModel driverTripRatingsModel : driverTripRatingsModelList) {
				rate += driverTripRatingsModel.getRate();
			}

			driverAvgRate = rate / size;
		}

		return (Math.round(driverAvgRate) * 10);
	}

	public static boolean checkForConflictingTourRideLater(String tourId, String driverId, long rideLaterPickupTime, RideLaterSettingsModel rideLaterSettingsModel) {

		long beforePickupTime = rideLaterPickupTime - (rideLaterSettingsModel.getPassengerTourAfterTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);
		long afterPickupTime = rideLaterPickupTime + (rideLaterSettingsModel.getPassengerTourAfterTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);

		int conflictingTimeSlots = TourModel.checkForConflictingSlotForRideLater(tourId, driverId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID, beforePickupTime, afterPickupTime);

		return conflictingTimeSlots > 0;
	}

	public static boolean checkForConflictingTourRideLater(String tourId, String driverId, long rideLaterPickupTime) {

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		return checkForConflictingTourRideLater(tourId, driverId, rideLaterPickupTime, rideLaterSettingsModel);
	}

	public static Map<String, Object> acceptTourByDriver(HttpServletRequest request, String headerVendorId, String tourId, String driverId, boolean isTakeBookingRideNow) {

		Map<String, Object> outputMap = new HashMap<String, Object>();
		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
			String errorMessage = String.format(BusinessApiAction.messageForKey("errorTripCancelledByPassenger", request), MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, errorMessage);
		}

		if (!(tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR))) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, BusinessApiAction.messageForKey("errorTourNotEligible", request));
			return outputMap;
		}

		//@formatter:off
		if ((tourModel.getDriverId().equals(driverId)
				&& !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) 
				&& !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR))|| isTakeBookingRideNow) {
		//@formatter:on

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
			AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

			TourUtils.updateTourStatusByTourId(tourId, driverId, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);

			if (isTakeBookingRideNow) {
				TourUtils.assignTourDriver(tourId, driverId, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST, driverId);
			}

			MultiTenantUtils.changeTourFareParametersWithVendorFare(tourId, driverId);

			DriverTourRequestUtils.updateDriverTourRequest(tourId, driverId, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);

			TourUtils.updateTourCarIdByTourId(tourId, driverId);

			DriverSubscriptionUtils.updateDriverSubscriptionAgainstTour(tourId, driverId, headerVendorId);

			TourUtils.updateTourAcceptedTime(tourId);

			TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

			DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(tourDetails.getDriverId());

			double distance = CommonUtils.distance(Double.parseDouble(tourDetails.getsLatitude()), Double.parseDouble(tourDetails.getsLongitude()), Double.parseDouble(driverLocation.getLatitude()), Double.parseDouble(driverLocation.getLongitude()), 'K');
			double min = CommonUtils.calculateETA(distance, adminSettingsModel.getDistanceUnits());

			DecimalFormat df = new DecimalFormat("0");

			String message = String.format(BusinessAction.messageForKeyAdmin("driverAction_1", tourModel.getLanguage()), df.format(min));
			String templateId = ProjectConstants.SMSConstants.SMS_ARRIVING_TEMPLATE_ID;

			if (df.format(min).equals("0")) {
				message = BusinessAction.messageForKeyAdmin("driverAction_3", tourModel.getLanguage());
				templateId = ProjectConstants.SMSConstants.SMS_AWAY_TEMPLATE_ID;
			}

			if (tourDetails.getBookingType().equals(ProjectConstants.INDIVIDUAL_BOOKING)) {

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setMessage(message);
				apnsMessage.setMessageType("push");
				apnsMessage.setToUserId(tourDetails.getPassengerId());
				apnsMessage.insertPushMessage();

				if (adminSmsSendingModel.ispAcceptByDriver()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone().trim(), templateId);
				}

				if (apnsDevice != null) {

					if (tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.INDIVIDUAL_BOOKING)) {
						new PassangerNotificationThread(apnsDevice.getDeviceToken(), tourId, apnsDevice.getApiSessionKey());
					}

					// apnsDevice.sendNotification("1", "Push", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					apnsDevice.sendFCMNotification("1", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, BusinessAction.messageForKeyAdmin("tripConfirmation", tourModel.getLanguage()));
				}

			} else {

				if (adminSmsSendingModel.ispAcceptByDriver()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone(), templateId);
				}

				if (adminSmsSendingModel.isBoAccepted()) {

					UserModel user = UserModel.getUserAccountDetailsById(tourDetails.getCreatedBy());

					String businessOwnermessage = String.format(BusinessAction.messageForKeyAdmin("msgAdminWhenDriverAcceptRequest1", tourDetails.getLanguage()), user.getFirstName(), tourDetails.getpFirstName() + " " + tourDetails.getpLastName());

					if (user != null) {
						MetamorphSystemsSmsUtils.sendSmsToSingleUser(businessOwnermessage, user.getPhoneNoCode() + user.getPhoneNo(), ProjectConstants.SMSConstants.SMS_PASSENGER_TEMPLATE_ID);
					}
				}

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setMessage(message);
				apnsMessage.setMessageType("push");
				apnsMessage.setToUserId(tourDetails.getPassengerId());
				apnsMessage.insertPushMessage();

				if (adminSmsSendingModel.ispAcceptByDriver()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone().trim(), templateId);
				}

				if (apnsDevice != null) {
					// apnsDevice.sendNotification("1", "Push", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					apnsDevice.sendFCMNotification("1", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, BusinessAction.messageForKeyAdmin("tripConfirmation", tourModel.getLanguage()));
				}
			}

			String pushMessage = BusinessAction.messageForKeyAdmin("pushMsgForPaymentMode1", tourDetails.getLanguage()) + " " + tourDetails.getUserTourId() + BusinessAction.messageForKeyAdmin("pushMsgForPaymentMode2", tourDetails.getLanguage());

			if (ProjectConstants.CASH_ID.equalsIgnoreCase(tourDetails.getPaymentType())) {
				pushMessage += " " + ProjectConstants.C_CASH + ".";
			} else {
				pushMessage += " " + ProjectConstants.C_CARD + ".";
			}

			ApnsDeviceModel apnsDeviceOfDriver = ApnsDeviceModel.getDeviseByUserId(tourDetails.getDriverId());

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(pushMessage);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(tourDetails.getDriverId());
			apnsMessage.insertPushMessage();

			if (apnsDeviceOfDriver != null) {
				apnsDeviceOfDriver.sendNotification("1", "Push", pushMessage, ProjectConstants.PAYMENT_MODE, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}

			outputMap.put("distance", tourModel.getDistance());
			outputMap.put("charges", tourModel.getCharges());

			outputMap.put("paymentType", tourModel.getPaymentType());

			outputMap.put("initialFare", tourDetails.getInitialFare());
			outputMap.put("perKmFare", tourDetails.getPerKmFare());
			outputMap.put("perMinuteFare", tourDetails.getPerMinuteFare());
			outputMap.put("bookingFees", tourDetails.getBookingFees());
			outputMap.put("minimumFare", tourDetails.getMinimumFare());
			outputMap.put("discount", tourDetails.getDiscount());
			outputMap.put("cancellationCharges", CancellationChargeModel.getAdminCancellationCharges().getCharge());

			outputMap.put("promoCodeApplied", tourModel.isPromoCodeApplied());
			outputMap.put("total", tourModel.getTotal());
			outputMap.put("promoDiscount", tourModel.getPromoDiscount());
			outputMap.put("usedCredits", tourModel.getUsedCredits());

			if (tourDetails.isPromoCodeApplied()) {

				PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourDetails.getPromoCodeId());

				if (promoCodeModel != null) {
					outputMap.put("promoCodeId", promoCodeModel.getPromoCodeId());
					outputMap.put("promoCode", promoCodeModel.getPromoCode());
					outputMap.put("usage", promoCodeModel.getUsage());
					outputMap.put("usageCount", promoCodeModel.getUsageCount());
					outputMap.put("mode", promoCodeModel.getMode());
					outputMap.put("promoCodeDiscount", promoCodeModel.getDiscount());
					outputMap.put("startDate", promoCodeModel.getStartDate());
					outputMap.put("usedCount", promoCodeModel.getUsedCount());
					outputMap.put("endDate", promoCodeModel.getEndDate());
				} else {
					outputMap.put("promoCodeId", "-");
					outputMap.put("promoCode", "-");
					outputMap.put("usage", "-");
					outputMap.put("usageCount", "-");
					outputMap.put("mode", "-");
					outputMap.put("promoCodeDiscount", "-");
					outputMap.put("startDate", "-");
					outputMap.put("usedCount", "-");
					outputMap.put("endDate", "-");
				}
			} else {

				outputMap.put("promoCodeId", "-");
				outputMap.put("promoCode", "-");
				outputMap.put("usage", "-");
				outputMap.put("usageCount", "-");
				outputMap.put("mode", "-");
				outputMap.put("promoCodeDiscount", "-");
				outputMap.put("startDate", "-");
				outputMap.put("usedCount", "-");
				outputMap.put("endDate", "-");
			}

			outputMap.put("tip", UserProfileModel.getUserAccountDetailsById(tourDetails.getPassengerId()).getTip());

			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

			return outputMap;

		} else {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, BusinessApiAction.messageForKey("errorTripAssignedToOtherDriver", request));
		}

		return outputMap;
	}
}
