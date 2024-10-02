package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.webapp.ProjectConstants;
import com.webapp.models.ExportAccessModel;

public class UserExportUtils {

	public static boolean hasExportAccess(String roleId, String userId, String exportTypeId) {

		if (UserRoleUtils.isSuperAdminRole(roleId)) {
			return true;
		}

		ExportAccessModel exportAccessModel = ExportAccessModel.getExportAccessDetailsByUserId(userId);

		if (exportAccessModel == null) {
			return false;
		}

		boolean status = false;

		switch (exportTypeId) {
		case ProjectConstants.EXPORT_ACCESS_BOOKING_ID:
			status = exportAccessModel.isBookingExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_PASSENGER_ID:
			status = exportAccessModel.isPassengerExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_RIDE_LATER_ID:
			status = exportAccessModel.isRideLaterExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_CRITICAL_RIDE_LATER_ID:
			status = exportAccessModel.isCriticalRideLaterExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID:
			status = exportAccessModel.isDriverIncomeReportExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_REFUND_REPORT_ID:
			status = exportAccessModel.isRefundReportExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID:
			status = exportAccessModel.isDriverDutyReportExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_CCAVENUE_LOG_REPORT_ID:
			status = exportAccessModel.isCcavenueLogReportExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID:
			status = exportAccessModel.isDriverDriveReportExport();
			break;

		case ProjectConstants.EXPORT_ACCESS_DRIVER_BENEFIT_REPORT_ID:
			status = exportAccessModel.isDriverBenefitReportExport();
			break;

		case ProjectConstants.EXPORT_DRIVER_ACCOUNT_ID:
			status = exportAccessModel.isDriverAccountExport();
			break;

		case ProjectConstants.EXPORT_VENDOR_ACCOUNT_ID:
			status = exportAccessModel.isVendorAccountExport();
			break;

		case ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID:
			status = exportAccessModel.isVendorDriverAccountExport();
			break;

		case ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID:
			status = exportAccessModel.isDriverSubscriptionExport();
			break;

		case ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID:
			status = exportAccessModel.isDriverTransactionHistoryExport();
			break;
		case ProjectConstants.EXPORT_ACCESS_PHONEPE_LOG_REPORT_ID:
			status = exportAccessModel.isPhonepeLogReportExport();
			break;
		case ProjectConstants.EXPORT_ACCESS_WAREHOUSE_ID:
			status = exportAccessModel.isWarehouseExport();
			break;	

		default:
			status = false;
			break;
		}

		return status;
	}

	public static void processAdminUserExport(List<String> exportAccessList, String loggedInUserId, String userId) {

		ExportAccessModel previousEexportAccessDetails = ExportAccessModel.getExportAccessDetailsByUserId(userId);

		ExportAccessModel exportAccessModel = new ExportAccessModel();

		exportAccessModel.setUserId(userId);

		for (String exportAccess : exportAccessList) {

			if (ProjectConstants.EXPORT_ACCESS_BOOKING_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setBookingExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_PASSENGER_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setPassengerExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_RIDE_LATER_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setRideLaterExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_CRITICAL_RIDE_LATER_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setCriticalRideLaterExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverIncomeReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_REFUND_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setRefundReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverDutyReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_CCAVENUE_LOG_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setCcavenueLogReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverDriveReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_BENEFIT_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverBenefitReportExport(true);

			} else if (ProjectConstants.EXPORT_DRIVER_ACCOUNT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverAccountExport(true);

			} else if (ProjectConstants.EXPORT_VENDOR_ACCOUNT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setVendorAccountExport(true);

			} else if (ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setVendorDriverAccountExport(true);

			} else if (ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverSubscriptionExport(true);

			} else if (ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverTransactionHistoryExport(true);
			} else if (ProjectConstants.EXPORT_ACCESS_PHONEPE_LOG_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setPhonepeLogReportExport(true);
			} else if (ProjectConstants.EXPORT_ACCESS_WAREHOUSE_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setWarehouseExport(true);
			}
			
		}

		if (previousEexportAccessDetails == null) {
			exportAccessModel.addExportAccessDetails(loggedInUserId);
		} else {
			exportAccessModel.updateExportAccessDetails(loggedInUserId);
		}
	}

	public static List<String> setAdminExportUrlAccessList(String userId) {

		List<String> selectedIdList = new ArrayList<String>();

		ExportAccessModel exportAccessModel = ExportAccessModel.getExportAccessDetailsByUserId(userId);
		
		if (exportAccessModel == null) {
			return null;
		}

		if (exportAccessModel.isBookingExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_BOOKING_ID);
		}

		if (exportAccessModel.isPassengerExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_PASSENGER_ID);
		}

		if (exportAccessModel.isRideLaterExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_RIDE_LATER_ID);
		}

		if (exportAccessModel.isCriticalRideLaterExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_CRITICAL_RIDE_LATER_ID);
		}

		if (exportAccessModel.isDriverIncomeReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID);
		}

		if (exportAccessModel.isRefundReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_REFUND_REPORT_ID);
		}

		if (exportAccessModel.isDriverDutyReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID);
		}

		if (exportAccessModel.isCcavenueLogReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_CCAVENUE_LOG_REPORT_ID);
		}

		if (exportAccessModel.isDriverDriveReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);
		}

		if (exportAccessModel.isDriverBenefitReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_BENEFIT_REPORT_ID);
		}

		if (exportAccessModel.isDriverAccountExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_DRIVER_ACCOUNT_ID);
		}

		if (exportAccessModel.isVendorAccountExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_VENDOR_ACCOUNT_ID);
		}

		if (exportAccessModel.isVendorDriverAccountExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID);
		}

		if (exportAccessModel.isDriverSubscriptionExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID);
		}

		if (exportAccessModel.isDriverTransactionHistoryExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID);
		}
		
		if (exportAccessModel.isPhonepeLogReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_PHONEPE_LOG_REPORT_ID);
		}
		
		if (exportAccessModel.isWarehouseExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_WAREHOUSE_ID);
		}

		return selectedIdList;
	}

	public static void processVendorExportAccessList(List<String> exportAccessList, String userId, String loggedInUserId) {

		System.out.println("\n\n\n\n\texportAccessList\t" + exportAccessList);

		ExportAccessModel previousEexportAccessDetails = ExportAccessModel.getExportAccessDetailsByUserId(userId);

		ExportAccessModel exportAccessModel = new ExportAccessModel();

		exportAccessModel.setUserId(userId);

		for (String exportAccess : exportAccessList) {

			if (ProjectConstants.EXPORT_ACCESS_BOOKING_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setBookingExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverIncomeReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverDutyReportExport(true);

			} else if (ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverDriveReportExport(true);

			} else if (ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setVendorDriverAccountExport(true);
			} else if (ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverSubscriptionExport(true);
			} else if (ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID.equalsIgnoreCase(exportAccess)) {

				exportAccessModel.setDriverTransactionHistoryExport(true);
			}
		}

		if (previousEexportAccessDetails == null) {
			exportAccessModel.addExportAccessDetails(loggedInUserId);
		} else {
			exportAccessModel.updateExportAccessDetails(loggedInUserId);
		}
	}

	public static List<String> setVendorExportUrlAccessList(String userId) {

		List<String> selectedIdList = new ArrayList<String>();

		ExportAccessModel exportAccessModel = ExportAccessModel.getExportAccessDetailsByUserId(userId);

		if (exportAccessModel == null) {
			return selectedIdList;
		}

		if (exportAccessModel.isBookingExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_BOOKING_ID);
		}

		if (exportAccessModel.isDriverIncomeReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID);
		}

		if (exportAccessModel.isDriverDutyReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID);
		}

		if (exportAccessModel.isDriverDriveReportExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);
		}

		if (exportAccessModel.isVendorDriverAccountExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID);
		}

		if (exportAccessModel.isDriverSubscriptionExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID);
		}

		if (exportAccessModel.isDriverTransactionHistoryExport()) {
			selectedIdList.add(ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID);
		}

		return selectedIdList;
	}
}
