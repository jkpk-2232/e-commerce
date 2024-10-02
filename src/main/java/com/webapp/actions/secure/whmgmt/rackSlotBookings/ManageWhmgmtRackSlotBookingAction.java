package com.webapp.actions.secure.whmgmt.rackSlotBookings;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.DateUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.WhmgmtRackSlotBookingModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-rack-slot-booking")
public class ManageWhmgmtRackSlotBookingAction extends BusinessAction {

	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getRackSlotBookings (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(null, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
			data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

			data.put(FieldConstants.VENDOR_ID, loginSessionMap.get(LoginUtils.USER_ID));
			
		} else {
			
			String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}
		
		
		
		
		return loadView(UrlConstants.JSP_URLS.MANAGE_RACK_SLOT_BOOKING_JSP);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRackSlotBookingList (
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

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		String vendorStroeId = dtu.getRequestParameter(FieldConstants.VENDOR_STORE_ID);

		if ("0".equals(vendorId)) {
			vendorId = null;
		}
		if (vendorStroeId.isEmpty() || "-1".equals(vendorStroeId) ) {
			vendorStroeId = null;
		}
		
		int total = WhmgmtRackSlotBookingModel.getRackSlotBookingCount(vendorId, vendorStroeId);
		
		List<WhmgmtRackSlotBookingModel> rackSlotBookingList = WhmgmtRackSlotBookingModel.getRackSlotBookingSearch(vendorId, vendorStroeId, dtu.getStartInt(), dtu.getLengthInt());
		
		String timeZone = TimeZoneUtils.getTimeZone();
		int count = dtu.getStartInt();
		
		for (WhmgmtRackSlotBookingModel rackSlotBookingModel : rackSlotBookingList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(rackSlotBookingModel.getId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(rackSlotBookingModel.getRackNumber());
			dtuInnerJsonArray.put(rackSlotBookingModel.getSlotNumber());
			dtuInnerJsonArray.put(rackSlotBookingModel.getUserName());
			dtuInnerJsonArray.put(rackSlotBookingModel.getPhoneNumber());
			dtuInnerJsonArray.put(rackSlotBookingModel.getProductName());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(rackSlotBookingModel.getStartDate().toInstant().toEpochMilli(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(rackSlotBookingModel.getEndDate().toInstant().toEpochMilli(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if ("Available".equals(rackSlotBookingModel.getSlotStatus())) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelAvailable")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.INFO, "Booked"));
			}

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}
		
		
		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, total);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_RACK_SLOT_BOOKING_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
	
}
