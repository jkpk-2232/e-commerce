package com.utils.myhub;

import com.webapp.models.DriverOrderRequestModel;

public class DriverOrderRequestUtils {

	public static void createDriverOrderRequest(String driverId, String orderId, String status) {
		DriverOrderRequestModel driverOrderRequestModel = new DriverOrderRequestModel();
		driverOrderRequestModel.setDriverId(driverId);
		driverOrderRequestModel.setOrderId(orderId);
		driverOrderRequestModel.setStatus(status);
		driverOrderRequestModel.createDriverOrderRequest(driverId);
	}

	public static void updateDriverOrderRequest(String orderId, String driverId, String status) {
		DriverOrderRequestModel driverOrderRequestModel = new DriverOrderRequestModel();
		driverOrderRequestModel.setDriverId(driverId);
		driverOrderRequestModel.setOrderId(orderId);
		driverOrderRequestModel.setStatus(status);
		driverOrderRequestModel.updateDriverOrderRequest();
	}
}
