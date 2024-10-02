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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.SurgePriceModel;

@Path("/edit-surge-price")
public class EditSurgePriceAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadEditSurgePriceSettingsGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SURGE_PRICE_ID) String surgePriceId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		SurgePriceModel surgePriceModel = SurgePriceModel.getSurgePriceDetailsById(surgePriceId);

		data.put(FieldConstants.SURGE_PRICE_ID, surgePriceModel.getSurgePriceId());

		String regionListOptions = DropDownUtils.getMultiCityRegionOptions(surgePriceModel.getMulticityCityRegionId(), "1");
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String surgeFilterOptions = DropDownUtils.getSurgePriceOptions(surgePriceModel.getSurgePrice() + "");
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);

		data.put(FieldConstants.START_TIME_HOUR_MINS, DateUtils.dbTimeStampToSesionDate(surgePriceModel.getStartTime(), TimeZoneUtils.getTimeZone(), DateUtils.TIME_FORMAT));
		data.put(FieldConstants.END_TIME_HOUR_MINS, DateUtils.dbTimeStampToSesionDate(surgePriceModel.getEndTime(), TimeZoneUtils.getTimeZone(), DateUtils.TIME_FORMAT));

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_SURGE_PRICE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response loadEditSurgePriceSettingsPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.SURGE_PRICE_ID) String surgePriceId,
		@FormParam(FieldConstants.REGION_LIST) String regionList,
		@FormParam(FieldConstants.SURGE_FILTER) String surgeFilter,
		@FormParam(FieldConstants.START_TIME_HOUR_MINS) String startTimeHourMins,
		@FormParam(FieldConstants.END_TIME_HOUR_MINS) String endTimeHourMins
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.SURGE_PRICE_ID, surgePriceId);

		String regionListOptions = DropDownUtils.getMultiCityRegionOptions(regionList, "1");
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String surgeFilterOptions = DropDownUtils.getSurgePriceOptions(surgeFilter + "");
		data.put(FieldConstants.SURGE_FILTER_OPTIONS, surgeFilterOptions);
		
		data.put(FieldConstants.START_TIME_HOUR_MINS, startTimeHourMins);
		data.put(FieldConstants.END_TIME_HOUR_MINS, endTimeHourMins);

		int startTimeHourMinsHH = StringUtils.intValueOf(startTimeHourMins.split(":")[0]);
		int startTimeHourMinsMM = StringUtils.intValueOf(startTimeHourMins.split(":")[1]);

		int endTimeHourMinsHH = StringUtils.intValueOf(endTimeHourMins.split(":")[0]);
		int endTimeHourMinsMM = StringUtils.intValueOf(endTimeHourMins.split(":")[1]);

		boolean hasErrors = false;

		boolean isNextDay = false;

		if (startTimeHourMinsHH > endTimeHourMinsHH) {

			isNextDay = true;

		} else if (startTimeHourMinsHH == endTimeHourMinsHH) {

			if (startTimeHourMinsMM > endTimeHourMinsMM) {

				isNextDay = true;
			}
		}

		long startTime = DateUtils.getTimeInMillisForSurge(startTimeHourMinsHH, startTimeHourMinsMM, false, TimeZoneUtils.getTimeZone()); // isNetDay is always false
		long endTime = DateUtils.getTimeInMillisForSurge(endTimeHourMinsHH, endTimeHourMinsMM, isNextDay, TimeZoneUtils.getTimeZone());

		List<SurgePriceModel> surgePriceModelList = SurgePriceModel.getSurgePriceListByMulticityCityRegionId(regionList);

		if (surgePriceModelList != null) {

			boolean isValidSlot = false;

			for (SurgePriceModel surgePriceModel : surgePriceModelList) {

				if (!surgePriceId.equals(surgePriceModel.getSurgePriceId())) {

					if ((startTime < surgePriceModel.getStartTime() && endTime < surgePriceModel.getStartTime()) || (startTime > surgePriceModel.getEndTime() && endTime > surgePriceModel.getEndTime())) {

					} else {

						isValidSlot = true;
						break;
					}
				}
			}

			if (isValidSlot) {

				hasErrors = true;
				data.put("errorSurgeTimeSlotConflictInOtherSlot", messageForKeyAdmin("errorSurgeTimeSlotConflictInOtherSlot"));
			}
		}

		if (hasErrors) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_SURGE_PRICE_JSP);
		}

		SurgePriceModel surgePriceModel = new SurgePriceModel();
		surgePriceModel.setSurgePriceId(surgePriceId);
		surgePriceModel.setMulticityCityRegionId(regionList);
		surgePriceModel.setSurgePrice(StringUtils.doubleValueOf(surgeFilter));
		surgePriceModel.setStartTime(startTime);
		surgePriceModel.setEndTime(endTime);
		surgePriceModel.updateSurgePriceDetails(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SURGE_PRICE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_SURGE_PRICE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}