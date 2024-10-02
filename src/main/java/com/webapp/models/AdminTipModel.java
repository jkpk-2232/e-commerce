package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.AdminTipDao;

public class AdminTipModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminTipModel.class);

	private String adminTipId;
	private String adminId;
	private long tip;

	public int updateAdminTip() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminTipDao adminTipDao = session.getMapper(AdminTipDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(this.getAdminId());

		try {

			status = adminTipDao.updateAdminTip(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAdminTip :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static AdminTipModel getAdminTipByAdminId(String loggedInUserId) {

		AdminTipModel adminTipModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminTipDao adminTipDao = session.getMapper(AdminTipDao.class);

		try {
			adminTipModel = adminTipDao.getAdminTipByAdminId(loggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminTipByAdminId :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return adminTipModel;
	}

	public static AdminTipModel getAdminTip() {

		AdminTipModel adminTipModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminTipDao adminTipDao = session.getMapper(AdminTipDao.class);

		try {
			adminTipModel = adminTipDao.getAdminTip();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminTip :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return adminTipModel;
	}

	public String getAdminTipId() {
		return adminTipId;
	}

	public void setAdminTipId(String adminTipId) {
		this.adminTipId = adminTipId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public long getTip() {
		return tip;
	}

	public void setTip(long tip) {
		this.tip = tip;
	}

}