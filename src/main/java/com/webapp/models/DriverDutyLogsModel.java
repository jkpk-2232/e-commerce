package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DriverDutyLogsDao;

public class DriverDutyLogsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverDutyLogsModel.class);

	private String driverDutyStatusLogsId;

	private String driverId;

	private boolean dutyStatus;

	private String latitude;

	private String longitude;

	private String firstName;

	private String lastName;

	public int addDriverDutyLogs() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {

			this.driverDutyStatusLogsId = UUIDGenerator.generateUUID();
			//Commented param was set when calling current function
			//			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = this.driverId;
			this.updatedBy = this.driverId;

			status = driverDutyLogsDao.addDriverDutyLogs(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addDriverDutyLogs : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static int insertDriverDutyLogsBatch(List<DriverDutyLogsModel> driverDutyLogsModelList) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			status = driverDutyLogsDao.insertDriverDutyLogsBatch(driverDutyLogsModelList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertDriverDutyLogsBatch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<DriverDutyLogsModel> getDriverDutyReportAdminListForSearch(String driverId, int start, int length, String order, long startDate, long endDate) {

		List<DriverDutyLogsModel> driverDutyLogsModels = new ArrayList<DriverDutyLogsModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			driverDutyLogsModels = driverDutyLogsDao.getDriverDutyReportAdminListForSearch(driverId, start, length, order, startDate, endDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDutyReportAdminListForSearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return driverDutyLogsModels;
	}

	public static List<DriverDutyLogsModel> getDriverOnOffDutyReportAdminListForSearch(String driverId, int start, int length, String order, long startDate, long endDate, boolean dutyStatus) {

		List<DriverDutyLogsModel> driverDutyLogsModels = new ArrayList<DriverDutyLogsModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			driverDutyLogsModels = driverDutyLogsDao.getDriverOnOffDutyReportAdminListForSearch(driverId, start, length, order, startDate, endDate, dutyStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverOnOffDutyReportAdminListForSearch :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return driverDutyLogsModels;
	}

	public static int getDriverDutyReportAdminListForSearchCount(String driverId, long startDate, long endDate) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			count = driverDutyLogsDao.getDriverDutyReportAdminListForSearchCount(driverId, startDate, endDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDutyReportAdminListForSearchCount :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static int getDriverOnOffDutyReportAdminListForSearchCount(String driverId, long startDate, long endDate, boolean dutyStatus) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			count = driverDutyLogsDao.getDriverOnOffDutyReportAdminListForSearchCount(driverId, startDate, endDate, dutyStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverOnOffDutyReportAdminListForSearchCount :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static int getTotalDutyReportCount(String driverId, long startDatelong, long endDatelong) {
		int userCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			userCount = driverDutyLogsDao.getTotalDutyReportCount(driverId, startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalDutyReportCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCount;
	}

	public static int getTotalOnOffDutyReportCount(String driverId, boolean dutyStatus) {
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			count = driverDutyLogsDao.getTotalOnOffDutyReportCount(driverId, dutyStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalOnOffDutyReportCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static DriverDutyLogsModel getLastDriverDutyLogDetails(String driverId) {

		DriverDutyLogsModel driverDutyLogsModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDutyLogsDao driverDutyLogsDao = session.getMapper(DriverDutyLogsDao.class);

		try {
			driverDutyLogsModel = driverDutyLogsDao.getLastDriverDutyLogDetails(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLastDriverDutyLogDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverDutyLogsModel;
	}

	public boolean isDutyStatus() {
		return dutyStatus;
	}

	public void setDutyStatus(boolean dutyStatus) {
		this.dutyStatus = dutyStatus;
	}

	public String getDriverDutyStatusLogsId() {
		return driverDutyStatusLogsId;
	}

	public void setDriverDutyStatusLogsId(String driverDutyStatusLogsId) {
		this.driverDutyStatusLogsId = driverDutyStatusLogsId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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

}