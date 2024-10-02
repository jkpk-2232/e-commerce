package com.webapp.actions.api.multicity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.MulticityUserRegionModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserProfileModel;

@Path("/api/multi-city")
public class MultiCityAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCountryList(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res
		) throws IOException, SQLException {
	//@formatter:on

		List<MulticityCountryModel> cityLists = MulticityCountryModel.getMulticityCountrySearch(0, 100000, "", "%%");

		LinkedHashMap<String, String> cityListMap = new LinkedHashMap<String, String>();

		for (MulticityCountryModel geoCityModel : cityLists) {
			cityListMap.put(geoCityModel.getMulticityCountryId() + "", geoCityModel.getCountryName());
		}

		return sendDataResponse(cityListMap);
	}

	@Path("/{countryId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMessageList(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@PathParam("countryId") String countryId
		) throws IOException, SQLException {
	//@formatter:on

		List<MulticityCityRegionModel> cityLists = MulticityCityRegionModel.getMulticityCityRegionSearch(0, 100000, "", "%%", countryId);

		LinkedHashMap<String, String> cityListMap = new LinkedHashMap<String, String>();

		for (MulticityCityRegionModel geoCityModel : cityLists) {
			cityListMap.put(geoCityModel.getMulticityCityRegionId() + "", MyHubUtils.getTrimmedTo(geoCityModel.getCityOriginalName(), 20));
		}

		return sendDataResponse(cityListMap);
	}

	@Path("/rental-fare/{countryId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRentalFare(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@PathParam("countryId") String countryId
		) throws IOException, SQLException {
	//@formatter:on

		List<MulticityCityRegionModel> cityLists = null;

		cityLists = MulticityCityRegionModel.getMulticityCityRegionSearch(0, 1000000, null, "%%", countryId);

		LinkedHashMap<String, String> cityListMap = new LinkedHashMap<String, String>();

		for (MulticityCityRegionModel geoCityModel : cityLists) {
			cityListMap.put(geoCityModel.getMulticityCityRegionId() + "", MyHubUtils.getTrimmedTo(geoCityModel.getCityOriginalName(), 20));
		}

		return sendDataResponse(cityListMap);
	}

	@Path("/car-type")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarTypesByRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		TourModel tourModel
		) throws IOException, SQLException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		Map<String, Object> output = new HashMap<String, Object>();

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(tourModel.getsLatitude(), tourModel.getsLongitude());
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(multicityCityRegionModel.getMulticityCountryId());

		List<CarTypeModel> carTypeModel = CarTypeModel.getAllCars();

		List<String> carType = new ArrayList<String>();

		for (CarTypeModel carTypeModel2 : carTypeModel) {
			carType.add(carTypeModel2.getCarTypeId());
		}

		output.put("multicityCountryId", multicityCountryModel.getMulticityCountryId());
		output.put("multicityCityRegionId", multicityCityRegionId);
		output.put("carType", carType);
		output.put("currencySymbol", multicityCountryModel.getCurrencySymbol());

		return sendDataResponse(output);
	}

	@Path("/pre-booking")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updatePhone(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		TourModel tourModel
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

		Map<String, Object> output = new HashMap<String, Object>();
		boolean isCardAvailable = false;

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(tourModel.getsLatitude(), tourModel.getsLongitude());
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(multicityCityRegionModel.getMulticityCountryId());

		CarFareModel carFare = CarFareModel.getCarFareDetailsByRegionCountryAndId(tourModel.getCarTypeId(), multicityCityRegionId, multicityCountryModel.getMulticityCountryId(), headerVendorId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		carFare.setCancellationCharges(CancellationChargeModel.getAdminCancellationCharges().getCharge());

		output.put("multicityCountryId", multicityCountryModel.getMulticityCountryId());
		output.put("multicityCityRegionId", multicityCityRegionId);
		output.put("currencySymbol", multicityCountryModel.getCurrencySymbol());
		output.put("isCardAvailable", isCardAvailable);
		output.put("carFare", carFare);

		return sendDataResponse(output);
	}

	public static String getMulticityRegionId(String sLati, String sLongi) {

		String multicityCityRegionId = null;

//		String constant1 = "\"WGS 84\"";
//		String distance1 = "round(CAST(ST_Distance_Spheroid(region_geolocation::geometry, ST_GeomFromText('POINT(" + sLongi + " " + sLati + ")',4326), 'SPHEROID[" + constant1 + ",6378137,298.257223563]')As numeric),2)";
		String distance1 = GeoLocationUtil.getDistanceQuery(sLati, sLongi, GeoLocationUtil.REGION_LOCATION);

		String radius = "100000";
		String latAndLong1 = "ST_DWithin(region_geolocation,ST_GeographyFromText('SRID=4326;POINT(" + sLongi + " " + sLati + ")'),  " + radius + ")";

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getNearByRegionList(latAndLong1, distance1);

		if (multicityCityRegionModelList.size() == 0) {
			return null;
		}

		List<MulticityCityRegionModel> validRegionList = new ArrayList<MulticityCityRegionModel>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			double businessDistance = distFrom(Double.parseDouble(sLati), Double.parseDouble(sLongi), Double.parseDouble(multicityCityRegionModel.getRegionLatitude()), Double.parseDouble(multicityCityRegionModel.getRegionLongitude()));

			if (businessDistance <= multicityCityRegionModel.getRegionRadius()) {
				validRegionList.add(multicityCityRegionModel);
			}
		}

		if (validRegionList.size() > 0) {

			{
				Collections.sort(validRegionList, new Comparator<MulticityCityRegionModel>() {
					@Override
					public int compare(MulticityCityRegionModel model1, MulticityCityRegionModel model2) {
						return String.valueOf(model1.getCreatedAt()).compareTo(String.valueOf(model2.getCreatedAt()));
					}
				});

				multicityCityRegionId = validRegionList.get(0).getMulticityCityRegionId();
			}
		} else {
			return null;
		}

		return multicityCityRegionId;
	}

	public static String setLatLngByUserId(UserProfileModel userProfileModel, Map<String, String> data) {

		String multicityRegionId = null;
		String currentLat = ProjectConstants.BASE_LATITUDE;
		String currentLng = ProjectConstants.BASE_LONGITUDE;

		switch (userProfileModel.getRoleId()) {

		case UserRoles.VENDOR_ROLE_ID:

			List<MulticityUserRegionModel> list = MulticityUserRegionModel.getMulticityUserRegionByUserId(userProfileModel.getUserId());

			if (list.size() > 0) {
				multicityRegionId = list.get(0).getMulticityCityRegionId();
				MulticityCityRegionModel mcrm = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityRegionId);
				currentLat = mcrm.getRegionLatitude();
				currentLng = mcrm.getRegionLongitude();
			}

			break;

		case UserRoles.ADMIN_ROLE_ID:

			List<MulticityUserRegionModel> list1 = MulticityUserRegionModel.getMulticityUserRegionByUserId(userProfileModel.getUserId());

			if (list1.size() > 0) {
				multicityRegionId = list1.get(0).getMulticityCityRegionId();
				MulticityCityRegionModel mcrm = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityRegionId);
				currentLat = mcrm.getRegionLatitude();
				currentLng = mcrm.getRegionLongitude();
			}

			break;

		case UserRoles.SUPER_ADMIN_ROLE_ID:

			multicityRegionId = WebappPropertyUtils.DEFAULT_REGION_ID;
			MulticityCityRegionModel mcrm = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityRegionId);
			currentLat = mcrm.getRegionLatitude();
			currentLng = mcrm.getRegionLongitude();

			break;

		default:

			currentLat = ProjectConstants.BASE_LATITUDE;
			currentLng = ProjectConstants.BASE_LONGITUDE;

			break;
		}

		data.put(FieldConstants.CURRENT_LAT, currentLat);
		data.put(FieldConstants.CURRENT_LNG, currentLng);

		return multicityRegionId;
	}

	public static List<String> getAssignedRegionList(String userRole, String userId) {
		UserProfileModel userProfileModel = new UserProfileModel();
		userProfileModel.setUserRole(userRole);
		userProfileModel.setUserId(userId);
		return getAssignedRegionList(userProfileModel);
	}

	public static List<String> getAssignedRegionList(UserProfileModel userProfileModel) {

		List<String> assignedRegionList = new ArrayList<String>();

		if (!UserRoles.SUPER_ADMIN_ROLE.equalsIgnoreCase(userProfileModel.getUserRole())) {

			List<MulticityUserRegionModel> multicityUserRegionModelList = MulticityUserRegionModel.getMulticityUserRegionByUserId(userProfileModel.getUserId());

			if (multicityUserRegionModelList != null && multicityUserRegionModelList.size() > 0) {
				for (MulticityUserRegionModel multicityUserRegionModel : multicityUserRegionModelList) {
					assignedRegionList.add(multicityUserRegionModel.getMulticityCityRegionId());
				}
			}
		}

		if (assignedRegionList.size() <= 0) {
			assignedRegionList = null;
		}

		return assignedRegionList;
	}

	public static List<String> getAssignedRegionListIntersectingWithAdminRegions(UserProfileModel userProfileModel, String adminUserId) {

		// show only those regions that are common to admin and the vendors
		UserProfileModel userAdminProfileModel = UserProfileModel.getAdminUserAccountDetailsById(adminUserId);
		List<String> assignedRegionListOfAdmin = getAssignedRegionList(userAdminProfileModel);

		List<String> assignedRegionList = new ArrayList<String>();

		if (!UserRoles.SUPER_ADMIN_ROLE.equalsIgnoreCase(userProfileModel.getUserRole())) {

			List<MulticityUserRegionModel> multicityUserRegionModelList = MulticityUserRegionModel.getMulticityUserRegionByUserId(userProfileModel.getUserId());

			if (multicityUserRegionModelList != null && multicityUserRegionModelList.size() > 0) {

				for (MulticityUserRegionModel multicityUserRegionModel : multicityUserRegionModelList) {

					if (assignedRegionListOfAdmin.contains(multicityUserRegionModel.getMulticityCityRegionId())) {
						assignedRegionList.add(multicityUserRegionModel.getMulticityCityRegionId());
					}
				}
			}
		}

		if (assignedRegionList.size() <= 0) {
			assignedRegionList = null;
		}

		return assignedRegionList;
	}

	public static boolean verifySelectedRegionAgainstAssignedRegion(String userId, String multicityCityRegionId) {

		List<MulticityUserRegionModel> multicityUserRegionModelList = MulticityUserRegionModel.getMulticityUserRegionByUserId(userId);

		if (multicityUserRegionModelList == null || multicityUserRegionModelList.isEmpty()) {
			return false;
		}

		for (MulticityUserRegionModel multicityUserRegionModel : multicityUserRegionModelList) {
			if (multicityUserRegionModel.getMulticityCityRegionId().equalsIgnoreCase(multicityCityRegionId)) {
				return true;
			}
		}

		return false;
	}

	public static boolean validateIfDestinationLiesWithinRegion(String dLat, String dLng, String multicityCityRegionId) {

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

		if (multicityCityRegionModel == null) {
			return false;
		}

		return validateIfDestinationLiesWithinRegion(dLat, dLng, multicityCityRegionModel);
	}

	public static boolean validateIfDestinationLiesWithinRegion(String dLat, String dLng, MulticityCityRegionModel multicityCityRegionModel) {

		if (multicityCityRegionModel == null) {
			return false;
		}

		// Check destination in same region
		double businessDistance = distFrom(Double.parseDouble(dLat), Double.parseDouble(dLng), Double.parseDouble(multicityCityRegionModel.getRegionLatitude()), Double.parseDouble(multicityCityRegionModel.getRegionLongitude()));
		return businessDistance <= multicityCityRegionModel.getRegionRadius();
	}
}