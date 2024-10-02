package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AdminFaqModel;

public interface AdminFaqDao {

	int addAdminFaq(AdminFaqModel adminFaqModel);

	int getAdminFaqCount();

	List<AdminFaqModel> getAdminFaqListForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString);

	int getTotalAdminFaqCountBySearch(@Param("globalSearchString") String globalSearchString);

	int deleteAdminFaq(AdminFaqModel adminFaqModel);

	List<AdminFaqModel> getAdminFaqList();

	AdminFaqModel getAdminFaqModelById(@Param("adminFaqId") String adminFaqId);

	int editAdminFaq(AdminFaqModel adminFaqModel);
}
