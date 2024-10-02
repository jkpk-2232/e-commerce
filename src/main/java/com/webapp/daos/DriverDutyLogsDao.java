package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverDutyLogsModel;

public interface DriverDutyLogsDao {

	int addDriverDutyLogs(DriverDutyLogsModel driverDutyLogsModel);

	List<DriverDutyLogsModel> getDriverDutyReportAdminListForSearch(@Param("driverId") String driverId, @Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("startDate") long startDate, @Param("endDate") long endDate);

	List<DriverDutyLogsModel> getDriverOnOffDutyReportAdminListForSearch(@Param("driverId") String driverId, @Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("startDate") long startDate, @Param("endDate") long endDate,
			@Param("dutyStatus") boolean dutyStatus);

	int getDriverDutyReportAdminListForSearchCount(@Param("driverId") String driverId, @Param("startDate") long startDate, @Param("endDate") long endDate);

	int getDriverOnOffDutyReportAdminListForSearchCount(@Param("driverId") String driverId, @Param("startDate") long startDate, @Param("endDate") long endDate, @Param("dutyStatus") boolean dutyStatus);

	int getTotalDutyReportCount(@Param("driverId") String driverId, @Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	int getTotalOnOffDutyReportCount(@Param("driverId") String driverId, @Param("dutyStatus") boolean dutyStatus);

	DriverDutyLogsModel getLastDriverDutyLogDetails(Map<String, Object> inputMap);

	int insertDriverDutyLogsBatch(@Param("driverDutyLogsModelList") List<DriverDutyLogsModel> driverDutyLogsModelList);

}