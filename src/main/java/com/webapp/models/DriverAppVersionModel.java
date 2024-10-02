package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.DriverAppVersionDao;

public class DriverAppVersionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VersionModel.class);

	private String driverAppVersionId;

	private String version;

	private String releaseNote;

	private boolean isMandatory;

	private long removeSupportFrom;

	private long releaseDate;

	private String deviceType;

	private String appLink;

	private String recordStatus;

	public String getDriverAppVersionId() {
		return driverAppVersionId;
	}

	public void setDriverAppVersionId(String driverAppVersionId) {
		this.driverAppVersionId = driverAppVersionId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getReleaseNote() {
		return releaseNote;
	}

	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public long getRemoveSupportFrom() {
		return removeSupportFrom;
	}

	public void setRemoveSupportFrom(long removeSupportFrom) {
		this.removeSupportFrom = removeSupportFrom;
	}

	public long getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(long releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAppLink() {
		return appLink;
	}

	public void setAppLink(String appLink) {
		this.appLink = appLink;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public static DriverAppVersionModel getLatestDriverAppVersion(String deviceType) {

		DriverAppVersionModel driverAppVersionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverAppVersionDao driverAppVersionDao = session.getMapper(DriverAppVersionDao.class);

		try {

			driverAppVersionModel = driverAppVersionDao.getLatestDriverAppVersion(deviceType);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getLatestDriverAppVersion : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverAppVersionModel;
	}

	public static boolean isMandatoryReleaseAvailableAfterThisRelease(String deviceType, long releaseDate) {

		boolean isAvailable = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverAppVersionDao driverAppVersionDao = session.getMapper(DriverAppVersionDao.class);

		try {

			isAvailable = driverAppVersionDao.isMandatoryReleaseAvailableAfterThisRelease(deviceType, releaseDate);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during isMandatoryReleaseAvailableAfterThisRelease : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return isAvailable;
	}

	public static DriverAppVersionModel getDriverAppVersionByVersion(String deviceType, String version) {

		DriverAppVersionModel driverAppVersionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverAppVersionDao driverAppVersionDao = session.getMapper(DriverAppVersionDao.class);

		try {

			driverAppVersionModel = driverAppVersionDao.getDriverAppVersionByVersion(deviceType, version);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverAppVersionByVersion : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return driverAppVersionModel;
	}

}