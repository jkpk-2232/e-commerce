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

import com.jeeutils.DateUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.PromoCodeModel;

@Path("/edit-promo-code")
public class EditPromoCodeAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editPromoCodeGet(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.PROMO_CODE_ID) String promoCodeId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		PromoCodeModel promoCodeDetails = PromoCodeModel.getActiveDeactivePromoCodeDetailsByPromoCodeId(promoCodeId);

		data.put(FieldConstants.PROMO_CODE_ID, promoCodeId);
		data.put(FieldConstants.PROMO_CODE, promoCodeDetails.getPromoCode());
		data.put(FieldConstants.DISCOUNT, df.format(promoCodeDetails.getDiscount()));
		data.put(FieldConstants.MAX_DISCOUNT, df.format(promoCodeDetails.getMaxDiscount()));

		if (ProjectConstants.PERCENTAGE_ID.equals(promoCodeDetails.getMode())) {
			data.put(FieldConstants.MODE, ProjectConstants.PERCENTAGE_TEXT);
		} else if (ProjectConstants.AMOUNT_ID.equals(promoCodeDetails.getMode())) {
			data.put(FieldConstants.MODE, ProjectConstants.AMOUNT_TEXT);
		} else {
			data.put(FieldConstants.MODE, ProjectConstants.NOT_AVAILABLE);
		}

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			data.put(FieldConstants.SERVICE_TYPE_ID, promoCodeDetails.getServiceTypeId().equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE) ? ProjectConstants.ALL_TEXT : promoCodeDetails.getServiceTypeName());
			data.put(FieldConstants.VENDOR_ID, promoCodeDetails.getVendorId().equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE) ? ProjectConstants.ALL_TEXT : promoCodeDetails.getVendorName());
		}

		String dataTableStartDateStr = DateUtils.dbTimeStampToSesionDate(promoCodeDetails.getStartDate(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put(FieldConstants.PROMO_CODE_START_DATE, dataTableStartDateStr);

		String dataTableEndDateStr = DateUtils.dbTimeStampToSesionDate(promoCodeDetails.getEndDate(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put(FieldConstants.PROMO_CODE_END_DATE, dataTableEndDateStr);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_PROMO_CODE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editPromoCodePost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PROMO_CODE_ID) String promoCodeId,
		@FormParam(FieldConstants.PROMO_CODE_END_DATE) String promoCodeEndDate
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		PromoCodeModel promoCodeDetails = PromoCodeModel.getActiveDeactivePromoCodeDetailsByPromoCodeId(promoCodeId);

		data.put(FieldConstants.PROMO_CODE_ID, promoCodeId);
		data.put(FieldConstants.PROMO_CODE, promoCodeDetails.getPromoCode());
		data.put(FieldConstants.DISCOUNT, df.format(promoCodeDetails.getDiscount()));
		data.put(FieldConstants.MAX_DISCOUNT, df.format(promoCodeDetails.getMaxDiscount()));

		if (ProjectConstants.PERCENTAGE_ID.equals(promoCodeDetails.getMode())) {
			data.put(FieldConstants.MODE, ProjectConstants.PERCENTAGE_TEXT);
		} else if (ProjectConstants.AMOUNT_ID.equals(promoCodeDetails.getMode())) {
			data.put(FieldConstants.MODE, ProjectConstants.AMOUNT_TEXT);
		} else {
			data.put(FieldConstants.MODE, ProjectConstants.NOT_AVAILABLE);
		}

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			data.put(FieldConstants.SERVICE_TYPE_ID, promoCodeDetails.getServiceTypeId().equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE) ? ProjectConstants.ALL_TEXT : promoCodeDetails.getServiceTypeName());
			data.put(FieldConstants.VENDOR_ID, promoCodeDetails.getVendorId().equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE) ? ProjectConstants.ALL_TEXT : promoCodeDetails.getVendorName());
		}

		String dataTableStartDateStr = DateUtils.dbTimeStampToSesionDate(promoCodeDetails.getStartDate(), TimeZoneUtils.getTimeZone(), DateUtils.DATE_FORMAT_FOR_VIEW);
		data.put(FieldConstants.PROMO_CODE_START_DATE, dataTableStartDateStr);

		data.put(FieldConstants.PROMO_CODE_END_DATE, promoCodeEndDate);

		boolean hasErrors = hasErrors();

		if (hasErrors) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_PROMO_CODE_JSP);
		}

		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(promoCodeEndDate, DateUtils.DATE_FORMAT_FOR_VIEW, TimeZoneUtils.getTimeZone());

		if (promoCodeDetails.getStartDate() > endDatelong) {

			data.put(FieldConstants.PROMO_CODE_END_DATE_CUSTOM_ERROR, messageForKeyAdmin("labelPromoCodeEndDateError"));

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
			return loadView(UrlConstants.JSP_URLS.EDIT_PROMO_CODE_JSP);
		}

		PromoCodeModel promoCodeModel = new PromoCodeModel();

		promoCodeModel.setPromoCodeId(promoCodeId);
		promoCodeModel.setEndDate(DateUtils.getEndOfDayDatatableUpdated(promoCodeEndDate, DateUtils.DATE_FORMAT_FOR_VIEW, TimeZoneUtils.getTimeZone()));
		promoCodeModel.updatePromoCodeDetails(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PROMO_CODE_END_DATE, messageForKeyAdmin("labelEndDate"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_PROMO_CODE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}