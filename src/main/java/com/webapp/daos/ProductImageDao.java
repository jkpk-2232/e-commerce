package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ProductImageModel;

public interface ProductImageDao {

	void insertProductImage(ProductImageModel productImageModel);

	ProductImageModel getProductImageDetailsByProductImageId(String productImageId);

	void updateProductImage(ProductImageModel productImageModel);

	int getProductImageCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("userIdList") List<String> userIdList);

	List<ProductImageModel> getProductImageSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("userIdList") List<String> userIdList);

	int getProductImageSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("userIdList") List<String> userIdList);

	void updateProductImageStatus(ProductImageModel productImageModel);

	List<ProductImageModel> getProductImageListByProductVariant(@Param("productVariantId") String productVariantId);

}
