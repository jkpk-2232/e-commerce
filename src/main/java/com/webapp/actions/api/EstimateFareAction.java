package com.webapp.actions.api;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.CommonUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.EstimateFareModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.RideLaterSettingsModel;
import com.webapp.models.SurgePriceModel;
import com.webapp.models.TaxModel;
import com.webapp.models.UserPromoCodeModel;
import com.webapp.models.UtilizedUserPromoCodeModel;

@Path("/api/estimate-fare")
public class EstimateFareAction extends BusinessApiAction {

	public Logger logger = Logger.getLogger(BusinessApiAction.class);

	private static final String SOURCE_LATITUDE = "sLatitude";
	private static final String SOURCE_LATITUDE_LABEL = "Source Latitude";

	private static final String SOURCE_LONGITUDE = "sLongitude";
	private static final String SOURCE_LONGITUDE_LABEL = "Source Longitude";

	private static final String CAR_TYPE_ID = "carTypeId";
	private static final String CAR_TYPE_ID_LABEL = "Car Type Id";

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	// @formatter:off
	public Response estimateFare(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@HeaderParam("x-language-code") String lang, 
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			EstimateFareModel estimateFareModel
			) throws IOException {
		// @formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = estimateFareModelValidation(estimateFareModel);

		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		double estimateFareWithoutDiscount = 0.0;
		double estimateFareWithDiscount = 0.0;
		double distanceInMeters = 0.0;
		double durationInMin = 0.0;

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude());
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		if (multicityCityRegionModel == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(multicityCityRegionModel.getMulticityCountryId());

		if (!MultiCityAction.validateIfDestinationLiesWithinRegion(estimateFareModel.getdLatitude(), estimateFareModel.getdLongitude(), multicityCityRegionModel)) {
			return sendBussinessError(messageForKey("errorNoServicesToDestination", request));
		}

		if (!MultiTenantUtils.validateVendorCarType(estimateFareModel.getCarTypeId(), headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
			return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
		}

		Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude(), estimateFareModel.getdLatitude(), estimateFareModel.getdLongitude());

		distanceInMeters = distanceMatrix.get("distanceInMeters");
		durationInMin = distanceMatrix.get("durationInMin");

		CarFareModel carFareModel = new CarFareModel();
		Map<String, Object> airportRegionMap = new HashMap<String, Object>();
		airportRegionMap = airPortbooking(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude(), estimateFareModel.getdLatitude(), estimateFareModel.getdLongitude(), estimateFareModel.getCarTypeId());
		String airportBookingType = "";

		if ((boolean) airportRegionMap.get("isAirportBooking")) {

			if (((estimateFareModel.getsLatitude() != null) && (!"".equals(estimateFareModel.getsLatitude()))) && ((estimateFareModel.getsLongitude() != null) && (!"".equals(estimateFareModel.getsLongitude())))) {

				String latAndLong = "";
				String airportBookingTypeFare = "";
				if ((boolean) airportRegionMap.get("isAirportPickUp")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + estimateFareModel.getsLatitude() + " " + estimateFareModel.getsLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_PICKUP;
				}
				if ((boolean) airportRegionMap.get("isAirportDrop")) {

					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + estimateFareModel.getdLatitude() + " " + estimateFareModel.getdLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_DROP;
				}

				carFareModel = MultiTenantUtils.getAirportCarFare(estimateFareModel.getCarTypeId(), latAndLong, airportBookingTypeFare, headerVendorId);

				Map<String, Object> ac = TourUtils.checkBookingForAirportPickupOrDrop(estimateFareModel.getsLatitude(), estimateFareModel.getsLongitude(), estimateFareModel.getdLatitude(), estimateFareModel.getdLongitude());
				if ((boolean) ac.get("isAirportPickUp")) {
					airportBookingType = ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP;
				} else {
					airportBookingType = ProjectConstants.AIRPORT_BOOKING_TYPE_DROP;
				}
			}
		} else {
			carFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(estimateFareModel.getCarTypeId(), multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), headerVendorId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		}
		if (((estimateFareModel.getdLatitude() != null) && (!"".equals(estimateFareModel.getdLatitude()))) && ((estimateFareModel.getdLongitude() != null) && (!"".equals(estimateFareModel.getdLongitude())))) {

			if (carFareModel != null) {

				estimateFareWithoutDiscount = calculateFare(distanceInMeters, durationInMin, adminSettingsModel, carFareModel, true);
			}

			estimateFareWithDiscount = estimateFareWithoutDiscount;

		}

		String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;

		ApnsDeviceModel userDeviceDetails = ApnsDeviceModel.getDeviseByUserId(loggedInUserId);

		if (userDeviceDetails != null) {
			timeZone = userDeviceDetails.getTimezone();
		}

		Calendar calender = Calendar.getInstance();

		calender.setTimeZone(TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE));

		int currentHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
		int currentMinute = calender.get(Calendar.MINUTE);

		long requestTimeInMilli = DateUtils.getTimeInMillisForSurge(currentHourOfDay, currentMinute, false, timeZone);

		String surgeMessage = "";
		SurgePriceModel surgePriceModel = SurgePriceModel.getSurgePriceDetailsByRequestTimeAndRegionId(requestTimeInMilli, multicityCityRegionId);

		boolean isSurgePriceApplied = false;
		String surgePriceId = "-1";
		double surgePrice = 1;

		if (!(boolean) airportRegionMap.get("isAirportBooking")) {
			if (surgePriceModel != null) {

				estimateFareWithoutDiscount = estimateFareWithoutDiscount * surgePriceModel.getSurgePrice();
				estimateFareWithDiscount = estimateFareWithoutDiscount;

				isSurgePriceApplied = true;
				surgePriceId = surgePriceModel.getSurgePriceId();
				surgePrice = surgePriceModel.getSurgePrice();
				surgeMessage = surgePrice + "x surge applied.";
			}
		}

		CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, multicityCityRegionId, adminSettingsModel);

		if (!(boolean) airportRegionMap.get("isAirportBooking")) {

			if (!estimateFareModel.isRideLater() && applicableCitySurgeModel != null && applicableCitySurgeModel.getSurgeRate() > surgePrice) {

				estimateFareWithoutDiscount = estimateFareWithoutDiscount * applicableCitySurgeModel.getSurgeRate();
				estimateFareWithDiscount = estimateFareWithoutDiscount;

				isSurgePriceApplied = true;
				surgePriceId = applicableCitySurgeModel.getCitySurgeId();
				surgePrice = applicableCitySurgeModel.getSurgeRate();
				surgeMessage = surgePrice + "x surge applied as driver is traveling from far for your pickup.";
			}
		}

		boolean isAirportFixedFareApplied = false;

		// ---------- Tax -------------------------------
		List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

		double finalAmountForTaxCalculation;
		double totalTaxAmount = 0.0;

		finalAmountForTaxCalculation = estimateFareWithoutDiscount;

		if (taxModelList != null) {

			for (TaxModel taxModel : taxModelList) {

				double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

				taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2);

				totalTaxAmount = totalTaxAmount + taxAmount;
			}
		}

		totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2);

		estimateFareWithoutDiscount = estimateFareWithoutDiscount + totalTaxAmount;

		estimateFareWithDiscount = estimateFareWithoutDiscount;

		// -------------------------------------------------------------------------------------

		boolean isPromoCodeApplied = false;
		double discount = 0.0;
		String promoCodeId = "";
		String mode = "-1";

		if ((estimateFareModel.getPromoCode() != null) && (!"".equals(estimateFareModel.getPromoCode()))) {

			PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(estimateFareModel.getPromoCode());

			if (promoCode != null) {

				long currentTime = DateUtils.nowAsGmtMillisec();

				if (currentTime >= promoCode.getStartDate() && currentTime <= promoCode.getEndDate()) {

					UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = UtilizedUserPromoCodeModel.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInUserId, promoCode.getPromoCodeId());

					if (utilizedUserPromoCodeModel != null) {
						isPromoCodeApplied = false;
					} else {

						if (promoCode.getUsageType().equalsIgnoreCase(ProjectConstants.ALL_ID)) {

							if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

								isPromoCodeApplied = true;
								discount = promoCode.getDiscount();
								promoCodeId = promoCode.getPromoCodeId();
								mode = promoCode.getMode();

							} else {

								if (promoCode.getUsedCount() < promoCode.getUsageCount()) {

									isPromoCodeApplied = true;
									discount = promoCode.getDiscount();
									promoCodeId = promoCode.getPromoCodeId();
									mode = promoCode.getMode();

								} else {

									isPromoCodeApplied = false;
								}
							}

						} else {

							UserPromoCodeModel userPromoCodeModel = UserPromoCodeModel.getUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInUserId, promoCode.getPromoCodeId());

							if (userPromoCodeModel != null) {

								isPromoCodeApplied = true;
								discount = promoCode.getDiscount();
								promoCodeId = promoCode.getPromoCodeId();
								mode = promoCode.getMode();

							} else {

								isPromoCodeApplied = false;
							}
						}
					}
				} else {

					isPromoCodeApplied = false;
				}
			} else {

				isPromoCodeApplied = false;
			}
		}

		if (isPromoCodeApplied) {

			if (estimateFareWithDiscount > 0) {

				if (mode.equals(ProjectConstants.PERCENTAGE_ID)) {
					estimateFareWithDiscount = estimateFareWithDiscount - ((estimateFareWithDiscount / 100) * discount);
				} else {
					estimateFareWithDiscount = estimateFareWithDiscount - discount;
				}
			}
		}

		if (estimateFareWithoutDiscount < 0) {

			estimateFareWithoutDiscount = 0;
		}

		if (estimateFareWithDiscount < 0) {

			estimateFareWithDiscount = 0;
		}

		if (((estimateFareModel.getdLatitude() == null) || ("".equals(estimateFareModel.getdLatitude()))) || ((estimateFareModel.getdLongitude() == null) || ("".equals(estimateFareModel.getdLongitude())))) {

			estimateFareWithoutDiscount = 0;
			estimateFareWithDiscount = 0;
		}

		InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(loggedInUserId);
		boolean isPaymentPaid = true;

		if (invoiceModel != null) {

			if (invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

				isPaymentPaid = false;
			}
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		outputMap.put("type", "SUCCESS");

		outputMap.put("carTypeId", estimateFareModel.getCarTypeId());
		outputMap.put("estimateFareWithoutDiscount", df_new.format(estimateFareWithoutDiscount));
		outputMap.put("estimateFareWithDiscount", df_new.format(estimateFareWithDiscount));
		outputMap.put("distanceInMeters", distanceInMeters);
		outputMap.put("promoCode", estimateFareModel.getPromoCode());
		outputMap.put("isPromoCodeApplied", isPromoCodeApplied);
		outputMap.put("promoCodeId", promoCodeId);
		outputMap.put("discount", discount);
		outputMap.put("mode", mode);
		outputMap.put("currencySymbol", multicityCountryModel.getCurrencySymbol());
		outputMap.put("multicityCityRegionId", multicityCityRegionId);
		outputMap.put("multicityCountryId", multicityCityRegionModel.getMulticityCountryId());
		outputMap.put("isSurgePriceApplied", isSurgePriceApplied);
		outputMap.put("surgePriceId", surgePriceId);
		outputMap.put("surgePrice", surgePrice);
		outputMap.put("carFareDetails", carFareModel);
		outputMap.put("isPaymentPaid", isPaymentPaid);
		outputMap.put("minBookingTime", rideLaterSettingsModel.getMinBookingTime());
		outputMap.put("maxBookingTime", rideLaterSettingsModel.getMaxBookingTime());
		outputMap.put("isAirportFixedFareApplied", isAirportFixedFareApplied);
		outputMap.put("airportBookingType", airportBookingType);
		outputMap.put("surgeMessage", surgeMessage);

		return sendDataResponse(outputMap);
	}

	public static double calculateFare(double distanceInMeters, double durationInMin, AdminSettingsModel adminSettingsModel, CarFareModel carFareModel, boolean isEstimateFare) {

		double estimateFareWithoutDiscount;
		double baseFare = carFareModel.getInitialFare(); // + carFareModel.getBookingFees();
		double timeFare = durationInMin * carFareModel.getPerMinuteFare();

		double totalDistanceInMeter = distanceInMeters - carFareModel.getFreeDistance();

		double distanceFare = 0.0;
		double distBeforeSpecificKmInMeters = 0.0;
		double distAfterSpecificKmInMeters = 0.0;
		double distanceFareBeforeSpecificKm = 0.0;
		double distanceFareAfterSpecificKm = 0.0;

		if (totalDistanceInMeter > carFareModel.getKmToIncreaseFare()) {
			distBeforeSpecificKmInMeters = carFareModel.getKmToIncreaseFare();
			distAfterSpecificKmInMeters = (totalDistanceInMeter - carFareModel.getKmToIncreaseFare());
		} else {
			distBeforeSpecificKmInMeters = totalDistanceInMeter;
		}

		double distBeforeSpecificKmInProjectUnit = 0.0;

		double distAfterSpecificKmInProjectUnit = 0.0;

		if (distBeforeSpecificKmInMeters > 0) {
			distBeforeSpecificKmInProjectUnit = distBeforeSpecificKmInMeters / adminSettingsModel.getDistanceUnits();
		} else {
			distBeforeSpecificKmInMeters = 0;
		}

		if (distAfterSpecificKmInMeters > 0) {
			distAfterSpecificKmInProjectUnit = distAfterSpecificKmInMeters / adminSettingsModel.getDistanceUnits();
		} else {
			distAfterSpecificKmInMeters = 0;
		}

		distanceFareBeforeSpecificKm = (distBeforeSpecificKmInProjectUnit > 0 ? distBeforeSpecificKmInProjectUnit : 0) * carFareModel.getPerKmFare();

		distanceFareAfterSpecificKm = (distAfterSpecificKmInProjectUnit > 0 ? distAfterSpecificKmInProjectUnit : 0) * carFareModel.getFareAfterSpecificKm();

		distanceFare = distanceFareBeforeSpecificKm + distanceFareAfterSpecificKm;

		estimateFareWithoutDiscount = baseFare + distanceFare + timeFare;

		return estimateFareWithoutDiscount;
	}

	private List<String> estimateFareModelValidation(EstimateFareModel estimateFareModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(SOURCE_LATITUDE, SOURCE_LATITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(SOURCE_LONGITUDE, SOURCE_LONGITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CAR_TYPE_ID, CAR_TYPE_ID_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(estimateFareModel);

		return errorMessages;
	}

}