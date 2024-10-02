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
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorProductUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.BrandModel;
import com.webapp.models.ProductImageModel;
import com.webapp.models.ProductTemplateModel;
import com.webapp.models.ProductVariantModel;
import com.webapp.models.UserModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.models.VendorStoreModel;

@Path("/add-vendor-product")
public class AddVendorProductAction extends BusinessAction {

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

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			prepareBarndAndProductTemplateFilterAndProductVariantFilterOptionsData(userIdList, "-1");
			/*
			 * String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList,
			 * ""); data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);
			 */
			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

			if (vscm == null) {
				data.put(FieldConstants.SERVICE_NAME_ERROR, messageForKeyAdmin("labelServiceNameError"));
				data.put(FieldConstants.CATEGORY_NAME_ERROR, messageForKeyAdmin("labelCategoryNameError"));
			} else {
				data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
				data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());
			}
		} else {
			/*
			 * String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null, "");
			 * data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);
			 */
			prepareBarndAndProductTemplateFilterAndProductVariantFilterOptionsData(null, "-1");

		}

		String isProductForAllVendorStoresOptions = DropDownUtils.getYesNoOption(false + "");
		data.put(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORE_OPTIONS, isProductForAllVendorStoresOptions);

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, Arrays.asList(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID), ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		data.put(FieldConstants.ADD_PRODUCT_FLOW, true + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
		return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_PRODUCT_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addProductPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.BRAND_ID) String brandId,
		@FormParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId,
		@FormParam(FieldConstants.PRODUCT_VARIANT_ID) String productVariantId,
		@FormParam(FieldConstants.PRODUCT_ACTUAL_PRICE) String productActualPrice,
		@FormParam(FieldConstants.PRODUCT_DISCOUNTED_PRICE) String productDiscountedPrice,
		@FormParam(FieldConstants.PRODUCT_INVENTORY_COUNT) String productInventoryCount,
		@FormParam(FieldConstants.IS_PRODUCT_FOR_ALL_VENDOR_STORES) String isProductForAllVendorStores,
		@FormParam(FieldConstants.VENDOR_STORE_ID_LIST_HIDDEN) String vendorStoreIdListHidden
		//@FormParam(FieldConstants.PRODUCT_BARCODE) String productBarcode
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCTS_URL)) {
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

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, Arrays.asList(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID), vendorId);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		if (productTemplateId == null) {
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID, "");
		}

		if (productVariantId == null) {
			data.put(FieldConstants.PRODUCT_VARIANT_ID, "");
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			if (productTemplateId != null) {

				ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productTemplateId);
				String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, productTemplateModel.getProductTemplateId(), brandId, null);
				data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

				if (productVariantId != null) {
					String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(userIdList, productVariantId, productTemplateModel.getProductTemplateId());
					data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
				}
			}

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList, brandId, "-1");
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

		} else {

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null, brandId, "-1");
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			if (productTemplateId != null) {

				ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productTemplateId);
				String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(null, productTemplateModel.getProductTemplateId(), brandId, null);
				data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

				if (productVariantId != null) {
					String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(null, productVariantId, productTemplateModel.getProductTemplateId());
					data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
				}
			}

		}

		data.put(FieldConstants.PRODUCT_ACTUAL_PRICE, productActualPrice);
		data.put(FieldConstants.PRODUCT_DISCOUNTED_PRICE, productDiscountedPrice);
		data.put(FieldConstants.PRODUCT_INVENTORY_COUNT, productInventoryCount);
		//data.put(FieldConstants.PRODUCT_BARCODE, productBarcode);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
		if (vscm != null) {
			data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
			data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());
		}

		boolean isProductForAllVendorStoresBoolean = Boolean.parseBoolean(isProductForAllVendorStores);

		if (hasErrors(isProductForAllVendorStoresBoolean, vendorStoreId, productTemplateId, productVariantId)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_PRODUCT_JSP);
		}

		if (vscm == null) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_PRODUCT_JSP);
		}

		String productName = "";
		String productInformation = "";
		String productWeight = "";
		String productWeightUnit = "";
		String productSpecification = "";
		String productQuantityType = "";
		String isNonVeg = "";
		String productImage = "";
		String productSubCategoryId = "";
		String productCategoryId = "";
		String productBarcode = "";

		ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productTemplateId);
		if (productTemplateModel != null) {
			productInformation = productTemplateModel.getProductInformation();
			productSpecification = productTemplateModel.getProductSpecification();
			productWeightUnit = String.valueOf(productTemplateModel.getUomId());
			productCategoryId = productTemplateModel.getProductCategoryId();
			productSubCategoryId = productTemplateModel.getProductSubCategoryId();
		}

		ProductVariantModel productVariantModel = ProductVariantModel.getProductVariantDetailsByProductVariantId(productVariantId);

		if (productVariantModel != null) {
			productWeight = String.valueOf(productVariantModel.getWeight());
			productName = productVariantModel.getProductVariantName();
			productQuantityType = productVariantModel.getProductQuantityType();
			isNonVeg = String.valueOf(productVariantModel.isNonVeg());
			productBarcode = productVariantModel.getBarcode();

			List<ProductImageModel> productImageList = ProductImageModel.getProductImageListByProductVariant(productVariantId);

			if (productImageList.size() > 0) {
				productImage = productImageList.get(0).getProductImageUrl();
			}
		}

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);

		if (vendorStoreList.isEmpty()) {
			data.put(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE, messageForKey(FieldConstants.ERROR_NO_VENDOR_STORES_AVAILABLE));
			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_PRODUCT_JSP);
		}

		//@formatter:off
		VendorProductUtils.insertProductThroughProductTemplateIntoSystemViaAdminPanel(vendorId, productCategoryId, productName, 
			productInformation, productActualPrice, productDiscountedPrice, productWeight, 
			productWeightUnit, productSpecification, productInventoryCount,
			isProductForAllVendorStores, vendorStoreId, productImage, loginSessionMap.get(LoginUtils.USER_ID), 
			assignedRegionList, isProductForAllVendorStoresBoolean, productBarcode, productQuantityType, isNonVeg, "0", 
			productSubCategoryId, productTemplateId, productVariantId);
		//@formatter:on

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
	}

	public boolean hasErrors(boolean isProductForAllVendorStoresBoolean, List<String> vendorStoreIdList, String productTemplateId, String productVariantID) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRODUCT_ACTUAL_PRICE, BusinessAction.messageForKeyAdmin("labelProductActualPrice"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_DISCOUNTED_PRICE, BusinessAction.messageForKeyAdmin("labelProductDiscountedPrice"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), new MinMaxValueValidationRule(0, 10000000));

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

			validator.addValidationMapping(FieldConstants.VENDOR_STORE_ID, BusinessAction.messageForKeyAdmin("labelVendorStores"), new RequiredListValidationRule(size));
		}

		if (productTemplateId == null) {
			validator.addValidationMapping(FieldConstants.PRODUCT_TEMPLATE_ID, BusinessAction.messageForKeyAdmin("labelProductTemplate"), new RequiredListValidationRule(0));
		}

		if (productVariantID == null) {
			validator.addValidationMapping(FieldConstants.PRODUCT_VARIANT_ID, BusinessAction.messageForKeyAdmin("labelProductVariant"), new RequiredListValidationRule(0));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	private void prepareBarndAndProductTemplateFilterAndProductVariantFilterOptionsData(List<String> userIdList, String approvedBrands) {

		List<BrandModel> brandModels = BrandModel.getBrandSearch(0, 0, "%%", 0, 0, userIdList, "1", approvedBrands);

		if (brandModels != null && brandModels.size() > 0) {
			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList, brandModels.get(0).getBrandId(), brandModels);
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			List<ProductTemplateModel> productTemplateModels = ProductTemplateModel.getProductTemplateList(userIdList, brandModels.get(0).getBrandId(), null);

			if (productTemplateModels != null && productTemplateModels.size() > 0) {
				String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, productTemplateModels.get(0).getProductTemplateId(), productTemplateModels);
				data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

				List<ProductVariantModel> productVariantModels = ProductVariantModel.getProductVariantList(userIdList, productTemplateModels.get(0).getProductTemplateId());

				if (productVariantModels != null && productVariantModels.size() > 0) {
					String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptionsForOldData(userIdList, productVariantModels.get(0).getProductVariantId(), productVariantModels);
					data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
				}

			}

		}

	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_VENDOR_PRODUCT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
