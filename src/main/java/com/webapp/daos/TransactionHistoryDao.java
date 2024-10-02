package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.TransactionHistoryModel;

public interface TransactionHistoryDao {

	int addTransactionHistory(TransactionHistoryModel transactionHistoryModel);

	int updateTransactionHistory(TransactionHistoryModel transactionHistoryModel);

	List<TransactionHistoryModel> getTransactionHistoryList(@Param("userId") String userId, @Param("start") int start, @Param("length") int length);

	int getTotalTransactionCountByUserId(@Param("userId") String userId);

	TransactionHistoryModel getTrasactionHistoryByTrasactionId(String transactionId);
}