package com.webapp.daos;

import com.webapp.models.CarVendorsModel;

public interface CarVendorsDao {

	int addCarVendorMapping(CarVendorsModel carVendorsModel);

	int deleteCarVendorMapping(String driverId);

	void batchUpdateExistingAdminCarMappingToVendor(CarVendorsModel carVendorsModel);
}