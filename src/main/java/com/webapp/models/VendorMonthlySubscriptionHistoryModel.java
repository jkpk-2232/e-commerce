package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorMonthlySubscriptionHistoryDao;

public class VendorMonthlySubscriptionHistoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorMonthlySubscriptionHistoryModel.class);

	private String vendorMonthlySubscriptionHistoryId;
	private String vendorMonthlySubscriptionHistorySerialId;
	private String vendorId;
	private double vendorMonthlySubscriptionFee;
	private String paymentType;
	private String transactionId;
	private long startDateTime;
	private long endDateTime;
	private boolean isFreeSubscriptionEntry;
	private boolean isVendorSubscriptionCurrentActive;

	private String vendorName;

	public String insertVendorMonthlySubscriptionHistory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		this.vendorMonthlySubscriptionHistoryId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorMonthlySubscriptionHistoryDao.insertVendorMonthlySubscriptionHistory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorMonthlySubscriptionHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorMonthlySubscriptionHistoryId;
	}

	public static int getVendorMonthlySubscriptionHistoryCount(long startDatelong, long endDatelong, String vendorId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		try {
			count = vendorMonthlySubscriptionHistoryDao.getVendorMonthlySubscriptionHistoryCount(startDatelong, endDatelong, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorMonthlySubscriptionHistoryCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<VendorMonthlySubscriptionHistoryModel> getVendorMonthlySubscriptionHistorySearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String vendorId) {

		List<VendorMonthlySubscriptionHistoryModel> vendorMonthlySubscriptionHistoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		try {
			vendorMonthlySubscriptionHistoryList = vendorMonthlySubscriptionHistoryDao.getVendorMonthlySubscriptionHistorySearch(startDatelong, endDatelong, searchKey, start, length, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorMonthlySubscriptionHistorySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorMonthlySubscriptionHistoryList;
	}

	public static int getVendorMonthlySubscriptionHistorySearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		try {
			count = vendorMonthlySubscriptionHistoryDao.getVendorMonthlySubscriptionHistorySearchCount(startDatelong, endDatelong, searchKey, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorMonthlySubscriptionHistorySearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static VendorMonthlySubscriptionHistoryModel getLastVendorMonthlySubscriptionHistoryEntry(String vendorId) {

		VendorMonthlySubscriptionHistoryModel lastVendorMonthlySubscriptionHistoryEntry = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		try {
			lastVendorMonthlySubscriptionHistoryEntry = vendorMonthlySubscriptionHistoryDao.getLastVendorMonthlySubscriptionHistoryEntry(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLastVendorMonthlySubscriptionHistoryEntry : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return lastVendorMonthlySubscriptionHistoryEntry;
	}

	public void updateVendorSubscriptionCurrentActive(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		try {
			vendorMonthlySubscriptionHistoryDao.updateVendorSubscriptionCurrentActive(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorSubscriptionCurrentActive : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}
	
	public static void updateVendorSubscriptionAccountExpired(List<VendorMonthlySubscriptionHistoryModel> updateVendorMonthlySubscriptionToExpiredList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorMonthlySubscriptionHistoryDao vendorMonthlySubscriptionHistoryDao = session.getMapper(VendorMonthlySubscriptionHistoryDao.class);

		try {
			vendorMonthlySubscriptionHistoryDao.updateVendorSubscriptionAccountExpired(updateVendorMonthlySubscriptionToExpiredList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorSubscriptionAccountExpired : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorMonthlySubscriptionHistoryId() {
		return vendorMonthlySubscriptionHistoryId;
	}

	public void setVendorMonthlySubscriptionHistoryId(String vendorMonthlySubscriptionHistoryId) {
		this.vendorMonthlySubscriptionHistoryId = vendorMonthlySubscriptionHistoryId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public double getVendorMonthlySubscriptionFee() {
		return vendorMonthlySubscriptionFee;
	}

	public void setVendorMonthlySubscriptionFee(double vendorMonthlySubscriptionFee) {
		this.vendorMonthlySubscriptionFee = vendorMonthlySubscriptionFee;
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

	public long getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(long startDateTime) {
		this.startDateTime = startDateTime;
	}

	public long getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(long endDateTime) {
		this.endDateTime = endDateTime;
	}

	public boolean isFreeSubscriptionEntry() {
		return isFreeSubscriptionEntry;
	}

	public void setFreeSubscriptionEntry(boolean isFreeSubscriptionEntry) {
		this.isFreeSubscriptionEntry = isFreeSubscriptionEntry;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorMonthlySubscriptionHistorySerialId() {
		return vendorMonthlySubscriptionHistorySerialId;
	}

	public void setVendorMonthlySubscriptionHistorySerialId(String vendorMonthlySubscriptionHistorySerialId) {
		this.vendorMonthlySubscriptionHistorySerialId = vendorMonthlySubscriptionHistorySerialId;
	}

	public boolean isVendorSubscriptionCurrentActive() {
		return isVendorSubscriptionCurrentActive;
	}

	public void setVendorSubscriptionCurrentActive(boolean isVendorSubscriptionCurrentActive) {
		this.isVendorSubscriptionCurrentActive = isVendorSubscriptionCurrentActive;
	}
}