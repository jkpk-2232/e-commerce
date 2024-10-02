package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.CcavenueResponseLogDao;

public class CcavenueResponseLogModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CcavenueRsaRequestModel.class);

	private String ccavenueResponseLogId;
	private String tourId;
	private String trackingId;
	private double amount;
	private String orderStatus; // (Success/Failure/Aborted/Invalid)
	private String failureMessage;
	private String paymentMode;
	private String bankRefNo;
	private String retriedPayment;
	private String bankResponseCode;
	private String cardName;
	private String currency;
	private String ccavenueRsaRequestId;
	private String billingCountry;
	private String billingTel;
	private String billingEmail;
	private String subscriptionId;
	private String userId;

	private String orderId;
	private String userTourId;
	private String shortSubscriptionId;
	private String paymentRequestType;
	private String userFullName;

	private String deliveryOrderId;
	private String orderShortId;
	private String orderUserId;

	public String getOrderUserId() {
		return orderUserId;
	}

	public void setOrderUserId(String orderUserId) {
		this.orderUserId = orderUserId;
	}

	public String getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(String deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getOrderShortId() {
		return orderShortId;
	}

	public void setOrderShortId(String orderShortId) {
		this.orderShortId = orderShortId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
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

	public String getShortSubscriptionId() {
		return shortSubscriptionId;
	}

	public void setShortSubscriptionId(String shortSubscriptionId) {
		this.shortSubscriptionId = shortSubscriptionId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getCcavenueResponseLogId() {
		return ccavenueResponseLogId;
	}

	public void setCcavenueResponseLogId(String ccavenueResponseLogId) {
		this.ccavenueResponseLogId = ccavenueResponseLogId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getBankRefNo() {
		return bankRefNo;
	}

	public void setBankRefNo(String bankRefNo) {
		this.bankRefNo = bankRefNo;
	}

	public String getRetriedPayment() {
		return retriedPayment;
	}

	public void setRetriedPayment(String retriedPayment) {
		this.retriedPayment = retriedPayment;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getBankResponseCode() {
		return bankResponseCode;
	}

	public void setBankResponseCode(String bankResponseCode) {
		this.bankResponseCode = bankResponseCode;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCcavenueRsaRequestId() {
		return ccavenueRsaRequestId;
	}

	public void setCcavenueRsaRequestId(String ccavenueRsaRequestId) {
		this.ccavenueRsaRequestId = ccavenueRsaRequestId;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getBillingTel() {
		return billingTel;
	}

	public void setBillingTel(String billingTel) {
		this.billingTel = billingTel;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	public String getUserTourId() {
		return userTourId;
	}

	public void setUserTourId(String userTourId) {
		this.userTourId = userTourId;
	}

	public int insertCcavenueResponseLog(String userId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {

			this.ccavenueResponseLogId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = ccavenueResponseLogDao.insertCcavenueResponseLog(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insert Ccavenue Response Log : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static CcavenueResponseLogModel getCcavenueResponseLogDetailsByTripId(String tripId) {

		CcavenueResponseLogModel ccavenueResponseLogModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {
			ccavenueResponseLogModel = ccavenueResponseLogDao.getCcavenueResponseLogDetailsByTripId(tripId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCcavenueResponseLogDetailsByTripId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return ccavenueResponseLogModel;
	}

	public static CcavenueResponseLogModel getCcavenueResponseLogDetailsBySubscriptionId(String subscriptionOrderId, String userId) {

		CcavenueResponseLogModel ccavenueResponseLogModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {
			ccavenueResponseLogModel = ccavenueResponseLogDao.getCcavenueResponseLogDetailsBySubscriptionId(subscriptionOrderId, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCcavenueResponseLogDetailsBySubscriptionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return ccavenueResponseLogModel;
	}

	public static CcavenueResponseLogModel getCcavenueResponseLogDetailsByDeliveryOrderId(String deliveryOrderId, String userId) {

		CcavenueResponseLogModel ccavenueResponseLogModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {
			ccavenueResponseLogModel = ccavenueResponseLogDao.getCcavenueResponseLogDetailsByDeliveryOrderId(deliveryOrderId, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCcavenueResponseLogDetailsByDeliveryOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return ccavenueResponseLogModel;
	}

	public static int getCcavenueResponseLogListCount(String[] ccavenueStatusArray, String globalSearchString, long startDate, long endDate, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("ccavenueStatusArray", ccavenueStatusArray);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {

			count = ccavenueResponseLogDao.getCcavenueResponseLogListCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCcavenueResponseLogListCount :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<CcavenueResponseLogModel> getCcavenueResponseLogListBySearch(int start, int length, String order, String[] ccavenueStatusArray, String globalSearchString, long startDate, long endDate, List<String> assignedRegionList) {

		List<CcavenueResponseLogModel> ccavenueResponseLogModelList = new ArrayList<CcavenueResponseLogModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("ccavenueStatusArray", ccavenueStatusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {

			ccavenueResponseLogModelList = ccavenueResponseLogDao.getCcavenueResponseLogListBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCcavenueResponseLogListBySearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return ccavenueResponseLogModelList;
	}

	public static List<CcavenueResponseLogModel> getCcavenueLogsReport(String globalSearchString, long startDate, long endDate, List<String> assignedRegionList) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		List<CcavenueResponseLogModel> ccavenueResponseLogModelList = new ArrayList<CcavenueResponseLogModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CcavenueResponseLogDao ccavenueResponseLogDao = session.getMapper(CcavenueResponseLogDao.class);

		try {

			ccavenueResponseLogModelList = ccavenueResponseLogDao.getCcavenueLogsReport(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCcavenueLogsReport :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return ccavenueResponseLogModelList;
	}

	@Override
	public String toString() {
		return "CcavenueResponseLogModel [ccavenueResponseLogId=" + ccavenueResponseLogId + ", tourId=" + tourId + ", trackingId=" + trackingId + ", amount=" + amount + ", orderStatus=" + orderStatus + ", failureMessage=" + failureMessage + ", paymentMode=" + paymentMode
					+ ", bankRefNo=" + bankRefNo + ", retriedPayment=" + retriedPayment + ", bankResponseCode=" + bankResponseCode + ", cardName=" + cardName + ", currency=" + currency + ", ccavenueRsaRequestId=" + ccavenueRsaRequestId + ", billingCountry="
					+ billingCountry + ", billingTel=" + billingTel + ", billingEmail=" + billingEmail + ", subscriptionId=" + subscriptionId + ", userId=" + userId + ", orderId=" + orderId + ", userTourId=" + userTourId + ", shortSubscriptionId="
					+ shortSubscriptionId + ", paymentRequestType=" + paymentRequestType + ", userFullName=" + userFullName + "]";
	}
}