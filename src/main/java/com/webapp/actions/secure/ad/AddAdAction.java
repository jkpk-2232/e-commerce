package com.webapp.actions.secure.ad;

import java.io.IOException;
import java.lang.reflect.Type;
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
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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
import com.webapp.models.AdRequestModel;
import com.webapp.models.MediaRequestModel;
import com.webapp.models.ResolutionModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorDetailsModel;

@Path("/add-ad")
public class AddAdAction extends BusinessAction {
	
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAds (
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

		String resolutionOptions = "";
		
		Map<String, Object> resolutionObjects = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_RESOLUTIONS);
		
		if (resolutionObjects != null) {
			ObjectMapper mapper = new ObjectMapper();

			List<ResolutionModel> resolutionList = mapper.convertValue(resolutionObjects.get("resolutions"), new TypeReference<List<ResolutionModel>>() {});

			resolutionOptions = DropDownUtils.getResolutionOptions(resolutionList, "");
		}

		
		data.put(FieldConstants.RESOLUTION_OPTIONS, resolutionOptions);

		
		String mediaTypeOptions = DropDownUtils.getMediaTypeOptions("");
		data.put(FieldConstants.MEDIA_TYPE_OPTIONS, mediaTypeOptions);

		data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADS_URL);
		data.put("adModel", "");

		return loadView(UrlConstants.JSP_URLS.ADD_AD_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addADPost (
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@FormParam("adModel") String adModel
			) throws ServletException, IOException {
	//@formatter:on
		
		preprocessRequestNewTheme(request, response);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		Type listType = new TypeToken<List<AdRequestModel>>() {}.getType();
		
		Gson gson = new Gson();
		List<AdRequestModel> adsList = gson.fromJson(adModel, listType);
		
		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(adsList.get(0).getVendorId());
		
		VendorDetailsModel vDetails = new VendorDetailsModel();

		if (userProfileModel != null) {
			vDetails.setVendor_id(userProfileModel.getUserId());
			vDetails.setVendor_name(userProfileModel.getVendorBrandName());
			vDetails.setContact_number(userProfileModel.getPhoneNo());
			vDetails.setContact_email(userProfileModel.getEmail());
		}

		List<MediaRequestModel> mediaList = new ArrayList<>();

		for (AdRequestModel ads : adsList) {
			MediaRequestModel mediaReqModel = new MediaRequestModel();
			mediaReqModel.setMedia_title(ads.getMediaTitle());
			mediaReqModel.setMedia_type(ads.getMediaType());
			mediaReqModel.setUrl(ads.getUrl());
			mediaReqModel.setFormat(ads.getFormat());
			mediaReqModel.setDuration(ads.getDuration());
			mediaReqModel.setResolution_id(ads.getResolution_id());
			mediaList.add(mediaReqModel);
		}

		Map<String, Object> map = new HashMap<>();

		map.put("ad_title", adsList.get(0).getAd_title());
		map.put("vendor_data", vDetails);
		map.put("media_data", mediaList);

		HttpURLConnectionUtils.sendPOST(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.ADD_NEW_AD, map);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ADS_URL);
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_AD_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
