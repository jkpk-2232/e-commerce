package com.utils.myhub;

import com.webapp.models.DriverTourRequestModel;

public class DriverTourRequestUtils {

	public static void createDriverTourRequest(String tourId, String driverId, String status) {

		DriverTourRequestModel tourRequest = new DriverTourRequestModel();
		tourRequest.setDriverId(driverId);
		tourRequest.setTourId(tourId);
		tourRequest.setStatus(status);

		tourRequest.deleteExistingEntry();

		tourRequest.createDriverTourRequest();
	}

	public static void createDriverTourRequest(String tourId, String driverId) {

		DriverTourRequestModel tourRequest = new DriverTourRequestModel();
		tourRequest.setDriverId(driverId);
		tourRequest.setTourId(tourId);

		tourRequest.deleteExistingEntry();

		tourRequest.createDriverTourRequest();
	}

	public static void updateDriverTourRequest(String tourId, String driverId, String status) {

		DriverTourRequestModel driverTourRequestModel = new DriverTourRequestModel();
		driverTourRequestModel.setTourId(tourId);
		driverTourRequestModel.setDriverId(driverId);
		driverTourRequestModel.setStatus(status);
		driverTourRequestModel.updateDriverTourRequest();
	}
}
