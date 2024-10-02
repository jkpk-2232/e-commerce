package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.PaymentTypeDao;

public class PaymentTypeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(PaymentTypeModel.class);

	private String paymentTypeId;
	private String paymentType;

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public static PaymentTypeModel getPaymentTypeIdBy(String paymentType) {

		PaymentTypeModel paymentTypeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PaymentTypeDao paymentTypeDao = session.getMapper(PaymentTypeDao.class);

		try {
			paymentTypeModel = paymentTypeDao.getPaymentTypeIdBy(paymentType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPaymentTypeIdBy : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return paymentTypeModel;
	}
}