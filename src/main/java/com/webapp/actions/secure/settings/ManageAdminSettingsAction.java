package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.StringUtils;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CurrencyModel;
import com.webapp.models.FreeWaitingTimeModel;

@Path("/manage-admin-settings")
public class ManageAdminSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();

		String radiusOptions = DropDownUtils.getRadiusString(String.valueOf(adminSettingsModel.getRadius()));
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		String radiusDeliveryDriverOptions = DropDownUtils.getRadiusString(String.valueOf(adminSettingsModel.getRadiusDeliveryDriver()));
		data.put(FieldConstants.RADIUS_DELIVERY_DRIVER_OPTIONS, radiusDeliveryDriverOptions);

		String radiusSelfDeliveryOptions = DropDownUtils.getRadiusString(String.valueOf(adminSettingsModel.getRadiusSelfDelivery()));
		data.put(FieldConstants.RADIUS_SELF_DELIVERY_OPTIONS, radiusSelfDeliveryOptions);

		String radiusStoreVisibilityOptions = DropDownUtils.getRadiusString(String.valueOf(adminSettingsModel.getRadiusStoreVisibility()));
		data.put(FieldConstants.RADIUS_STORE_VISIBILITY_OPTIONS, radiusStoreVisibilityOptions);

		String distanceTypeOptions = DropDownUtils.getDistanceTypeOption(String.valueOf(adminSettingsModel.getDistanceType()));
		data.put(FieldConstants.DISTANCE_TYPE_OPTIONS, distanceTypeOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(String.valueOf(adminSettingsModel.getCountryCode()));
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String noOfCarsOptions = DropDownUtils.getNumberOfCarsOption(String.valueOf(adminSettingsModel.getNoOfCars()));
		data.put(FieldConstants.NO_OF_CAR_OPTIONS, noOfCarsOptions);

		String currencySymbolOptions = DropDownUtils.getCurrencyOption(String.valueOf(adminSettingsModel.getCurrencyId()));
		data.put(FieldConstants.CURRENCY_SYMBOL_OPTIONS, currencySymbolOptions);

		data.put(FieldConstants.CHARGE, cancellationChargeModel.getCharge() + "");
		data.put(FieldConstants.SENDER_BENEFIT, (int) adminSettingsModel.getSenderBenefit() + "");
		data.put(FieldConstants.RECEIVER_BENEFIT, (int) adminSettingsModel.getReceiverBenefit() + "");
		data.put(FieldConstants.DRIVER_REFERRAL_BENEFIT, (int) adminSettingsModel.getDriverReferralBenefit() + "");
		data.put(FieldConstants.WAITING_TIME, (int) freeWaitingTimeModel.getWaitingTime() + "");
		data.put(FieldConstants.CANCEL_TIME, (int) freeWaitingTimeModel.getCancelTime() + "");
		data.put(FieldConstants.DEMAND_VENDOR_PERCENTAGE, (int) adminSettingsModel.getDemandVendorPercentage() + "");
		data.put(FieldConstants.SUPPLIER_VENDOR_PERCENTAGE, (int) adminSettingsModel.getSupplierVendorPercentage() + "");

		String selectedDriverIdealTime = "";

		if (adminSettingsModel.getDriverIdealTime() >= ProjectConstants.ONE_HOUR_MILLISECONDS_LONG) {
			selectedDriverIdealTime = (((adminSettingsModel.getDriverIdealTime() / 1000) / 60) / 60) + "_hr";
		} else {
			selectedDriverIdealTime = ((adminSettingsModel.getDriverIdealTime() / 1000) / 60) + "_min";
		}

		String driverIdealTimeOptions = DropDownUtils.getDriverIdealTimeOptions(selectedDriverIdealTime);
		data.put(FieldConstants.DRIVER_IDEAL_TIME_OPTIONS, driverIdealTimeOptions);

		String isAutoAssignOptions = DropDownUtils.getYesNoOption(adminSettingsModel.isAutoAssign() + "");
		data.put(FieldConstants.IS_AUTO_ASSIGN_OPTIONS, isAutoAssignOptions);

		String isCarServiceAutoAssignOptions = DropDownUtils.getYesNoOption(adminSettingsModel.isCarServiceAutoAssign() + "");
		data.put(FieldConstants.IS_CAR_SERVICE_AUTO_ASSIGN_OPTIONS, isCarServiceAutoAssignOptions);

		String isRestrictDriverVendorSubscriptionExpiryOptions = DropDownUtils.getYesNoOption(adminSettingsModel.isRestrictDriverVendorSubscriptionExpiry() + "");
		data.put(FieldConstants.IS_RESTRICTED_DRIVER_VENDOR_SUBSCRIPTION_EXPIRY_OPTIONS, isRestrictDriverVendorSubscriptionExpiryOptions);

		String cronJobTripExpiryAfterXMinsOptions = DropDownUtils.getNumberOptions(StringUtils.valueOf(adminSettingsModel.getCronJobTripExpiryAfterXMins()), ProjectConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_MIN_VALUE,
					ProjectConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_MAX_VALUE);
		data.put(FieldConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_OPTIONS, cronJobTripExpiryAfterXMinsOptions);

		String driverProcessingViaOptions = DropDownUtils.getDriverProcessingViaOptions(StringUtils.valueOf(adminSettingsModel.getDriverProcessingVia()));
		data.put(FieldConstants.DRIVER_PROCESSING_VIA_OPTIONS, driverProcessingViaOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL);
		return loadView(UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.RADIUS) String radius,
		@FormParam(FieldConstants.RADIUS_DELIVERY_DRIVER) String radiusDeliveryDriver,
		@FormParam(FieldConstants.RADIUS_SELF_DELIVERY) String radiusSelfDelivery,
		@FormParam(FieldConstants.RADIUS_STORE_VISIBILITY) String radiusStoreVisibility,
		@FormParam(FieldConstants.CHARGE) String charge,
		@FormParam(FieldConstants.SENDER_BENEFIT) String senderBenefit,
		@FormParam(FieldConstants.RECEIVER_BENEFIT) String receiverBenefit,
		@FormParam(FieldConstants.DRIVER_REFERRAL_BENEFIT) String driverReferralBenefit,
		@FormParam(FieldConstants.WAITING_TIME) String waitingTime,
		@FormParam(FieldConstants.CANCEL_TIME) String cancelTime,
		@FormParam(FieldConstants.DISTANCE_TYPE) String distanceType,
		@FormParam(FieldConstants.NO_OF_CARS) String noOfCars,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.CURRENCY_SYMBOL) String currencySymbol,
		@FormParam(FieldConstants.DRIVER_IDEAL_TIME) String driverIdealTime,
		@FormParam(FieldConstants.DEMAND_VENDOR_PERCENTAGE) String demandVendorPercentage,
		@FormParam(FieldConstants.SUPPLIER_VENDOR_PERCENTAGE) String supplierVendorPercentage,
		@FormParam(FieldConstants.IS_RESTRICTED_DRIVER_VENDOR_SUBSCRIPTION_EXPIRY) String isRestrictDriverVendorSubscriptionExpiry,
		@FormParam(FieldConstants.IS_AUTO_ASSIGN) String isAutoAssign,
		@FormParam(FieldConstants.IS_CAR_SERVICE_AUTO_ASSIGN) String isCarServiceAutoAssign,
		@FormParam(FieldConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS) String cronJobTripExpiryAfterXMins,
		@FormParam(FieldConstants.DRIVER_PROCESSING_VIA) String driverProcessingVia
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		String radiusOptions = DropDownUtils.getRadiusString(String.valueOf(radius));
		data.put(FieldConstants.RADIUS_OPTIONS, radiusOptions);

		String radiusDeliveryDriverOptions = DropDownUtils.getRadiusString(String.valueOf(radiusDeliveryDriver));
		data.put(FieldConstants.RADIUS_DELIVERY_DRIVER_OPTIONS, radiusDeliveryDriverOptions);

		String radiusSelfDeliveryOptions = DropDownUtils.getRadiusString(String.valueOf(radiusSelfDelivery));
		data.put(FieldConstants.RADIUS_SELF_DELIVERY_OPTIONS, radiusSelfDeliveryOptions);

		String radiusStoreVisibilityOptions = DropDownUtils.getRadiusString(String.valueOf(radiusStoreVisibility));
		data.put(FieldConstants.RADIUS_STORE_VISIBILITY_OPTIONS, radiusStoreVisibilityOptions);

		String distanceTypeOptions = DropDownUtils.getDistanceTypeOption(String.valueOf(distanceType));
		data.put(FieldConstants.DISTANCE_TYPE_OPTIONS, distanceTypeOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(String.valueOf(countryCode));
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String noOfCarsOptions = DropDownUtils.getNumberOfCarsOption(String.valueOf(noOfCars));
		data.put(FieldConstants.NO_OF_CAR_OPTIONS, noOfCarsOptions);

		String currencySymbolOptions = DropDownUtils.getCurrencyOption(String.valueOf(currencySymbol));
		data.put(FieldConstants.CURRENCY_SYMBOL_OPTIONS, currencySymbolOptions);

		String driverIdealTimeOptions = DropDownUtils.getDriverIdealTimeOptions(driverIdealTime);
		data.put(FieldConstants.DRIVER_IDEAL_TIME_OPTIONS, driverIdealTimeOptions);

		String isRestrictDriverVendorSubscriptionExpiryOptions = DropDownUtils.getYesNoOption(isRestrictDriverVendorSubscriptionExpiry);
		data.put(FieldConstants.IS_RESTRICTED_DRIVER_VENDOR_SUBSCRIPTION_EXPIRY_OPTIONS, isRestrictDriverVendorSubscriptionExpiryOptions);

		String isAutoAssignOptions = DropDownUtils.getYesNoOption(isAutoAssign);
		data.put(FieldConstants.IS_AUTO_ASSIGN_OPTIONS, isAutoAssignOptions);

		String isCarServiceAutoAssignOptions = DropDownUtils.getYesNoOption(isCarServiceAutoAssign);
		data.put(FieldConstants.IS_CAR_SERVICE_AUTO_ASSIGN_OPTIONS, isCarServiceAutoAssignOptions);

		String cronJobTripExpiryAfterXMinsOptions = DropDownUtils.getNumberOptions(cronJobTripExpiryAfterXMins, ProjectConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_MIN_VALUE, ProjectConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_MAX_VALUE);
		data.put(FieldConstants.CRON_JOB_TRIP_EXPIRY_AFTER_X_MINS_OPTIONS, cronJobTripExpiryAfterXMinsOptions);

		String driverProcessingViaOptions = DropDownUtils.getDriverProcessingViaOptions(driverProcessingVia);
		data.put(FieldConstants.DRIVER_PROCESSING_VIA_OPTIONS, driverProcessingViaOptions);

		data.put(FieldConstants.CHARGE, charge + "");
		data.put(FieldConstants.SENDER_BENEFIT, senderBenefit + "");
		data.put(FieldConstants.RECEIVER_BENEFIT, receiverBenefit + "");
		data.put(FieldConstants.DRIVER_REFERRAL_BENEFIT, driverReferralBenefit + "");
		data.put(FieldConstants.WAITING_TIME, waitingTime + "");
		data.put(FieldConstants.CANCEL_TIME, cancelTime + "");
		data.put(FieldConstants.DEMAND_VENDOR_PERCENTAGE, demandVendorPercentage + "");
		data.put(FieldConstants.SUPPLIER_VENDOR_PERCENTAGE, supplierVendorPercentage + "");

		boolean hasErrors = hasErrors();

		if (hasErrors) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL);
			return loadView(UrlConstants.JSP_URLS.MANAGE_ADMIN_SETTINGS_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		adminSettingsModel.setRadius(StringUtils.longValueOf(radius.trim()));
		adminSettingsModel.setRadiusDeliveryDriver(StringUtils.intValueOf(radiusDeliveryDriver.trim()));
		adminSettingsModel.setRadiusSelfDelivery(StringUtils.intValueOf(radiusSelfDelivery.trim()));
		adminSettingsModel.setRadiusStoreVisibility(StringUtils.intValueOf(radiusStoreVisibility.trim()));

		adminSettingsModel.setCronJobTripExpiryAfterXMins(StringUtils.intValueOf(cronJobTripExpiryAfterXMins));
		adminSettingsModel.setDriverProcessingVia(StringUtils.intValueOf(driverProcessingVia));

		adminSettingsModel.setSenderBenefit(Double.parseDouble(senderBenefit));
		adminSettingsModel.setReceiverBenefit(Double.parseDouble(receiverBenefit));
		adminSettingsModel.setDriverReferralBenefit(Double.parseDouble(driverReferralBenefit));

		adminSettingsModel.updateBenefits();

		adminSettingsModel.setNoOfCars(Long.parseLong(noOfCars));
		adminSettingsModel.setCountryCode(countryCode);
		adminSettingsModel.setDistanceType(distanceType);

		if (distanceType.equalsIgnoreCase(ProjectConstants.KM)) {
			adminSettingsModel.setDistanceUnits(ProjectConstants.KM_UNITS);
		} else {
			adminSettingsModel.setDistanceUnits(ProjectConstants.MILES_UNITS);
		}

		CurrencyModel currencyModel = CurrencyModel.getCurrencyDetailsByCurrencyId(Long.parseLong(currencySymbol));

		if (ProjectConstants.DEFAULT_COUNTRY.equalsIgnoreCase(currencyModel.getCountry())) {
			adminSettingsModel.setCurrencySymbol(ProjectConstants.DEFAULT_CURRENCY);
		} else {
			adminSettingsModel.setCurrencySymbol(currencyModel.getSymbol());
		}

		adminSettingsModel.setCurrencySymbolHtml(currencyModel.getSymbol());
		adminSettingsModel.setCurrencyId(currencySymbol);

		long driverIdeal = Long.parseLong(driverIdealTime.split("_")[0]);

		if ("min".equalsIgnoreCase(driverIdealTime.split("_")[1])) {
			adminSettingsModel.setDriverIdealTime((driverIdeal * 60) * 1000);
		} else {
			adminSettingsModel.setDriverIdealTime(((driverIdeal * 60) * 60) * 1000);
		}

		adminSettingsModel.setAutoAssign(Boolean.parseBoolean(isAutoAssign));
		adminSettingsModel.setCarServiceAutoAssign(Boolean.parseBoolean(isCarServiceAutoAssign));
		adminSettingsModel.setDemandVendorPercentage(Double.parseDouble(demandVendorPercentage));
		adminSettingsModel.setSupplierVendorPercentage(Double.parseDouble(supplierVendorPercentage));
		adminSettingsModel.setRestrictDriverVendorSubscriptionExpiry(Boolean.parseBoolean(isRestrictDriverVendorSubscriptionExpiry));

		adminSettingsModel.updateAdminSettings();

		CancellationChargeModel cancellationChargeModel = new CancellationChargeModel();
		cancellationChargeModel.setAdminId(loggedInUserId);
		cancellationChargeModel.setCharge(Double.parseDouble(charge));
		cancellationChargeModel.updateAdminCancellationCharges();

		FreeWaitingTimeModel freeWaitingTimeModel = new FreeWaitingTimeModel();

		freeWaitingTimeModel.setUserId(loggedInUserId);
		freeWaitingTimeModel.setWaitingTime(Double.parseDouble(waitingTime));
		freeWaitingTimeModel.updateWaitingTime();

		freeWaitingTimeModel.setCancelTime(Double.parseDouble(cancelTime));
		freeWaitingTimeModel.updateCancelTime();

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.CHARGE, messageForKey("labelCancellationCharges"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.CHARGE, messageForKey("labelCancellationCharges"), new MinMaxValueValidationRule(0, 100000));

		validator.addValidationMapping(FieldConstants.SENDER_BENEFIT, messageForKey("labelSenderBenefitPassenger"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.SENDER_BENEFIT, messageForKey("labelSenderBenefitPassenger"), new MinMaxValueValidationRule(0, 9999));

		validator.addValidationMapping(FieldConstants.RECEIVER_BENEFIT, messageForKey("labelReceiverBenefitPassenger"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.RECEIVER_BENEFIT, messageForKey("labelReceiverBenefitPassenger"), new MinMaxValueValidationRule(0, 9999));

		validator.addValidationMapping(FieldConstants.DRIVER_REFERRAL_BENEFIT, messageForKey("labelSenderBenefitDriver"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_REFERRAL_BENEFIT, messageForKey("labelSenderBenefitDriver"), new MinMaxValueValidationRule(0, 9999));

		validator.addValidationMapping(FieldConstants.WAITING_TIME, messageForKey("labelWaitingTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.WAITING_TIME, messageForKey("labelWaitingTime"), new MinMaxValueValidationRule(0, 60));

		validator.addValidationMapping(FieldConstants.CANCEL_TIME, messageForKey("labelCancelTime"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.CANCEL_TIME, messageForKey("labelCancelTime"), new MinMaxValueValidationRule(0, 60));

		validator.addValidationMapping(FieldConstants.DEMAND_VENDOR_PERCENTAGE, messageForKey("labelDemandVendorPercentage"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DEMAND_VENDOR_PERCENTAGE, messageForKey("labelDemandVendorPercentage"), new MinMaxValueValidationRule(0, 100));

		validator.addValidationMapping(FieldConstants.SUPPLIER_VENDOR_PERCENTAGE, messageForKey("labelSupplierVendorPercentage"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.SUPPLIER_VENDOR_PERCENTAGE, messageForKey("labelSupplierVendorPercentage"), new MinMaxValueValidationRule(0, 100));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ADMIN_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}