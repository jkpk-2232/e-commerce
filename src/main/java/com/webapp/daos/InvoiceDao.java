package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.InvoiceModel;

public interface InvoiceDao {

	int generateInvoice(InvoiceModel invoiceModel);

	InvoiceModel getInvoiceByTourId(@Param("tourId") String tourId);

	int getTotalInvoicesAmountByDriverId(@Param("userId") String userId);

	int getTotalCountBySearch(@Param("userId") String userId, @Param("globalSearchString") String globalSearchString);

	int updateTransactionId(@Param("transactionId") String transactionId, @Param("tourId") String tourId);

	int updateRefundAndTotalAmount(@Param("tourId") String tourId, @Param("refundAmount") double refundAmount, @Param("updatedAt") long updatedAt);

	int getAllDriverTotalPayableAmount(@Param("startDate") long startDate, @Param("endDate") long endDate, @Param("userId") String userId);

	int setRefundedStatus(InvoiceModel invoiceModel);

	List<InvoiceModel> getMonthlyDriverDashboardSummary(Map<String, Object> inputMap);

	List<InvoiceModel> getWeeklyDriverDashboardSummary(Map<String, Object> inputMap);

	List<InvoiceModel> getDailyDriverDashboardSummary(Map<String, Object> inputMap);

	int updateStaticMapImgUrlByTourId(@Param("tourId") String tourId, @Param("staticMapImgUrl") String staticMapImgUrl, @Param("updatedAt") long updatedAt);

	InvoiceModel getPendingPaymentTourByPassengerId(@Param("passengerId") String passengerId);

	int getTotalInvoicesByDate(Map<String, Object> inputMap);

	List<InvoiceModel> getRefundedIvoiceListBySearchAndDateReports(Map<String, Object> inputMap);

	int getTotalInvoicesByUserId(Map<String, Object> inputMap);

	List<InvoiceModel> getInvoiceListBySearch(Map<String, Object> inputMap);

	double getTotalAdminSettlementAmount(Map<String, Object> inputMap);

	double getAllRefundedTripsAmount(Map<String, Object> inputMap);

	int updateUpdatedAmountCollectWithRemark(InvoiceModel invoiceModel);

	int updatePaymentPaidStatus(Map<String, Object> inputMap);

	double getTotalEarningsPerDay(@Param("driverId") String driverId, @Param("startOfDay") long startOfDay, @Param("endOfDay") long endOfDay);

}