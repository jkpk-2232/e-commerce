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

import com.jeeutils.validator.AlphaNumericValidationRule;
import com.jeeutils.validator.DuplicatePromoCodeValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.VendorServiceCategoryModel;

@Path("/add-promo-code")
public class AddPromoCodeAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddPromoCode(
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

		String modeOptions = DropDownUtils.getModeOption(ProjectConstants.PERCENTAGE_ID);
		data.put(FieldConstants.MODE_OPTIONS, modeOptions);

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(true, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			data.put(FieldConstants.SERVICE_TYPE_ID_OPTIONS, serviceTypeIdOptions);

			String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(true, assignedRegionList, null, ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_PROMO_CODE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.SERVICE_TYPE_ID) String serviceTypeId,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.PROMO_CODE) String promoCode,
		@FormParam(FieldConstants.MODE) String mode,
		@FormParam(FieldConstants.DISCOUNT) String discount,
		@FormParam(FieldConstants.MAX_DISCOUNT) String maxDiscount,
		@FormParam(FieldConstants.START_DATE) String startDate,
		@FormParam(FieldConstants.END_DATE) String endDate
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_MARKETING_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String modeOptions = DropDownUtils.getModeOption(mode);
		data.put(FieldConstants.MODE_OPTIONS, modeOptions);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		if (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(true, serviceTypeId);
			data.put(FieldConstants.SERVICE_TYPE_ID_OPTIONS, serviceTypeIdOptions);

			String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(true, assignedRegionList, Arrays.asList(serviceTypeId), vendorId);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		} else if(UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))){
			
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
			
			serviceTypeId = ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE;
			
		} else {

			vendorId = UserRoleUtils.getParentVendorId(loginSessionMap);

			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);
			serviceTypeId = vscm.getServiceTypeId();
		}

		data.put(FieldConstants.PROMO_CODE, promoCode);
		data.put(FieldConstants.DISCOUNT, discount);
		data.put(FieldConstants.MAX_DISCOUNT, maxDiscount);
		data.put(FieldConstants.START_DATE, startDate);
		data.put(FieldConstants.END_DATE, endDate);

		if (hasErrors(mode)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_PROMO_CODE_JSP);
		}

		PromoCodeModel promoCodeModel = new PromoCodeModel();
		promoCodeModel.setVendorId(vendorId);
		promoCodeModel.setServiceTypeId(serviceTypeId);
		promoCodeModel.setPromoCode(promoCode.toUpperCase());
		promoCodeModel.setStartDate(MyHubUtils.convertSecondsToMillis(startDate));
		promoCodeModel.setEndDate(MyHubUtils.convertSecondsToMillis(endDate));
		promoCodeModel.setDiscount(Double.parseDouble(discount));
		promoCodeModel.setMode(mode);

		promoCodeModel.setUsage(ProjectConstants.UNLIMITED_ID);
		promoCodeModel.setUsageType(ProjectConstants.ALL_ID);

		if (mode.equalsIgnoreCase(ProjectConstants.PERCENTAGE_ID)) {
			promoCodeModel.setMaxDiscount(Double.parseDouble(maxDiscount));
		} else {
			promoCodeModel.setMaxDiscount(Double.parseDouble(discount));
		}

		promoCodeModel.addPromoCode(loginSessionMap.get(LoginUtils.USER_ID));

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_PROMO_CODE_URL);
	}

	public boolean hasErrors(String mode) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PROMO_CODE, messageForKeyAdmin("labelPromoCode"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PROMO_CODE, messageForKeyAdmin("labelPromoCode"), new AlphaNumericValidationRule());
		validator.addValidationMapping(FieldConstants.PROMO_CODE, messageForKeyAdmin("labelPromoCode"), new DuplicatePromoCodeValidationRule());

		validator.addValidationMapping(FieldConstants.DISCOUNT, messageForKeyAdmin("labelDiscount"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DISCOUNT, messageForKeyAdmin("labelDiscount"), new NumericValidationRule());

		if (mode.equalsIgnoreCase(ProjectConstants.PERCENTAGE_ID)) {

			validator.addValidationMapping(FieldConstants.DISCOUNT, messageForKeyAdmin("labelDiscount"), new MinMaxValueValidationRule(0, 100));

			validator.addValidationMapping(FieldConstants.MAX_DISCOUNT, messageForKeyAdmin("labelMaxDiscount"), new NumericValidationRule());
			validator.addValidationMapping(FieldConstants.MAX_DISCOUNT, messageForKeyAdmin("labelMaxDiscount"), new MinMaxValueValidationRule(0, 9999));

		} else {
			validator.addValidationMapping(FieldConstants.DISCOUNT, messageForKeyAdmin("labelDiscount"), new MinMaxValueValidationRule(0, 9999));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_PROMO_CODE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}