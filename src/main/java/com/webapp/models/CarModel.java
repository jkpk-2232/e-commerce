package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.CarDao;
import com.webapp.daos.CarDriversDao;

public class CarModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverInfoModel.class);

	private String carId;
	private String modelName;
	private String carColor;
	private String carPlateNo;
	private long carYear;
	private long noOfPassenger;
	private String driverId;
	private String carTypeId;
	private String owner;
	private String make;
	private String backImgUrl;
	private String frontImgUrl;
	private String carTitle;
	private String registrationPhotoUrl;
	private String insurancePhotoUrl;
	private String inspectionReportPhotoUrl;
	private String vehicleCommercialLicencePhotoUrl;
	private boolean approvelStatus;

	private String carType;
	private String carTypeIconImage;

	public static int getTotalCarCount(long startDatelong, long endDatelong) {

		int carCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			carCount = carDao.getTotalCarCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalCarCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carCount;
	}

	public static List<CarModel> getCarListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, String approvelCheck, boolean approvelStatus) {

		List<CarModel> carModel = new ArrayList<CarModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			carModel = carDao.getCarListForSearch(start, length, order, roleId, globalSearchString, startDatelong, endDatelong, approvelCheck, approvelStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carModel;
	}

	public int insertCar(String loggedInUserId, String vendorId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {

			this.setCarId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setCreatedBy(loggedInUserId);
			this.setUpdatedBy(loggedInUserId);
			this.setApprovelStatus(true);

			status = carDao.insertCar(this);
			CarVendorsModel.addCarVendorMapping(this.carId, vendorId, loggedInUserId);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertCar : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static CarModel getCarDetailsByDriverId(String driverId) {

		CarModel car = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {

			CarDriversModel carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(driverId);
			if (StringUtils.validString(carDriversModel.getCarId()) && (!ProjectConstants.DEFAULT_CAR_ID.equals(carDriversModel.getCarId()))) {
				car = carDao.getCarDetailsByCarId(carDriversModel.getCarId());
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarDetailsByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return car;
	}

	public static CarModel getCarDetailsByCarId(String carId) {

		CarModel car;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			car = carDao.getCarDetailsByCarId(carId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarDetailsByCarId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return car;
	}

	public int updateCarDetails(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInUserId);

			status = carDao.updateCarDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCarDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;

	}

	public int updateCarImages(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInUserId);

			status = carDao.updateCarImages(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCarDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<CarModel> getCarList() {

		List<CarModel> car;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			car = carDao.getCarList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return car;
	}

	public static boolean isCarPlateExists(String carPlateNo, String carId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			status = carDao.isCarPlateExists(carPlateNo, carId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isCarPlateExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<CarModel> getCarListByCarTypeIds(List<String> carTypeList, String vendorId) {

		List<CarModel> cars;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeList", carTypeList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			cars = carDao.getCarListByCarTypeIds(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarListByCarTypeIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return cars;
	}

	public int approvedCarByAdmin() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			status = carDao.approvedCarByAdmin(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateDeactivateUserByAdmin :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static CarModel getCarActiveDeativeDetailsById(String carId) {

		CarModel carModel = new CarModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			carModel = carDao.getCarActiveDeativeDetailsById(carId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carModel;
	}

	public static List<CarModel> getVendorsCarListByCarTypeIds(List<String> carTypeList, String loggedInUserId) {

		List<CarModel> cars;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeList", carTypeList);
		inputMap.put("loggedInUserId", loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			cars = carDao.getVendorsCarListByCarTypeIds(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarListByCarTypeIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return cars;
	}

	public static int getVendorTotalCarCount(long startDatelong, long endDatelong, String approvelCheck, boolean approvelStatus, String userId) {

		int carCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			carCount = carDao.getVendorTotalCarCount(startDatelong, endDatelong, approvelCheck, approvelStatus, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalCarCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carCount;
	}

	public static List<CarModel> getVendorCarListForSearch(int start, int length, String order, String roleId, String globalSearchString, long startDatelong, long endDatelong, String approvelCheck, boolean approvelStatus, String loggedInUserId) {

		List<CarModel> carModel = new ArrayList<CarModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			carModel = carDao.getVendorCarListForSearch(start, length, order, roleId, globalSearchString, startDatelong, endDatelong, approvelCheck, approvelStatus, loggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCarListForSearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carModel;
	}

	public static CarModel getViewModeVendorsCarListByCarTypeIds(String dricerId) {

		CarModel carModel = new CarModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarDao carDao = session.getMapper(CarDao.class);

		try {
			carModel = carDao.getViewModeVendorsCarListByCarTypeIds(dricerId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getViewModeVendorsCarListByCarTypeIds :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carModel;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getCarPlateNo() {
		return carPlateNo;
	}

	public void setCarPlateNo(String carPlateNo) {
		this.carPlateNo = carPlateNo;
	}

	public long getCarYear() {
		return carYear;
	}

	public void setCarYear(long carYear) {
		this.carYear = carYear;
	}

	public long getNoOfPassenger() {
		return noOfPassenger;
	}

	public void setNoOfPassenger(long noOfPassenger) {
		this.noOfPassenger = noOfPassenger;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getBackImgUrl() {
		return backImgUrl;
	}

	public void setBackImgUrl(String backImgUrl) {
		this.backImgUrl = backImgUrl;
	}

	public String getFrontImgUrl() {
		return frontImgUrl;
	}

	public void setFrontImgUrl(String frontImgUrl) {
		this.frontImgUrl = frontImgUrl;
	}

	public String getCarTitle() {
		return carTitle;
	}

	public void setCarTitle(String carTitle) {
		this.carTitle = carTitle;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getRegistrationPhotoUrl() {
		return registrationPhotoUrl;
	}

	public void setRegistrationPhotoUrl(String registrationPhotoUrl) {
		this.registrationPhotoUrl = registrationPhotoUrl;
	}

	public String getInsurancePhotoUrl() {
		return insurancePhotoUrl;
	}

	public void setInsurancePhotoUrl(String insurancePhotoUrl) {
		this.insurancePhotoUrl = insurancePhotoUrl;
	}

	public String getInspectionReportPhotoUrl() {
		return inspectionReportPhotoUrl;
	}

	public void setInspectionReportPhotoUrl(String inspectionReportPhotoUrl) {
		this.inspectionReportPhotoUrl = inspectionReportPhotoUrl;
	}

	public String getVehicleCommercialLicencePhotoUrl() {
		return vehicleCommercialLicencePhotoUrl;
	}

	public void setVehicleCommercialLicencePhotoUrl(String vehicleCommercialLicencePhotoUrl) {
		this.vehicleCommercialLicencePhotoUrl = vehicleCommercialLicencePhotoUrl;
	}

	public boolean isApprovelStatus() {
		return approvelStatus;
	}

	public void setApprovelStatus(boolean approvelStatus) {
		this.approvelStatus = approvelStatus;
	}

	public String getCarTypeIconImage() {
		return carTypeIconImage;
	}

	public void setCarTypeIconImage(String carTypeIconImage) {
		this.carTypeIconImage = carTypeIconImage;
	}

	@Override
	public String toString() {
		return "CarModel [carId=" + carId + ", modelName=" + modelName + ", carColor=" + carColor + ", carPlateNo=" + carPlateNo + ", carYear=" + carYear + ", noOfPassenger=" + noOfPassenger + ", driverId=" + driverId + ", carTypeId=" + carTypeId + ", owner=" + owner
					+ ", make=" + make + ", backImgUrl=" + backImgUrl + ", frontImgUrl=" + frontImgUrl + ", carTitle=" + carTitle + ", carType=" + carType + ", registrationPhotoUrl=" + registrationPhotoUrl + ", insurancePhotoUrl=" + insurancePhotoUrl
					+ ", inspectionReportPhotoUrl=" + inspectionReportPhotoUrl + ", vehicleCommercialLicencePhotoUrl=" + vehicleCommercialLicencePhotoUrl + ", approvelStatus=" + approvelStatus + "]";
	}

}