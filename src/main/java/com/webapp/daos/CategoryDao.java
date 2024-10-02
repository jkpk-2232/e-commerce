package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CategoryModel;

public interface CategoryDao {

	void insertCategory(CategoryModel categoryModel);

	void updateCategory(CategoryModel categoryModel);

	int getCategoryCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("serviceId") String serviceId);

	List<CategoryModel> getCategorySearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("serviceId") String serviceId,
				@Param("displayType") String displayType);

	int getCategorySearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("serviceId") String serviceId);

	void updateCategoryStatus(CategoryModel categoryModel);

	boolean isCategoryNameExists(@Param("categoryName") String categoryName, @Param("categoryId") String categoryId);

	CategoryModel getCategoryDetailsByCategoryId(@Param("categoryId") String categoryId);
}