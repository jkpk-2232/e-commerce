package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DriverOrderRequestDao;

public class DriverOrderRequestModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverOrderRequestModel.class);

	private String driverOrderRequestId;
	private String orderId;
	private String driverId;
	private String status;

	public void createDriverOrderRequest(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverOrderRequestDao driverOrderRequestDao = session.getMapper(DriverOrderRequestDao.class);

		try {

			this.setDriverOrderRequestId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setCreatedBy(userId);
			driverOrderRequestDao.createDriverOrderRequest(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during createDriverOrderRequest : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<DriverOrderRequestModel> getAllOrderRequestByDriverId(String driverId) {

		List<DriverOrderRequestModel> driverOrderRequestModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverOrderRequestDao driverOrderRequestDao = session.getMapper(DriverOrderRequestDao.class);

		try {
			driverOrderRequestModel = driverOrderRequestDao.getAllOrderRequestByDriverId(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllOrderRequestByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverOrderRequestModel;
	}

	public static DriverOrderRequestModel getOrderRequestByDriverIdAndOrderId(String driverId, String orderId) {

		DriverOrderRequestModel driverTourRequestModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverOrderRequestDao driverOrderRequestDao = session.getMapper(DriverOrderRequestDao.class);

		try {
			driverTourRequestModel = driverOrderRequestDao.getOrderRequestByDriverIdAndOrderId(driverId, orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderRequestByDriverIdAndOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverTourRequestModel;
	}

	public void updateDriverOrderRequest() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverOrderRequestDao driverOrderRequestDao = session.getMapper(DriverOrderRequestDao.class);

		try {
			driverOrderRequestDao.updateDriverOrderRequest(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverOrderRequest : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getDriverOrderRequestId() {
		return driverOrderRequestId;
	}

	public void setDriverOrderRequestId(String driverOrderRequestId) {
		this.driverOrderRequestId = driverOrderRequestId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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