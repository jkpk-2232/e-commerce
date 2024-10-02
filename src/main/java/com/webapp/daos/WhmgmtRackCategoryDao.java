package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.WhmgmtRackCategoryModel;

public interface WhmgmtRackCategoryDao {

	List<WhmgmtRackCategoryModel> getRackCategoryList();

	WhmgmtRackCategoryModel getRackCategoryDetailsByCategoryId(@Param("rackCategoryId") String rackCategoryId);

	void insertRackCategory(WhmgmtRackCategoryModel whmgmtRackCategoryModel);

	void updateRackCategory(WhmgmtRackCategoryModel whmgmtRackCategoryModel);

	boolean isCategoryNameExists(@Param("categoryName") String categoryName, @Param("categoryId") String categoryId);

	List<WhmgmtRackCategoryModel> getRackCategorySearch(@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);

	int getRackCategorySearchCount(@Param("searchKey") String searchKey);

}
