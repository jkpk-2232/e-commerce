package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.DriverDao;

public class DriverModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverModel.class);

	private String bookingRequestId;
	private String driverJobId;
	private String driverId;
	private String passengerId;
	private long dateTime;
	private String sourceAddress;
	private String destinationAddress;
	private String carTypeId;
	private String passengerName;
	private String driverJobRequestStatus;
	private String bookingRequestStatus;
	private String requestStatus;
	private String driverReferralCode;

	public static List<DriverModel> getDriverJobHistory(String driverId, long updatedAt) {

		List<DriverModel> driverJobHistoryList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDao driverDao = session.getMapper(DriverDao.class);

		try {

			driverJobHistoryList = driverDao.getDriverJobHistory(driverId, updatedAt);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverJobHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverJobHistoryList;
	}

	public static DriverModel getDriverJobDetails(String bookingRequestId) {

		DriverModel driverModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDao driverDao = session.getMapper(DriverDao.class);

		try {
			driverModel = driverDao.getDriverJobDetails(bookingRequestId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during acceptTrip : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return driverModel;
	}

	public static DriverModel getDriverDetailsByReferralCode(String driverReferralCode, String applicationStatus) {

		DriverModel driverModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverDao driverDao = session.getMapper(DriverDao.class);

		try {
			driverModel = driverDao.getDriverDetailsByReferralCode(driverReferralCode, applicationStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDetailsByReferralCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverModel;
	}

	public String getBookingRequestId() {
		return bookingRequestId;
	}

	public void setBookingRequestId(String bookingRequestId) {
		this.bookingRequestId = bookingRequestId;
	}

	public String getDriverJobId() {
		return driverJobId;
	}

	public void setDriverJobId(String driverJobId) {
		this.driverJobId = driverJobId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
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

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getDriverJobRequestStatus() {
		return driverJobRequestStatus;
	}

	public void setDriverJobRequestStatus(String driverJobRequestStatus) {
		this.driverJobRequestStatus = driverJobRequestStatus;
	}

	public String getBookingRequestStatus() {
		return bookingRequestStatus;
	}

	public void setBookingRequestStatus(String bookingRequestStatus) {
		this.bookingRequestStatus = bookingRequestStatus;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getDriverReferralCode() {
		return driverReferralCode;
	}

	public void setDriverReferralCode(String driverReferralCode) {
		this.driverReferralCode = driverReferralCode;
	}
}
