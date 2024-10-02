package com.webapp.actions.secure.businessOwner;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.CommonUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.AdminSettingsAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.actions.api.revised.DriverAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.EstimateFareModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.SurgePriceModel;
import com.webapp.models.TaxModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UserPromoCodeModel;
import com.webapp.models.UtilizedUserPromoCodeModel;

@Path("/fare-calculator")
public class BusinessOwnerFareCalculatorAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response businessOwnerCalculateFare(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res) 
			throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String userId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(userId);

		if (userProfileModel == null) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		data.put("labelFareCalculator", messageForKeyAdmin("labelFareCalculator"));
		data.put("labelCalculateCarFare", messageForKeyAdmin("labelCalculateCarFare"));
		data.put("labelPickUpLocation", messageForKeyAdmin("labelPickUpLocation"));
		data.put("labelDropOffLocation", messageForKeyAdmin("labelDropOffLocation"));
		data.put("labelCancel", messageForKeyAdmin("labelCancel"));
		data.put("labelCalculateFare", messageForKeyAdmin("labelCalculateFare"));
		data.put("labelTotalFare", messageForKeyAdmin("labelTotalFare"));
		data.put("labelDistance", messageForKeyAdmin("labelDistance"));
		data.put("labelPickUpLocationisrequiredHidden", messageForKeyAdmin("labelPickUpLocationisrequired"));
		data.put("labelDropOffLocationisrequiredHidden", messageForKeyAdmin("labelDropOffLocationisrequired"));
		data.put("labelEnterALocationSourceHidden", messageForKeyAdmin("labelEnterALocationSource"));
		data.put("labelEnterALocationDestinationHidden", messageForKeyAdmin("labelEnterALocationDestination"));

		return loadView("/secure/business-owner/fare-calculator.jsp");
	}

	@GET
	@Path("/fare-details")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverDetails(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("sourcePlaceId") String sourcePlaceId,
			@QueryParam("destinationPlaceId") String destinationPlaceId,
			@QueryParam("carTypeId") String carTypeId,
			@QueryParam("rentalBooking") boolean rentalBooking,
			@QueryParam("rideType") String rideType,
			@QueryParam("rentalPackageId") String rentalPackageId,
			@QueryParam("markupFare") String markupFare
			) throws IOException, SQLException {
	//@formatter:on	

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String userId = userInfo.get("user_id").toString();

		UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(userId);

		boolean hasErrors = false;

		data.put("sourcePlaceId", sourcePlaceId);
		data.put("destinationPlaceId", destinationPlaceId);

		hasErrors = hasErrors(rentalBooking);

		if (rentalBooking) {

			RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(rentalPackageId, carTypeId);

			if (rentalPackageFareModel == null) {

				hasErrors = true;
			}

			if (rentalPackageId == null || "".equals(rentalPackageId)) {

				hasErrors = true;
			}
		}

		if (hasErrors) {

			data.put("type", "ERROR");
			data.put("message", "Please enter valid pickup & destination address.");

			return sendDataResponse(data);

		} else {

			Map<String, String> sourceLatLng = CommonUtils.getLatitudeLongitudeByPlaceId(sourcePlaceId);

			String sourcePlaceLat = sourceLatLng.get("latitude");
			String sourcePlaceLng = sourceLatLng.get("longitude");

			Map<String, String> destinationLatLng = new HashMap<String, String>();
			String destinationPlaceLat = "0";
			String destinationPlaceLng = "0";

			if (!rentalBooking) {

				destinationLatLng = CommonUtils.getLatitudeLongitudeByPlaceId(destinationPlaceId);
				destinationPlaceLat = destinationLatLng.get("latitude");
				destinationPlaceLng = destinationLatLng.get("longitude");
			}

			TourModel tourModel = new TourModel();

			tourModel.setsLatitude(sourcePlaceLat);
			tourModel.setsLongitude(sourcePlaceLng);
			tourModel.setdLatitude(destinationPlaceLat);
			tourModel.setdLongitude(destinationPlaceLng);
			tourModel.setPassengerId(userId);

			if (markupFare == null || "".equals(markupFare)) {
				markupFare = "0";
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			} else {
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			}

			if (Double.parseDouble(markupFare) > Double.parseDouble(ProjectConstants.MAXIMUM_MARKUP_VALUE) || Double.parseDouble(markupFare) < 0) {
				data.put("type", "Failure");
				data.put("subType", "markupFare");
				data.put("message", messageForKeyAdmin("errorMarkupFare"));
				return sendDataResponse(data);
			} else if (!UserRoles.SUPER_ADMIN_ROLE_ID.equals(userProfile.getRoleId())) {
				if (Double.parseDouble(markupFare) > userProfile.getMaximumMarkupFare()) {
					data.put("type", "Failure");
					data.put("subType", "markupFare");
					data.put("message", messageForKeyAdmin("errorMarkupFare"));
					return sendDataResponse(data);
				}
			}

			String multicityCityRegionId = MultiCityAction.getMulticityRegionId(tourModel.getsLatitude(), tourModel.getsLongitude());

			if (multicityCityRegionId == null) {
				data.put("type", "ERROR");
				data.put("message", messageForKeyAdmin("errorNoServicesInThisCountry"));
				return sendDataResponse(data);
			}

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

			// Check destination in same region
			if (!rentalBooking) {

				if (multicityCityRegionModel != null) {

					if (((destinationPlaceLat != null) && (!"".equals(destinationPlaceLat))) && ((destinationPlaceLng != null) && (!"".equals(destinationPlaceLng)))) {

						double businessDistance = MyHubUtils.distFrom(Double.parseDouble(destinationPlaceLat), Double.parseDouble(destinationPlaceLng), Double.parseDouble(multicityCityRegionModel.getRegionLatitude()),
									Double.parseDouble(multicityCityRegionModel.getRegionLongitude()));

						if (businessDistance > multicityCityRegionModel.getRegionRadius()) {

							data.put("type", "ERROR");
							data.put("message", messageForKeyAdmin("errorNoServicesToDestination"));
							return sendDataResponse(data);
						}
					}
				}
			}

			tourModel.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
			tourModel.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
			// --------------------------------------------------

			tourModel.setRentalBooking(rentalBooking);

			if (rentalBooking) {

				RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(rentalPackageId, carTypeId);

				tourModel.setRentalPackageId(rentalPackageId);

				if (rentalPackageFareModel != null) {

					tourModel.setInitialFare(rentalPackageFareModel.getBaseFare());
					tourModel.setRentalPackageTime(rentalPackageFareModel.getPackageTime());
					tourModel.setFreeDistance(rentalPackageFareModel.getPackageDistance());
					tourModel.setPerKmFare(rentalPackageFareModel.getPerKmFare());
					tourModel.setPerMinuteFare(rentalPackageFareModel.getPerMinuteFare());

					tourModel.setBookingFees(0);
					tourModel.setDiscount(0);
					tourModel.setMinimumFare(0);

				} else {

					tourModel.setInitialFare(0);
					tourModel.setRentalPackageTime(0);
					tourModel.setFreeDistance(0);
					tourModel.setPerKmFare(0);
					tourModel.setPerMinuteFare(0);

					tourModel.setBookingFees(0);
					tourModel.setDiscount(0);
					tourModel.setMinimumFare(0);
				}

			}

			Map<String, Object> driverDetailsMap = getFareDetails(carTypeId, tourModel, rideType, userProfile.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) ? userId : null);

			return sendDataResponse(driverDetailsMap);
		}
	}

	@GET
	@Path("/fare-details-bittoor")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverDetailsBittoor(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@QueryParam("sourcePlaceId") String sourcePlaceId,
		@QueryParam("destinationPlaceId") String destinationPlaceId,
		@QueryParam("carTypeId") String carTypeId,
		@QueryParam("rentalBooking") boolean rentalBooking,
		@QueryParam("rideType") String rideType,
		@QueryParam("rentalPackageId") String rentalPackageId,
		@QueryParam("markupFare") String markupFare
		) throws IOException, SQLException {
	//@formatter:on	

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			data.put("type", "ERROR");
			data.put("message", "Not a valid vendor");
			return sendDataResponse(data);
		}

		UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(headerVendorId);

		if (userProfile == null) {
			data.put("type", "ERROR");
			data.put("message", "User is not valid. Please pass a proper user.");
			return sendDataResponse(data);
		}

		if (sourcePlaceId == null || sourcePlaceId.isEmpty()) {
			data.put("type", "ERROR");
			data.put("message", "Please enter valid pickup address.");
			return sendDataResponse(data);
		}

		if (!rentalBooking && (destinationPlaceId == null || destinationPlaceId.isEmpty())) {
			data.put("type", "ERROR");
			data.put("message", "Please enter valid destination address.");
			return sendDataResponse(data);
		}

		data.put("sourcePlaceId", sourcePlaceId);
		data.put("destinationPlaceId", destinationPlaceId);

		boolean hasErrors = false;

		if (rentalBooking) {

			RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(rentalPackageId, carTypeId);

			if (rentalPackageFareModel == null) {
				hasErrors = true;
			}

			if (rentalPackageId == null || "".equals(rentalPackageId)) {
				hasErrors = true;
			}

			if (hasErrors) {
				data.put("type", "ERROR");
				data.put("message", "Rental packages are not valid.");
				return sendDataResponse(data);
			}
		}

		if (hasErrors) {

			data.put("type", "ERROR");
			data.put("message", "Please enter valid pickup & destination address.");
			return sendDataResponse(data);

		} else {

			Map<String, String> sourceLatLng = CommonUtils.getLatitudeLongitudeByPlaceId(sourcePlaceId);

			String sourcePlaceLat = sourceLatLng.get("latitude");
			String sourcePlaceLng = sourceLatLng.get("longitude");

			Map<String, String> destinationLatLng = new HashMap<String, String>();
			String destinationPlaceLat = "0";
			String destinationPlaceLng = "0";

			if (!rentalBooking) {

				destinationLatLng = CommonUtils.getLatitudeLongitudeByPlaceId(destinationPlaceId);
				destinationPlaceLat = destinationLatLng.get("latitude");
				destinationPlaceLng = destinationLatLng.get("longitude");
			}

			TourModel tourModel = new TourModel();

			tourModel.setsLatitude(sourcePlaceLat);
			tourModel.setsLongitude(sourcePlaceLng);
			tourModel.setdLatitude(destinationPlaceLat);
			tourModel.setdLongitude(destinationPlaceLng);
			tourModel.setPassengerId(headerVendorId);

			if (markupFare == null || "".equals(markupFare)) {
				markupFare = "0";
				tourModel.setMarkupFare(Double.parseDouble(markupFare));

			} else {
				tourModel.setMarkupFare(Double.parseDouble(markupFare));
			}

			if (Double.parseDouble(markupFare) > Double.parseDouble(ProjectConstants.MAXIMUM_MARKUP_VALUE) || Double.parseDouble(markupFare) < 0) {
				data.put("type", "Failure");
				data.put("subType", "markupFare");
				data.put("message", messageForKeyAdmin("errorMarkupFare", null));
				return sendDataResponse(data);
			} else if (!UserRoles.SUPER_ADMIN_ROLE_ID.equals(userProfile.getRoleId())) {
				if (Double.parseDouble(markupFare) > userProfile.getMaximumMarkupFare()) {
					data.put("type", "Failure");
					data.put("subType", "markupFare");
					data.put("message", messageForKeyAdmin("errorMarkupFare", null));
					return sendDataResponse(data);
				}
			}

			String multicityCityRegionId = MultiCityAction.getMulticityRegionId(tourModel.getsLatitude(), tourModel.getsLongitude());
			if (multicityCityRegionId == null) {
				data.put("type", "ERROR");
				data.put("message", messageForKeyAdmin("errorNoServicesInThisCountry", null));
				return sendDataResponse(data);
			}

			MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

			// Check destination in same region
			if (!rentalBooking) {

				if (multicityCityRegionModel != null) {

					if (((destinationPlaceLat != null) && (!"".equals(destinationPlaceLat))) && ((destinationPlaceLng != null) && (!"".equals(destinationPlaceLng)))) {

						double businessDistance = MyHubUtils.distFrom(Double.parseDouble(destinationPlaceLat), Double.parseDouble(destinationPlaceLng), Double.parseDouble(multicityCityRegionModel.getRegionLatitude()),
									Double.parseDouble(multicityCityRegionModel.getRegionLongitude()));

						if (businessDistance > multicityCityRegionModel.getRegionRadius()) {

							data.put("type", "ERROR");
							data.put("message", messageForKeyAdmin("errorNoServicesToDestination", null));
							return sendDataResponse(data);
						}
					}
				}
			}

			tourModel.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
			tourModel.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
			// --------------------------------------------------

			tourModel.setRentalBooking(rentalBooking);

			if (rentalBooking) {

				RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(rentalPackageId, carTypeId);

				tourModel.setRentalPackageId(rentalPackageId);

				if (rentalPackageFareModel != null) {

					tourModel.setInitialFare(rentalPackageFareModel.getBaseFare());
					tourModel.setRentalPackageTime(rentalPackageFareModel.getPackageTime());
					tourModel.setFreeDistance(rentalPackageFareModel.getPackageDistance());
					tourModel.setPerKmFare(rentalPackageFareModel.getPerKmFare());
					tourModel.setPerMinuteFare(rentalPackageFareModel.getPerMinuteFare());

					tourModel.setBookingFees(0);
					tourModel.setDiscount(0);
					tourModel.setMinimumFare(0);

				} else {

					tourModel.setInitialFare(0);
					tourModel.setRentalPackageTime(0);
					tourModel.setFreeDistance(0);
					tourModel.setPerKmFare(0);
					tourModel.setPerMinuteFare(0);

					tourModel.setBookingFees(0);
					tourModel.setDiscount(0);
					tourModel.setMinimumFare(0);
				}

			}

			Map<String, Object> driverDetailsMap = getFareDetails(carTypeId, tourModel, rideType, userProfile.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) ? headerVendorId : null);

			return sendDataResponse(driverDetailsMap);
		}
	}

	public static Map<String, Object> getFareDetails(String carTypeId, TourModel tourModel, String rideType, String vendorId) {

		Map<String, Object> driverDetailsMap = new HashMap<String, Object>();

		Map<String, Object> totalMap = new HashMap<String, Object>();

		double total = 0.0;

		List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

		double distance = 0.0;
		double distanceInMeters = 0.0;
		double duration = 0.0;
		double estimateFareWithDiscount = 0;
		double surgeFare = 0;

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		if (tourModel.isRentalBooking()) {

			distance = (tourModel.getFreeDistance() / adminSettingsModel.getDistanceUnits());
			distanceInMeters = tourModel.getFreeDistance();
			duration = tourModel.getRentalPackageTime() * 60;
			estimateFareWithDiscount = 0;

			// Calculate tax ------------
			double finalAmountForTaxCalculation = tourModel.getInitialFare();
			double totalTaxAmount = 0.0;

			if (taxModelList != null) {

				for (TaxModel taxModel : taxModelList) {

					double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

					taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2); // roundOff(taxAmount, true, true, RoundingMode.DOWN, 2);

					totalTaxAmount = totalTaxAmount + taxAmount;
				}
			}

			totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2); // roundOff(totalTaxAmount, true, true, RoundingMode.DOWN, 2);

			finalAmountForTaxCalculation = finalAmountForTaxCalculation + totalTaxAmount;

			finalAmountForTaxCalculation = roundUpDecimalValueWithDownMode(finalAmountForTaxCalculation, 2); // roundOff(charges, true, true, RoundingMode.DOWN, 2);
			// ----------------------------

			total = finalAmountForTaxCalculation;

		} else {

			Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());

			distance = distanceMatrix.get("distanceInKm");
			distanceInMeters = distanceMatrix.get("distanceInMeters");
			duration = distanceMatrix.get("durationInMin");
			estimateFareWithDiscount = 0;

			InvoiceModel invoice = new InvoiceModel();
			invoice.setDistance(distanceInMeters);
			invoice.setDuration(((duration * 60) * 1000));
			invoice.setWaitingTime(0);

			CarFareModel carFareModel = new CarFareModel();
			Map<String, Object> airportRegionMap = new HashMap<String, Object>();
			airportRegionMap = BusinessApiAction.airPortbooking(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude(), carTypeId);

			String airportBookingTypeFare = "";

			if ((boolean) airportRegionMap.get("isAirportBooking")) {

				String latAndLong = "";

				if ((boolean) airportRegionMap.get("isAirportPickUp")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + tourModel.getsLatitude() + " " + tourModel.getsLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_PICKUP;
				}
				if ((boolean) airportRegionMap.get("isAirportDrop")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + tourModel.getdLatitude() + " " + tourModel.getdLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_DROP;
				}

				carFareModel = MultiTenantUtils.getAirportCarFare(carTypeId, latAndLong, airportBookingTypeFare, vendorId);

			} else {

				carFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(carTypeId, tourModel.getMulticityCityRegionId(), tourModel.getMulticityCountryId(), vendorId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
			}
			tourModel.setMinimumFare(carFareModel.getMinimumFare());
			tourModel.setInitialFare(carFareModel.getInitialFare());
			tourModel.setBookingFees(carFareModel.getBookingFees());
			tourModel.setPerKmFare(carFareModel.getPerKmFare());
			tourModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
			tourModel.setFreeDistance(carFareModel.getFreeDistance());

			tourModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
			tourModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());

			String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;

			Calendar calender = Calendar.getInstance();

			calender.setTimeZone(TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE));

			int currentHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
			int currentMinute = calender.get(Calendar.MINUTE);

			long requestTimeInMilli = DateUtils.getTimeInMillisForSurge(currentHourOfDay, currentMinute, false, timeZone);

			SurgePriceModel surgePriceModel = null;

			if (!(boolean) airportRegionMap.get("isAirportBooking")) {
				surgePriceModel = SurgePriceModel.getSurgePriceDetailsByRequestTimeAndRegionId(requestTimeInMilli, tourModel.getMulticityCityRegionId());
			}

			driverDetailsMap.put("surgeMessage", "");
			driverDetailsMap.put("surgeType", null);

			if (surgePriceModel != null) {

				tourModel.setSurgePriceApplied(true);
				tourModel.setSurgePriceId(surgePriceModel.getSurgePriceId());
				tourModel.setSurgePrice(surgePriceModel.getSurgePrice());
				driverDetailsMap.put("surgeMessage", tourModel.getSurgePrice() + "x surge applied.");

			} else {

				tourModel.setSurgePriceApplied(false);
				tourModel.setSurgePriceId("-1");
				tourModel.setSurgePrice(1);
			}

			if (rideType.equals("1")) {

				// ----------------------------- Check City Surge
				// ------------------------------------

				EstimateFareModel estimateFareModel = new EstimateFareModel();
				estimateFareModel.setCarTypeId(carTypeId);
				estimateFareModel.setsLatitude(tourModel.getsLatitude());
				estimateFareModel.setsLongitude(tourModel.getsLongitude());

				if (!(boolean) airportRegionMap.get("isAirportBooking")) {
					CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, tourModel.getMulticityCityRegionId(), adminSettingsModel);

					if (applicableCitySurgeModel != null && applicableCitySurgeModel.getSurgeRate() > tourModel.getSurgePrice()) {

						tourModel.setSurgePriceApplied(true);
						tourModel.setSurgePriceId(applicableCitySurgeModel.getCitySurgeId());
						tourModel.setSurgePrice(applicableCitySurgeModel.getSurgeRate());
						tourModel.setSurgeRadius(applicableCitySurgeModel.getRadius());

						driverDetailsMap.put("surgeType", ProjectConstants.SURGE_TYPE_RADIUS);
						driverDetailsMap.put("surgeMessage", applicableCitySurgeModel.getSurgeRate() + "x surge applied as driver is traveling from far to pickup passenger.");
					}

					if (applicableCitySurgeModel != null && applicableCitySurgeModel.getSurgeRate() < tourModel.getSurgePrice()) {

						tourModel.setSurgeRadius(applicableCitySurgeModel.getRadius());
					}
				}
			}

			// -------------------------------------

			totalMap = DriverAction.calculateFare(invoice, tourModel, 0, 0, 0, null, false, 0);

			total = (Double) totalMap.get("total");

			surgeFare = (Double) totalMap.get("surgeFare");

			total = total + AdminSettingsAction.getTotalTaxAmount(total);
		}

		boolean isPromoCodeApplied = false;
		double discount = 0.0;
		String mode = "-1";
		String promoCodeId = null;

		if ((tourModel.getPromoCode() != null) && (!"".equals(tourModel.getPromoCode()))) {

			PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(tourModel.getPromoCode());

			if (promoCode != null) {

				long currentTime = DateUtils.nowAsGmtMillisec();

				if (currentTime >= promoCode.getStartDate() && currentTime <= promoCode.getEndDate()) {

					UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = UtilizedUserPromoCodeModel.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(tourModel.getPassengerId(), promoCode.getPromoCodeId());

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

							UserPromoCodeModel userPromoCodeModel = UserPromoCodeModel.getUserPromoCodeDetailsByPromoCodeIdAndUserId(tourModel.getPassengerId(), promoCode.getPromoCodeId());

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

		estimateFareWithDiscount = total;

		if (isPromoCodeApplied) {

			PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(tourModel.getPromoCode());

			if (estimateFareWithDiscount > 0) {

				if (mode.equals(ProjectConstants.PERCENTAGE_ID)) {

					estimateFareWithDiscount = estimateFareWithDiscount - ((estimateFareWithDiscount / 100) * discount);

				} else {
					estimateFareWithDiscount = estimateFareWithDiscount - discount;
				}
			}

			driverDetailsMap.put("promoCodeModel", promoCode);
			driverDetailsMap.put("promoCodeId", promoCodeId);
		} else {
			driverDetailsMap.put("promoCodeModel", null);
			driverDetailsMap.put("promoCodeId", null);
		}

		if (estimateFareWithDiscount < 0) {
			estimateFareWithDiscount = 0;
		}

		// Driver list for ride later
		// ------------------------------------------------------------
		Map<String, Object> inputMap = new HashMap<String, Object>();

//		String constant = "\"WGS 84\"";
//		String distance1 = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + tourModel.getsLongitude() + " " + tourModel.getsLatitude() + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
		String distance1 = GeoLocationUtil.getDistanceQuery(tourModel.getsLatitude(), tourModel.getsLongitude(), GeoLocationUtil.CAR_LOCATION);
		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + " " + tourModel.getsLatitude() + ")'),  " + adminSettingsModel.getRadiusString() + ")";

		inputMap.put("carTypeId", carTypeId);
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance1);
		inputMap.put("transmissionTypeIdList", null);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);
		driverDetailsMap.put("driverListForRideLaterOptions", DropDownUtils.getDriverListOptionsForRideLater("-1", inputMap));
		// --------------------------------------------------------------------------------------

		driverDetailsMap.put("type", "Success");
		driverDetailsMap.put("distanceInMeters", distanceInMeters);
		driverDetailsMap.put("distance", df.format(distance) + " " + adminSettingsModel.getDistanceType());
		driverDetailsMap.put("duration", duration);
		// driverDetailsMap.put("total", (adminSettingsModel.getCurrencySymbolHtml() +
		// df.format(total)));
		driverDetailsMap.put("total", (adminSettingsModel.getCurrencySymbol() + df.format(total)));
		driverDetailsMap.put("totalRaw", Double.parseDouble(df.format(total)));
		driverDetailsMap.put("estimateFareWithDiscount", (adminSettingsModel.getCurrencySymbolHtml() + estimateFareWithDiscount));
		driverDetailsMap.put("estimateFareWithDiscount", Double.parseDouble(df.format(estimateFareWithDiscount)));
		driverDetailsMap.put("estimateFareWithoutDiscount", total);
		driverDetailsMap.put("souceLat", tourModel.getsLatitude());
		driverDetailsMap.put("souceLongi", tourModel.getsLongitude());
		driverDetailsMap.put("destiLat", tourModel.getdLatitude());
		driverDetailsMap.put("destiLongi", tourModel.getdLongitude());

		driverDetailsMap.put("currencyHtml", adminSettingsModel.getCurrencySymbolHtml());
		driverDetailsMap.put("currency", adminSettingsModel.getCurrencySymbol());
		driverDetailsMap.put("mode", mode);
		driverDetailsMap.put("isPromoCodeApplied", isPromoCodeApplied);

		driverDetailsMap.put("isSurgePriceApplied", tourModel.isSurgePriceApplied());
		driverDetailsMap.put("surgePriceId", tourModel.getSurgePriceId());
		driverDetailsMap.put("surgePrice", tourModel.getSurgePrice());
		driverDetailsMap.put("surgeFare", surgeFare);
		driverDetailsMap.put("surgeRadius", tourModel.getSurgeRadius());

		return driverDetailsMap;
	}

	public static double roundUpDecimalValueWithDownMode(double value, int numberOfDigitsAfterDecimalPoint) {

		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_DOWN);
		return bigDecimal.doubleValue();
	}

	public static double roundUpDecimalValueWithUpMode(double value, int numberOfDigitsAfterDecimalPoint) {

		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_UP);
		return bigDecimal.doubleValue();
	}

	public boolean hasErrors(boolean rentalBooking) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping("sourcePlaceId", "Pickup location", new RequiredValidationRule());

		if (!rentalBooking) {

			validator.addValidationMapping("destinationPlaceId", "Destination location", new RequiredValidationRule());
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {

			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/business-owner/fare-calculator.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}