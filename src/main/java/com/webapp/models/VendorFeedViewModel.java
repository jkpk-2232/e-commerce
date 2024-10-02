package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorFeedViewDao;

public class VendorFeedViewModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorFeedViewModel.class);

	private String vendorFeedViewId;
	private String vendorFeedId;
	private String vendorId;
	private String userId;

	public String insertVendorFeedView(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedViewDao vendorFeedViewDao = session.getMapper(VendorFeedViewDao.class);

		this.vendorFeedViewId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorFeedViewDao.insertVendorFeedView(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorFeedView : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorFeedViewId;
	}

	public void deleteVendorFeedViewsByVendorFeedId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedViewDao vendorFeedViewDao = session.getMapper(VendorFeedViewDao.class);

		try {
			vendorFeedViewDao.deleteVendorFeedViewsByVendorFeedId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteVendorFeedViewsByVendorFeedId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static boolean isVendorFeedViewedByUserId(String userId, String vendorFeedId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedViewDao vendorFeedViewDao = session.getMapper(VendorFeedViewDao.class);

		try {
			status = vendorFeedViewDao.isVendorFeedViewedByUserId(userId, vendorFeedId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isVendorFeedViewedByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static int getVendorFeedViewsCount(String vendorFeedId) {

		int vendorFeedViewCount = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedViewDao vendorFeedViewDao = session.getMapper(VendorFeedViewDao.class);

		try {
			vendorFeedViewCount = vendorFeedViewDao.getVendorFeedViewsCount(vendorFeedId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedViewsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedViewCount;
	}

	public String getVendorFeedViewId() {
		return vendorFeedViewId;
	}

	public void setVendorFeedViewId(String vendorFeedViewId) {
		this.vendorFeedViewId = vendorFeedViewId;
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