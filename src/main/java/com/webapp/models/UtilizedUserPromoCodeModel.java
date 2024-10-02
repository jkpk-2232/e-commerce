package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.UtilizedUserPromoCodeDao;

public class UtilizedUserPromoCodeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UtilizedUserPromoCodeModel.class);

	private String utilizedUserPromoCodeId;
	private String promoCodeId;
	private String userId;

	public String getUtilizedUserPromoCodeId() {
		return utilizedUserPromoCodeId;
	}

	public void setUtilizedUserPromoCodeId(String utilizedUserPromoCodeId) {
		this.utilizedUserPromoCodeId = utilizedUserPromoCodeId;
	}

	public String getPromoCodeId() {
		return promoCodeId;
	}

	public void setPromoCodeId(String promoCodeId) {
		this.promoCodeId = promoCodeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int addUtilizedUserPromoCodeList(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UtilizedUserPromoCodeDao utilizedUserPromoCodeDao = session.getMapper(UtilizedUserPromoCodeDao.class);

		try {
			this.utilizedUserPromoCodeId = UUIDGenerator.generateUUID();
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;
			this.createdBy = userId;

			status = utilizedUserPromoCodeDao.addUtilizedUserPromoCodeList(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addUtilizedUserPromoCodeList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static UtilizedUserPromoCodeModel getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(String userId, String promoCodeId) {

		UtilizedUserPromoCodeModel userPromoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UtilizedUserPromoCodeDao utilizedUserPromoCodeDao = session.getMapper(UtilizedUserPromoCodeDao.class);

		try {
			userPromoCodeModel = utilizedUserPromoCodeDao.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(userId, promoCodeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userPromoCodeModel;
	}

	public static long getPromoCodeUsedCount(String promoCodeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UtilizedUserPromoCodeDao utilizedUserPromoCodeDao = session.getMapper(UtilizedUserPromoCodeDao.class);

		try {
			count = utilizedUserPromoCodeDao.getPromoCodeUsedCount(promoCodeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPromoCodeUsedCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}
}