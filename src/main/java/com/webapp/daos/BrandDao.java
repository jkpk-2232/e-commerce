package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.BrandModel;

public interface BrandDao {

	boolean isBrandNameExists(@Param("brandName") String brandName, @Param("brandId") String brandId);

	void insertBrand(BrandModel brandModel);

	BrandModel getBrandDetailsByBrandId(String brandId);

	void updateBrand(BrandModel brandModel);

	int getBrandsCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("userIdList") List<String> userIdList);

	List<BrandModel> getBrandSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("userIdList") List<String> userIdList,
				@Param("brandStatus") String brandStatus, @Param("approvedBrands") String approvedBrands);

	int getBrandSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("userIdList") List<String> userIdList);

	void updateBrandStatus(BrandModel brandModel);

	List<BrandModel> getBrandListByCreatedBy(@Param("userIdList") List<String> userIdList);

	void updateBrandPublicStatus(BrandModel brandModel);

}
