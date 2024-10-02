package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorAdminSettingsDao;

public class VendorAdminSettingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorAdminSettingsModel.class);

	private String vendorAdminSettingsId;
	private String vendorId;
	private String aboutUs;
	private String privacyPolicy;
	private String termsConditions;
	private String refundPolicy;
	private String driverFare;

	public static VendorAdminSettingsModel getVendorAdminSettingsDetailsByVendorId(String vendorId) {

		VendorAdminSettingsModel adminSettingsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAdminSettingsDao vendorAdminSettingsDao = session.getMapper(VendorAdminSettingsDao.class);

		try {
			adminSettingsModel = vendorAdminSettingsDao.getVendorAdminSettingsDetailsByVendorId(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorAdminSettingsDetailsByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminSettingsModel;
	}

	public void updateVendorAdminSettings(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAdminSettingsDao vendorAdminSettingsDao = session.getMapper(VendorAdminSettingsDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			vendorAdminSettingsDao.updateVendorAdminSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorAdminSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void insertVendorAdminSettings(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAdminSettingsDao vendorAdminSettingsDao = session.getMapper(VendorAdminSettingsDao.class);

		this.vendorAdminSettingsId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = userId;
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			vendorAdminSettingsDao.insertVendorAdminSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorAdminSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorAdminSettingsId() {
		return vendorAdminSettingsId;
	}

	public void setVendorAdminSettingsId(String vendorAdminSettingsId) {
		this.vendorAdminSettingsId = vendorAdminSettingsId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	public String getPrivacyPolicy() {
		return privacyPolicy;
	}

	public void setPrivacyPolicy(String privacyPolicy) {
		this.privacyPolicy = privacyPolicy;
	}

	public String getTermsConditions() {
		return termsConditions;
	}

	public void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public String getRefundPolicy() {
		return refundPolicy;
	}

	public void setRefundPolicy(String refundPolicy) {
		this.refundPolicy = refundPolicy;
	}

	public String getDriverFare() {
		return driverFare;
	}

	public void setDriverFare(String driverFare) {
		this.driverFare = driverFare;
	}
}