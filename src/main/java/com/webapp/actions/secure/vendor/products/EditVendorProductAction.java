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
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorServiceCategoryModel;

@Path("edit-vendor-product")
public class EditVendorProductAction extends BusinessAction {

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

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(false, assignedRegionList, Arrays.asList(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID), vendorProductModel.getVendorId());
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		// if (vendorProductModel.getProductVariantId() != null) {

		// ProductVariantModel productVariantModel =
		// ProductVariantModel.getProductVariantDetailsByProductVariantId(vendorProductModel.getProductVariantId());

		// ProductTemplateModel productTemplateModel =
		// ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productVariantModel.getProductTemplateId());

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> roleIdList = UserRoleUtils.getAdminRoleIds();

			List<String> userIdList = UserModel.getUsersListByRoleIds(roleIdList);

			userIdList.add(loginSessionMap.get(LoginUtils.USER_ID));

			if (vendorProductModel.getProductVariantId() != null) {

				ProductVariantModel productVariantModel = ProductVariantModel.getProductVariantDetailsByProductVariantId(vendorProductModel.getProductVariantId());

				ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productVariantModel.getProductTemplateId());

				prepareBarndAndProductTemplateAndProductVariantOptionsData(userIdList, productTemplateModel, productVariantModel, vendorProductModel, "-1");

			} else {

				prepareBarndAndProductTemplateFilterAndProductVariantFilterOptionsData(userIdList, "-1");
			}

			/*
			 * if (productTemplateModel != null) { String productTemplateIdOptions =
			 * DropDownUtils.getProductTemplateFilterListOptions(userIdList,
			 * productTemplateModel.getProductTemplateId(),
			 * productTemplateModel.getBrandId(),
			 * productTemplateModel.getProductCategoryId());
			 * data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS,
			 * productTemplateIdOptions); }
			 * 
			 * String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList,
			 * productTemplateModel.getBrandId()); data.put(FieldConstants.BRAND_ID_OPTIONS,
			 * brandIdOptions);
			 * 
			 * String productVariantIdOptions =
			 * DropDownUtils.getProductVariantFilterListOptions(userIdList,
			 * vendorProductModel.getProductVariantId(),
			 * vendorProductModel.getProductTemplateId());
			 * data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
			 */

		} else {

			if (vendorProductModel.getProductVariantId() != null) {

				ProductVariantModel productVariantModel = ProductVariantModel.getProductVariantDetailsByProductVariantId(vendorProductModel.getProductVariantId());

				ProductTemplateModel productTemplateModel = ProductTemplateModel.getProductTemplateDetailsByProductTemplateId(productVariantModel.getProductTemplateId());

				prepareBarndAndProductTemplateAndProductVariantOptionsData(null, productTemplateModel, productVariantModel, vendorProductModel, "-1");

			} else {

				prepareBarndAndProductTemplateFilterAndProductVariantFilterOptionsData(null, "-1");
			}

			/*
			 * 
			 * if (productTemplateModel != null) { String productTemplateIdOptions =
			 * DropDownUtils.getProductTemplateFilterListOptions(null,
			 * productTemplateModel.getProductTemplateId(),
			 * productTemplateModel.getBrandId(),
			 * productTemplateModel.getProductCategoryId());
			 * data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS,
			 * productTemplateIdOptions); }
			 * 
			 * String productVariantIdOptions =
			 * DropDownUtils.getProductVariantFilterListOptions(null,
			 * vendorProductModel.getProductVariantId(),
			 * vendorProductModel.getProductTemplateId());
			 * data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
			 * 
			 * String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null,
			 * productTemplateModel.getBrandId()); data.put(FieldConstants.BRAND_ID_OPTIONS,
			 * brandIdOptions);
			 * 
			 */

		}

		// }

		data.put(FieldConstants.VENDOR_PRODUCT_ID, vendorProductModel.getVendorProductId());
		//data.put(FieldConstants.PRODUCT_BARCODE, vendorProductModel.getProductBarcode());
		data.put(FieldConstants.PRODUCT_ACTUAL_PRICE, df.format(vendorProductModel.getProductActualPrice()));
		data.put(FieldConstants.PRODUCT_DISCOUNTED_PRICE, df.format(vendorProductModel.getProductDiscountedPrice()));
		data.put(FieldConstants.PRODUCT_INVENTORY_COUNT, vendorProductModel.getProductInventoryCount() + "");
		data.put(FieldConstants.PRODUCT_NAME, vendorProductModel.getProductName());

		data.put(FieldConstants.ADD_PRODUCT_FLOW, false + "");

		data.put("isExists", false + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_PRODUCT_JSP);
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
		@FormParam(FieldConstants.BRAND_ID) String brandId,
		@FormParam(FieldConstants.PRODUCT_TEMPLATE_ID) String productTemplateId,
		@FormParam(FieldConstants.PRODUCT_VARIANT_ID) String productVariantId,
		@FormParam(FieldConstants.PRODUCT_ACTUAL_PRICE) String productActualPrice,
		@FormParam(FieldConstants.PRODUCT_DISCOUNTED_PRICE) String productDiscountedPrice,
		@FormParam(FieldConstants.PRODUCT_INVENTORY_COUNT) String productInventoryCount
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

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.ADD_PRODUCT_FLOW, false + "");

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

		data.put(FieldConstants.VENDOR_PRODUCT_ID, vendorProductId);
		data.put(FieldConstants.PRODUCT_ACTUAL_PRICE, productActualPrice);
		data.put(FieldConstants.PRODUCT_DISCOUNTED_PRICE, productDiscountedPrice);
		data.put(FieldConstants.PRODUCT_INVENTORY_COUNT, productInventoryCount);
		//data.put(FieldConstants.PRODUCT_BARCODE, productBarcode);

		VendorProductModel vendorProductModel = VendorProductModel.getProductsDetailsByProductId(vendorProductId);

		data.put(FieldConstants.PRODUCT_NAME, vendorProductModel.getProductName());

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
		if (vscm != null) {
			data.put(FieldConstants.SERVICE_NAME, vscm.getServiceName());
			data.put(FieldConstants.CATEGORY_NAME, vscm.getCategoryName());
		}

		if (hasErrors(productTemplateId, productVariantId)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_PRODUCT_JSP);
		}

		if (vscm == null) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_PRODUCT_JSP);
		}

		boolean productExists = vendorProductModel.isVendorStoreIdAndProductVariantIdExists(vendorProductModel.getVendorStoreId(), productVariantId, vendorProductId);

		if (productExists) {

			data.put("isExists", true + "");
			data.put("errorMessage", "Stroe And Product Combination Already Exists");

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_PRODUCT_JSP);
		}

		String productName = "";
		String productInformation = "";
		String productWeight = "";
		String productWeightUnit = "";
		String productSpecification = "";
		String productQuantityType = "";
		String isNonVeg = "";
		String productImage = "";
		String productCategoryId = "";
		String productSubCategoryId = "";
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

		//@formatter:off
		VendorProductUtils.updateProductThroughProductTemplateIntoSystemViaAdminPanel(vendorProductId, vendorId, productCategoryId, productName, 
			productInformation, productActualPrice, productDiscountedPrice, productWeight, productWeightUnit, 
			productSpecification, productInventoryCount, productImage,
			loginSessionMap.get(LoginUtils.USER_ID), productBarcode, productQuantityType, isNonVeg, "0", 
			productSubCategoryId, productTemplateId, productVariantId);
		//@formatter:on

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_PRODUCTS_URL);

	}

	public boolean hasErrors(String productTemplateId, String productVariantId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PRODUCT_ACTUAL_PRICE, BusinessAction.messageForKeyAdmin("labelProductActualPrice"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_DISCOUNTED_PRICE, BusinessAction.messageForKeyAdmin("labelProductDiscountedPrice"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PRODUCT_INVENTORY_COUNT, BusinessAction.messageForKeyAdmin("labelProductInventoryCount"), new MinMaxValueValidationRule(0, 10000000));

		if (productTemplateId == null) {
			validator.addValidationMapping(FieldConstants.PRODUCT_TEMPLATE_ID, BusinessAction.messageForKeyAdmin("labelProductTemplate"), new RequiredListValidationRule(0));
		}

		if (productVariantId == null) {
			validator.addValidationMapping(FieldConstants.PRODUCT_VARIANT_ID, BusinessAction.messageForKeyAdmin("labelProductVariant"), new RequiredListValidationRule(0));
		}

		Map<String, String> resultsForValidation = validator.validate(data);
		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	private void prepareBarndAndProductTemplateAndProductVariantOptionsData(List<String> userIdList, ProductTemplateModel productTemplateModel, ProductVariantModel productVariantModel, VendorProductModel vendorProductModel, String approvedBrands) {

		if (productTemplateModel != null) {

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList, productTemplateModel.getBrandId(), approvedBrands);
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, productTemplateModel.getProductTemplateId(), productTemplateModel.getBrandId(), productTemplateModel.getProductCategoryId());
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);

		} else {

			String brandIdOptions = DropDownUtils.getBrandFilterListOptions(userIdList, "", approvedBrands);
			data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

			String productTemplateIdOptions = DropDownUtils.getProductTemplateFilterListOptions(userIdList, "", null, null);
			data.put(FieldConstants.PRODUCT_TEMPLATE_ID_OPTIONS, productTemplateIdOptions);
		}

		if (vendorProductModel.getProductVariantId() != null) {
			String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(userIdList, vendorProductModel.getProductVariantId(), vendorProductModel.getProductTemplateId());
			data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
		} else {
			String productVariantIdOptions = DropDownUtils.getProductVariantFilterListOptions(userIdList, "", null);
			data.put(FieldConstants.PRODUCT_VARIANT_ID_OPTIONS, productVariantIdOptions);
		}

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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_VENDOR_PRODUCT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
