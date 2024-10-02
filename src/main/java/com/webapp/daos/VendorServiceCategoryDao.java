package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorServiceCategoryModel;

public interface VendorServiceCategoryDao {

	void insertVendorServiceCategory(VendorServiceCategoryModel vendorServiceCategoryModel);

	void deleteVendorServiceCategoryByVendorId(VendorServiceCategoryModel vendorServiceCategoryModel);

	VendorServiceCategoryModel getVendorServiceCategoryByVendorId(@Param("vendorId") String vendorId);
}