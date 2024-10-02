package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ProductTemplateModel;

public interface ProductTemplateDao {

	boolean isProductNameExists(@Param("productName") String productName, @Param("productTemplateId") String productTemplateId);

	void insertProductTemplate(ProductTemplateModel productTemplateModel);

	ProductTemplateModel getProductTemplateDetailsByProductTemplateId(String productTemplateId);

	void updateProductTemplate(ProductTemplateModel productTemplateModel);

	int getProductTemplatesCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("userIdList") List<String> userIdList);

	List<ProductTemplateModel> getProductTemplateSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length,
				@Param("userIdList") List<String> userIdList);

	int getProductTemplateSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("userIdList") List<String> userIdList);

	void updateProductTemplateStatus(ProductTemplateModel productTemplateModel);

	List<ProductTemplateModel> getProductTemplateList(@Param("userIdList") List<String> userIdList, @Param("brandId") String brandId, @Param("productCategoryId") String productCategoryId);

	boolean isProductNameAndUomIdExists(@Param("productName") String productName, @Param("uomId") int uomId, @Param("productTemplateId") String productTemplateId);

	boolean isProductNameAndUomIdListExists(@Param("productName") String productName, @Param("uomList") List<String> uomList);

}
