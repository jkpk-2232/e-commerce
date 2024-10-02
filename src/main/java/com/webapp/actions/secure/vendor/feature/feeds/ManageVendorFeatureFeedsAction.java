package com.webapp.actions.secure.vendor.feature.feeds;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.utils.myhub.FeedUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.FeedFareModel;
import com.webapp.models.FeedSettingsModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorFeedModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-vendor-feature-feeds")
public class ManageVendorFeatureFeedsAction extends BusinessAction {

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

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorFilterListOptions("0", ProjectConstants.UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_VENDOR_FEATURE_FEEDS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_FEATURE_FEEDS_JSP);
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

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		} else {
			vendorId = DropDownUtils.parserForAllOptionsForZero(vendorId);
		}

		int total = VendorFeedModel.getVendorFeedCount(dtu.getStartDatelong(), dtu.getEndDatelong(), vendorId);
		List<VendorFeedModel> vendorFeedList = VendorFeedModel.getVendorFeedSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), vendorId);

		int count = dtu.getStartInt();

		for (VendorFeedModel vendorFeedModel : vendorFeedList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(vendorFeedModel.getVendorFeedId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(vendorFeedModel.getVendorName());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedName());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedMessage());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedLikesCount());
			dtuInnerJsonArray.put(vendorFeedModel.getFeedViewsCount());
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(vendorFeedModel.getCreatedAt(), TimeZoneUtils.getTimeZone(), DateUtils.DISPLAY_DATE_TIME_FORMAT));
			dtuInnerJsonArray.put(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DELETE, messageForKeyAdmin("labelDelete"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_DELETE_URL + "?vendorFeedId=" + vendorFeedModel.getVendorFeedId())));

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
		@QueryParam(FieldConstants.VENDOR_FEED_ID) String vendorFeedId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		FeedUtils.deleteFeeds(vendorFeedId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL);
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

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		UserProfileModel vendorModel;
		Map<String, String> output = new HashMap<>();

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorModel = loggedInUserModelViaSession;
			output.put("restrictAddFeed", vendorModel.isVendorSubscriptionCurrentActive() ? ProjectConstants.NO : ProjectConstants.YES);
		} else {
			vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);
			output.put("restrictAddFeed", ProjectConstants.NO);
		}

		output.put(ProjectConstants.STATUS_TYPE, vendorModel.isVendorSubscriptionCurrentActive() ? ProjectConstants.STATUS_SUCCESS : ProjectConstants.STATUS_ERROR);

		return sendDataResponse(output);
	}

	@Path("/get-estimated-cost")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getEstimatedCost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.REGION_LIST) String regionList,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceIds,
		@QueryParam(FieldConstants.START_DATE) String startDate,
		@QueryParam(FieldConstants.END_DATE) String endDate
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		FeedSettingsModel feedSettingsModel = FeedSettingsModel.getfeedSettingsByMultiCityRegionId(regionList);

		String[] serviceIdArray = serviceIds.split(",");
		List<String> serviceIdList = new ArrayList<>();
		Double finalAmount = 0D;
		for (String serviceId : serviceIdArray) {
			serviceIdList.add(serviceId);
		}

		if (feedSettingsModel != null) {

			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
			long diff = 0L;
			try {
				Date sDate = formatter1.parse(startDate);
				Date eDate = formatter2.parse(endDate);
				diff = (eDate.getTime() / 60000) - (sDate.getTime() / 60000);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<FeedFareModel> feedFareList = FeedFareModel.getFeedFareListByFeedSettingsIdAndServiceId(feedSettingsModel.getFeedSettingsId(), serviceIdList);

			for (FeedFareModel feedFareModel : feedFareList) {
				finalAmount += feedFareModel.getBaseFare() + feedFareModel.getPerMinuteFare() * diff;
				double gstAmount = ((feedFareModel.getBaseFare() + feedFareModel.getPerMinuteFare() * diff) * feedFareModel.getGSTPercentage()) / 100;
				finalAmount = finalAmount + gstAmount;
			}

		} else {

		}

		data.put("estimatedCost", finalAmount.toString());

		return sendDataResponse(data);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_FEATURE_FEEDS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
