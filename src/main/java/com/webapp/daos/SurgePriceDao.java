package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.SurgePriceModel;

public interface SurgePriceDao {

	int addSurgePriceDetails(SurgePriceModel surgePriceModel);

	SurgePriceModel getSurgePriceDetailsByIdAndActiveStatus(@Param("surgePriceId") String surgePriceId, @Param("isActive") boolean isActive);

	SurgePriceModel getSurgePriceDetailsById(@Param("surgePriceId") String surgePriceId);

	int getSurgePriceCount(@Param("surgePriceInDouble") double surgePriceInDouble, @Param("multicityCityRegionId") String multicityCityRegionId);

	List<SurgePriceModel> getSurgePriceListForSearch(Map<String, Object> inputMap);

	List<SurgePriceModel> getSurgePriceListByMulticityCityRegionId(@Param("multicityCityRegionId") String multicityCityRegionId);

	SurgePriceModel getSurgePriceDetailsByRequestTimeAndRegionId(Map<String, Object> inputMap);

	int activateDeactivateSurgePrice(SurgePriceModel surgePriceModel);

	int deleteSurgePrice(SurgePriceModel surgePriceModel);

	int updateSurgePriceDetails(SurgePriceModel surgePriceModel);
}