package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AirportRegionModel;

public interface AirportRegionDao {
	
	int addAirportRegion(AirportRegionModel airportRegionModel);

	List<AirportRegionModel> getAllRegions();

	AirportRegionModel getAirportRegionDetailsById(@Param("airportRegionId") String airportRegionId);

	int editAirportRegion(AirportRegionModel airportRegionModel);

	AirportRegionModel getPolygonContainingLatLngArea(@Param("latAndLong") String latAndLong);

	int updateActiveDeactive(AirportRegionModel airportRegionModel);

	List<AirportRegionModel> getAirportRegionActiveDeactiveDatatable(@Param("start") int start, @Param("length") int length,@Param("onOffStatusForCheck") String onOffStatusForCheck, @Param("onOffStatus") boolean onOffStatus,@Param("globalSearchString") String globalSearchString);

	List<AirportRegionModel> getAirportRegionSearchDatatable(Map<String, Object> inputMap);

	int getAirportRegionCount(Map<String, Object> inputMap);
	
	boolean getDriverCity(@Param("userId") String userId,@Param("multicityCityRegionId") String multicityCityRegionId);

	AirportRegionModel getAirportRegionContainingLatLng(@Param("latAndLong") String latAndLong);

	List<AirportRegionModel> getScriptAllExistingAirports();
}