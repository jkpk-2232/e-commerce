package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.TrackMyLocationDao;

public class TrackMyLocationModel extends AbstractModel {
	private static Logger logger = Logger.getLogger(EmergencyNumbersPersonalModel.class);

	private String passengerId;
	private String passengerName;
	private String passengerPhoneNo;
	private String passengerEmail;

	private String driverId;
	private String driverName;
	private String driverPhoneNo;
	private String driverEmail;
	private double driverAvgRating;
	private String driverPhotoUrl;

	private String tourId;
	private String sourceAddress;
	private double sourceLatitude;
	private double sourceLongitude;
	private String destinationAddress;
	private double destLatitude;
	private double destLongitude;
	private String tourStatus;

	private String carId;
	private String carType;
	private String carModel;
	private String carColor;
	private String carPlateNo;

	private String contactNumber;
	private String supportEmail;

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerPhoneNo() {
		return passengerPhoneNo;
	}

	public void setPassengerPhoneNo(String passengerPhoneNo) {
		this.passengerPhoneNo = passengerPhoneNo;
	}

	public String getPassengerEmail() {
		return passengerEmail;
	}

	public void setPassengerEmail(String passengerEmail) {
		this.passengerEmail = passengerEmail;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public double getSourceLatitude() {
		return sourceLatitude;
	}

	public void setSourceLatitude(double sourceLatitude) {
		this.sourceLatitude = sourceLatitude;
	}

	public double getSourceLongitude() {
		return sourceLongitude;
	}

	public void setSourceLongitude(double sourceLongitude) {
		this.sourceLongitude = sourceLongitude;
	}

	public double getDestLatitude() {
		return destLatitude;
	}

	public void setDestLatitude(double destLatitude) {
		this.destLatitude = destLatitude;
	}

	public double getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(double destLongitude) {
		this.destLongitude = destLongitude;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverPhoneNo() {
		return driverPhoneNo;
	}

	public void setDriverPhoneNo(String driverPhoneNo) {
		this.driverPhoneNo = driverPhoneNo;
	}

	public String getDriverEmail() {
		return driverEmail;
	}

	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}

	public double getDriverAvgRating() {
		return driverAvgRating;
	}

	public void setDriverAvgRating(double driverAvgRating) {
		this.driverAvgRating = driverAvgRating;
	}

	public String getDriverPhotoUrl() {
		return driverPhotoUrl;
	}

	public void setDriverPhotoUrl(String driverPhotoUrl) {
		this.driverPhotoUrl = driverPhotoUrl;
	}

	public String getTourStatus() {
		return tourStatus;
	}

	public void setTourStatus(String tourStatus) {
		this.tourStatus = tourStatus;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getSupportEmail() {
		return supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public String getCarPlateNo() {
		return carPlateNo;
	}

	public void setCarPlateNo(String carPlateNo) {
		this.carPlateNo = carPlateNo;
	}

	public static TrackMyLocationModel getPassengerLastUpdatedLocationByUserId(String userId) {

		TrackMyLocationModel trackMyLocationModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TrackMyLocationDao trackMyLocationDao = session.getMapper(TrackMyLocationDao.class);

		try {
			trackMyLocationModel = trackMyLocationDao.getPassengerLastUpdatedLocationByUserId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassengerLastUpdatedLocationByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return trackMyLocationModel;
	}

	public static TrackMyLocationModel getPassengerTourDetailsByTourId(String tourId) {

		TrackMyLocationModel trackMyLocationModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TrackMyLocationDao trackMyLocationDao = session.getMapper(TrackMyLocationDao.class);

		try {
			trackMyLocationModel = trackMyLocationDao.getPassengerTourDetailsByTourId(tourId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassengerTourDetailsByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return trackMyLocationModel;
	}

	public static TrackMyLocationModel getDriverUpdatedLocationWithDetailsById(String userId) {

		TrackMyLocationModel trackMyLocationModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TrackMyLocationDao trackMyLocationDao = session.getMapper(TrackMyLocationDao.class);

		try {
			trackMyLocationModel = trackMyLocationDao.getDriverUpdatedLocationWithDetailsById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverUpdatedLocationWithDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return trackMyLocationModel;
	}

}