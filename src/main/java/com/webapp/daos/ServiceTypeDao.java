package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ServiceTypeModel;

public interface ServiceTypeDao {

	List<ServiceTypeModel> getServiceTypeSearch(@Param("startDatelong") int startDatelong, @Param("endDatelong") int endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);
}