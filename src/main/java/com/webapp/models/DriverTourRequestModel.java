package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DriverTourRequestDao;

public class DriverTourRequestModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverTourRequestModel.class);

	private String driverTourRequestId;
	private String tourId;
	private String driverId;
	private String status;

	public int createDriverTourRequest() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourRequestDao driverTourRequestDao = session.getMapper(DriverTourRequestDao.class);

		int status = 0;
		try {

			this.setDriverTourRequestId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setCreatedBy(driverId);

			status = driverTourRequestDao.createDriverTourRequest(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during createDriverTourRequest : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}

	public static List<DriverTourRequestModel> getAllTourRequestByDriverId(String driverId) {

		List<DriverTourRequestModel> driverTourRequestModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourRequestDao driverTourRequestDao = session.getMapper(DriverTourRequestDao.class);

		try {
			driverTourRequestModel = driverTourRequestDao.getAllTourRequestByDriverId(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllTourRequestByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverTourRequestModel;
	}

	public boolean isTourRequestSent() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourRequestDao driverTourRequestDao = session.getMapper(DriverTourRequestDao.class);

		try {
			count = driverTourRequestDao.isTourRequestSent(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllTourRequestByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	public static DriverTourRequestModel getTourRequestByDriverIdAndTourId(String driverId, String tourId) {

		DriverTourRequestModel driverTourRequestModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourRequestDao driverTourRequestDao = session.getMapper(DriverTourRequestDao.class);

		try {
			driverTourRequestModel = driverTourRequestDao.getTourRequestByDriverIdAndTourId(driverId, tourId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTourRequestByDriverIdAndTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverTourRequestModel;
	}

	public int updateDriverTourRequest() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourRequestDao driverTourRequestDao = session.getMapper(DriverTourRequestDao.class);

		try {
			status = driverTourRequestDao.updateDriverTourRequest(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverTourRequest : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public void deleteExistingEntry() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTourRequestDao driverTourRequestDao = session.getMapper(DriverTourRequestDao.class);

		try {
			driverTourRequestDao.deleteExistingEntry(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteExistingEntry : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getDriverTourRequestId() {
		return driverTourRequestId;
	}

	public void setDriverTourRequestId(String driverTourRequestId) {
		this.driverTourRequestId = driverTourRequestId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
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