package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverSubscriptionExtensionLogsModel;

public interface DriverSubscriptionExtensionLogsDao {

	void insertDriverSubscriptionExtensionLogs(DriverSubscriptionExtensionLogsModel driverSubscriptionExtensionLogsModel);

	void updateDriverSubscriptionExtensionLogs(DriverSubscriptionExtensionLogsModel driverSubscriptionExtensionLogsModel);

	int getDriverSubscriptionExtensionLogsCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	List<DriverSubscriptionExtensionLogsModel> getDriverSubscriptionExtensionLogsListForSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey);
}