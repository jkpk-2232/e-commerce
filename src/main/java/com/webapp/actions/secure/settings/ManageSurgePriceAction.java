package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.SurgePriceModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-surge-price")
public class ManageSurgePriceAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getSurgePrice(
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

		String surgeFilterOptions = DropDownUtils.getOptionsForSurgeFilter(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, false);
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		String regionListOptions = DropDownUtils.getOptionsForegionFilter(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, "1");
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_SURGE_PRICE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_SURGE_PRICE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getSurgePriceList(
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
		String surgeFilter = dtu.getRequestParameter(FieldConstants.SURGE_FILTER);
		surgeFilter = DropDownUtils.parserForAllOptions(surgeFilter);

		String regionList = dtu.getRequestParameter(FieldConstants.REGION_LIST);
		regionList = DropDownUtils.parserForAllOptions(regionList);

		double surgePriceInDouble = 0;

		if (surgeFilter != null) {
			surgePriceInDouble = StringUtils.doubleValueOf(surgeFilter);
		}

		int total = SurgePriceModel.getSurgePriceCount(surgePriceInDouble, regionList);
		List<SurgePriceModel> surgePriceModelList = SurgePriceModel.getSurgePriceListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), surgePriceInDouble, regionList);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (SurgePriceModel surgePriceModel : surgePriceModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(surgePriceModel.getSurgePriceId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(surgePriceModel.getSurgePrice());
			dtuInnerJsonArray.put(DateUtils.getHoursAndMinutesStringFromMillis(surgePriceModel.getStartTime(), timeZone));
			dtuInnerJsonArray.put(DateUtils.getHoursAndMinutesStringFromMillis(surgePriceModel.getEndTime(), timeZone));
			dtuInnerJsonArray.put(surgePriceModel.getCityDisplayName());

			if (surgePriceModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_SURGE_PRICE_URL + "?surgePriceId=" + surgePriceModel.getSurgePriceId())));

			if (surgePriceModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_ACTIVATE_DEACTIVATE_URL + "?surgePriceId=" + surgePriceModel.getSurgePriceId() + "&status=active")));
			} else {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_ACTIVATE_DEACTIVATE_URL + "?surgePriceId=" + surgePriceModel.getSurgePriceId() + "&status=deactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_DELETE_URL + "?surgePriceId=" + surgePriceModel.getSurgePriceId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? surgePriceModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateSurgePrice(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SURGE_PRICE_ID) String surgePriceId,
		@QueryParam(FieldConstants.STATUS) String status
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		SurgePriceModel surgePriceModel = new SurgePriceModel();

		surgePriceModel.setSurgePriceId(surgePriceId);

		if ("active".equals(status)) {
			surgePriceModel.setActive(false);
		} else {
			surgePriceModel.setActive(true);
		}

		surgePriceModel.activateDeactivateSurgePrice(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_URL);
	}

	@Path("/delete")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteSurge(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SURGE_PRICE_ID) String surgePriceId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		SurgePriceModel surgePriceModel = new SurgePriceModel();
		surgePriceModel.setSurgePriceId(surgePriceId);
		surgePriceModel.deleteSurgePrice(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_SURGE_PRICE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}