package com.webapp.daos;

import java.sql.SQLException;
import java.util.Map;

import com.webapp.models.UrlAccessModel;

public interface UrlAccessDao {

	public boolean hasAccess(Map<String, String> map) throws SQLException;

	/*
	 * public boolean toggleGroupAccess(long groupId, long roleId, boolean
	 * toggle, long actorId) throws SQLException;
	 * 
	 * public boolean toggleUrlAccess(long urlId, long roleId, long actorId)
	 * throws SQLException;
	 */
	public boolean removeAccess(Map<String, String> map) throws SQLException;

	public boolean addAccess(UrlAccessModel map) throws SQLException;

	public boolean userHasAccessToUrl(String userId, String url) throws SQLException;

}
