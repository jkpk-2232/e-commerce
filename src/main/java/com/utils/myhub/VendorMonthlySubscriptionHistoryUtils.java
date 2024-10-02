package com.utils.myhub;

import java.util.List;
import java.util.Map;

import com.jeeutils.DateUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorMonthlySubscriptionHistoryModel;

public class VendorMonthlySubscriptionHistoryUtils {

	public static void setInputParamForVendorMonthlySubscriptionRestrictingDriver(AdminSettingsModel adminSettingsModel, Map<String, Object> inputMap) {
		inputMap.put("isRestrictDriverVendorSubscriptionExpiry", adminSettingsModel.isRestrictDriverVendorSubscriptionExpiry() ? "yes" : null);
	}

	public static void setInputParamForVendorMonthlySubscriptionRestrictingDriver(Map<String, Object> inputMap) {
		inputMap.put("isRestrictDriverVendorSubscriptionExpiry", AdminSettingsModel.getAdminSettingsDetails().isRestrictDriverVendorSubscriptionExpiry() ? "yes" : null);
	}

	public static void setVendorMonthlySubscriptionStartEndTimeUiFieldsForAdd(Map<String, String> data, String timeZone) {

		String vendorCurrentSubscriptionStartDateTimeString = DateUtils.dbTimeStampToSesionDate(DateUtils.nowAsGmtMillisec(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put(FieldConstants.VENDOR_CURRENT_SUBSCRIPTION_START_DATE_TIME, vendorCurrentSubscriptionStartDateTimeString);

		String vendorCurrentSubscriptionEndDateTimeString = DateUtils.dbTimeStampToSesionDate(DateUtils.getXDaysInstantInFuture(ProjectConstants.VENDOR_SUBSCRIPTION_MONTHLY_DAYS).toEpochMilli(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put(FieldConstants.VENDOR_CURRENT_SUBSCRIPTION_END_DATE_TIME, vendorCurrentSubscriptionEndDateTimeString);
	}

	public static void setVendorMonthlySubscriptionStartEndTimeUiFieldsForEdit(Map<String, String> data, String timeZone, UserProfileModel userProfileModel) {

		data.put("vendorMonthlySubscriptionFee", BusinessApiAction.df.format(userProfileModel.getVendorMonthlySubscriptionFee()));

		String vendorCurrentSubscriptionStartDateTimeString = DateUtils.dbTimeStampToSesionDate(userProfileModel.getVendorCurrentSubscriptionStartDateTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put("vendorCurrentSubscriptionStartDateTime", vendorCurrentSubscriptionStartDateTimeString);

		String vendorCurrentSubscriptionEndDateTimeString = DateUtils.dbTimeStampToSesionDate(userProfileModel.getVendorCurrentSubscriptionEndDateTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put("vendorCurrentSubscriptionEndDateTime", vendorCurrentSubscriptionEndDateTimeString);
	}

	public static void setAddVendorMonthlySubscription(UserProfileModel userProfileModel, String vendorMonthlySubscriptionFee) {

		long vendorFreeSubscriptionStartDateTime = DateUtils.nowAsGmtMillisec();
		long vendorCurrentSubscriptionStartDateTime = vendorFreeSubscriptionStartDateTime;
		long vendorFreeSubscriptionEndDateTime = DateUtils.getXDaysInstantInFuture(ProjectConstants.VENDOR_SUBSCRIPTION_MONTHLY_DAYS).toEpochMilli();
		long vendorCurrentSubscriptionEndDateTime = vendorFreeSubscriptionEndDateTime;

		userProfileModel.setVendorMonthlySubscriptionFee(Double.parseDouble(vendorMonthlySubscriptionFee));
		userProfileModel.setVendorSubscriptionFreeActive(true);
		userProfileModel.setVendorSubscriptionCurrentActive(true);
		userProfileModel.setVendorFreeSubscriptionStartDateTime(vendorFreeSubscriptionStartDateTime);
		userProfileModel.setVendorFreeSubscriptionEndDateTime(vendorFreeSubscriptionEndDateTime);
		userProfileModel.setVendorCurrentSubscriptionStartDateTime(vendorCurrentSubscriptionStartDateTime);
		userProfileModel.setVendorCurrentSubscriptionEndDateTime(vendorCurrentSubscriptionEndDateTime);
	}

	public static void setEditVendorMonthlySubscription(UserProfileModel userProfileModel, String vendorMonthlySubscriptionFee) {

		userProfileModel.setVendorMonthlySubscriptionFee(Double.parseDouble(vendorMonthlySubscriptionFee));
	}

	public static void addFreeVendorMonthlySubscriptionEntryViaScript(UserProfileModel userProfileModel, String loggedInUserId) {

		VendorMonthlySubscriptionHistoryUtils.setAddVendorMonthlySubscription(userProfileModel, "0");
		userProfileModel.updateVendorMonthlySubscriptionScriptParametersExistingVendors();

		VendorMonthlySubscriptionHistoryModel vendorMonthlySubscriptionHistoryModel = new VendorMonthlySubscriptionHistoryModel();

		vendorMonthlySubscriptionHistoryModel.setVendorId(userProfileModel.getUserId());
		vendorMonthlySubscriptionHistoryModel.setVendorMonthlySubscriptionFee(0);
		vendorMonthlySubscriptionHistoryModel.setPaymentType(ProjectConstants.VENDOR_MONTHLY_SUBSCRIPTION_PAYMENT_TYPE.FREE);
		vendorMonthlySubscriptionHistoryModel.setTransactionId(null);
		vendorMonthlySubscriptionHistoryModel.setStartDateTime(userProfileModel.getVendorFreeSubscriptionStartDateTime());
		vendorMonthlySubscriptionHistoryModel.setEndDateTime(userProfileModel.getVendorFreeSubscriptionEndDateTime());
		vendorMonthlySubscriptionHistoryModel.setFreeSubscriptionEntry(true);
		vendorMonthlySubscriptionHistoryModel.setVendorSubscriptionCurrentActive(true);

		String vendorMonthlySubscriptionHistoryId = vendorMonthlySubscriptionHistoryModel.insertVendorMonthlySubscriptionHistory(loggedInUserId);

		userProfileModel.setVendorMonthlySubscriptionHistoryId(vendorMonthlySubscriptionHistoryId);
		userProfileModel.updateVendorMonthlySubscriptionHistoryId();
	}

	public static void addFreeVendorMonthlySubscriptionEntry(UserProfileModel userProfileModel, String loggedInUserId) {

		VendorMonthlySubscriptionHistoryModel vendorMonthlySubscriptionHistoryModel = new VendorMonthlySubscriptionHistoryModel();

		vendorMonthlySubscriptionHistoryModel.setVendorId(userProfileModel.getUserId());
		vendorMonthlySubscriptionHistoryModel.setVendorMonthlySubscriptionFee(0);
		vendorMonthlySubscriptionHistoryModel.setPaymentType(ProjectConstants.VENDOR_MONTHLY_SUBSCRIPTION_PAYMENT_TYPE.FREE);
		vendorMonthlySubscriptionHistoryModel.setTransactionId(null);
		vendorMonthlySubscriptionHistoryModel.setStartDateTime(userProfileModel.getVendorFreeSubscriptionStartDateTime());
		vendorMonthlySubscriptionHistoryModel.setEndDateTime(userProfileModel.getVendorFreeSubscriptionEndDateTime());
		vendorMonthlySubscriptionHistoryModel.setFreeSubscriptionEntry(true);
		vendorMonthlySubscriptionHistoryModel.setVendorSubscriptionCurrentActive(true);

		String vendorMonthlySubscriptionHistoryId = vendorMonthlySubscriptionHistoryModel.insertVendorMonthlySubscriptionHistory(loggedInUserId);

		userProfileModel.setVendorMonthlySubscriptionHistoryId(vendorMonthlySubscriptionHistoryId);
		userProfileModel.updateVendorMonthlySubscriptionHistoryId();
	}

	public static void addPaidVendorMonthlySubscriptionEntry(UserProfileModel userProfileModel, String loggedInUserId, String paymentType, String transactionId) {

		VendorMonthlySubscriptionHistoryModel lastVendorMonthlySubscriptionHistoryEntry = VendorMonthlySubscriptionHistoryModel.getLastVendorMonthlySubscriptionHistoryEntry(userProfileModel.getUserId());

		if (lastVendorMonthlySubscriptionHistoryEntry != null) {
			lastVendorMonthlySubscriptionHistoryEntry.setVendorSubscriptionCurrentActive(false);
			lastVendorMonthlySubscriptionHistoryEntry.updateVendorSubscriptionCurrentActive(loggedInUserId);
		}

		long startDateTime = DateUtils.nowAsGmtMillisec();
		long endDateTime = DateUtils.getXDaysInstantInFuture(ProjectConstants.VENDOR_SUBSCRIPTION_MONTHLY_DAYS).toEpochMilli();

		VendorMonthlySubscriptionHistoryModel vendorMonthlySubscriptionHistoryModel = new VendorMonthlySubscriptionHistoryModel();

		vendorMonthlySubscriptionHistoryModel.setVendorId(userProfileModel.getUserId());
		vendorMonthlySubscriptionHistoryModel.setVendorMonthlySubscriptionFee(userProfileModel.getVendorMonthlySubscriptionFee());
		vendorMonthlySubscriptionHistoryModel.setPaymentType(paymentType);
		vendorMonthlySubscriptionHistoryModel.setTransactionId(transactionId);
		vendorMonthlySubscriptionHistoryModel.setStartDateTime(startDateTime);
		vendorMonthlySubscriptionHistoryModel.setEndDateTime(endDateTime);
		vendorMonthlySubscriptionHistoryModel.setFreeSubscriptionEntry(false);
		vendorMonthlySubscriptionHistoryModel.setVendorSubscriptionCurrentActive(true);

		String vendorMonthlySubscriptionHistoryId = vendorMonthlySubscriptionHistoryModel.insertVendorMonthlySubscriptionHistory(loggedInUserId);

		userProfileModel.setVendorSubscriptionFreeActive(false);
		userProfileModel.setVendorSubscriptionCurrentActive(true);
		userProfileModel.setVendorCurrentSubscriptionStartDateTime(startDateTime);
		userProfileModel.setVendorCurrentSubscriptionEndDateTime(endDateTime);
		userProfileModel.setVendorMonthlySubscriptionHistoryId(vendorMonthlySubscriptionHistoryId);
		userProfileModel.setVendorSubscriptionMarkedExpiredByCronJob(false);
		userProfileModel.setVendorSubscriptionMarkedExpiredByCronJobTiming(0);

		userProfileModel.updateVendorMonthlySubscriptionParameters();
	}

	public static void takeExpiryAction(List<UserProfileModel> updateAccountToExpiredList, List<VendorMonthlySubscriptionHistoryModel> updateVendorMonthlySubscriptionToExpiredList) {

		UserProfileModel.updateVendorSubscriptionAccountExpired(updateAccountToExpiredList);

		sendSubscriptionExpiredNotification(updateAccountToExpiredList);

		updateAccountToExpiredList.clear();

		VendorMonthlySubscriptionHistoryModel.updateVendorSubscriptionAccountExpired(updateVendorMonthlySubscriptionToExpiredList);
		updateVendorMonthlySubscriptionToExpiredList.clear();
	}

	public static void sendSubscriptionExpiredNotification(List<UserProfileModel> updateAccountToExpiredList) {

		String notificationMessage = BusinessAction.messageForKeyAdmin("notificationVendorMonthlySusbcriptionExpired", null);

		for (UserProfileModel userProfileModel : updateAccountToExpiredList) {
			MyHubNotificationUtils.sendPushNotificationToUser(userProfileModel.getUserId(), ProjectConstants.NOTIFICATION_TYPE_VENDOR_MONTHLY_SUBSCRIPTION_EXPIRED, notificationMessage);
		}
	}
}
