package com.webapp.actions.api;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CategoryModel;
import com.webapp.models.ServiceModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.VendorSubscriberModel;

@Path("/api/vendor-brand")
public class VendorBrandAction extends BusinessApiAction {

	@Path("/{serviceId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBrandListing(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("serviceId") String serviceId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("latitude") String latitude,
		@QueryParam("longitude") String longitude,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		// 01. get all categories
		// 02. get list of brands for each category (max 25 (from app) -> sorted on
		// distance & radius(settings), name)
		// 03. category should be then sorted on the distance
		// 04. remove blank categories

		Instant todayInstantObject = DateUtils.getNowInstant();
		String currentDayOfWeekValue = DateUtils.getDayOfWeek(todayInstantObject);

		List<Map<String, Object>> finalList = new ArrayList<>();

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String latAndLong = "ST_DWithin(vs.store_address_geolocation,ST_GeographyFromText('SRID=4326;POINT(" + longitude + " " + latitude + ")'),  " + adminSettingsModel.getRadiusStoreVisibilityString() + ")";

		List<CategoryModel> categoryList = CategoryModel.getCategorySearch(0, 0, "%%", 0, 0, serviceId, "1");

		for (CategoryModel categoryModel : categoryList) {

			List<UserProfileModel> brandList = UserProfileModel.getVendorBrandList(serviceId, categoryModel.getCategoryId(), start, length, "%" + searchKey + "%", latitude, longitude, latAndLong, loggedInUserId, currentDayOfWeekValue, null,
						ProjectConstants.NOT_AVAILABLE, null);

			Map<String, Object> finalMap = new HashMap<>();
			LinkedList<Map<String, Object>> vendorMapList = new LinkedList<>();

			boolean isFirstEntry = true;

			for (UserProfileModel userProfileModel : brandList) {

				if (isFirstEntry) {

					finalMap.put("distance", userProfileModel.getDistance());
					finalMap.put("categoryId", userProfileModel.getCategoryId());
					finalMap.put("categoryName", userProfileModel.getCategoryName());

					isFirstEntry = false;
				}

				boolean isVendorStoreOpen = OrderUtils.isVendorStoreOpen(todayInstantObject, userProfileModel);

				vendorMapList.add(VendorSubscriberModel.getLimitedData(userProfileModel, isVendorStoreOpen));
			}

			if (vendorMapList.size() > 0) {
				// if any category has no brand list, then skip that category
				finalMap.put("vendorList", vendorMapList);
				finalList.add(finalMap);
			}
		}

		if (finalList.size() > 0) {

			Comparator<Map<String, Object>> distanceComparator = new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> e1, Map<String, Object> e2) {
					Double v1 = Double.parseDouble(e1.get("distance").toString());
					Double v2 = Double.parseDouble(e2.get("distance").toString());
					return v1.compareTo(v2);
				}
			};

			Comparator<Map<String, Object>> categoryNameComparator = new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> e1, Map<String, Object> e2) {
					String v1 = (String) e1.get("categoryName");
					String v2 = (String) e2.get("categoryName");
					return v1.compareTo(v2);
				}
			};

			Collections.sort(finalList, distanceComparator.thenComparing(categoryNameComparator));
		}

		Map<String, List<Map<String, Object>>> finalResponse = new HashMap<>();
		finalResponse.put("vendorBrandList", finalList);
		return sendDataResponse(finalResponse);
	}

	@Path("/vendor-store/{isVendorStoreSubscribed}/{serviceId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoreListServiceWise(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("isVendorStoreSubscribed") String isVendorStoreSubscribed,
		@PathParam("serviceId") String serviceId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("serviceTypeId") String serviceTypeId,
		@QueryParam("latitude") String latitude,
		@QueryParam("longitude") String longitude,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = "";
		
		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			
			loggedInUserId = checkValidSession(sessionKeyHeader);
			if (loggedInUserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Instant todayInstantObject = DateUtils.getNowInstant();
		String currentDayOfWeekValue = DateUtils.getDayOfWeek(todayInstantObject);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String latAndLong = "ST_DWithin(vs.store_address_geolocation,ST_GeographyFromText('SRID=4326;POINT(" + longitude + " " + latitude + ")'),  " + adminSettingsModel.getRadiusStoreVisibilityString() + ")";

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(latitude, longitude);

		boolean isVendorStoreSubscribedFlag = Boolean.parseBoolean(isVendorStoreSubscribed);
		if (!isVendorStoreSubscribedFlag) {
			isVendorStoreSubscribed = null;
		}

		String considerDistance = ProjectConstants.NOT_AVAILABLE;
		if (isVendorStoreSubscribedFlag && latitude.equalsIgnoreCase("0.0")) {
			considerDistance = null;
			multicityCityRegionId = null;
		}

		if (serviceId.equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE)) {

			String tempServiceTypeId = StringUtils.validString(serviceTypeId) ? serviceTypeId : null;

			List<ServiceModel> serviceList = ServiceModel.getServiceSearch(0, 0, "%" + searchKey + "%", 0, 1, "1", "s.service_priority ASC", tempServiceTypeId);

			for (ServiceModel serviceModel : serviceList) {
				serviceId = serviceModel.getServiceId();
			}
		}

		List<UserProfileModel> brandList = UserProfileModel.getVendorBrandList(serviceId, null, start, length, "%" + searchKey + "%", latitude, longitude, latAndLong, loggedInUserId, currentDayOfWeekValue, isVendorStoreSubscribed, considerDistance, multicityCityRegionId);

		LinkedList<Map<String, Object>> vendorMapList = new LinkedList<>();

		for (UserProfileModel userProfileModel : brandList) {

			logger.info("*************************************************************************");
			logger.info("*************************************************************************");
			logger.info("\ncurrentDayOfWeekValue\t" + currentDayOfWeekValue);
			logger.info("\nuserProfileModel.getVendorStoreId()\t" + userProfileModel.getVendorStoreId());
			logger.info("\nuserProfileModel.getDistance()\t" + userProfileModel.getDistance());
			logger.info("\nadminSettings.getRadius()     \t" + (adminSettingsModel.getRadiusStoreVisibility() * ProjectConstants.KM_UNITS));

			if (userProfileModel.getDistance() <= (adminSettingsModel.getRadiusStoreVisibility() * ProjectConstants.KM_UNITS)) {

				logger.info("\nAdding store due to distance");

				boolean isVendorStoreOpen = OrderUtils.isVendorStoreOpen(todayInstantObject, userProfileModel);

				vendorMapList.add(VendorSubscriberModel.getLimitedData(userProfileModel, isVendorStoreOpen));
			} else {
				logger.info("\nIgnoring store due to distance");
			}
		}

		logger.info("*************************************************************************");
		logger.info("*************************************************************************");	

		Map<String, LinkedList<Map<String, Object>>> finalResponse = new HashMap<>();
		finalResponse.put("storeList", vendorMapList);
		return sendDataResponse(finalResponse);
	}

	@Path("/products/{vendorStoreId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorProducts(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorStoreId") String vendorStoreId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			String loggedInUserId = checkValidSession(sessionKeyHeader);
			if (loggedInUserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> outputMap = new HashMap<>();

		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorStoreModel.getVendorId());
		ServiceModel serviceModel = ServiceModel.getServiceDetailsByServiceId(vscm.getServiceId());
		List<VendorProductModel> vendorProductList = VendorProductModel.getProductListApi(vendorStoreModel.getVendorId(), Arrays.asList(vendorStoreId), start, length, "1", "%" + searchKey + "%", null);

		vendorStoreModel.setVendorStoreTimingList(new ArrayList<>());

		outputMap.put("vendorStoreModel", vendorStoreModel);
		outputMap.put("vendorProductList", vendorProductList);

		CourierUtils.getServiceSpecificSettings(outputMap, vscm, serviceModel);

		return sendDataResponse(outputMap);
	}
	
	@Path("/store-products-search")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStoresOrProductsSearch(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("latitude") String latitude,
		@QueryParam("longitude") String longitude,
		@QueryParam("start") int start,
		@QueryParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = "";
		
		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			
			loggedInUserId = checkValidSession(sessionKeyHeader);
			if (loggedInUserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}
		
		Instant todayInstantObject = DateUtils.getNowInstant();
		String currentDayOfWeekValue = DateUtils.getDayOfWeek(todayInstantObject);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String latAndLong = "ST_DWithin(vs.store_address_geolocation,ST_GeographyFromText('SRID=4326;POINT(" + longitude + " " + latitude + ")'),  " + adminSettingsModel.getRadiusStoreVisibilityString() + ")";

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(latitude, longitude);

		String isVendorStoreSubscribed = "";
		boolean isVendorStoreSubscribedFlag = Boolean.parseBoolean("false");
		if (!isVendorStoreSubscribedFlag) {
			isVendorStoreSubscribed = null;
		}
		
		String considerDistance = ProjectConstants.NOT_AVAILABLE;
		if (isVendorStoreSubscribedFlag && latitude.equalsIgnoreCase("0.0")) {
			considerDistance = null;
			multicityCityRegionId = null;
		}

		List<UserProfileModel> brandList  = new ArrayList<>();
		
		if (searchKey != null && !"".equals(searchKey)) {
			
			brandList = UserProfileModel.getVendorStoresListForApiSearch(start, length, "%" + searchKey + "%", latitude, longitude, latAndLong, loggedInUserId, currentDayOfWeekValue, isVendorStoreSubscribed, considerDistance, multicityCityRegionId);
		}

		LinkedList<Map<String, Object>> vendorMapList = new LinkedList<>();
		
		for (UserProfileModel userProfileModel : brandList) {

			logger.info("*************************************************************************");
			logger.info("*************************************************************************");
			logger.info("\ncurrentDayOfWeekValue\t" + currentDayOfWeekValue);
			logger.info("\nuserProfileModel.getVendorStoreId()\t" + userProfileModel.getVendorStoreId());
			logger.info("\nuserProfileModel.getDistance()\t" + userProfileModel.getDistance());
			logger.info("\nadminSettings.getRadius()     \t" + (adminSettingsModel.getRadiusStoreVisibility() * ProjectConstants.KM_UNITS));

			if (userProfileModel.getDistance() <= (adminSettingsModel.getRadiusStoreVisibility() * ProjectConstants.KM_UNITS)) {

				logger.info("\nAdding store due to distance");

				boolean isVendorStoreOpen = OrderUtils.isVendorStoreOpen(todayInstantObject, userProfileModel);

				vendorMapList.add(VendorSubscriberModel.getLimitedData(userProfileModel, isVendorStoreOpen));
			} else {
				logger.info("\nIgnoring store due to distance");
			}
		}

		logger.info("*************************************************************************");

		Map<String, Object> finalResponse = new HashMap<>();
		finalResponse.put("storeList", vendorMapList);
		
		if (vendorMapList.size() == 0) {
			if (searchKey != null && !"".equals(searchKey)) {
				List<VendorProductModel> vendorProductList = VendorProductModel.getProductListForApiSearch(start, length, "1", "%" + searchKey + "%", null);

				finalResponse.put("productList", vendorProductList);
			} else
				finalResponse.put("productList", new ArrayList<>());
			
		} else 
			finalResponse.put("productList", new ArrayList<>());

		return sendDataResponse(finalResponse);
		
	}
}