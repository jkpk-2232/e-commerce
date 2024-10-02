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
import javax.ws.rs.QueryParam;
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

@Path("/edit-brand")
public class EditBrandAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.BRAND_ID) String brandId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		BrandModel brandModel = BrandModel.getBrandDetailsByBrandId(brandId);

		data.put(FieldConstants.BRAND_NAME, brandModel.getBrandName());
		data.put(FieldConstants.BRAND_DESCRIPTION, brandModel.getBrandDescription());
		data.put(FieldConstants.BRAND_ID, brandId);

		data.put(FieldConstants.BRAND_IMAGE_HIDDEN, StringUtils.validString(brandModel.getBrandImage()) ? brandModel.getBrandImage() : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.BRAND_IMAGE_HIDDEN_DUMMY, StringUtils.validString(brandModel.getBrandImage()) ? brandModel.getBrandImage() : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_BRAND_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editBrand(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.BRAND_ID) String brandId,
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
		data.put(FieldConstants.BRAND_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenBrandImage) ? hiddenBrandImage : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.BRAND_NAME, brandName);
		data.put(FieldConstants.BRAND_DESCRIPTION, brandDescription);
		data.put(FieldConstants.BRAND_IMAGE_HIDDEN, hiddenBrandImage);

		if (hasErrors(brandId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_BRAND_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		BrandModel brandModel = BrandModel.getBrandDetailsByBrandId(brandId);
		brandModel.setBrandName(brandName);
		brandModel.setBrandDescription(brandDescription);
		brandModel.setBrandImage(hiddenBrandImage);
		brandModel.updateBrand(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_URL);
	}
	
	public boolean hasErrors(String brandId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.BRAND_NAME, messageForKeyAdmin("labelBrandName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.BRAND_NAME, messageForKeyAdmin("labelBrandName"), new DuplicateBrandNameValidationRule(brandId));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_BRAND_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}