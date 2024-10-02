package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import com.webapp.models.UrlModel;

public interface UrlDao {

	public List<UrlModel> getPriorityMenus(String userId) throws SQLException;

	public String getFirstPriorityURL(String userId) throws SQLException;

}
