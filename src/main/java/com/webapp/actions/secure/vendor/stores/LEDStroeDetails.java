package com.webapp.actions.secure.vendor.stores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.HttpURLConnectionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.CategoryGroupModel;
import com.webapp.models.CityModel;
import com.webapp.models.LocationModel;
import com.webapp.models.StoreCategorieModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorStoreModel;

@Path("/led-store-details")
public class LEDStroeDetails extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response geLEDStoreDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId,
		@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);

		try {
			Map<String, Object> responseObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_CATEGORY_GROUPS);

			ObjectMapper mapper = new ObjectMapper();
			
			List<CategoryGroupModel> categoryGroupList = new ArrayList<>();
			String categoryGroupOptions = ""; 

			if (responseObjects != null) {
				categoryGroupList = mapper.convertValue(responseObjects.get("cat_groups"), new TypeReference<List<CategoryGroupModel>>() {});
				categoryGroupOptions = DropDownUtils.getCategoryGroupOptions(categoryGroupList, "");
			}
			data.put(FieldConstants.CATEGORY_GROUP_OPTIONS, categoryGroupOptions);

			if (categoryGroupList.size() > 0) {
				Map<String, Object> storeCategoryObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_STORE_CATEGORIES + "?cat_group_id=" + categoryGroupList.get(0).get_id());
				String storeCategoryOptions = "";
				
				if (storeCategoryObjects != null) {
					List<StoreCategorieModel> storeCategoryList = mapper.convertValue(storeCategoryObjects.get("store_categories"), new TypeReference<List<StoreCategorieModel>>() {});

					storeCategoryOptions = DropDownUtils.getStoreCategoryListOption(storeCategoryList);
				}
				
				data.put(FieldConstants.STORE_CATEGORY_OPTIONS, storeCategoryOptions);
			}

			Map<String, Object> citiesObject = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_CITIES);

			List<CityModel> citiesList = new ArrayList<>();
			String cityOptions = "";
			
			if (citiesObject != null) {
				citiesList = mapper.convertValue(citiesObject.get("cities"), new TypeReference<List<CityModel>>() {});
				
				cityOptions = DropDownUtils.getCityOptions(citiesList, "", null);
			}

			data.put(FieldConstants.CITY_OPTIONS, cityOptions);

			if (citiesList.size() > 0) {
				Map<String, Object> locationObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_LOCATIONS + "?city_id=" + citiesList.get(0).get_id());

				String locationOptions = "";
				if (locationObjects != null) {
					List<LocationModel> locationList = mapper.convertValue(locationObjects.get("locations"), new TypeReference<List<LocationModel>>() {
					});

					locationOptions = DropDownUtils.getLocationListOption(locationList, false, "");
				}
				

				data.put(FieldConstants.LOCATION_OPTIONS, locationOptions);
			}

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);
			} else {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + vendorId);
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loadView(UrlConstants.JSP_URLS.LED_STORE_DETAILS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		@FormParam(FieldConstants.CATEGORY_GROUP) String categoryGroupId,
		@FormParam(FieldConstants.STORE_CATEGROY) String storeCategoryId,
		@FormParam(FieldConstants.CITY) String cityId,
		@FormParam(FieldConstants.LOCATION) String locationId
		) throws ServletException, SQLException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}


		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(vendorId);

		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);

		Map<String, String> vendorMap = new HashMap<>();
		vendorMap.put("vendor_id", userProfileModel.getUserId());
		vendorMap.put("vendor_name", userProfileModel.getVendorBrandName());
		vendorMap.put("contact_number", userProfileModel.getPhoneNoCode() + userProfileModel.getPhoneNo());
		vendorMap.put("contact_email", userProfileModel.getEmail());

		Map<String, Object> map = new HashMap<>();

		map.put("store_category", storeCategoryId);
		map.put("store_id", vendorStoreId);
		map.put("store_name", vendorStoreModel.getStoreName());
		map.put("reg_number", null);
		map.put("lat_long", vendorStoreModel.getStoreAddressLat() + "," + vendorStoreModel.getStoreAddressLng());
		map.put("location_id", locationId);
		map.put("store_address", vendorStoreModel.getStoreAddress());
		map.put("vendor_data", vendorMap);

		try {
			HttpURLConnectionUtils.sendPOST(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.ADD_NEW_STORE, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + vendorId);
		}
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.LED_STORE_DETAILS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
