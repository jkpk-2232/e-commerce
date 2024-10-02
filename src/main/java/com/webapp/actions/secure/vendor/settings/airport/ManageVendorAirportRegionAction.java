package com.webapp.actions.secure.vendor.settings.airport;

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
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AirportRegionModel;
import com.webapp.models.VendorAirportRegionModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-vendor-airport-regions")
public class ManageVendorAirportRegionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getManageVendorAirportRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId)
		throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (!StringUtils.validString(vendorId)) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, vendorId, loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String statusOptions = DropDownUtils.getPassengerSearchList("0");
		data.put(FieldConstants.STATUS_OPTIONS, statusOptions);

		data.put(FieldConstants.VENDOR_ID, vendorId);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_AIRPORT_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getManageVendorAirportRegionList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String status = dtu.getRequestParameter(FieldConstants.STATUS);
		String regionList = dtu.getRequestParameter(FieldConstants.REGION_LIST);
		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		String onOffStatusForCheck = null;
		boolean onOffStatus = true;

		if (status.equals("1")) {
			onOffStatusForCheck = "YES";
			onOffStatus = true;
		} else if (status.equals("2")) {
			onOffStatusForCheck = "YES";
			onOffStatus = false;
		}

		List<String> regionIds = DropDownUtils.getUserAccessWiseRegionListDatatable(regionList, vendorId, UserRoles.VENDOR_ROLE);

		int total = VendorAirportRegionModel.getVendorAirportRegionCount(regionIds, onOffStatusForCheck, onOffStatus, vendorId);
		List<VendorAirportRegionModel> airportRegionModelList = VendorAirportRegionModel.getVendorAirportRegionSearchDatatable(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), regionIds, onOffStatusForCheck, onOffStatus,
					vendorId);

		int count = dtu.getStartInt();

		for (VendorAirportRegionModel airportRegionModel : airportRegionModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			AirportRegionModel airportRegionDetails = AirportRegionModel.getAirportRegionDetailsById(airportRegionModel.getAirportRegionId());

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(airportRegionModel.getAirportRegionId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(airportRegionModel.getAliasName());
			dtuInnerJsonArray.put(airportRegionModel.getAddress());
			dtuInnerJsonArray.put(airportRegionModel.getCityDisplayName());

			if (airportRegionDetails.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_VENDOR_AIRPORT_REGION_URL + "?vendorAirportRegionId=" + airportRegionModel.getVendorAirportRegionId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? airportRegionModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_AIRPORT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}