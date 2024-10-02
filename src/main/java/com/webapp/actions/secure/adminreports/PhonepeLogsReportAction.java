package com.webapp.actions.secure.adminreports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.PhonepePaymentModel;

@Path("/phonepe/logs-reports")
public class PhonepeLogsReportAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getPhonepeReport(
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

		String phonepeStatusFilterOptions = DropDownUtils.getPhonepeStatusFilterList(ProjectConstants.ALL_USERS_ID);
		data.put(FieldConstants.PHONEPE_STATUS_OPTIONS, phonepeStatusFilterOptions);

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_PHONEPE_LOG_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_PHONEPE_LOGS_REPORTS_JSP);
	}
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPhonepeReportList(
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
		String phonepeStatus = dtu.getRequestParameter(FieldConstants.PHONEPE_STATUS);

		String[] phonepeStatusArray = getPhonepeStatusArray(phonepeStatus);

		int total = PhonepePaymentModel.getPhonepePaymentListCount(phonepeStatusArray, null, dtu.getStartDatelong(), dtu.getEndDatelong());
		List<PhonepePaymentModel> phonepePaymentModelList = PhonepePaymentModel.getPhonepePaymentListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), phonepeStatusArray, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(),
					dtu.getEndDatelong());

		int count = dtu.getStartInt();

		for (PhonepePaymentModel phonepePaymentModel : phonepePaymentModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(phonepePaymentModel.getPaymentOrderId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(phonepePaymentModel.getPaymentRequestType());
			dtuInnerJsonArray.put(phonepePaymentModel.getFullName());
			dtuInnerJsonArray.put(phonepePaymentModel.getTransactionId());
			dtuInnerJsonArray.put(phonepePaymentModel.getAmount());
			dtuInnerJsonArray.put(phonepePaymentModel.getPaymentStatus());
			dtuInnerJsonArray.put(phonepePaymentModel.getPaymentInstrumentType());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(phonepePaymentModel.getUpdatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DISPLAY_DATE_TIME_FORMAT));
			

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? phonepePaymentModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}
	
	private String[] getPhonepeStatusArray(String phonepeStatus) {

		String[] phonepeStatusArray;

		if ("2".equalsIgnoreCase(phonepeStatus)) {

			phonepeStatusArray = new String[1];
			phonepeStatusArray[0] = "PAYMENT_PENDING";

		} else if ("3".equalsIgnoreCase(phonepeStatus)) {

			phonepeStatusArray = new String[1];
			phonepeStatusArray[0] = "PAYMENT_SUCCESS";

		} else {

			phonepeStatusArray = new String[2];
			phonepeStatusArray[0] = "PAYMENT_PENDING";
			phonepeStatusArray[1] = "PAYMENT_SUCCESS";
			
		}

		return phonepeStatusArray;
	}
	
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_PHONEPE_LOGS_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
