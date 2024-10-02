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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.StringUtils;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
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
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.models.VendorStoreModel;

@Path("/add-product")
public class AddProductAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addProductGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
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

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(null, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
			data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);
			
			data.put(FieldConstants.VENDOR_ID, loginSessionMap.get(LoginUtils.USER_ID));
			
			List<String>	productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(loginSessionMap.get(LoginUtils.USER_ID));
			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(productCategoryIdList);
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

			if (vscm == null) {
				data.put(FieldConstants.SERVICE_NAME_ERROR, messageForKeyAdmin("labelServiceNameError"));
				data.put(FieldConstants.CATEGORY_NAME_ERROR, messageForKeyAdmin("labelCategoryNameError"));
			} else {
				data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
				data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());
			}
		}

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, CourierUtils.listOfServiceTypesProductAddition(), ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String productWeightUnitOptions = DropDownUtils.getProductWeightUnitOption(ProjectConstants.WeightConstants.GRAMS_ID + "");
		data.put(FieldConstants.PRODUCT_WEIGHT_UNIT_OPTIONS, productWeightUnitOptions);

		String isProductForAllVendorStoresOptions = DropDownUtils.getYesNoOption(false + "");
		data.put(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORE_OPTIONS, isProductForAllVendorStoresOptions);
		
		String isNonVegOptions = DropDownUtils.getYesNoOption(false + "");
		data.put(FieldConstants.IS_NON_VEG_OPTIONS, isNonVegOptions);

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.ADD_PRODUCT_FLOW, true + "");
		
		//String productCategoryIdOptions = DropDownUtils.getProductCategoryOption("");
		//data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		
		String productQuantityTypeOptions = DropDownUtils.getProductQuantityTypeOption(ProjectConstants.QuantityTypeConstants.PIECES_OPTION + "");
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE_OPTIONS,productQuantityTypeOptions);
		
		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
		
		return loadView(UrlConstants.JSP_URLS.ADD_PRODUCT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addProductPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
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
		@FormParam(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORES) String isProductForAllVendorStores,
		@FormParam(FieldConstants.VENDOR_STORE_ID_LIST_HIDDEN) String vendorStoreIdListHidden,
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
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		logger.info("\n\n\nvendorStoreIdListHidden\t" + vendorStoreIdListHidden);
		logger.info("\n\n\nisProductForAllVendorStores\t" + isProductForAllVendorStores);

		List<String> vendorStoreId = Arrays.asList(vendorStoreIdListHidden.split("\\s*,\\s*"));
		logger.info("\n\n\nvendorStoreId\t" + vendorStoreId);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String isProductForAllVendorStoresOptions = DropDownUtils.getYesNoOption(isProductForAllVendorStores);
		data.put(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORE_OPTIONS, isProductForAllVendorStoresOptions);

		data.put(FieldConstants.VENDOR_STORE_ID, "");
		data.put(FieldConstants.VENDOR_STORE_ID_LIST_HIDDEN, vendorStoreIdListHidden);

		String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(vendorStoreId, vendorId, assignedRegionList);
		data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		data.put(FieldConstants.ADD_PRODUCT_FLOW, true + "");

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, CourierUtils.listOfServiceTypesProductAddition(), vendorId);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String productWeightUnitOptions = DropDownUtils.getProductWeightUnitOption(productWeightUnit + "");
		data.put(FieldConstants.PRODUCT_WEIGHT_UNIT_OPTIONS, productWeightUnitOptions);

		data.put(FieldConstants.PRODUCT_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenProductImage) ? hiddenProductImage : ProjectConstants.DEFAULT_IMAGE);
		
		// String productCategoryIdOptions = DropDownUtils.getProductCategoryOption(productCategoryId);
		// data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		
		List<String>	productCategoryIdList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(vendorId);
		if (productCategoryId != null) {
			String productCategoryIdOptions = DropDownUtils.getProductCategoryAssocListOption(productCategoryIdList, productCategoryId);
			data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		}
		
		String productQuantityTypeOptions = DropDownUtils.getProductQuantityTypeOption(productQuantityType);
		data.put(FieldConstants.PRODUCT_QUANTITY_TYPE_OPTIONS,productQuantityTypeOptions);
		
		String isNonVegOptions = DropDownUtils.getYesNoOption(isNonVeg);
		data.put(FieldConstants.IS_NON_VEG_OPTIONS, isNonVegOptions);
		
		if (productCategoryId != null && productSubCategoryId != null ) {
			String productSubCategoryIdOPtions = DropDownUtils.getProductSubCategoryListOptions(productCategoryId, productSubCategoryId);
			data.put(FieldConstants.PRODUCT_SUB_CATEGORY_ID_OPTIONS,productSubCategoryIdOPtions);
		}
		

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
		data.put(FieldConstants.IS_NON_VEG, isNonVeg);

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
		if (vscm != null) {
			data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
			data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());
		}

		boolean isProductForAllVendorStoresBoolean = Boolean.parseBoolean(isProductForAllVendorStores);

		if (hasErrors(isProductForAllVendorStoresBoolean, vendorStoreId)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_PRODUCT_JSP);
		}

		if (vscm == null) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_PRODUCT_JSP);
		}

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);

		if (vendorStoreList.isEmpty()) {
			data.put(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE, messageForKey(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE));
			return loadView(UrlConstants.JSP_URLS.ADD_PRODUCT_JSP);
		}
		
		if (ProjectConstants.QuantityTypeConstants.LOOSE.equals(productQuantityType)) {
			productName = "L " + productName;
		}
		/*
		else 
			productName = "P " + productName;
		*/
		//@formatter:off
		VendorProductUtils.insertProductIntoSystemViaAdminPanel(vendorId, productCategoryId, productName, 
			productInformation, productActualPrice, productDiscountedPrice, productWeight, 
			productWeightUnit, productSpecification, productInventoryCount,
			isProductForAllVendorStores, vendorStoreId, hiddenProductImage, loginSessionMap.get(LoginUtils.USER_ID), 
			assignedRegionList, isProductForAllVendorStoresBoolean, productBarcode, productQuantityType, isNonVeg, gst, productSubCategoryId);
		//@formatter:on

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL);
	}

	public boolean hasErrors(boolean isProductForAllVendorStoresBoolean, List<String> vendorStoreIdList) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		//validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY, BusinessAction.messageForKeyAdmin("labelProductCategory"), new RequiredValidationRule());
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
		//validator.addValidationMapping(FieldConstants.PRODUCT_BARCODE, BusinessAction.messageForKeyAdmin("labelProductBarcode"), new RequiredValidationRule());

		if (!isProductForAllVendorStoresBoolean) {

			int size = 0;

			logger.info("vendorStoreIdList null   \t" + (vendorStoreIdList != null));
			logger.info("vendorStoreIdList isEmpty\t" + vendorStoreIdList.isEmpty());
			logger.info("vendorStoreIdList size   \t" + vendorStoreIdList.size());

			for (String string : vendorStoreIdList) {
				logger.info("string \t" + string);
				if (StringUtils.validString(string)) {
					size++;
				}
			}

			logger.info("size size   \t" + size);

//			if (vendorStoreIdList != null && !vendorStoreIdList.isEmpty() && vendorStoreIdList.size() > 1) {
//				logger.info("vendorStoreIdList size\t" + vendorStoreIdList.size());
//				size = vendorStoreIdList.size();
//			} else {
//				logger.info("vendorStoreIdList empty\t");
//				size = 0;
//			}

			validator.addValidationMapping(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), new RequiredListValidationRule(size));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_PRODUCT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}