package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UserAccountLogsModel;

public interface UserAccountLogsDao {

	int insertUserAccountLog(UserAccountLogsModel userAccountLogsModel);

	List<UserAccountLogsModel> getUserAccountLogsListByUserId(Map<String, Object> inputMap);

	int getTotalUserAccountLogsCount(Map<String, Object> inputMap);

	List<UserAccountLogsModel> getUserAccountLogsListForSearch(Map<String, Object> inputMap);

	int getFilteredUserAccountLogsCount(Map<String, Object> inputMap);

	List<UserAccountLogsModel> getUserAccountLogsListForExport(Map<String, Object> inputMap);

	UserAccountLogsModel getUserAccountLogDetailsByEncashRequestId(@Param("encashRequestId") String encashRequestId);

	int updateUserAccountLogByEncashRequestId(UserAccountLogsModel userAccountLogsModel);

}