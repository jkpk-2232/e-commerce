package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AuthorizeCreditCardDao;

public class AuthorizeCreditCardModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AuthorizeCreditCardModel.class);

	private String authorizeCreditCardDetailsId;
	private String cvv;
	private String expiryMonth;
	private String expiryYear;
	private String cardNumber;
	private String authorizeCustomerProfileId;
	private String authorizeCustomerPaymentProfileId;
	private String cardType;
	private String userId;
	private String gateWayType;

	private String firstName;
	private String lastName;
	private String phoneNo;
	private String phoneNoCode;
	private String email;

	private String address;
	private String city;
	private String state;
	private String country;
	private String zip;

	private String firstTransactionId;

	public static boolean isAuthorizeCreditCardDetailsPresent(String userId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		try {
			status = authorizeCreditCardDao.isAuthorizeCreditCardDetailsPresent(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isAuthorizeCreditCardDetailsPresent : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static boolean isAuthorizeCreditCardDetailsPresentByEmail(String email) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		try {
			status = authorizeCreditCardDao.isAuthorizeCreditCardDetailsPresentByEmail(email);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isAuthorizeCreditCardDetailsPresentByEmail : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int updateAuthorizeCreditCardDetails(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		int updateStatus = 0;

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);
			this.setUserId(userId);

			updateStatus = authorizeCreditCardDao.updateAuthorizeCreditCardDetails(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAuthorizeCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}

	public int updateAuthorizeCreditCardDetailsByEmail(String email) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		int updateStatus = 0;

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);
			this.setUserId(userId);
			this.setEmail(email);

			updateStatus = authorizeCreditCardDao.updateAuthorizeCreditCardDetailsByEmail(this);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAuthorizeCreditCardDetailsByEmail : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}

	public int addAuthorizeCreditCardDetails(String loggedInuserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		int status;

		try {

			this.setAuthorizeCreditCardDetailsId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setCreatedBy(loggedInuserId);
			this.setUpdatedBy(loggedInuserId);

			status = authorizeCreditCardDao.addAuthorizeCreditCardDetails(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAuthorizeCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static AuthorizeCreditCardModel getAuthorizeCreditCardDetails(String userId) {

		AuthorizeCreditCardModel authorizeCreditCardModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		try {
			authorizeCreditCardModel = authorizeCreditCardDao.getAuthorizeCreditCardDetails(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAuthorizeCreditCardDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return authorizeCreditCardModel;
	}

	public static AuthorizeCreditCardModel getAuthorizeCreditCardDetailsByEmail(String email) {

		AuthorizeCreditCardModel authorizeCreditCardModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AuthorizeCreditCardDao authorizeCreditCardDao = session.getMapper(AuthorizeCreditCardDao.class);

		try {
			authorizeCreditCardModel = authorizeCreditCardDao.getAuthorizeCreditCardDetailsByEmail(email);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAuthorizeCreditCardDetailsByEmail : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return authorizeCreditCardModel;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getAuthorizeCreditCardDetailsId() {
		return authorizeCreditCardDetailsId;
	}

	public void setAuthorizeCreditCardDetailsId(String authorizeCreditCardDetailsId) {
		this.authorizeCreditCardDetailsId = authorizeCreditCardDetailsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGateWayType() {
		return gateWayType;
	}

	public void setGateWayType(String gateWayType) {
		this.gateWayType = gateWayType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getAuthorizeCustomerProfileId() {
		return authorizeCustomerProfileId;
	}

	public void setAuthorizeCustomerProfileId(String authorizeCustomerProfileId) {
		this.authorizeCustomerProfileId = authorizeCustomerProfileId;
	}

	public String getAuthorizeCustomerPaymentProfileId() {
		return authorizeCustomerPaymentProfileId;
	}

	public void setAuthorizeCustomerPaymentProfileId(String authorizeCustomerPaymentProfileId) {
		this.authorizeCustomerPaymentProfileId = authorizeCustomerPaymentProfileId;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getFirstTransactionId() {
		return firstTransactionId;
	}

	public void setFirstTransactionId(String firstTransactionId) {
		this.firstTransactionId = firstTransactionId;
	}

}
