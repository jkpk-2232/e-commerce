package com.webapp.actions.secure.adminbookings;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.SendEmailThread;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.PassengerTripRatingsModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserProfileModel;

@Path("/admin-bookings-details")
public class AdminBookingsDetailsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadAdminBookingDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TOUR_ID) String tourId,
		@QueryParam(FieldConstants.TOUR_TYPE) String tourType) 
		throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		if (tourModel == null) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (StringUtils.validString(tourType)) {
			data.put(FieldConstants.TOUR_TYPE, tourType);
		} else {
			data.put(FieldConstants.TOUR_TYPE, "bookings");
		}

		data.put("isShowReleasePassengerDiv", "NO");

		InvoiceModel invoiceModel = InvoiceModel.getInvoiceByTourId(tourId);
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		data.put("labelDistanceFareBeforeSpecificKm", "Distance Fare Before " + StringUtils.valueOf(tourModel.getKmToIncreaseFare() / adminSettings.getDistanceUnits()) + " km");
		data.put("labeldistanceFareAfterSpecificKm", "Distance Fare After " + StringUtils.valueOf(tourModel.getKmToIncreaseFare() / adminSettings.getDistanceUnits()) + " km");

		data.put("tourId", tourId);
		data.put("userTourId", tourModel.getUserTourId());

		data.put("isAirportFixedFareApplied", String.valueOf(tourModel.isAirportFixedFareApplied()));
		data.put("airportBookingType", tourModel.getAirportBookingType());

		if (tourModel.isSurgePriceApplied()) {

			data.put("isSurgeApplied", String.valueOf(true));

			if (tourModel.getSurgeType() == null) {
				data.put("surgeType", ProjectConstants.SURGE_TYPE_TIME);
				data.put("surgeRadius", "-");
			} else {
				if (tourModel.getSurgeType().equals(ProjectConstants.SURGE_TYPE_RADIUS)) {
					data.put("surgeType", ProjectConstants.SURGE_TYPE_RADIUS);
					data.put("surgeRadius", tourModel.getSurgeRadius() + "");
				} else {
					data.put("surgeType", ProjectConstants.SURGE_TYPE_TIME);
					data.put("surgeRadius", "-");
				}
			}
		} else {
			data.put("isSurgeApplied", String.valueOf(false));
		}

		String tourStatus = "";

		if ((tourModel.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING))) {
			tourStatus += "Business Owner Booking - ";
		} else if ((tourModel.getBookingType().equals(ProjectConstants.ADMIN_BOOKING))) {
			tourStatus += "Admin Booking - ";
		} else {
			tourStatus += "Individual Booking - ";
		}

		tourStatus += TourUtils.getTourStatus(tourModel.getStatus());

		data.put("tourStatus", tourStatus);

		String statusForFareBreakdown = "";

		if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
			statusForFareBreakdown = "cancel by driver";
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
			statusForFareBreakdown = "cancel by passenger";
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
			statusForFareBreakdown = "passenger unavailable";
		}

		data.put("statusForFareBreakdown", statusForFareBreakdown);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			if (tourModel.getPassengerVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
				data.put("pFullName", MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
				data.put("pPhone", MyHubUtils.formatPhoneNumber(tourModel.getpPhoneCode(), tourModel.getpPhone()));
				data.put("pEmail", tourModel.getpEmail());
			} else {
				data.put("pFullName", ProjectConstants.NOT_AVAILABLE);
				data.put("pPhone", ProjectConstants.NOT_AVAILABLE);
				data.put("pEmail", ProjectConstants.NOT_AVAILABLE);
			}
		} else {
			data.put("pFullName", MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			data.put("pPhone", MyHubUtils.formatPhoneNumber(tourModel.getpPhoneCode(), tourModel.getpPhone()));
			data.put("pEmail", tourModel.getpEmail());
		}

		data.put("airportBookingType", tourModel.getAirportBookingType());

		CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(tourModel.getCarTypeId());

		data.put("carType", carTypeModel.getCarType());

		if (!ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(tourModel.getDriverId())) {
			data.put("dFullName", MyHubUtils.formatFullName(tourModel.getFirstName(), tourModel.getLastName()));
			data.put("dPhone", MyHubUtils.formatPhoneNumber(tourModel.getPhoneNoCode(), tourModel.getPhoneNo()));
			data.put("dEmail", tourModel.getEmail());
			data.put("dServiceType", carTypeModel.getCarType());
		} else {
			data.put("dFullName", ProjectConstants.NOT_AVAILABLE);
			data.put("dPhone", ProjectConstants.NOT_AVAILABLE);
			data.put("dEmail", ProjectConstants.NOT_AVAILABLE);
			data.put("dServiceType", ProjectConstants.NOT_AVAILABLE);
		}

		if (tourModel.isRideLater()) {
			data.put("tripRequestTime", DateUtils.dbTimeStampToSesionDate(tourModel.getRideLaterPickupTime(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		} else {
			data.put("tripRequestTime", DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		}

		data.put("sourceAddress", tourModel.getSourceAddress());

		if (StringUtils.validString(tourModel.getDestinationAddress())) {
			data.put("destinationAddress", tourModel.getDestinationAddress());
		} else {
			data.put("destinationAddress", ProjectConstants.NOT_AVAILABLE);
		}

		if (invoiceModel != null) {

			data.put("distanceFareBeforeSpecificKm", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getDistanceFareBeforeSpecificKm()));
			data.put("distanceFareAfterSpecificKm", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getDistanceFareAfterSpecificKm()));

			data.put("charges", adminSettings.getCurrencySymbolHtml() + invoiceModel.getCharges());
			data.put("total", adminSettings.getCurrencySymbolHtml() + invoiceModel.getTotal());
			data.put("finalAmountCollected", adminSettings.getCurrencySymbolHtml() + invoiceModel.getFinalAmountCollected());

			data.put("finalAmountCollectedHidden", invoiceModel.getFinalAmountCollected() + "");

			data.put("updatedAmountCollected", adminSettings.getCurrencySymbolHtml() + invoiceModel.getUpdatedAmountCollected());

			logger.info("\n\n\n invoiceModel.getRemark(): " + invoiceModel.getRemark() + "\n\n\n");

			if (invoiceModel.getRemark() == null || "".equals(invoiceModel.getRemark().trim())) {
				data.put("remark", "");
			} else {
				data.put("remark", invoiceModel.getRemark());
			}

			data.put("refundAmount", adminSettings.getCurrencySymbolHtml() + invoiceModel.getRefundAmount());
			data.put("paymentMode", invoiceModel.getPaymentMode());
			data.put("paymentStatus", invoiceModel.getPaymentStatus());

			data.put("distance", StringUtils.valueOf(invoiceModel.getDistance() / adminSettings.getDistanceUnits()) + " " + adminSettings.getDistanceType());

			long millis = Long.parseLong(new DecimalFormat("0").format(invoiceModel.getDuration()));

			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
						TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

			data.put("duration", hms);

			long millisWaitingTime = Long.parseLong(new DecimalFormat("0").format(invoiceModel.getArrivedWaitingTime()));

			String hms1 = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisWaitingTime), TimeUnit.MILLISECONDS.toMinutes(millisWaitingTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisWaitingTime)),
						TimeUnit.MILLISECONDS.toSeconds(millisWaitingTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisWaitingTime)));

			data.put("waitingTime", hms1);

			data.put("baseFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getBaseFare()));
			data.put("distanceFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getDistanceFare()));
			data.put("timeFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getTimeFare()));
			data.put("waitingFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getArrivedWaitingTimeFare()));
			data.put("surgeFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getSurgeFare()));
			data.put("totalFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getTotal()));
			data.put("promoDiscount", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getPromoDiscount()));
			data.put("taxAmount", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getTotalTaxAmount()));
			data.put("total", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getCharges()));
			data.put("amountCollected", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()));
			data.put("markupFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getMarkupFare()));

			String usedCreditsStr = StringUtils.valueOfDfNew(invoiceModel.getUsedCredits()) + "";

			if (usedCreditsStr.contains("-")) {

				if (invoiceModel.getUsedCredits() != 0) {

					usedCreditsStr = usedCreditsStr.replace("-", "+");
				}

			} else {

				if (invoiceModel.getUsedCredits() != 0) {

					usedCreditsStr = "-" + usedCreditsStr;
				}
			}

			data.put("credits", adminSettings.getCurrencySymbolHtml() + usedCreditsStr);

			if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				data.put("cancellationCharges", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFine()));
				data.put("usedCredits", adminSettings.getCurrencySymbolHtml() + usedCreditsStr);
				data.put("taxAmount", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getTotalTaxAmount()));
				data.put("amountCollected", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getCharges()));
			} else {
				data.put("cancellationCharges", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(0));
				data.put("usedCredits", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(0));
			}

			if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				data.put("paymentMode", ProjectConstants.CREDITS_STRING);
				data.put("paymentStatusString", ProjectConstants.ADJUSTED_IN_CREDITS_MESSAGE + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getCharges()));

			} else {

				if (invoiceModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {

					data.put("isShowReleasePassengerDiv", "NO");

					data.put("paymentMode", ProjectConstants.C_CASH);

					if (invoiceModel.isCashNotReceived()) {

						data.put("paymentStatusString", ProjectConstants.CASH_INVOICE_MESSAGE_NOT_COLLECTED);

					} else {

//							data.put("paymentStatusString",
//										ProjectConstants.CASH_INVOICE_MESSAGE_COLLECTED + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()) + " " + "<span id='editSpan'>Edit</span>");

						data.put("paymentStatusString", ProjectConstants.CASH_INVOICE_MESSAGE_COLLECTED + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()));
					}

				} else {

					data.put("paymentMode", ProjectConstants.C_CARD);

					if (invoiceModel.isPaymentPaid()) {

						data.put("isShowReleasePassengerDiv", "NO");

//							data.put("paymentStatusString", ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()) + " " + "<span id='editSpan'>Edit</span>");
						data.put("paymentStatusString", ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()));

					} else {

						data.put("isShowReleasePassengerDiv", "YES");

//							data.put("paymentStatusString",
//										ProjectConstants.CARD_INVOICE_PENDING_MESSAGE + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()) + " " + "<span id='editSpan'>Edit</span>");

						data.put("paymentStatusString", ProjectConstants.CARD_INVOICE_PENDING_MESSAGE + " " + adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(invoiceModel.getFinalAmountCollected()));
					}
				}
			}

			if (invoiceModel.isRefunded()) {
				data.put("isRefunded", "YES");
			} else {
				data.put("isRefunded", "NO");
			}

		} else {

			data.put("distanceFareBeforeSpecificKm", 0.00 + "");
			data.put("distanceFareAfterSpecificKm", 0.00 + "");

			data.put("charges", adminSettings.getCurrencySymbolHtml() + tourModel.getCharges());
			data.put("total", adminSettings.getCurrencySymbolHtml() + tourModel.getTotal());
			data.put("finalAmountCollected", adminSettings.getCurrencySymbolHtml() + 0);

			data.put("finalAmountCollectedHidden", 0 + "");

			data.put("updatedAmountCollected", adminSettings.getCurrencySymbolHtml() + 0);
			data.put("remark", "");

			data.put("refundAmount", adminSettings.getCurrencySymbolHtml() + 0);

			if (tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.CARD_ID)) {
				data.put("paymentMode", ProjectConstants.CARD);
			} else if (tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.CASH_ID)) {
				data.put("paymentMode", ProjectConstants.CASH);
			} else {
				data.put("paymentMode", ProjectConstants.WALLET);
			}

			data.put("paymentStatus", ProjectConstants.NOT_AVAILABLE);

			if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) || (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER))) {

				if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER))) {
					data.put("paymentStatusString", messageForKeyAdmin("labelCancelledByPassenger"));
				} else if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER))) {
					data.put("paymentStatusString", messageForKeyAdmin("labelCancelledByDriver"));
				} else {
					data.put("paymentStatusString", ProjectConstants.NOT_AVAILABLE);
				}

			} else {

				data.put("paymentStatusString", TourUtils.getTourStatus(tourModel.getStatus()));
			}

			data.put("distance", StringUtils.valueOf(tourModel.getDistance() / adminSettings.getDistanceUnits()) + " " + adminSettings.getDistanceType());
			data.put("duration", "00:00:00");
			data.put("waitingTime", "00:00:00");

			data.put("baseFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("distanceFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("timeFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("waitingFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("surgeFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("totalFare", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("promoDiscount", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("taxAmount", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("total", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("credits", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			data.put("amountCollected", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));

			if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				data.put("cancellationCharges", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
				data.put("usedCredits", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
				data.put("taxAmount", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
				data.put("amountCollected", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOfDfNew(0.0));
			} else {
				data.put("cancellationCharges", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(0));
				data.put("usedCredits", adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(0));
			}

			data.put("isRefunded", "NO");
		}

		DriverTripRatingsModel driverRatings = DriverTripRatingsModel.getRatingDetailsByTourId(tourId);

		if (driverRatings != null) {
			data.put("driverComment", driverRatings.getNote());
			data.put("driverRate", (Math.round(driverRatings.getRate())) + "");
		} else {
			data.put("driverComment", ProjectConstants.NOT_AVAILABLE);
			data.put("driverRate", ProjectConstants.NOT_AVAILABLE);
		}

		PassengerTripRatingsModel passengerRatings = PassengerTripRatingsModel.getPassenerRatingsByTripId(tourId);

		if (passengerRatings != null) {
			data.put("passengerComment", passengerRatings.getNote());
			data.put("passengerRate", (Math.round(passengerRatings.getRate())) + "");
		} else {
			data.put("passengerComment", ProjectConstants.NOT_AVAILABLE);
			data.put("passengerRate", ProjectConstants.NOT_AVAILABLE);
		}

		if ((tourModel.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING)) || (tourModel.getBookingType().equals(ProjectConstants.ADMIN_BOOKING))) {
			data.put("bookingType", "B");
		} else {
			data.put("bookingType", "I");

			if (invoiceModel != null) {
				if (invoiceModel.isRefunded()) {
					data.put("refunded", "refunded");
				} else {
					data.put("refunded", "notRefunded");
				}
			} else {
				data.put("refunded", "refunded");
			}
		}

		String tourTypeStatus = "";

		// Rental
		if (tourModel.isRentalBooking()) {

			tourTypeStatus += "Rental- ";

			double packageDistance = tourModel.getFreeDistance();
			long packageTime = tourModel.getRentalPackageTime();

			String rentalPackage = "";

			if (packageTime == 1) {
				rentalPackage = packageTime + " Hour";
			} else {
				rentalPackage = packageTime + " Hours";
			}

			rentalPackage += ", " + StringUtils.valueOfDfNew((packageDistance / adminSettings.getDistanceUnits())) + " " + adminSettings.getDistanceType().toUpperCase();

			data.put("isRentalBooking", "true");
			data.put("rentalPackage", rentalPackage);

		} else {

			tourTypeStatus += "Taxi- ";

			data.put("isRentalBooking", "false");
			data.put("rentalPackage", "");
		}

		if (tourModel.isRideLater()) {
			tourTypeStatus += ProjectConstants.RIDE_LATER_STRING;
		} else {
			tourTypeStatus += ProjectConstants.RIDE_NOW_STRING;
		}

		if (tourModel.isAirportFixedFareApplied()) {
			tourTypeStatus += " -" + tourModel.getAirportBookingType();
		}

		data.put("tourTypeStatus", tourTypeStatus);

		data.put("takeBookingByDriver", tourModel.isTakeBookingByDriver() ? ProjectConstants.YES : ProjectConstants.NO);
		if (tourModel.getTakeBookingByDriverTime() > 0) {
			data.put("takeBookingByDriverTime", DateUtils.dbTimeStampToSesionDate(tourModel.getTakeBookingByDriverTime(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		} else {
			data.put("takeBookingByDriverTime", ProjectConstants.NOT_AVAILABLE);
		}

		return loadView(UrlConstants.JSP_URLS.ADMIN_BOOKING_DETAILS_JSP);
	}

	@Path("/refund")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response refund(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.TOUR_ID) String tourId,
		@FormParam(FieldConstants.AMOUNT_REFUNDED) String amountRefunded
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> refundStatus = new HashMap<String, String>();

		double amount = Double.parseDouble(amountRefunded);

		if (amount < 0) {
			refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("invalidRefundValue"));
			return sendDataResponse(refundStatus);
		}

		TourModel tourDetils = TourModel.getTourDetailsByTourId(tourId);

		if (tourDetils.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) || tourDetils.getBookingType().equals(ProjectConstants.ADMIN_BOOKING)) {
			refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("invalidRefundValue"));
			return sendDataResponse(refundStatus);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
		InvoiceModel invoice = InvoiceModel.getInvoiceByTourId(tourId);

		if (invoice.isRefunded()) {
			refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("alreadyRefunded"));
			return sendDataResponse(refundStatus);
		}

		double totalRefundAmount = 0.0;

		if (invoice.getUsedCredits() > 0) {
			totalRefundAmount = invoice.getCharges() + invoice.getUsedCredits();
		} else {
			totalRefundAmount = invoice.getCharges();
		}

		if (totalRefundAmount < amount) {
			refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorRefundGreaterThanTripCharges"));
			return sendDataResponse(refundStatus);
		}

		InvoiceModel.updateRefundAndTotalAmount(tourId, amount, loginSessionMap.get(LoginUtils.USER_ID));

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(tourDetils.getPassengerId());
		userProfileModel.setCredit(userProfileModel.getCredit() + amount);
		userProfileModel.updateUserCredits();

		String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("successCreditRefunded", tourDetils.getLanguage()), adminSettings.getCurrencySymbolHtml() + amount);
		MyHubNotificationUtils.sendPushNotificationToUser(tourDetils.getPassengerId(), ProjectConstants.NOTIFICATION_REFUND, notificationMessage);

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		if (adminSmsSendingModel.ispRefund()) {
			MetamorphSystemsSmsUtils.sendSmsToSingleUser(notificationMessage, tourDetils.getpPhoneCode() + tourDetils.getpPhone(), ProjectConstants.SMSConstants.SMS_REFUND_TEMPLATE_ID);
		}

		refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successrefund"));

		return sendDataResponse(refundStatus);
	}

	@Path("/send-email")
	@GET
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response sendEmail(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("tourId") String tourId,
		@QueryParam("email") String email
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> refundStatus = new HashMap<String, String>();

		TourModel tourDetils = TourModel.getTourDetailsByTourId(tourId);

		if (tourDetils == null) {
			refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorInvalidTour"));
			return sendDataResponse(refundStatus);
		}

		refundStatus.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		refundStatus.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successEmailSent"));

		InvoiceModel invoiceModel = InvoiceModel.getInvoiceByTourId(tourDetils.getTourId());

		if (tourDetils.getpEmail() != null) {
			String messasge = getInvoiceMessageNewTemplate(tourDetils, invoiceModel, tourDetils.getLanguage());
			new SendEmailThread(email, BusinessAction.messageForKeyAdmin("labelInvoiceDetials", tourDetils.getLanguage()), messasge);
		}

		return sendDataResponse(refundStatus);
	}

	@Path("/amount-collected/update")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateAmountCollected(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam("tourId") String tourId,
		@FormParam("newFinalAmtCollectedACDialog") String newFinalAmtCollectedACDialog,
		@FormParam("remarkACDialog") String remarkACDialog,
		@FormParam("isPassengerReleased") boolean isPassengerReleased
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> outPutMap = new HashMap<String, String>();

		data.put("tourId", tourId);
		data.put("newFinalAmtCollectedACDialog", newFinalAmtCollectedACDialog);
		data.put("remarkACDialog", remarkACDialog);

		if (hasErrorsForEnglish()) {
			return sendDataResponse(data);
		}

		TourModel tourDetils = TourModel.getTourDetailsByTourId(tourId);

		if (tourDetils == null) {
			outPutMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outPutMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("invalidTourDetails"));
			return sendDataResponse(outPutMap);
		}

		InvoiceModel invoiceModel = new InvoiceModel();
		invoiceModel.setTourId(tourId);
		invoiceModel.setUpdatedAmountCollected(StringUtils.doubleValueOf(newFinalAmtCollectedACDialog));
		invoiceModel.setRemark(remarkACDialog);
		invoiceModel.updateUpdatedAmountCollectWithRemark(loginSessionMap.get(LoginUtils.USER_ID));

		if (isPassengerReleased) {
			InvoiceModel.updatePaymentPaidStatus(tourId, true, true, loginSessionMap.get(LoginUtils.USER_ID));
		}

		outPutMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outPutMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successAmountCollectedUpdated"));

		return sendDataResponse(outPutMap);
	}

	public boolean hasErrorsForEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping("tourId", "Tour Id", new RequiredValidationRule());

		validator.addValidationMapping("newFinalAmtCollectedACDialog", "New Amount", new RequiredValidationRule());
		validator.addValidationMapping("newFinalAmtCollectedACDialog", "New Amount", new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("remarkACDialog", "Remark", new RequiredValidationRule());
		validator.addValidationMapping("remarkACDialog", "Remark", new MinMaxLengthValidationRule(1, 250));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADMIN_BOOKING_DETAILS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredPageExtaSupportJs() {
		List<String> requiredPageExtaSupportJs = Arrays.asList(UrlConstants.JS_URLS.RATING_JS);
		return requiredPageExtaSupportJs.toArray(new String[requiredPageExtaSupportJs.size()]);
	}

	@Override
	protected String[] requiredPageExtaSupportCss() {
		List<String> requiredPageExtaSupportCss = Arrays.asList(UrlConstants.JS_URLS.RATING_CSS);
		return requiredPageExtaSupportCss.toArray(new String[requiredPageExtaSupportCss.size()]);
	}
}