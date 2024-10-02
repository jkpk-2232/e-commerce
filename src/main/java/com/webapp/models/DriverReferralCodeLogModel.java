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
import com.webapp.ProjectConstants;
import com.webapp.daos.DriverReferralCodeLogDao;

public class DriverReferralCodeLogModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverReferralCodeLogModel.class);

	private String driverReferralCodeLogId;
	private String driverId;
	private String passengerId;
	private double driverPercentage;

	private String passengerEmail;
	private String passengerName;
	private String passengerPhoneNumber;

	private String driverEmail;
	private String driverName;
	private String driverPhoneNumber;

	public String getDriverReferralCodeLogId() {
		return driverReferralCodeLogId;
	}

	public void setDriverReferralCodeLogId(String driverReferralCodeLogId) {
		this.driverReferralCodeLogId = driverReferralCodeLogId;
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

	public double getDriverPercentage() {
		return driverPercentage;
	}

	public void setDriverPercentage(double driverPercentage) {
		this.driverPercentage = driverPercentage;
	}

	public String getPassengerEmail() {
		return passengerEmail;
	}

	public void setPassengerEmail(String passengerEmail) {
		this.passengerEmail = passengerEmail;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerPhoneNumber() {
		return passengerPhoneNumber;
	}

	public void setPassengerPhoneNumber(String passengerPhoneNumber) {
		this.passengerPhoneNumber = passengerPhoneNumber;
	}

	public String getDriverEmail() {
		return driverEmail;
	}

	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhoneNumber() {
		return driverPhoneNumber;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		this.driverPhoneNumber = driverPhoneNumber;
	}

	public String addDriverReferralCodeLog(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverReferralCodeLogDao driverReferralCodeLogDao = session.getMapper(DriverReferralCodeLogDao.class);

		try {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			this.driverPercentage = adminSettingsModel.getDriverReferralBenefit();

			this.driverReferralCodeLogId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = driverReferralCodeLogDao.addDriverReferralCodeLog(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addDriverReferralCodeLog : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		if (status > 0) {

			return this.driverReferralCodeLogId;

		} else {

			return null;
		}
	}

	public int deleteDriverReferralCodeLogByPassengerId(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverReferralCodeLogDao driverReferralCodeLogDao = session.getMapper(DriverReferralCodeLogDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);

		try {

			status = driverReferralCodeLogDao.deleteDriverReferralCodeLogByPassengerId(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteDriverReferralCodeLogByPassengerId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static DriverReferralCodeLogModel getDriverReferralCodeLogByPassengerId(String passengerId) {

		DriverReferralCodeLogModel driverReferralCodeLogModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("passengerId", passengerId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverReferralCodeLogDao driverReferralCodeLogDao = session.getMapper(DriverReferralCodeLogDao.class);

		try {

			driverReferralCodeLogModel = driverReferralCodeLogDao.getDriverReferralCodeLogByPassengerId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverReferralCodeLogByPassengerId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverReferralCodeLogModel;
	}

	public static int getTotalDriverReferralLogsCount(String driverId, long startDate, long endDate) {

		int driverReferralLogsCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverReferralCodeLogDao driverReferralCodeLogDao = session.getMapper(DriverReferralCodeLogDao.class);

		try {

			driverReferralLogsCount = driverReferralCodeLogDao.getTotalDriverReferralLogsCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDriverReferralLogsCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverReferralLogsCount;
	}

	public static List<DriverReferralCodeLogModel> getDriverReferralLogsListForSearch(String driverId, int start, int length, String order, String globalSearchString, long startDate, long endDate) {

		List<DriverReferralCodeLogModel> driverReferralCodeLogModelList = new ArrayList<DriverReferralCodeLogModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverReferralCodeLogDao driverReferralCodeLogDao = session.getMapper(DriverReferralCodeLogDao.class);

		try {

			driverReferralCodeLogModelList = driverReferralCodeLogDao.getDriverReferralLogsListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverReferralLogsListForSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverReferralCodeLogModelList;
	}
}