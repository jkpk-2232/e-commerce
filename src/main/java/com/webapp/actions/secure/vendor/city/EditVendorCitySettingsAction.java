package com.webapp.actions.secure.vendor.city;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.StringUtils;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.VendorCarFareModel;
import com.webapp.models.VendorCarTypeModel;

@Path("/edit-vendor-city-settings")
public class EditVendorCitySettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditVendorMulticity(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId) 
		throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

		data.put(FieldConstants.RADIUS, StringUtils.valueOf(multicityCityRegionModel.getRegionRadius()));
		data.put(FieldConstants.MULTICITY_REGION_ID, multicityCityRegionId);
		data.put(FieldConstants.AREA_RADIUS, StringUtils.valueOf(multicityCityRegionModel.getRegionRadius()));
		data.put(FieldConstants.AREA_DISPLAY_NAME, multicityCityRegionModel.getCityDisplayName());
		data.put(FieldConstants.AREA_NAME, multicityCityRegionModel.getRegionName());
		data.put(FieldConstants.AREA_PLACE_ID, multicityCityRegionModel.getRegionPlaceId());
		data.put(FieldConstants.AREA_LATITUDE, multicityCityRegionModel.getRegionLatitude());
		data.put(FieldConstants.AREA_LONGITUDE, multicityCityRegionModel.getRegionLongitude());

		String TRANSPORTATION_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;
		String COURIER_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID;
		String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;

		// --------------------------------------------------------------------------------------------

		String dataAvailableCarListTransportation = "";
		String fareAvailableCarListTransportation = "";

		List<CarFareModel> regionCarListTransportation = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, TRANSPORTATION_ID);
		List<VendorCarTypeModel> vendorCarTypeListTransportation = VendorCarTypeModel.getVendorCarTypeListByVendorId(loginSessionMap.get(LoginUtils.USER_ID), TRANSPORTATION_ID);
		List<String> listTransportation = new ArrayList<String>();
		for (VendorCarTypeModel string : vendorCarTypeListTransportation) {
			listTransportation.add(string.getCarTypeId());
		}

		for (CarFareModel carFareModel : regionCarListTransportation) {
			if (listTransportation.contains(carFareModel.getCarTypeId())) {
				dataAvailableCarListTransportation += carFareModel.getCarTypeId() + ",";
			}
		}

		List<VendorCarFareModel> carFareListTransportation = VendorCarFareModel.getVendorCarFareListByRegionIdAndVendorId(multicityCityRegionModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), TRANSPORTATION_ID);

		for (VendorCarFareModel carFareModel : carFareListTransportation) {

			String carTypeId = carFareModel.getCarTypeId();
			data.put(carTypeId + "_InitialFare_" + TRANSPORTATION_ID, StringUtils.valueOf(carFareModel.getInitialFare()));
			data.put(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID, StringUtils.valueOf(carFareModel.getPerKmFare()));
			data.put(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID, StringUtils.valueOf(carFareModel.getPerMinuteFare()));
			data.put(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID, StringUtils.valueOf((carFareModel.getFreeDistance() / adminSettingsModel.getDistanceUnits())));
			data.put(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID, StringUtils.valueOf(carFareModel.getKmToIncreaseFare() / adminSettingsModel.getDistanceUnits()));
			data.put(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID, StringUtils.valueOf(carFareModel.getFareAfterSpecificKm()));
			fareAvailableCarListTransportation += carTypeId + "_IsAvailable_" + TRANSPORTATION_ID + ",";
		}

		// --------------------------------------------------------------------------------------------

		String dataAvailableCarListCourier = "";
		String fareAvailableCarListCourier = "";

		List<CarFareModel> regionCarListCourier = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, COURIER_ID);
		List<VendorCarTypeModel> vendorCarTypeListCourier = VendorCarTypeModel.getVendorCarTypeListByVendorId(loginSessionMap.get(LoginUtils.USER_ID), COURIER_ID);
		List<String> listCourier = new ArrayList<String>();
		for (VendorCarTypeModel string : vendorCarTypeListCourier) {
			listCourier.add(string.getCarTypeId());
		}

		for (CarFareModel carFareModel : regionCarListCourier) {
			if (listCourier.contains(carFareModel.getCarTypeId())) {
				dataAvailableCarListCourier += carFareModel.getCarTypeId() + ",";
			}
		}

		List<VendorCarFareModel> carFareListCourier = VendorCarFareModel.getVendorCarFareListByRegionIdAndVendorId(multicityCityRegionModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), COURIER_ID);

		for (VendorCarFareModel carFareModel : carFareListCourier) {

			String carTypeId = carFareModel.getCarTypeId();
			data.put(carTypeId + "_InitialFare_" + COURIER_ID, StringUtils.valueOf(carFareModel.getInitialFare()));
			data.put(carTypeId + "_PerKmFare_" + COURIER_ID, StringUtils.valueOf(carFareModel.getPerKmFare()));
			data.put(carTypeId + "_PerMinuteFare_" + COURIER_ID, StringUtils.valueOf(carFareModel.getPerMinuteFare()));
			data.put(carTypeId + "_FreeDistance_" + COURIER_ID, StringUtils.valueOf((carFareModel.getFreeDistance() / adminSettingsModel.getDistanceUnits())));
			data.put(carTypeId + "_KmToIncreaseFare_" + COURIER_ID, StringUtils.valueOf(carFareModel.getKmToIncreaseFare() / adminSettingsModel.getDistanceUnits()));
			data.put(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID, StringUtils.valueOf(carFareModel.getFareAfterSpecificKm()));
			fareAvailableCarListCourier += carTypeId + "_IsAvailable_" + COURIER_ID + ",";
		}

		// --------------------------------------------------------------------------------------------

		String dataAvailableCarListEcommerce = "";
		String fareAvailableCarListEcommerce = "";

		List<CarFareModel> regionCarListEcommerce = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, ECOMMERCE_ID);
		List<VendorCarTypeModel> vendorCarTypeListEcommerce = VendorCarTypeModel.getVendorCarTypeListByVendorId(loginSessionMap.get(LoginUtils.USER_ID), ECOMMERCE_ID);
		List<String> listEcommerce = new ArrayList<String>();
		for (VendorCarTypeModel string : vendorCarTypeListEcommerce) {
			listEcommerce.add(string.getCarTypeId());
		}

		for (CarFareModel carFareModel : regionCarListEcommerce) {
			if (listEcommerce.contains(carFareModel.getCarTypeId())) {
				dataAvailableCarListEcommerce += carFareModel.getCarTypeId() + ",";
			}
		}

		List<VendorCarFareModel> carFareListEcommerce = VendorCarFareModel.getVendorCarFareListByRegionIdAndVendorId(multicityCityRegionModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), ECOMMERCE_ID);

		for (VendorCarFareModel carFareModel : carFareListEcommerce) {

			String carTypeId = carFareModel.getCarTypeId();
			data.put(carTypeId + "_InitialFare_" + ECOMMERCE_ID, StringUtils.valueOf(carFareModel.getInitialFare()));
			data.put(carTypeId + "_PerKmFare_" + ECOMMERCE_ID, StringUtils.valueOf(carFareModel.getPerKmFare()));
			data.put(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID, StringUtils.valueOf(carFareModel.getPerMinuteFare()));
			data.put(carTypeId + "_FreeDistance_" + ECOMMERCE_ID, StringUtils.valueOf((carFareModel.getFreeDistance() / adminSettingsModel.getDistanceUnits())));
			data.put(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID, StringUtils.valueOf(carFareModel.getKmToIncreaseFare() / adminSettingsModel.getDistanceUnits()));
			data.put(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID, StringUtils.valueOf(carFareModel.getFareAfterSpecificKm()));
			fareAvailableCarListEcommerce += carTypeId + "_IsAvailable_" + ECOMMERCE_ID + ",";
		}

		// --------------------------------------------------------------------------------------------

		if (dataAvailableCarListTransportation.length() > 0) {
			data.put("dataAvailableCarListTransportation", dataAvailableCarListTransportation.substring(0, dataAvailableCarListTransportation.length() - 1));
		}

		if (dataAvailableCarListCourier.length() > 0) {
			data.put("dataAvailableCarListCourier", dataAvailableCarListCourier.substring(0, dataAvailableCarListCourier.length() - 1));
		}

		if (dataAvailableCarListEcommerce.length() > 0) {
			data.put("dataAvailableCarListEcommerce", dataAvailableCarListEcommerce.substring(0, dataAvailableCarListEcommerce.length() - 1));
		}

		if (fareAvailableCarListTransportation.length() > 0) {
			data.put("fareAvailableCarListTransportation", fareAvailableCarListTransportation.substring(0, fareAvailableCarListTransportation.length() - 1));
		}

		if (fareAvailableCarListCourier.length() > 0) {
			data.put("fareAvailableCarListCourier", fareAvailableCarListCourier.substring(0, fareAvailableCarListCourier.length() - 1));
		}

		if (fareAvailableCarListEcommerce.length() > 0) {
			data.put("fareAvailableCarListEcommerce", fareAvailableCarListEcommerce.substring(0, fareAvailableCarListEcommerce.length() - 1));
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_CITY_SETTINGS_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_CITY_SETTINGS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postEditVendorMulticity(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.QUERY_STRING) String queryString,
		@FormParam(FieldConstants.CAR_AVAILABLE_LIST) String carAvailableList,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		@FormParam(FieldConstants.RADIUS) String radius,
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

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

		data.put(FieldConstants.RADIUS, StringUtils.valueOf(multicityCityRegionModel.getRegionRadius()));
		data.put(FieldConstants.MULTICITY_REGION_ID, multicityCityRegionId);
		data.put(FieldConstants.AREA_RADIUS, StringUtils.valueOf(multicityCityRegionModel.getRegionRadius()));
		data.put(FieldConstants.AREA_DISPLAY_NAME, multicityCityRegionModel.getCityDisplayName());
		data.put(FieldConstants.AREA_NAME, multicityCityRegionModel.getRegionName());
		data.put(FieldConstants.AREA_PLACE_ID, multicityCityRegionModel.getRegionPlaceId());
		data.put(FieldConstants.AREA_LATITUDE, multicityCityRegionModel.getRegionLatitude());
		data.put(FieldConstants.AREA_LONGITUDE, multicityCityRegionModel.getRegionLongitude());

		data.put("queryString", queryString);

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
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_CITY_SETTINGS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_CITY_SETTINGS_JSP);
		}

		VendorCarFareModel vendorCarFareModelTemp = new VendorCarFareModel();
		vendorCarFareModelTemp.setMulticityCityRegionId(multicityCityRegionId);
		vendorCarFareModelTemp.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));

		vendorCarFareModelTemp.setServiceTypeId(TRANSPORTATION_ID);
		vendorCarFareModelTemp.deleteExistingVendorCarFare(TRANSPORTATION_ID);

		vendorCarFareModelTemp.setServiceTypeId(COURIER_ID);
		vendorCarFareModelTemp.deleteExistingVendorCarFare(COURIER_ID);

		vendorCarFareModelTemp.setServiceTypeId(ECOMMERCE_ID);
		vendorCarFareModelTemp.deleteExistingVendorCarFare(ECOMMERCE_ID);

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + TRANSPORTATION_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			VendorCarFareModel previousCarFare = VendorCarFareModel.getVendorCarFareDetailsByRegionCountryAndId(carTypeId, multicityCityRegionId, ProjectConstants.DEFAULT_COUNTRY_ID, loginSessionMap.get(LoginUtils.USER_ID), TRANSPORTATION_ID);
			VendorCarFareModel vendorCarFareModel = new VendorCarFareModel();

			vendorCarFareModel.setCarTypeId(carTypeId);
			vendorCarFareModel.setMulticityCityRegionId(multicityCityRegionId);
			vendorCarFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			vendorCarFareModel.setInitialFare(Double.parseDouble(inputMap.get(carTypeId + "_InitialFare_" + TRANSPORTATION_ID).toString().trim()));
			vendorCarFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare_" + TRANSPORTATION_ID).toString().trim()));
			vendorCarFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare_" + TRANSPORTATION_ID).toString().trim()));
			vendorCarFareModel.setFreeDistance((Double.parseDouble(inputMap.get(carTypeId + "_FreeDistance_" + TRANSPORTATION_ID).toString().trim())) * adminSettingsModel.getDistanceUnits());
			vendorCarFareModel.setKmToIncreaseFare((Double.parseDouble(inputMap.get(carTypeId + "_KmToIncreaseFare_" + TRANSPORTATION_ID).toString())) * adminSettingsModel.getDistanceUnits());
			vendorCarFareModel.setFareAfterSpecificKm(Double.parseDouble(inputMap.get(carTypeId + "_FareAfterSpecificKm_" + TRANSPORTATION_ID).toString().trim()));
			vendorCarFareModel.setServiceTypeId(TRANSPORTATION_ID);

			if (previousCarFare != null) {
				vendorCarFareModel.setVendorCarFareId(previousCarFare.getVendorCarFareId());
				vendorCarFareModel.updateVendorCarFareForMultiCity(loginSessionMap.get(LoginUtils.USER_ID), TRANSPORTATION_ID);
			} else {
				vendorCarFareModel.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));
				vendorCarFareModel.addVendorCarFare(loginSessionMap.get(LoginUtils.USER_ID), TRANSPORTATION_ID);
			}
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + COURIER_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			VendorCarFareModel previousCarFare = VendorCarFareModel.getVendorCarFareDetailsByRegionCountryAndId(carTypeId, multicityCityRegionId, ProjectConstants.DEFAULT_COUNTRY_ID, loginSessionMap.get(LoginUtils.USER_ID), COURIER_ID);
			VendorCarFareModel vendorCarFareModel = new VendorCarFareModel();

			vendorCarFareModel.setCarTypeId(carTypeId);
			vendorCarFareModel.setMulticityCityRegionId(multicityCityRegionId);
			vendorCarFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			vendorCarFareModel.setInitialFare(Double.parseDouble(inputMap.get(carTypeId + "_InitialFare_" + COURIER_ID).toString().trim()));
			vendorCarFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare_" + COURIER_ID).toString().trim()));
			vendorCarFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare_" + COURIER_ID).toString().trim()));
			vendorCarFareModel.setFreeDistance((Double.parseDouble(inputMap.get(carTypeId + "_FreeDistance_" + COURIER_ID).toString().trim())) * adminSettingsModel.getDistanceUnits());
			vendorCarFareModel.setKmToIncreaseFare((Double.parseDouble(inputMap.get(carTypeId + "_KmToIncreaseFare_" + COURIER_ID).toString())) * adminSettingsModel.getDistanceUnits());
			vendorCarFareModel.setFareAfterSpecificKm(Double.parseDouble(inputMap.get(carTypeId + "_FareAfterSpecificKm_" + COURIER_ID).toString().trim()));
			vendorCarFareModel.setServiceTypeId(COURIER_ID);

			if (previousCarFare != null) {
				vendorCarFareModel.setVendorCarFareId(previousCarFare.getVendorCarFareId());
				vendorCarFareModel.updateVendorCarFareForMultiCity(loginSessionMap.get(LoginUtils.USER_ID), COURIER_ID);
			} else {
				vendorCarFareModel.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));
				vendorCarFareModel.addVendorCarFare(loginSessionMap.get(LoginUtils.USER_ID), COURIER_ID);
			}
		}

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable_" + ECOMMERCE_ID, "");

			if (carTypeId.equalsIgnoreCase(string)) {
				continue;
			}

			VendorCarFareModel previousCarFare = VendorCarFareModel.getVendorCarFareDetailsByRegionCountryAndId(carTypeId, multicityCityRegionId, ProjectConstants.DEFAULT_COUNTRY_ID, loginSessionMap.get(LoginUtils.USER_ID), ECOMMERCE_ID);
			VendorCarFareModel vendorCarFareModel = new VendorCarFareModel();

			vendorCarFareModel.setCarTypeId(carTypeId);
			vendorCarFareModel.setMulticityCityRegionId(multicityCityRegionId);
			vendorCarFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			vendorCarFareModel.setInitialFare(Double.parseDouble(inputMap.get(carTypeId + "_InitialFare_" + ECOMMERCE_ID).toString().trim()));
			vendorCarFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare_" + ECOMMERCE_ID).toString().trim()));
			vendorCarFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare_" + ECOMMERCE_ID).toString().trim()));
			vendorCarFareModel.setFreeDistance((Double.parseDouble(inputMap.get(carTypeId + "_FreeDistance_" + ECOMMERCE_ID).toString().trim())) * adminSettingsModel.getDistanceUnits());
			vendorCarFareModel.setKmToIncreaseFare((Double.parseDouble(inputMap.get(carTypeId + "_KmToIncreaseFare_" + ECOMMERCE_ID).toString())) * adminSettingsModel.getDistanceUnits());
			vendorCarFareModel.setFareAfterSpecificKm(Double.parseDouble(inputMap.get(carTypeId + "_FareAfterSpecificKm_" + ECOMMERCE_ID).toString().trim()));
			vendorCarFareModel.setServiceTypeId(ECOMMERCE_ID);

			if (previousCarFare != null) {
				vendorCarFareModel.setVendorCarFareId(previousCarFare.getVendorCarFareId());
				vendorCarFareModel.updateVendorCarFareForMultiCity(loginSessionMap.get(LoginUtils.USER_ID), ECOMMERCE_ID);
			} else {
				vendorCarFareModel.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));
				vendorCarFareModel.addVendorCarFare(loginSessionMap.get(LoginUtils.USER_ID), ECOMMERCE_ID);
			}
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_CITY_SETTINGS_URL);
	}

	public boolean hasErrors(String[] availableCarList) {

		boolean hasErrors = false;
		Validator validator = new Validator();

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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_VENDOR_CITY_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}