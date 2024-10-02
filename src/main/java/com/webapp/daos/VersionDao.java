package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VersionModel;

public interface VersionDao {

	public VersionModel getLatestVersion(@Param("deviceType") String deviceType);

	public boolean isMandatoryReleaseAvailableAfterThisRelease(@Param("deviceType") String deviceType, @Param("releaseDate") long releaseDate);

	public VersionModel getVersionByVersion(@Param("deviceType") String deviceType, @Param("version") String version);


}
