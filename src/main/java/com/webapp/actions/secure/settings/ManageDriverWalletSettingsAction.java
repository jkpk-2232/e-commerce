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
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.DriverWalletSettingsModel;

@Path("/manage-driver-wallet/settings")
public class ManageDriverWalletSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadDriverWalletSettings(
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

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		data.put(FieldConstants.MINIMUM_AMOUNT, StringUtils.valueOf(driverWalletSettingsModel.getMinimumAmount()));
		data.put(FieldConstants.NOTIFY_AMOUNT, StringUtils.valueOf(driverWalletSettingsModel.getNotifyAmount()));

		data.put("labelDriverWalletSettings", messageForKeyAdmin("labelDriverWalletSettings"));
		data.put("labelMinimumAmount", messageForKeyAdmin("labelMinimumAmount"));
		data.put("labelNotifyAmount", messageForKeyAdmin("labelNotifyAmount"));
		data.put("labelCancel", messageForKeyAdmin("labelCancel"));
		data.put("labelSave", messageForKeyAdmin("labelSave"));

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_WALLET_SETTINGS_URL);
		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_WALLET_SETTINGS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postDriverWalletSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.MINIMUM_AMOUNT) String minimumAmount,
		@FormParam(FieldConstants.NOTIFY_AMOUNT) String notifyAmount
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.MINIMUM_AMOUNT, minimumAmount);
		data.put(FieldConstants.NOTIFY_AMOUNT, notifyAmount);

		if (hasErrorsEnglish()) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_WALLET_SETTINGS_URL);
			return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_WALLET_SETTINGS_JSP);
		}

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();
		driverWalletSettingsModel.setMinimumAmount(StringUtils.doubleValueOf(minimumAmount));
		driverWalletSettingsModel.setNotifyAmount(StringUtils.doubleValueOf(notifyAmount));
		driverWalletSettingsModel.updateDriverWalletSettings(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DRIVER_WALLET_SETTINGS_URL);
	}

	public boolean hasErrorsEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.MINIMUM_AMOUNT, messageForKeyAdmin("labelMinimumAmount"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.MINIMUM_AMOUNT, messageForKeyAdmin("labelMinimumAmount"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping(FieldConstants.NOTIFY_AMOUNT, messageForKeyAdmin("labelNotifyAmount"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.NOTIFY_AMOUNT, messageForKeyAdmin("labelNotifyAmount"), new MinMaxValueValidationRule(1, 100000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_WALLET_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}