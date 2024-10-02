package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CarTypeModel;

public interface CarTypeDao {

	List<CarTypeModel> getAllCars() throws SQLException;

	CarTypeModel getCarTypeByCarTypeId(@Param("carTypeId") String carTypeId);

	List<CarTypeModel> getActiveDeactaiveCarDetails();

	int getCarTypeCount();

	List<CarTypeModel> getCarTypeListForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString);

	int getCarTypeListCountForSearch(@Param("globalSearchString") String globalSearchString);

	void updateCarTypeStatus(CarTypeModel carTypeModel);

	void insertCarType(CarTypeModel carTypeModel);

	CarTypeModel getLastPriority();

	void updateCarType(CarTypeModel carTypeModel);

	boolean isCarTypeNameExists(@Param("carType") String carType, @Param("carTypeId") String carTypeId);

	List<CarTypeModel> getAllCarsActiveDeactive();

	List<CarTypeModel> getCarListByIds(@Param("carType") List<String> carType);
}