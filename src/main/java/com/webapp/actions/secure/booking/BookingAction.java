package com.webapp.actions.secure.booking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.DateUtils;
import com.jeeutils.S3Utils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.OrderModel;
import com.webapp.models.TourModel;
import com.webapp.models.VendorFeedModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.VendorSubscriberModel;

@Path("/bookings")
public class BookingAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response bookingGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.BOOKING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		data.put(FieldConstants.ROLE_ID, loginSessionMap.get(LoginUtils.ROLE_ID));
		
		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			String vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			
			long currentTime = DateUtils.nowAsGmtMillisec();
			
			  int totalSubscribersCount = VendorSubscriberModel.getVendorSubscriberCount(0,
			  currentTime, vendorId, null); data.put(FieldConstants.TOTAL_SUBSCRIBERS,
			  totalSubscribersCount + "");
			  
			  int totalFeedCount = VendorFeedModel.getVendorFeedCount(0, currentTime,
			  vendorId); data.put(FieldConstants.FEEDS, totalFeedCount + "");
			  
			  int totalActiveFeedsCount = VendorFeedModel.getVendorFeedCount(0,
			  currentTime, vendorId); data.put(FieldConstants.ACTIVE_FEEDS,
			  totalActiveFeedsCount + "");
			 
			
			String vendorStoreIdOptions = DropDownUtils.getERPBrandsFilterListOptions(null, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
			
			data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);
			
			Map<String, Object> resultObject =  VendorFeedModel.getVendorFeedViewsAndLikesCount(vendorId);
			
			if (resultObject != null) {
				data.put(FieldConstants.FEED_VIEWS_COUNT, resultObject.get("feedviewscount") + "");
				data.put(FieldConstants.FEED_LIKES_COUNT, resultObject.get("feedlikescount") + "");
				data.put(FieldConstants.FEED_WISHLIST_COUNT, "0");
				data.put(FieldConstants.FEED_SHARES_COUNT, "0");
				data.put(FieldConstants.FEED_IMPRESSION_COUNT, "0");
			} else {
				data.put(FieldConstants.FEED_VIEWS_COUNT, "0");
				data.put(FieldConstants.FEED_LIKES_COUNT, "0");
				data.put(FieldConstants.FEED_WISHLIST_COUNT, "0");
				data.put(FieldConstants.FEED_SHARES_COUNT, "0");
				data.put(FieldConstants.FEED_IMPRESSION_COUNT, "0");
				
			}
			
			int commentsCount =  VendorFeedModel.getVendorFeedCommentsCountByVendor(vendorId);
			data.put(FieldConstants.COMMENTS_COUNT, commentsCount + "");
			
		} else {
			
			String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(null, assignedRegionList);
			data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
			DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

			double minimumWalletAmount = 0.0;

			if (driverWalletSettingsModel != null) {
				minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
			}

			long currentDayStartInMilliseconds = DateUtils.atStartOfDay(Instant.now());

			int totalBookingsCount = 0;
			int totalPeopleOpeneedAppCount = 0;
			int availableDriversCount = 0;

			if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				totalBookingsCount = TourModel.getVendorsTotalBookingsCountByTime(currentDayStartInMilliseconds, assignedRegionList, UserRoleUtils.getParentVendorId(loginSessionMap));

				availableDriversCount = DriverGeoLocationModel.getVendorsTotalAvailableDriver(assignedRegionList, UserRoleUtils.getParentVendorId(loginSessionMap), minimumWalletAmount, adminSettingsModel);

			} else {

				totalBookingsCount = TourModel.getTotalBookingsCountByTime(currentDayStartInMilliseconds, assignedRegionList);

				totalPeopleOpeneedAppCount = LoginUtils.getTotalPeopleOpeneedAppCount(assignedRegionList);

				availableDriversCount = DriverGeoLocationModel.getTotalAvailableDriver(assignedRegionList, minimumWalletAmount, loginSessionMap.get(LoginUtils.USER_ID), adminSettingsModel);
			}

			data.put(FieldConstants.TOTAL_BOOKINGS_COUNT, totalBookingsCount + "");
			data.put(FieldConstants.TOTAL_PEOPLE_OPENEED_APP_COUNT, totalPeopleOpeneedAppCount + "");
			data.put(FieldConstants.AVAILABLE_DRIVERS_COUNT, availableDriversCount + "");

		}

		// Second column - Static values for Active Orders, Order Value, Transportation Online
	    int activeOrdersCount = 15;  // Mock static data
	    double totalOrderValue = 5000.00;  // Mock static data
	    int transportationOnlineCount = 12;  // Mock static data
	    
	    data.put(FieldConstants.ORDER_VALUE, activeOrdersCount + "");
	    data.put(FieldConstants.TOTAL_ORDER_VALUE, totalOrderValue + "");
	    data.put(FieldConstants.TRANSPORTATION_ONLINE, transportationOnlineCount + "");
	    
	    // Third column - Static values for Transportation Value, ERP Users, Warehouse Slots Booked
	    double totalTransportationValue = 3000.00;  // Mock static data
	    int totalErpUsers = 50;  // Mock static data
	    int totalWarehouseSlotsBooked = 8;  // Mock static data
	    
	    data.put(FieldConstants.TOTAL_TRANSPORTATION_VALUE, totalTransportationValue + "");
	    data.put(FieldConstants.TOTAL_ERP_USERS, totalErpUsers + "");
	    data.put(FieldConstants.TOTAL_WAREHOUSE_SLOTS_BOOKED, totalWarehouseSlotsBooked + "");
	    

		return loadView(UrlConstants.JSP_URLS.MANAGE_BOOKINGS_JSP);
	}
	
	@GET
	@Path("/erp-products")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getErpProducts(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.BOOKING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		DatatableUtils dtu = new DatatableUtils(request);
		
		String vendorStoreId = dtu.getRequestParameter(FieldConstants.VENDOR_STORE_ID);
		
		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			VendorStoreModel vsModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
			
			
			if (vsModel != null) {
				
				List<VendorProductModel> vendorProdctList =  VendorProductModel.getBrandProductsListForDashboard(vsModel.getBrandId());
			
				if (vendorProdctList.size() > 0) {
					int count = 1;
					
					  for (VendorProductModel vendorProductModel : vendorProdctList) {
						  	dtuInnerJsonArray = new JSONArray();
							dtuInnerJsonArray.put(count);
							dtuInnerJsonArray.put(vendorProductModel.getProductName());
							dtuInnerJsonArray.put(vendorProductModel.getProductDiscountedPrice());
							dtuInnerJsonArray.put(vendorProductModel.getProductsInCart());
							dtuInnerJsonArray.put(vendorProductModel.getCartValue());
							dtuInnerJsonArray.put("");
							dtuInnerJsonArray.put("");
							dtuOuterJsonArray.put(dtuInnerJsonArray);
					  }
					 
				}
			}
			
		} 
		
		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, 0, 0);
		return sendDataResponse(dtuJsonObject.toString());
	}
	
	@GET
	@Path("/erp-dashboard")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getErpDashboard(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.BOOKING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		
		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			data.put(FieldConstants.ROLE_ID, loginSessionMap.get(LoginUtils.ROLE_ID));
			
			VendorStoreModel vsModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
			
			List<OrderModel> orderList =  OrderModel.getOnlineAndOfflineSalesDashboardByBrand(vsModel.getBrandId());
			
			JSONArray labelsList = new JSONArray();
			JSONArray onlineSaleslist = new JSONArray();
			JSONArray offlinesaleslist = new JSONArray();
			
			for (OrderModel orderModel : orderList) {
				
				if (orderModel != null) {
					
					if (orderModel.getOrderDate() != null) {
						
						offlinesaleslist.put(orderModel.getOfflineOrderCount());
						onlineSaleslist.put(orderModel.getOnlineOrderCount());
						labelsList.put(orderModel.getOrderDate());
						
					}
					
				}
			}
			
			List<String> vendorStoreIdList = VendorProductModel.getDistinctVendorStoreIdsBasedonBrandId(vsModel.getBrandId());
			
			JSONArray latList = new JSONArray();
			JSONArray lngList = new JSONArray();
			JSONArray titleList = new JSONArray();

			for (String vsId : vendorStoreIdList) {
				VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(vsId);
				if (vendorStoreModel != null) {
					latList.put(vendorStoreModel.getStoreAddressLat());
					lngList.put(vendorStoreModel.getStoreAddressLng());
					titleList.put(vendorStoreModel.getStoreName());
				}
			}
			
			data.put("labelsList", labelsList.toString());
			data.put("onlineList", onlineSaleslist.toString());
			data.put("offlineList", offlinesaleslist.toString());
			data.put("latList", latList.toString());
			data.put("lngList", lngList.toString());
			data.put("titleList", titleList.toString());
			
		}
		
		
		return sendDataResponse(data);
	}

	@GET
	@Path("/booking-details")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBookingDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TOUR_ID) String tourId
		) throws ServletException, IOException, SQLException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		DriverGeoLocationModel driverGeo = DriverGeoLocationModel.getCurrentDriverPosition(tourModel.getDriverId());

		Map<String, Object> tourDetailsMap = new HashMap<String, Object>();

		if (tourModel != null) {

			CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(tourModel.getCarTypeId());

			tourDetailsMap.put(FieldConstants.TOUR_ID, tourModel.getTourId());

			tourDetailsMap.put(FieldConstants.P_FIRST_NAME, tourModel.getpFirstName());
			tourDetailsMap.put(FieldConstants.P_LAST_NAME, tourModel.getpLastName());
			tourDetailsMap.put(FieldConstants.D_LATITUDE, tourModel.getdLatitude());
			tourDetailsMap.put(FieldConstants.D_LONGITUDE, tourModel.getdLongitude());
			tourDetailsMap.put(FieldConstants.S_LATITUDE, tourModel.getsLatitude());
			tourDetailsMap.put(FieldConstants.S_LONGITUDE, tourModel.getsLongitude());

			if (!ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(tourModel.getDriverId())) {

				tourDetailsMap.put(FieldConstants.FIRST_NAME, tourModel.getFirstName());
				tourDetailsMap.put(FieldConstants.LAST_NAME, tourModel.getLastName());
				tourDetailsMap.put(FieldConstants.PHONE_NO, MyHubUtils.formatPhoneNumber(tourModel.getPhoneNoCode(), tourModel.getPhoneNo()));
				tourDetailsMap.put(FieldConstants.PHOTO_URL, tourModel.getPhotoUrl());
				tourDetailsMap.put(FieldConstants.EMAIL, tourModel.getEmail());

			} else {

				tourDetailsMap.put(FieldConstants.FIRST_NAME, ProjectConstants.NOT_AVAILABLE);
				tourDetailsMap.put(FieldConstants.LAST_NAME, ProjectConstants.NOT_AVAILABLE);
				tourDetailsMap.put(FieldConstants.PHONE_NO, ProjectConstants.NOT_AVAILABLE);
				tourDetailsMap.put(FieldConstants.PHOTO_URL, ProjectConstants.NOT_AVAILABLE);
				tourDetailsMap.put(FieldConstants.EMAIL, ProjectConstants.NOT_AVAILABLE);
			}

			tourDetailsMap.put(FieldConstants.CAR_MODEL_TYPE_ID, tourModel.getCarTypeId());
			tourDetailsMap.put(FieldConstants.CAR_ICON, carTypeModel.getIcon());
			tourDetailsMap.put(FieldConstants.TOUR_STATUS, tourModel.getStatus());

			int driverAvgRate = TourUtils.getDriverAverageRatings(tourModel.getDriverId());

			tourDetailsMap.put(FieldConstants.RATINGS, StringUtils.valueOf(driverAvgRate));

			if (driverGeo != null) {
				tourDetailsMap.put(FieldConstants.C_LAT, driverGeo.getLatitude());
				tourDetailsMap.put(FieldConstants.C_LONG, driverGeo.getLongitude());
			} else {
				tourDetailsMap.put(FieldConstants.C_LAT, tourModel.getsLatitude());
				tourDetailsMap.put(FieldConstants.C_LONG, tourModel.getsLongitude());
			}
		}

		return sendDataResponse(tourDetailsMap);
	}

	@GET
	@Path("/booking-locations")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBookingLocation(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@Context ServletContext context,
		@QueryParam(FieldConstants.TOUR_ID) String tourId
		) throws ServletException, IOException, SQLException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		String fileName = tourId + ".csv";

		List<Map<String, Object>> locationList = new ArrayList<Map<String, Object>>();

		try {

			String bucket = WebappPropertyUtils.getWebAppProperty("imageBucket");

			File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("tempDir") + "/" + fileName);

			S3Utils.downloadFromBucket(bucket, fileName, fileToWrite);

			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			br = new BufferedReader(new FileReader(fileToWrite));

			while ((line = br.readLine()) != null) {

				String[] latLong = line.split(cvsSplitBy);
				Map<String, Object> location = new HashMap<String, Object>();

				location.put("latitude", latLong[1]);
				location.put("longitude", latLong[2]);

				locationList.add(location);
			}

			if (fileToWrite != null && fileToWrite.exists()) {
				fileToWrite.delete();
				br.close();
			}

		} catch (Exception e) {
			logger.error(e);
		}

		Map<String, Object> ouputMap = new HashMap<String, Object>();

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		if (locationList.size() == 0) {

			Map<String, Object> location = new HashMap<String, Object>();

			location.put("latitude", tourModel.getsLatitude());
			location.put("longitude", tourModel.getsLongitude());
			locationList.add(location);

			location = new HashMap<String, Object>();
			location.put("latitude", tourModel.getsLatitude());
			location.put("longitude", tourModel.getsLongitude());

			locationList.add(location);
		}

		ouputMap.put("locationList", locationList);

		return sendDataResponse(ouputMap);
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_BOOKINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}