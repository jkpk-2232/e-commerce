package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AnnouncementModel;

public interface AnnouncementDao {

	int addAnnouncementtMessage(AnnouncementModel announcementModel);

	List<AnnouncementModel> getAnnouncementListForSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("start") int start, @Param("length") int length, @Param("order") String order,
				@Param("globalSearchString") String globalSearchString);

	int getTotalAnnouncementCountBySearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("globalSearchString") String globalSearchString);

	int getAnnouncementCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);
}
