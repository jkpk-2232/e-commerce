package com.webapp.actions.secure.productcategory;

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
import com.jeeutils.validator.DuplicateProductCategoryNameValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.ProductCategoryModel;

@Path("/edit-product-category")
public class EditProductCategoryAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editProductCategoryGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(productCategoryId);

		data.put(FieldConstants.PRODUCT_CATEGORY_NAME, productCategoryModel.getProductCategoryName());
		data.put(FieldConstants.PRODUCT_CATEGORY_DESCRIPTION, productCategoryModel.getProductCategoryDescription());
		data.put(FieldConstants.PRODUCT_CATEGORY_ID, productCategoryId);
		
		data.put(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN, StringUtils.validString(productCategoryModel.getProductCategoryImage()) ? productCategoryModel.getProductCategoryImage() : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN_DUMMY, StringUtils.validString(productCategoryModel.getProductCategoryImage()) ? productCategoryModel.getProductCategoryImage() : ProjectConstants.DEFAULT_IMAGE);


		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL);
		
		return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_CATEGORY_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editSuperServicesPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_NAME) String productCategoryName,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_DESCRIPTION) String productCategoryDescription,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN) String hiddenProductCategoryImage
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		data.put(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN, StringUtils.validString(hiddenProductCategoryImage) ? hiddenProductCategoryImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenProductCategoryImage) ? hiddenProductCategoryImage : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.PRODUCT_CATEGORY_NAME, productCategoryName);
		data.put(FieldConstants.PRODUCT_CATEGORY_DESCRIPTION, productCategoryDescription);
		data.put(FieldConstants.PRODUCT_CATEGORY_IMAGE_HIDDEN, hiddenProductCategoryImage);

		if (hasErrors(productCategoryId)) {


			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL);
			
			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_CATEGORY_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(productCategoryId);
		productCategoryModel.setProductCategoryName(productCategoryName);
		productCategoryModel.setProductCategoryDescription(productCategoryDescription);
		productCategoryModel.setProductCategoryImage(hiddenProductCategoryImage);
		productCategoryModel.updateProductCategory(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL);
	}

	public boolean hasErrors(String productCategoryId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY_NAME, messageForKeyAdmin("labelProductCategoryName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY_NAME, messageForKeyAdmin("labelProductCategoryName"), new DuplicateProductCategoryNameValidationRule(productCategoryId));
		validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY_DESCRIPTION, messageForKeyAdmin("labelProductCategoryDescription"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_PRODUCT_CATEGORY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
