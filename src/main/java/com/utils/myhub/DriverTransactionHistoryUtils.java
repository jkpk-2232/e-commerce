package com.utils.myhub;

import com.webapp.models.DriverTransactionHistoryModel;

public class DriverTransactionHistoryUtils {

	public static void logDriverTransactionHistory(String driverId, String vendorId, String paymentTypeId, String transactionId, String transactionType, boolean isDebit, double amount, String status, boolean isDriverSubscriptionExtension,
				String driverSubscriptionExtensionLogId) {

		DriverTransactionHistoryModel driverTransactionHistoryModel = new DriverTransactionHistoryModel();

		driverTransactionHistoryModel.setDriverId(driverId);
		driverTransactionHistoryModel.setVendorId(vendorId);
		driverTransactionHistoryModel.setPaymentTypeId(paymentTypeId);
		driverTransactionHistoryModel.setTransactionId(transactionId);
		driverTransactionHistoryModel.setTransactionType(transactionType);
		driverTransactionHistoryModel.setDebit(isDebit);
		driverTransactionHistoryModel.setAmount(amount);
		driverTransactionHistoryModel.setStatus(status);
		driverTransactionHistoryModel.setDriverSubscriptionExtension(isDriverSubscriptionExtension);
		driverTransactionHistoryModel.setDriverSubscriptionExtensionLogId(driverSubscriptionExtensionLogId);

		driverTransactionHistoryModel.addTransactionHistory(driverId);
	}
}
