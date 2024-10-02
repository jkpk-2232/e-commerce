package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.EmergencyNumbersModel;

public interface EmergencyNumbersDao {
	
	List<EmergencyNumbersModel> getEmergencyPoliceNumbers(String noType);
	
	int addEmergencyNumber(EmergencyNumbersModel emergencyNumbersModel);
	
	int getEmergencyNumberCountByType(String noType);
	
	List<EmergencyNumbersModel> getEmergencyNumberListByTypeForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString, @Param("noType") String noType);

	int getTotalEmergencyNumberListByTypeCountBySearch(@Param("globalSearchString") String globalSearchString, @Param("noType") String noType);
	
	int deleteEmergencyNumber(EmergencyNumbersModel emergencyNumbersModel);
	
}
