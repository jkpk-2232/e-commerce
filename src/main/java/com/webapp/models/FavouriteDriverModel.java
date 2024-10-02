package com.webapp.models;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.FavouriteDriverDao;

public class FavouriteDriverModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(FavouriteDriverModel.class);

	private String favouriteDriverId;
	private String passengerId;
	private String driverId;

	private double driverRatings;

	private DriverInfoModel driverInfoModel;

	public String addFavouriteDriver(String userId) throws IOException {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteDriverDao favouriteDriverDao = session.getMapper(FavouriteDriverDao.class);

		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;
		this.favouriteDriverId = UUIDGenerator.generateUUID();

		try {
			favouriteDriverDao.addFavouriteDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add user addFavouriteDriver : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.favouriteDriverId;
	}

	public static FavouriteDriverModel getFavouriteDriverDetails(String passengerId, String driverId) {

		FavouriteDriverModel favouriteDriverModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteDriverDao favouriteDriverDao = session.getMapper(FavouriteDriverDao.class);

		try {
			favouriteDriverModel = favouriteDriverDao.getFavouriteDriverDetails(passengerId, driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add user getFavouriteDriverDetails : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return favouriteDriverModel;
	}

	public int deleteFavouriteDriver(String userId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteDriverDao favouriteDriverDao = session.getMapper(FavouriteDriverDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			status = favouriteDriverDao.deleteFavouriteDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add user deleteFavouriteDriver : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return status;
	}

	public static List<FavouriteDriverModel> getFavouriteDriverList(String passengerId, int start, int length) {

		List<FavouriteDriverModel> favouriteDriverModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FavouriteDriverDao favouriteDriverDao = session.getMapper(FavouriteDriverDao.class);

		try {
			favouriteDriverModel = favouriteDriverDao.getFavouriteDriverList(passengerId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during add user getFavouriteDriverList : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return favouriteDriverModel;
	}

	public String getFavouriteDriverId() {
		return favouriteDriverId;
	}

	public void setFavouriteDriverId(String favouriteDriverId) {
		this.favouriteDriverId = favouriteDriverId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public DriverInfoModel getDriverInfoModel() {
		return driverInfoModel;
	}

	public void setDriverInfoModel(DriverInfoModel driverInfoModel) {
		this.driverInfoModel = driverInfoModel;
	}

	public double getDriverRatings() {
		return driverRatings;
	}

	public void setDriverRatings(double driverRatings) {
		this.driverRatings = driverRatings;
	}
}