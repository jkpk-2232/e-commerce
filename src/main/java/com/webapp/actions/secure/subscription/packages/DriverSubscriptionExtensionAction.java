package com.webapp.actions.secure.subscription.packages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.json.JSONArray;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.DriverSubscriptionExtensionLogsModel;

@Path("/driver-subscription-extension")
public class DriverSubscriptionExtensionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverSubscriptionExtension(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.isSuperAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String driverAllOptions = DropDownUtils.getYesNoOption(true + "");
		data.put(FieldConstants.DRIVER_ALL_OPTIONS, driverAllOptions);

		String driverOptions = DropDownUtils.getDriverListByRoleIdOptionsAndApplicationStatus(null, UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, assignedRegionList);
		data.put(FieldConstants.DRIVER_LIST_OPTIONS, driverOptions);

		String multicityCityRegionIdOptions = DropDownUtils.getRegionListByMulticityCountryIdOptionsSingleSelect(null, ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		String durationDaysOptions = DropDownUtils.getSubscriptionDurationInDaysOption(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.DURATION_DAYS_OPTIONS, durationDaysOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL);

		return loadView(UrlConstants.JSP_URLS.DRIVER_SUBSCRIPTION_EXTENSTION_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response getAnnouncementList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		@FormParam(FieldConstants.DRIVER_ALL) String driverAll,
		@FormParam(FieldConstants.DRIVER_LIST) List<String> driverList,
		@FormParam(FieldConstants.PRICE) String price,
		@FormParam(FieldConstants.DURATION_DAYS) String durationDays
		) throws ServletException, IOException, SQLException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.isSuperAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String driverAllOptions = DropDownUtils.getYesNoOption(true + "");
		data.put(FieldConstants.DRIVER_ALL_OPTIONS, driverAllOptions);

		String driverOptions = DropDownUtils.getDriverListByRoleIdOptionsAndApplicationStatus(null, UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, assignedRegionList);
		data.put(FieldConstants.DRIVER_LIST_OPTIONS, driverOptions);

		String multicityCityRegionIdOptions = DropDownUtils.getRegionListByMulticityCountryIdOptionsSingleSelect(null, ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		String durationDaysOptions = DropDownUtils.getSubscriptionDurationInDaysOption(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.DURATION_DAYS_OPTIONS, durationDaysOptions);

		data.put(FieldConstants.PRICE, price);
		data.put(FieldConstants.DRIVER_LIST, driverList + "");

		if (hasErrors(driverAll, driverList)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL);

			return loadView(UrlConstants.JSP_URLS.DRIVER_SUBSCRIPTION_EXTENSTION_JSP);
		}

		DriverSubscriptionUtils.processDriverSubscriptionExtension(loginSessionMap.get(LoginUtils.USER_ID), multicityCityRegionId, driverAll, driverList, StringUtils.doubleValueOf(price), StringUtils.intValueOf(durationDays));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverSubscriptionExtensionLogsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		int total = DriverSubscriptionExtensionLogsModel.getDriverSubscriptionExtensionLogsCount(dtu.getStartDatelong(), dtu.getEndDatelong());
		List<DriverSubscriptionExtensionLogsModel> driverSubscriptionExtensionLogsList = DriverSubscriptionExtensionLogsModel.getDriverSubscriptionExtensionLogsListForSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getStartInt(), dtu.getLengthInt(),
					dtu.getGlobalSearchStringWithPercentage());

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (DriverSubscriptionExtensionLogsModel driverSubscriptionExtensionLogsModel : driverSubscriptionExtensionLogsList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getDriverSubscriptionExtensionLogId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getRegionName());
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getCreatedByName());
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getStatus());
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getTotalDrivers());
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getTotalCompletedDrivers());
			dtuInnerJsonArray.put(driverSubscriptionExtensionLogsModel.getTotalFailedDrivers());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(driverSubscriptionExtensionLogsModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	public boolean hasErrors(String driverAll, List<String> driverList) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRICE, messageForKeyAdmin("labelPrice"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRICE, messageForKeyAdmin("labelPrice"), new MinMaxValueValidationRule(0, 100000));

		if (!StringUtils.booleanValueOf(driverAll)) {
			validator.addValidationMapping(FieldConstants.DRIVER_LIST, messageForKeyAdmin("labelDriver"), new RequiredListValidationRule(driverList.size()));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_SUBSCRIPTION_EXTENSTION_JS);
		
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
	@Override
	protected String[] requiredCss() {

		List<String> requiredCSS = new ArrayList<String>();

		requiredCSS.add("new-ui/css/bootstrap-material-datetimepicker.css");

		return requiredCSS.toArray(new String[requiredCSS.size()]);
	}
}