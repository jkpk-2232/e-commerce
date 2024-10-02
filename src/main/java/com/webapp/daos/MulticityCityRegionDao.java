package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.MulticityCityRegionModel;

public interface MulticityCityRegionDao {

	int getMulticityCityRegionCount(@Param("multicityCountryId") String multicityCountryId);

	List<MulticityCityRegionModel> getMulticityCityRegionSearch(@Param("start") int start, @Param("length") int length, @Param("globalSearchString") String globalSearchString, @Param("multicityCountryId") String multicityCountryId);

	int addMulticityCityRegion(MulticityCityRegionModel multicityCityRegionModel);

	List<MulticityCityRegionModel> test(Map<String, Object> inputMap);

	MulticityCityRegionModel getMulticityCityRegionDetailsByCityId(@Param("multicityCityRegionId") String multicityCityRegionId);

	List<MulticityCityRegionModel> getNearByRegionList(@Param("latAndLong") String latAndLong, @Param("distance") String distance);

	int editMulticityCityRegion(MulticityCityRegionModel multicityCityRegionModel);

	List<MulticityCityRegionModel> getMulticityCityRegionList(@Param("regionList") List<String> regionList, @Param("multicityCountryId") String multicityCountryId);

	List<MulticityCityRegionModel> getMulticityCityRegionSearchDatatable(@Param("start") int start, @Param("length") int length, @Param("globalSearchString") String globalSearchString, @Param("multicityCountryId") String multicityCountryId);

	void permanentDeleteRegion(MulticityCityRegionModel multicityCityRegionModel);

	void deleteRegion(MulticityCityRegionModel multicityCityRegionModel);

	void activateRegion(MulticityCityRegionModel multicityCityRegionModel);

	List<MulticityCityRegionModel> getMulticityCityRegionListByMulticityCountryId(Map<String, Object> inputMap);

	List<MulticityCityRegionModel> getMulticityCityRegionDetails();

	int getMulticityCityRegionCountByVendorId(@Param("multicityCountryId") String multicityCountryId, @Param("vendorId") String vendorId);

	List<MulticityCityRegionModel> getMulticityCityRegionSearchDatatableByVendorId(@Param("start") int start, @Param("length") int length, @Param("globalSearchString") String globalSearchString, @Param("multicityCountryId") String multicityCountryId,
				@Param("vendorId") String vendorId);
}