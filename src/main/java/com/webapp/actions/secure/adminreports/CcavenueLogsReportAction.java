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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.CcavenueResponseLogModel;

@Path("/ccavenue/logs-reports")
public class CcavenueLogsReportAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getCcavenueReport(
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

		String ccavenueStatusFilterOptions = DropDownUtils.getCcavenueStatusFilterList(ProjectConstants.ALL_USERS_ID);
		data.put(FieldConstants.CCAVENUE_STATUS_OPTIONS, ccavenueStatusFilterOptions);

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_CCAVENUE_LOG_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.MANAGE_CCAVENUE_LOGS_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCcavenueReportList(
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
		String ccavenueStatus = dtu.getRequestParameter(FieldConstants.CCAVENUE_STATUS);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String[] ccavenueStatusArray = getCcavenueStatusArray(ccavenueStatus);

		int total = CcavenueResponseLogModel.getCcavenueResponseLogListCount(ccavenueStatusArray, null, dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);
		List<CcavenueResponseLogModel> ccavenueResponseLogModelList = CcavenueResponseLogModel.getCcavenueResponseLogListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), ccavenueStatusArray, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(),
					dtu.getEndDatelong(), assignedRegionList);

		int count = dtu.getStartInt();

		for (CcavenueResponseLogModel ccavenueResponseLogModel : ccavenueResponseLogModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(ccavenueResponseLogModel.getCcavenueResponseLogId());
			dtuInnerJsonArray.put(count);

			if (ccavenueResponseLogModel.getPaymentRequestType().equalsIgnoreCase(ProjectConstants.CCAVENUE_RSA_REQUEST_TYPE_TOUR_ID)) {

				dtuInnerJsonArray.put(ProjectConstants.TOURS);

				if (StringUtils.validString(ccavenueResponseLogModel.getUserTourId())) {
					dtuInnerJsonArray.put(ccavenueResponseLogModel.getUserTourId());
				} else {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				}

			} else {

				dtuInnerJsonArray.put(ProjectConstants.DRIVER_SUBSCRIPTION);

				if (ccavenueResponseLogModel.getShortSubscriptionId() != null && !ccavenueResponseLogModel.getShortSubscriptionId().isEmpty()) {
					dtuInnerJsonArray.put(ccavenueResponseLogModel.getShortSubscriptionId());
				} else {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				}
			}

			dtuInnerJsonArray.put(ccavenueResponseLogModel.getUserFullName());

			if (StringUtils.validString(ccavenueResponseLogModel.getTrackingId())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getTrackingId());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(ccavenueResponseLogModel.getOrderStatus())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getOrderStatus());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(ccavenueResponseLogModel.getFailureMessage())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getFailureMessage());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(ccavenueResponseLogModel.getAmount());

			if (StringUtils.validString(ccavenueResponseLogModel.getCurrency())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getCurrency());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(ccavenueResponseLogModel.getPaymentMode())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getPaymentMode());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(ccavenueResponseLogModel.getCardName())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getCardName());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(ccavenueResponseLogModel.getBankRefNo())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getBankRefNo());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(ccavenueResponseLogModel.getBankResponseCode())) {
				dtuInnerJsonArray.put(ccavenueResponseLogModel.getBankResponseCode());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? ccavenueResponseLogModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	private String[] getCcavenueStatusArray(String ccavenueStatus) {

		String[] ccavenueStatusArray;

		if ("2".equalsIgnoreCase(ccavenueStatus)) {

			ccavenueStatusArray = new String[1];
			ccavenueStatusArray[0] = messageForKeyAdmin("labelSuccess");

		} else if ("3".equalsIgnoreCase(ccavenueStatus)) {

			ccavenueStatusArray = new String[1];
			ccavenueStatusArray[0] = messageForKeyAdmin("labelFailure");

		} else if ("4".equalsIgnoreCase(ccavenueStatus)) {

			ccavenueStatusArray = new String[1];
			ccavenueStatusArray[0] = messageForKeyAdmin("labelAborted");

		} else if ("5".equalsIgnoreCase(ccavenueStatus)) {

			ccavenueStatusArray = new String[1];
			ccavenueStatusArray[0] = messageForKeyAdmin("labelInvalid");

		} else if ("6".equalsIgnoreCase(ccavenueStatus)) {

			ccavenueStatusArray = new String[1];
			ccavenueStatusArray[0] = messageForKeyAdmin("labelInitiated");

		} else {

			ccavenueStatusArray = new String[5];
			ccavenueStatusArray[0] = messageForKeyAdmin("labelSuccess");
			ccavenueStatusArray[1] = messageForKeyAdmin("labelFailure");
			ccavenueStatusArray[2] = messageForKeyAdmin("labelAborted");
			ccavenueStatusArray[3] = messageForKeyAdmin("labelInvalid");
			ccavenueStatusArray[4] = messageForKeyAdmin("labelInitiated");
		}

		return ccavenueStatusArray;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_CCAVENUE_LOGS_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}