package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UserModel;

public interface UserDao {

	UserModel actorForCredential(String username, String password);

	UserModel getUserAccountDetailsByEmailId(String emailId);

	UserModel getUserAccountDetailsById(String userId);

	UserModel getUserActiveDeativeDetailsById(String userId);

	int updatePassword(UserModel userModel);

	boolean isEmailIdExists(@Param("emailId") String emailId, @Param("vendorId") String vendorId, @Param("passengerRoleId") String passengerRoleId);

	int addUser(UserModel userModel);

	boolean deleteUserById(UserModel userModel);

	public UserModel getUserModel(Map<String, String> map);

	String checkLoginCredentials(UserModel userModel);

	String checkLoginCredentialsRoleWise(UserModel userModel);

	String checkLoginCredentialsNoActiveCheck(UserModel userModel);

	String checkLoginCredentialsRoleWiseNoActiveCheck(UserModel userModel);

	String checkLoginCredentialsRoleWiseOwnerOperator(UserModel userModel);

	int updateNotificationStatus(UserModel userModel);

	int driverOnOffDuty(UserModel userModel);

	String getRoleByUserId(String userId);

	int updateDriverInfoPhoto(UserModel userModel);

	int updateUser(UserModel userModel);

	int updateDriverUpdatedAt(UserModel userModel);

	int getTotalOperatorCountByBusinessOwnerId(@Param("userId") String userId, @Param("assignedRegionList") List<String> assignedRegionList, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("logggedInUserId") String logggedInUserId);

	List<UserModel> getBusinessOpertorListForSearch(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("roleId") String roleId, @Param("globalSearchString") String globalSearchString,
				@Param("assignedRegionList") List<String> assignedRegionList, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("logggedInUserId") String logggedInUserId);

	int getTotalBusinessOperatorCountBySearch(@Param("userId") String userId, @Param("globalSearchString") String globalSearchString, @Param("roleId") String roleId);

	int addDriver(UserModel userModel);

	int addDriverWeb(UserModel userModel);

	int updateDriverActiveStatus(UserModel userModel);

	boolean isEmailIdExistsForOtherUser(@Param("email") String email, @Param("userId") String userId, @Param("roleId") String roleId);

	int updateProfile(UserModel user);

	int updatePhoneNumber(UserModel userModel);

	int deleteUser(String userId);

	int activateDeactivateUserByAdmin(UserModel userModel);

	int getNetIncreaseInPassengerUser(@Param("createdAt") long createdAt, @Param("roleId") String roleId);

	int getDriverListForSearchCount(@Param("roleId") String roleId, @Param("globalSearchString") String globalSearchString);

	int deleteProfileImageByUserId(UserModel userModel);

	boolean isPhoneNumberExistsForRole(@Param("phoneNo") String phoneNo, @Param("phoneNoCode") String phoneNoCode, @Param("roleId") String roleId, @Param("userId") String userId, @Param("vendorId") String vendorId);

	String checkLoginCredentialsForLogin(Map<String, Object> inputMap);

	// List<UserModel> getDriverKmReportAdminListForSearch(@Param("start") int
	// start, @Param("length") int length, @Param("order") String order,
	// @Param("roleId") String roleId, @Param("globalSearchString") String
	// globalSearchString, @Param("tourStatus") String tourStatus,
	// @Param("startDate") long startDate, @Param("endDate") long endDate);

	int getDriverKmReportAdminListForSearchCount(@Param("roleId") String roleId, @Param("globalSearchString") String globalSearchString, @Param("startDate") long startDate, @Param("endDate") long endDate);

	List<UserModel> getAdminUserListForSearch(Map<String, Object> inputMap);

	int getTotalUserCount(Map<String, Object> inputMap);

	int getVendorTotalUserCount(Map<String, Object> inputMap);

	List<UserModel> getActiveUserListForSearch(Map<String, Object> inputMap);

	List<UserModel> getDeactiveUserListForSearch(Map<String, Object> inputMap);

	List<UserModel> getUserListForSearch(Map<String, Object> inputMap);

	List<UserModel> getUserListForSearchBasedOnVendorId(Map<String, Object> inputMap);

	int getTotalActiveUserCountBySearch(Map<String, Object> inputMap);

	int getTotalDeactiveUserCountBySearch(Map<String, Object> inputMap);

	int getTotalUserCountBySearch(Map<String, Object> inputMap);

	List<UserModel> getDriverReportAdminListForSearch(Map<String, Object> inputMap);

	int getDriverReportAdminListForSearchCount(Map<String, Object> inputMap);

	List<UserModel> getDriverKmReportAdminListForSearch(Map<String, Object> inputMap);

	List<UserModel> getDriverListForReferBenefitsReportsSearch(Map<String, Object> inputMap);

	int getTotalDriverCountForReferBenefitsReportsSearch(Map<String, Object> inputMap);

	int getTotalDriverUserCount(Map<String, Object> inputMap);

	List<UserModel> getDriverListForSearch(Map<String, Object> inputMap);

	List<UserModel> getDriverListForLoggedInTimeCronJob(Map<String, Object> inputMap);

	UserModel getDriverDetailsForLoggedInTimeReportById(Map<String, Object> inputMap);

	List<UserModel> getVendorListForSearch(Map<String, Object> inputMap);

	int approvedUserByAdmin(UserModel userModel);

	List<UserModel> getDriverList(Map<String, Object> inputMap);

	List<UserModel> getVendorList(String roleId);

	UserModel getDriverVendor(@Param("driverId") String driverId);

	UserModel getVendorForCar(@Param("carId") String carId);

	int getTotalPassengerUserCount(Map<String, Object> inputMap);

	int getVendorsTotalActiveUserCountBySearch(Map<String, Object> inputMap);

	int getVendorsTotalDeactiveUserCountBySearch(Map<String, Object> inputMap);

	int getVendorsTotalUserCountBySearch(Map<String, Object> inputMap);

	int getTotalActiveDriverCountBySearch(Map<String, Object> inputMap);

	int getTotalDeactiveDriverCountBySearch(Map<String, Object> inputMap);

	int getTotalDriverCountBySearch(Map<String, Object> inputMap);

	List<UserModel> getVendorDriverList(Map<String, Object> inputMap);

	int getVendorTotalDriverCount(Map<String, Object> inputMap);

	int getDriverListCount(Map<String, Object> inputMap);

	UserModel getUserModelWithRoleIds(Map<String, Object> map);

	boolean isEmailIdExistsForRoleIds(@Param("roleIdList") List<String> roleIdList, @Param("emailId") String emailId, @Param("userId") String userId);

	UserModel getUserAccountDetailsByRoleIdAndEmailId(@Param("roleIds") List<String> roleIds, @Param("emailId") String emailId);

	List<UserModel> getUserWithAccountDetailsListByRoleIds(Map<String, Object> inputMap);

	List<UserModel> getScriptAssignDefaultVendorUsers(@Param("offset") int offset, @Param("limit") int limit);

	UserModel getUserAccountDetailsByPhoneNumAndRoleId(@Param("phoneNum") String phoneNum, @Param("roleId") String roleId);

	int addMartUser(UserModel userModel);

	boolean isEmailIdExistsByRoleId(@Param("emailId") String emailId, @Param("roleId") String roleId);
	
	List<String> getUsersListByRoleIds(@Param("roleIdList") List<String> roleIdList);

	int updatePhoneNum(UserModel userModel);

	int getErpTotalUserCount(Map<String, Object> inputMap);

	List<UserModel> getErpListForSearch(Map<String, Object> inputMap);
}