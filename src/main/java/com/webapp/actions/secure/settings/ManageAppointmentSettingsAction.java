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
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AppointmentSettingModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-appointment-settings")
public class ManageAppointmentSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getOrderSettings(
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

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, ProjectConstants.SUPER_SERVICE_TYPE_ID.APPOINTMENT_ID);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		return loadView(UrlConstants.JSP_URLS.MANAGE_APPOINTMENT_SETTINGS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrderSettingsList(
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
		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);
		serviceId = DropDownUtils.parserForAllOptions(serviceId);

		int total = AppointmentSettingModel.getAppointmentSettingCount(serviceId);
		List<AppointmentSettingModel> appointmentSettingsList = AppointmentSettingModel.getAppointmentSettingSearch(dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), "os.updated_at", serviceId);

		int count = dtu.getStartInt();

		for (AppointmentSettingModel appointmentSettingModel : appointmentSettingsList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(appointmentSettingModel.getServiceId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(appointmentSettingModel.getServiceName());
			dtuInnerJsonArray.put(MyHubUtils.convertMinutesToHours(appointmentSettingModel.getMinBookingTime()));
			dtuInnerJsonArray.put(MyHubUtils.convertMinutesToDays(appointmentSettingModel.getMaxBookingTime()));
			dtuInnerJsonArray.put(appointmentSettingModel.getFreeCancellationTimeMins());
			dtuInnerJsonArray.put(StringUtils.valueOf(appointmentSettingModel.getCronJobExpireTimeMins()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_APPOINTMENT_SETTINGS_URL + "?serviceId=" + appointmentSettingModel.getServiceId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = AppointmentSettingModel.getAppointmentSettingSearchCount(dtu.getGlobalSearchStringWithPercentage(), serviceId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_APPOINTMENT_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}