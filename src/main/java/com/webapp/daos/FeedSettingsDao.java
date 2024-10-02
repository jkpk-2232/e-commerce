package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.FeedSettingsModel;

public interface FeedSettingsDao {

	int addFeedSettings(FeedSettingsModel feedSettingsModel);

	int getFeedSettingsCountByUser(String userId);

	List<FeedSettingsModel> getFeedSettingsListForSearch(Map<String, Object> inputMap);

	FeedSettingsModel getFeedSettingsDetailsById(@Param("feedSettingsId") String feedSettingsId);

	int updateFeedSettings(FeedSettingsModel feedSettingsModel);

	FeedSettingsModel getfeedSettingsByMultiCityRegionId(@Param("multicityCityRegionId") String multicityCityRegionId);

	boolean isMultiCityRegionIdExists(@Param("regionId") String regionId);

}
