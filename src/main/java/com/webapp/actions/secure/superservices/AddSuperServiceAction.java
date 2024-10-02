package com.webapp.actions.secure.superservices;

import java.io.IOException;
import java.sql.SQLException;
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
import com.jeeutils.validator.DuplicateServiceNameValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AppointmentSettingModel;
import com.webapp.models.OrderSettingModel;
import com.webapp.models.ServiceModel;

@Path("/add-super-service")
public class AddSuperServiceAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addSuperServicesGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(false, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_TYPE_ID_OPTIONS, serviceTypeIdOptions);
		
		data.put(FieldConstants.SERVICE_IMAGE_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		
		data.put("cdnUrl", WebappPropertyUtils.CDN_URL);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_SUPER_SERVICE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addSuperServicesPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.SERVICE_NAME) String serviceName,
		@FormParam(FieldConstants.SERVICE_DESCRIPTION) String serviceDescription,
		@FormParam(FieldConstants.SERVICE_TYPE_ID) String serviceTypeId,
		@FormParam(FieldConstants.SERVICE_PRIORITY) String servicePriority,
		@FormParam(FieldConstants.SERVICE_IMAGE_HIDDEN) String HiddenServiceImage
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put("cdnUrl", WebappPropertyUtils.CDN_URL);
		
		data.put(FieldConstants.SERVICE_NAME, serviceName);
		data.put(FieldConstants.SERVICE_DESCRIPTION, serviceDescription);
		data.put(FieldConstants.SERVICE_PRIORITY, servicePriority);
		data.put(FieldConstants.SERVICE_IMAGE_HIDDEN_DUMMY, StringUtils.validString(HiddenServiceImage) ? HiddenServiceImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.SERVICE_IMAGE_HIDDEN, HiddenServiceImage);
		
		if (hasErrors()) {

			String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(false, serviceTypeId);
			data.put(FieldConstants.SERVICE_TYPE_ID_OPTIONS, serviceTypeIdOptions);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_SUPER_SERVICE_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		ServiceModel serviceModel = new ServiceModel();
		serviceModel.setServiceName(serviceName);
		serviceModel.setServiceDescription(serviceDescription);
		serviceModel.setServiceTypeId(serviceTypeId);
		serviceModel.setServicePriority(StringUtils.intValueOf(servicePriority));
		serviceModel.setServiceImage(HiddenServiceImage);
		String serviceId = serviceModel.insertServices(loggedInUserId);

		if (CourierUtils.isAppointmentService(serviceTypeId)) {
			AppointmentSettingModel asm = new AppointmentSettingModel(serviceId, loggedInUserId);
			asm.insertAppointmentSettings(loggedInUserId);
		} else if (CourierUtils.isEcommerceService(serviceTypeId)) {
			OrderSettingModel osm = new OrderSettingModel(serviceId, loggedInUserId);
			osm.insertOrderSettings(loggedInUserId);
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.SERVICE_NAME, messageForKeyAdmin("labelServiceName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.SERVICE_NAME, messageForKeyAdmin("labelServiceName"), new DuplicateServiceNameValidationRule(null));
		validator.addValidationMapping(FieldConstants.SERVICE_DESCRIPTION, messageForKeyAdmin("labelServiceDescription"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.SERVICE_PRIORITY, messageForKeyAdmin("labelServicePriority"), new MinMaxValueValidationRule(1, 1000000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_SUPER_SERVICES_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}