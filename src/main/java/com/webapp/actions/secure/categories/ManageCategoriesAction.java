package com.webapp.actions.secure.categories;

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
import com.webapp.models.CategoryModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-categories")
public class ManageCategoriesAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadManageCategoriesGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, StringUtils.validString(serviceId) ? serviceId : ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_CATEGORIES_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_CATEGORIES_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadManageCategoriesListGet(
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

		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);
		serviceId = DropDownUtils.parserForAllOptions(serviceId);

		int total = CategoryModel.getCategoryCount(dtu.getStartDatelong(), dtu.getEndDatelong(), serviceId);
		List<CategoryModel> categoryList = CategoryModel.getCategorySearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), serviceId, null);

		int count = dtu.getStartInt();

		for (CategoryModel categoryModel : categoryList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(categoryModel.getCategoryId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(categoryModel.getCategoryName());
			dtuInnerJsonArray.put(categoryModel.getCategoryDescription());
			dtuInnerJsonArray.put(categoryModel.getServiceName());

			if (categoryModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_CATEGORIES_URL + "?categoryId=" + categoryModel.getCategoryId())));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelViewVendorsByCategories"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDORS_URL + "?serviceId=" + categoryModel.getServiceId() + "&categoryId=" + categoryModel.getCategoryId()), UrlConstants.JSP_URLS.MANAGE_VENDORS_ICON));

			if (categoryModel.isActive()) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_ACTIVATE_DEACTIVATE_URL + "?categoryId=" + categoryModel.getCategoryId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_ACTIVATE_DEACTIVATE_URL + "?categoryId=" + categoryModel.getCategoryId() + "&currentStatus=deactive")));
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = CategoryModel.getCategorySearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), serviceId);
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
	public Response acivateDeactivateCategory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.CATEGORY_ID) String categoryId,
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

		CategoryModel categoryModel = new CategoryModel();
		categoryModel.setCategoryId(categoryId);

		if ("active".equals(currentStatus)) {
			categoryModel.setActive(false);
			categoryModel.setDeleted(true);
		} else {
			categoryModel.setActive(true);
			categoryModel.setDeleted(false);
		}

		categoryModel.updateCategoryStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_CATEGORIES_URL);
	}

	@Path("/categories-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCategoryList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SHOW_ALL) String showAll,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (serviceId.equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE)) {
			serviceId = null;
		}

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String categoryIdOptions = DropDownUtils.getCategoryList(Boolean.parseBoolean(showAll), null, serviceId, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		output.put(FieldConstants.CATEGORY_ID_OPTIONS, categoryIdOptions);

		return sendDataResponse(output);
	}

	@Path("/categories-list-add-edit")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCategoryListEdit(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DISPLAY_TYPE) String displayType,
		@QueryParam(FieldConstants.SHOW_ALL) String showAll,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SERVICES_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (serviceId.equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE)) {
			serviceId = null;
		}

		Map<String, String> output = new HashMap<>();
		output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String categoryIdOptions = DropDownUtils.getCategoryList(Boolean.parseBoolean(showAll), displayType, serviceId, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		output.put(FieldConstants.CATEGORY_ID_OPTIONS, categoryIdOptions);

		return sendDataResponse(output);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_CATEGORIES_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}