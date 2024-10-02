package com.webapp.actions.secure.settings;

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

import com.jeeutils.DateUtils;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.FeedFareModel;
import com.webapp.models.FeedSettingsModel;
import com.webapp.models.ServiceModel;

@Path("/add-feed-settings")
public class AddFeedSettingsAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddFeedSettingsDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);
		

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			List<ServiceModel> serviceList =  ServiceModel.getAllActiveServices();
			String dataAvailableServiceList = "";
			for (ServiceModel serviceModel : serviceList) {
				dataAvailableServiceList += serviceModel.getServiceName() + ",";
			}

			if (dataAvailableServiceList.length() > 0) {
				data.put(FieldConstants.SERVICES_ACTIVE_LIST, dataAvailableServiceList.substring(0, dataAvailableServiceList.length() - 1));
			}

		} else {
			data.put(FieldConstants.SERVICES_ACTIVE_LIST, "");
		}
		
		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_FEED_SETTINGS_URL);
		return loadView(UrlConstants.JSP_URLS.ADD_FEED_SETTINGS_JSP);
	}
	
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addFeedSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.REGION_LIST) String regionList,
		@FormParam(FieldConstants.QUERY_STRING) String queryString,
		@FormParam(FieldConstants.SERVICES_ACTIVE_LIST) String serviceAvailableList
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		
		data.put(FieldConstants.QUERY_STRING, queryString);


		Map<String, Object> inputMap = MultiTenantUtils.parseInputParameters(queryString);


		String[] availableServiceList = serviceAvailableList.split(",");
		String dataAvailableServiceList = "";

		for (String string : availableServiceList) {

			String serviceId = string.replace("_IsAvailable", "");
			data.put(serviceId + "_BaseFare", inputMap.get(serviceId + "_BaseFare") != null ? inputMap.get(serviceId + "_BaseFare").toString() : "");
			data.put(serviceId + "_PerMinuteFare", inputMap.get(serviceId + "_PerMinuteFare") != null ? inputMap.get(serviceId + "_PerMinuteFare").toString() : "");
			data.put(serviceId + "_GSTPercentage", inputMap.get(serviceId + "_GSTPercentage") != null ? inputMap.get(serviceId + "_GSTPercentage").toString() : "");

			dataAvailableServiceList += serviceId + ",";
		}

		if (dataAvailableServiceList.length() > 0) {
			data.put(FieldConstants.SERVICES_ACTIVE_LIST, dataAvailableServiceList.substring(0, dataAvailableServiceList.length() - 1));
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		
		if (hasErrors(availableServiceList)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_FEED_SETTINGS_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_FEED_SETTINGS_JSP);
		}

		FeedSettingsModel feedSettingsModel = new FeedSettingsModel();
		feedSettingsModel.setMulticityCityRegionId(regionList);

		String feedSettingsId = feedSettingsModel.addFeedSettings(loginSessionMap.get(LoginUtils.USER_ID));

		List<FeedFareModel> feedFareModelList = new ArrayList<FeedFareModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		for (String string : availableServiceList) {

			String serviceId = string.replace("_IsAvailable", "");

			FeedFareModel feedFareModel = new FeedFareModel();

			feedFareModel.setFeedFareId(UUIDGenerator.generateUUID());
			feedFareModel.setFeedSettingsId(feedSettingsId);
			feedFareModel.setServiceId(serviceId);
			feedFareModel.setBaseFare(Double.parseDouble(inputMap.get(serviceId + "_BaseFare").toString().trim()));
			feedFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(serviceId + "_PerMinuteFare").toString().trim()));
			feedFareModel.setGSTPercentage(Double.parseDouble(inputMap.get(serviceId + "_GSTPercentage").toString().trim()));
			feedFareModel.setCreatedAt(currentTime);
			feedFareModel.setCreatedBy(loginSessionMap.get(LoginUtils.USER_ID));
			feedFareModel.setUpdatedAt(currentTime);
			feedFareModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));

			feedFareModelList.add(feedFareModel);
		}

		FeedFareModel.insertFeedFareBatch(feedFareModelList);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_FEED_SETTINGS_URL);
	}
	
	
	public boolean hasErrors(String[] availableServiceList) {

		boolean hasErrors = false;

		Validator validator = new Validator();


		for (String string : availableServiceList) {

			String serviceId = string.replace("_IsAvailable", "");

			validator.addValidationMapping(serviceId + "_BaseFare", messageForKeyAdmin("labelBaseFare"), new RequiredValidationRule());
			validator.addValidationMapping(serviceId + "_BaseFare", messageForKeyAdmin("labelBaseFare"), new DecimalValidationRule());
			validator.addValidationMapping(serviceId + "_BaseFare", messageForKeyAdmin("labelBaseFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(serviceId + "_PerMinuteFare", messageForKeyAdmin("labelRentalPerMinuteFare"), new RequiredValidationRule());
			validator.addValidationMapping(serviceId + "_PerMinuteFare", messageForKeyAdmin("labelRentalPerMinuteFare"), new DecimalValidationRule());
			validator.addValidationMapping(serviceId + "_PerMinuteFare", messageForKeyAdmin("labelRentalPerMinuteFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(serviceId + "_GSTPercentage", messageForKeyAdmin("labelGSTPercentage"), new RequiredValidationRule());
			validator.addValidationMapping(serviceId + "_GSTPercentage", messageForKeyAdmin("labelGSTPercentage"), new DecimalValidationRule());
			validator.addValidationMapping(serviceId + "_GSTPercentage", messageForKeyAdmin("labelGSTPercentage"), new MinMaxValueValidationRule(0, 100000));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_FEED_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
