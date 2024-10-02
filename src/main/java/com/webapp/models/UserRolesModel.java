package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.UserRolesDao;

public class UserRolesModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserRolesModel.class);

	private String userRoleId;
	private String userId;
	private String roleId;
	private String parentId;

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public static boolean isUserRole(String userId, long roleId) {

		boolean isUserRole = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserRolesDao userDao = session.getMapper(UserRolesDao.class);

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", userId);
			map.put("roleId", roleId + "");
			isUserRole = userDao.isUserRole(map);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isUserRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isUserRole;
	}

	public static List<String> getUserRoleIds(String userId) {

		List<String> roles = new ArrayList<String>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserRolesDao userDao = session.getMapper(UserRolesDao.class);

		try {
			List<UserRolesModel> roleList = userDao.getUserRoleIds(userId);

			for (UserRolesModel userRolesModel : roleList) {
				roles.add(userRolesModel.getRoleId());
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserRoleIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roles;
	}

	public static boolean deleteUserRoles(UserModel userModel) {

		boolean isDeleted = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserRolesDao userDao = session.getMapper(UserRolesDao.class);

		try {
			isDeleted = userDao.deleteUserRoles(userModel);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteUserRoles : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDeleted;
	}

	public static boolean deleteUserRolesByUserId(String userId) {

		boolean isDeleted = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserRolesDao userDao = session.getMapper(UserRolesDao.class);

		try {
			isDeleted = userDao.deleteUserRolesByUserId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteUserRolesByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDeleted;
	}

	public String addUserRole() {

		String userRoleId = "";

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserRolesDao userDao = session.getMapper(UserRolesDao.class);

		try {
			userRoleId = userDao.addUserRole(this) > 0 ? this.userRoleId : null;
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addUserRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userRoleId;
	}

	public static boolean isRoleAssignToSomeOne(String roleId) {

		boolean isRoleAssign = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserRolesDao userDao = session.getMapper(UserRolesDao.class);

		try {
			isRoleAssign = userDao.isRoleAssignToSomeOne(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isRoleAssignToSomeOne : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isRoleAssign;
	}

}
