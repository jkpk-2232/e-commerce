package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.RideLaterSettingsModel;

@Path("/manage-ride-later-settings")
public class ManageRideLaterSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getRideLaterSettings(
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

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		data.put(FieldConstants.MIN_BOOKING_TIME, MyHubUtils.convertMinutesToHours(rideLaterSettingsModel.getMinBookingTime()));
		data.put(FieldConstants.DRIVER_JOB_REQUEST_TIME, StringUtils.valueOf(rideLaterSettingsModel.getDriverJobRequestTime()));
		data.put(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME, StringUtils.valueOf(rideLaterSettingsModel.getDriverAllocateBeforeTime()));
		data.put(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME, StringUtils.valueOf(rideLaterSettingsModel.getDriverAllocateAfterTime()));

		String hourOptions = DropDownUtils.getDaysOptionRideLater(MyHubUtils.convertMinutesToDays(rideLaterSettingsModel.getMaxBookingTime()));
		data.put(FieldConstants.HOUR_OPTIONS, hourOptions);

		data.put(FieldConstants.TAKE_BOOKING_FOR_NEXT_X_DAYS, (int) rideLaterSettingsModel.getTakeBookingForNextXDays() + "");
		data.put(FieldConstants.TAKE_BOOKING_MAX_NUMBER_ALLOWED, (int) rideLaterSettingsModel.getTakeBookingMaxNumberAllowed() + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_RIDE_LATER_SETTINGS_URL);
		return loadView(UrlConstants.JSP_URLS.MANAGE_RIDE_LATER_SETTINGS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postRideLaterSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.MIN_BOOKING_TIME) String minBookingTime,
		@FormParam(FieldConstants.HOUR) String hour,
		@FormParam(FieldConstants.DRIVER_JOB_REQUEST_TIME) String driverJobRequestTime,
		@FormParam(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME) String driverAllocateBeforeTime,
		@FormParam(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME) String driverAllocateAfterTime,
		@FormParam(FieldConstants.TAKE_BOOKING_FOR_NEXT_X_DAYS) String takeBookingForNextXDays,
		@FormParam(FieldConstants.TAKE_BOOKING_MAX_NUMBER_ALLOWED) String takeBookingMaxNumberAllowed
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String hourOptions = DropDownUtils.getDaysOptionRideLater(hour);
		data.put(FieldConstants.HOUR_OPTIONS, hourOptions);

		data.put(FieldConstants.MIN_BOOKING_TIME, StringUtils.valueOf(MyHubUtils.convertHoursToMinutes(minBookingTime, 0)));
		data.put(FieldConstants.MAX_BOOKING_TIME, StringUtils.valueOf(MyHubUtils.convertHoursToMinutes(ProjectConstants.TIME_00_00, Long.parseLong(hour))));
		data.put(FieldConstants.DRIVER_JOB_REQUEST_TIME, driverJobRequestTime);
		data.put(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME, driverAllocateBeforeTime);
		data.put(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME, driverAllocateAfterTime);

		data.put(FieldConstants.TAKE_BOOKING_FOR_NEXT_X_DAYS, takeBookingForNextXDays + "");
		data.put(FieldConstants.TAKE_BOOKING_MAX_NUMBER_ALLOWED, takeBookingMaxNumberAllowed + "");

		if (hasErrorsEnglish()) {

			data.put(FieldConstants.MIN_BOOKING_TIME, minBookingTime);
			data.put(FieldConstants.MAX_BOOKING_TIME, StringUtils.valueOf(0));

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_RIDE_LATER_SETTINGS_URL);
			return loadView(UrlConstants.JSP_URLS.MANAGE_RIDE_LATER_SETTINGS_JSP);
		}

		RideLaterSettingsModel rideLaterSettingsModel = new RideLaterSettingsModel();

		rideLaterSettingsModel.setMinBookingTime(MyHubUtils.convertHoursToMinutes(minBookingTime, 0));
		rideLaterSettingsModel.setMaxBookingTime(MyHubUtils.convertHoursToMinutes(ProjectConstants.TIME_00_00, StringUtils.longValueOf(hour)));
		rideLaterSettingsModel.setDriverJobRequestTime(StringUtils.longValueOf(driverJobRequestTime));

		rideLaterSettingsModel.setDriverAllocateBeforeTime(StringUtils.longValueOf(driverAllocateBeforeTime));
		rideLaterSettingsModel.setDriverAllocateAfterTime(StringUtils.longValueOf(driverAllocateAfterTime));
		rideLaterSettingsModel.setPassengerTourBeforeTime(StringUtils.longValueOf(driverAllocateBeforeTime));
		rideLaterSettingsModel.setPassengerTourAfterTime(StringUtils.longValueOf(driverAllocateAfterTime));
		rideLaterSettingsModel.setTakeBookingForNextXDays(StringUtils.intValueOf(takeBookingForNextXDays));
		rideLaterSettingsModel.setTakeBookingMaxNumberAllowed(StringUtils.intValueOf(takeBookingMaxNumberAllowed));

		rideLaterSettingsModel.updateRideLaterSettings();

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_RIDE_LATER_SETTINGS_URL);
	}

	public boolean hasErrorsEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.MIN_BOOKING_TIME, BusinessAction.messageForKeyAdmin("labelMinimumBookingTime"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.MIN_BOOKING_TIME, BusinessAction.messageForKeyAdmin("labelMinimumBookingTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.MIN_BOOKING_TIME, BusinessAction.messageForKeyAdmin("labelMinimumBookingTime"), new MinMaxValueValidationRule(0, 9999));

		validator.addValidationMapping(FieldConstants.DRIVER_JOB_REQUEST_TIME, BusinessAction.messageForKeyAdmin("labelDriverJobRequestTime"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_JOB_REQUEST_TIME, BusinessAction.messageForKeyAdmin("labelDriverJobRequestTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_JOB_REQUEST_TIME, BusinessAction.messageForKeyAdmin("labelDriverJobRequestTime"), new MinMaxValueValidationRule(0, 30));

		validator.addValidationMapping(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME, BusinessAction.messageForKeyAdmin("labelAllocationBeforeTime"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME, BusinessAction.messageForKeyAdmin("labelAllocationBeforeTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_ALLOCATE_BEFORE_TIME, BusinessAction.messageForKeyAdmin("labelAllocationBeforeTime"), new MinMaxValueValidationRule(5, 120));

		validator.addValidationMapping(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME, BusinessAction.messageForKeyAdmin("labelAllocationAfterTime"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME, BusinessAction.messageForKeyAdmin("labelAllocationAfterTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_ALLOCATE_AFTER_TIME, BusinessAction.messageForKeyAdmin("labelAllocationAfterTime"), new MinMaxValueValidationRule(5, 120));

		validator.addValidationMapping(FieldConstants.TAKE_BOOKING_FOR_NEXT_X_DAYS, messageForKey("labelTakeBookingForNextXDays"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.TAKE_BOOKING_FOR_NEXT_X_DAYS, messageForKey("labelTakeBookingForNextXDays"), new MinMaxValueValidationRule(1, 7));

		validator.addValidationMapping(FieldConstants.TAKE_BOOKING_MAX_NUMBER_ALLOWED, messageForKey("labelTakeBookingMaximumNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.TAKE_BOOKING_MAX_NUMBER_ALLOWED, messageForKey("labelTakeBookingMaximumNumber"), new MinMaxValueValidationRule(1, 10));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredPageExtaSupportCss() {
		List<String> requiredPageExtaSupportCss = Arrays.asList(UrlConstants.JS_URLS.TIMEENTRY_CSS);
		return requiredPageExtaSupportCss.toArray(new String[requiredPageExtaSupportCss.size()]);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_RIDE_LATER_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredPageExtaSupportJs() {
		List<String> requiredPageExtaSupportJs = Arrays.asList(UrlConstants.JS_URLS.TIME_PLUGIN_JS, UrlConstants.JS_URLS.TIMEENTRY_MIN_JS, UrlConstants.JS_URLS.TIMEENTRY_JS);
		return requiredPageExtaSupportJs.toArray(new String[requiredPageExtaSupportJs.size()]);
	}
}