package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorAirportRegionCarFareModel;

public interface VendorAirportRegionCarFareDao {

	void addVendorAirportRegionCarFare(VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel);

	void batchInsert(@Param("fareList") List<VendorAirportRegionCarFareModel> fareList);

	void updateMulticityRegionId(VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel);

	VendorAirportRegionCarFareModel getCarFareDetailsByVendorIdAirportIdAndCarTypeId(@Param("vendorId") String vendorId, @Param("airportRegionId") String airportRegionId, @Param("carTypeId") String carTypeId, @Param("airportBookingType") String airportBookingType);

	void deleteExistingData(VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel);
}