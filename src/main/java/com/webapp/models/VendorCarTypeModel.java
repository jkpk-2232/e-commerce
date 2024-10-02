package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorCarTypeDao;

public class VendorCarTypeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorCarTypeModel.class);

	private String vendorCarTypeId;
	private String vendorId;
	private String carTypeId;
	private boolean isActive;
	private boolean isDeleted;
	private boolean isPermanentDeleted;

	private String carType;
	private String icon;

	private String serviceTypeId;
	private int priority;

	// done
	public void insertVendorCarType(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);

		this.vendorCarTypeId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		try {
			this.isActive = true;
			this.isDeleted = false;
			this.isPermanentDeleted = false;
			vendorCarTypeDao.insertVendorCarType(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorCarType : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<VendorCarTypeModel> getVendorCarTypeListByVendorId(String vendorId, String serviceTypeId) {

		List<VendorCarTypeModel> list = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);

		try {
			list = vendorCarTypeDao.getVendorCarTypeListByVendorId(vendorId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCarTypeListByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}
	
	public static List<VendorCarTypeModel> getVendorCarTypeListByVendorIdSortPriority(String vendorId, String serviceTypeId) {

		List<VendorCarTypeModel> list = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);

		try {
			list = vendorCarTypeDao.getVendorCarTypeListByVendorIdSortPriority(vendorId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCarTypeListByVendorIdSortPriority : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static VendorCarTypeModel getVendorCarTypeListByVendorIdForBike(String vendorId, String serviceTypeId, String carTypeId) {

		VendorCarTypeModel list = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);

		try {
			list = vendorCarTypeDao.getVendorCarTypeListByVendorIdForBike(vendorId, serviceTypeId, carTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCarTypeListByVendorIdForBike : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static void batchInsertVendorCarType(List<VendorCarTypeModel> vendorCarTypeList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);
		try {
			vendorCarTypeDao.batchInsertVendorCarType(vendorCarTypeList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during batchInsertVendorCarType : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	// done
	public void deleteExistingCarTypes() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);

		try {
			vendorCarTypeDao.deleteExistingCarTypes(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteExistingCarTypes : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateVendorCarTypeStatusByCarTypeId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarTypeDao vendorCarTypeDao = session.getMapper(VendorCarTypeDao.class);

		try {
			vendorCarTypeDao.updateVendorCarTypeStatusByCarTypeId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorCarTypeStatusByCarTypeId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorCarTypeId() {
		return vendorCarTypeId;
	}

	public void setVendorCarTypeId(String vendorCarTypeId) {
		this.vendorCarTypeId = vendorCarTypeId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isPermanentDeleted() {
		return isPermanentDeleted;
	}

	public void setPermanentDeleted(boolean isPermanentDeleted) {
		this.isPermanentDeleted = isPermanentDeleted;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}