package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AdminTopUpLogsDao;

public class AdminTopUpLogsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminTopUpLogsModel.class);

	private String adminTopUpLogsId;
	private String adminId;
	private String passengerId;
	private double previousAmount;
	private double newAmount;
	private String status;

	public String getAdminTopUpLogsId() {
		return adminTopUpLogsId;
	}

	public void setAdminTopUpLogsId(String adminTopUpLogsId) {
		this.adminTopUpLogsId = adminTopUpLogsId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public double getPreviousAmount() {
		return previousAmount;
	}

	public void setPreviousAmount(double previousAmount) {
		this.previousAmount = previousAmount;
	}

	public double getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(double newAmount) {
		this.newAmount = newAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String addAdminTopUpLogs(String userId) {

		int status = 0;

		String adminTopUpLogsId = UUIDGenerator.generateUUID();

		this.adminTopUpLogsId = adminTopUpLogsId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminTopUpLogsDao adminTopUpLogsDao = session.getMapper(AdminTopUpLogsDao.class);

		try {
			status = adminTopUpLogsDao.addAdminTopUpLogs(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAdminTopUpLogs : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {
			return adminTopUpLogsId;
		} else {
			return null;
		}
	}
}