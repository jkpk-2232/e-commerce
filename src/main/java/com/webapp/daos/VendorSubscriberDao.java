package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.StatsModel;
import com.webapp.models.VendorSubscriberModel;

public interface VendorSubscriberDao {

	void insertVendorSubscriber(VendorSubscriberModel vendorSubscriberModel);

	int getVendorSubscriberCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId);

	List<VendorSubscriberModel> getVendorSubscriberSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId,
				@Param("vendorStoreId") String vendorStoreId);

	int getVendorSubscriberSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId);

	boolean isUserSubscribedToVendorStore(@Param("vendorStoreId") String vendorStoreId, @Param("userId") String userId);

	void deleteVendorSubscriber(VendorSubscriberModel vendorSubscriberModel);

	List<VendorSubscriberModel> getVendorSubscribersByVendorId(@Param("vendorId") String vendorId, @Param("start") int start, @Param("length") int length);

	StatsModel getSubscriberStatsByTimeForVendor(@Param("vendorId") String vendorId, @Param("startTime") long startTime, @Param("endTime") long endTime);

	int getVendorSubscribersCountByVendorId(@Param("vendorId") String vendorId);
}