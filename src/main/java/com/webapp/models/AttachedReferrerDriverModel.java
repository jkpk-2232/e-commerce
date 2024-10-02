package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AttachedReferrerDriverDao;

public class AttachedReferrerDriverModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AttachedReferrerDriverModel.class);

	private String attachedReferrerDriverId;
	private String attachedDriverId;
	private String userId;
	private String referredUserRoleId;

	public String getAttachedReferrerDriverId() {
		return attachedReferrerDriverId;
	}

	public void setAttachedReferrerDriverId(String attachedReferrerDriverId) {
		this.attachedReferrerDriverId = attachedReferrerDriverId;
	}

	public String getAttachedDriverId() {
		return attachedDriverId;
	}

	public void setAttachedDriverId(String attachedDriverId) {
		this.attachedDriverId = attachedDriverId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReferredUserRoleId() {
		return referredUserRoleId;
	}

	public void setReferredUserRoleId(String referredUserRoleId) {
		this.referredUserRoleId = referredUserRoleId;
	}

	public int addAttachedReferrerDriver(String driverId, String userId, String userRoleId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AttachedReferrerDriverDao attachedReferrerDriverDao = session.getMapper(AttachedReferrerDriverDao.class);

		try {

			this.attachedReferrerDriverId = UUIDGenerator.generateUUID();
			this.attachedDriverId = driverId;
			this.userId = userId;
			this.referredUserRoleId = userRoleId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;
			this.recordStatus = "A";

			status = attachedReferrerDriverDao.addAttachedReferrerDriver(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addAttachedReferrerDriver : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

}