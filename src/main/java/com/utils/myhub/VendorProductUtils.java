package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.models.ProductCategoryModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorStoreModel;

public class VendorProductUtils {

	public static boolean insertProductIntoSystemViaAPI(VendorProductModel vendorProductModelInput, String loggedInUserId) {

		VendorProductModel vendorProductModel = new VendorProductModel();

		vendorProductModelInput.setPaid(true);
		vendorProductModelInput.setProductSku(OrderUtils.formProductSKU(vendorProductModelInput));

		if (vendorProductModelInput.isProductForAllVendorStores()) {

			long createdAt = DateUtils.nowAsGmtMillisec();

			vendorProductModelInput.setCreatedAt(createdAt);
			vendorProductModelInput.setUpdatedAt(createdAt);
			vendorProductModelInput.setCreatedBy(loggedInUserId);
			vendorProductModelInput.setUpdatedBy(loggedInUserId);
			vendorProductModelInput.setActive(true);
			vendorProductModelInput.setDeleted(false);

			String vendorId = vendorProductModelInput.getVendorId();
			List<String> vendorStoreIdList = new ArrayList<>();
			List<VendorProductModel> vendorProductList = new ArrayList<>();
			UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

			if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

				// by default send it all for sub vendor
				vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInUserId, false);

				if (vendorStoreIdList.isEmpty()) {
					return false;
				}

				vendorId = UserRoleUtils.getParentVendorId(loggedInUserId);

			} else {

				List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(null, loggedInUserId);
				for (VendorStoreModel vendorStoreModel : vendorStoreList) {
					vendorStoreIdList.add(vendorStoreModel.getVendorStoreId());
				}
			}

			for (String vendorStoreId : vendorStoreIdList) {

				vendorProductModel = new VendorProductModel();
				vendorProductModel.setVendorId(vendorId);
				vendorProductModel.setProductCategory(vendorProductModelInput.getProductCategory());
				vendorProductModel.setProductName(vendorProductModelInput.getProductName());
				vendorProductModel.setProductInformation(vendorProductModelInput.getProductInformation());
				vendorProductModel.setProductActualPrice(vendorProductModelInput.getProductActualPrice());
				vendorProductModel.setProductDiscountedPrice(vendorProductModelInput.getProductDiscountedPrice());
				vendorProductModel.setProductWeight(vendorProductModelInput.getProductWeight());
				vendorProductModel.setProductWeightUnit(vendorProductModelInput.getProductWeightUnit());
				vendorProductModel.setProductSpecification(vendorProductModelInput.getProductSpecification());
				vendorProductModel.setProductImage(vendorProductModelInput.getProductImage());
				vendorProductModel.setPaid(vendorProductModelInput.isPaid());
				vendorProductModel.setProductForAllVendorStores(vendorProductModelInput.isProductForAllVendorStores());
				vendorProductModel.setProductSku(vendorProductModelInput.getProductSku());
				vendorProductModel.setProductInventoryCount(vendorProductModelInput.getProductInventoryCount());
				vendorProductModel.setCreatedAt(vendorProductModelInput.getCreatedAt());
				vendorProductModel.setUpdatedAt(vendorProductModelInput.getCreatedAt());
				vendorProductModel.setCreatedBy(loggedInUserId);
				vendorProductModel.setUpdatedBy(loggedInUserId);
				vendorProductModel.setActive(vendorProductModelInput.isActive());
				vendorProductModel.setDeleted(vendorProductModelInput.isDeleted());

				vendorProductModel.setVendorProductId(UUIDGenerator.generateUUID());
				vendorProductModel.setVendorStoreId(vendorStoreId);

				vendorProductList.add(vendorProductModel);

				if (vendorProductList.size() >= ProjectConstants.BATCH_INSERT_SIZE) {

					VendorProductModel.insertVendorProductsBatchInsert(vendorProductList);
					vendorProductList.clear();
				}
			}

			if (!vendorProductList.isEmpty()) {
				VendorProductModel.insertVendorProductsBatchInsert(vendorProductList);
				vendorProductList.clear();
			}

		} else {
			vendorProductModelInput.insertVendorProducts(loggedInUserId);
		}

		return true;
	}

	//@formatter:off
	public static void insertProductIntoSystemViaAdminPanel(String vendorId, String productCategoryId, String productName, 
				String productInformation, String productActualPrice, String productDiscountedPrice, String productWeight, 
				String productWeightUnit, String productSpecification, String productInventoryCount, 
				String isProductForAllVendorStores, List<String> vendorStoreIdList, String hiddenProductImage, String loggedInUserId, 
				List<String> assignedRegionList, boolean isProductForAllVendorStoresBoolean, String productBarcode, 
				String  productQuantityType, String isNonVeg, String gst, String productSubCategoryId) {
	//@formatter:on

		VendorProductModel vendorProductModel = new VendorProductModel();
		long createdAt = DateUtils.nowAsGmtMillisec();

		List<VendorProductModel> vendorProductList = new ArrayList<>();

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);

		ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(productCategoryId);

		String productCategoryName = null;

		if (productCategoryModel != null) {
			productCategoryName = productCategoryModel.getProductCategoryName();
		}

		boolean addProduct = false;

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {

			if (isProductForAllVendorStoresBoolean || (!vendorStoreIdList.isEmpty() && vendorStoreIdList.contains(vendorStoreModel.getVendorStoreId()))) {
				addProduct = true;
			}

			if (addProduct) {

				vendorProductModel = new VendorProductModel();
				vendorProductModel.setVendorId(vendorId);
				vendorProductModel.setProductCategory(productCategoryName);
				vendorProductModel.setProductName(productName);
				vendorProductModel.setProductInformation(productInformation);
				vendorProductModel.setProductActualPrice(Double.parseDouble(productActualPrice));
				vendorProductModel.setProductDiscountedPrice(Double.parseDouble(productDiscountedPrice));
				vendorProductModel.setProductWeight(Double.parseDouble(productWeight));
				vendorProductModel.setProductWeightUnit(Integer.parseInt(productWeightUnit));
				vendorProductModel.setProductSpecification(productSpecification);
				vendorProductModel.setProductImage(hiddenProductImage);
				vendorProductModel.setPaid(true);
				vendorProductModel.setProductForAllVendorStores(Boolean.parseBoolean(isProductForAllVendorStores));
				vendorProductModel.setProductSku(OrderUtils.formProductSKU(vendorProductModel));
				vendorProductModel.setProductInventoryCount(Long.parseLong(productInventoryCount));
				vendorProductModel.setCreatedAt(createdAt);
				vendorProductModel.setUpdatedAt(createdAt);
				vendorProductModel.setCreatedBy(loggedInUserId);
				vendorProductModel.setUpdatedBy(loggedInUserId);
				vendorProductModel.setActive(true);
				vendorProductModel.setDeleted(false);
				vendorProductModel.setProductCategoryId(productCategoryId);
				vendorProductModel.setProductBarcode(productBarcode);
				vendorProductModel.setPrdQtyType(productQuantityType);
				vendorProductModel.setIsNonVeg(Boolean.parseBoolean(isNonVeg));
				if (!gst.isEmpty()) {
					vendorProductModel.setGst(Double.parseDouble(gst));
				}
				vendorProductModel.setProductSubCategoryId(productSubCategoryId);

				vendorProductModel.setVendorProductId(UUIDGenerator.generateUUID());
				vendorProductModel.setVendorStoreId(vendorStoreModel.getVendorStoreId());

				vendorProductList.add(vendorProductModel);

				if (vendorProductList.size() >= ProjectConstants.BATCH_INSERT_SIZE) {

					VendorProductModel.insertVendorProductsBatchInsert(vendorProductList);
					vendorProductList.clear();
				}
			}

			addProduct = false;
		}

		if (!vendorProductList.isEmpty()) {
			VendorProductModel.insertVendorProductsBatchInsert(vendorProductList);
			vendorProductList.clear();
		}
	}

	public static void updateProductIntoSystemViaAPI(VendorProductModel vendorProductModel, String loggedInUserId) {

		vendorProductModel.setPaid(true);
		vendorProductModel.setProductForAllVendorStores(false);
		vendorProductModel.setProductSku(OrderUtils.formProductSKU(vendorProductModel));
		vendorProductModel.updateVendorProducts(loggedInUserId);
	}

	//@formatter:off
	public static void updateProductIntoSystemViaAdminPanel(String vendorProductId, String vendorId, String productCategoryId, 
				String productName, String productInformation, String productActualPrice, String productDiscountedPrice, 
				String productWeight, String productWeightUnit, String productSpecification, String productInventoryCount, 
				String hiddenProductImage, String loggedInUserId, String productBarcode, String productQuantityType,
				String isNonVeg, String gst, String productSubCategoryId) {
	//@formatter:on

		ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(productCategoryId);

		String productCategoryName = null;

		if (productCategoryModel != null) {
			productCategoryName = productCategoryModel.getProductCategoryName();
		}

		VendorProductModel vendorProductModel = VendorProductModel.getProductsDetailsByProductId(vendorProductId);
		vendorProductModel.setVendorId(vendorId);
		vendorProductModel.setProductCategory(productCategoryName);
		vendorProductModel.setProductName(productName);
		vendorProductModel.setProductInformation(productInformation);
		vendorProductModel.setProductActualPrice(Double.parseDouble(productActualPrice));
		vendorProductModel.setProductDiscountedPrice(Double.parseDouble(productDiscountedPrice));
		vendorProductModel.setProductWeight(Double.parseDouble(productWeight));
		vendorProductModel.setProductWeightUnit(Integer.parseInt(productWeightUnit));
		vendorProductModel.setProductSpecification(productSpecification);
		vendorProductModel.setProductImage(hiddenProductImage);
		vendorProductModel.setProductSku(OrderUtils.formProductSKU(vendorProductModel));
		vendorProductModel.setProductInventoryCount(Long.parseLong(productInventoryCount));
		vendorProductModel.setProductCategoryId(productCategoryId);
		vendorProductModel.setProductBarcode(productBarcode);
		vendorProductModel.setPrdQtyType(productQuantityType);
		vendorProductModel.setIsNonVeg(Boolean.parseBoolean(isNonVeg));
		
		if (Double.parseDouble(productActualPrice) > Double.parseDouble(productDiscountedPrice)) {
			vendorProductModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		}
		
		if (!gst.isEmpty()) {
			vendorProductModel.setGst(Double.parseDouble(gst));
		}
		vendorProductModel.setProductSubCategoryId(productSubCategoryId);

		vendorProductModel.updateVendorProducts(loggedInUserId);
		
		if (Double.parseDouble(productActualPrice) > Double.parseDouble(productDiscountedPrice) && vendorProductModel.getProductImage() != null ) {
			
			FeedUtils.insertFeed(vendorId, productName, productInformation, null, loggedInUserId, ProjectConstants.Image, "false", vendorProductModel.getVendorStoreId(), vendorProductId);
			
		}
	}
	
	//@formatter:off
	public static void insertProductThroughProductTemplateIntoSystemViaAdminPanel(String vendorId, String productCategoryId, String productName, 
					String productInformation, String productActualPrice, String productDiscountedPrice, String productWeight, 
					String productWeightUnit, String productSpecification, String productInventoryCount, 
					String isProductForAllVendorStores, List<String> vendorStoreIdList, String hiddenProductImage, String loggedInUserId, 
					List<String> assignedRegionList, boolean isProductForAllVendorStoresBoolean, String productBarcode, 
					String  productQuantityType, String isNonVeg, String gst, String productSubCategoryId, String productTemplateId, 
					String productVariantId) {
	//@formatter:on

		VendorProductModel vendorProductModel = new VendorProductModel();
		long createdAt = DateUtils.nowAsGmtMillisec();

		List<VendorProductModel> vendorProductList = new ArrayList<>();

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);

		ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(productCategoryId);

		String productCategoryName = null;

		if (productCategoryModel != null) {
			productCategoryName = productCategoryModel.getProductCategoryName();
		}

		boolean addProduct = false;

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {

			if (isProductForAllVendorStoresBoolean || (!vendorStoreIdList.isEmpty() && vendorStoreIdList.contains(vendorStoreModel.getVendorStoreId()))) {
				addProduct = true;
			}
			
			boolean productExists =  vendorProductModel.isVendorStoreIdAndProductVariantIdExists(vendorStoreModel.getVendorStoreId(), productVariantId, null);
			
			if (!productExists) {
				
				if (addProduct) {

					vendorProductModel = new VendorProductModel();
					vendorProductModel.setVendorId(vendorId);
					vendorProductModel.setProductCategory(productCategoryName);
					vendorProductModel.setProductName(productName);
					vendorProductModel.setProductInformation(productInformation);
					vendorProductModel.setProductActualPrice(Double.parseDouble(productActualPrice));
					vendorProductModel.setProductDiscountedPrice(Double.parseDouble(productDiscountedPrice));
					vendorProductModel.setProductWeight(Double.parseDouble(productWeight));
					vendorProductModel.setProductWeightUnit(Integer.parseInt(productWeightUnit));
					vendorProductModel.setProductSpecification(productSpecification);
					vendorProductModel.setProductImage(hiddenProductImage);
					vendorProductModel.setPaid(true);
					vendorProductModel.setProductForAllVendorStores(Boolean.parseBoolean(isProductForAllVendorStores));
					vendorProductModel.setProductSku(vendorStoreModel.getVendorStoreId().concat("-").concat(MyHubUtils.removeWhiteSpaces(productVariantId)));
					vendorProductModel.setProductInventoryCount(Long.parseLong(productInventoryCount));
					vendorProductModel.setCreatedAt(createdAt);
					vendorProductModel.setUpdatedAt(createdAt);
					vendorProductModel.setCreatedBy(loggedInUserId);
					vendorProductModel.setUpdatedBy(loggedInUserId);
					vendorProductModel.setActive(true);
					vendorProductModel.setDeleted(false);
					vendorProductModel.setProductCategoryId(productCategoryId);
					vendorProductModel.setProductBarcode(productBarcode);
					vendorProductModel.setPrdQtyType(productQuantityType);
					vendorProductModel.setIsNonVeg(Boolean.parseBoolean(isNonVeg));
					vendorProductModel.setProductTemplateId(productTemplateId);
					vendorProductModel.setProductVariantId(productVariantId);
					vendorProductModel.setStorePrice(0);
					if (!gst.isEmpty()) {
						vendorProductModel.setGst(Double.parseDouble(gst));
					}
					vendorProductModel.setProductSubCategoryId(productSubCategoryId);

					vendorProductModel.setVendorProductId(UUIDGenerator.generateUUID());
					vendorProductModel.setVendorStoreId(vendorStoreModel.getVendorStoreId());

					vendorProductList.add(vendorProductModel);

					if (vendorProductList.size() >= ProjectConstants.BATCH_INSERT_SIZE) {

						VendorProductModel.insertVendorProductsBatchInsert(vendorProductList);
						vendorProductList.clear();
					}
				}
			}

			addProduct = false;
		}

		if (!vendorProductList.isEmpty()) {
			VendorProductModel.insertVendorProductsBatchInsert(vendorProductList);
			vendorProductList.clear();
		}

	}
	
	//@formatter:off
	public static void updateProductThroughProductTemplateIntoSystemViaAdminPanel(String vendorProductId, String vendorId, String productCategoryId, 
					String productName, String productInformation, String productActualPrice, String productDiscountedPrice, 
					String productWeight, String productWeightUnit, String productSpecification, String productInventoryCount, 
					String hiddenProductImage, String loggedInUserId, String productBarcode, String productQuantityType,
					String isNonVeg, String gst, String productSubCategoryId, String productTemplateId, String productVariantId) {
	//@formatter:on
		
		ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(productCategoryId);

		String productCategoryName = null;

		if (productCategoryModel != null) {
			productCategoryName = productCategoryModel.getProductCategoryName();
		}

		VendorProductModel vendorProductModel = VendorProductModel.getProductsDetailsByProductId(vendorProductId);
		vendorProductModel.setVendorId(vendorId);
		vendorProductModel.setProductCategory(productCategoryName);
		vendorProductModel.setProductName(productName);
		vendorProductModel.setProductInformation(productInformation);
		vendorProductModel.setProductActualPrice(Double.parseDouble(productActualPrice));
		vendorProductModel.setProductDiscountedPrice(Double.parseDouble(productDiscountedPrice));
		vendorProductModel.setProductWeight(Double.parseDouble(productWeight));
		vendorProductModel.setProductWeightUnit(Integer.parseInt(productWeightUnit));
		vendorProductModel.setProductSpecification(productSpecification);
		vendorProductModel.setProductImage(hiddenProductImage);
		//vendorProductModel.setProductSku(OrderUtils.formProductSKU(vendorProductModel));
		vendorProductModel.setProductSku(vendorProductModel.getVendorStoreId().concat("-").concat(MyHubUtils.removeWhiteSpaces(productVariantId)));
		vendorProductModel.setProductInventoryCount(Long.parseLong(productInventoryCount));
		vendorProductModel.setProductCategoryId(productCategoryId);
		vendorProductModel.setProductBarcode(productBarcode);
		vendorProductModel.setPrdQtyType(productQuantityType);
		vendorProductModel.setIsNonVeg(Boolean.parseBoolean(isNonVeg));
		vendorProductModel.setProductVariantId(productVariantId);
		vendorProductModel.setProductVariantId(productVariantId);
		
		if (Double.parseDouble(productActualPrice) > Double.parseDouble(productDiscountedPrice)) {
			vendorProductModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		}
		
		if (!gst.isEmpty()) {
			vendorProductModel.setGst(Double.parseDouble(gst));
		}
		vendorProductModel.setProductSubCategoryId(productSubCategoryId);

		vendorProductModel.updateVendorProducts(loggedInUserId);
		
		
		if (Double.parseDouble(productActualPrice) > Double.parseDouble(productDiscountedPrice) && vendorProductModel.getProductImage() != null ) {
			
			FeedUtils.insertFeed(vendorId, productName, productInformation, null, loggedInUserId, ProjectConstants.Image, "false", vendorProductModel.getVendorStoreId(), vendorProductId);
			
		}

	}
}
