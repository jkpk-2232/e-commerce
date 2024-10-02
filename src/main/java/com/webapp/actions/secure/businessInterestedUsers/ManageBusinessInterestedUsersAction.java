package com.webapp.actions.secure.businessInterestedUsers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.BrandModel;
import com.webapp.models.BusinessInterestedUsersModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-business-interested-users")
public class ManageBusinessInterestedUsersAction extends BusinessAction {
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getBusinessInterestedUsers(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on
		
		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}


		return loadView(UrlConstants.JSP_URLS.MANAGE_BUSINESS_INTERESTED_USERS_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBusinessInterestedUsersList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);


		int total = BusinessInterestedUsersModel.getBusinessInterestedUsersCount(dtu.getStartDatelong(), dtu.getEndDatelong());
		
		List<String> userIdList = null;
		
		List<BusinessInterestedUsersModel> businessUsersList = BusinessInterestedUsersModel.getBusinessInterestedUsersSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt());

		int count = dtu.getStartInt();

		for (BusinessInterestedUsersModel businessUserModel : businessUsersList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(businessUserModel.getBusinessInterestedUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(businessUserModel.getName());
			dtuInnerJsonArray.put(businessUserModel.getEmail());
			dtuInnerJsonArray.put(businessUserModel.getPhoneNo());
			dtuInnerJsonArray.put(businessUserModel.getCity());
			//dtuInnerJsonArray.put(businessUserModel.getBusinessType());
			if ("erp".equals(businessUserModel.getBusinessType())) {
				dtuInnerJsonArray.put("brands");
			}else {
				dtuInnerJsonArray.put(businessUserModel.getBusinessType());
			}
			dtuInnerJsonArray.put(businessUserModel.getVechicleType());
			dtuInnerJsonArray.put(businessUserModel.getBusinessCategory());
			dtuInnerJsonArray.put(businessUserModel.getStoreName());
			dtuInnerJsonArray.put(businessUserModel.getNoOfStores());
			dtuInnerJsonArray.put(businessUserModel.getBrandName());
			dtuInnerJsonArray.put(businessUserModel.getNoOfOutlets());
			
			
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = BusinessInterestedUsersModel.getBusinessUserSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_BUSINESS_INTERESTED_USERS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
	
	

}
