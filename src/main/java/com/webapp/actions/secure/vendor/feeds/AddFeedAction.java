package com.webapp.actions.secure.vendor.feeds;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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

import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.FeedUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;

@Path("/add-feed")
public class AddFeedAction extends BusinessAction {

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

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		String vendorId = "";
		String vendorStoreIdOptions;
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			
			vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(null, vendorId, assignedRegionList);
			
			data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);
		}
		
		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			
			vendorStoreIdOptions = DropDownUtils.getERPBrandsFilterListOptions(null, vendorId, assignedRegionList);
			
			data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);
			data.put(FieldConstants.VENDOR_ID, vendorId);
		}

		String vendorIdOptions = DropDownUtils.getVendorListOptions(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		
		String mediaTypeOptions = DropDownUtils.getMediaTypeOptions("");
		data.put(FieldConstants.MEDIA_TYPE_OPTIONS, mediaTypeOptions);
		
		String isSponsoredOptions = DropDownUtils.getYesNoOption(false + "");
		data.put(FieldConstants.IS_SPONSORED_OPTIONS , isSponsoredOptions);
		
		data.put("cdnUrl", WebappPropertyUtils.CDN_URL);

		data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_FEEDS_JSP);
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
		@FormParam(FieldConstants.MEDIA_TYPE) String mediaType,
		@FormParam(FieldConstants.IS_SPONSORED) String isSponSored,
		@FormParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		@FormParam(FieldConstants.FEED_BANNER_HIDDEN_IMAGE) String hiddenFeedBanerImage
		) throws ServletException, SQLException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);
		}
		
		String isSponsoredOptions = DropDownUtils.getYesNoOption(isSponSored);
		data.put(FieldConstants.IS_SPONSORED_OPTIONS, isSponsoredOptions);
		
		String mediaTypeOptions = DropDownUtils.getMediaTypeOptions(mediaType);
		data.put(FieldConstants.MEDIA_TYPE_OPTIONS, mediaTypeOptions);
		
		data.put("cdnUrl", WebappPropertyUtils.CDN_URL);

		data.put(FieldConstants.FEED_NAME, feedName);
		data.put(FieldConstants.FEED_MESSAGE, feedMessage);
		data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE, hiddenFeedBanerImage);
		data.put(FieldConstants.MEDIA_TYPE, mediaType);
		data.put(FieldConstants.IS_SPONSORED_OPTIONS, isSponsoredOptions);
		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);

		if (hasErrors()) {

			List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

			String vendorIdOptions = DropDownUtils.getVendorListOptions(vendorId, UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

			data.put(FieldConstants.FEED_BANNER_HIDDEN_IMAGE_DUMMY, StringUtils.validString(hiddenFeedBanerImage) ? hiddenFeedBanerImage : ProjectConstants.DEFAULT_IMAGE);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_FEEDS_JSP);
		}

		FeedUtils.insertFeed(vendorId, feedName, feedMessage, hiddenFeedBanerImage, loginSessionMap.get(LoginUtils.USER_ID), mediaType, isSponSored, vendorStoreId, null);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_FEEDS_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FEED_NAME, BusinessAction.messageForKeyAdmin("labelFeedName"), new MinMaxLengthValidationRule(1, 30));
		validator.addValidationMapping(FieldConstants.FEED_MESSAGE, BusinessAction.messageForKeyAdmin("labelFeedMessage"), new MinMaxLengthValidationRule(1, 250));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_VENDOR_FEEDS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}