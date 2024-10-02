package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.UserPaymentProfileDao;

public class UserPaymentProfileModel extends AbstractModel {

	private String userPaymentProfileId;
	private String userId;
	private long userAuthorizeProfileId;
	private long userAuthorizePaymentProfileId;

	private static Logger logger = Logger.getLogger(UserPaymentProfileModel.class);

	public String getUserPaymentProfileId() {
		return userPaymentProfileId;
	}

	public void setUserPaymentProfileId(String userPaymentProfileId) {
		this.userPaymentProfileId = userPaymentProfileId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getUserAuthorizeProfileId() {
		return userAuthorizeProfileId;
	}

	public void setUserAuthorizeProfileId(long userAuthorizeProfileId) {
		this.userAuthorizeProfileId = userAuthorizeProfileId;
	}

	public long getUserAuthorizePaymentProfileId() {
		return userAuthorizePaymentProfileId;
	}

	public void setUserAuthorizePaymentProfileId(long userAuthorizePaymentProfileId) {
		this.userAuthorizePaymentProfileId = userAuthorizePaymentProfileId;
	}

	public int insertUserPaymentProfile() {
		int insertedId = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserPaymentProfileDao userPaymentProfileDao = session.getMapper(UserPaymentProfileDao.class);
		try {
			insertedId = userPaymentProfileDao.insertUserPaymentProfile(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertUserPaymentProfile :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return insertedId;
	}

	public static UserPaymentProfileModel getLatesetUserPaymentProfileModel(String userId) {

		UserPaymentProfileModel model = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserPaymentProfileDao userPaymentProfileDao = session.getMapper(UserPaymentProfileDao.class);
		try {
			model = userPaymentProfileDao.getLatesetUserPaymentProfileModel(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getLatesetUserPaymentProfileModel :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return model;
	}

}
