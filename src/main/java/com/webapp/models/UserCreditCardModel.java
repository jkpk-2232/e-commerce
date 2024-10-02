package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.UserCreditCardDao;

public class UserCreditCardModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserCreditCardModel.class);

	private String userCreditCardDetailsId;
	private String userId;
	private String cardNumber;
	private String token;
	private String authCode;
	private String cardType;

	private double amount;

	private String braintreeProfileId;
	private String nonceToken;

	public String getNonceToken() {
		return nonceToken;
	}

	public void setNonceToken(String nonceToken) {
		this.nonceToken = nonceToken;
	}

	public String getBraintreeProfileId() {
		return braintreeProfileId;
	}

	public void setBraintreeProfileId(String braintreeProfileId) {
		this.braintreeProfileId = braintreeProfileId;
	}

	public String getUserCreditCardDetailsId() {
		return userCreditCardDetailsId;
	}

	public void setUserCreditCardDetailsId(String userCreditCardDetailsId) {
		this.userCreditCardDetailsId = userCreditCardDetailsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int addUserCreditCardDetails(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		this.userCreditCardDetailsId = UUIDGenerator.generateUUID();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.setCreatedBy(userId);
		this.setCreatedAt(this.updatedAt);
		this.updatedBy = userId;
		this.authCode = null;

		try {
			count = userCreditCardDao.addUserCreditCardDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addUserCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static UserCreditCardModel getCreditCardDetails(String userId) {

		UserCreditCardModel userCreditCardModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		try {
			userCreditCardModel = userCreditCardDao.getCreditCardDetails(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userCreditCardModel;
	}

	public int updateUserCreditCardDetails(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		this.authCode = null;

		try {
			count = userCreditCardDao.updateUserCreditCardDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUserCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public int updateUserCreditCardDetails1(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		this.authCode = null;

		try {
			count = userCreditCardDao.updateUserCreditCardDetails1(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUserCreditCardDetails1 : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public int updateAuthorizationCode(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			count = userCreditCardDao.updateUserCreditCardDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUserCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public int editUserCreditCardDetails(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserCreditCardDao userCreditCardDao = session.getMapper(UserCreditCardDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		this.authCode = null;

		try {
			count = userCreditCardDao.editUserCreditCardDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUserCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}
}