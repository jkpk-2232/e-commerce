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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CitySurgeModel;
import com.webapp.models.MulticityCityRegionModel;

@Path("/add-city-surge")
public class AddCitySurgeAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadAddCitySurgeSettings(
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

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		data.put("minRadius", StringUtils.valueOf(adminSettingsModel.getRadius()));

		String radiusOptions = DropDownUtils.getAdminAreaRadiusStartsWithMinRadius(adminSettingsModel.getRadius() + 1, adminSettingsModel.getRadius() + 1 + "");
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		String surgeFilterOptions = DropDownUtils.getSurgePriceOptions("");
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CITY_SURGE_URL);
		return loadView(UrlConstants.JSP_URLS.ADD_CITY_SURGE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCitySurgeList(
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
		String regionList = dtu.getRequestParameter(FieldConstants.REGION_LIST);

		List<String> regionIds = DropDownUtils.getUserAccessWiseRegionListDatatable(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));

		int total = CitySurgeModel.getAllCitySurgeCount(regionIds);
		List<CitySurgeModel> citySurgeModelList = CitySurgeModel.getAllCitySurgeByRegionyIds(dtu.getStartInt(), dtu.getLengthInt(), regionIds);

		int count = dtu.getStartInt();

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		long maxRadius = adminSettingsModel.getRadius();
		double maxSurge = 1;

		for (CitySurgeModel citySurgeModel : citySurgeModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(citySurgeModel.getCitySurgeId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(citySurgeModel.getCityName());
			dtuInnerJsonArray.put(citySurgeModel.getRadius());

			maxRadius = (long) ((maxRadius < citySurgeModel.getRadius()) ? citySurgeModel.getRadius() : maxRadius);

			maxSurge = ((maxSurge < citySurgeModel.getSurgeRate()) ? citySurgeModel.getSurgeRate() : maxSurge);

			dtuInnerJsonArray.put(citySurgeModel.getSurgeRate() + " x");

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		MulticityCityRegionModel mcrm = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(regionList);
		String radiusOptions = DropDownUtils.getAdminAreaRadiusStartsWithMinRadius(maxRadius, String.valueOf(maxRadius));
		String surgePriceOptions = DropDownUtils.getSurgeRateOptionsStartWithMin(maxSurge, "");

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? citySurgeModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		dtuJsonObject.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);
		dtuJsonObject.put(FieldConstants.SURGE_FILTER_OPTIONS, surgePriceOptions);
		dtuJsonObject.put(FieldConstants.CITY_NAME, mcrm.getCityDisplayName());

		return sendDataResponse(dtuJsonObject.toString());
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addCitySurge(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.REGION_LIST) String regionList,
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

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String radiusOptions = DropDownUtils.getAdminAreaRadiusStartsWithMinRadius(Long.parseLong(radius), radius);
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		String surgeFilterOptions = DropDownUtils.getSurgePriceOptions(surgeFilter);
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		if (CitySurgeModel.getCitySurgeByRadiusAndRegionId(StringUtils.doubleValueOf(radius), regionList, null) != null) {
			data.put("radiusError", "Surge for this radius already exist.");
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CITY_SURGE_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_CITY_SURGE_JSP);
		}

		CitySurgeModel citySurgeModel = new CitySurgeModel();

		MulticityCityRegionModel regionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(regionList);
		citySurgeModel.setMulticityCityRegionId(regionList);
		citySurgeModel.setCityName(regionModel.getCityDisplayName());
		citySurgeModel.setSurgeRate(StringUtils.doubleValueOf(surgeFilter));
		citySurgeModel.setRadius(StringUtils.doubleValueOf(radius));
		citySurgeModel.addCitySurge(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_CITY_SURGE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_CITY_SURGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}