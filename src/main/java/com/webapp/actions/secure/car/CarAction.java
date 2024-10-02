package com.webapp.actions.secure.car;

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
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.CarModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-cars")
public class CarAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getCars(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String approvelOptions = DropDownUtils.getApprovelSearchList("0");
		data.put(FieldConstants.APPROVEL_OPTIONS, approvelOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_CAR_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_CAR_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String approvel = dtu.getRequestParameter(FieldConstants.APPROVEL);

		String approvelCheck = null;
		boolean approvelStatus = true;

		if (approvel.equalsIgnoreCase("true")) {
			approvelCheck = "YES";
			approvelStatus = true;
		} else if (approvel.equalsIgnoreCase("false")) {
			approvelCheck = "YES";
			approvelStatus = false;
		}

		int total = 0;
		List<CarModel> carModelList = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			total = CarModel.getVendorTotalCarCount(dtu.getStartDatelong(), dtu.getEndDatelong(), approvelCheck, approvelStatus, loginSessionMap.get(LoginUtils.USER_ID));
			carModelList = CarModel.getVendorCarListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), approvelCheck, approvelStatus,
						loginSessionMap.get(LoginUtils.USER_ID));

		} else {

			total = CarModel.getTotalCarCount(dtu.getStartDatelong(), dtu.getEndDatelong());
			carModelList = CarModel.getCarListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.DRIVER_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), approvelCheck, approvelStatus);
		}

		int count = dtu.getStartInt();

		for (CarModel carModel : carModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(carModel.getCarId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(carModel.getModelName());
			dtuInnerJsonArray.put(carModel.getCarType());
			dtuInnerJsonArray.put(carModel.getCarColor());
			dtuInnerJsonArray.put(carModel.getCarPlateNo());
			dtuInnerJsonArray.put(carModel.getCarYear());
			dtuInnerJsonArray.put(carModel.getNoOfPassenger());
			dtuInnerJsonArray.put(carModel.getOwner());

			if (carModel.isApprovelStatus()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, ProjectConstants.APPROVED));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, ProjectConstants.NOT_APPROVED));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_CAR_URL + "?carId=" + carModel.getCarId())));

			if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID)) && !carModel.isApprovelStatus()) {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(carModel.getCarId(), "car", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_CAR_URL), "userApproved"));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? carModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_CAR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}