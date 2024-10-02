package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.WhmgmtRackSlotBookingModel;

public interface WhmgmtRackSlotBookingDao {

	int getRackSlotBookingCount(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStroeId);

	List<WhmgmtRackSlotBookingModel> getRackSlotBookingSearch(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStroeId, @Param("start") int start, @Param("length") int length);

}
