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
import com.webapp.daos.DriverSubscriberDao;

public class DriverSubscriberModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverSubscriberModel.class);

	private String driverSubscriberId;
	private String driverId;
	private String userId;
	private int priorityNumber;

	private String driverName;
	private String subscriberName;

	public String insertDriverSubscriber(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		this.driverSubscriberId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			driverSubscriberDao.insertDriverSubscriber(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertDriverSubscriber : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.driverSubscriberId;
	}

	public static int getDriverSubscribedBySubsciberIdCount(long startDatelong, long endDatelong, String subscriberUserId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			count = driverSubscriberDao.getDriverSubscribedBySubsciberIdCount(startDatelong, endDatelong, subscriberUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscribedBySubsciberIdCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<DriverSubscriberModel> getDriverSubscribedBySubsciberIdSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String subscriberUserId) {

		List<DriverSubscriberModel> driverSubscriberList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			driverSubscriberList = driverSubscriberDao.getDriverSubscribedBySubsciberIdSearch(startDatelong, endDatelong, searchKey, start, length, subscriberUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscribedBySubsciberIdSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverSubscriberList;
	}

	public static int getDriverSubscribedBySubsciberIdSearchCount(long startDatelong, long endDatelong, String searchKey, String subscriberUserId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			count = driverSubscriberDao.getDriverSubscribedBySubsciberIdSearchCount(startDatelong, endDatelong, searchKey, subscriberUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscribedBySubsciberIdSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static boolean isUserSubscribedToDriver(String driverId, String userId) {

		boolean isUserSubscribedToVendor = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			isUserSubscribedToVendor = driverSubscriberDao.isUserSubscribedToDriver(driverId, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isUserSubscribedToDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isUserSubscribedToVendor;
	}

	public void deleteDriverSubscriber() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			driverSubscriberDao.deleteDriverSubscriber(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteDriverSubscriber : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}
	

	public static int getDriverSubscriptionCount(String driverId, long startTime, long endTime) {

		int driverSubscriptionCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			driverSubscriptionCount = driverSubscriberDao.getDriverSubscriptionCount(driverId, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverSubscriptionCount;
	}

	public String getDriverSubscriberId() {
		return driverSubscriberId;
	}

	public void setDriverSubscriberId(String driverSubscriberId) {
		this.driverSubscriberId = driverSubscriberId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public int getPriorityNumber() {
		return priorityNumber;
	}

	public void setPriorityNumber(int priorityNumber) {
		this.priorityNumber = priorityNumber;
	}

	public static Map<String, Object> getLimitedData(UserProfileModel userProfileModel) {

		Map<String, Object> map = new HashMap<>();

		map.put("driverId", userProfileModel.getUserId());
		map.put("driverName", userProfileModel.getFullName());
		map.put("phoneNo", userProfileModel.getPhoneNo());
		map.put("phoneNoCode", userProfileModel.getPhoneNoCode());
		map.put("email", userProfileModel.getEmail());
		map.put("isDriverSubscribed", userProfileModel.isDriverSubscribed());
		map.put("photoUrl", userProfileModel.getPhotoUrl());
		map.put("priorityNumber", userProfileModel.getDriverPriorityNumber());

		return map;
	}

	public static int getUserSubscribedCountByDriverId(String driverId) {
		
		int driverSubscriptionCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriberDao driverSubscriberDao = session.getMapper(DriverSubscriberDao.class);

		try {
			driverSubscriptionCount = driverSubscriberDao.getUserSubscribedCountByDriverId(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserSubscribedCountByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverSubscriptionCount;
		
	}
}