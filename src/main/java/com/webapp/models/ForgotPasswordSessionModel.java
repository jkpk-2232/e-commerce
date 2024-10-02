package com.webapp.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ForgotPasswordSessionDao;

public class ForgotPasswordSessionModel {

	private static Logger logger = Logger.getLogger(ForgotPasswordSessionModel.class);

	private String forgotpasswordSessionId;
	private String userId;
	private long requestedAt;
	private long expiresAt;
	private long consumeAt;

	public String getForgotpasswordSessionId() {
		return forgotpasswordSessionId;
	}

	public void setForgotpasswordSessionId(String forgotpasswordSessionId) {
		this.forgotpasswordSessionId = forgotpasswordSessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(long requestedAt) {
		this.requestedAt = requestedAt;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public long getConsumeAt() {
		return consumeAt;
	}

	public void setConsumeAt(long consumeAt) {
		this.consumeAt = consumeAt;
	}

	public String createForgotPasswordSession() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ForgotPasswordSessionDao fpDao = session.getMapper(ForgotPasswordSessionDao.class);

		try {
			this.forgotpasswordSessionId = UUIDGenerator.generateUUID();
			this.forgotpasswordSessionId = fpDao.createForgotPasswordSession(this) > 0 ? this.forgotpasswordSessionId : null;
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during createForgotPasswordSession :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return forgotpasswordSessionId;
	}

	public static ForgotPasswordSessionModel getForgotPasswordSessionById(String forgotpasswordSessionId) {

		ForgotPasswordSessionModel forgotPasswordSessionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ForgotPasswordSessionDao fpDao = session.getMapper(ForgotPasswordSessionDao.class);

		try {
			forgotPasswordSessionModel = fpDao.getForgotPasswordSessionById(forgotpasswordSessionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getForgotPasswordSessionById :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return forgotPasswordSessionModel;
	}

	public static boolean isValidFPSession(String forgotpasswordSessionId) {

		boolean isValid = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ForgotPasswordSessionDao fpDao = session.getMapper(ForgotPasswordSessionDao.class);

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("forgotpasswordSessionId", forgotpasswordSessionId);
			map.put("expiresAt", DateUtils.nowAsGmtMillisec());
			isValid = fpDao.isValidFPSession(map);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isValidFPSession :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isValid;
	}

	public static boolean markSessionAsConsume(String forgotpasswordSessionId) {

		boolean isMarked = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ForgotPasswordSessionDao fpDao = session.getMapper(ForgotPasswordSessionDao.class);

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("forgotpasswordSessionId", forgotpasswordSessionId);
			map.put("expiresAt", DateUtils.nowAsGmtMillisec());
			isMarked = fpDao.markSessionAsConsume(map) > 0 ? true : false;
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during markSessionAsConsume :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isMarked;
	}
}