package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.CommonUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminAreaModel;
import com.webapp.models.AdminCompanyContactModel;
import com.webapp.models.AdminFaqModel;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.SurgePriceModel;
import com.webapp.models.TaxModel;
import com.webapp.models.TourModel;
import com.webapp.models.VendorAdminSettingsModel;

@Path("/api")
public class AdminSettingsAction extends BusinessApiAction {

	@Path("/admin-area")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminArea(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		List<AdminAreaModel> adminAreaLists = new ArrayList<AdminAreaModel>();

		adminAreaLists = AdminAreaModel.getAdminAreaList();

		return sendDataResponse(adminAreaLists);
	}

	@Path("/about-us")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAboutUs(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> outputMap = new HashMap<String, Object>();

		outputMap.put("aboutUs", adminSettings.getAboutUs());

		return sendDataResponse(outputMap);
	}

	@Path("/privacy-policy")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPrivacyPolicy(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> outputMap = new HashMap<String, Object>();

		outputMap.put("privacyPolicy", adminSettings.getPrivacyPolicy());

		return sendDataResponse(outputMap);
	}

	@Path("/terms-conditions")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTermsConditions(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> outputMap = new HashMap<String, Object>();

		outputMap.put("termsConditions", adminSettings.getTermsConditions());

		return sendDataResponse(outputMap);
	}

	@Path("/refund-policy")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRefundPolicy(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> outputMap = new HashMap<String, Object>();

		outputMap.put("refundPolicy", adminSettings.getRefundPolicy());

		return sendDataResponse(outputMap);
	}

	@Path("/vendor-about-us")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAboutUsVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-vendor-id") String headerVendorId
		) throws SQLException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		VendorAdminSettingsModel vendorAdminSettings = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(headerVendorId);

		String abountUs = "";

		if (vendorAdminSettings != null && vendorAdminSettings.getAboutUs() != null) {
			abountUs = vendorAdminSettings.getAboutUs();
		} else {
			abountUs = AdminSettingsModel.getAdminSettingsDetails().getAboutUs();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("aboutUs", abountUs);
		return sendDataResponse(outputMap);
	}

	@Path("/vendor-privacy-policy")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPrivacyPolicyVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-vendor-id") String headerVendorId
		) throws SQLException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		VendorAdminSettingsModel vendorAdminSettings = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(headerVendorId);
		String privacyPolicy = "";

		if (vendorAdminSettings != null && vendorAdminSettings.getPrivacyPolicy() != null) {
			privacyPolicy = vendorAdminSettings.getPrivacyPolicy();
		} else {
			privacyPolicy = AdminSettingsModel.getAdminSettingsDetails().getPrivacyPolicy();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("privacyPolicy", privacyPolicy);
		return sendDataResponse(outputMap);
	}

	@Path("/vendor-terms-conditions")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTermsConditionsVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-vendor-id") String headerVendorId
		) throws SQLException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		VendorAdminSettingsModel vendorAdminSettings = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(headerVendorId);
		String termsConditions = "";

		if (vendorAdminSettings != null && vendorAdminSettings.getTermsConditions() != null) {
			termsConditions = vendorAdminSettings.getTermsConditions();
		} else {
			termsConditions = AdminSettingsModel.getAdminSettingsDetails().getTermsConditions();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("termsConditions", termsConditions);
		return sendDataResponse(outputMap);
	}

	@Path("/vendor-refund-policy")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRefundPolicyVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-vendor-id") String headerVendorId
		) throws SQLException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		VendorAdminSettingsModel vendorAdminSettings = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(headerVendorId);
		String refundPolicy = "";

		if (vendorAdminSettings != null && vendorAdminSettings.getRefundPolicy() != null) {
			refundPolicy = vendorAdminSettings.getRefundPolicy();
		} else {
			refundPolicy = AdminSettingsModel.getAdminSettingsDetails().getRefundPolicy();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("refundPolicy", refundPolicy);
		return sendDataResponse(outputMap);
	}

	@Path("/contact-us")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminContactUs(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on
		return sendDataResponse(AdminCompanyContactModel.getAdminCompanyContactByVendorId(UserRoles.SUPER_ADMIN_USER_ID));
	}

	@Path("/vendor-contact-us")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminContactUs(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-vendor-id") String headerVendorId
		) throws SQLException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		AdminCompanyContactModel adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(headerVendorId);
		if (adminCompanyContactModel == null) {
			adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(UserRoles.SUPER_ADMIN_USER_ID);
		}

		return sendDataResponse(adminCompanyContactModel);
	}

	@Path("/car-fare-web")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFareWeb(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response 
		) throws SQLException {
	//@formatter:on

		List<CarFareModel> carTypeLists = new ArrayList<CarFareModel>();

		for (CarFareModel carfareModel : CarFareModel.getCarFare(ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {

			carfareModel.setCancellationCharges(CancellationChargeModel.getAdminCancellationCharges().getCharge());
			carTypeLists.add(carfareModel);
		}

		return sendDataResponse(carTypeLists);
	}

	@Path("/admin-faq")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminFaq(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		List<AdminFaqModel> adminFaqModel = new ArrayList<AdminFaqModel>();

		adminFaqModel = AdminFaqModel.getAdminFaqList();

		return sendDataResponse(adminFaqModel);
	}

	@POST
	@Path("/all-car-fare-web")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response allFareDetailsForBookCar(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		TourModel tourModel
		) throws IOException, SQLException {
	//@formatter:on		

		Map<String, Object> driverDetailsMap = new HashMap<String, Object>();

		String sourcePlaceLat = tourModel.getsLatitude();
		String sourcePlaceLng = tourModel.getsLongitude();
		String destinationPlaceLat = tourModel.getdLatitude();
		String destinationPlaceLng = tourModel.getdLongitude();

		if (sourcePlaceLat == null || "".equals(sourcePlaceLat) || sourcePlaceLng == null || "".equals(sourcePlaceLng)) {

			driverDetailsMap.put("type", "Failure");
			driverDetailsMap.put("message", "Invalid Pick Up Location.");
			return sendDataResponse(driverDetailsMap);
		}

		if (destinationPlaceLat == null || "".equals(destinationPlaceLat) || destinationPlaceLng == null || "".equals(destinationPlaceLng)) {

			driverDetailsMap.put("type", "Failure");
			driverDetailsMap.put("message", "Invalid Drop Off Location.");
			return sendDataResponse(driverDetailsMap);
		}

		String multicityCityRegionId = null;

		tourModel.setsLatitude(sourcePlaceLat);
		tourModel.setsLongitude(sourcePlaceLng);
		tourModel.setdLatitude(destinationPlaceLat);
		tourModel.setdLongitude(destinationPlaceLng);

		multicityCityRegionId = MultiCityAction.getMulticityRegionId(tourModel.getsLatitude(), tourModel.getsLongitude());

		if (multicityCityRegionId == null) {

			driverDetailsMap.put("type", "Failure");
			driverDetailsMap.put("message", "No services are provided within this region.");
			return sendDataResponse(driverDetailsMap);
		}

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

		MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(multicityCityRegionModel.getMulticityCountryId());

		Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(sourcePlaceLat, sourcePlaceLng, destinationPlaceLat, destinationPlaceLng, multicityCountryModel.getDistanceUnits());

		double distance = distanceMatrix.get("distanceInKm");
		double duration = distanceMatrix.get("durationInMin");

		CarFareModel firstCarFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.First_Vehicle_ID, multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), WebappPropertyUtils.DEFAULT_VENDOR_ID,
					ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel secondCarFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Second_Vehicle_ID, multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), WebappPropertyUtils.DEFAULT_VENDOR_ID,
					ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel thirdCarFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Third_Vehicle_ID, multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), WebappPropertyUtils.DEFAULT_VENDOR_ID,
					ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel fourthCarFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Fourth_Vehicle_ID, multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), WebappPropertyUtils.DEFAULT_VENDOR_ID,
					ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel fifthCarFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Fifth_Vehicle_ID, multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), WebappPropertyUtils.DEFAULT_VENDOR_ID,
					ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel sixthCarFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Seventh_Vehicle_ID, multicityCityRegionId, multicityCityRegionModel.getMulticityCountryId(), WebappPropertyUtils.DEFAULT_VENDOR_ID,
					ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		List<CarFareModel> carTypeLists = new ArrayList<CarFareModel>();

		if (firstCarFareModel != null) {
			firstCarFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			double freeDist = firstCarFareModel.getFreeDistance();
			freeDist = freeDist / multicityCountryModel.getDistanceUnits();
			firstCarFareModel.setFreeDistance((freeDist > 0 ? freeDist : 0));
			double kmToIncreaseFare = firstCarFareModel.getKmToIncreaseFare() / multicityCountryModel.getDistanceUnits();
			firstCarFareModel.setKmToIncreaseFare((kmToIncreaseFare > 0 ? kmToIncreaseFare : 0));
			firstCarFareModel.setTotalFare(getTotalFareForWebsite(firstCarFareModel, distance, duration, multicityCityRegionId));
		}

		if (secondCarFareModel != null) {

			secondCarFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			double freeDist = secondCarFareModel.getFreeDistance();
			freeDist = freeDist / multicityCountryModel.getDistanceUnits();
			secondCarFareModel.setFreeDistance((freeDist > 0 ? freeDist : 0));
			double kmToIncreaseFare = secondCarFareModel.getKmToIncreaseFare() / multicityCountryModel.getDistanceUnits();
			secondCarFareModel.setKmToIncreaseFare((kmToIncreaseFare > 0 ? kmToIncreaseFare : 0));
			secondCarFareModel.setTotalFare(getTotalFareForWebsite(secondCarFareModel, distance, duration, multicityCityRegionId));
		}

		if (thirdCarFareModel != null) {

			thirdCarFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			double freeDist = thirdCarFareModel.getFreeDistance();
			freeDist = freeDist / multicityCountryModel.getDistanceUnits();
			thirdCarFareModel.setFreeDistance((freeDist > 0 ? freeDist : 0));
			double kmToIncreaseFare = thirdCarFareModel.getKmToIncreaseFare() / multicityCountryModel.getDistanceUnits();
			thirdCarFareModel.setKmToIncreaseFare((kmToIncreaseFare > 0 ? kmToIncreaseFare : 0));
			thirdCarFareModel.setTotalFare(getTotalFareForWebsite(thirdCarFareModel, distance, duration, multicityCityRegionId));
		}

		if (fourthCarFareModel != null) {

			fourthCarFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			double freeDist = fourthCarFareModel.getFreeDistance();
			freeDist = freeDist / multicityCountryModel.getDistanceUnits();
			fourthCarFareModel.setFreeDistance((freeDist > 0 ? freeDist : 0));
			double kmToIncreaseFare = fourthCarFareModel.getKmToIncreaseFare() / multicityCountryModel.getDistanceUnits();
			fourthCarFareModel.setKmToIncreaseFare((kmToIncreaseFare > 0 ? kmToIncreaseFare : 0));
			fourthCarFareModel.setTotalFare(getTotalFareForWebsite(fourthCarFareModel, distance, duration, multicityCityRegionId));
		}

		if (fifthCarFareModel != null) {

			fifthCarFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			double freeDist = fifthCarFareModel.getFreeDistance();
			freeDist = freeDist / multicityCountryModel.getDistanceUnits();
			fifthCarFareModel.setFreeDistance((freeDist > 0 ? freeDist : 0));
			double kmToIncreaseFare = fifthCarFareModel.getKmToIncreaseFare() / multicityCountryModel.getDistanceUnits();
			fifthCarFareModel.setKmToIncreaseFare((kmToIncreaseFare > 0 ? kmToIncreaseFare : 0));
			fifthCarFareModel.setTotalFare(getTotalFareForWebsite(fifthCarFareModel, distance, duration, multicityCityRegionId));
		}

		if (sixthCarFareModel != null) {

			sixthCarFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			double freeDist = sixthCarFareModel.getFreeDistance();
			freeDist = freeDist / multicityCountryModel.getDistanceUnits();
			sixthCarFareModel.setFreeDistance((freeDist > 0 ? freeDist : 0));
			double kmToIncreaseFare = fifthCarFareModel.getKmToIncreaseFare() / multicityCountryModel.getDistanceUnits();
			sixthCarFareModel.setKmToIncreaseFare((kmToIncreaseFare > 0 ? kmToIncreaseFare : 0));
			sixthCarFareModel.setTotalFare(getTotalFareForWebsite(sixthCarFareModel, distance, duration, multicityCityRegionId));
		}

		carTypeLists.add(firstCarFareModel);
		carTypeLists.add(secondCarFareModel);
		carTypeLists.add(thirdCarFareModel);
		carTypeLists.add(fourthCarFareModel);
		carTypeLists.add(fifthCarFareModel);
		carTypeLists.add(sixthCarFareModel);

		return sendDataResponse(carTypeLists);

	}

	public static double getTotalTaxAmount(double totalFare) {

		double totalTaxAmount = 0.0;

		List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

		if (taxModelList != null) {

			for (TaxModel taxModel : taxModelList) {

				double taxAmount = ((taxModel.getTaxPercentage() / 100) * totalFare);

				taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2); // roundOff(taxAmount, true, true, RoundingMode.DOWN, 2);

				totalTaxAmount = totalTaxAmount + taxAmount;
			}
		}

		totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2); // roundOff(totalTaxAmount, true, true, RoundingMode.DOWN, 2);

		return (totalTaxAmount > 0 ? totalTaxAmount : 0);
	}

	private double getTotalFareForWebsite(CarFareModel carFareModel, double distance, double duration, String multicityCityRegionId) {

		distance = distance - (carFareModel.getFreeDistance());

		distance = (distance > 0 ? distance : 0);

		double beforeSpecificKm = 0.0;
		double afterSpecificKm = 0.0;

		if (distance > carFareModel.getKmToIncreaseFare()) {

			beforeSpecificKm = carFareModel.getKmToIncreaseFare();
			afterSpecificKm = distance - carFareModel.getKmToIncreaseFare();

		} else {
			beforeSpecificKm = distance;
		}

		beforeSpecificKm = (beforeSpecificKm > 0 ? beforeSpecificKm : 0);
		afterSpecificKm = (afterSpecificKm > 0 ? afterSpecificKm : 0);

		double distanceFare = (beforeSpecificKm * carFareModel.getPerKmFare()) + (afterSpecificKm * carFareModel.getFareAfterSpecificKm());

		double totalFareForCarType = (carFareModel.getInitialFare()) + (distanceFare) + (duration * carFareModel.getPerMinuteFare());

		String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;

		Calendar calender = Calendar.getInstance();
		calender.setTimeZone(TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE));

		int currentHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
		int currentMinute = calender.get(Calendar.MINUTE);

		long requestTimeInMilli = DateUtils.getTimeInMillisForSurge(currentHourOfDay, currentMinute, false, timeZone);

		SurgePriceModel surgePriceModel = SurgePriceModel.getSurgePriceDetailsByRequestTimeAndRegionId(requestTimeInMilli, multicityCityRegionId);

		if (surgePriceModel != null) {
			totalFareForCarType = totalFareForCarType * surgePriceModel.getSurgePrice();
		}

		totalFareForCarType = totalFareForCarType + getTotalTaxAmount(totalFareForCarType);

		return Double.parseDouble(df.format(totalFareForCarType));
	}
	
	@Path("/vendor-driver-fare")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverFareVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-vendor-id") String headerVendorId
		) throws SQLException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		VendorAdminSettingsModel vendorAdminSettings = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(headerVendorId);
		String driverFare = "";

		if (vendorAdminSettings != null && vendorAdminSettings.getDriverFare() != null) {
			driverFare = vendorAdminSettings.getDriverFare();
		} else {
			driverFare = AdminSettingsModel.getAdminSettingsDetails().getDriverFare();
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("driverFare", driverFare);
		return sendDataResponse(outputMap);
	}
}