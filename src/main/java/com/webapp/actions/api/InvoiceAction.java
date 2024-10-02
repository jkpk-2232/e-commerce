package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.FavouriteDriverModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.PassengerTripRatingsModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTaxModel;

@Path("/api/invoice")
public class InvoiceAction extends BusinessApiAction {

	Logger logger = Logger.getLogger(InvoiceAction.class);

	@Path("/{tourId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getInvoiceByTourId(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("tourId") String tourId
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		InvoiceModel invoiceModel = InvoiceModel.getInvoiceByTourId(tourId);

		if (invoiceModel != null) {

			TourModel tour = TourModel.getTourDetailsByTourId(invoiceModel.getTourId());
			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			outPutMap.put("isAirportFixedFareApplied", tour.isAirportFixedFareApplied());
			outPutMap.put("airportBookingType", tour.getAirportBookingType());
			outPutMap.put("airportBookingFare", invoiceModel.getTotal());
			outPutMap.put("passengerId", tour.getPassengerId());
			outPutMap.put("driverId", tour.getDriverId());
			outPutMap.put("tourId", tour.getTourId());
			outPutMap.put("firstName", tour.getFirstName());
			outPutMap.put("lastName", tour.getLastName());
			outPutMap.put("dPhotoUrl", tour.getPhotoUrl());
			outPutMap.put("phoneNo", tour.getPhoneNo());
			outPutMap.put("phoneNoCode", tour.getPhoneNoCode());
			outPutMap.put("sourceAddress", tour.getSourceAddress());
			outPutMap.put("destinationAddress", tour.getDestinationAddress());
			outPutMap.put("pFirstName", tour.getpFirstName());
			outPutMap.put("pLastName", tour.getpLastName());
			outPutMap.put("pPhotoUrl", tour.getpPhotoUrl());
			outPutMap.put("initialFare", invoiceModel.getInitialFare() + invoiceModel.getMarkupFare());
			outPutMap.put("perKmFare", invoiceModel.getPerKmFare());
			outPutMap.put("perMinuteFare", invoiceModel.getPerMinuteFare());
			outPutMap.put("bookingFees", invoiceModel.getBookingFees());
			outPutMap.put("discount", invoiceModel.getDiscount());
			outPutMap.put("subTotal", invoiceModel.getSubTotal());
			outPutMap.put("total", invoiceModel.getTotal() + invoiceModel.getMarkupFare());
			outPutMap.put("finalAmountCollected", invoiceModel.getFinalAmountCollected());
			outPutMap.put("total", invoiceModel.getTotal());
			outPutMap.put("distance", invoiceModel.getDistance());
			outPutMap.put("charges", invoiceModel.getCharges());
			outPutMap.put("duration", invoiceModel.getDuration());
			outPutMap.put("avgSpeed", invoiceModel.getAvgSpeed());
			outPutMap.put("fine", invoiceModel.getFine());
			outPutMap.put("distanceFare", invoiceModel.getDistanceFare());
			outPutMap.put("timeFare", invoiceModel.getTimeFare());
			outPutMap.put("carTypeId", tour.getCarTypeId());
			outPutMap.put("minimumFare", invoiceModel.getMinimumFare());
			outPutMap.put("tollAmount", invoiceModel.getTollAmount());
			outPutMap.put("freeDistance", tour.getFreeDistance());
			outPutMap.put("staticMapImgUrl", invoiceModel.getStaticMapImgUrl());
			outPutMap.put("isPaymentPaid", invoiceModel.isPaymentPaid());
			outPutMap.put("arrivedWaitingTimeFare", invoiceModel.getArrivedWaitingTimeFare());
			outPutMap.put("surgeFare", invoiceModel.getSurgeFare());
			outPutMap.put("isSurgePriceApplied", invoiceModel.isSurgePriceApplied());
			outPutMap.put("vendorMarkupFare", invoiceModel.getMarkupFare());

			if (invoiceModel.isSurgePriceApplied()) {

				outPutMap.put("surgePriceId", invoiceModel.getSurgePriceId());
				outPutMap.put("surgePrice", invoiceModel.getSurgePrice());
				outPutMap.put("totalWithSurge", invoiceModel.getTotalWithSurge());

			} else {

				outPutMap.put("surgePriceId", "-1");
				outPutMap.put("surgePrice", 1);
				outPutMap.put("totalWithSurge", 0);
			}

			if (invoiceModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.CARD)) {
				outPutMap.put("card", true);
			} else {
				outPutMap.put("card", false);
			}

			outPutMap.put("paymentMode", invoiceModel.getPaymentMode());
			outPutMap.put("cashCollected", invoiceModel.getCashCollected());
			outPutMap.put("cashToBeCollected", invoiceModel.getCashToBeCollected());
			outPutMap.put("paymentStatus", invoiceModel.getPaymentStatus());
			outPutMap.put("cashNotReceived", invoiceModel.isCashNotReceived());
			outPutMap.put("isPromoCodeApplied", invoiceModel.isPromoCodeApplied());
			outPutMap.put("promoDiscount", invoiceModel.getPromoDiscount());
			outPutMap.put("usedCredits", invoiceModel.getUsedCredits());
			outPutMap.put("percentage", invoiceModel.getPercentage());
			outPutMap.put("driverAmount", df_new.format(invoiceModel.getDriverAmount()));
			outPutMap.put("waitingTime", invoiceModel.getArrivedWaitingTime());

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

			outPutMap.put("driverId", tour.getDriverId());
			outPutMap.put("passengerId", tour.getPassengerId());
			outPutMap.put("userTourId", tour.getUserTourId());
			outPutMap.put("sourceLatitude", tour.getsLatitude());
			outPutMap.put("sourceLongitude", tour.getsLongitude());
			outPutMap.put("destinationLatitude", tour.getdLatitude());
			outPutMap.put("destinationLongitude", tour.getdLongitude());
			outPutMap.put("status", tour.getStatus());

			outPutMap.put("isRideLater", tour.isRideLater());

			if (tour.isRideLater()) {
				outPutMap.put("dateTime", tour.getRideLaterPickupTime());
			} else {
				outPutMap.put("dateTime", tour.getCreatedAt());
			}

			DriverTripRatingsModel driverTripRatingsModel = DriverTripRatingsModel.getRatingDetailsByTourId(tour.getTourId());

			if (driverTripRatingsModel != null) {
				outPutMap.put("isPassengerRated", true);
				outPutMap.put("passengerRatings", driverTripRatingsModel.getRate());
				outPutMap.put("passengerComments", driverTripRatingsModel.getNote());
			} else {
				outPutMap.put("isPassengerRated", false);
				outPutMap.put("passengerRatings", "-1");
				outPutMap.put("passengerComments", "");
			}

			PassengerTripRatingsModel passengerTripRatingsModel = PassengerTripRatingsModel.getPassenerRatingsByTripId(tour.getTourId());

			if (passengerTripRatingsModel != null) {
				outPutMap.put("isDriverRated", true);
				outPutMap.put("driverRatings", passengerTripRatingsModel.getRate());
				outPutMap.put("driverComments", passengerTripRatingsModel.getNote());
			} else {
				outPutMap.put("isDriverRated", false);
				outPutMap.put("driverRatings", "-1");
				outPutMap.put("driverComments", "");
			}

			FavouriteDriverModel favouriteDriverModelCheck = FavouriteDriverModel.getFavouriteDriverDetails(loggedInuserId, tour.getDriverId());

			if (favouriteDriverModelCheck != null) {
				outPutMap.put("favouriteDriver", true);
			} else {
				outPutMap.put("favouriteDriver", false);
			}

			List<Map<String, Object>> taxOutPutMapList = new ArrayList<Map<String, Object>>();

			List<TourTaxModel> tourTaxModelList = TourTaxModel.getTourTaxListByTourId(tourId);

			for (TourTaxModel tourTaxModel : tourTaxModelList) {

				Map<String, Object> taxOutPutMap = new HashMap<String, Object>();

				taxOutPutMap.put("tourTaxId", tourTaxModel.getTourTaxId());
				taxOutPutMap.put("taxId", tourTaxModel.getTaxId());
				taxOutPutMap.put("taxName", tourTaxModel.getTaxName());
				taxOutPutMap.put("taxPercentage", tourTaxModel.getTaxPercentage());
				taxOutPutMap.put("taxAmount", tourTaxModel.getTaxAmount());

				taxOutPutMapList.add(taxOutPutMap);
			}

			outPutMap.put("taxList", taxOutPutMapList);
			outPutMap.put("totalTaxAmount", invoiceModel.getTotalTaxAmount());
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

			return sendDataResponse(outPutMap);

		} else {
			return sendBussinessError(messageForKey("errorNoDataAvailableInvoice", request));
		}
	}

}