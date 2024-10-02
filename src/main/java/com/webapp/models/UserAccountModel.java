package com.webapp.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.UserAccountDao;

public class UserAccountModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserAccountModel.class);

	private String userAccountId;

	private String userId;

	private double currentBalance;

	private double holdBalance;

	private double approvedBalance;

	private double totalBalance;

	private boolean isActive;

	private String firstName;

	private String lastName;

	private String phoneNoCode;

	private String phoneNo;

	private String email;

	private boolean isVendorDriverSubscriptionAppliedInBookingFlow;

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		UserAccountModel.logger = logger;
	}

	public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getHoldBalance() {
		return holdBalance;
	}

	public void setHoldBalance(double holdBalance) {
		this.holdBalance = holdBalance;
	}

	public double getApprovedBalance() {
		return approvedBalance;
	}

	public void setApprovedBalance(double approvedBalance) {
		this.approvedBalance = approvedBalance;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVendorDriverSubscriptionAppliedInBookingFlow() {
		return isVendorDriverSubscriptionAppliedInBookingFlow;
	}

	public void setVendorDriverSubscriptionAppliedInBookingFlow(boolean isVendorDriverSubscriptionAppliedInBookingFlow) {
		this.isVendorDriverSubscriptionAppliedInBookingFlow = isVendorDriverSubscriptionAppliedInBookingFlow;
	}

	public String insertAccountBalanceDetails(String loggedInUserId) {

		int addStatus = 0;

		this.userAccountId = UUIDGenerator.generateUUID();
		this.isActive = true;
		this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = loggedInUserId;
		this.updatedAt = this.createdAt;
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			addStatus = userAccountDao.insertAccountBalanceDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertAccountBalanceDetails: ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (addStatus > 0) {
			return userAccountId;
		} else {
			return null;
		}
	}

	public static int insertAccountBalanceDetailsBatch(List<UserAccountModel> userAccountModelList) throws IOException {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			status = userAccountDao.insertAccountBalanceDetailsBatch(userAccountModelList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertAccountBalanceDetailsBatch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int updateAccountBalanceDetails(String loggedInUserId) {

		int updateStatus = 0;

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			updateStatus = userAccountDao.updateAccountBalanceDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAccountBalanceDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}

	public static void createUserAccountBalance(String loggedInUserId, String userId) {
		UserAccountModel userAccountModel = new UserAccountModel();
		userAccountModel.setUserId(userId);
		userAccountModel.setCurrentBalance(0);
		userAccountModel.setHoldBalance(0);
		userAccountModel.setApprovedBalance(0);
		userAccountModel.setTotalBalance(0);

		userAccountModel.insertAccountBalanceDetails(loggedInUserId);
	}

	public static void updateUserAccountAndCreateLog(String userId, double amount, String transactionType, String description, String remark, String transactionBy, boolean isAccountRecharge) {

		UserAccountModel userPreviousAccount = getAccountBalanceDetailsByUserId(userId.trim());

		String userAccountId = "";

		UserAccountModel userAccountModel = new UserAccountModel();
		userAccountModel.setUserId(userId);

		if (userPreviousAccount == null) {

			if (ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(transactionType)) {
				userAccountModel.setCurrentBalance(0 - amount);
			} else if (ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT.equalsIgnoreCase(transactionType)) {
				userAccountModel.setCurrentBalance(amount);
			} else {
				userAccountModel.setCurrentBalance(0);
			}

			userAccountModel.setHoldBalance(0);
			userAccountModel.setApprovedBalance(0);
			userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());

			userAccountId = userAccountModel.insertAccountBalanceDetails(userId);

		} else {

			userAccountId = userPreviousAccount.getUserAccountId();

			userAccountModel.setUserAccountId(userAccountId);

			if (ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(transactionType)) {
				userAccountModel.setCurrentBalance(userPreviousAccount.getCurrentBalance() - amount);
			} else if (ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT.equalsIgnoreCase(transactionType)) {
				userAccountModel.setCurrentBalance(userPreviousAccount.getCurrentBalance() + amount);
			} else {
				userAccountModel.setCurrentBalance(userPreviousAccount.getCurrentBalance());
			}

			userAccountModel.setHoldBalance(userPreviousAccount.getHoldBalance());
			userAccountModel.setApprovedBalance(userPreviousAccount.getApprovedBalance());
			userAccountModel.setTotalBalance(userAccountModel.getCurrentBalance() + userAccountModel.getHoldBalance() + userAccountModel.getApprovedBalance());

			userAccountModel.updateAccountBalanceDetails(userId);
		}

		UserAccountLogsModel userAccountLogsModel = new UserAccountLogsModel();
		userAccountLogsModel.setUserAccountId(userAccountId);
		userAccountLogsModel.setUserId(userId);
		userAccountLogsModel.setDescription(description);
		userAccountLogsModel.setRemark(remark);

		if (ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(transactionType)) {
			userAccountLogsModel.setCreditedAmount(0);
			userAccountLogsModel.setDebitedAmount(amount);
		} else if (ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT.equalsIgnoreCase(transactionType)) {
			userAccountLogsModel.setCreditedAmount(amount);
			userAccountLogsModel.setDebitedAmount(0);
		} else {
			userAccountLogsModel.setCreditedAmount(0);
			userAccountLogsModel.setDebitedAmount(0);
		}

		userAccountLogsModel.setTransactionType(transactionType);
		userAccountLogsModel.setTransactionStatus(transactionType);

		userAccountLogsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
		userAccountLogsModel.setHoldBalance(userAccountModel.getHoldBalance());
		userAccountLogsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
		userAccountLogsModel.setTotalBalance(userAccountModel.getTotalBalance());

		userAccountLogsModel.setTransactionBy(transactionBy);
		userAccountLogsModel.setAccountRecharge(isAccountRecharge);

		String userAccountLogId = userAccountLogsModel.insertUserAccountLog(userId);

		if (userAccountLogId != null) {

			// Add account log details
			UserAccountLogDetailsModel userAccountLogDetailsModel = new UserAccountLogDetailsModel();
			userAccountLogDetailsModel.setUserAccountLogId(userAccountLogId);
			userAccountLogDetailsModel.setUserAccountId(userAccountId);
			userAccountLogDetailsModel.setUserId(userId);
			userAccountLogDetailsModel.setDescription(description);
			userAccountLogDetailsModel.setRemark(remark);

			if (ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(transactionType)) {
				userAccountLogDetailsModel.setCreditedAmount(0);
				userAccountLogDetailsModel.setDebitedAmount(amount);
			} else if (ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT.equalsIgnoreCase(transactionType)) {
				userAccountLogDetailsModel.setCreditedAmount(amount);
				userAccountLogDetailsModel.setDebitedAmount(0);
			} else {
				userAccountLogDetailsModel.setCreditedAmount(0);
				userAccountLogDetailsModel.setDebitedAmount(0);
			}

			userAccountLogDetailsModel.setTransactionType(transactionType);
			userAccountLogDetailsModel.setTransactionStatus(transactionType);
			userAccountLogDetailsModel.setCurrentBalance(userAccountModel.getCurrentBalance());
			userAccountLogDetailsModel.setHoldBalance(userAccountModel.getHoldBalance());
			userAccountLogDetailsModel.setApprovedBalance(userAccountModel.getApprovedBalance());
			userAccountLogDetailsModel.setTotalBalance(userAccountModel.getTotalBalance());

			userAccountLogDetailsModel.insertUserAccountLogDetails(userId);
		}
	}

	public static UserAccountModel getAccountBalanceDetailsById(String userAccountId) {

		UserAccountModel userAccountModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			userAccountModel = userAccountDao.getAccountBalanceDetailsById(userAccountId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAccountBalanceDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountModel;
	}

	public static UserAccountModel getAccountBalanceDetailsByUserId(String userId) {

		UserAccountModel userAccountModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			userAccountModel = userAccountDao.getAccountBalanceDetailsByUserId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAccountBalanceDetailsByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountModel;
	}

	public static int getTotalUserAccountCount(String roleId, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			count = userAccountDao.getTotalUserAccountCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalUserAccountCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserAccountModel> getUserAccountListForSearch(int start, int length, String order, String globalSearchString, List<String> assignedRegionList, String roleId) {

		List<UserAccountModel> userAccountModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("roleId", roleId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			userAccountModelList = userAccountDao.getUserAccountListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountModelList;
	}

	public static int getFilteredUserAccountCount(String globalSearchString, List<String> assignedRegionList, String roleId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("roleId", roleId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			count = userAccountDao.getFilteredUserAccountCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFilteredUserAccountCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static UserAccountModel getUserWithAccountDetails(String userId) {

		UserAccountModel userAccountModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			userAccountModel = userAccountDao.getUserWithAccountDetails(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserWithAccountDetails :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountModel;
	}

	public static int getTotalVendorUserAccountCount(String roleId, List<String> assignedRegionList, String vendorId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			count = userAccountDao.getTotalVendorUserAccountCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalVendorUserAccountCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserAccountModel> getVendorUserAccountListForSearch(int start, int length, String order, String globalSearchString, List<String> assignedRegionList, String roleId, String vendorId) {

		List<UserAccountModel> userAccountModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("roleId", roleId);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			userAccountModelList = userAccountDao.getVendorUserAccountListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorUserAccountListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountModelList;
	}

	public static int getFilteredVendorUserAccountCount(String globalSearchString, List<String> assignedRegionList, String roleId, String vendorId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("roleId", roleId);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			count = userAccountDao.getFilteredVendorUserAccountCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFilteredVendorUserAccountCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserAccountModel> getUserAccountDetailsListForExport(String searchString, List<String> assignedRegionList, String roleId, String vendorId) {

		List<UserAccountModel> userAccountModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("searchString", searchString);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("roleId", roleId);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountDao userAccountDao = session.getMapper(UserAccountDao.class);

		try {
			userAccountModelList = userAccountDao.getUserAccountDetailsListForExport(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountDetailsListForExport :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountModelList;
	}

}