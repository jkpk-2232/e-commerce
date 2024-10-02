package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.UserAccountLogsDao;

public class UserAccountLogsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserAccountLogsModel.class);

	private String userAccountLogId;

	private String userAccountId;

	private String userId;

	private String description;

	private String remark;

	private double creditedAmount;

	private double debitedAmount;

	private String transactionType;

	private String transactionStatus;

	private double currentBalance;

	private double holdBalance;

	private double approvedBalance;

	private double totalBalance;

	private String transactionBy;

	private boolean isAccountRecharge;

	private String encashRequestId;

	public String getUserAccountLogId() {
		return userAccountLogId;
	}

	public void setUserAccountLogId(String userAccountLogId) {
		this.userAccountLogId = userAccountLogId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getCreditedAmount() {
		return creditedAmount;
	}

	public void setCreditedAmount(double creditedAmount) {
		this.creditedAmount = creditedAmount;
	}

	public double getDebitedAmount() {
		return debitedAmount;
	}

	public void setDebitedAmount(double debitedAmount) {
		this.debitedAmount = debitedAmount;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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

	public String getTransactionBy() {
		return transactionBy;
	}

	public void setTransactionBy(String transactionBy) {
		this.transactionBy = transactionBy;
	}

	public boolean isAccountRecharge() {
		return isAccountRecharge;
	}

	public void setAccountRecharge(boolean isAccountRecharge) {
		this.isAccountRecharge = isAccountRecharge;
	}

	public String getEncashRequestId() {
		return encashRequestId;
	}

	public void setEncashRequestId(String encashRequestId) {
		this.encashRequestId = encashRequestId;
	}

	public String insertUserAccountLog(String loggedInUserId) {

		int addStatus = 0;

		this.userAccountLogId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = loggedInUserId;
		this.updatedAt = this.createdAt;
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			addStatus = userAccountLogsDao.insertUserAccountLog(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertUserAccountLog: ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (addStatus > 0) {
			return this.userAccountLogId;
		} else {
			return null;
		}
	}

	public int updateUserAccountLogByEncashRequestId(String loggedInUserId) {

		int updateStatus = 0;

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			updateStatus = userAccountLogsDao.updateUserAccountLogByEncashRequestId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUserAccountLogByEncashRequestId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}

	public static List<UserAccountLogsModel> getUserAccountLogsListByUserId(String userId, int offset, int length) {

		List<UserAccountLogsModel> userAccountLogsModelList = null;

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("userId", userId);
		inputMap.put("offset", offset);
		inputMap.put("length", length);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			userAccountLogsModelList = userAccountLogsDao.getUserAccountLogsListByUserId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountLogsListByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountLogsModelList;
	}

	public static int getTotalUserAccountLogsCount(String userId, long startDateLong, long endDateLong) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			count = userAccountLogsDao.getTotalUserAccountLogsCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalUserAccountLogsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserAccountLogsModel> getUserAccountLogsListForSearch(int start, int length, String order, String globalSearchString, String userId, long startDateLong, long endDateLong) {

		List<UserAccountLogsModel> userAccountLogsModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("userId", userId);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			userAccountLogsModelList = userAccountLogsDao.getUserAccountLogsListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountLogsListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountLogsModelList;
	}

	public static int getFilteredUserAccountLogsCount(String globalSearchString, String userId, long startDateLong, long endDateLong) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("userId", userId);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			count = userAccountLogsDao.getFilteredUserAccountLogsCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFilteredUserAccountLogsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<UserAccountLogsModel> getUserAccountLogsListForExport(String userId, long startDateLong, long endDateLong, String searchString) {

		List<UserAccountLogsModel> userAccountLogsModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);
		inputMap.put("searchString", searchString);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			userAccountLogsModelList = userAccountLogsDao.getUserAccountLogsListForExport(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountLogsListForExport :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountLogsModelList;
	}

	public static UserAccountLogsModel getUserAccountLogDetailsByEncashRequestId(String encashRequestId) {

		UserAccountLogsModel userAccountLogsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogsDao userAccountLogsDao = session.getMapper(UserAccountLogsDao.class);

		try {
			userAccountLogsModel = userAccountLogsDao.getUserAccountLogDetailsByEncashRequestId(encashRequestId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserAccountLogDetailsByEncashRequestId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userAccountLogsModel;
	}

}