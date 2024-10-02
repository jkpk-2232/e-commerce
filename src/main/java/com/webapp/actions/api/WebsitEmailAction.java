package com.webapp.actions.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.WebsiteEmailModel;

@Path("/api/email")
public class WebsitEmailAction extends BusinessApiAction {

	private static final String NAME = "name";
	private static final String NAME_LABEL = "Name";

	private static final String EMAIL_ADDRESS = "emailAddress";
	private static final String EMAIL_ADDRESS_LABEL = "Email Address";

	private static final String SUBJECT = "subject";
	private static final String SUBJECT_LABEL = "Subject";

	private static final String MESSAGE = "message";
	private static final String MESSAGE_LABEL = "Message";

	@Path("/send")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response sendEmail(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			WebsiteEmailModel websiteEmailModel
			) throws IOException {
	//@formatter:on

		errorMessages = websiteEmailModelValidation(websiteEmailModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		return sendSuccessMessage("Email sent successfully.");
	}

	private List<String> websiteEmailModelValidation(WebsiteEmailModel websiteEmailModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(NAME, NAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(EMAIL_ADDRESS, EMAIL_ADDRESS_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(EMAIL_ADDRESS, EMAIL_ADDRESS_LABEL, new EmailValidationRule());
		validator.addValidationMapping(SUBJECT, SUBJECT_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(MESSAGE, MESSAGE_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(websiteEmailModel);

		return errorMessages;
	}

}