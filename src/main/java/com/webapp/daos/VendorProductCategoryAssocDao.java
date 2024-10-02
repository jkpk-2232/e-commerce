package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorProductCategoryAssocModel;

public interface VendorProductCategoryAssocDao {

	void deleteVendorProductCategoryAssocByUser(String userId);

	void addVendorProductCategoryAssoc(VendorProductCategoryAssocModel vPCAssocModel);

	List<VendorProductCategoryAssocModel> getVendorProductCategoryAssocByVendorId(String vendorId);

	VendorProductCategoryAssocModel getVendorProductCategoryAssocByVendorIdAndProductCategoryId(@Param("vendorId") String vendorId, @Param("productCategoryId") String productCategoryId);

}
