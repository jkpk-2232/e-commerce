package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TrackMyLocationModel;



public interface TrackMyLocationDao {
	
	TrackMyLocationModel getPassengerLastUpdatedLocationByUserId(@Param("userId") String userId);
	
	TrackMyLocationModel getPassengerTourDetailsByTourId(@Param("tourId") String tourId);
	
	TrackMyLocationModel getDriverUpdatedLocationWithDetailsById(@Param("userId") String userId);

}
