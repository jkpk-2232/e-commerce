package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AdminCompanyContactDao;

public class AdminCompanyContactModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminCompanyContactModel.class);

	private String adminCompanyContactId;
	private String userId;
	private String vendorId;
	private String address;
	private String phoneNumberOne;
	private String phoneNumberTwo;
	private String phoneNumberOneCode;
	private String phoneNumberTwoCode;
	private String latitude;
	private String longitude;
	private String geolocation;
	private String fax;
	private String email;

	public String getAdminCompanyContactId() {
		return adminCompanyContactId;
	}

	public void setAdminCompanyContactId(String adminCompanyContactId) {
		this.adminCompanyContactId = adminCompanyContactId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumberOne() {
		return phoneNumberOne;
	}

	public void setPhoneNumberOne(String phoneNumberOne) {
		this.phoneNumberOne = phoneNumberOne;
	}

	public String getPhoneNumberTwo() {
		return phoneNumberTwo;
	}

	public void setPhoneNumberTwo(String phoneNumberTwo) {
		this.phoneNumberTwo = phoneNumberTwo;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public String getPhoneNumberOneCode() {
		return phoneNumberOneCode;
	}

	public void setPhoneNumberOneCode(String phoneNumberOneCode) {
		this.phoneNumberOneCode = phoneNumberOneCode;
	}

	public String getPhoneNumberTwoCode() {
		return phoneNumberTwoCode;
	}

	public void setPhoneNumberTwoCode(String phoneNumberTwoCode) {
		this.phoneNumberTwoCode = phoneNumberTwoCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public int addAdminCompanyContact(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminCompanyContactDao adminCompanyContactDao = session.getMapper(AdminCompanyContactDao.class);

		try {

			this.adminCompanyContactId = UUIDGenerator.generateUUID();
			this.vendorId = userId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			this.setGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.longitude + "  " + this.latitude + ")')");

			status = adminCompanyContactDao.addAdminCompanyContact(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAdminCompanyContact : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static AdminCompanyContactModel getAdminCompanyContactByVendorId(String vendorId) {

		AdminCompanyContactModel adminCompanyContactModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminCompanyContactDao adminCompanyContactDao = session.getMapper(AdminCompanyContactDao.class);

		try {
			adminCompanyContactModel = adminCompanyContactDao.getAdminCompanyContactByVendorId(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminCompanyContactByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminCompanyContactModel;
	}

	public int updateAdminCompanyContact(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminCompanyContactDao adminCompanyContactDao = session.getMapper(AdminCompanyContactDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			this.setGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.longitude + "  " + this.latitude + ")')");

			status = adminCompanyContactDao.updateAdminCompanyContact(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAdminCompanyContact : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}
}