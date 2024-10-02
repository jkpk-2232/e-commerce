package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.RentalPackageDao;

public class RentalPackageModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(RentalPackageModel.class);

	private String rentalPackageId;
	private String multicityCityRegionId;
	private long packageTime;
	private double packageDistance;
	private String regionName;
	private double latitude;
	private double longitude;
	private boolean isActive;
	private String rentalPackageType;
	private String vendorId;

	public String getRentalPackageId() {
		return rentalPackageId;
	}

	public void setRentalPackageId(String rentalPackageId) {
		this.rentalPackageId = rentalPackageId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public long getPackageTime() {
		return packageTime;
	}

	public void setPackageTime(long packageTime) {
		this.packageTime = packageTime;
	}

	public double getPackageDistance() {
		return packageDistance;
	}

	public void setPackageDistance(double packageDistance) {
		this.packageDistance = packageDistance;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getRentalPackageType() {
		return rentalPackageType;
	}

	public void setRentalPackageType(String rentalPackageType) {
		this.rentalPackageType = rentalPackageType;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String addRentalPackage(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {
			this.rentalPackageId = UUIDGenerator.generateUUID();
			this.isActive = true;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;
			status = rentalPackageDao.addRentalPackage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addRentalPackage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {

			return this.rentalPackageId;
		} else {

			return null;
		}
	}

	public int updateRentalPackage(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			status = rentalPackageDao.updateRentalPackage(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateRentalPackage : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static RentalPackageModel getRentalPackageDetailsById(String rentalPackageId) {

		RentalPackageModel rentalPackageModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {

			rentalPackageModel = rentalPackageDao.getRentalPackageDetailsById(rentalPackageId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getRentalPackageDetailsById : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return rentalPackageModel;
	}

	public static int getTotalRentalPackagesCount(List<String> regionIds, String rentalPackageType, String[] vendorIds) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("regionIds", regionIds);
		inputMap.put("rentalPackageType", rentalPackageType);
		inputMap.put("vendorIds", vendorIds);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {
			count = rentalPackageDao.getTotalRentalPackagesCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalRentalPackagesCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<RentalPackageModel> getRentalPackageListForSearch(int start, int length, String order, String globalSearchString, List<String> regionIds, String rentalPackageType, String[] vendorIds) {

		List<RentalPackageModel> rentalPackageModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("regionIds", regionIds);
		inputMap.put("rentalPackageType", rentalPackageType);
		inputMap.put("vendorIds", vendorIds);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {
			rentalPackageModelList = rentalPackageDao.getRentalPackageListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRentalPackageListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return rentalPackageModelList;
	}

	public static List<RentalPackageModel> getRentalPackageListPagination(int start, int length, String multicityCityRegionId, String rentalPackageType, String vendorId) {

		List<RentalPackageModel> rentalPackageModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("multicityCityRegionId", multicityCityRegionId);
		inputMap.put("rentalPackageType", rentalPackageType);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {
			rentalPackageModelList = rentalPackageDao.getRentalPackageListPagination(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRentalPackageListPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return rentalPackageModelList;
	}

	public void activeDeactiveRentalPackage(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {

			rentalPackageDao.activeDeactiveRentalPackage(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activeDeactiveRentalPackage : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}
	}

	public void deleteRentalPackage(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {

			rentalPackageDao.deleteRentalPackage(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteRentalPackage : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}
	}

	public void updateVendorIdForRentalPackages() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = "Script";

		try {
			rentalPackageDao.updateVendorIdForRentalPackages(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorIdForRentalPackages : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<RentalPackageModel> getAllRentalPackages() {

		List<RentalPackageModel> list = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		try {
			list = rentalPackageDao.getAllRentalPackages();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllRentalPackages : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}
}