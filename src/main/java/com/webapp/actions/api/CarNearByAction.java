package com.webapp.actions.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.CommonUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.EstimateFareModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.SurgePriceModel;
import com.webapp.models.TaxModel;

@Path("/api/car-near-by")
public class CarNearByAction extends BusinessApiAction {

	public static Logger logger = Logger.getLogger(CarNearByAction.class);

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
		@QueryParam("longi") String longi, 
		@QueryParam("carTypeId") String carTypeId
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

		lati = lati.trim();
		longi = longi.trim();

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(lati, longi);
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		String maxRadius = "0";

		CitySurgeModel citySurgeModel = CitySurgeModel.getMaxRadiusCitySurgeByRegionId(multicityCityRegionId);

		if (citySurgeModel == null) {
			maxRadius = adminSettingsModel.getRadiusString();
		} else {
			maxRadius = String.valueOf(citySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits());
		}

//		String constant = "\"WGS 84\"";
//		String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + longi + " " + lati + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
		String distance = GeoLocationUtil.getDistanceQuery(lati, longi, GeoLocationUtil.CAR_LOCATION);
		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + longi + " " + lati + ")'),  " + maxRadius + ")";

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		if (!MultiTenantUtils.validateVendorCarType(carTypeId, headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
			return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
		}

		//@formatter:off
		//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
		List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(latAndLong, distance, multicityCityRegionId, Arrays.asList(carTypeId), 
												null, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, 
												headerVendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
		//@formatter:on

		List<Map<String, Object>> gpsList = new ArrayList<Map<String, Object>>();

		if (carLocation.size() != 0 && carTypeId != null) {

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

	@Path("/new")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarListNew(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@QueryParam("lati") String lati, 
		@QueryParam("longi") String longi, 
		@QueryParam("carTypeId") String carTypeId
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

		lati = lati.trim();
		longi = longi.trim();

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(lati, longi);
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();
		CitySurgeModel citySurgeModel = CitySurgeModel.getMaxRadiusCitySurgeByRegionId(multicityCityRegionId);

		String maxRadius = citySurgeModel == null ? adminSettingsModel.getRadiusString() : (String.valueOf(citySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits()));
		String distance = GeoLocationUtil.getDistanceQuery(lati, longi, GeoLocationUtil.CAR_LOCATION);
		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + longi + " " + lati + ")'),  " + maxRadius + ")";

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);
		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		double minimumWalletAmount = 0.0;
		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		// If carTypeId is not null then consider only the carTypeId sent from UI. Else
		// get the car type's for the region that has fare assigned to it.
//		if (StringUtils.validString(carTypeId)) {
//
//			if (!MultiTenantUtils.validateVendorCarType(carTypeId, headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
//				return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
//			}
//		}

		List<CarFareModel> carFareList = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		if (carFareList.isEmpty()) {
			return sendBussinessError(messageForKey("errorNoCarFound", request));
		}

		List<String> carTypeList = new ArrayList<>();
		for (CarFareModel carFareModel : carFareList) {
			carTypeList.add(carFareModel.getCarTypeId());
		}

		//@formatter:off
		//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
		List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(latAndLong, distance, multicityCityRegionId, carTypeList, 
												null, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, 
												headerVendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
		//@formatter:on

		Map<String, Object> outmapFinalMap = new HashMap<>();

		// 01. Get the car types with fare available for the region alongwith available
		// driver eta's
		String firstCarTypeId = getCarTypesWithEta(lati, longi, adminSettingsModel, carFareList, carLocation, outmapFinalMap);

		// 02. Get the list of cars available for the user provided OR the 1st near car
		// type id
		getDriverListByCarType(lati, longi, carTypeId, adminSettingsModel, carLocation, outmapFinalMap, firstCarTypeId);

		outmapFinalMap.put("multicityCityRegionId", multicityCityRegionId);

		return sendDataResponse(outmapFinalMap);
	}
	
	@Path("/new-eta-fare")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarListNewWithEtaFare(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@QueryParam("sLati") String sLati, 
		@QueryParam("sLongi") String sLongi,
		@QueryParam("dLati") String dLati, 
		@QueryParam("dLongi") String dLongi,
		@QueryParam("carTypeId") String carTypeId,
		@QueryParam("rideLater") boolean rideLater
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

		sLati = sLati.trim();
		sLongi = sLongi.trim();
		
		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(sLati, sLongi);
		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		
		if (multicityCityRegionModel == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();
		CitySurgeModel citySurgeModel = CitySurgeModel.getMaxRadiusCitySurgeByRegionId(multicityCityRegionId);

		String maxRadius = citySurgeModel == null ? adminSettingsModel.getRadiusString() : (String.valueOf(citySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits()));
		String distance = GeoLocationUtil.getDistanceQuery(sLati, sLongi, GeoLocationUtil.CAR_LOCATION);
		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + sLongi + " " + sLati + ")'),  " + maxRadius + ")";

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);
		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		double minimumWalletAmount = 0.0;
		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		// If carTypeId is not null then consider only the carTypeId sent from UI. Else
		// get the car type's for the region that has fare assigned to it.
//		if (StringUtils.validString(carTypeId)) {
//
//			if (!MultiTenantUtils.validateVendorCarType(carTypeId, headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
//				return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
//			}
//		}

		List<CarFareModel> carFareList = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		if (carFareList.isEmpty()) {
			return sendBussinessError(messageForKey("errorNoCarFound", request));
		}

		List<String> carTypeList = new ArrayList<>();
		for (CarFareModel carFareModel : carFareList) {
			carTypeList.add(carFareModel.getCarTypeId());
		}

		//@formatter:off
		//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
		List<DriverGeoLocationModel> carLocation = DriverGeoLocationModel.getNearByCarList(latAndLong, distance, multicityCityRegionId, carTypeList, 
												null, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, 
												headerVendorId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
		//@formatter:on

		Map<String, Object> outmapFinalMap = new HashMap<>();

		// 01. Get the car types with fare available for the region alongwith available
		// driver eta's
		String firstCarTypeId = getCarTypesWithEtaTimeAndFare(sLati, sLongi, dLati, dLongi, headerVendorId, userId, multicityCityRegionModel, adminSettingsModel, carFareList, carLocation, outmapFinalMap, rideLater);

		// 02. Get the list of cars available for the user provided OR the 1st near car
		// type id
		getDriverListByCarType(sLati, sLongi, carTypeId, adminSettingsModel, carLocation, outmapFinalMap, firstCarTypeId);

		outmapFinalMap.put("multicityCityRegionId", multicityCityRegionId);

		return sendDataResponse(outmapFinalMap);
	}
	
	private String getCarTypesWithEtaTimeAndFare(String sLati, String sLongi, String dLati, String dLongi, String headerVendorId, String userId, MulticityCityRegionModel multicityCityRegionModel, AdminSettingsModel adminSettingsModel, List<CarFareModel> carFareList,
				List<DriverGeoLocationModel> carLocation, Map<String, Object> outmapFinalMap, boolean isRideLater) {
		
		double etaInMins = 0;
		double distanceBetweenCarPickup = 0;
		String firstCarTypeId = null;
		Map<String, Object> outPutMap = new HashMap<String, Object>();

		// ETA of car types with available drivers
		List<Map<String, Object>> etaOfAvailableCarList = new ArrayList<Map<String, Object>>();

//		// ETA of car types with no available drivers
//		List<Map<String, Object>> etaOfNotAvailableCarList = new ArrayList<Map<String, Object>>();

		for (CarFareModel carFareModel : carFareList) {
			
			double estimateFareWithoutDiscount = 0.0;
			double estimateFareWithDiscount = 0.0;
			double distanceInMeters = 0.0;
			double durationInMin = 0.0;
			
			if (((dLati != null) && (!"".equals(dLati))) && ((dLongi != null) && (!"".equals(dLongi)))) {
				
				Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(sLati, sLongi, dLati, dLongi);

				distanceInMeters = distanceMatrix.get("distanceInMeters");
				durationInMin = distanceMatrix.get("durationInMin");
				
				Map<String, Object> airportRegionMap = new HashMap<String, Object>();
				airportRegionMap = airPortbooking(sLati, sLongi, dLati, dLongi, carFareModel.getCarTypeId());
				String airportBookingType = "";
				
				if ((boolean) airportRegionMap.get("isAirportBooking")) {

					if (((sLati != null) && (!"".equals(sLati))) && ((sLongi != null) && (!"".equals(sLongi)))) {

						String latAndLong = "";
						String airportBookingTypeFare = "";
						if ((boolean) airportRegionMap.get("isAirportPickUp")) {
							latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + sLati + " " + sLongi + ")'), area_polygon)";
							airportBookingTypeFare = ProjectConstants.AIRPORT_PICKUP;
						}
						if ((boolean) airportRegionMap.get("isAirportDrop")) {

							latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + dLati + " " + dLongi + ")'), area_polygon)";
							airportBookingTypeFare = ProjectConstants.AIRPORT_DROP;
						}

						carFareModel = MultiTenantUtils.getAirportCarFare(carFareModel.getCarTypeId(), latAndLong, airportBookingTypeFare, headerVendorId);
						
						Map<String, Object> ac = TourUtils.checkBookingForAirportPickupOrDrop(sLati, sLongi, dLati, dLongi);
						if ((boolean) ac.get("isAirportPickUp")) {
							airportBookingType = ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP;
						} else {
							airportBookingType = ProjectConstants.AIRPORT_BOOKING_TYPE_DROP;
						}
					}
				} else {
					
					carFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(carFareModel.getCarTypeId(), multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), headerVendorId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
				}
				if (((dLati != null) && (!"".equals(dLati))) && ((dLongi != null) && (!"".equals(dLongi)))) {

					if (carFareModel != null) {

						estimateFareWithoutDiscount = EstimateFareAction.calculateFare(distanceInMeters, durationInMin, adminSettingsModel, carFareModel, true);
					}
					
					estimateFareWithDiscount = estimateFareWithoutDiscount;

				}

				String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE;

				ApnsDeviceModel userDeviceDetails = ApnsDeviceModel.getDeviseByUserId(userId);

				if (userDeviceDetails != null) {
					timeZone = userDeviceDetails.getTimezone();
				}

				Calendar calender = Calendar.getInstance();

				calender.setTimeZone(TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE));

				int currentHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
				int currentMinute = calender.get(Calendar.MINUTE);

				long requestTimeInMilli = DateUtils.getTimeInMillisForSurge(currentHourOfDay, currentMinute, false, timeZone);

				String surgeMessage = "";
				SurgePriceModel surgePriceModel = SurgePriceModel.getSurgePriceDetailsByRequestTimeAndRegionId(requestTimeInMilli, multicityCityRegionModel.getMulticityCityRegionId());

				boolean isSurgePriceApplied = false;
				String surgePriceId = "-1";
				double surgePrice = 1;

				if (!(boolean) airportRegionMap.get("isAirportBooking")) {
					if (surgePriceModel != null) {

						estimateFareWithoutDiscount = estimateFareWithoutDiscount * surgePriceModel.getSurgePrice();
						estimateFareWithDiscount = estimateFareWithoutDiscount;

						isSurgePriceApplied = true;
						surgePriceId = surgePriceModel.getSurgePriceId();
						surgePrice = surgePriceModel.getSurgePrice();
						surgeMessage = surgePrice + "x surge applied.";
					}
				}

				EstimateFareModel estimateFareModel = new EstimateFareModel();
				estimateFareModel.setsLatitude(sLati);
				estimateFareModel.setsLongitude(sLongi);
				estimateFareModel.setRideLater(isRideLater);
				
				CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, multicityCityRegionModel.getMulticityCityRegionId(), adminSettingsModel);

				if (!(boolean) airportRegionMap.get("isAirportBooking")) {

					if (!estimateFareModel.isRideLater() && applicableCitySurgeModel != null && applicableCitySurgeModel.getSurgeRate() > surgePrice) {

						estimateFareWithoutDiscount = estimateFareWithoutDiscount * applicableCitySurgeModel.getSurgeRate();
						estimateFareWithDiscount = estimateFareWithoutDiscount;

						isSurgePriceApplied = true;
						surgePriceId = applicableCitySurgeModel.getCitySurgeId();
						surgePrice = applicableCitySurgeModel.getSurgeRate();
						surgeMessage = surgePrice + "x surge applied as driver is traveling from far for your pickup.";
					}
				}

				// ---------- Tax -------------------------------
				List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

				double finalAmountForTaxCalculation;
				double totalTaxAmount = 0.0;

				finalAmountForTaxCalculation = estimateFareWithoutDiscount;

				if (taxModelList != null) {

					for (TaxModel taxModel : taxModelList) {

						double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

						taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2);

						totalTaxAmount = totalTaxAmount + taxAmount;
					}
				}

				totalTaxAmount = roundUpDecimalValueWithDownMode(totalTaxAmount, 2);

				estimateFareWithoutDiscount = estimateFareWithoutDiscount + totalTaxAmount;

				estimateFareWithDiscount = estimateFareWithoutDiscount;
			}
			

			// Check for the given car type, if any current driver is available or not.
			DriverGeoLocationModel gps = null;
			
			if (carFareModel != null) {
				 gps = getNearByDriverEtaForCarType(carLocation, carFareModel);
				 
				 outPutMap = new HashMap<String, Object>();
				 
					outPutMap.put("carTypeId", carFareModel.getCarTypeId());
					outPutMap.put("carType", carFareModel.getCarType());
					outPutMap.put("carTypeIconImage", carFareModel.getCarTypeIconImage());
					outPutMap.put("estimateFareWithoutDiscount", df_new.format(estimateFareWithoutDiscount));
					outPutMap.put("estimateFareWithDiscount",  df_new.format(estimateFareWithDiscount));
					
					// Driver for this particular car type is available
					if (gps != null) {

						distanceBetweenCarPickup = CommonUtils.distance(Double.parseDouble(sLati), Double.parseDouble(sLongi), Double.parseDouble(gps.getLatitude()), Double.parseDouble(gps.getLongitude()), 'K');
						etaInMins = CommonUtils.calculateETA(distanceBetweenCarPickup, adminSettingsModel.getDistanceUnits());

						outPutMap.put("distance", StringUtils.valueOf(distanceBetweenCarPickup) + " " + adminSettingsModel.getDistanceType());
						outPutMap.put("etaInMins", etaInMins > 1 ? etaInMins : 1);

					} else {

						// Driver for this particular car type is NOT available
						outPutMap.put("distance", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE + " " + adminSettingsModel.getDistanceType());
						outPutMap.put("etaInMins", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);

					}
					
					etaOfAvailableCarList.add(outPutMap);	
			}
			
		}

		outmapFinalMap.put("etaOfAvailableCarList", etaOfAvailableCarList);

		firstCarTypeId = etaOfAvailableCarList.get(0).get("carTypeId").toString();

		return firstCarTypeId;
	}

	private void getDriverListByCarType(String lati, String longi, String carTypeId, AdminSettingsModel adminSettingsModel, List<DriverGeoLocationModel> carLocation, Map<String, Object> outmapFinalMap, String firstCarTypeId) {

		// if Car type is null ---> Then consider the 1st car type coming in the list.
		// if Car type is not null ---> Then consider fetch the details of the car type
		if (!StringUtils.validString(carTypeId)) {
			carTypeId = firstCarTypeId;
		}

		double etaInMins;
		double distanceBetweenCarPickup;
		Map<String, Object> outPutMap = new HashMap<String, Object>();
		List<Map<String, Object>> gpsList = new ArrayList<Map<String, Object>>();

		for (DriverGeoLocationModel gps : carLocation) {

			if (gps.getCarTypeId().equalsIgnoreCase(carTypeId)) {

				outPutMap = new HashMap<String, Object>();

				outPutMap.put("driverId", gps.getDriverId());
				outPutMap.put("tourId", gps.getTourId());
				outPutMap.put("createdDateTime", gps.getCreatedAt());
				outPutMap.put("longitude", gps.getLongitude());
				outPutMap.put("latitude", gps.getLatitude());
				outPutMap.put("carTypeId", gps.getCarTypeId());

				distanceBetweenCarPickup = CommonUtils.distance(Double.parseDouble(lati), Double.parseDouble(longi), Double.parseDouble(gps.getLatitude()), Double.parseDouble(gps.getLongitude()), 'K');
				etaInMins = CommonUtils.calculateETA(distanceBetweenCarPickup, adminSettingsModel.getDistanceUnits());

				outPutMap.put("distance", StringUtils.valueOf(distanceBetweenCarPickup) + " " + adminSettingsModel.getDistanceType());
				outPutMap.put("etaInMins", etaInMins > 1 ? etaInMins : 1);

				gpsList.add(outPutMap);
			}
		}

		outmapFinalMap.put("carNearByList", gpsList);
	}

	private String getCarTypesWithEta(String lati, String longi, AdminSettingsModel adminSettingsModel, List<CarFareModel> carFareList, List<DriverGeoLocationModel> carLocation, Map<String, Object> outmapFinalMap) {

		/**
		 * For each car type available within the region with fare, find the nearest car
		 * with ETA.
		 */

		double etaInMins = 0;
		double distanceBetweenCarPickup = 0;
		String firstCarTypeId = null;
		Map<String, Object> outPutMap = new HashMap<String, Object>();

		// ETA of car types with available drivers
		List<Map<String, Object>> etaOfAvailableCarList = new ArrayList<Map<String, Object>>();

//		// ETA of car types with no available drivers
//		List<Map<String, Object>> etaOfNotAvailableCarList = new ArrayList<Map<String, Object>>();

		for (CarFareModel carFareModel : carFareList) {

			// Check for the given car type, if any current driver is available or not.
			DriverGeoLocationModel gps = getNearByDriverEtaForCarType(carLocation, carFareModel);

			outPutMap = new HashMap<String, Object>();
			outPutMap.put("carTypeId", carFareModel.getCarTypeId());
			outPutMap.put("carType", carFareModel.getCarType());
			outPutMap.put("carTypeIconImage", carFareModel.getCarTypeIconImage());

			// Driver for this particular car type is available
			if (gps != null) {

				distanceBetweenCarPickup = CommonUtils.distance(Double.parseDouble(lati), Double.parseDouble(longi), Double.parseDouble(gps.getLatitude()), Double.parseDouble(gps.getLongitude()), 'K');
				etaInMins = CommonUtils.calculateETA(distanceBetweenCarPickup, adminSettingsModel.getDistanceUnits());

				outPutMap.put("distance", StringUtils.valueOf(distanceBetweenCarPickup) + " " + adminSettingsModel.getDistanceType());
				outPutMap.put("etaInMins", etaInMins > 1 ? etaInMins : 1);

			} else {

				// Driver for this particular car type is NOT available
				outPutMap.put("distance", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE + " " + adminSettingsModel.getDistanceType());
				outPutMap.put("etaInMins", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);

//				etaOfNotAvailableCarList.add(outPutMap);
			}

			etaOfAvailableCarList.add(outPutMap);
		}

//		if (etaOfAvailableCarList.size() > 0) {
//
//			Comparator<Map<String, Object>> distanceComparator = new Comparator<Map<String, Object>>() {
//
//				@Override
//				public int compare(Map<String, Object> e1, Map<String, Object> e2) {
//					Double v1 = Double.parseDouble(e1.get("etaInMins").toString());
//					Double v2 = Double.parseDouble(e2.get("etaInMins").toString());
//					return v1.compareTo(v2);
//				}
//			};
//
//			Collections.sort(etaOfAvailableCarList, distanceComparator);
//		}
//
//		etaOfAvailableCarList.addAll(etaOfNotAvailableCarList);

		outmapFinalMap.put("etaOfAvailableCarList", etaOfAvailableCarList);

		firstCarTypeId = etaOfAvailableCarList.get(0).get("carTypeId").toString();

		return firstCarTypeId;
	}

	private DriverGeoLocationModel getNearByDriverEtaForCarType(List<DriverGeoLocationModel> carLocation, CarFareModel carFareModel) {
		
		for (DriverGeoLocationModel gps : carLocation) {
			if (gps.getCarTypeId().equalsIgnoreCase(carFareModel.getCarTypeId())) {
				return gps;
			}
		}

		return null;
	}
}