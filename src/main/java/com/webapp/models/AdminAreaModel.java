package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AdminAreaDao;

public class AdminAreaModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminAreaModel.class);

	private String adminAreaId;
	private String userId;
	private String areaDisplayName;
	private String areaName;
	private String areaPlaceId;
	private String areaLatitude;
	private String areaLongitude;
	private String areaGeolocation;
	private String areaCountry;
	private long areaRadius;

	public String getAdminAreaId() {
		return adminAreaId;
	}

	public void setAdminAreaId(String adminAreaId) {
		this.adminAreaId = adminAreaId;
	}

	public String getAreaDisplayName() {
		return areaDisplayName;
	}

	public void setAreaDisplayName(String areaDisplayName) {
		this.areaDisplayName = areaDisplayName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaPlaceId() {
		return areaPlaceId;
	}

	public void setAreaPlaceId(String areaPlaceId) {
		this.areaPlaceId = areaPlaceId;
	}

	public String getAreaLatitude() {
		return areaLatitude;
	}

	public void setAreaLatitude(String areaLatitude) {
		this.areaLatitude = areaLatitude;
	}

	public String getAreaLongitude() {
		return areaLongitude;
	}

	public void setAreaLongitude(String areaLongitude) {
		this.areaLongitude = areaLongitude;
	}

	public String getAreaGeolocation() {
		return areaGeolocation;
	}

	public void setAreaGeolocation(String areaGeolocation) {
		this.areaGeolocation = areaGeolocation;
	}

	public long getAreaRadius() {
		return areaRadius;
	}

	public void setAreaRadius(long areaRadius) {
		this.areaRadius = areaRadius;
	}

	public String getAreaCountry() {
		return areaCountry;
	}

	public void setAreaCountry(String areaCountry) {
		this.areaCountry = areaCountry;
	}

	public int addAdminArea(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		try {

			this.adminAreaId = UUIDGenerator.generateUUID();
			this.userId = userId;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			logger.info("\n\n\n\t" + "ST_GeographyFromText('SRID=4326;POINT(" + this.areaLongitude + "  " + this.areaLatitude + ")')");

			this.setAreaGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.areaLongitude + "  " + this.areaLatitude + ")')");

			status = adminAreaDao.addAdminArea(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addAdminArea : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static int getAdminAreaCount() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		try {
			status = adminAreaDao.getAdminAreaCount();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminAreaCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<AdminAreaModel> getAdminAreaListForSearch(int start, int length, String order, String globalSearchString) {

		List<AdminAreaModel> adminAreaModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		try {
			adminAreaModel = adminAreaDao.getAdminAreaListForSearch(start, length, order, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminAreaListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminAreaModel;
	}

	public static int getTotalAdminAreaCountBySearch(String globalSearchString) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		try {
			status = adminAreaDao.getTotalAdminAreaCountBySearch(globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalAdminAreaCountBySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int deleteAdminArea(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			status = adminAreaDao.deleteAdminArea(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteAdminArea : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static boolean isAdminAreaExists(String areaPlaceId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		try {
			status = adminAreaDao.isAdminAreaExists(areaPlaceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isAdminAreaExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<AdminAreaModel> getAdminAreaList() {

		List<AdminAreaModel> adminAreaModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminAreaDao adminAreaDao = session.getMapper(AdminAreaDao.class);

		try {
			adminAreaModel = adminAreaDao.getAdminAreaList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminAreaList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminAreaModel;
	}
}