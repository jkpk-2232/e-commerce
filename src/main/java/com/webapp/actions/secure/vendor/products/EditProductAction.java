package com.webapp.actions.secure.vendor.products;

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
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorProductCategoryAssocUtils;
import com.utils.myhub.VendorProductUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorServiceCategoryModel;

@Path("/edit-product")
public class EditProductAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editProductGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_PRODUCT_ID) String vendorProductId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		VendorProductModel vendorProductModel = VendorProductModel.getProductsDetailsByProductId(vendorProductId);

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorProductModel.getVendorId());
		data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
		data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, CourierUtils.listOfServiceTypesProductAddition(), vendorProductModel.getVendorId());
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		List<String>	productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(vendorProductModel.getVendorId());
		// String productCategoryIdOptions = DropDownUtils.getProductCategoryOption(vendorProductModel.getProductCategoryId());
		
		if (vendorProductModel.getProductCategoryId() != null) {
			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(productCategoryIdList, vendorProductModel.getProductCategoryId());
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		}else {
			List<String>	PCIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(vendorProductModel.getVendorId());
			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(PCIdList);
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		}
		
		
		String productWeightUnitOptions = DropDownUtils.getProductWeightUnitOption(vendorProductModel.getProductWeightUnit() + "");
		data.put(FieldConstants.PRODUCT_WEIGHT_UNIT_OPTIONS, productWeightUnitOptions);
		
		String productQuantityTypeOptions = DropDownUtils.getProductQuantityTypeOption(vendorProductModel.getPrdQtyType());
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE_OPTIONS,productQuantityTypeOptions);
		
		String isNonVegOptions = DropDownUtils.getYesNoOption(vendorProductModel.getIsNonVeg() + "");
		data.put(FieldConstants.IS_NON_VEG_OPTIONS, isNonVegOptions);
		
		if (vendorProductModel.getProductCategoryId() != null && vendorProductModel.getProductSubCategoryId() != null) {
			String productSubCategoryIdOPtions = DropDownUtils.getProductSubCategoryListOptions(vendorProductModel.getProductCategoryId(), vendorProductModel.getProductSubCategoryId());
			data.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID_OPTIONS,productSubCategoryIdOPtions);
		}
		

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, StringUtils.validString(vendorProductModel.getProductImage()) ? vendorProductModel.getProductImage() : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, StringUtils.validString(vendorProductModel.getProductImage()) ? vendorProductModel.getProductImage() : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.VENDOR_PRODUCT_ID, vendorProductModel.getVendorProductId());
		data.put(FieldConstants.PRODUCT_CATEGORY, vendorProductModel.getProductCategory());
		data.put(FieldConstants.PRODUCT_NAME, vendorProductModel.getProductName());
		data.put(FieldConstants.PRODUCT_INFORMATION, vendorProductModel.getProductInformation());
		data.put(FieldConstants.PRODUCT_ACTUAL_PRICE, df.format(vendorProductModel.getProductActualPrice()));
		data.put(FieldConstants.PRODUCT_DISCOUNTED_PRICE, df.format(vendorProductModel.getProductDiscountedPrice()));
		data.put(FieldConstants.PRODUCT_WEIGHT, df.format(vendorProductModel.getProductWeight()));
		data.put(FieldConstants.PRODUCT_SPECIFICATION, vendorProductModel.getProductSpecification());
		data.put(FieldConstants.PRODUCT_INVENTORY_COUNT, vendorProductModel.getProductInventoryCount() + "");
		data.put(FieldConstants.PRODUCT_BARCODE, vendorProductModel.getProductBarcode());
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE, vendorProductModel.getPrdQtyType());
		data.put(FieldConstants.GST, df.format(vendorProductModel.getGst()));

		data.put(FieldConstants.ADD_PRODUCT_FLOW, false + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editProductPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_PRODUCT_ID) String vendorProductId,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		/*@FormParam(FieldConstants.PRODUCT_CATEGORY) String productCategory,*/
		@FormParam(FieldConstants.PRODUCT_CATEGORY_ID) String productCategoryId,
		@FormParam(FieldConstants.PRODUCT_NAME) String productName,
		@FormParam(FieldConstants.PRODUCT_BARCODE) String productBarcode,
		@FormParam(FieldConstants.PRODUCT_INFORMATION) String productInformation,
		@FormParam(FieldConstants.PRODUCT_ACTUAL_PRICE) String productActualPrice,
		@FormParam(FieldConstants.PRODUCT_DISCOUNTED_PRICE) String productDiscountedPrice,
		@FormParam(FieldConstants.PRODUCT_WEIGHT) String productWeight,
		@FormParam(FieldConstants.PRODUCT_WEIGHT_UNIT) String productWeightUnit,
		@FormParam(FieldConstants.PRODUCT_SPECIFICATION) String productSpecification,
		@FormParam(FieldConstants.PRODUCT_INVENTORY_COUNT) String productInventoryCount,
		@FormParam(FieldConstants.PRODUCT_IMAGE_HIDDEN) String hiddenProductImage,
		@FormParam(FieldConstants.PRODUCT_QUANTITY_TYPE) String productQuantityType,
		@FormParam(FieldConstants.PRODUCT_SUB_CATEGORY_ID) String productSubCategoryId,
		@FormParam(FieldConstants.IS_NON_VEG) String isNonVeg,
		@FormParam(FieldConstants.GST) String gst
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.ADD_PRODUCT_FLOW, false + "");

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, CourierUtils.listOfServiceTypesProductAddition(), vendorId);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String productWeightUnitOptions = DropDownUtils.getProductWeightUnitOption(productWeightUnit + "");
		data.put(FieldConstants.PRODUCT_WEIGHT_UNIT_OPTIONS, productWeightUnitOptions);

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, StringUtils.validString(hiddenProductImage) ? hiddenProductImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenProductImage) ? hiddenProductImage : ProjectConstants.DEFAULT_IMAGE);
		
		List<String>	productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(vendorId);
		if (productCategoryId != null) {
			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(productCategoryIdList, productCategoryId);
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		}
		
		String productQuantityTypeOptions = DropDownUtils.getProductQuantityTypeOption(productQuantityType);
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE_OPTIONS,productQuantityTypeOptions);
		
		String isNonVegOptions = DropDownUtils.getYesNoOption(isNonVeg);
		data.put(FieldConstants.IS_NON_VEG_OPTIONS, isNonVegOptions);
		
		if (productCategoryId != null && productSubCategoryId != null) {
			String productSubCategoryIdOPtions = DropDownUtils.getProductSubCategoryListOptions(productCategoryId, productSubCategoryId);
			data.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID_OPTIONS,productSubCategoryIdOPtions);
		}
		

		data.put(FieldConstants.VENDOR_PRODUCT_ID, vendorProductId);
		/* data.put(FieldConstants.PRODUCT_CATEGORY, productCategory); */
		data.put(FieldConstants.PRODUCT_CATEGORY_ID, productCategoryId);
		data.put(FieldConstants.PRODUCT_NAME, productName);
		data.put(FieldConstants.PRODUCT_INFORMATION, productInformation);
		data.put(FieldConstants.PRODUCT_ACTUAL_PRICE, productActualPrice);
		data.put(FieldConstants.PRODUCT_DISCOUNTED_PRICE, productDiscountedPrice);
		data.put(FieldConstants.PRODUCT_WEIGHT, productWeight);
		data.put(FieldConstants.PRODUCT_SPECIFICATION, productSpecification);
		data.put(FieldConstants.PRODUCT_INVENTORY_COUNT, productInventoryCount);
		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN, hiddenProductImage);
		data.put(FieldConstants.PRODUCT_BARCODE, productBarcode);
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE, productQuantityType);
		data.put(FieldConstants.GST, gst);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
		if (vscm != null) {
			data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
			data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());
		}

		if (hasErrors()) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_JSP);
		}

		if (vscm == null) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_PRODUCT_JSP);
		}
		
		if (ProjectConstants.QuantityTypeConstants.LOOSE.equals(productQuantityType)) {
			if (!productName.contains("L ")) {
				if (productName.contains("P ")) {
					productName = productName.substring(1);
				}
				productName = "L " + productName;
			}
		}
		/*
		else {
			if (!productName.contains("P ")) {
				if (productName.contains("L ")) {
					productName = productName.substring(1);
				}
				productName = "P " + productName;
			}
		}*/

		//@formatter:off
		VendorProductUtils.updateProductIntoSystemViaAdminPanel(vendorProductId, vendorId, productCategoryId, productName, 
			productInformation, productActualPrice, productDiscountedPrice, productWeight, productWeightUnit, 
			productSpecification, productInventoryCount, hiddenProductImage,
			loginSessionMap.get(LoginUtils.USER_ID), productBarcode, productQuantityType, isNonVeg, gst, productSubCategoryId);
		//@formatter:on

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);

	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		// validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY, BusinessAction.messageForKeyAdmin("labelProductCategory"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_NAME, BusinessAction.messageForKeyAdmin("labelProductName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INFORMATION, BusinessAction.messageForKeyAdmin("labelProductInformation"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_ACTUAL_PRICE, BusinessAction.messageForKeyAdmin("labelProductActualPrice"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_DISCOUNTED_PRICE, BusinessAction.messageForKeyAdmin("labelProductDiscountedPrice"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_WEIGHT, BusinessAction.messageForKeyAdmin("labelProductWeight"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_WEIGHT, BusinessAction.messageForKeyAdmin("labelProductWeight"), new MinMaxValueValidationRule(0, 10000000));
		validator.addValidationMapping(FieldConstants.PRODUCT_SPECIFICATION, BusinessAction.messageForKeyAdmin("labelProductSpecification"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), new MinMaxValueValidationRule(0, 10000000));
		validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY_ID, BusinessAction.messageForKeyAdmin("labelProductCategory"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_PRODUCT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}