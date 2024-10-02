package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverOrderRequestModel;

public interface DriverOrderRequestDao {

	void createDriverOrderRequest(DriverOrderRequestModel driverOrderRequestModel);

	List<DriverOrderRequestModel> getAllOrderRequestByDriverId(@Param("driverId") String driverId);

	DriverOrderRequestModel getOrderRequestByDriverIdAndOrderId(@Param("driverId") String driverId, @Param("orderId") String orderId);

	void updateDriverOrderRequest(DriverOrderRequestModel driverOrderRequestModel);
}