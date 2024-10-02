package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.BrandDao;
import com.webapp.daos.WhmgmtRackCategoryDao;

public class WhmgmtRackCategoryModel {
	
	private static Logger logger = Logger.getLogger(WhmgmtRackCategoryModel.class);

	public String categoryId;
	private String categoryName;
	private double slotHeight;
	private double slotWidth;
	private double maxWeight;
	private boolean catStatus;
	private int chargePerSlot;
	private int noOfSlots;
	private int numberOfDays;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getSlotHeight() {
		return slotHeight;
	}

	public void setSlotHeight(double slotHeight) {
		this.slotHeight = slotHeight;
	}

	public double getSlotWidth() {
		return slotWidth;
	}

	public void setSlotWidth(double slotWidth) {
		this.slotWidth = slotWidth;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public boolean isCatStatus() {
		return catStatus;
	}

	public void setCatStatus(boolean catStatus) {
		this.catStatus = catStatus;
	}

	public int getChargePerSlot() {
		return chargePerSlot;
	}

	public void setChargePerSlot(int chargePerSlot) {
		this.chargePerSlot = chargePerSlot;
	}

	public int getNoOfSlots() {
		return noOfSlots;
	}

	public void setNoOfSlots(int noOfSlots) {
		this.noOfSlots = noOfSlots;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public static List<WhmgmtRackCategoryModel> getRackCategoryList() {
		
		List<WhmgmtRackCategoryModel> rackCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);

		try {
			rackCategoryList = rackCategoryDao.getRackCategoryList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRackCategoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return rackCategoryList;
	}

	public static WhmgmtRackCategoryModel getRackCategoryDetailsByCategoryId(String rackCategoryId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);
		
		WhmgmtRackCategoryModel whmgmtRackCategoryModel = null;
		
		try {
			whmgmtRackCategoryModel = rackCategoryDao.getRackCategoryDetailsByCategoryId(rackCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRackDetailsByCategoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return whmgmtRackCategoryModel;
		
	}

	public void insertRackCategory(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);
		
		try {
			this.setCategoryId(UUIDGenerator.generateUUID());
			rackCategoryDao.insertRackCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertRackCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public void updateRackCategory(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);
		
		try {
			rackCategoryDao.updateRackCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRackCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static boolean isCategoryNameExists(String categoryName, String categoryId) {
		
		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);

		try {
			isDuplicate = rackCategoryDao.isCategoryNameExists(categoryName, categoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isCategoryNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}

	public static List<WhmgmtRackCategoryModel> getRackCategorySearch(String searchKey, int start, int length) {
		
		List<WhmgmtRackCategoryModel> rackCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);

		try {
			rackCategoryList = rackCategoryDao.getRackCategorySearch(searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRackCategorySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return rackCategoryList;
	}

	public static int getRackCategorySearchCount(String searchKey) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		WhmgmtRackCategoryDao rackCategoryDao = session.getMapper(WhmgmtRackCategoryDao.class);

		try {

			count = rackCategoryDao.getRackCategorySearchCount(searchKey);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

}
