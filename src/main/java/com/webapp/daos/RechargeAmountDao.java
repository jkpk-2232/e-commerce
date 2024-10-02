package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.RechargeAmountModel;

public interface RechargeAmountDao {
	
	List<RechargeAmountModel> getAllRechargeAmountList();
	
	RechargeAmountModel getRechargeAmountById(@Param("rechargeAmountId") String rechargeAmountId);
}