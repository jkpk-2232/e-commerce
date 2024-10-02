package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorFeedModel;

public interface VendorFeedDao {

	void insertVendorFeed(VendorFeedModel vendorFeedModel);

	int getVendorFeedCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId);

	List<VendorFeedModel> getVendorFeedSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId);

	int getVendorFeedSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId);

	void deleteVendorFeedByVendorFeedId(VendorFeedModel vendorFeedModel);

	VendorFeedModel getVendorFeedDetailsByFeedId(@Param("vendorFeedId") String vendorFeedId);

	List<VendorFeedModel> getVendorFeedsByVendorIdAndVendorStoreId(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("loggedInuserId") String loggedInuserId);
	
	List<VendorFeedModel> getVendorFeedsBySubscriberId(@Param("subscriberUserId") String subscriberUserId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("extraInfoType") String extraInfoType);

	void updateFeedLikesCount(VendorFeedModel vendorFeedModel);

	void updateFeedViewsCount(VendorFeedModel vendorFeedModel);

	void updateFeedNotificationStatus(VendorFeedModel vendorFeedModel);

	void updateVendorFeedStatus(VendorFeedModel vendorFeedModel);

	List<VendorFeedModel> getVendorFeedsByVendorIdAndVendorStoreIdAndRegion(@Param("regionId") String regionId,@Param("vendorId") String vendorId,@Param("vendorStoreId") String vendorStoreId,@Param("start") int start,@Param("length") int length,@Param("searchKey") String searchKey,@Param("loggedInuserId") String loggedInuserId);
	
	List<VendorFeedModel> getNewVendorFeedsBySubscriberId(@Param("regionId") String regionId, @Param("subscriberUserId") String subscriberUserId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("extraInfoType") String extraInfoType);
	
	void repostVendorFeed(VendorFeedModel vendorFeedModel);

	Map<String, Object> getVendorFeedViewsAndLikesCount(@Param("vendorId") String vendorId);

	int getVendorFeedCommentsCountByVendor(@Param("vendorId") String vendorId);
}