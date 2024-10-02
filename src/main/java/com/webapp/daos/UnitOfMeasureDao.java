package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UnitOfMeasureModel;

public interface UnitOfMeasureDao {

	boolean isUomNameExists(@Param("uomName") String uomName, @Param("uomId") Integer uomId);

	void insertUom(UnitOfMeasureModel unitOfMeasureModel);

	UnitOfMeasureModel getUomDetailsByUomId(Integer uomId);

	void updateUom(UnitOfMeasureModel unitOfMeasureModel);

	int getUomsCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong);

	List<UnitOfMeasureModel> getUomSearch(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey, @Param("start") int start, @Param("length") int length);

	int getUomSearchCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("searchKey") String searchKey);

	void updateUomStatus(UnitOfMeasureModel unitOfMeasureModel);

	List<UnitOfMeasureModel> getuomList();

}
