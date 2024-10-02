package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.RentalPackageFareModel;

public interface RentalPackageFareDao {

	int insertRentalPackageFareBatch(@Param("rentalPackageFareModelList") List<RentalPackageFareModel> rentalPackageFareModelList);

	int deleteRentalPackageFareByRentalPackageId(RentalPackageFareModel rentalPackageCarTypeModel);

	List<RentalPackageFareModel> getRentalPackageFareListByRentalPackageId(@Param("rentalPackageId") String rentalPackageId);

	RentalPackageFareModel getRentalPackageFareDetailsByRentalIdnCarType(Map<String, Object> inputMap);

}