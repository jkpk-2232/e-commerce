package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.TransactionHistoryDao;

public class TransactionHistoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TransactionHistoryModel.class);

	private String transactionHistoryId;
	private long orderId;
	private String userId;
	private String paymentTypeId;
	private String paymentType;
	private String transactionId;
	private double amount;
	private String status;

	public String getTransactionHistoryId() {
		return transactionHistoryId;
	}

	public void setTransactionHistoryId(String transactionHistoryId) {
		this.transactionHistoryId = transactionHistoryId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	public String addTransactionHistory(String userId) {

		int status = 0;

		this.transactionHistoryId = UUIDGenerator.generateUUID();
		this.createdBy = userId;
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TransactionHistoryDao transactionHistoryDao = session.getMapper(TransactionHistoryDao.class);

		try {
			status = transactionHistoryDao.addTransactionHistory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addTransactionHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {
			return this.transactionHistoryId;
		} else {
			return null;
		}
	}

	public int updateTransactionHistory(String userId) {

		int status = 0;

		this.updatedBy = userId;
		this.updatedAt = DateUtils.nowAsGmtMillisec();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TransactionHistoryDao transactionHistoryDao = session.getMapper(TransactionHistoryDao.class);

		try {
			status = transactionHistoryDao.updateTransactionHistory(this);
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

	public static List<TransactionHistoryModel> getTransactionHistoryList(String userId, int start, int length) {

		List<TransactionHistoryModel> transactionHistoryList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TransactionHistoryDao transactionHistoryDao = session.getMapper(TransactionHistoryDao.class);

		try {
			transactionHistoryList = transactionHistoryDao.getTransactionHistoryList(userId, start, length);
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

	public static int getTotalTransactionCountByUserId(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TransactionHistoryDao transactionHistoryDao = session.getMapper(TransactionHistoryDao.class);

		try {
			count = transactionHistoryDao.getTotalTransactionCountByUserId(userId);
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

	public static TransactionHistoryModel getTrasactionHistoryByTrasactionId(String transactionId) {

		TransactionHistoryModel transactionHistory = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TransactionHistoryDao transactionHistoryDao = session.getMapper(TransactionHistoryDao.class);

		try {
			transactionHistory = transactionHistoryDao.getTrasactionHistoryByTrasactionId(transactionId);
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
}