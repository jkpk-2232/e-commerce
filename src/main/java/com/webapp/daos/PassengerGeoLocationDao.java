package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.PassengerGeoLocationModel;

public interface PassengerGeoLocationDao {
	
	int insertPassengerGeoLocationDetails(PassengerGeoLocationModel passengerGeoLocationModel);
	
	int updatePassengerGeoLocationDetails(PassengerGeoLocationModel passengerGeoLocationModel);
	
	PassengerGeoLocationModel getPassengerGeoLocationDetailsById(@Param("passengerId") String passengerId);

}
