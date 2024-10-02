package com.webapp.daos;

import com.webapp.models.DrivingLicenseInfoModel;

public interface DrivingLicenseInfoDao {

	int insertDriverLicenseDetails(DrivingLicenseInfoModel drivingLicenseInfoModel);

	DrivingLicenseInfoModel getDriverLicenseDetails(String userId);

	int updateDrivingLicenseInfo(DrivingLicenseInfoModel drivingLicenseInfoModel);
}
