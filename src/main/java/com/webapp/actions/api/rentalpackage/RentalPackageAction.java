package com.webapp.actions.api.rentalpackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.RentalPackageModel;
import com.webapp.models.RideLaterSettingsModel;

@Path("/api/rental")
public class RentalPackageAction extends BusinessApiAction {

	@Path("/packages/{offset}/{length}")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getSecurityDeposit(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("offset") int offset,
			@PathParam("length") int length,
			RentalPackageModel rentalPackageModel
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		if (rentalPackageModel.getLatitude() == 0) {
			return sendBussinessError(messageForKey("errorLatitudeRequired", request));
		}

		if (rentalPackageModel.getLongitude() == 0) {
			return sendBussinessError(messageForKey("errorLongitudeRequired", request));
		}

		if (length <= 0) {
			length = ProjectConstants.LIST_LIMIT;
		}

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(rentalPackageModel.getLatitude() + "", rentalPackageModel.getLongitude() + "");
		if (multicityCityRegionId == null || "".equals(multicityCityRegionId)) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		if (multicityCityRegionModel == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		if ((rentalPackageModel.getRentalPackageType() == null) || ("".equals(rentalPackageModel.getRentalPackageType()))) {
			rentalPackageModel.setRentalPackageType(null);
		} else if ((!ProjectConstants.RENTAL_INTERCITY_ID.equals(rentalPackageModel.getRentalPackageType())) && (!ProjectConstants.RENTAL_OUTSTATION_ID.equals(rentalPackageModel.getRentalPackageType()))) {
			return sendBussinessError(messageForKey("errorInvalidRentalPackageType", request));
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		List<Map<String, Object>> rentalPackageListMap = new ArrayList<Map<String, Object>>();
		List<RentalPackageModel> rentalPackageList = RentalPackageModel.getRentalPackageListPagination(offset, length, multicityCityRegionId, rentalPackageModel.getRentalPackageType(), headerVendorId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		for (RentalPackageModel rentalPackage : rentalPackageList) {

			Map<String, Object> innerOutPutMap = new HashMap<String, Object>();

			innerOutPutMap.put("rentalPackageId", rentalPackage.getRentalPackageId());

			if (rentalPackage.getPackageTime() == 1) {
				innerOutPutMap.put("packageTime", rentalPackage.getPackageTime() + " Hour");
			} else {
				innerOutPutMap.put("packageTime", rentalPackage.getPackageTime() + " Hours");
			}

			innerOutPutMap.put("packageDistance", df_new.format(((rentalPackage.getPackageDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());
			innerOutPutMap.put("createdDate", rentalPackage.getCreatedAt());
			innerOutPutMap.put("rentalPackageType", rentalPackage.getRentalPackageType());

			List<Map<String, Object>> carWiseFareDetailsMap = new ArrayList<Map<String, Object>>();

			List<RentalPackageFareModel> rentalPackageFareList = RentalPackageFareModel.getRentalPackageFareListByRentalPackageId(rentalPackage.getRentalPackageId());

			for (RentalPackageFareModel rentalPackageFareModel : rentalPackageFareList) {

				Map<String, Object> carFareOutPutMap = new HashMap<String, Object>();

				carFareOutPutMap.put("rentalPackageFareId", rentalPackageFareModel.getRentalPackageFareId());
				carFareOutPutMap.put("carTypeId", rentalPackageFareModel.getCarTypeId());
				carFareOutPutMap.put("baseFare", df_new.format(rentalPackageFareModel.getBaseFare()));
				carFareOutPutMap.put("additionalPerKmFare", df_new.format(rentalPackageFareModel.getPerKmFare()));
				carFareOutPutMap.put("additionalPerMinuteFare", df_new.format(rentalPackageFareModel.getPerMinuteFare()));

				if (rentalPackage.getPackageTime() == 1) {
					carFareOutPutMap.put("packageTime", rentalPackage.getPackageTime() + " Hour");
				} else {
					carFareOutPutMap.put("packageTime", rentalPackage.getPackageTime() + " Hours");
				}

				carFareOutPutMap.put("packageDistance", df_new.format(((rentalPackage.getPackageDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());
				carWiseFareDetailsMap.add(carFareOutPutMap);
			}

			innerOutPutMap.put("carWiseFareDetails", carWiseFareDetailsMap);
			rentalPackageListMap.add(innerOutPutMap);
		}

		outPutMap.put("rentalPackageList", rentalPackageListMap);
		outPutMap.put("multicityCityRegionId", multicityCityRegionId);
		outPutMap.put("multicityCountryId", multicityCityRegionModel.getMulticityCountryId());

		return sendDataResponse(outPutMap);
	}

	@Path("/car-near-by")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@QueryParam("lati") String lati, 
		@QueryParam("longi") String longi
		) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);
		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		if ((lati == null) || ("".equals(lati)) || (Double.parseDouble(lati) == 0)) {
			return sendBussinessError(messageForKey("errorLatitudeRequired", request));
		}

		if ((longi == null) || ("".equals(longi)) || (Double.parseDouble(longi) == 0)) {
			return sendBussinessError(messageForKey("errorLongitudeRequired", request));
		}

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(lati, longi);
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

//		String constant = "\"WGS 84\"";
//		String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + longi + " " + lati + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
		String distance = GeoLocationUtil.getDistanceQuery(lati, longi, GeoLocationUtil.CAR_LOCATION);
		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + longi + " " + lati + ")'),  " + adminSettingsModel.getRadiusString() + ")";

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		//@formatter:off
		//Driver subscription validity considered only for ride now.
		List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList
								(latAndLong, distance, multicityCityRegionId, null, null, 
								timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, 
								headerVendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
		//@formatter:on

		List<Map<String, Object>> gpsList = new ArrayList<Map<String, Object>>();

		if (carLocation.size() != 0) {

			for (DriverGeoLocationModel gps : carLocation) {

				Map<String, Object> outPutMap = new HashMap<String, Object>();

				outPutMap.put("driverId", gps.getDriverId());
				outPutMap.put("tourId", gps.getTourId());
				outPutMap.put("createdDateTime", gps.getCreatedAt());
				outPutMap.put("longitude", gps.getLongitude());
				outPutMap.put("latitude", gps.getLatitude());
				outPutMap.put("carTypeId", gps.getCarTypeId());

				gpsList.add(outPutMap);
			}

			return sendDataResponse(gpsList);
		} else {
			return sendBussinessError(messageForKey("errorNoCarFound", request));
		}
	}

	@Path("/ride-later/min-max-time")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRideLaterMinMaxTime(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {

			return sendUnauthorizedRequestError();
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		outPutMap.put("minBookingTime", rideLaterSettingsModel.getMinBookingTime());
		outPutMap.put("maxBookingTime", rideLaterSettingsModel.getMaxBookingTime());

		return sendDataResponse(outPutMap);
	}
}