package com.webapp.actions.secure.ad;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-ads")
public class ManageAdsAction extends BusinessAction {
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAds (
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

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_AD_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ADS_JSP);
	}
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdList(
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
		
		String loggedUserId = "";
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) 
			loggedUserId = loginSessionMap.get(LoginUtils.USER_ID);
		
		int total = 0;
		
		int pageNum = 0;
		
		if (dtu.getStartInt() == 0) {
			pageNum = 1;
		}else {
			pageNum = dtu.getStartInt() / dtu.getLengthInt() ;
			pageNum = pageNum + 1;
		}
		
		boolean status = false;

		try {
			Map<String, Object> responseObject = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.GET_ADS+"?page="+pageNum+"&limit="+dtu.getLengthInt()+"&vendor_id="+loggedUserId);
			
			if (responseObject != null) {
				status = (boolean) responseObject.get("status");
				
				ObjectMapper mapper = new ObjectMapper();

				List<AdModel> adModels = mapper.convertValue(responseObject.get("data"), new TypeReference<List<AdModel>>() {});
				
				total = (int) responseObject.get("total_records");
				
				int count = dtu.getStartInt();
				
				for (AdModel adModel : adModels) {
					
					count++;
					
					dtuInnerJsonArray = new JSONArray();
					dtuInnerJsonArray.put(adModel.get_id());
					dtuInnerJsonArray.put(count);
					dtuInnerJsonArray.put(adModel.getAdId());
					dtuInnerJsonArray.put(adModel.getAdTitle());
					dtuInnerJsonArray.put(DateUtils.getDateTimeStringwithZeroTimeFormat(adModel.getCreatedDate(),DateUtils.DATE_TIME_WITH_ZERO_TIME_ZONE, DateUtils.DISPLAY_DATE_TIME_FORMAT));
					dtuInnerJsonArray.put(adModel.getRemarks());
					dtuInnerJsonArray.put(adModel.getStatus());
					
					if (adModel.getIsActive()) {
						dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
					} else {
						dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
					}
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
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ADS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
