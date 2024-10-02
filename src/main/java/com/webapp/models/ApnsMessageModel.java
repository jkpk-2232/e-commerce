package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ApnsMessageDao;

public class ApnsMessageModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ApnsMessageModel.class);

	private String apnsMessageId;
	private String fromUserId;
	private String toUserId;
	private String message;
	private String messageType;
	private int dataLimit;
	private int offset;
	private long afterTime;

	private String extraInfoType;
	private String extraInfoId;

	private UserModel fromUserProfileDeatils;

	public String getExtraInfoType() {
		return extraInfoType;
	}

	public void setExtraInfoType(String extraInfoType) {
		this.extraInfoType = extraInfoType;
	}

	public String getExtraInfoId() {
		return extraInfoId;
	}

	public void setExtraInfoId(String extraInfoId) {
		this.extraInfoId = extraInfoId;
	}

	public long getAfterTime() {
		return afterTime;
	}

	public void setAfterTime(long afterTime) {
		this.afterTime = afterTime;
	}

	public String getApnsMessageId() {
		return apnsMessageId;
	}

	public void setApnsMessageId(String apnsMessageId) {
		this.apnsMessageId = apnsMessageId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public int getDataLimit() {
		return dataLimit;
	}

	public void setDataLimit(int dataLimit) {
		this.dataLimit = dataLimit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public UserModel getFromUserProfileDeatils() {
		return fromUserProfileDeatils;
	}

	public void setFromUserProfileDeatils(UserModel fromUserProfileDeatils) {
		this.fromUserProfileDeatils = fromUserProfileDeatils;
	}

	public static void insertMultiplePushMessage(List<ApnsMessageModel> apnsMessageModel) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		try {
			apnsMessageDao.insertMultiplePushMessage(apnsMessageModel);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertPushMessage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String insertPushMessage() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		this.setApnsMessageId(UUIDGenerator.generateUUID());
		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {
			apnsMessageDao.insertPushMessage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertPushMessage : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.getApnsMessageId();
	}

	public List<ApnsMessageModel> getAllNotificationsByUserId() {

		List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();

		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		try {
			apnsMessageList = apnsMessageDao.getAllNotificationsByUserId(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertApnsDevice : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return apnsMessageList;
	}

	public static int getTotalNotificationCount(String userId, long afterTime) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		try {
			count = apnsMessageDao.getTotalNotificationCount(userId, afterTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalNotificationCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static ApnsMessageModel getById(String apnsMessageId) {

		ApnsMessageModel apnsMessageModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		try {
			apnsMessageModel = apnsMessageDao.getById(apnsMessageId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertApnsDevice : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return apnsMessageModel;
	}

	public int deleteNotificationById() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		try {
			status = apnsMessageDao.deleteNotificationById(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteNotificationById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public void deleteVendorFeedsByVendorFeedId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ApnsMessageDao apnsMessageDao = session.getMapper(ApnsMessageDao.class);

		try {
			apnsMessageDao.deleteVendorFeedsByVendorFeedId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteVendorFeedsByVendorFeedId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}
}