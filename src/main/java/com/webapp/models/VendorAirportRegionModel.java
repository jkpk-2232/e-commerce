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
import com.webapp.daos.VendorAirportRegionDao;

public class VendorAirportRegionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorAirportRegionModel.class);

	private String vendorAirportRegionId;
	private String vendorId;
	private String airportRegionId;
	private String multicityCityRegionId;
	private String multicityCountryId;
	private boolean isActive;

	private String address;
	private String aliasName;
	private String regionName;
	private String cityDisplayName;
	private double airportDistance;

	public String addVendorAirportRegion(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionDao vendorAirportRegion = session.getMapper(VendorAirportRegionDao.class);

		this.vendorAirportRegionId = UUIDGenerator.generateUUID();
		this.createdBy = userId;
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.isActive = true;
		try {
			vendorAirportRegion.addVendorAirportRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addVendorAirportRegion : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorAirportRegionId;
	}

	public static VendorAirportRegionModel getVendorAirportDetailsByVendorIdAndAirportRegionId(String vendorId, String airportRegionId) {

		VendorAirportRegionModel vendorAirportRegionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionDao vendorAirportRegion = session.getMapper(VendorAirportRegionDao.class);

		try {
			vendorAirportRegionModel = vendorAirportRegion.getVendorAirportDetailsByVendorIdAndAirportRegionId(vendorId, airportRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorAirportDetailsByVendorIdAndAirportRegionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorAirportRegionModel;
	}

	public void updateMulticityRegionId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionDao vendorAirportRegion = session.getMapper(VendorAirportRegionDao.class);

		try {
			vendorAirportRegion.updateMulticityRegionId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateMulticityRegionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getVendorAirportRegionCount(List<String> regionIds, String onOffStatusForCheck, boolean onOffStatus, String vendorId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("vendorId", vendorId);
		inputMap.put("regionIds", regionIds);
		inputMap.put("onOffStatusForCheck", onOffStatusForCheck);
		inputMap.put("onOffStatus", onOffStatus);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionDao vendorAirportRegion = session.getMapper(VendorAirportRegionDao.class);

		try {
			count = vendorAirportRegion.getVendorAirportRegionCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorAirportRegionCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<VendorAirportRegionModel> getVendorAirportRegionSearchDatatable(int start, int length, String order, String globalSearchString, List<String> regionIds, String onOffStatusForCheck, boolean onOffStatus, String vendorId) {

		List<VendorAirportRegionModel> airportRegionModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionDao vendorAirportRegion = session.getMapper(VendorAirportRegionDao.class);

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("regionIds", regionIds);
		inputMap.put("onOffStatusForCheck", onOffStatusForCheck);
		inputMap.put("onOffStatus", onOffStatus);
		inputMap.put("vendorId", vendorId);
		try {
			airportRegionModelList = vendorAirportRegion.getVendorAirportRegionSearchDatatable(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorAirportRegionSearchDatatable : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return airportRegionModelList;
	}

	public static VendorAirportRegionModel getVendorAirportRegionDetailsById(String vendorAirportRegionId) {

		VendorAirportRegionModel vendorAirportRegionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionDao vendorAirportRegion = session.getMapper(VendorAirportRegionDao.class);

		try {
			vendorAirportRegionModel = vendorAirportRegion.getVendorAirportRegionDetailsById(vendorAirportRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorAirportRegionDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorAirportRegionModel;
	}

	public String getVendorAirportRegionId() {
		return vendorAirportRegionId;
	}

	public void setVendorAirportRegionId(String vendorAirportRegionId) {
		this.vendorAirportRegionId = vendorAirportRegionId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getAirportRegionId() {
		return airportRegionId;
	}

	public void setAirportRegionId(String airportRegionId) {
		this.airportRegionId = airportRegionId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getMulticityCountryId() {
		return multicityCountryId;
	}

	public void setMulticityCountryId(String multicityCountryId) {
		this.multicityCountryId = multicityCountryId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCityDisplayName() {
		return cityDisplayName;
	}

	public void setCityDisplayName(String cityDisplayName) {
		this.cityDisplayName = cityDisplayName;
	}

	public double getAirportDistance() {
		return airportDistance;
	}

	public void setAirportDistance(double airportDistance) {
		this.airportDistance = airportDistance;
	}
}