package com.webapp.actions.secure.airport;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AirportRegionModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-airport-regions")
public class ManageAirportRegionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAirportRegions(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String statusOptions = DropDownUtils.getPassengerSearchList("0");
		data.put(FieldConstants.STATUS_OPTIONS, statusOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_AIRPORT_REGION_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_AIRPORT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAirportRegionsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String status = dtu.getRequestParameter(FieldConstants.STATUS);
		String regionList = dtu.getRequestParameter(FieldConstants.REGION_LIST);

		String onOffStatusForCheck = null;

		boolean onOffStatus = true;

		if (status.equals("1")) {
			onOffStatusForCheck = "YES";
			onOffStatus = true;
		} else if (status.equals("2")) {
			onOffStatusForCheck = "YES";
			onOffStatus = false;
		}

		List<String> regionIds = DropDownUtils.getUserAccessWiseRegionListDatatable(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));

		int total = AirportRegionModel.getAirportRegionCount(dtu.getGlobalSearchStringWithPercentage(), regionIds, onOffStatusForCheck, onOffStatus);
		List<AirportRegionModel> airportRegionModelList = AirportRegionModel.getAirportRegionSearchDatatable(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), regionIds, onOffStatusForCheck, onOffStatus);

		int count = dtu.getStartInt();

		for (AirportRegionModel airportRegionModel : airportRegionModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(airportRegionModel.getAirportRegionId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(airportRegionModel.getAliasName());
			dtuInnerJsonArray.put(airportRegionModel.getAddress());
			dtuInnerJsonArray.put(airportRegionModel.getCityDisplayName());

			if (airportRegionModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_AIRPORT_REGION_URL + "?airportRegionId=" + airportRegionModel.getAirportRegionId())));

			if (airportRegionModel.isActive()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_ACTIVATE_DEACTIVATE_URL + "?airportRegionId=" + airportRegionModel.getAirportRegionId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_ACTIVATE_DEACTIVATE_URL + "?airportRegionId=" + airportRegionModel.getAirportRegionId() + "&currentStatus=deactive")));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? airportRegionModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/activate-deactivate-airport-region")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response activateDeactivateAirportRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus,
		@QueryParam(FieldConstants.AIRPORT_REGION_ID) String airportRegionId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AirportRegionModel airportRegionModel = new AirportRegionModel();

		airportRegionModel.setAirportRegionId(airportRegionId);
		if ("active".equals(currentStatus)) {
			airportRegionModel.setActive(false);
		} else {
			airportRegionModel.setActive(true);
		}

		airportRegionModel.updateActiveDeactive();

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_AIRPORT_REGION_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_AIRPORT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}