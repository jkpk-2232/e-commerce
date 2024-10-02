package com.webapp.daos;

import com.webapp.models.DriverTourStatusModel;

public interface DriverTourStatusDao {

	int createDriverTourStatus(DriverTourStatusModel driverTourStatusModel);

	DriverTourStatusModel getDriverTourStatus(String driverId);

	int updateDriverTourStatus(DriverTourStatusModel driverTourStatusModel);

}
