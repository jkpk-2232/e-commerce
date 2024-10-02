package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TempAirportRegionModel;

public interface TempAirportRegionDao {
	
	int addTempAirportRegion(TempAirportRegionModel tempAirportRegionModel);

	TempAirportRegionModel checkMarkerLiesInPolygonRegion(@Param("latAndLong") String latAndLong);
	
	int deleteTempAirportRegion();

}
