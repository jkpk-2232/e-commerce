package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.VersionDao;

public class VersionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VersionModel.class);

	private String versionId;

	private String version;

	private String releaseNote;

	private boolean isMandatory;

	private long removeSupportFrom;

	private long releaseDate;

	private String deviceType;

	private String appLink;

	private String recordStatus;

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
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

	public static VersionModel getLatestVersion(String deviceType) {

		VersionModel versionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		VersionDao versionDao = session.getMapper(VersionDao.class);

		try {
			versionModel = versionDao.getLatestVersion(deviceType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLatestVersion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return versionModel;
	}

	public static boolean isMandatoryReleaseAvailableAfterThisRelease(String deviceType, long releaseDate) {

		boolean isAvailable = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		VersionDao versionDao = session.getMapper(VersionDao.class);

		try {
			isAvailable = versionDao.isMandatoryReleaseAvailableAfterThisRelease(deviceType, releaseDate);
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

	public static VersionModel getVersionByVersion(String deviceType, String version) {

		VersionModel versionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		VersionDao versionDao = session.getMapper(VersionDao.class);

		try {
			versionModel = versionDao.getVersionByVersion(deviceType, version);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVersionByVersion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return versionModel;
	}
}