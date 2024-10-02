package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorAdminSettingsModel;

public interface VendorAdminSettingsDao {

	VendorAdminSettingsModel getVendorAdminSettingsDetailsByVendorId(@Param("vendorId") String vendorId);

	void updateVendorAdminSettings(VendorAdminSettingsModel vendorAdminSettingsModel);

	void insertVendorAdminSettings(VendorAdminSettingsModel vendorAdminSettingsModel);
}