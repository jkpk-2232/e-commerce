package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.WhmgmtRackModel;

public interface WhmgmtRackDao {

	List<WhmgmtRackModel> getRackListByvendorIdAndStoreId(@Param("vendorId") String vendorId, @Param("vendorStroeId") String vendorStoreId,@Param("start") int start ,@Param("length") int length);

	void insertRacks(@Param("rackList") List<WhmgmtRackModel> rackList);

	void insertRack(WhmgmtRackModel rackModel);

}
