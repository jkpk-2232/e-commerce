package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverCarTypeModel;

public interface DriverCarTypeDao {

	int insertDriverCarTypesBatch(@Param("driverCarTypeModelList") List<DriverCarTypeModel> driverCarTypeModelList);

	List<DriverCarTypeModel> getDriverCarTypesListByDriverId(@Param("driverId") String driverId);

	int deleteDriverCarTypes(DriverCarTypeModel driverCarTypeModel);

}
