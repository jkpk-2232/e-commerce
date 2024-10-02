package com.webapp.daos;

import com.webapp.models.DriverPayablePercentageModel;

public interface DriverPayablePercentageDao {

	int updatePayablePercentage(DriverPayablePercentageModel driverPayablePercentageModel);

	DriverPayablePercentageModel getPayablePercentage();

}
