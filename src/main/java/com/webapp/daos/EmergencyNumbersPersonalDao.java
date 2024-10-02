package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.EmergencyNumbersPersonalModel;

public interface EmergencyNumbersPersonalDao {

	int insertEmergencyNumbersPersonalBatch(@Param("emergencyNumbersPersonalModelList") List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalModelList);

	List<EmergencyNumbersPersonalModel> getEmergencyNumbersPersonalListById(String userId);

	int deleteEmergencyNumberPersonal(EmergencyNumbersPersonalModel emergencyNumbersPersonalModel);

	EmergencyNumbersPersonalModel getEmergencyNumbersPersonalDetailsById(String emergencyNumberPersonalId);

}