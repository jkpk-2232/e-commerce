package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorFeedLikeDao;

public class VendorFeedLikeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorFeedLikeModel.class);

	private String vendorFeedLikeId;
	private String vendorFeedId;
	private String vendorId;
	private String userId;

	public String insertVendorFeedLike(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedLikeDao vendorFeedLikeDao = session.getMapper(VendorFeedLikeDao.class);

		this.vendorFeedLikeId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorFeedLikeDao.insertVendorFeedLike(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorFeedLike : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorFeedLikeId;
	}

	public void deleteVendorFeedLikesByVendorFeedId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedLikeDao vendorFeedLikeDao = session.getMapper(VendorFeedLikeDao.class);

		try {
			vendorFeedLikeDao.deleteVendorFeedLikesByVendorFeedId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteVendorFeedLikesByVendorFeedId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static boolean isVendorFeedLikedByUserId(String userId, String vendorFeedId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedLikeDao vendorFeedLikeDao = session.getMapper(VendorFeedLikeDao.class);

		try {
			status = vendorFeedLikeDao.isVendorFeedLikedByUserId(userId, vendorFeedId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isVendorFeedLikedByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public void deleteVendorFeedLikeByUserId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedLikeDao vendorFeedLikeDao = session.getMapper(VendorFeedLikeDao.class);

		try {
			vendorFeedLikeDao.deleteVendorFeedLikeByUserId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteVendorFeedLikeByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getVendorFeedLikesCount(String vendorFeedId) {

		int vendorFeedLikeCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedLikeDao vendorFeedLikeDao = session.getMapper(VendorFeedLikeDao.class);

		try {
			vendorFeedLikeCount = vendorFeedLikeDao.getVendorFeedLikesCount(vendorFeedId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedLikesCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedLikeCount;
	}

	public String getVendorFeedLikeId() {
		return vendorFeedLikeId;
	}

	public void setVendorFeedLikeId(String vendorFeedLikeId) {
		this.vendorFeedLikeId = vendorFeedLikeId;
	}

	public String getVendorFeedId() {
		return vendorFeedId;
	}

	public void setVendorFeedId(String vendorFeedId) {
		this.vendorFeedId = vendorFeedId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}