package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.OfflineOrderModel;

public interface OfflineOrderDao {

	void insertOfflineOrder(OfflineOrderModel offlineOrderModel);

	OfflineOrderModel getOrderDetailsByOfflineStoreOrderId(@Param("offlineStoreOrderId") String offlineStoreOrderId);

}
