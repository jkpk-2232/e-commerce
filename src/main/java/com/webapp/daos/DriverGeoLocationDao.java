package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverGeoLocationModel;

public interface DriverGeoLocationDao {

	List<DriverGeoLocationModel> getNearByAvailableCarListForWeb(Map<String, Object> inputMap);

	List<DriverGeoLocationModel> getNearestDriver(Map<String, Object> inputMap);

	DriverGeoLocationModel getNearestSingledriver(Map<String, Object> inputMap);

	DriverGeoLocationModel getFavouriteNearestSingledriver(Map<String, Object> inputMap);

	DriverGeoLocationModel getCurrentDriverPosition(String driverId);

	String isSessionExists(String sessionKey);

	int getTotalAvailableDriver(Map<String, Object> inputMap);

	List<DriverGeoLocationModel> getNearByCarList(Map<String, Object> inputMap);

	void updateDriverGeoLocation(DriverGeoLocationModel driverGeoLocationModel);

	void saveCarGeoLocation(DriverGeoLocationModel driverGeoLocationModel);

	List<DriverGeoLocationModel> getIdealDriverListForCronJob(Map<String, Object> inputMap);

	boolean checkDriverHasMultipleEntries(@Param("driverId") String driverId);

	void deleteDriverGeoLocations(@Param("driverId")  String driverId);
	
	List<DriverGeoLocationModel> getNearByCarDriverList(Map<String, Object> inputMap);

	int getVendorsTotalAvailableDriver(Map<String, Object> inputMap);
	
	List<DriverGeoLocationModel> getVendorsNearByCarList(Map<String, Object> inputMap);

	DriverGeoLocationModel getNearestSingleDriverForOrders(Map<String, Object> inputMap);
}