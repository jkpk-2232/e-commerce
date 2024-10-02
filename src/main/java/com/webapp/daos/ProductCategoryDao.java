package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ProductCategoryModel;

public interface ProductCategoryDao {

	List<ProductCategoryModel> getProductCategoryList();
	
	List<ProductCategoryModel> getProductCategorySearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);

	ProductCategoryModel getProductCategoryDetailsByProductCategoryId(@Param("productCategoryId") String productCategoryId);

	void insertProductCategory(ProductCategoryModel productCategoryModel);

	void updateProductCategory(ProductCategoryModel productCategoryModel);

	int getProductCategorySearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey);

	void updateProductCategoryStatus(ProductCategoryModel productCategoryModel);

	int getProductCategoryCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	ProductCategoryModel getProductCategoryDetailsByProductCategoryName(@Param("productCategory") String productCategory);

	boolean isProductCategoryNameExists(@Param("productCategoryName") String productCategoryName, @Param("productCategoryId") String productCategoryId);

	List<ProductCategoryModel> getProductCategoryListByProductCategoryId(@Param("productCategoryIdList") List<String> productCategoryIdList);

}
