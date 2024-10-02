package com.webapp.actions.secure.vendor.settings.airport;

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
import com.utils.LoginUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AirportRegionModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.VendorAirportRegionCarFareModel;
import com.webapp.models.VendorAirportRegionModel;

@Path("/edit-vendor-airport-region")
public class EditVendorAirportRegionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditVendorAirportRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_AIRPORT_REGION_ID) String vendorAirportRegionId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorAirportRegionModel vendorAirportRegionModel = VendorAirportRegionModel.getVendorAirportRegionDetailsById(vendorAirportRegionId);
		AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionDetailsById(vendorAirportRegionModel.getAirportRegionId());
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		MulticityCityRegionModel mcrm = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(airportRegionModel.getMulticityCityRegionId());

		VendorAirportRegionCarFareModel carFareFirstVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(), ProjectConstants.Fifth_Vehicle_ID,
					ProjectConstants.AIRPORT_PICKUP);
		VendorAirportRegionCarFareModel carFareSecondVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(), ProjectConstants.Second_Vehicle_ID,
					ProjectConstants.AIRPORT_PICKUP);
		VendorAirportRegionCarFareModel carFareThirdVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(), ProjectConstants.Third_Vehicle_ID,
					ProjectConstants.AIRPORT_PICKUP);
		VendorAirportRegionCarFareModel carFareFourthVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(), ProjectConstants.Fourth_Vehicle_ID,
					ProjectConstants.AIRPORT_PICKUP);

		VendorAirportRegionCarFareModel carAirportDropFareFirstVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(),
					ProjectConstants.Fifth_Vehicle_ID, ProjectConstants.AIRPORT_DROP);
		VendorAirportRegionCarFareModel carAirportDropFareSecondVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(),
					ProjectConstants.Second_Vehicle_ID, ProjectConstants.AIRPORT_DROP);
		VendorAirportRegionCarFareModel carAirportDropFareThirdVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(),
					ProjectConstants.Third_Vehicle_ID, ProjectConstants.AIRPORT_DROP);
		VendorAirportRegionCarFareModel carAirportDropFareFourthVehicle = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorAirportRegionModel.getVendorId(), vendorAirportRegionModel.getAirportRegionId(),
					ProjectConstants.Fourth_Vehicle_ID, ProjectConstants.AIRPORT_DROP);

		if (carFareFirstVehicle == null) {

			data.put("initialFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("perKmFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("perMinuteFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("freeDistanceFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("kmToIncreaseFareFirstVehicleAirportPickUp", StringUtils.valueOf(0));
			data.put("fareAfterSpecificKmFirstVehicleAirportPickUp", StringUtils.valueOf(0));

		} else {

			data.put("initialFareFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getInitialFare()));
			data.put("perKmFareFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getPerKmFare()));
			data.put("perMinuteFareFirstVehicleAirportPickUp", StringUtils.valueOf(carFareFirstVehicle.getPerMinuteFare()));
			data.put("freeDistanceFirstVehicleAirportPickUp", MyHubUtils.getDistanceInProjectUnitFromMeters(carFareFirstVehicle.getFreeDistance(), adminSettingsModel));
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

		data.put("kmToIncreaseFareSecondVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareSecondVehicle.getKmToIncreaseFare(), adminSettingsModel));
		data.put("kmToIncreaseFareThirdVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareThirdVehicle.getKmToIncreaseFare(), adminSettingsModel));
		data.put("kmToIncreaseFareFourthVehicleAirportDrop", MyHubUtils.getDistanceInProjectUnitFromMeters(carAirportDropFareFourthVehicle.getKmToIncreaseFare(), adminSettingsModel));

		data.put("fareAfterSpecificKmSecondVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareSecondVehicle.getFareAfterSpecificKm()));
		data.put("fareAfterSpecificKmThirdVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareThirdVehicle.getFareAfterSpecificKm()));
		data.put("fareAfterSpecificKmFourthVehicleAirportDrop", StringUtils.valueOf(carAirportDropFareFourthVehicle.getFareAfterSpecificKm()));

		data.put(FieldConstants.VENDOR_AIRPORT_REGION_ID, vendorAirportRegionModel.getVendorAirportRegionId());
		data.put(FieldConstants.MULTICITY_REGION_ID, airportRegionModel.getMulticityCityRegionId());
		data.put(FieldConstants.AIRPORT_REGION_ID, airportRegionModel.getAirportRegionId());
		data.put(FieldConstants.ADDRESS, airportRegionModel.getAddress());
		data.put(FieldConstants.ALIAS_NAME, airportRegionModel.getAliasName());
		data.put(FieldConstants.LAT_LONG, airportRegionModel.getAreaPolygon());
		data.put(FieldConstants.AREA_LATITUDE, airportRegionModel.getRegionLatitude());
		data.put(FieldConstants.AREA_LONGITUDE, airportRegionModel.getRegionLongitude());
		data.put(FieldConstants.REGION_NAME, mcrm.getCityDisplayName());

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_AIRPORT_REGION_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_AIRPORT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postEditVendorAirportRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_AIRPORT_REGION_ID) String vendorAirportRegionId,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		
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

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorAirportRegionModel vendorAirportRegionModel = VendorAirportRegionModel.getVendorAirportRegionDetailsById(vendorAirportRegionId);
		AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionDetailsById(vendorAirportRegionModel.getAirportRegionId());
		MulticityCityRegionModel mcrm = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(airportRegionModel.getMulticityCityRegionId());

		data.put(FieldConstants.VENDOR_AIRPORT_REGION_ID, vendorAirportRegionModel.getVendorAirportRegionId());
		data.put(FieldConstants.MULTICITY_REGION_ID, airportRegionModel.getMulticityCityRegionId());
		data.put(FieldConstants.AIRPORT_REGION_ID, airportRegionModel.getAirportRegionId());
		data.put(FieldConstants.ADDRESS, airportRegionModel.getAddress());
		data.put(FieldConstants.ALIAS_NAME, airportRegionModel.getAliasName());
		data.put(FieldConstants.LAT_LONG, airportRegionModel.getAreaPolygon());
		data.put(FieldConstants.AREA_LATITUDE, airportRegionModel.getRegionLatitude());
		data.put(FieldConstants.AREA_LONGITUDE, airportRegionModel.getRegionLongitude());
		data.put(FieldConstants.REGION_NAME, mcrm.getCityDisplayName());

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

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_AIRPORT_REGION_URL);
			} else {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_AIRPORT_REGION_URL + "?vendorId=" + vendorAirportRegionModel.getVendorId());
			}

			return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_AIRPORT_JSP);
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
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

		addVendorAirportCarFare(airportregionFare, adminSettings, vendorAirportRegionId, vendorAirportRegionModel, airportRegionModel);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_AIRPORT_REGION_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_AIRPORT_REGION_URL + "?vendorId=" + vendorAirportRegionModel.getVendorId());
		}
	}

	public boolean addVendorAirportCarFare(Map<String, String> airportregionFare, AdminSettingsModel adminSettingsModel, String vendorAirportRegionId, VendorAirportRegionModel vendorAirportRegionModel, AirportRegionModel airportRegionModel) {

		String vendorId = vendorAirportRegionModel.getVendorId();

		VendorAirportRegionCarFareModel deleteModel = new VendorAirportRegionCarFareModel();
		deleteModel.setVendorId(vendorId);
		deleteModel.setAirportRegionId(vendorAirportRegionModel.getAirportRegionId());
		deleteModel.deleteExistingData();

		VendorAirportRegionCarFareModel carFareFirstVehicleAirportPickUp = new VendorAirportRegionCarFareModel();
		carFareFirstVehicleAirportPickUp.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareFirstVehicleAirportPickUp.setVendorId(vendorId);
		carFareFirstVehicleAirportPickUp.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareFirstVehicleAirportPickUp.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareFirstVehicleAirportPickUp.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareFirstVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);
		carFareFirstVehicleAirportPickUp.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
		carFareFirstVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportPickUp").trim()));
		carFareFirstVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportPickUp").trim()));
		carFareFirstVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportPickUp").trim()));
		carFareFirstVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportPickUp")));

		carFareFirstVehicleAirportPickUp.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareSecondVehicleAirportPickUp = new VendorAirportRegionCarFareModel();

		carFareSecondVehicleAirportPickUp.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareSecondVehicleAirportPickUp.setVendorId(vendorId);
		carFareSecondVehicleAirportPickUp.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareSecondVehicleAirportPickUp.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareSecondVehicleAirportPickUp.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareSecondVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);
		carFareSecondVehicleAirportPickUp.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
		carFareSecondVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareSecondVehicleAirportPickUp").trim()));
		carFareSecondVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceSecondVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareSecondVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmSecondVehicleAirportPickUp")));

		carFareSecondVehicleAirportPickUp.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareThirdVehicleAirportPickUp = new VendorAirportRegionCarFareModel();

		carFareThirdVehicleAirportPickUp.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareThirdVehicleAirportPickUp.setVendorId(vendorId);
		carFareThirdVehicleAirportPickUp.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareThirdVehicleAirportPickUp.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareThirdVehicleAirportPickUp.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareThirdVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);
		carFareThirdVehicleAirportPickUp.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
		carFareThirdVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareThirdVehicleAirportPickUp").trim()));
		carFareThirdVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceThirdVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareThirdVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmThirdVehicleAirportPickUp")));

		carFareThirdVehicleAirportPickUp.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareFourthVehicleAirportPickUp = new VendorAirportRegionCarFareModel();

		carFareFourthVehicleAirportPickUp.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareFourthVehicleAirportPickUp.setVendorId(vendorId);
		carFareFourthVehicleAirportPickUp.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareFourthVehicleAirportPickUp.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareFourthVehicleAirportPickUp.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareFourthVehicleAirportPickUp.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);
		carFareFourthVehicleAirportPickUp.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
		carFareFourthVehicleAirportPickUp.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicleAirportPickUp.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicleAirportPickUp.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFourthVehicleAirportPickUp").trim()));
		carFareFourthVehicleAirportPickUp.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFourthVehicleAirportPickUp").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportPickUp.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFourthVehicleAirportPickUp"))) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportPickUp.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFourthVehicleAirportPickUp")));

		carFareFourthVehicleAirportPickUp.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareFirstVehicleAirportDrop = new VendorAirportRegionCarFareModel();

		carFareFirstVehicleAirportDrop.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareFirstVehicleAirportDrop.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareFirstVehicleAirportDrop.setVendorId(vendorId);
		carFareFirstVehicleAirportDrop.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareFirstVehicleAirportDrop.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareFirstVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);
		carFareFirstVehicleAirportDrop.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
		carFareFirstVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFirstVehicleAirportDrop").trim()));
		carFareFirstVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFirstVehicleAirportDrop").trim()));
		carFareFirstVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFirstVehicleAirportDrop").trim()));
		carFareFirstVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFirstVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFirstVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareFirstVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFirstVehicleAirportDrop")));

		carFareFirstVehicleAirportDrop.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareSecondVehicleAirportDrop = new VendorAirportRegionCarFareModel();

		carFareSecondVehicleAirportDrop.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareSecondVehicleAirportDrop.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareSecondVehicleAirportDrop.setVendorId(vendorId);
		carFareSecondVehicleAirportDrop.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareSecondVehicleAirportDrop.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareSecondVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);
		carFareSecondVehicleAirportDrop.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
		carFareSecondVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareSecondVehicleAirportDrop").trim()));
		carFareSecondVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareSecondVehicleAirportDrop").trim()));
		carFareSecondVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareSecondVehicleAirportDrop").trim()));
		carFareSecondVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceSecondVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareSecondVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareSecondVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmSecondVehicleAirportDrop")));

		carFareSecondVehicleAirportDrop.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareThirdVehicleAirportDrop = new VendorAirportRegionCarFareModel();

		carFareThirdVehicleAirportDrop.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareThirdVehicleAirportDrop.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareThirdVehicleAirportDrop.setVendorId(vendorId);
		carFareThirdVehicleAirportDrop.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareThirdVehicleAirportDrop.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareThirdVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);
		carFareThirdVehicleAirportDrop.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
		carFareThirdVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareThirdVehicleAirportDrop").trim()));
		carFareThirdVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceThirdVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareThirdVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareThirdVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmThirdVehicleAirportDrop")));

		carFareThirdVehicleAirportDrop.addVendorAirportRegion(vendorId);

		// -------------------------------------------------------------------------------------------------------------

		VendorAirportRegionCarFareModel carFareFourthVehicleAirportDrop = new VendorAirportRegionCarFareModel();

		carFareFourthVehicleAirportDrop.setVendorAirportRegionId(vendorAirportRegionModel.getVendorAirportRegionId());
		carFareFourthVehicleAirportDrop.setAirportRegionId(airportRegionModel.getAirportRegionId());
		carFareFourthVehicleAirportDrop.setVendorId(vendorId);
		carFareFourthVehicleAirportDrop.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
		carFareFourthVehicleAirportDrop.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		carFareFourthVehicleAirportDrop.setAirportBookingType(ProjectConstants.AIRPORT_DROP);
		carFareFourthVehicleAirportDrop.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
		carFareFourthVehicleAirportDrop.setInitialFare(Double.parseDouble(airportregionFare.get("initialFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAirportDrop.setPerKmFare(Double.parseDouble(airportregionFare.get("perKmFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAirportDrop.setPerMinuteFare(Double.parseDouble(airportregionFare.get("perMinuteFareFourthVehicleAirportDrop").trim()));
		carFareFourthVehicleAirportDrop.setFreeDistance((Double.parseDouble(airportregionFare.get("freeDistanceFourthVehicleAirportDrop").trim())) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportDrop.setKmToIncreaseFare((Double.parseDouble(airportregionFare.get("kmToIncreaseFareFourthVehicleAirportDrop"))) * adminSettingsModel.getDistanceUnits());
		carFareFourthVehicleAirportDrop.setFareAfterSpecificKm(Double.parseDouble(airportregionFare.get("fareAfterSpecificKmFourthVehicleAirportDrop")));

		carFareFourthVehicleAirportDrop.addVendorAirportRegion(vendorId);
		return false;
	}

	public boolean hasErrorsEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_VENDOR_AIRPORT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}