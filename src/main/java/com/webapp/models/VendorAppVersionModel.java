package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.VendorAppVersionDao;

public class VendorAppVersionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VersionModel.class);

	private String vendorAppVersionId;
	private String version;
	private String releaseNote;
	private boolean isMandatory;
	private long removeSupportFrom;
	private long releaseDate;
	private String deviceType;
	private String appLink;

	public static VendorAppVersionModel getLatestVendorAppVersion(String deviceType) {

		VendorAppVersionModel vendorAppVersionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAppVersionDao vendorAppVersionDao = session.getMapper(VendorAppVersionDao.class);

		try {
			vendorAppVersionModel = vendorAppVersionDao.getLatestVendorAppVersion(deviceType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLatestVendorAppVersion : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorAppVersionModel;
	}

	public static boolean isMandatoryReleaseAvailableAfterThisRelease(String deviceType, long releaseDate) {

		boolean isAvailable = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAppVersionDao vendorAppVersionDao = session.getMapper(VendorAppVersionDao.class);

		try {
			isAvailable = vendorAppVersionDao.isMandatoryReleaseAvailableAfterThisRelease(deviceType, releaseDate);
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

	public static VendorAppVersionModel getVendorAppVersionByVersion(String deviceType, String version) {

		VendorAppVersionModel vendorAppVersionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAppVersionDao vendorAppVersionDao = session.getMapper(VendorAppVersionDao.class);

		try {
			vendorAppVersionModel = vendorAppVersionDao.getVendorAppVersionByVersion(deviceType, version);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorAppVersionByVersion : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorAppVersionModel;
	}

	public String getVendorAppVersionId() {
		return vendorAppVersionId;
	}

	public void setVendorAppVersionId(String vendorAppVersionId) {
		this.vendorAppVersionId = vendorAppVersionId;
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
}