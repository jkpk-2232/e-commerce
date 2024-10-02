package com.webapp.actions.api;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.jets3t.service.S3ServiceException;

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.S3Utils;
import com.jeeutils.SendEmailThread;
import com.jeeutils.validator.DuplicateEmailValidationRule;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.EncryptionUtils;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.Action;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.DriverBankDetailsModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverModel;
import com.webapp.models.DriverReferralCodeLogModel;
import com.webapp.models.InputModel;
import com.webapp.models.ReferralCodeLogsModel;
import com.webapp.models.TourReferrerBenefitModel;
import com.webapp.models.UserCreditCardModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

@Path("/api/user")
public class UserProfileAction extends BusinessApiAction {

	Logger logger = Logger.getLogger(UserProfileAction.class);

	private static final String FIRSTNAME = "firstName";
	private static final String FIRSTNAME_LABEL = "First Name";

	private static final String LASTNAME = "lastName";
	private static final String LASTNAME_LABEL = "Last Name";

	private static final String EMAIL = "email";
	private static final String EMAIL_LABEL = "Email";

	private static final String PASSWORD = "password";
	private static final String PASSWORD_LABEL = "Password";

	private static final String OLDPASSWORD = "oldPassword";
	private static final String OLDPASSWORD_LABEL = "Old Password";

	private static final String NEWPASSWORD = "newPassword";
	private static final String NEWPASSWORD_LABEL = "New Password";

	private static final String DEVICE_UID = "deviceUid";
	private static final String DEVICE_UID_LABEL = "device Uid";

	private static final String DEVICE_TOKEN = "deviceToken";
	private static final String DEVICE_TOKEN_LABEL = "Device Token";

	private static final String PHONE_NO = "phoneNo";
	private static final String PHONE_NO_LABEL = "Phone number";

	private static final String PHONE_NO_CODE = "phoneNoCode";
	private static final String PHONE_NO_CODE_LABEL = "Phone number code";

	private static final String ROLE_ID = "roleId";
	private static final String ROLE_ID_LABEL = "Role id";

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response userRegistration(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			UserProfileModel userProfileModel
			) throws IOException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = userModelValidation(userProfileModel, headerVendorId);

		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		String referralCodeInput = userProfileModel.getReferralCode();

		UserProfileModel senderUserModel = new UserProfileModel();

		if (referralCodeInput != null && !"".equalsIgnoreCase(referralCodeInput)) {

			senderUserModel = getReferrerDetails(userProfileModel.getReferralCode().trim());

			if (senderUserModel == null) {
				return sendBussinessError(messageForKey("errorReferralCode", request));
			}
		}

		String driverReferralCodeInput = userProfileModel.getDriverReferralCode();

		DriverModel senderDriverModel = new DriverModel();

		if (driverReferralCodeInput != null && !"".equalsIgnoreCase(driverReferralCodeInput)) {

			senderDriverModel = DriverModel.getDriverDetailsByReferralCode(driverReferralCodeInput, ProjectConstants.DRIVER_APPLICATION_ACCEPTED);

			if (senderDriverModel == null) {
				return sendBussinessError(messageForKey("errorDriverReferralCode", request));
			}
		}

		userProfileModel.setUserId(UUIDGenerator.generateUUID());

		String userId = userProfileModel.addUserRegistration(UserRoles.PASSENGER_ROLE_ID, headerVendorId);

		if (userId == null) {
			return sendBussinessError(messageForKey("errorFailedToRegister", request));
		}

		String apiSessionKey = LoginUtils.createApiSessionKey(userProfileModel.getUserId());

		if (!"".equalsIgnoreCase(referralCodeInput)) {

			if (UserRoles.DRIVER_ROLE_ID.equals(senderUserModel.getRoleId())) {

				DriverReferralCodeLogModel driverReferralCodeLogModel = new DriverReferralCodeLogModel();
				driverReferralCodeLogModel.setDriverId(senderUserModel.getUserId());
				driverReferralCodeLogModel.setPassengerId(userId);
				driverReferralCodeLogModel.addDriverReferralCodeLog(userId);

				ReferralCodeLogsModel referralCodeLogsModel = new ReferralCodeLogsModel();
				referralCodeLogsModel.setSenderId(senderUserModel.getUserId());
				referralCodeLogsModel.setReceiverId(userId);
				referralCodeLogsModel.addReferralCodeLogs(userId, senderUserModel.getRoleId());

			} else {

				ReferralCodeLogsModel referralCodeLogsModel = new ReferralCodeLogsModel();
				referralCodeLogsModel.setSenderId(senderUserModel.getUserId());
				referralCodeLogsModel.setReceiverId(userId);
				referralCodeLogsModel.addReferralCodeLogs(userId, senderUserModel.getRoleId());
			}
		}

		UserProfileModel userModel = UserProfileModel.getUserAccountDetailsById(userProfileModel.getUserId());

		userModel.setPassword("");
		userModel.setApiSessionKey(apiSessionKey);

		UserCreditCardModel userCreditCardModel = UserCreditCardModel.getCreditCardDetails(userId);
		userModel.setCreditCardDetails(userCreditCardModel);

		String verificationCode = MyHubUtils.generateVerificationCode();

		UserProfileModel.updateVerificationCodeForPassenger(userProfileModel.getUserId(), verificationCode);

		String message = String.format(messageForKey("successMobileVerficationCode", request), userModel.getFullName(), convertVerificationCode(verificationCode));
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userProfileModel.getPhoneNoCode() + userProfileModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

		return sendDataResponse(userModel);
	}

	@Path("/password-update")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response ChangePassword(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			InputModel inputModel
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = passwordValidation(inputModel);

		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		UserModel dbUserModel = UserModel.getUserAccountDetailsById(userId);

		if (dbUserModel == null) {
			return sendBussinessError(messageForKey("errorNoEmailExists", request));
		}

		String password = EncryptionUtils.decryptByte(dbUserModel.getPassword());
		String newPassword = EncryptionUtils.encryptString(inputModel.getNewPassword());

		UserModel userModel = new UserModel();
		userModel.setUserId(userId);

		if (password.equals(inputModel.getOldPassword())) {
			userModel.setPassword(newPassword);
		} else {
			return sendBussinessError(messageForKey("errorInvalidOldPassword", request));
		}

		boolean result = userModel.updatePassword();

		if (result) {
			return sendSuccessMessage(messageForKey("successPasswordChanged", request));
		} else {
			return sendBussinessError(messageForKey("errorPasswordChanged", request));
		}
	}

	@Path("/phone-update")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updatePhone(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@QueryParam("phoneNo") String phoneNo,
			@QueryParam("phoneNoCode") String phoneNoCode
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		if (phoneNo != null && !phoneNo.equals("")) {

			if (!phoneNoCode.contains("+")) {
				phoneNoCode = "+" + phoneNoCode.trim();
			}

			UserModel userValidate = UserModel.getUserAccountDetailsById(userId);

			boolean isNumberExist = false;

			if (userValidate.getRoleId().equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {
				isNumberExist = UserModel.isPhoneNumberExistsForRole(phoneNo, phoneNoCode.trim(), userValidate.getRoleId(), userValidate.getUserId(), headerVendorId);
			} else {
				isNumberExist = UserModel.isPhoneNumberExistsForRole(phoneNo, phoneNoCode.trim(), userValidate.getRoleId(), userValidate.getUserId(), null);
			}

			if (isNumberExist) {
				return sendBussinessError(messageForKey("errorPassengerUpdatedPhoneError", request));
			}

			UserModel userModel = new UserModel();
			userModel.setUserId(userId);
			userModel.setPhoneNo(phoneNo.trim());
			userModel.setPhoneNoCode(phoneNoCode.trim());

			userModel.updatePhoneNumber();

			UserProfileModel userProfileModel = new UserProfileModel();
			userProfileModel.setUserId(userId);
			userProfileModel.setVerified(false);
			userProfileModel.updateVerificationStatus();
			userProfileModel.setPhoneNo(phoneNo.trim());
			userProfileModel.setPhoneNoCode(phoneNoCode.trim());

			String verificationCode = MyHubUtils.generateVerificationCode();

			UserProfileModel.updateVerificationCodeForPassenger(userProfileModel.getUserId(), verificationCode);

			String message = String.format(messageForKey("successMobileVerficationCode", request), userValidate.getFullName(), convertVerificationCode(verificationCode));
			MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userModel.getPhoneNoCode() + userModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

			UserProfileModel userReferralModel = new UserProfileModel();
			userReferralModel.setUserId(userId);
			userReferralModel.setReferralCode(userModel.getPhoneNo());
			userReferralModel.updateReferralCode(userId);

			return sendSuccessMessage(messageForKey("successPhoneNumberChanged", request));

		} else {

			return sendBussinessError(messageForKey("errorPhoneNumberChanged", request));
		}
	}

	@Path("/device")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deviceRegistration(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String key, 
			ApnsDeviceModel apnsDeviceModel
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(key);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = apnsDeviceModelValidation(apnsDeviceModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		apnsDeviceModel.setUserId(userId);
		apnsDeviceModel.setApiSessionKey(key);

		int status = apnsDeviceModel.insertApnsDeviceDetails();

		if (status > 0) {
			return sendSuccessMessage(messageForKey("successDeviceRegistered", request));

		}

		return sendUnexpectedError();
	}

	@Path("/badge-count")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getUserBadgeCount(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		int badgeCount = ApnsDeviceModel.getBadgeCount(loggedInUserId);

		if (badgeCount > 0) {

			Map<String, Object> outPutMap = new HashMap<String, Object>();
			outPutMap.put("badgeCount", badgeCount);
			return sendDataResponse(outPutMap);

		} else {
			badgeCount = 0;
			Map<String, Object> outPutMap = new HashMap<String, Object>();
			outPutMap.put("badgeCount", badgeCount);
			return sendDataResponse(outPutMap);
		}
	}

	@Path("/user-details")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getLoggedInUserDetails(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);

		if (userProfileModel != null) {

			if (userProfileModel.getUserRole().equals(UserRoles.DRIVER_ROLE)) {

				DriverInfoModel driverInfo = DriverInfoModel.getDriverAccountDetailsById(loggedInUserId);

				return sendDataResponse(driverInfo);
			}

			return sendDataResponse(userProfileModel);

		} else {

			return sendBussinessError(messageForKey("errorUserNotFound", request));
		}
	}

	@Path("/update-passenger")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response passengerUpdate(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			UserProfileModel userProfileModel
			) throws IOException, S3ServiceException, NoSuchAlgorithmException {
	//@formatter:on

		String loggedOnUserId = checkValidSession(sessionKeyHeader);

		if (loggedOnUserId == null) {

			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		UserModel userValidate = UserModel.getUserAccountDetailsById(loggedOnUserId);
		boolean isNumberExist = false;

		if (userValidate.getRoleId().equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {
			isNumberExist = UserModel.isPhoneNumberExistsForRole(userProfileModel.getPhoneNo(), userProfileModel.getPhoneNoCode(), UserRoles.PASSENGER_ROLE_ID, userValidate.getUserId(), headerVendorId);
		} else {
			isNumberExist = UserModel.isPhoneNumberExistsForRole(userProfileModel.getPhoneNo(), userProfileModel.getPhoneNoCode(), UserRoles.PASSENGER_ROLE_ID, userValidate.getUserId(), null);
		}

		if (isNumberExist) {
			return sendBussinessError(messageForKey("errorPassengerUpdatedPhoneError", request));
		}

		if (userValidate.getUserRole().equals(UserRoles.PASSENGER_ROLE)) {

			if (userProfileModel.getPhotoUrl() == null || "".equals(userProfileModel.getPhotoUrl())) {

				if ((userValidate.getPhotoUrl() != null) && (!"".equals(userValidate.getPhotoUrl()))) {
					S3Utils.deleteProfileImageFromBucket(userValidate.getPhotoUrl());
				}

			} else if (!userProfileModel.getPhotoUrl().equalsIgnoreCase(userValidate.getPhotoUrl())) {

				if ((userValidate.getPhotoUrl() != null) && (!"".equals(userValidate.getPhotoUrl()))) {
					S3Utils.deleteProfileImageFromBucket(userValidate.getPhotoUrl());
				}

			} else {

				userProfileModel.setPhotoUrl(userValidate.getPhotoUrl());
			}

			int status = userProfileModel.updatePassenger(loggedOnUserId);

			if (status > 0) {

				if (!userValidate.getPhoneNo().equalsIgnoreCase(userProfileModel.getPhoneNo())) {

					UserProfileModel userReferralModel = new UserProfileModel();
					userReferralModel.setUserId(userProfileModel.getUserId());
					userReferralModel.setReferralCode(userProfileModel.getPhoneNo());
					userReferralModel.updateReferralCode(loggedOnUserId);

					String verificationCode = MyHubUtils.generateVerificationCode();

					UserProfileModel.updateVerificationCodeForPassenger(userProfileModel.getUserId(), verificationCode);

					UserProfileModel user = UserProfileModel.getAdminUserAccountDetailsById(loggedOnUserId);

					user.setVerified(false);
					user.setUserId(loggedOnUserId);

					user.updateVerificationStatus();

					String message = String.format(messageForKey("successMobileVerficationCode", request), user.getFullName(), convertVerificationCode(verificationCode));
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userProfileModel.getPhoneNoCode() + userProfileModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);
				}

				return sendSuccessMessage(messageForKey("successPassengerUpdatedSuccess", request));

			} else {

				return sendBussinessError(messageForKey("errorPassengerUpdatedSuccess", request));
			}
		} else {

			return sendBussinessError(messageForKey("errorUserNotFound", request));
		}
	}

	@Path("/credit-card")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response creditCardUpdateVersion1(
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			UserCreditCardModel userCreditCardModel
			) throws IOException {
	//@formatter:on

		String loggedOnUserId = checkValidSession(sessionKeyHeader);

		if (loggedOnUserId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = creditCardValidation(userCreditCardModel);

		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		Map<String, Object> output = new HashMap<String, Object>();

		UserCreditCardModel userCreditCheck = UserCreditCardModel.getCreditCardDetails(loggedOnUserId);

		if (userCreditCheck == null) {

			userCreditCardModel.setUserId(loggedOnUserId);
			userCreditCardModel.addUserCreditCardDetails(loggedOnUserId);
		} else {

			userCreditCardModel.setUserId(loggedOnUserId);
			userCreditCardModel.updateUserCreditCardDetails1(loggedOnUserId);
		}

		UserCreditCardModel userCredit = UserCreditCardModel.getCreditCardDetails(loggedOnUserId);

		if (userCredit != null) {
			userCredit.setAuthCode(null);
			userCredit.setToken(null);
			output.put("cardAvailable", true);
			output.put("userCreditModel", userCredit);
		} else {
			output.put("cardAvailable", false);
			output.put("userCreditModel", null);
		}

		return sendDataResponse(output);
	}

	@Path("/update-driver")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverUpdate(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			DriverInfoModel driverInfoModel
			) throws IOException {
	//@formatter:on

		String loggedOnUserId = checkValidSession(sessionKeyHeader);

		if (loggedOnUserId == null) {
			return sendUnauthorizedRequestError();
		}

		UserModel userValidate = UserModel.getUserAccountDetailsById(loggedOnUserId);

		boolean isNumberExist = false;

		isNumberExist = UserModel.isPhoneNumberExistsForRole(driverInfoModel.getPhoneNo(), driverInfoModel.getPhoneNoCode(), UserRoles.DRIVER_ROLE_ID, userValidate.getUserId(), null);
		if (isNumberExist) {
			return sendBussinessError(messageForKey("errorPassengerUpdatedPhoneError", request));
		}

		if (userValidate.getUserRole().equals(UserRoles.DRIVER_ROLE)) {

			int status = driverInfoModel.updateDriverForApi(loggedOnUserId);

			if (status > 0) {

				UserModel user = UserModel.getUserAccountDetailsById(loggedOnUserId);
				DriverInfoModel driverModel = DriverInfoModel.getDriverAccountDetailsById(loggedOnUserId);

				driverModel.setFirstName(user.getFirstName());
				driverModel.setLastName(user.getLastName());
				driverModel.setUserId(user.getUserId());
				driverModel.setEmail(user.getEmail());

				DriverBankDetailsModel driverBankDetailsModel = DriverBankDetailsModel.getDriverBankDetails(loggedOnUserId);
				driverModel.setDriverBankDetails(driverBankDetailsModel);

				return sendSuccessMessage(messageForKey("successDriverUpdatedSuccess", request));

			} else {

				return sendBussinessError(messageForKey("errorPassengerUpdatedSuccess", request));
			}

		} else {

			return sendBussinessError(messageForKey("errorUserNotFound", request));
		}
	}

	@Path("/tip")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateTip(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			UserProfileModel userProfileModel
			) throws IOException {
	//@formatter:on

		String loggedOnUserId = checkValidSession(sessionKeyHeader);

		if (loggedOnUserId == null) {

			return sendUnauthorizedRequestError();
		}

		int status = userProfileModel.updateTip(loggedOnUserId);

		if (status > 0) {
			return sendSuccessMessage(messageForKey("successTipUpdate", request));
		}

		return sendBussinessError(messageForKey("errorTipUpdate", request));
	}

	@Path("/validation")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response checkValidUser(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@QueryParam("userId") String userId, 
			@QueryParam("deviceToken") String deviceToken, 
			@QueryParam("x-api-key") String sessionKey
			) {
	//@formatter:on

		if (userId == null) {

			return sendUnauthorizedRequestError();

		} else {

			return sendSuccessMessage(messageForKey("successValidUser", request));
		}

	}

	@Path("/forgot-password")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response ForgotPassword(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			UserModel userModel
			) throws IOException {
	//@formatter:on

		errorMessages = userModelValidationForForgotPassword(userModel);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		String userId = "";

		if ((userModel.getPhoneNoCode() == null) || "".equals(userModel.getPhoneNoCode())) {

			if (userModel.getRoleId() == null || "".equalsIgnoreCase(userModel.getRoleId())) {
				userId = UserModel.checkLoginCredentialsForLogin(null, null, null, ((userModel.getEmail()).trim()).toLowerCase(), null, null);
			} else {

				List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(userModel);

				userId = UserModel.checkLoginCredentialsForLogin(null, null, null, ((userModel.getEmail()).trim()).toLowerCase(), roleIds, null);
			}

		} else {

			String phoneNoCode = "";
			String phoneNo = userModel.getEmail().trim();

			if (!((userModel.getPhoneNoCode()).contains("+"))) {
				phoneNoCode = "+" + userModel.getPhoneNoCode();
			} else {
				phoneNoCode = userModel.getPhoneNoCode();
			}

			List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(userModel);

			userId = UserModel.checkLoginCredentialsForLogin(null, phoneNoCode, phoneNo, null, roleIds, null);
		}

		if (userId == null || "".equals(userId)) {
			return sendBussinessError(messageForKey("errorAccountDoesNotExists", request));
		}

		UserModel userModelForAccountCheck = UserModel.getUserAccountDetailsById(userId);
		if (userModelForAccountCheck == null) {
			return sendBussinessError(messageForKey("errorAccountDoesNotExists", request));
		}

		if (!UserRoleUtils.hasAppForgotPasswordAccess(userModelForAccountCheck.getRoleId())) {
			return sendBussinessError(messageForKey("errorInvalidUser", request));
		}

		String password = EncryptionUtils.decryptByte(userModelForAccountCheck.getPassword());
		userModelForAccountCheck.setPassword(password);

		if (password == null) {
			return sendBussinessError(messageForKey("errorFailedToGetPassword", request));
		}

		if ((userModel.getPhoneNoCode() == null) || "".equals(userModel.getPhoneNoCode())) {

			String subject = String.format(BusinessAction.messageForKeyAdmin("labelLoginCredentials", ProjectConstants.ENGLISH_ID), WebappPropertyUtils.PROJECT_NAME);
			String message = Action.getForgotPasswordMessage(userModelForAccountCheck.getEmail(), userModelForAccountCheck.getPassword());

			new SendEmailThread(userModelForAccountCheck.getEmail(), subject, message);

			return sendSuccessMessage(messageForKey("successPasswordSentSuccessfully", request));

		} else {

			String message = String.format(messageForKey("labelFleetManagementCredentials", request), userModelForAccountCheck.getPhoneNoCode() + userModelForAccountCheck.getPhoneNo(), userModelForAccountCheck.getPassword());
			MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userModelForAccountCheck.getPhoneNoCode() + userModelForAccountCheck.getPhoneNo(), ProjectConstants.SMSConstants.SMS_CREDENTIALS_TEMPLATE_ID);

			return sendSuccessMessage(messageForKey("successPasswordSentSuccessfullyBySMS", request));
		}
	}

	@Path("/notification/{status}")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateNotification(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKey, 
			@PathParam("status") boolean status
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKey);

		if (userId == null) {

			return sendUnauthorizedRequestError();
		}

		UserModel user = new UserModel();
		user.setUserId(userId);
		user.setNotificationStatus(status);

		int notificationStatus = user.updateNotificationStatus();

		if (notificationStatus > 0) {
			return sendSuccessMessage(messageForKey("successNotificationStatusUpdateSuccessfully", request));
		} else {
			return sendBussinessError(messageForKey("errorNotificationStatusUpdateSuccessfully", request));
		}
	}

	@Path("/verification")
	@PUT
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateVerificationStatus(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKey, 
			@QueryParam("verificationCode") String verificationCode
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKey);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		UserProfileModel receiverDetails = UserProfileModel.getAdminUserAccountDetailsById(userId);

		String verifyCode = UserProfileModel.getVerificationCodeOfUser(userId);

		if (verifyCode != null && verifyCode.equals(verificationCode)) {

			UserProfileModel userProfileModel = new UserProfileModel();
			userProfileModel.setUserId(userId);
			userProfileModel.setVerified(true);
			userProfileModel.setFirstTime(false);

			int notificationStatus = userProfileModel.updateVerificationStatus();

			if (notificationStatus > 0) {

				ReferralCodeLogsModel referralCodeLogsModel = ReferralCodeLogsModel.getReferralCodeLogsByReceiverId(userId);

				if (referralCodeLogsModel != null && receiverDetails.isFirstTime()) {

					UserProfileModel sender = UserProfileModel.getAdminUserAccountDetailsById(referralCodeLogsModel.getSenderId());

					if (sender != null) {

						if (UserRoles.DRIVER_ROLE_ID.equals(sender.getRoleId())) {

						} else {

							sender.setCredit(sender.getCredit() + referralCodeLogsModel.getSenderBenefit());
							sender.updateUserCredits();

						}

						receiverDetails.setCredit(receiverDetails.getCredit() + referralCodeLogsModel.getReceiverBenefit());
						receiverDetails.updateUserCredits();
					}
				}

				return sendSuccessMessage(messageForKey("successMobileNumberVerficationSuccess", request));

			} else {

				return sendBussinessError(messageForKey("errorMobileNumberVerfication", request));
			}

		} else {

			return sendBussinessError(messageForKey("errorMobileNumberVerfication", request));
		}
	}

	@Path("/code-resent")
	@POST
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response reSentVerificationCode(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKey
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKey);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		String verifyCode = UserProfileModel.getVerificationCodeOfUser(userId);

		if (verifyCode != null) {

			UserModel user = UserModel.getUserAccountDetailsById(userId);

			String message = String.format(messageForKey("successMobileVerficationCode", request), user.getFullName(), verifyCode);
			boolean isSmsSent = MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, user.getPhoneNoCode() + user.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

			if (isSmsSent) {

				return sendSuccessMessage(messageForKey("successVerficationCodeSentSuccessfully", request));

			} else {

				return sendBussinessError(messageForKey("errorVerficationCodeSent", request));
			}

		} else {

			return sendBussinessError(messageForKey("errorVerficationCodeSent", request));
		}
	}

	@Path("/credits")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCredits(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKey
			) {
	//@formatter:on

		String loginUserId = checkValidSession(sessionKey);

		if (loginUserId == null) {
			return sendUnauthorizedRequestError();
		}

		UserProfileModel user = UserProfileModel.getAdminUserAccountDetailsById(loginUserId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> output = new HashMap<String, Object>();

		if (user != null) {

			output.put("credit", df_new.format(user.getCredit()));
			output.put("referralCode", user.getReferralCode());
			output.put("receiverBenefit", adminSettingsModel.getReceiverBenefit());

			if (UserRoles.DRIVER_ROLE_ID.equals(user.getRoleId())) {

				output.put("senderBenefit", adminSettingsModel.getDriverReferralBenefit());

				double totalBenefit = TourReferrerBenefitModel.getTotalDriverBenefitByDriverId(loginUserId);

				output.put("totalBenefit", Double.parseDouble(df_new.format(totalBenefit)));

			} else {

				output.put("senderBenefit", adminSettingsModel.getSenderBenefit());
				output.put("totalBenefit", 0.0);
			}

			return sendDataResponse(output);
		}

		return sendBussinessError(messageForKey("errorInvalidUser", request));
	}

	private List<String> passwordValidation(InputModel inputModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(OLDPASSWORD, OLDPASSWORD_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(NEWPASSWORD, NEWPASSWORD_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(inputModel);

		return errorMessages;
	}

	private List<String> userModelValidation(UserProfileModel userProfileModel, String headerVendorId) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new DuplicateEmailValidationRule(headerVendorId));
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new EmailValidationRule());
		validator.addValidationMapping(PASSWORD, PASSWORD_LABEL, new RequiredValidationRule());

		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO_CODE, PHONE_NO_CODE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new DuplicatePhoneNumberValidationRule(userProfileModel.getPhoneNoCode(), UserRoles.PASSENGER_ROLE_ID, null, headerVendorId));

		errorMessages = validator.validate(userProfileModel);

		return errorMessages;
	}

	private List<String> apnsDeviceModelValidation(ApnsDeviceModel model) {

		Validator validator = new Validator();

		validator.addValidationMapping(DEVICE_UID, DEVICE_UID_LABEL, new RequiredValidationRule());

		validator.addValidationMapping(DEVICE_TOKEN, DEVICE_TOKEN_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(model);

		return errorMessages;
	}

	private List<String> userModelValidationForForgotPassword(UserModel userModel) throws IOException {

		Validator validator = new Validator();

		if (userModel.getPhoneNoCode() == null || "".equals(userModel.getPhoneNoCode())) {

			validator.addValidationMapping(EMAIL, EMAIL_LABEL, new RequiredValidationRule());
			validator.addValidationMapping(EMAIL, EMAIL_LABEL, new EmailValidationRule());

			errorMessages = validator.validate(userModel);
		} else {

			validator.addValidationMapping(PHONE_NO_CODE, PHONE_NO_CODE_LABEL, new RequiredValidationRule());

			errorMessages = validator.validate(userModel);

			if (errorMessages.size() < 1) {

				validator.addValidationMapping(EMAIL, PHONE_NO_LABEL, new RequiredValidationRule());

				errorMessages = validator.validate(userModel);

				if (errorMessages.size() < 1) {
					validator.addValidationMapping(ROLE_ID, ROLE_ID_LABEL, new RequiredValidationRule());
					errorMessages = validator.validate(userModel);
				}
			}

		}

		return errorMessages;
	}

	private List<String> creditCardValidation(UserCreditCardModel userCreditCard) throws IOException {

		Validator validator = new Validator();

		List<String> errorMessages = new ArrayList<String>();

		validator.addValidationMapping("cardNumber", "Card Number", new RequiredValidationRule());
		validator.addValidationMapping("token", "Token", new RequiredValidationRule());

		errorMessages = validator.validate(userCreditCard);

		return errorMessages;

	}

	private UserProfileModel getReferrerDetails(String referralCode) {

		UserProfileModel userProfileModel = null;

		List<UserProfileModel> senderList = UserProfileModel.getSenderListByReferralCode(referralCode);

		if (senderList.size() > 0) {

			AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

			int passengerCount = 0;
			int driverCount = 0;

			for (UserProfileModel userProfile : senderList) {

				if (UserRoles.PASSENGER_ROLE_ID.equals(userProfile.getRoleId())) {

					passengerCount++;

				} else if (UserRoles.DRIVER_ROLE_ID.equals(userProfile.getRoleId())) {

					driverCount++;
				}
			}

			if (driverCount > 0) {

				for (UserProfileModel userProfile : senderList) {

					if ((UserRoles.DRIVER_ROLE_ID.equals(userProfile.getRoleId())) && (adminSettings.getCountryCode().equals(userProfile.getPhoneNoCode()))) {

						userProfileModel = userProfile;

						break;
					}
				}

				if (userProfileModel != null) {

					for (UserProfileModel userProfile : senderList) {

						if (UserRoles.DRIVER_ROLE_ID.equals(userProfile.getRoleId())) {

							userProfileModel = userProfile;

							break;
						}
					}
				}

			} else if ((passengerCount > 0)) {

				for (UserProfileModel userProfile : senderList) {

					if ((UserRoles.PASSENGER_ROLE_ID.equals(userProfile.getRoleId())) && (adminSettings.getCountryCode().equals(userProfile.getPhoneNoCode()))) {

						userProfileModel = userProfile;

						break;
					}
				}

				if (userProfileModel != null) {

					for (UserProfileModel userProfile : senderList) {

						if (UserRoles.PASSENGER_ROLE_ID.equals(userProfile.getRoleId())) {

							userProfileModel = userProfile;

							break;
						}
					}
				}

			}
		}

		return userProfileModel;
	}

}