package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.TourTimeDao;

public class TourTimeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TourTimeModel.class);

	private String tourTimeId;
	private String tourId;
	private long bookingTime;
	private long acceptTime;
	private long arrivedWaitingTime;
	private long startTime;
	private long endTime;

	public String getTourTimeId() {
		return tourTimeId;
	}

	public void setTourTimeId(String tourTimeId) {
		this.tourTimeId = tourTimeId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public long getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(long bookingTime) {
		this.bookingTime = bookingTime;
	}

	public long getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(long acceptTime) {
		this.acceptTime = acceptTime;
	}

	public long getArrivedWaitingTime() {
		return arrivedWaitingTime;
	}

	public void setArrivedWaitingTime(long arrivedWaitingTime) {
		this.arrivedWaitingTime = arrivedWaitingTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String createTourTime() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTimeDao tourTimeDao = session.getMapper(TourTimeDao.class);

		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {

			tourTimeDao.createTourTime(this);
			session.commit();
		} catch (Throwable t) {
			tourId = null;
			session.rollback();
			logger.error("Exception occured during createTourTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.tourId;
	}

	public int updateTourAcceptedTime() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTimeDao tourTimeDao = session.getMapper(TourTimeDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {

			updated = tourTimeDao.updateTourAcceptedTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourAcceptedTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public int updateTourArrivedWaitingTime() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTimeDao tourTimeDao = session.getMapper(TourTimeDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		//this.setUpdatedBy(loggedInuserId);

		try {

			updated = tourTimeDao.updateTourArrivedWaitingTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourArrivedWaitingTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public int updateTourStartTime() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTimeDao tourTimeDao = session.getMapper(TourTimeDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		//this.setUpdatedBy(loggedInuserId);

		try {

			updated = tourTimeDao.updateTourStartTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourStartTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public int updateTourEndTime() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTimeDao tourTimeDao = session.getMapper(TourTimeDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		//this.setUpdatedBy(loggedInuserId);

		try {

			updated = tourTimeDao.updateTourEndTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourEndTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public static TourTimeModel getTourTimesDetailsByTourId(String tourId) {

		TourTimeModel tourTimeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTimeDao tourTimeDao = session.getMapper(TourTimeDao.class);

		try {
			tourTimeModel = tourTimeDao.getTourTimesDetailsByTourId(tourId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTourTimesDetailsByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourTimeModel;
	}

}