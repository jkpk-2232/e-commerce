package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverTripRatingsModel;

public interface DriverTripRatingsDao {

	int ratingsByDriver(DriverTripRatingsModel driverTripRatingsModel) throws SQLException;

	String getPassengerIdFromTours(String tripId) throws SQLException;

	List<DriverTripRatingsModel> getDriversTripRatingsList(String driverId);

	List<DriverTripRatingsModel> getAllPassangerRatings(String passangerId);

	int checkRating(@Param("driverId") String driverId, @Param("passangerId") String passangerId);

	DriverTripRatingsModel getRatingDetailsByTourId(String tripId);
	
}

