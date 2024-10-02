package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.OrderModel;
import com.webapp.models.StatsModel;

public interface OrderDao {

	void insertOrder(OrderModel orderModel);

	void updateOrder(OrderModel orderModel);

	List<OrderModel> getOrdersByUserId(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("orderShortId") int orderShortId, @Param("roleId") String roleId, @Param("statusNotToBeConsidered") List<String> statusNotToBeConsidered);

	OrderModel getOrderDetailsByOrderId(@Param("orderId") String orderId);

	OrderModel getOrderDetailsByOrderIdWithOrderItems(@Param("orderId") String orderId);

	OrderModel getOrderLimitedDetailsByOrderId(@Param("orderId") String orderId);

	void updateOrderDeliveryStatus(OrderModel orderModel);

	int getOrdersCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId, @Param("orderStatus") List<String> orderStatus,
				@Param("orderShortIdSearch") int orderShortIdSearch, @Param("orderStatusFilter") String orderStatusFilter, @Param("vendorOrderManagement") String vendorOrderManagement, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<OrderModel> getOrdersSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("displayType") String displayType,
				@Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId, @Param("orderStatus") List<String> orderStatus, @Param("orderShortIdSearch") int orderShortIdSearch,
				@Param("orderStatusFilter") String orderStatusFilter, @Param("vendorOrderManagement") String vendorOrderManagement, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<OrderModel> getOrdersSearchAPI(@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId, @Param("orderStatus") List<String> orderStatus, @Param("orderShortIdSearch") int orderShortIdSearch,
				@Param("vendorOrderManagement") String vendorOrderManagement, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	int getOrdersSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId, @Param("serviceId") String serviceId, @Param("categoryId") String categoryId,
				@Param("orderStatus") List<String> orderStatus, @Param("orderShortIdSearch") int orderShortIdSearch, @Param("orderStatusFilter") String orderStatusFilter, @Param("vendorOrderManagement") String vendorOrderManagement,
				@Param("vendorStoreIdList") List<String> vendorStoreIdList);

	void updateDelieveryManagedByVendorDriver(OrderModel orderModel);

	List<OrderModel> getOrdersForProcessingCronJob(@Param("orderStatus") List<String> orderStatus, @Param("start") int start, @Param("length") int length);

	void updateDriverIdAgainstOrder(OrderModel orderModel);

	void updateCarTypeIdAgainstOrder(OrderModel orderModel);

	void updatePaymentStatus(OrderModel orderModel);

	StatsModel getOrderStatsByTimeForVendor(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime);

	List<StatsModel> getOrderListForHighestSpendingCustomers(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList, @Param("startTime") long startTime, @Param("endTime") long endTime, @Param("start") int start,
				@Param("length") int length);

	OrderModel getCurrentOrderByDriverId(@Param("driverId") String driverId, @Param("orderStatus") List<String> orderStatus);

	List<OrderModel> getAllOrdersDataForMigration(@Param("start") int start, @Param("length") int length);
	
	List<OrderModel> getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt(@Param("vendorId") String vendorId, @Param("orderDeliveryStatusList") List<String> orderDeliveryStatusList,  @Param("startDateLong") long startDateLong, @Param("endDateLong") long endDateLong);
	
	List<OrderModel> getOrdersListByUserId(@Param("userId") String userId, @Param("orderShortId") int orderShortId, @Param("roleId") String roleId, @Param("statusNotToBeConsidered") List<String> statusNotToBeConsidered);
	
	int getOrdersCountByUserId(@Param("userId") String userId, @Param("roleId") String roleId, @Param("statusNotToBeConsidered") List<String> statusNotToBeConsidered );
	
	List<OrderModel> getOrderListBasedOnStoreAndVendorAndSearchAPI(@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId,
				@Param("orderStatus") List<String> orderStatus, @Param("orderShortIdSearch") int orderShortIdSearch, @Param("vendorOrderManagement") String vendorOrderManagement);

	void updatePaymentAndOrderStatusForCCavenuePayment(OrderModel orderModel);

	void updatePaymentAndOrderStatusForKPMart(OrderModel orderModel);

	OrderModel getOrderDetailsByPaymentToken(@Param("paymentToken") String paymentToken);

	List<OrderModel> getOnlineAndOfflineSalesDashboardByBrand(@Param("brandId") String brandId);
}