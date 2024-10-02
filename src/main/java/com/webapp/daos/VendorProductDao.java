package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorProductModel;

public interface VendorProductDao {

	void insertVendorProducts(VendorProductModel vendorProductModel);

	void insertVendorProductsBatchInsert(@Param("vendorProductList") List<VendorProductModel> vendorProductList);

	void updateVendorProducts(VendorProductModel vendorProductModel);

	int getVendorProductsCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId, @Param("vendorStoreId") String vendorStoreId);

	List<VendorProductModel> getVendorProductsSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("displayType") String displayType,
				@Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId, @Param("orderColumn") String orderColumn, @Param("vendorStoreId") String vendorStoreId);

	int getVendorProductsSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId,
				@Param("vendorStoreId") String vendorStoreId);

	void updateVendorProductsStatus(VendorProductModel vendorProductModel);

	VendorProductModel getVendorProductsDetailsByProductId(@Param("vendorProductId") String vendorProductId);

	List<VendorProductModel> getProductDetailsByVendorProductsIdsAndVendorStoreIdList(@Param("vendorProductIds") List<String> vendorProductIds, @Param("vendorStoreId") String vendorStoreId);

	List<VendorProductModel> getProductListApi(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("start") int start, @Param("length") int length, @Param("productStatus") String productStatus, @Param("searchKey") String searchKey,
				@Param("filterOrder") String filterOrder);

	List<VendorProductModel> getVendorProductListByProductSkus(@Param("productSkus") List<String> productSkus, @Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	void updateProductInventoryCount(@Param("vpmList") List<VendorProductModel> vpmList);
	
	VendorProductModel getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(@Param("vendorId") String vendorId,
				@Param("productSku") String productSku,@Param("productName") String productName,@Param("productCategory") String productCategory,
				@Param("vendorStoreId") String vendorStoreId,@Param("productBarcode") String productBarcode);
	
	List<VendorProductModel> getProductListByVendorId(String vendorId);
	
	List<VendorProductModel> getProductListWithOutPagination(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("productStatus") String productStatus, @Param("productCategoryId") String productCategoryId,
				@Param("searchKey") String searchKey, @Param("productSubCategoryId") String productSubCategoryId);
	
	List<VendorProductModel> getVendorProductsDetailsByProductIdsList(@Param("vendorProductIds") List<String> vendorProdIdList);

	List<VendorProductModel> getProductDetailsByProductSkuAndVendorStoreIdList(@Param("productSkuList") List<String> productSkuList, @Param("vendorStoreId") String vendorStoreId);

	void updateVendorProductsFromCSV(VendorProductModel vendorProductModel);

	VendorProductModel getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(@Param("vendorId") String vendorId, @Param("productSku") String productSku, @Param("productName") String productName,
				@Param("productCategory") String productCategory, @Param("vendorStoreId") String vendorStoreId);

	boolean isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("productBarcode") String productBarcode);

	List<VendorProductModel> getNewlyAddedProductsList(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId);
	
	List<VendorProductModel> getProductListForStore(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("productStatus") String productStatus);
	
	List<VendorProductModel> getOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("productCategory") String productCategory);
	
	List<VendorProductModel> getVendorProductListForKPMARTByProductSkus(@Param("productSkus") List<String> productSkus, @Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId);
	
	List<VendorProductModel> getNewProductListWithOutPagination(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("productStatus") String productStatus, @Param("productCategoryId") String productCategoryId,
				@Param("searchKey") String searchKey, @Param("productSubCategoryId") String productSubCategoryId);
	
	VendorProductModel getNewVendorProductsDetailsByProductId(@Param("vendorProductId") String vendorProductId);
	
	List<VendorProductModel> getVendorProductListForKPMARTByProductIds(@Param("productIds") List<String> productSkus, @Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId);
	
	List<VendorProductModel> getNewlyAddedProductIdsList(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId);
	
	List<VendorProductModel> getNewOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("productCategory") String productCategory);

	void updateProductNameAndWeightAndQtyTypeAndIsNonVeg(VendorProductModel vendorProductModel);

	List<VendorProductModel> getProductListByProductVariantIdAndProductTemplateId(@Param("productVariantId") String productVariantId, @Param("productTemplateId") String productTemplateId);

	void updateInformationAndSpecificationAndWeightUnitAndCategoryAndSubCategory(VendorProductModel vendorProductModel);

	void updateProductCategory(VendorProductModel vendorProductModel);

	boolean isVendorStoreIdAndProductVariantIdExists(@Param("vendorStoreId") String vendorStoreId, @Param("productVariantId") String productVariantId, @Param("vendorProductId") String vendorProductId);

	List<VendorProductModel> getBrandProductsListForDashboard(@Param("brandId") String brandId);
	

	List<String> getDistinctVendorStoreIdsBasedonBrandId(@Param("brandId") String brandId);
	


	List<VendorProductModel> getProductListForApiSearch(@Param("start") int start, @Param("length") int length, @Param("productStatus") String productStatus, @Param("searchKey") String searchKey,
				@Param("filterOrder") String filterOrder);

}