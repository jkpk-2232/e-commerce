package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.webapp.models.VendorProductCategoryAssocModel;

public class VendorProductCategoryAssocUtils {

	public static List<String> getVendorProductCategoryAssocByvendorId(String vendorId) {
		
		List<VendorProductCategoryAssocModel> VPCAssocList = VendorProductCategoryAssocModel.getVendorProductCategoryAssocByVendorId(vendorId);
		
		List<String> productCategoryIdList = new ArrayList<>();
		
		for (VendorProductCategoryAssocModel vendorProductCategoryAssocModel : VPCAssocList) {
			productCategoryIdList.add(vendorProductCategoryAssocModel.getProductCategoryId());
		}
		
		return productCategoryIdList;
	}

}
