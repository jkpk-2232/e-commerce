package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorFeedLikeModel;

public interface VendorFeedLikeDao {

	void insertVendorFeedLike(VendorFeedLikeModel vendorFeedLikeModel);

	void deleteVendorFeedLikesByVendorFeedId(VendorFeedLikeModel vendorFeedLikeModel);

	boolean isVendorFeedLikedByUserId(@Param("userId") String userId, @Param("vendorFeedId") String vendorFeedId);

	void deleteVendorFeedLikeByUserId(VendorFeedLikeModel vendorFeedLikeModel);

	int getVendorFeedLikesCount(@Param("vendorFeedId") String vendorFeedId);
}