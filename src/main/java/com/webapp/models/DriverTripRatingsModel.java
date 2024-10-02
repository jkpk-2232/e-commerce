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
import com.webapp.daos.DriverTripRatingsDao;

public class DriverTripRatingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverTripRatingsModel.class);

	private String driverTripRatingsId;
	private String driverId;
	private String tripId;
	private String passengerId;
	private String note;
	private double rate;

	private String driverJobId;

	public int ratingsByDriver(String loggedInuserId, String passengerId) throws SQLException {

		int status = 0;

		String driverTripRatingsId = UUIDGenerator.generateUUID();
		this.setDriverTripRatingsId(driverTripRatingsId);
		this.setDriverId(loggedInuserId);
		this.setPassengerId(passengerId);
		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setCreatedBy(loggedInuserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTripRatingsDao driverTripRatingsDao = session.getMapper(DriverTripRatingsDao.class);

		try {
			status = driverTripRatingsDao.ratingsByDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during ratingsByDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static String getPassengerIdFromTours(String tripId) {

		String passengerId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTripRatingsDao driverTripRatingsDao = session.getMapper(DriverTripRatingsDao.class);

		try {

			passengerId = driverTripRatingsDao.getPassengerIdFromTours(tripId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverIdFromTours : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return passengerId;
	}

	public static DriverTripRatingsModel getRatingDetailsByTourId(String tripId) {

		DriverTripRatingsModel ratingDetails = new DriverTripRatingsModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTripRatingsDao driverTripRatingsDao = session.getMapper(DriverTripRatingsDao.class);

		try {
			ratingDetails = driverTripRatingsDao.getRatingDetailsByTourId(tripId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverIdFromTours : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return ratingDetails;
	}

	public static List<DriverTripRatingsModel> getDriversTripRatingsList(String driverId) {

		List<DriverTripRatingsModel> driverTripRatingsModelList = new ArrayList<DriverTripRatingsModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTripRatingsDao driverTripRatingsDao = session.getMapper(DriverTripRatingsDao.class);

		try {
			driverTripRatingsModelList = driverTripRatingsDao.getDriversTripRatingsList(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriversTripRatingsList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverTripRatingsModelList;
	}

	public static int checkRating(String driverId, String passangerId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTripRatingsDao driverTripRatingsDao = session.getMapper(DriverTripRatingsDao.class);

		try {
			count = driverTripRatingsDao.checkRating(driverId, passangerId);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkRating : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<DriverTripRatingsModel> getAllPassangerRatings(String passangerId) {

		List<DriverTripRatingsModel> driverTripRatingsModelList = new ArrayList<DriverTripRatingsModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverTripRatingsDao driverTripRatingsDao = session.getMapper(DriverTripRatingsDao.class);

		try {
			driverTripRatingsModelList = driverTripRatingsDao.getAllPassangerRatings(passangerId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriversTripRatingsList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverTripRatingsModelList;
	}

	public String getDriverTripRatingsId() {
		return driverTripRatingsId;
	}

	public void setDriverTripRatingsId(String driverTripRatingsId) {
		this.driverTripRatingsId = driverTripRatingsId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getDriverJobId() {
		return driverJobId;
	}

	public void setDriverJobId(String driverJobId) {
		this.driverJobId = driverJobId;
	}
}
