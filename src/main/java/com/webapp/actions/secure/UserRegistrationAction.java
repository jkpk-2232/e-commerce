package com.webapp.actions.secure;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.validator.DuplicateEmailValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.UUIDGenerator;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ReferralCodeLogsModel;
import com.webapp.models.UserProfileModel;

@Path("/signup")
public class UserRegistrationAction extends BusinessAction {

	private final static String FIRST_NAME = "firstName";

	private final static String LAST_NAME = "lastName";

	private final static String EMAIL_ADDRESS = "emailAddress";

	private final static String PHONE = "phone";

	private final static String COUNTRY_CODE = "countryCode";

	private final static String PASSWORD = "password";

	private final static String CONFIRM_PASSWORD = "confirmPassword";

	private final static String REFERRAL_CODE = "referralCode";

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("referralCode") String referralCode
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		data.put("referralCode", referralCode);

		UserProfileModel senderUserModel = null;

		if (referralCode != null && !"".equalsIgnoreCase(referralCode)) {
			senderUserModel = UserProfileModel.getUserDetailsByReferralCode(referralCode.trim());
		}

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		if (senderUserModel == null) {
			data.put("referralCodeFlag", "0");
		} else {
			data.put("referralCodeFlag", "1");
		}

		String language = ProjectConstants.ENGLISH_ID;
		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());

		data.put("countryCodeOptions", countryCodeOptions);

		data.put("labelSignUp", messageForKeyAdmin("labelSignUp", language));
		data.put("labelCallMasa", messageForKeyAdmin("labelCallMasa", language));
		data.put("labelDownload", messageForKeyAdmin("labelDownload", language));
		data.put("labelFirstName", messageForKeyAdmin("labelFirstName", language));
		data.put("labelLastName", messageForKeyAdmin("labelLastName", language));
		data.put("labelEmail", messageForKeyAdmin("labelEmail", language));
		data.put("labelPassword", messageForKeyAdmin("labelPassword", language));
		data.put("labelConfirmPassword", messageForKeyAdmin("labelConfirmPassword", language));
		data.put("labelMobileNo", messageForKeyAdmin("labelMobileNo", language));
		data.put("labelReferralCode", messageForKeyAdmin("labelReferralCode", language));
		data.put("labelAgree", messageForKeyAdmin("labelAgree", language));
		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy", language));
		data.put("labelTermsofServices", messageForKeyAdmin("labelTermsofServices", language));
		data.put("labelAnd", messageForKeyAdmin("labelAnd", language));

		data.put("labelGO", messageForKeyAdmin("labelGO", language));
		data.put("labelEconomy", messageForKeyAdmin("labelEconomy", language));
		data.put("labelBusiness", messageForKeyAdmin("labelBusiness", language));
		data.put("labelExtra", messageForKeyAdmin("labelExtra", language));

		data.put("labelDOWNLOADTHEAPPHERE", messageForKeyAdmin("labelDOWNLOADTHEAPPHERE", language));
		data.put("labelCopyright", messageForKeyAdmin("labelCopyright", language));

		data.put("language", messageForKeyAdmin("language", language));

		String languageOptions = DropDownUtils.getLanguageStringLanguage(language, language);
		data.put("languageOptions", languageOptions);

		data.put("labelPasswordandConfirmpasswordshouldbesame", messageForKeyAdmin("labelPasswordandConfirmpasswordshouldbesame", language));
		data.put("errorReferralCodeIsRquired", messageForKeyAdmin("errorReferralCodeIsRquired", language));

		data.put("REFERRAL_CODE_PHONE", BusinessApiAction.convertVerificationCode(ProjectConstants.REFERRAL_CODE_PHONE));

		return loadView("/error.jsp");
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addUserRegistration(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@FormParam(FIRST_NAME) String firstName,
		@FormParam(LAST_NAME) String lastName,
		@FormParam(EMAIL_ADDRESS) String emailAddress,
		@FormParam(PHONE) String phone,
		@FormParam(COUNTRY_CODE) String countryCode,
		@FormParam(PASSWORD) String password,
		@FormParam(CONFIRM_PASSWORD) String confirmPassword,
		@FormParam(REFERRAL_CODE) String referralCode
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		String language = ProjectConstants.ENGLISH_ID;

		preprocessRequest(req, res);

		UserProfileModel senderUserModel = null;

		if (referralCode != null && !"".equalsIgnoreCase(referralCode)) {

			senderUserModel = UserProfileModel.getUserDetailsByReferralCode(referralCode.trim());

			if (senderUserModel == null) {

				setSuccessMessage(messageForKeyAdmin("labelInvalidReferralCode", language));
				return redirectToPage("/signup.do?referralCode=" + referralCode);
			}
		}

		boolean hasErrors = false;

		data.put(FIRST_NAME, firstName);
		data.put(LAST_NAME, lastName);
		data.put(EMAIL_ADDRESS, emailAddress);
		data.put(PHONE, phone);
		data.put(REFERRAL_CODE, referralCode);
		data.put(PASSWORD, password);
		data.put(CONFIRM_PASSWORD, confirmPassword);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(countryCode);
		data.put("countryCodeOptions", countryCodeOptions);

		String languageOptions = DropDownUtils.getLanguageStringLanguage(language, language);
		data.put("languageOptions", languageOptions);

		data.put("labelSignUp", messageForKeyAdmin("labelSignUp", language));
		data.put("labelCallMasa", messageForKeyAdmin("labelCallMasa", language));
		data.put("labelDownload", messageForKeyAdmin("labelDownload", language));
		data.put("labelFirstName", messageForKeyAdmin("labelFirstName", language));
		data.put("labelLastName", messageForKeyAdmin("labelLastName", language));
		data.put("labelEmail", messageForKeyAdmin("labelEmail", language));
		data.put("labelPassword", messageForKeyAdmin("labelPassword", language));
		data.put("labelConfirmPassword", messageForKeyAdmin("labelConfirmPassword", language));
		data.put("labelMobileNo", messageForKeyAdmin("labelMobileNo", language));
		data.put("labelReferralCode", messageForKeyAdmin("labelReferralCode", language));
		data.put("labelAgree", messageForKeyAdmin("labelAgree", language));
		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy", language));
		data.put("labelTermsofServices", messageForKeyAdmin("labelTermsofServices", language));
		data.put("labelAnd", messageForKeyAdmin("labelAnd", language));

		data.put("labelGO", messageForKeyAdmin("labelGO", language));
		data.put("labelEconomy", messageForKeyAdmin("labelEconomy", language));
		data.put("labelBusiness", messageForKeyAdmin("labelBusiness", language));
		data.put("labelExtra", messageForKeyAdmin("labelExtra", language));

		data.put("labelDOWNLOADTHEAPPHERE", messageForKeyAdmin("labelDOWNLOADTHEAPPHERE", language));
		data.put("labelCopyright", messageForKeyAdmin("labelCopyright", language));
		data.put("labelPasswordandConfirmpasswordshouldbesame", messageForKeyAdmin("labelPasswordandConfirmpasswordshouldbesame", language));
		data.put("errorReferralCodeIsRquired", messageForKeyAdmin("errorReferralCodeIsRquired", language));
		data.put("language", messageForKeyAdmin("language", language));

		data.put("REFERRAL_CODE_PHONE", BusinessApiAction.convertVerificationCode(ProjectConstants.REFERRAL_CODE_PHONE));

		hasErrors = hasErrorsEnglish(language);

		if (hasErrors) {

			data.put(PASSWORD, "");
			data.put(CONFIRM_PASSWORD, "");

			return loadView("/signup.jsp");

		} else {

			UserProfileModel userProfileModel = new UserProfileModel();

			userProfileModel.setUserId(UUIDGenerator.generateUUID());
			userProfileModel.setEmail(emailAddress);
			userProfileModel.setFirstName(firstName);
			userProfileModel.setLastName(lastName);
			userProfileModel.setPhoneNo(phone);
			userProfileModel.setPhoneNoCode(countryCode);
			userProfileModel.setPassword(password);
			userProfileModel.setRoleId(UserRoles.PASSENGER_ROLE_ID);

			userProfileModel.addUserRegistration(UserRoles.PASSENGER_ROLE_ID, null);

			String verificationCode = MyHubUtils.generateVerificationCode();

			UserProfileModel userModel = UserProfileModel.getUserAccountDetailsById(userProfileModel.getUserId());

			UserProfileModel.updateVerificationCodeForPassenger(userProfileModel.getUserId(), verificationCode);

			String message = String.format(messageForKeyAdmin("successMobileVerficationCode", language), userModel.getFullName(), verificationCode);
			MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userModel.getPhoneNoCode() + userModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

			if (senderUserModel != null) {

				ReferralCodeLogsModel referralCodeLogsModel = new ReferralCodeLogsModel();

				referralCodeLogsModel.setSenderId(senderUserModel.getUserId());
				referralCodeLogsModel.setReceiverId(userProfileModel.getUserId());
				referralCodeLogsModel.addReferralCodeLogs(userProfileModel.getUserId(), userProfileModel.getRoleId());
			}

			setSuccessMessage(messageForKeyAdmin("labelRegisteredSuccessfully", language));
		}

		return redirectToPage("/signup.do?referralCode=" + referralCode);
	}

	public boolean hasErrorsEnglish(String language) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(FIRST_NAME, messageForKeyAdmin("labelFirstName", language), new RequiredValidationRule());
		validator.addValidationMapping(LAST_NAME, messageForKeyAdmin("labelLastName", language), new RequiredValidationRule());
		validator.addValidationMapping(EMAIL_ADDRESS, messageForKeyAdmin("labelEmail", language), new RequiredValidationRule());
		validator.addValidationMapping(EMAIL_ADDRESS, messageForKeyAdmin("labelEmail", language), new EmailValidationRule());
		validator.addValidationMapping(EMAIL_ADDRESS, messageForKeyAdmin("labelEmail", language), new MaxLengthValidationRule(200));
		validator.addValidationMapping(EMAIL_ADDRESS, messageForKeyAdmin("labelEmail", language), new DuplicateEmailValidationRule(null));
		validator.addValidationMapping(PHONE, messageForKeyAdmin("labelMobileNo", language), new RequiredValidationRule());
		validator.addValidationMapping(PHONE, messageForKeyAdmin("labelMobileNo", language), new NumericValidationRule());
		validator.addValidationMapping(PHONE, messageForKeyAdmin("labelMobileNo", language), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(PASSWORD, messageForKeyAdmin("labelPassword", language), new RequiredValidationRule());
		validator.addValidationMapping(PASSWORD, messageForKeyAdmin("labelPassword", language), new MinMaxLengthValidationRule(6, 50));
		validator.addValidationMapping(CONFIRM_PASSWORD, messageForKeyAdmin("labelConfirmPassword", language), new RequiredValidationRule());
		validator.addValidationMapping(CONFIRM_PASSWORD, messageForKeyAdmin("labelConfirmPassword", language), new MinMaxLengthValidationRule(6, 50));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}
		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/site-common.js");
		requiredJS.add("js/signup.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

	@Override
	protected String[] requiredCss() {

		List<String> requiredCSS = new ArrayList<String>();

		requiredCSS.add("site-common.css");

		return requiredCSS.toArray(new String[requiredCSS.size()]);
	}

}