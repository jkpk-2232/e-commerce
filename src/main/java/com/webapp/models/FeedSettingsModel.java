package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.FeedSettingsDao;

public class FeedSettingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(RentalPackageModel.class);

	private String feedSettingsId;
	private String multicityCityRegionId;
	private String regionName;
	

	public String getFeedSettingsId() {
		return feedSettingsId;
	}

	public void setFeedSettingsId(String feedSettingsId) {
		this.feedSettingsId = feedSettingsId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String addFeedSettings(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {
			this.feedSettingsId = UUIDGenerator.generateUUID();
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;
			status = feedSettingsDao.addFeedSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addFeedSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		if (status > 0) {

			return this.feedSettingsId;
		} else {

			return null;
		}
	}

	public static int getFeedSettingsCountByUser(String userId) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {
			count = feedSettingsDao.getFeedSettingsCountByUser(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFeedSettingsCountByUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<FeedSettingsModel> getFeedSettingsListForSearch(int start, int length, String globalSearchStringWithPercentage) {
		
		List<FeedSettingsModel> feedSettingsModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("globalSearchString", globalSearchStringWithPercentage);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {
			feedSettingsModelList = feedSettingsDao.getFeedSettingsListForSearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFeedSettingsListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return feedSettingsModelList;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public static FeedSettingsModel getFeedSettingsDetailsById(String feedSettingsId) {
		
		FeedSettingsModel feedSettingsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {

			feedSettingsModel = feedSettingsDao.getFeedSettingsDetailsById(feedSettingsId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getFeedSettingsDetailsById : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return feedSettingsModel;
	}

	public int updateFeedSettings(String userId) {
		
		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			status = feedSettingsDao.updateFeedSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateFeedSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
		
		
	}

	public static FeedSettingsModel getfeedSettingsByMultiCityRegionId(String regionList) {
		
		FeedSettingsModel feedSettingsModel = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {

			feedSettingsModel = feedSettingsDao.getfeedSettingsByMultiCityRegionId(regionList);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getfeedSettingsByMultiCityRegionId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return feedSettingsModel;
	}

	public static boolean isMultiCityRegionIdExists(String regionId) {
		
		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedSettingsDao feedSettingsDao = session.getMapper(FeedSettingsDao.class);

		try {
			status = feedSettingsDao.isMultiCityRegionIdExists(regionId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isMultiCityRegionIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	
}
