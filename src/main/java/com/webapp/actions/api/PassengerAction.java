package com.webapp.actions.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
import com.utils.CommonUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.PromoCodeUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CarModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.EstimateFareModel;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.PassengerGeoLocationModel;
import com.webapp.models.PassengerTripRatingsModel;
import com.webapp.models.ProcessBookingRequest;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.RideLaterSettingsModel;
import com.webapp.models.SurgePriceModel;
import com.webapp.models.TaxModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTaxModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UserPromoCodeModel;
import com.webapp.models.UtilizedUserPromoCodeModel;
import com.webapp.models.WebSocketClient;

@Path("/api/passenger")
public class PassengerAction extends BusinessApiAction {

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

	private static final String TRANSMISSION_TYPE_ID = "transmissionTypeId";
	private static final String TRANSMISSION_TYPE_LABEL = "Transmission Type";

	private static final String IS_AIRPORT_FIXED_FARE_APPLIED = "airportFixedFareApplied";
	private static final String IS_AIRPORT_FIXED_FARE_APPLIED_LABEL = "Airport Fixed Fare Flag";

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

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = CarBookModelValidtion(tourModel);

		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		long beforePickupTime = DateUtils.nowAsGmtMillisec() - (rideLaterSettingsModel.getPassengerTourBeforeTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);
		long afterPickupTime = DateUtils.nowAsGmtMillisec() + (rideLaterSettingsModel.getPassengerTourAfterTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);

		int count = TourModel.getRideLaterPassengerDetailsBetweenTimeSlot(loggedInuserId, beforePickupTime, afterPickupTime);

		if (count > 0) {
			return sendBussinessError(messageForKey("errorPassengerFutureRideLaterRequestExist", request));
		}

		TourModel currentTour = TourModel.getCurrentTourByPassangerId(loggedInuserId);

		if (currentTour != null) {

			return sendBussinessError(messageForKey("errorPreviousBooking", request));

		} else {

			if (!tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.WALLET_ID) && !tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.CASH_ID) && !tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.CARD_ID)) {

				return sendBussinessError(messageForKey("errorInvlidPaymentType", request));
			}

			if (tourModel.getMulticityCityRegionId() == null || "".equals(tourModel.getMulticityCityRegionId())) {

				return sendBussinessError(messageForKey("errorMulticityCityRegionIdRequired", request));
			}

			if (tourModel.getMulticityCountryId() == null || "".equals(tourModel.getMulticityCountryId())) {

				return sendBussinessError(messageForKey("errorMulticityCountryIdRequired", request));
			}

			// ----------Check valid package for rental ----------------
			if (tourModel.isRentalBooking()) {

				RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(tourModel.getRentalPackageId(), tourModel.getCarTypeId());
				if (rentalPackageFareModel == null) {
					return sendBussinessError(messageForKey("errorInvalidRentalPackage", request));
				}

				// TODO: rental api check for rental car type available for vendor
				if (!MultiTenantUtils.validateVendorCarTypeRental(tourModel.getCarTypeId(), headerVendorId, tourModel.getMulticityCityRegionId(), tourModel.getRentalPackageId())) {
					return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegionForRental", request));
				}
			}
			// ---------------------------------------------------------

			// ------- Validate if previous card payment is pending
			// ---------------------------------------------
			InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(loggedInuserId);

			if (invoiceModel != null) {

				if (invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

					Map<String, Object> outputMap = new HashMap<String, Object>();

					outputMap.put("statusCode", 202);
					outputMap.put("type", "ERROR-BUSSINESS");
					outputMap.put("messages", messageForKey("errorPendingPaymentOfPreviousBooking", request));

					return sendDataResponse(outputMap);
				}
			}
			// --------------------------------------------------------------------------------------------------

			// Check car is available
			// ------------------------------------------------------------------

			Map<String, Object> ac = TourUtils.checkBookingForAirportPickupOrDrop(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());

			if (((boolean) ac.get("isAirportPickUp") || (boolean) ac.get("isAirportDrop"))) {
				tourModel.setAirportBooking(true);
			}

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(tourModel.getMulticityCityRegionId());

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			boolean isRadiusSurgeApplied = false;

			if (multicityCityRegionModel == null) {

				return sendBussinessError(messageForKey("errorNearByCarNotAvailable", request));

			} else {

				String maxRadius = adminSettingsModel.getRadiusString();

				if (!tourModel.isAirportBooking()) {

					if (tourModel.isSurgePriceApplied()) {

						SurgePriceModel surgePriceModel = SurgePriceModel.getSurgePriceDetailsById(tourModel.getSurgePriceId());

						if (surgePriceModel != null) {

							EstimateFareModel estimateFareModel = new EstimateFareModel();
							estimateFareModel.setsLatitude(tourModel.getsLatitude());
							estimateFareModel.setsLongitude(tourModel.getsLongitude());
							estimateFareModel.setCarTypeId(tourModel.getCarTypeId());

							CitySurgeModel applicableCitySurgeModel1 = TourUtils.getApplicableRadiusSurge(estimateFareModel, tourModel.getMulticityCityRegionId(), adminSettingsModel);

							if (applicableCitySurgeModel1 != null && applicableCitySurgeModel1.getSurgeRate() < tourModel.getSurgePrice()) {
								tourModel.setSurgeRadius(applicableCitySurgeModel1.getRadius());
								maxRadius = String.valueOf(applicableCitySurgeModel1.getRadius() * adminSettingsModel.getDistanceUnits());
							}
						}

						CitySurgeModel applicableCitySurgeModel = CitySurgeModel.getActiveDeactiveDeletedNonDeletedCitySurgeByCitySurgeId(tourModel.getSurgePriceId());

						if (applicableCitySurgeModel != null) {
							maxRadius = String.valueOf(applicableCitySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits());
							isRadiusSurgeApplied = true;
							tourModel.setSurgeRadius(applicableCitySurgeModel.getRadius());
							tourModel.setSurgeType(ProjectConstants.SURGE_TYPE_RADIUS);
						}
					}
				}

//				String constant = "\"WGS 84\"";
//				String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + tourModel.getsLongitude() + " " + tourModel.getsLatitude() + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
				String distance = GeoLocationUtil.getDistanceQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION);
				String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + " " + tourModel.getsLatitude() + ")'),  " + maxRadius + ")";

				AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

				long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

				DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

				double minimumWalletAmount = 0.0;

				if (driverWalletSettingsModel != null) {
					minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
				}

				//@formatter:off
				//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
				List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(
							latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), Arrays.asList(tourModel.getCarTypeId()), 
							tourModel.getTransmissionTypeId(), timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, 
							headerVendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
				//@formatter:on

				if (carLocation.size() <= 0 || tourModel.getCarTypeId() == null) {

					return sendBussinessError(messageForKey("errorNearByCarNotAvailable", request));
				}
			}

			// ----------------------------------------------------------------------------------------

			// Check valid surge applied
			// -------------------------------------------------------------------

			if (!isRadiusSurgeApplied) {
				boolean isSurgePriceApplied = false;
				String surgePriceId = "-1";
				double surgePrice = 1;

				if (tourModel.isRentalBooking() || tourModel.isAirportBooking()) {

					isSurgePriceApplied = false;
					surgePriceId = "-1";
					surgePrice = 1;

				} else {

					String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;

					ApnsDeviceModel userDeviceDetails = ApnsDeviceModel.getDeviseByUserId(loggedInuserId);

					if (userDeviceDetails != null) {
						timeZone = userDeviceDetails.getTimezone();
					}

					Calendar calender = Calendar.getInstance();
					calender.setTimeZone(TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE));

					int currentHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
					int currentMinute = calender.get(Calendar.MINUTE);

					long requestTimeInMilli = DateUtils.getTimeInMillisForSurge(currentHourOfDay, currentMinute, false, timeZone);

					SurgePriceModel surgePriceModel = null;
					if (!tourModel.isAirportBooking()) {
						surgePriceModel = SurgePriceModel.getSurgePriceDetailsByRequestTimeAndRegionId(requestTimeInMilli, tourModel.getMulticityCityRegionId());
					}
					if (surgePriceModel != null) {

						if (tourModel.isSurgePriceApplied()) {

							if (surgePriceModel.getSurgePrice() > tourModel.getSurgePrice()) {

								isSurgePriceApplied = true;
								surgePriceId = surgePriceModel.getSurgePriceId();
								surgePrice = surgePriceModel.getSurgePrice();
							}

						} else {

							isSurgePriceApplied = true;
							surgePriceId = surgePriceModel.getSurgePriceId();
							surgePrice = surgePriceModel.getSurgePrice();
						}

					}

					if (isSurgePriceApplied) {

						double estimateFareWithoutDiscount = 0.0;
						double estimateFareWithDiscount = 0.0;
						double distanceInMeters = 0.0;

						if (((tourModel.getdLatitude() != null) && (!"".equals(tourModel.getdLatitude()))) && ((tourModel.getdLongitude() != null) && (!"".equals(tourModel.getdLongitude())))) {

							Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());

							distanceInMeters = distanceMatrix.get("distanceInMeters");
							double durationInMin = distanceMatrix.get("durationInMin");

							CarFareModel carFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(tourModel.getCarTypeId(), tourModel.getMulticityCityRegionId(), tourModel.getMulticityCountryId(), headerVendorId,
										ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

							if (carFareModel != null) {

								double distanceInKm = (distanceInMeters - carFareModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits();

								double baseFare = carFareModel.getInitialFare(); // + carFareModel.getBookingFees();
								double distanceFare = (distanceInKm > 0 ? distanceInKm : 0) * carFareModel.getPerKmFare();
								double timeFare = durationInMin * carFareModel.getPerMinuteFare();

								estimateFareWithoutDiscount = baseFare + distanceFare + timeFare;

							}
						}

						if (tourModel.isPromoCodeApplied()) {

							estimateFareWithDiscount = estimateFareWithoutDiscount - tourModel.getPromoDiscount();

							if (estimateFareWithDiscount <= 0) {
								estimateFareWithDiscount = 0;
							}

						} else {

							estimateFareWithDiscount = estimateFareWithoutDiscount;
						}

						Map<String, Object> output = new HashMap<String, Object>();

						output.put("type", "SUCCESS");
						output.put("messages", surgePrice + "x " + messageForKey("surgeApplicableMessage", request));
						output.put("tourId", "");
						output.put("isSurgePriceApplied", isSurgePriceApplied);
						output.put("surgePriceId", surgePriceId);
						output.put("surgePrice", surgePrice);
						output.put("estimateFareWithoutDiscount", estimateFareWithoutDiscount);
						output.put("estimateFareWithDiscount", estimateFareWithDiscount);

						return sendDataResponse(output);
					}
				}
			}
			// ----------------------------------------------------------------------------------------------------------

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

					UserModel userInfo = UserModel.getUserAccountDetailsById(loggedInuserId);

					tourModel.setPassengerId(loggedInuserId);
					tourModel.setTourId(UUIDGenerator.generateUUID());
					tourModel.setDriverId("-1");
					tourModel.setpFirstName(userInfo.getFirstName());
					tourModel.setpLastName(userInfo.getLastName());
					tourModel.setpEmail(userInfo.getEmail());
					tourModel.setpPhone(userInfo.getPhoneNo());
					tourModel.setpPhoneCode(userInfo.getPhoneNoCode());
					tourModel.setpPhotoUrl(userInfo.getPhotoUrl());
					tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
					tourModel.setBookingType(ProjectConstants.INDIVIDUAL_BOOKING);
					tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");

					if (tourModel.isRentalBooking()) {

						// In rental booking app side not sending destination and we can't keep
						// geo-location blank
						tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");

					} else {

						tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getdLongitude() + "  " + tourModel.getdLatitude() + ")')");
					}

					lang = ProjectConstants.ENGLISH_ID;

					tourModel.setLanguage(lang);

					tourModel.setAcknowledged(true);

					if (!MultiTenantUtils.validateVendorCarType(tourModel.getCarTypeId(), headerVendorId, tourModel.getMulticityCityRegionId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
						return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
					}

					String tourId = tourModel.createTourV2(loggedInuserId);

					if (tourId != null) {

						TourTimeModel tourTimeModel = new TourTimeModel();
						tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
						tourTimeModel.setTourId(tourId);
						tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
						tourTimeModel.createTourTime();

						if (adminSettingsModel.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
							new ProcessBookingRequest(tourId, loggedInuserId);
						}

						Map<String, Object> output = new HashMap<String, Object>();

						output.put("tourId", tourId);
						output.put("type", "SUCCESS");
						output.put("messages", messageForKey("successRequestSent", request));

						output.put("isSurgePriceApplied", false);
						output.put("surgePriceId", "-1");
						output.put("surgePrice", 1);
						output.put("estimateFareWithoutDiscount", 0);
						output.put("estimateFareWithDiscount", 0);
						output.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
						output.put("airportBookingType", tourModel.getAirportBookingType());

						return sendDataResponse(output);

					} else {
						return sendBussinessError(messageForKey("errorFailedSendRequest", request));
					}

				} else {

					return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
				}

			} else {

				UserModel userInfo = UserModel.getUserAccountDetailsById(loggedInuserId);

				tourModel.setPassengerId(loggedInuserId);
				tourModel.setTourId(UUIDGenerator.generateUUID());
				tourModel.setDriverId("-1");
				tourModel.setpFirstName(userInfo.getFirstName());
				tourModel.setpLastName(userInfo.getLastName());
				tourModel.setpEmail(userInfo.getEmail());
				tourModel.setpPhone(userInfo.getPhoneNo());
				tourModel.setpPhoneCode(userInfo.getPhoneNoCode());
				tourModel.setpPhotoUrl(userInfo.getPhotoUrl());
				tourModel.setStatus(ProjectConstants.TourStatusConstants.NEW_TOUR);
				tourModel.setBookingType(ProjectConstants.INDIVIDUAL_BOOKING);
				tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");

				if (tourModel.isRentalBooking()) {
					tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");
				} else {
					tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getdLongitude() + "  " + tourModel.getdLatitude() + ")')");
				}

				tourModel.setPromoCodeId(null);
				tourModel.setPromoCodeApplied(false);

				lang = ProjectConstants.ENGLISH_ID;

				tourModel.setLanguage(lang);

				tourModel.setAcknowledged(true);

				tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_PASSENGER);

				if (!MultiTenantUtils.validateVendorCarType(tourModel.getCarTypeId(), headerVendorId, tourModel.getMulticityCityRegionId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
					return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
				}

				String tourId = tourModel.createTourV2(loggedInuserId);

				if (tourId != null) {

					TourTimeModel tourTimeModel = new TourTimeModel();
					tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
					tourTimeModel.setTourId(tourId);
					tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
					tourTimeModel.createTourTime();

					if (adminSettingsModel.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
						new ProcessBookingRequest(tourId, loggedInuserId);
					}

					Map<String, Object> output = new HashMap<String, Object>();

					output.put("tourId", tourId);
					output.put("type", "SUCCESS");
					output.put("messages", messageForKey("successRequestSent", request));

					output.put("isSurgePriceApplied", false);
					output.put("surgePriceId", "-1");
					output.put("surgePrice", 1);
					output.put("estimateFareWithoutDiscount", 0);
					output.put("estimateFareWithDiscount", 0);
					output.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
					output.put("airportBookingType", tourModel.getAirportBookingType());

					return sendDataResponse(output);

				} else {

					return sendBussinessError(messageForKey("errorFailedSendRequest", request));
				}
			}
		}
	}

	@Path("/cancel/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response cancelTripRequestV4(
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

		df.setRoundingMode(RoundingMode.DOWN);

		double creditAdjustmentAmount = 0.0;

		double currentTime = DateUtils.nowAsGmtMillisec();

		double timeDiff = currentTime - tourTimeModel.getAcceptTime();

		boolean directCancelTripWithoutPayment = false;

		if (timeDiff <= (freeWaitingTimeModel.getCancelTime() * (60 * 1000))) {

			directCancelTripWithoutPayment = true;
		}

		List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();
		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
		apnsMessage.setCreatedAt(DateUtils.nowAsGmtMillisec());
		apnsMessage.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		apnsMessage.setToUserId(tourDetails.getPassengerId());
		apnsMessage.setMessageType("push");

		String pushMessage = "";

		//@formatter:off
		if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.STARTED_TOUR) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
		//@formatter:on

			if (tour.isRideLater()) {

				TourModel tour1 = new TourModel();
				tour1.setTourId(tour.getTourId());
				tour1.setDriverId(tour.getDriverId());
				tour1.setTourRideLater(false);

				tour1.updateRideLaterTourFlag();

				TourUtils.updateTourCriticalStatus(tour, false);
			}

			PromoCodeUtils.promoCodeCancel(tourDetails, false);

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				DriverTourStatusUtils.updateDriverTourStatus(tour.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

				TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR, loggedInuserId);

				tour.setCharges(0);
				tour.updateCharges();
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

					invoice.setPaymentMode(ProjectConstants.CASH);
					invoice.setTotal(0);
					invoice.setFine(0);
					invoice.setCharges(0);

				} else {

					CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();

					invoice.setFine(cancellationChargeModel.getCharge());
					invoice.setTotal(cancellationChargeModel.getCharge());

					List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

					double totalTaxAmount = 0.0;

					if (taxModelList != null && cancellationChargeModel.getCharge() > 0) {

						for (TaxModel taxModel : taxModelList) {

							totalTaxAmount = totalTaxAmount + ((taxModel.getTaxPercentage() / 100) * cancellationChargeModel.getCharge());
						}
					}

					invoice.setTotalTaxAmount(totalTaxAmount);

					creditAdjustmentAmount = invoice.getTotal() + totalTaxAmount;

					invoice.setCharges(cancellationChargeModel.getCharge() + totalTaxAmount);

					UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(tourDetails.getPassengerId());

					if (userProfileModel != null) {

						if (userProfileModel.getCredit() >= 0 && invoice.getCharges() > 0) {

							if (invoice.getCharges() >= userProfileModel.getCredit()) {

								invoice.setCharges(invoice.getCharges() - userProfileModel.getCredit());
								invoice.setUsedCredits(userProfileModel.getCredit());

							} else {

								invoice.setUsedCredits(invoice.getCharges());
								invoice.setCharges(0);
							}

						}

					}

					if (taxModelList != null && cancellationChargeModel.getCharge() > 0) {

						updateTaxDetails(taxModelList, cancellationChargeModel.getCharge(), tourId, loggedInuserId);
					}

					//@formatter:off					
					if ((tour.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) 
						&& tour.getCardOwner() != null && tour.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) 
							|| (tour.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) 
							&& tour.getCardOwner() != null && tour.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
					//@formatter:on

					} else {

						UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(tourDetails.getPassengerId());

						userProfile.setCredit(userProfile.getCredit() - creditAdjustmentAmount);

						invoice.setPaymentMode(ProjectConstants.CASH);
						invoice.setCard(false);
						invoice.setCashCollected(invoice.getCharges());
						invoice.setCashToBeCollected(invoice.getCharges());
						invoice.setCashNotReceived(false);

						pushMessage = messageForKey("labelForTripId", request) + " " /* + tourDetails.getUserTourId() + ", " */ + messageForKey("labelAmountCashCurrency", request) + invoice.getFine() + " "
									+ messageForKey("labelHasBeenDebitedFromCredits", request);

						userProfile.updateUserCredits();
					}
				}

				invoice.generateInvoice(loggedInuserId, true);
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

				UserModel user = UserModel.getUserAccountDetailsById(tourDetails.getDriverId());

				String message = BusinessAction.messageForKeyAdmin("errorPassengerCancelledTrip", adminSmsSendingModel.getLanguage());

				if (adminSmsSendingModel.isdCancelledByPassengerBusinessO()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, user.getPhoneNoCode() + user.getPhoneNo().trim(), ProjectConstants.SMSConstants.SMS_PASSENGER_CANCELLED_TEMPLATE_ID);
				}

				if (!directCancelTripWithoutPayment) {

					if ((pushMessage != null) && (!"".equals(pushMessage))) {

						apnsMessage.setMessage(pushMessage);
						ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());
						apnsMessageList.add(apnsMessage);
						ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);

						if (apnsDevice != null) {
							apnsDevice.sendNotification("1", "Push", pushMessage, ProjectConstants.NOTIFICATION_TRANSACTION, WebappPropertyUtils.getWebAppProperty("certificatePath"));
						}
					}
				}

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());
				if (apnsDevice != null) {
					apnsDevice.sendFCMNotification("1", "Push", "Cancelled", messageForKey("successTripCancelled", request));
				}
				return sendSuccessMessage(messageForKey("successTripCancelled", request));

			} else {

				return sendBussinessError(messageForKey("errorTripCancelled", request));
			}
		} else {
			return sendBussinessError(messageForKey("errorTripCantBeCancelled", request));
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

		TourModel tourModel = TourModel.getCurrentTourByPassangerId(loggedInuserId);
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		if (tourModel != null) {

			//@formatter:off
			if (tourModel.getStatus().equals(ProjectConstants.PENDING_REQUEST) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR) 
					|| tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR)) {
			//@formatter:on

				long currentTime = DateUtils.nowAsGmtMillisec();
				long oldTime = tourModel.getCreatedAt();
				long diffTime = ProjectConstants.REQUEST_TIME * 1000 * adminSettings.getNoOfCars();

				if ((currentTime - oldTime) > diffTime) {

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, loggedInuserId);

					TourModel tour1 = new TourModel();
					tour1.setCharges(0);
					tour1.setTourId(tourId);
					tour1.updateCharges();

					outputMap.put("statusCode", 201);
					outputMap.put("message", messageForKey("successTripExpiredSuccessfully", request));

					return sendDataResponse(outputMap);
				}

			} else {

				outputMap.put("statusCode", 200);
				outputMap.put("message", messageForKey("successTripExpiredSuccessfully", request));

				return sendDataResponse(outputMap);
			}
		}

		outputMap.put("statusCode", 400);
		outputMap.put("message", messageForKey("errorTripCantBeCancelled", request));

		return sendDataResponse(outputMap);
	}

	@Path("/booking-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBookingSttaus(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@HeaderParam("x-language-code") String lang
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		HashMap<String, Object> outputMap = new HashMap<String, Object>();

		TourModel tour = TourModel.getCurrentTourByPassangerId(loggedInuserId);

		if (tour != null) {

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

				outputMap.put("status", "inprogress");

			} else if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

				AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

				if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
					outputMap.put("status", ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
				} else {
					outputMap.put("status", ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);
				}

				UserModel user = UserModel.getUserAccountDetailsById(tour.getDriverId());

				if ((tour.getCarTypeId() == null) || ("".equals(tour.getCarTypeId())) || (("-1".equals(tour.getCarTypeId()))) || (ProjectConstants.Fifth_Vehicle_ID.equals(tour.getCarTypeId()))) {

					outputMap.put("modelName", "");
					outputMap.put("carColor", "");
					outputMap.put("carPlateNo", "");
					outputMap.put("carYear", "");
					outputMap.put("noOfPassenger", "");
					outputMap.put("owner", "");
					outputMap.put("make", "");

				} else {

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

				}

				outputMap.put("fullName", tour.getFirstName());
				outputMap.put("firstName", tour.getFirstName());
				outputMap.put("lastName", tour.getLastName());
				outputMap.put("dPhotoUrl", tour.getPhotoUrl());
				outputMap.put("phoneNo", tour.getPhoneNo());
				outputMap.put("phoneNoCode", tour.getPhoneNoCode());
				outputMap.put("carTypeId", tour.getCarTypeId());
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
				outputMap.put("isSurgePriceApplied", tour.isSurgePriceApplied());

				if (tour.isSurgePriceApplied()) {

					outputMap.put("surgePriceId", tour.getSurgePriceId());
					outputMap.put("surgePrice", tour.getSurgePrice());
					outputMap.put("totalWithSurge", tour.getTotalWithSurge());

				} else {

					outputMap.put("surgePriceId", "-1");
					outputMap.put("surgePrice", 1);
					outputMap.put("totalWithSurge", 0);
				}

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

				outputMap.put("paymentType", tour.getPaymentType());

				if (tour.isRideLater()) {
					outputMap.put("dateTime", tour.getRideLaterPickupTime());
				} else {
					outputMap.put("dateTime", tour.getCreatedAt());
				}

				// For rental
				outputMap.put("isRentalBooking", tour.isRentalBooking());

				Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

				if (tour.isRentalBooking()) {

					outputMap.put("rentalPackageId", tour.getRentalPackageId());
					outputMap.put("rentalPackageTime", tour.getRentalPackageTime());

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

					outputMap.put("rentalPackageDetails", rentalPackageDetails);

				} else {

					outputMap.put("rentalPackageId", "-1");
					outputMap.put("rentalPackageTime", 0);
					outputMap.put("rentalPackageDetails", rentalPackageDetails);
				}
				// ------------------------------------------------------------------------------

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

	@Path("/update/geolocation")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateGeoLocation(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			PassengerGeoLocationModel passengerGeoLocationModel
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {

			return sendUnauthorizedRequestError();
		}

		PassengerGeoLocationModel previousGeoLocationDetails = PassengerGeoLocationModel.getPassengerGeoLocationDetailsById(loggedInUserId);

		if (previousGeoLocationDetails != null) {

			if (previousGeoLocationDetails.getPassengerId() == null || "".equals(previousGeoLocationDetails.getPassengerId())) {

				passengerGeoLocationModel.insertPassengerGeoLocationDetails(loggedInUserId);
			} else {
				passengerGeoLocationModel.updatePassengerGeoLocationDetails(loggedInUserId);
			}

		} else {
			passengerGeoLocationModel.insertPassengerGeoLocationDetails(loggedInUserId);
		}

		return sendSuccessMessage(messageForKey("successUpdateGeoLocation", request));
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
		validator.addValidationMapping(SOURCE_ADDRESS, SOURCE_ADDRESS_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CAR_TYPE_ID, CAR_TYPE_ID_LABEL, new RequiredValidationRule());

		validator.addValidationMapping(DISTANCE, DISTANCE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CHARGES, CHARGES_LABEL, new RequiredValidationRule());

		if (tourModel.getCarTypeId().equals("5")) {
			validator.addValidationMapping(TRANSMISSION_TYPE_ID, TRANSMISSION_TYPE_LABEL, new RequiredValidationRule());
		}

		if (!tourModel.isRentalBooking()) {

			validator.addValidationMapping(DESTINATION_LATITUDE, DESTINATION_LATITUDE_LABEL, new RequiredValidationRule());
			validator.addValidationMapping(DESTINATION_LONGITUDE, DESTINATION_LONGITUDE_LABEL, new RequiredValidationRule());
			validator.addValidationMapping(DESTINATION_ADDRESS, DESTINATION_ADDRESS_LABEL, new RequiredValidationRule());
		}

		validator.addValidationMapping(IS_AIRPORT_FIXED_FARE_APPLIED, IS_AIRPORT_FIXED_FARE_APPLIED_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(tourModel);

		return errorMessages;
	}

	private void updateTaxDetails(List<TaxModel> taxModelList, double finalAmountForTaxCalculation, String tourId, String userId) {

		List<TourTaxModel> tourTaxModelList = new ArrayList<TourTaxModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		for (TaxModel taxModel : taxModelList) {

			double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

			taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2);

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

	public static double roundUpDecimalValueWithDownMode(double value, int numberOfDigitsAfterDecimalPoint) {

		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_DOWN);
		return bigDecimal.doubleValue();
	}
}