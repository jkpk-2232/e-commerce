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
import com.webapp.daos.CitySurgeDao;

public class CitySurgeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CitySurgeModel.class);

	private String citySurgeId;
	private String multicityCityRegionId;
	private String cityName;
	private double surgeRate;
	private double radius;
	private boolean isActive;

	@Override
	public String toString() {
		return "CitySurgeModel [citySurgeId=" + citySurgeId + ", multicityCityRegionId=" + multicityCityRegionId + ", cityName=" + cityName + ", surgeRate=" + surgeRate + ", radius=" + radius + ", isActive=" + isActive + "]";
	}

	public int addCitySurge(String userId) {

		int status = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {

			this.citySurgeId = UUIDGenerator.generateUUID();
			this.isActive = true;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = citySurgeDao.addCitySurge(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addCitySurge : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static List<CitySurgeModel> getAllCitySurgeByRegionyIds(int start, int length, List<String> regionIds) {

		List<CitySurgeModel> citySurgeModelList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("start", start);
			inputMap.put("length", length);
			inputMap.put("regionIds", regionIds);
			citySurgeModelList = citySurgeDao.getAllCitySurgeByRegionyIds(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getAllCitySurgeByStatusAndRegionyIds : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return citySurgeModelList;
	}

	public static int getAllCitySurgeCount(List<String> regionIds) {

		int count = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("regionIds", regionIds);
			count = citySurgeDao.getAllCitySurgeCount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getAllCitySurgeCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updateCitySurge(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			status = citySurgeDao.updateCitySurge(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateCitySurge : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int activateDeactivateCitySurge(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);

			status = citySurgeDao.activateDeactivateCitySurge(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateDeactivateCitySurge :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int deleteCitySurge(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {

			this.isActive = false;
			this.recordStatus = ProjectConstants.DEACTIVATE_STATUS;
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);

			status = citySurgeDao.deleteCitySurge(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteCitySurge :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static CitySurgeModel getCitySurgeByCitySurgeId(String citySurgeId) {

		CitySurgeModel citySurgeModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			citySurgeModel = citySurgeDao.getCitySurgeByCitySurgeId(citySurgeId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCitySurgeByCitySurgeId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return citySurgeModel;
	}

	public static CitySurgeModel getActiveDeactiveDeletedNonDeletedCitySurgeByCitySurgeId(String citySurgeId) {

		CitySurgeModel citySurgeModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			citySurgeModel = citySurgeDao.getActiveDeactiveDeletedNonDeletedCitySurgeByCitySurgeId(citySurgeId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCitySurgeByCitySurgeId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return citySurgeModel;
	}

	public static CitySurgeModel getCitySurgeByRadiusAndRegionId(double radius, String regionId, String citySurgeId) {

		CitySurgeModel citySurgeModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("radius", radius);
			inputMap.put("multicityCityRegionId", regionId);
			inputMap.put("citySurgeId", citySurgeId);
			citySurgeModel = citySurgeDao.getCitySurgeByRadiusAndRegionId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCitySurgeByRadiusAndRegionId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return citySurgeModel;
	}

	public static CitySurgeModel getMaxRadiusCitySurgeByRegionId(String regionId) {

		CitySurgeModel citySurgeModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			citySurgeModel = citySurgeDao.getMaxRadiusCitySurgeByRegionId(regionId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getMaxRadiusCitySurgeByRegionId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return citySurgeModel;
	}

	public static List<CitySurgeModel> getCitySurgeByRegionIdAndRadiusOrder(String regionId, String radiusOrder) {

		List<CitySurgeModel> citySurgeModelList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CitySurgeDao citySurgeDao = session.getMapper(CitySurgeDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("order", radiusOrder);
			inputMap.put("multicityCityRegionId", regionId);
			citySurgeModelList = citySurgeDao.getCitySurgeByRegionIdAndRadiusOrder(inputMap);
			if (citySurgeModelList.size() < 1) {
				citySurgeModelList = null;
			}
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCitySurgeByRegionyIdAndRadiusOrder : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return citySurgeModelList;
	}

	public String getCitySurgeId() {
		return citySurgeId;
	}

	public void setCitySurgeId(String citySurgeId) {
		this.citySurgeId = citySurgeId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public double getSurgeRate() {
		return surgeRate;
	}

	public void setSurgeRate(double surgeRate) {
		this.surgeRate = surgeRate;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
