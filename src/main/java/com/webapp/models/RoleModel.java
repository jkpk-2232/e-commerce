package com.webapp.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jeeutils.ComboUtils;
import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.JQTableUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.daos.ActivityLogDao;
import com.webapp.daos.RoleDao;

public class RoleModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(RoleModel.class);

	private String roleId;
	private String role;
	private boolean isSystemRole;

	public String getRole() {
		return role;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isSystemRole() {
		return isSystemRole;
	}

	public void setSystemRole(boolean isSystemRole) {
		this.isSystemRole = isSystemRole;
	}

	public static String getSystemRoleCombo(long selectdRoleId) {

		List<RoleModel> roles = getAllRoles(UserModel.ACTOR_MODEL_NOT_REQUIRED);
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (RoleModel roleModel : roles) {
			values.add(roleModel.getRoleId() + "");
			names.add(roleModel.getRole());
		}

		return ComboUtils.getOptionArray(names, values, selectdRoleId + "");
	}

	public static List<RoleModel> getAllRoles(UserModel actorModelNotRequired) {

		List<RoleModel> roles = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			roles = roleDao.getAllRoles();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllRoles : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roles;
	}

	public static RoleModel getRoleModelById(String roleId) {

		RoleModel roleModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			roleModel = roleDao.getRoleModelById(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRoleModelById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roleModel;
	}

	public static boolean isRoleNameExist(String roleName) {

		boolean isRollNameExist = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			isRollNameExist = roleDao.isRoleNameExist(roleName);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isRoleNameExist : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isRollNameExist;
	}

	public long addRole() {

		long roleId = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		logDao = session.getMapper(ActivityLogDao.class);

		try {
			roleId = roleDao.addRole(this);
			if (roleId > 0) {
				ActivityLogModel logModel = getActivityLogModel("{}", "Add Role");
				logDao.addInsertLogAb(logModel);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roleId;
	}

	public static String getRoleNameById(String roleId) {

		String role = "";

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			role = roleDao.getRoleNameById(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRoleNameById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return role;
	}

	public static boolean deleteRoleById(String roleId, String updatedBy) {

		boolean isDeleted = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);
		logDao = session.getMapper(ActivityLogDao.class);

		try {
			isDeleted = roleDao.deleteRoleById(roleId, updatedBy);
			if (isDeleted) {
				RoleModel roleModel = new RoleModel();
				roleModel.setRoleId(roleId);
				roleModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				roleModel.setUpdatedBy(updatedBy);
				ActivityLogModel logModel = getActivityLog(roleModel, "Delete Role");
				logDao.addDeleteLogAb(logModel);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDeleted;

	}

	public static boolean deleteRoles(String roleIdArray, String updatedBy) {

		UserModel actorModel = new UserModel();

		actorModel.setUserId(updatedBy);

		boolean isDeleted = false;

		String[] strRoleArray = MyHubUtils.splitStringByCommaArray(roleIdArray);

		for (int i = 0; i < strRoleArray.length; i++) {
			isDeleted = deleteRoleById(strRoleArray[i].toString(), updatedBy);
		}

		return isDeleted;
	}

	public static JSONObject getJsonRoleListForServerSide(JQTableUtils tableUtils, String getUserTimeZone) {
		JSONObject list = null;
		/*
		 * Connection connection = null; try { connection =
		 * ConnectionPoolManager.getConnectionPoolManager().getConnection();
		 * connection.setAutoCommit(false); RoleDao roleDao =
		 * DaoFactory.createRoleDao(connection, UserModel.ACTOR_MODEL_NOT_REQUIRED);
		 * list = roleDao.getJsonRoleListForServerSide(tableUtils, getUserTimeZone);
		 * connection.commit(); } catch (Exception e) { JdbcHelper.rollback(connection);
		 * logger.error("Exception occured during getJsonRoleListForServerSide", e);
		 * throw new DatabaseOperationException(e); } finally {
		 * JdbcHelper.closeConnection(connection); }
		 */
		return list;
	}

	public boolean updateRole() {

		boolean isupdated = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);
		logDao = session.getMapper(ActivityLogDao.class);

		try {
			isupdated = roleDao.updateRole(this);

			if (isupdated) {
				RoleModel roleModel = new RoleModel();
				roleModel.setRoleId(roleId);
				roleModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				roleModel.setUpdatedBy(updatedBy);
				ActivityLogModel logModel = getActivityLog(roleModel, "Update Role");
				logDao.addUpdateLogAb(logModel);
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isupdated;
	}

	public static String getRoleComboExcludeRunner(String selectdRoleId) {

		List<RoleModel> roles = getAllRoles(UserModel.ACTOR_MODEL_NOT_REQUIRED);
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (RoleModel roleModel : roles) {
			values.add(roleModel.getRoleId() + "");
			names.add(roleModel.getRole());
		}

		return ComboUtils.getOptionArray(names, values, selectdRoleId + "");
	}

	private static ActivityLogModel getActivityLog(RoleModel roleModel, String description) throws SQLException {

		// RoleModel model = getRoleModelById(roleModel.getRoleId());

		ActivityLogModel logModel = null;

		if (roleModel != null) {
			logModel = new ActivityLogModel();
			// logModel.setLog(gson.toJson(model, RoleModel.class));
			logModel.setDescription(description);
			logModel.setCreatedAt(roleModel.getUpdatedAt());
			logModel.setCreatedBy(roleModel.getUpdatedBy());
		}

		return logModel;
	}

	public static boolean isUserHasLoginAccess(String userId) {

		boolean hasAccess = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			List<String> roleList = UserRoleUtils.rolesWithWebLoginAccess();
			hasAccess = roleDao.isUserHasLoginAccess(roleList, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isUserHasLoginAccess : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return hasAccess;
	}

	public static boolean isUserHasPassengerLoginAccess(String userId) {

		boolean hasAccess = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			Map<String, String> roleMap = new HashMap<String, String>();

			roleMap.put("passenger", UserRoles.PASSENGER_ROLE);
			roleMap.put("userId", userId);

			hasAccess = roleDao.isUserHasPassengerLoginAccess(roleMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isUserHasLoginAccess : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return hasAccess;
	}

	public static List<RoleModel> getRolesByIds(String[] roleIds) {

		List<RoleModel> roles = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			roles = roleDao.getRolesByIds(roleIds);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRolesByIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roles;
	}
	
	public static RoleModel getRoleDetailsByRoleId(String roleId) {

		RoleModel roleModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RoleDao roleDao = session.getMapper(RoleDao.class);

		try {
			roleModel = roleDao.getRoleDetailsByRoleId(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRoleDetailsByRoleId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roleModel;
	}
}