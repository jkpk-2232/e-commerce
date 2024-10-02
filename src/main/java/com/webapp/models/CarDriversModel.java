package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.CarDriversDao;

public class CarDriversModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CarDriversModel.class);

	private String carDriversId;
	private String carId;
	private String driverId;
	private boolean isActive;

	private String driverName;
	private String driverPhoneNo;

	@Override
	public String toString() {
		return "CarDriversModel [carDriversId=" + carDriversId + ", carId=" + carId + ", driverId=" + driverId + ", isActive=" + isActive + ", driverName=" + driverName + ", driverPhoneNo=" + driverPhoneNo + "]";
	}

	public int insertCarDrivers(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {

			this.setCarDriversId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setCreatedBy(loggedInUserId);
			this.setUpdatedBy(loggedInUserId);

			status = carDriversDao.insertCarDrivers(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertCarDrivers : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static CarDriversModel getCarDriverDetailsByDriverId(String driverId) {

		CarDriversModel carDriversModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {
			carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarDriversModelDetailsByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carDriversModel;
	}

	public static int getTotalCarDriverCountByCarId(String carId) {

		int count = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {
			count = carDriversDao.getTotalCarDriverCountByCarId(carId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalCarDriverCountByCarId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<CarDriversModel> getCarDriversListForSearch(int start, int length, String order, String carId, String globalSearchString) {

		List<CarDriversModel> carDriversModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {
			carDriversModel = carDriversDao.getCarDriversListForSearch(start, length, carId, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarDriversListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carDriversModel;
	}

	public String getCarDriversId() {
		return carDriversId;
	}

	public void setCarDriversId(String carDriversId) {
		this.carDriversId = carDriversId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhoneNo() {
		return driverPhoneNo;
	}

	public void setDriverPhoneNo(String driverPhoneNo) {
		this.driverPhoneNo = driverPhoneNo;
	}

}