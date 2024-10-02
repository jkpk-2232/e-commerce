package com.webapp.actions.secure.campaign;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.HttpURLConnectionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdModel;
import com.webapp.models.CityModel;
import com.webapp.models.LEDDeviceModel;
import com.webapp.models.LocationModel;
import com.webapp.models.StoreModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorDetailsModel;

@Path("/add-campaign")
public class AddCampaignAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddCampaign (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			data.put(FieldConstants.VENDOR_ID, loginSessionMap.get(LoginUtils.USER_ID));

		} else {
			
			List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

			String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String adOptions = "";
		
		Map<String, Object> adObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_ADS);

		if (adObjects != null) {
			List<AdModel> adList = mapper.convertValue(adObjects.get("data"), new TypeReference<List<AdModel>>() {});

			adOptions = DropDownUtils.getAdOptions(adList, null);
		}
		data.put(FieldConstants.AD_OPTIONS, adOptions);

		Map<String, Object> citiesObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_CITIES);
		
		String cityOptions = "";
		
		if (citiesObjects != null) {
			List<CityModel> citiesList = mapper.convertValue(citiesObjects.get("cities"), new TypeReference<List<CityModel>>() {});

			cityOptions = DropDownUtils.getCityOptions(citiesList, "All Cities", "All Cities");
		}
		data.put(FieldConstants.CITY_OPTIONS, cityOptions);

		String targetSpecificOptions = DropDownUtils.getTargetSpecificOptions("All");
		data.put("targetSpecificOptions", targetSpecificOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CAMPAIGNS_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_CAMPAIGN_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddCampaign (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.CAMPAIGN_NAME) String campaignName,
		@FormParam(FieldConstants.SCHEDULE_START_DATE) String scheduleStartDate,
		@FormParam(FieldConstants.SCHEDULE_START_TIME) String scheduleStartTime,
		@FormParam(FieldConstants.SCHEDULE_END_DATE) String scheduleEndDate,
		@FormParam(FieldConstants.SCHEDULE_END_TIME) String scheduleEndTime,
		@FormParam(FieldConstants.BUDGET) String budget,
		@FormParam(FieldConstants.AD) List<String> adList,
		@FormParam(FieldConstants.CITY) String city,
		@FormParam(FieldConstants.LOCATION) String location,
		@FormParam(FieldConstants.STORE) String store,
		@FormParam(FieldConstants.DEVICE) String device,
		@FormParam(FieldConstants.TARGET_SPECIFIC) String targetSpecific
		) throws ServletException, SQLException, IOException {
	//@formatter:on
		

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		data.put(FieldConstants.AD, "");
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			data.put(FieldConstants.VENDOR_ID, loginSessionMap.get(LoginUtils.USER_ID));

		} else {
			
			List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

			String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, null, vendorId);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		ObjectMapper mapper = new ObjectMapper();
		
		Map<String, Object> responseObject = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_ADS);

		String adOptions = "";
		
		if (responseObject != null) {
			List<AdModel> adsList = mapper.convertValue(responseObject.get("data"), new TypeReference<List<AdModel>>() {});

			adOptions = DropDownUtils.getAdOptions(adsList, adList);
		}
		
		data.put(FieldConstants.AD_OPTIONS, adOptions);

		Map<String, Object> citiesObject = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_CITIES);

		List<CityModel> citiesList = new ArrayList<>();
		
		if (citiesObject != null) {
			citiesList = mapper.convertValue(citiesObject.get("cities"), new TypeReference<List<CityModel>>() {});
		}
		

		String cityOptions = "";
		
		if (city != null) {
			if (city.contains("All")) {
				cityOptions = DropDownUtils.getCityOptions(citiesList, city, "All Cities");
			} else {
				cityOptions = DropDownUtils.getCityOptions(citiesList, city, null);
			}
		}
		
		data.put(FieldConstants.CITY_OPTIONS, cityOptions);
		
		String targetSpecificOptions = "";
		
		if (targetSpecific != null) {
			if (targetSpecific.contains("All")) {
				targetSpecificOptions = DropDownUtils.getTargetSpecificOptions("All");
			} else {
				targetSpecificOptions = DropDownUtils.getTargetSpecificOptions(targetSpecific);
			}
		}
		
		data.put(FieldConstants.TARGET_SPECIFIC_OPTIONS, targetSpecificOptions);
		
		String locationOptions = "";
		if (location != null) {
			if (!city.contains("All")) {
				if (location.contains("All")) {
					Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_LOCATIONS + "?city_id=" + city);

					List<LocationModel> locationList = mapper.convertValue(locationsObjects.get("locations"), new TypeReference<List<LocationModel>>() {
					});

					locationOptions = DropDownUtils.getLocationListOption(locationList, true, "");
				} else {
					Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_LOCATIONS + "?city_id=" + city);

					List<LocationModel> locationList = mapper.convertValue(locationsObjects.get("locations"), new TypeReference<List<LocationModel>>() {
					});

					locationOptions = DropDownUtils.getLocationListOption(locationList, true, location);

				}

			}
		}
		data.put(FieldConstants.LOCATION_OPTIONS, locationOptions);
		
		String storeOptions = "";
		if (store != null) {
			if (!location.contains("All")) {
				if (store.contains("All")) {
					Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_STORES_BY_LOCATION + "?loc_id=" + location);

					List<StoreModel> storeList = mapper.convertValue(locationsObjects.get("stores"), new TypeReference<List<StoreModel>>() {
					});

					storeOptions = DropDownUtils.getStoreListOption(storeList, "All Stores", "");
				} else {
					Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_STORES_BY_LOCATION + "?loc_id=" + location);

					List<StoreModel> storeList = mapper.convertValue(locationsObjects.get("stores"), new TypeReference<List<StoreModel>>() {
					});

					storeOptions = DropDownUtils.getStoreListOption(storeList, "All Stores", store);

				}

			}
		}
		data.put(FieldConstants.STORE_OPTIONS, storeOptions);
		
		String deviceOptions = "";
		if (device != null) {
			if (!store.contains("All")) {
				if (device.contains("All")) {
					
					Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_DEVICES_BY_STORE + "?store_id=" + store);

					List<LEDDeviceModel> deviceList = mapper.convertValue(locationsObjects.get("devices"), new TypeReference<List<LEDDeviceModel>>() {
					});

					deviceOptions = DropDownUtils.getDeviceListOption(deviceList,"All Devices", "");
				} else {
					
					Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_DEVICES_BY_STORE + "?store_id=" + store);

					List<LEDDeviceModel> deviceList = mapper.convertValue(locationsObjects.get("devices"), new TypeReference<List<LEDDeviceModel>>() {
					});

					deviceOptions = DropDownUtils.getDeviceListOption(deviceList,"All Devices", device);

				}

			}
		}
		data.put(FieldConstants.DEVICE_OPTIONS, deviceOptions);
		

		data.put(FieldConstants.CAMPAIGN_NAME, campaignName);
		data.put(FieldConstants.SCHEDULE_START_DATE, scheduleStartDate);
		data.put(FieldConstants.SCHEDULE_START_TIME, scheduleStartTime);
		data.put(FieldConstants.SCHEDULE_END_DATE, scheduleEndDate);
		data.put(FieldConstants.SCHEDULE_END_TIME, scheduleEndTime);
		data.put(FieldConstants.BUDGET, budget);
		
		
		if (hasErrors(adList)) {
			
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CAMPAIGNS_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_CAMPAIGN_JSP);
			
		}
		
		Map<String, Object> map = new HashMap<>();
		
		UserProfileModel userProfileModel =  UserProfileModel.getUserAccountDetailsById(vendorId);
	        
	        VendorDetailsModel vDetails = new VendorDetailsModel();
	        
	        if (userProfileModel != null) {
			vDetails.setVendor_id(userProfileModel.getUserId());
			vDetails.setVendor_name(userProfileModel.getVendorBrandName());
			vDetails.setContact_number(userProfileModel.getPhoneNo());
			vDetails.setContact_email(userProfileModel.getEmail());
		}
	        
	       String ads = String.join(",", adList);
		
		map.put("campaign_name", campaignName);
		map.put("target_specific", "All");
		map.put("schedule_start_date", scheduleStartDate);
		map.put("schedule_start_time", scheduleStartTime);
		map.put("schedule_end_date", scheduleEndDate);
		map.put("schedule_end_time", scheduleEndTime);
		
		if (city != null) {
			if (city.contains("All")) {
				map.put("cities", "");
			} else {
				map.put("cities", city);
			}
		} else
			map.put("cities", "");
		
		if (location != null) {
			if (location.contains("All")) {
				map.put("locations", "");
			} else {
				map.put("locations", location);
			}
		} else
			map.put("locations", "");
		
		if (store != null) {
			if (store.contains("All")) {
				map.put("stores", "");
			} else {
				map.put("stores", store);
			}
		} else
			map.put("stores", "");
		
		if (device != null) {
			if (device.contains("All")) {
				map.put("devices", "");
			} else {
				map.put("devices", device);
			}
		} else
			map.put("devices", "");
		
		map.put("store_category", "");
		map.put("budget", budget);
		map.put("ads", ads);
		map.put("vendor_data", vDetails);
		
		HttpURLConnectionUtils.sendPOST(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.ADD_NEW_CAMPAIGN, map);
		
		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_CAMPAIGNS_URL);
	}
	
	public boolean hasErrors(List<String> adList) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.CAMPAIGN_NAME, BusinessAction.messageForKeyAdmin("labelCampaignName"), new MinMaxLengthValidationRule(1, 100));
		validator.addValidationMapping(FieldConstants.BUDGET, BusinessAction.messageForKeyAdmin("labelBudget"), new DecimalValidationRule());
		
		int size = 0;

		for (String string : adList) {
			
			if (StringUtils.validString(string)) {
				size++;
			}
		}

		validator.addValidationMapping(FieldConstants.AD, BusinessAction.messageForKeyAdmin("labelAD"), new RequiredListValidationRule(size));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_CAMPAIGN_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
