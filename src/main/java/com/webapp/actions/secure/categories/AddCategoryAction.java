package com.webapp.actions.secure.categories;

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

import com.jeeutils.validator.DuplicateServiceNameValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.CategoryModel;

@Path("/add-category")
public class AddCategoryAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addCategoryGet(
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

		String serviceIdOptions = DropDownUtils.getSuperServicesList(false, "1", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_CATEGORIES_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addCategoryPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.SERVICE_ID) String serviceId,
		@FormParam(FieldConstants.CATEGORY_NAME) String categoryName,
		@FormParam(FieldConstants.CATEGORY_DESCRIPTION) String categoryDescription
		) throws ServletException, SQLException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.CATEGORY_NAME, categoryName);
		data.put(FieldConstants.CATEGORY_DESCRIPTION, categoryDescription);

		if (hasErrors()) {

			String serviceIdOptions = DropDownUtils.getSuperServicesList(false, "1", serviceId);
			data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_CATEGORIES_JSP);
		}

		CategoryModel categoryModel = new CategoryModel();
		categoryModel.setServiceId(serviceId);
		categoryModel.setCategoryName(categoryName);
		categoryModel.setCategoryDescription(categoryDescription);
		categoryModel.insertCategory(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.CATEGORY_NAME, messageForKeyAdmin("labelCategoryName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CATEGORY_NAME, messageForKeyAdmin("labelCategoryName"), new DuplicateServiceNameValidationRule(null));
		validator.addValidationMapping(FieldConstants.CATEGORY_DESCRIPTION, messageForKeyAdmin("labelCategoryDescription"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_CATEGORIES_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}