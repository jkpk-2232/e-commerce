package com.webapp.actions.secure.productTemplates;

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
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorProductCategoryAssocUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.ProductTemplateModel;
import com.webapp.models.UserModel;

@Path("/edit-product-template")
public class EditProductTemplateAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditPrdouctTemplate (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productTemplateId);
		
		

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList, productTemplateModel.getBrandId(), "1");
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			List<String> productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(loginSessionMap.get(LoginUtils.USER_ID));

			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(productCategoryIdList, productTemplateModel.getProductCategoryId());
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		} else {

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null, productTemplateModel.getBrandId(), "1");
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			String productCategoryIdOptions = DropDownUtils.getProductCategoryListOption(productTemplateModel.getProductCategoryId());
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

		}

		String uomIdOptions = DropDownUtils.getUOMListOptions(String.valueOf(productTemplateModel.getUomId()));
		data.put(FieldConstants.UOM_ID_OPTIONS, uomIdOptions);

		String taxApplicableOptions = DropDownUtils.getYesNoOption(productTemplateModel.isTaxApplicable() + "");
		data.put(FieldConstants.TAX_APPLICABLE_OPTIONS, taxApplicableOptions);

		String isProductToAllOptions = DropDownUtils.getYesNoOption(productTemplateModel.isProductToAll() + "");
		data.put(FieldConstants.IS_PRODUCT_TO_ALL_OPTIONS, isProductToAllOptions);
		
		if (productTemplateModel.getProductCategoryId() != null) {
			String productsubCategoryId = "";
			if (productTemplateModel.getProductSubCategoryId() != null) {
				productsubCategoryId = productTemplateModel.getProductSubCategoryId();
			}
			String productSubCategoryIdOPtions = DropDownUtils.getProductSubCategoryListOptions(productTemplateModel.getProductCategoryId(), productsubCategoryId);
			data.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID_OPTIONS, productSubCategoryIdOPtions);
		}

		data.put(FieldConstants.PRODUCT_TEMPLATE_ID, productTemplateId);
		data.put(FieldConstants.PRODUCT_NAME, productTemplateModel.getProductName());
		data.put(FieldConstants.PRODUCT_INFORMATION, productTemplateModel.getProductInformation());
		data.put(FieldConstants.PRODUCT_SPECIFICATION, productTemplateModel.getProductSpecification());
		data.put(FieldConstants.HSN_CODE, productTemplateModel.getHsnCode());

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, StringUtils.validString(productTemplateModel.getProductImage()) ? productTemplateModel.getProductImage() : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, StringUtils.validString(productTemplateModel.getProductImage()) ? productTemplateModel.getProductImage() : ProjectConstants.DEFAULT_IMAGE);
		
		data.put("isExists", String.valueOf(false));
		
		data.put(FieldConstants.ADD_PRODUCT_TEMPLATE_FLOW, false + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_TEMPLATE_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editProductTemplate (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId,
		@FormParam(FieldConstants.PRODUCT_NAME) String productName,
		@FormParam(FieldConstants.PRODUCT_INFORMATION) String productInformation,
		@FormParam(FieldConstants.PRODUCT_SPECIFICATION) String productSpecification,
		@FormParam(FieldConstants.TAX_APPLICABLE) String taxApplicable,
		@FormParam(FieldConstants.IS_PRODUCT_TO_ALL) String isProductToAll,
		@FormParam(FieldConstants.HSN_CODE) String hsnCode,
		@FormParam(FieldConstants.BRAND_ID) String brandId,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId,
		@FormParam(FieldConstants.UOM_ID) String uomId,
		@FormParam(FieldConstants.PRODUCT_QUANTITY_TYPE) String productQuantityType,
		@FormParam(FieldConstants.PRODUCT_SUB_CATEGORY_ID) String productSubCategoryId,
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
		
		data.put(FieldConstants.ADD_PRODUCT_TEMPLATE_FLOW, false + "");
		
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, StringUtils.validString(hiddenProductImage) ? hiddenProductImage : ProjectConstants.DEFAULT_IMAGE);
		
		if (brandId == null) {
			data.put(FieldConstants.BRAND_ID, "");
		}
		

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList, brandId, "1");
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			List<String> productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(loginSessionMap.get(LoginUtils.USER_ID));

			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(productCategoryIdList, productCategoryId);
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		} else {

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null, brandId, "1");
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			String productCategoryIdOptions = DropDownUtils.getProductCategoryListOption(productCategoryId);
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

		}

		String uomIdOptions = DropDownUtils.getUOMListOptions(uomId);
		data.put(FieldConstants.UOM_ID_OPTIONS, uomIdOptions);
		
		String taxApplicableOptions = DropDownUtils.getYesNoOption(taxApplicable);
		data.put(FieldConstants.TAX_APPLICABLE_OPTIONS, taxApplicableOptions);
		
		String isProductToAllOptions = DropDownUtils.getYesNoOption(isProductToAll);
		data.put(FieldConstants.IS_PRODUCT_TO_ALL_OPTIONS, isProductToAllOptions);
		
		if (productCategoryId != null && productSubCategoryId != null) {	
			String productSubCategoryIdOPtions = DropDownUtils.getProductSubCategoryListOptions(productCategoryId, productSubCategoryId);
			data.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID_OPTIONS,productSubCategoryIdOPtions);
		}

		data.put(FieldConstants.PRODUCT_NAME, productName);
		data.put(FieldConstants.PRODUCT_INFORMATION, productInformation);
		data.put(FieldConstants.PRODUCT_SPECIFICATION, productSpecification);
		data.put(FieldConstants.HSN_CODE, hsnCode);
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE, productQuantityType);
		data.put(FieldConstants.PRODUCT_TEMPLATE_ID, productTemplateId);

		if (hasErrors(productTemplateId, brandId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_TEMPLATE_JSP);
		}
		
		boolean isExists = ProductTemplateModel.isProductNameAndUomIdExists(productName.toUpperCase(), Integer.parseInt(uomId), productTemplateId);
		
		if (isExists) {
			
			data.put("isExists", String.valueOf(true));
			data.put("errorMessage", "Product Name And Weight Unit Combination Already Exists");
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_TEMPLATE_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productTemplateId);
		productTemplateModel.setProductName(productName);
		productTemplateModel.setProductInformation(productInformation);
		productTemplateModel.setProductSpecification(productSpecification);
		productTemplateModel.setTaxApplicable( Boolean.parseBoolean(taxApplicable));
		productTemplateModel.setProductToAll(Boolean.parseBoolean(isProductToAll));
		productTemplateModel.setHsnCode(hsnCode);
		productTemplateModel.setBrandId(brandId);
		productTemplateModel.setProductCategoryId(productCategoryId);
		productTemplateModel.setUomId(Integer.valueOf(uomId));
		productTemplateModel.setProductSubCategoryId(productSubCategoryId);
		productTemplateModel.setPrdQtyType(productQuantityType);
		
		productTemplateModel.updateProductTemplate(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_TEMPLATE_URL);
	}
	
	public boolean hasErrors(String productTemplateId, String brandId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRODUCT_NAME, messageForKeyAdmin("labelProductName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INFORMATION, messageForKeyAdmin("labelProductInformation"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_SPECIFICATION, messageForKeyAdmin("labelProductSpecification"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.HSN_CODE, messageForKeyAdmin("labelHSNCode"), new RequiredValidationRule());
		//validator.addValidationMapping(FieldConstants.PRODUCT_NAME, messageForKeyAdmin("labelProductName"), new DuplicateProductNameValidationRule(productTemplateId));
		
		if (brandId == null) {
			validator.addValidationMapping(FieldConstants.BRAND_ID, BusinessAction.messageForKeyAdmin("labelBrand"), new RequiredListValidationRule(0));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
	
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_PRODUCT_TEMPLATE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
