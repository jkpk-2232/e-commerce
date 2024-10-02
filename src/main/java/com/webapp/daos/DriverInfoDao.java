package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverInfoModel;

public interface DriverInfoDao {

	DriverInfoModel getDriverAccountDetailsById(String userId);

	DriverInfoModel getActiveDeactiveDriverAccountDetailsById(String userId);

	DriverInfoModel getUserDetailsByRole(String roleId);

	int updateDriverInfo(DriverInfoModel driverInfoModel);

	int addBusinessOwner(DriverInfoModel driverInfoModel);

	int addDriver(DriverInfoModel driverInfoModel);

	int updateDriverApplicationStatus(DriverInfoModel driverInfoModel);

	DriverInfoModel getDriverDetailsByRoleAndApplication(@Param("roleId") String roleId, @Param("applicationStatus") String applicationStatus);

	DriverInfoModel getActiveDeactiveDriverDetailsByRoleAndApplication(@Param("roleId") String roleId, @Param("applicationStatus") String applicationStatus);

	int getDriverCountByRoleAndApplicationStatus(@Param("roleId") String roleId, @Param("applicationStatus") String applicationStatus);

	List<DriverInfoModel> getDriverApplicationListByRoleAndApplication(Map<String, Object> driverMap);

	void updateVerificationCode(@Param("userId") String userId, @Param("verificationCode") String verificationCode);

	int updateDriverForApi(DriverInfoModel driverInfoModel);

	List<DriverInfoModel> getDriverListByMulticityRegionIds(Map<String, Object> driverMap);

	List<DriverInfoModel> getAllDriverDetailsByRoleIdAndApplicationStatus(Map<String, Object> inputMap);
}