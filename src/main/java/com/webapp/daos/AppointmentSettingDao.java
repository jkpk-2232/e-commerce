package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AppointmentSettingModel;

public interface AppointmentSettingDao {

	void insertAppointmentSettings(AppointmentSettingModel appointmentSettingModel);

	void updateAppointmentSettings(AppointmentSettingModel appointmentSettingModel);

	int getAppointmentSettingCount(@Param("serviceId") String serviceId);

	List<AppointmentSettingModel> getAppointmentSettingSearch(@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("orderColumn") String orderColumn, @Param("serviceId") String serviceId);

	int getAppointmentSettingSearchCount(@Param("searchKey") String searchKey, @Param("serviceId") String serviceId);

	AppointmentSettingModel getAppointmentSettingDetailsByServiceId(@Param("serviceId") String serviceId);
}