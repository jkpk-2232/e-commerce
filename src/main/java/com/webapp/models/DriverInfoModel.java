package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.EncryptionUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.daos.CarDao;
import com.webapp.daos.CarDriversDao;
import com.webapp.daos.DriverBankDetailsDao;
import com.webapp.daos.DriverInfoDao;
import com.webapp.daos.DriverVendorsDao;
import com.webapp.daos.DrivingLicenseInfoDao;
import com.webapp.daos.UserDao;

public class DriverInfoModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverInfoModel.class);

	private String userInfoId;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private String phoneNoCode;
	private String drivingLicense;
	private long joiningDate;
	private String mailAddressLineOne;
	private String mailAddressLineTwo;
	private String mailCountryId;
	private String mailStateId;
	private String mailCityId;
	private String mailZipCode;
	private String billAddressLineOne;
	private String billAddressLineTwo;
	private String billCountryId;
	private String billStateId;
	private String billCityId;
	private String billZipCode;
	private String gender;
	private String userId;
	private boolean isNotificationStatus;
	private boolean isOnDuty;
	private long roleId;
	private String userRole;
	private long badgeCount;
	private boolean isSameAsMailing;
	private String applicationStatus;
	private DriverBankDetailsModel driverBankDetails;
	private CarModel carModel;
	private CarDriversModel carDriversModel;
	private DrivingLicenseInfoModel drivingLicenseModel;
	private String email;
	private String password;
	private String photoUrl;
	private String companyName;
	private String companyAddress;
	private String drivingLicensephoto;
	private String verificationCode;
	private boolean isVerified;
	private boolean isActive;
	private boolean isDeleted;
	private String fullName;
	private String apiSessionKey;
	private long companyDriver;
	private String driverReferralCode;
	private String modelName;
	private String make;
	private String driveTransmissionTypeId;
	private boolean approvelStatus;
	private double driverPayablePercentage;

	private String inputRegionId;
	private String inputServiceType;
	private List<String> inputCarType;

	private String agentNumber;

	@Override
	public String toString() {
		return "DriverInfoModel [userInfoId=" + userInfoId + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNo=" + phoneNo + ", phoneNoCode=" + phoneNoCode + ", drivingLicense=" + drivingLicense + ", joiningDate=" + joiningDate + ", mailAddressLineOne="
					+ mailAddressLineOne + ", mailAddressLineTwo=" + mailAddressLineTwo + ", mailCountryId=" + mailCountryId + ", mailStateId=" + mailStateId + ", mailCityId=" + mailCityId + ", mailZipCode=" + mailZipCode + ", billAddressLineOne="
					+ billAddressLineOne + ", billAddressLineTwo=" + billAddressLineTwo + ", billCountryId=" + billCountryId + ", billStateId=" + billStateId + ", billCityId=" + billCityId + ", billZipCode=" + billZipCode + ", gender=" + gender + ", userId="
					+ userId + ", isNotificationStatus=" + isNotificationStatus + ", isOnDuty=" + isOnDuty + ", roleId=" + roleId + ", userRole=" + userRole + ", badgeCount=" + badgeCount + ", isSameAsMailing=" + isSameAsMailing + ", applicationStatus="
					+ applicationStatus + ", driverBankDetails=" + driverBankDetails + ", carModel=" + carModel + ", carDriversModel=" + carDriversModel + ", drivingLicenseModel=" + drivingLicenseModel + ", email=" + email + ", password=" + password + ", photoUrl="
					+ photoUrl + ", companyName=" + companyName + ", companyAddress=" + companyAddress + ", drivingLicensephoto=" + drivingLicensephoto + ", verificationCode=" + verificationCode + ", isVerified=" + isVerified + ", isActive=" + isActive
					+ ", isDeleted=" + isDeleted + ", fullName=" + fullName + ", apiSessionKey=" + apiSessionKey + ", companyDriver=" + companyDriver + ", driverReferralCode=" + driverReferralCode + ", modelName=" + modelName + ", make=" + make
					+ ", driveTransmissionTypeId=" + driveTransmissionTypeId + ", approvelStatus=" + approvelStatus + ", driverPayablePercentage=" + driverPayablePercentage + "]";
	}

	public static DriverInfoModel getDriverAccountDetailsById(String userId) {

		DriverInfoModel driverInfoModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);

		try {
			driverInfoModel = driverInfoDao.getDriverAccountDetailsById(userId);

			if (driverInfoModel != null) {

				driverInfoModel.setDriverBankDetails(driverBankDetailsDao.getDriverBankDetails(userId));

				CarDriversModel carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(userId);

				if (carDriversModel != null) {

					if ((carDriversModel.getCarId() != null) && (!"".equals(carDriversModel.getCarId())) && (!"-1".equals(carDriversModel.getCarId()))) {

						CarModel car = CarModel.getCarDetailsByCarId(carDriversModel.getCarId());
						driverInfoModel.setCarModel(car);

					} else {

						CarModel car = new CarModel();
						car.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);

						driverInfoModel.setCarModel(car);
					}

				} else {

					CarModel car = new CarModel();
					car.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);

					driverInfoModel.setCarModel(car);
				}

				driverInfoModel.setDrivingLicenseModel(drivingLicenseInfoDao.getDriverLicenseDetails(userId));
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverAccountDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverInfoModel;
	}

	public static DriverInfoModel getActiveDeactiveDriverAccountDetailsById(String userId) {

		DriverInfoModel driverInfoModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);

		try {
			driverInfoModel = driverInfoDao.getActiveDeactiveDriverAccountDetailsById(userId);

			if (driverInfoModel != null) {

				driverInfoModel.setDriverBankDetails(driverBankDetailsDao.getDriverBankDetails(userId));
				CarDriversModel carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(userId);

				if ((carDriversModel != null) && (!"-1".equals(carDriversModel.getCarId()))) {

					CarModel car = CarModel.getCarDetailsByCarId(carDriversModel.getCarId());
					driverInfoModel.setCarModel(car);

				} else {

					driverInfoModel.setCarModel(null);
				}

				driverInfoModel.setDrivingLicenseModel(drivingLicenseInfoDao.getDriverLicenseDetails(userId));
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverAccountDetailsById : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverInfoModel;
	}

	public static DriverInfoModel getUserDetailsByRole(String roleId) {
		DriverInfoModel driverInfoModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {
			driverInfoModel = driverInfoDao.getUserDetailsByRole(roleId);
			if (driverInfoModel != null) {
				driverInfoModel.setDriverBankDetails(driverBankDetailsDao.getDriverBankDetails(driverInfoModel.getUserId()));
				CarDriversModel carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(driverInfoModel.getUserId());
				if (carDriversModel != null) {
					CarModel car = CarModel.getCarDetailsByCarId(carDriversModel.getCarId());
					driverInfoModel.setCarModel(car);
				} else {
					driverInfoModel.setCarModel(null);
				}
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverInfoModel;
	}

	// Method for WEB
	public int updateDriverInfo(CarDriversModel carDriversModel, DriverBankDetailsModel driverBankDetailsModel, DrivingLicenseInfoModel drivingLicenseInfoModel) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);
		UserDao userDao = session.getMapper(UserDao.class);
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			driverBankDetailsModel.setUpdatedAt(this.getUpdatedAt());
			drivingLicenseInfoModel.setUpdatedAt(this.getUpdatedAt());

			UserModel userModel = new UserModel();
			userModel.setUserId(this.getUserId());
			userModel.setEmail(this.email);
			userModel.setUpdatedAt(this.getUpdatedAt());
			userModel.setPhotoUrl(this.photoUrl);

			this.driverReferralCode = this.phoneNo;

			status = driverInfoDao.updateDriverInfo(this);

			status = driverBankDetailsDao.updateDriverBankDetails(driverBankDetailsModel);

			if (carDriversModel.getCarId() != null && !carDriversModel.getCarId().equals("")) {
				status = carDriversDao.updateCarDriverDetails(carDriversModel);
			}

			status = userDao.updateDriverUpdatedAt(userModel);

			if (drivingLicenseInfoDao.getDriverLicenseDetails(this.getUserId()) != null) {

				status = drivingLicenseInfoDao.updateDrivingLicenseInfo(drivingLicenseInfoModel);

			} else {

				drivingLicenseInfoModel.setDrivingLicenseInfoId(UUIDGenerator.generateUUID());
				drivingLicenseInfoModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				drivingLicenseInfoModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				drivingLicenseInfoModel.setUserId(this.getUserId());
				status = drivingLicenseInfoDao.insertDriverLicenseDetails(drivingLicenseInfoModel);
			}

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverInfo : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	// Method for API
	public int updateDriver(String loggedOnUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedOnUserId);
			this.setUserId(loggedOnUserId);
			status = driverInfoDao.updateDriverInfo(this);

			UserModel userModel = new UserModel();

			userModel.setUserId(loggedOnUserId);
			userModel.setPhotoUrl(this.getPhotoUrl());
			userModel.setUpdatedAt(this.getUpdatedAt());
			userModel.setUpdatedBy(loggedOnUserId);

			status = userDao.updateDriverInfoPhoto(userModel);

			DriverBankDetailsModel driverBankDetailsModel = new DriverBankDetailsModel();

			driverBankDetailsModel.setUserId(loggedOnUserId);
			driverBankDetailsModel.setBankName(this.getDriverBankDetails().getBankName());
			driverBankDetailsModel.setAccountNumber(this.getDriverBankDetails().getAccountNumber());
			// driverBankDetailsModel.setAccountName(this.getDriverBankDetails().getAccountName());
			// driverBankDetailsModel.setRoutingNumber(this.getDriverBankDetails().getRoutingNumber());
			// driverBankDetailsModel.setType(this.getDriverBankDetails().getType());
			driverBankDetailsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			driverBankDetailsModel.setUpdatedBy(loggedOnUserId);

			status = driverBankDetailsDao.updateDriverBankDetails(driverBankDetailsModel);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverInfo : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}

	public int updateDriverForApi(String loggedOnUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserDao userDao = session.getMapper(UserDao.class);
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedOnUserId);
			this.setUserId(loggedOnUserId);
			status = driverInfoDao.updateDriverForApi(this);

			UserModel userModel = new UserModel();

			userModel.setUserId(loggedOnUserId);
			userModel.setPhotoUrl(this.getPhotoUrl());
			userModel.setUpdatedAt(this.getUpdatedAt());
			userModel.setUpdatedBy(loggedOnUserId);

			userDao.updateDriverInfoPhoto(userModel);

			// DriverBankDetailsModel driverBankDetailsModel = new DriverBankDetailsModel();
			//
			// driverBankDetailsModel.setUserId(loggedOnUserId);
			// driverBankDetailsModel.setBankName(this.getDriverBankDetails().getBankName());
			// driverBankDetailsModel.setAccountNumber(this.getDriverBankDetails().getAccountNumber());
			// driverBankDetailsModel.setAccountName(this.getDriverBankDetails().getAccountName());
			// driverBankDetailsModel.setRoutingNumber(this.getDriverBankDetails().getRoutingNumber());
			// driverBankDetailsModel.setType(this.getDriverBankDetails().getType());
			//
			// driverBankDetailsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			// driverBankDetailsModel.setUpdatedBy(loggedOnUserId);
			//
			// driverBankDetailsDao.updateDriverBankDetails(driverBankDetailsModel);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverForApi : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return status;
	}

	public String addDriver() {

		int status = -1;

		String userId = "";

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		UserDao userDao = session.getMapper(UserDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDao carDao = session.getMapper(CarDao.class);
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {

			UserModel userModel = new UserModel();

			userId = UUIDGenerator.generateUUID();
			userModel.setUserId(userId);
			userModel.setEmail(this.getEmail());

			userModel.setPassword(EncryptionUtils.encryptString(this.getPassword()));
			userModel.setActive(false);
			userModel.setPhotoUrl(this.photoUrl);
			userModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			userModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			userModel.setCreatedBy(userId);
			userModel.setUpdatedBy(userId);
			userModel.setRoleId(UserRoles.DRIVER_ROLE_ID);
			userModel.setPhoneNoCode(this.getPhoneNoCode());

			status = userDao.addDriver(userModel);

			if (status > 0) {

				this.setUserInfoId(UUIDGenerator.generateUUID());
				this.setUserId(userId);
				this.setCreatedBy(userId);
				this.setUpdatedBy(userId);
				this.setCreatedAt(DateUtils.nowAsGmtMillisec());
				this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				this.setJoiningDate(this.getCreatedAt());
				this.setDrivingLicense(this.getDrivingLicenseModel().getDriverLicenseCardNumber());

				// ADDING REFERRAL CODE TO DRIVER
				// this.setDriverReferralCode(UUIDGenerator.generateUUID().substring(0,
				// 10).toUpperCase());

				this.setDriverReferralCode(this.phoneNo);

				status = driverInfoDao.addDriver(this);

				DriverBankDetailsModel driverBankDetailsModel = this.getDriverBankDetails();
				driverBankDetailsModel.setDriverBankDetailsId(UUIDGenerator.generateUUID());
				driverBankDetailsModel.setUserId(userId);
				driverBankDetailsModel.setCreatedBy(userId);
				driverBankDetailsModel.setUpdatedBy(userId);
				driverBankDetailsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				driverBankDetailsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

				status = driverBankDetailsDao.addDriverBankDetails(driverBankDetailsModel);

				CarModel car = this.getCarModel();

				String carId = UUIDGenerator.generateUUID();
				car.setCarId(carId);
				car.setDriverId(userId);
				car.setCreatedAt(DateUtils.nowAsGmtMillisec());
				car.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				car.setCreatedBy(userId);
				car.setUpdatedBy(userId);
				// car.setModelName(this.getModelName());
				// car.setMake(this.getMake());

				status = carDao.insertCar(car);

				CarDriversModel carsDriverModel = new CarDriversModel();

				carsDriverModel.setCarDriversId(UUIDGenerator.generateUUID());
				carsDriverModel.setDriverId(userId);
				carsDriverModel.setCarId(carId);
				carsDriverModel.setCreatedBy(userId);
				carsDriverModel.setUpdatedBy(userId);
				carsDriverModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				carsDriverModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

				status = carDriversDao.insertCarDrivers(carsDriverModel);

				DrivingLicenseInfoModel drivingLicenseInfoModel = this.getDrivingLicenseModel();

				drivingLicenseInfoModel.setDrivingLicenseInfoId(UUIDGenerator.generateUUID());
				drivingLicenseInfoModel.setUserId(userId);
				drivingLicenseInfoModel.setfName(this.getFirstName());
				drivingLicenseInfoModel.setlName(this.getLastName());
				drivingLicenseInfoModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				drivingLicenseInfoModel.setCreatedBy(userId);
				drivingLicenseInfoModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				drivingLicenseInfoModel.setUpdatedBy(userId);

				status = drivingLicenseInfoDao.insertDriverLicenseDetails(drivingLicenseInfoModel);

			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			userId = null;
			logger.error("Exception occured during isLoggedIn", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userId;
	}

	public String addDriverWeb(String loggedInUser, String vendorId, boolean addNewCarAnDriver, boolean isApiDriverSignUp, boolean isV2ApiCall) {

		int status = -1;

		String userId = "";

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		UserDao userDao = session.getMapper(UserDao.class);
		CarDao carDao = session.getMapper(CarDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);
		DriverVendorsDao driverVendorsDao = session.getMapper(DriverVendorsDao.class);

		try {

			UserModel userModel = new UserModel();

			userId = UUIDGenerator.generateUUID();

			if (loggedInUser == null) {
				loggedInUser = userId;
			}

			userModel.setUserId(userId);
			userModel.setEmail(this.getEmail());

			userModel.setPassword(EncryptionUtils.encryptString(this.password));
			userModel.setDeleted(false);
			userModel.setActive(true);
			userModel.setPhotoUrl(this.photoUrl);
			userModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			userModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			userModel.setCreatedBy(loggedInUser);
			userModel.setUpdatedBy(loggedInUser);
			userModel.setRoleId(UserRoles.DRIVER_ROLE_ID);

			if (isV2ApiCall) {

				// call from driver app for short fields driver registration form.
				userModel.setApprovelStatus(true);

			} else {

				if (isApiDriverSignUp) {

					userModel.setApprovelStatus(false);

				} else {

					if (addNewCarAnDriver) {
						// call from driver sign up api
						userModel.setApprovelStatus(false);
					} else {
						// call admin add driver
						userModel.setApprovelStatus(true);
					}
				}
			}

			status = userDao.addDriverWeb(userModel);

			DriverVendorsModel driverVendorModel = new DriverVendorsModel();

			driverVendorModel.setDriverVendorId(UUIDGenerator.generateUUID());
			driverVendorModel.setDriverId(userId);
			driverVendorModel.setVendorId(vendorId);
			driverVendorModel.setCreatedBy(loggedInUser);
			driverVendorModel.setUpdatedBy(loggedInUser);
			driverVendorModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			driverVendorModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			driverVendorModel.setRoleId(UserRoles.DRIVER_ROLE_ID);

			driverVendorsDao.addDriverVendorMapping(driverVendorModel);

			if (status > 0) {

				this.setUserInfoId(UUIDGenerator.generateUUID());
				this.setUserId(userId);
				this.setCreatedBy(loggedInUser);
				this.setUpdatedBy(loggedInUser);
				this.setCreatedAt(DateUtils.nowAsGmtMillisec());
				this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				this.setJoiningDate(this.getCreatedAt());
				this.setDriverReferralCode(this.phoneNo);

				status = driverInfoDao.addDriver(this);

				DriverBankDetailsModel driverBankDetailsModel = this.getDriverBankDetails();
				driverBankDetailsModel.setDriverBankDetailsId(UUIDGenerator.generateUUID());
				driverBankDetailsModel.setUserId(userId);
				driverBankDetailsModel.setCreatedBy(loggedInUser);
				driverBankDetailsModel.setUpdatedBy(loggedInUser);
				driverBankDetailsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				driverBankDetailsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

				status = driverBankDetailsDao.addDriverBankDetails(driverBankDetailsModel);

				CarDriversModel carsDriverModel = this.getCarDriversModel();
				if (carsDriverModel == null) {
					carsDriverModel = new CarDriversModel();
				}

				if (addNewCarAnDriver) {
					CarModel car = this.getCarModel();

					String carId = UUIDGenerator.generateUUID();
					car.setCarId(carId);
					car.setDriverId(userId);
					car.setCreatedAt(DateUtils.nowAsGmtMillisec());
					car.setUpdatedAt(DateUtils.nowAsGmtMillisec());
					car.setCreatedBy(loggedInUser);
					car.setUpdatedBy(loggedInUser);

					if (isV2ApiCall) {
						car.setApprovelStatus(true);
					} else {
						car.setApprovelStatus(false);
					}

					status = carDao.insertCar(car);

					if (ProjectConstants.SERVICE_TYPE_CAR_ID.equals(this.inputServiceType)) {
						carsDriverModel.setCarId(carId);
					} else {
						carsDriverModel.setCarId(ProjectConstants.DEFAULT_CAR_ID);
					}

					CarVendorsModel.addCarVendorMapping(carId, vendorId, userId);

				} else {
					// carsDriverModel.setCarId(ProjectConstants.DEFAULT_CAR_ID);
				}

				carsDriverModel.setCarDriversId(UUIDGenerator.generateUUID());
				carsDriverModel.setDriverId(userId);
				carsDriverModel.setCreatedBy(userId);
				carsDriverModel.setUpdatedBy(userId);
				carsDriverModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				carsDriverModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

				carDriversDao.insertCarDrivers(carsDriverModel);

				DrivingLicenseInfoModel drivingLicenseInfoModel = this.getDrivingLicenseModel();

				drivingLicenseInfoModel.setDrivingLicenseInfoId(UUIDGenerator.generateUUID());
				drivingLicenseInfoModel.setUserId(userId);
				drivingLicenseInfoModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				drivingLicenseInfoModel.setCreatedBy(loggedInUser);
				drivingLicenseInfoModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				drivingLicenseInfoModel.setUpdatedBy(loggedInUser);

				status = drivingLicenseInfoDao.insertDriverLicenseDetails(drivingLicenseInfoModel);

			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			userId = null;
			logger.error("Exception occured during addDriverWeb", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return userId;
	}

	public int updateDriverApplicationStatus(String loggedInuserId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInuserId);

			status = driverInfoDao.updateDriverApplicationStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static DriverInfoModel getDriverDetailsByRoleAndApplication(String roleId, String applicationStatus) {

		DriverInfoModel driverInfoModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {
			driverInfoModel = driverInfoDao.getDriverDetailsByRoleAndApplication(roleId, applicationStatus);
			if (driverInfoModel != null) {
				driverInfoModel.setDriverBankDetails(driverBankDetailsDao.getDriverBankDetails(driverInfoModel.getUserId()));
				CarDriversModel carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(driverInfoModel.getUserId());
				if (carDriversModel != null) {
					CarModel car = CarModel.getCarDetailsByCarId(carDriversModel.getCarId());
					driverInfoModel.setCarModel(car);
				} else {
					driverInfoModel.setCarModel(null);
				}
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDetailsByRoleAndApplication : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverInfoModel;
	}

	public static DriverInfoModel getActiveDeactiveDriverDetailsByRoleAndApplication(String roleId, String applicationStatus) {

		DriverInfoModel driverInfoModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);
		CarDriversDao carDriversDao = session.getMapper(CarDriversDao.class);

		try {
			driverInfoModel = driverInfoDao.getActiveDeactiveDriverDetailsByRoleAndApplication(roleId, applicationStatus);
			if (driverInfoModel != null) {
				driverInfoModel.setDriverBankDetails(driverBankDetailsDao.getDriverBankDetails(driverInfoModel.getUserId()));
				CarDriversModel carDriversModel = carDriversDao.getCarDriverDetailsByDriverId(driverInfoModel.getUserId());
				if (carDriversModel != null) {
					CarModel car = CarModel.getCarDetailsByCarId(carDriversModel.getCarId());
					driverInfoModel.setCarModel(car);
				} else {
					driverInfoModel.setCarModel(null);
				}
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDetailsByRoleAndApplication : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverInfoModel;
	}

	public static List<DriverInfoModel> getAllDriverDetailsByRoleIdAndApplicationStatus(String roleId, String applicationStatus, List<String> assignedRegionList) {

		List<DriverInfoModel> driverInfoModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("roleId", roleId);
		inputMap.put("applicationStatus", applicationStatus);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {

			driverInfoModel = driverInfoDao.getAllDriverDetailsByRoleIdAndApplicationStatus(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getAllDriverDetailsByRoleIdAndApplicationStatus : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverInfoModel;
	}

	public static int getDriverCountByRoleAndApplicationStatus(String roleId, String applicationStatus) {

		int count = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {
			count = driverInfoDao.getDriverCountByRoleAndApplicationStatus(roleId, applicationStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverCountByRoleAndApplicationStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return count;
	}

	public static List<DriverInfoModel> getDriverApplicationListByRoleAndApplication(String roleId, String searchKey, long startOffSet, long recordOffset, String applicationStatus) {

		List<DriverInfoModel> driversList = new ArrayList<DriverInfoModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {
			Map<String, Object> driverMap = new HashMap<String, Object>();

			driverMap.put("roleId", roleId);
			driverMap.put("applicationStatus", applicationStatus);
			driverMap.put("searchKey", searchKey);
			driverMap.put("startOffSet", startOffSet);
			driverMap.put("recordOffset", recordOffset);

			driversList = driverInfoDao.getDriverApplicationListByRoleAndApplication(driverMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverApplicationListByRoleAndApplication : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driversList;
	}

	public static void updateVerificationCode(String userId, String verificationCode) {
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {

			driverInfoDao.updateVerificationCode(userId, verificationCode);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVerificationCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<DriverInfoModel> getDriverListByMulticityRegionIds(String roleId, String applicationStatus, String[] multicityRegionIds) {

		Map<String, Object> driverMap = new HashMap<String, Object>();

		driverMap.put("roleId", roleId);
		driverMap.put("applicationStatus", applicationStatus);
		driverMap.put("multicityRegionIds", multicityRegionIds);

		List<DriverInfoModel> driverInfoModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		DriverInfoDao driverInfoDao = session.getMapper(DriverInfoDao.class);

		try {
			driverInfoModel = driverInfoDao.getDriverListByMulticityRegionIds(driverMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverListByMulticityRegionIds : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverInfoModel;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public long getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(long joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getMailAddressLineOne() {
		return mailAddressLineOne;
	}

	public void setMailAddressLineOne(String mailAddressLineOne) {
		this.mailAddressLineOne = mailAddressLineOne;
	}

	public String getMailAddressLineTwo() {
		return mailAddressLineTwo;
	}

	public void setMailAddressLineTwo(String mailAddressLineTwo) {
		this.mailAddressLineTwo = mailAddressLineTwo;
	}

	public String getMailZipCode() {
		return mailZipCode;
	}

	public void setMailZipCode(String mailZipCode) {
		this.mailZipCode = mailZipCode;
	}

	public String getBillAddressLineOne() {
		return billAddressLineOne;
	}

	public void setBillAddressLineOne(String billAddressLineOne) {
		this.billAddressLineOne = billAddressLineOne;
	}

	public String getBillAddressLineTwo() {
		return billAddressLineTwo;
	}

	public void setBillAddressLineTwo(String billAddressLineTwo) {
		this.billAddressLineTwo = billAddressLineTwo;
	}

	public String getBillZipCode() {
		return billZipCode;
	}

	public void setBillZipCode(String billZipCode) {
		this.billZipCode = billZipCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isNotificationStatus() {
		return isNotificationStatus;
	}

	public void setNotificationStatus(boolean isNotificationStatus) {
		this.isNotificationStatus = isNotificationStatus;
	}

	public boolean isOnDuty() {
		return isOnDuty;
	}

	public void setOnDuty(boolean isOnDuty) {
		this.isOnDuty = isOnDuty;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public DriverBankDetailsModel getDriverBankDetails() {
		return driverBankDetails;
	}

	public void setDriverBankDetails(DriverBankDetailsModel driverBankDetails) {
		this.driverBankDetails = driverBankDetails;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getApiSessionKey() {
		return apiSessionKey;
	}

	public void setApiSessionKey(String apiSessionKey) {
		this.apiSessionKey = apiSessionKey;
	}

	public CarModel getCarModel() {
		return carModel;
	}

	public void setCarModel(CarModel carModel) {
		this.carModel = carModel;
	}

	public long getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(long badgeCount) {
		this.badgeCount = badgeCount;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getDrivingLicensephoto() {
		return drivingLicensephoto;
	}

	public void setDrivingLicensephoto(String drivingLicensephoto) {
		this.drivingLicensephoto = drivingLicensephoto;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public DrivingLicenseInfoModel getDrivingLicenseModel() {
		return drivingLicenseModel;
	}

	public void setDrivingLicenseModel(DrivingLicenseInfoModel drivingLicenseModel) {
		this.drivingLicenseModel = drivingLicenseModel;
	}

	public boolean isSameAsMailing() {
		return isSameAsMailing;
	}

	public void setSameAsMailing(boolean isSameAsMailing) {
		this.isSameAsMailing = isSameAsMailing;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getMailCountryId() {
		return mailCountryId;
	}

	public void setMailCountryId(String mailCountryId) {
		this.mailCountryId = mailCountryId;
	}

	public String getMailStateId() {
		return mailStateId;
	}

	public void setMailStateId(String mailStateId) {
		this.mailStateId = mailStateId;
	}

	public String getMailCityId() {
		return mailCityId;
	}

	public void setMailCityId(String mailCityId) {
		this.mailCityId = mailCityId;
	}

	public String getBillCountryId() {
		return billCountryId;
	}

	public void setBillCountryId(String billCountryId) {
		this.billCountryId = billCountryId;
	}

	public String getBillStateId() {
		return billStateId;
	}

	public void setBillStateId(String billStateId) {
		this.billStateId = billStateId;
	}

	public String getBillCityId() {
		return billCityId;
	}

	public void setBillCityId(String billCityId) {
		this.billCityId = billCityId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public long getCompanyDriver() {
		return companyDriver;
	}

	public void setCompanyDriver(long companyDriver) {
		this.companyDriver = companyDriver;
	}

	public CarDriversModel getCarDriversModel() {
		return carDriversModel;
	}

	public void setCarDriversModel(CarDriversModel carDriversModel) {
		this.carDriversModel = carDriversModel;
	}

	public String getDriverReferralCode() {
		return driverReferralCode;
	}

	public void setDriverReferralCode(String driverReferralCode) {
		this.driverReferralCode = driverReferralCode;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getDriveTransmissionTypeId() {
		return driveTransmissionTypeId;
	}

	public void setDriveTransmissionTypeId(String driveTransmissionTypeId) {
		this.driveTransmissionTypeId = driveTransmissionTypeId;
	}

	public boolean isApprovelStatus() {
		return approvelStatus;
	}

	public void setApprovelStatus(boolean approvelStatus) {
		this.approvelStatus = approvelStatus;
	}

	public double getDriverPayablePercentage() {
		return driverPayablePercentage;
	}

	public void setDriverPayablePercentage(double driverPayablePercentage) {
		this.driverPayablePercentage = driverPayablePercentage;
	}

	public String getInputRegionId() {
		return inputRegionId;
	}

	public void setInputRegionId(String inputRegionId) {
		this.inputRegionId = inputRegionId;
	}

	public String getInputServiceType() {
		return inputServiceType;
	}

	public void setInputServiceType(String inputServiceType) {
		this.inputServiceType = inputServiceType;
	}

	public List<String> getInputCarType() {
		return inputCarType;
	}

	public void setInputCarType(List<String> inputCarType) {
		this.inputCarType = inputCarType;
	}

	public String getAgentNumber() {
		return agentNumber;
	}

	public void setAgentNumber(String agentNumber) {
		this.agentNumber = agentNumber;
	}
}