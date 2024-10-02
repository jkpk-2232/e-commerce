package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.FreeWaitingTimeDao;

public class FreeWaitingTimeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(FreeWaitingTimeModel.class);

	private String freeWaitingTimeId;
	private String userId;
	private double waitingTime;
	private double cancelTime;

	public int updateWaitingTime() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FreeWaitingTimeDao freeWaitingTimeDao = session.getMapper(FreeWaitingTimeDao.class);
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(this.getUserId());

		try {
			status = freeWaitingTimeDao.updateWaitingTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateWaitingTime :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static FreeWaitingTimeModel getFreeWaitingTime() {

		FreeWaitingTimeModel freeWaitingTimeModel = new FreeWaitingTimeModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FreeWaitingTimeDao freeWaitingTimeDao = session.getMapper(FreeWaitingTimeDao.class);

		try {
			freeWaitingTimeModel = freeWaitingTimeDao.getFreeWaitingTime();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFreeWaitingTime :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return freeWaitingTimeModel;

	}

	public int updateCancelTime() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FreeWaitingTimeDao freeWaitingTimeDao = session.getMapper(FreeWaitingTimeDao.class);

		try {
			status = freeWaitingTimeDao.updateCancelTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCancelTime :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public String getFreeWaitingTimeId() {
		return freeWaitingTimeId;
	}

	public void setFreeWaitingTimeId(String freeWaitingTimeId) {
		this.freeWaitingTimeId = freeWaitingTimeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}

	public double getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(double cancelTime) {
		this.cancelTime = cancelTime;
	}
}