package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverModel;

public interface DriverDao {

	List<DriverModel> getDriverJobHistory(@Param("driverId") String driverId,@Param("updatedAt") long updatedAt) throws SQLException;

	int acceptTrip(DriverModel driverModel);

	DriverModel getDriverJobDetails(String bookingRequestId);

	int cancelTrip(DriverModel driverModel);
	
	DriverModel getDriverDetailsByReferralCode(@Param("driverReferralCode") String driverReferralCode ,@Param("applicationStatus") String applicationStatus );
}
