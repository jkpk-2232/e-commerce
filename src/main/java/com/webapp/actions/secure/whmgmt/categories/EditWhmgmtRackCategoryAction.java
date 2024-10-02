package com.webapp.actions.secure.whmgmt.categories;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.DuplicateRackCategoryNameValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.WhmgmtRackCategoryModel;

@Path("/edit-rack-category")
public class EditWhmgmtRackCategoryAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditRackCategory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.CATEGORY_ID) String categoryId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		WhmgmtRackCategoryModel rackCategoryModel = WhmgmtRackCategoryModel.getRackCategoryDetailsByCategoryId(categoryId);

		data.put(FieldConstants.CATEGORY_ID, rackCategoryModel.getCategoryId());
		data.put(FieldConstants.CATEGORY_NAME, rackCategoryModel.getCategoryName());
		data.put(FieldConstants.SLOT_HEIGHT, String.valueOf(rackCategoryModel.getSlotHeight()));
		data.put(FieldConstants.SLOT_WIDTH, String.valueOf(rackCategoryModel.getSlotWidth()));
		data.put(FieldConstants.MAX_WEIGHT, String.valueOf(rackCategoryModel.getMaxWeight()));
		data.put(FieldConstants.CHARGE_PER_SLOT, String.valueOf(rackCategoryModel.getChargePerSlot()));
		data.put(FieldConstants.NO_OF_SLOTS, String.valueOf(rackCategoryModel.getNoOfSlots()));
		data.put(FieldConstants.NUMBER_OF_DAYS, String.valueOf(rackCategoryModel.getNumberOfDays()));

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_RACK_CATEGORY_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editRackCategory (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.CATEGORY_ID) String categoryId,
		@FormParam(FieldConstants.CATEGORY_NAME) String categoryName,
		@FormParam(FieldConstants.SLOT_HEIGHT) String slotHeight,
		@FormParam(FieldConstants.SLOT_WIDTH) String slotWidth,
		@FormParam(FieldConstants.MAX_WEIGHT) String maxWeight,
		@FormParam(FieldConstants.CHARGE_PER_SLOT) String chargePerSlot,
		@FormParam(FieldConstants.NO_OF_SLOTS) String noOfSlots,
		@FormParam(FieldConstants.NUMBER_OF_DAYS) String numberOfDays
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.CATEGORY_ID, categoryId);
		data.put(FieldConstants.CATEGORY_NAME, categoryName);
		data.put(FieldConstants.SLOT_HEIGHT, slotHeight);
		data.put(FieldConstants.SLOT_WIDTH, slotWidth);
		data.put(FieldConstants.MAX_WEIGHT, maxWeight);
		data.put(FieldConstants.CHARGE_PER_SLOT, chargePerSlot);
		data.put(FieldConstants.NO_OF_SLOTS, noOfSlots);
		data.put(FieldConstants.NUMBER_OF_DAYS, numberOfDays);

		if (hasErrors(categoryId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_RACK_CATEGORY_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		WhmgmtRackCategoryModel rackCategoryModel = WhmgmtRackCategoryModel.getRackCategoryDetailsByCategoryId(categoryId);
		
		rackCategoryModel.setCategoryName(categoryName);
		if (slotHeight != null && !slotHeight.isEmpty()) {
			rackCategoryModel.setSlotHeight(Double.parseDouble(slotHeight));
		}
		if (slotWidth != null && !slotWidth.isEmpty()) {
			rackCategoryModel.setSlotWidth(Double.parseDouble(slotWidth));
		}
		if (maxWeight != null && !maxWeight.isEmpty()) {
			rackCategoryModel.setMaxWeight(Double.parseDouble(maxWeight));
		}
		rackCategoryModel.setChargePerSlot(Integer.parseInt(chargePerSlot));
		rackCategoryModel.setNoOfSlots(Integer.parseInt(noOfSlots));
		rackCategoryModel.setNumberOfDays(Integer.parseInt(numberOfDays));
		
		rackCategoryModel.updateRackCategory(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_RACK_CATEGORY_URL);
	}
	
	public boolean hasErrors(String categoryId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.CATEGORY_NAME, messageForKeyAdmin("labelCategoryName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CATEGORY_NAME, messageForKeyAdmin("labelCategoryName"), new DuplicateRackCategoryNameValidationRule(categoryId));
		validator.addValidationMapping(FieldConstants.CHARGE_PER_SLOT, messageForKeyAdmin("labelChargePerSlot"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.NO_OF_SLOTS, messageForKeyAdmin("labelNoOfSlots"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.NUMBER_OF_DAYS, messageForKeyAdmin("labelNumberOfDays"), new NumericValidationRule());
		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_RACK_CATEGORY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}