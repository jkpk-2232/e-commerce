package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.QrProfileDao;

public class QrProfileModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(QrProfileModel.class);

	private String qrProfileId;
	private String vendorStoreId;
	private String qrCodeId;
	private String terminalId;

	public String getQrProfileId() {
		return qrProfileId;
	}

	public void setQrProfileId(String qrProfileId) {
		this.qrProfileId = qrProfileId;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getQrCodeId() {
		return qrCodeId;
	}

	public void setQrCodeId(String qrCodeId) {
		this.qrCodeId = qrCodeId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String insertQrProfile() {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		QrProfileDao qrProfileDao = session.getMapper(QrProfileDao.class);

		this.qrProfileId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;

		try {
			qrProfileDao.insertQrProfile(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertQrProfile : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.qrProfileId;
		
	}

	public static int getQrProfileCount(String vendorStoreId) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		QrProfileDao qrProfileDao = session.getMapper(QrProfileDao.class);

		try {
			count = qrProfileDao.getQrProfileCount(vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getQrProfileCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
		
	}

	public static List<QrProfileModel> getQrProfileSearch(int start, int length, String searchKey, String vendorStoreId) {
		
		List<QrProfileModel> qrProfileModelList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		QrProfileDao qrProfileDao = session.getMapper(QrProfileDao.class);

		try {
			qrProfileModelList = qrProfileDao.getQrProfileSearch(start, length, searchKey, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getQrProfileSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return qrProfileModelList;
		
	}

	public static int getQrProfileSearchCount(String searchKey, String vendorStoreId) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		QrProfileDao qrProfileDao = session.getMapper(QrProfileDao.class);

		try {
			count = qrProfileDao.getQrProfileSearchCount(searchKey, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getQrProfileSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
		
	}

	public static boolean isQrCodeIdExists(String qrCodeId, String qrProfileId) {
		
		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		QrProfileDao qrProfileDao = session.getMapper(QrProfileDao.class);

		try {
			isDuplicate = qrProfileDao.isQrCodeIdExists(qrCodeId, qrProfileId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isQrCodeIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
		
	}

}
