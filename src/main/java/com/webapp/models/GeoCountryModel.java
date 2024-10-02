package com.webapp.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.GeoCountryDao;

public class GeoCountryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(GeoCountryModel.class);

	private long countryId;
	private String countryName;
	private double latitude;
	private double longitude;

	public static List<GeoCountryModel> getAllCountries() throws SQLException {

		List<GeoCountryModel> geoCountryList = new ArrayList<GeoCountryModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		GeoCountryDao geoCountryDao = session.getMapper(GeoCountryDao.class);

		try {
			geoCountryList = geoCountryDao.getAllCountries();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllCountries : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return geoCountryList;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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
}
