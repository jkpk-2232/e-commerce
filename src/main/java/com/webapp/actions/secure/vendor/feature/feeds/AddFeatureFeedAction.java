package com.webapp.actions.secure.vendor.feature.feeds;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.FeedFareModel;
import com.webapp.models.FeedSettingsModel;
import com.webapp.models.VendorFeatureFeedModel;

@Path("/add-feature-feed")
public class AddFeatureFeedAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorListOptions(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, ProjectConstants.UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(false, "1", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_FEATURE_FEEDS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.FEED_NAME) String feedName,
		@FormParam(FieldConstants.FEED_MESSAGE) String feedMessage,
		@FormParam(FieldConstants.FEED_BANNER_HIDDEN_IMAGE) String hiddenFeedBanerImage,
		@FormParam(FieldConstants.ESTIMATED_COST) String estimatedCost,
		@FormParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		@FormParam(FieldConstants.SERVICE_ID) List<String> serviceIds,
		@FormParam(FieldConstants.START_DATE) String startDate,
		@FormParam(FieldConstants.END_DATE) String endDate,
		@FormParam(FieldConstants.REGION_LIST) String regionList
		) throws ServletException, SQLException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String vendorIdOptions = DropDownUtils.getVendorListOptions(vendorId, ProjectConstants.UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), true);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(false, "1", ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		data.put(FieldConstants.FEED_NAME, feedName);
		data.put(FieldConstants.FEED_MESSAGE, feedMessage);
		data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE, hiddenFeedBanerImage);
		data.put(FieldConstants.START_DATE, startDate);
		data.put(FieldConstants.END_DATE, endDate);
		data.put(FieldConstants.REGION_LIST, regionList);
		data.put(FieldConstants.SERVICE_ID, serviceIds.toString());

		if (hasErrors()) {

			data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, StringUtils.validString(hiddenFeedBanerImage) ? hiddenFeedBanerImage : ProjectConstants.DEFAULT_IMAGE);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_FEATURE_FEEDS_JSP);
		}

		FeedSettingsModel feedSettingsModel = FeedSettingsModel.getfeedSettingsByMultiCityRegionId(regionList);

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

		List<VendorFeatureFeedModel> feedModels = new ArrayList<>();

		if (feedSettingsModel != null) {

			for (String serviceId : serviceIds) {

				Double finalAmount = 0D;

				FeedFareModel feedFareDetails = FeedFareModel.getFeedFareDetailsByFeedSettingsIdAndServiceId(feedSettingsModel.getFeedSettingsId(), serviceId);
				if (feedFareDetails != null) {
					finalAmount = feedFareDetails.getBaseFare() + feedFareDetails.getPerMinuteFare() * diff;
					double gstAmount = (finalAmount * feedFareDetails.getGSTPercentage()) / 100;
					finalAmount = finalAmount + gstAmount;
				}

				VendorFeatureFeedModel vendorFeedModel = new VendorFeatureFeedModel();
				vendorFeedModel.setVendorFeedId(UUIDGenerator.generateUUID());
				vendorFeedModel.setVendorId(vendorId);
				vendorFeedModel.setFeedName(feedName);
				vendorFeedModel.setFeedMessage(feedMessage);
				vendorFeedModel.setFeedBaner(hiddenFeedBanerImage);
				vendorFeedModel.setVendorStoreId(vendorStoreId);
				vendorFeedModel.setServiceId(serviceId);
				vendorFeedModel.setEstimatedCost(finalAmount);
				vendorFeedModel.setStartDate(DateUtils.getDateFromString(startDate, DateUtils.DATE_FORMAT_FOR_VIEW));
				vendorFeedModel.setEndDate(DateUtils.getDateFromString(endDate, DateUtils.DATE_FORMAT_FOR_VIEW));
				vendorFeedModel.setRegion(regionList);
				vendorFeedModel.setCreatedBy(loginSessionMap.get(LoginUtils.USER_ID));
				vendorFeedModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
				vendorFeedModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				vendorFeedModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

				feedModels.add(vendorFeedModel);

			}
		}

		if (feedModels.size() > 0) {
			VendorFeatureFeedModel.insertFeeds(feedModels);
		}

		// FeedUtils.insertFeed(vendorId, feedName, feedMessage, hiddenFeedBanerImage,
		// regionList, vendorStoreId, serviceId, estimatedCost, startDate, endDate,
		// loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEATURE_FEEDS_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FEED_NAME, BusinessAction.messageForKeyAdmin("labelFeedName"), new MinMaxLengthValidationRule(1, 30));
		validator.addValidationMapping(FieldConstants.FEED_MESSAGE, BusinessAction.messageForKeyAdmin("labelFeedMessage"), new MinMaxLengthValidationRule(1, 250));
		validator.addValidationMapping(FieldConstants.START_DATE, messageForKeyAdmin("labelStartDate"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.END_DATE, messageForKeyAdmin("labelEndDate"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = new ArrayList<String>();
		requiredJS.add(UrlConstants.JS_URLS.ADD_VENDOR_FEATURE_FEEDS_JS);
		requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredCss() {

		List<String> requiredCSS = new ArrayList<String>();

		requiredCSS.add("new-ui/css/bootstrap-material-datetimepicker.css");

		return requiredCSS.toArray(new String[requiredCSS.size()]);
	}

}
