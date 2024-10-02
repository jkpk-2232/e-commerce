package com.webapp.actions.secure.brands;

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
import com.jeeutils.validator.DuplicateBrandNameValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.BrandModel;

@Path("/add-brand")
public class AddBrandAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddBrand (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.BRAND_IMAGE_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		
		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_BRAND_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.BRAND_NAME) String brandName,
		@FormParam(FieldConstants.BRAND_DESCRIPTION) String brandDescription,
		@FormParam(FieldConstants.BRAND_IMAGE_HIDDEN) String hiddenBrandImage
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		data.put(FieldConstants.BRAND_IMAGE_HIDDEN, StringUtils.validString(hiddenBrandImage) ? hiddenBrandImage : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.BRAND_NAME, brandName);
		data.put(FieldConstants.BRAND_DESCRIPTION, brandDescription);
		data.put(FieldConstants.BRAND_IMAGE_HIDDEN, hiddenBrandImage);

		if (hasErrors()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_BRAND_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		BrandModel brandModel = new BrandModel();

		brandModel.setBrandName(brandName);
		brandModel.setBrandDescription(brandDescription);
		brandModel.setBrandImage(hiddenBrandImage);

		brandModel.insertBrand(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.BRAND_NAME, messageForKeyAdmin("labelBrandName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.BRAND_NAME, messageForKeyAdmin("labelBrandName"), new DuplicateBrandNameValidationRule(null));
		validator.addValidationMapping(FieldConstants.BRAND_DESCRIPTION, messageForKeyAdmin("labelBrandDescription"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_BRAND_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
