package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.VendorFeatureFeedDao;
import com.webapp.daos.VendorFeedDao;
import com.webapp.daos.VendorFeedLikeDao;
import com.webapp.daos.VendorFeedViewDao;

public class VendorFeatureFeedModel extends AbstractModel {
	
	
	private static Logger logger = Logger.getLogger(VendorFeedModel.class);

	private String vendorFeedId;
	private String vendorId;
	private String feedName;
	private String feedMessage;
	private String feedBaner;
	private long feedViewsCount;
	private long feedLikesCount;
	private String feedNotificationStatus;
	private double estimatedCost;
	private long startDate;
	private long endDate;
	private String serviceId;
	private String vendorStoreId;
	private String region;
	

	private String vendorName;
	private boolean isFeedViewed;
	private boolean isFeedLiked;

	public String insertVendorFeed(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);

		this.vendorFeedId = UUIDGenerator.generateUUID();
		this.feedNotificationStatus = ProjectConstants.VENDOR_FEED_STATUS.NEW;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorFeedDao.insertVendorFeed(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorFeed : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorFeedId;
	}

	public static int getVendorFeedCount(long startDatelong, long endDatelong, String vendorId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			count = vendorFeedDao.getVendorFeedCount(startDatelong, endDatelong, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<VendorFeedModel> getVendorFeedSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String vendorId) {

		List<VendorFeedModel> vendorSubscriberList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorSubscriberList = vendorFeedDao.getVendorFeedSearch(startDatelong, endDatelong, searchKey, start, length, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorSubscriberSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorSubscriberList;
	}

	public static int getVendorFeedSearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			count = vendorFeedDao.getVendorFeedSearchCount(startDatelong, endDatelong, searchKey, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void deleteVendorFeedByVendorFeedId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);
		VendorFeedViewDao vendorFeedViewDao = session.getMapper(VendorFeedViewDao.class);
		VendorFeedLikeDao vendorFeedLikeDao = session.getMapper(VendorFeedLikeDao.class);

		try {

			vendorFeedDao.deleteVendorFeedByVendorFeedId(this);

			VendorFeedLikeModel vflm = new VendorFeedLikeModel();
			vflm.setVendorFeedId(this.getVendorFeedId());
			vendorFeedLikeDao.deleteVendorFeedLikesByVendorFeedId(vflm);

			VendorFeedViewModel vfvm = new VendorFeedViewModel();
			vfvm.setVendorFeedId(this.getVendorFeedId());
			vendorFeedViewDao.deleteVendorFeedViewsByVendorFeedId(vfvm);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static VendorFeedModel getVendorFeedDetailsByFeedId(String vendorFeedId) {

		VendorFeedModel vendorFeedModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorFeedModel = vendorFeedDao.getVendorFeedDetailsByFeedId(vendorFeedId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedDetailsByFeedId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedModel;
	}

	public static List<VendorFeedModel> getVendorFeedsByVendorId(String vendorId, int start, int length, String searchKey) {

		List<VendorFeedModel> vendorFeedList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			//vendorFeedList = vendorFeedDao.getVendorFeedsByVendorId(vendorId, start, length, searchKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedsByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedList;
	}
	
	public static List<VendorFeedModel> getVendorFeedsBySubscriberId(String subscriberUserId, int start, int length, String searchKey, String extraInfoType) {

		List<VendorFeedModel> vendorFeedList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorFeedList = vendorFeedDao.getVendorFeedsBySubscriberId(subscriberUserId, start, length, searchKey, extraInfoType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedsBySubscriberId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedList;
	}

	public void updateFeedLikesCount(String loggedInuserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);

		try {
			this.updatedBy = loggedInuserId;
			this.updatedAt = DateUtils.nowAsGmtMillisec();

			vendorFeedDao.updateFeedLikesCount(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateFeedLikesCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateFeedViewsCount(String loggedInuserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);

		try {
			this.updatedBy = loggedInuserId;
			this.updatedAt = DateUtils.nowAsGmtMillisec();

			vendorFeedDao.updateFeedViewsCount(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateFeedViewsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateFeedNotificationStatus() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			vendorFeedDao.updateFeedNotificationStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateFeedNotificationStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorFeedId() {
		return vendorFeedId;
	}

	public void setVendorFeedId(String vendorFeedId) {
		this.vendorFeedId = vendorFeedId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getFeedName() {
		return feedName;
	}

	public void setFeedName(String feedName) {
		this.feedName = feedName;
	}

	public String getFeedMessage() {
		return feedMessage;
	}

	public void setFeedMessage(String feedMessage) {
		this.feedMessage = feedMessage;
	}

	public String getFeedBaner() {
		return feedBaner;
	}

	public void setFeedBaner(String feedBaner) {
		this.feedBaner = feedBaner;
	}

	public long getFeedViewsCount() {
		return feedViewsCount;
	}

	public void setFeedViewsCount(long feedViewsCount) {
		this.feedViewsCount = feedViewsCount;
	}

	public long getFeedLikesCount() {
		return feedLikesCount;
	}

	public void setFeedLikesCount(long feedLikesCount) {
		this.feedLikesCount = feedLikesCount;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public boolean isFeedViewed() {
		return isFeedViewed;
	}

	public void setFeedViewed(boolean isFeedViewed) {
		this.isFeedViewed = isFeedViewed;
	}

	public boolean isFeedLiked() {
		return isFeedLiked;
	}

	public void setFeedLiked(boolean isFeedLiked) {
		this.isFeedLiked = isFeedLiked;
	}

	public String getFeedNotificationStatus() {
		return feedNotificationStatus;
	}

	public void setFeedNotificationStatus(String feedNotificationStatus) {
		this.feedNotificationStatus = feedNotificationStatus;
	}

	public double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public static List<VendorFeedModel> getFeedDetailsByVendorStoreId(String vendorStoreId) {
		
		List<VendorFeedModel> vendorFeedList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);

		try {
			vendorFeedList = vendorFeedDao.getFeedDetailsByVendorStoreId(vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFeedDetailsByVendorStoreId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedList;
	}

	public static void insertFeeds(List<VendorFeatureFeedModel> feedModels) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeatureFeedDao vendorFeedDao = session.getMapper(VendorFeatureFeedDao.class);

		try {
			vendorFeedDao.insertFeeds(feedModels);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getFeedDetailsByVendorStoreId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	@Override
	public String toString() {
		return "VendorFeedModel [vendorFeedId=" + vendorFeedId + ", vendorId=" + vendorId + ", feedName=" + feedName + ", feedMessage=" + feedMessage + ", feedBaner=" + feedBaner + ", feedViewsCount=" + feedViewsCount + ", feedLikesCount=" + feedLikesCount
					+ ", feedNotificationStatus=" + feedNotificationStatus + ", estimatedCost=" + estimatedCost + ", startDate=" + startDate + ", endDate=" + endDate + ", serviceId=" + serviceId + ", vendorStoreId=" + vendorStoreId + ", region=" + region
					+ ", vendorName=" + vendorName + ", isFeedViewed=" + isFeedViewed + ", isFeedLiked=" + isFeedLiked + "]";
	}
	

}
