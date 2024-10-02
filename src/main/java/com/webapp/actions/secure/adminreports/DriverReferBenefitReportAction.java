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
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/driver/refer-benefit/reports")
public class DriverReferBenefitReportAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverReferBenefitReports(
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

		boolean hasExportAccess = UserExportUtils.hasExportAccess(loginSessionMap.get(LoginUtils.ROLE_ID), loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.EXPORT_ACCESS_DRIVER_BENEFIT_REPORT_ID);
		data.put(FieldConstants.IS_EXPORT_ACCESS, hasExportAccess ? ProjectConstants.TRUE_STRING : ProjectConstants.FALSE_STRING);

		return loadView(UrlConstants.JSP_URLS.DRIVER_REFER_BENEFITS_REPORTS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverReferBenefitReportsList(
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

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = UserModel.getTotalDriverCountForReferBenefitsReportsSearch(UserRoles.DRIVER_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList);

		List<UserModel> userModelList = UserModel.getDriverListForReferBenefitsReportsSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(),
					assignedRegionList);

		int count = dtu.getStartInt();

		for (UserModel userModel : userModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userModel.getFirstName(), userModel.getLastName()));
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userModel.getPhoneNoCode(), userModel.getPhoneNo()));
			dtuInnerJsonArray.put(userModel.getNoOfRefer());
			dtuInnerJsonArray.put(StringUtils.valueOf(userModel.getTotalReferralBenefits()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.DRIVER_REFER_BENEFITS_DETAIL_REPORTS_URL + "?driverId=" + userModel.getUserId())));

			if (userModel.getTotalReferralBenefits() > 0) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelReferBenefit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.DRIVER_REFER_BENEFITS_TRIP_REPORTS_URL + "?driverId=" + userModel.getUserId()),
							UrlConstants.JSP_URLS.DRIVER_REFER_BENEFITS_REPORTS_ICON));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.DRIVER_REFER_BENEFITS_REPORTS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}