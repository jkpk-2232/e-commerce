package com.webapp.actions.secure.unitOfMeasures;

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

import com.jeeutils.validator.DuplicateUomNameValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.UnitOfMeasureModel;

@Path("add-uom")
public class AddUnitOfMeasureAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddUnitOfMeasure (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_UNIT_OF_MEASURE_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_UNIT_OF_MEASURE_JSP);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addUnitOfMeasure (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.UOM_NAME) String uomName,
		@FormParam(FieldConstants.UOM_SHORT_NAME) String uomShortName,
		@FormParam(FieldConstants.UOM_DESCRIPTION) String uomDescription
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		

		data.put(FieldConstants.UOM_NAME, uomName);
		data.put(FieldConstants.UOM_SHORT_NAME, uomShortName);
		data.put(FieldConstants.UOM_DESCRIPTION, uomDescription);

		if (hasErrors()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_UNIT_OF_MEASURE_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_UNIT_OF_MEASURE_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		UnitOfMeasureModel uomModel = new UnitOfMeasureModel();

		uomModel.setUomName(uomName);
		uomModel.setUomShortName(uomShortName);
		uomModel.setUomDescription(uomDescription);

		uomModel.insertUom(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_UNIT_OF_MEASURE_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.UOM_NAME, messageForKeyAdmin("labelUomName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.UOM_NAME, messageForKeyAdmin("labelUomName"), new DuplicateUomNameValidationRule(0));
		validator.addValidationMapping(FieldConstants.UOM_DESCRIPTION, messageForKeyAdmin("labelUomDescription"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_UNIT_OF_MEASURE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}