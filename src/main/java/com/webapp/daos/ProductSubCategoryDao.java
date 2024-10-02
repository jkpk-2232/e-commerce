package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ProductSubCategoryModel;

public interface ProductSubCategoryDao {
	
	List<ProductSubCategoryModel> getProductSubCategorySearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);
	
	ProductSubCategoryModel getProductSubCategoryDetailsByProductSubCategoryId(@Param("productSubCategoryId") String productSubCategoryId);
	
	List<ProductSubCategoryModel> getProductSubCategoryList();
	
	void insertProductSubCategory(ProductSubCategoryModel productSubCategoryModel);
	
	void updateProductSubCategory(ProductSubCategoryModel productSubCategoryModel);
	
	int getProductSubCategorySearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey);
	
	void updateProductSubCategoryStatus(ProductSubCategoryModel productSubCategoryModel);
	
	boolean isProductSubCategoryNameExists(@Param("productSubCategoryName") String productSubCategoryName, @Param("productSubCategoryId") String productSubCategoryId);
	
	int getProductSubCategoryCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	List<ProductSubCategoryModel> getProductSubCategoryListByProductCategoryId(@Param("productCategoryId") String productCategoryId);

	ProductSubCategoryModel getProductSubCategoryDetailsByProductCategoryIdAndProductSubCategoryName(@Param("productCategoryId") String productCategoryId, @Param("productSubCategoryName") String productSubCategoryName);

}
