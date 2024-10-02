package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.ProjectConstants;
import com.webapp.daos.AirportRegionDao;

public class AirportRegionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AirportRegionModel.class);

	private String airportRegionId;
	private String address;
	private String aliasName;
	private String multicityCityRegionId;
	private String areaPolygon;
	private boolean isActive;
	private String regionName;
	private String regionLatitude;
	private String regionLongitude;
	private String sLatitude;
	private String sLongitude;
	private String cityDisplayName;
	private double airportDistance;
	private double airportPickupFixedFareMini;
	private double airportPickupFixedFareSedan;
	private double airportPickupFixedFareSuv;
	private double airportDropFixedFareMini;
	private double airportDropFixedFareSedan;
	private double airportDropFixedFareSuv;

	public String getAirportRegionId() {
		return airportRegionId;
	}

	public void setAirportRegionId(String airportRegionId) {
		this.airportRegionId = airportRegionId;
	}

	public double getAirportDistance() {
		return airportDistance;
	}

	public void setAirportDistance(double airportDistance) {
		this.airportDistance = airportDistance;
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

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getAreaPolygon() {
		return areaPolygon;
	}

	public void setAreaPolygon(String areaPolygon) {
		this.areaPolygon = areaPolygon;
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

	public String getRegionLatitude() {
		return regionLatitude;
	}

	public void setRegionLatitude(String regionLatitude) {
		this.regionLatitude = regionLatitude;
	}

	public String getRegionLongitude() {
		return regionLongitude;
	}

	public void setRegionLongitude(String regionLongitude) {
		this.regionLongitude = regionLongitude;
	}

	public String getsLatitude() {
		return sLatitude;
	}

	public void setsLatitude(String sLatitude) {
		this.sLatitude = sLatitude;
	}

	public String getsLongitude() {
		return sLongitude;
	}

	public void setsLongitude(String sLongitude) {
		this.sLongitude = sLongitude;
	}

	public String getCityDisplayName() {
		return cityDisplayName;
	}

	public void setCityDisplayName(String cityDisplayName) {
		this.cityDisplayName = cityDisplayName;
	}

	public double getAirportPickupFixedFareMini() {
		return airportPickupFixedFareMini;
	}

	public void setAirportPickupFixedFareMini(double airportPickupFixedFareMini) {
		this.airportPickupFixedFareMini = airportPickupFixedFareMini;
	}

	public double getAirportPickupFixedFareSedan() {
		return airportPickupFixedFareSedan;
	}

	public void setAirportPickupFixedFareSedan(double airportPickupFixedFareSedan) {
		this.airportPickupFixedFareSedan = airportPickupFixedFareSedan;
	}

	public double getAirportPickupFixedFareSuv() {
		return airportPickupFixedFareSuv;
	}

	public void setAirportPickupFixedFareSuv(double airportPickupFixedFareSuv) {
		this.airportPickupFixedFareSuv = airportPickupFixedFareSuv;
	}

	public double getAirportDropFixedFareMini() {
		return airportDropFixedFareMini;
	}

	public void setAirportDropFixedFareMini(double airportDropFixedFareMini) {
		this.airportDropFixedFareMini = airportDropFixedFareMini;
	}

	public double getAirportDropFixedFareSedan() {
		return airportDropFixedFareSedan;
	}

	public void setAirportDropFixedFareSedan(double airportDropFixedFareSedan) {
		this.airportDropFixedFareSedan = airportDropFixedFareSedan;
	}

	public double getAirportDropFixedFareSuv() {
		return airportDropFixedFareSuv;
	}

	public void setAirportDropFixedFareSuv(double airportDropFixedFareSuv) {
		this.airportDropFixedFareSuv = airportDropFixedFareSuv;
	}

	public double getFixedFareByCarTypeAndBookingType(String carTypeId, String airportBookingType) {

		if (airportBookingType.equals(ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP)) {
			if (carTypeId.equals(ProjectConstants.Second_Vehicle_ID)) { // 2
				return this.airportPickupFixedFareMini;
			}
			if (carTypeId.equals(ProjectConstants.Third_Vehicle_ID)) { // 3
				return this.airportPickupFixedFareSedan;
			}
			if (carTypeId.equals(ProjectConstants.Fourth_Vehicle_ID)) { // 4
				return this.airportPickupFixedFareSuv;
			}
		} else {
			if (carTypeId.equals(ProjectConstants.Second_Vehicle_ID)) { // 2
				return this.airportDropFixedFareMini;
			}
			if (carTypeId.equals(ProjectConstants.Third_Vehicle_ID)) { // 3
				return this.airportDropFixedFareSedan;
			}
			if (carTypeId.equals(ProjectConstants.Fourth_Vehicle_ID)) { // 4
				return this.airportDropFixedFareSuv;
			}
		}

		return 0;
	}

	public String addAirportRegion(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		this.createdBy = userId;
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.isActive = true;
		try {
			airportRegionDao.addAirportRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addMulticityCityRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.multicityCityRegionId;
	}

	public static int getAirportRegionCount(String globalSearchString, List<String> regionIds, String onOffStatusForCheck, boolean onOffStatus) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("regionIds", regionIds);
		inputMap.put("onOffStatusForCheck", onOffStatusForCheck);
		inputMap.put("onOffStatus", onOffStatus);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		try {
			count = airportRegionDao.getAirportRegionCount(inputMap);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionCount : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static List<AirportRegionModel> getAirportRegionSearchDatatable(int start, int length, String order, String globalSearchString, List<String> regionIds, String onOffStatusForCheck, boolean onOffStatus) {

		List<AirportRegionModel> airportRegionModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("regionIds", regionIds);
		inputMap.put("onOffStatusForCheck", onOffStatusForCheck);
		inputMap.put("onOffStatus", onOffStatus);
		try {
			airportRegionModelList = airportRegionDao.getAirportRegionSearchDatatable(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionSearchDatatable : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return airportRegionModelList;
	}

	public static List<AirportRegionModel> getAirportRegionActiveDeactiveDatatable(int start, int length, String order, String onOffStatusForCheck, boolean onOffStatus, String globalSearchString) {

		List<AirportRegionModel> airportRegionModelList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);
		try {
			airportRegionModelList = airportRegionDao.getAirportRegionActiveDeactiveDatatable(start, length, onOffStatusForCheck, onOffStatus, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionSearchDatatable : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return airportRegionModelList;
	}

	public static AirportRegionModel getAirportRegionDetailsById(String airportRegionId) {

		AirportRegionModel airportRegionModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		try {
			airportRegionModelList = airportRegionDao.getAirportRegionDetailsById(airportRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionDetailsByCityId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return airportRegionModelList;
	}

	public void editAirportRegion(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		this.updatedBy = userId;
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.isActive = true;

		try {
			airportRegionDao.editAirportRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during editMulticityCityRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

	}

	public static AirportRegionModel getPolygonContainingLatLngArea(String latAndLong) {

		AirportRegionModel airportRegionModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		try {
			airportRegionModelList = airportRegionDao.getPolygonContainingLatLngArea(latAndLong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPolygonContainingLatLngArea : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return airportRegionModelList;
	}

	public int updateActiveDeactive() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);
		try {
			count = airportRegionDao.updateActiveDeactive(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateActiveDeactive : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public boolean getDriverCity(String userId, String multiCityRegionId) {

		boolean count = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);
		try {
			count = airportRegionDao.getDriverCity(userId, multiCityRegionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateActiveDeactive : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static AirportRegionModel getAirportRegionContainingLatLng(String latAndLong) {

		AirportRegionModel airportRegionModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		try {
			airportRegionModelList = airportRegionDao.getAirportRegionContainingLatLng(latAndLong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAirportRegionContainingLatLng : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return airportRegionModelList;
	}

	public static List<AirportRegionModel> getScriptAllExistingAirports() {

		List<AirportRegionModel> airportRegionModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AirportRegionDao airportRegionDao = session.getMapper(AirportRegionDao.class);

		try {
			airportRegionModelList = airportRegionDao.getScriptAllExistingAirports();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getScriptAllExistingAirports : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return airportRegionModelList;
	}
}