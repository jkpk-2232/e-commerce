package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.utils.dbsession.DbSession;

public interface SessionDao {

	public int newSession(String sessionId, long createdAt, long accessedAt) throws SQLException;

	public boolean sessionExistsForKey(String key) throws SQLException;

	public void destroySession(String sessionKey) throws SQLException;

	public void setSessionLastAccessedTimeStamp(Map<String, Object> map) throws SQLException;

	public String attributeExistsForSession(Map<String, String> map) throws SQLException;

	public void insertAttributeForSession(Map<String, String> map) throws SQLException;

	public void updateAttribute(@Param("attributeId") long attributeId, @Param("attributeValue") String attributeValue) throws SQLException;

	public String attributeValueForSession(Map<String, String> map) throws SQLException;

	public List<DbSession> allattributeValuesForSession(Map<String, String> map);

	public boolean removeAttributeForSession(Map<String, String> map) throws SQLException;

	public void destroySessionAttribute(String sessionKey) throws SQLException;
}
