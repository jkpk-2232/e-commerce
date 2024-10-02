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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.OrderSettingModel;

@Path("/edit-order-settings")
public class EditOrderSettingsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditOrderSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SERVICE_ID) String serviceId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		OrderSettingModel osm = OrderSettingModel.getOrderSettingDetailsByServiceId(serviceId);

		data.put(FieldConstants.SERVICE_NAME, osm.getServiceName());
		data.put(FieldConstants.SERVICE_ID, osm.getServiceId());
		data.put(FieldConstants.MAX_NUMBER_OF_ITEMS, StringUtils.valueOf(osm.getMaxNumberOfItems()));
		data.put(FieldConstants.MAX_WEIGHT_ALLOWED, StringUtils.valueOf(osm.getMaxWeightAllowed()));
		data.put(FieldConstants.FREE_CANCELLATION_TIME_MINS, StringUtils.valueOf(osm.getFreeCancellationTimeMins()));
		data.put(FieldConstants.ORDER_JOB_CANCELLATION_TIME_HOURS, StringUtils.valueOf(osm.getOrderJobCancellationTimeHours()));
		data.put(FieldConstants.ORDER_NEW_CANCELLATION_TIME_HOURS, StringUtils.valueOf(osm.getOrderNewCancellationTimeHours()));
		data.put(FieldConstants.DELIVERY_BASE_FEE, StringUtils.valueOf(osm.getDeliveryBaseFee()));
		data.put(FieldConstants.DELIVERY_BASE_KM, StringUtils.valueOf(osm.getDeliveryBaseKm()));
		data.put(FieldConstants.DELIVERY_FEE_PER_KM, StringUtils.valueOf(osm.getDeliveryFeePerKm()));

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ORDER_SETTINGS_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_ORDER_SETTINGS_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postEditOrderSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.SERVICE_ID) String serviceId,
		@FormParam(FieldConstants.MAX_NUMBER_OF_ITEMS) String maxNumberOfItems,
		@FormParam(FieldConstants.MAX_WEIGHT_ALLOWED) String maxWeightAllowed,
		@FormParam(FieldConstants.FREE_CANCELLATION_TIME_MINS) String freeCancellationTimeMins,
		@FormParam(FieldConstants.ORDER_JOB_CANCELLATION_TIME_HOURS) String orderJobCancellationTimeHours,
		@FormParam(FieldConstants.ORDER_NEW_CANCELLATION_TIME_HOURS) String orderNewCancellationTimeHours,
		@FormParam(FieldConstants.DELIVERY_BASE_FEE) String deliveryBaseFee,
		@FormParam(FieldConstants.DELIVERY_BASE_KM) String deliveryBaseKm,
		@FormParam(FieldConstants.DELIVERY_FEE_PER_KM) String deliveryFeePerKm
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		OrderSettingModel osm = OrderSettingModel.getOrderSettingDetailsByServiceId(serviceId);

		data.put(FieldConstants.SERVICE_NAME, osm.getServiceName());
		data.put(FieldConstants.SERVICE_ID, serviceId);
		data.put(FieldConstants.MAX_NUMBER_OF_ITEMS, maxNumberOfItems);
		data.put(FieldConstants.MAX_WEIGHT_ALLOWED, maxWeightAllowed);
		data.put(FieldConstants.FREE_CANCELLATION_TIME_MINS, freeCancellationTimeMins);
		data.put(FieldConstants.ORDER_JOB_CANCELLATION_TIME_HOURS, orderJobCancellationTimeHours);
		data.put(FieldConstants.ORDER_NEW_CANCELLATION_TIME_HOURS, orderNewCancellationTimeHours);
		data.put(FieldConstants.DELIVERY_BASE_FEE, deliveryBaseFee);
		data.put(FieldConstants.DELIVERY_BASE_KM, deliveryBaseKm);
		data.put(FieldConstants.DELIVERY_FEE_PER_KM, deliveryFeePerKm);

		if (hasErrors()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ORDER_SETTINGS_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_ORDER_SETTINGS_JSP);
		}

		osm.setServiceId(serviceId);
		osm.setMaxNumberOfItems(StringUtils.doubleValueOf(maxNumberOfItems));
		osm.setMaxWeightAllowed(StringUtils.doubleValueOf(maxWeightAllowed));
		osm.setFreeCancellationTimeMins(StringUtils.intValueOf(freeCancellationTimeMins));
		osm.setOrderJobCancellationTimeHours(StringUtils.intValueOf(orderJobCancellationTimeHours));
		osm.setOrderNewCancellationTimeHours(StringUtils.intValueOf(orderNewCancellationTimeHours));
		osm.setDeliveryBaseFee(StringUtils.doubleValueOf(deliveryBaseFee));
		osm.setDeliveryBaseKm(StringUtils.doubleValueOf(deliveryBaseKm));
		osm.setDeliveryFeePerKm(StringUtils.doubleValueOf(deliveryFeePerKm));

		osm.updateOrderSettings(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ORDER_SETTINGS_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.MAX_NUMBER_OF_ITEMS, messageForKeyAdmin("labelMaxNumberOfItems"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.MAX_NUMBER_OF_ITEMS, messageForKeyAdmin("labelMaxNumberOfItems"), new MinMaxValueValidationRule(0, 100));

		validator.addValidationMapping(FieldConstants.MAX_WEIGHT_ALLOWED, messageForKeyAdmin("labelMaxWeightAllowed"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.MAX_WEIGHT_ALLOWED, messageForKeyAdmin("labelMaxWeightAllowed"), new MinMaxValueValidationRule(0, 50));

		validator.addValidationMapping(FieldConstants.FREE_CANCELLATION_TIME_MINS, messageForKeyAdmin("labelFreeCancellationTimeMins"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.FREE_CANCELLATION_TIME_MINS, messageForKeyAdmin("labelFreeCancellationTimeMins"), new MinMaxValueValidationRule(10, 1000));

		validator.addValidationMapping(FieldConstants.ORDER_JOB_CANCELLATION_TIME_HOURS, messageForKeyAdmin("labelOrderJobExpirationTimeHours"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.ORDER_JOB_CANCELLATION_TIME_HOURS, messageForKeyAdmin("labelOrderJobExpirationTimeHours"), new MinMaxValueValidationRule(1, 1000));

		validator.addValidationMapping(FieldConstants.ORDER_NEW_CANCELLATION_TIME_HOURS, messageForKeyAdmin("labelNewOrderExpirationTimeHours"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.ORDER_NEW_CANCELLATION_TIME_HOURS, messageForKeyAdmin("labelNewOrderExpirationTimeHours"), new MinMaxValueValidationRule(1, 1000));

		validator.addValidationMapping(FieldConstants.DELIVERY_BASE_FEE, messageForKeyAdmin("labelDeliveryBaseFee"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DELIVERY_BASE_FEE, messageForKeyAdmin("labelDeliveryBaseFee"), new MinMaxValueValidationRule(0, 1000));

		validator.addValidationMapping(FieldConstants.DELIVERY_BASE_KM, messageForKeyAdmin("labelDeliveryBaseKm"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DELIVERY_BASE_KM, messageForKeyAdmin("labelDeliveryBaseKm"), new MinMaxValueValidationRule(0, 1000));

		validator.addValidationMapping(FieldConstants.DELIVERY_FEE_PER_KM, messageForKeyAdmin("labelDeliveryFeePerKm"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.DELIVERY_FEE_PER_KM, messageForKeyAdmin("labelDeliveryFeePerKm"), new MinMaxValueValidationRule(0, 1000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_ORDER_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}