package com.webapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.VendorFeedDao;
import com.webapp.daos.VendorFeedLikeDao;
import com.webapp.daos.VendorFeedViewDao;

public class VendorFeedModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorFeedModel.class);

	private String vendorFeedId;
	private String vendorId;
	private String feedName;
	private String feedMessage;
	private String feedBaner;
	private long feedViewsCount;
	private long feedLikesCount;
	private String feedNotificationStatus;

	private String vendorName;
	private boolean isFeedViewed;
	private boolean isFeedLiked;
	
	private String mediaType;
	private boolean isSponsored;
	private boolean isDeleted;
	private String vendorBrandImage;
	
	private String vendorStoreId;
	
	private String brandName;
	private String storeAddressLat;
	private String storeAddressLng;
	private String storeName;
	private String storeAddress;
	private String storePlaceId;
	private boolean isFeedLike;
	private boolean isVendorStoreSubscribe;
	private String serviceId;
	
	private String vendorProductId;
	private List<ProductImageModel> productImageList;
	
	private String serviceName;
	private String serviceImage;
	
	public String insertVendorFeed(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

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
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);
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

	public static List<VendorFeedModel> getVendorFeedsByVendorIdAndVendorStoreId(String vendorId, String vendorStoreId, int start, int length, String searchKey, String loggedInuserId) {

		List<VendorFeedModel> vendorFeedList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorFeedList = vendorFeedDao.getVendorFeedsByVendorIdAndVendorStoreId(vendorId, vendorStoreId, start, length, searchKey, loggedInuserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedsByVendorIdAndVendorStoreId : ", t);
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
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

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
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

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
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

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

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public boolean getIsSponsored() {
		return isSponsored;
	}

	public void setIsSponsored(boolean isSponsored) {
		this.isSponsored = isSponsored;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void updateVendorFeedStatus(String string) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {

			vendorFeedDao.updateVendorFeedStatus(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorFeedStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public String getVendorBrandImage() {
		return vendorBrandImage;
	}

	public void setVendorBrandImage(String vendorBrandImage) {
		this.vendorBrandImage = vendorBrandImage;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		VendorFeedModel.logger = logger;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getStoreAddressLat() {
		return storeAddressLat;
	}

	public void setStoreAddressLat(String storeAddressLat) {
		this.storeAddressLat = storeAddressLat;
	}

	public String getStoreAddressLng() {
		return storeAddressLng;
	}

	public void setStoreAddressLng(String storeAddressLng) {
		this.storeAddressLng = storeAddressLng;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStorePlaceId() {
		return storePlaceId;
	}

	public void setStorePlaceId(String storePlaceId) {
		this.storePlaceId = storePlaceId;
	}

	public boolean isFeedLike() {
		return isFeedLike;
	}

	public void setFeedLike(boolean isFeedLike) {
		this.isFeedLike = isFeedLike;
	}

	public boolean isVendorStoreSubscribe() {
		return isVendorStoreSubscribe;
	}

	public void setVendorStoreSubscribe(boolean isVendorStoreSubscribe) {
		this.isVendorStoreSubscribe = isVendorStoreSubscribe;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getVendorProductId() {
		return vendorProductId;
	}

	public void setVendorProductId(String vendorProductId) {
		this.vendorProductId = vendorProductId;
	}

	public List<ProductImageModel> getProductImageList() {
		return productImageList;
	}

	public void setProductImageList(List<ProductImageModel> productImageList) {
		this.productImageList = productImageList;
	}

	public static List<VendorFeedModel> getVendorFeedsByVendorIdAndVendorStoreIdAndRegion(String regionId, String vendorId, String vendorStoreId, int start, int length, String searchKey, String loggedInuserId) {
		
		List<VendorFeedModel> vendorFeedList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorFeedList = vendorFeedDao.getVendorFeedsByVendorIdAndVendorStoreIdAndRegion(regionId, vendorId, vendorStoreId, start, length, searchKey, loggedInuserId);
			for (VendorFeedModel vendorFeedModel : vendorFeedList) {
				
				if (vendorFeedModel.getVendorProductId() != null) {
					VendorProductModel vendorProductModel =  VendorProductModel.getProductsDetailsByProductId(vendorFeedModel.getVendorProductId());
					if (vendorProductModel.getProductVariantId() != null) {
						List<ProductImageModel> productImageList =  ProductImageModel.getProductImageListByProductVariant(vendorProductModel.getProductVariantId());
						vendorFeedModel.setProductImageList(productImageList);
					} else 
						vendorFeedModel.setProductImageList(new ArrayList<>());
					
				} else 
					vendorFeedModel.setProductImageList(new ArrayList<>());
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedsByVendorIdAndVendorStoreIdAndRegion : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedList;
	}
	
	public static List<VendorFeedModel> getNewVendorFeedsBySubscriberId(String regionId, String subscriberUserId, int start, int length, String searchKey, String extraInfoType) {

		List<VendorFeedModel> vendorFeedList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorFeedList = vendorFeedDao.getNewVendorFeedsBySubscriberId(regionId, subscriberUserId, start, length, searchKey, extraInfoType);
			for (VendorFeedModel vendorFeedModel : vendorFeedList) {
				
				if (vendorFeedModel.getVendorProductId() != null) {
					VendorProductModel vendorProductModel =  VendorProductModel.getProductsDetailsByProductId(vendorFeedModel.getVendorProductId());
					if (vendorProductModel.getProductVariantId() != null) {
						List<ProductImageModel> productImageList =  ProductImageModel.getProductImageListByProductVariant(vendorProductModel.getProductVariantId());
						vendorFeedModel.setProductImageList(productImageList);
					} else 
						vendorFeedModel.setProductImageList(new ArrayList<>());
					
				} else 
					vendorFeedModel.setProductImageList(new ArrayList<>());
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNewVendorFeedsBySubscriberId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedList;
	}
	
	public void repostVendorFeed(String userLogin) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			this.setUpdatedBy(userLogin);
			vendorFeedDao.repostVendorFeed(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during repostVendorFeed : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}
	
	public static VendorFeedModel getNewVendorFeedDetailsByFeedId(String vendorFeedId) {

		VendorFeedModel vendorFeedModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			vendorFeedModel = vendorFeedDao.getVendorFeedDetailsByFeedId(vendorFeedId);
			
			if (vendorFeedModel.getVendorProductId() != null) {
				VendorProductModel vendorProductModel =  VendorProductModel.getProductsDetailsByProductId(vendorFeedModel.getVendorProductId());
				if (vendorProductModel.getProductVariantId() != null) {
					List<ProductImageModel> productImageList =  ProductImageModel.getProductImageListByProductVariant(vendorProductModel.getProductVariantId());
					vendorFeedModel.setProductImageList(productImageList);
				} else 
					vendorFeedModel.setProductImageList(new ArrayList<>());
				
			} else 
				vendorFeedModel.setProductImageList(new ArrayList<>());
			
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceImage() {
		return serviceImage;
	}

	public void setServiceImage(String serviceImage) {
		this.serviceImage = serviceImage;
	}

	public static Map<String, Object> getVendorFeedViewsAndLikesCount(String vendorId) {
		
		Map<String, Object> vendorFeedModel = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);
		
		try {
			vendorFeedModel = vendorFeedDao.getVendorFeedViewsAndLikesCount(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedViewsAndLikesCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorFeedModel;
	}

	public static int getVendorFeedCommentsCountByVendor(String vendorId) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorFeedDao vendorFeedDao = session.getMapper(VendorFeedDao.class);

		try {
			count = vendorFeedDao.getVendorFeedCommentsCountByVendor(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorFeedCommentsCountByVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}
}