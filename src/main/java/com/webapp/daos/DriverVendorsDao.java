package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverVendorsModel;

public interface DriverVendorsDao {

	int addDriverVendorMapping(DriverVendorsModel driverVendorsModel);

	int deleteDriverVendorMapping(String driverId);

	DriverVendorsModel getDriverVendorDetailsByDriverId(@Param("driverId") String driverId);

	void batchInsertDefaultUserVendorMapping(@Param("list") List<DriverVendorsModel> list);
}