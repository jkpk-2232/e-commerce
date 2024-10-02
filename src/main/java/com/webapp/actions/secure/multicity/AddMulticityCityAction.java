package com.webapp.actions.secure.multicity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.MulticityCityRegionModel;

@Path("/add-multicity-city")
public class AddMulticityCityAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddMulticity(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String radiusOptions = DropDownUtils.getAdminAreaRadius(ProjectConstants.DEFAULT_OPERATING_RADIUS);
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		data.put(FieldConstants.IS_EDIT_CITY, false + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_MULTICITY_URL);
		return loadView(UrlConstants.JSP_URLS.ADD_MULTICITY_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddMulticity(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.QUERY_STRING) String queryString,
		@FormParam(FieldConstants.CAR_AVAILABLE_LIST) String carAvailableList,
		@FormParam(FieldConstants.AREA_DISPLAY_NAME) String areaDisplayName,
		@FormParam(FieldConstants.AREA_NAME) String areaName,
		@FormParam(FieldConstants.AREA_PLACE_ID) String areaPlaceId,
		@FormParam(FieldConstants.AREA_LATITUDE) String areaLatitude,
		@FormParam(FieldConstants.AREA_LONGITUDE) String areaLongitude,
		@FormParam(FieldConstants.AREA_RADIUS) String areaRadius
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String radiusOptions = DropDownUtils.getAdminAreaRadius(areaRadius);
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		data.put(FieldConstants.AREA_RADIUS, areaRadius);
		data.put(FieldConstants.AREA_DISPLAY_NAME, areaDisplayName);
		data.put(FieldConstants.AREA_NAME, areaName);
		data.put(FieldConstants.AREA_PLACE_ID, areaPlaceId);
		data.put(FieldConstants.AREA_LATITUDE, areaLatitude);
		data.put(FieldConstants.AREA_LONGITUDE, areaLongitude);
		data.put(FieldConstants.QUERY_STRING, queryString);
		data.put(FieldConstants.IS_EDIT_CITY, false + "");

		logger.info("\n\n\n\tqueryString\t" + queryString);

		Map<String, Object> inputMap = MultiTenantUtils.parseInputParameters(queryString);

		logger.info("\n\n\n\tinputMap\t" + inputMap);
		logger.info("\n\n\n\tcarAvailableList\t" + carAvailableList);

		String[] availableCarList = MyHubUtils.splitStringByCommaArray(carAvailableList);
		String dataAvailableCarList = "";

		data.put("errorDataAvailableCarList", carAvailableList);

		String TRANSPORTATION_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;
		String COURIER_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID;
		String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + TRANSPORTATION_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			data.put(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, inputMap.get(carTypeId + "_InitialFare_" + TRANSPORTATION_ID) != null ? inputMap.get(carTypeId + "_InitialFare_" + TRANSPORTATION_ID).toString() : "");
			data.put(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, inputMap.get(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID) != null ? inputMap.get(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID).toString() : "");
			data.put(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, inputMap.get(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID) != null ? inputMap.get(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID).toString() : "");
			data.put(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, inputMap.get(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID) != null ? inputMap.get(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID).toString() : "");
			data.put(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, inputMap.get(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID) != null ? inputMap.get(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID).toString() : "");
			data.put(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, inputMap.get(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID) != null ? inputMap.get(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID).toString() : "");

			dataAvailableCarList += carTypeId + ",";
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + COURIER_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			data.put(carTypeId + "_InitialFare_" + COURIER_ID, inputMap.get(carTypeId + "_InitialFare_" + COURIER_ID) != null ? inputMap.get(carTypeId + "_InitialFare_" + COURIER_ID).toString() : "");
			data.put(carTypeId + "_PerKmFare_" + COURIER_ID, inputMap.get(carTypeId + "_PerKmFare_" + COURIER_ID) != null ? inputMap.get(carTypeId + "_PerKmFare_" + COURIER_ID).toString() : "");
			data.put(carTypeId + "_PerMinuteFare_" + COURIER_ID, inputMap.get(carTypeId + "_PerMinuteFare_" + COURIER_ID) != null ? inputMap.get(carTypeId + "_PerMinuteFare_" + COURIER_ID).toString() : "");
			data.put(carTypeId + "_FreeDistance_" + COURIER_ID, inputMap.get(carTypeId + "_FreeDistance_" + COURIER_ID) != null ? inputMap.get(carTypeId + "_FreeDistance_" + COURIER_ID).toString() : "");
			data.put(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, inputMap.get(carTypeId + "_KmToIncreaseFare_" + COURIER_ID) != null ? inputMap.get(carTypeId + "_KmToIncreaseFare_" + COURIER_ID).toString() : "");
			data.put(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, inputMap.get(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID) != null ? inputMap.get(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID).toString() : "");

			dataAvailableCarList += carTypeId + ",";
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + ECOMMERCE_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			data.put(carTypeId + "_InitialFare_" + ECOMMERCE_ID, inputMap.get(carTypeId + "_InitialFare_" + ECOMMERCE_ID) != null ? inputMap.get(carTypeId + "_InitialFare_" + ECOMMERCE_ID).toString() : "");
			data.put(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, inputMap.get(carTypeId + "_PerKmFare_" + ECOMMERCE_ID) != null ? inputMap.get(carTypeId + "_PerKmFare_" + ECOMMERCE_ID).toString() : "");
			data.put(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, inputMap.get(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID) != null ? inputMap.get(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID).toString() : "");
			data.put(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, inputMap.get(carTypeId + "_FreeDistance_" + ECOMMERCE_ID) != null ? inputMap.get(carTypeId + "_FreeDistance_" + ECOMMERCE_ID).toString() : "");
			data.put(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, inputMap.get(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID) != null ? inputMap.get(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID).toString() : "");
			data.put(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, inputMap.get(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID) != null ? inputMap.get(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID).toString() : "");

			dataAvailableCarList += carTypeId + ",";
		}

		if (dataAvailableCarList.length() > 0) {
			data.put("carAvailableList", dataAvailableCarList.substring(0, dataAvailableCarList.length() - 1));
		}

		if (hasErrors(availableCarList)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_MULTICITY_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_MULTICITY_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		MulticityCityRegionModel multicityCityRegionModel = new MulticityCityRegionModel();

		multicityCityRegionModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		multicityCityRegionModel.setCityDisplayName(areaDisplayName);
		multicityCityRegionModel.setCityOriginalName(areaDisplayName);
		multicityCityRegionModel.setRegionName(areaName);
		multicityCityRegionModel.setRegionPlaceId(areaPlaceId);
		multicityCityRegionModel.setRegionLatitude(areaLatitude);
		multicityCityRegionModel.setRegionLongitude(areaLongitude);
		multicityCityRegionModel.setRegionRadius(Long.parseLong(areaRadius));

		String multicityCityRegionId = multicityCityRegionModel.addMulticityCityRegion(loginSessionMap.get(LoginUtils.USER_ID));

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + TRANSPORTATION_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			CarFareModel carFareModel = new CarFareModel();

			carFareModel.setCarTypeId(carTypeId);
			carFareModel.setMulticityCityRegionId(multicityCityRegionId);
			carFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			carFareModel.setInitialFare(Double.parseDouble(inputMap.get(carTypeId + "_InitialFare_" + TRANSPORTATION_ID).toString().trim()));
			carFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID).toString().trim()));
			carFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID).toString().trim()));
			carFareModel.setFreeDistance((Double.parseDouble(inputMap.get(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID).toString().trim())) * adminSettingsModel.getDistanceUnits());
			carFareModel.setKmToIncreaseFare((Double.parseDouble(inputMap.get(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID).toString())) * adminSettingsModel.getDistanceUnits());
			carFareModel.setFareAfterSpecificKm(Double.parseDouble(inputMap.get(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID).toString().trim()));

			carFareModel.addCarFare(loginSessionMap.get(LoginUtils.USER_ID), TRANSPORTATION_ID);
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + COURIER_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			CarFareModel carFareModel = new CarFareModel();

			carFareModel.setCarTypeId(carTypeId);
			carFareModel.setMulticityCityRegionId(multicityCityRegionId);
			carFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			carFareModel.setInitialFare(Double.parseDouble(inputMap.get(carTypeId + "_InitialFare_" + COURIER_ID).toString().trim()));
			carFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare_" + COURIER_ID).toString().trim()));
			carFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare_" + COURIER_ID).toString().trim()));
			carFareModel.setFreeDistance((Double.parseDouble(inputMap.get(carTypeId + "_FreeDistance_" + COURIER_ID).toString().trim())) * adminSettingsModel.getDistanceUnits());
			carFareModel.setKmToIncreaseFare((Double.parseDouble(inputMap.get(carTypeId + "_KmToIncreaseFare_" + COURIER_ID).toString())) * adminSettingsModel.getDistanceUnits());
			carFareModel.setFareAfterSpecificKm(Double.parseDouble(inputMap.get(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID).toString().trim()));

			carFareModel.addCarFare(loginSessionMap.get(LoginUtils.USER_ID), COURIER_ID);
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + ECOMMERCE_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			CarFareModel carFareModel = new CarFareModel();

			carFareModel.setCarTypeId(carTypeId);
			carFareModel.setMulticityCityRegionId(multicityCityRegionId);
			carFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			carFareModel.setInitialFare(Double.parseDouble(inputMap.get(carTypeId + "_InitialFare_" + ECOMMERCE_ID).toString().trim()));
			carFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare_" + ECOMMERCE_ID).toString().trim()));
			carFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID).toString().trim()));
			carFareModel.setFreeDistance((Double.parseDouble(inputMap.get(carTypeId + "_FreeDistance_" + ECOMMERCE_ID).toString().trim())) * adminSettingsModel.getDistanceUnits());
			carFareModel.setKmToIncreaseFare((Double.parseDouble(inputMap.get(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID).toString())) * adminSettingsModel.getDistanceUnits());
			carFareModel.setFareAfterSpecificKm(Double.parseDouble(inputMap.get(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID).toString().trim()));

			carFareModel.addCarFare(loginSessionMap.get(LoginUtils.USER_ID), ECOMMERCE_ID);
		}

		MultiTenantUtils.assignDynamicCityFareToAllVendors(multicityCityRegionId, loginSessionMap.get(LoginUtils.USER_ID), false, TRANSPORTATION_ID);
		MultiTenantUtils.assignDynamicCityFareToAllVendors(multicityCityRegionId, loginSessionMap.get(LoginUtils.USER_ID), false, COURIER_ID);
		MultiTenantUtils.assignDynamicCityFareToAllVendors(multicityCityRegionId, loginSessionMap.get(LoginUtils.USER_ID), false, ECOMMERCE_ID);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_MULTICITY_URL);
	}

	public boolean hasErrors(String[] availableCarList) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.AREA_DISPLAY_NAME, messageForKeyAdmin("labelCityName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.AREA_DISPLAY_NAME, messageForKeyAdmin("labelCityName"), new MinMaxLengthValidationRule(1, 400));

		validator.addValidationMapping(FieldConstants.AREA_NAME, messageForKeyAdmin("labelRegionName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.AREA_NAME, messageForKeyAdmin("labelRegionName"), new MinMaxLengthValidationRule(1, 400));

		String TRANSPORTATION_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;
		String COURIER_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID;
		String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + TRANSPORTATION_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			validator.addValidationMapping(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + COURIER_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			validator.addValidationMapping(carTypeId + "_InitialFare_" + COURIER_ID, messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_InitialFare_" + COURIER_ID, messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_InitialFare_" + COURIER_ID, messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerKmFare_" + COURIER_ID, messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare_" + COURIER_ID, messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare_" + COURIER_ID, messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + COURIER_ID, messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + COURIER_ID, messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + COURIER_ID, messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_FreeDistance_" + COURIER_ID, messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_FreeDistance_" + COURIER_ID, messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_FreeDistance_" + COURIER_ID, messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + ECOMMERCE_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			validator.addValidationMapping(carTypeId + "_InitialFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_InitialFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_InitialFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_MULTICITY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}