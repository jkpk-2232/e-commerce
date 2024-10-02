package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverSubscriberModel;

public interface DriverSubscriberDao {

	void insertDriverSubscriber(DriverSubscriberModel driverSubscriberModel);

	int getDriverSubscribedBySubsciberIdCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("subscriberUserId") String subscriberUserId);

	List<DriverSubscriberModel> getDriverSubscribedBySubsciberIdSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length,
				@Param("subscriberUserId") String subscriberUserId);

	int getDriverSubscribedBySubsciberIdSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("subscriberUserId") String subscriberUserId);

	boolean isUserSubscribedToDriver(@Param("driverId") String driverId, @Param("userId") String userId);

	void deleteDriverSubscriber(DriverSubscriberModel driverSubscriberModel);

	int getDriverSubscriptionCount(@Param("driverId") String driverId, @Param("startTime") long startTime, @Param("endTime") long endTime);

	int getUserSubscribedCountByDriverId(@Param("driverId") String driverId);
}