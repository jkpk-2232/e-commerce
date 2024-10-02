package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.FavouriteLocationsDao;

public class FavouriteLocationsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(FavouriteLocationsModel.class);

	private String favouriteLocationsId;
	private String userId;
	private String favouriteNickname;
	private String favouriteLatitude;
	private String favouriteLongitude;
	private String favouriteAddress;
	private String favouritePlaceId;
	private String favouriteGeolocation;

	private String tourId;
	private boolean isSource;

	public String addFavouriteLocations(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteLocationsDao favouriteLocationsDao = session.getMapper(FavouriteLocationsDao.class);

		this.favouriteLocationsId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.createdBy = userId;
		this.updatedBy = userId;

		this.setFavouriteGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getFavouriteLongitude() + "  " + this.getFavouriteLatitude() + ")')");

		try {
			favouriteLocationsDao.addFavouriteLocations(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addFavouriteLocations : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.favouriteLocationsId;
	}

	public static boolean isFavouriteNicknameExists(String favouriteNickname, String favouriteLocationsId, String userId) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteLocationsDao favouriteLocationsDao = session.getMapper(FavouriteLocationsDao.class);

		try {
			status = favouriteLocationsDao.isFavouriteNicknameExists(favouriteNickname, favouriteLocationsId, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isFavouriteNicknameExists : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public int deletefavouriteLocation(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteLocationsDao favouriteLocationsDao = session.getMapper(FavouriteLocationsDao.class);

		try {
			status = favouriteLocationsDao.deletefavouriteLocation(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deletefavouriteLocation : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static List<FavouriteLocationsModel> getFavouriteLocationsList(String userId, int start, int length, String searchText, boolean isSource) {

		List<FavouriteLocationsModel> favouriteLocationsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteLocationsDao favouriteLocationsDao = session.getMapper(FavouriteLocationsDao.class);

		try {
			favouriteLocationsModel = favouriteLocationsDao.getFavouriteLocationsList(userId, start, length, searchText, isSource);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFavouriteLocationsList : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return favouriteLocationsModel;
	}

	public int updateFavouriteLocations(String loggedInuserId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteLocationsDao favouriteLocationsDao = session.getMapper(FavouriteLocationsDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		this.setFavouriteGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getFavouriteLongitude() + "  " + this.getFavouriteLatitude() + ")')");

		try {
			status = favouriteLocationsDao.updateFavouriteLocations(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateFavouriteLocations : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static FavouriteLocationsModel getFavouriteLocationsDetailsById(String favouriteLocationsId) {

		FavouriteLocationsModel favouriteLocationsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteLocationsDao favouriteLocationsDao = session.getMapper(FavouriteLocationsDao.class);

		try {
			favouriteLocationsModel = favouriteLocationsDao.getFavouriteLocationsDetailsById(favouriteLocationsId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFavouriteLocationsDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return favouriteLocationsModel;
	}

	public String getFavouriteLocationsId() {
		return favouriteLocationsId;
	}

	public void setFavouriteLocationsId(String favouriteLocationsId) {
		this.favouriteLocationsId = favouriteLocationsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFavouriteNickname() {
		return favouriteNickname;
	}

	public void setFavouriteNickname(String favouriteNickname) {
		this.favouriteNickname = favouriteNickname;
	}

	public String getFavouriteLatitude() {
		return favouriteLatitude;
	}

	public void setFavouriteLatitude(String favouriteLatitude) {
		this.favouriteLatitude = favouriteLatitude;
	}

	public String getFavouriteLongitude() {
		return favouriteLongitude;
	}

	public void setFavouriteLongitude(String favouriteLongitude) {
		this.favouriteLongitude = favouriteLongitude;
	}

	public String getFavouriteAddress() {
		return favouriteAddress;
	}

	public void setFavouriteAddress(String favouriteAddress) {
		this.favouriteAddress = favouriteAddress;
	}

	public String getFavouritePlaceId() {
		return favouritePlaceId;
	}

	public void setFavouritePlaceId(String favouritePlaceId) {
		this.favouritePlaceId = favouritePlaceId;
	}

	public String getFavouriteGeolocation() {
		return favouriteGeolocation;
	}

	public void setFavouriteGeolocation(String favouriteGeolocation) {
		this.favouriteGeolocation = favouriteGeolocation;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public boolean isSource() {
		return isSource;
	}

	public void setSource(boolean isSource) {
		this.isSource = isSource;
	}
}