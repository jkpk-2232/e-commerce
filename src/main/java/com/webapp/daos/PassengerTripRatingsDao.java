package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.PassengerTripRatingsModel;

public interface PassengerTripRatingsDao {

	int ratingsByPassenger(PassengerTripRatingsModel passengerTripRatingsModel) throws SQLException;

	String getDriverIdFromTours(String tripId) throws SQLException;

	List<PassengerTripRatingsModel> getAlldriverRatings(String driverId);

	int checkRating(@Param("driverId") String driverId, @Param("passangerId") String passangerId);

	PassengerTripRatingsModel getPassengerRatingsForTour(@Param("tripId") String tripId);

	PassengerTripRatingsModel getPassenerRatingsByTripId(String tripId);
}

