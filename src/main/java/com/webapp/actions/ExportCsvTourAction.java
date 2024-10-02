package com.webapp.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONException;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.opencsv.CSVWriter;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CcavenueResponseLogModel;
import com.webapp.models.DriverDutyLogsModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverLoggedInTimeModel;
import com.webapp.models.DriverReferralCodeLogModel;
import com.webapp.models.DriverSubscriptionPackageHistoryModel;
import com.webapp.models.DriverTransactionHistoryModel;
import com.webapp.models.EncashRequestsModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.PhonepePaymentModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourReferrerBenefitModel;
import com.webapp.models.UserAccountLogsModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

@Path("/export")
public class ExportCsvTourAction extends BusinessAction {

	@Path("/manage-driver-reports")
	@GET
	//@formatter:off
	public void manageDriverReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_reports_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelSrNo"));
		columnNames.add(messageForKeyAdmin("labelDriverName"));
		columnNames.add(messageForKeyAdmin("labelPhoneNumber"));
		columnNames.add(messageForKeyAdmin("labelEmailAddress"));
		columnNames.add(messageForKeyAdmin("labelTotalFare"));
		columnNames.add(messageForKeyAdmin("labelTotalAmountCollected"));
		columnNames.add(messageForKeyAdmin("labelTotalDriverIncome"));
		columnNames.add(messageForKeyAdmin("labelMarkupIncome"));
		columnNames.add(messageForKeyAdmin("labelTotalPromoDiscount"));
		columnNames.add(messageForKeyAdmin("labelTotalTaxAmount"));
		columnNames.add(messageForKeyAdmin("labelAdminSettlement"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		long totalTourCount = 0;
		long totalCancelledTrip = 0;
		double amount = 0.0d;
		double invoiceAmount = 0.0d;

		double totalAmountCollected = 0.0d;
		double totalDriverIncome = 0.0d;
		double totalPromoDiscount = 0.0d;
		double totalTaxAmount = 0.0d;

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
		String timeZone = TimeZoneUtils.getTimeZone();

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Admin name");
		columnHeaderNames.add("Total tour count");
		columnHeaderNames.add("Total Fare");
		columnHeaderNames.add("Total Amount Collected");
		columnHeaderNames.add("Total Driver Income");
		columnHeaderNames.add("Total Promo Discount");
		columnHeaderNames.add("Total Tax Amount");
		columnHeaderNames.add("Total Admin Settlement Amount");
		columnHeaderNames.add("Start date");
		columnHeaderNames.add("End date");
		createRowWithData(sheet, columnHeaderNames, 0);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		String vendorId = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<UserModel> userModelList = UserModel.getDriverReportAdminListForSearch(0, 0, "DESC", UserRoles.DRIVER_ROLE_ID, "%%", startDateLong, endDateLong, assignedRegionList, vendorId);

		ArrayList<String> userIds = new ArrayList<String>();
		List<TourModel> tourList = new ArrayList<TourModel>();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (StringUtils.validString(searchString)) {

				for (UserModel userModel : userModelList) {
					userIds.add(userModel.getUserId());
				}

				if (userIds.size() > 0) {
					tourList = TourModel.getVendorDriverReportTourListByDateByUserIds(startDateLong, endDateLong, userIds, loginSessionMap.get(LoginUtils.USER_ID));
				} else {
					tourList = TourModel.getVendorDriverReportTourListByDate(startDateLong, endDateLong, loginSessionMap.get(LoginUtils.USER_ID));
				}

			} else {
				tourList = TourModel.getVendorDriverReportTourListByDate(startDateLong, endDateLong, loginSessionMap.get(LoginUtils.USER_ID));
			}

		} else if (StringUtils.validString(searchString)) {
			for (UserModel userModel : userModelList) {
				userIds.add(userModel.getUserId());
			}

			if (userIds.size() > 0) {
				tourList = TourModel.getDriverReportTourListByDateByUserIds(startDateLong, endDateLong, userIds);
			} else {
				tourList = TourModel.getDriverReportTourListByDate(startDateLong, endDateLong);
			}

		} else {
			tourList = TourModel.getDriverReportTourListByDate(startDateLong, endDateLong);
		}

		int i = 4;
		int counter = 1;
		double totalAdminSettlementAmount = 0;

		for (UserModel user : userModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(user.getFirstName(), user.getLastName()));
			row.createCell(2).setCellValue(MyHubUtils.formatPhoneNumber(user.getPhoneNoCode(), user.getPhoneNo()));
			row.createCell(3).setCellValue(user.getEmail());
			row.createCell(4).setCellValue(StringUtils.valueOf(user.getInvoiceTotal()));
			row.createCell(5).setCellValue(StringUtils.valueOf(user.getDriverReceivableAmount()));
			row.createCell(6).setCellValue(StringUtils.valueOf(user.getDriverAmountTotal()));
			row.createCell(7).setCellValue(StringUtils.valueOf(user.getMarkupFare()));
			row.createCell(8).setCellValue(StringUtils.valueOf(user.getPromoDiscount()));
			row.createCell(9).setCellValue(StringUtils.valueOf(user.getTaxAmount()));
			row.createCell(10).setCellValue(MyHubUtils.getDriverSettlement(adminSettings, user.getAdminSettlementAmount()));

			amount = amount + Double.parseDouble(StringUtils.valueOf(user.getDriverAmountTotal()));
			invoiceAmount = invoiceAmount + Double.parseDouble(StringUtils.valueOf(user.getInvoiceTotal()));

			totalAdminSettlementAmount += user.getAdminSettlementAmount();

			totalAmountCollected += user.getDriverReceivableAmount();
			totalDriverIncome += user.getDriverAmountTotal();
			totalPromoDiscount += user.getPromoDiscount();
			totalTaxAmount += user.getTaxAmount();

			i++;
			counter++;
		}

		totalTourCount = tourList.size();

		for (TourModel tour : tourList) {
			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
				totalCancelledTrip = totalCancelledTrip + 1;
			}
		}

		UserModel userDetails = UserModel.getUserActiveDeativeDetailsById(loginSessionMap.get(LoginUtils.USER_ID));

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(MyHubUtils.formatFullName(userDetails.getFirstName(), userDetails.getLastName()));
		columnHeaderValues.add(StringUtils.valueOf(totalTourCount));
		columnHeaderValues.add(StringUtils.valueOf(invoiceAmount));
		columnHeaderValues.add(StringUtils.valueOf(totalAmountCollected));
		columnHeaderValues.add(StringUtils.valueOf(totalDriverIncome));
		columnHeaderValues.add(StringUtils.valueOf(totalPromoDiscount));
		columnHeaderValues.add(StringUtils.valueOf(totalTaxAmount));
		columnHeaderValues.add(MyHubUtils.getDriverSettlement(adminSettings, totalAdminSettlementAmount));
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver-bookings")
	@GET
	//@formatter:off
	public void getDriverBookings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId, 
		@QueryParam("startDate") String startDate, 
		@QueryParam("endDate") String endDate,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Tour Id");
		columnNames.add("Trip Request Time");
		columnNames.add("Pick Up Location");
		columnNames.add("Drop Off Location");
		columnNames.add("Customer Name");
		columnNames.add("Customer Phone");
		columnNames.add("Distance");
		columnNames.add(messageForKeyAdmin("labelTotalFare"));
		columnNames.add(messageForKeyAdmin("labelPromotion"));
		columnNames.add(messageForKeyAdmin("labelTotalCharges"));
		columnNames.add("Tax Amount");
		columnNames.add(messageForKeyAdmin("labelCredits"));
		columnNames.add(messageForKeyAdmin("labelAmountCollected"));
		columnNames.add("Driver Income");
		columnNames.add("Status");
		columnNames.add("Rating");
		columnNames.add("Payment Mode");
		columnNames.add("Admin Settlement");
		columnNames.add("Payment Status");
		columnNames.add("Refund Status");
		columnNames.add("Booking Type");
		columnNames.add("Markup Income");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Driver Name");
		columnHeaderNames.add("Total Tour Count");
		columnHeaderNames.add("Total Invoice Amount");
		columnHeaderNames.add("Total Driver Amount");
		columnHeaderNames.add("Amount Collected");
		columnHeaderNames.add("Total Admin Settlement Amount");
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		createRowWithData(sheet, columnHeaderNames, 0);

		long totalTourCount = 0;
		long totalCancelledTrip = 0;
		double amount = 0.0d;
		double invoiceAmount = 0.0d;
		double cashCollected = 0.0d;

		AdminSettingsModel adminSettingModel = AdminSettingsModel.getAdminSettingsDetails();
		String timeZone = TimeZoneUtils.getTimeZone();

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		List<TourModel> tourList = TourModel.getDriverToursBySpecificDate(driverId, startDateLong, endDateLong, searchString, assignedRegionList);

		boolean isVendorRole = UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID));

		int i = 4;
		totalTourCount = tourList.size();

		for (TourModel tour : tourList) {

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
				totalCancelledTrip = totalCancelledTrip + 1;
			}

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(tour.getUserTourId());
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(tour.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(2).setCellValue(tour.getSourceAddress());
			row.createCell(3).setCellValue(tour.getDestinationAddress());

			row.createCell(4).setCellValue(MyHubUtils.formatFullName(tour.getpFirstName(), tour.getpLastName()));

			if (isVendorRole) {
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			} else {
				row.createCell(5).setCellValue(MyHubUtils.formatPhoneNumber(tour.getpPhoneCode(), tour.getpPhone()));
			}

			if (tour.getInvoiceId() != null) {

				if ("0".equalsIgnoreCase(StringUtils.valueOf(tour.getDistance()))) {
					row.createCell(6).setCellValue("Unavailable");
				} else {
					row.createCell(6).setCellValue(StringUtils.valueOf(tour.getDistance() / adminSettingModel.getDistanceUnits()) + " " + adminSettingModel.getDistanceType());
				}

				// total fare
				row.createCell(7).setCellValue(tour.getCurrencySymbol() + StringUtils.valueOf(tour.getTotal()));

				// Promotion
				row.createCell(8).setCellValue(tour.getCurrencySymbol() + StringUtils.valueOf(tour.getPromoDiscount()));

				// total
				row.createCell(9).setCellValue(tour.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));

				// Tax amount
				row.createCell(10).setCellValue(tour.getCurrencySymbol() + StringUtils.valueOf(tour.getTotalTaxAmount()));

				// Used credits
				row.createCell(11).setCellValue(tour.getCurrencySymbol() + StringUtils.valueOf(tour.getUsedCredits()));

				// Driver amount collected
				row.createCell(12).setCellValue(adminSettingModel.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));

				// Driver amount
				row.createCell(13).setCellValue(StringUtils.valueOf(tour.getDriverAmount()));

				if (tour.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {

					row.createCell(16).setCellValue(ProjectConstants.C_CASH);

					if (tour.isCashNotReceived()) {
						row.createCell(18).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_NOT_COLLECTED);
					} else {

						cashCollected = cashCollected + tour.getFinalAmountCollected();

						row.createCell(18).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_COLLECTED + ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettingModel.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
					}
				} else {

					row.createCell(16).setCellValue(ProjectConstants.C_CARD);
					row.createCell(18).setCellValue(ProjectConstants.CARD_INVOICE_MESSAGE + " " + tour.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
				}

				row.createCell(17).setCellValue(MyHubUtils.getDriverSettlement(adminSettingModel, tour.getAdminSettlementAmount()));

				if (tour.isRefunded()) {
					row.createCell(19).setCellValue("Amount Refunded");
				} else {
					row.createCell(19).setCellValue("Amount Not Refunded");
				}

				invoiceAmount = invoiceAmount + Double.parseDouble(StringUtils.valueOf(tour.getDriverAmount()));

				amount = amount + Double.parseDouble(StringUtils.valueOf(tour.getFinalAmountCollected()));
				row.createCell(21).setCellValue(StringUtils.valueOf(tour.getMarkupFare()));
			} else {
				row.createCell(6).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(7).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(8).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(9).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(10).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(11).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(12).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(13).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(16).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(17).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(18).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(19).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(14).setCellValue(tour.getStatus());
			row.createCell(15).setCellValue(tour.getPassengerRating());

			if (tour.isRentalBooking()) {
				row.createCell(20).setCellValue(messageForKeyAdmin("labelRental"));
			} else {
				row.createCell(20).setCellValue(messageForKeyAdmin("labelTaxi"));
			}

			i++;
		}

		UserModel userDetails = UserModel.getUserActiveDeativeDetailsById(driverId);
		double totalAdminSettlementAmount = InvoiceModel.getTotalAdminSettlementAmount(startDateLong, endDateLong, driverId, assignedRegionList);

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(MyHubUtils.formatFullName(userDetails.getFirstName(), userDetails.getLastName()));
		columnHeaderValues.add(StringUtils.valueOf(totalTourCount));
		columnHeaderValues.add(StringUtils.valueOf(amount));
		columnHeaderValues.add(StringUtils.valueOf(invoiceAmount));
		columnHeaderValues.add(StringUtils.valueOf(cashCollected));
		columnHeaderValues.add(MyHubUtils.getDriverSettlement(adminSettingModel, totalAdminSettlementAmount));
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);

		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/manage-refund-reports")
	@GET
	//@formatter:off
	public void getRefundReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate, 
		@QueryParam("endDate") String endDate,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("refund_reports_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Tour Id");
		columnNames.add("Trip Request Time");
		columnNames.add("Pick Up Location");
		columnNames.add("Drop Off Location");
		columnNames.add("Customer Name");
		columnNames.add("Driver Name");
		columnNames.add("Booking Type");
		columnNames.add("Payment Mode");
		columnNames.add("Invoice Date");
		columnNames.add("Invoice Amount");
		columnNames.add("Refund Date");
		columnNames.add("Refund Amount");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		columnHeaderNames.add("Total Refunded Amount");
		createRowWithData(sheet, columnHeaderNames, 0);

		double cashCollected = 0.0d;

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
		String timeZone = TimeZoneUtils.getTimeZone();

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<InvoiceModel> invoiceList = InvoiceModel.getRefundedIvoiceListBySearchAndDateReports(0, 0, null, null, "%%", startDateLong, endDateLong, assignedRegionList);

		int i = 4;

		for (InvoiceModel invoice : invoiceList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(invoice.getUserTourId());
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(invoice.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(2).setCellValue(invoice.getSourceAddess());
			row.createCell(3).setCellValue(invoice.getDestiAddess());
			row.createCell(4).setCellValue(invoice.getPassengerFullName() != null ? invoice.getPassengerFullName() : ProjectConstants.NOT_AVAILABLE);
			row.createCell(5).setCellValue(invoice.getDriverFullName() != null ? invoice.getDriverFullName() : ProjectConstants.NOT_AVAILABLE);
			row.createCell(6).setCellValue(invoice.getBookingType());

			if (invoice.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {
				row.createCell(7).setCellValue(ProjectConstants.C_CASH);
			} else {
				row.createCell(7).setCellValue(ProjectConstants.C_CARD);
			}

			row.createCell(8).setCellValue(DateUtils.dbTimeStampToSesionDate(invoice.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(9).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(invoice.getCharges()));
			row.createCell(10).setCellValue(DateUtils.dbTimeStampToSesionDate(invoice.getUpdatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(11).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(invoice.getRefundAmount()));

			cashCollected += invoice.getRefundAmount();

			i++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		columnHeaderValues.add(StringUtils.valueOf(cashCollected));
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver-duty-detail-reports")
	@GET
	//@formatter:off
	public void manageDriverDutyDetailsReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("driverId") String driverId,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("onOffStatusId") String onOffStatusId,
		@QueryParam("logsDate") String logsDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_duty_details_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Driver Name");

		if (onOffStatusId.equals("1")) {
			columnNames.add("ON Status");
		} else if (onOffStatusId.equals("2")) {
			columnNames.add("OFF Status");
		} else {
			columnNames.add("ON/OFF Status");
		}

		if (onOffStatusId.equals("1")) {
			columnNames.add("ON Time");
		} else if (onOffStatusId.equals("2")) {
			columnNames.add("OFF Time");
		} else {
			columnNames.add("ON/OFF Time");
		}

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Admin name");
		columnHeaderNames.add("Driver name");
		columnHeaderNames.add("Driver email");
		columnHeaderNames.add("Driver phone no");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<DriverDutyLogsModel> driverDutyLogsModelList = new ArrayList<DriverDutyLogsModel>();
		UserModel driverUserModel = UserModel.getUserAccountDetailsById(driverId);

		if (onOffStatusId.equals("1")) {
			driverDutyLogsModelList = DriverDutyLogsModel.getDriverOnOffDutyReportAdminListForSearch(driverId, 0, 100000, "DESC", startDateLong, endDateLong, true);
		} else if (onOffStatusId.equals("2")) {
			driverDutyLogsModelList = DriverDutyLogsModel.getDriverOnOffDutyReportAdminListForSearch(driverId, 0, 100000, "DESC", startDateLong, endDateLong, false);
		} else {
			driverDutyLogsModelList = DriverDutyLogsModel.getDriverDutyReportAdminListForSearch(driverId, 0, 100000, "DESC", startDateLong, endDateLong);
		}

		int i = 4;
		int srNo = 1;

		for (DriverDutyLogsModel driverDutyLogsModel : driverDutyLogsModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(srNo);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(driverUserModel.getFirstName(), driverUserModel.getLastName()));

			if (driverDutyLogsModel.isDutyStatus()) {
				row.createCell(2).setCellValue(ProjectConstants.ON_DUTY);
			} else {
				row.createCell(2).setCellValue(ProjectConstants.OFF_DUTY);
			}

			row.createCell(3).setCellValue(DateUtils.dbTimeStampToSesionDate(driverDutyLogsModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			i++;
			srNo++;
		}

		UserModel userDetails = UserModel.getUserActiveDeativeDetailsById(loginSessionMap.get(LoginUtils.USER_ID));

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(MyHubUtils.formatFullName(userDetails.getFirstName(), userDetails.getLastName()));
		columnHeaderValues.add(MyHubUtils.formatFullName(driverUserModel.getFirstName(), driverUserModel.getLastName()));
		columnHeaderValues.add(driverUserModel.getEmail());
		columnHeaderValues.add(MyHubUtils.formatPhoneNumber(driverUserModel.getPhoneNoCode(), driverUserModel.getPhoneNo()));
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/passenger-bookings")
	@GET
	//@formatter:off
	public void getPassengerBookings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("passengerId") String passengerId, 
		@QueryParam("startDate") String startDate, 
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("customer_bookings_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Tour Id");
		columnNames.add("Trip Request Time");
		columnNames.add("Pick Up Location");
		columnNames.add("Drop Off Location");
		columnNames.add("Driver Name");
		columnNames.add("Driver Phone");
		columnNames.add("Booking Fees");
		columnNames.add("Distance");
		columnNames.add(messageForKeyAdmin("labelTotalFare"));
		columnNames.add("Promo Code");
		columnNames.add("Promocode type");
		columnNames.add("Promocode value");
		columnNames.add("Promocode user type");
		columnNames.add("Promocode usage type");
		columnNames.add("Promocode usage count");
		columnNames.add(messageForKeyAdmin("labelPromotion"));
		columnNames.add(messageForKeyAdmin("labelTollCharges"));
		columnNames.add(messageForKeyAdmin("labelTotalCharges"));
		columnNames.add("Credits adjustment");
		columnNames.add(messageForKeyAdmin("labelAmountCollected"));
		columnNames.add("Driver Income");
		columnNames.add("Driver Percentage");
		columnNames.add("Status");
		columnNames.add(messageForKeyAdmin("labelPassengerRatings"));
		columnNames.add(messageForKeyAdmin("labelPassengerComments"));
		columnNames.add(messageForKeyAdmin("labelDriverRating"));
		columnNames.add(messageForKeyAdmin("labelDriverComments"));
		columnNames.add("Payment Mode");
		columnNames.add("Payment Status");
		columnNames.add("Refund Status");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Passenger Name");
		columnHeaderNames.add("Total Tour Count");
		columnHeaderNames.add("Total Cancelled Tour");
		columnHeaderNames.add("Total Charges");
		columnHeaderNames.add("Total Final Amount");
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		columnHeaderNames.add("Total Cash Collected");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		long totalTourCount = 0;
		long totalCancelledTrip = 0;
		double amount = 0.0d;
		double invoiceAmount = 0.0d;
		double cashCollected = 0.0d;

		long tourStartDate = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long tourEndDate = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<TourModel> tourList = TourModel.getPassengerToursBySpecificDate(passengerId, tourStartDate, tourEndDate);

		int i = 4;

		totalTourCount = tourList.size();

		for (TourModel tour : tourList) {

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
				totalCancelledTrip = totalCancelledTrip + 1;
			}

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(tour.getUserTourId());
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(tour.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(2).setCellValue(tour.getSourceAddress());
			row.createCell(3).setCellValue(tour.getDestinationAddress());

			if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
				row.createCell(4).setCellValue(MyHubUtils.formatFullName(tour.getFirstName(), tour.getLastName()));
				row.createCell(5).setCellValue(tour.getPhoneNo());
			} else {
				row.createCell(4).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (tour.getInvoiceId() != null) {

				df.setRoundingMode(RoundingMode.DOWN);

				if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
					row.createCell(17).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFine()));
				} else {
					row.createCell(17).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
					amount = amount + Double.parseDouble(StringUtils.valueOf(tour.getCharges()));
				}

				row.createCell(6).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getBookingFees()));

				if ("0".equalsIgnoreCase(StringUtils.valueOf(tour.getDistance()))) {
					row.createCell(7).setCellValue("Unavailable");
				} else {
					row.createCell(7).setCellValue(StringUtils.valueOf(MyHubUtils.getKilometerFromMeters(tour.getDistance())) + " " + adminSettings.getDistanceType());
				}

				row.createCell(8).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getTotal()));

				if (tour.getPromoCodeId() != null && !tour.getPromoCodeId().equalsIgnoreCase("")) {

					PromoCodeModel promocodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tour.getPromoCodeId());

					if (promocodeModel != null) {

						row.createCell(9).setCellValue(promocodeModel.getPromoCode());

						if (promocodeModel.getMode().equals(ProjectConstants.PERCENTAGE_ID)) {
							row.createCell(10).setCellValue(ProjectConstants.PERCENTAGE_TEXT);
							row.createCell(11).setCellValue(promocodeModel.getDiscount() + "%");
						} else {
							row.createCell(10).setCellValue(ProjectConstants.AMOUNT_TEXT);
							row.createCell(11).setCellValue(promocodeModel.getDiscount());
						}

						if (promocodeModel.getUsageType().equalsIgnoreCase(ProjectConstants.ALL_ID)) {
							row.createCell(12).setCellValue(ProjectConstants.ALL_TEXT);
						} else {
							row.createCell(12).setCellValue(ProjectConstants.INDIVIDUAL_TEXT);
						}

						if (promocodeModel.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

							row.createCell(13).setCellValue(ProjectConstants.UNLIMITED_TEXT);
							row.createCell(14).setCellValue(ProjectConstants.UNLIMITED_TEXT);

						} else {
							row.createCell(13).setCellValue(ProjectConstants.LIMITED_TEXT);
							row.createCell(14).setCellValue(promocodeModel.getUsageCount());
						}

						row.createCell(15).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getPromoDiscount()));
					}

				} else {
					row.createCell(9).setCellValue("-");
					row.createCell(10).setCellValue("-");
					row.createCell(11).setCellValue("-");
					row.createCell(12).setCellValue("-");
					row.createCell(13).setCellValue("-");
					row.createCell(14).setCellValue("-");
					row.createCell(15).setCellValue("-");
				}

				row.createCell(16).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getTollAmount()));

				if (tour.getUsedCredits() < 0) {

					String usedCredits = StringUtils.valueOf(tour.getUsedCredits()) + "";
					usedCredits = usedCredits.replace("-", "-" + adminSettings.getCurrencySymbol());
					row.createCell(18).setCellValue(usedCredits);

				} else {

					row.createCell(18).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getUsedCredits()));

				}

				row.createCell(19).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));
				row.createCell(20).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getDriverAmount()));
				row.createCell(21).setCellValue(tour.getPercentage());

				if (tour.getPaymentMode().equalsIgnoreCase(ProjectConstants.CASH)) {

					row.createCell(27).setCellValue(ProjectConstants.C_CASH);

					if (tour.isCashNotReceived()) {
						row.createCell(28).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_NOT_COLLECTED);
					} else {
						row.createCell(28).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_COLLECTED + ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
					}
				} else {

					row.createCell(27).setCellValue(ProjectConstants.C_CARD);

					cashCollected = cashCollected + tour.getCashCollected();

					row.createCell(28).setCellValue(ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
				}

				if (tour.isRefunded()) {
					row.createCell(29).setCellValue("Amount Refunded");
				} else {
					row.createCell(29).setCellValue("Amount Not Refunded");
				}

				row.createCell(23).setCellValue(tour.getPassengerRating());
				row.createCell(24).setCellValue(tour.getPassengerComment());
				row.createCell(25).setCellValue(tour.getDriverRating());
				row.createCell(26).setCellValue(tour.getDriverComment());

			} else {

				row.createCell(6).setCellValue("-");
				row.createCell(7).setCellValue("-");
				row.createCell(8).setCellValue("-");
				row.createCell(9).setCellValue("-");
				row.createCell(10).setCellValue("-");
				row.createCell(11).setCellValue("-");
				row.createCell(12).setCellValue("-");

				row.createCell(13).setCellValue("-");
				row.createCell(14).setCellValue("-");
				row.createCell(15).setCellValue("-");

				row.createCell(17).setCellValue("-");
				row.createCell(18).setCellValue("-");
				row.createCell(19).setCellValue("-");
				row.createCell(20).setCellValue("-");
			}

			invoiceAmount = invoiceAmount + Double.parseDouble(StringUtils.valueOf(tour.getFinalAmountCollected()));

			if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
				row.createCell(22).setCellValue(messageForKeyAdmin("labelCancelledByPassenger"));
			} else if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
				row.createCell(22).setCellValue(messageForKeyAdmin("labelCancelledByPassenger"));
			} else {
				row.createCell(22).setCellValue(tour.getStatus());
			}

			i++;
		}

		UserModel userDetails = UserModel.getUserActiveDeativeDetailsById(passengerId);

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(MyHubUtils.formatFullName(userDetails.getFirstName(), userDetails.getLastName()));
		columnHeaderValues.add(StringUtils.valueOf(totalTourCount));
		columnHeaderValues.add(StringUtils.valueOf(totalCancelledTrip));
		columnHeaderValues.add(StringUtils.valueOf(amount));
		columnHeaderValues.add(StringUtils.valueOf(invoiceAmount));
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		columnHeaderValues.add(StringUtils.valueOf(cashCollected));
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/ccavenue-log-reports")
	@GET
	//@formatter:off
	public void manageCcavenueLogReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("ccavenue_logs_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Payment Request Type");
		columnNames.add("Event Id");
		columnNames.add("User Id");
		columnNames.add("Tracking Id");
		columnNames.add("Order Status");
		columnNames.add("Failure Message");
		columnNames.add("Amount");
		columnNames.add("Currency");
		columnNames.add("Payment Mode");
		columnNames.add("Card Name");
		columnNames.add("Bank Ref No");
		columnNames.add("Bank Response Code");
		columnNames.add("Date");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<CcavenueResponseLogModel> ccavenueResponseLogModelList = CcavenueResponseLogModel.getCcavenueLogsReport("%%", startDatelong, endDatelong, assignedRegionList);

		int i = 1;
		int counter = 1;

		for (CcavenueResponseLogModel ccavenueResponseLogModel : ccavenueResponseLogModelList) {

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(counter);

			if (ccavenueResponseLogModel.getPaymentRequestType().equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_TOUR_ID)) {

				row.createCell(1).setCellValue(ProjectConstants.TOURS);

				if (ccavenueResponseLogModel.getUserTourId() != null && !ccavenueResponseLogModel.getUserTourId().isEmpty()) {
					row.createCell(2).setCellValue(ccavenueResponseLogModel.getUserTourId());
				} else {
					row.createCell(2).setCellValue(ProjectConstants.NOT_AVAILABLE);
				}

			} else {
				row.createCell(1).setCellValue(ProjectConstants.DRIVER_SUBSCRIPTION);

				if (ccavenueResponseLogModel.getShortSubscriptionId() != null && !ccavenueResponseLogModel.getShortSubscriptionId().isEmpty()) {
					row.createCell(2).setCellValue(ccavenueResponseLogModel.getShortSubscriptionId());
				} else {
					row.createCell(2).setCellValue(ProjectConstants.NOT_AVAILABLE);
				}
			}

			row.createCell(3).setCellValue(ccavenueResponseLogModel.getUserFullName());

			if ((ccavenueResponseLogModel.getTrackingId() != null) && (!"".equals(ccavenueResponseLogModel.getTrackingId())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getTrackingId()))) {
				row.createCell(4).setCellValue(ccavenueResponseLogModel.getTrackingId());
			} else {
				row.createCell(4).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if ((ccavenueResponseLogModel.getOrderStatus() != null) && (!"".equals(ccavenueResponseLogModel.getOrderStatus())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getOrderStatus()))) {
				row.createCell(5).setCellValue(ccavenueResponseLogModel.getOrderStatus());
			} else {
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if ((ccavenueResponseLogModel.getFailureMessage() != null) && (!"".equals(ccavenueResponseLogModel.getFailureMessage())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getFailureMessage()))) {
				row.createCell(6).setCellValue(ccavenueResponseLogModel.getFailureMessage());
			} else {
				row.createCell(6).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(7).setCellValue(StringUtils.valueOf(ccavenueResponseLogModel.getAmount()));

			if ((ccavenueResponseLogModel.getCurrency() != null) && (!"".equals(ccavenueResponseLogModel.getCurrency())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getCurrency()))) {
				row.createCell(8).setCellValue(ccavenueResponseLogModel.getCurrency());
			} else {
				row.createCell(8).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if ((ccavenueResponseLogModel.getPaymentMode()) != null && (!"".equals(ccavenueResponseLogModel.getPaymentMode())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getPaymentMode()))) {
				row.createCell(9).setCellValue(ccavenueResponseLogModel.getPaymentMode());
			} else {
				row.createCell(9).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if ((ccavenueResponseLogModel.getCardName() != null) && (!"".equals(ccavenueResponseLogModel.getCardName())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getCardName()))) {
				row.createCell(10).setCellValue(ccavenueResponseLogModel.getCardName());
			} else {
				row.createCell(10).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if ((ccavenueResponseLogModel.getBankRefNo() != null) && (!"".equals(ccavenueResponseLogModel.getBankRefNo())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getBankRefNo()))) {
				row.createCell(11).setCellValue(ccavenueResponseLogModel.getBankRefNo());
			} else {
				row.createCell(11).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if ((ccavenueResponseLogModel.getBankResponseCode() != null) && (!"".equals(ccavenueResponseLogModel.getBankResponseCode())) && (!"null".equalsIgnoreCase(ccavenueResponseLogModel.getBankResponseCode()))) {
				row.createCell(12).setCellValue(ccavenueResponseLogModel.getBankResponseCode());
			} else {
				row.createCell(12).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(13).setCellValue(DateUtils.dbTimeStampToSesionDate(ccavenueResponseLogModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/drivers-drive-reports")
	@GET
	//@formatter:off
	public void grtDriversDriveReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("drivers_drive_reports_");

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Driver Name");
		columnNames.add("Phone No");
		columnNames.add("Email");
		columnNames.add("Distance (" + adminSettingsModel.getDistanceType() + ")");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorId = null;
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		List<UserModel> driversDriveReportList = UserModel.getDriverKmReportAdminListForSearch(0, 0, "DESC", UserRoles.DRIVER_ROLE_ID, "%%", ProjectConstants.TourStatusConstants.ENDED_TOUR, startDatelong, endDatelong, assignedRegionList, vendorId);

		int i = 1;
		int counter = 1;

		for (UserModel driversDriveReport : driversDriveReportList) {

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(driversDriveReport.getFirstName() + " " + driversDriveReport.getFirstName());
			row.createCell(2).setCellValue(driversDriveReport.getPhoneNoCode() + driversDriveReport.getPhoneNo());
			row.createCell(3).setCellValue(driversDriveReport.getEmail());
			row.createCell(4).setCellValue(StringUtils.valueOf(driversDriveReport.getDriverDistanceTotal() / adminSettingsModel.getDistanceUnits()));

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver-drive-detail-report")
	@GET
	//@formatter:off
	public void grtDetailDriverDriveReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("driverId") String driverId,  
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(driverId);

		File fileToWrite = createFileToExport(MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()) + "_drive_report_");

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Trip Id");
		columnNames.add("Source Address");
		columnNames.add("Destination Address");
		columnNames.add("Distance (" + adminSettingsModel.getDistanceType() + ")");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Driver Name");
		columnHeaderNames.add("Total Distanced Traveled (" + adminSettingsModel.getDistanceType() + ")");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String[] statusArray = { ProjectConstants.TourStatusConstants.ENDED_TOUR };

		List<TourModel> driverTourList = TourModel.getDriverAllTourListBySearch(driverId, 0, 0, "DESC", searchString, statusArray, startDatelong, endDatelong, assignedRegionList);

		double totalDistanceTravled = 0;

		int i = 4;
		int counter = 1;

		for (TourModel driverTour : driverTourList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(driverTour.getUserTourId());
			row.createCell(2).setCellValue(driverTour.getSourceAddress());
			row.createCell(3).setCellValue(driverTour.getDestinationAddress());
			row.createCell(4).setCellValue(StringUtils.valueOf(driverTour.getDistance() / adminSettingsModel.getDistanceUnits()));

			totalDistanceTravled += driverTour.getDistance();

			i++;
			counter++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));
		columnHeaderValues.add(StringUtils.valueOf(totalDistanceTravled / adminSettingsModel.getDistanceUnits()));
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver/refer-benefits-reports")
	@GET
	//@formatter:off
	public void driverReferBenefitsReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("referral_benefits_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Name");
		columnNames.add("Phone Number");
		columnNames.add("No Of Refer");
		columnNames.add("Benefits");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<UserModel> userModelList = UserModel.getDriverListForReferBenefitsReportsSearch(0, 0, "", UserRoles.DRIVER_ROLE_ID, "%%", startDatelong, endDatelong, assignedRegionList);

		int i = 4;
		int counter = 1;

		for (UserModel userModel : userModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			row.createCell(2).setCellValue(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
			row.createCell(3).setCellValue(userModel.getNoOfRefer());
			row.createCell(4).setCellValue(userModel.getTotalReferralBenefits());

			i++;
			counter++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver/refer-benefits/details-reports")
	@GET
	//@formatter:off
	public void driverReferBenefitsDetailsReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("driverId") String driverId,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate			
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_referral_logs_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Date");
		columnNames.add("Customer Name");
		columnNames.add("Customer Phone Number");
		columnNames.add("Customer Email");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<DriverReferralCodeLogModel> driverReferralCodeLogModelList = DriverReferralCodeLogModel.getDriverReferralLogsListForSearch(driverId, 0, 0, "", "%%", startDatelong, endDatelong);

		int i = 4;
		int counter = 1;

		for (DriverReferralCodeLogModel driverReferralCodeLogModel : driverReferralCodeLogModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(driverReferralCodeLogModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));
			row.createCell(2).setCellValue(driverReferralCodeLogModel.getPassengerName());
			row.createCell(3).setCellValue(driverReferralCodeLogModel.getPassengerPhoneNumber());
			row.createCell(4).setCellValue(driverReferralCodeLogModel.getPassengerEmail());

			i++;
			counter++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver/refer-benefits/trips-reports")
	@GET
	//@formatter:off
	public void driverReferBenefitsTripsReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("driverId") String driverId,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate			
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_trip_benefit_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(BusinessAction.messageForKeyAdmin("labelSrNo"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelDRBenefits"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelUserTourId"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverName"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverPhoneNumber"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelDriverEmail"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelPassengerName"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelPassengerPhoneNumber"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelPassengerEmail"));
		columnNames.add(BusinessAction.messageForKeyAdmin("labelDate"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		columnHeaderNames.add("Total Benefits");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<TourReferrerBenefitModel> tourReferrerBenefitModelList = TourReferrerBenefitModel.getTourReferrerBenefitListForSearch(driverId, 0, 0, "", "%%", startDatelong, endDatelong);

		int i = 4;
		int counter = 1;

		double totalBenefits = 0.0;

		for (TourReferrerBenefitModel tourReferrerBenefitModel : tourReferrerBenefitModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(StringUtils.valueOf(tourReferrerBenefitModel.getReferrerDriverBenefit()));
			row.createCell(2).setCellValue(tourReferrerBenefitModel.getUserTourId());
			row.createCell(3).setCellValue(tourReferrerBenefitModel.getDriverName());
			row.createCell(4).setCellValue(tourReferrerBenefitModel.getDriverPhoneNumber());
			row.createCell(5).setCellValue(tourReferrerBenefitModel.getDriverEmail());
			row.createCell(6).setCellValue(tourReferrerBenefitModel.getPassengerName());
			row.createCell(7).setCellValue(tourReferrerBenefitModel.getPassengerPhoneNumber());
			row.createCell(8).setCellValue(tourReferrerBenefitModel.getPassengerEmail());
			row.createCell(9).setCellValue(DateUtils.dbTimeStampToSesionDate(tourReferrerBenefitModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			totalBenefits = totalBenefits + tourReferrerBenefitModel.getReferrerDriverBenefit();

			i++;
			counter++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		columnHeaderValues.add(StringUtils.valueOf(totalBenefits));
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/passenger-details")
	@GET
	//@formatter:off
	public void loadPassengerDetailsForReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("startDate") String startDate, 
		@QueryParam("endDate") String endDate,
		@QueryParam("status") String status
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("passenger_details_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Name");
		columnNames.add("Phone Number");
		columnNames.add("Email Address");
		columnNames.add("Type Of User");
		columnNames.add("Wallet Balance");
		columnNames.add("Referral Code");
		columnNames.add("Verified Status");
		columnNames.add("Status");
		columnNames.add("Joining Date");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		columnHeaderNames.add("Selected Filter");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		String statusCheck = null;
		if (status.equalsIgnoreCase(ProjectConstants.ACTIVE_ID)) {
			statusCheck = ProjectConstants.ACTIVE_ID;
		} else if (status.equalsIgnoreCase(ProjectConstants.DEACTIVE_ID)) {
			statusCheck = ProjectConstants.DEACTIVE_ID;
		}

		String vendorId = null;
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		List<UserModel> userModelList = UserModel.getUserListForSearch(0, 0, "", UserRoles.PASSENGER_ROLE_ID, "%%", startDatelong, endDatelong, null, vendorId, statusCheck);

		int i = 4;
		int srNo = 1;

		for (UserModel userModel : userModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(srNo);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			row.createCell(2).setCellValue(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
			row.createCell(3).setCellValue(userModel.getEmail());
			if (userModel.getPhoneNum() != null) {
				row.createCell(4).setCellValue("KP E-MART");
			} else {
				row.createCell(4).setCellValue("My Hub");
			}
			row.createCell(5).setCellValue(userModel.getCredit());
			row.createCell(6).setCellValue(userModel.getReferralCode());

			if (userModel.isVerified()) {
				row.createCell(7).setCellValue(messageForKeyAdmin("labelVerified"));
			} else {
				row.createCell(7).setCellValue(messageForKeyAdmin("labelNotVerified"));
			}

			if (userModel.isActive()) {
				row.createCell(8).setCellValue(messageForKeyAdmin("labelActive"));
			} else {
				row.createCell(8).setCellValue(messageForKeyAdmin("labelDeactive"));
			}

			row.createCell(9).setCellValue(DateUtils.dbTimeStampToSesionDate(userModel.getCreatedAt(), timeZone, DateUtils.DISPLAY_DATE_TIME_FORMAT));

			i++;
			srNo++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);

		if (status.equalsIgnoreCase(ProjectConstants.ACTIVE_ID)) {
			columnHeaderValues.add(messageForKeyAdmin("labelActive"));
		} else if (status.equalsIgnoreCase(ProjectConstants.DEACTIVE_ID)) {
			columnHeaderValues.add(messageForKeyAdmin("labelDeactive"));
		} else {
			columnHeaderValues.add(messageForKeyAdmin("labelAll"));
		}

		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver/loggedin/time-report")
	@GET
	//@formatter:off
	public void driverLoggedInTimeLogsReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("driverId") String driverId,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate			
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_loggedin_time_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Logged In Hours");
		columnNames.add("Date");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		columnHeaderNames.add("Driver Name");
		columnHeaderNames.add("Email Address");
		columnHeaderNames.add("Phone Number");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<DriverLoggedInTimeModel> driverLoggedInTimeModelList = DriverLoggedInTimeModel.getDriverLoggedInTimeLogListForSearch(driverId, 0, 0, "", startDatelong, endDatelong);

		String currentDateStr = DateUtils.getDateInFormatFromMilliSecond(DateUtils.nowAsGmtMillisec(), DateUtils.DATE_FORMAT_FOR_VIEW);
		UserModel userDetails = UserModel.getDriverDetailsForLoggedInTimeReportById(driverId);

		long extraLoggedInTime = 0;

		if (userDetails != null) {

			long currentTime = DateUtils.nowAsGmtMillisec();

			if (userDetails.isOnDuty()) {

				if (currentTime > userDetails.getUpdatedAt()) {
					extraLoggedInTime = currentTime - userDetails.getUpdatedAt() > 0 ? (currentTime - userDetails.getUpdatedAt()) : 0;
				}
			}
		}

		int i = 4;
		int counter = 1;

		DecimalFormat dfTwoDigit = new DecimalFormat("#00");

		for (DriverLoggedInTimeModel driverLoggedInTimeModel : driverLoggedInTimeModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);

			long loggedInTime = driverLoggedInTimeModel.getLoggedInTime();

			if ((counter == 1) && (currentDateStr.equals(endDate))) {
				loggedInTime = loggedInTime + extraLoggedInTime;
			}

			if (loggedInTime > 0) {

				long secondsInMilli = 1000;
				long minutesInMilli = secondsInMilli * 60;
				long hoursInMilli = minutesInMilli * 60;

				long elapsedHours = loggedInTime / hoursInMilli;
				loggedInTime = loggedInTime % hoursInMilli;

				long elapsedMinutes = loggedInTime / minutesInMilli;
				loggedInTime = loggedInTime % minutesInMilli;

				long elapsedSeconds = loggedInTime / secondsInMilli;

				row.createCell(1).setCellValue(dfTwoDigit.format(elapsedHours) + ":" + dfTwoDigit.format(elapsedMinutes) + ":" + dfTwoDigit.format(elapsedSeconds));

			} else {

				row.createCell(1).setCellValue(0);
			}

			row.createCell(2).setCellValue(DateUtils.dbTimeStampToSesionDate(driverLoggedInTimeModel.getDateTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));

			i++;

			counter++;
		}

		DriverInfoModel driverModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);

		if (driverModel != null) {
			columnHeaderValues.add(MyHubUtils.formatFullName(driverModel.getFirstName(), driverModel.getLastName()));
			columnHeaderValues.add(driverModel.getEmail());
			columnHeaderValues.add(MyHubUtils.formatPhoneNumber(driverModel.getPhoneNoCode(), driverModel.getPhoneNo()));
		} else {
			columnHeaderValues.add(ProjectConstants.NOT_AVAILABLE);
			columnHeaderValues.add(ProjectConstants.NOT_AVAILABLE);
			columnHeaderValues.add(ProjectConstants.NOT_AVAILABLE);
		}

		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/driver-duty-report")
	@GET
	//@formatter:off
	public void driverDutyReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("onOffStatusId") String onOffStatusId,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_duty_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Driver Name");
		columnNames.add("Email");
		columnNames.add("Phone");
		columnNames.add("Duty Status");
		columnNames.add("State");
		columnNames.add("Last Location Time");
		columnNames.add("Total Logged In Hours");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		createRowWithData(sheet, columnHeaderNames, 0);

		String onOffStatusForCheck = null;
		boolean onOffStatus = true;

		if (onOffStatusId.equals("1")) {
			onOffStatusForCheck = "YES";
			onOffStatus = true;
		} else if (onOffStatusId.equals("2")) {
			onOffStatusForCheck = "YES";
			onOffStatus = false;
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		String vendorId = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		List<UserModel> userModelList = UserModel.getDriverListForSearch(0, 100000, "", UserRoles.DRIVER_ROLE_ID, searchString, startDateLong, endDateLong, onOffStatusForCheck, onOffStatus, vendorId);

		DecimalFormat dfTwoDigit = new DecimalFormat("#00");
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);
		long idealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();
		long currentTime = DateUtils.nowAsGmtMillisec();

		int i = 4;
		int counter = 1;

		for (UserModel userModel : userModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			row.createCell(2).setCellValue(userModel.getEmail());
			row.createCell(3).setCellValue(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));

			if (userModel.isOnDuty()) {

				row.createCell(4).setCellValue(ProjectConstants.ON_DUTY);

				if (userModel.getCreatedAt() > idealTimeInMillis) {
					// innerJsonArray.put(ProjectConstants.ONLINE_STRING);
					row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
				} else {
					row.createCell(5).setCellValue(ProjectConstants.IDEAL_STRING);
				}

			} else {

				row.createCell(4).setCellValue(ProjectConstants.OFF_DUTY);
				// innerJsonArray.put(ProjectConstants.OFFLINE_STRING);
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (userModel.getCreatedAt() <= 0) {
				row.createCell(6).setCellValue(ProjectConstants.NOT_AVAILABLE);
			} else {
				row.createCell(6).setCellValue(DateUtils.dbTimeStampToSesionDate(userModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			}

			// Get total logged in hours by date ---------------------
			long totalLoggedInTime = DriverLoggedInTimeModel.getTotalLoggedInTimeByDriverIdandDate(userModel.getUserId(), startDateLong, endDateLong);

			// Use updated_at for current logged in time
			if (userModel.isOnDuty()) {

				if (currentTime > userModel.getUpdatedAt()) {
					totalLoggedInTime = totalLoggedInTime + (currentTime - userModel.getUpdatedAt() > 0 ? (currentTime - userModel.getUpdatedAt()) : 0);
				}
			}

			if (totalLoggedInTime > 0) {

				long secondsInMilli = 1000;
				long minutesInMilli = secondsInMilli * 60;
				long hoursInMilli = minutesInMilli * 60;

				long elapsedHours = totalLoggedInTime / hoursInMilli;
				totalLoggedInTime = totalLoggedInTime % hoursInMilli;

				long elapsedMinutes = totalLoggedInTime / minutesInMilli;
				totalLoggedInTime = totalLoggedInTime % minutesInMilli;

				long elapsedSeconds = totalLoggedInTime / secondsInMilli;

				row.createCell(7).setCellValue(dfTwoDigit.format(elapsedHours) + ":" + dfTwoDigit.format(elapsedMinutes) + ":" + dfTwoDigit.format(elapsedSeconds));

			} else {

				row.createCell(7).setCellValue(0);
			}

			i++;
			counter++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);
		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	@Path("/admin-bookings")
	@GET
	//@formatter:off
	public void getAdminBookingsFile(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("type") String type, 
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam(FieldConstants.STATUS_FILTER) String statusFilter,
		@QueryParam(FieldConstants.SURGE_FILTER) String surgeFilter,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("admin_bookings_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelTourId"));
		columnNames.add(messageForKeyAdmin("labelTripRequestTime"));
		columnNames.add(messageForKeyAdmin("labelPickUpLocation"));
		columnNames.add(messageForKeyAdmin("labelDropOffLocation"));
		columnNames.add(messageForKeyAdmin("labelDriverName"));
		columnNames.add(messageForKeyAdmin("labelDriverPhone"));
		columnNames.add(messageForKeyAdmin("labelPassengerName"));
		columnNames.add(messageForKeyAdmin("labelPassengerPhone"));
		columnNames.add("Distance");
		columnNames.add(messageForKeyAdmin("labelTotalFare"));
		columnNames.add("Promo Code");
		columnNames.add("Promocode type");
		columnNames.add("Promocode value");
		columnNames.add("Promocode user type");
		columnNames.add("Promocode usage type");
		columnNames.add("Promocode usage count");
		columnNames.add(messageForKeyAdmin("labelPromotion"));
		columnNames.add(messageForKeyAdmin("labelTotalCharges"));
		columnNames.add(messageForKeyAdmin("labelSurgePrice"));
		columnNames.add("Credits adjustment");
		columnNames.add(messageForKeyAdmin("labelAmountCollected"));
		columnNames.add("Driver Income");
		columnNames.add("Driver Percentage");
		columnNames.add("Status");
		columnNames.add(messageForKeyAdmin("labelPassengerRatings"));
		columnNames.add(messageForKeyAdmin("labelPassengerComments"));
		columnNames.add(messageForKeyAdmin("labelDriverRating"));
		columnNames.add(messageForKeyAdmin("labelDriverComments"));
		columnNames.add("Payment Mode");
		columnNames.add("Payment Status");
		columnNames.add("Refund Status");
		columnNames.add("Booking Type");
		columnNames.add("Updated Amount Collected");
		columnNames.add("Remark");
		columnNames.add("Remark By");
		columnNames.add(messageForKeyAdmin("labelTripStatus"));
		columnNames.add(messageForKeyAdmin("labelServiceType"));
		columnNames.add("Airport Booking");
		columnNames.add("Surge Type");
		columnNames.add("Surge Radius");
		columnNames.add("Vendor Name");
		columnNames.add("Markup Amount");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add(messageForKeyAdmin("labelBusinessOwnerName"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalTourCount"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalCancelledTour"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalAmount"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalAmountCollected"));
		createRowWithData(sheet, columnHeaderNames, 0);

		long totalTourCount = 0;
		long totalCancelledTrip = 0;
		double amount = 0.0d;
		double cashCollected = 0.0d;

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		String timeZone = TimeZoneUtils.getTimeZone();

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<TourModel> tourList = new ArrayList<TourModel>();

		long tourStartDate = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
		long tourEndDate = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());

		String[] statusArray = TourUtils.getStatusArray(statusFilter);
		double[] surgeArray = TourUtils.getSurgeArray(surgeFilter);

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		if (ProjectConstants.ALL_USERS_ID.equalsIgnoreCase(type) || ProjectConstants.DRIVER_USERS_ID.equalsIgnoreCase(type)) {
			tourList = TourModel.getTourListForBookingsExport(searchString, null, statusArray, tourStartDate, tourEndDate, surgeArray, assignedRegionList, null, UserRoles.VENDOR_ROLE_ID);
		} else if (ProjectConstants.ADMIN_USERS_ID.equalsIgnoreCase(type)) {
			tourList = TourModel.getTourListForBookingsExport(searchString, ProjectConstants.ADMIN_BOOKING, statusArray, tourStartDate, tourEndDate, surgeArray, assignedRegionList, null, UserRoles.VENDOR_ROLE_ID);
		} else if (ProjectConstants.CORPORATE_OWNERS_ID.equalsIgnoreCase(type)) {
			tourList = TourModel.getTourListForBookingsExport(searchString, ProjectConstants.BUSINESS_OWNER_BOOKING, statusArray, tourStartDate, tourEndDate, surgeArray, assignedRegionList, null, UserRoles.VENDOR_ROLE_ID);
		} else if (ProjectConstants.PASSENGER_USERS_ID.equalsIgnoreCase(type)) {
			tourList = TourModel.getTourListForBookingsExport(searchString, ProjectConstants.INDIVIDUAL_BOOKING, statusArray, tourStartDate, tourEndDate, surgeArray, assignedRegionList, null, UserRoles.VENDOR_ROLE_ID);
		} else {
			tourList = TourModel.getTourListForBookingsExport(searchString, null, statusArray, tourStartDate, tourEndDate, surgeArray, assignedRegionList, null, UserRoles.VENDOR_ROLE_ID);
		}

		int i = 4;

		for (TourModel tour : tourList) {

			totalTourCount++;

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
				totalCancelledTrip = totalCancelledTrip + 1;
			}

			HSSFRow row = sheet.createRow((int) i);

			row.createCell(0).setCellValue(tour.getUserTourId());
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(tour.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(2).setCellValue(tour.getSourceAddress());

			if (StringUtils.validString(tour.getDestinationAddress())) {
				row.createCell(3).setCellValue(tour.getDestinationAddress());
			} else {
				row.createCell(3).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getFirstName())) {
				row.createCell(4).setCellValue(MyHubUtils.formatFullName(tour.getFirstName(), tour.getLastName()));
			} else {
				row.createCell(4).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getPhoneNoCode()) && StringUtils.validString(tour.getPhoneNo())) {
				row.createCell(5).setCellValue(MyHubUtils.formatPhoneNumber(tour.getPhoneNoCode(), tour.getPhoneNo()));
			} else {
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(6).setCellValue(MyHubUtils.formatFullName(tour.getpFirstName(), tour.getpLastName()));

			row.createCell(7).setCellValue(MyHubUtils.formatPhoneNumber(tour.getpPhoneCode(), tour.getpPhone()));

			if ("0".equalsIgnoreCase(StringUtils.valueOf(tour.getDistance()))) {
				row.createCell(8).setCellValue(ProjectConstants.NOT_AVAILABLE);
			} else {
				row.createCell(8).setCellValue(StringUtils.valueOf(MyHubUtils.getKilometerFromMeters(tour.getDistance())) + " " + adminSettings.getDistanceType());
			}

			row.createCell(9).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getTotal()));

			if (tour.isPromoCodeApplied()) {

				row.createCell(10).setCellValue(tour.getPromoCode());

				if (ProjectConstants.PERCENTAGE_ID.equals(tour.getMode())) {
					row.createCell(11).setCellValue(ProjectConstants.PERCENTAGE_TEXT);
					row.createCell(12).setCellValue(tour.getDiscount() + "%");
				} else {
					row.createCell(11).setCellValue(ProjectConstants.AMOUNT_TEXT);
					row.createCell(12).setCellValue(tour.getDiscount());
				}

				if (ProjectConstants.ALL_ID.equalsIgnoreCase(tour.getUsageType())) {
					row.createCell(13).setCellValue(ProjectConstants.ALL_TEXT);
				} else {
					row.createCell(13).setCellValue(ProjectConstants.INDIVIDUAL_TEXT);
				}

				if (ProjectConstants.UNLIMITED_ID.equalsIgnoreCase(tour.getUsage())) {
					row.createCell(14).setCellValue(ProjectConstants.UNLIMITED_TEXT);
					row.createCell(15).setCellValue(ProjectConstants.UNLIMITED_TEXT);
				} else {
					row.createCell(14).setCellValue(ProjectConstants.LIMITED_TEXT);
					row.createCell(15).setCellValue(tour.getUsageCount());
				}

				row.createCell(16).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getPromoDiscount()));

			} else {

				row.createCell(10).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(11).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(12).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(13).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(14).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(15).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(16).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
				row.createCell(17).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFine()));
			} else {
				row.createCell(17).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
				amount = amount + Double.parseDouble(StringUtils.valueOf(tour.getCharges()));
			}

			// surge price
			if (tour.isSurgePriceApplied()) {
				row.createCell(18).setCellValue(tour.getSurgePrice() + "x");
			} else {
				row.createCell(18).setCellValue("1x");
			}

			if (tour.getUsedCredits() < 0) {
				String usedCredits = StringUtils.valueOf(tour.getUsedCredits()) + "";
				usedCredits = usedCredits.replace("-", "-" + adminSettings.getCurrencySymbol());
				row.createCell(19).setCellValue(usedCredits);
			} else {
				row.createCell(19).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getUsedCredits()));
			}

			row.createCell(20).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));

			row.createCell(21).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getDriverAmount()));

			row.createCell(22).setCellValue(tour.getPercentage());

			if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
				row.createCell(23).setCellValue(messageForKeyAdmin("labelCancelledByPassenger"));
			} else if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
				row.createCell(23).setCellValue(messageForKeyAdmin("labelCancelledByPassenger"));
			} else {
				row.createCell(23).setCellValue(tour.getStatus());
			}

			row.createCell(24).setCellValue(tour.getPassengerRating());

			if (StringUtils.validString(tour.getPassengerComment())) {
				row.createCell(25).setCellValue(tour.getPassengerComment());
			} else {
				row.createCell(25).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(26).setCellValue(tour.getDriverRating());

			if (StringUtils.validString(tour.getDriverComment())) {
				row.createCell(27).setCellValue(tour.getDriverComment());
			} else {
				row.createCell(27).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (ProjectConstants.CASH.equalsIgnoreCase(tour.getPaymentMode())) {
				row.createCell(28).setCellValue(ProjectConstants.C_CASH);

				if (tour.isCashNotReceived()) {
					row.createCell(29).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_NOT_COLLECTED);
				} else {
					cashCollected = cashCollected + tour.getFinalAmountCollected();
					row.createCell(29).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_COLLECTED + ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));
				}

			} else {
				row.createCell(28).setCellValue(ProjectConstants.C_CARD);
				row.createCell(29).setCellValue(ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));
			}

			if (tour.isRefunded()) {
				row.createCell(30).setCellValue("Amount Refunded");
			} else {
				row.createCell(30).setCellValue("Amount Not Refunded");
			}

			if (tour.isRentalBooking()) {
				row.createCell(31).setCellValue(messageForKeyAdmin("labelRental"));
			} else {
				row.createCell(31).setCellValue(messageForKeyAdmin("labelTaxi"));
			}

			row.createCell(32).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getUpdatedAmountCollected()));

			if (StringUtils.validString(tour.getRemark())) {
				row.createCell(33).setCellValue(tour.getRemark());
			} else {
				row.createCell(33).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getRemarkBy())) {
				row.createCell(34).setCellValue(tour.getRemarkBy());
			} else {
				row.createCell(34).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(35).setCellValue(TourUtils.getTourStatus(tour.getStatus()));

			if (StringUtils.validString(tour.getCarType())) {
				row.createCell(36).setCellValue(tour.getCarType());
			} else {
				row.createCell(36).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getAirportBookingType())) {
				row.createCell(37).setCellValue(tour.getAirportBookingType());
			} else {
				row.createCell(37).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (tour.isSurgePriceApplied()) {

				if (tour.getSurgeType() == null) {
					row.createCell(38).setCellValue(ProjectConstants.SURGE_TYPE_TIME);
					row.createCell(39).setCellValue(ProjectConstants.NOT_AVAILABLE);
				} else {
					if (tour.getSurgeType().equals(ProjectConstants.SURGE_TYPE_RADIUS)) {
						row.createCell(38).setCellValue(ProjectConstants.SURGE_TYPE_RADIUS);
						row.createCell(39).setCellValue(tour.getSurgeRadius());
					} else {
						row.createCell(38).setCellValue(ProjectConstants.SURGE_TYPE_TIME);
						row.createCell(39).setCellValue(ProjectConstants.NOT_AVAILABLE);
					}

				}

			} else {
				row.createCell(38).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(39).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getVendorName())) {
				row.createCell(40).setCellValue(tour.getVendorName());
			} else {
				row.createCell(40).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(41).setCellValue(tour.getMarkupFare());
			i++;
		}

		List<String> columnHeaderData = new ArrayList<>();
		columnHeaderData.add(loggedInUserModelViaSession.getFullName());
		columnHeaderData.add(StringUtils.valueOf(totalTourCount));
		columnHeaderData.add(StringUtils.valueOf(totalCancelledTrip));
		columnHeaderData.add(StringUtils.valueOf(amount));
		columnHeaderData.add(StringUtils.valueOf(cashCollected));
		createRowWithData(sheet, columnHeaderData, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	// Done
	@Path("/vendor-bookings")
	@GET
	//@formatter:off
	public void getVendorBookingsFile(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("statusFilter") String statusFilter,
		@QueryParam("surgeFilter") String surgeFilter,
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("vendor_bookings_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelTourId"));
		columnNames.add(messageForKeyAdmin("labelTripRequestTime"));
		columnNames.add(messageForKeyAdmin("labelPickUpLocation"));
		columnNames.add(messageForKeyAdmin("labelDropOffLocation"));
		columnNames.add(messageForKeyAdmin("labelDriverName"));
		columnNames.add(messageForKeyAdmin("labelDriverPhone"));
		columnNames.add(messageForKeyAdmin("labelPassengerName"));
		columnNames.add(messageForKeyAdmin("labelPassengerPhone"));
		columnNames.add(messageForKeyAdmin("labelDistance"));
		columnNames.add(messageForKeyAdmin("labelTotalFare"));
		columnNames.add(messageForKeyAdmin("labelPromoCode"));
		columnNames.add(messageForKeyAdmin("labelType"));
		columnNames.add(messageForKeyAdmin("labelValue"));
		columnNames.add(messageForKeyAdmin("labelUserType"));
		columnNames.add(messageForKeyAdmin("labelUsageType"));
		columnNames.add(messageForKeyAdmin("labelUsageCount"));
		columnNames.add(messageForKeyAdmin("labelPromotion"));
		columnNames.add(messageForKeyAdmin("labelTotalCharges"));
		columnNames.add(messageForKeyAdmin("labelSurgePrice"));
		columnNames.add(messageForKeyAdmin("labelCreditsAdjustment"));
		columnNames.add(messageForKeyAdmin("labelAmountCollected"));
		columnNames.add(messageForKeyAdmin("labelDriverIncome"));
		columnNames.add(messageForKeyAdmin("labelDriverPercentage"));
		columnNames.add(messageForKeyAdmin("labelStatus"));
		columnNames.add(messageForKeyAdmin("labelPassengerRatings"));
		columnNames.add(messageForKeyAdmin("labelPassengerComments"));
		columnNames.add(messageForKeyAdmin("labelDriverRating"));
		columnNames.add(messageForKeyAdmin("labelDriverComments"));
		columnNames.add(messageForKeyAdmin("labelPaymentMode"));
		columnNames.add(messageForKeyAdmin("labelPaymentStatus"));
		columnNames.add(messageForKeyAdmin("labelRefundStatus"));
		columnNames.add(messageForKeyAdmin("labelBookingType"));
		columnNames.add(messageForKeyAdmin("labelUpdatedAmountCollected"));
		columnNames.add(messageForKeyAdmin("labelRemark"));
		columnNames.add(messageForKeyAdmin("labelRemarkBy"));
		columnNames.add(messageForKeyAdmin("labelTripStatus"));
		columnNames.add(messageForKeyAdmin("labelServiceType"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add(messageForKeyAdmin("labelBusinessOwnerName"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalTourCount"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalCancelledTour"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalAmount"));
		columnHeaderNames.add(messageForKeyAdmin("labelTotalAmountCollected"));
		createRowWithData(sheet, columnHeaderNames, 0);

		long totalTourCount = 0;
		long totalCancelledTrip = 0;
		double amount = 0.0d;
		double cashCollected = 0.0d;

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String timeZone = TimeZoneUtils.getTimeZone();

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		String[] statusArray = TourUtils.getStatusArray(statusFilter);
		double[] surgeArray = TourUtils.getSurgeArray(surgeFilter);

		List<TourModel> tourList = TourModel.getTourListForBookingsExport(searchString, null, statusArray, startDateLong, endDateLong, surgeArray, assignedRegionList, loginSessionMap.get(LoginUtils.USER_ID), UserRoles.VENDOR_ROLE_ID);

		int i = 4;

		for (TourModel tour : tourList) {

			totalTourCount++;

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
				totalCancelledTrip = totalCancelledTrip + 1;
			}

			HSSFRow row = sheet.createRow((int) i);

			row.createCell(0).setCellValue(tour.getUserTourId());
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(tour.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(2).setCellValue(tour.getSourceAddress());

			if (StringUtils.validString(tour.getDestinationAddress())) {
				row.createCell(3).setCellValue(tour.getDestinationAddress());
			} else {
				row.createCell(3).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getFirstName())) {
				row.createCell(4).setCellValue(MyHubUtils.formatFullName(tour.getFirstName(), tour.getLastName()));
			} else {
				row.createCell(4).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getPhoneNo())) {
				row.createCell(5).setCellValue(MyHubUtils.formatPhoneNumber(tour.getPhoneNoCode(), tour.getPhoneNo()));
			} else {
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (tour.getPassengerVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
				row.createCell(6).setCellValue(MyHubUtils.formatFullName(tour.getpFirstName(), tour.getpLastName()));
			} else {
				row.createCell(6).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(7).setCellValue(ProjectConstants.NOT_AVAILABLE);

			if (tour.getDistance() > 0) {
				row.createCell(8).setCellValue(MyHubUtils.getDistanceInProjectUnitFromMeters(tour.getDistance(), adminSettings) + adminSettings.getDistanceType());
			} else {
				row.createCell(8).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(9).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getTotal()));

			if (tour.isPromoCodeApplied()) {

				row.createCell(10).setCellValue(tour.getPromoCode());

				if (ProjectConstants.PERCENTAGE_ID.equals(tour.getMode())) {
					row.createCell(11).setCellValue(ProjectConstants.PERCENTAGE_TEXT);
					row.createCell(12).setCellValue(tour.getDiscount() + "%");
				} else {
					row.createCell(11).setCellValue(ProjectConstants.AMOUNT_TEXT);
					row.createCell(12).setCellValue(tour.getDiscount());
				}

				if (ProjectConstants.ALL_ID.equalsIgnoreCase(tour.getUsageType())) {
					row.createCell(13).setCellValue(ProjectConstants.ALL_TEXT);
				} else {
					row.createCell(13).setCellValue(ProjectConstants.INDIVIDUAL_TEXT);
				}

				if (ProjectConstants.UNLIMITED_ID.equalsIgnoreCase(tour.getUsage())) {
					row.createCell(14).setCellValue(ProjectConstants.UNLIMITED_TEXT);
					row.createCell(15).setCellValue(ProjectConstants.UNLIMITED_TEXT);
				} else {
					row.createCell(14).setCellValue(ProjectConstants.LIMITED_TEXT);
					row.createCell(15).setCellValue(tour.getUsageCount());
				}

				row.createCell(16).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getPromoDiscount()));

			} else {

				row.createCell(10).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(11).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(12).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(13).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(14).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(15).setCellValue(ProjectConstants.NOT_AVAILABLE);
				row.createCell(16).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
				row.createCell(17).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFine()));
			} else {
				row.createCell(17).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getCharges()));
				amount = amount + StringUtils.doubleValueOf(StringUtils.valueOf(tour.getCharges()));
			}

			// surge price
			if (tour.isSurgePriceApplied()) {
				row.createCell(18).setCellValue(tour.getSurgePrice() + "x");
			} else {
				row.createCell(18).setCellValue("1x");
			}

			if (tour.getUsedCredits() < 0) {
				row.createCell(19).setCellValue(StringUtils.valueOf(tour.getUsedCredits()).replace("-", "-" + adminSettings.getCurrencySymbol()));
			} else {
				row.createCell(19).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getUsedCredits()));
			}

			row.createCell(20).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));
			row.createCell(21).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getDriverAmount()));
			row.createCell(22).setCellValue(tour.getPercentage());

			if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
				row.createCell(23).setCellValue(messageForKeyAdmin("labelCancelledByPassenger"));
			} else if (tour.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
				row.createCell(23).setCellValue(messageForKeyAdmin("labelCancelledByPassenger"));
			} else {
				row.createCell(23).setCellValue(tour.getStatus());
			}

			row.createCell(24).setCellValue(tour.getPassengerRating());

			if (StringUtils.validString(tour.getPassengerComment())) {
				row.createCell(25).setCellValue(tour.getPassengerComment());
			} else {
				row.createCell(25).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(26).setCellValue(tour.getDriverRating());

			if (StringUtils.validString(tour.getDriverComment())) {
				row.createCell(27).setCellValue(tour.getDriverComment());
			} else {
				row.createCell(27).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (ProjectConstants.CASH.equalsIgnoreCase(tour.getPaymentMode())) {

				row.createCell(28).setCellValue(ProjectConstants.C_CASH);

				if (tour.isCashNotReceived()) {
					row.createCell(29).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_NOT_COLLECTED);
				} else {
					cashCollected = cashCollected + tour.getFinalAmountCollected();
					row.createCell(29).setCellValue(ProjectConstants.CASH_INVOICE_MESSAGE_COLLECTED + ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));
				}

			} else {
				row.createCell(28).setCellValue(ProjectConstants.C_CARD);
				row.createCell(29).setCellValue(ProjectConstants.CARD_INVOICE_MESSAGE + " " + adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getFinalAmountCollected()));
			}

			if (tour.isRefunded()) {
				row.createCell(30).setCellValue("Amount Refunded");
			} else {
				row.createCell(30).setCellValue("Amount Not Refunded");
			}

			if (tour.isRentalBooking()) {
				row.createCell(31).setCellValue(messageForKeyAdmin("labelRental"));
			} else {
				row.createCell(31).setCellValue(messageForKeyAdmin("labelTaxi"));
			}

			row.createCell(32).setCellValue(adminSettings.getCurrencySymbol() + StringUtils.valueOf(tour.getUpdatedAmountCollected()));

			if (StringUtils.validString(tour.getRemark())) {
				row.createCell(33).setCellValue(tour.getRemark());
			} else {
				row.createCell(33).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tour.getRemarkBy())) {
				row.createCell(34).setCellValue(tour.getRemarkBy());
			} else {
				row.createCell(34).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			row.createCell(35).setCellValue(TourUtils.getTourStatus(tour.getStatus()));

			if (StringUtils.validString(tour.getCarType())) {
				row.createCell(36).setCellValue(tour.getCarType());
			} else {
				row.createCell(36).setCellValue(ProjectConstants.NOT_AVAILABLE);
			}

			i++;
		}

		UserModel userDetails = UserModel.getUserActiveDeativeDetailsById(loginSessionMap.get(LoginUtils.USER_ID));

		List<String> columnHeaderData = new ArrayList<>();
		columnHeaderData.add(MyHubUtils.formatFullName(userDetails.getFirstName(), userDetails.getLastName()));
		columnHeaderData.add(StringUtils.valueOf(totalTourCount));
		columnHeaderData.add(StringUtils.valueOf(totalCancelledTrip));
		columnHeaderData.add(StringUtils.valueOf(amount));
		columnHeaderData.add(StringUtils.valueOf(cashCollected));
		createRowWithData(sheet, columnHeaderData, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	// Done
	@Path("/user-account/reports")
	@GET
	//@formatter:off
	public void userAccountReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("userType") String userType, 	//Possible values drivers/vendors/vendorDrivers
		@QueryParam("searchString") String searchString
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("user_account_reports_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelSrNo"));
		columnNames.add(messageForKeyAdmin("labelName"));
		columnNames.add(messageForKeyAdmin("labelEmailAddress"));
		columnNames.add(messageForKeyAdmin("labelPhoneNumber"));
		columnNames.add(messageForKeyAdmin("labelCurrentBalance"));
		columnNames.add(messageForKeyAdmin("labelHoldBalance"));
		columnNames.add(messageForKeyAdmin("labelApprovedBalance"));
		columnNames.add(messageForKeyAdmin("labelTotalBalance"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		String vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		String roleId = UserRoles.DRIVER_ROLE_ID;

		// userType possible values drivers/vendors/vendorDrivers
		if ("drivers".equalsIgnoreCase(userType)) {
			vendorId = null;
			roleId = UserRoles.DRIVER_ROLE_ID;
		} else if ("vendors".equalsIgnoreCase(userType)) {
			vendorId = null;
			roleId = UserRoles.VENDOR_ROLE_ID;
		} else if ("vendorDrivers".equalsIgnoreCase(userType)) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			roleId = UserRoles.DRIVER_ROLE_ID;
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<UserAccountModel> userAccountModelList = UserAccountModel.getUserAccountDetailsListForExport(searchString, assignedRegionList, roleId, vendorId);

		int i = 1;
		int counter = 1;

		for (UserAccountModel userAccountModel : userAccountModelList) {

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(userAccountModel.getFirstName(), userAccountModel.getLastName()));
			row.createCell(2).setCellValue(userAccountModel.getEmail());
			row.createCell(3).setCellValue(MyHubUtils.formatPhoneNumber(userAccountModel.getPhoneNoCode(), userAccountModel.getPhoneNo()));
			row.createCell(4).setCellValue(StringUtils.valueOf(userAccountModel.getCurrentBalance()));
			row.createCell(5).setCellValue(StringUtils.valueOf(userAccountModel.getHoldBalance()));
			row.createCell(6).setCellValue(StringUtils.valueOf(userAccountModel.getApprovedBalance()));
			row.createCell(7).setCellValue(StringUtils.valueOf(userAccountModel.getTotalBalance()));

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	// Done
	@Path("/user-account/logs-reports")
	@GET
	//@formatter:off
	public void userAccountLogsReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("userId") String userId, 	
		@QueryParam("searchString") String searchString,
		@QueryParam("startDate") String startDate,
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("user_account_logs_reports_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelSrNo"));
		columnNames.add(messageForKeyAdmin("labelDate"));
		columnNames.add(messageForKeyAdmin("labelRemark"));
		columnNames.add(messageForKeyAdmin("labelCredit"));
		columnNames.add(messageForKeyAdmin("labelDebit"));
		columnNames.add(messageForKeyAdmin("labelStatus"));
		columnNames.add(messageForKeyAdmin("labelType"));
		columnNames.add(messageForKeyAdmin("labelCurrentBalance"));
		columnNames.add(messageForKeyAdmin("labelHoldBalance"));
		columnNames.add(messageForKeyAdmin("labelApprovedBalance"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add(messageForKeyAdmin("labelName"));
		columnHeaderNames.add(messageForKeyAdmin("labelEmailAddress"));
		columnHeaderNames.add(messageForKeyAdmin("labelPhoneNumber"));
		columnHeaderNames.add(messageForKeyAdmin("labelCurrentBalance"));
		columnHeaderNames.add(messageForKeyAdmin("labelHoldBalance"));
		columnHeaderNames.add(messageForKeyAdmin("labelApprovedBalance"));
		createRowWithData(sheet, columnHeaderNames, 0);

		UserProfileModel userModel = UserProfileModel.getAdminUserAccountDetailsById(userId);
		UserAccountModel userAccountModel = UserAccountModel.getAccountBalanceDetailsByUserId(userId);

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
		columnHeaderValues.add(userModel.getEmail());
		columnHeaderValues.add(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
		columnHeaderValues.add(userAccountModel != null ? StringUtils.valueOf(userAccountModel.getCurrentBalance()) : StringUtils.valueOf(0));
		columnHeaderValues.add(userAccountModel != null ? StringUtils.valueOf(userAccountModel.getHoldBalance()) : StringUtils.valueOf(0));
		columnHeaderValues.add(userAccountModel != null ? StringUtils.valueOf(userAccountModel.getApprovedBalance()) : StringUtils.valueOf(0));
		createRowWithData(sheet, columnHeaderValues, 1);

		String timeZone = TimeZoneUtils.getTimeZone();

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<UserAccountLogsModel> userAccountLogsModelList = UserAccountLogsModel.getUserAccountLogsListForExport(userId, startDateLong, endDateLong, "%" + searchString + "%");

		int i = 4;
		int counter = 1;

		for (UserAccountLogsModel userAccountLogsModel : userAccountLogsModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(DateUtils.dbTimeStampToSesionDate(userAccountLogsModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(2).setCellValue(userAccountLogsModel.getRemark());

			if (userAccountLogsModel.getCreditedAmount() == 0) {
				row.createCell(3).setCellValue(ProjectConstants.NOT_AVAILABLE);
			} else {
				row.createCell(3).setCellValue(StringUtils.valueOf(userAccountLogsModel.getCreditedAmount()));
			}

			if (userAccountLogsModel.getDebitedAmount() == 0) {
				row.createCell(4).setCellValue(ProjectConstants.NOT_AVAILABLE);
			} else {
				row.createCell(4).setCellValue(StringUtils.valueOf(userAccountLogsModel.getDebitedAmount()));
			}

			//@formatter:off
			if (ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT.equalsIgnoreCase(userAccountLogsModel.getTransactionStatus()) 
					|| ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT.equalsIgnoreCase(userAccountLogsModel.getTransactionStatus())
					|| ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(userAccountLogsModel.getTransactionStatus())) {
			//@formatter:on
				row.createCell(5).setCellValue(ProjectConstants.NOT_AVAILABLE);
			} else {
				row.createCell(5).setCellValue(userAccountLogsModel.getTransactionStatus());
			}

			row.createCell(6).setCellValue(userAccountLogsModel.getTransactionType());
			row.createCell(7).setCellValue(StringUtils.valueOf(userAccountLogsModel.getCurrentBalance()));
			row.createCell(8).setCellValue(StringUtils.valueOf(userAccountLogsModel.getHoldBalance()));
			row.createCell(9).setCellValue(StringUtils.valueOf(userAccountLogsModel.getApprovedBalance()));

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	// Done
	@Path("/encash-requests/reports")
	@GET
	//@formatter:off
	public void encashRequestsReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("requestsType") String requestsType, 	
		@QueryParam("searchString") String searchString,
		@QueryParam("startDate") String startDate,
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		String exportFileNamePrefix = "encash_requests_report_";

		if (ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(requestsType)) {
			exportFileNamePrefix = "encash_transferred_requests_report_";
		} else if (ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED.equalsIgnoreCase(requestsType)) {
			exportFileNamePrefix = "encash_rejected_requests_report_";
		}

		File fileToWrite = createFileToExport(exportFileNamePrefix);

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelSrNo"));
		columnNames.add(messageForKeyAdmin("labelName"));
		columnNames.add(messageForKeyAdmin("labelEmailAddress"));
		columnNames.add(messageForKeyAdmin("labelContactNumber"));
		columnNames.add(messageForKeyAdmin("labelRequestedAmount"));

		if (ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(requestsType)) {
			columnNames.add(messageForKeyAdmin("labelTransferredDate"));
		} else if (ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED.equalsIgnoreCase(requestsType)) {
			columnNames.add(messageForKeyAdmin("labelRejectedDate"));
		}

		columnNames.add(messageForKeyAdmin("labelStatus"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		String status = "";
		if (ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(requestsType)) {
			status = ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED;
		} else if (ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED.equalsIgnoreCase(requestsType)) {
			status = ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED;
		}

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		List<EncashRequestsModel> encashRequestsModelList = EncashRequestsModel.getEncashRequestsListForExport(status, searchString, startDateLong, endDateLong, assignedRegionList);

		int i = 1;
		int counter = 1;
		long dateInMillies = 0;

		for (EncashRequestsModel encashRequestsModel : encashRequestsModelList) {

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(encashRequestsModel.getFirstName(), encashRequestsModel.getLastName()));
			row.createCell(2).setCellValue(encashRequestsModel.getEmail());
			row.createCell(3).setCellValue(MyHubUtils.formatPhoneNumber(encashRequestsModel.getPhoneNoCode(), encashRequestsModel.getPhoneNo()));
			row.createCell(4).setCellValue(StringUtils.valueOf(encashRequestsModel.getRequestedAmount()));

			if (ProjectConstants.ENCASH_REQUEST_STATUS_TRANSFERRED.equalsIgnoreCase(requestsType)) {
				dateInMillies = encashRequestsModel.getTransferDate();
			} else if (ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED.equalsIgnoreCase(requestsType)) {
				dateInMillies = encashRequestsModel.getRejectedDate();
			}

			row.createCell(5).setCellValue(DateUtils.dbTimeStampToSesionDate(dateInMillies, timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(6).setCellValue(encashRequestsModel.getStatus());

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	// Done
	@Path("/driver-subscription-reports")
	@GET
	//@formatter:off
	public void driverSubscriptionReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("searchString") String searchString,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId, 	
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_subscription_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelSrNo"));
		columnNames.add(messageForKeyAdmin("labelDriverName"));
		columnNames.add(messageForKeyAdmin("labelVendorName"));
		columnNames.add(messageForKeyAdmin("labelPackageName"));
		columnNames.add(messageForKeyAdmin("labelDurationDays"));
		columnNames.add(messageForKeyAdmin("labelPrice"));
		columnNames.add(messageForKeyAdmin("labelCarType"));
		columnNames.add(messageForKeyAdmin("labelOrderId"));
		columnNames.add(messageForKeyAdmin("labelPackageStartTime"));
		columnNames.add(messageForKeyAdmin("labelPackageEndTime"));
		columnNames.add(messageForKeyAdmin("labelStatus"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {
			vendorId = DropDownUtils.parserForAllOptions(vendorId);
		}

		String[] vendorIds = DropDownUtils.getUserAccessWiseVendorListDatatable(vendorId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));

		if (driverId.isEmpty()) {
			driverId = null;
		}

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<DriverSubscriptionPackageHistoryModel> driverSubscriptionPackageHistoryList = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryForSearch(0, 0, searchString, startDateLong, endDateLong, vendorIds, driverId);

		int i = 1;
		int counter = 1;
		long timeNow = DateUtils.nowAsGmtMillisec();

		for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : driverSubscriptionPackageHistoryList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(driverSubscriptionPackageHistoryModel.getDriverName());
			row.createCell(2).setCellValue(driverSubscriptionPackageHistoryModel.getVendorName());
			row.createCell(3).setCellValue(driverSubscriptionPackageHistoryModel.getPackageName());
			row.createCell(4).setCellValue(driverSubscriptionPackageHistoryModel.getDurationDays());
			row.createCell(5).setCellValue(driverSubscriptionPackageHistoryModel.getPrice());
			row.createCell(6).setCellValue(driverSubscriptionPackageHistoryModel.getCarType());
			row.createCell(7).setCellValue(driverSubscriptionPackageHistoryModel.getOrderId());
			row.createCell(8).setCellValue(DateUtils.dbTimeStampToSesionDate(driverSubscriptionPackageHistoryModel.getPackageStartTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			row.createCell(9).setCellValue(DateUtils.dbTimeStampToSesionDate(driverSubscriptionPackageHistoryModel.getPackageEndTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if (driverSubscriptionPackageHistoryModel.getPackageStartTime() <= timeNow && timeNow <= driverSubscriptionPackageHistoryModel.getPackageEndTime()) {
				row.createCell(10).setCellValue(messageForKeyAdmin("labelActive"));
			} else if (driverSubscriptionPackageHistoryModel.getPackageStartTime() >= timeNow) {
				row.createCell(10).setCellValue(messageForKeyAdmin("labelNotYetActive"));
			} else {
				row.createCell(10).setCellValue(messageForKeyAdmin("labelExpiredPackage"));
			}

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	// Done
	@Path("/driver-transaction-history-reports")
	@GET
	//@formatter:off
	public void driverTransactionHistoryReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate,
		@QueryParam("searchString") String searchString,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId, 	
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("driver_transaction_history_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add(messageForKeyAdmin("labelSrNo"));
		columnNames.add(messageForKeyAdmin("labelDriverName"));
		columnNames.add(messageForKeyAdmin("labelVendorName"));
		columnNames.add(messageForKeyAdmin("labelOrderId"));
		columnNames.add(messageForKeyAdmin("labelPaymentMode"));
		columnNames.add(messageForKeyAdmin("labelTransactionType"));
		columnNames.add(messageForKeyAdmin("labelPaymentType"));
		columnNames.add(messageForKeyAdmin("labelAmount"));
		columnNames.add(messageForKeyAdmin("labelStatus"));
		columnNames.add(messageForKeyAdmin("labelDateAndTime"));

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {
			vendorId = DropDownUtils.parserForAllOptions(vendorId);
		}

		String[] vendorIds = DropDownUtils.getUserAccessWiseVendorListDatatable(vendorId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE));

		if (driverId.isEmpty()) {
			driverId = null;
		}

		searchString = MyHubUtils.getSearchStringFormat(searchString);

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<DriverTransactionHistoryModel> driverTransactionHistoryList = DriverTransactionHistoryModel.getTransactionListForSearch(0, 0, searchString, startDateLong, endDateLong, vendorIds, driverId);

		int i = 1;
		int counter = 1;

		for (DriverTransactionHistoryModel driverTransactionHistoryModel : driverTransactionHistoryList) {

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(driverTransactionHistoryModel.getDriverName());
			row.createCell(2).setCellValue(driverTransactionHistoryModel.getVendorName());
			row.createCell(3).setCellValue(driverTransactionHistoryModel.getOrderId());
			row.createCell(4).setCellValue(driverTransactionHistoryModel.getPaymentType());
			row.createCell(5).setCellValue(driverTransactionHistoryModel.getTransactionType());
			row.createCell(6).setCellValue(driverTransactionHistoryModel.isDebit() ? ProjectConstants.TRANSACTION_LOG_TYPE_DEBIT : ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT);
			row.createCell(7).setCellValue(StringUtils.valueOf(driverTransactionHistoryModel.getAmount()));
			row.createCell(8).setCellValue(driverTransactionHistoryModel.getStatus());
			row.createCell(9).setCellValue(DateUtils.dbTimeStampToSesionDate(driverTransactionHistoryModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}
	
	
	@Path("/sample-csv-format")
	@GET
	//@formatter:off
	public void sampleCSVFormat(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@Context ServletContext context
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		String fileName = "products_" + DateUtils.nowAsGmtMillisec() + ".csv";

		File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("csvDir") + fileName);

		if (!fileToWrite.exists()) {
			fileToWrite.createNewFile();
		}

		CSVWriter csvWrite = new CSVWriter(new FileWriter(fileToWrite));
		
		String[] entries = { FieldConstants.PRODUCT_BARCODE, FieldConstants.PRODUCT_CATEGORY,
					FieldConstants.PRODUCT_SUB_CATEGORY,FieldConstants.PRODUCT_NAME, FieldConstants.PRODUCT_INFORMATION, 
					FieldConstants.PRODUCT_ACTUAL_PRICE, FieldConstants.PRODUCT_DISCOUNTED_PRICE,
					FieldConstants.PRD_QTY_TYPE, FieldConstants.PRODUCT_WEIGHT,
					FieldConstants.PRODUCT_WEIGHT_UNIT, FieldConstants.PRODUCT_INVENTORY_COUNT,
					FieldConstants.NON_VEG, FieldConstants.GST,
					FieldConstants.PRODUCT_SPECIFICATION, FieldConstants.ACTION };
		String[] values = {"890107200","FRUITS & VEGETABLES", "FRUITS","APPLE","APPLE","100","90","L","1000","1","10","NO","5","Organic Apple","1" };
		
		csvWrite.writeNext(entries);
		csvWrite.writeNext(values);
		csvWrite.flush();
		csvWrite.close();

		try (FileInputStream fis = new FileInputStream(fileToWrite); BufferedInputStream in = new BufferedInputStream(fis)) {

			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", " attachment; filename=" + fileToWrite.getName());

			try (ServletOutputStream out = response.getOutputStream()) {

				byte[] buffer = new byte[4 * 1024];
				int data;

				while ((data = in.read(buffer)) != -1) {
					out.write(buffer, 0, data);
				}

				out.flush();

				if (fileToWrite != null && fileToWrite.exists()) {
					fileToWrite.delete();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	@Path("/phonepe-log-reports")
	@GET
	//@formatter:off
	public void phonepeLogReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@Context ServletContext context,
		@QueryParam("startDate") String startDate,  
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("phonepe_logs_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Payment Request Type");
		columnNames.add("User Id");
		columnNames.add("Tracking Id");
		columnNames.add("Amount");
		columnNames.add("Order Status");
		columnNames.add("Payment Mode");
		columnNames.add("Date");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<PhonepePaymentModel> phonepePaymentModelList = PhonepePaymentModel.getPhonepeLogsReport("%%", startDatelong, endDatelong);

		int i = 1;
		int counter = 1;

		for (PhonepePaymentModel phonepePaymentModel : phonepePaymentModelList) {

			HSSFRow row = sheet.createRow((short) i);
			row.createCell(0).setCellValue(counter);
			row.createCell(1).setCellValue(phonepePaymentModel.getPaymentRequestType());
			row.createCell(2).setCellValue(phonepePaymentModel.getFullName());
			row.createCell(3).setCellValue(phonepePaymentModel.getTransactionId());
			row.createCell(4).setCellValue(phonepePaymentModel.getAmount());
			row.createCell(5).setCellValue(phonepePaymentModel.getPaymentStatus());
			row.createCell(6).setCellValue(phonepePaymentModel.getPaymentInstrumentType());
			row.createCell(7).setCellValue(DateUtils.dbTimeStampToSesionDate(phonepePaymentModel.getUpdatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			i++;
			counter++;
		}

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}
	
	
	@Path("/warehouse-users-details")
	@GET
	//@formatter:off
	public void loadWarehouseUsersForReport(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam("startDate") String startDate, 
		@QueryParam("endDate") String endDate
		) throws ServletException, IOException, JSONException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		File fileToWrite = createFileToExport("warehouse_users_details_report_");

		List<String> columnNames = new ArrayList<>();
		columnNames.add("Sr No");
		columnNames.add("Name");
		columnNames.add("Phone Number");
		columnNames.add("Email Address");
		columnNames.add("Joining Date");

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("new sheet");
		createSheet(hwb, sheet, columnNames, 3);

		List<String> columnHeaderNames = new ArrayList<>();
		columnHeaderNames.add("Start Date");
		columnHeaderNames.add("End Date");
		createRowWithData(sheet, columnHeaderNames, 0);

		String timeZone = TimeZoneUtils.getTimeZone();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		List<UserModel> userModelList = UserModel.getUserListForSearch(0, 0, "", UserRoles.WAREHOUSE_ROLE_ID, "%%", startDatelong, endDatelong, null, null, null);

		int i = 4;
		int srNo = 1;

		for (UserModel userModel : userModelList) {

			HSSFRow row = sheet.createRow((short) i);

			row.createCell(0).setCellValue(srNo);
			row.createCell(1).setCellValue(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			row.createCell(2).setCellValue(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
			row.createCell(3).setCellValue(userModel.getEmail());
			row.createCell(4).setCellValue(DateUtils.dbTimeStampToSesionDate(userModel.getCreatedAt(), timeZone, DateUtils.DISPLAY_DATE_TIME_FORMAT));

			i++;
			srNo++;
		}

		List<String> columnHeaderValues = new ArrayList<>();
		columnHeaderValues.add(startDate);
		columnHeaderValues.add(endDate);

		createRowWithData(sheet, columnHeaderValues, 1);

		writeToSheet(response, fileToWrite, hwb, sheet, columnNames);
	}

	private void createSheet(HSSFWorkbook hwb, HSSFSheet sheet, List<String> columnNames, int rowStartPosition) {

		for (int columnPosition = 0; columnPosition < columnNames.size(); columnPosition++) {
			sheet.autoSizeColumn((short) (columnPosition));
		}

		HSSFCellStyle style = hwb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.LIME.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFFont font = hwb.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);

		HSSFRow rowhead = sheet.createRow((short) rowStartPosition);

		for (int i = 0; i < columnNames.size(); i++) {
			HSSFCell cell = rowhead.createCell(i);
			cell.setCellValue(columnNames.get(i));
			cell.setCellStyle(style);
		}
	}

	private void writeToSheet(HttpServletResponse response, File fileToWrite, HSSFWorkbook hwb, HSSFSheet sheet, List<String> columnNames) throws FileNotFoundException, IOException {

		for (int columnPosition = 0; columnPosition < columnNames.size(); columnPosition++) {
			sheet.autoSizeColumn((short) (columnPosition));
		}

		try (FileOutputStream fileOut = new FileOutputStream(fileToWrite)) {
			hwb.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try (FileInputStream fis = new FileInputStream(fileToWrite); BufferedInputStream in = new BufferedInputStream(fis)) {

			response.setContentType("text/xls");
			response.setHeader("Content-Disposition", " attachment; filename=" + fileToWrite.getName());

			try (ServletOutputStream out = response.getOutputStream()) {

				byte[] buffer = new byte[4 * 1024];
				int data;

				while ((data = in.read(buffer)) != -1) {
					out.write(buffer, 0, data);
				}

				out.flush();

				if (fileToWrite != null && fileToWrite.exists()) {
					fileToWrite.delete();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private File createFileToExport(String name) throws IOException {

		File fileToWrite = new File(WebappPropertyUtils.getWebAppProperty("tempDir") + name + DateUtils.nowAsGmtMillisec() + ".xls");

		if (!fileToWrite.exists()) {
			fileToWrite.createNewFile();
		}

		return fileToWrite;
	}

	private void createRowWithData(HSSFSheet sheet, List<String> data, int position) {

		HSSFRow head = sheet.createRow((int) position);

		for (int i = 0; i < data.size(); i++) {
			head.createCell(i).setCellValue(data.get(i));
		}
	}
}