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
import com.webapp.daos.PassengerTripRatingsDao;

public class PassengerTripRatingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(PassengerTripRatingsModel.class);

	private String passengerTripRatingsId;
	private String passengerId;
	private String tripId;
	private String driverId;
	private String note;
	private double rate;

	private String driverJobId;

	public int ratingsByPassenger(String loggedInuserId, String driverId) throws SQLException {

		int status = 0;

		String passengerTripRatingsId = UUIDGenerator.generateUUID();
		this.setPassengerTripRatingsId(passengerTripRatingsId);
		this.setPassengerId(loggedInuserId);
		this.setDriverId(driverId);
		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setCreatedBy(loggedInuserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerTripRatingsDao passengerTripRatingsDao = session.getMapper(PassengerTripRatingsDao.class);

		try {

			status = passengerTripRatingsDao.ratingsByPassenger(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during ratingsByPassenger : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static String getDriverIdFromTours(String tripId) {

		String driverId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerTripRatingsDao passengerTripRatingsDao = session.getMapper(PassengerTripRatingsDao.class);

		try {

			driverId = passengerTripRatingsDao.getDriverIdFromTours(tripId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverIdFromTours : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverId;

	}

	public static PassengerTripRatingsModel getPassenerRatingsByTripId(String tripId) {

		PassengerTripRatingsModel passengerRating = new PassengerTripRatingsModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerTripRatingsDao passengerTripRatingsDao = session.getMapper(PassengerTripRatingsDao.class);

		try {

			passengerRating = passengerTripRatingsDao.getPassenerRatingsByTripId(tripId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassenerRatingsByTripId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return passengerRating;

	}

	public static List<PassengerTripRatingsModel> getAlldriverRatings(String driverId) {

		List<PassengerTripRatingsModel> passRatingList = new ArrayList<PassengerTripRatingsModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerTripRatingsDao passengerTripRatingsDao = session.getMapper(PassengerTripRatingsDao.class);

		try {
			passRatingList = passengerTripRatingsDao.getAlldriverRatings(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAlldriverRatings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return passRatingList;

	}

	public static int checkRating(String driverId, String passangerId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerTripRatingsDao passengerTripRatingsDao = session.getMapper(PassengerTripRatingsDao.class);

		try {
			count = passengerTripRatingsDao.checkRating(driverId, passangerId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAlldriverRatings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;

	}

	public static PassengerTripRatingsModel getPassengerRatingsForTour(String tripId) {

		PassengerTripRatingsModel passRating = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerTripRatingsDao passengerTripRatingsDao = session.getMapper(PassengerTripRatingsDao.class);

		try {
			passRating = passengerTripRatingsDao.getPassengerRatingsForTour(tripId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassengerRatingsForTour : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return passRating;
	}

	public String getPassengerTripRatingsId() {
		return passengerTripRatingsId;
	}

	public void setPassengerTripRatingsId(String passengerTripRatingsId) {
		this.passengerTripRatingsId = passengerTripRatingsId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
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
