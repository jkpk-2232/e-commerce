package com.utils.myhub;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverSubscriptionExtensionLogsModel;
import com.webapp.models.DriverSubscriptionPackageHistoryModel;
import com.webapp.models.PaymentTypeModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.TourModel;

public class DriverSubscriptionUtils {

	private static Logger logger = Logger.getLogger(DriverSubscriptionUtils.class);

	public static boolean isEligibleForNewSubscription(String subscriptionId, String loggedInuserId, String headerVendorId) {

		List<DriverSubscriptionPackageHistoryModel> driverSubscriptionPackageHistoryList = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryForStatus(loggedInuserId, headerVendorId);

		long tempTimeNow = DateUtils.nowAsGmtMillisec();

		if (driverSubscriptionPackageHistoryList.size() == 0) {
			// Driver has subscription package purchased, hence purchase directly
			return true;
		} else {
			// Driver has history of packages.

			// Step1: Determine drivers current active subscription
			String currentSubscriptionPackageId = getDriverCurrentSubscription(loggedInuserId, headerVendorId, tempTimeNow);
			if (currentSubscriptionPackageId == null) {
				// No current subscription package found
				return true;
			} else {
				// Current subscription package found, now check whether there is another
				// package already purchased after this current package
				long temp = 0;
				List<DriverSubscriptionPackageHistoryModel> list = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionHistoryByPackageIdDriverId(subscriptionId, loggedInuserId, headerVendorId);
				for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : list) {
					if (driverSubscriptionPackageHistoryModel.isCurrentPackage()) {
						temp = driverSubscriptionPackageHistoryModel.getCreatedAt();
						break;
					}
				}

				int numberOfPackages = DriverSubscriptionPackageHistoryModel.getNumberOfPackages(loggedInuserId, headerVendorId, temp);
				if (numberOfPackages == 0) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public static boolean processDriverSubscription(SubscriptionPackageModel subscriptionPackageModel, String loggedInuserId, String headerVendorId, String paymentMode) {

		List<DriverSubscriptionPackageHistoryModel> driverSubscriptionPackageHistoryList = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryForStatus(loggedInuserId, headerVendorId);

		long tempTimeNow = DateUtils.nowAsGmtMillisec();
		long tempPackageStartTime = tempTimeNow;

		if (driverSubscriptionPackageHistoryList.size() == 0) {
			// Driver has subscription package purchased, hence purchase directly
			subscribePackage(subscriptionPackageModel.getSubscriptionPackageId(), loggedInuserId, headerVendorId, true, tempTimeNow, paymentMode);
			FeedAutomationUtils.uploadAutoFeed(subscriptionPackageModel.getSubscriptionPackageId(), loggedInuserId, headerVendorId, null);
			return true;
		} else {
			// Driver has history of packages.

			// Step1: Determine drivers current active subscription
			String currentSubscriptionPackageId = getDriverCurrentSubscription(loggedInuserId, headerVendorId, tempTimeNow);
			if (currentSubscriptionPackageId == null) {
				// No current subscription package found
				subscribePackage(subscriptionPackageModel.getSubscriptionPackageId(), loggedInuserId, headerVendorId, true, tempTimeNow, paymentMode);
				return true;
			} else {
				// Current subscription package found, now check whether there is another
				// package already purchased after this current package
				long temp = 0;
				List<DriverSubscriptionPackageHistoryModel> list = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionHistoryByPackageIdDriverId(subscriptionPackageModel.getSubscriptionPackageId(), loggedInuserId, headerVendorId);
				for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : list) {
					if (driverSubscriptionPackageHistoryModel.isCurrentPackage()) {
						temp = driverSubscriptionPackageHistoryModel.getCreatedAt();
						tempPackageStartTime = driverSubscriptionPackageHistoryModel.getPackageEndTime();
						break;
					}
				}

				int numberOfPackages = DriverSubscriptionPackageHistoryModel.getNumberOfPackages(loggedInuserId, headerVendorId, temp);

				if (numberOfPackages == 0) {
					subscribePackage(subscriptionPackageModel.getSubscriptionPackageId(), loggedInuserId, headerVendorId, false, tempPackageStartTime, paymentMode);
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public static void subscribePackage(String subscriptionPackageId, String driverId, String vendorId, boolean isCurrentPackage, long packageStartTime, String paymentMode) {

		DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel = new DriverSubscriptionPackageHistoryModel();

		if (isCurrentPackage) {
			driverSubscriptionPackageHistoryModel.setDriverSubscriptionPackageHistoryId(null);
			driverSubscriptionPackageHistoryModel.setDriverId(driverId);
			driverSubscriptionPackageHistoryModel.setVendorId(vendorId);
			driverSubscriptionPackageHistoryModel.setCurrentPackage(false);
			driverSubscriptionPackageHistoryModel.updateExistingPackagesAsNotCurrent();
		}

		SubscriptionPackageModel subscriptionPackageModel = SubscriptionPackageModel.getSubscriptionPackageDetailsById(subscriptionPackageId);

		driverSubscriptionPackageHistoryModel = new DriverSubscriptionPackageHistoryModel();
		driverSubscriptionPackageHistoryModel.setSubscriptionPackageId(subscriptionPackageModel.getSubscriptionPackageId());
		driverSubscriptionPackageHistoryModel.setDriverId(driverId);
		driverSubscriptionPackageHistoryModel.setVendorId(vendorId);
		driverSubscriptionPackageHistoryModel.setPackageName(subscriptionPackageModel.getPackageName());
		driverSubscriptionPackageHistoryModel.setDurationDays(subscriptionPackageModel.getDurationDays());
		driverSubscriptionPackageHistoryModel.setPrice(subscriptionPackageModel.getPrice());
		driverSubscriptionPackageHistoryModel.setCarTypeId(subscriptionPackageModel.getCarTypeId());

		long millisOfDay = ProjectConstants.ONE_DAY_MILLISECONDS_LONG * subscriptionPackageModel.getDurationDays();
		long packageEndTime = packageStartTime + millisOfDay;

		driverSubscriptionPackageHistoryModel.setPackageStartTime(packageStartTime);
		driverSubscriptionPackageHistoryModel.setPackageEndTime(packageEndTime);
		driverSubscriptionPackageHistoryModel.setCurrentPackage(isCurrentPackage);
		driverSubscriptionPackageHistoryModel.setPaymentMode(ProjectConstants.CASH_ID);

		String driverSubscriptionPackageHistoryId = driverSubscriptionPackageHistoryModel.addDriverSubscriptionPackageHistory(driverId);

		PaymentTypeModel pym = PaymentTypeModel.getPaymentTypeIdBy(paymentMode);

		DriverTransactionHistoryUtils.logDriverTransactionHistory(driverId, vendorId, pym.getPaymentTypeId(), driverSubscriptionPackageHistoryId, ProjectConstants.DRIVER_SUBSCRIPTION, true, subscriptionPackageModel.getPrice(), ProjectConstants.ADMIN_STATUS_SUCCESS, false,
					null);
	}

	public static String getDriverCurrentSubscription(String driverId, String vendorId, long time) {

		List<DriverSubscriptionPackageHistoryModel> driverSubscriptionPackageHistoryList = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryForStatus(driverId, vendorId);

		if (driverSubscriptionPackageHistoryList.size() > 0) {
			for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : driverSubscriptionPackageHistoryList) {
				if (driverSubscriptionPackageHistoryModel.getPackageStartTime() <= time && time <= driverSubscriptionPackageHistoryModel.getPackageEndTime()) {
					return driverSubscriptionPackageHistoryModel.getSubscriptionPackageId();
				}
			}
		}

		return null;
	}
	
	public static DriverSubscriptionPackageHistoryModel getDriverCurrentSubscriptionWithModel(String driverId, String vendorId, long time) {

		List<DriverSubscriptionPackageHistoryModel> driverSubscriptionPackageHistoryList = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryForStatus(driverId, vendorId);

		if (driverSubscriptionPackageHistoryList.size() > 0) {
			for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : driverSubscriptionPackageHistoryList) {
				if (driverSubscriptionPackageHistoryModel.getPackageStartTime() <= time && time <= driverSubscriptionPackageHistoryModel.getPackageEndTime()) {
					return driverSubscriptionPackageHistoryModel;
				}
			}
		}

		return null;
	}

	public static void updateDriverSubscriptionAgainstTour(String tourId, String driverId, String vendorId) {

		long tempTimeNow = DateUtils.nowAsGmtMillisec();
		String currentSubscriptionPackageId = getDriverCurrentSubscription(driverId, vendorId, tempTimeNow);

		TourModel tourModel = new TourModel();
		tourModel.setTourId(tourId);

		if (currentSubscriptionPackageId != null) {
			tourModel.setSubscriptionPackageId(currentSubscriptionPackageId);
		} else {
			tourModel.setSubscriptionPackageId("-1");
		}

		tourModel.updateDriverSubscriptionAgainstTour();
	}

	public static void processDriverSubscriptionExtension(String loggedInUserId, String multicityCityRegionId, String driverAll, List<String> driverList, double price, int durationDays) {

		logger.info("\n\n\t processDriverSubscriptionExtension\t");
		logger.info("\n\n\t multicityCityRegionId\t" + multicityCityRegionId);
		logger.info("\n\n\t driverAll\t" + driverAll);
		logger.info("\n\n\t driverList\t" + driverList);
		logger.info("\n\n\t price\t" + price);
		logger.info("\n\n\t durationDays\t" + durationDays);

		Runnable runnable = () -> {

			String paymentMode = ProjectConstants.CASH_ID;
			PaymentTypeModel pym = PaymentTypeModel.getPaymentTypeIdBy(ProjectConstants.PAYMENT_TYPE_CASH);
			String[] multicityCityRegionIds = new String[] { multicityCityRegionId };

			if (StringUtils.booleanValueOf(driverAll)) {

				driverList.clear();

				List<DriverInfoModel> driverInfoList = DriverInfoModel.getDriverListByMulticityRegionIds(UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, multicityCityRegionIds);

				for (DriverInfoModel driverInfoModel : driverInfoList) {
					driverList.add(driverInfoModel.getUserId());
				}
			}

			logger.info("\n\n\t driverList\t" + driverList.size());

			DriverSubscriptionExtensionLogsModel dselm = new DriverSubscriptionExtensionLogsModel();
			dselm.setStatus(ProjectConstants.IN_PROGRESS);
			dselm.setMulticityCityRegionId(multicityCityRegionId);
			dselm.setTotalDrivers(driverList.size());
			String driverSubscriptionExtensionLogId = dselm.insertDriverSubscriptionExtensionLogs(loggedInUserId);

			logger.info("\n\n\t driverSubscriptionExtensionLogId\t" + driverSubscriptionExtensionLogId);

			AtomicInteger totalCompletedDrivers = new AtomicInteger(0);
			AtomicInteger totalFailedDrivers = new AtomicInteger(0);

			int count = 0;

			for (String driverId : driverList) {

				count++;
				subscribePackageDriverSubscriptionExtension(driverId, price, durationDays, pym, paymentMode, driverSubscriptionExtensionLogId, totalCompletedDrivers, totalFailedDrivers);

				logger.info("\n\n\t count.get()\t" + count);
				logger.info("\n\n\t totalCompletedDrivers.get()\t" + totalCompletedDrivers.get());
				logger.info("\n\n\t totalFailedDrivers.get()\t" + totalFailedDrivers.get());

				if (count % 100 == 0) {

					dselm = new DriverSubscriptionExtensionLogsModel();
					dselm.setDriverSubscriptionExtensionLogId(driverSubscriptionExtensionLogId);
					dselm.setStatus(ProjectConstants.IN_PROGRESS);
					dselm.setTotalCompletedDrivers(totalCompletedDrivers.get());
					dselm.setTotalFailedDrivers(totalFailedDrivers.get());
					dselm.updateDriverSubscriptionExtensionLogs(loggedInUserId);
				}
			}

			logger.info("\n\n\t count.get()\t" + count);
			logger.info("\n\n\t totalCompletedDrivers.get()\t" + totalCompletedDrivers.get());
			logger.info("\n\n\t totalFailedDrivers.get()\t" + totalFailedDrivers.get());

			dselm = new DriverSubscriptionExtensionLogsModel();
			dselm.setDriverSubscriptionExtensionLogId(driverSubscriptionExtensionLogId);
			dselm.setStatus(ProjectConstants.COMPLETED);
			dselm.setTotalCompletedDrivers(totalCompletedDrivers.get());
			dselm.setTotalFailedDrivers(totalFailedDrivers.get());
			dselm.updateDriverSubscriptionExtensionLogs(loggedInUserId);
		};

		Thread t = new Thread(runnable);
		t.start();
	}

	public static void subscribePackageDriverSubscriptionExtension(String driverId, double price, int durationDays, PaymentTypeModel pym, String paymentMode, String driverSubscriptionExtensionLogId, AtomicInteger totalCompletedDrivers, AtomicInteger totalFailedDrivers) {

		String vendorId = MultiTenantUtils.getVendorIdByUserId(driverId);

		DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel = new DriverSubscriptionPackageHistoryModel();
		driverSubscriptionPackageHistoryModel.setDriverSubscriptionPackageHistoryId(null);
		driverSubscriptionPackageHistoryModel.setDriverId(driverId);
		driverSubscriptionPackageHistoryModel.setVendorId(vendorId);
		driverSubscriptionPackageHistoryModel.setCurrentPackage(false);
		driverSubscriptionPackageHistoryModel.updateExistingPackagesAsNotCurrent();

		driverSubscriptionPackageHistoryModel = DriverSubscriptionPackageHistoryModel.getLatestDriverSubscriptionPackageHistory(driverId);

		// if the driver has no active subscription then ignore the driver.
		if (driverSubscriptionPackageHistoryModel == null) {
			totalFailedDrivers.incrementAndGet();
			return;
		}

		boolean isCurrentPackage = true;
		long packageStartTime = 0;
		if (driverSubscriptionPackageHistoryModel.getPackageEndTime() < DateUtils.nowAsGmtMillisec()) {
			// current package has expired
			packageStartTime = DateUtils.nowAsGmtMillisec();
		} else {
			// current package is still active
			packageStartTime = driverSubscriptionPackageHistoryModel.getPackageEndTime();
		}

		SubscriptionPackageModel subscriptionPackageModel = SubscriptionPackageModel.getSubscriptionPackageDetailsById(driverSubscriptionPackageHistoryModel.getSubscriptionPackageId());

		driverSubscriptionPackageHistoryModel = new DriverSubscriptionPackageHistoryModel();
		driverSubscriptionPackageHistoryModel.setSubscriptionPackageId(subscriptionPackageModel.getSubscriptionPackageId());
		driverSubscriptionPackageHistoryModel.setDriverId(driverId);
		driverSubscriptionPackageHistoryModel.setVendorId(vendorId);
		driverSubscriptionPackageHistoryModel.setPackageName(subscriptionPackageModel.getPackageName());
		driverSubscriptionPackageHistoryModel.setDurationDays(durationDays);
		driverSubscriptionPackageHistoryModel.setPrice(price);
		driverSubscriptionPackageHistoryModel.setCarTypeId(subscriptionPackageModel.getCarTypeId());

		long millisOfDay = ProjectConstants.ONE_DAY_MILLISECONDS_LONG * durationDays;
		long packageEndTime = packageStartTime + millisOfDay;

		driverSubscriptionPackageHistoryModel.setPackageStartTime(packageStartTime);
		driverSubscriptionPackageHistoryModel.setPackageEndTime(packageEndTime);
		driverSubscriptionPackageHistoryModel.setCurrentPackage(isCurrentPackage);
		driverSubscriptionPackageHistoryModel.setPaymentMode(paymentMode);

		String driverSubscriptionPackageHistoryId = driverSubscriptionPackageHistoryModel.addDriverSubscriptionPackageHistory(driverId);

		DriverTransactionHistoryUtils.logDriverTransactionHistory(driverId, vendorId, pym.getPaymentTypeId(), driverSubscriptionPackageHistoryId, ProjectConstants.DRIVER_SUBSCRIPTION, true, price, ProjectConstants.ADMIN_STATUS_SUCCESS, true,
					driverSubscriptionExtensionLogId);

		totalCompletedDrivers.incrementAndGet();
	}
}