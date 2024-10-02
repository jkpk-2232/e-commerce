package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorAirportRegionModel;

public interface VendorAirportRegionDao {

	void addVendorAirportRegion(VendorAirportRegionModel vendorAirportRegionModel);

	VendorAirportRegionModel getVendorAirportDetailsByVendorIdAndAirportRegionId(@Param("vendorId") String vendorId, @Param("airportRegionId") String airportRegionId);

	void updateMulticityRegionId(VendorAirportRegionModel vendorAirportRegionModel);

	int getVendorAirportRegionCount(Map<String, Object> inputMap);

	List<VendorAirportRegionModel> getVendorAirportRegionSearchDatatable(Map<String, Object> inputMap);

	VendorAirportRegionModel getVendorAirportRegionDetailsById(@Param("vendorAirportRegionId") String vendorAirportRegionId);
}