package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.DriverPayablePercentageDao;

public class DriverPayablePercentageModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverPayablePercentageModel.class);

	private String driverPayablePercentageId;
	private String userId;
	private double percentage;

	public int updatePayablePercentage() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverPayablePercentageDao driverPayableDao = session.getMapper(DriverPayablePercentageDao.class);
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(this.getUserId());

		try {
			status = driverPayableDao.updatePayablePercentage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePayablePercentage :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return status;

	}

	public static DriverPayablePercentageModel getPayablePercentage() {

		DriverPayablePercentageModel payablePercentage = new DriverPayablePercentageModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverPayablePercentageDao driverPayableDao = session.getMapper(DriverPayablePercentageDao.class);

		try {
			payablePercentage = driverPayableDao.getPayablePercentage();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPayablePercentage :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return payablePercentage;

	}

	public String getDriverPayablePercentageId() {
		return driverPayablePercentageId;
	}

	public void setDriverPayablePercentageId(String driverPayablePercentageId) {
		this.driverPayablePercentageId = driverPayablePercentageId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
