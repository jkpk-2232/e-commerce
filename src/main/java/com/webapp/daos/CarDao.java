package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CarModel;

public interface CarDao {

	int insertCar(CarModel carModel);

	int updateCarDetails(CarModel carModel);

	int updateCarImages(CarModel carModel);

	int approvedCarByAdmin(CarModel carModel);

	boolean isCarPlateExists(@Param("carPlateNo") String carPlateNo, @Param("carId") String carId);

	CarModel getCarDetailsByCarId(String carId);

	CarModel getCarActiveDeativeDetailsById(String carId);

	CarModel getViewModeVendorsCarListByCarTypeIds(String driverId);

	List<CarModel> getCarList();

	List<CarModel> getCarListByCarTypeIds(Map<String, Object> inputMap);

	List<CarModel> getVendorsCarListByCarTypeIds(Map<String, Object> inputMap);

	int getTotalCarCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	List<CarModel> getCarListForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("roleId") String roleId, @Param("globalSearchString") String globalSearchString, @Param("startDatelong") long startDatelong,
				@Param("endDatelong") long endDatelong, @Param("approvelCheck") String approvelCheck, @Param("approvelStatus") boolean approvelStatus);

	int getVendorTotalCarCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("approvelCheck") String approvelCheck, @Param("approvelStatus") boolean approvelStatus, @Param("loggedInUserId") String loggedInUserId);

	List<CarModel> getVendorCarListForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("roleId") String roleId, @Param("globalSearchString") String globalSearchString, @Param("startDatelong") long startDatelong,
				@Param("endDatelong") long endDatelong, @Param("approvelCheck") String approvelCheck, @Param("approvelStatus") boolean approvelStatus, @Param("loggedInUserId") String loggedInUserId);
}