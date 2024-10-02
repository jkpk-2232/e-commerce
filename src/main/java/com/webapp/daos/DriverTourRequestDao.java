package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverTourRequestModel;

public interface DriverTourRequestDao {
	
	int createDriverTourRequest(DriverTourRequestModel driverTourRequestModel);
	
	List<DriverTourRequestModel> getAllTourRequestByDriverId(String driverId);
	
	int isTourRequestSent(DriverTourRequestModel driverTourRequestModel);
	
	DriverTourRequestModel getTourRequestByDriverIdAndTourId(@Param("driverId") String driverId, @Param("tourId") String tourId);
	
	int updateDriverTourRequest(DriverTourRequestModel driverTourRequestModel);

	void deleteExistingEntry(DriverTourRequestModel driverTourRequestModel);
	
}