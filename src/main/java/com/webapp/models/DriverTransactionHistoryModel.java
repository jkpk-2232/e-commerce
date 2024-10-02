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
import com.utils.UUIDGenerator;
import com.webapp.daos.DriverTransactionHistoryDao;

public class DriverTransactionHistoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverTransactionHistoryModel.class);

	private String driverTransactionHistoryId;
	private long orderId;
	private String driverId;
	private String vendorId;
	private String paymentTypeId;
	private String transactionId;
	private String transactionType;
	private boolean isDebit;
	private double amount;
	private String status;
	private boolean isDriverSubscriptionExtension;
	private String driverSubscriptionExtensionLogId;

	private String driverName;
	private String vendorName;
	private String paymentType;

	public String addTransactionHistory(String userId) {

		int status = 0;

		this.driverTransactionHistoryId = UUIDGenerator.generateUUID();
		this.createdBy = userId;
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTransactionHistoryDao driverTransactionHistoryDao = session.getMapper(DriverTransactionHistoryDao.class);

		try {
			status = driverTransactionHistoryDao.addTransactionHistory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addTransactionHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {
			return this.driverTransactionHistoryId;
		} else {
			return null;
		}
	}

	public int updateTransactionHistory(String userId) {

		int status = 0;

		this.updatedBy = userId;
		this.updatedAt = DateUtils.nowAsGmtMillisec();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTransactionHistoryDao driverTransactionHistoryDao = session.getMapper(DriverTransactionHistoryDao.class);

		try {
			status = driverTransactionHistoryDao.updateTransactionHistory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTransactionHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<DriverTransactionHistoryModel> getTransactionHistoryList(String userId, int start, int length) {

		List<DriverTransactionHistoryModel> transactionHistoryList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTransactionHistoryDao driverTransactionHistoryDao = session.getMapper(DriverTransactionHistoryDao.class);

		try {
			transactionHistoryList = driverTransactionHistoryDao.getTransactionHistoryList(userId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTransactionHistoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return transactionHistoryList;
	}

	public static int getTotalTransactionCountByUserId(long startDatelong, long endDatelong, String[] vendorIds, String driverId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTransactionHistoryDao driverTransactionHistoryDao = session.getMapper(DriverTransactionHistoryDao.class);

		try {
			count = driverTransactionHistoryDao.getTotalTransactionCountByUserId(startDatelong, endDatelong, vendorIds, driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalTransactionCountByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<DriverTransactionHistoryModel> getTransactionListForSearch(int start, int length, String globalSearchString, long startDatelong, long endDatelong, String[] vendorIds, String driverId) {

		List<DriverTransactionHistoryModel> list = new ArrayList<DriverTransactionHistoryModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTransactionHistoryDao driverTransactionHistoryDao = session.getMapper(DriverTransactionHistoryDao.class);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("vendorIds", vendorIds);
		inputMap.put("driverId", driverId);

		try {
			list = driverTransactionHistoryDao.getTransactionListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTransactionListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static DriverTransactionHistoryModel getTrasactionHistoryByTrasactionId(String transactionId) {

		DriverTransactionHistoryModel transactionHistory = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTransactionHistoryDao driverTransactionHistoryDao = session.getMapper(DriverTransactionHistoryDao.class);

		try {
			transactionHistory = driverTransactionHistoryDao.getTrasactionHistoryByTrasactionId(transactionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTrasactionHistoryByTrasactionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return transactionHistory;
	}

	public String getDriverTransactionHistoryId() {
		return driverTransactionHistoryId;
	}

	public void setDriverTransactionHistoryId(String driverTransactionHistoryId) {
		this.driverTransactionHistoryId = driverTransactionHistoryId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public boolean isDebit() {
		return isDebit;
	}

	public void setDebit(boolean isDebit) {
		this.isDebit = isDebit;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public boolean isDriverSubscriptionExtension() {
		return isDriverSubscriptionExtension;
	}

	public void setDriverSubscriptionExtension(boolean isDriverSubscriptionExtension) {
		this.isDriverSubscriptionExtension = isDriverSubscriptionExtension;
	}

	public String getDriverSubscriptionExtensionLogId() {
		return driverSubscriptionExtensionLogId;
	}

	public void setDriverSubscriptionExtensionLogId(String driverSubscriptionExtensionLogId) {
		this.driverSubscriptionExtensionLogId = driverSubscriptionExtensionLogId;
	}
}