package com.webapp.actions.api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverTourRequestModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.OrderModel;
import com.webapp.models.PassengerTripRatingsModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UserPromoCodeModel;
import com.webapp.models.UtilizedUserPromoCodeModel;

@Path("/api/tours")
public class TourAction extends BusinessApiAction {

	@GET
	@Path("/cancellation-charges")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCancellationCharges(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();

		FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();

		double charges = cancellationChargeModel.getCharge();
		Map<String, Object> outPutMap = new HashMap<String, Object>();

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		String message = messageForKey("cancellationCharges1", request) + " " + convertVerificationCode(String.valueOf(df.format(freeWaitingTimeModel.getCancelTime()))) + " " + messageForKey("cancellationCharges2", request) + ", " + adminSettings.getCurrencySymbol()
					+ convertVerificationCode(String.valueOf(charges)) + " " + messageForKey("cancellationCharges3", request);

		outPutMap.put("cancelTime", freeWaitingTimeModel.getCancelTime());
		outPutMap.put("charges", charges);
		outPutMap.put("message", message);

		return sendDataResponse(outPutMap);
	}

	@POST
	@Path("/check-promo-code")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response checkPromoCode(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			PromoCodeModel promoCodeModel) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(promoCodeModel.getPromoCode());

		long currentTime = DateUtils.nowAsGmtMillisec();

		if (promoCode != null) {

			if (currentTime >= promoCode.getStartDate() && currentTime <= promoCode.getEndDate()) {

				UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = UtilizedUserPromoCodeModel.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInuserId, promoCode.getPromoCodeId());

				if (utilizedUserPromoCodeModel != null) {

					return sendBussinessError(messageForKey("errorUsedPromoCode", request));
				}

				if (promoCode.getUsageType().equalsIgnoreCase(ProjectConstants.ALL_ID)) {

					if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

						return sendDataResponse(promoCode);

					} else {

						if (promoCode.getUsedCount() < promoCode.getUsageCount()) {
							return sendDataResponse(promoCode);
						} else {
							return sendBussinessError(messageForKey("errorPromoCodeExpired", request));
						}
					}

				} else {

					UserPromoCodeModel userPromoCodeModel = UserPromoCodeModel.getUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInuserId, promoCode.getPromoCodeId());

					if (userPromoCodeModel != null) {
						return sendDataResponse(promoCode);
					} else {
						return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
					}
				}

			} else {

				return sendBussinessError(messageForKey("errorPromoCodeExpired", request) + "1");
			}

		} else {

			return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
		}
	}

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTourListByPagination(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("start") long start,
			@PathParam("length") long length,
			@QueryParam("afterTime") long afterTime
			) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<TourModel> tourList = new ArrayList<TourModel>();

		UserModel user = UserModel.getUserActiveDeativeDetailsById(loggedInuserId);

		afterTime = DateUtils.nowAsGmtMillisec() - (24 * ProjectConstants.ONE_HOUR_MILLISECONDS_LONG);

		if (user.getUserRole().equals(UserRoles.PASSENGER_ROLE) || user.getUserRole().equals(UserRoles.BUSINESS_OWNER_ROLE) || user.getUserRole().equals(UserRoles.OPERATOR_ROLE)) {
			tourList = TourModel.getToursByPassengerIdPagination(loggedInuserId, afterTime, start, length);
		} else {
			tourList = TourModel.getToursByDriverIdPagination(loggedInuserId, afterTime, start, length);
		}

		List<Map<String, Object>> outerOutPutMap = new ArrayList<Map<String, Object>>();

		for (TourModel tourModel : tourList) {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			outPutMap.put("tourId", tourModel.getTourId());
			outPutMap.put("userTourId", tourModel.getUserTourId());
			outPutMap.put("passengerId", tourModel.getPassengerId());

			outPutMap.put("driverId", tourModel.getDriverId());

			outPutMap.put("distance", tourModel.getDistance());
			outPutMap.put("paymentType", tourModel.getPaymentType());

			outPutMap.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
			outPutMap.put("airportBookingType", tourModel.getAirportBookingType());

			if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

				outPutMap.put("charges", tourModel.getFinalAmountCollected());
				outPutMap.put("isInvoice", true);

			} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				InvoiceModel invoiceForCancelledTour = InvoiceModel.getInvoiceByTourId(tourModel.getTourId());

				if (invoiceForCancelledTour != null && invoiceForCancelledTour.getFine() > 0) {

					outPutMap.put("charges", invoiceForCancelledTour.getCharges());
					outPutMap.put("isInvoice", true);

				} else {

					outPutMap.put("charges", tourModel.getCharges());
					outPutMap.put("isInvoice", false);
				}
			} else {

				outPutMap.put("charges", tourModel.getCharges());
				outPutMap.put("isInvoice", false);
			}

			outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));
			if (user.getUserRole().equals(UserRoles.DRIVER_ROLE)) {

				if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ENDED_TOUR) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)
							|| tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.STARTED_TOUR) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

					double tourEndTime = tourModel.getUpdatedAt();
					double currentTime = DateUtils.nowAsGmtMillisec();
					double timeDiff = currentTime - (24 * ProjectConstants.ONE_HOUR_MILLISECONDS_LONG);

					if (tourEndTime < timeDiff) {
						outPutMap.put("firstName", "");
						outPutMap.put("lastName", "");
						outPutMap.put("email", "");
						outPutMap.put("phone", "");
						outPutMap.put("phoneCode", "");
						outPutMap.put("photoUrl", "");
					} else {
						outPutMap.put("firstName", tourModel.getpFirstName());
						outPutMap.put("lastName", tourModel.getpLastName());
						outPutMap.put("email", tourModel.getpEmail());
						outPutMap.put("phone", tourModel.getpPhone());
						outPutMap.put("phoneCode", tourModel.getpPhoneCode());
						outPutMap.put("photoUrl", tourModel.getpPhotoUrl());
					}
				}

			} else {
				if (!tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
					outPutMap.put("firstName", tourModel.getFirstName());
					outPutMap.put("lastName", tourModel.getLastName());
					outPutMap.put("email", tourModel.getEmail());
					outPutMap.put("phone", tourModel.getPhoneNo());
					outPutMap.put("phoneCode", tourModel.getPhoneNoCode());
					outPutMap.put("photoUrl", tourModel.getPhotoUrl());
				} else {
					outPutMap.put("firstName", "");
					outPutMap.put("lastName", "");
					outPutMap.put("email", "");
					outPutMap.put("phone", "");
					outPutMap.put("phoneCode", "");
					outPutMap.put("photoUrl", "");

				}
			}
			outPutMap.put("isFixedFare", tourModel.isFixedFare());
			outPutMap.put("carTypeId", tourModel.getCarTypeId());
			outPutMap.put("sourceLatitude", tourModel.getsLatitude());
			outPutMap.put("sourceLongitude", tourModel.getsLongitude());
			outPutMap.put("destinationLatitude", tourModel.getdLatitude());
			outPutMap.put("destinationLongitude", tourModel.getdLongitude());

			outPutMap.put("initialFare", tourModel.getInitialFare());
			outPutMap.put("perKmFare", tourModel.getPerKmFare());
			outPutMap.put("perMinuteFare", tourModel.getPerMinuteFare());
			outPutMap.put("bookingFees", tourModel.getBookingFees());
			outPutMap.put("minimumFare", tourModel.getMinimumFare());
			outPutMap.put("discount", tourModel.getDiscount());

			outPutMap.put("promoCodeApplied", tourModel.isPromoCodeApplied());
			outPutMap.put("total", tourModel.getTotal());
			outPutMap.put("promoDiscount", tourModel.getPromoDiscount());
			outPutMap.put("usedCredits", tourModel.getUsedCredits());

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

			outPutMap.put("sourceAddress", tourModel.getSourceAddress());
			outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
			outPutMap.put("status", tourModel.getStatus());
			outPutMap.put("isRideLater", tourModel.isRideLater());
			outPutMap.put("isAcknowledged", tourModel.isAcknowledged());

			outPutMap.put("isRentalBooking", tourModel.isRentalBooking());

			Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

			if (tourModel.isRentalBooking()) {

				outPutMap.put("rentalPackageId", tourModel.getRentalPackageId());
				outPutMap.put("rentalPackageTime", tourModel.getRentalPackageTime());

				rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
				rentalPackageDetails.put("baseFare", df_new.format(tourModel.getInitialFare()));
				rentalPackageDetails.put("additionalPerKmFare", df_new.format(tourModel.getPerKmFare()));
				rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tourModel.getPerMinuteFare()));

				if (tourModel.getRentalPackageTime() == 1) {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

				} else {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
				}

				rentalPackageDetails.put("packageDistance", df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

				outPutMap.put("rentalPackageDetails", rentalPackageDetails);

			} else {

				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", rentalPackageDetails);
			}

			outerOutPutMap.add(outPutMap);
		}

		if (tourList.size() != 0) {
			afterTime = DateUtils.nowAsGmtMillisec();
			Map<Object, Object> res = new HashMap<Object, Object>();
			res.put("afterTime", afterTime);
			res.put("tourList", outerOutPutMap);
			return sendDataResponse(res);
		} else {
			return sendBussinessError(messageForKey("errorNoDataAvailableInvoice", request));
		}
	}

	@Path("/current")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCurrentTour(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = null;

		UserModel user = UserModel.getUserActiveDeativeDetailsById(loggedInuserId);

		if (user.getUserRole().equals(UserRoles.PASSENGER_ROLE)) {

			tourModel = TourModel.getCurrentTourByPassangerId(loggedInuserId);

		} else {

			tourModel = TourModel.getCurrentTourByDriverId(loggedInuserId);

			if (tourModel != null) {

				long currentTime = 0;
				long oldTime = 0;
				long diffTime = 0;

				if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

					DriverTourRequestModel driverTourRequestModel = DriverTourRequestModel.getTourRequestByDriverIdAndTourId(tourModel.getDriverId(), tourModel.getTourId());

					if (driverTourRequestModel != null) {

						currentTime = DateUtils.nowAsGmtMillisec();
						oldTime = driverTourRequestModel.getCreatedAt();
						diffTime = ProjectConstants.REQUEST_TIME_ONE * 1000;

						if ((currentTime - oldTime) > diffTime) {

							DriverTourStatusUtils.updateDriverTourStatus(loggedInuserId, ProjectConstants.DRIVER_FREE_STATUS);

							return sendBussinessError(messageForKey("errorNoJobAvailableInvoice", request));
						}
					}
				}
			}
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		if (tourModel != null) {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(tourModel.getCarTypeId());

			outPutMap.put("tourId", tourModel.getTourId());
			outPutMap.put("userTourId", tourModel.getUserTourId());
			outPutMap.put("passengerId", tourModel.getPassengerId());

			outPutMap.put("driverId", tourModel.getDriverId());
			outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));

			outPutMap.put("distance", tourModel.getDistance());
			outPutMap.put("charges", tourModel.getCharges());
			outPutMap.put("carTypeId", tourModel.getCarTypeId());
			outPutMap.put("paymentType", tourModel.getPaymentType());

			outPutMap.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
			outPutMap.put("airportBookingType", tourModel.getAirportBookingType());

			if (carTypeModel != null) {
				outPutMap.put("carType", carTypeModel.getCarType());
			} else {
				outPutMap.put("carType", "");
			}

			if (user.getUserRole().equals(UserRoles.DRIVER_ROLE)) {

				outPutMap.put("firstName", tourModel.getpFirstName());
				outPutMap.put("lastName", tourModel.getpLastName());
				outPutMap.put("email", tourModel.getpEmail());
				outPutMap.put("phone", tourModel.getpPhone());
				outPutMap.put("phoneCode", tourModel.getpPhoneCode());
				outPutMap.put("photoUrl", tourModel.getpPhotoUrl());

				outPutMap.put("isRideLater", tourModel.isRideLater());
				outPutMap.put("isAcknowledged", tourModel.isAcknowledged());

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

			} else if (!user.getUserRole().equals(UserRoles.DRIVER_ROLE) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				outPutMap.put("firstName", tourModel.getFirstName());
				outPutMap.put("lastName", tourModel.getLastName());
				outPutMap.put("email", tourModel.getEmail());
				outPutMap.put("phone", tourModel.getPhoneNo());
				outPutMap.put("phoneCode", tourModel.getPhoneNoCode());
				outPutMap.put("photoUrl", tourModel.getPhotoUrl());

				List<PassengerTripRatingsModel> ratingList = PassengerTripRatingsModel.getAlldriverRatings(tourModel.getDriverId());

				double rating = 0.0;

				for (PassengerTripRatingsModel paRating : ratingList) {
					rating = rating + paRating.getRate();
				}

				DecimalFormat df = new DecimalFormat("#.#");

				if (rating != 0.0) {
					rating = (double) rating / ratingList.size();
					outPutMap.put("rating", df.format(rating));
				} else {
					outPutMap.put("rating", -1);
				}

			} else {

				outPutMap.put("firstName", "-");
				outPutMap.put("lastName", "-");
				outPutMap.put("email", "-");
				outPutMap.put("phone", "-");
				outPutMap.put("photoUrl", "-");
			}

			outPutMap.put("sourceLatitude", tourModel.getsLatitude());
			outPutMap.put("sourceLongitude", tourModel.getsLongitude());
			outPutMap.put("destinationLatitude", tourModel.getdLatitude());
			outPutMap.put("destinationLongitude", tourModel.getdLongitude());

			outPutMap.put("sourceAddress", tourModel.getSourceAddress());
			outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
			outPutMap.put("status", tourModel.getStatus());

			outPutMap.put("updatedAt", tourModel.getUpdatedAt());

			long milliSecond = DateUtils.nowAsGmtMillisec() - tourModel.getUpdatedAt();
			long jobExpireTime = milliSecond / 1000;

			outPutMap.put("initialFare", tourModel.getInitialFare());
			outPutMap.put("perKmFare", tourModel.getPerKmFare());
			outPutMap.put("perMinuteFare", tourModel.getPerMinuteFare());
			outPutMap.put("bookingFees", tourModel.getBookingFees());
			outPutMap.put("minimumFare", tourModel.getMinimumFare());
			outPutMap.put("discount", tourModel.getDiscount());

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
			}

			outPutMap.put("cancellationCharges", CancellationChargeModel.getAdminCancellationCharges().getCharge());

			outPutMap.put("tip", UserProfileModel.getAdminUserAccountDetailsById(tourModel.getPassengerId()).getTip());

			if (jobExpireTime < ProjectConstants.REQUEST_TIME_ONE) {

				outPutMap.put("jobExpireTime", ProjectConstants.REQUEST_TIME_ONE - jobExpireTime);

			} else {

				outPutMap.put("jobExpireTime", 0);
			}

			outPutMap.put("isRentalBooking", tourModel.isRentalBooking());

			Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

			if (tourModel.isRentalBooking()) {

				outPutMap.put("rentalPackageId", tourModel.getRentalPackageId());
				outPutMap.put("rentalPackageTime", tourModel.getRentalPackageTime());

				rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
				rentalPackageDetails.put("baseFare", df_new.format(tourModel.getInitialFare()));
				rentalPackageDetails.put("additionalPerKmFare", df_new.format(tourModel.getPerKmFare()));
				rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tourModel.getPerMinuteFare()));

				if (tourModel.getRentalPackageTime() == 1) {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

				} else {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
				}

				rentalPackageDetails.put("packageDistance", df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

				outPutMap.put("rentalPackageDetails", rentalPackageDetails);

			} else {

				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", rentalPackageDetails);
			}

			CourierUtils.formCourierData(tourModel, outPutMap);
			OrderUtils.formOrdersNGBData(null, outPutMap);
			outPutMap.put("tripType", ProjectConstants.TRIP_TYPE_TOUR_ID);

			return sendDataResponse(outPutMap);

		} else {

			List<String> orderStatus = OrderUtils.getOrderStatusForCurrentDriverOrders();
			OrderModel orderModel = OrderModel.getCurrentOrderByDriverId(loggedInuserId, orderStatus);

			if (orderModel != null) {

				CourierUtils.formCourierData(null, outPutMap);

				OrderUtils.formOrdersNGBData(orderModel, outPutMap);
				outPutMap.put("tripType", ProjectConstants.TRIP_TYPE_ORDER_ID);

				CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(orderModel.getCarTypeId());

				outPutMap.put("tourId", "");
				outPutMap.put("userTourId", "");
				outPutMap.put("passengerId", "");

				outPutMap.put("driverId", "");
				outPutMap.put("dateTime", "0");

				outPutMap.put("distance", 0);
				outPutMap.put("charges", 0);
				outPutMap.put("carTypeId", orderModel.getCarTypeId());
				outPutMap.put("paymentType", "");

				outPutMap.put("isAirportFixedFareApplied", false);
				outPutMap.put("airportBookingType", "");

				if (carTypeModel != null) {
					outPutMap.put("carType", carTypeModel.getCarType());
				} else {
					outPutMap.put("carType", "");
				}

				outPutMap.put("firstName", "-");
				outPutMap.put("lastName", "-");
				outPutMap.put("email", "-");
				outPutMap.put("phone", "-");
				outPutMap.put("phoneCode", "-");
				outPutMap.put("photoUrl", "-");
				outPutMap.put("rating", -1);

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
				outPutMap.put("cancellationCharges", 0);
				outPutMap.put("tip", 0);
				outPutMap.put("isRentalBooking", false);
				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", null);

				return sendDataResponse(outPutMap);

			} else {

				if (!user.getUserRole().equals(UserRoles.PASSENGER_ROLE)) {
					DriverTourStatusUtils.updateDriverTourStatus(loggedInuserId, ProjectConstants.DRIVER_FREE_STATUS);
				}

				return sendBussinessError(messageForKey("errorNoJobAvailableInvoice", request));
			}
		}
	}

	@Path("/{status}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOwnerTripList(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("status") String status, 
			@PathParam("start") int start, 
			@PathParam("length") int length, 
			@QueryParam("afterTime") long afterTime
			) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<TourModel> tourList = new ArrayList<TourModel>();

		UserModel user = UserModel.getUserActiveDeativeDetailsById(loggedInuserId);

		if (user.getUserRole().equals(UserRoles.PASSENGER_ROLE) || user.getUserRole().equals(UserRoles.BUSINESS_OWNER_ROLE) || user.getUserRole().equals(UserRoles.OPERATOR_ROLE)) {

			if (status.equalsIgnoreCase(ProjectConstants.CURRENT)) {
				status = "started,accepted,arrived & waiting,assigned,new,pending";
			} else if (status.equalsIgnoreCase(ProjectConstants.COMPLETED)) {
				status = "ended,expired,passenger unavailable";
			} else if (status.equalsIgnoreCase(ProjectConstants.CANCELLED)) {
				status = "cancelled by passenger,cancelled by driver";
			}

			Map<String, Object> inputMap = new HashMap<String, Object>();

			String[] statusArray = MyHubUtils.splitStringByCommaArray(status);

			inputMap.put("statusArray", statusArray);
			inputMap.put("userId", loggedInuserId);
			inputMap.put("afterTime", afterTime);
			inputMap.put("start", start);
			inputMap.put("length", length);

			tourList = TourModel.getToursByStatus(inputMap);
		}

		List<Map<String, Object>> outerOutPutMap = new ArrayList<Map<String, Object>>();

		for (TourModel tourModel : tourList) {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			CarModel car = CarModel.getCarDetailsByDriverId(tourModel.getDriverId());

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			outPutMap.put("tourId", tourModel.getTourId());
			outPutMap.put("userTourId", tourModel.getUserTourId());
			outPutMap.put("passengerId", tourModel.getPassengerId());

			outPutMap.put("driverId", tourModel.getDriverId());

			outPutMap.put("distance", tourModel.getDistance());
			outPutMap.put("charges", tourModel.getCharges());
			outPutMap.put("paymentType", tourModel.getPaymentType());

			outPutMap.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
			outPutMap.put("airportBookingType", tourModel.getAirportBookingType());

			if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

				outPutMap.put("isInvoice", true);

			} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				if (InvoiceModel.getInvoiceByTourId(tourModel.getTourId()) != null) {
					outPutMap.put("isInvoice", true);
				} else {
					outPutMap.put("isInvoice", false);
				}
			} else {
				outPutMap.put("isInvoice", false);
			}

			outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));

			if (!tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
				outPutMap.put("firstName", tourModel.getFirstName());
				outPutMap.put("lastName", tourModel.getLastName());
				outPutMap.put("email", tourModel.getEmail());
				outPutMap.put("phone", tourModel.getPhoneNo());
				outPutMap.put("phoneCode", tourModel.getPhoneNoCode());
				outPutMap.put("photoUrl", tourModel.getPhotoUrl());

			} else {
				outPutMap.put("firstName", "");
				outPutMap.put("lastName", "");
				outPutMap.put("email", "");
				outPutMap.put("phone", "");
				outPutMap.put("phoneCode", "");
				outPutMap.put("photoUrl", "");
			}

			outPutMap.put("pFirstName", tourModel.getpFirstName());
			outPutMap.put("pLastName", tourModel.getpLastName());
			outPutMap.put("pEmail", tourModel.getpEmail());
			outPutMap.put("pPhone", tourModel.getpPhone());
			outPutMap.put("pPhoneCode", tourModel.getpPhoneCode());

			if (car != null) {

				outPutMap.put("modelName", car.getModelName());
				outPutMap.put("carColor", car.getCarColor());
				outPutMap.put("carPlateNo", car.getCarPlateNo());
				outPutMap.put("carYear", car.getCarYear());
				outPutMap.put("noOfPassenger", car.getNoOfPassenger());
				outPutMap.put("owner", car.getOwner());
				outPutMap.put("make", car.getMake());

			} else {

				outPutMap.put("modelName", "-");
				outPutMap.put("carColor", "-");
				outPutMap.put("carPlateNo", "-");
				outPutMap.put("carYear", "-");
				outPutMap.put("noOfPassenger", "-");
				outPutMap.put("owner", "-");
				outPutMap.put("make", "-");
			}

			outPutMap.put("carTypeId", tourModel.getCarTypeId());
			outPutMap.put("sourceLatitude", tourModel.getsLatitude());
			outPutMap.put("sourceLongitude", tourModel.getsLongitude());
			outPutMap.put("destinationLatitude", tourModel.getdLatitude());
			outPutMap.put("destinationLongitude", tourModel.getdLongitude());

			outPutMap.put("initialFare", tourModel.getInitialFare());
			outPutMap.put("perKmFare", tourModel.getPerKmFare());
			outPutMap.put("perMinuteFare", tourModel.getPerMinuteFare());
			outPutMap.put("bookingFees", tourModel.getBookingFees());
			outPutMap.put("minimumFare", tourModel.getMinimumFare());
			outPutMap.put("discount", tourModel.getDiscount());

			outPutMap.put("promoCodeApplied", tourModel.isPromoCodeApplied());
			outPutMap.put("total", tourModel.getTotal());
			outPutMap.put("promoDiscount", tourModel.getPromoDiscount());
			outPutMap.put("usedCredits", tourModel.getUsedCredits());

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

			outPutMap.put("sourceAddress", tourModel.getSourceAddress());
			outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
			outPutMap.put("status", tourModel.getStatus());

			outPutMap.put("isRideLater", tourModel.isRideLater());
			outPutMap.put("isAcknowledged", tourModel.isAcknowledged());

			outPutMap.put("isRentalBooking", tourModel.isRentalBooking());

			Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

			if (tourModel.isRentalBooking()) {

				outPutMap.put("rentalPackageId", tourModel.getRentalPackageId());
				outPutMap.put("rentalPackageTime", tourModel.getRentalPackageTime());

				rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
				rentalPackageDetails.put("baseFare", df_new.format(tourModel.getInitialFare()));
				rentalPackageDetails.put("additionalPerKmFare", df_new.format(tourModel.getPerKmFare()));
				rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tourModel.getPerMinuteFare()));

				if (tourModel.getRentalPackageTime() == 1) {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

				} else {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
				}

				rentalPackageDetails.put("packageDistance", df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

				outPutMap.put("rentalPackageDetails", rentalPackageDetails);

			} else {

				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", rentalPackageDetails);
			}

			outerOutPutMap.add(outPutMap);
		}

		if (tourList.size() != 0) {
			afterTime = DateUtils.nowAsGmtMillisec();
			Map<Object, Object> res = new HashMap<Object, Object>();
			res.put("afterTime", afterTime);
			res.put("tourList", outerOutPutMap);
			return sendDataResponse(res);
		} else {
			return sendBussinessError(messageForKey("errorNoDataAvailableInvoice", request));
		}
	}

	@Path("/{tourId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTourDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@PathParam("tourId") String tourId
		) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		CarModel carModel = CarModel.getCarDetailsByDriverId(tourModel.getDriverId());
		DriverGeoLocationModel driverGeoLocationModel = DriverGeoLocationModel.getCurrentDriverPosition(tourModel.getDriverId());

		Map<String, Object> tourDetails = new HashMap<String, Object>();
		Map<String, Object> driverDetails = new HashMap<String, Object>();
		Map<String, Object> carDetails = new HashMap<String, Object>();

		tourDetails.put("tourId", tourModel.getTourId());
		tourDetails.put("userTourId", tourModel.getUserTourId());
		tourDetails.put("passengerId", tourModel.getPassengerId());
		tourDetails.put("driverId", tourModel.getDriverId());
		tourDetails.put("distance", tourModel.getDistance());
		tourDetails.put("charges", tourModel.getCharges());
		tourDetails.put("paymentType", tourModel.getPaymentType());
		tourDetails.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
		tourDetails.put("airportBookingType", tourModel.getAirportBookingType());
		tourDetails.put("dateTime", String.valueOf(tourModel.getCreatedAt()));
		tourDetails.put("pFirstName", tourModel.getpFirstName());
		tourDetails.put("pLastName", tourModel.getpLastName());
		tourDetails.put("pEmail", tourModel.getpEmail());
		tourDetails.put("pPhone", tourModel.getpPhone());
		tourDetails.put("pPhoneCode", tourModel.getpPhoneCode());
		tourDetails.put("carTypeId", tourModel.getCarTypeId());
		tourDetails.put("sourceLatitude", tourModel.getsLatitude());
		tourDetails.put("sourceLongitude", tourModel.getsLongitude());
		tourDetails.put("destinationLatitude", tourModel.getdLatitude());
		tourDetails.put("destinationLongitude", tourModel.getdLongitude());
		tourDetails.put("initialFare", tourModel.getInitialFare());
		tourDetails.put("perKmFare", tourModel.getPerKmFare());
		tourDetails.put("perMinuteFare", tourModel.getPerMinuteFare());
		tourDetails.put("bookingFees", tourModel.getBookingFees());
		tourDetails.put("minimumFare", tourModel.getMinimumFare());
		tourDetails.put("discount", tourModel.getDiscount());
		tourDetails.put("promoCodeApplied", tourModel.isPromoCodeApplied());
		tourDetails.put("total", tourModel.getTotal());
		tourDetails.put("promoDiscount", tourModel.getPromoDiscount());
		tourDetails.put("usedCredits", tourModel.getUsedCredits());
		tourDetails.put("sourceAddress", tourModel.getSourceAddress());
		tourDetails.put("destinationAddress", tourModel.getDestinationAddress());
		tourDetails.put("status", tourModel.getStatus());
		tourDetails.put("isRideLater", tourModel.isRideLater());
		tourDetails.put("isAcknowledged", tourModel.isAcknowledged());
		tourDetails.put("isRentalBooking", tourModel.isRentalBooking());

		if (tourModel.isPromoCodeApplied()) {

			PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

			if (promoCodeModel != null) {
				tourDetails.put("promoCodeId", promoCodeModel.getPromoCodeId());
				tourDetails.put("promoCode", promoCodeModel.getPromoCode());
				tourDetails.put("usage", promoCodeModel.getUsage());
				tourDetails.put("usageCount", promoCodeModel.getUsageCount());
				tourDetails.put("mode", promoCodeModel.getMode());
				tourDetails.put("promoCodeDiscount", promoCodeModel.getDiscount());
				tourDetails.put("startDate", promoCodeModel.getStartDate());
				tourDetails.put("usedCount", promoCodeModel.getUsedCount());
				tourDetails.put("endDate", promoCodeModel.getEndDate());
			} else {
				tourDetails.put("promoCodeId", "");
				tourDetails.put("promoCode", "");
				tourDetails.put("usage", "");
				tourDetails.put("usageCount", "");
				tourDetails.put("mode", "");
				tourDetails.put("promoCodeDiscount", "");
				tourDetails.put("startDate", "");
				tourDetails.put("usedCount", "");
				tourDetails.put("endDate", "");
			}
		} else {

			tourDetails.put("promoCodeId", "");
			tourDetails.put("promoCode", "");
			tourDetails.put("usage", "");
			tourDetails.put("usageCount", "");
			tourDetails.put("mode", "");
			tourDetails.put("promoCodeDiscount", "");
			tourDetails.put("startDate", "");
			tourDetails.put("usedCount", "");
			tourDetails.put("endDate", "");
		}

		Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

		if (tourModel.isRentalBooking()) {

			tourDetails.put("rentalPackageId", tourModel.getRentalPackageId());
			tourDetails.put("rentalPackageTime", tourModel.getRentalPackageTime());

			rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
			rentalPackageDetails.put("baseFare", df_new.format(tourModel.getInitialFare()));
			rentalPackageDetails.put("additionalPerKmFare", df_new.format(tourModel.getPerKmFare()));
			rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tourModel.getPerMinuteFare()));

			if (tourModel.getRentalPackageTime() == 1) {

				rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

			} else {

				rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
			}

			rentalPackageDetails.put("packageDistance", df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

			tourDetails.put("rentalPackageDetails", rentalPackageDetails);

		} else {

			tourDetails.put("rentalPackageId", "-1");
			tourDetails.put("rentalPackageTime", 0);
			tourDetails.put("rentalPackageDetails", rentalPackageDetails);
		}

		if (carModel != null) {
			carDetails.put("modelName", carModel.getModelName());
			carDetails.put("carColor", carModel.getCarColor());
			carDetails.put("carPlateNo", carModel.getCarPlateNo());
			carDetails.put("carYear", carModel.getCarYear());
			carDetails.put("noOfPassenger", carModel.getNoOfPassenger());
			carDetails.put("owner", carModel.getOwner());
			carDetails.put("make", carModel.getMake());
			carDetails.put("carTypeIconImage", carModel.getCarTypeIconImage());
		} else {
			carDetails.put("modelName", "");
			carDetails.put("carColor", "");
			carDetails.put("carPlateNo", "");
			carDetails.put("carYear", "");
			carDetails.put("noOfPassenger", "");
			carDetails.put("owner", "");
			carDetails.put("make", "");
			carDetails.put("carTypeIconImage", "");
		}

		if (tourModel.getDriverId() != null && !tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID) && driverGeoLocationModel != null) {
			driverDetails.put("firstName", tourModel.getFirstName());
			driverDetails.put("lastName", tourModel.getLastName());
			driverDetails.put("email", tourModel.getEmail());
			driverDetails.put("phone", tourModel.getPhoneNo());
			driverDetails.put("phoneCode", tourModel.getPhoneNoCode());
			driverDetails.put("photoUrl", tourModel.getPhotoUrl());
			driverDetails.put(FieldConstants.C_LAT, driverGeoLocationModel.getLatitude());
			driverDetails.put(FieldConstants.C_LONG, driverGeoLocationModel.getLongitude());

		} else {
			driverDetails.put("firstName", "");
			driverDetails.put("lastName", "");
			driverDetails.put("email", "");
			driverDetails.put("phone", "");
			driverDetails.put("phoneCode", "");
			driverDetails.put("photoUrl", "");
			driverDetails.put(FieldConstants.C_LAT, "");
			driverDetails.put(FieldConstants.C_LONG, "");
		}

		Map<String, Object> outerOutPutMap = new HashMap<>();
		outerOutPutMap.put("tourDetails", tourDetails);
		outerOutPutMap.put("driverDetails", driverDetails);
		outerOutPutMap.put("carDetails", carDetails);
		return sendDataResponse(outerOutPutMap);
	}
}