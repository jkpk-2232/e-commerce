package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.webapp.models.UserModel;
import com.webapp.models.UserRolesModel;

public interface UserRolesDao {

	public List<UserRolesModel> getUserRoles(String userId) throws SQLException;

	public List<UserRolesModel> getUserRoleIds(String userId) throws SQLException;

	public int addUserRole(UserRolesModel rolesModel) throws SQLException;

	public boolean isUserRole(Map<String, String> map) throws SQLException;

	public boolean deleteUserRoles(UserModel userModel) throws SQLException;

	public boolean updateUserRoles(UserModel userModel) throws SQLException;

	public boolean isRoleAssignToSomeOne(String roleId) throws SQLException;

	public String getUserAllRoles(String userId) throws SQLException;
	
	public boolean deleteUserRolesByUserId(String userId) throws SQLException;
}
