package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.CarIconDao;

public class CarIconModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CarIconModel.class);

	private String carIconId;
	private boolean isActive;

	public static List<CarIconModel> getAllCarIcons() {

		List<CarIconModel> carIconList = new ArrayList<CarIconModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarIconDao carIconDao = session.getMapper(CarIconDao.class);

		try {
			carIconList = carIconDao.getAllCarIcons();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllCarIcons : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return carIconList;
	}

	public String getCarIconId() {
		return carIconId;
	}

	public void setCarIconId(String carIconId) {
		this.carIconId = carIconId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}