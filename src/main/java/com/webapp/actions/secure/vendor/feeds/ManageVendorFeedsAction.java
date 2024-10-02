package com.webapp.actions.secure.vendor.feeds;

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

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorFeedModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-vendor-feeds")
public class ManageVendorFeedsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorFeeds(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_VENDOR_FEEDS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_FEEDS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorFeedsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		} else if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))){
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		int total = VendorFeedModel.getVendorFeedCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId);
		List<VendorFeedModel> vendorFeedList = VendorFeedModel.getVendorFeedSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), vendorId);

		int count = dtu.getStartInt();

		for (VendorFeedModel vendorFeedModel : vendorFeedList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorFeedModel.getVendorFeedId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorFeedModel.getVendorName());
			dtuInnerJsonArray.put(vendorFeedModel.getStoreName());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedName());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedMessage());
			dtuInnerJsonArray.put(vendorFeedModel.getMediaType());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedLikesCount());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedViewsCount());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(vendorFeedModel.getCreatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DISPLAY_DATE_TIME_FORMAT));
			/*
			dtuInnerJsonArray.put(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_DELETE_URL + "?vendorFeedId=" + vendorFeedModel.getVendorFeedId())));
			*/
			
			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelRepost"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_REPOST_URL + "?vendorFeedId=" + vendorFeedModel.getVendorFeedId()), "fas fa-lg fa-fw me-2 fa-redo"));
			
			if (!vendorFeedModel.isDeleted()) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_DELETE_URL + "?vendorFeedId=" + vendorFeedModel.getVendorFeedId() + "&currentStatus=active")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REACTIVATE, messageForKeyAdmin("labelReactivate"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_DELETE_URL + "?vendorFeedId=" + vendorFeedModel.getVendorFeedId() + "&currentStatus=deactive")));
			}
			

			dtuInnerJsonArray.put(btnGroupStr);
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = VendorFeedModel.getVendorFeedSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), vendorId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/delete")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateSurge(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_FEED_ID) String vendorFeedId,
		@QueryParam(FieldConstants.CURRENT_STATUS) String currentStatus
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorFeedModel vendorFeedModel = new VendorFeedModel();
		vendorFeedModel.setVendorFeedId(vendorFeedId);
		
		if ("active".equals(currentStatus)) {
			vendorFeedModel.setDeleted(true);
		} else {
			vendorFeedModel.setDeleted(false);
		}
		
		vendorFeedModel.updateVendorFeedStatus(loginSessionMap.get(LoginUtils.USER_ID));
		//FeedUtils.deleteFeeds(vendorFeedId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_URL);
	}

	@Path("/vendor-monthly-subscription-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorMonthlySubscriptionStatus(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		UserProfileModel vendorModel;
		Map<String, String> output = new HashMap<>();

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);

			vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

			output.put("restrictAddFeed", vendorModel.isVendorSubscriptionCurrentActive() ? ProjectConstants.NO : ProjectConstants.YES);

		} else {
			
			vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);
			
			output.put("restrictAddFeed", ProjectConstants.NO);
		}

		output.put(ProjectConstants.STATUS_TYPE, vendorModel.isVendorSubscriptionCurrentActive() ? ProjectConstants.STATUS_SUCCESS : ProjectConstants.STATUS_ERROR);

		return sendDataResponse(output);
	}
	
	@Path("/repost")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response repostVendorFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_FEED_ID) String vendorFeedId
		) throws IOException, SQLException, ServletException {
	//@formatter:on
		
		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorFeedModel vendorFeedModel = new VendorFeedModel();
		vendorFeedModel.setVendorFeedId(vendorFeedId);
		
		vendorFeedModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		
		vendorFeedModel.repostVendorFeed(loginSessionMap.get(LoginUtils.USER_ID));
		
		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_FEEDS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}