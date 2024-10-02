package com.utils.myhub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.utils.LoginUtils;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.models.BusinessOperatorModel;
import com.webapp.models.UserModel;

public class UserRoleUtils {

	private final static List<String> ADMIN_FORGOT_PASSWORD_ACCESS_LIST = Arrays.asList(UserRoles.SUPER_ADMIN_ROLE_ID, UserRoles.ADMIN_ROLE_ID, UserRoles.VENDOR_ROLE_ID, UserRoles.SUB_VENDOR_ROLE_ID);
	private final static List<String> APP_FORGOT_PASSWORD_ACCESS_LIST = Arrays.asList(UserRoles.VENDOR_ROLE_ID, UserRoles.SUB_VENDOR_ROLE_ID, UserRoles.DRIVER_ROLE_ID, UserRoles.PASSENGER_ROLE_ID);
	private final static List<String> ADMIN_PROFILE_ACCESS_LIST = Arrays.asList(UserRoles.SUPER_ADMIN_ROLE_ID, UserRoles.ADMIN_ROLE_ID, UserRoles.VENDOR_ROLE_ID, UserRoles.SUB_VENDOR_ROLE_ID);

	public static boolean hasAdminForgotPasswordAccess(String roleId) {
		return ADMIN_FORGOT_PASSWORD_ACCESS_LIST.contains(roleId);
	}

	public static boolean hasAppForgotPasswordAccess(String roleId) {
		return APP_FORGOT_PASSWORD_ACCESS_LIST.contains(roleId);
	}

	public static boolean hasProfileAccess(String roleId) {
		return ADMIN_PROFILE_ACCESS_LIST.contains(roleId);
	}

	public static boolean isVendorRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID);
	}

	public static boolean isSubVendorRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.SUB_VENDOR_ROLE_ID);
	}

	public static boolean isVendorAndSubVendorRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID) || roleId.equalsIgnoreCase(UserRoles.SUB_VENDOR_ROLE_ID);
	}

	public static boolean isSuperAdminRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE_ID);
	}

	public static boolean isPassengerRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID);
	}

	public static boolean isSuperAdminAndAdminRole(String roleId) {
		return (roleId.equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE_ID) || roleId.equalsIgnoreCase(UserRoles.ADMIN_ROLE_ID));
	}

	public static boolean hasSettingsAccess(Map<String, String> loginSessionMap) {
		return loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL) || UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID));
	}

	public static boolean hasAccountsAccess(Map<String, String> loginSessionMap) {

		if (isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return (loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_DRIVER_ACCOUNT_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.VENDOR_MY_ACCOUNT_URL));
		}

		if (isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return (loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_DRIVER_ACCOUNT_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_ACCOUNT_URL));
		}

		return false;
	}

	public static List<String> getRoleIdsListForLogin(String roleId) {
		UserModel userModel = new UserModel();
		userModel.setRoleId(roleId);
		return getRoleIdsListForLogin(userModel);
	}

	public static List<String> getRoleIdsListForLogin(UserModel userModel) {

		List<String> roleIds = new ArrayList<>();
		roleIds.add(userModel.getRoleId());

		if (UserRoleUtils.isVendorRole(userModel.getRoleId())) {
			roleIds.add(UserRoles.SUB_VENDOR_ROLE_ID);
		}

		return roleIds;
	}

	public static List<String> rolesWithWebLoginAccess() {
		List<String> roleList = new ArrayList<>();
		roleList.add(UserRoles.SUPER_ADMIN_ROLE_ID);
		roleList.add(UserRoles.ADMIN_ROLE_ID);
		roleList.add(UserRoles.BUSINESS_OWNER_ROLE_ID);
		roleList.add(UserRoles.BUSINESS_OPERATOR_ROLE_ID);
		roleList.add(UserRoles.VENDOR_ROLE_ID);
		roleList.add(UserRoles.SUB_VENDOR_ROLE_ID);
		roleList.add(UserRoles.ERP_ROLE_ID);
		return roleList;
	}

	public static String getParentVendorId(Map<String, String> loginSessionMap) {

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			String parentVendorId = BusinessOperatorModel.getBusinessOwnerId(loginSessionMap.get(LoginUtils.USER_ID));
			return parentVendorId;
		}

		return loginSessionMap.get(LoginUtils.USER_ID);
	}

	public static String getParentVendorId(String subVendorId) {
		String parentVendorId = BusinessOperatorModel.getBusinessOwnerId(subVendorId);
		return parentVendorId;
	}

	public static List<String> getAdminRoleIds() {
		List<String> roleList = new ArrayList<>();
		roleList.add(UserRoles.SUPER_ADMIN_ROLE_ID);
		roleList.add(UserRoles.ADMIN_ROLE_ID);
		return roleList;
	}
	
	public static boolean isErpRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.ERP_ROLE_ID);
	}
	
	public static boolean isErpUserAndErpEmployeeRole(String roleId) {
		return roleId.equalsIgnoreCase(UserRoles.ERP_ROLE_ID) || roleId.equalsIgnoreCase(UserRoles.ERP_EMPLOYEE_ROLE_ID);
	}
}
