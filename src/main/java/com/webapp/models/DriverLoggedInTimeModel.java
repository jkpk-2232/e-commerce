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
import com.webapp.ProjectConstants;
import com.webapp.daos.DriverLoggedInTimeDao;

public class DriverLoggedInTimeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverLoggedInTimeModel.class);

	private String driverLoggedInTimeId;
	private String driverId;
	private long loggedInTime;
	private long dateTime;

	public String getDriverLoggedInTimeId() {
		return driverLoggedInTimeId;
	}

	public void setDriverLoggedInTimeId(String driverLoggedInTimeId) {
		this.driverLoggedInTimeId = driverLoggedInTimeId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public long getLoggedInTime() {
		return loggedInTime;
	}

	public void setLoggedInTime(long loggedInTime) {
		this.loggedInTime = loggedInTime;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public int addDriverLoggedInTimeDetails(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			this.driverLoggedInTimeId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;

			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = loggedInUserId;
			this.updatedBy = loggedInUserId;

			status = driverLoggedInTimeDao.addDriverLoggedInTimeDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addDriverLoggedInTimeDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static int insertDriverLoggedInTimeBatch(List<DriverLoggedInTimeModel> driverLoggedInTimeModelList) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			status = driverLoggedInTimeDao.insertDriverLoggedInTimeBatch(driverLoggedInTimeModelList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertDriverLoggedInTimeBatch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;

	}

	public int updateLoggedInTimeById(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInUserId);

			status = driverLoggedInTimeDao.updateLoggedInTimeById(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateLoggedInTimeById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<DriverLoggedInTimeModel> getLoggedInTimesListByDriverIdAndTime(String driverId, long startTimeInMillies, long endTimeInMillies) {

		List<DriverLoggedInTimeModel> driverLoggedInTimeModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("startTimeInMillies", startTimeInMillies);
		inputMap.put("endTimeInMillies", endTimeInMillies);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			driverLoggedInTimeModelList = driverLoggedInTimeDao.getLoggedInTimesListByDriverIdAndTime(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLoggedInTimesListByDriverIdAndTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverLoggedInTimeModelList;
	}

	public static long getTotalLoggedInTimeByDriverIdandDate(String driverId, long startDatelong, long endDatelong) {

		long totalLoggedInTime = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			totalLoggedInTime = driverLoggedInTimeDao.getTotalLoggedInTimeByDriverIdandDate(inputMap);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalLoggedInTimeByDriverIdandDate : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return totalLoggedInTime;
	}

	public static int getTotalDriverLoggedInTimeLogCount(String driverId, long startDatelong, long endDatelong) {

		int userCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			userCount = driverLoggedInTimeDao.getTotalDriverLoggedInTimeLogCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalDriverLoggedInTimeLogCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static List<DriverLoggedInTimeModel> getDriverLoggedInTimeLogListForSearch(String driverId, int start, int length, String order, long startDatelong, long endDatelong) {

		List<DriverLoggedInTimeModel> driverLoggedInTimeModelList = new ArrayList<DriverLoggedInTimeModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverLoggedInTimeDao driverLoggedInTimeDao = session.getMapper(DriverLoggedInTimeDao.class);

		try {
			driverLoggedInTimeModelList = driverLoggedInTimeDao.getDriverLoggedInTimeLogListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverLoggedInTimeLogListForSearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return driverLoggedInTimeModelList;
	}
}