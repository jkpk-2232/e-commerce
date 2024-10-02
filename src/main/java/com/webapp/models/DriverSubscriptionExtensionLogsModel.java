package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DriverSubscriptionExtensionLogsDao;

public class DriverSubscriptionExtensionLogsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverSubscriptionExtensionLogsModel.class);

	private String driverSubscriptionExtensionLogId;
	private String status;
	private String multicityCityRegionId;
	private int totalDrivers;
	private int totalCompletedDrivers;
	private int totalFailedDrivers;

	private String createdByName;
	private String regionName;

	public String insertDriverSubscriptionExtensionLogs(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionExtensionLogsDao driverSubscriptionExtensionLogsDao = session.getMapper(DriverSubscriptionExtensionLogsDao.class);

		this.driverSubscriptionExtensionLogId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			driverSubscriptionExtensionLogsDao.insertDriverSubscriptionExtensionLogs(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertDriverSubscriptionExtensionLogs : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.driverSubscriptionExtensionLogId;
	}

	public void updateDriverSubscriptionExtensionLogs(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionExtensionLogsDao driverSubscriptionExtensionLogsDao = session.getMapper(DriverSubscriptionExtensionLogsDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			driverSubscriptionExtensionLogsDao.updateDriverSubscriptionExtensionLogs(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverSubscriptionExtensionLogs : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getDriverSubscriptionExtensionLogsCount(long startDatelong, long endDatelong) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionExtensionLogsDao driverSubscriptionExtensionLogsDao = session.getMapper(DriverSubscriptionExtensionLogsDao.class);

		try {
			count = driverSubscriptionExtensionLogsDao.getDriverSubscriptionExtensionLogsCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionExtensionLogsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<DriverSubscriptionExtensionLogsModel> getDriverSubscriptionExtensionLogsListForSearch(long startDatelong, long endDatelong, int start, int length, String searchKey) {

		List<DriverSubscriptionExtensionLogsModel> driverSubscriptionExtensionLogsList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverSubscriptionExtensionLogsDao driverSubscriptionExtensionLogsDao = session.getMapper(DriverSubscriptionExtensionLogsDao.class);

		try {
			driverSubscriptionExtensionLogsList = driverSubscriptionExtensionLogsDao.getDriverSubscriptionExtensionLogsListForSearch(startDatelong, endDatelong, start, length, searchKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverSubscriptionExtensionLogsListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverSubscriptionExtensionLogsList;
	}

	public String getDriverSubscriptionExtensionLogId() {
		return driverSubscriptionExtensionLogId;
	}

	public void setDriverSubscriptionExtensionLogId(String driverSubscriptionExtensionLogId) {
		this.driverSubscriptionExtensionLogId = driverSubscriptionExtensionLogId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public int getTotalDrivers() {
		return totalDrivers;
	}

	public void setTotalDrivers(int totalDrivers) {
		this.totalDrivers = totalDrivers;
	}

	public int getTotalCompletedDrivers() {
		return totalCompletedDrivers;
	}

	public void setTotalCompletedDrivers(int totalCompletedDrivers) {
		this.totalCompletedDrivers = totalCompletedDrivers;
	}

	public int getTotalFailedDrivers() {
		return totalFailedDrivers;
	}

	public void setTotalFailedDrivers(int totalFailedDrivers) {
		this.totalFailedDrivers = totalFailedDrivers;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
}