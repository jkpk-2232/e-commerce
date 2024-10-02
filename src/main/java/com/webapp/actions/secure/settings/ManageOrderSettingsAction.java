package com.webapp.actions.secure.settings;

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
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.OrderSettingModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-order-settings")
public class ManageOrderSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getOrderSettings(
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

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ORDER_SETTINGS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getOrderSettingsList(
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
		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);
		serviceId = DropDownUtils.parserForAllOptions(serviceId);

		int total = OrderSettingModel.getOrderSettingCount(serviceId);
		List<OrderSettingModel> orderSettingsList = OrderSettingModel.getOrderSettingSearch(dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), "os.updated_at", serviceId);

		int count = dtu.getStartInt();

		for (OrderSettingModel orderSettingModel : orderSettingsList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(orderSettingModel.getServiceId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(orderSettingModel.getServiceName());
			dtuInnerJsonArray.put(StringUtils.valueOf(orderSettingModel.getMaxNumberOfItems()));
			dtuInnerJsonArray.put(StringUtils.valueOf(orderSettingModel.getMaxWeightAllowed()));
			dtuInnerJsonArray.put(orderSettingModel.getFreeCancellationTimeMins());
			dtuInnerJsonArray.put(orderSettingModel.getOrderJobCancellationTimeHours());
			dtuInnerJsonArray.put(orderSettingModel.getOrderNewCancellationTimeHours());
			dtuInnerJsonArray.put(StringUtils.valueOf(orderSettingModel.getDeliveryBaseFee()));
			dtuInnerJsonArray.put(StringUtils.valueOf(orderSettingModel.getDeliveryBaseKm()));
			dtuInnerJsonArray.put(StringUtils.valueOf(orderSettingModel.getDeliveryFeePerKm()));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_ORDER_SETTINGS_URL + "?serviceId=" + orderSettingModel.getServiceId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = OrderSettingModel.getOrderSettingSearchCount(dtu.getGlobalSearchStringWithPercentage(), serviceId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ORDER_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}