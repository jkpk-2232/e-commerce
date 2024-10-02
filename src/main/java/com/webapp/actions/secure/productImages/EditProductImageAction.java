package com.webapp.actions.secure.productImages;

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
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.ProductImageModel;
import com.webapp.models.UserModel;

@Path("/edit-product-image")
public class EditProductImageAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditProductImage (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_IMAGE_ID) String productImageId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		ProductImageModel productImageModel = ProductImageModel.getProductImageDetailsByProductImageId(productImageId);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(userIdList, productImageModel.getProductVariantId(), null);
			data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);

		} else {

			String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(null, productImageModel.getProductVariantId(), null);
			data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);

		}

		data.put(FieldConstants.PRODUCT_IMAGE_ID, productImageId);

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, StringUtils.validString(productImageModel.getProductImageUrl()) ? productImageModel.getProductImageUrl() : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, StringUtils.validString(productImageModel.getProductImageUrl()) ? productImageModel.getProductImageUrl() : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_IMAGE_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_IMAGE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editProductImage (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PRODUCT_IMAGE_ID) String productImageId,
		@FormParam(FieldConstants.PRODUCT_VARIANT_ID) String productVariantId,
		@FormParam(FieldConstants.PRODUCT_IMAGE_HIDDEN) String hiddenProductImage
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(userIdList, productVariantId, null);
			data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);

		} else {

			String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(null, productVariantId, null);
			data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);

		}

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, StringUtils.validString(hiddenProductImage) ? hiddenProductImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenProductImage) ? hiddenProductImage : ProjectConstants.DEFAULT_IMAGE);

		if (hasErrors(productImageId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_IMAGE_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_IMAGE_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		ProductImageModel productImageModel = ProductImageModel.getProductImageDetailsByProductImageId(productImageId);
		productImageModel.setProductVariantId(productVariantId);
		productImageModel.setProductImageUrl(hiddenProductImage);
		productImageModel.updateProductImage(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_IMAGE_URL);
	}

	public boolean hasErrors(String brandId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		// validator.addValidationMapping(FieldConstants.PRODUCT_VARIANT_ID,
		// messageForKeyAdmin("labelProductVariantId"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_PRODUCT_IMAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
