package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.QrProfileModel;

public interface QrProfileDao {

	void insertQrProfile(QrProfileModel qrProfileModel);

	int getQrProfileCount(@Param("vendorStoreId") String vendorStoreId);

	List<QrProfileModel> getQrProfileSearch(@Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey, @Param("vendorStoreId") String vendorStoreId);

	int getQrProfileSearchCount(@Param("searchKey") String searchKey, @Param("vendorStoreId") String vendorStoreId);

	boolean isQrCodeIdExists(@Param("qrCodeId") String qrCodeId, @Param("qrProfileId") String qrProfileId);

}
