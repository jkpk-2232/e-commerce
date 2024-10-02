package com.webapp.models;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.webapp.ProjectConstants;

public class PassangerNotificationThread extends Thread {

	private String deviceToken;

	private String tourId;

	private String apiSessionKey;

	private DecimalFormat df_new = new DecimalFormat("#######0.00");

	public PassangerNotificationThread(String deviceToken, String tourId, String apiSessionKey) {

		this.deviceToken = deviceToken;
		this.tourId = tourId;
		this.apiSessionKey = apiSessionKey;
		this.start();
	}

	public void run() {

		for (;;) {

			TourModel tour = TourModel.getTourDetailsByTourId(this.tourId);
			DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(tour.getDriverId());
			UserModel user = UserModel.getUserAccountDetailsById(tour.getDriverId());

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			if ((tour.getCarTypeId() == null) || ("".equals(tour.getCarTypeId())) || (("-1".equals(tour.getCarTypeId()))) || (ProjectConstants.Fifth_Vehicle_ID.equals(tour.getCarTypeId()))) {

				outPutMap.put("modelName", "");
				outPutMap.put("carColor", "");
				outPutMap.put("carPlateNo", "");
				outPutMap.put("carYear", "");
				outPutMap.put("noOfPassenger", "");
				outPutMap.put("make", "");
				outPutMap.put("owner", "");
			} else {

				CarModel car = CarModel.getCarDetailsByDriverId(tour.getDriverId());

				if (car != null) {

					outPutMap.put("modelName", car.getModelName());
					outPutMap.put("carColor", car.getCarColor());
					outPutMap.put("carPlateNo", car.getCarPlateNo());
					outPutMap.put("carYear", car.getCarYear());
					outPutMap.put("noOfPassenger", car.getNoOfPassenger());
					outPutMap.put("make", car.getMake());
					outPutMap.put("owner", car.getOwner());

				} else {

					outPutMap.put("modelName", "");
					outPutMap.put("carColor", "");
					outPutMap.put("carPlateNo", "");
					outPutMap.put("carYear", "");
					outPutMap.put("noOfPassenger", "");
					outPutMap.put("make", "");
					outPutMap.put("owner", "");
				}
			}

			outPutMap.put("latitude", driverLocation.getLatitude());
			outPutMap.put("longitude", driverLocation.getLongitude());
			outPutMap.put("slatitude", tour.getsLatitude());
			outPutMap.put("slongitude", tour.getsLongitude());
			outPutMap.put("carTypeId", tour.getCarTypeId());
			outPutMap.put("carTpeId", tour.getCarTypeId());
			outPutMap.put("status", tour.getStatus());
			outPutMap.put("tourId", this.tourId);
			outPutMap.put("fullName", tour.getFirstName());
			outPutMap.put("firstName", tour.getFirstName());
			outPutMap.put("lastName", tour.getLastName());
			outPutMap.put("dPhotoUrl", tour.getPhotoUrl());
			outPutMap.put("driverId", tour.getDriverId());
			outPutMap.put("driverPhoneNumber", user.getPhoneNo());
			outPutMap.put("driverPhoneNumberCode", user.getPhoneNoCode());
			outPutMap.put("initialFare", tour.getInitialFare());
			outPutMap.put("perKmFare", tour.getPerKmFare());
			outPutMap.put("perMinuteFare", tour.getPerMinuteFare());
			outPutMap.put("bookingFees", tour.getBookingFees());
			outPutMap.put("minimumFare", tour.getMinimumFare());
			outPutMap.put("discount", tour.getDiscount());
			outPutMap.put("promoCodeApplied", tour.isPromoCodeApplied());
			outPutMap.put("total", tour.getTotal());
			outPutMap.put("promoDiscount", tour.getPromoDiscount());
			outPutMap.put("usedCredits", tour.getUsedCredits());
			outPutMap.put("multicityCountryId", tour.getMulticityCountryId());
			outPutMap.put("multicityCityRegionId", tour.getMulticityCityRegionId());
			outPutMap.put("isSurgePriceApplied", tour.isSurgePriceApplied());

			if (tour.isSurgePriceApplied()) {

				outPutMap.put("surgePriceId", tour.getSurgePriceId());
				outPutMap.put("surgePrice", tour.getSurgePrice());
				outPutMap.put("totalWithSurge", tour.getTotalWithSurge());

			} else {

				outPutMap.put("surgePriceId", "-1");
				outPutMap.put("surgePrice", 1);
				outPutMap.put("totalWithSurge", 0);
			}

			if (tour.isPromoCodeApplied()) {

				PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tour.getPromoCodeId());

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

			outPutMap.put("paymentType", tour.getPaymentType());

			if (tour.isRideLater()) {

				outPutMap.put("dateTime", tour.getRideLaterPickupTime());

			} else {

				outPutMap.put("dateTime", tour.getCreatedAt());
			}

			// For rental
			outPutMap.put("isRentalBooking", tour.isRentalBooking());

			Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

			if (tour.isRentalBooking()) {

				outPutMap.put("rentalPackageId", tour.getRentalPackageId());
				outPutMap.put("rentalPackageTime", tour.getRentalPackageTime());

				rentalPackageDetails.put("carTypeId", tour.getCarTypeId());
				rentalPackageDetails.put("baseFare", df_new.format(tour.getInitialFare()));
				rentalPackageDetails.put("additionalPerKmFare", df_new.format(tour.getPerKmFare()));
				rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tour.getPerMinuteFare()));

				if (tour.getRentalPackageTime() == 1) {

					rentalPackageDetails.put("packageTime", tour.getRentalPackageTime() + " Hour");

				} else {

					rentalPackageDetails.put("packageTime", tour.getRentalPackageTime() + " Hours");
				}

				rentalPackageDetails.put("packageDistance", df_new.format(((tour.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

				outPutMap.put("rentalPackageDetails", rentalPackageDetails);

			} else {

				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", rentalPackageDetails);
			}

			// List<DriverTripRatingsModel> driverTripRatingsModelList =
			// DriverTripRatingsModel.getDriversTripRatingsList(tour.getDriverId());

			List<PassengerTripRatingsModel> passengerTripRatingsModelList = PassengerTripRatingsModel.getAlldriverRatings(tour.getDriverId());

			int driverAvgRate = 0;

			if (!passengerTripRatingsModelList.isEmpty()) {

				int size = passengerTripRatingsModelList.size();
				int rate = 0;

				for (PassengerTripRatingsModel passengerTripRatingsModel : passengerTripRatingsModelList) {

					rate += passengerTripRatingsModel.getRate();
				}

				driverAvgRate = rate / size;
			}

			outPutMap.put("rating", driverAvgRate);

			outPutMap.put("isAirportFixedFareApplied", tour.isAirportFixedFareApplied());
			outPutMap.put("airportBookingType", tour.getAirportBookingType());

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

				InvoiceModel invoice = InvoiceModel.getInvoiceByTourId(this.tourId);

				if (invoice != null) {
					JSONObject outputObject = new JSONObject(outPutMap);
					String messge = ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.PCB_SOCKET_START + deviceToken + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.PCB_SOCKET + outputObject.toString();
					WebSocketClient.sendDriverNotification(messge, tour.getPassengerId(), apiSessionKey);

				} else {

					try {
						sleep(2500);
						continue;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {

				JSONObject outputObject = new JSONObject(outPutMap);
				String messge = ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.PCB_SOCKET_START + deviceToken + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.PCB_SOCKET + outputObject.toString();
				WebSocketClient.sendDriverNotification(messge, tour.getPassengerId(), apiSessionKey);
			}

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)
						|| tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				break;
			}

			try {
				sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}