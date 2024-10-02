package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.GeoCityModel;

public interface GeoCityDao {

	List<GeoCityModel> getAllCitesByStateId(@Param("stateId") long stateId) throws SQLException;

	String getCityNameByCityId(long cityId);

	int insertCity(GeoCityModel geoCityModel);
}
