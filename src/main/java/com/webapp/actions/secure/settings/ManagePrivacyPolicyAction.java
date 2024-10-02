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

@Path("/manage-privacy-policy")
public class ManagePrivacyPolicyAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getPrivacyPolicy(
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
		data.put(FieldConstants.PRIVACY_POLICY, adminSettingsModel.getPrivacyPolicy());

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			VendorAdminSettingsModel vendorAdminSettingsModel = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

			if (vendorAdminSettingsModel != null && vendorAdminSettingsModel.getPrivacyPolicy() != null) {
				data.put(FieldConstants.PRIVACY_POLICY, vendorAdminSettingsModel.getPrivacyPolicy());
			}
		}

		data.put(FieldConstants.LANGUAGE, FrameworkConstants.LANGUAGE_ENGLISH);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRIVACY_POLICY_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PRIVACY_POLICY_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postPrivacyPolicy(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PRIVACY_POLICY) String privacyPolicy
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.PRIVACY_POLICY, privacyPolicy);

		if (hasErrors()) {

			data.put(FieldConstants.LANGUAGE, FrameworkConstants.LANGUAGE_ENGLISH);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRIVACY_POLICY_URL);

			return loadView(UrlConstants.JSP_URLS.MANAGE_PRIVACY_POLICY_JSP);
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			VendorAdminSettingsModel vendorAdminSettingsModel = VendorAdminSettingsModel.getVendorAdminSettingsDetailsByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

			if (vendorAdminSettingsModel != null) {
				vendorAdminSettingsModel.setPrivacyPolicy(privacyPolicy);
				vendorAdminSettingsModel.updateVendorAdminSettings(loginSessionMap.get(LoginUtils.USER_ID));
			} else {
				vendorAdminSettingsModel = new VendorAdminSettingsModel();
				vendorAdminSettingsModel.setPrivacyPolicy(privacyPolicy);
				vendorAdminSettingsModel.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));
				vendorAdminSettingsModel.insertVendorAdminSettings(loginSessionMap.get(LoginUtils.USER_ID));
			}

		} else {
			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
			adminSettingsModel.setPrivacyPolicy(privacyPolicy);
			adminSettingsModel.updatePrivacyPolicyAdminSettings();
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRIVACY_POLICY_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRIVACY_POLICY, messageForKeyAdmin("labelPrivacyPolicy"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRIVACY_POLICY, messageForKeyAdmin("labelPrivacyPolicy"), new MaxLengthValidationRule(450000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PRIVACY_POLICY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredPageExtaSupportJs() {
		List<String> requiredPageExtaSupportJs = Arrays.asList(UrlConstants.JS_URLS.CKEDITOR_MIN_JS, UrlConstants.JS_URLS.CKEDITOR_CONFIG_JS);
		return requiredPageExtaSupportJs.toArray(new String[requiredPageExtaSupportJs.size()]);
	}
}