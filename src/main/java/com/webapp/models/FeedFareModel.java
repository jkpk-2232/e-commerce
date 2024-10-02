package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.FeedFareDao;

public class FeedFareModel {

	private static Logger logger = Logger.getLogger(RentalPackageFareModel.class);

	private String feedFareId;
	private String feedSettingsId;
	private String serviceId;
	private double baseFare;
	private double perMinuteFare;
	private double GSTPercentage;
	private long createdAt;
	private String createdBy;
	private long updatedAt;
	private String updatedBy;
	

	public String getFeedFareId() {
		return feedFareId;
	}

	public void setFeedFareId(String feedFareId) {
		this.feedFareId = feedFareId;
	}

	public String getFeedSettingsId() {
		return feedSettingsId;
	}

	public void setFeedSettingsId(String feedSettingsId) {
		this.feedSettingsId = feedSettingsId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(double baseFare) {
		this.baseFare = baseFare;
	}

	public double getPerMinuteFare() {
		return perMinuteFare;
	}

	public void setPerMinuteFare(double perMinuteFare) {
		this.perMinuteFare = perMinuteFare;
	}

	public double getGSTPercentage() {
		return GSTPercentage;
	}

	public void setGSTPercentage(double gSTPercentage) {
		GSTPercentage = gSTPercentage;
	}
	
	public static int insertFeedFareBatch(List<FeedFareModel> feedFareModelList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedFareDao feedFareDao = session.getMapper(FeedFareDao.class);

		try {

			count = feedFareDao.insertFeedFareBatch(feedFareModelList);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insertRentalPackageFareBatch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public static List<FeedFareModel> getFeedFareListByFeedSettingsId(String feedSettingsId) {
		
		List<FeedFareModel> feedFareModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedFareDao feedFareDao = session.getMapper(FeedFareDao.class);

		try {

			feedFareModelList = feedFareDao.getFeedFareListByFeedSettingsId(feedSettingsId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getFeedFareListByFeedSettingsId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return feedFareModelList;
	}
	
	
	public int deleteFeedFareByFeedSettingsId() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedFareDao feedFareDao = session.getMapper(FeedFareDao.class);

		try {

			count = feedFareDao.deleteFeedFareByFeedSettingsId(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteFeedFareByFeedSettingsId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<FeedFareModel> getFeedFareListByFeedSettingsIdAndServiceId(String feedSettingsId, List<String> serviceIdList) {
		
		List<FeedFareModel> feedFareModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedFareDao feedFareDao = session.getMapper(FeedFareDao.class);

		try {

			feedFareModelList = feedFareDao.getFeedFareListByFeedSettingsIdAndServiceId(feedSettingsId,serviceIdList);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getFeedFareListByFeedSettingsIdAndServiceId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return feedFareModelList;
	}

	public static FeedFareModel getFeedFareDetailsByFeedSettingsIdAndServiceId(String feedSettingsId, String serviceId) {
		
		FeedFareModel feedFareMode = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		FeedFareDao feedFareDao = session.getMapper(FeedFareDao.class);

		try {

			feedFareMode = feedFareDao.getFeedFareDetailsByFeedSettingsIdAndServiceId(feedSettingsId,serviceId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getFeedFareDetailsByFeedSettingsIdAndServiceId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return feedFareMode;
	}

}
