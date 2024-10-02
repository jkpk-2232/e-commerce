package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.MulticityUserRegionModel;

public interface MulticityUserRegionDao {

	List<MulticityUserRegionModel> getMulticityUserRegionByUserId(@Param("userId") String userId);

	int addMulticityUserRegion(@Param("multicityUserRegionModel") List<MulticityUserRegionModel> multicityUserRegionModel);

	int deleteUserRegions(MulticityUserRegionModel multicityDriverRegionModel);
}