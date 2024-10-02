package com.utils.myhub;

import com.webapp.models.VendorServiceCategoryModel;

public class ServiceCategoryUtils {

	public static void mapVendorServiceCategory(String loggedInUserId, String vendorId, String serviceId, String categoryId) {

		VendorServiceCategoryModel vscm = new VendorServiceCategoryModel();
		vscm.setVendorId(vendorId);
		vscm.setServiceId(serviceId);
		vscm.setCategoryId(categoryId);

		vscm.deleteVendorServiceCategoryByVendorId();

		vscm.insertVendorServiceCategory(loggedInUserId);
	}
}
