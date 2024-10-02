package com.webapp.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.WhmgmtRackDao;

public class WhmgmtRackModel {

	private static Logger logger = Logger.getLogger(WhmgmtRackModel.class);

	private String rackId;
	private String rackNumber;
	private String description;
	private boolean rackStatus;
	private String rackCatId;
	private String storeId;
	private String vendorId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String createdBy;
	private String updatedBy;
	private String vendorName;
	private String storeName;

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getRackNumber() {
		return rackNumber;
	}

	public void setRackNumber(String rackNumber) {
		this.rackNumber = rackNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getRackStatus() {
		return rackStatus;
	}

	public void setRackStatus(boolean rackStatus) {
		this.rackStatus = rackStatus;
	}

	public String getRackCatId() {
		return rackCatId;
	}

	public void setRackCatId(String rackCatId) {
		this.rackCatId = rackCatId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public static List<WhmgmtRackModel> getRackListByvendorIdAndStoreId(String vendorId, String vendorStoreId, int start , int length) {
		
		List<WhmgmtRackModel> rackModelList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackDao rackDao = session.getMapper(WhmgmtRackDao.class);

		try {
			rackModelList = rackDao.getRackListByvendorIdAndStoreId(vendorId, vendorStoreId, start , length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRackListByvendorIdAndStoreId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return rackModelList;
	}

	public static void insertRacks(List<WhmgmtRackModel> rackList) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackDao rackDao = session.getMapper(WhmgmtRackDao.class);

		try {
			rackDao.insertRacks(rackList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertRacks : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static void insertRack(WhmgmtRackModel rackModel) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackDao rackDao = session.getMapper(WhmgmtRackDao.class);
		
		try {
			rackDao.insertRack(rackModel);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertRack : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

}
