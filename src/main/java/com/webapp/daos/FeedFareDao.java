package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.FeedFareModel;

public interface FeedFareDao {

	int insertFeedFareBatch(@Param("feedFareModelList") List<FeedFareModel> feedFareModelList);

	List<FeedFareModel> getFeedFareListByFeedSettingsId(@Param("feedSettingsId") String feedSettingsId);

	int deleteFeedFareByFeedSettingsId(FeedFareModel feedFareModel);

	List<FeedFareModel> getFeedFareListByFeedSettingsIdAndServiceId(@Param("feedSettingsId") String feedSettingsId, @Param("serviceIdList") List<String> serviceIdList);

	FeedFareModel getFeedFareDetailsByFeedSettingsIdAndServiceId(@Param("feedSettingsId") String feedSettingsId, @Param("serviceId") String serviceId);

}
