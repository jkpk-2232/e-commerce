package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
import com.utils.myhub.MultiTenantUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.CarTypeModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-dynamic-cars")
public class ManageDynamicCarsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDynamicCar(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_DYNAMIC_CAR_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DYNAMIC_CARS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDynamicCarList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		int total = CarTypeModel.getCarTypeCount();
		List<CarTypeModel> carTypeList = CarTypeModel.getCarTypeListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage());

		int count = dtu.getStartInt();

		for (CarTypeModel carTypeModel : carTypeList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(carTypeModel.getCarTypeId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(carTypeModel.getCarType());
			dtuInnerJsonArray.put(carTypeModel.isPredefinedCar() ? "Predefined System Car Type" : "Dynamic Car Type");

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_DYNAMIC_CAR_URL + "?carTypeId=" + carTypeModel.getCarTypeId())));

			if (carTypeModel.isActive()) {

				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, ProjectConstants.ACTIVE));

				if (!carTypeModel.isPredefinedCar()) {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DYNAMIC_CARS_ACTIVATE_DEACTIVATE_URL + "?carTypeId=" + carTypeModel.getCarTypeId() + "&currentStatus=active")));

				} else {
					btnGroupStr.append(ProjectConstants.NOT_AVAILABLE);
				}

			} else {

				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, ProjectConstants.DEACTIVE));

				if (!carTypeModel.isPredefinedCar()) {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_DYNAMIC_CARS_ACTIVATE_DEACTIVATE_URL + "?carTypeId=" + carTypeModel.getCarTypeId() + "&currentStatus=deactive")));

				} else {
					btnGroupStr.append(ProjectConstants.NOT_AVAILABLE);
				}
			}

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = CarTypeModel.getCarTypeListCountForSearch(dtu.getGlobalSearchStringWithPercentage());
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
	public Response activateDeactivateDynamicCars(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.CAR_TYPE_ID) String carTypeId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		carTypeId = carTypeId.trim();

		CarTypeModel carType = CarTypeModel.getCarTypeByCarTypeId(carTypeId);

		if ("active".equals(currentStatus)) {
			carType.setActive(false);
			carType.setDeleted(true);
		} else {
			carType.setActive(true);
			carType.setDeleted(false);
		}

		carType.updateCarTypeStatus(loginSessionMap.get(LoginUtils.USER_ID));

		MultiTenantUtils.updateVendorCarTypeStatusByCarTypeId(carTypeId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DYNAMIC_CARS_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DYNAMIC_CARS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}