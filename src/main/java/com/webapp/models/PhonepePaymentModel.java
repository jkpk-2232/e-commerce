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
import com.webapp.daos.PhonepePaymentDao;

public class PhonepePaymentModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(PhonepePaymentModel.class);

	private String phonepePaymentId;
	private String vendorId;
	private String merchantTransactionId;
	private String transactionId;
	private String userId;
	private String code;
	private String message;
	private double amount;
	private String state;
	private String responseCode;
	private String paymentInstrumentType;
	private String utr;
	private String cardType;
	private String pgTransactionId;
	private String pgServiceTransactionId;
	private String bankTransactionId;
	private String pgAuthorizationCode;
	private String arn;
	private String bankId;
	private String brn;
	private String responseCodeDescription;
	private Integer paymentOrderId;
	
	private String paymentRequestType;
	private String fullName;
	private String paymentStatus;
	

	public String getPhonepePaymentId() {
		return phonepePaymentId;
	}

	public void setPhonepePaymentId(String phonepePaymentId) {
		this.phonepePaymentId = phonepePaymentId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getPaymentInstrumentType() {
		return paymentInstrumentType;
	}

	public void setPaymentInstrumentType(String paymentInstrumentType) {
		this.paymentInstrumentType = paymentInstrumentType;
	}

	public String getUtr() {
		return utr;
	}

	public void setUtr(String utr) {
		this.utr = utr;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getPgTransactionId() {
		return pgTransactionId;
	}

	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}

	public String getPgServiceTransactionId() {
		return pgServiceTransactionId;
	}

	public void setPgServiceTransactionId(String pgServiceTransactionId) {
		this.pgServiceTransactionId = pgServiceTransactionId;
	}

	public String getBankTransactionId() {
		return bankTransactionId;
	}

	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}

	public String getPgAuthorizationCode() {
		return pgAuthorizationCode;
	}

	public void setPgAuthorizationCode(String pgAuthorizationCode) {
		this.pgAuthorizationCode = pgAuthorizationCode;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PhonepePaymentModel insertPhonepePayment(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		PhonepePaymentModel phonepePaymentModel = new PhonepePaymentModel();

		try {
			this.phonepePaymentId = UUIDGenerator.generateUUID();
			this.transactionId = UUIDGenerator.generateUUID();
			this.createdBy = userId;
			this.updatedBy = userId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.state = ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_PENDING;

			phonepePaymentDao.insertPhonepePayment(this);
			session.commit();
			
			phonepePaymentModel = phonepePaymentModel.getPhonepePaymentDetailsByPhonepePaymentId(this.phonepePaymentId);
			

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertPhonepePayment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return phonepePaymentModel;
	}

	public PhonepePaymentModel getPhonepePaymentDetailsByPhonepePaymentId(String phonepePaymentId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		PhonepePaymentModel phonepePaymentModel = null;
		
		try {
			phonepePaymentModel = phonepePaymentDao.getPhonepePaymentDetailsByPhonepePaymentId(phonepePaymentId);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPhonepePaymentDetailsByPhonepePaymentId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return phonepePaymentModel;
	}

	public static PhonepePaymentModel getPhonepePaymentDetailsByMerchantTransactionId(String merchantTransactionId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		PhonepePaymentModel phonepePaymentModel = null;
		
		try {
			phonepePaymentModel = phonepePaymentDao.getPhonepePaymentDetailsByMerchantTransactionId(merchantTransactionId);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPhonepePaymentDetailsByMerchantTransactionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return phonepePaymentModel;
	}

	public void updatePhonepePaymentDetails() {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		try {
			
			this.updatedAt = DateUtils.nowAsGmtMillisec();

			phonepePaymentDao.updatePhonepePaymentDetails(this);
			session.commit();
			

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePhonepePaymentDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getResponseCodeDescription() {
		return responseCodeDescription;
	}

	public void setResponseCodeDescription(String responseCodeDescription) {
		this.responseCodeDescription = responseCodeDescription;
	}

	public void insertPayment() {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		try {
			this.phonepePaymentId = UUIDGenerator.generateUUID();
			//this.transactionId = UUIDGenerator.generateUUID();
			this.createdBy = userId;
			this.updatedBy = userId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;

			phonepePaymentDao.insertPhonepePayment(this);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertPayment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public Integer getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(Integer paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}

	public static int getPhonepePaymentListCount(String[] ccavenueStatusArray, String globalSearchString, long startDate, long endDate) {
		
		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("ccavenueStatusArray", ccavenueStatusArray);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		try {

			count = phonepePaymentDao.getPhonepePaymentListCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getPhonepePaymentListCount :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<PhonepePaymentModel> getPhonepePaymentListBySearch(int start, int length, String order, String[] ccavenueStatusArray, String globalSearchString, long startDate, long endDate) {
		
		List<PhonepePaymentModel> phonepePaymentModels = new ArrayList<PhonepePaymentModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("ccavenueStatusArray", ccavenueStatusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		try {

			phonepePaymentModels = phonepePaymentDao.getPhonepePaymentListBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getPhonepePaymentListBySearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return phonepePaymentModels;
	}

	public String getPaymentRequestType() {
		return paymentRequestType;
	}

	public void setPaymentRequestType(String paymentRequestType) {
		this.paymentRequestType = paymentRequestType;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public static List<PhonepePaymentModel> getPhonepeLogsReport(String globalSearchString, long startDate, long endDate) {
		
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		List<PhonepePaymentModel> phonepeLogModelList = new ArrayList<PhonepePaymentModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PhonepePaymentDao phonepePaymentDao = session.getMapper(PhonepePaymentDao.class);

		try {

			phonepeLogModelList = phonepePaymentDao.getPhonepeLogsReport(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getPhonepeLogsReport :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return phonepeLogModelList;
	}

}
