package com.webapp.actions.secure.subscription.packages;

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
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-subscription-packages")
public class ManageSubscriptionPackagesAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getSubscriptionPackage(
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

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_SUBSCRIPTION_PACKAGE_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_SUBSCRIPTION_PACKAGE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getSubscriptionPackageList(
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

		int total = SubscriptionPackageModel.getSubscriptionPackageCount();
		List<SubscriptionPackageModel> subscriptionPackageList = SubscriptionPackageModel.getSubscriptionPackageListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getGlobalSearchStringWithPercentage());

		int count = dtu.getStartInt();

		for (SubscriptionPackageModel subscriptionPackageModel : subscriptionPackageList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(subscriptionPackageModel.getSubscriptionPackageId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(subscriptionPackageModel.getPackageName());
			dtuInnerJsonArray.put(subscriptionPackageModel.getDurationDays());
			dtuInnerJsonArray.put(subscriptionPackageModel.getPrice());
			dtuInnerJsonArray.put(subscriptionPackageModel.getCarType());

			if (subscriptionPackageModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_SUBSCRIPTION_PACKAGE_URL + "?subscriptionPackageId=" + subscriptionPackageModel.getSubscriptionPackageId())));

			if (subscriptionPackageModel.isActive()) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_ACTIVATE_DEACTIVATE_URL + "?subscriptionPackageId=" + subscriptionPackageModel.getSubscriptionPackageId() + "&status=active")));

			} else {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_ACTIVATE_DEACTIVATE_URL + "?subscriptionPackageId=" + subscriptionPackageModel.getSubscriptionPackageId() + "&status=deactive")));

			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_DELETE_URL + "?subscriptionPackageId=" + subscriptionPackageModel.getSubscriptionPackageId())));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? subscriptionPackageList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/active-deactive")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateSubscriptionPackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SUBSCRIPTION_PACKAGE_ID) String subscriptionPackageId,
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

		SubscriptionPackageModel subscriptionPackageModel = new SubscriptionPackageModel();
		subscriptionPackageModel.setSubscriptionPackageId(subscriptionPackageId);

		if ("active".equals(status)) {
			subscriptionPackageModel.setActive(false);
		} else {
			subscriptionPackageModel.setActive(true);
		}

		subscriptionPackageModel.activateDeactivateSubscriptionPackage(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_URL);
	}

	@Path("/delete")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteSubscriptionPackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SUBSCRIPTION_PACKAGE_ID) String subscriptionPackageId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		SubscriptionPackageModel subscriptionPackageModel = new SubscriptionPackageModel();
		subscriptionPackageModel.setSubscriptionPackageId(subscriptionPackageId);
		subscriptionPackageModel.deleteSubscriptionPackage(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SUBSCRIPTION_PACKAGE_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_SUBSCRIPTION_PACKAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}