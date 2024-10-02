package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.FavouriteLocationsModel;

public interface FavouriteLocationsDao {

	int addFavouriteLocations(FavouriteLocationsModel favouriteLocationsModel);

	boolean isFavouriteNicknameExists(@Param("favouriteNickname") String favouriteNickname, @Param("favouriteLocationsId") String favouriteLocationsId, @Param("userId") String userId);

	int deletefavouriteLocation(FavouriteLocationsModel favouriteLocationsModel);

	List<FavouriteLocationsModel> getFavouriteLocationsList(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("searchText") String searchText, @Param("isSource") boolean isSource);

	int updateFavouriteLocations(FavouriteLocationsModel favouriteLocationsModel);

	FavouriteLocationsModel getFavouriteLocationsDetailsById(@Param("favouriteLocationsId") String favouriteLocationsId);
}