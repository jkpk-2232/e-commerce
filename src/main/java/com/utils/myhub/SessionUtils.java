package com.utils.myhub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.dbsession.DbSession;
import com.webapp.ProjectConstants;
import com.webapp.models.UserModel;

public class SessionUtils {

	public static DbSession getDbSessionObject(HttpServletRequest request, HttpServletResponse response, boolean create) {
		return DbSession.getSession(request, response, create);
	}

	public static void updateAttribute(HttpServletRequest request, HttpServletResponse response, String key, String value) {
		DbSession sessionDB = getDbSessionObject(request, response, true);
		sessionDB.setAttribute(key, value);
	}

	public static Map<String, String> getSession(HttpServletRequest request, HttpServletResponse response, boolean create) {
		DbSession sessionDB = getDbSessionObject(request, response, create);
		return sessionAttributes(sessionDB);
	}

	public static Map<String, String> sessionAttributes(DbSession sessionDB) {

		if (sessionDB == null) {
			return null;
		}

		List<DbSession> list = sessionDB.getAllAttributes();

		if (list == null || list.isEmpty()) {
			return null;
		}

		Map<String, String> sessionAttributes = new HashMap<>();

		for (DbSession dbSession : list) {
			sessionAttributes.put(dbSession.getAttribute(), dbSession.getAttributeValue());
		}

		return sessionAttributes;
	}

	public static String getAttributeValue(Map<String, String> sessionAttributes, String key) {
		return sessionAttributes.containsKey(key) ? sessionAttributes.get(key) : "";
	}

	public static void setSessionAttibutesForUserLogin(DbSession session, String userId, UserModel userProfileModel) {
		session.setAttribute(LoginUtils.USER_ID, userId);
		session.setAttribute(LoginUtils.ROLE_ID, userProfileModel.getRoleId());
		session.setAttribute(LoginUtils.ROLE, userProfileModel.getUserRole());
		session.setAttribute(LoginUtils.USER_FULL_NAME, MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));
		if (UserRoleUtils.isVendorAndSubVendorRole(userProfileModel.getRoleId())) {
			session.setAttribute(LoginUtils.PHOTO_URL, StringUtils.validString(userProfileModel.getPhotoUrl()) ? userProfileModel.getPhotoUrl() : ProjectConstants.DEFAULT_IMAGE_ADMIN);
		}
	}
}
