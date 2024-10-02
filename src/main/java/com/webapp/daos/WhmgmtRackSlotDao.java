package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.WhmgmtRackSlotModel;

public interface WhmgmtRackSlotDao {

	void insertSlots(@Param("whmgmtRackSlotList") List<WhmgmtRackSlotModel> whmgmtRackSlotList);

}
