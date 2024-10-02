package com.utils.myhub;

import com.webapp.models.DriverTourStatusModel;

public class DriverTourStatusUtils {

	public static void updateDriverTourStatus(String driverId, String status) {
		DriverTourStatusModel tourStatus = new DriverTourStatusModel();
		tourStatus.setDriverId(driverId);
		tourStatus.setStatus(status);
		tourStatus.updateDriverTourStatus();
	}
}
