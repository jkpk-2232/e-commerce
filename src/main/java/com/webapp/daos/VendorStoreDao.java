package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorStoreModel;

public interface VendorStoreDao {

	void insertVendorStore(VendorStoreModel vendorStoreModel);

	void updateVendorStore(VendorStoreModel vendorStoreModel);

	int getVendorStoreCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<VendorStoreModel> getVendorStoreSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("vendorId") String vendorId,
				@Param("vendorStoreIdList") List<String> vendorStoreIdList);

	int getVendorStoreSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	VendorStoreModel getVendorStoreDetailsById(@Param("vendorStoreId") String vendorStoreId);

	List<VendorStoreModel> getVendorStoresByOpenClosedStatus(@Param("isClosedToday") boolean isClosedToday, @Param("todayEpoch") long todayEpoch, @Param("currentDayOfWeekValue") String currentDayOfWeekValue);

	void updateClosedTodayFlag(@Param("updateList") List<VendorStoreModel> updateList);

	void updateVendorStoreStatus(VendorStoreModel vendorStoreModel);

	List<VendorStoreModel> getVendorStoreList(@Param("assignedRegionList") List<String> assignedRegionList, @Param("vendorId") String vendorId);

	List<VendorStoreModel> getVendorStoreListApi(@Param("vendorId") String vendorId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey);

	List<VendorStoreModel> getVendorStoresBasedOnService(@Param("serviceIds") List<String> serviceIds);

	List<VendorStoreModel> getVendorStoresInfoList(String vendorId);

	List<VendorStoreModel> getVendorStoreListApi(@Param("vendorId") String vendorId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<VendorStoreModel> getVendorStoreListByVendorAndSubVendor(@Param("vendorId") String vendorId, @Param("vendorStoreIdList") List<String> vendorStoreIdList);

	List<VendorStoreModel> getERPBrandList(@Param("assignedRegionList") List<String> assignedRegionList, @Param("vendorId") String vendorId);
	
	List<VendorStoreModel> getVendorStoresWithRacks(@Param("vendorId") String vendorId);

	List<VendorStoreModel> getVendorStoresWithRacks(String vendorId, List<String> vendorStoreIdList);

}