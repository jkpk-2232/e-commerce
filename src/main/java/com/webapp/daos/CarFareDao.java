package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CarFareModel;

public interface CarFareDao {

	int addCarFare(CarFareModel carFareModel);

	int updateCarFareForMultiCity(CarFareModel carFareModel);

	int updateCarFareForAirportRegion(CarFareModel carFareModel);

	int updateAirportDropCarFareForAirportRegion(CarFareModel carFareModel);

	void deleteExistingCarFare(CarFareModel carFareModel);

	// ---------------------------------------------------------------

	List<CarFareModel> getCarFare(@Param("serviceTypeId") String serviceTypeId) throws SQLException;

	CarFareModel getById(String carTypeId, @Param("serviceTypeId") String serviceTypeId);

	CarFareModel getCarFareDetailsByRegionCountryAndId(Map<String, Object> inputMap);

	CarFareModel getCarFareDetailsByAirportRegionId(Map<String, Object> inputMap);

	CarFareModel getActiveCarFareDetailsByAirportRegionId(Map<String, Object> inputMap);

	CarFareModel getAirportDropCarFareDetailsByAirportRegionId(Map<String, Object> inputMap);

	List<CarFareModel> getCarFareListByRegionId(@Param("multicityCityRegionId") String multicityCityRegionId, @Param("serviceTypeId") String serviceTypeId);

	List<CarFareModel> getCarFareListByRegionIdAndCarTypeId(@Param("multicityCityRegionId") String multicityCityRegionId, @Param("carTypeId") String carTypeId, @Param("serviceTypeId") String serviceTypeId);

	List<CarFareModel> getScriptAirportRegionCarFareDetails(@Param("airportRegionId") String airportRegionId, @Param("airportBookingType") String airportBookingType, @Param("serviceTypeId") String serviceTypeId);
}