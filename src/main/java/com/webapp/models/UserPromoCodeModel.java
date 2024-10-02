package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.UserPromoCodeDao;

public class UserPromoCodeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UserPromoCodeModel.class);

	private String userPromoCodeId;
	private String promoCodeId;
	private String userId;

	public String getUserPromoCodeId() {
		return userPromoCodeId;
	}

	public void setUserPromoCodeId(String userPromoCodeId) {
		this.userPromoCodeId = userPromoCodeId;
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

	public static int addUserPromoCodeList(List<UserPromoCodeModel> userPromoCodeList) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserPromoCodeDao userPromoCodeDao = session.getMapper(UserPromoCodeDao.class);

		try {
			status = userPromoCodeDao.addUserPromoCodeList(userPromoCodeList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addUserPromoCodeList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static UserPromoCodeModel getUserPromoCodeDetailsByPromoCodeIdAndUserId(String userId, String promoCodeId) {

		UserPromoCodeModel userPromoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UserPromoCodeDao userPromoCodeDao = session.getMapper(UserPromoCodeDao.class);

		try {
			userPromoCodeModel = userPromoCodeDao.getUserPromoCodeDetailsByPromoCodeIdAndUserId(userId, promoCodeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserPromoCodeDetailsByPromoCodeIdAndUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return userPromoCodeModel;
	}
}