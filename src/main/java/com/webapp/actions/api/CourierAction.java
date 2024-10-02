package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserModel;

@Path("/api/courier")
public class CourierAction extends BusinessApiAction {

	private final static String API_TYPE_VEHICLE_TYPES = "vehicle-types";
	private final static String API_TYPE_ESTIMATE_FARE = "estimate-fare";
	private final static String API_TYPE_PLACE_ORDER = "place-order";

	@Path("/{start}/{length}")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCourierHistory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		UserModel loggedInUserModel = UserModel.getUserActiveDeativeDetailsById(loggedInUserId);

		List<TourModel> courierHistoryList = TourModel.getCouriersHistoryByUserId(loggedInUserId, loggedInUserModel.getRoleId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID, start, length);

		List<Map<String, Object>> courierHistoryFinalList = new ArrayList<>();

		for (TourModel tourModel : courierHistoryList) {
			courierHistoryFinalList.add(CourierUtils.setCourierData(tourModel));
		}

		Map<String, List<Map<String, Object>>> outputMap = new HashMap<>();
		outputMap.put("courierHistory", courierHistoryFinalList);

		return sendDataResponse(outputMap);
	}

	@Path("/vehicle-types")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCourierVehicleTypes(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		TourModel tourModel
		) throws SQLException, IOException {
	//@formatter:on

		return processCourierApiAction(request, headerVendorId, sessionKeyHeader, tourModel, API_TYPE_VEHICLE_TYPES);
	}

	@Path("/estimate-fare")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCourierEstimateFare(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		TourModel tourModel
		) throws SQLException, IOException {
	//@formatter:on

		return processCourierApiAction(request, headerVendorId, sessionKeyHeader, tourModel, API_TYPE_ESTIMATE_FARE);
	}

	@Path("/place-order")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response placeCourierOrder(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		TourModel tourModel
		) throws SQLException, IOException {
	//@formatter:on

		return processCourierApiAction(request, headerVendorId, sessionKeyHeader, tourModel, API_TYPE_PLACE_ORDER);
	}

	private Response processCourierApiAction(HttpServletRequest request, String headerVendorId, String sessionKeyHeader, TourModel tourModel, String apiType) throws IOException {

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = courierModelValidation(tourModel, apiType);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(tourModel.getsLatitude(), tourModel.getsLongitude());
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		if (!MultiCityAction.validateIfDestinationLiesWithinRegion(tourModel.getdLatitude(), tourModel.getdLongitude(), multicityCityRegionId)) {
			return sendBussinessError(messageForKey("errorNoServicesToDestination", request));
		}

		if (!MultiCityAction.verifySelectedRegionAgainstAssignedRegion(headerVendorId, multicityCityRegionId)) {
			return sendBussinessError(messageForKey("errorVendorNoServiceWithinRegion", request));
		}

		List<CarTypeModel> carTypeList = MultiTenantUtils.getCarTypeListOfVendor(headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID);

		if (carTypeList.isEmpty()) {
			return sendBussinessError(messageForKey("errorNoCarTypeFound", request));
		}

		if (apiType.equalsIgnoreCase(API_TYPE_VEHICLE_TYPES)) {

			Map<String, List<CarTypeModel>> finalList = new HashMap<>();
			finalList.put("carTypeList", carTypeList);

			return sendDataResponse(finalList);
		}

		boolean isValidCar = false;

		for (CarTypeModel carTypeModel : carTypeList) {
			if (carTypeModel.getCarTypeId().equalsIgnoreCase(tourModel.getCarTypeId())) {
				isValidCar = true;
			}
		}

		if (!isValidCar) {
			return sendBussinessError(messageForKey("errorCarTypeIdNotValid", request));
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> estimateFareMap = CourierUtils.getEstimateFare(tourModel, multicityCityRegionId, headerVendorId, apiType.equalsIgnoreCase(API_TYPE_ESTIMATE_FARE), adminSettingsModel);

		if (estimateFareMap.containsKey(ProjectConstants.STATUS_TYPE)) {
			return sendBussinessError(messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
		}

		if (apiType.equalsIgnoreCase(API_TYPE_ESTIMATE_FARE)) {
			return sendDataResponse(estimateFareMap);
		}

		String tourId = CourierUtils.placeCourierOrder(loggedInuserId, tourModel, adminSettingsModel, headerVendorId);

		Map<String, Object> output = new HashMap<String, Object>();

		output.put("tourId", tourId);
		output.put("type", "SUCCESS");
		output.put("messages", messageForKey("successRequestSent", request));

		return sendDataResponse(output);
	}

	private List<String> courierModelValidation(TourModel tourModel, String apiType) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping("sourceAddress", "Pickup Location", new RequiredValidationRule());
		validator.addValidationMapping("sLatitude", "Pickup Address Lat", new RequiredValidationRule());
		validator.addValidationMapping("sLongitude", "Pickup Address Lng", new RequiredValidationRule());

		validator.addValidationMapping("destinationAddress", "Drop Location", new RequiredValidationRule());
		validator.addValidationMapping("dLatitude", "Drop Address Lat", new RequiredValidationRule());
		validator.addValidationMapping("dLongitude", "Drop Address Lng", new RequiredValidationRule());

		if (apiType.equalsIgnoreCase(API_TYPE_ESTIMATE_FARE) || apiType.equalsIgnoreCase(API_TYPE_PLACE_ORDER)) {
			validator.addValidationMapping("carTypeId", "Car Type", new RequiredValidationRule());
		}

		if (apiType.equalsIgnoreCase(API_TYPE_PLACE_ORDER)) {

			validator.addValidationMapping("rideLaterPickupTimeLogs", "Pickup date time", new RequiredValidationRule());

			validator.addValidationMapping("courierPickupAddress", "Pickup Address", new RequiredValidationRule());

			validator.addValidationMapping("courierContactPersonName", "Pickup Contact Person Name", new RequiredValidationRule());
			validator.addValidationMapping("courierContactPersonName", "Pickup Contact Person Name", new MinMaxLengthValidationRule(0, 40));

			validator.addValidationMapping("courierContactPhoneNo", "Pickup Contact Phone Number", new RequiredValidationRule());
			validator.addValidationMapping("courierContactPhoneNo", "Pickup Contact Phone Number", new MinMaxLengthValidationRule(10, 14));

			validator.addValidationMapping("courierDropAddress", "Drop Address", new RequiredValidationRule());

			validator.addValidationMapping("courierDropContactPersonName", "Drop Contact Person Name", new RequiredValidationRule());
			validator.addValidationMapping("courierDropContactPersonName", "Drop Contact Person Name", new MinMaxLengthValidationRule(0, 40));

			validator.addValidationMapping("courierDropContactPhoneNo", "Drop Contact Phone Number", new RequiredValidationRule());
			validator.addValidationMapping("courierDropContactPhoneNo", "Drop Contact Phone Number", new MinMaxLengthValidationRule(10, 14));

			validator.addValidationMapping("courierDetails", "Courier Details", new RequiredValidationRule());
			validator.addValidationMapping("courierDetails", "Courier Details", new MinMaxLengthValidationRule(0, 80));
		}

		errorMessages = validator.validate(tourModel);

		return errorMessages;
	}
}