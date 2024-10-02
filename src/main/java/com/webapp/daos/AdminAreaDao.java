package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AdminAreaModel;

public interface AdminAreaDao {

	int addAdminArea(AdminAreaModel adminAreaModel);

	int getAdminAreaCount();

	List<AdminAreaModel> getAdminAreaListForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString);

	int getTotalAdminAreaCountBySearch(@Param("globalSearchString") String globalSearchString);

	int deleteAdminArea(AdminAreaModel adminAreaModel);

	boolean isAdminAreaExists(@Param("areaPlaceId") String areaPlaceId);

	List<AdminAreaModel> getAdminAreaList();
}
