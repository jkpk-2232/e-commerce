package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.CarVendorsDao;

public class CarVendorsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CarVendorsModel.class);

	private String carVendorId;
	private String carId;
	private String vendorId;

	public String getCarVendorId() {
		return carVendorId;
	}

	public void setCarVendorId(String carVendorId) {
		this.carVendorId = carVendorId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public static CarVendorsModel addCarVendorMapping(String carId, String vendorId, String loggedInUserId) {

		CarVendorsModel carVendorsModel = new CarVendorsModel();
		carVendorsModel.setCarVendorId(UUIDGenerator.generateUUID());
		carVendorsModel.setCarId(carId);
		carVendorsModel.setVendorId(vendorId);
		carVendorsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
		carVendorsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		carVendorsModel.setCreatedBy(loggedInUserId);
		carVendorsModel.setUpdatedBy(loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarVendorsDao carVendorsDao = session.getMapper(CarVendorsDao.class);
		try {
			carVendorsDao.addCarVendorMapping(carVendorsModel);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carVendorsModel;
	}

	public static CarVendorsModel updateCarVendor(String carId, String vendorId, String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		CarVendorsDao carVendorsDao = session.getMapper(CarVendorsDao.class);
		CarVendorsModel carVendorsModel = new CarVendorsModel();

		try {

			carVendorsModel.setCarVendorId(UUIDGenerator.generateUUID());
			carVendorsModel.setCarId(carId);
			carVendorsModel.setVendorId(vendorId);
			carVendorsModel.setCreatedBy(loggedInUserId);
			carVendorsModel.setUpdatedBy(loggedInUserId);
			carVendorsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			carVendorsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			carVendorsDao.deleteCarVendorMapping(carId);
			carVendorsDao.addCarVendorMapping(carVendorsModel);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return carVendorsModel;

	}

	public void batchUpdateExistingAdminCarMappingToVendor() {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarVendorsDao carVendorsDao = session.getMapper(CarVendorsDao.class);

		try {
			carVendorsDao.batchUpdateExistingAdminCarMappingToVendor(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during batchUpdateExistingAdminCarMappingToVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}
}