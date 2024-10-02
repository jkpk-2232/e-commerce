package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverAppVersionModel;

public interface DriverAppVersionDao {

	public DriverAppVersionModel getLatestDriverAppVersion(@Param("deviceType") String deviceType);

	public boolean isMandatoryReleaseAvailableAfterThisRelease(@Param("deviceType") String deviceType, @Param("releaseDate") long releaseDate);

	public DriverAppVersionModel getDriverAppVersionByVersion(@Param("deviceType") String deviceType, @Param("version") String version);

}
