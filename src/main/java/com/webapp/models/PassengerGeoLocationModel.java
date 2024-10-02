package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.PassengerGeoLocationDao;

public class PassengerGeoLocationModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(EmergencyNumbersPersonalModel.class);

	private String passengerGeoLocationId;
	private String passengerId;
	private String latitude;
	private String longitude;
	private String geoLocation;

	public String getPassengerGeoLocationId() {
		return passengerGeoLocationId;
	}

	public void setPassengerGeoLocationId(String passengerGeoLocationId) {
		this.passengerGeoLocationId = passengerGeoLocationId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public int insertPassengerGeoLocationDetails(String userId) {
		
		int status = 0;

		String passengerGpsId = UUIDGenerator.generateUUID();
		long currentTime = DateUtils.nowAsGmtMillisec();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerGeoLocationDao passengerGeoLocationDao = session.getMapper(PassengerGeoLocationDao.class);

		try {

			this.passengerGeoLocationId = passengerGpsId;
			this.passengerId = userId;
			this.geoLocation = "ST_GeomFromText('POINT(" + Double.parseDouble(this.longitude) + " " + Double.parseDouble(this.latitude) + ")',4326)";
			this.recordStatus = "A";
			this.createdAt = currentTime;
			this.createdBy = userId;
			this.updatedAt = currentTime;
			this.updatedBy = userId;

			status = passengerGeoLocationDao.insertPassengerGeoLocationDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertPassengerGeoLocationDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public int updatePassengerGeoLocationDetails(String userId) {
		
		int status = 0;
		long currentTime = DateUtils.nowAsGmtMillisec();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerGeoLocationDao passengerGeoLocationDao = session.getMapper(PassengerGeoLocationDao.class);

		try {

			this.passengerId = userId;
			this.geoLocation = "ST_GeomFromText('POINT(" + Double.parseDouble(this.longitude) + " " + Double.parseDouble(this.latitude) + ")',4326)";
			this.updatedAt = currentTime;
			this.updatedBy = userId;

			status = passengerGeoLocationDao.updatePassengerGeoLocationDetails(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePassengerGeoLocationDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static PassengerGeoLocationModel getPassengerGeoLocationDetailsById(String userId) {

		PassengerGeoLocationModel passengerGeoLocationModel = new PassengerGeoLocationModel();
		passengerGeoLocationModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PassengerGeoLocationDao passengerGeoLocationDao = session.getMapper(PassengerGeoLocationDao.class);

		try {
			passengerGeoLocationModel = passengerGeoLocationDao.getPassengerGeoLocationDetailsById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePassengerGeoLocationDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return passengerGeoLocationModel;
	}

}
