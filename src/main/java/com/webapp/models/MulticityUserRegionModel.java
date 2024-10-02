package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.MulticityUserRegionDao;

public class MulticityUserRegionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(MulticityUserRegionModel.class);

	private String multicityUserRegionId;
	private String multicityCountryId;
	private String multicityCityRegionId;
	private String userId;
	private String roleId;
	private String regionName;

	public String getMulticityUserRegionId() {
		return multicityUserRegionId;
	}

	public void setMulticityUserRegionId(String multicityUserRegionId) {
		this.multicityUserRegionId = multicityUserRegionId;
	}

	public String getMulticityCountryId() {
		return multicityCountryId;
	}

	public void setMulticityCountryId(String multicityCountryId) {
		this.multicityCountryId = multicityCountryId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public static List<MulticityUserRegionModel> getMulticityUserRegionByUserId(String driverId) {

		List<MulticityUserRegionModel> multicityUserRegionModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityUserRegionDao multicityUserRegionDao = session.getMapper(MulticityUserRegionDao.class);

		try {

			multicityUserRegionModel = multicityUserRegionDao.getMulticityUserRegionByUserId(driverId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getMulticityUserRegionByUserId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return multicityUserRegionModel;
	}

	public static int addMulticityUserRegion(List<MulticityUserRegionModel> multicityUserRegionModel) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityUserRegionDao multicityUserRegionDao = session.getMapper(MulticityUserRegionDao.class);

		try {
			count = multicityUserRegionDao.addMulticityUserRegion(multicityUserRegionModel);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addMulticityUserRegion : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int deleteUserRegions() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityUserRegionDao multicityUserRegionDao = session.getMapper(MulticityUserRegionDao.class);

		try {
			count = multicityUserRegionDao.deleteUserRegions(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteUserRegions : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}
}