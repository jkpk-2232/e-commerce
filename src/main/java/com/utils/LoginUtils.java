package com.utils;

import java.io.IOException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.dbsession.DbSession;
import com.utils.myhub.SessionUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.daos.LoginDao;
import com.webapp.daos.UserDao;
import com.webapp.models.BusinessOperatorModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

public class LoginUtils {

	private static Logger logger = Logger.getLogger(LoginUtils.class);

	public static final String FORWORD_URL = "fUrl";
	public static final String USER_ID = "user_id";
	public static final String ROLE_ID = "role_id";
	public static final String ROLE = "role";
	public static final String PHOTO_URL = "photo_url";
	public static final String USER_FULL_NAME = "full_name";

	public static final int minUsernameLength = 2;
	public static final int maxUsernameLength = 110;

	public static final int minPasswordLength = 6;
	public static final int maxPasswordLength = 20;

	public static boolean checkLogIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return (LoginUtils.checkValidSession(request, response) && LoginUtils.isLoggedIn(request, response));
	}

	public static String isValidLoginCredentials(String username, String password) {

		String actorId = "";

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("lowerusername", username.toLowerCase());
			map.put("password", password);
			actorId = loginDao.isValidLoginCredeitials(map);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during isValidLoginCredentials", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return actorId;
	}

	public static String processPostLogin(String userId, DbSession session) {

		UserModel userProfileModel = UserModel.getUserAccountDetailsById(userId);

		if (userProfileModel != null) {
			SessionUtils.setSessionAttibutesForUserLogin(session, userId, userProfileModel);
		}

		return BusinessAction.getPriorityUrlByUserd(userId);
	}

	public static DbSession createSession(UserModel userModel, HttpServletRequest request, HttpServletResponse response) {

		DbSession session = DbSession.getSession(request, response, true);

		addLoginTrail(userModel.getUserId(), session.getSessionKey());

		session.setAttribute(USER_ID, userModel.getUserId());
		session.setAttribute(PHOTO_URL, userModel.getPhotoUrl() != null ? userModel.getPhotoUrl() : ProjectConstants.DEFAULT_IMAGE);

		if (userModel.getRoleId().equalsIgnoreCase(UserRoles.BUSINESS_OWNER_ROLE_ID) || userModel.getRoleId().equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {

			UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(userModel.getUserId());

			if (userProfileModel != null) {
				session.setAttribute("companyName", userProfileModel.getCompanyName() + "");
			} else {
				session.setAttribute("companyName", "Not available");
			}

		} else if (userModel.getRoleId().equalsIgnoreCase(UserRoles.BUSINESS_OPERATOR_ROLE_ID)) {

			String businessOperatorOwnerId = BusinessOperatorModel.getBusinessOwnerId(userModel.getUserId());

			if (businessOperatorOwnerId != null) {

				UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(businessOperatorOwnerId);

				if (userProfileModel != null) {
					session.setAttribute("companyName", userProfileModel.getCompanyName());
				} else {
					session.setAttribute("companyName", "Not available");
				}
			}
		}

		return session;
	}

	private static void addLoginTrail(String userId, String sessionId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			String loginTrailId = UUIDGenerator.generateUUID();

			map.put("loginTrailId", loginTrailId);
			map.put("userId", userId);
			map.put("loggedInAt", DateUtils.nowAsGmtMillisec());
			map.put("loggedOutAt", Types.TIMESTAMP + "");
			map.put("sessionId", sessionId);

			LoginDao loginDao = session.getMapper(LoginDao.class);

			loginDao.addLoginTrail(map);

			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during addLoginTrail", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
	}

	private static void closeLoginTrail(String actorId, String sessionId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loggedOutAt", DateUtils.nowAsGmtMillisec());
			map.put("userId", actorId);
			map.put("sessionId", sessionId);

			LoginDao loginDao = session.getMapper(LoginDao.class);
			loginDao.closeLoginTrail(map);

			session.commit();

		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during closeLoginTrail", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}
	}

	public static boolean checkValidSession(HttpServletRequest request, HttpServletResponse response) {

		DbSession session = DbSession.getSession(request, response, false);

		if (session == null) {
			return false;
		}

		return session.getAttribute(LoginUtils.USER_ID) != null;
	}

	public static void destoryCurrentSession(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> loggedInActor = getLoggedInActor(request, response);

		if (loggedInActor == null) {
			return;
		}

		String userId = loggedInActor.get(USER_ID).toString();

		DbSession session = DbSession.getSession(request, response, false);

		closeLoginTrail(userId, session.getSessionKey());

		session.invalidate();
	}

	public static boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) {

		boolean loggedIn = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		Map<String, Object> loggedInActor = getLoggedInActor(request, response);

		if (loggedInActor == null) {
			return false;
		}

		String userId = loggedInActor.get(USER_ID).toString();

		try {
			LoginDao loginDao = session.getMapper(LoginDao.class);

			long loginTrailIdCount = loginDao.isLoggedIn(userId);

			if (loginTrailIdCount > 0) {
				loggedIn = true;
			}

			session.commit();

		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during isLoggedIn", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return loggedIn;
	}

	public static Map<String, Object> getLoggedInActor(HttpServletRequest request, HttpServletResponse response) {

		DbSession session = DbSession.getSession(request, response, false);

		if (session == null) {
			return null;
		}

		String actorIdStr = session.getAttribute(USER_ID);

		if (actorIdStr == null) {
			return null;
		}

		String userRole = session.getAttribute(ROLE);

		if (userRole == null) {
			return null;
		}

		String actorId = actorIdStr;

		Map<String, Object> info = new HashMap<String, Object>();
		info.put(USER_ID, actorId);
		info.put(ROLE, userRole);

		return info;

	}

	public static String getLoginUserId(HttpServletRequest request, HttpServletResponse response) {

		DbSession session = DbSession.getSession(request, response, false);

		if (session == null) {
			return null;
		}

		String actorIdStr = session.getAttribute(USER_ID);

		if (actorIdStr == null) {
			return null;
		}

		return actorIdStr;

	}

	public static String getLoginUserRole(HttpServletRequest request, HttpServletResponse response) {

		DbSession session = DbSession.getSession(request, response, false);

		if (session == null) {
			return null;
		}

		String role = session.getAttribute(ROLE);

		if (role == null) {
			return null;
		}

		return role;
	}

	public static String getUserIdByRememberMeId(String keyId) {

		String actorId = "";

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		try {
			LoginDao loginDao = session.getMapper(LoginDao.class);
			actorId = loginDao.getUserIdByRememberMeKeyId(keyId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during getUserIdByRememberMeId", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return actorId;
	}

	public static UserModel getUserModelById(String userId) {

		UserModel model = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		try {
			UserDao userDao = session.getMapper(UserDao.class);
			model = userDao.getUserAccountDetailsById(userId);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("Exception occured during getUserModelById ", e);
			throw new PersistenceException(e);
		} finally {
			session.close();
		}

		return model;
	}

	public static boolean isValidEmail(String email) {

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		boolean result = false;

		Pattern p = Pattern.compile(EMAIL_PATTERN);

		if (email != null) {
			Matcher matcher = p.matcher(email);
			result = matcher.matches();
			System.out.println(matcher.matches());
		}

		return result;
	}

	public static String createApiSessionKey(String userId) {

		String apiSessionKey = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			apiSessionKey = UUIDGenerator.generateUUID();
			loginDao.deleteApiSessionKey(userId, DateUtils.nowAsGmtMillisec());
			loginDao.insertApiSessionKey(apiSessionKey, userId, DateUtils.nowAsGmtMillisec());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return apiSessionKey;
	}

	public static int deleteApiSessionKey(String userId) {

		int deleteStatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			deleteStatus = loginDao.deleteApiSessionKey(userId, DateUtils.nowAsGmtMillisec());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return deleteStatus;
	}

	public static String isSessionExists(String sessionKey) {

		String userId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			userId = loginDao.isSessionExists(sessionKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isSessionExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userId;
	}

	public static String getSessionKey(String userId) {

		String sessionKey = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			sessionKey = loginDao.getSessionKey(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			sessionKey = null;
			logger.error("Exception occured during isSessionExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return sessionKey;
	}

	public static UserModel getUserModel(String email, String password) {

		UserModel model = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("email", email);
			map.put("password", password);

			model = userDao.getUserModel(map);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserModel : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return model;
	}

	public static UserModel getUserModelWithRoleIds(List<String> roleIds, String email, String password) {

		UserModel model = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("email", email);
			map.put("password", password);
			map.put("roleIdList", roleIds);

			model = userDao.getUserModelWithRoleIds(map);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserModel : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return model;
	}

	public static String addRememberMeKey(String userId) {

		String recordId = UUIDGenerator.generateUUID();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			recordId = loginDao.addRememberMeKey(recordId, userId, DateUtils.nowAsGmtMillisec()) > 0 ? recordId : null;
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addRememberMeKey : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return recordId;
	}

	public static void deleteRememberMeKey(String rememberMeKey) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			loginDao.deleteRememberMeKey(rememberMeKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteRememberMeKey : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getTotalPeopleOpeneedAppCount(List<String> assignedRegionList) {

		int totalPeopleOpeneedAppCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		LoginDao loginDao = session.getMapper(LoginDao.class);

		try {
			totalPeopleOpeneedAppCount = loginDao.getTotalPeopleOpeneedAppCount();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalPeopleOpeneedAppCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return totalPeopleOpeneedAppCount;
	}
}