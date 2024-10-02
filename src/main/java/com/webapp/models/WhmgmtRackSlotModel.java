package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.WhmgmtRackDao;
import com.webapp.daos.WhmgmtRackSlotDao;

public class WhmgmtRackSlotModel {

	private static Logger logger = Logger.getLogger(WhmgmtRackModel.class);

	private String slotId;
	private String slotNumber;
	private String slotStatus;
	private String productId;
	private String rackId;

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String getSlotStatus() {
		return slotStatus;
	}

	public void setSlotStatus(String slotStatus) {
		this.slotStatus = slotStatus;
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

	public static void insertSlots(List<WhmgmtRackSlotModel> whmgmtRackSlotList) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackSlotDao slotDao = session.getMapper(WhmgmtRackSlotDao.class);

		try {
			slotDao.insertSlots(whmgmtRackSlotList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertSlots : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

}
