package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.OfflineOrderItemModel;

public interface OfflineOrderItemDao {

	void insertOfflineOrderitem(@Param("offlineOrderItemList") List<OfflineOrderItemModel> offlineOrderItemList);

}
