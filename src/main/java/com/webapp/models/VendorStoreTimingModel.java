package com.webapp.models;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.VendorStoreTimingDao;

public class VendorStoreTimingModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorStoreTimingModel.class);

	private String vendorStoreTimingId;
	private String vendorStoreId;
	private String day;
	private long openingMorningHours;
	private long closingMorningHours;
	private long openingEveningHours;
	private long closingEveningHours;

	public String addVendorStoreTiming(String userId, String vendorStoreId) throws SQLException {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreTimingDao vendorStoreTimingDao = session.getMapper(VendorStoreTimingDao.class);

		this.vendorStoreTimingId = UUIDGenerator.generateUUID();
		this.vendorStoreId = vendorStoreId;
		this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorStoreTimingDao.addVendorStoreTiming(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addVendorStoreTiming : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorStoreTimingId;
	}

	public static void deletePreviousEntries(String vendorStoreId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreTimingDao vendorStoreTimingDao = session.getMapper(VendorStoreTimingDao.class);

		try {
			vendorStoreTimingDao.deletePreviousEntries(vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deletePreviousEntries : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<VendorStoreTimingModel> getVendorStoreTimingListById(String vendorStoreId) {

		List<VendorStoreTimingModel> list = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreTimingDao vendorStoreTimingDao = session.getMapper(VendorStoreTimingDao.class);

		try {
			list = vendorStoreTimingDao.getVendorStoreTimingListById(vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreTimingListById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public String getVendorStoreTimingId() {
		return vendorStoreTimingId;
	}

	public void setVendorStoreTimingId(String vendorStoreTimingId) {
		this.vendorStoreTimingId = vendorStoreTimingId;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public long getOpeningMorningHours() {
		return openingMorningHours;
	}

	public void setOpeningMorningHours(long openingMorningHours) {
		this.openingMorningHours = openingMorningHours;
	}

	public long getClosingMorningHours() {
		return closingMorningHours;
	}

	public void setClosingMorningHours(long closingMorningHours) {
		this.closingMorningHours = closingMorningHours;
	}

	public long getOpeningEveningHours() {
		return openingEveningHours;
	}

	public void setOpeningEveningHours(long openingEveningHours) {
		this.openingEveningHours = openingEveningHours;
	}

	public long getClosingEveningHours() {
		return closingEveningHours;
	}

	public void setClosingEveningHours(long closingEveningHours) {
		this.closingEveningHours = closingEveningHours;
	}
}