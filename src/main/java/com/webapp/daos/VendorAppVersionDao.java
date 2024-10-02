package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.VendorAppVersionModel;

public interface VendorAppVersionDao {

	public VendorAppVersionModel getVendorAppVersionByVersion(@Param("deviceType") String deviceType, @Param("version") String version);
	
	public VendorAppVersionModel getLatestVendorAppVersion(@Param("deviceType") String deviceType);

	public boolean isMandatoryReleaseAvailableAfterThisRelease(@Param("deviceType") String deviceType, @Param("releaseDate") long releaseDate);
}
