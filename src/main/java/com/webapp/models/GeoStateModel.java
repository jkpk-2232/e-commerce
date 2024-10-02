package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.GeoStateDao;

public class GeoStateModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(GeoStateModel.class);

	private long stateId;
	private long countryId;
	private String stateName;

	public static List<GeoStateModel> getAllStatesByCountryId(long countryId) {

		List<GeoStateModel> geoStateList = new ArrayList<GeoStateModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		GeoStateDao geoStateDao = session.getMapper(GeoStateDao.class);

		try {
			geoStateList = geoStateDao.getAllStatesByCountryId(countryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllStatesByCountryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return geoStateList;
	}

	public static String getStateNameByStateId(long stateId) {
		String state = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		GeoStateDao geoStateDao = session.getMapper(GeoStateDao.class);

		try {
			state = geoStateDao.getStateNameByStateId(stateId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getStateNameByStateId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return state;
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

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
