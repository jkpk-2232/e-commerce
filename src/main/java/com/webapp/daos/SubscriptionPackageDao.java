package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.SubscriptionPackageModel;

public interface SubscriptionPackageDao {

	List<SubscriptionPackageModel> getAllActiveSubscriptionPackagesList(@Param("start") int start, @Param("length") int length, @Param("carTypeId") String carTypeId);

	List<SubscriptionPackageModel> getAllActiveSubscriptionPackagesListDriverCarTypeWise(@Param("start") int start, @Param("length") int length, @Param("driverId") String driverId);

	int getSubscriptionPackageCount();

	List<SubscriptionPackageModel> getSubscriptionPackageListForSearch(Map<String, Object> inputMap);

	void activateDeactivateSubscriptionPackage(SubscriptionPackageModel subscriptionPackageModel);

	void deleteSubscriptionPackage(SubscriptionPackageModel subscriptionPackageModel);

	void addSubscriptionPackage(SubscriptionPackageModel subscriptionPackageModel);

	boolean isSubscriptionPackageExists(@Param("packageName") String packageName, @Param("subscriptionPackageId") String subscriptionPackageId);

	SubscriptionPackageModel getSubscriptionPackageDetailsById(@Param("subscriptionPackageId") String subscriptionPackageId);

	void updateSubscriptionPackage(SubscriptionPackageModel subscriptionPackageModel);
}