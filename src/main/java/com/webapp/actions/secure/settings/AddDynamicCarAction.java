package com.webapp.actions.secure.settings;

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
import com.jeeutils.validator.DuplicateCarTypeValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.CarTypeModel;

@Path("/add-dynamic-car")
public class AddDynamicCarAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addDynamicCarGet(
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

		String carIconOptions = DropDownUtils.getCarIconsListOptions("", request.getContextPath());
		data.put(FieldConstants.CAR_ICON_OPTIONS, carIconOptions);

		data.put(FieldConstants.CAR_TYPE_ICON_IMAGE_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DYNAMIC_CARS_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_DYNAMIC_CAR_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addDynamicCarPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.CAR_MODEL_TYPE_ID) String carType,
		@FormParam(FieldConstants.CAR_ICON) String carIcon,
		@FormParam(FieldConstants.CAR_TYPE_ICON_IMAGE_URL_HIDDEN) String hiddenCarTypeIconImage
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.CAR_MODEL_TYPE_ID, carType);
		data.put(FieldConstants.CAR_TYPE_ICON_IMAGE_URL_HIDDEN, hiddenCarTypeIconImage);
		data.put(FieldConstants.CAR_TYPE_ICON_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenCarTypeIconImage) ? hiddenCarTypeIconImage : ProjectConstants.DEFAULT_IMAGE);

		if (hasErrors(null)) {

			String carIconOptions = DropDownUtils.getCarIconsListOptions(carIcon, request.getContextPath());
			data.put(FieldConstants.CAR_ICON_OPTIONS, carIconOptions);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DYNAMIC_CARS_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_DYNAMIC_CAR_JSP);
		}

		CarTypeModel carTypeModel = new CarTypeModel();
		carTypeModel.setCarType(carType);
		carTypeModel.setIcon(carIcon);
		carTypeModel.setCarTypeIconImage(hiddenCarTypeIconImage);
		String carTypeId = carTypeModel.insertCarType(loginSessionMap.get(LoginUtils.USER_ID));

		MultiTenantUtils.assignNewCarTypeToExistingVendors(carTypeId, loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		// IMPORTANT NOTE ---> ManageVendorDynamicCarsAction will take care of car type
		// allocation for the vendors. No need to do it for each newly created car type.
//		MultiTenantUtils.assignNewCarTypeToExistingVendors(carTypeId, loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID);
//		MultiTenantUtils.assignNewCarTypeToExistingVendors(carTypeId, loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DYNAMIC_CARS_URL);
	}

	public boolean hasErrors(String carTypeId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.CAR_MODEL_TYPE_ID, messageForKeyAdmin("labelCarType"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CAR_MODEL_TYPE_ID, messageForKeyAdmin("labelCarType"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.CAR_MODEL_TYPE_ID, messageForKeyAdmin("labelCarType"), new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(FieldConstants.CAR_MODEL_TYPE_ID, messageForKeyAdmin("labelCarType"), new DuplicateCarTypeValidationRule(carTypeId));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_DYNAMIC_CAR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}