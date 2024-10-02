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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.TaxModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-tax")
public class ManageTaxAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadTaxSettings(
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

		data.put("labelManageTax", messageForKeyAdmin("labelManageTax"));

		data.put("labelTaxName", messageForKeyAdmin("labelTaxName"));
		data.put("labelTaxPercentage", messageForKeyAdmin("labelTaxPercentage"));

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_TAX_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_TAX_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getannouncementDetailsList(
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

		int total = TaxModel.getTaxCount();
		List<TaxModel> taxModelList = TaxModel.getTaxListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage());

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (TaxModel taxModel : taxModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(taxModel.getTaxId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(taxModel.getTaxName());
			dtuInnerJsonArray.put(taxModel.getTaxPercentage());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(taxModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW));

			if (taxModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_TAX_URL + "?taxId=" + taxModel.getTaxId())));

			if (taxModel.isActive()) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_TAX_ACTIVATE_DEACTIVATE_URL + "?taxId=" + taxModel.getTaxId() + "&currentStatus=active")));
			} else {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_TAX_ACTIVATE_DEACTIVATE_URL + "?taxId=" + taxModel.getTaxId() + "&currentStatus=deactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_TAX_DELETE_URL + "?taxId=" + taxModel.getTaxId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = TaxModel.getTotalTaxCountBySearch(dtu.getGlobalSearchStringWithPercentage());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/delete-tax")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteTax(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TAX_ID) String taxId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		TaxModel taxModel = new TaxModel();
		taxModel.setTaxId(taxId);
		taxModel.deleteTax(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_TAX_URL);
	}

	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response activateDeactivateTax(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TAX_ID) String taxId,
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

		TaxModel taxModel = new TaxModel();
		taxModel.setTaxId(taxId);

		if ("active".equals(currentStatus)) {
			taxModel.setActive(false);
		} else {
			taxModel.setActive(true);
		}

		taxModel.updateTaxStatus(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_TAX_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_TAX_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}