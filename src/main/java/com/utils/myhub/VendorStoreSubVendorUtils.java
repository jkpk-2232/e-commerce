package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.models.VendorStoreSubVendorModel;

public class VendorStoreSubVendorUtils {

	public static void mapSubVendorsToVendorStore(List<String> vendorStoreIds, String vendorId, String subVendorId) {

		VendorStoreSubVendorModel vendorStoreSubVendorModelExisting = new VendorStoreSubVendorModel();
		vendorStoreSubVendorModelExisting.setSubVendorId(subVendorId);
		vendorStoreSubVendorModelExisting.deleteMapSubVendorsToVendorStore();

		long createdAt = DateUtils.nowAsGmtMillisec();
		long updatedAt = createdAt;
		List<VendorStoreSubVendorModel> list = new ArrayList<>();

		for (String vendorStoreId : vendorStoreIds) {

			VendorStoreSubVendorModel vendorStoreSubVendorModel = new VendorStoreSubVendorModel();
			vendorStoreSubVendorModel.setVendorStoreSubVendorId(UUIDGenerator.generateUUID());
			vendorStoreSubVendorModel.setVendorId(vendorId);
			vendorStoreSubVendorModel.setSubVendorId(subVendorId);
			vendorStoreSubVendorModel.setVendorStoreId(vendorStoreId);
			vendorStoreSubVendorModel.setCreatedAt(createdAt);
			vendorStoreSubVendorModel.setUpdatedAt(updatedAt);
			vendorStoreSubVendorModel.setCreatedBy(vendorId);
			vendorStoreSubVendorModel.setUpdatedBy(vendorId);

			list.add(vendorStoreSubVendorModel);

			if (list.size() >= ProjectConstants.BATCH_INSERT_SIZE) {
				VendorStoreSubVendorModel.batchInsertVendorStoreSubVendorEntryOs(list);
				list.clear();
			}
		}

		if (!list.isEmpty()) {
			VendorStoreSubVendorModel.batchInsertVendorStoreSubVendorEntryOs(list);
			list.clear();
		}
	}

	public static List<String> getVendorStoresAssignedToTheSubVendor(String subVendorId, boolean fetchAllStores) {

		List<String> list = new ArrayList<>();
		List<VendorStoreSubVendorModel> vendorStoreSubVendorList = VendorStoreSubVendorModel.getVendorStoresAddedToTheSubVendor(subVendorId, fetchAllStores ? null : ProjectConstants.NO);
		for (VendorStoreSubVendorModel vendorStoreSubVendorModel : vendorStoreSubVendorList) {
			list.add(vendorStoreSubVendorModel.getVendorStoreId());
		}

		return list;
	}
}
