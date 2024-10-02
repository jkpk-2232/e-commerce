package com.webapp.actions.secure.vendor.appointments;

import java.io.IOException;
import java.util.ArrayList;
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
import com.utils.myhub.AppointmentUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AppointmentModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-appointments-new")
public class ManageAppointmentsNewAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAppointmentsNew(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		String appointmentStatusFilterOptions = DropDownUtils.getAppointmentStatusList(true, ProjectConstants.AppointmentConstants.APPOINTMENTS_NEW_TAB, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.APPOINTMENT_STATUS_FILTER_OPTIONS, appointmentStatusFilterOptions);

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.AppointmentConstants.APPOINTMENTS_NEW_TAB);

		return loadView(UrlConstants.JSP_URLS.MANAGE_APPOINTMENTS_NEW_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAppointmentsNewList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_APPOINTMENT_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String type = dtu.getRequestParameter(ProjectConstants.STATUS_TYPE);
		List<String> appointmentStatus = AppointmentUtils.getAppointmentStatusListAsPerAppointmentType(type);

		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

		List<String> vendorStoreIdList = new ArrayList<>();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceId = vscm != null ? vscm.getServiceId() : null;
			vendorStoreIdList = null;
		} else if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceId = vscm != null ? vscm.getServiceId() : null;

			vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loginSessionMap.get(LoginUtils.USER_ID), false);

		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
			serviceId = DropDownUtils.parserForAllOptions(serviceId);
			vendorStoreIdList = null;
		}

		String appointmentStatusFilter = dtu.getRequestParameter(FieldConstants.APPOINTMENT_STATUS_FILTER);
		appointmentStatusFilter = DropDownUtils.parserForAllOptions(appointmentStatusFilter);

		int appointmentShortIdSearch = MyHubUtils.searchNumericFormat(dtu.getGlobalSearchString());

		int total = AppointmentModel.getAppointmentsCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId, serviceId, null, appointmentStatus, appointmentShortIdSearch, appointmentStatusFilter, vendorStoreIdList);
		List<AppointmentModel> appointmentList = AppointmentModel.getAppointmentsSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), null, vendorId, serviceId, null, appointmentStatus,
					appointmentShortIdSearch, appointmentStatusFilter, vendorStoreIdList);

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (AppointmentModel appointmentModel : appointmentList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(appointmentModel.getAppointmentId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(appointmentModel.getAppointmentShortId());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(appointmentModel.getAppointmentCreationTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(appointmentModel.getAppointmentTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			dtuInnerJsonArray.put(appointmentModel.getCustomerName());
			dtuInnerJsonArray.put(appointmentModel.getVendorName());
			dtuInnerJsonArray.put(df.format(appointmentModel.getAppointmentTotal()));
			dtuInnerJsonArray.put(df.format(appointmentModel.getAppointmentCharges()));
			dtuInnerJsonArray.put(appointmentModel.getAppointmentNumberOfItems());
			dtuInnerJsonArray.put(AppointmentUtils.getAppointmentStatusDisplayLabels(appointmentModel.getAppointmentStatus()));
			dtuInnerJsonArray.put(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelView"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.VIEW_APPOINMENTS_DETAILS_URL + "?appointmentId=" + appointmentModel.getAppointmentId() + "&type=" + type)));
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = AppointmentModel.getAppointmentsSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId, serviceId, null, appointmentStatus, appointmentShortIdSearch, appointmentStatusFilter,
						vendorStoreIdList);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_APPOINTMENTS_NEW_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}