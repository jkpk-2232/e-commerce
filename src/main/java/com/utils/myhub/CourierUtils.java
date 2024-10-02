package com.utils.myhub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeutils.DateUtils;
import com.utils.CommonUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.EstimateFareAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.AppointmentSettingModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.OrderSettingModel;
import com.webapp.models.ServiceModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorServiceCategoryModel;

public class CourierUtils {

	public static List<String> listOfServiceTypesProductAddition() {
		List<String> serviceTypeIdList = Arrays.asList(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID, ProjectConstants.SUPER_SERVICE_TYPE_ID.APPOINTMENT_ID);
		return serviceTypeIdList;
	}

	public static List<String> getStatusListForCourierProcessingCronJob() {
		// Cron will pull in "new" couriers jobs
		return Arrays.asList(ProjectConstants.TourStatusConstants.NEW_TOUR);
	}

	public static boolean enableAddingCars(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID:
			return true;

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID:
			return true;

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID:
			return true;
		}

		return false;
	}

	public static String getServiceTypeCarIcon(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID:
			return UrlConstants.JSP_URLS.TRANSPORTATION_ICON;

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID:
			return UrlConstants.JSP_URLS.COURIER_ICON;

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID:
			return UrlConstants.JSP_URLS.ECOMMERCE_ICON;
		}

		return null;
	}

	public static String getServiceTypeName(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID:
			return BusinessAction.messageForKeyAdmin("labelTransportation");

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID:
			return BusinessAction.messageForKeyAdmin("labelCourier");

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID:
			return BusinessAction.messageForKeyAdmin("labelEcommerce");
		}

		return null;
	}

	public static boolean enableAddingVendorStores(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID:
			return true;

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.APPOINTMENT_ID:
			return true;
		}

		return false;
	}

	public static boolean isAppointmentService(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.APPOINTMENT_ID:
			return true;
		}

		return false;
	}

	public static boolean isCourierService(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID:
			return true;
		}

		return false;
	}

	public static boolean isEcommerceService(String serviceTypeId) {

		switch (serviceTypeId) {

		case ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID:
			return true;
		}

		return false;
	}

	public static void getServiceSpecificSettings(Map<String, Object> outputMap, VendorServiceCategoryModel vscm, ServiceModel serviceModel) {

		if (CourierUtils.isAppointmentService(serviceModel.getServiceTypeId())) {
			AppointmentSettingModel appointmentSettingModel = AppointmentSettingModel.getAppointmentSettingDetailsByServiceId(vscm.getServiceId());
			outputMap.put("appointmentSettingModel", appointmentSettingModel);
		} else if (CourierUtils.isEcommerceService(serviceModel.getServiceTypeId())) {
			OrderSettingModel orderSettingsModel = OrderSettingModel.getOrderSettingDetailsByServiceId(vscm.getServiceId());
			outputMap.put("orderSettingsModel", orderSettingsModel);
		}
	}

	public static Map<String, Object> getEstimateFare(TourModel tourModel, String multicityCityRegionId, String headerVendorId, boolean isEstimateFare, AdminSettingsModel adminSettingsModel) {

		Map<String, Object> estimateFareMap = new HashMap<>();

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

		Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());

		double distanceInMeters = distanceMatrix.get("distanceInMeters");
		double durationInMin = distanceMatrix.get("durationInMin");

		CarFareModel carFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(tourModel.getCarTypeId(), multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), headerVendorId, ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID);

		if (carFareModel == null) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorCourierCarFareNotAvailable");
			return estimateFareMap;
		}

		double estimateFareWithoutDiscount = EstimateFareAction.calculateFare(distanceInMeters, durationInMin, adminSettingsModel, carFareModel, isEstimateFare);
		double estimateFareWithDiscount = estimateFareWithoutDiscount;

		estimateFareMap.put("estimateFareWithDiscount", BusinessAction.df.format(estimateFareWithDiscount));
		estimateFareMap.put("estimateFareWithoutDiscount", BusinessAction.df.format(estimateFareWithoutDiscount));

		tourModel.setDistance(distanceInMeters);
		tourModel.setTotal(estimateFareWithoutDiscount);
		tourModel.setCharges(estimateFareWithoutDiscount);
		tourModel.setMulticityCityRegionId(multicityCityRegionId);
		tourModel.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());

		tourModel.setInitialFare(carFareModel.getInitialFare());
		tourModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
		tourModel.setPerKmFare(carFareModel.getPerKmFare());
		tourModel.setMinimumFare(carFareModel.getMinimumFare());
		tourModel.setDiscount(carFareModel.getDiscount());
		tourModel.setBookingFees(carFareModel.getBookingFees());
		tourModel.setFreeDistance(carFareModel.getFreeDistance());
		tourModel.setDriverAmount((carFareModel.getDriverPayablePercentage() * (tourModel.getTotal())) / 100);
		tourModel.setPercentage(carFareModel.getDriverPayablePercentage());
		tourModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
		tourModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());

		return estimateFareMap;
	}

	public static String placeCourierOrder(String loggedInuserId, TourModel tourModel, AdminSettingsModel adminSettingsModel, String headerVendorId) {

		String tourId = UUIDGenerator.generateUUID();

		tourModel.setTourId(tourId);
		tourModel.setDriverId(ProjectConstants.DEFAULT_DRIVER_ID);

		String passengerVendorId = MultiTenantUtils.getVendorIdByUserId(loggedInuserId);
		tourModel.setPassengerId(loggedInuserId);
		tourModel.setPassengerVendorId(passengerVendorId);

		UserProfileModel passengerUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);
		tourModel.setpFirstName(passengerUserModel.getFirstName());
		tourModel.setpLastName(passengerUserModel.getLastName());
		tourModel.setpEmail(passengerUserModel.getEmail());
		tourModel.setpPhone(passengerUserModel.getPhoneNo());
		tourModel.setpPhoneCode(passengerUserModel.getPhoneNoCode());
		tourModel.setpPhotoUrl(passengerUserModel.getPhotoUrl());

		tourModel.setAcknowledged(false);
		tourModel.setFixedFare(false);
		tourModel.setLanguage(ProjectConstants.ENGLISH_ID);
		tourModel.setPaymentType(ProjectConstants.CASH_ID);

		tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_PASSENGER);
		tourModel.setBookingType(ProjectConstants.INDIVIDUAL_BOOKING);

		String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;
		ApnsDeviceModel apnsDeviceModel = ApnsDeviceModel.getDeviseByUserId(loggedInuserId);
		if (apnsDeviceModel != null) {
			timeZone = apnsDeviceModel.getTimezone();
		}

		long pickupTime = DateUtils.getDateFromStringForRideLater(tourModel.getRideLaterPickupTimeLogs(), timeZone);

		tourModel.setRideLaterPickupTime(pickupTime);
		tourModel.setRideLater(true);
		tourModel.setTourRideLater(true);
		tourModel.setCriticalTourRideLater(false);
		tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);

		tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");
		tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getdLongitude() + "  " + tourModel.getdLatitude() + ")')");

		MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(tourModel.getMulticityCountryId());

		if (multicityCountryModel != null) {
			tourModel.setDistanceType(multicityCountryModel.getDistanceType());
			tourModel.setDistanceUnits(multicityCountryModel.getDistanceUnits());
			tourModel.setCurrencySymbol(multicityCountryModel.getCurrencySymbol());
			tourModel.setCurrencySymbolHtml(multicityCountryModel.getCurrencySymbolHtml());
		}

		tourModel.setDemandVendorPercentage(adminSettingsModel.getDemandVendorPercentage());
		tourModel.setSupplierVendorPercentage(adminSettingsModel.getSupplierVendorPercentage());

		CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
		tourModel.setCancellationCharges(cancellationChargeModel.getCharge());

		tourModel.setSurgePriceApplied(false);
		tourModel.setSurgePriceId(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);

		tourModel.setCourierOrderReceivedAgainstVendorId(headerVendorId);

		tourModel.createCourierOrder(loggedInuserId);

		TourTimeModel tourTimeModel = new TourTimeModel();
		tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
		tourTimeModel.setTourId(tourId);
		tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
		tourTimeModel.createTourTime();

		return tourId;
	}

	public static String searchDriver(AdminSettingsModel adminSettingsModel, TourModel tourModel, DriverWalletSettingsModel driverWalletSettingsModel) {

		// 16. Search near by driver -> minimumWalletAmount not needed as of now
		Map<String, Object> tempInputMap = new HashMap<>();
		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(tempInputMap, tourModel.getPassengerVendorId(), DateUtils.nowAsGmtMillisec());

		Map<String, Object> inputMap = formCommonInputMapForDriverSearch(adminSettingsModel, driverWalletSettingsModel, tourModel);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		// 1. search for specific vendor driver with search for subscribed driver 1st
		boolean searchSubscribedDriver = true;
		String driverId = getNearestDriver(tourModel, inputMap, tempInputMap, searchSubscribedDriver, "1");

		// 2. search for specific vendor driver with search for all subscribed driver
		// and unsubscribed drivers
		if (driverId == null) {
			searchSubscribedDriver = false;
			driverId = getNearestDriver(tourModel, inputMap, tempInputMap, searchSubscribedDriver, "1");
		}

		if (driverId != null && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)
					&& !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

			String freeDriverId = null;

			if (!tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {
				freeDriverId = tourModel.getDriverId();
			}

			DriverTourStatusUtils.updateDriverTourStatus(driverId, ProjectConstants.DRIVER_HIRED_STATUS);

			TourUtils.assignTourDriver(tourModel.getTourId(), driverId, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR, driverId);

			DriverTourRequestUtils.createDriverTourRequest(tourModel.getTourId(), driverId, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

			if (freeDriverId != null) {
				TourSearchDriverUtils.freeDriver(freeDriverId);
			}

			return driverId;

		} else {
			return null;
		}
	}

	private static Map<String, Object> formCommonInputMapForDriverSearch(AdminSettingsModel adminSettingsModel, DriverWalletSettingsModel driverWalletSettingsModel, TourModel tourModel) {

		String distance = GeoLocationUtil.getDistanceQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION);
		String latAndLong = GeoLocationUtil.getLatLngQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION, adminSettingsModel.getRadiusString());

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("tourId", tourModel.getTourId());
		inputMap.put("carTypeId", tourModel.getCarTypeId());
		inputMap.put("startOffSet", 0);
		inputMap.put("recordOffset", 1);
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("multicityCityRegionId", tourModel.getMulticityCityRegionId());
		inputMap.put("multicityCountryId", tourModel.getMulticityCountryId());
		inputMap.put("minimumWalletAmount", driverWalletSettingsModel != null ? driverWalletSettingsModel.getMinimumAmount() : 0.0);
		inputMap.put("transmissionTypeIdList", null);
		inputMap.put("passengerId", tourModel.getPassengerId());

		return inputMap;
	}

	private static String getNearestDriver(TourModel tourModel, Map<String, Object> inputMap, Map<String, Object> tempInputMap, boolean searchSubscribedDriver, String vendorType) {

		String vendorId = null;
		if (vendorType.equalsIgnoreCase("1")) {
			// search specific vendor ---> order received against vendor id
			vendorId = tourModel.getCourierOrderReceivedAgainstVendorId() != null ? tourModel.getCourierOrderReceivedAgainstVendorId() : MultiTenantUtils.getVendorIdByUserId(tourModel.getPassengerId());
		} else if (vendorType.equalsIgnoreCase("2")) {
			// search default vendor
			vendorId = WebappPropertyUtils.DEFAULT_VENDOR_ID;
		} else {
			// search all the vendors
			vendorId = null;
		}

		inputMap.put("vendorId", vendorId);
		inputMap.put("searchSubscribedDriver", searchSubscribedDriver);

		// 16. Search near by driver -> minimumWalletAmount not needed as of now
		// inputMap.putAll(tempInputMap);

		DriverGeoLocationModel driverGeoLocationModel = DriverGeoLocationModel.getNearestSingledriver(inputMap);
		return driverGeoLocationModel != null ? driverGeoLocationModel.getDriverId() : null;
	}

	public static void formCourierData(TourModel tourModel, Map<String, Object> outPutMap) {

		if (tourModel != null && tourModel.getServiceTypeId().equalsIgnoreCase(ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID)) {

			outPutMap.put("courierPickupAddress", tourModel.getCourierPickupAddress());
			outPutMap.put("courierContactPersonName", tourModel.getCourierContactPersonName());
			outPutMap.put("courierContactPhoneNo", tourModel.getCourierContactPhoneNo());
			outPutMap.put("courierDropAddress", tourModel.getCourierDropAddress());
			outPutMap.put("courierDropContactPersonName", tourModel.getCourierDropContactPersonName());
			outPutMap.put("courierDropContactPhoneNo", tourModel.getCourierDropContactPhoneNo());
			outPutMap.put("courierDetails", tourModel.getCourierDetails());
			outPutMap.put("courierOrderReceivedAgainstVendorId", tourModel.getCourierOrderReceivedAgainstVendorId());

		} else {

			outPutMap.put("courierPickupAddress", "-");
			outPutMap.put("courierContactPersonName", "-");
			outPutMap.put("courierContactPhoneNo", "-");
			outPutMap.put("courierDropAddress", "-");
			outPutMap.put("courierDropContactPersonName", "-");
			outPutMap.put("courierDropContactPhoneNo", "-");
			outPutMap.put("courierDetails", "-");
			outPutMap.put("courierOrderReceivedAgainstVendorId", "-");
		}
	}

	public static Map<String, Object> setCourierData(TourModel tourModel) {

		Map<String, Object> map = new HashMap<>();

		map.put("sLatitude", tourModel.getsLatitude());
		map.put("sLongitude", tourModel.getsLongitude());
		map.put("dLatitude", tourModel.getdLatitude());
		map.put("dLongitude", tourModel.getdLongitude());
		map.put("total", Double.parseDouble(BusinessApiAction.df.format(tourModel.getTotal())));
		map.put("charges", Double.parseDouble(BusinessApiAction.df.format(tourModel.getCharges())));
		map.put("status", tourModel.getStatus());
		map.put("sourceAddress", tourModel.getSourceAddress());
		map.put("destinationAddress", tourModel.getDestinationAddress());
		map.put("tourId", tourModel.getTourId());
		map.put("userTourId", tourModel.getUserTourId());
		map.put("passengerId", tourModel.getPassengerId());
		map.put("driverId", tourModel.getDriverId());
		map.put("driverName", tourModel.getFirstName() + " " + tourModel.getLastName());
		map.put("passengerName", tourModel.getpFirstName() + " " + tourModel.getpLastName());
		map.put("rideLaterPickupTime", tourModel.getRideLaterPickupTime());
		map.put("rideLaterPickupTimeLogs", tourModel.getRideLaterPickupTimeLogs());
		map.put("serviceTypeId", tourModel.getServiceTypeId());
		map.put("courierPickupAddress", tourModel.getCourierPickupAddress());
		map.put("courierContactPersonName", tourModel.getCourierContactPersonName());
		map.put("courierContactPhoneNo", tourModel.getCourierContactPhoneNo());
		map.put("courierDropAddress", tourModel.getCourierDropAddress());
		map.put("courierDropContactPersonName", tourModel.getCourierDropContactPersonName());
		map.put("courierDropContactPhoneNo", tourModel.getCourierDropContactPhoneNo());
		map.put("courierDetails", tourModel.getCourierDetails());
		map.put("courierOrderReceivedAgainstVendorId", tourModel.getCourierOrderReceivedAgainstVendorId());

		return map;
	}
}
