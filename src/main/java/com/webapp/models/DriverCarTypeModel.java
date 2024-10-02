package com.webapp.models;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.DriverCarTypeDao;

public class DriverCarTypeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverCarTypeModel.class);

	private String driverCarTypeId;
	private String driverId;
	private String carTypeId;

	public String getDriverCarTypeId() {
		return driverCarTypeId;
	}

	public void setDriverCarTypeId(String driverCarTypeId) {
		this.driverCarTypeId = driverCarTypeId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public static int insertDriverCarTypesBatch(List<DriverCarTypeModel> driverCarTypeModelList) throws IOException {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverCarTypeDao driverCarTypeDao = session.getMapper(DriverCarTypeDao.class);

		try {

			status = driverCarTypeDao.insertDriverCarTypesBatch(driverCarTypeModelList);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insertDriverCarTypesBatch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;

	}

	public static List<DriverCarTypeModel> getDriverCarTypesListByDriverId(String driverId) {

		List<DriverCarTypeModel> driverCarTypeModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverCarTypeDao driverCarTypeDao = session.getMapper(DriverCarTypeDao.class);

		try {

			driverCarTypeModelList = driverCarTypeDao.getDriverCarTypesListByDriverId(driverId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverCarTypesListByDriverId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverCarTypeModelList;
	}

	public int deleteDriverCarTypes() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverCarTypeDao driverCarTypeDao = session.getMapper(DriverCarTypeDao.class);

		try {
			count = driverCarTypeDao.deleteDriverCarTypes(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteDriverCarTypes : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

}
