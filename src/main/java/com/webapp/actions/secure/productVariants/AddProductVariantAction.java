package com.webapp.actions.secure.productVariants;

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

import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.ProductVariantModel;
import com.webapp.models.UserModel;

@Path("/add-product-variant")
public class AddProductVariantAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddProductVariant (
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

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));
			String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, "", null, null);
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

		} else {

			String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(null, "", null, null);
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

		}

		String isNonVegOptions = DropDownUtils.getYesNoOption(false + "");
		data.put(FieldConstants.IS_NON_VEG_OPTIONS, isNonVegOptions);

		String productQuantityTypeOptions = DropDownUtils.getProductQuantityTypeOption(ProjectConstants.QuantityTypeConstants.PIECES_OPTION + "");
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE_OPTIONS, productQuantityTypeOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_VARIANT_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_PRODUCT_VARIANT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addProductVariant (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PRODUCT_VARIANT_NAME) String productVariantName,
		@FormParam(FieldConstants.PRODUCT_VARIANT_DESCRIPTION) String productVariantDescription,
		@FormParam(FieldConstants.PRODUCT_VARIANT_PRICE) String productVariantPrice,
		@FormParam(FieldConstants.BARCODE) String barcode,
		@FormParam(FieldConstants.WEIGHT) String weight,
		@FormParam(FieldConstants.COLOR) String color,
		@FormParam(FieldConstants.PRODUCT_VARIANT_SKU) String productVariantSku,
		@FormParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId,
		@FormParam(FieldConstants.PRODUCT_QUANTITY_TYPE) String productQuantityType,
		@FormParam(FieldConstants.IS_NON_VEG) String isNonVeg
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

			String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, productTemplateId, null, null);
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

		} else {

			String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(null, productTemplateId, null, null);
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

		}

		String productQuantityTypeOptions = DropDownUtils.getProductQuantityTypeOption(productQuantityType);
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE_OPTIONS, productQuantityTypeOptions);

		String isNonVegOptions = DropDownUtils.getYesNoOption(isNonVeg);
		data.put(FieldConstants.IS_NON_VEG_OPTIONS, isNonVegOptions);

		data.put(FieldConstants.PRODUCT_VARIANT_NAME, productVariantName);
		data.put(FieldConstants.PRODUCT_VARIANT_DESCRIPTION, productVariantDescription);
		data.put(FieldConstants.PRODUCT_VARIANT_PRICE, productVariantPrice);
		data.put(FieldConstants.BARCODE, barcode);
		data.put(FieldConstants.WEIGHT, weight);
		data.put(FieldConstants.COLOR, color);
		data.put(FieldConstants.PRODUCT_VARIANT_SKU, productVariantSku);
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE, productQuantityType);
		data.put(FieldConstants.IS_NON_VEG, isNonVeg);

		if (hasErrors()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_VARIANT_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_PRODUCT_VARIANT_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		if (ProjectConstants.QuantityTypeConstants.LOOSE.equals(productQuantityType)) {
			productVariantName = "L " + productVariantName;
		}

		ProductVariantModel variantModel = new ProductVariantModel();

		variantModel.setProductVariantName(productVariantName);
		variantModel.setProductVariantDescription(productVariantDescription);
		variantModel.setProductVariantPrice(Double.parseDouble(productVariantPrice));
		variantModel.setWeight(Double.parseDouble(weight));
		variantModel.setColor(color);
		variantModel.setProductVariantSku(productVariantSku);
		variantModel.setProductTemplateId(productTemplateId);
		variantModel.setBarcode(barcode);
		variantModel.setProductQuantityType(productQuantityType);
		variantModel.setNonVeg(Boolean.parseBoolean(isNonVeg));

		variantModel.insertProductVariant(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_VARIANT_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRODUCT_VARIANT_NAME, messageForKeyAdmin("labelProductVariantName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_VARIANT_DESCRIPTION, messageForKeyAdmin("labelProductVariantDescription"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.WEIGHT, messageForKeyAdmin("labelWeight"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_VARIANT_PRICE, messageForKeyAdmin("labelProductVariantPrice"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_PRODUCT_VARIANT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
