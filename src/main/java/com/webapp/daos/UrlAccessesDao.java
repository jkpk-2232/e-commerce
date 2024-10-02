package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UrlAccessesModel;

public interface UrlAccessesDao {

	int addUserUrlAccesses(UrlAccessesModel urlAccessesModel);

	List<UrlAccessesModel> getUserUrlAccesses(String userId);

	void deleteUrlAccesses(String userId);
	
	public boolean hasUserurlAccess(Map<String, String> map) throws SQLException;

	int addUserUrlAccessesBatch(@Param("urlAccessesModelList")  List<UrlAccessesModel> urlAccessesModelList);
	
}