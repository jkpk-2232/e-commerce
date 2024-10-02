package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AppointmentSettingModel;

@Path("/edit-appointment-settings")
public class EditAppointmentSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditAppointmentSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AppointmentSettingModel asm = AppointmentSettingModel.getAppointmentSettingDetailsByServiceId(serviceId);

		data.put(FieldConstants.SERVICE_NAME, asm.getServiceName());
		data.put(FieldConstants.SERVICE_ID, asm.getServiceId());
		data.put(FieldConstants.MIN_BOOKING_TIME, MyHubUtils.convertMinutesToHours(asm.getMinBookingTime()));
		data.put(FieldConstants.FREE_CANCELLATION_TIME_MINS, StringUtils.valueOf(asm.getFreeCancellationTimeMins()));
		data.put(FieldConstants.CRON_JOB_EXPIRE_TIME_MINS, StringUtils.valueOf(asm.getCronJobExpireTimeMins()));

		String maxBookingTimeOptions = DropDownUtils.getDaysOptionRideLater(MyHubUtils.convertMinutesToDays(asm.getMaxBookingTime()));
		data.put(FieldConstants.MAX_BOOKING_TIME_OPTIONS, maxBookingTimeOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_SETTINGS_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_APPOINTMENT_SETTINGS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postEditAppointmentSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.SERVICE_ID) String serviceId,
		@FormParam(FieldConstants.MIN_BOOKING_TIME) String minBookingTime,
		@FormParam(FieldConstants.MAX_BOOKING_TIME) String maxBookingTime,
		@FormParam(FieldConstants.FREE_CANCELLATION_TIME_MINS) String freeCancellationTimeMins,
		@FormParam(FieldConstants.CRON_JOB_EXPIRE_TIME_MINS) String cronJobExpireTimeMins
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AppointmentSettingModel asm = AppointmentSettingModel.getAppointmentSettingDetailsByServiceId(serviceId);

		data.put(FieldConstants.SERVICE_NAME, asm.getServiceName());
		data.put(FieldConstants.SERVICE_ID, serviceId);
		data.put(FieldConstants.MIN_BOOKING_TIME, StringUtils.valueOf(MyHubUtils.convertHoursToMinutes(minBookingTime, 0)));
		data.put(FieldConstants.FREE_CANCELLATION_TIME_MINS, freeCancellationTimeMins);
		data.put(FieldConstants.CRON_JOB_EXPIRE_TIME_MINS, cronJobExpireTimeMins);

		String maxBookingTimeOptions = DropDownUtils.getDaysOptionRideLater(maxBookingTime);
		data.put(FieldConstants.MAX_BOOKING_TIME_OPTIONS, maxBookingTimeOptions);

		if (hasErrors()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_SETTINGS_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_APPOINTMENT_SETTINGS_JSP);
		}

		asm.setServiceId(serviceId);
		asm.setMinBookingTime(MyHubUtils.convertHoursToMinutes(minBookingTime, 0));
		asm.setMaxBookingTime(MyHubUtils.convertHoursToMinutes(ProjectConstants.TIME_00_00, StringUtils.longValueOf(maxBookingTime)));
		asm.setFreeCancellationTimeMins(StringUtils.intValueOf(freeCancellationTimeMins));
		asm.setCronJobExpireTimeMins(StringUtils.intValueOf(cronJobExpireTimeMins));

		asm.updateAppointmentSettings(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_SETTINGS_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.MIN_BOOKING_TIME, messageForKeyAdmin("labelMinimumBookingTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.MIN_BOOKING_TIME, messageForKeyAdmin("labelMinimumBookingTime"), new MinMaxValueValidationRule(0, 9999));

		validator.addValidationMapping(FieldConstants.FREE_CANCELLATION_TIME_MINS, messageForKeyAdmin("labelFreeCancellationTimeMins"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.FREE_CANCELLATION_TIME_MINS, messageForKeyAdmin("labelFreeCancellationTimeMins"), new MinMaxValueValidationRule(10, 1000));

		validator.addValidationMapping(FieldConstants.CRON_JOB_EXPIRE_TIME_MINS, messageForKeyAdmin("labelCronJobExpireTimeMins"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.CRON_JOB_EXPIRE_TIME_MINS, messageForKeyAdmin("labelCronJobExpireTimeMins"), new MinMaxValueValidationRule(1, 1000));

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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_APPOINTMENT_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredPageExtaSupportJs() {
		List<String> requiredPageExtaSupportJs = Arrays.asList(UrlConstants.JS_URLS.TIME_PLUGIN_JS, UrlConstants.JS_URLS.TIMEENTRY_MIN_JS, UrlConstants.JS_URLS.TIMEENTRY_JS);
		return requiredPageExtaSupportJs.toArray(new String[requiredPageExtaSupportJs.size()]);
	}
}