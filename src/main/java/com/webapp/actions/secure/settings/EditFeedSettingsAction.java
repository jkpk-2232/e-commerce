package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.FeedFareModel;
import com.webapp.models.FeedSettingsModel;
import com.webapp.models.ServiceModel;

@Path("/edit-feed-settings")
public class EditFeedSettingsAction  extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadEditFeedSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.FEED_SETTINGS_ID) String feedSettingsId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		FeedSettingsModel feedSettingsModel = FeedSettingsModel.getFeedSettingsDetailsById(feedSettingsId);

		data.put(FieldConstants.FEED_SETTINGS_ID, feedSettingsId);

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(feedSettingsModel.getMulticityCityRegionId(), loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) { 
			Set<String> result = new HashSet<String>();
			String feedFareAvailableList = "";

			List<ServiceModel> serviceList =  ServiceModel.getAllActiveServices();
			for (ServiceModel serviceModel : serviceList) {
				result.add(serviceModel.getServiceName());
			}

			List<FeedFareModel> feedFareModelList = FeedFareModel.getFeedFareListByFeedSettingsId(feedSettingsId);
			
			for (FeedFareModel feedFareModel : feedFareModelList) {
				result.add(feedFareModel.getServiceId());
				String serviceId = feedFareModel.getServiceId();
				
				data.put(serviceId + "_BaseFare", StringUtils.valueOf(feedFareModel.getBaseFare()));
				data.put(serviceId + "_PerMinuteFare", StringUtils.valueOf(feedFareModel.getPerMinuteFare()));
				data.put(serviceId + "_GSTPercentage", StringUtils.valueOf(feedFareModel.getGSTPercentage()));
				
				feedFareAvailableList += feedFareModel.getServiceId() + ",";
			}


			String string = String.join(",", result);

			if (string.length() > 0) {
				data.put(FieldConstants.SERVICES_ACTIVE_LIST, string);
			}

			if (feedFareAvailableList.length() > 0) {
				data.put("feedFareAvailableList", feedFareAvailableList.substring(0, feedFareAvailableList.length() - 1));
			}

		} else {
			String feedFareAvailableList = "";

			String dataAvailableCarList = "";
			
			List<FeedFareModel> feedFareModelList = FeedFareModel.getFeedFareListByFeedSettingsId(feedSettingsId);
			
			for (FeedFareModel feedFareModel : feedFareModelList) {
				
				String serviceId = feedFareModel.getServiceId();
				
				data.put(serviceId + "_BaseFare", StringUtils.valueOf(feedFareModel.getBaseFare()));
				data.put(serviceId + "_PerMinuteFare", StringUtils.valueOf(feedFareModel.getPerMinuteFare()));
				data.put(serviceId + "_GSTPercentage", StringUtils.valueOf(feedFareModel.getGSTPercentage()));
				
				dataAvailableCarList += serviceId + ",";
				feedFareAvailableList += serviceId + ",";
			}
			
			

			if (dataAvailableCarList.length() > 0) {
				data.put(FieldConstants.SERVICES_ACTIVE_LIST, dataAvailableCarList.substring(0, dataAvailableCarList.length() - 1));
			}

			if (feedFareAvailableList.length() > 0) {
				data.put("feedFareAvailableList", feedFareAvailableList.substring(0, feedFareAvailableList.length() - 1));
			}
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_FEED_SETTINGS_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_FEED_SETTINGS_JSP);
		
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addFeedSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.FEED_SETTINGS_ID) String feedSettingsId,
		@FormParam(FieldConstants.REGION_LIST) String regionList,
		@FormParam(FieldConstants.QUERY_STRING) String queryString,
		@FormParam(FieldConstants.SERVICES_ACTIVE_LIST) String servicesActiveList
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
		data.put("serviceAvailableList", servicesActiveList);


		Map<String, Object> inputMap = MultiTenantUtils.parseInputParameters(queryString);

		logger.info("\n\n\n\tinputMap\t" + inputMap);
		logger.info("\n\n\n\tcarAvailableList\t" + servicesActiveList);

		Set<String> result = new HashSet<String>();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) { 
			List<ServiceModel> serviceModelList = ServiceModel.getAllActiveServices();
			for (ServiceModel serviceModel : serviceModelList) {
				result.add(serviceModel.getServiceId());
			}
		}

		String[] availableCarList = servicesActiveList.split(",");
		String dataAvailableCarList = "";

		for (String string : availableCarList) {

			String serviceId = string.replace("_IsAvailable", "");
			result.add(serviceId);

			data.put(serviceId + "_BaseFare", inputMap.get(serviceId + "_BaseFare") != null ? inputMap.get(serviceId + "_BaseFare").toString() : "");
			data.put(serviceId + "_PerMinuteFare", inputMap.get(serviceId + "_PerMinuteFare") != null ? inputMap.get(serviceId + "_PerMinuteFare").toString() : "");
			data.put(serviceId + "_GSTPercentage", inputMap.get(serviceId + "_GSTPercentage") != null ? inputMap.get(serviceId + "_GSTPercentage").toString() : "");

			dataAvailableCarList += serviceId + ",";
		}

		if (dataAvailableCarList.length() > 0) {
			String string = String.join(",", result);
			data.put(FieldConstants.SERVICES_ACTIVE_LIST, string);
			data.put("feedFareAvailableList", dataAvailableCarList.substring(0, dataAvailableCarList.length() - 1));
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);


		if (hasErrors(availableCarList)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_FEED_SETTINGS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_FEED_SETTINGS_JSP);
		}


		FeedSettingsModel feedSettingsModel = new FeedSettingsModel();
		feedSettingsModel.setFeedSettingsId(feedSettingsId);
		feedSettingsModel.setMulticityCityRegionId(regionList);


		feedSettingsModel.updateFeedSettings(loginSessionMap.get(LoginUtils.USER_ID));

		FeedFareModel previousFeedFareModel = new FeedFareModel();
		previousFeedFareModel.setFeedSettingsId(feedSettingsId);
		previousFeedFareModel.deleteFeedFareByFeedSettingsId();

		List<FeedFareModel> feedFareModelList = new ArrayList<FeedFareModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		for (String string : availableCarList) {

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
	
	public boolean hasErrors(String[] availableCarList) {

		boolean hasErrors = false;

		Validator validator = new Validator();


		for (String string : availableCarList) {

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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_FEED_SETTINGS_JS);
		
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
