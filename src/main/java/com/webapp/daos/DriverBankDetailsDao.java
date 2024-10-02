package com.webapp.daos;

import com.webapp.models.DriverBankDetailsModel;

public interface DriverBankDetailsDao {

	int addDriverBankDetails(DriverBankDetailsModel driverBankDetailsModel);

	DriverBankDetailsModel getDriverBankDetails(String userId);

	int updateDriverBankDetails(DriverBankDetailsModel driverBankDetailsModel);
}
