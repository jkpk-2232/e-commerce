package com.webapp.actions.secure.adminreports;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.InvoiceModel;

@Path("/manage-refund-reports")
public class RefundReportsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getRefundReports(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_REFUND_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_REFUND_REPORTS_JSP);
	}

	@Path("/refund-payable-info")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRefundPayableInfo(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId,
		@QueryParam(FieldConstants.START_DATE) String startDate,
		@QueryParam(FieldConstants.END_DATE) String endDate
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.REPORTS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, timeZone);

		double amountRefunded = InvoiceModel.getAllRefundedTripsAmount(startDatelong, endDatelong, assignedRegionList);

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outputMap.put(FieldConstants.AMOUNT_REFUNDED, adminSettings.getCurrencySymbolHtml() + StringUtils.valueOf(amountRefunded));
		return sendDataResponse(outputMap);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRefundReportsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PASSENGER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = InvoiceModel.getTotalInvoicesByDate(dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);
		List<InvoiceModel> invoiceList = InvoiceModel.getRefundedIvoiceListBySearchAndDateReports(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), null, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (InvoiceModel invoiceModel : invoiceList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(invoiceModel.getTourId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(invoiceModel.getUserTourId());

			if (StringUtils.validString(invoiceModel.getSourceAddess())) {
				dtuInnerJsonArray.put(MyHubUtils.getTrimmedTo(invoiceModel.getSourceAddess()));
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(invoiceModel.getDestiAddess())) {
				dtuInnerJsonArray.put(MyHubUtils.getTrimmedTo(invoiceModel.getDestiAddess()));
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(invoiceModel.getPassengerFullName() != null ? invoiceModel.getPassengerFullName() : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(invoiceModel.getDriverFullName() != null ? invoiceModel.getDriverFullName() : ProjectConstants.NOT_AVAILABLE);
			dtuInnerJsonArray.put(invoiceModel.getBookingType());
			dtuInnerJsonArray.put(invoiceModel.getPaymentMode());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(invoiceModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));
			dtuInnerJsonArray.put(StringUtils.valueOf(invoiceModel.getCharges()));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(invoiceModel.getUpdatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));
			dtuInnerJsonArray.put(StringUtils.valueOf(invoiceModel.getRefundAmount()));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? invoiceList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_REFUND_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}