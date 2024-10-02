package com.webapp.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.GeoCityDao;

public class GeoCityModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(GeoCityModel.class);

	private long cityId;
	private long stateId;
	private long countryId;
	private String cityName;
	private long latitude;
	private long longitude;

	public int insertCity() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		GeoCityDao geoCityDao = session.getMapper(GeoCityDao.class);

		try {
			status = geoCityDao.insertCity(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertCity :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<GeoCityModel> getAllCitesByStateId(long stateId) throws SQLException {

		List<GeoCityModel> geoCityList = new ArrayList<GeoCityModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		GeoCityDao geoCityDao = session.getMapper(GeoCityDao.class);

		try {
			geoCityList = geoCityDao.getAllCitesByStateId(stateId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllCitesByStateId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return geoCityList;
	}

	public static String getCityNameByCityId(long cityId) {
		String city = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		GeoCityDao geoCityDao = session.getMapper(GeoCityDao.class);

		try {
			city = geoCityDao.getCityNameByCityId(cityId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCityNameByCityId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return city;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public long getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
}
