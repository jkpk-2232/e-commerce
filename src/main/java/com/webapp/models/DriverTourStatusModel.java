package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.DriverTourStatusDao;

public class DriverTourStatusModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverTourStatusModel.class);

	private String driverTourStatusId;
	private String driverId;
	private String status;

	public int createDriverTourStatus() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourStatusDao driverTourStatusDao = session.getMapper(DriverTourStatusDao.class);

		int status = 0;
		try {

			this.setDriverTourStatusId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(this.getCreatedAt());
			status = driverTourStatusDao.createDriverTourStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during createDriverTourStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}

	public static DriverTourStatusModel getDriverTourStatus(String driverId) {

		DriverTourStatusModel driverTourStatusModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourStatusDao driverTourStatusDao = session.getMapper(DriverTourStatusDao.class);

		try {
			driverTourStatusModel = driverTourStatusDao.getDriverTourStatus(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllTourStatusByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverTourStatusModel;
	}

	public int updateDriverTourStatus() {
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourStatusDao driverTourStatusDao = session.getMapper(DriverTourStatusDao.class);

		int status = 0;
		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(driverId);

			status = driverTourStatusDao.updateDriverTourStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverTourStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}
	
	public static void createDriverTourStatusMethod(String userId) {
		DriverTourStatusModel driverTourStatusModel = new DriverTourStatusModel();
		driverTourStatusModel.setDriverId(userId);
		driverTourStatusModel.setStatus(ProjectConstants.DRIVER_FREE_STATUS);
		driverTourStatusModel.createDriverTourStatus();
	}

	public String getDriverTourStatusId() {
		return driverTourStatusId;
	}

	public void setDriverTourStatusId(String driverTourStatusId) {
		this.driverTourStatusId = driverTourStatusId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
