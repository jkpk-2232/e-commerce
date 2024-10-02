package com.webapp.actions.secure.productsubcategory;

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
import com.jeeutils.validator.DuplicateProductSubCategoryNameValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.ProductSubCategoryModel;

@Path("/edit-product-sub-category")
public class EditProductSubCategoryAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editProductSubCategoryGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_SUB_CATEGORY_ID) String productSubCategoryId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		ProductSubCategoryModel productSubCategoryModel = ProductSubCategoryModel.getProductSubCategoryDetailsByProductSubCategoryId(productSubCategoryId);
		
		String productCategoryIdOptions = DropDownUtils.getProductCategoryOption(productSubCategoryModel.getProductCategoryId());
		data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_NAME, productSubCategoryModel.getProductSubCategoryName());
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_DESCRIPTION, productSubCategoryModel.getProductSubCategoryDescription());
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID, productSubCategoryId);
		
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN, StringUtils.validString(productSubCategoryModel.getProductSubCategoryImage()) ? productSubCategoryModel.getProductSubCategoryImage() : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN_DUMMY, StringUtils.validString(productSubCategoryModel.getProductSubCategoryImage()) ? productSubCategoryModel.getProductSubCategoryImage() : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_SUB_CATEGORY_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_SUB_CATEGORY_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editProductSubCategoryPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PRODUCT_SUB_CATEGORY_ID) String productSubCategoryId,
		@FormParam(FieldConstants.PRODUCT_SUB_CATEGORY_NAME) String productSubCategoryName,
		@FormParam(FieldConstants.PRODUCT_SUB_CATEGORY_DESCRIPTION) String productSubCategoryDescription,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId,
		@FormParam(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN) String hiddenProductSubCategoryImage
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		String productCategoryIdOptions = DropDownUtils.getProductCategoryOption(productCategoryId);
		data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN, StringUtils.validString(hiddenProductSubCategoryImage) ? hiddenProductSubCategoryImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenProductSubCategoryImage) ? hiddenProductSubCategoryImage : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_NAME, productSubCategoryName);
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_DESCRIPTION, productSubCategoryDescription);
		data.put(FieldConstants.PRODUCT_SUB_CATEGORY_IMAGE_HIDDEN, hiddenProductSubCategoryImage);

		if (hasErrors(productSubCategoryId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_SUB_CATEGORY_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_SUB_CATEGORY_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		ProductSubCategoryModel productSubCategoryModel = ProductSubCategoryModel.getProductSubCategoryDetailsByProductSubCategoryId(productSubCategoryId);
		productSubCategoryModel.setProductSubCategoryName(productSubCategoryName);
		productSubCategoryModel.setProductSubCategoryDescription(productSubCategoryDescription);
		productSubCategoryModel.setProductSubCategoryImage(hiddenProductSubCategoryImage);
		productSubCategoryModel.setProductCategoryId(productCategoryId);
		
		productSubCategoryModel.updateProductSubCategory(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_SUB_CATEGORY_URL);
	}

	public boolean hasErrors(String productSubCategoryId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRODUCT_SUB_CATEGORY_NAME, messageForKeyAdmin("labelProductSubCategoryName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_SUB_CATEGORY_NAME, messageForKeyAdmin("labelProductSubCategoryName"), new DuplicateProductSubCategoryNameValidationRule(productSubCategoryId));
		validator.addValidationMapping(FieldConstants.PRODUCT_SUB_CATEGORY_DESCRIPTION, messageForKeyAdmin("labelProductSubCategoryDescription"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_PRODUCT_SUB_CATEGORY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}