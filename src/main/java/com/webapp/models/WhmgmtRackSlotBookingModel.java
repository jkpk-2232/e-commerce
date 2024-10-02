package com.webapp.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.WhmgmtRackSlotBookingDao;

public class WhmgmtRackSlotBookingModel {

	private static Logger logger = Logger.getLogger(WhmgmtRackSlotBookingModel.class);

	private long id;
	private Timestamp startDate;
	private Timestamp endDate;
	private boolean isActive;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String vendorId;
	private String slotId;
	private String orderId;

	private String userName;
	private String slotStatus;
	private String slotNumber;
	private String productId;
	private String rackId;
	private String rackNumber;
	private String storeId;
	private String orginalVendorId;
	private String productName;
	private String phoneNumber;

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSlotStatus() {
		return slotStatus;
	}

	public void setSlotStatus(String slotStatus) {
		this.slotStatus = slotStatus;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getOrginalVendorId() {
		return orginalVendorId;
	}

	public void setOrginalVendorId(String orginalVendorId) {
		this.orginalVendorId = orginalVendorId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static int getRackSlotBookingCount(String vendorId, String vendorStroeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackSlotBookingDao whmgmtRackSlotBookingDao = session.getMapper(WhmgmtRackSlotBookingDao.class);

		try {
			count = whmgmtRackSlotBookingDao.getRackSlotBookingCount(vendorId, vendorStroeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRackSlotBookingCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<WhmgmtRackSlotBookingModel> getRackSlotBookingSearch(String vendorId, String vendorStroeId, int start, int length) {

		List<WhmgmtRackSlotBookingModel> slotBookingList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackSlotBookingDao whmgmtRackSlotBookingDao = session.getMapper(WhmgmtRackSlotBookingDao.class);

		try {
			slotBookingList = whmgmtRackSlotBookingDao.getRackSlotBookingSearch(vendorId, vendorStroeId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRackSlotBookingSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return slotBookingList;
	}

}
