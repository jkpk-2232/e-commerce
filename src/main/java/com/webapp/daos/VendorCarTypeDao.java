package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorCarTypeModel;

public interface VendorCarTypeDao {

	void insertVendorCarType(VendorCarTypeModel carTypeModel);

	List<VendorCarTypeModel> getVendorCarTypeListByVendorId(@Param("vendorId") String vendorId, @Param("serviceTypeId") String serviceTypeId);
	
	List<VendorCarTypeModel> getVendorCarTypeListByVendorIdSortPriority(@Param("vendorId") String vendorId, @Param("serviceTypeId") String serviceTypeId);

	VendorCarTypeModel getVendorCarTypeListByVendorIdForBike(@Param("vendorId") String vendorId, @Param("serviceTypeId") String serviceTypeId, @Param("carTypeId") String carTypeId);

	void deleteExistingCarTypes(VendorCarTypeModel vendorCarTypeModel);

	void batchInsertVendorCarType(List<VendorCarTypeModel> vendorCarTypeList);

	void updateVendorCarTypeStatusByCarTypeId(VendorCarTypeModel vendorCarTypeModel);
}