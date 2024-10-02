package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.OrderItemModel;
import com.webapp.models.StatsModel;

public interface OrderItemDao {

	void insertOrderItem(@Param("orderItemList") List<OrderItemModel> orderItemList);

	void deleteOrderItem(@Param("orderId") String orderId);

	int getOrderItemCount(@Param("orderId") String orderId);

	List<OrderItemModel> getOrderItemSearch(@Param("searchKey") String searchKey, @Param("orderId") String orderId, @Param("start") int start, @Param("length") int length, @Param("numberSearch") double numberSearch);

	int getOrderItemSearchCount(@Param("searchKey") String searchKey, @Param("orderId") String orderId, @Param("numberSearch") double numberSearch);

	List<StatsModel> getOrderItemListForTrendingProductSkuForOrders(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime, @Param("start") int start,
				@Param("length") int length);

	StatsModel getOrderItemStatsByTimeForVendor(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("startTime") long startTime, @Param("endTime") long endTime);

	List<StatsModel> getOrderItemListForKPMartTrendingProductSku(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("startTime") long currentDate, @Param("endTime") long previousMonth);

	List<StatsModel> getOrderItemListForKPMartPopularProductSku(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("startTime") long currentDate, @Param("previousThreeMonth") long previousThreeMonth);

	List<StatsModel> getPreviousOrderItemListByUser(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("loggedInUserId") String loggedInUserId);

	List<StatsModel> getOrderItemListForKPMartTrendingProducts(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("startTime") long currentDate, @Param("endTime") long previousMonth);

	List<StatsModel> getOrderItemListForKPMartPopularProducts(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("startTime") long currentDate, @Param("previousThreeMonth") long previousThreeMonth);

	List<StatsModel> getPreviousOrderItemProductIdsListByUser(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("loggedInUserId") String loggedInUserId);

	StatsModel getOrderItemStatsByTimeForVendor(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime);

	List<StatsModel> getOrderItemListForTrendingProductSkuForAppointments(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime, @Param("start") int start,
				@Param("length") int length);

	StatsModel getOrderItemStatsByTimeForVendorForOrders(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime);

	StatsModel getOrderItemStatsByTimeForVendorForAppointments(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime);
}