package com.webapp.actions.secure.airport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
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
import com.utils.CommonUtils;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AirportRegionModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.TempAirportRegionModel;

@Path("/add-airport-region")
public class AddAirportRegionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddAirportRegions(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String multicityCityRegionIdOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_AIRPORT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddAirportRegions(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		@FormParam(FieldConstants.ALIAS_NAME) String aliasName,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.LAT_LONG) String latlong,
		@FormParam(FieldConstants.AREA_LATITUDE) String areaLatitude,
		@FormParam(FieldConstants.AREA_LONGITUDE) String areaLongitude,
		
		@FormParam("initialFareFirstVehicleAirportPickUp") String initialFareFirstVehicleAirportPickUp,
		@FormParam("initialFareSecondVehicleAirportPickUp") String initialFareSecondVehicleAirportPickUp,
		@FormParam("initialFareThirdVehicleAirportPickUp") String initialFareThirdVehicleAirportPickUp,
		@FormParam("initialFareFourthVehicleAirportPickUp") String initialFareFourthVehicleAirportPickUp,
		
		@FormParam("perKmFareFirstVehicleAirportPickUp") String perKmFareFirstVehicleAirportPickUp,
		@FormParam("perKmFareSecondVehicleAirportPickUp") String perKmFareSecondVehicleAirportPickUp,
		@FormParam("perKmFareThirdVehicleAirportPickUp") String perKmFareThirdVehicleAirportPickUp,
		@FormParam("perKmFareFourthVehicleAirportPickUp") String perKmFareFourthVehicleAirportPickUp,
		
		@FormParam("perMinuteFareFirstVehicleAirportPickUp") String perMinuteFareFirstVehicleAirportPickUp,
		@FormParam("perMinuteFareSecondVehicleAirportPickUp") String perMinuteFareSecondVehicleAirportPickUp,
		@FormParam("perMinuteFareThirdVehicleAirportPickUp") String perMinuteFareThirdVehicleAirportPickUp,
		@FormParam("perMinuteFareFourthVehicleAirportPickUp") String perMinuteFareFourthVehicleAirportPickUp,
		
		@FormParam("freeDistanceFirstVehicleAirportPickUp") String freeDistanceFirstVehicleAirportPickUp,
		@FormParam("freeDistanceSecondVehicleAirportPickUp") String freeDistanceSecondVehicleAirportPickUp,
		@FormParam("freeDistanceThirdVehicleAirportPickUp") String freeDistanceThirdVehicleAirportPickUp,
		@FormParam("freeDistanceFourthVehicleAirportPickUp") String freeDistanceFourthVehicleAirportPickUp,
		
		@FormParam("kmToIncreaseFareFirstVehicleAirportPickUp") String kmToIncreaseFareFirstVehicleAirportPickUp,
		@FormParam("kmToIncreaseFareSecondVehicleAirportPickUp") String kmToIncreaseFareSecondVehicleAirportPickUp,
		@FormParam("kmToIncreaseFareThirdVehicleAirportPickUp") String kmToIncreaseFareThirdVehicleAirportPickUp,
		@FormParam("kmToIncreaseFareFourthVehicleAirportPickUp") String kmToIncreaseFareFourthVehicleAirportPickUp,
		
		@FormParam("fareAfterSpecificKmFirstVehicleAirportPickUp") String fareAfterSpecificKmFirstVehicleAirportPickUp,
		@FormParam("fareAfterSpecificKmSecondVehicleAirportPickUp") String fareAfterSpecificKmSecondVehicleAirportPickUp,
		@FormParam("fareAfterSpecificKmThirdVehicleAirportPickUp") String fareAfterSpecificKmThirdVehicleAirportPickUp,
		@FormParam("fareAfterSpecificKmFourthVehicleAirportPickUp") String fareAfterSpecificKmFourthVehicleAirportPickUp,
		
		@FormParam("initialFareFirstVehicleAirportDrop") String initialFareFirstVehicleAirportDrop,
		@FormParam("initialFareSecondVehicleAirportDrop") String initialFareSecondVehicleAirportDrop,
		@FormParam("initialFareThirdVehicleAirportDrop") String initialFareThirdVehicleAirportDrop,
		@FormParam("initialFareFourthVehicleAirportDrop") String initialFareFourthVehicleAirportDrop,
		
		@FormParam("perKmFareFirstVehicleAirportDrop") String perKmFareFirstVehicleAirportDrop,
		@FormParam("perKmFareSecondVehicleAirportDrop") String perKmFareSecondVehicleAirportDrop,
		@FormParam("perKmFareThirdVehicleAirportDrop") String perKmFareThirdVehicleAirportDrop,
		@FormParam("perKmFareFourthVehicleAirportDrop") String perKmFareFourthVehicleAirportDrop,
		
		@FormParam("perMinuteFareFirstVehicleAirportDrop") String perMinuteFareFirstVehicleAirportDrop,
		@FormParam("perMinuteFareSecondVehicleAirportDrop") String perMinuteFareSecondVehicleAirportDrop,
		@FormParam("perMinuteFareThirdVehicleAirportDrop") String perMinuteFareThirdVehicleAirportDrop,
		@FormParam("perMinuteFareFourthVehicleAirportDrop") String perMinuteFareFourthVehicleAirportDrop,
		
		@FormParam("freeDistanceFirstVehicleAirportDrop") String freeDistanceFirstVehicleAirportDrop,
		@FormParam("freeDistanceSecondVehicleAirportDrop") String freeDistanceSecondVehicleAirportDrop,
		@FormParam("freeDistanceThirdVehicleAirportDrop") String freeDistanceThirdVehicleAirportDrop,
		@FormParam("freeDistanceFourthVehicleAirportDrop") String freeDistanceFourthVehicleAirportDrop,
		
		@FormParam("kmToIncreaseFareFirstVehicleAirportDrop") String kmToIncreaseFareFirstVehicleAirportDrop,
		@FormParam("kmToIncreaseFareSecondVehicleAirportDrop") String kmToIncreaseFareSecondVehicleAirportDrop,
		@FormParam("kmToIncreaseFareThirdVehicleAirportDrop") String kmToIncreaseFareThirdVehicleAirportDrop,
		@FormParam("kmToIncreaseFareFourthVehicleAirportDrop") String kmToIncreaseFareFourthVehicleAirportDrop,
		
		@FormParam("fareAfterSpecificKmFirstVehicleAirportDrop") String fareAfterSpecificKmFirstVehicleAirportDrop,
		@FormParam("fareAfterSpecificKmSecondVehicleAirportDrop") String fareAfterSpecificKmSecondVehicleAirportDrop,
		@FormParam("fareAfterSpecificKmThirdVehicleAirportDrop") String fareAfterSpecificKmThirdVehicleAirportDrop,
		@FormParam("fareAfterSpecificKmFourthVehicleAirportDrop") String fareAfterSpecificKmFourthVehicleAirportDrop
		) throws ServletException, IOException, SQLException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String multicityCityRegionIdOptions = DropDownUtils.getUserAccessWiseRegionList(multicityCityRegionId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		data.put(FieldConstants.LAT_LONG, latlong);
		data.put(FieldConstants.MULTICITY_REGION_ID, multicityCityRegionId);
		data.put(FieldConstants.ALIAS_NAME, aliasName);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.AREA_LATITUDE, areaLatitude);
		data.put(FieldConstants.AREA_LONGITUDE, areaLongitude);

		data.put("initialFareFirstVehicleAirportPickUp", initialFareFirstVehicleAirportPickUp);
		data.put("initialFareSecondVehicleAirportPickUp", initialFareSecondVehicleAirportPickUp);
		data.put("initialFareThirdVehicleAirportPickUp", initialFareThirdVehicleAirportPickUp);
		data.put("initialFareFourthVehicleAirportPickUp", initialFareFourthVehicleAirportPickUp);

		data.put("perKmFareFirstVehicleAirportPickUp", perKmFareFirstVehicleAirportPickUp);
		data.put("perKmFareSecondVehicleAirportPickUp", perKmFareSecondVehicleAirportPickUp);
		data.put("perKmFareThirdVehicleAirportPickUp", perKmFareThirdVehicleAirportPickUp);
		data.put("perKmFareFourthVehicleAirportPickUp", perKmFareFourthVehicleAirportPickUp);

		data.put("perMinuteFareFirstVehicleAirportPickUp", perMinuteFareFirstVehicleAirportPickUp);
		data.put("perMinuteFareSecondVehicleAirportPickUp", perMinuteFareSecondVehicleAirportPickUp);
		data.put("perMinuteFareThirdVehicleAirportPickUp", perMinuteFareThirdVehicleAirportPickUp);
		data.put("perMinuteFareFourthVehicleAirportPickUp", perMinuteFareFourthVehicleAirportPickUp);

		data.put("freeDistanceFirstVehicleAirportPickUp", freeDistanceFirstVehicleAirportPickUp);
		data.put("freeDistanceSecondVehicleAirportPickUp", freeDistanceSecondVehicleAirportPickUp);
		data.put("freeDistanceThirdVehicleAirportPickUp", freeDistanceThirdVehicleAirportPickUp);
		data.put("freeDistanceFourthVehicleAirportPickUp", freeDistanceFourthVehicleAirportPickUp);

		data.put("kmToIncreaseFareFirstVehicleAirportPickUp", kmToIncreaseFareFirstVehicleAirportPickUp);
		data.put("kmToIncreaseFareSecondVehicleAirportPickUp", kmToIncreaseFareSecondVehicleAirportPickUp);
		data.put("kmToIncreaseFareThirdVehicleAirportPickUp", kmToIncreaseFareThirdVehicleAirportPickUp);
		data.put("kmToIncreaseFareFourthVehicleAirportPickUp", kmToIncreaseFareFourthVehicleAirportPickUp);

		data.put("fareAfterSpecificKmFirstVehicleAirportPickUp", fareAfterSpecificKmFirstVehicleAirportPickUp);
		data.put("fareAfterSpecificKmSecondVehicleAirportPickUp", fareAfterSpecificKmSecondVehicleAirportPickUp);
		data.put("fareAfterSpecificKmThirdVehicleAirportPickUp", fareAfterSpecificKmThirdVehicleAirportPickUp);
		data.put("fareAfterSpecificKmFourthVehicleAirportPickUp", fareAfterSpecificKmFourthVehicleAirportPickUp);

		data.put("initialFareFirstVehicleAirportDrop", initialFareFirstVehicleAirportDrop);
		data.put("initialFareSecondVehicleAirportDrop", initialFareSecondVehicleAirportDrop);
		data.put("initialFareThirdVehicleAirportDrop", initialFareThirdVehicleAirportDrop);
		data.put("initialFareFourthVehicleAirportDrop", initialFareFourthVehicleAirportDrop);

		data.put("perKmFareFirstVehicleAirportDrop", perKmFareFirstVehicleAirportDrop);
		data.put("perKmFareSecondVehicleAirportDrop", perKmFareSecondVehicleAirportDrop);
		data.put("perKmFareThirdVehicleAirportDrop", perKmFareThirdVehicleAirportDrop);
		data.put("perKmFareFourthVehicleAirportDrop", perKmFareFourthVehicleAirportDrop);

		data.put("perMinuteFareFirstVehicleAirportDrop", perMinuteFareFirstVehicleAirportDrop);
		data.put("perMinuteFareSecondVehicleAirportDrop", perMinuteFareSecondVehicleAirportDrop);
		data.put("perMinuteFareThirdVehicleAirportDrop", perMinuteFareThirdVehicleAirportDrop);
		data.put("perMinuteFareFourthVehicleAirportDrop", perMinuteFareFourthVehicleAirportDrop);

		data.put("freeDistanceFirstVehicleAirportDrop", freeDistanceFirstVehicleAirportDrop);
		data.put("freeDistanceSecondVehicleAirportDrop", freeDistanceSecondVehicleAirportDrop);
		data.put("freeDistanceThirdVehicleAirportDrop", freeDistanceThirdVehicleAirportDrop);
		data.put("freeDistanceFourthVehicleAirportDrop", freeDistanceFourthVehicleAirportDrop);

		data.put("kmToIncreaseFareFirstVehicleAirportDrop", kmToIncreaseFareFirstVehicleAirportDrop);
		data.put("kmToIncreaseFareSecondVehicleAirportDrop", kmToIncreaseFareSecondVehicleAirportDrop);
		data.put("kmToIncreaseFareThirdVehicleAirportDrop", kmToIncreaseFareThirdVehicleAirportDrop);
		data.put("kmToIncreaseFareFourthVehicleAirportDrop", kmToIncreaseFareFourthVehicleAirportDrop);

		data.put("fareAfterSpecificKmFirstVehicleAirportDrop", fareAfterSpecificKmFirstVehicleAirportDrop);
		data.put("fareAfterSpecificKmSecondVehicleAirportDrop", fareAfterSpecificKmSecondVehicleAirportDrop);
		data.put("fareAfterSpecificKmThirdVehicleAirportDrop", fareAfterSpecificKmThirdVehicleAirportDrop);
		data.put("fareAfterSpecificKmFourthVehicleAirportDrop", fareAfterSpecificKmFourthVehicleAirportDrop);

		if (hasErrorsEnglish()) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_AIRPORT_JSP);
		}

		MulticityCityRegionModel multicityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(multicityRegionModel.getRegionLatitude(), multicityRegionModel.getRegionLongitude(), areaLatitude, areaLongitude);

		Map<String, String> airportregionFare = new HashMap<String, String>();

		airportregionFare.put("initialFareFirstVehicleAirportPickUp", initialFareFirstVehicleAirportPickUp);
		airportregionFare.put("initialFareSecondVehicleAirportPickUp", initialFareSecondVehicleAirportPickUp);
		airportregionFare.put("initialFareThirdVehicleAirportPickUp", initialFareThirdVehicleAirportPickUp);
		airportregionFare.put("initialFareFourthVehicleAirportPickUp", initialFareFourthVehicleAirportPickUp);

		airportregionFare.put("perKmFareFirstVehicleAirportPickUp", perKmFareFirstVehicleAirportPickUp);
		airportregionFare.put("perKmFareSecondVehicleAirportPickUp", perKmFareSecondVehicleAirportPickUp);
		airportregionFare.put("perKmFareThirdVehicleAirportPickUp", perKmFareThirdVehicleAirportPickUp);
		airportregionFare.put("perKmFareFourthVehicleAirportPickUp", perKmFareFourthVehicleAirportPickUp);

		airportregionFare.put("perMinuteFareFirstVehicleAirportPickUp", perMinuteFareFirstVehicleAirportPickUp);
		airportregionFare.put("perMinuteFareSecondVehicleAirportPickUp", perMinuteFareSecondVehicleAirportPickUp);
		airportregionFare.put("perMinuteFareThirdVehicleAirportPickUp", perMinuteFareThirdVehicleAirportPickUp);
		airportregionFare.put("perMinuteFareFourthVehicleAirportPickUp", perMinuteFareFourthVehicleAirportPickUp);

		airportregionFare.put("freeDistanceFirstVehicleAirportPickUp", freeDistanceFirstVehicleAirportPickUp);
		airportregionFare.put("freeDistanceSecondVehicleAirportPickUp", freeDistanceSecondVehicleAirportPickUp);
		airportregionFare.put("freeDistanceThirdVehicleAirportPickUp", freeDistanceThirdVehicleAirportPickUp);
		airportregionFare.put("freeDistanceFourthVehicleAirportPickUp", freeDistanceFourthVehicleAirportPickUp);

		airportregionFare.put("kmToIncreaseFareFirstVehicleAirportPickUp", kmToIncreaseFareFirstVehicleAirportPickUp);
		airportregionFare.put("kmToIncreaseFareSecondVehicleAirportPickUp", kmToIncreaseFareSecondVehicleAirportPickUp);
		airportregionFare.put("kmToIncreaseFareThirdVehicleAirportPickUp", kmToIncreaseFareThirdVehicleAirportPickUp);
		airportregionFare.put("kmToIncreaseFareFourthVehicleAirportPickUp", kmToIncreaseFareFourthVehicleAirportPickUp);

		airportregionFare.put("fareAfterSpecificKmFirstVehicleAirportPickUp", fareAfterSpecificKmFirstVehicleAirportPickUp);
		airportregionFare.put("fareAfterSpecificKmSecondVehicleAirportPickUp", fareAfterSpecificKmSecondVehicleAirportPickUp);
		airportregionFare.put("fareAfterSpecificKmThirdVehicleAirportPickUp", fareAfterSpecificKmThirdVehicleAirportPickUp);
		airportregionFare.put("fareAfterSpecificKmFourthVehicleAirportPickUp", fareAfterSpecificKmFourthVehicleAirportPickUp);

		airportregionFare.put("initialFareFirstVehicleAirportDrop", initialFareFirstVehicleAirportDrop);
		airportregionFare.put("initialFareSecondVehicleAirportDrop", initialFareSecondVehicleAirportDrop);
		airportregionFare.put("initialFareThirdVehicleAirportDrop", initialFareThirdVehicleAirportDrop);
		airportregionFare.put("initialFareFourthVehicleAirportDrop", initialFareFourthVehicleAirportDrop);

		airportregionFare.put("perKmFareFirstVehicleAirportDrop", perKmFareFirstVehicleAirportDrop);
		airportregionFare.put("perKmFareSecondVehicleAirportDrop", perKmFareSecondVehicleAirportDrop);
		airportregionFare.put("perKmFareThirdVehicleAirportDrop", perKmFareThirdVehicleAirportDrop);
		airportregionFare.put("perKmFareFourthVehicleAirportDrop", perKmFareFourthVehicleAirportDrop);

		airportregionFare.put("perMinuteFareFirstVehicleAirportDrop", perMinuteFareFirstVehicleAirportDrop);
		airportregionFare.put("perMinuteFareSecondVehicleAirportDrop", perMinuteFareSecondVehicleAirportDrop);
		airportregionFare.put("perMinuteFareThirdVehicleAirportDrop", perMinuteFareThirdVehicleAirportDrop);
		airportregionFare.put("perMinuteFareFourthVehicleAirportDrop", perMinuteFareFourthVehicleAirportDrop);

		airportregionFare.put("freeDistanceFirstVehicleAirportDrop", freeDistanceFirstVehicleAirportDrop);
		airportregionFare.put("freeDistanceSecondVehicleAirportDrop", freeDistanceSecondVehicleAirportDrop);
		airportregionFare.put("freeDistanceThirdVehicleAirportDrop", freeDistanceThirdVehicleAirportDrop);
		airportregionFare.put("freeDistanceFourthVehicleAirportDrop", freeDistanceFourthVehicleAirportDrop);

		airportregionFare.put("kmToIncreaseFareFirstVehicleAirportDrop", kmToIncreaseFareFirstVehicleAirportDrop);
		airportregionFare.put("kmToIncreaseFareSecondVehicleAirportDrop", kmToIncreaseFareSecondVehicleAirportDrop);
		airportregionFare.put("kmToIncreaseFareThirdVehicleAirportDrop", kmToIncreaseFareThirdVehicleAirportDrop);
		airportregionFare.put("kmToIncreaseFareFourthVehicleAirportDrop", kmToIncreaseFareFourthVehicleAirportDrop);

		airportregionFare.put("fareAfterSpecificKmFirstVehicleAirportDrop", fareAfterSpecificKmFirstVehicleAirportDrop);
		airportregionFare.put("fareAfterSpecificKmSecondVehicleAirportDrop", fareAfterSpecificKmSecondVehicleAirportDrop);
		airportregionFare.put("fareAfterSpecificKmThirdVehicleAirportDrop", fareAfterSpecificKmThirdVehicleAirportDrop);
		airportregionFare.put("fareAfterSpecificKmFourthVehicleAirportDrop", fareAfterSpecificKmFourthVehicleAirportDrop);

		double distanceInMeters = distanceMatrix.get("distanceInMeters");

		String[] longi = MyHubUtils.splitStringByCommaArray(latlong);
		String first = longi[0];

		String latitudelongitude = "'POLYGON((" + latlong + first + "))'";

		AirportRegionModel airportRegionModel = new AirportRegionModel();
		String airportRegionId = UUIDGenerator.generateUUID();
		airportRegionModel.setAirportRegionId(airportRegionId);
		airportRegionModel.setMulticityCityRegionId(multicityCityRegionId);
		airportRegionModel.setAliasName(aliasName);
		airportRegionModel.setAddress(address);
		airportRegionModel.setAreaPolygon(latitudelongitude);
		airportRegionModel.setRegionLatitude(areaLatitude);
		airportRegionModel.setRegionLongitude(areaLongitude);

		TempAirportRegionModel tempAirportRegionModel = new TempAirportRegionModel();
		tempAirportRegionModel.setAreaPolygon(latitudelongitude);
		tempAirportRegionModel.setRegionLatitude(areaLatitude);
		tempAirportRegionModel.setRegionLongitude(areaLongitude);
		TempAirportRegionModel tempAirportRegion = new TempAirportRegionModel();

		if (!(StringUtils.validString(areaLatitude) && StringUtils.validString(areaLongitude))) {
			data.put(FieldConstants.ADDRESS_ERROR, messageForKeyAdmin("errorInvalidAirportLocation"));
			data.put(FieldConstants.LAT_LONG, "");
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_AIRPORT_JSP);
		}

		tempAirportRegionModel.addTempAirportRegion();
		tempAirportRegion = TempAirportRegionModel.checkLatLongInAirportRegion(areaLatitude, areaLongitude);

		boolean isAirportAdded = false;

		if (distanceInMeters / 1000 < multicityRegionModel.getRegionRadius()) {

			if (tempAirportRegion != null) {
				airportRegionModel.addAirportRegion(loginSessionMap.get(LoginUtils.USER_ID));
				tempAirportRegionModel.deleteTempAirportRegion();
				addAirportFare(airportregionFare, adminSettingsModel, loginSessionMap.get(LoginUtils.USER_ID), airportRegionId);
				isAirportAdded = true;

			} else {
				tempAirportRegionModel.deleteTempAirportRegion();
				data.put(FieldConstants.ADDRESS_ERROR, messageForKeyAdmin("errorInvalidPolygon"));
				data.put(FieldConstants.LAT_LONG, "");
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
				return loadView(UrlConstants.JSP_URLS.ADD_AIRPORT_JSP);
			}

		} else {

			tempAirportRegionModel.deleteTempAirportRegion();
			data.put(FieldConstants.ADDRESS_ERROR, messageForKeyAdmin("errorAirportAddressError"));
			data.put(FieldConstants.LAT_LONG, "");

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_AIRPORT_JSP);
		}

		if (isAirportAdded) {
			// Assign the newly created airport and it's car fare to all the vendors
			MultiTenantUtils.assignAirportRegionAndCarFareToAllVendors(airportRegionId);
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
	}

	@Path("/load-map")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@formatter:off
	public Response loadCityMap(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		Map<String, Object> outputMap = new HashMap<String, Object>();

		MulticityCityRegionModel multicityRegionModelById = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

		outputMap.put("cityLatitude", multicityRegionModelById.getRegionLatitude());
		outputMap.put("cityLogitude", multicityRegionModelById.getRegionLongitude());
		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		return sendDataResponse(outputMap);
	}

	public boolean addAirportFare(Map<String, String> airportregionFare, AdminSettingsModel adminSettingsModel, String loggedInUserId, String airportRegionId) {

		CarFareModel carFareFirstVehicle = new CarFareModel();

		carFareFirstVehicle.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
		carFareFirstVehicle.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportPickUp").trim()));
		carFareFirstVehicle.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportPickUp").trim()));
		carFareFirstVehicle.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportPickUp").trim()));
		carFareFirstVehicle.setDriverPayablePercentage(0);
		carFareFirstVehicle.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicle.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicle.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportPickUp")));
		carFareFirstVehicle.setAirportRegionId(airportRegionId);
		carFareFirstVehicle.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareFirstVehicle.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareSecondVehicle = new CarFareModel();

		carFareSecondVehicle.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
		carFareSecondVehicle.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicle.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicle.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicle.setDriverPayablePercentage(0);
		carFareSecondVehicle.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceSecondVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicle.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareSecondVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicle.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmSecondVehicleAirportPickUp")));
		carFareSecondVehicle.setAirportRegionId(airportRegionId);
		carFareSecondVehicle.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareSecondVehicle.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareThirdVehicle = new CarFareModel();

		carFareThirdVehicle.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
		carFareThirdVehicle.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicle.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicle.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicle.setDriverPayablePercentage(0);
		carFareThirdVehicle.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceThirdVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicle.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareThirdVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicle.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmThirdVehicleAirportPickUp")));
		carFareThirdVehicle.setAirportRegionId(airportRegionId);
		carFareThirdVehicle.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareThirdVehicle.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareFourthVehicle = new CarFareModel();

		carFareFourthVehicle.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
		carFareFourthVehicle.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicle.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicle.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicle.setDriverPayablePercentage(0);
		carFareFourthVehicle.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFourthVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicle.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFourthVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicle.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFourthVehicleAirportPickUp")));
		carFareFourthVehicle.setAirportRegionId(airportRegionId);
		carFareFourthVehicle.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareFourthVehicle.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareFirstVehicleAirportDrop = new CarFareModel();

		carFareFirstVehicleAirportDrop.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
		carFareFirstVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportDrop").trim()));
		carFareFirstVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportDrop").trim()));
		carFareFirstVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportDrop").trim()));
		carFareFirstVehicleAirportDrop.setDriverPayablePercentage(0);
		carFareFirstVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportDrop")));
		carFareFirstVehicleAirportDrop.setAirportRegionId(airportRegionId);
		carFareFirstVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

		carFareFirstVehicleAirportDrop.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareSecondVehicleAirportDrop = new CarFareModel();

		carFareSecondVehicleAirportDrop.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
		carFareSecondVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareSecondVehicleAirportDrop").trim()));
		carFareSecondVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareSecondVehicleAirportDrop").trim()));
		carFareSecondVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareSecondVehicleAirportDrop").trim()));
		carFareSecondVehicleAirportDrop.setDriverPayablePercentage(0);
		carFareSecondVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceSecondVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareSecondVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmSecondVehicleAirportDrop")));
		carFareSecondVehicleAirportDrop.setAirportRegionId(airportRegionId);
		carFareSecondVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

		carFareSecondVehicleAirportDrop.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareThirdVehicleAirportDrop = new CarFareModel();

		carFareThirdVehicleAirportDrop.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
		carFareThirdVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAirportDrop.setDriverPayablePercentage(0);
		carFareThirdVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceThirdVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareThirdVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmThirdVehicleAirportDrop")));
		carFareThirdVehicleAirportDrop.setAirportRegionId(airportRegionId);
		carFareThirdVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

		carFareThirdVehicleAirportDrop.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareFourthVehicleAirportDrop = new CarFareModel();

		carFareFourthVehicleAirportDrop.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
		carFareFourthVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAirportDrop.setDriverPayablePercentage(0);
		carFareFourthVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFourthVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFourthVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFourthVehicleAirportDrop")));
		carFareFourthVehicleAirportDrop.setAirportRegionId(airportRegionId);
		carFareFourthVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

		carFareFourthVehicleAirportDrop.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		return false;
	}

	public boolean hasErrorsEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ALIAS_NAME, messageForKeyAdmin("labelAliasName"), new RequiredValidationRule());

		validator.addValidationMapping("initialFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceFirstVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceFirstVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceFirstVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceSecondVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceSecondVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceSecondVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceThirdVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceThirdVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceThirdVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceFourthVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceFourthVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceFourthVehicleAirportPickUp", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFirstVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareSecondVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareThirdVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFourthVehicleAirportPickUp", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmFirstVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFirstVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFirstVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmSecondVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmSecondVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmSecondVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmThirdVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmThirdVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmThirdVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmFourthVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFourthVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFourthVehicleAirportPickUp", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareFirstVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareFirstVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareFirstVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareSecondVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareSecondVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareSecondVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareThirdVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareThirdVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareThirdVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("initialFareFourthVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new RequiredValidationRule());
		validator.addValidationMapping("initialFareFourthVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new DecimalValidationRule());
		validator.addValidationMapping("initialFareFourthVehicleAirportDrop", messageForKeyAdmin("labelInitialFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareFirstVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareFirstVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareFirstVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareSecondVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareSecondVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareSecondVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareThirdVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareThirdVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareThirdVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perKmFareFourthVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new RequiredValidationRule());
		validator.addValidationMapping("perKmFareFourthVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new DecimalValidationRule());
		validator.addValidationMapping("perKmFareFourthVehicleAirportDrop", messageForKeyAdmin("labelPerKmFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareFirstVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareFirstVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareFirstVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareSecondVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareSecondVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareSecondVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareThirdVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareThirdVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareThirdVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("perMinuteFareFourthVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new RequiredValidationRule());
		validator.addValidationMapping("perMinuteFareFourthVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new DecimalValidationRule());
		validator.addValidationMapping("perMinuteFareFourthVehicleAirportDrop", messageForKeyAdmin("labelPerMinFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceFirstVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceFirstVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceFirstVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceSecondVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceSecondVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceSecondVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceThirdVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceThirdVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceThirdVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("freeDistanceFourthVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new RequiredValidationRule());
		validator.addValidationMapping("freeDistanceFourthVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new DecimalValidationRule());
		validator.addValidationMapping("freeDistanceFourthVehicleAirportDrop", messageForKeyAdmin("labelFreeDistance"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareFirstVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFirstVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFirstVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareSecondVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareSecondVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareSecondVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareThirdVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareThirdVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareThirdVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("kmToIncreaseFareFourthVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new RequiredValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFourthVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new DecimalValidationRule());
		validator.addValidationMapping("kmToIncreaseFareFourthVehicleAirportDrop", messageForKeyAdmin("labelKmToIncreaseFare"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmFirstVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFirstVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFirstVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmSecondVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmSecondVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmSecondVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmThirdVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmThirdVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmThirdVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping("fareAfterSpecificKmFourthVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new RequiredValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFourthVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new DecimalValidationRule());
		validator.addValidationMapping("fareAfterSpecificKmFourthVehicleAirportDrop", messageForKeyAdmin("labelFareAfterSpecificKm"), new MinMaxValueValidationRule(0, 100000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_AIRPORT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}