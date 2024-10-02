package com.webapp.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.CarTypeDao;

public class CarTypeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CarTypeModel.class);

	private String carTypeId;
	private String carType;
	private int carPriority;
	private String icon;
	private boolean isActive;
	private boolean isDeleted;
	private boolean isPermanentDeleted;
	private boolean isPredefinedCar;
	private String carTypeIconImage;

	private String iconPath;
	private String iconName;

	public static List<CarTypeModel> getAllCars() {

		List<CarTypeModel> carList = new ArrayList<CarTypeModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			carList = carDao.getAllCars();
			for (CarTypeModel carTypeModel : carList) {
				carTypeModel.setIconPath("/assets/image/car_icons/" + carTypeModel.getIcon() + "/" + carTypeModel.getIcon() + "_cab.png");
				carTypeModel.setIconName("Icon " + carTypeModel.getIcon());
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllCars : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return carList;
	}

	public static List<CarTypeModel> getAllCarsActiveDeactive() {

		List<CarTypeModel> carList = new ArrayList<CarTypeModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			carList = carDao.getAllCarsActiveDeactive();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllCarsActiveDeactive : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return carList;
	}

	public static CarTypeModel getCarTypeByCarTypeId(String carTypeId) {

		CarTypeModel carList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {

			carList = carDao.getCarTypeByCarTypeId(carTypeId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCarTypeByCarTypeId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carList;
	}

	public static List<CarTypeModel> getActiveDeactaiveCarDetails() throws SQLException {

		List<CarTypeModel> carList = new ArrayList<CarTypeModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {

			carList = carDao.getActiveDeactaiveCarDetails();
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getActiveDeactaiveCarDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carList;
	}

	public static int getCarTypeCount() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			count = carDao.getCarTypeCount();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarTypeCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return count;
	}

	public static List<CarTypeModel> getCarTypeListForSearch(int start, int length, String order, String globalSearchString) {

		List<CarTypeModel> carList = new ArrayList<CarTypeModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			carList = carDao.getCarTypeListForSearch(start, length, order, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarTypeListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return carList;
	}

	public static int getCarTypeListCountForSearch(String globalSearchString) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			count = carDao.getCarTypeListCountForSearch(globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarTypeListCountForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return count;
	}

	public void updateCarTypeStatus(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			carDao.updateCarTypeStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCarTypeStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String insertCarType(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		this.carTypeId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		try {
			CarTypeModel test = carDao.getLastPriority();

			this.isActive = true;
			this.isDeleted = false;
			this.isPermanentDeleted = false;
			this.isPredefinedCar = false;
			if (test != null) {
				this.carPriority = test.getCarPriority() + 1;
			} else {
				this.carPriority = 1;
			}

			carDao.insertCarType(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertCarType : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.carTypeId;
	}

	public void updateCarType(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			carDao.updateCarType(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCarType : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static boolean isCarTypeNameExists(String carType, String carTypeId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			status = carDao.isCarTypeNameExists(carType, carTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isCarTypeNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}

	public static List<CarTypeModel> getCarListByIds(List<String> carType) {

		List<CarTypeModel> list = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarTypeDao carDao = session.getMapper(CarTypeDao.class);

		try {
			list = carDao.getCarListByIds(carType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarListByIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isPermanentDeleted() {
		return isPermanentDeleted;
	}

	public void setPermanentDeleted(boolean isPermanentDeleted) {
		this.isPermanentDeleted = isPermanentDeleted;
	}

	public int getCarPriority() {
		return carPriority;
	}

	public void setCarPriority(int carPriority) {
		this.carPriority = carPriority;
	}

	public boolean isPredefinedCar() {
		return isPredefinedCar;
	}

	public void setPredefinedCar(boolean isPredefinedCar) {
		this.isPredefinedCar = isPredefinedCar;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getCarTypeIconImage() {
		return carTypeIconImage;
	}

	public void setCarTypeIconImage(String carTypeIconImage) {
		this.carTypeIconImage = carTypeIconImage;
	}
}