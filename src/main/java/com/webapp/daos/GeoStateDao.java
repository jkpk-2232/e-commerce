package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.GeoStateModel;

public interface GeoStateDao {

	List<GeoStateModel> getAllStatesByCountryId(@Param("countryId") long countryId) throws SQLException;

	String getStateNameByStateId(long stateId);
	
}


