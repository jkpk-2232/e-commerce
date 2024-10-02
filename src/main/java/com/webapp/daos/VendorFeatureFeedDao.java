package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorFeatureFeedModel;
import com.webapp.models.VendorFeedModel;

public interface VendorFeatureFeedDao {
	
	void insertVendorFeed(VendorFeatureFeedModel vendorFeedModel);

	int getVendorFeedCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId);

	List<VendorFeatureFeedModel> getVendorFeedSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId);

	int getVendorFeedSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId);

	void deleteVendorFeedByVendorFeedId(VendorFeatureFeedModel vendorFeedModel);

	VendorFeatureFeedModel getVendorFeedDetailsByFeedId(@Param("vendorFeedId") String vendorFeedId);

	List<VendorFeatureFeedModel> getVendorFeedsByVendorId(@Param("vendorId") String vendorId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey);
	
	List<VendorFeatureFeedModel> getVendorFeedsBySubscriberId(@Param("subscriberUserId") String subscriberUserId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("extraInfoType") String extraInfoType);

	void updateFeedLikesCount(VendorFeatureFeedModel vendorFeedModel);

	void updateFeedViewsCount(VendorFeatureFeedModel vendorFeedModel);

	void updateFeedNotificationStatus(VendorFeatureFeedModel vendorFeedModel);

	List<VendorFeedModel> getFeedDetailsByVendorStoreId(@Param("vendorStoreId") String vendorStoreId);

	void insertFeeds(@Param("feedModels") List<VendorFeatureFeedModel> feedModels);

}
