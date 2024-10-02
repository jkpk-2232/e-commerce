package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.BusinessOperatorDao;

public class BusinessOperatorModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(BusinessOperatorModel.class);

	private String businessOperatorId;
	private String businessOwnerId;
	private String operatorId;
	private boolean deleted;

	public String getBusinessOperatorId() {
		return businessOperatorId;
	}

	public void setBusinessOperatorId(String businessOperatorId) {
		this.businessOperatorId = businessOperatorId;
	}

	public String getBusinessOwnerId() {
		return businessOwnerId;
	}

	public void setBusinessOwnerId(String businessOwnerId) {
		this.businessOwnerId = businessOwnerId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int assignBusinessOperatorToBusinessOwner(String loggedInUserId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BusinessOperatorDao businessOperatorDao = session.getMapper(BusinessOperatorDao.class);

		try {
			this.setBusinessOperatorId(UUIDGenerator.generateUUID());
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(this.getCreatedAt());
			this.setCreatedBy(loggedInUserId);
			this.setUpdatedBy(loggedInUserId);

			status = businessOperatorDao.assignBusinessOperatorToBusinessOwner(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during assignBusinessOperatorToBusinessOwner : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static String getBusinessOwnerId(String userId) {

		String businessOwnerId = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BusinessOperatorDao businessOperatorDao = session.getMapper(BusinessOperatorDao.class);

		try {
			businessOwnerId = businessOperatorDao.getBusinessOwnerId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBusinessOwnerId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return businessOwnerId;
	}
	
}