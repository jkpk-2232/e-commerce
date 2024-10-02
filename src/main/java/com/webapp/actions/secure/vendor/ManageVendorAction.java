package com.webapp.actions.secure.vendor;

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
import com.utils.myhub.CourierUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorCarTypeModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-vendors")
public class ManageVendorAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendors(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, StringUtils.validString(serviceId) ? serviceId : ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_VENDOR_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_VENDOR_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String serviceId = dtu.getRequestParameter(FieldConstants.SERVICE_ID);
		serviceId = DropDownUtils.parserForAllOptions(serviceId);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String ownUserId = null;

		if (!UserRoleUtils.isSuperAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			ownUserId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		int total = UserModel.getVendorTotalUserCount(UserRoles.VENDOR_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), ownUserId, assignedRegionList, serviceId, null);
		List<UserModel> userModelList = UserModel.getVendorListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.VENDOR_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, ownUserId,
					serviceId, null);

		int count = dtu.getStartInt();

		for (UserModel userProfileModel : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfileModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));
			dtuInnerJsonArray.put(userProfileModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(userProfileModel.getPhoneNoCode(), userProfileModel.getPhoneNo()));
			dtuInnerJsonArray.put(userProfileModel.getDriverCount());
			dtuInnerJsonArray.put(userProfileModel.getCarCount());

			if (CourierUtils.enableAddingCars(userProfileModel.getServiceTypeId())) {

				StringBuilder string = new StringBuilder();
				List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
				for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeList) {
					string.append(vendorCarTypeModel.getCarType()).append(", ");
				}

				dtuInnerJsonArray.put(string.length() > 2 ? string.substring(0, string.length() - 2) : messageForKeyAdmin("labelCarTypeNotAssigned"));

				string = new StringBuilder();
				vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID);
				for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeList) {
					string.append(vendorCarTypeModel.getCarType()).append(", ");
				}

				dtuInnerJsonArray.put(string.length() > 2 ? string.substring(0, string.length() - 2) : messageForKeyAdmin("labelCarTypeNotAssigned"));

				string = new StringBuilder();
				vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
				for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeList) {
					string.append(vendorCarTypeModel.getCarType()).append(", ");
				}

				dtuInnerJsonArray.put(string.length() > 2 ? string.substring(0, string.length() - 2) : messageForKeyAdmin("labelCarTypeNotAssigned"));

			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(userProfileModel.getServiceName());

			if (userProfileModel.isVendorSubscriptionCurrentActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (messageForKeyAdmin("labelVendorMonthlySubscriptionStatus") + " - " + messageForKeyAdmin("labelActive"))));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, (messageForKeyAdmin("labelVendorMonthlySubscriptionStatus") + " - " + messageForKeyAdmin("labelDeactive"))));
			}

			if (userProfileModel.isVendorDriverSubscriptionAppliedInBookingFlow()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (messageForKeyAdmin("labelVendorDriverSubscriptionAppliedInBookingFlow") + " - " + messageForKeyAdmin("labelActive"))));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, (messageForKeyAdmin("labelVendorDriverSubscriptionAppliedInBookingFlow") + " - " + messageForKeyAdmin("labelDeactive"))));
			}

			if (userProfileModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, (messageForKeyAdmin("labelStatus") + " - " + messageForKeyAdmin("labelActive"))));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, (messageForKeyAdmin("labelStatus") + " - " + messageForKeyAdmin("labelDeactive"))));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_VENDOR_URL + "?userId=" + userProfileModel.getUserId())));

			if (!userProfileModel.getUserId().equalsIgnoreCase(WebappPropertyUtils.DEFAULT_VENDOR_ID)) {
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfileModel.getUserId(), "vendor", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL), userProfileModel.isActive() ? "userDeactivate" : "userReactivate"));
			}

			if (CourierUtils.enableAddingCars(userProfileModel.getServiceTypeId())) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewTours"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.VENDOR_TOURS_URL + "?vendorId=" + userProfileModel.getUserId())));

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelAssignCarTypeTransportation"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_DYNAMIC_CARS_URL + "?serviceTypeId=" + ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID + "&vendorId=" + userProfileModel.getUserId()),
							UrlConstants.JSP_URLS.TRANSPORTATION_ICON));

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelAssignCarTypeCourier"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_DYNAMIC_CARS_URL + "?serviceTypeId=" + ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID + "&vendorId=" + userProfileModel.getUserId()),
							UrlConstants.JSP_URLS.COURIER_ICON));

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelAssignCarTypeEcommerce"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_DYNAMIC_CARS_URL + "?serviceTypeId=" + ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID + "&vendorId=" + userProfileModel.getUserId()),
							UrlConstants.JSP_URLS.ECOMMERCE_ICON));

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelSetCarPriority"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL + "?vendorId=" + userProfileModel.getUserId()),
							UrlConstants.JSP_URLS.MANAGE_CAR_ICON));

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelAirportRegions"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_AIRPORT_REGIONS_URL + "?vendorId=" + userProfileModel.getUserId()),
							UrlConstants.JSP_URLS.MANAGE_AIRPORT_ICON));

				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfileModel.getUserId(), "vendor subscrption in booking flow",
							UrlConstants.getUrl(request, (UrlConstants.PAGE_URLS.MANAGE_VENDOR_SUBSCRIPTION_URL + "?vendorId=" + userProfileModel.getUserId())),
							userProfileModel.isVendorDriverSubscriptionAppliedInBookingFlow() ? "subscriptionDeactivate" : "subscriptionReactivate"));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelVendorMonthlySubscriptionHistory"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL + "?vendorId=" + userProfileModel.getUserId()), UrlConstants.JSP_URLS.VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_ICON));

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelVendorMonthlySubscription"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.VENDOR_MONTHLY_SUBSCRIPTION_URL + "?vendorId=" + userProfileModel.getUserId()),
						UrlConstants.JSP_URLS.VENDOR_MONTHLY_SUBSCRIPTION_ICON));

			if (CourierUtils.enableAddingVendorStores(userProfileModel.getServiceTypeId())) {

				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLinkCustom(messageForKeyAdmin("labelStoreLocation"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + userProfileModel.getUserId()),
							UrlConstants.JSP_URLS.VENDOR_STORE_LOCATION_ICON));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/subscription")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response acivateDeactivateVendorDriverSubscriptionAppliedInBookingFlow(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);
		if (userProfileModel.isVendorDriverSubscriptionAppliedInBookingFlow()) {
			userProfileModel.setVendorDriverSubscriptionAppliedInBookingFlow(false);
		} else {
			userProfileModel.setVendorDriverSubscriptionAppliedInBookingFlow(true);
		}

		userProfileModel.updateVendorDriverSubscriptionAppliedInBookingFlowFlag();

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_VENDOR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}