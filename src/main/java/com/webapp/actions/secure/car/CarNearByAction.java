package com.webapp.actions.secure.car;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.jeeutils.DateUtils;
import com.utils.CommonUtils;
import com.utils.LoginUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CarModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.EstimateFareModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.UserModel;

@Path("/car-near-by")
public class CarNearByAction extends BusinessAction {

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getNearByCarList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.REGION_LIST) String regionListString
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		System.out.println("\n\n\n\n\tregionListString\t" + regionListString);
		List<String> regionList = MyHubUtils.convertStringToList(regionListString);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		String distance = GeoLocationUtil.getDistanceQuery(ProjectConstants.BASE_LATITUDE, ProjectConstants.BASE_LONGITUDE, GeoLocationUtil.CAR_LOCATION);
		String latAndLong = GeoLocationUtil.getLatLngQuery(ProjectConstants.BASE_LATITUDE, ProjectConstants.BASE_LONGITUDE, GeoLocationUtil.CAR_LOCATION, adminSettingsModel.getRadiusString());

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("regionList", regionList);

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		inputMap.put("minimumWalletAmount", minimumWalletAmount);

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);

		int updatedDriverAvailableCount = 0;

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			inputMap.put("vendorId", UserRoleUtils.getParentVendorId(loginSessionMap));

			MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, loginSessionMap.get(LoginUtils.USER_ID), DateUtils.nowAsGmtMillisec());

			updatedDriverAvailableCount = DriverGeoLocationModel.getVendorsTotalAvailableDriver(regionList, loginSessionMap.get(LoginUtils.USER_ID), minimumWalletAmount, adminSettingsModel);

		} else {
			inputMap.put("vendorId", null);
			// In case of non vendor user, check for default vendor

			MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, loginSessionMap.get(LoginUtils.USER_ID), DateUtils.nowAsGmtMillisec());

			// Since it is the admin, drivers of all vendors will be shown. For "driver
			// subscription", flag of default vendor will be considered
			updatedDriverAvailableCount = DriverGeoLocationModel.getTotalAvailableDriver(regionList, minimumWalletAmount, loginSessionMap.get(LoginUtils.USER_ID), adminSettingsModel);
		}

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		List<DriverGeoLocationModel> carLocationList = DriverGeoLocationModel.getNearByAvailableCarListForWeb(inputMap);

		List<Map<String, String>> carMapList = new ArrayList<Map<String, String>>();
		Map<String, String> driverGeoLocationModelMap = new HashMap<String, String>();

		if (carLocationList.size() > 0) {

			for (DriverGeoLocationModel driverGeoLocationModel : carLocationList) {

				driverGeoLocationModelMap = new HashMap<String, String>();

				driverGeoLocationModelMap.put("driverId", driverGeoLocationModel.getDriverId());

				if (driverGeoLocationModel.getDriverTourStatus().equals(ProjectConstants.DRIVER_FREE_STATUS)) {
					driverGeoLocationModelMap.put("tourId", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
				} else {
					driverGeoLocationModelMap.put("tourId", "1");
				}

				driverGeoLocationModelMap.put("driverName", driverGeoLocationModel.getDriverName());
				driverGeoLocationModelMap.put("latitude", driverGeoLocationModel.getLatitude());
				driverGeoLocationModelMap.put("longitude", driverGeoLocationModel.getLongitude());
				driverGeoLocationModelMap.put("carIcon", driverGeoLocationModel.getIcon());

				if (driverGeoLocationModel.getCreatedAt() > timeBeforeDriverIdealTimeInMillis) {
					driverGeoLocationModelMap.put("locationUpdatedWithinIdealTime", "YES");
				} else {
					driverGeoLocationModelMap.put("locationUpdatedWithinIdealTime", "NO");
				}

				carMapList.add(driverGeoLocationModelMap);
			}
		}

		Map<String, Object> carListMap = new HashMap<String, Object>();
		carListMap.put("carMapList", carMapList);
		carListMap.put("updatedDriverAvailableCount", updatedDriverAvailableCount);
		return sendDataResponse(carListMap);
	}

	@GET
	@Path("/available-cars")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAvailableCarList(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res, 
		@QueryParam("address") String address,
		@QueryParam("carTypeId") String carTypeId,
		@QueryParam("sourcePlaceLat") String sourcePlaceLat,
		@QueryParam("sourcePlaceLng") String sourcePlaceLng
		) throws IOException, JSONException {
	//@formatter:on		

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();
		String roleId = UserModel.getRoleByUserId(loggedInUserId);

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(0);

		String longi = ProjectConstants.BASE_LONGITUDE;
		String lati = ProjectConstants.BASE_LATITUDE;

		if (sourcePlaceLat != null && sourcePlaceLng != null) {
			longi = sourcePlaceLng;
			lati = sourcePlaceLat;
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		String status = "";
		String message = "";

		List<Map<String, Object>> carMapList = new ArrayList<Map<String, Object>>();

		List<DriverGeoLocationModel> carLocationList = new ArrayList<DriverGeoLocationModel>();

		if (longi != null && lati != null) {

			String multicityCityRegionId = MultiCityAction.getMulticityRegionId(lati, longi);

			if (multicityCityRegionId == null) {

				status = "RegionFailure";
				message = "No services are provided within this region.";

			} else {

				if (!MultiTenantUtils.validateVendorCarType(carTypeId, loggedInUserId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
					outPutMap.put("status", "Failure");
					outPutMap.put("message", messageForKeyAdmin("errorInvalidCarTypeForVendorAndRegion", null));
					return sendDataResponse(outPutMap);
				}

				MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
				AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

				if (multicityCityRegionModel != null) {

					String maxRadius = adminSettingsModel.getRadiusString();

					EstimateFareModel estimateFareModel = new EstimateFareModel();
					estimateFareModel.setsLatitude(sourcePlaceLat);
					estimateFareModel.setsLongitude(sourcePlaceLng);
					estimateFareModel.setCarTypeId(carTypeId);

					CitySurgeModel applicableCitySurgeModel = TourUtils.getApplicableRadiusSurge(estimateFareModel, multicityCityRegionId, adminSettingsModel);

					if (applicableCitySurgeModel != null) {
						maxRadius = String.valueOf(applicableCitySurgeModel.getRadius() * adminSettingsModel.getDistanceUnits());
					}

//					String constant = "\"WGS 84\"";
//					String distance = "round(CAST(ST_Distance_Spheroid(car_location::geometry, ST_GeomFromText('POINT(" + longi + " " + lati + ")',4326), 'SPHEROID[" + constant + ",6378137,298.257223563]')As numeric),2)";
					String distance = GeoLocationUtil.getDistanceQuery(lati, longi, GeoLocationUtil.CAR_LOCATION);
					String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + longi + " " + lati + ")'),  " + maxRadius + ")";

					AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

					long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

					DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

					double minimumWalletAmount = 0.0;

					if (driverWalletSettingsModel != null) {
						minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
					}

					String newCarTypeId = carTypeId;
					if (carTypeId == null || "".equals(carTypeId) || (ProjectConstants.Rental_Type_ID.equals(carTypeId))) {
						newCarTypeId = null;
					}

					//@formatter:off
					//Driver subscription validity considered only for ride now. Driver subscription validity for ride later considered while assigning driver 
					if (UserRoles.VENDOR_ROLE_ID.equals(roleId)) {
						carLocationList = DriverGeoLocationModel.getVendorsNearByCarList(
									latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), 
									newCarTypeId, timeBeforeDriverIdealTimeInMillis, loggedInUserId, minimumWalletAmount,
									loggedInUserId, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
					} else {
						carLocationList = DriverGeoLocationModel.getNearByCarList(
									latAndLong, distance, multicityCityRegionModel.getMulticityCityRegionId(), 
									Arrays.asList(newCarTypeId), null, timeBeforeDriverIdealTimeInMillis, minimumWalletAmount, 
									WebappPropertyUtils.DEFAULT_VENDOR_ID, DateUtils.nowAsGmtMillisec(), adminSettingsModel);
					}
					//@formatter:on

					if (carLocationList.size() > 0) {
						status = "SUCCESS";
					} else {
						status = "Failure";
						message = "No cars/drivers are available currently. Please try after sometime.";
					}

				} else {
					status = "RegionFailure";
					message = "No services are provided within this region.";
				}
			}
		}

		if (carLocationList.size() > 0) {

			status = "SUCCESS";

			for (DriverGeoLocationModel driverGeoLocationModel : carLocationList) {

				Map<String, Object> innerMap = new HashMap<String, Object>();

				AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

				innerMap.put("driverId", driverGeoLocationModel.getDriverId());
				innerMap.put("tourId", driverGeoLocationModel.getTourId());
				innerMap.put("createdAt", driverGeoLocationModel.getCreatedAt() + "");
				innerMap.put("latitude", driverGeoLocationModel.getLatitude());
				innerMap.put("longitude", driverGeoLocationModel.getLongitude());

				innerMap.put("createdAt", driverGeoLocationModel.getCreatedAt() + "");
				innerMap.put("latitude", driverGeoLocationModel.getLatitude());
				innerMap.put("longitude", driverGeoLocationModel.getLongitude());

				DriverInfoModel driverInfo = DriverInfoModel.getDriverAccountDetailsById(driverGeoLocationModel.getDriverId());

				innerMap.put("driverName", driverInfo.getFirstName() + " " + driverInfo.getLastName());
				innerMap.put("phoneNo", driverInfo.getPhoneNoCode() + "-" + driverInfo.getPhoneNo());
				innerMap.put("phoneNoCode", driverInfo.getPhoneNoCode());
				innerMap.put("photoUrl", driverInfo.getPhotoUrl());
				innerMap.put("email", driverInfo.getEmail());
				innerMap.put("dob", DateUtils.getDatePickerFormatedWithoutZone1(new Date(driverInfo.getDrivingLicenseModel().getDob())));

				CarModel carModel = CarModel.getCarDetailsByDriverId(driverGeoLocationModel.getDriverId());

				if (carModel != null) {

					if ((carModel.getCarTypeId() != null) && (!"".equals(carModel.getCarTypeId())) && (!"-1".equals(carModel.getCarTypeId()))) {

						CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(carModel.getCarTypeId());

						innerMap.put("carType", carModel.getCarTypeId());
						innerMap.put("carTypeName", carModel.getCarType());
						innerMap.put("carIcon", carTypeModel.getIcon());
					} else {

						CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(ProjectConstants.Fifth_Vehicle_ID);

						innerMap.put("carType", carTypeModel.getCarTypeId());
						innerMap.put("carTypeName", carTypeModel.getCarType());
						innerMap.put("carIcon", carTypeModel.getIcon());
					}

				} else {

					CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(ProjectConstants.Fifth_Vehicle_ID);

					innerMap.put("carType", carTypeModel.getCarTypeId());
					innerMap.put("carTypeName", carTypeModel.getCarType());
					innerMap.put("carIcon", carTypeModel.getIcon());
				}

				if (driverInfo.getCarModel() != null) {

					if ((driverInfo.getCarModel().getCarPlateNo() != null) && (!"".equals(driverInfo.getCarModel().getCarPlateNo()))) {

						innerMap.put("carPlateNo", driverInfo.getCarModel().getCarPlateNo());

					} else {

						innerMap.put("carPlateNo", "");
					}
				} else {

					innerMap.put("carPlateNo", "");
				}

				List<DriverTripRatingsModel> driverTripRatingsModelList = DriverTripRatingsModel.getDriversTripRatingsList(driverGeoLocationModel.getDriverId());

				int driverAvgRate = 0;

				if (!driverTripRatingsModelList.isEmpty()) {

					int size = driverTripRatingsModelList.size();
					int rate = 0;

					for (DriverTripRatingsModel driverTripRatingsModel : driverTripRatingsModelList) {
						rate += driverTripRatingsModel.getRate();
					}

					driverAvgRate = rate / size;

					innerMap.put("ratings", driverAvgRate + "");
				} else {
					innerMap.put("ratings", "-1");
				}

				innerMap.put("ratings", driverAvgRate + "");

				double distance = CommonUtils.getInbetweenLocations(driverGeoLocationModel.getLatitude(), driverGeoLocationModel.getLongitude(), lati, longi);

				innerMap.put("distance", distance + " " + adminSettings.getDistanceType());

				innerMap.put("driverInfo", driverInfo.getFirstName() + " " + driverInfo.getLastName() + ", " + driverInfo.getEmail() + ", " + driverInfo.getCarModel().getCarPlateNo() + ", " + driverAvgRate + "");

				carMapList.add(innerMap);

			}
		}

		outPutMap.put("carMapList", carMapList);
		outPutMap.put("status", status);
		outPutMap.put("message", message);

		return sendDataResponse(outPutMap);
	}
}