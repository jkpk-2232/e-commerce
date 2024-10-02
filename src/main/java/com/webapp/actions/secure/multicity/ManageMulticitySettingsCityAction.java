package com.webapp.actions.secure.multicity;

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
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-multicity-settings-city")
public class ManageMulticitySettingsCityAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getMulticitySettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		)throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_MULTICITY_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_MULTICITY_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMulticitySettingsList(
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

		int total = MulticityCityRegionModel.getMulticityCityRegionCount(ProjectConstants.DEFAULT_COUNTRY_ID);
		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionSearchDatatable(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), ProjectConstants.DEFAULT_COUNTRY_ID);

		int count = dtu.getStartInt();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(multicityCityRegionModel.getMulticityCityRegionId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(multicityCityRegionModel.getCityDisplayName());
			dtuInnerJsonArray.put(multicityCityRegionModel.getRegionRadius());

			if (multicityCityRegionModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_MULTICITY_URL + "?multicityCityRegionId=" + multicityCityRegionModel.getMulticityCityRegionId())));

			if (!multicityCityRegionModel.getMulticityCityRegionId().equalsIgnoreCase(WebappPropertyUtils.DEFAULT_REGION_ID)) {

				if (multicityCityRegionModel.isActive()) {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_MULTICITY_ACTIVATE_DEACTIVATE_URL + "?multicityCityRegionId=" + multicityCityRegionModel.getMulticityCityRegionId() + "&status=active")));
				} else {

					btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
								UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_MULTICITY_ACTIVATE_DEACTIVATE_URL + "?multicityCityRegionId=" + multicityCityRegionModel.getMulticityCityRegionId() + "&status=deactive")));
				}

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_MULTICITY_DELETE_URL + "?multicityCityRegionId=" + multicityCityRegionModel.getMulticityCityRegionId())));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? multicityCityRegionModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateMulticityRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		@QueryParam(FieldConstants.STATUS) String status
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		MulticityCityRegionModel multicityCityRegionModel = new MulticityCityRegionModel();

		multicityCityRegionModel.setMulticityCityRegionId(multicityCityRegionId);

		if ("active".equals(status)) {

			multicityCityRegionModel.setActive(false);
			multicityCityRegionModel.setDeleted(true);

			multicityCityRegionModel.deleteRegion();

		} else {

			multicityCityRegionModel.setActive(true);
			multicityCityRegionModel.setDeleted(false);

			multicityCityRegionModel.activateRegion();
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_MULTICITY_URL);
	}

	@Path("/delete")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteMulticityRegion(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		MulticityCityRegionModel multicityCityRegionModel = new MulticityCityRegionModel();
		multicityCityRegionModel.setMulticityCityRegionId(multicityCityRegionId);
		multicityCityRegionModel.permanentDeleteRegion();

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_MULTICITY_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_MULTICITY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}