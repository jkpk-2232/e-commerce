package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TaxModel;

public interface TaxDao {

	int addTaxDetails(TaxModel taxModel);

	int updateTaxDetails(TaxModel taxModel);

	TaxModel getTaxDetailsById(@Param("taxId") String taxId);

	List<TaxModel> getActiveTaxList();

	int getTaxCount();

	List<TaxModel> getTaxListForSearch(Map<String, Object> inputMap);

	int getTotalTaxCountBySearch(Map<String, Object> inputMap);

	int deleteTax(TaxModel taxModel);

	int updateTaxStatus(TaxModel taxModel);

}
