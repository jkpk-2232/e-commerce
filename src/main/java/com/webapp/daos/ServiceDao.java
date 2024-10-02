package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ServiceModel;

public interface ServiceDao {

	void insertServices(ServiceModel serviceModel);

	void updateServices(ServiceModel serviceModel);

	int getServiceCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("serviceTypeId") String serviceTypeId);

	List<ServiceModel> getServiceSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length, @Param("displayType") String displayType,
				@Param("orderColumn") String orderColumn, @Param("serviceTypeId") String serviceTypeId);

	int getServiceSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("serviceTypeId") String serviceTypeId);

	void updateServiceStatus(ServiceModel serviceModel);

	boolean isServiceNameExists(@Param("serviceName") String serviceName, @Param("serviceId") String serviceId);

	ServiceModel getServiceDetailsByServiceId(@Param("serviceId") String serviceId);

	ServiceModel getDefaultServiceModel();
	
	List<ServiceModel> getAllActiveServices();
}