package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DriverTransactionHistoryModel;

public interface DriverTransactionHistoryDao {

	int addTransactionHistory(DriverTransactionHistoryModel transactionHistoryModel);

	int updateTransactionHistory(DriverTransactionHistoryModel transactionHistoryModel);

	List<DriverTransactionHistoryModel> getTransactionHistoryList(@Param("driverId") String driverId, @Param("start") int start, @Param("length") int length);

	int getTotalTransactionCountByUserId(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("vendorIds") String[] vendorIds, @Param("driverId") String driverId);

	DriverTransactionHistoryModel getTrasactionHistoryByTrasactionId(String transactionId);

	List<DriverTransactionHistoryModel> getTransactionListForSearch(Map<String, Object> inputMap);
}