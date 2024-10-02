package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.CcavenueRsaRequestDao;

public class CcavenueRsaRequestModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CcavenueRsaRequestModel.class);

	private String ccavenueRsaRequestId;
	private String userId;
	private String ccavenueRsaOrderId;
	private String rsaKey;
	private String paymentRequestType;

	private String orderId;
	private String tourId;
	private String subscriptionId;
	private String deliveryOrderId;

	private String accessCode;
	private String environment;

	public String getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(String deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getCcavenueRsaRequestId() {
		return ccavenueRsaRequestId;
	}

	public void setCcavenueRsaRequestId(String ccavenueRsaRequestId) {
		this.ccavenueRsaRequestId = ccavenueRsaRequestId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCcavenueRsaOrderId() {
		return ccavenueRsaOrderId;
	}

	public void setCcavenueRsaOrderId(String ccavenueRsaOrderId) {
		this.ccavenueRsaOrderId = ccavenueRsaOrderId;
	}

	public String getRsaKey() {
		return rsaKey;
	}

	public void setRsaKey(String rsaKey) {
		this.rsaKey = rsaKey;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getPaymentRequestType() {
		return paymentRequestType;
	}

	public void setPaymentRequestType(String paymentRequestType) {
		this.paymentRequestType = paymentRequestType;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public int insertCcavenueRsaRequestDetails(String loggedInUserId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueRsaRequestDao ccavenueRsaRequestDao = session.getMapper(CcavenueRsaRequestDao.class);

		try {

			this.ccavenueRsaRequestId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = loggedInUserId;
			this.updatedBy = loggedInUserId;

			status = ccavenueRsaRequestDao.insertCcavenueRsaRequestDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insert Ccavenue Rsa Request Details : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static CcavenueRsaRequestModel getCcavenueRsaRequestByOrderId(String orderId) {

		CcavenueRsaRequestModel ccavenueRsaRequestModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueRsaRequestDao ccavenueRsaRequestDao = session.getMapper(CcavenueRsaRequestDao.class);

		try {
			ccavenueRsaRequestModel = ccavenueRsaRequestDao.getCcavenueRsaRequestByOrderId(orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCcavenueRsaRequestByOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return ccavenueRsaRequestModel;
	}

	@Override
	public String toString() {
		return "CcavenueRsaRequestModel [ccavenueRsaRequestId=" + ccavenueRsaRequestId + ", userId=" + userId + ", ccavenueRsaOrderId=" + ccavenueRsaOrderId + ", rsaKey=" + rsaKey + ", paymentRequestType=" + paymentRequestType + ", orderId=" + orderId + ", tourId=" + tourId
					+ ", subscriptionId=" + subscriptionId + ", accessCode=" + accessCode + ", environment=" + environment + "]";
	}
}