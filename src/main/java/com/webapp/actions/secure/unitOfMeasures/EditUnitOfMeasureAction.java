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
import javax.ws.rs.QueryParam;
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

@Path("edit-uom")
public class EditUnitOfMeasureAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditUnitOfMeasure (
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.UOM_ID) String uomId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_PRODUCT_CATEGORY_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		UnitOfMeasureModel uomModel = UnitOfMeasureModel.getUomDetailsByUomId(Integer.valueOf(uomId));

		data.put(FieldConstants.UOM_NAME, uomModel.getUomName());
		data.put(FieldConstants.UOM_SHORT_NAME, uomModel.getUomShortName());
		data.put(FieldConstants.UOM_DESCRIPTION, uomModel.getUomDescription());
		data.put(FieldConstants.UOM_ID, uomId);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_UNIT_OF_MEASURE_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_UNIT_OF_MEASURE_JSP);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response editUom(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.UOM_ID) String uomId,
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

		if (hasErrors(uomId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_UNIT_OF_MEASURE_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_UNIT_OF_MEASURE_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		UnitOfMeasureModel uomModel = UnitOfMeasureModel.getUomDetailsByUomId(Integer.valueOf(uomId));
		uomModel.setUomName(uomName);
		uomModel.setUomShortName(uomShortName);
		uomModel.setUomDescription(uomDescription);
		uomModel.updateUom(loggedInUserId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_UNIT_OF_MEASURE_URL);
	}
	
	public boolean hasErrors(String uomId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.UOM_NAME, messageForKeyAdmin("labelUomName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.UOM_NAME, messageForKeyAdmin("labelUomName"), new DuplicateUomNameValidationRule(Integer.valueOf(uomId)));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_UNIT_OF_MEASURE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
