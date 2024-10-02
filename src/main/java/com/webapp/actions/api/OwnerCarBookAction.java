package com.webapp.actions.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.UUIDGenerator;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.PromoCodeUtils;
import com.utils.myhub.TourUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarModel;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.PassengerTripRatingsModel;
import com.webapp.models.ProcessBookingRequest;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UserPromoCodeModel;
import com.webapp.models.UtilizedUserPromoCodeModel;
import com.webapp.models.WebSocketClient;

@Path("/api/owner")
public class OwnerCarBookAction extends BusinessApiAction {

	private static final String SOURCE_LATITUDE = "sLatitude";
	private static final String SOURCE_LATITUDE_LABEL = "Source Latitude";

	private static final String DESTINATION_LATITUDE = "dLatitude";
	private static final String DESTINATION_LATITUDE_LABEL = "Destination Latitude";

	private static final String SOURCE_LONGITUDE = "sLongitude";
	private static final String SOURCE_LONGITUDE_LABEL = "Source Longitude";

	private static final String DESTINATION_LONGITUDE = "dLongitude";
	private static final String DESTINATION_LONGITUDE_LABEL = "Destination Longitude";

	private static final String CAR_TYPE_ID = "carTypeId";
	private static final String CAR_TYPE_ID_LABEL = "Car Type Id";

	private static final String SOURCE_ADDRESS = "sourceAddress";
	private static final String SOURCE_ADDRESS_LABEL = "Source Address";

	private static final String DESTINATION_ADDRESS = "destinationAddress";
	private static final String DESTINATION_ADDRESS_LABEL = "Destination Address ";

	private static final String DISTANCE = "distance";
	private static final String DISTANCE_LABEL = "Distance";

	private static final String CHARGES = "charges";
	private static final String CHARGES_LABEL = "Charges";

	@Path("/carbook")
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response carBook(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			TourModel tourModel
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = CarBookModelValidtion(tourModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		boolean promoCodeValid = false;

		if (tourModel.isPromoCodeApplied()) {

			PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

			long currentTime = DateUtils.nowAsGmtMillisec();

			if (promoCode != null) {

				if (currentTime >= promoCode.getStartDate() && currentTime <= promoCode.getEndDate()) {

					UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = UtilizedUserPromoCodeModel.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInuserId, promoCode.getPromoCodeId());

					if (utilizedUserPromoCodeModel != null) {
						return sendBussinessError(messageForKey("errorUsedPromoCode", request));
					}

					if (promoCode.getUsageType().equalsIgnoreCase(ProjectConstants.ALL_ID)) {

						if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {
							promoCodeValid = true;
						} else {

							if (promoCode.getUsedCount() < promoCode.getUsageCount()) {
								promoCodeValid = true;
							} else {
								return sendBussinessError(messageForKey("errorPromoCodeExpired", request));
							}
						}

					} else {

						UserPromoCodeModel userPromoCodeModel = UserPromoCodeModel.getUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInuserId, promoCode.getPromoCodeId());

						if (userPromoCodeModel != null) {
							promoCodeValid = true;
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

			// ---------update promo code count--------------------------------------

			long newPromoCodeCount = promoCode.getUsedCount() + 1;

			if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

				promoCodeValid = true;

				PromoCodeModel updatePromoCodeModel = new PromoCodeModel();

				updatePromoCodeModel.setPromoCodeId(promoCode.getPromoCodeId());
				updatePromoCodeModel.setUsedCount(newPromoCodeCount);

				updatePromoCodeModel.updatePromoCodeCount();

			} else {

				if ((promoCode.getUsedCount() < promoCode.getUsageCount()) && (newPromoCodeCount <= promoCode.getUsageCount())) {

					promoCodeValid = true;

					PromoCodeModel updatePromoCodeModel = new PromoCodeModel();

					updatePromoCodeModel.setPromoCodeId(promoCode.getPromoCodeId());
					updatePromoCodeModel.setUsedCount(newPromoCodeCount);

					updatePromoCodeModel.updatePromoCodeCount();

				} else {

					return sendBussinessError(messageForKey("errorPromoCodeExpired", request) + "3");
				}
			}

			// ----------------------------------------------------------------------

			if (promoCodeValid) {

				// UserModel userInfo = UserModel.getUserAccountDetailsById(loggedInuserId);

				tourModel.setPassengerId(loggedInuserId);
				tourModel.setTourId(UUIDGenerator.generateUUID());
				tourModel.setDriverId("-1");
				tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
				tourModel.setBookingType(ProjectConstants.BUSINESS_OWNER_BOOKING);
				tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");
				tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getdLongitude() + "  " + tourModel.getdLatitude() + ")')");

				lang = ProjectConstants.ENGLISH_ID;

				tourModel.setLanguage(lang);

				String tourId = tourModel.createTour(loggedInuserId);

				if (tourId != null) {

					TourTimeModel tourTimeModel = new TourTimeModel();
					tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
					tourTimeModel.setTourId(tourId);
					tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
					tourTimeModel.createTourTime();

					AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
					if (adminSettings.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
						new ProcessBookingRequest(tourId, loggedInuserId);
					}

					Map<String, Object> output = new HashMap<String, Object>();

					output.put("tourId", tourId);
					output.put("type", "SUCCESS");
					output.put("messages", messageForKey("successRequestSent", request));

					return sendDataResponse(output);

				} else {
					return sendBussinessError(messageForKey("errorFailedSendRequest", request));
				}
			} else {
				return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
			}

		} else {

			tourModel.setPassengerId(loggedInuserId);
			tourModel.setTourId(UUIDGenerator.generateUUID());
			tourModel.setDriverId("-1");
			tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
			tourModel.setBookingType(ProjectConstants.BUSINESS_OWNER_BOOKING);
			tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");
			tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getdLongitude() + "  " + tourModel.getdLatitude() + ")')");

			tourModel.setPromoCodeId(null);
			tourModel.setPromoCodeApplied(false);

			lang = ProjectConstants.ENGLISH_ID;

			tourModel.setLanguage(lang);

			String tourId = tourModel.createTour(loggedInuserId);

			if (tourId != null) {

				TourTimeModel tourTimeModel = new TourTimeModel();
				tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
				tourTimeModel.setTourId(tourId);
				tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
				tourTimeModel.createTourTime();

				AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
				if (adminSettings.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
					new ProcessBookingRequest(tourId, loggedInuserId);
				}

				Map<String, Object> output = new HashMap<String, Object>();

				output.put("tourId", tourId);
				output.put("type", "SUCCESS");
				output.put("messages", messageForKey("successRequestSent", request));

				return sendDataResponse(output);

			} else {
				return sendBussinessError(messageForKey("errorFailedSendRequest", request));
			}
		}
	}

	@Path("/cancel/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response cancelTripRequest(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("tourId") String tourId
			) throws SQLException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);
		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

		FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();
		TourTimeModel tourTimeModel = TourTimeModel.getTourTimesDetailsByTourId(tourId);

		double currentTime = DateUtils.nowAsGmtMillisec();
		double timeDiff = currentTime - tourTimeModel.getAcceptTime();

		boolean directCancelTripWithoutPayment = false;

		if (timeDiff <= (freeWaitingTimeModel.getCancelTime() * (60 * 1000))) {
			directCancelTripWithoutPayment = true;
		}

		//@formatter:off
		if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.STARTED_TOUR) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
		//@formatter:on

			PromoCodeUtils.promoCodeCancel(tourDetails, false);

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				DriverTourStatusUtils.updateDriverTourStatus(tour.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

				TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR, loggedInuserId);
			}

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

				InvoiceModel invoice = new InvoiceModel();

				invoice.setTourId(tourId);
				invoice.setInitialFare(tourDetails.getInitialFare());
				invoice.setPerKmFare(tourDetails.getPerKmFare());
				invoice.setPerMinuteFare(tourDetails.getPerMinuteFare());
				invoice.setBookingFees(tourDetails.getBookingFees());
				invoice.setDiscount(tourDetails.getDiscount());
				invoice.setMinimumFare(tourDetails.getMinimumFare());

				invoice.setDistance(tour.getDistance());
				invoice.setPromoDiscount(0);
				invoice.setPromoCodeApplied(false);
				invoice.setPromoCodeId(null);
				invoice.setUsedCredits(0);

				if (directCancelTripWithoutPayment) {

					invoice.setPaymentMode(ProjectConstants.CARD);
					invoice.setTotal(0);
					invoice.setFine(0);
					invoice.setCharges(0);
				} else {

					invoice.setTotal(CancellationChargeModel.getAdminCancellationCharges().getCharge());
					invoice.setFine(CancellationChargeModel.getAdminCancellationCharges().getCharge());
					invoice.setCharges(CancellationChargeModel.getAdminCancellationCharges().getCharge());

					//@formatter:off					
					if ((tour.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
						&& tour.getCardOwner() != null && tour.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
							|| (tour.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
							&& tour.getCardOwner() != null && tour.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
					//@formatter:on

					} else {

						// payment mode

						UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(tourDetails.getPassengerId());
						userProfile.setCredit(userProfile.getCredit() - invoice.getFine());
						userProfile.updateUserCredits();

						invoice.setPaymentMode(ProjectConstants.CASH);
						invoice.setCard(false);
						invoice.setCashCollected(0);
						invoice.setCashToBeCollected(0);
						invoice.setCashNotReceived(true);
					}
				}

				invoice.generateInvoice(loggedInuserId, false);
			}

			tour = new TourModel();
			tour.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			tour.setUpdatedBy(loggedInuserId);
			tour.setTourId(tourId);
			tour.setStatus(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
			int status = tour.updateTourStatusByPassenger(loggedInuserId);

			if (status > 0) {

				tour = TourModel.getTourDetailsByTourId(tourId);

				if (!tour.getDriverId().equals("-1")) {

					sendDriverNotification(tour.getDriverId(), messageForKey("errorPassengerCancelledTrip", request));

					DriverTourStatusUtils.updateDriverTourStatus(tour.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);
				}

				AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

				UserModel user = UserModel.getUserAccountDetailsById(tour.getDriverId());

				String message = BusinessAction.messageForKeyAdmin("errorPassengerCancelledTrip", adminSmsSendingModel.getLanguage());

				if (adminSmsSendingModel.isdCancelledByPassengerBusinessO()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, user.getPhoneNoCode() + user.getPhoneNo().trim(), ProjectConstants.SMSConstants.SMS_PASSENGER_CANCELLED_TEMPLATE_ID);
				}

				return sendSuccessMessage(messageForKey("successTripCancelled", request));

			} else {

				return sendBussinessError(messageForKey("errorTripCancelled", request));
			}
		} else {
			return sendSuccessMessage(messageForKey("errorTripCantBeCancelled", request));
		}
	}

	@Path("/expire/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response expireTripRequest(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("tourId") String tourId
			) throws SQLException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		if (tourModel != null) {

			//@formatter:off
			if (tourModel.getStatus().equals(ProjectConstants.PENDING_REQUEST) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
			//@formatter:on

				long currentTime = DateUtils.nowAsGmtMillisec();
				long oldTime = tourModel.getCreatedAt();
				long diffTime = ProjectConstants.REQUEST_TIME * 1000 * adminSettings.getNoOfCars();

				if ((currentTime - oldTime) > diffTime) {

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, loggedInuserId);

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					outputMap.put("statusCode", 201);
					outputMap.put("message", messageForKey("successTripExpiredSuccessfully", request));

					return sendDataResponse(outputMap);
				}

			} else {

				outputMap.put("statusCode", 200);
				outputMap.put("message", messageForKey("successTripEnded", request));

				return sendDataResponse(outputMap);
			}
		}

		outputMap.put("statusCode", 400);
		outputMap.put("message", messageForKey("errorTripCantBeCancelled", request));

		return sendDataResponse(outputMap);
	}

	@Path("/booking-status/{tourId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBookingSttaus(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@PathParam("tourId") String tourId
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		HashMap<String, Object> outputMap = new HashMap<String, Object>();

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);

		if (tour != null) {

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				outputMap.put("status", "inprogress");

			} else if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

				CarModel car = CarModel.getCarDetailsByDriverId(tour.getDriverId());

				if (car != null) {

					outputMap.put("modelName", car.getModelName());
					outputMap.put("carColor", car.getCarColor());
					outputMap.put("carPlateNo", car.getCarPlateNo());
					outputMap.put("carYear", car.getCarYear());
					outputMap.put("noOfPassenger", car.getNoOfPassenger());
					outputMap.put("carTypeId", car.getCarTypeId());
					outputMap.put("owner", car.getOwner());

				} else {

					outputMap.put("modelName", "");
					outputMap.put("carColor", "");
					outputMap.put("carPlateNo", "");
					outputMap.put("carYear", "");
					outputMap.put("noOfPassenger", "");
					outputMap.put("carTypeId", "");
					outputMap.put("owner", "");
				}

				if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
					outputMap.put("status", ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
				} else {
					outputMap.put("status", ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);
				}

				UserModel user = UserModel.getUserAccountDetailsById(tour.getDriverId());

				outputMap.put("fullName", tour.getFirstName() + " " + tour.getLastName());
				outputMap.put("photoUrl", tour.getPhotoUrl());
				outputMap.put("phoneNo", tour.getPhoneNo());
				outputMap.put("phoneNoCode", tour.getPhoneNoCode());
				outputMap.put("tourId", tour.getTourId());
				outputMap.put("driverId", tour.getDriverId());
				outputMap.put("driverPhoneNumber", user.getPhoneNo());
				outputMap.put("driverPhoneNumberCode", user.getPhoneNoCode());

				outputMap.put("initialFare", tour.getInitialFare());
				outputMap.put("perKmFare", tour.getPerKmFare());
				outputMap.put("perMinuteFare", tour.getPerMinuteFare());
				outputMap.put("bookingFees", tour.getBookingFees());
				outputMap.put("minimumFare", tour.getMinimumFare());
				outputMap.put("discount", tour.getDiscount());

				outputMap.put("promoCodeApplied", tour.isPromoCodeApplied());
				outputMap.put("total", tour.getTotal());
				outputMap.put("promoDiscount", tour.getPromoDiscount());
				outputMap.put("usedCredits", tour.getUsedCredits());

				if (tour.isPromoCodeApplied()) {

					PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tour.getPromoCodeId());

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

				List<PassengerTripRatingsModel> ratingList = PassengerTripRatingsModel.getAlldriverRatings(tour.getDriverId());

				double rating = 0.0;

				for (PassengerTripRatingsModel paRating : ratingList) {
					rating = rating + paRating.getRate();
				}

				DecimalFormat df = new DecimalFormat("#.#");

				if (rating != 0.0) {

					rating = (double) rating / ratingList.size();

					outputMap.put("rating", df.format(rating));

				} else {

					outputMap.put("rating", -1);
				}

			} else if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				outputMap.put("status", ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE);

			} else {

				outputMap.put("status", ProjectConstants.PENDING_REQUEST);
			}

			return sendDataResponse(outputMap);

		} else {

			return sendBussinessError(messageForKey("errorTripNotFound", request));
		}
	}

	private void sendDriverNotification(String driverId, String message) {

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);
		String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.CGB_SOCKET;
		WebSocketClient.sendDriverNotification(messge, driverId, apnsDevice.getApiSessionKey());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(driverId);
		apnsMessage.insertPushMessage();
	}

	private List<String> CarBookModelValidtion(TourModel tourModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(SOURCE_LATITUDE, SOURCE_LATITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(SOURCE_LONGITUDE, SOURCE_LONGITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DESTINATION_LATITUDE, DESTINATION_LATITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DESTINATION_LONGITUDE, DESTINATION_LONGITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CAR_TYPE_ID, CAR_TYPE_ID_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(SOURCE_ADDRESS, SOURCE_ADDRESS_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DESTINATION_ADDRESS, DESTINATION_ADDRESS_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DISTANCE, DISTANCE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CHARGES, CHARGES_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(tourModel);

		return errorMessages;
	}

}