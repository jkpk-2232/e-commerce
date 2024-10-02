package com.webapp.models;

import java.io.IOException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.UserLoginOtpDao;

public class UserLoginOtpModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserModel.class);

	private String userLoginOtpId;
	private String verificationCode;
	private boolean isVerified;
	private String roleId;
	private String userId;
	private String phoneNo;
	private String phoneNoCode;

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getUserLoginOtpId() {
		return userLoginOtpId;
	}

	public void setUserLoginOtpId(String userLoginOtpId) {
		this.userLoginOtpId = userLoginOtpId;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public static UserLoginOtpModel getUserDetailsByOtp(String verficationCode, String userId) {

		UserLoginOtpModel verifyOtpLogsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserLoginOtpDao userLoginOtpDao = session.getMapper(UserLoginOtpDao.class);

		try {
			verifyOtpLogsModel = userLoginOtpDao.getUserDetailsByOtp(verficationCode, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByOtp : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return verifyOtpLogsModel;
	}

	public String addVerificationCode(String userId) throws IOException {

		String userLoginOtpId = UUIDGenerator.generateUUID();

		int status = -1;

		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserLoginOtpDao userLoginOtpDao = session.getMapper(UserLoginOtpDao.class);

		try {
			this.userLoginOtpId = userLoginOtpId;
			this.createdBy = userId;
			this.updatedBy = userId;
			status = userLoginOtpDao.addVerificationCode(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add verification code : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {
			return userLoginOtpId;
		} else {
			return null;
		}
	}

	public static int deleteVerificationCode(String userId) throws IOException {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserLoginOtpDao userLoginOtpDao = session.getMapper(UserLoginOtpDao.class);

		try {
			status = userLoginOtpDao.deleteVerificationCode(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add verification code : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		if (status > 0) {
			return status;
		}

		return status;
	}

	public int getVerificationCodeCount(String userId) throws IOException {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserLoginOtpDao userLoginOtpDao = session.getMapper(UserLoginOtpDao.class);

		try {
			this.userId = userId;
			status = userLoginOtpDao.getVerificationCodeCount(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add verification code count : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static String getVerificationCodeOfUser(String userId) {

		String verifyCode = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserLoginOtpDao userLoginOtpDao = session.getMapper(UserLoginOtpDao.class);

		try {

			verifyCode = userLoginOtpDao.getVerificationCodeOfUser(userId);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVerificationCodeOfUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return verifyCode;
	}

	public static UserLoginOtpModel getUserDetails(String userLoginOtpId) {

		UserLoginOtpModel verifyOtpLogsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserLoginOtpDao userLoginOtpDao = session.getMapper(UserLoginOtpDao.class);

		try {
			verifyOtpLogsModel = userLoginOtpDao.getUserDetails(userLoginOtpId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByOtp : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return verifyOtpLogsModel;
	}

}
