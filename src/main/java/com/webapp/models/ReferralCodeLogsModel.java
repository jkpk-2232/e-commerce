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
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.daos.ReferralCodeLogsDao;

public class ReferralCodeLogsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ReferralCodeLogsModel.class);

	private String referralCodeLogsId;
	private String senderId;
	private String receiverId;
	private double senderBenefit;
	private double receiverBenefit;

	private String passengerName;
	private String passengerPhoneNumber;

	public String getReferralCodeLogsId() {
		return referralCodeLogsId;
	}

	public void setReferralCodeLogsId(String referralCodeLogsId) {
		this.referralCodeLogsId = referralCodeLogsId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public double getSenderBenefit() {
		return senderBenefit;
	}

	public void setSenderBenefit(double senderBenefit) {
		this.senderBenefit = senderBenefit;
	}

	public double getReceiverBenefit() {
		return receiverBenefit;
	}

	public void setReceiverBenefit(double receiverBenefit) {
		this.receiverBenefit = receiverBenefit;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerPhoneNumber() {
		return passengerPhoneNumber;
	}

	public void setPassengerPhoneNumber(String passengerPhoneNumber) {
		this.passengerPhoneNumber = passengerPhoneNumber;
	}

	public int addReferralCodeLogs(String userId, String roleId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ReferralCodeLogsDao referralCodeLogsDao = session.getMapper(ReferralCodeLogsDao.class);
		//		UserProfileDao userProfileDao = session.getMapper(UserProfileDao.class);

		try {

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			this.referralCodeLogsId = UUIDGenerator.generateUUID();

			if (UserRoles.DRIVER_ROLE_ID.equals(roleId)) {

				this.senderBenefit = adminSettingsModel.getDriverReferralBenefit();

			} else {

				this.senderBenefit = adminSettingsModel.getSenderBenefit();
			}

			this.receiverBenefit = adminSettingsModel.getReceiverBenefit();
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = referralCodeLogsDao.addReferralCodeLogs(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addReferralCodeLogs : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static ReferralCodeLogsModel getReferralCodeLogsByReceiverId(String userId) {

		ReferralCodeLogsModel referralCodeLogsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ReferralCodeLogsDao referralCodeLogsDao = session.getMapper(ReferralCodeLogsDao.class);

		try {
			referralCodeLogsModel = referralCodeLogsDao.getReferralCodeLogsByReceiverId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getReferralCodeLogsByReceiverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return referralCodeLogsModel;
	}

	public static int getTotalReferralLogsCount(String senderId, long startDate, long endDate) {

		int referralLogsCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("senderId", senderId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ReferralCodeLogsDao referralCodeLogsDao = session.getMapper(ReferralCodeLogsDao.class);

		try {

			referralLogsCount = referralCodeLogsDao.getTotalDriverReferralLogsCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDriverReferralLogsCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return referralLogsCount;
	}

	public static List<ReferralCodeLogsModel> getReferralLogsListForSearch(String senderId, int start, int length, String order, String globalSearchString, long startDate, long endDate) {

		List<ReferralCodeLogsModel> referralCodeLogsModelList = new ArrayList<ReferralCodeLogsModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("senderId", senderId);
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ReferralCodeLogsDao referralCodeLogsDao = session.getMapper(ReferralCodeLogsDao.class);

		try {

			referralCodeLogsModelList = referralCodeLogsDao.getReferralLogsListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getReferralLogsListForSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return referralCodeLogsModelList;
	}
}