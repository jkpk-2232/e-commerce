package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ApnsDeviceModel;

public interface ApnsDeviceDao {

	int insertApnsDeviceDetails(ApnsDeviceModel apnsDeviceModel);

	ApnsDeviceModel getDeviseByUserId(String userId);

	long isdevicsTokenExist(String devicsToken);

	int deleteApnsDeviceDetails(@Param("deviceToken") String userId, @Param("updatedAt") long updatedAt);

	int deleteApnsDeviceDetailsByUserId(@Param("userId") String userId, @Param("updatedAt") long updatedAt);

	int getBadgeCount(String userId);

	int recordViewedNotificationTime(ApnsDeviceModel apnsDeviceModel);

	boolean isdevicsUidExist(String deviceUid);

	int updateApnsDeviceUser(ApnsDeviceModel apnsDeviceModel);

	boolean checkRecordViewedNotificationTime(String userId);

	int deleteNotificationTime(String userId);

	ApnsDeviceModel getDeviseByDeviceTokenAndDeviceUid(@Param("deviceToken") String deviceToken, @Param("deviceUid") String deviceUid);

	List<ApnsDeviceModel> getDeviceListByUserIds(Map<String, Object> inputMap);

}