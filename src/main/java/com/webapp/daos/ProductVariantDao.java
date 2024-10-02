package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ProductVariantModel;

public interface ProductVariantDao {

	void insertProductVariant(ProductVariantModel productVariantModel);

	ProductVariantModel getProductVariantDetailsByProductVariantId(String productVariantId);

	void updateProductVariant(ProductVariantModel productVariantModel);

	int getProductVariantsCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("userIdList") List<String> userIdList);

	List<ProductVariantModel> getProductVariantSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length,
				@Param("userIdList") List<String> userIdList);

	int getProductVariantSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("userIdList") List<String> userIdList);

	void updateProductVariantStatus(ProductVariantModel productVariantModel);

	List<ProductVariantModel> getProductVariantList(@Param("userIdList") List<String> userIdList, @Param("productTemplateId") String productTemplateId);

}
