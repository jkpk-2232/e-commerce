package com.webapp.models;

import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.ActivityLogDao;

public class ActivityLogModel {

	private static Logger logger = Logger.getLogger(RoleModel.class);

	private String activityLogId;
	private String log;
	private long createdAt;
	private String createdBy;
	private String description;
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActivityLogId() {
		return activityLogId;
	}

	public void setActivityLogId(String activityLogId) {
		this.activityLogId = activityLogId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long addInsertLogAb() throws SQLException {
		long insertedRecordId = -1;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ActivityLogDao activityLogDao = session.getMapper(ActivityLogDao.class);
		try {
			this.setAction(AbstractModel.ACTIVE_RECORD_STATUS);
			insertedRecordId = activityLogDao.addInsertLogAb(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addInsertLogAb : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return insertedRecordId;
	}

	public long addUpdateLogAb() throws SQLException {
		long insertedRecordId = -1;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ActivityLogDao activityLogDao = session.getMapper(ActivityLogDao.class);

		try {
			this.setAction(AbstractModel.UPDATED_RECORD_STATUS);
			insertedRecordId = activityLogDao.addInsertLogAb(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addUpdateLogAb : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return insertedRecordId;
	}

	public long addDeleteLogAb() throws SQLException {
		long insertedRecordId = -1;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ActivityLogDao activityLogDao = session.getMapper(ActivityLogDao.class);

		try {
			this.setAction(AbstractModel.DELETED_RECORD_STATUS);
			insertedRecordId = activityLogDao.addInsertLogAb(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addDeleteLogAb : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return insertedRecordId;
	}

}
