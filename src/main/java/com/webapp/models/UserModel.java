package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.EncryptionUtils;
import com.utils.LoginUtils;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.daos.UserDao;

public class UserModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserModel.class);

	public static final UserModel ACTOR_MODEL_NOT_REQUIRED = null;

	private String firstName;
	private String lastName;
	private String fullName;
	private String userId;
	private String email;
	private String password;
	private String photoUrl;
	private boolean isActive;
	private boolean isDeleted;

	private double invoiceTotal;
	private double driverAmountTotal;
	private double driverBookingFees;

	private boolean isNotificationStatus;

	private boolean isOnDuty;

	private String userRole;
	private String apiSessionKey;
	private String roleId;

	private long badgeCount;

	private String tip;

	private UserCreditCardModel creditCardDetails;
	private String phoneNo;
	private String phoneNoCode;
	private String gender;

	private String verificationCode;

	private boolean isVerified;

	private String companyName;
	private String companyAddress;

	private String status;
	private String timezone;

	private double credit;
	private String referralCode;
	private boolean isFirstTime;
	private boolean isCardAvailable;

	private double driverReceivableAmount;
	private double driverCashAmountCollected;
	private double driverCashIncome;
	private double driverCardIncome;
	private double adminSettlementAmount;

	private long noOfRefer;
	private double totalReferralBenefits;

	private double promoDiscount;
	private double taxAmount;

	private long driverCount;
	private long carCount;

	private boolean isApprovelStatus;

	private String vendorId;
	private String vendorName;
	private double markupFare;

	// For KM reports
	private double driverDistanceTotal;

	private String latitude;
	private String longitude;

	private String userAccountId;
	private long urlId;
	private boolean isVendorDriverSubscriptionAppliedInBookingFlow;

	private String agentNumber;

	private String serviceId;
	private String serviceName;
	private String categoryId;
	private String categoryName;
	private String serviceTypeId;

	private double vendorMonthlySubscriptionFee;
	private boolean isVendorSubscriptionFreeActive;
	private boolean isVendorSubscriptionCurrentActive;
	private long vendorFreeSubscriptionStartDateTime;
	private long vendorFreeSubscriptionEndDateTime;
	private long vendorCurrentSubscriptionStartDateTime;
	private long vendorCurrentSubscriptionEndDateTime;
	private String vendorMonthlySubscriptionHistoryId;
	private boolean isVendorSubscriptionMarkedExpiredByCronJob;
	private long vendorSubscriptionMarkedExpiredByCronJobTiming;

	private String driverReferralCodeLogId;
	private String driverReferralDriverName;
	private String driverLicenseCardNumber;
	
	private String dob;
	private String phoneNum;
	
	private String vendorBrandName;

	public String checkLoginCredentials() {

		String userId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		UserModel userModel = this;

		if (userModel != null) {
			userModel.setPassword(EncryptionUtils.encryptString(userModel.getPassword()));
		}

		try {

			this.email = this.email.toLowerCase();

			userId = userDao.checkLoginCredentials(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentials :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userId;
	}

	public String checkLoginCredentialsRoleWise() {

		String userId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		UserDao userDao = session.getMapper(UserDao.class);

		UserModel userModel = this;

		if (userModel != null) {
			userModel.setPassword(EncryptionUtils.encryptString(userModel.getPassword()));
		}

		try {

			this.email = this.email.toLowerCase();

			userId = userDao.checkLoginCredentialsRoleWise(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentials :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userId;
	}

	public String checkLoginCredentialsNoActiveCheck() {

		String userId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		UserModel userModel = this;

		if (userModel != null) {
			userModel.setPassword(EncryptionUtils.encryptString(userModel.getPassword()));
		}

		try {
			this.email = this.email.toLowerCase();

			userId = userDao.checkLoginCredentialsNoActiveCheck(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentialsNoActiveCheck :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userId;
	}

	public String checkLoginCredentialsRoleWiseNoActiveCheck() {

		String userId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		UserModel userModel = this;

		if (userModel != null) {
			userModel.setPassword(EncryptionUtils.encryptString(userModel.getPassword()));
		}

		try {

			this.email = this.email.toLowerCase();

			userId = userDao.checkLoginCredentialsRoleWiseNoActiveCheck(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentialsRoleWiseNoActiveCheck :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userId;
	}

	public String checkLoginCredentialsRoleWiseOwnerOperator() {

		String userId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		UserModel userModel = this;

		if (userModel != null) {
			userModel.setPassword(EncryptionUtils.encryptString(userModel.getPassword()));
		}

		try {

			this.email = this.email.toLowerCase();

			userId = userDao.checkLoginCredentialsRoleWiseOwnerOperator(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentialsRoleWiseOwnerOperator :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userId;
	}

	public static UserModel getUserAccountDetailsByEmailId(String emailId) {

		UserModel userModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModel = userDao.getUserAccountDetailsByEmailId(emailId.toLowerCase());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountDetailsByEmailId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModel;
	}

	public static UserModel getUserAccountDetailsByRoleIdAndEmailId(List<String> roleIds, String emailId) {

		UserModel userModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModel = userDao.getUserAccountDetailsByRoleIdAndEmailId(roleIds, emailId.toLowerCase());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountDetailsByEmailId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModel;
	}

	public int updateNotificationStatus() {

		int updateSatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			long createdAt = DateUtils.nowAsGmtMillisec();
			this.setUpdatedBy(this.getUserId());
			this.setUpdatedAt(createdAt);

			updateSatus = userDao.updateNotificationStatus(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateNotificationStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateSatus;
	}

	public static int deleteUser(String userId) {

		int updateSatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			updateSatus = userDao.deleteUser(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateSatus;
	}

	public int driverOnOffDuty() {

		int updateSatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			long createdAt = DateUtils.nowAsGmtMillisec();
			this.setUpdatedBy(this.getUserId());
			this.setUpdatedAt(createdAt);

			updateSatus = userDao.driverOnOffDuty(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during driverOnOffDuty : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateSatus;
	}

	public static UserModel getUserAccountDetailsById(String userId) {

		UserModel userModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModel = userDao.getUserAccountDetailsById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModel;
	}

	public static UserModel getUserActiveDeativeDetailsById(String userId) {

		UserModel userModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModel = userDao.getUserActiveDeativeDetailsById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserActiveDeativeDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModel;
	}

	public boolean updatePassword() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userDao.updatePassword(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePassword : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return true;
	}

	public boolean updatePhoneNumber() {

		boolean isUpdated = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			int count = userDao.updatePhoneNumber(this);

			if (count > 0) {
				isUpdated = true;
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePassword : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isUpdated;
	}

	public static boolean isEmailIdExists(String emailId, String vendorId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			status = userDao.isEmailIdExists(emailId.toLowerCase(), vendorId, UserRoles.PASSENGER_ROLE_ID);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isEmailIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static boolean isEmailIdExistsForOtherUser(String emailId, String userId, String roleId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			status = userDao.isEmailIdExistsForOtherUser(emailId.toLowerCase(), userId, roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isEmailIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static boolean isEmailIdExistsForRoleIds(List<String> roleIdList, String emailId, String userId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			status = userDao.isEmailIdExistsForRoleIds(roleIdList, emailId.toLowerCase(), userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isEmailIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static String getRoleByUserId(String userId) {

		String roleId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			roleId = userDao.getRoleByUserId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRoleByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return roleId;
	}

	public static int getTotalUserCount(String roleId, long startDate, long endDate, String ownUserId, List<String> assignedRegionList, String vendorId) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("ownUserId", ownUserId);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalUserCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getVendorTotalUserCount(String roleId, long startDate, long endDate, String ownUserId, List<String> assignedRegionList, String serviceId, String categoryId) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("ownUserId", ownUserId);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("serviceId", serviceId);
		inputMap.put("categoryId", categoryId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getVendorTotalUserCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorTotalUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getTotalOperatorCountByBusinessOwnerId(String userId, List<String> assignedRegionList, long startDate, long endDate, String logggedInUserId) {

		int userCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalOperatorCountByBusinessOwnerId(userId, assignedRegionList, startDate, endDate, logggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalOperatorCountByBusinessOwnerId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getTotalUserCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, long startDate, long endDate) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalUserCountBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getTotalActiveUserCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalActiveUserCountBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalActiveUserCountBySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getTotalDeactiveUserCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalDeactiveUserCountBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalDeactiveUserCountBySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getTotalBusinessOperatorCountBySearch(String userId, String globalSearch, String roleId) {

		int userCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalBusinessOperatorCountBySearch(userId, roleId, globalSearch);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static List<UserModel> getUserListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String vendorId, String statusCheck) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("statusCheck", statusCheck);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			if (vendorId != null) {
				inputMap.put("vendorId", vendorId);
				userProfileModels = userDao.getUserListForSearchBasedOnVendorId(inputMap);
			} else {
				userProfileModels = userDao.getUserListForSearch(inputMap);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static List<UserModel> getDriverReportAdminListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDate, long endDate, List<String> assignedRegionList, String vendorId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getDriverReportAdminListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverReportAdminListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static int getDriverReportAdminListForSearchCount(String roleId, String globalSearchString, long startDate, long endDate, List<String> assignedRegionList, String vendorId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			count = userDao.getDriverReportAdminListForSearchCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverReportAdminListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserModel> getDriverListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, String onOffStatusForCheck, boolean onOffStatus, String vendorId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("roleId", roleId);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("onOffStatusForCheck", onOffStatusForCheck);
		inputMap.put("onOffStatus", onOffStatus);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getDriverListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static int getTotalDriverUserCount(String roleId, long startDate, long endDate, List<String> assignedRegionList, String onOffStatusForCheck, boolean onOffStatus, String vendorId) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("onOffStatusForCheck", onOffStatusForCheck);
		inputMap.put("onOffStatus", onOffStatus);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getTotalDriverUserCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDriverUserCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getDriverListForSearchCount(String roleId, String globalSearchString) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			count = userDao.getDriverListForSearchCount(roleId, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListForSearchCount :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static List<UserModel> getActiveUserListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userProfileModels = userDao.getActiveUserListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getActiveUserListForSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static List<UserModel> getDeactiveUserListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getDeactiveUserListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDeactiveUserListForSearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return userProfileModels;
	}

	public static List<UserModel> getAdminUserListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String ownUserId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("ownUserId", ownUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userProfileModels = userDao.getAdminUserListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getAdminUserListForSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static List<UserModel> getBusinessOpertorListForSearch(String userId, int start, int length, String order, String roleId, String globalSearchString, List<String> assignedRegionList, long startDate, long endDate, String logggedInUserId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getBusinessOpertorListForSearch(userId, start, length, order, roleId, globalSearchString, assignedRegionList, startDate, endDate, logggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return userProfileModels;
	}

	public int updateDriverActiveStatus(String loggedInUserId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInUserId);

			status = userDao.updateDriverActiveStatus(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return status;
	}

	public int activateDeactivateUserByAdmin() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			status = userDao.activateDeactivateUserByAdmin(this);

			if (this.roleId.equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID)) {
				LoginUtils.deleteApiSessionKey(userId);
				this.setOnDuty(false);
				userDao.driverOnOffDuty(this);
			} else if (this.roleId.equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {
				LoginUtils.deleteApiSessionKey(userId);
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateDeactivateUserByAdmin :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static int getNetIncreaseInPassengerUser(long createdAt, String roleId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			status = userDao.getNetIncreaseInPassengerUser(createdAt, roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNetIncreaseInPassengerUser :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public int deleteProfileImageByUserId() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();

			status = userDao.deleteProfileImageByUserId(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteProfileImageByUserId :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static boolean isPhoneNumberExistsForRole(String phoneNo, String phoneCode, String roleId, String userId, String vendorId) {
		boolean status = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {
			status = userDao.isPhoneNumberExistsForRole(phoneNo.trim(), phoneCode.trim(), roleId, userId, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isNumberExistsForRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}

	public static String checkLoginCredentialsForLogin(String password, String phoneNoCode, String phoneNo, String email, List<String> roleId, String vendorId) {

		// This method is used in login and forgot password api

		String userId = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("phoneNoCode", phoneNoCode);
		inputMap.put("phoneNo", phoneNo);
		inputMap.put("email", email);
		inputMap.put("roleId", roleId);
		inputMap.put("vendorId", vendorId);

		if (password == null || "".equals(password)) {
			inputMap.put("password", null);
		} else {
			inputMap.put("password", EncryptionUtils.encryptString(password));
		}

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userId = userDao.checkLoginCredentialsForLogin(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentialsForLogin :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userId;
	}

	public static List<UserModel> getDriverKmReportAdminListForSearch(int start, int length, String order, String roleId, String globalSearchString, String tourStatus, long startDate, long endDate, List<String> assignedRegionList, String vendorId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("roleId", roleId);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("tourStatus", tourStatus);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getDriverKmReportAdminListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverKmReportAdminListForSearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return userProfileModels;
	}

	public static List<UserModel> getDriverListForReferBenefitsReportsSearch(int start, int length, String order, String roleId, String globalSearchString, long startDate, long endDate, List<String> assignedRegionList) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userProfileModels = userDao.getDriverListForReferBenefitsReportsSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverListForReferBenefitsReportsSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static int getTotalDriverCountForReferBenefitsReportsSearch(String roleId, long startDate, long endDate, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			count = userDao.getTotalDriverCountForReferBenefitsReportsSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDriverCountForReferBenefitsReportsSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<UserModel> getDriverListForLoggedInTimeCronJob(String roleId, boolean isOnDuty) {

		List<UserModel> userModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("isOnDuty", isOnDuty);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModelList = userDao.getDriverListForLoggedInTimeCronJob(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListForLoggedInTimeCronJob :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModelList;
	}

	public static UserModel getDriverDetailsForLoggedInTimeReportById(String driverId) {

		UserModel userProfileModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);

		try {
			userProfileModel = userDao.getDriverDetailsForLoggedInTimeReportById(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDetailsForLoggedInTimeReportById :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModel;
	}

	public static List<UserModel> getVendorListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String ownUserId, String serviceId, String categoryId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("ownUserId", ownUserId);
		inputMap.put("serviceId", serviceId);
		inputMap.put("categoryId", categoryId);
		inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getVendorListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminUserListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public int approvedUserByAdmin() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			status = userDao.approvedUserByAdmin(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateDeactivateUserByAdmin :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static List<UserModel> getDriverList(int start, int length, String order, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String statusCheck, boolean status, String approvelCheck, boolean approvelStatus,
				String vendorId, String role) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("statusCheck", statusCheck);
		inputMap.put("status", status);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);
		inputMap.put("vendorId", vendorId);
		inputMap.put("role", role);
		inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {

			userProfileModels = userDao.getDriverList(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static List<UserModel> getVendorList(String roleId) {

		List<UserModel> userModels = new ArrayList<UserModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userModels = userDao.getVendorList(roleId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverListForManualBookingRideLater :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userModels;
	}

	public static UserModel getDriverVendor(String driverId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		UserDao userDao = session.getMapper(UserDao.class);

		UserModel userModel = new UserModel();
		try {

			userModel = userDao.getDriverVendor(driverId);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentials :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userModel;
	}

	public static UserModel getVendorForCar(String carId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		UserDao userDao = session.getMapper(UserDao.class);
		UserModel userModel = new UserModel();

		try {
			userModel = userDao.getVendorForCar(carId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkLoginCredentials :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModel;
	}

	public static int getTotalPassengerUserCount(String roleId, long startDate, long endDate, String ownUserId, List<String> assignedRegionList, String userId, String statusCheck) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("ownUserId", ownUserId);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);
		inputMap.put("statusCheck", statusCheck);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getTotalPassengerUserCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalPassengerUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getVendorsTotalActiveUserCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String approvelCheck, boolean approvelStatus, String userId) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getVendorsTotalActiveUserCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalActiveUserCountBySearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getVendorsTotalDeactiveUserCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String approvelCheck, boolean approvelStatus, String userId) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getVendorsTotalDeactiveUserCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDeactiveUserCountBySearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getVendorsTotalUserCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String approvelCheck, boolean approvelStatus, String loggedInUserId) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", loggedInUserId);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getVendorsTotalUserCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getTotalActiveDriverCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String approvelCheck, boolean approvelStatus, String vendorId, String role) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);
		inputMap.put("vendorId", vendorId);
		inputMap.put("role", role);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getTotalActiveDriverCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalActiveUserCountBySearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getTotalDeactiveDriverCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String approvelCheck, boolean approvelStatus, String vendorId, String role) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);
		inputMap.put("vendorId", vendorId);
		inputMap.put("role", role);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getTotalDeactiveDriverCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDeactiveUserCountBySearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getTotalDriverCountBySearch(String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String approvelCheck, boolean approvelStatus, String vendorId, String role) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);
		inputMap.put("vendorId", vendorId);
		inputMap.put("role", role);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getTotalDriverCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static List<UserModel> getVendorDriverList(int start, int length, String order, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String statusCheck, boolean status, String approvelCheck, boolean approvelStatus,
				String userId) {

		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("statusCheck", statusCheck);
		inputMap.put("status", status);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);
		inputMap.put("userId", userId);
		inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);
		try {

			userProfileModels = userDao.getVendorDriverList(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static int getVendorTotalDriverCount(String roleId, long startDate, long endDate, String ownUserId, List<String> assignedRegionList) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("ownUserId", ownUserId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			userCount = userDao.getVendorTotalDriverCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalUserCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return userCount;
	}

	public static int getDriverListCount(long startDate, long endDate, List<String> assignedRegionList, String statusCheck, boolean status, String approvelCheck, boolean approvelStatus, String vendor, String role) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("statusCheck", statusCheck);
		inputMap.put("status", status);
		inputMap.put("approvelCheck", approvelCheck);
		inputMap.put("approvelStatus", approvelStatus);
		inputMap.put("vendor", vendor);
		inputMap.put("role", role);
		inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getDriverListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static List<UserModel> getUserWithAccountDetailsListByRoleIds(List<String> roleIdList, long urlId) {

		List<UserModel> userModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleIdList", roleIdList);
		inputMap.put("urlId", urlId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModelList = userDao.getUserWithAccountDetailsListByRoleIds(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserWithAccountDetailsListByRoleIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModelList;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getApiSessionKey() {
		return apiSessionKey;
	}

	public void setApiSessionKey(String apiSessionKey) {
		this.apiSessionKey = apiSessionKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public UserCreditCardModel getCreditCardDetails() {
		return creditCardDetails;
	}

	public void setCreditCardDetails(UserCreditCardModel creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}

	public boolean isNotificationStatus() {
		return isNotificationStatus;
	}

	public void setNotificationStatus(boolean isNotificationStatus) {
		this.isNotificationStatus = isNotificationStatus;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean isOnDuty() {
		return isOnDuty;
	}

	public void setOnDuty(boolean isOnDuty) {
		this.isOnDuty = isOnDuty;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public long getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(long badgeCount) {
		this.badgeCount = badgeCount;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public double getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public double getDriverAmountTotal() {
		return driverAmountTotal;
	}

	public void setDriverAmountTotal(double driverAmountTotal) {
		this.driverAmountTotal = driverAmountTotal;
	}

	public double getDriverBookingFees() {
		return driverBookingFees;
	}

	public void setDriverBookingFees(double driverBookingFees) {
		this.driverBookingFees = driverBookingFees;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public boolean isFirstTime() {
		return isFirstTime;
	}

	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public boolean isCardAvailable() {
		return isCardAvailable;
	}

	public void setCardAvailable(boolean isCardAvailable) {
		this.isCardAvailable = isCardAvailable;
	}

	public double getDriverReceivableAmount() {
		return driverReceivableAmount;
	}

	public void setDriverReceivableAmount(double driverReceivableAmount) {
		this.driverReceivableAmount = driverReceivableAmount;
	}

	public double getDriverCashAmountCollected() {
		return driverCashAmountCollected;
	}

	public void setDriverCashAmountCollected(double driverCashAmountCollected) {
		this.driverCashAmountCollected = driverCashAmountCollected;
	}

	public double getDriverCashIncome() {
		return driverCashIncome;
	}

	public void setDriverCashIncome(double driverCashIncome) {
		this.driverCashIncome = driverCashIncome;
	}

	public double getDriverCardIncome() {
		return driverCardIncome;
	}

	public void setDriverCardIncome(double driverCardIncome) {
		this.driverCardIncome = driverCardIncome;
	}

	public double getAdminSettlementAmount() {
		return adminSettlementAmount;
	}

	public void setAdminSettlementAmount(double adminSettlementAmount) {
		this.adminSettlementAmount = adminSettlementAmount;
	}

	public double getDriverDistanceTotal() {
		return driverDistanceTotal;
	}

	public void setDriverDistanceTotal(double driverDistanceTotal) {
		this.driverDistanceTotal = driverDistanceTotal;
	}

	public long getNoOfRefer() {
		return noOfRefer;
	}

	public void setNoOfRefer(long noOfRefer) {
		this.noOfRefer = noOfRefer;
	}

	public double getTotalReferralBenefits() {
		return totalReferralBenefits;
	}

	public void setTotalReferralBenefits(double totalReferralBenefits) {
		this.totalReferralBenefits = totalReferralBenefits;
	}

	public double getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(double promoDiscount) {
		this.promoDiscount = promoDiscount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public long getDriverCount() {
		return driverCount;
	}

	public void setDriverCount(long driverCount) {
		this.driverCount = driverCount;
	}

	public long getCarCount() {
		return carCount;
	}

	public void setCarCount(long carCount) {
		this.carCount = carCount;
	}

	public boolean isApprovelStatus() {
		return isApprovelStatus;
	}

	public void setApprovelStatus(boolean isApprovelStatus) {
		this.isApprovelStatus = isApprovelStatus;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public double getMarkupFare() {
		return markupFare;
	}

	public void setMarkupFare(double markupFare) {
		this.markupFare = markupFare;
	}

	public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public long getUrlId() {
		return urlId;
	}

	public void setUrlId(long urlId) {
		this.urlId = urlId;
	}

	public boolean isVendorDriverSubscriptionAppliedInBookingFlow() {
		return isVendorDriverSubscriptionAppliedInBookingFlow;
	}

	public void setVendorDriverSubscriptionAppliedInBookingFlow(boolean isVendorDriverSubscriptionAppliedInBookingFlow) {
		this.isVendorDriverSubscriptionAppliedInBookingFlow = isVendorDriverSubscriptionAppliedInBookingFlow;
	}

	public String getAgentNumber() {
		return agentNumber;
	}

	public void setAgentNumber(String agentNumber) {
		this.agentNumber = agentNumber;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public double getVendorMonthlySubscriptionFee() {
		return vendorMonthlySubscriptionFee;
	}

	public void setVendorMonthlySubscriptionFee(double vendorMonthlySubscriptionFee) {
		this.vendorMonthlySubscriptionFee = vendorMonthlySubscriptionFee;
	}

	public boolean isVendorSubscriptionFreeActive() {
		return isVendorSubscriptionFreeActive;
	}

	public void setVendorSubscriptionFreeActive(boolean isVendorSubscriptionFreeActive) {
		this.isVendorSubscriptionFreeActive = isVendorSubscriptionFreeActive;
	}

	public boolean isVendorSubscriptionCurrentActive() {
		return isVendorSubscriptionCurrentActive;
	}

	public void setVendorSubscriptionCurrentActive(boolean isVendorSubscriptionCurrentActive) {
		this.isVendorSubscriptionCurrentActive = isVendorSubscriptionCurrentActive;
	}

	public long getVendorFreeSubscriptionStartDateTime() {
		return vendorFreeSubscriptionStartDateTime;
	}

	public void setVendorFreeSubscriptionStartDateTime(long vendorFreeSubscriptionStartDateTime) {
		this.vendorFreeSubscriptionStartDateTime = vendorFreeSubscriptionStartDateTime;
	}

	public long getVendorFreeSubscriptionEndDateTime() {
		return vendorFreeSubscriptionEndDateTime;
	}

	public void setVendorFreeSubscriptionEndDateTime(long vendorFreeSubscriptionEndDateTime) {
		this.vendorFreeSubscriptionEndDateTime = vendorFreeSubscriptionEndDateTime;
	}

	public long getVendorCurrentSubscriptionStartDateTime() {
		return vendorCurrentSubscriptionStartDateTime;
	}

	public void setVendorCurrentSubscriptionStartDateTime(long vendorCurrentSubscriptionStartDateTime) {
		this.vendorCurrentSubscriptionStartDateTime = vendorCurrentSubscriptionStartDateTime;
	}

	public long getVendorCurrentSubscriptionEndDateTime() {
		return vendorCurrentSubscriptionEndDateTime;
	}

	public void setVendorCurrentSubscriptionEndDateTime(long vendorCurrentSubscriptionEndDateTime) {
		this.vendorCurrentSubscriptionEndDateTime = vendorCurrentSubscriptionEndDateTime;
	}

	public String getVendorMonthlySubscriptionHistoryId() {
		return vendorMonthlySubscriptionHistoryId;
	}

	public void setVendorMonthlySubscriptionHistoryId(String vendorMonthlySubscriptionHistoryId) {
		this.vendorMonthlySubscriptionHistoryId = vendorMonthlySubscriptionHistoryId;
	}

	public boolean isVendorSubscriptionMarkedExpiredByCronJob() {
		return isVendorSubscriptionMarkedExpiredByCronJob;
	}

	public void setVendorSubscriptionMarkedExpiredByCronJob(boolean isVendorSubscriptionMarkedExpiredByCronJob) {
		this.isVendorSubscriptionMarkedExpiredByCronJob = isVendorSubscriptionMarkedExpiredByCronJob;
	}

	public long getVendorSubscriptionMarkedExpiredByCronJobTiming() {
		return vendorSubscriptionMarkedExpiredByCronJobTiming;
	}

	public void setVendorSubscriptionMarkedExpiredByCronJobTiming(long vendorSubscriptionMarkedExpiredByCronJobTiming) {
		this.vendorSubscriptionMarkedExpiredByCronJobTiming = vendorSubscriptionMarkedExpiredByCronJobTiming;
	}

	public String getDriverReferralCodeLogId() {
		return driverReferralCodeLogId;
	}

	public void setDriverReferralCodeLogId(String driverReferralCodeLogId) {
		this.driverReferralCodeLogId = driverReferralCodeLogId;
	}

	public String getDriverReferralDriverName() {
		return driverReferralDriverName;
	}

	public void setDriverReferralDriverName(String driverReferralDriverName) {
		this.driverReferralDriverName = driverReferralDriverName;
	}

	public String getDriverLicenseCardNumber() {
		return driverLicenseCardNumber;
	}

	public void setDriverLicenseCardNumber(String driverLicenseCardNumber) {
		this.driverLicenseCardNumber = driverLicenseCardNumber;
	}

	public UserModel getUserAccountDetailsByPhoneNumAndRoleId(String phoneNum, String roleId) {
		
		UserModel userModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userModel = userDao.getUserAccountDetailsByPhoneNumAndRoleId(phoneNum, roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountDetailsByPhoneNumAndRoleId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userModel;
	}

	public static boolean isEmailIdExistsByRoleId(String email, String roleId) {
		
		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			status = userDao.isEmailIdExistsByRoleId(email.toLowerCase(), roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isEmailIdExistsByRoleId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
		
	}
	
	public static List<String> getUsersListByRoleIds(List<String> roleIdList) {

		List<String> userIdList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userIdList = userDao.getUsersListByRoleIds(roleIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isEmailIdExistsByRoleId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userIdList;
	}
	
	public boolean updatePhoneNum() {

		boolean isUpdated = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			int count = userDao.updatePhoneNum(this);

			if (count > 0) {
				isUpdated = true;
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePhoneNum : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isUpdated;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public static int getErpTotalUserCount(String roleId, long startDate, long endDate, String ownUserId, List<String> assignedRegionList) {
		
		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("ownUserId", ownUserId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userCount = userDao.getErpTotalUserCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getErpTotalUserCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static List<UserModel> getErpListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList, String ownUserId) {
		
		List<UserModel> userProfileModels = new ArrayList<UserModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("roleId", roleId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("ownUserId", ownUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);

		try {
			userProfileModels = userDao.getErpListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getErpListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public String getVendorBrandName() {
		return vendorBrandName;
	}

	public void setVendorBrandName(String vendorBrandName) {
		this.vendorBrandName = vendorBrandName;
	}
}