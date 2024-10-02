package com.webapp.actions.secure.subscription.packages;

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
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.DuplicateSubscriptionPackageValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.SubscriptionPackageModel;

@Path("/add-subscription-package")
public class AddSubscriptionPackageAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddSubscriptionPackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String durationDaysOptions = DropDownUtils.getSubscriptionDurationInDaysOption(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.DURATION_DAYS_OPTIONS, durationDaysOptions);

		String carTypeIdOptions = DropDownUtils.getCarModelOption(ProjectConstants.Second_Vehicle_ID, true, false);
		data.put(FieldConstants.CAR_TYPE_ID_OPTIONS, carTypeIdOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_URL);
		return loadView(UrlConstants.JSP_URLS.ADD_SUBSCRIPTION_PACKAGE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddSubscriptionPackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PACKAGE_NAME) String packageName,
		@FormParam(FieldConstants.DURATION_DAYS) String durationDays,
		@FormParam(FieldConstants.PRICE) String price,
		@FormParam(FieldConstants.CAR_TYPE_ID) String carTypeId
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String durationDaysOptions = DropDownUtils.getSubscriptionDurationInDaysOption(durationDays);
		data.put(FieldConstants.DURATION_DAYS_OPTIONS, durationDaysOptions);

		String carTypeIdOptions = DropDownUtils.getCarModelOption(carTypeId, true, false);
		data.put(FieldConstants.CAR_TYPE_ID_OPTIONS, carTypeIdOptions);

		data.put(FieldConstants.PACKAGE_NAME, packageName);
		data.put(FieldConstants.PRICE, price);

		if (hasErrors()) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_SUBSCRIPTION_PACKAGE_JSP);
		}

		SubscriptionPackageModel subscriptionPackageModel = new SubscriptionPackageModel();
		subscriptionPackageModel.setPackageName(packageName);
		subscriptionPackageModel.setDurationDays(StringUtils.intValueOf(durationDays));
		subscriptionPackageModel.setPrice(StringUtils.doubleValueOf(price));
		subscriptionPackageModel.setCarTypeId(carTypeId);

		subscriptionPackageModel.addSubscriptionPackage(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping("packageName", messageForKeyAdmin("labelPackageName"), new RequiredValidationRule());
		validator.addValidationMapping("packageName", messageForKeyAdmin("labelPackageName"), new DuplicateSubscriptionPackageValidationRule(null));

		validator.addValidationMapping("price", messageForKeyAdmin("labelPrice"), new RequiredValidationRule());
		validator.addValidationMapping("price", messageForKeyAdmin("labelPrice"), new DecimalValidationRule());
		validator.addValidationMapping("price", messageForKeyAdmin("labelPrice"), new MinMaxValueValidationRule(0, 1000000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_SUBSCRIPTION_PACKAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}