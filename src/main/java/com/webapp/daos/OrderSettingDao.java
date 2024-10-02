package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.OrderSettingModel;

public interface OrderSettingDao {

	void insertOrderSettings(OrderSettingModel orderSettingModel);

	void updateOrderSettings(OrderSettingModel orderSettingModel);

	int getOrderSettingCount(@Param("serviceId") String serviceId);

	List<OrderSettingModel> getOrderSettingSearch(@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("orderColumn") String orderColumn, @Param("serviceId") String serviceId);

	int getOrderSettingSearchCount(@Param("searchKey") String searchKey, @Param("serviceId") String serviceId);

	OrderSettingModel getOrderSettingDetailsByServiceId(@Param("serviceId") String serviceId);
}