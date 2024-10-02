package com.webapp.actions.secure.campaign;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeutils.DateUtils;
import com.utils.HttpURLConnectionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.CampaignModel;
import com.webapp.models.LEDDeviceModel;
import com.webapp.models.StoreModel;

@Path("/manage-campaigns")
public class ManageCampaignsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getCampaigns (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_CAMPAIGN_URL);
		
		return loadView(UrlConstants.JSP_URLS.MANAGE_CAMPAIGNS_JSP);
	}
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCampaignList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		
		
		DatatableUtils dtu = new DatatableUtils(request);
		
		int pageNum = 0;
		
		if (dtu.getStartInt() == 0) {
			pageNum = 1;
		}else {
			pageNum = dtu.getStartInt() / dtu.getLengthInt() ;
			pageNum = pageNum + 1;
		}
		
		
		String loggedUserId = "";
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) 
			loggedUserId = loginSessionMap.get(LoginUtils.USER_ID);
		
		int total = 0;
		
		boolean status = false;
		
		try {
			Map<String, Object> responseObject = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_CAMPAIGNS+"?page="+pageNum+"&limit="+dtu.getLengthInt()+"&vendor_id="+loggedUserId);
			
			if (responseObject != null) {
				
				status = (boolean) responseObject.get(FieldConstants.STATUS);
				
				total = (int) responseObject.get("total_records");
				
				ObjectMapper mapper = new ObjectMapper();

				List<CampaignModel> campaignModels = mapper.convertValue(responseObject.get("data"), new TypeReference<List<CampaignModel>>() {});
				
				int count = dtu.getStartInt();
				
				for (CampaignModel campaignModel : campaignModels) {
					
					count++;
					
					dtuInnerJsonArray = new JSONArray();
					dtuInnerJsonArray.put(campaignModel.get_id());
					dtuInnerJsonArray.put(count);
					dtuInnerJsonArray.put(campaignModel.getCampaignId());
					dtuInnerJsonArray.put(campaignModel.getCampaignName());
					dtuInnerJsonArray.put(campaignModel.getBudget());
					dtuInnerJsonArray.put(campaignModel.getCampaignStatus());
					dtuInnerJsonArray.put(campaignModel.getUserId().getVendorName());
					dtuInnerJsonArray.put(DateUtils.getDateTimeStringwithZeroTimeFormat(campaignModel.getCreatedDate(),DateUtils.DATE_TIME_WITH_ZERO_TIME_ZONE, DateUtils.DISPLAY_DATE_TIME_FORMAT));
					dtuInnerJsonArray.put("");
					
					dtuOuterJsonArray.put(dtuInnerJsonArray);
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, total);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	
	@Path("/get-stores")
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response getStores (
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res, 
		@QueryParam("locationId") String locationId
		) throws ServletException, IOException {
		
		preprocessRequest(req, res);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		Map<String, String> output = new HashMap<>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		String storeOptions = "";
		
		Map<String, Object> locationsObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_STORES_BY_LOCATION + "?loc_id=" + locationId);

		if (locationsObjects != null) {
			List<StoreModel> storeList = mapper.convertValue(locationsObjects.get("stores"), new TypeReference<List<StoreModel>>() {});

			storeOptions = DropDownUtils.getStoreListOption(storeList, "All Stores", "");
		}
		
		output.put(FieldConstants.STORE_OPTIONS, storeOptions);
		
		return sendDataResponse(output);
	}
	
	@Path("/get-devices")
	@GET
	@Produces({ "application/json", "application/xml" })
	public Response getDevices (
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res, 
		@QueryParam("storeId") String storeId
		) throws ServletException, IOException {

		preprocessRequest(req, res);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		Map<String, String> output = new HashMap<>();
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			
			String deviceOptions = "";
			
			Map<String, Object> deviceObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_DEVICES_BY_STORE + "?store_id=" + storeId);
			
			if (deviceObjects != null) {
				List<LEDDeviceModel> deviceList = mapper.convertValue(deviceObjects.get("devices"), new TypeReference<List<LEDDeviceModel>>() {});

				deviceOptions = DropDownUtils.getDeviceListOption(deviceList, "All Devices", "");
			}
			
			output.put(FieldConstants.DEVICE_OPTIONS, deviceOptions);
			 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendDataResponse(output);
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_CAMPAIGNS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
