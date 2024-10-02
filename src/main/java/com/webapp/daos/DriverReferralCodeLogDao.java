package com.webapp.daos;

import java.util.List;
import java.util.Map;

import com.webapp.models.DriverReferralCodeLogModel;

public interface DriverReferralCodeLogDao {

	int addDriverReferralCodeLog(DriverReferralCodeLogModel driverReferralCodeLogModel);

	DriverReferralCodeLogModel getDriverReferralCodeLogByPassengerId(Map<String, Object> inputMap);

	int getTotalDriverReferralLogsCount(Map<String, Object> inputMap);

	List<DriverReferralCodeLogModel> getDriverReferralLogsListForSearch(Map<String, Object> inputMap);

	int deleteDriverReferralCodeLogByPassengerId(DriverReferralCodeLogModel driverReferralCodeLogModel); 

}