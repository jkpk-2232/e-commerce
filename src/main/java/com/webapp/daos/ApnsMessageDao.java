package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ApnsMessageModel;

public interface ApnsMessageDao {

	int insertPushMessage(ApnsMessageModel apnsMessageModel);
	
	int insertMultiplePushMessage(List<ApnsMessageModel> apnsMessageModel);

	List<ApnsMessageModel> getAllNotificationsByUserId(ApnsMessageModel apnsMessageModel);

	ApnsMessageModel getById(String apnsMessageId);

	int getTotalNotificationCount(@Param("userId") String userId, @Param("afterTime") long afterTime);

	int deleteNotificationById(ApnsMessageModel apnsMessageModel);

	void deleteVendorFeedsByVendorFeedId(ApnsMessageModel apnsMessageModel);
}