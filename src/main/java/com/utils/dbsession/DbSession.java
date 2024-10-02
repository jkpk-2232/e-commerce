package com.utils.dbsession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.SessionUtils;
import com.webapp.daos.SessionDao;
import com.webapp.models.UserModel;

public class DbSession {

	private static Logger logger = Logger.getLogger(DbSession.class);

	public static final String SESSION_COOKIE_NAME = "framworksession";

	public static final String REMEMBER_ME_SESSION_COOKIE_NAME = "framworkRsession";

	public static final int SESSION_COOKIE_MAX_AGE = -1;

	private String sessionKey;

	private HttpServletRequest servletRequest;

	private HttpServletResponse servletResponse;

	private boolean valid;

	private DbSession() {

	}

	private DbSession(String sessionKey, HttpServletRequest request, HttpServletResponse response) {
		this.sessionKey = sessionKey;
		this.servletRequest = request;
		this.servletResponse = response;
		this.valid = true;
	}

	private static String newSessionKey() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		String sessionKey = UUIDGenerator.generateUUID();
		long gmtTimeStamp = DateUtils.nowAsGmtMillisec();

		try {
			sessionDao.newSession(sessionKey, gmtTimeStamp, gmtTimeStamp);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while creating newSessionKey :: ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return sessionKey;
	}

	private static String sessionKeyForCookie(Cookie cookie) {
		return cookie.getValue();
	}

	private static boolean sessionExistsForKey(String sessionKey) {

		boolean sessionExists = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		try {
			sessionExists = sessionDao.sessionExistsForKey(sessionKey);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while sessionExistsForKey :: ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return sessionExists;
	}

	public static DbSession getSession(HttpServletRequest request, HttpServletResponse response, boolean create) {

		Cookie sessionCookie = CookieUtils.getCookie(SESSION_COOKIE_NAME, request);

		if (sessionCookie != null) {

			if (CookieUtils.DELETED_COOKIE_VALUE.equals(sessionCookie.getValue())) {
				sessionCookie = null;
			}

		} else {
			sessionCookie = null;
		}

		DbSession session = null;

		if (sessionCookie != null) {

			String sessionKey = sessionKeyForCookie(sessionCookie);

			boolean sessionExists = sessionExistsForKey(sessionKey);

			if (sessionExists) {

				session = new DbSession(sessionKey, request, response);

				// Activate this if in future we want to have session auto logged out after some
				// period of inactivity by the user.
				// session.setLastAccessedAt();
			}

		} else {

			Cookie rememberedCookie = CookieUtils.getCookie(REMEMBER_ME_SESSION_COOKIE_NAME, request);

			if (rememberedCookie != null) {

				String userId = LoginUtils.getUserIdByRememberMeId(rememberedCookie.getValue());

				UserModel userModel = UserModel.getUserAccountDetailsById(userId);

				if (userId != null && userId.length() > 0 && userModel != null) {

					String newSessionKey = newSessionKey();

					Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "" + newSessionKey);
					cookie.setMaxAge(SESSION_COOKIE_MAX_AGE);

					String ctxPath = request.getContextPath();

					if (ctxPath.length() == 0) {
						ctxPath = "/";
					}

					cookie.setPath(ctxPath);

					CookieUtils.setCookie(cookie, request, response);

					session = new DbSession(newSessionKey, request, response);

					SessionUtils.setSessionAttibutesForUserLogin(session, userId, userModel);

					return session;
				}
			}

		}

		if (session == null && create) {

			String newSessionKey = newSessionKey();

			Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "" + newSessionKey);
			cookie.setMaxAge(SESSION_COOKIE_MAX_AGE);

			String ctxPath = request.getContextPath();

			if (ctxPath.length() == 0) {
				ctxPath = "/";
			}

			cookie.setPath(ctxPath);

			CookieUtils.setCookie(cookie, request, response);

			session = new DbSession(newSessionKey, request, response);
		}

		return session;
	}

	public boolean isValid() {
		return valid;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	private void checkValidity() {

		if (!valid) {
			throw new IllegalStateException("Session is invalid");
		}
	}

//	private void setLastAccessedAt() {
//
//		SqlSession session = ConnectionBuilderAction.getSqlSession();
//		SessionDao sessionDao = session.getMapper(SessionDao.class);
//
//		long gmtTimeStamp = DateUtils.nowAsGmtMillisec();
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		map.put("sessionKey", sessionKey);
//		map.put("accessedAt", gmtTimeStamp);
//
//		try {
//			sessionDao.setSessionLastAccessedTimeStamp(map);
//			session.commit();
//		} catch (Exception e) {
//			session.rollback();
//			logger.error("Exception occured while setLastAccessedAt", e);
//			throw new PersistenceException(e);
//		} finally {
//			session.close();
//		}
//	}

	private String attributeExists(String attribute) {

		String sessionAtributeId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionKey);
		map.put("attribute", attribute);

		try {
			sessionAtributeId = sessionDao.attributeExistsForSession(map);
			session.commit();
		} catch (Exception e) {
			sessionAtributeId = null;
			session.rollback();
			logger.error("Exception occured while attributeExists ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return sessionAtributeId;
	}

	private void insertAttribute(String attribute, String attributeValue) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionKey);
		map.put("attribute", attribute);
		map.put("attributeValue", attributeValue);

		try {
			sessionDao.insertAttributeForSession(map);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while insertAttribute ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
	}

	private void updateAttribute(String attributeId, String attributeValue) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("attributeId", attributeId);
		map.put("attributeValue", attributeValue);

		try {
			sessionDao.updateAttribute(Long.parseLong(attributeId), attributeValue);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while updateAttribute ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
	}

	public void setAttribute(String attribute, String attributeValue) {

		checkValidity();

		String attributeId = attributeExists(attribute);

		if (attributeId == null) {
			insertAttribute(attribute, attributeValue);
		} else {
			updateAttribute(attributeId, attributeValue);
		}
	}

	public String getAttribute(String attribute) {

		checkValidity();

		String attributeValue = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionKey);
		map.put("attribute", attribute);

		try {
			attributeValue = sessionDao.attributeValueForSession(map);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while getAttribute ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return attributeValue;
	}

	public List<DbSession> getAllAttributes() {

		checkValidity();

		List<DbSession> attributeValues = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionKey);

		try {
			attributeValues = sessionDao.allattributeValuesForSession(map);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while getAllAttributes ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return attributeValues;
	}

	public boolean removeAttribute(String attribute) {

		checkValidity();

		boolean isRemoved = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionKey);
		map.put("attribute", attribute);

		try {
			sessionDao.removeAttributeForSession(map);
			isRemoved = true;
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while removeAttribute ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return isRemoved;
	}

	private void deleteSessionCookie() {

		Cookie sessionCookie = CookieUtils.getCookie(SESSION_COOKIE_NAME, servletRequest);

		if (sessionCookie != null) {
			CookieUtils.deleteCookie(sessionCookie, servletRequest, servletResponse);
		}
	}

	public void invalidate() {

		checkValidity();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SessionDao sessionDao = session.getMapper(SessionDao.class);

		try {
			sessionDao.destroySessionAttribute(sessionKey);
			sessionDao.destroySession(sessionKey);
			session.commit();
			valid = false;
			deleteSessionCookie();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured while invalidate ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
	}

	public String getUserId() {
		return getAttribute(LoginUtils.USER_ID).toString();
	}

	private String sessionId;
	private long createdAt;
	private long accessedAt;
	private int sessionAttributeId;
	private String attribute;
	private String attributeValue;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getAccessedAt() {
		return accessedAt;
	}

	public void setAccessedAt(long accessedAt) {
		this.accessedAt = accessedAt;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public int getSessionAttributeId() {
		return sessionAttributeId;
	}

	public void setSessionAttributeId(int sessionAttributeId) {
		this.sessionAttributeId = sessionAttributeId;
	}
}