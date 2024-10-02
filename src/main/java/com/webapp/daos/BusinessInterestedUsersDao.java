package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.BusinessInterestedUsersModel;

public interface BusinessInterestedUsersDao {

	void insertBusinessInterestedUser(BusinessInterestedUsersModel businessInterestedUsersModel);

	int getBusinessInterestedUsersCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	List<BusinessInterestedUsersModel> getBusinessInterestedUsersSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong,
				@Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);

	int getBusinessUserSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey);

}
