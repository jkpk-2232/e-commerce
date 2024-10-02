package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CitySurgeModel;

@Path("/edit-city-surge")
public class EditCitySurgeAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadEditSurgePriceSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.CITY_SURGE_ID) String citySurgeId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		CitySurgeModel citySurgeModel = CitySurgeModel.getCitySurgeByCitySurgeId(citySurgeId);

		data.put(FieldConstants.CITY_SURGE_ID, citySurgeId);
		data.put(FieldConstants.CITY_NAME, citySurgeModel.getCityName());

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(citySurgeModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String radiusOptions = DropDownUtils.getAdminAreaRadiusStartsWithMinRadius(adminSettingsModel.getRadius() + 1, (long) citySurgeModel.getRadius() + 1 + "");
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		String surgeFilterOptions = DropDownUtils.getSurgePriceOptions(citySurgeModel.getSurgeRate() + "");
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CITY_SURGE_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_CITY_SURGE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addCitySurge(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.CITY_SURGE_ID) String citySurgeId,
		@FormParam(FieldConstants.SURGE_FILTER) String surgeFilter,
		@FormParam(FieldConstants.RADIUS) String radius
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		CitySurgeModel citySurgeModel = CitySurgeModel.getCitySurgeByCitySurgeId(citySurgeId);

		data.put(FieldConstants.CITY_SURGE_ID, citySurgeId);
		data.put(FieldConstants.CITY_NAME, citySurgeModel.getCityName());

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(citySurgeModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String radiusOptions = DropDownUtils.getAdminAreaRadiusStartsWithMinRadius(Long.parseLong(radius), radius);
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		String surgeFilterOptions = DropDownUtils.getSurgePriceOptions(surgeFilter);
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		if (CitySurgeModel.getCitySurgeByRadiusAndRegionId(StringUtils.doubleValueOf(radius), citySurgeModel.getMulticityCityRegionId(), null) != null) {
			data.put("radiusError", "Surge for this radius already exist.");
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CITY_SURGE_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_CITY_SURGE_JSP);
		}

		citySurgeModel.setSurgeRate(StringUtils.doubleValueOf(surgeFilter));
		citySurgeModel.setRadius(StringUtils.doubleValueOf(radius));
		citySurgeModel.updateCitySurge(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_CITY_SURGE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_CITY_SURGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}