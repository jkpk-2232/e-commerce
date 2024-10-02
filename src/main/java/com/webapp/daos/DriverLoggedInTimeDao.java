package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverLoggedInTimeModel;

public interface DriverLoggedInTimeDao {

	int addDriverLoggedInTimeDetails(DriverLoggedInTimeModel driverLoggedInTimeModel);

	int insertDriverLoggedInTimeBatch(@Param("driverLoggedInTimeModelList") List<DriverLoggedInTimeModel> driverLoggedInTimeModelList);

	int updateLoggedInTimeById(DriverLoggedInTimeModel driverLoggedInTimeModel);

	List<DriverLoggedInTimeModel> getLoggedInTimesListByDriverIdAndTime(Map<String, Object> inputMap);

	long getTotalLoggedInTimeByDriverIdandDate(Map<String, Object> inputMap);

	int getTotalDriverLoggedInTimeLogCount(Map<String, Object> inputMap);

	List<DriverLoggedInTimeModel> getDriverLoggedInTimeLogListForSearch(Map<String, Object> inputMap);

}