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
import com.webapp.daos.SubscriptionPackageDao;

public class SubscriptionPackageModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(SubscriptionPackageModel.class);

	private String subscriptionPackageId;
	private String packageName;
	private int durationDays;
	private double price;
	private String carTypeId;
	private boolean isActive;
	private boolean isDeleted;
	private boolean isPermanentDeleted;
	private String shortSubscriptionId;
	
	private String carType;

	public static List<SubscriptionPackageModel> getAllActiveSubscriptionPackagesList(int start, int length, String carTypeId) {

		List<SubscriptionPackageModel> subscriptionPackageList = new ArrayList<SubscriptionPackageModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			subscriptionPackageList = subscriptionPackageDao.getAllActiveSubscriptionPackagesList(start, length, carTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllActiveSubscriptionPackagesList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return subscriptionPackageList;
	}
	
	public static List<SubscriptionPackageModel> getAllActiveSubscriptionPackagesListDriverCarTypeWise(int start, int length, String driverId) {

		List<SubscriptionPackageModel> subscriptionPackageList = new ArrayList<SubscriptionPackageModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			subscriptionPackageList = subscriptionPackageDao.getAllActiveSubscriptionPackagesListDriverCarTypeWise(start, length, driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllActiveSubscriptionPackagesListDriverCarTypeWise : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return subscriptionPackageList;
	}

	public static int getSubscriptionPackageCount() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			count = subscriptionPackageDao.getSubscriptionPackageCount();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getSubscriptionPackageCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<SubscriptionPackageModel> getSubscriptionPackageListForSearch(int start, int length, String globalSearchString) {

		List<SubscriptionPackageModel> subscriptionPackageList = new ArrayList<SubscriptionPackageModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("globalSearchString", globalSearchString);

		try {
			subscriptionPackageList = subscriptionPackageDao.getSubscriptionPackageListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getSubscriptionPackageListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return subscriptionPackageList;
	}

	public void deleteSubscriptionPackage(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;
			subscriptionPackageDao.deleteSubscriptionPackage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteSubscriptionPackage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void activateDeactivateSubscriptionPackage(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;
			subscriptionPackageDao.activateDeactivateSubscriptionPackage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateDeactivateSubscriptionPackage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void addSubscriptionPackage(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		this.subscriptionPackageId = UUIDGenerator.generateUUID();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = userId;
		this.isActive = true;
		this.isDeleted = false;
		this.isPermanentDeleted = false;

		try {
			subscriptionPackageDao.addSubscriptionPackage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addSubscriptionPackage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static boolean isSubscriptionPackageExists(String packageName, String subscriptionPackageId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			status = subscriptionPackageDao.isSubscriptionPackageExists(packageName, subscriptionPackageId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isSubscriptionPackageExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static SubscriptionPackageModel getSubscriptionPackageDetailsById(String subscriptionPackageId) {

		SubscriptionPackageModel subscriptionPackageModel = new SubscriptionPackageModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		try {
			subscriptionPackageModel = subscriptionPackageDao.getSubscriptionPackageDetailsById(subscriptionPackageId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getSubscriptionPackageDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return subscriptionPackageModel;
	}

	public void updateSubscriptionPackage(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SubscriptionPackageDao subscriptionPackageDao = session.getMapper(SubscriptionPackageDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			subscriptionPackageDao.updateSubscriptionPackage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateSubscriptionPackage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
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

	public boolean isPermanentDeleted() {
		return isPermanentDeleted;
	}

	public void setPermanentDeleted(boolean isPermanentDeleted) {
		this.isPermanentDeleted = isPermanentDeleted;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getShortSubscriptionId() {
		return shortSubscriptionId;
	}

	public void setShortSubscriptionId(String shortSubscriptionId) {
		this.shortSubscriptionId = shortSubscriptionId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
}