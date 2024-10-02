package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CarDriversModel;

public interface CarDriversDao {

	int insertCarDrivers(CarDriversModel carDriversModel);

	CarDriversModel getCarDriverDetailsByDriverId(@Param("driverId") String driverId);

	int updateCarDriverDetails(CarDriversModel carModel);

	int getTotalCarDriverCountByCarId(@Param("carId") String carId);

	List<CarDriversModel> getCarDriversListForSearch(@Param("start") int start, @Param("length") int length, @Param("carId") String carId, @Param("globalSearchString") String globalSearchString);
}