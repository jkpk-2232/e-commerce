package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.UserAccountLogDetailsDao;

public class UserAccountLogDetailsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserAccountLogDetailsModel.class);

	private String userAccountLogDetailsId;

	private String userAccountLogId;

	private String userAccountId;

	private String userId;

	private String description;

	private String remark;

	private double creditedAmount;

	private double debitedAmount;

	private String transactionType;

	private String transactionStatus;

	private double currentBalance;

	private double holdBalance;

	private double approvedBalance;

	private double totalBalance;

	private String transactionBy;

	private boolean isAccountRecharge;

	public String getUserAccountLogDetailsId() {
		return userAccountLogDetailsId;
	}

	public void setUserAccountLogDetailsId(String userAccountLogDetailsId) {
		this.userAccountLogDetailsId = userAccountLogDetailsId;
	}

	public String getUserAccountLogId() {
		return userAccountLogId;
	}

	public void setUserAccountLogId(String userAccountLogId) {
		this.userAccountLogId = userAccountLogId;
	}

	public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getCreditedAmount() {
		return creditedAmount;
	}

	public void setCreditedAmount(double creditedAmount) {
		this.creditedAmount = creditedAmount;
	}

	public double getDebitedAmount() {
		return debitedAmount;
	}

	public void setDebitedAmount(double debitedAmount) {
		this.debitedAmount = debitedAmount;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getHoldBalance() {
		return holdBalance;
	}

	public void setHoldBalance(double holdBalance) {
		this.holdBalance = holdBalance;
	}

	public double getApprovedBalance() {
		return approvedBalance;
	}

	public void setApprovedBalance(double approvedBalance) {
		this.approvedBalance = approvedBalance;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getTransactionBy() {
		return transactionBy;
	}

	public void setTransactionBy(String transactionBy) {
		this.transactionBy = transactionBy;
	}

	public boolean isAccountRecharge() {
		return isAccountRecharge;
	}

	public void setAccountRecharge(boolean isAccountRecharge) {
		this.isAccountRecharge = isAccountRecharge;
	}

	public int insertUserAccountLogDetails(String loggedInUserId) {

		int addStatus = 0;

		this.userAccountLogDetailsId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = loggedInUserId;
		this.updatedAt = this.createdAt;
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserAccountLogDetailsDao userAccountLogDetailsDao = session.getMapper(UserAccountLogDetailsDao.class);

		try {
			addStatus = userAccountLogDetailsDao.insertUserAccountLogDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertUserAccountLogDetails: ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return addStatus;
	}
}