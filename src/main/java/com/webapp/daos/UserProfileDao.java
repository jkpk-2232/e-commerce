package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UserProfileModel;

public interface UserProfileDao {

	int addUser(UserProfileModel userProfileModel);

	UserProfileModel getUserAccountDetailsById(String userId);

	UserProfileModel getAdminUserAccountDetailsById(String userId);

	int updatePassenger(UserProfileModel userProfileModel);

	List<UserProfileModel> getUserListByRole(Map<String, Object> driverMap);

	UserProfileModel getUserDetailsByRole(String roleId);

	int insertAdminUser(UserProfileModel userProfileModel);

	int updateTip(UserProfileModel userProfileModel);

	List<UserProfileModel> getAllUsersByRoleId(String roleId);

	List<UserProfileModel> getBOUsersByRoleId(String roleId);

	int updateAdminUserInfo(UserProfileModel userProfileModel);

	int updateVerificationStatus(UserProfileModel userProfileModel);

	String getVerificationCodeOfUser(String userId);

	void updateVerificationCodeForPassenger(@Param("userId") String userId, @Param("verificationCode") String verificationCode);

	UserProfileModel getUserDetailsByReferralCode(@Param("referralCode") String referralCode);

	int updateUserCredits(UserProfileModel userProfileModel);

	double getTotalCredits();

	void updateCardAvailableStatus(@Param("userId") String userId, @Param("isCardAvailable") boolean cardAvailable);

	int updateUserBillingAddressDetails(UserProfileModel userProfileModel);

	int getRideLaterDriverListCount(Map<String, Object> inputMap);

	List<UserProfileModel> getRideLaterDriverListBySearch(Map<String, Object> inputMap);

	int updateRideLaterVisitedTime(UserProfileModel userProfileModel);

	int updateReferralCode(UserProfileModel userProfileModel);

	List<UserProfileModel> getSenderListByReferralCode(@Param("referralCode") String referralCode);

	List<UserProfileModel> getDriverListForManualBookingRideLater(Map<String, Object> inputMap);

	List<UserProfileModel> getVendorDriverListForManualBookingRideLater(Map<String, Object> inputMap);

	List<UserProfileModel> getVendorRideLaterDriverListBySearch(Map<String, Object> inputMap);

	int getVendorRideLaterDriverListCount(Map<String, Object> inputMap);

	List<UserProfileModel> getUserIdByRoleId(@Param("roleId") String roleId);

	List<UserProfileModel> getVendorListByRegion(@Param("roleId") String roleId, @Param("assignedRegionList") List<String> assignedRegionList);

	List<UserProfileModel> getVendorPassengerList(@Param("vendorId") String vendorId, @Param("roleId") String roleId, @Param("assignedRegionList") List<String> assignedRegionList);

	void updateVendorDriverSubscriptionAppliedInBookingFlowFlag(UserProfileModel userProfileModel);

	List<UserProfileModel> getUserDriverSubscriptionByUserId(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey);

	List<UserProfileModel> getVendorBrandList(@Param("serviceId") String serviceId, @Param("categoryId") String categoryId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("distance") String distance,
				@Param("latAndLong") String latAndLong, @Param("loggedInUserId") String loggedInUserId, @Param("currentDayOfWeekValue") String currentDayOfWeekValue, @Param("isVendorStoreSubscribed") String isVendorStoreSubscribed,
				@Param("considerDistance") String considerDistance, @Param("multicityCityRegionId") String multicityCityRegionId);

	List<UserProfileModel> getDriverListForSearch(@Param("loggedInuserId") String loggedInuserId, @Param("roleId") String roleId, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);

	List<UserProfileModel> getVendorListByServiceTypeId(Map<String, Object> inputMap);

	List<UserProfileModel> getUserDetailsForGorupOfIds(@Param("userIds") List<String> userIds);

	void updateVendorMonthlySubscriptionHistoryId(UserProfileModel userProfileModel);

	void updateVendorMonthlySubscriptionParameters(UserProfileModel userProfileModel);

	void updateVendorMonthlySubscriptionScriptParametersExistingVendors(UserProfileModel userProfileModel);

	List<UserProfileModel> getVendorListForVendorSubscriptionExpiry(@Param("roleId") String roleId, @Param("currentTime") long currentTime, @Param("next3Days") long next3Days);

	void updateVendorSubscriptionAccountExpired(@Param("updateAccountToExpiredList") List<UserProfileModel> updateAccountToExpiredList);

	int insertMartUser(UserProfileModel userProfileModel);
	
	List<UserProfileModel> getVendorBrandStoresList(@Param("vendorId") String vendorId, @Param("serviceId") String serviceId,@Param("distance") String distance,@Param("latAndLong") String latAndLong,@Param("loggedInUserId") String loggedInUserId,
				@Param("currentDayOfWeekValue") String currentDayOfWeekValue, @Param("isVendorStoreSubscribed") boolean isVendorStoreSubscribed,@Param("considerDistance") String considerDistance,@Param("multicityCityRegionId") String multicityCityRegionId);

	List<UserProfileModel> getVendorStoresListForApiSearch(@Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("distance") String distance,
				@Param("latAndLong") String latAndLong, @Param("loggedInUserId") String loggedInUserId, @Param("currentDayOfWeekValue") String currentDayOfWeekValue, @Param("isVendorStoreSubscribed") String isVendorStoreSubscribed,
				@Param("considerDistance") String considerDistance, @Param("multicityCityRegionId") String multicityCityRegionId);
}