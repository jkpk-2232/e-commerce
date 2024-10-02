package com.webapp.actions.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.TaxModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTimeModel;

@Path("/api/live-fare")
public class LiveFareAction extends BusinessApiAction {

	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateGeoLocation(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@QueryParam("tourId") String tourId			
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {

			return sendUnauthorizedRequestError();
		}

		Map<String, Object> output = new HashMap<String, Object>();

		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

		if (tourDetails != null) {

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(tourDetails.getMulticityCityRegionId());

			MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(multicityCityRegionModel.getMulticityCountryId());

			TourTimeModel tourTimeModel = TourTimeModel.getTourTimesDetailsByTourId(tourDetails.getTourId());

			FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();

			double baseFare = 0.0;
			double timeFare = 0.0;
			double distanceFare = 0.0;

			if (tourDetails.isRentalBooking()) {

				double duration = 0.0;
				double distance = 0.0;

				if (tourTimeModel.getArrivedWaitingTime() < tourTimeModel.getBookingTime()) {

					double totalTime = DateUtils.nowAsGmtMillisec() - (tourTimeModel.getBookingTime() + (freeWaitingTimeModel.getWaitingTime() * (60 * 1000)));

					if ((totalTime > tourDetails.getRentalPackageTime()) && (totalTime > 0)) {

						duration = totalTime;

					} else {

						duration = DateUtils.nowAsGmtMillisec() - tourTimeModel.getStartTime();
					}

				} else if (tourTimeModel.getArrivedWaitingTime() > tourTimeModel.getBookingTime()) {

					double totalTime = DateUtils.nowAsGmtMillisec() - (tourTimeModel.getArrivedWaitingTime() + (freeWaitingTimeModel.getWaitingTime() * (60 * 1000)));

					if ((totalTime > tourDetails.getRentalPackageTime()) && (totalTime > 0)) {

						duration = totalTime;

					} else {

						duration = DateUtils.nowAsGmtMillisec() - tourTimeModel.getStartTime();
					}

				} else {

					duration = DateUtils.nowAsGmtMillisec() - tourTimeModel.getStartTime();
				}

				distance = tourDetails.getDistanceLive();

				Map<String, Object> fareDetailsMap = new HashMap<String, Object>();

				fareDetailsMap = calculateFareForRentalBooking(tourDetails, duration, distance);

				baseFare = (Double) fareDetailsMap.get("baseFare");
				timeFare = (Double) fareDetailsMap.get("timeFare");
				distanceFare = (Double) fareDetailsMap.get("distanceFare");

			} else {

				baseFare = tourDetails.getInitialFare(); // + tourDetails.getBookingFees();

				double totalDistanceInMeter = tourDetails.getDistanceLive() - tourDetails.getFreeDistance();

				double distBeforeSpecificKmInMeters = 0.0;
				double distAfterSpecificKmInMeters = 0.0;
				double distanceFareBeforeSpecificKm = 0.0;
				double distanceFareAfterSpecificKm = 0.0;

				if (totalDistanceInMeter > tourDetails.getKmToIncreaseFare()) {
					distBeforeSpecificKmInMeters = tourDetails.getKmToIncreaseFare();
					distAfterSpecificKmInMeters = (totalDistanceInMeter - tourDetails.getKmToIncreaseFare());
				} else {
					distBeforeSpecificKmInMeters = totalDistanceInMeter;
				}

				double distBeforeSpecificKmInProjectUnit = 0.0;

				double distAfterSpecificKmInProjectUnit = 0.0;

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

				distanceFareBeforeSpecificKm = (distBeforeSpecificKmInProjectUnit > 0 ? distBeforeSpecificKmInProjectUnit : 0) * tourDetails.getPerKmFare();

				distanceFareAfterSpecificKm = (distAfterSpecificKmInProjectUnit > 0 ? distAfterSpecificKmInProjectUnit : 0) * tourDetails.getFareAfterSpecificKm();

				distanceFare = distanceFareBeforeSpecificKm + distanceFareAfterSpecificKm;

				double arrivedWaitingTime = (tourTimeModel.getStartTime() - tourTimeModel.getArrivedWaitingTime()) - (freeWaitingTimeModel.getWaitingTime() * (60 * 1000));

				arrivedWaitingTime = arrivedWaitingTime > 0 ? arrivedWaitingTime : 0;

				long currentTourTime = (DateUtils.nowAsGmtMillisec() - tourTimeModel.getStartTime());

				timeFare = (((arrivedWaitingTime + currentTourTime) / (1000 * 60)) * tourDetails.getPerMinuteFare());

			}

			double currentFare = baseFare + timeFare + distanceFare;

			if (currentFare <= 0) {

				currentFare = 0;
			}

			if (tourDetails.isSurgePriceApplied()) {
				currentFare = currentFare * tourDetails.getSurgePrice();
			}

			double totalTaxAmount = 0.0;
			double finalAmountForTaxCalculation = currentFare;

			List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

			if (taxModelList != null) {

				for (TaxModel taxModel : taxModelList) {

					double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

					taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2); // roundOff(taxAmount, true, true, RoundingMode.DOWN, 2); 

					totalTaxAmount = totalTaxAmount + taxAmount;
				}
			}

			totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2); //roundOff(totalTaxAmount, true, true, RoundingMode.DOWN, 2); 

			currentFare = currentFare + totalTaxAmount;

			if (tourDetails.isAirportFixedFareApplied()) {
				currentFare = tourDetails.getCharges();
			}

			output.put("currencySymbol", multicityCountryModel.getCurrencySymbol());
			output.put("currentFare", df.format(currentFare));

		} else {

			output.put("currencySymbol", "");
			output.put("currentFare", 0);
		}

		return sendDataResponse(output);
	}

	@Path("/update/distance")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateLiveDistance(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			TourModel tourModel
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {

			return sendUnauthorizedRequestError();
		}

		TourModel previousDetailsOfTour = TourModel.getTourDetailsByTourId(tourModel.getTourId());

		if (previousDetailsOfTour != null) {

			if (tourModel.getDistanceLive() > previousDetailsOfTour.getDistanceLive()) {

				tourModel.updateDistanceLiveByTourId(loggedInUserId);
			}
		}

		return sendSuccessMessage(messageForKey("successDistanceUpdated", request));
	}

	public static Map<String, Object> calculateFareForRentalBooking(TourModel tourModel, double duration, double distance) {

		Map<String, Object> outputMap = new HashMap<String, Object>();

		double baseFare = 0.0;
		double timeFare = 0.0;
		double distanceFare = 0.0;
		double total = 0.0;
		double timeInMin = 0.0;
		double distanceInProjectUnit = 0.0;

		baseFare = tourModel.getInitialFare();

		timeInMin = millisToMin(duration);

		double rentalPackageTimeInMin = tourModel.getRentalPackageTime() * 60;

		if (timeInMin > rentalPackageTimeInMin) {

			timeFare = (timeInMin - rentalPackageTimeInMin) * tourModel.getPerMinuteFare();

			timeFare = roundUpDecimalValueWithDownMode(timeFare, 2);
		}

		double totalDistanceInMeter = distance - tourModel.getFreeDistance();

		if (totalDistanceInMeter < 0) {

			totalDistanceInMeter = 0;
		}

		if (totalDistanceInMeter > 0) {

			distanceInProjectUnit = getDistanceInProjectUnitFromMeters(totalDistanceInMeter);

			distanceFare = (distanceInProjectUnit > 0 ? distanceInProjectUnit : 0) * tourModel.getPerKmFare();

			distanceFare = roundUpDecimalValueWithDownMode(distanceFare, 2);
		}

		total = baseFare + timeFare + distanceFare;

		outputMap.put("baseFare", baseFare);
		outputMap.put("timeFare", timeFare);
		outputMap.put("distanceFare", distanceFare);
		outputMap.put("total", total);

		return outputMap;
	}

}