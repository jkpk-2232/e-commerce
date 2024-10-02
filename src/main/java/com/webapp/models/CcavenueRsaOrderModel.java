package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.CcavenueRsaOrderDao;

public class CcavenueRsaOrderModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CcavenueRsaRequestModel.class);

	private String ccavenueRsaOrderId;
	private String orderId;
	private String userId;
	private String tourId;
	private String subscriptionId;
	private String deliveryOrderId;
	private String paymentRequestType;

	public String getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(String deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getPaymentRequestType() {
		return paymentRequestType;
	}

	public void setPaymentRequestType(String paymentRequestType) {
		this.paymentRequestType = paymentRequestType;
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

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String insertCcavenueRsaOrderDetails(String loggedInUserId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueRsaOrderDao ccavenueRsaOrderDao = session.getMapper(CcavenueRsaOrderDao.class);

		try {

			this.ccavenueRsaOrderId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = loggedInUserId;
			this.updatedBy = loggedInUserId;

			status = ccavenueRsaOrderDao.insertCcavenueRsaOrderDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insert Ccavenue Rsa Order Details : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {
			return this.ccavenueRsaOrderId;
		} else {
			return "-1";
		}
	}

	public static boolean isOrderIdExists(String orderId) {

		boolean status = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueRsaOrderDao ccavenueRsaOrderDao = session.getMapper(CcavenueRsaOrderDao.class);

		try {
			status = ccavenueRsaOrderDao.isOrderIdExists(orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isOrderIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}
}