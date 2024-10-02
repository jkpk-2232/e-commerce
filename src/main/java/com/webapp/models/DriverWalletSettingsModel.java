package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.DriverWalletSettingsDao;

public class DriverWalletSettingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverWalletSettingsModel.class);

	private String driverWalletSettingId;

	private double minimumAmount;

	private double notifyAmount;

	private boolean isActive;

	public String getDriverWalletSettingId() {
		return driverWalletSettingId;
	}

	public void setDriverWalletSettingId(String driverWalletSettingId) {
		this.driverWalletSettingId = driverWalletSettingId;
	}

	public double getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(double minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public double getNotifyAmount() {
		return notifyAmount;
	}

	public void setNotifyAmount(double notifyAmount) {
		this.notifyAmount = notifyAmount;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int updateDriverWalletSettings(String loggedInUserId) {

		int updateStatus = 0;

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverWalletSettingsDao driverWalletSettingsDao = session.getMapper(DriverWalletSettingsDao.class);

		try {
			updateStatus = driverWalletSettingsDao.updateDriverWalletSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverWalletSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}

	public static DriverWalletSettingsModel getDriverWalletSettings() {

		DriverWalletSettingsModel driverWalletSettingsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverWalletSettingsDao driverWalletSettingsDao = session.getMapper(DriverWalletSettingsDao.class);

		try {
			driverWalletSettingsModel = driverWalletSettingsDao.getDriverWalletSettings();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverWalletSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverWalletSettingsModel;
	}

}