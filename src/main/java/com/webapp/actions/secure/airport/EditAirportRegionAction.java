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

@Path("/edit-airport-region")
public class EditAirportRegionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditAirportRegions(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.AIRPORT_REGION_ID) String airportRegionId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionDetailsById(airportRegionId);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		CarFareModel carFareFirstVehicle = CarFareModel.getCarFareDetailsByAirportRegionId(ProjectConstants.Fifth_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel carFareSecondVehicle = CarFareModel.getCarFareDetailsByAirportRegionId(ProjectConstants.Second_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel carFareThirdVehicle = CarFareModel.getCarFareDetailsByAirportRegionId(ProjectConstants.Third_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel carFareFourthVehicle = CarFareModel.getCarFareDetailsByAirportRegionId(ProjectConstants.Fourth_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carAirportDropFareFirstVehicle = CarFareModel.getAirportDropCarFareDetailsByAirportRegionId(ProjectConstants.Fifth_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel carAirportDropFareSecondVehicle = CarFareModel.getAirportDropCarFareDetailsByAirportRegionId(ProjectConstants.Second_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel carAirportDropFareThirdVehicle = CarFareModel.getAirportDropCarFareDetailsByAirportRegionId(ProjectConstants.Third_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		CarFareModel carAirportDropFareFourthVehicle = CarFareModel.getAirportDropCarFareDetailsByAirportRegionId(ProjectConstants.Fourth_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		if (carFareFirstVehicle == null) {

			data.put("initialFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("perKmFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("perMinuteFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("freeDistanceFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("driverPayablePercentageFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("kmToIncreaseFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("fareAfterSpecificKmFirstVehicleAirportPickUp", StringUtils.valueOf(0));

		} else {

			data.put("initialFareFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getInitialFare()));
			data.put("perKmFareFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getPerKmFare()));
			data.put("perMinuteFareFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getPerMinuteFare()));
			data.put("freeDistanceFirstVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareFirstVehicle.getFreeDistance(), adminSettingsModel));
			data.put("driverPayablePercentageFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getDriverPayablePercentage()));
			data.put("kmToIncreaseFareFirstVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareFirstVehicle.getKmToIncreaseFare(), adminSettingsModel));
			data.put("fareAfterSpecificKmFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getFareAfterSpecificKm()));
		}

		data.put("initialFareSecondVehicleAirportPickUp", StringUtils.valueOf(carFareSecondVehicle.getInitialFare()));
		data.put("initialFareThirdVehicleAirportPickUp", StringUtils.valueOf(carFareThirdVehicle.getInitialFare()));
		data.put("initialFareFourthVehicleAirportPickUp", StringUtils.valueOf(carFareFourthVehicle.getInitialFare()));

		data.put("perKmFareSecondVehicleAirportPickUp", StringUtils.valueOf(carFareSecondVehicle.getPerKmFare()));
		data.put("perKmFareThirdVehicleAirportPickUp", StringUtils.valueOf(carFareThirdVehicle.getPerKmFare()));
		data.put("perKmFareFourthVehicleAirportPickUp", StringUtils.valueOf(carFareFourthVehicle.getPerKmFare()));

		data.put("perMinuteFareSecondVehicleAirportPickUp", StringUtils.valueOf(carFareSecondVehicle.getPerMinuteFare()));
		data.put("perMinuteFareThirdVehicleAirportPickUp", StringUtils.valueOf(carFareThirdVehicle.getPerMinuteFare()));
		data.put("perMinuteFareFourthVehicleAirportPickUp", StringUtils.valueOf(carFareFourthVehicle.getPerMinuteFare()));

		data.put("freeDistanceSecondVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareSecondVehicle.getFreeDistance(), adminSettingsModel));
		data.put("freeDistanceThirdVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareThirdVehicle.getFreeDistance(), adminSettingsModel));
		data.put("freeDistanceFourthVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareFourthVehicle.getFreeDistance(), adminSettingsModel));

		data.put("driverPayablePercentageSecondVehicleAirportPickUp", StringUtils.valueOf(carFareSecondVehicle.getDriverPayablePercentage()));
		data.put("driverPayablePercentageThirdVehicleAirportPickUp", StringUtils.valueOf(carFareThirdVehicle.getDriverPayablePercentage()));
		data.put("driverPayablePercentageFourthVehicleAirportPickUp", StringUtils.valueOf(carFareFourthVehicle.getDriverPayablePercentage()));

		data.put("kmToIncreaseFareSecondVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareSecondVehicle.getKmToIncreaseFare(), adminSettingsModel));
		data.put("kmToIncreaseFareThirdVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareThirdVehicle.getKmToIncreaseFare(), adminSettingsModel));
		data.put("kmToIncreaseFareFourthVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareFourthVehicle.getKmToIncreaseFare(), adminSettingsModel));

		data.put("fareAfterSpecificKmSecondVehicleAirportPickUp", StringUtils.valueOf(carFareSecondVehicle.getFareAfterSpecificKm()));
		data.put("fareAfterSpecificKmThirdVehicleAirportPickUp", StringUtils.valueOf(carFareThirdVehicle.getFareAfterSpecificKm()));
		data.put("fareAfterSpecificKmFourthVehicleAirportPickUp", StringUtils.valueOf(carFareFourthVehicle.getFareAfterSpecificKm()));

		if (carFareFirstVehicle == null) {

			data.put("initialFareFirstVehicleAirportDrop", StringUtils.valueOf(0));
			data.put("perKmFareFirstVehicleAirportDrop", StringUtils.valueOf(0));
			data.put("perMinuteFareFirstVehicleAirportDrop", StringUtils.valueOf(0));
			data.put("freeDistanceFirstVehicleAirportDrop", StringUtils.valueOf(0));
			data.put("driverPayablePercentageFirstVehicleAirportDrop", StringUtils.valueOf(0));
			data.put("kmToIncreaseFareFirstVehicleAirportDrop", StringUtils.valueOf(0));
			data.put("fareAfterSpecificKmFirstVehicleAirportDrop", StringUtils.valueOf(0));

		} else {

			data.put("initialFareFirstVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFirstVehicle.getInitialFare()));
			data.put("perKmFareFirstVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFirstVehicle.getPerKmFare()));
			data.put("perMinuteFareFirstVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFirstVehicle.getPerMinuteFare()));
			data.put("freeDistanceFirstVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareFirstVehicle.getFreeDistance(), adminSettingsModel));
			data.put("driverPayablePercentageFirstVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFirstVehicle.getDriverPayablePercentage()));
			data.put("kmToIncreaseFareFirstVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareFirstVehicle.getKmToIncreaseFare(), adminSettingsModel));
			data.put("fareAfterSpecificKmFirstVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFirstVehicle.getFareAfterSpecificKm()));
		}

		data.put("initialFareSecondVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareSecondVehicle.getInitialFare()));
		data.put("initialFareThirdVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareThirdVehicle.getInitialFare()));
		data.put("initialFareFourthVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFourthVehicle.getInitialFare()));

		data.put("perKmFareSecondVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareSecondVehicle.getPerKmFare()));
		data.put("perKmFareThirdVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareThirdVehicle.getPerKmFare()));
		data.put("perKmFareFourthVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFourthVehicle.getPerKmFare()));

		data.put("perMinuteFareSecondVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareSecondVehicle.getPerMinuteFare()));
		data.put("perMinuteFareThirdVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareThirdVehicle.getPerMinuteFare()));
		data.put("perMinuteFareFourthVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFourthVehicle.getPerMinuteFare()));

		data.put("freeDistanceSecondVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareSecondVehicle.getFreeDistance(), adminSettingsModel));
		data.put("freeDistanceThirdVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareThirdVehicle.getFreeDistance(), adminSettingsModel));
		data.put("freeDistanceFourthVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareFourthVehicle.getFreeDistance(), adminSettingsModel));

		data.put("driverPayablePercentageSecondVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareSecondVehicle.getDriverPayablePercentage()));
		data.put("driverPayablePercentageThirdVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareThirdVehicle.getDriverPayablePercentage()));
		data.put("driverPayablePercentageFourthVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFourthVehicle.getDriverPayablePercentage()));

		data.put("kmToIncreaseFareSecondVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareSecondVehicle.getKmToIncreaseFare(), adminSettingsModel));
		data.put("kmToIncreaseFareThirdVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareThirdVehicle.getKmToIncreaseFare(), adminSettingsModel));
		data.put("kmToIncreaseFareFourthVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareFourthVehicle.getKmToIncreaseFare(), adminSettingsModel));

		data.put("fareAfterSpecificKmSecondVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareSecondVehicle.getFareAfterSpecificKm()));
		data.put("fareAfterSpecificKmThirdVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareThirdVehicle.getFareAfterSpecificKm()));
		data.put("fareAfterSpecificKmFourthVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFourthVehicle.getFareAfterSpecificKm()));

		String multicityCityRegionIdOptions = DropDownUtils.getUserAccessWiseRegionList(airportRegionModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		data.put(FieldConstants.AIRPORT_REGION_ID, airportRegionModel.getAirportRegionId());
		data.put(FieldConstants.ADDRESS, airportRegionModel.getAddress());
		data.put(FieldConstants.ALIAS_NAME, airportRegionModel.getAliasName());
		data.put(FieldConstants.LAT_LONG, airportRegionModel.getAreaPolygon());
		data.put(FieldConstants.AREA_LATITUDE, airportRegionModel.getRegionLatitude());
		data.put(FieldConstants.AREA_LONGITUDE, airportRegionModel.getRegionLongitude());

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_AIRPORT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addAirportRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.AIRPORT_REGION_ID) String airportRegionId,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		@FormParam(FieldConstants.ALIAS_NAME) String aliasName,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.LAT_LONG) String latlong,
		@FormParam(FieldConstants.AREA_LATITUDE) String areaLatitude,
		@FormParam(FieldConstants.AREA_LONGITUDE) String areaLongitude,
		
		@FormParam("initialFareFirstVehicleAirportPickUp") String initialFareFirstVehicle,
		@FormParam("initialFareSecondVehicleAirportPickUp") String initialFareSecondVehicle,
		@FormParam("initialFareThirdVehicleAirportPickUp") String initialFareThirdVehicle,
		@FormParam("initialFareFourthVehicleAirportPickUp") String initialFareFourthVehicle,
		
		@FormParam("perKmFareFirstVehicleAirportPickUp") String perKmFareFirstVehicle,
		@FormParam("perKmFareSecondVehicleAirportPickUp") String perKmFareSecondVehicle,
		@FormParam("perKmFareThirdVehicleAirportPickUp") String perKmFareThirdVehicle,
		@FormParam("perKmFareFourthVehicleAirportPickUp") String perKmFareFourthVehicle,
		
		@FormParam("perMinuteFareFirstVehicleAirportPickUp") String perMinuteFareFirstVehicle,
		@FormParam("perMinuteFareSecondVehicleAirportPickUp") String perMinuteFareSecondVehicle,
		@FormParam("perMinuteFareThirdVehicleAirportPickUp") String perMinuteFareThirdVehicle,
		@FormParam("perMinuteFareFourthVehicleAirportPickUp") String perMinuteFareFourthVehicle,
		
		@FormParam("freeDistanceFirstVehicleAirportPickUp") String freeDistanceFirstVehicle,
		@FormParam("freeDistanceSecondVehicleAirportPickUp") String freeDistanceSecondVehicle,
		@FormParam("freeDistanceThirdVehicleAirportPickUp") String freeDistanceThirdVehicle,
		@FormParam("freeDistanceFourthVehicleAirportPickUp") String freeDistanceFourthVehicle,
		
		@FormParam("kmToIncreaseFareFirstVehicleAirportPickUp") String kmToIncreaseFareFirstVehicle,
		@FormParam("kmToIncreaseFareSecondVehicleAirportPickUp") String kmToIncreaseFareSecondVehicle,
		@FormParam("kmToIncreaseFareThirdVehicleAirportPickUp") String kmToIncreaseFareThirdVehicle,
		@FormParam("kmToIncreaseFareFourthVehicleAirportPickUp") String kmToIncreaseFareFourthVehicle,
		
		@FormParam("fareAfterSpecificKmFirstVehicleAirportPickUp") String fareAfterSpecificKmFirstVehicle,
		@FormParam("fareAfterSpecificKmSecondVehicleAirportPickUp") String fareAfterSpecificKmSecondVehicle,
		@FormParam("fareAfterSpecificKmThirdVehicleAirportPickUp") String fareAfterSpecificKmThirdVehicle,
		@FormParam("fareAfterSpecificKmFourthVehicleAirportPickUp") String fareAfterSpecificKmFourthVehicle,
		
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

		data.put(FieldConstants.AIRPORT_REGION_ID, airportRegionId);
		data.put(FieldConstants.LAT_LONG, latlong);
		data.put(FieldConstants.MULTICITY_REGION_ID, multicityCityRegionId);
		data.put(FieldConstants.ALIAS_NAME, aliasName);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.AREA_LATITUDE, areaLatitude);
		data.put(FieldConstants.AREA_LONGITUDE, areaLongitude);

		data.put("initialFareFirstVehicleAirportPickUp", initialFareFirstVehicle);
		data.put("initialFareSecondVehicleAirportPickUp", initialFareSecondVehicle);
		data.put("initialFareThirdVehicleAirportPickUp", initialFareThirdVehicle);
		data.put("initialFareFourthVehicleAirportPickUp", initialFareFourthVehicle);

		data.put("perKmFareFirstVehicleAirportPickUp", perKmFareFirstVehicle);
		data.put("perKmFareSecondVehicleAirportPickUp", perKmFareSecondVehicle);
		data.put("perKmFareThirdVehicleAirportPickUp", perKmFareThirdVehicle);
		data.put("perKmFareFourthVehicleAirportPickUp", perKmFareFourthVehicle);

		data.put("perMinuteFareFirstVehicleAirportPickUp", perMinuteFareFirstVehicle);
		data.put("perMinuteFareSecondVehicleAirportPickUp", perMinuteFareSecondVehicle);
		data.put("perMinuteFareThirdVehicleAirportPickUp", perMinuteFareThirdVehicle);
		data.put("perMinuteFareFourthVehicleAirportPickUp", perMinuteFareFourthVehicle);

		data.put("freeDistanceFirstVehicleAirportPickUp", freeDistanceFirstVehicle);
		data.put("freeDistanceSecondVehicleAirportPickUp", freeDistanceSecondVehicle);
		data.put("freeDistanceThirdVehicleAirportPickUp", freeDistanceThirdVehicle);
		data.put("freeDistanceFourthVehicleAirportPickUp", freeDistanceFourthVehicle);

		data.put("kmToIncreaseFareFirstVehicleAirportPickUp", kmToIncreaseFareFirstVehicle);
		data.put("kmToIncreaseFareSecondVehicleAirportPickUp", kmToIncreaseFareSecondVehicle);
		data.put("kmToIncreaseFareThirdVehicleAirportPickUp", kmToIncreaseFareThirdVehicle);
		data.put("kmToIncreaseFareFourthVehicleAirportPickUp", kmToIncreaseFareFourthVehicle);

		data.put("fareAfterSpecificKmFirstVehicleAirportPickUp", fareAfterSpecificKmFirstVehicle);
		data.put("fareAfterSpecificKmSecondVehicleAirportPickUp", fareAfterSpecificKmSecondVehicle);
		data.put("fareAfterSpecificKmThirdVehicleAirportPickUp", fareAfterSpecificKmThirdVehicle);
		data.put("fareAfterSpecificKmFourthVehicleAirportPickUp", fareAfterSpecificKmFourthVehicle);

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
			return loadView(UrlConstants.JSP_URLS.EDIT_AIRPORT_JSP);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		MulticityCityRegionModel multicityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(multicityRegionModel.getRegionLatitude(), multicityRegionModel.getRegionLongitude(), areaLatitude, areaLongitude);

		Map<String, String> airportregionFare = new HashMap<String, String>();

		airportregionFare.put("initialFareFirstVehicleAirportPickUp", initialFareFirstVehicle);
		airportregionFare.put("initialFareSecondVehicleAirportPickUp", initialFareSecondVehicle);
		airportregionFare.put("initialFareThirdVehicleAirportPickUp", initialFareThirdVehicle);
		airportregionFare.put("initialFareFourthVehicleAirportPickUp", initialFareFourthVehicle);

		airportregionFare.put("perKmFareFirstVehicleAirportPickUp", perKmFareFirstVehicle);
		airportregionFare.put("perKmFareSecondVehicleAirportPickUp", perKmFareSecondVehicle);
		airportregionFare.put("perKmFareThirdVehicleAirportPickUp", perKmFareThirdVehicle);
		airportregionFare.put("perKmFareFourthVehicleAirportPickUp", perKmFareFourthVehicle);

		airportregionFare.put("perMinuteFareFirstVehicleAirportPickUp", perMinuteFareFirstVehicle);
		airportregionFare.put("perMinuteFareSecondVehicleAirportPickUp", perMinuteFareSecondVehicle);
		airportregionFare.put("perMinuteFareThirdVehicleAirportPickUp", perMinuteFareThirdVehicle);
		airportregionFare.put("perMinuteFareFourthVehicleAirportPickUp", perMinuteFareFourthVehicle);

		airportregionFare.put("freeDistanceFirstVehicleAirportPickUp", freeDistanceFirstVehicle);
		airportregionFare.put("freeDistanceSecondVehicleAirportPickUp", freeDistanceSecondVehicle);
		airportregionFare.put("freeDistanceThirdVehicleAirportPickUp", freeDistanceThirdVehicle);
		airportregionFare.put("freeDistanceFourthVehicleAirportPickUp", freeDistanceFourthVehicle);

		airportregionFare.put("kmToIncreaseFareFirstVehicleAirportPickUp", kmToIncreaseFareFirstVehicle);
		airportregionFare.put("kmToIncreaseFareSecondVehicleAirportPickUp", kmToIncreaseFareSecondVehicle);
		airportregionFare.put("kmToIncreaseFareThirdVehicleAirportPickUp", kmToIncreaseFareThirdVehicle);
		airportregionFare.put("kmToIncreaseFareFourthVehicleAirportPickUp", kmToIncreaseFareFourthVehicle);

		airportregionFare.put("fareAfterSpecificKmFirstVehicleAirportPickUp", fareAfterSpecificKmFirstVehicle);
		airportregionFare.put("fareAfterSpecificKmSecondVehicleAirportPickUp", fareAfterSpecificKmSecondVehicle);
		airportregionFare.put("fareAfterSpecificKmThirdVehicleAirportPickUp", fareAfterSpecificKmThirdVehicle);
		airportregionFare.put("fareAfterSpecificKmFourthVehicleAirportPickUp", fareAfterSpecificKmFourthVehicle);

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
		airportRegionModel.setAirportRegionId(airportRegionId);
		airportRegionModel.setMulticityCityRegionId(multicityCityRegionId);
		airportRegionModel.setAliasName(aliasName);
		airportRegionModel.setAddress(address);
		airportRegionModel.setAreaPolygon(latitudelongitude);
		airportRegionModel.setRegionLongitude(areaLongitude);
		airportRegionModel.setRegionLatitude(areaLatitude);

		TempAirportRegionModel tempAirportRegionModel = new TempAirportRegionModel();
		tempAirportRegionModel.setAreaPolygon(latitudelongitude);
		tempAirportRegionModel.setRegionLatitude(areaLatitude);
		tempAirportRegionModel.setRegionLongitude(areaLongitude);
		TempAirportRegionModel tempAirportRegion = new TempAirportRegionModel();

		if (!(StringUtils.validString(areaLatitude) && StringUtils.validString(areaLongitude))) {
			data.put(FieldConstants.ADDRESS_ERROR, messageForKeyAdmin("errorInvalidAirportLocation"));
			data.put(FieldConstants.LAT_LONG, "");
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_AIRPORT_JSP);
		}

		tempAirportRegionModel.addTempAirportRegion();
		tempAirportRegion = TempAirportRegionModel.checkLatLongInAirportRegion(areaLatitude, areaLongitude);

		boolean isAirportEdited = false;

		if (distanceInMeters / 1000 < multicityRegionModel.getRegionRadius()) {

			if (tempAirportRegion != null) {

				airportRegionModel.editAirportRegion(loginSessionMap.get(LoginUtils.USER_ID));
				tempAirportRegionModel.deleteTempAirportRegion();
				addAirportFare(airportregionFare, adminSettings, loginSessionMap.get(LoginUtils.USER_ID), airportRegionId);
				isAirportEdited = true;
			} else {
				tempAirportRegionModel.deleteTempAirportRegion();
				data.put(FieldConstants.ADDRESS_ERROR, messageForKeyAdmin("errorInvalidPolygon"));
				data.put(FieldConstants.LAT_LONG, "");
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
				return loadView(UrlConstants.JSP_URLS.EDIT_AIRPORT_JSP);
			}

		} else {

			tempAirportRegionModel.deleteTempAirportRegion();
			data.put(FieldConstants.ADDRESS_ERROR, messageForKeyAdmin("errorAirportAddressError"));
			data.put(FieldConstants.LAT_LONG, "");

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_AIRPORT_JSP);
		}

		if (isAirportEdited) {
			MultiTenantUtils.updateAllVendorAirportRegionsAndCarFareWithCityId(airportRegionId, multicityCityRegionId);
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
	}

	public boolean addAirportFare(Map<String, String> airportregionFare, AdminSettingsModel adminSettingsModel, String loggedInUserId, String airportRegionId) {

		CarFareModel previousCarFareFirstVehicle = CarFareModel.getCarFareDetailsByAirportRegionId(ProjectConstants.Fifth_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		if (previousCarFareFirstVehicle == null) {

			CarFareModel carFareFirstVehicleAirportPickUp = new CarFareModel();
			carFareFirstVehicleAirportPickUp.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
			carFareFirstVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportPickUp").trim()));
			carFareFirstVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportPickUp").trim()));
			carFareFirstVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportPickUp").trim()));
			carFareFirstVehicleAirportPickUp.setDriverPayablePercentage(0);
			carFareFirstVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportPickUp")));
			carFareFirstVehicleAirportPickUp.setAirportRegionId(airportRegionId);
			carFareFirstVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

			carFareFirstVehicleAirportPickUp.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		} else {

			CarFareModel carFareFirstVehicleAirportPickUp = new CarFareModel();
			carFareFirstVehicleAirportPickUp.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
			carFareFirstVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportPickUp").trim()));
			carFareFirstVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportPickUp").trim()));
			carFareFirstVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportPickUp").trim()));
			carFareFirstVehicleAirportPickUp.setDriverPayablePercentage(0);
			carFareFirstVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportPickUp")));
			carFareFirstVehicleAirportPickUp.setAirportRegionId(airportRegionId);
			carFareFirstVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

			carFareFirstVehicleAirportPickUp.updateCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		}

		CarFareModel carFareSecondVehicleAirportPickUp = new CarFareModel();

		carFareSecondVehicleAirportPickUp.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
		carFareSecondVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicleAirportPickUp.setDriverPayablePercentage(0);
		carFareSecondVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceSecondVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareSecondVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmSecondVehicleAirportPickUp")));
		carFareSecondVehicleAirportPickUp.setAirportRegionId(airportRegionId);
		carFareSecondVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareSecondVehicleAirportPickUp.updateCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareThirdVehicleAirportPickUp = new CarFareModel();

		carFareThirdVehicleAirportPickUp.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
		carFareThirdVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicleAirportPickUp.setDriverPayablePercentage(0);
		carFareThirdVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceThirdVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareThirdVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmThirdVehicleAirportPickUp")));
		carFareThirdVehicleAirportPickUp.setAirportRegionId(airportRegionId);
		carFareThirdVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareThirdVehicleAirportPickUp.updateCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareFourthVehicleAirportPickUp = new CarFareModel();

		carFareFourthVehicleAirportPickUp.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
		carFareFourthVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicleAirportPickUp.setDriverPayablePercentage(0);
		carFareFourthVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFourthVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFourthVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFourthVehicleAirportPickUp")));
		carFareFourthVehicleAirportPickUp.setAirportRegionId(airportRegionId);
		carFareFourthVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);

		carFareFourthVehicleAirportPickUp.updateCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel previousAirportDropCarFareFirstVehicle = CarFareModel.getCarFareDetailsByAirportRegionId(ProjectConstants.Fifth_Vehicle_ID, airportRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		if (previousAirportDropCarFareFirstVehicle == null) {

			CarFareModel carFareFirstVehicleAiportDrop = new CarFareModel();
			carFareFirstVehicleAiportDrop.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
			carFareFirstVehicleAiportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportDrop").trim()));
			carFareFirstVehicleAiportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAiportDrop").trim()));
			carFareFirstVehicleAiportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAiportDrop").trim()));
			carFareFirstVehicleAiportDrop.setDriverPayablePercentage(0);
			carFareFirstVehicleAiportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAiportDrop").trim())) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicleAiportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAiportDrop"))) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicleAiportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAiportDrop")));
			carFareFirstVehicleAiportDrop.setAirportRegionId(airportRegionId);
			carFareFirstVehicleAiportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

			carFareFirstVehicleAiportDrop.addCarFare(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		} else {

			CarFareModel carFareFirstVehicle = new CarFareModel();
			carFareFirstVehicle.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
			carFareFirstVehicle.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportDrop").trim()));
			carFareFirstVehicle.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportDrop").trim()));
			carFareFirstVehicle.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportDrop").trim()));
			carFareFirstVehicle.setDriverPayablePercentage(0);
			carFareFirstVehicle.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicle.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
			carFareFirstVehicle.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportDrop")));
			carFareFirstVehicle.setAirportRegionId(airportRegionId);
			carFareFirstVehicle.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

			carFareFirstVehicle.updateAirportDropCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		}

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

		carFareSecondVehicleAirportDrop.updateAirportDropCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareThirdVehicleAiportDrop = new CarFareModel();

		carFareThirdVehicleAiportDrop.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
		carFareThirdVehicleAiportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAiportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAiportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAiportDrop.setDriverPayablePercentage(0);
		carFareThirdVehicleAiportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceThirdVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAiportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareThirdVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAiportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmThirdVehicleAirportDrop")));
		carFareThirdVehicleAiportDrop.setAirportRegionId(airportRegionId);
		carFareThirdVehicleAiportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

		carFareThirdVehicleAiportDrop.updateAirportDropCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		CarFareModel carFareFourthVehicleAiportDrop = new CarFareModel();

		carFareFourthVehicleAiportDrop.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
		carFareFourthVehicleAiportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAiportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAiportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAiportDrop.setDriverPayablePercentage(0);
		carFareFourthVehicleAiportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFourthVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAiportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFourthVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAiportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFourthVehicleAirportDrop")));
		carFareFourthVehicleAiportDrop.setAirportRegionId(airportRegionId);
		carFareFourthVehicleAiportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);

		carFareFourthVehicleAiportDrop.updateAirportDropCarFareForAirportRegion(loggedInUserId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_AIRPORT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}