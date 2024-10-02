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
import com.webapp.daos.DriverSubscriptionPackageHistoryDao;

public class DriverSubscriptionPackageHistoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverSubscriptionPackageHistoryModel.class);

	private String driverSubscriptionPackageHistoryId;
	private String driverId;
	private String vendorId;
	private String subscriptionPackageId;
	private String paymentMode;
	private String packageName;
	private int durationDays;
	private double price;
	private long packageStartTime;
	private long packageEndTime;
	private boolean isActive;
	private boolean isCurrentPackage;
	private String carTypeId;

	private String driverName;
	private String vendorName;
	private String orderId;
	private String carType;

	public String addDriverSubscriptionPackageHistory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		this.driverSubscriptionPackageHistoryId = UUIDGenerator.generateUUID();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = userId;
		this.isActive = true;

		try {
			driverSubscriptionPackageHistoryDao.addDriverSubscriptionPackageHistory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addDriverSubscriptionPackageHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.driverSubscriptionPackageHistoryId;
	}

	public static List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionPackageHistoryList(String driverId, int start, int length) {

		List<DriverSubscriptionPackageHistoryModel> list = new ArrayList<DriverSubscriptionPackageHistoryModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			list = driverSubscriptionPackageHistoryDao.getDriverSubscriptionPackageHistoryList(driverId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionPackageHistoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionPackageHistoryForStatus(String driverId, String vendorId) {

		List<DriverSubscriptionPackageHistoryModel> list = new ArrayList<DriverSubscriptionPackageHistoryModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			list = driverSubscriptionPackageHistoryDao.getDriverSubscriptionPackageHistoryForStatus(driverId, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionPackageHistoryForStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionHistoryByPackageIdDriverId(String subscriptionPackageId, String driverId, String vendorId) {

		List<DriverSubscriptionPackageHistoryModel> list = new ArrayList<DriverSubscriptionPackageHistoryModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			list = driverSubscriptionPackageHistoryDao.getDriverSubscriptionHistoryByPackageIdDriverId(subscriptionPackageId, driverId, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionHistoryByPackageIdDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static int getNumberOfPackages(String driverId, String vendorId, long temp) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			count = driverSubscriptionPackageHistoryDao.getNumberOfPackages(driverId, vendorId, temp);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNumberOfPackages : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateExistingPackagesAsNotCurrent() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			driverSubscriptionPackageHistoryDao.updateExistingPackagesAsNotCurrent(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateExistingPackagesAsNotCurrent : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionPackageHistoryForSearch(int start, int length, String globalSearchString, long startDatelong, long endDatelong, String[] vendorIds, String driverId) {

		List<DriverSubscriptionPackageHistoryModel> list = new ArrayList<DriverSubscriptionPackageHistoryModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("vendorIds", vendorIds);
		inputMap.put("driverId", driverId);

		try {
			list = driverSubscriptionPackageHistoryDao.getDriverSubscriptionPackageHistoryForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionPackageHistoryForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static int getDriverSubscriptionPackageHistoryCount(long startDatelong, long endDatelong, String[] vendorIds, String driverId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("vendorIds", vendorIds);
		inputMap.put("driverId", driverId);

		try {
			count = driverSubscriptionPackageHistoryDao.getDriverSubscriptionPackageHistoryCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionPackageHistoryCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static DriverSubscriptionPackageHistoryModel getLatestDriverSubscriptionPackageHistory(String driverId) {

		DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel = new DriverSubscriptionPackageHistoryModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			driverSubscriptionPackageHistoryModel = driverSubscriptionPackageHistoryDao.getLatestDriverSubscriptionPackageHistory(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLatestDriverSubscriptionPackageHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverSubscriptionPackageHistoryModel;
	}

	public String getDriverSubscriptionPackageHistoryId() {
		return driverSubscriptionPackageHistoryId;
	}

	public void setDriverSubscriptionPackageHistoryId(String driverSubscriptionPackageHistoryId) {
		this.driverSubscriptionPackageHistoryId = driverSubscriptionPackageHistoryId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getSubscriptionPackageId() {
		return subscriptionPackageId;
	}

	public void setSubscriptionPackageId(String subscriptionPackageId) {
		this.subscriptionPackageId = subscriptionPackageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getPackageStartTime() {
		return packageStartTime;
	}

	public void setPackageStartTime(long packageStartTime) {
		this.packageStartTime = packageStartTime;
	}

	public long getPackageEndTime() {
		return packageEndTime;
	}

	public void setPackageEndTime(long packageEndTime) {
		this.packageEndTime = packageEndTime;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isCurrentPackage() {
		return isCurrentPackage;
	}

	public void setCurrentPackage(boolean isCurrentPackage) {
		this.isCurrentPackage = isCurrentPackage;
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

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public static DriverSubscriptionPackageHistoryModel getCurrentPackageByDriverId(String driverId, long currentTime) {
		
		DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel = new DriverSubscriptionPackageHistoryModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionPackageHistoryDao driverSubscriptionPackageHistoryDao = session.getMapper(DriverSubscriptionPackageHistoryDao.class);

		try {
			driverSubscriptionPackageHistoryModel = driverSubscriptionPackageHistoryDao.getCurrentPackageByDriverId(driverId, currentTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrentPackageByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverSubscriptionPackageHistoryModel;
	}
}