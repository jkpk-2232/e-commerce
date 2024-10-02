package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.RentalPackageModel;

public interface RentalPackageDao {

	int addRentalPackage(RentalPackageModel rentalPackageModel);

	int updateRentalPackage(RentalPackageModel rentalPackageModel);

	RentalPackageModel getRentalPackageDetailsById(@Param("rentalPackageId") String rentalPackageId);

	int getTotalRentalPackagesCount(Map<String, Object> inputMap);

	List<RentalPackageModel> getRentalPackageListForSearch(Map<String, Object> inputMap);

	List<RentalPackageModel> getRentalPackageListPagination(Map<String, Object> inputMap);

	void activeDeactiveRentalPackage(RentalPackageModel rentalPackageModel);

	void deleteRentalPackage(RentalPackageModel rentalPackageModel);

	void updateVendorIdForRentalPackages(RentalPackageModel rentalPackageModel);

	List<RentalPackageModel> getAllRentalPackages();
}