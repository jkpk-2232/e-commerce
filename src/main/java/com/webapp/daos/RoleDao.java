package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.json.JSONException;
import org.json.JSONObject;

import com.utils.JQTableUtils;
import com.webapp.models.RoleModel;

public interface RoleDao {

	List<RoleModel> getAllRoles() throws SQLException;

	List<RoleModel> getRolesByIds(@Param("roleIds") String[] roleIds) throws SQLException;

	RoleModel getRoleModelById(String roleId) throws SQLException;

	boolean isRoleNameExist(String roleName) throws SQLException;

	long addRole(RoleModel roleModel) throws SQLException;

	String getRoleNameById(String roleId) throws SQLException;

	boolean deleteRoles(String roleIdArray, String updatedBy) throws SQLException;

	boolean deleteRoleById(String roleIdArray, String updatedBy) throws SQLException;

	JSONObject getJsonRoleListForServerSide(JQTableUtils tableUtils, String getUserTimeZone) throws SQLException, JSONException;

	boolean updateRole(RoleModel roleModel) throws SQLException;

	boolean isUserHasLoginAccess(@Param("roleList") List<String> roleList, @Param("userId") String userId) throws SQLException;

	boolean isUserHasPassengerLoginAccess(Map<String, String> roleMap) throws SQLException;
	
	RoleModel getRoleDetailsByRoleId(String roleId);
}
