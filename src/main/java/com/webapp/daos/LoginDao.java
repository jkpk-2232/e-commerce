package com.webapp.daos;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface LoginDao {

	public String getUserIdByRememberMeKeyId(String keyId) throws SQLException;

	public long isLoggedIn(String userId) throws SQLException;

	public void closeLoginTrail(Map<String, Object> map) throws SQLException;

	public void addLoginTrail(Map<String, Object> map) throws SQLException;

	public String isValidLoginCredeitials(Map<String, String> map) throws SQLException;

	public int addRememberMeKey(@Param("rememberedLoginId") String apiSessionId, @Param("userId") String userId, @Param("createdAt") long createdAt) throws SQLException;

	public void deleteRememberMeKey(@Param("rememberMeKey") String rememberMeKey) throws SQLException;

	int deleteApiSessionKey(@Param("userId") String userId, @Param("updatedAt") long updatedAt);

	void insertApiSessionKey(@Param("apiSessionId") String apiSessionId, @Param("userId") String userId, @Param("createdAt") long createdAt);

	String isSessionExists(String apiSessionId);

	String getSessionKey(String userId);

	public int getTotalPeopleOpeneedAppCount();

}
