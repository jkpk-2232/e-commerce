package com.webapp.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.SendEmailThread;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.EncryptionUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.Action;
import com.webapp.actions.BusinessAction;
import com.webapp.daos.DriverVendorsDao;
import com.webapp.daos.UserCreditCardDao;
import com.webapp.daos.UserDao;
import com.webapp.daos.UserProfileDao;

public class UserProfileModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserProfileModel.class);

	public static final UserModel ACTOR_MODEL_NOT_REQUIRED = null;

	private String userInfoId;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private String phoneNoCode;
	private String drivingLicense;
	private long joiningDate;
	private String mailAddressLineOne;
	private String mailAddressLineTwo;
	private String mailCountryId;
	private String mailStateId;
	private String mailCityId;
	private String mailZipCode;
	private String billAddressLineOne;
	private String billAddressLineTwo;
	private String billCountryId;
	private String billStateId;
	private String billCityId;
	private String billZipCode;
	private String gender;
	private String userId;
	private String cardType;
	private String accountNumber;
	private boolean isNotificationStatus;
	private boolean isOnDuty;
	private String roleId;
	private String userRole;
	private long tip;
	private long badgeCount;
	private boolean isApprovelStatus;
	private boolean isSameAsMailing;

	private UserCreditCardModel creditCardDetails;

	private String verificationCode;

	private boolean isVerified;

	private String email;
	private String password;
	private String photoUrl;
	private boolean isActive;
	private boolean isDeleted;

	private String fullName;

	private String apiSessionKey;

	private String companyName;
	private String companyAddress;

	private double credit;
	private String referralCode;
	private String driverReferralCode;

	private boolean isFirstTime;

	private boolean isCardAvailable;

	private long rideLaterVisitedTime;

	private double maximumMarkupFare;

	private boolean isVendorDriverSubscriptionAppliedInBookingFlow;

	private String vendorBrandName;
	private String vendorBrandImage;
	private String vendorBrandSearchKeywords;
	private String vendorStoreId;
	private String storeName;
	private String storeAddress;
	private String storeAddressLat;
	private String storeAddressLng;
	private String storeImage;
	private boolean isClosedToday;
	private int dateType;
	private long startDate;
	private long endDate;
	private long dateOpeningMorningHours;
	private long dateClosingMorningHours;
	private long dateOpeningEveningHours;
	private long dateClosingEveningHours;
	private String day;
	private long openingMorningHours;
	private long closingMorningHours;
	private long openingEveningHours;
	private long closingEveningHours;

	private double distance;
	private String serviceName;
	private String categoryName;
	private String categoryId;
	private String serviceId;
	private boolean isVendorStoreSubscribed;

	private boolean isDriverSubscribed;
	private int driverPriorityNumber;

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

	private boolean isSelfDeliveryWithinXKm;
	private double selfDeliveryFee;
	
	private String phonepeMerchantId;
	private String phonepeMerchantName;
	private String saltKey;
	private String saltIndex;
	
	private long dob;

	public String addUserRegistration(String userRoleId, String headerVendorId) throws IOException {

		String userInfoId = UUIDGenerator.generateUUID();

		int status = -1;

		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.userInfoId = userInfoId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		UserDao userDao = session.getMapper(UserDao.class);
		DriverVendorsDao driverVendorsDao = session.getMapper(DriverVendorsDao.class);
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		try {

			UserModel userModel = new UserModel();

			userModel.setUserId(this.userId);
			userModel.setEmail(this.getEmail().trim().toLowerCase());
			userModel.setPassword(EncryptionUtils.encryptString(this.getPassword()));
			userModel.setPhotoUrl(this.getPhotoUrl());
			userModel.setCreatedAt(this.createdAt);
			userModel.setUpdatedAt(this.updatedAt);
			userModel.setCreatedBy(this.userId);
			userModel.setUpdatedBy(this.userId);
			userModel.setRoleId(userRoleId);
			userModel.setApprovelStatus(true);

			status = userDao.addUser(userModel);

			this.createdBy = userId;
			this.updatedBy = userId;

			this.setReferralCode(this.phoneNo);

			status = userProfileDao.addUser(this);

			if (this.isCardAvailable()) {

				UserCreditCardModel userCreditCardModel = new UserCreditCardModel();

				userCreditCardModel.setUserCreditCardDetailsId(UUIDGenerator.generateUUID());
				userCreditCardModel.setUserId(userId);
				userCreditCardModel.setCardNumber(this.getCreditCardDetails().getCardNumber());
				userCreditCardModel.setToken(this.getCreditCardDetails().getToken());
				userCreditCardModel.setAuthCode(null);
				userCreditCardModel.setCardType(this.getCreditCardDetails().getCardType());
				userCreditCardModel.setCreatedBy(this.createdBy);
				userCreditCardModel.setUpdatedBy(this.createdBy);
				userCreditCardModel.setCreatedAt(this.updatedAt);
				userCreditCardModel.setUpdatedAt(this.updatedAt);
				userCreditCardModel.setNonceToken(this.getCreditCardDetails().getNonceToken());
				userCreditCardModel.setBraintreeProfileId(this.getCreditCardDetails().getBraintreeProfileId());

				userCreditCardDao.addUserCreditCardDetails(userCreditCardModel);
			}

			if (userModel.getRoleId().equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {
				if (headerVendorId != null) {

					DriverVendorsModel driverVendorModel = new DriverVendorsModel();

					driverVendorModel.setDriverVendorId(UUIDGenerator.generateUUID());
					driverVendorModel.setDriverId(this.userId);
					driverVendorModel.setVendorId(headerVendorId);
					driverVendorModel.setCreatedBy(this.userId);
					driverVendorModel.setUpdatedBy(this.userId);
					driverVendorModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
					driverVendorModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
					driverVendorModel.setRoleId(UserRoles.PASSENGER_ROLE_ID);

					driverVendorsDao.addDriverVendorMapping(driverVendorModel);
				}
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add user passenger : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		if (status > 0) {
			return userId;
		} else {
			return null;
		}
	}

	public String addUser() {

		int status = -1;
		String userId = UUIDGenerator.generateUUID();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			String password = MyHubUtils.generatePassword();
			UserModel userModel = new UserModel();

			userModel.setUserId(userId);
			userModel.setEmail(this.getEmail().trim().toLowerCase());
			userModel.setPhotoUrl(this.getPhotoUrl());
			userModel.setPassword(EncryptionUtils.encryptString(password));
			userModel.setCreatedAt(this.getCreatedAt());
			userModel.setUpdatedAt(this.getUpdatedAt());
			userModel.setCreatedBy(this.getUserId());
			userModel.setUpdatedBy(this.getUserId());
			userModel.setRoleId(this.getRoleId());
			userModel.setApprovelStatus(true);

			status = userDao.addUser(userModel);

			if (status > 0) {

				this.setUserInfoId(UUIDGenerator.generateUUID());
				this.setUserId(userId);

				status = userProfileDao.insertAdminUser(this);

				String subject = String.format(BusinessAction.messageForKeyAdmin("labelLoginCredentials", ProjectConstants.ENGLISH_ID), WebappPropertyUtils.PROJECT_NAME);
				String message = Action.getCrateAccountMessage(this.getFirstName() + " " + this.getLastName(), this.getEmail(), password);

				if (this.roleId.equals(UserRoles.VENDOR_ROLE_ID)) {
					message = Action.getCreateVendorAccountMessage(this.getFirstName() + " " + this.getLastName(), this.getEmail(), password);
				}

				new SendEmailThread(this.getEmail(), subject, message);
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userId;
	}

	public static UserProfileModel getUserAccountDetailsById(String userId) {

		UserProfileModel userProfileModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		try {
			userProfileModel = userProfileDao.getUserAccountDetailsById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return userProfileModel;
	}

	public static UserProfileModel getAdminUserAccountDetailsById(String userId) {

		UserProfileModel userProfileModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		try {
			userProfileModel = userProfileDao.getAdminUserAccountDetailsById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminUserAccountDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return userProfileModel;
	}

	public int updatePassenger(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		UserDao userDao = session.getMapper(UserDao.class);

		int updatePassengerStatus = 0;

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);
			this.setUserId(userId);

			updatePassengerStatus = userProfileDao.updatePassenger(this);

			if (updatePassengerStatus > 0) {

				UserModel userModel = new UserModel();

				userModel.setPhotoUrl(this.photoUrl);
				userModel.setUserId(userId);
				userModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				userModel.setUpdatedBy(userId);

				userDao.updateDriverInfoPhoto(userModel);
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updatePassenger : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return updatePassengerStatus;
	}

	public static List<UserProfileModel> getUserListByRole(String roleId, String searchKey, long startOffSet, long recordOffset) {
		List<UserProfileModel> driversList = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			Map<String, Object> driverMap = new HashMap<String, Object>();

			driverMap.put("roleId", roleId);
			driverMap.put("searchKey", searchKey);
			driverMap.put("startOffSet", startOffSet);
			driverMap.put("recordOffset", recordOffset);

			driversList = userProfileDao.getUserListByRole(driverMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserListByRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driversList;
	}

	public static UserProfileModel getUserDetailsByRole(String roleId) {

		UserProfileModel userProfileModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		try {
			userProfileModel = userProfileDao.getUserDetailsByRole(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return userProfileModel;
	}

	public int updateTip(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		int updateTipStatus = 0;

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);
			this.setUserId(userId);

			updateTipStatus = userProfileDao.updateTip(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTip : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return updateTipStatus;
	}

	public static List<UserProfileModel> getAllUsersByRoleId(String roleId) {

		List<UserProfileModel> usersList = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			usersList = userProfileDao.getAllUsersByRoleId(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllUsersByRoleId : ", t);
		} finally {
			session.close();
		}
		return usersList;
	}

	public static List<UserProfileModel> getUserIdByRoleId(String roleId) {

		List<UserProfileModel> usersList = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			usersList = userProfileDao.getUserIdByRoleId(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserIdByRoleId : ", t);
		} finally {
			session.close();
		}
		return usersList;
	}

	public static List<UserProfileModel> getBOUsersByRoleId(String roleId) {

		List<UserProfileModel> usersList = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			usersList = userProfileDao.getBOUsersByRoleId(roleId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBOUsersByRoleId : ", t);
		} finally {
			session.close();
		}
		return usersList;
	}

	public void updateUser() {

		int isUpdated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			UserModel userModel = new UserModel();

			userModel.setUserId(this.getUserId());
			userModel.setEmail(this.getEmail().trim().toLowerCase());
			userModel.setPhotoUrl(this.getPhotoUrl());
			userModel.setUpdatedAt(this.getUpdatedAt());
			userModel.setUpdatedBy(this.getUpdatedBy());
			userModel.setActive(this.isActive());

			isUpdated = userDao.updateUser(userModel);

			if (isUpdated > 0) {
				isUpdated = userProfileDao.updateAdminUserInfo(this);
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTip : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public void updateAdminProfile() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		UserDao userDao = session.getMapper(UserDao.class);

		try {

			UserModel userModel = new UserModel();

			userModel.setUserId(this.getUserId());
			userModel.setEmail(this.getEmail().trim().toLowerCase());
			userModel.setPhotoUrl(this.getPhotoUrl());
			userModel.setUpdatedAt(this.getUpdatedAt());
			userModel.setUpdatedBy(this.getUpdatedBy());
			userModel.setActive(this.isActive());

			userDao.updateProfile(userModel);
			userProfileDao.updateAdminUserInfo(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAdminProfile : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static String getVerificationCodeOfUser(String userId) {

		String verifyCode = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		try {

			verifyCode = userProfileDao.getVerificationCodeOfUser(userId);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVerificationCodeOfUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return verifyCode;
	}

	public int updateVerificationStatus() {

		int updateSatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			this.setUpdatedBy(this.getUserId());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			updateSatus = userProfileDao.updateVerificationStatus(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVerificationStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return updateSatus;
	}

	public static void updateVerificationCodeForPassenger(String userId, String verificationCode) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			userProfileDao.updateVerificationCodeForPassenger(userId, verificationCode);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVerificationCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static UserProfileModel getUserDetailsByReferralCode(String referralCode) {

		UserProfileModel userProfileModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModel = userProfileDao.getUserDetailsByReferralCode(referralCode);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByReferralCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModel;
	}

	public int updateUserCredits() {

		int updateSatus = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			this.setUpdatedBy(this.getUserId());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			updateSatus = userProfileDao.updateUserCredits(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUserCredits : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateSatus;
	}

	public static double getTotalCreditsBalance(String userId) {

		double count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			count = userProfileDao.getTotalCredits();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalCredits : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateCardAvailableStatus(String userId, boolean cardAvailable) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			userProfileDao.updateCardAvailableStatus(userId, cardAvailable);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCardAvailableStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public int updateUserBillingAddressDetails() {

		int isUpdated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(this.getUserId());

			isUpdated = userProfileDao.updateUserBillingAddressDetails(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAdminProfile : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isUpdated;
	}

	public static int getRideLaterDriverListCount(Map<String, Object> inputMap) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);
			count = userProfileDao.getRideLaterDriverListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterDriverListCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserProfileModel> getRideLaterDriverListBySearch(Map<String, Object> inputMap) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);
			userProfileModels = userProfileDao.getRideLaterDriverListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterDriverListBySearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public int updateRideLaterVisitedTime() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			count = userProfileDao.updateRideLaterVisitedTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRideLaterVisitedTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public int updateReferralCode(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		int updateStatus = 0;

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInUserId);

			updateStatus = userProfileDao.updateReferralCode(this);

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateReferralCode : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return updateStatus;
	}

	public static List<UserProfileModel> getSenderListByReferralCode(String referralCode) {

		List<UserProfileModel> senderList = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			senderList = userProfileDao.getSenderListByReferralCode(referralCode);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSenderListByReferralCode : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return senderList;
	}

	public static List<UserProfileModel> getDriverListForManualBookingRideLater(Map<String, Object> inputMap) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);
			userProfileModels = userProfileDao.getDriverListForManualBookingRideLater(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListForManualBookingRideLater :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getVendorDriverListForManualBookingRideLater(Map<String, Object> inputMap) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);
			userProfileModels = userProfileDao.getVendorDriverListForManualBookingRideLater(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorDriverListForManualBookingRideLater :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getVendorRideLaterDriverListBySearch(Map<String, Object> inputMap) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);
			userProfileModels = userProfileDao.getVendorRideLaterDriverListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorRideLaterDriverListBySearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static int getVendorRideLaterDriverListCount(Map<String, Object> inputMap) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			inputMap.put("driverRoleId", UserRoles.DRIVER_ROLE_ID);
			count = userProfileDao.getVendorRideLaterDriverListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorRideLaterDriverListCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserProfileModel> getVendorListByRegion(String roleId, List<String> assignedRegionList) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModels = userProfileDao.getVendorListByRegion(roleId, assignedRegionList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorListByRegion :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getVendorPassengerList(String vendorId, String roleId, List<String> assignedRegionList) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModels = userProfileDao.getVendorPassengerList(vendorId, roleId, assignedRegionList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorPassengerList :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public void updateVendorDriverSubscriptionAppliedInBookingFlowFlag() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileDao.updateVendorDriverSubscriptionAppliedInBookingFlowFlag(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorDriverSubscriptionAppliedInBookingFlowFlag :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<UserProfileModel> getUserDriverSubscriptionByUserId(String userId, int start, int length, String searchKey) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModels = userProfileDao.getUserDriverSubscriptionByUserId(userId, start, length, searchKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDriverSubscriptionByUserId :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getVendorBrandList(String serviceId, String categoryId, int start, int length, String searchKey, String latitude, String longitude, String latAndLong, String loggedInUserId, String currentDayOfWeekValue, String isVendorStoreSubscribed,
				String considerDistance, String multicityCityRegionId) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			String distance = GeoLocationUtil.getDistanceQuery(latitude, longitude, GeoLocationUtil.STORE_ADDRESS_GEOLOCATION);
			userProfileModels = userProfileDao.getVendorBrandList(serviceId, categoryId, start, length, searchKey, distance, latAndLong, loggedInUserId, currentDayOfWeekValue, isVendorStoreSubscribed, considerDistance, multicityCityRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorBrandList :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getDriverListForSearch(String loggedInuserId, String roleId, String searchKey, int start, int length) {

		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModels = userProfileDao.getDriverListForSearch(loggedInuserId, roleId, searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getVendorListByServiceTypeId(List<String> assignedRegionList, List<String> serviceTypeId, String order) {

		List<UserProfileModel> userProfileModels = new ArrayList<>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("serviceTypeId", serviceTypeId);
		inputMap.put("roleId", UserRoles.VENDOR_ROLE_ID);
		inputMap.put("order", order);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModels = userProfileDao.getVendorListByServiceTypeId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorListByServiceTypeId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public static List<UserProfileModel> getUserDetailsForGorupOfIds(List<String> userIds) {

		List<UserProfileModel> userProfileModels = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileModels = userProfileDao.getUserDetailsForGorupOfIds(userIds);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsForGorupOfIds :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userProfileModels;
	}

	public void updateVendorMonthlySubscriptionHistoryId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileDao.updateVendorMonthlySubscriptionHistoryId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorMonthlySubscriptionHistoryId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateVendorMonthlySubscriptionParameters() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileDao.updateVendorMonthlySubscriptionParameters(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorMonthlySubscriptionParameters :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateVendorMonthlySubscriptionScriptParametersExistingVendors() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileDao.updateVendorMonthlySubscriptionScriptParametersExistingVendors(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorMonthlySubscriptionScriptParametersExistingVendors :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<UserProfileModel> getVendorListForVendorSubscriptionExpiry(String roleId, long currentTime, long next3Days) {

		List<UserProfileModel> usersList = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			usersList = userProfileDao.getVendorListForVendorSubscriptionExpiry(roleId, currentTime, next3Days);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorListForVendorSubscriptionExpiry : ", t);
		} finally {
			session.close();
		}

		return usersList;
	}

	public static void updateVendorSubscriptionAccountExpired(List<UserProfileModel> updateAccountToExpiredList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			userProfileDao.updateVendorSubscriptionAccountExpired(updateAccountToExpiredList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorSubscriptionAccountExpired :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public UserCreditCardModel getCreditCardDetails() {
		return creditCardDetails;
	}

	public void setCreditCardDetails(UserCreditCardModel creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public long getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(long joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getMailAddressLineOne() {
		return mailAddressLineOne;
	}

	public void setMailAddressLineOne(String mailAddressLineOne) {
		this.mailAddressLineOne = mailAddressLineOne;
	}

	public String getMailAddressLineTwo() {
		return mailAddressLineTwo;
	}

	public void setMailAddressLineTwo(String mailAddressLineTwo) {
		this.mailAddressLineTwo = mailAddressLineTwo;
	}

	public String getMailCountryId() {
		return mailCountryId;
	}

	public void setMailCountryId(String mailCountryId) {
		this.mailCountryId = mailCountryId;
	}

	public String getMailStateId() {
		return mailStateId;
	}

	public void setMailStateId(String mailStateId) {
		this.mailStateId = mailStateId;
	}

	public String getMailCityId() {
		return mailCityId;
	}

	public void setMailCityId(String mailCityId) {
		this.mailCityId = mailCityId;
	}

	public String getBillCountryId() {
		return billCountryId;
	}

	public void setBillCountryId(String billCountryId) {
		this.billCountryId = billCountryId;
	}

	public String getBillStateId() {
		return billStateId;
	}

	public void setBillStateId(String billStateId) {
		this.billStateId = billStateId;
	}

	public String getBillCityId() {
		return billCityId;
	}

	public void setBillCityId(String billCityId) {
		this.billCityId = billCityId;
	}

	public String getMailZipCode() {
		return mailZipCode;
	}

	public void setMailZipCode(String mailZipCode) {
		this.mailZipCode = mailZipCode;
	}

	public String getBillAddressLineOne() {
		return billAddressLineOne;
	}

	public void setBillAddressLineOne(String billAddressLineOne) {
		this.billAddressLineOne = billAddressLineOne;
	}

	public String getBillAddressLineTwo() {
		return billAddressLineTwo;
	}

	public void setBillAddressLineTwo(String billAddressLineTwo) {
		this.billAddressLineTwo = billAddressLineTwo;
	}

	public String getBillZipCode() {
		return billZipCode;
	}

	public void setBillZipCode(String billZipCode) {
		this.billZipCode = billZipCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getApiSessionKey() {
		return apiSessionKey;
	}

	public void setApiSessionKey(String apiSessionKey) {
		this.apiSessionKey = apiSessionKey;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isNotificationStatus() {
		return isNotificationStatus;
	}

	public void setNotificationStatus(boolean isNotificationStatus) {
		this.isNotificationStatus = isNotificationStatus;
	}

	public boolean isOnDuty() {
		return isOnDuty;
	}

	public void setOnDuty(boolean isOnDuty) {
		this.isOnDuty = isOnDuty;
	}

	public long getTip() {
		return tip;
	}

	public void setTip(long tip) {
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

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public boolean isSameAsMailing() {
		return isSameAsMailing;
	}

	public void setSameAsMailing(boolean isSameAsMailing) {
		this.isSameAsMailing = isSameAsMailing;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
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

	public String getDriverReferralCode() {
		return driverReferralCode;
	}

	public void setDriverReferralCode(String driverReferralCode) {
		this.driverReferralCode = driverReferralCode;
	}

	public long getRideLaterVisitedTime() {
		return rideLaterVisitedTime;
	}

	public void setRideLaterVisitedTime(long rideLaterVisitedTime) {
		this.rideLaterVisitedTime = rideLaterVisitedTime;
	}

	@Override
	public String toString() {
		return "UserProfileModel [userInfoId=" + userInfoId + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNo=" + phoneNo + ", phoneNoCode=" + phoneNoCode + ", userId=" + userId + ", roleId=" + roleId + ", userRole=" + userRole + "]";
	}

	public boolean isApprovelStatus() {
		return isApprovelStatus;
	}

	public void setApprovelStatus(boolean isApprovelStatus) {
		this.isApprovelStatus = isApprovelStatus;
	}

	public double getMaximumMarkupFare() {
		return maximumMarkupFare;
	}

	public void setMaximumMarkupFare(double maximumMarkupFare) {
		this.maximumMarkupFare = maximumMarkupFare;
	}

	public boolean isVendorDriverSubscriptionAppliedInBookingFlow() {
		return isVendorDriverSubscriptionAppliedInBookingFlow;
	}

	public void setVendorDriverSubscriptionAppliedInBookingFlow(boolean isVendorDriverSubscriptionAppliedInBookingFlow) {
		this.isVendorDriverSubscriptionAppliedInBookingFlow = isVendorDriverSubscriptionAppliedInBookingFlow;
	}

	public String getVendorBrandName() {
		return vendorBrandName;
	}

	public void setVendorBrandName(String vendorBrandName) {
		this.vendorBrandName = vendorBrandName;
	}

	public String getVendorBrandImage() {
		return vendorBrandImage;
	}

	public void setVendorBrandImage(String vendorBrandImage) {
		this.vendorBrandImage = vendorBrandImage;
	}

	public String getVendorBrandSearchKeywords() {
		return vendorBrandSearchKeywords;
	}

	public void setVendorBrandSearchKeywords(String vendorBrandSearchKeywords) {
		this.vendorBrandSearchKeywords = vendorBrandSearchKeywords;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public boolean isVendorStoreSubscribed() {
		return isVendorStoreSubscribed;
	}

	public void setVendorStoreSubscribed(boolean isVendorStoreSubscribed) {
		this.isVendorStoreSubscribed = isVendorStoreSubscribed;
	}

	public boolean isDriverSubscribed() {
		return isDriverSubscribed;
	}

	public void setDriverSubscribed(boolean isDriverSubscribed) {
		this.isDriverSubscribed = isDriverSubscribed;
	}

	public int getDriverPriorityNumber() {
		return driverPriorityNumber;
	}

	public void setDriverPriorityNumber(int driverPriorityNumber) {
		this.driverPriorityNumber = driverPriorityNumber;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreAddressLat() {
		return storeAddressLat;
	}

	public void setStoreAddressLat(String storeAddressLat) {
		this.storeAddressLat = storeAddressLat;
	}

	public String getStoreAddressLng() {
		return storeAddressLng;
	}

	public void setStoreAddressLng(String storeAddressLng) {
		this.storeAddressLng = storeAddressLng;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public long getDateOpeningMorningHours() {
		return dateOpeningMorningHours;
	}

	public void setDateOpeningMorningHours(long dateOpeningMorningHours) {
		this.dateOpeningMorningHours = dateOpeningMorningHours;
	}

	public long getDateClosingMorningHours() {
		return dateClosingMorningHours;
	}

	public void setDateClosingMorningHours(long dateClosingMorningHours) {
		this.dateClosingMorningHours = dateClosingMorningHours;
	}

	public long getDateOpeningEveningHours() {
		return dateOpeningEveningHours;
	}

	public void setDateOpeningEveningHours(long dateOpeningEveningHours) {
		this.dateOpeningEveningHours = dateOpeningEveningHours;
	}

	public long getDateClosingEveningHours() {
		return dateClosingEveningHours;
	}

	public void setDateClosingEveningHours(long dateClosingEveningHours) {
		this.dateClosingEveningHours = dateClosingEveningHours;
	}

	public boolean isClosedToday() {
		return isClosedToday;
	}

	public void setClosedToday(boolean isClosedToday) {
		this.isClosedToday = isClosedToday;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public long getOpeningMorningHours() {
		return openingMorningHours;
	}

	public void setOpeningMorningHours(long openingMorningHours) {
		this.openingMorningHours = openingMorningHours;
	}

	public long getClosingMorningHours() {
		return closingMorningHours;
	}

	public void setClosingMorningHours(long closingMorningHours) {
		this.closingMorningHours = closingMorningHours;
	}

	public long getOpeningEveningHours() {
		return openingEveningHours;
	}

	public void setOpeningEveningHours(long openingEveningHours) {
		this.openingEveningHours = openingEveningHours;
	}

	public long getClosingEveningHours() {
		return closingEveningHours;
	}

	public void setClosingEveningHours(long closingEveningHours) {
		this.closingEveningHours = closingEveningHours;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreImage() {
		return storeImage;
	}

	public void setStoreImage(String storeImage) {
		this.storeImage = storeImage;
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

	public boolean isSelfDeliveryWithinXKm() {
		return isSelfDeliveryWithinXKm;
	}

	public void setSelfDeliveryWithinXKm(boolean isSelfDeliveryWithinXKm) {
		this.isSelfDeliveryWithinXKm = isSelfDeliveryWithinXKm;
	}

	public double getSelfDeliveryFee() {
		return selfDeliveryFee;
	}

	public void setSelfDeliveryFee(double selfDeliveryFee) {
		this.selfDeliveryFee = selfDeliveryFee;
	}
	
	public void addMartUser() {
		
		int status = -1;
		String userId = UUIDGenerator.generateUUID();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);
		UserDao userDao = session.getMapper(UserDao.class);
		
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;

		try {

			UserModel userModel = new UserModel();

			userModel.setUserId(userId);
			userModel.setEmail(this.getEmail());
			userModel.setCreatedAt(this.createdAt);
			userModel.setUpdatedAt(this.updatedAt);
			userModel.setCreatedBy(userId);
			userModel.setUpdatedBy(userId);
			userModel.setRoleId(this.getRoleId());
			userModel.setApprovelStatus(true);
			userModel.setPhoneNo(this.getPhoneNo());
			userModel.setVerificationCode(this.getVerificationCode());

			status = userDao.addMartUser(userModel);

			if (status > 0) {
				
				UserLoginOtpModel userLoginOtpModel = new UserLoginOtpModel();
				userLoginOtpModel.setUserId(userModel.getUserId());
				userLoginOtpModel.setRoleId(userModel.getRoleId());
				userLoginOtpModel.setVerificationCode(this.getVerificationCode());
				userLoginOtpModel.addVerificationCode(userModel.getUserId());
				this.setCreatedAt(this.createdAt);
				this.setUpdatedAt(this.updatedAt);
				this.setCreatedBy(userId);
				this.setUpdatedBy(userId);

				this.setUserInfoId(UUIDGenerator.generateUUID());
				this.setUserId(userId);

				status = userProfileDao.insertMartUser(this);
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}
	
	public static List<UserProfileModel> getVendorBrandStoresList(String vendorId,String serviceId, String latitude, String longitude, String latAndLong, String loggedInUserId, String currentDayOfWeekValue, boolean isVendorStoreSubscribed, String considerDistance, String multicityCityRegionId) {
		
		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			String distance = GeoLocationUtil.getDistanceQuery(latitude, longitude, GeoLocationUtil.STORE_ADDRESS_GEOLOCATION);
			
			userProfileModels = userProfileDao.getVendorBrandStoresList(vendorId, serviceId, distance, latAndLong, loggedInUserId, currentDayOfWeekValue, isVendorStoreSubscribed, considerDistance, multicityCityRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorBrandStoresList :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}

	public String getPhonepeMerchantId() {
		return phonepeMerchantId;
	}

	public void setPhonepeMerchantId(String phonepeMerchantId) {
		this.phonepeMerchantId = phonepeMerchantId;
	}

	public String getPhonepeMerchantName() {
		return phonepeMerchantName;
	}

	public void setPhonepeMerchantName(String phonepeMerchantName) {
		this.phonepeMerchantName = phonepeMerchantName;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public String getSaltIndex() {
		return saltIndex;
	}

	public void setSaltIndex(String saltIndex) {
		this.saltIndex = saltIndex;
	}

	public long getDob() {
		return dob;
	}

	public void setDob(long dob) {
		this.dob = dob;
	}

	public static List<UserProfileModel> getVendorStoresListForApiSearch(int start, int length, String searchKey, String latitude, String longitude, String latAndLong, String loggedInUserId, String currentDayOfWeekValue, String isVendorStoreSubscribed, String considerDistance,
				String multicityCityRegionId) {
		
		List<UserProfileModel> userProfileModels = new ArrayList<UserProfileModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {
			String distance = GeoLocationUtil.getDistanceQuery(latitude, longitude, GeoLocationUtil.STORE_ADDRESS_GEOLOCATION);
			userProfileModels = userProfileDao.getVendorStoresListForApiSearch(start, length, searchKey, distance, latAndLong, loggedInUserId, currentDayOfWeekValue, isVendorStoreSubscribed, considerDistance, multicityCityRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoresListForApiSearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userProfileModels;
	}
}