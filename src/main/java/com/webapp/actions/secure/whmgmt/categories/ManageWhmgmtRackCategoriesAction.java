package com.webapp.actions.secure.whmgmt.categories;

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
import com.webapp.models.WhmgmtRackCategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-rack-categories")
public class ManageWhmgmtRackCategoriesAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_RACK_CATEGORY_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_RACK_CATEGORIES_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getBrandList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);


		int total = WhmgmtRackCategoryModel.getRackCategorySearchCount("%%");
		
		List<String> userIdList = null;
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			userIdList = new ArrayList<>();
			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));
		} 
		
		List<WhmgmtRackCategoryModel> rackCategoryList = WhmgmtRackCategoryModel.getRackCategorySearch( dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt());

		int count = dtu.getStartInt();

		for (WhmgmtRackCategoryModel rackCategoryModel : rackCategoryList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(rackCategoryModel.getCategoryId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(rackCategoryModel.getCategoryName());
			dtuInnerJsonArray.put(rackCategoryModel.getChargePerSlot());
			dtuInnerJsonArray.put(rackCategoryModel.getNoOfSlots());
			dtuInnerJsonArray.put(rackCategoryModel.getNumberOfDays());

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_RACK_CATEGORY_URL + "?categoryId=" + rackCategoryModel.getCategoryId())));

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = WhmgmtRackCategoryModel.getRackCategorySearchCount(dtu.getGlobalSearchStringWithPercentage());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_RACK_CATEGORIES_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
