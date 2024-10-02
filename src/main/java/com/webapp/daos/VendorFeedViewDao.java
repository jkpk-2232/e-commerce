package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorFeedViewModel;

public interface VendorFeedViewDao {

	void insertVendorFeedView(VendorFeedViewModel vendorFeedViewModel);

	void deleteVendorFeedViewsByVendorFeedId(VendorFeedViewModel vendorFeedViewModel);

	boolean isVendorFeedViewedByUserId(@Param("userId") String userId, @Param("vendorFeedId") String vendorFeedId);

	int getVendorFeedViewsCount(@Param("vendorFeedId") String vendorFeedId);
}