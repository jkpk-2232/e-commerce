package com.webapp.actions.secure.superservices;

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

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.ServiceModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-super-services")
public class ManageSuperServicesAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getSuperServices(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(true, null);
		data.put(FieldConstants.SERVICE_TYPE_ID_OPTIONS, serviceTypeIdOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_SUPER_SERVICE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_SUPER_SERVICES_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getSuperServicesList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		Map<String, String> sortingMappings = new HashMap<>();
		sortingMappings.put(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, "s.updated_at");
		sortingMappings.put("2", "s.service_name");
		sortingMappings.put("3", "s.service_description");
		sortingMappings.put("4", "st.service_type_name");
		sortingMappings.put("5", "s.service_priority");
		String sortColumnAndDir = dtu.setAndGetSortColumnAndDir(dtu, sortingMappings);

		String serviceTypeId = dtu.getRequestParameter(FieldConstants.SERVICE_TYPE_ID);
		serviceTypeId = DropDownUtils.parserForAllOptions(serviceTypeId);

		int total = ServiceModel.getServiceCount(dtu.getStartDatelong(), dtu.getEndDatelong(), serviceTypeId);
		List<ServiceModel> serviceList = ServiceModel.getServiceSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), null, sortColumnAndDir, serviceTypeId);

		int count = dtu.getStartInt();

		for (ServiceModel serviceModel : serviceList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(serviceModel.getServiceId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(serviceModel.getServiceName());
			dtuInnerJsonArray.put(serviceModel.getServiceDescription());
			dtuInnerJsonArray.put(serviceModel.getServiceTypeName());
			dtuInnerJsonArray.put(serviceModel.getServicePriority());

			if (serviceModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			if (!serviceModel.isDefault()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_SUPER_SERVICE_URL + "?serviceId=" + serviceModel.getServiceId())));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewCategories"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_URL + "?serviceId=" + serviceModel.getServiceId())));

			if (!serviceModel.isDefault()) {

				if (serviceModel.isActive()) {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_ACTIVATE_DEACTIVATE_URL + "?serviceId=" + serviceModel.getServiceId() + "&currentStatus=active")));
				} else {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_ACTIVATE_DEACTIVATE_URL + "?serviceId=" + serviceModel.getServiceId() + "&currentStatus=deactive")));
				}
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = ServiceModel.getServiceSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), serviceTypeId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateSuperServices(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		ServiceModel serviceModel = new ServiceModel();
		serviceModel.setServiceId(serviceId);

		if ("active".equals(currentStatus)) {
			serviceModel.setActive(false);
			serviceModel.setDeleted(true);
		} else {
			serviceModel.setActive(true);
			serviceModel.setDeleted(false);
		}

		serviceModel.updateServiceStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SUPER_SERVICES_URL);
	}

	@Path("/service-type-vendors")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getServiceTypeVendors(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DISPLAY_TYPE) String displayType,
		@QueryParam(FieldConstants.SHOW_ALL) String showAll,
		@QueryParam(FieldConstants.SERVICE_TYPE_ID) String serviceTypeId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(Boolean.parseBoolean(showAll), assignedRegionList, Arrays.asList(serviceTypeId), ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		output.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		return sendDataResponse(output);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_SUPER_SERVICES_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}