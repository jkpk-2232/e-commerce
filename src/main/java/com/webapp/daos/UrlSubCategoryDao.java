package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UrlSubCategoryModel;

public interface UrlSubCategoryDao {

	List<UrlSubCategoryModel> getSubPriorityMenus(@Param("userId") String userId);
}