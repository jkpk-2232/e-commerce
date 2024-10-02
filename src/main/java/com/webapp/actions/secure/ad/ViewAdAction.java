package com.webapp.actions.secure.ad;

import java.io.IOException;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.HttpURLConnectionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdRequestModel;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CategoryGroupModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.MediaModel;
import com.webapp.models.OrderModel;

@Path("/view-ad")
public class ViewAdAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getViewAds(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("ad_id") String adId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		Map<String, Object> responseString;
		try {
			responseString = HttpURLConnectionUtils.sendGET(WebappPropertyUtils.LED_BASE_URL + UrlConstants.LED_URLS.VIEW_AD + "?ad_id=" + "65d481448743b959c6443463");
			ObjectMapper mapper = new ObjectMapper();

			AdRequestModel adModel = mapper.convertValue(responseString.get("ad_data"), new TypeReference<AdRequestModel>() {});
			
			List<MediaModel> mediaModelList = mapper.convertValue(responseString.get("media_data"), new TypeReference<List<MediaModel>>() {});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return loadView(UrlConstants.JSP_URLS.VIEW_ADS_JSP);
	}
	

}
