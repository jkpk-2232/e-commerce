package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TourTaxModel;

public interface TourTaxDao {

	int insertTourTaxBatch(@Param("tourTaxModelList") List<TourTaxModel> tourTaxModelList);

	TourTaxModel getTourTaxDetailsById(@Param("tourTaxId") String tourTaxId);

	List<TourTaxModel> getTourTaxListByTourId(@Param("tourId") String tourId);

}