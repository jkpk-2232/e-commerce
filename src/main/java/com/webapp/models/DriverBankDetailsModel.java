package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DriverBankDetailsDao;

public class DriverBankDetailsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverBankDetailsModel.class);

	private String driverBankDetailsId;
	private String userId;
	private String bankName;
	private String accountNumber;
	private String accountName;
	private String routingNumber;
	private String type;

	public int addDriverBankDetails(String userId) {

		int status = -1;

		this.setDriverBankDetailsId(UUIDGenerator.generateUUID());
		this.setUserId(userId);
		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(userId);
		this.setCreatedBy(userId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);

		try {

			status = driverBankDetailsDao.addDriverBankDetails(this);
			session.commit();
		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addDriverBankDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return status;
	}

	public static DriverBankDetailsModel getDriverBankDetails(String userId) {

		DriverBankDetailsModel driverBankDetailsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverBankDetailsDao driverBankDetailsDao = session.getMapper(DriverBankDetailsDao.class);

		try {

			driverBankDetailsModel = driverBankDetailsDao.getDriverBankDetails(userId);
			session.commit();
		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverBankDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return driverBankDetailsModel;
	}

	public String getDriverBankDetailsId() {
		return driverBankDetailsId;
	}

	public void setDriverBankDetailsId(String driverBankDetailsId) {
		this.driverBankDetailsId = driverBankDetailsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
