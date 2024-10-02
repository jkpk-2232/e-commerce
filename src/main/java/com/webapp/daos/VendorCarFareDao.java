package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorCarFareModel;

public interface VendorCarFareDao {

	int addVendorCarFare(VendorCarFareModel vendorCarFareModel);

	VendorCarFareModel getVendorCarFareDetailsByRegionCountryAndId(Map<String, Object> inputMap);

	int updateVendorCarFareForMultiCity(VendorCarFareModel carFareModel);

	void batchInsert(List<VendorCarFareModel> vendorCarFareList);

	void deleteRegionFare(VendorCarFareModel vendorCarFareModel);

	boolean isEntryExist(@Param("multicityCityRegionId") String multicityCityRegionId, @Param("vendorId") String vendorId, @Param("serviceTypeId") String serviceTypeId);

	void deleteVendorCarFareByRegionIdAndCarTypeList(Map<String, Object> inputMap);

	List<VendorCarFareModel> getVendorCarFareListByRegionIdAndVendorId(@Param("multicityCityRegionId") String multicityCityRegionId, @Param("vendorId") String vendorId, @Param("serviceTypeId") String serviceTypeId);

	void deleteExistingVendorCarFare(VendorCarFareModel vendorCarFareModel);
}