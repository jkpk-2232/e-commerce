package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorMonthlySubscriptionHistoryModel;

public interface VendorMonthlySubscriptionHistoryDao {

	void insertVendorMonthlySubscriptionHistory(VendorMonthlySubscriptionHistoryModel vendorMonthlySubscriptionHistoryModel);

	int getVendorMonthlySubscriptionHistoryCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId);

	List<VendorMonthlySubscriptionHistoryModel> getVendorMonthlySubscriptionHistorySearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length,
				@Param("vendorId") String vendorId);

	int getVendorMonthlySubscriptionHistorySearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId);

	VendorMonthlySubscriptionHistoryModel getLastVendorMonthlySubscriptionHistoryEntry(@Param("vendorId") String vendorId);

	void updateVendorSubscriptionCurrentActive(VendorMonthlySubscriptionHistoryModel vendorMonthlySubscriptionHistoryModel);

	void updateVendorSubscriptionAccountExpired(@Param("updateVendorMonthlySubscriptionToExpiredList") List<VendorMonthlySubscriptionHistoryModel> updateVendorMonthlySubscriptionToExpiredList);
}