package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorStoreTimingModel;

public interface VendorStoreTimingDao {

	void addVendorStoreTiming(VendorStoreTimingModel vendorStoreTimingModel);

	List<VendorStoreTimingModel> getVendorStoreTimingListById(@Param("vendorStoreId") String vendorStoreId);

	void deletePreviousEntries(@Param("vendorStoreId") String vendorStoreId);
}