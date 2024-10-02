package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CurrencyModel;

public interface CurrencyDao {
	
	List<CurrencyModel> getCurrencyList();
	
	CurrencyModel getCurrencyDetailsByCurrencyId(@Param("currencyId") long currencyId);
}