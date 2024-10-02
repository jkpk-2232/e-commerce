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
import com.utils.UUIDGenerator;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.webapp.ProjectConstants;
import com.webapp.daos.DriverGeoLocationDao;

public class DriverGeoLocationModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverGeoLocationModel.class);

	private String driverGpsId;
	private long sentAt;
	private String driverId;
	private String tourId;
	private String latitude;
	private String longitude;
	private String carLocation;
	private String driverName;
	private String phoneNo;
	private String photoUrl;
	private String driverTourStatus;
	private String icon;
	private String carType;
	private String carTypeId;
	private String carTypeIconImage;

	public static List<DriverGeoLocationModel> getNearByCarList(String latAndLong, String distance, String regionId, List<String> carTypeIds, String transmissionTypeId, long timeBeforeDriverIdealTimeInMillis, double minimumWalletAmount, String vendorId, long time,
				AdminSettingsModel adminSettingsModel) {

		List<DriverGeoLocationModel> carLocarion = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("regionId", regionId);
		inputMap.put("carTypeIds", carTypeIds);
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("transmissionTypeIdList", getDriveTransmissionListFromType(transmissionTypeId, null));
		inputMap.put("minimumWalletAmount", minimumWalletAmount);
		inputMap.put("vendorId", vendorId);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, vendorId, DateUtils.nowAsGmtMillisec());

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			carLocarion = carGeoDao.getNearByCarList(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNearByCarList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carLocarion;
	}

	public static List<DriverGeoLocationModel> getNearByAvailableCarListForWeb(Map<String, Object> inputMap) {

		List<DriverGeoLocationModel> availableCarList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			availableCarList = carGeoDao.getNearByAvailableCarListForWeb(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNearByAvailableCarListForWeb : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return availableCarList;
	}

	// This method is only called for admin
	public static int getTotalAvailableDriver(List<String> assignedRegionList, double minimumWalletAmount, String userId, AdminSettingsModel adminSettingsModel) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("minimumWalletAmount", minimumWalletAmount);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, userId, DateUtils.nowAsGmtMillisec());

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			count = carGeoDao.getTotalAvailableDriver(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalAvailableDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static DriverGeoLocationModel getNearestSingledriver(Map<String, Object> inputMap) {

		DriverGeoLocationModel availableCarList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			availableCarList = carGeoDao.getNearestSingledriver(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNearByAvailableCarList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return availableCarList;
	}

	public static DriverGeoLocationModel getFavouriteNearestSingledriver(Map<String, Object> inputMap) {

		DriverGeoLocationModel availableCarList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			availableCarList = carGeoDao.getFavouriteNearestSingledriver(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFavouriteNearestSingledriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return availableCarList;
	}

	public static DriverGeoLocationModel getNearestSingleDriverForOrders(Map<String, Object> inputMap) {

		DriverGeoLocationModel availableCarList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			availableCarList = carGeoDao.getNearestSingleDriverForOrders(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNearestSingleDriverForOrders : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return availableCarList;
	}

	public static DriverGeoLocationModel getCurrentDriverPosition(String driverId) {

		DriverGeoLocationModel availableCarList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);
		try {
			availableCarList = carGeoDao.getCurrentDriverPosition(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrentDriverPosition : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return availableCarList;
	}

	public static String isSessionExists(String sessionKey) {

		String availableCarList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);
		try {
			availableCarList = carGeoDao.isSessionExists(sessionKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isSessionExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return availableCarList;
	}

	public void saveCarLocation(String driverId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoLocationDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			this.setCarLocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getLongitude() + "  " + this.getLatitude() + ")')");
			this.sentAt = DateUtils.nowAsGmtMillisec();
			this.setDriverId(driverId);

			TourModel tour = TourModel.getCurrentTourByDriverId(driverId);

			if (tour != null) {

				this.tourId = tour.getTourId();

			} else {

				this.tourId = "-1";
			}

			boolean driverHasMultipleLocations = carGeoLocationDao.checkDriverHasMultipleEntries(driverId);

			if (driverHasMultipleLocations) {

				carGeoLocationDao.deleteDriverGeoLocations(driverId);

				this.setDriverGpsId(UUIDGenerator.generateUUID());
				carGeoLocationDao.saveCarGeoLocation(this);

			} else {

				DriverGeoLocationModel currentDriverLocation = carGeoLocationDao.getCurrentDriverPosition(driverId);

				if (currentDriverLocation != null) {

					this.setDriverGpsId(currentDriverLocation.getDriverGpsId());
					carGeoLocationDao.updateDriverGeoLocation(this);

				} else {

					this.setDriverGpsId(UUIDGenerator.generateUUID());
					carGeoLocationDao.saveCarGeoLocation(this);
				}
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured saveCarLocation : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}
	}

	public static List<DriverGeoLocationModel> getIdealDriverListForCronJob(long idealTimeInMillis) {

		List<DriverGeoLocationModel> carLocarion = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("idealTimeInMillis", idealTimeInMillis);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {

			carLocarion = carGeoDao.getIdealDriverListForCronJob(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getIdealDriverListForCronJob : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carLocarion;
	}

	public static List<DriverGeoLocationModel> getNearByCarDriverList(String latAndLong, String distance, String regionId, String carTypeId, String transmissionTypeId, long timeBeforeDriverIdealTimeInMillis, String vendorId, String tempCarTypeId, double minimumWalletAmount,
				AdminSettingsModel adminSettingsModel) {

		List<DriverGeoLocationModel> carLocarion = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("regionId", regionId);
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("transmissionTypeIdList", getDriveTransmissionListFromType(transmissionTypeId, tempCarTypeId));
		inputMap.put("minimumWalletAmount", minimumWalletAmount);
		inputMap.put("vendorId", vendorId);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, vendorId, DateUtils.nowAsGmtMillisec());

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			carLocarion = carGeoDao.getNearByCarDriverList(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNearByCarDriverList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carLocarion;
	}

	public static int getVendorsTotalAvailableDriver(List<String> assignedRegionList, String vendorId, double minimumWalletAmount, AdminSettingsModel adminSettingsModel) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("minimumWalletAmount", minimumWalletAmount);
		inputMap.put("vendorId", vendorId);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, vendorId, DateUtils.nowAsGmtMillisec());

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			count = carGeoDao.getVendorsTotalAvailableDriver(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorsTotalAvailableDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<DriverGeoLocationModel> getVendorsNearByCarList(String latAndLong, String distance, String regionId, String carTypeId, long timeBeforeDriverIdealTimeInMillis, String userId, double minimumWalletAmount, String vendorId, long time,
				AdminSettingsModel adminSettingsModel) {

		List<DriverGeoLocationModel> carLocarion = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("regionId", regionId);
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("vendorId", vendorId);
		inputMap.put("minimumWalletAmount", minimumWalletAmount);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, vendorId, DateUtils.nowAsGmtMillisec());

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverGeoLocationDao carGeoDao = session.getMapper(DriverGeoLocationDao.class);

		try {
			carLocarion = carGeoDao.getVendorsNearByCarList(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorsNearByCarList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carLocarion;
	}

	public static List<String> getDriveTransmissionListFromType(String transmissionType, String carTypeId) {

		List<String> result = new ArrayList<String>();

		if (carTypeId == null) {
			return null;
		}

		if (!carTypeId.equals("5")) {
			return null;
		}

		if (transmissionType == null) {
			return null;
		}

		if (transmissionType.trim().equals("")) {
			return null;
		}

		result.add(transmissionType);
		result.add(ProjectConstants.TRANSMISSION_TYPE_BOTH_ID);

		return result;
	}

	public long getSentAt() {
		return sentAt;
	}

	public void setSentAt(long sentAt) {
		this.sentAt = sentAt;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCarLocation() {
		return carLocation;
	}

	public void setCarLocation(String carLocation) {
		this.carLocation = carLocation;
	}

	public String getDriverGpsId() {
		return driverGpsId;
	}

	public void setDriverGpsId(String driverGpsId) {
		this.driverGpsId = driverGpsId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getDriverTourStatus() {
		return driverTourStatus;
	}

	public void setDriverTourStatus(String driverTourStatus) {
		this.driverTourStatus = driverTourStatus;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarTypeIconImage() {
		return carTypeIconImage;
	}

	public void setCarTypeIconImage(String carTypeIconImage) {
		this.carTypeIconImage = carTypeIconImage;
	}
}