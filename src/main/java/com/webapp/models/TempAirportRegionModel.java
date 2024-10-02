package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.TempAirportRegionDao;

public class TempAirportRegionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TempAirportRegionModel.class);

	private String tempAirportRegionId;
	private String areaPolygon;
	private String regionLatitude;
	private String regionLongitude;

	public String getAreaPolygon() {
		return areaPolygon;
	}

	public void setAreaPolygon(String areaPolygon) {
		this.areaPolygon = areaPolygon;
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

	public static TempAirportRegionModel checkLatLongInAirportRegion(String lati, String longi) {

		String latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + lati + " " + longi + ")'),area_polygon)";

		logger.info("\n\n\n\tlatAndLong\t" + latAndLong);

		TempAirportRegionModel airportRegionModel = TempAirportRegionModel.checkMarkerLiesInPolygonRegion(latAndLong);

		return airportRegionModel;
	}

	public static TempAirportRegionModel checkMarkerLiesInPolygonRegion(String latAndLong) {

		TempAirportRegionModel tempAirportRegionModelList = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TempAirportRegionDao tempAirportRegionDao = session.getMapper(TempAirportRegionDao.class);

		try {
			tempAirportRegionModelList = tempAirportRegionDao.checkMarkerLiesInPolygonRegion(latAndLong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPolygonContainingLatLngArea : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return tempAirportRegionModelList;
	}

	public String addTempAirportRegion() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TempAirportRegionDao tempAirportRegionDao = session.getMapper(TempAirportRegionDao.class);
		this.tempAirportRegionId = UUIDGenerator.generateUUID();
		try {
			tempAirportRegionDao.addTempAirportRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addMulticityCityRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.tempAirportRegionId;
	}

	public void deleteTempAirportRegion() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TempAirportRegionDao tempAirportRegionDao = session.getMapper(TempAirportRegionDao.class);

		this.tempAirportRegionId = UUIDGenerator.generateUUID();
		try {
			tempAirportRegionDao.deleteTempAirportRegion();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addMulticityCityRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
	}

	public String getTempAirportRegionId() {
		return tempAirportRegionId;
	}

	public void setTempAirportRegionId(String tempAirportRegionId) {
		this.tempAirportRegionId = tempAirportRegionId;
	}

}
