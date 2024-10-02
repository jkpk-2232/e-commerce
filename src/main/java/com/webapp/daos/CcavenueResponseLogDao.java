package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CcavenueResponseLogModel;

public interface CcavenueResponseLogDao {

	int insertCcavenueResponseLog(CcavenueResponseLogModel ccavenueResponseLogModel);

	CcavenueResponseLogModel getCcavenueResponseLogDetailsByTripId(@Param("tripId") String tripId);

	CcavenueResponseLogModel getCcavenueResponseLogDetailsBySubscriptionId(@Param("subscriptionOrderId") String subscriptionOrderId, @Param("userId") String userId);

	CcavenueResponseLogModel getCcavenueResponseLogDetailsByDeliveryOrderId(@Param("deliveryOrderId") String deliveryOrderId, @Param("userId") String userId);

	int getCcavenueResponseLogListCount(Map<String, Object> inputMap);

	List<CcavenueResponseLogModel> getCcavenueResponseLogListBySearch(Map<String, Object> inputMap);

	List<CcavenueResponseLogModel> getCcavenueLogsReport(Map<String, Object> inputMap);
}