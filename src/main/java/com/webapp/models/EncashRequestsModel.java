package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.EncashRequestsDao;

public class EncashRequestsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(EncashRequestsModel.class);

	private String encashRequestId;

	private String userId;

	private double requestedAmount;

	private long requestedDate;

	private String status;

	private long holdDate;

	private long approvedDate;

	private long rejectedDate;

	private long transferDate;

	private String firstName;

	private String lastName;

	private String phoneNoCode;

	private String phoneNo;

	private String email;

	private double currentBalance;

	private double holdBalance;

	private double approvedBalance;

	private double totalBalance;

	private String remark;

	public String getEncashRequestId() {
		return encashRequestId;
	}

	public void setEncashRequestId(String encashRequestId) {
		this.encashRequestId = encashRequestId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(double requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public long getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(long requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getHoldDate() {
		return holdDate;
	}

	public void setHoldDate(long holdDate) {
		this.holdDate = holdDate;
	}

	public long getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(long approvedDate) {
		this.approvedDate = approvedDate;
	}

	public long getRejectedDate() {
		return rejectedDate;
	}

	public void setRejectedDate(long rejectedDate) {
		this.rejectedDate = rejectedDate;
	}

	public long getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(long transferDate) {
		this.transferDate = transferDate;
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

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String addEncashRequest(String loggedInUserId) {

		int addStatus = 0;

		this.encashRequestId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = loggedInUserId;
		this.updatedAt = this.createdAt;
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			addStatus = encashRequestsDao.addEncashRequest(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addEncashRequest: ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (addStatus > 0) {
			return this.encashRequestId;
		} else {
			return null;
		}

	}

	public int updateEncashRequestStatus(String loggedInUserId) {

		int updateStatus = 0;

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			updateStatus = encashRequestsDao.updateEncashRequestStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateEncashRequestStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}

	public static EncashRequestsModel getEncashRequestDetailsById(String encashRequestId) {

		EncashRequestsModel encashRequestsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			encashRequestsModel = encashRequestsDao.getEncashRequestDetailsById(encashRequestId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getEncashRequestDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return encashRequestsModel;
	}

	public static List<EncashRequestsModel> getEncashRequestForSearchByStatus(int start, int length, String order, String globalSearchString, String status, long startDateLong, long endDateLong) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("status", status);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);

		List<EncashRequestsModel> encashRequestsModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			encashRequestsModelList = encashRequestsDao.getEncashRequestForSearchByStatus(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getEncashRequestForSearchByStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return encashRequestsModelList;
	}

	public static int getTotalCountOfEncashRequestForSearchByStatus(String status, long startDateLong, long endDateLong) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("status", status);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			count = encashRequestsDao.getTotalCountOfEncashRequestForSearchByStatus(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalCountOfEncashRequestForSearchByStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int getFilteredCountOfEncashRequestForSearchByStatus(String status, String globalSearchString, long startDateLong, long endDateLong) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("status", status);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			count = encashRequestsDao.getFilteredCountOfEncashRequestForSearchByStatus(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFilteredCountOfEncashRequestForSearchByStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<EncashRequestsModel> getEncashRequestsListForExport(String status, String searchString, long startDateLong, long endDateLong, List<String> assignedRegionList) {

		List<EncashRequestsModel> encashRequestsModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("status", status);
		inputMap.put("searchString", searchString);
		inputMap.put("startDateLong", startDateLong);
		inputMap.put("endDateLong", endDateLong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EncashRequestsDao encashRequestsDao = session.getMapper(EncashRequestsDao.class);

		try {
			encashRequestsModelList = encashRequestsDao.getEncashRequestsListForExport(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getEncashRequestsListForExport :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return encashRequestsModelList;
	}

}