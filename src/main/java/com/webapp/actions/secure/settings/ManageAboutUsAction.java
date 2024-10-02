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

import com.jeeutils.FrameworkConstants;
import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.VendorAdminSettingsModel;

@Path("/manage-about-us")
public class ManageAboutUsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAboutUs(
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

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		data.put(FieldConstants.ABOUT_US, adminSettingsModel.getAboutUs());

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			VendorAdminSettingsModel vendorAdminSettingsModel = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

			if (vendorAdminSettingsModel != null && vendorAdminSettingsModel.getAboutUs() != null) {
				data.put(FieldConstants.ABOUT_US, vendorAdminSettingsModel.getAboutUs());
			}
		}

		data.put(FieldConstants.LANGUAGE, FrameworkConstants.LANGUAGE_ENGLISH);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ABOUT_US_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ABOUT_US_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAboutUs(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.ABOUT_US) String aboutUs
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ABOUT_US, aboutUs);

		if (hasErrors()) {
			return loadView("/secure/settings/manage-about-us.jsp");
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			VendorAdminSettingsModel vendorAdminSettingsModel = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

			if (vendorAdminSettingsModel != null) {
				vendorAdminSettingsModel.setAboutUs(aboutUs);
				vendorAdminSettingsModel.updateVendorAdminSettings(loginSessionMap.get(LoginUtils.USER_ID));
			} else {
				vendorAdminSettingsModel = new VendorAdminSettingsModel();
				vendorAdminSettingsModel.setAboutUs(aboutUs);
				vendorAdminSettingsModel.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));
				vendorAdminSettingsModel.insertVendorAdminSettings(loginSessionMap.get(LoginUtils.USER_ID));
			}

		} else {
			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
			adminSettingsModel.setAboutUs(aboutUs);
			adminSettingsModel.updateAboutUsAdminSettings();
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ABOUT_US_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.ABOUT_US, messageForKeyAdmin("labelAboutUs"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ABOUT_US, messageForKeyAdmin("labelAboutUs"), new MaxLengthValidationRule(450000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ABOUT_US_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredPageExtaSupportJs() {
		List<String> requiredPageExtaSupportJs = Arrays.asList(UrlConstants.JS_URLS.CKEDITOR_MIN_JS, UrlConstants.JS_URLS.CKEDITOR_CONFIG_JS);
		return requiredPageExtaSupportJs.toArray(new String[requiredPageExtaSupportJs.size()]);
	}
}