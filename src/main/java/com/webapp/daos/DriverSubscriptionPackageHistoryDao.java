package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverSubscriptionPackageHistoryModel;

public interface DriverSubscriptionPackageHistoryDao {

	void addDriverSubscriptionPackageHistory(DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel);

	List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionPackageHistoryList(@Param("driverId") String driverId, @Param("start") int start, @Param("length") int length);

	List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionPackageHistoryForStatus(@Param("driverId") String driverId, @Param("vendorId") String vendorId);

	List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionHistoryByPackageIdDriverId(@Param("subscriptionPackageId") String subscriptionPackageId, @Param("driverId") String driverId, @Param("vendorId") String vendorId);

	int getNumberOfPackages(@Param("driverId") String driverId, @Param("vendorId") String vendorId, @Param("temp") long temp);

	void updateExistingPackagesAsNotCurrent(DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel);

	List<DriverSubscriptionPackageHistoryModel> getDriverSubscriptionPackageHistoryForSearch(Map<String, Object> inputMap);

	int getDriverSubscriptionPackageHistoryCount(Map<String, Object> inputMap);

	DriverSubscriptionPackageHistoryModel getLatestDriverSubscriptionPackageHistory(@Param("driverId") String driverId);

	DriverSubscriptionPackageHistoryModel getCurrentPackageByDriverId(@Param("driverId") String driverId, @Param("currentTime") long currentTime);
}