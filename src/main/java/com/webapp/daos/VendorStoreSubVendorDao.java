package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorStoreSubVendorModel;

public interface VendorStoreSubVendorDao {

	void batchInsertVendorStoreSubVendorEntry(List<VendorStoreSubVendorModel> vendorStoreSubVendorList);

	List<VendorStoreSubVendorModel> getVendorStoresAddedToTheSubVendor(@Param("subVendorId") String subVendorId, @Param("fetchAllStores") String fetchAllStores);

	void deleteMapSubVendorsToVendorStore(VendorStoreSubVendorModel vendorStoreSubVendorModel);

	List<VendorStoreSubVendorModel> getSubVendorsAllocatedToTheStore(@Param("vendorStoreId") String vendorStoreId);
}