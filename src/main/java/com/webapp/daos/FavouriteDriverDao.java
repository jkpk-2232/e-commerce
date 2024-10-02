package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.FavouriteDriverModel;

public interface FavouriteDriverDao {

	int addFavouriteDriver(FavouriteDriverModel favouriteDriverModel);

	FavouriteDriverModel getFavouriteDriverDetails(@Param("passengerId") String passengerId, @Param("driverId") String driverId);

	int deleteFavouriteDriver(FavouriteDriverModel favouriteDriverModel);

	List<FavouriteDriverModel> getFavouriteDriverList(@Param("passengerId") String passengerId, @Param("start") int start, @Param("length") int length);
}