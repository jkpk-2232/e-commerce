package com.webapp.daos;

import java.util.List;
import java.util.Map;

import com.webapp.models.CitySurgeModel;

public interface CitySurgeDao {

	int addCitySurge(CitySurgeModel citySurgeModel);

	int deleteCitySurge(CitySurgeModel citySurgeModel);

	int updateCitySurge(CitySurgeModel citySurgeModel);

	List<CitySurgeModel> getAllCitySurgeByRegionyIds(Map<String, Object> inputMap);

	int activateDeactivateCitySurge(CitySurgeModel citySurgeModel);

	CitySurgeModel getCitySurgeByCitySurgeId(String citySurgeId);

	CitySurgeModel getMaxRadiusCitySurgeByRegionId(String regionId);

	CitySurgeModel getCitySurgeByRadiusAndRegionId(Map<String, Object> inputMap);
	
	List<CitySurgeModel> getCitySurgeByRegionIdAndRadiusOrder(Map<String, Object> inputMap);

	int getAllCitySurgeCount(Map<String, Object> inputMap);

	CitySurgeModel getActiveDeactiveDeletedNonDeletedCitySurgeByCitySurgeId(String citySurgeId);
}