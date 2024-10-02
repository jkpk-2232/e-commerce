package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.FeedSettingsModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-feed-settings")
public class ManageFeedSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getFeedSettingsDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_FEED_SETTINGS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_FEED_SETTINGS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getFeedSettingsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		int total = FeedSettingsModel.getFeedSettingsCountByUser(vendorId);

		List<FeedSettingsModel> feedSettingsModelList = FeedSettingsModel.getFeedSettingsListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getGlobalSearchStringWithPercentage());

		int count = dtu.getStartInt();

		for (FeedSettingsModel feedSettingsModel : feedSettingsModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(feedSettingsModel.getMulticityCityRegionId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(feedSettingsModel.getRegionName());

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_FEED_SETTINGS_URL + "?feedSettingsId=" + feedSettingsModel.getFeedSettingsId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? feedSettingsModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_FEED_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
