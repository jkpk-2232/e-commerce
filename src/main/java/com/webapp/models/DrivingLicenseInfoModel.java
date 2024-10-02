package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DrivingLicenseInfoDao;

public class DrivingLicenseInfoModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DrivingLicenseInfoModel.class);

	private String drivingLicenseInfoId;
	private String fName;
	private String mName;
	private String lName;
	private String driverLicenseCardNumber;
	private long dob;
	private String socialSecurityNumber;
	private long insuranceEffectiveDate;
	private long insuranceExpirationDate;
	private String insurancePhotoUrl;
	private String drivingLicensePhotoUrl;
	private String userId;

	private String drivingLicenseBackPhotoUrl;
	private String birthAccreditationPassportPhotoUrl;
	private String criminalHistoryPhotoUrl;
	private String auBusinessNo;

	private String socilaSecurityPhotoUrl;

	private long licenceExpirationDate;

	private String licenceExpirationDateString;
	private String dobString;
	private String insuranceEffectiveDateString;
	private String insuranceExpirationDateString;

	public String getSocilaSecurityPhotoUrl() {
		return socilaSecurityPhotoUrl;
	}

	public void setSocilaSecurityPhotoUrl(String socilaSecurityPhotoUrl) {
		this.socilaSecurityPhotoUrl = socilaSecurityPhotoUrl;
	}

	public long getLicenceExpirationDate() {
		return licenceExpirationDate;
	}

	public void setLicenceExpirationDate(long licenceExpirationDate) {
		this.licenceExpirationDate = licenceExpirationDate;
	}

	public String getDrivingLicenseBackPhotoUrl() {
		return drivingLicenseBackPhotoUrl;
	}

	public void setDrivingLicenseBackPhotoUrl(String drivingLicenseBackPhotoUrl) {
		this.drivingLicenseBackPhotoUrl = drivingLicenseBackPhotoUrl;
	}

	public String getBirthAccreditationPassportPhotoUrl() {
		return birthAccreditationPassportPhotoUrl;
	}

	public void setBirthAccreditationPassportPhotoUrl(String birthAccreditationPassportPhotoUrl) {
		this.birthAccreditationPassportPhotoUrl = birthAccreditationPassportPhotoUrl;
	}

	public String getCriminalHistoryPhotoUrl() {
		return criminalHistoryPhotoUrl;
	}

	public void setCriminalHistoryPhotoUrl(String criminalHistoryPhotoUrl) {
		this.criminalHistoryPhotoUrl = criminalHistoryPhotoUrl;
	}

	public String getAuBusinessNo() {
		return auBusinessNo;
	}

	public void setAuBusinessNo(String auBusinessNo) {
		this.auBusinessNo = auBusinessNo;
	}

	public String getDrivingLicenseInfoId() {
		return drivingLicenseInfoId;
	}

	public void setDrivingLicenseInfoId(String drivingLicenseInfoId) {
		this.drivingLicenseInfoId = drivingLicenseInfoId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getDriverLicenseCardNumber() {
		return driverLicenseCardNumber;
	}

	public void setDriverLicenseCardNumber(String driverLicenseCardNumber) {
		this.driverLicenseCardNumber = driverLicenseCardNumber;
	}

	public long getDob() {
		return dob;
	}

	public void setDob(long dob) {
		this.dob = dob;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public long getInsuranceEffectiveDate() {
		return insuranceEffectiveDate;
	}

	public void setInsuranceEffectiveDate(long insuranceEffectiveDate) {
		this.insuranceEffectiveDate = insuranceEffectiveDate;
	}

	public long getInsuranceExpirationDate() {
		return insuranceExpirationDate;
	}

	public void setInsuranceExpirationDate(long insuranceExpirationDate) {
		this.insuranceExpirationDate = insuranceExpirationDate;
	}

	public String getInsurancePhotoUrl() {
		return insurancePhotoUrl;
	}

	public void setInsurancePhotoUrl(String insurancePhotoUrl) {
		this.insurancePhotoUrl = insurancePhotoUrl;
	}

	public String getDrivingLicensePhotoUrl() {
		return drivingLicensePhotoUrl;
	}

	public void setDrivingLicensePhotoUrl(String drivingLicensePhotoUrl) {
		this.drivingLicensePhotoUrl = drivingLicensePhotoUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLicenceExpirationDateString() {
		return licenceExpirationDateString;
	}

	public void setLicenceExpirationDateString(String licenceExpirationDateString) {
		this.licenceExpirationDateString = licenceExpirationDateString;
	}

	public String getDobString() {
		return dobString;
	}

	public void setDobString(String dobString) {
		this.dobString = dobString;
	}

	public String getInsuranceEffectiveDateString() {
		return insuranceEffectiveDateString;
	}

	public void setInsuranceEffectiveDateString(String insuranceEffectiveDateString) {
		this.insuranceEffectiveDateString = insuranceEffectiveDateString;
	}

	public String getInsuranceExpirationDateString() {
		return insuranceExpirationDateString;
	}

	public void setInsuranceExpirationDateString(String insuranceExpirationDateString) {
		this.insuranceExpirationDateString = insuranceExpirationDateString;
	}

	public int insertDriverLicenseDetails(String loggedOnUserId) {

		int status = -1;

		this.setDrivingLicenseInfoId(UUIDGenerator.generateUUID());
		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedOnUserId);
		this.setCreatedBy(loggedOnUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);

		try {

			status = drivingLicenseInfoDao.insertDriverLicenseDetails(this);
			session.commit();
		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insertDriverLicenseDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static DrivingLicenseInfoModel getDriverLicenseDetails(String userId) {

		DrivingLicenseInfoModel drivingLicenseInfoModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DrivingLicenseInfoDao drivingLicenseInfoDao = session.getMapper(DrivingLicenseInfoDao.class);

		try {
			drivingLicenseInfoModel = drivingLicenseInfoDao.getDriverLicenseDetails(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverLicenseDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return drivingLicenseInfoModel;
	}
}
