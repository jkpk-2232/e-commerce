package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.TrackLocationTokenDao;

public class TrackLocationTokenModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(EmergencyNumbersPersonalModel.class);

	private String trackLocationTokenId;
	private String userId;
	private String tourId;

	public String getTrackLocationTokenId() {
		return trackLocationTokenId;
	}

	public void setTrackLocationTokenId(String trackLocationTokenId) {
		this.trackLocationTokenId = trackLocationTokenId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String insertTrackLocationDetails() {
		
		int status = 0;

		String token = UUIDGenerator.generateUUID();
		long currentTime = DateUtils.nowAsGmtMillisec();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TrackLocationTokenDao trackLocationDetailsDao = session.getMapper(TrackLocationTokenDao.class);

		try {

			this.trackLocationTokenId = token;
			this.recordStatus = ACTIVE_RECORD_STATUS;
			this.createdAt = currentTime;
			this.createdBy = this.userId;
			this.updatedAt = currentTime;
			this.updatedBy = this.userId;

			status = trackLocationDetailsDao.insertTrackLocationDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertTrackLocationDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		if (status > 0) {
			return token;
		} else {
			return null;
		}
	}

	public static TrackLocationTokenModel getTrackLocationTokenDetailsById(String trackLocationTokenId) {
		
		TrackLocationTokenModel trackLocationTokenModel = new TrackLocationTokenModel();
		trackLocationTokenModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TrackLocationTokenDao trackLocationDetailsDao = session.getMapper(TrackLocationTokenDao.class);

		try {
			trackLocationTokenModel = trackLocationDetailsDao.getTrackLocationTokenDetailsById(trackLocationTokenId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTrackLocationTokenDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return trackLocationTokenModel;
	}

}
