package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.MulticityCountryModel;

public interface MulticityCountryDao {

	int getMulticityCountryCount();

	List<MulticityCountryModel> getMulticityCountrySearch(@Param("start") int start, @Param("length") int length, @Param("globalSearchString") String globalSearchString);

	MulticityCountryModel getMulticityCountryIdDetailsById(@Param("multicityCountryId") String multicityCountryId);

	MulticityCountryModel getMulticityCountryIdDetailsByCountryName(@Param("country") String country);

	MulticityCountryModel getMulticityCountryIdDetailsByPhoneNumberCode(@Param("phoneNoCode") String phoneNoCode);
}