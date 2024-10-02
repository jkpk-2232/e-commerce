package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.PaymentOrderDao;

public class PaymentOrdersModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(PaymentOrdersModel.class);

	private Integer paymentOrderId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String orderType;
	private String paymentStatus;
	private String paymentTypeId;
	private double amount;
	private String orderId;
	private String userId;
	private String orderRefId;

	public Integer getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(Integer paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderRefId() {
		return orderRefId;
	}

	public void setOrderRefId(String orderRefId) {
		this.orderRefId = orderRefId;
	}

	public  PaymentOrdersModel insertPaymentOrder(String loggedInuserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PaymentOrderDao paymentOrderDao = session.getMapper(PaymentOrderDao.class);

		PaymentOrdersModel paymentOrderModel = new PaymentOrdersModel();

		try {
			this.createdBy = userId;
			this.updatedBy = userId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;

			paymentOrderDao.insertPaymentOrder(this);
			session.commit();
			
			paymentOrderModel = PaymentOrdersModel.getPaymentDetailsByOrderId(this.orderId);
			

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertPaymentOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return paymentOrderModel;
	}

	public static PaymentOrdersModel getPaymentDetailsByOrderId(String orderId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PaymentOrderDao paymentOrderDao = session.getMapper(PaymentOrderDao.class);

		PaymentOrdersModel paymentOrderModel = null;
		
		try {
			paymentOrderModel = paymentOrderDao.getPaymentDetailsByOrderId(orderId);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPaymentDetailsByOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return paymentOrderModel;
	}

	public void updatePaymentStatus() {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PaymentOrderDao paymentOrderDao = session.getMapper(PaymentOrderDao.class);
		
		try {
			paymentOrderDao.updatePaymentStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePaymentStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public boolean isOrderIdExists(String orderId) {
		
		boolean status = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PaymentOrderDao paymentOrderDao = session.getMapper(PaymentOrderDao.class);

		try {
			status = paymentOrderDao.isOrderIdExists(orderId);
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
