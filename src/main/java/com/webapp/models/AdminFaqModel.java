package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AdminFaqDao;

public class AdminFaqModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminFaqModel.class);

	private String adminFaqId;
	private String userId;
	private String question;
	private String answer;

	public String getAdminFaqId() {
		return adminFaqId;
	}

	public void setAdminFaqId(String adminFaqId) {
		this.adminFaqId = adminFaqId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int addAdminFaq(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		try {

			this.adminFaqId = UUIDGenerator.generateUUID();
			this.userId = userId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = adminFaqDao.addAdminFaq(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAdminFaq : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static int getAdminFaqCount() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		try {
			status = adminFaqDao.getAdminFaqCount();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminFaqCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<AdminFaqModel> getAdminFaqListForSearch(int start, int length, String order, String globalSearchString) {

		List<AdminFaqModel> adminAreaModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		try {
			adminAreaModel = adminFaqDao.getAdminFaqListForSearch(start, length, order, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminFaqListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminAreaModel;
	}

	public static int getTotalAdminFaqCountBySearch(String globalSearchString) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		try {
			status = adminFaqDao.getTotalAdminFaqCountBySearch(globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalAdminFaqCountBySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int deleteAdminFaq(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			status = adminFaqDao.deleteAdminFaq(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteAdminFaq : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<AdminFaqModel> getAdminFaqList() {

		List<AdminFaqModel> adminAreaModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		try {
			adminAreaModel = adminFaqDao.getAdminFaqList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminFaqList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminAreaModel;
	}

	public static AdminFaqModel getAdminFaqModelById(String adminFaqId) {

		AdminFaqModel adminAreaModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		try {
			adminAreaModel = adminFaqDao.getAdminFaqModelById(adminFaqId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminFaqModelById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminAreaModel;
	}

	public int editAdminFaq(String loggedInUserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminFaqDao adminFaqDao = session.getMapper(AdminFaqDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();

		try {
			status = adminFaqDao.editAdminFaq(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during editAdminFaq : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}
}