package com.webapp.actions.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.AuthorizeCreditCardModel;
import com.webapp.models.DriverDutyLogsModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserCreditCardModel;
import com.webapp.models.UserModel;

@Path("/api/login")
public class LoginAction extends BusinessApiAction {

	private static final String EMAIL = "email";
	private static final String EMAIL_LABEL = "email";

	private static final String PASSWORD = "password";
	private static final String PASSWORD_LABEL = "Password";

	private static final String PHONE_NO_LABEL = "Phone number";

	private static final String PHONE_NO_CODE = "phoneNoCode";
	private static final String PHONE_NO_CODE_LABEL = "Phone number code";

	private static final String ROLE_ID = "roleId";
	private static final String ROLE_ID_LABEL = "Role id";

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response userLogin(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		UserModel userModel
		) throws IOException {
	//@formatter:on

		errorMessages = userModelValidation(userModel);

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
				userId = UserModel.checkLoginCredentialsForLogin(userModel.getPassword(), null, null, ((userModel.getEmail()).trim()).toLowerCase(), null, null);
			} else {

				List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(userModel);

				userId = UserModel.checkLoginCredentialsForLogin(userModel.getPassword(), null, null, ((userModel.getEmail()).trim()).toLowerCase(), roleIds, null);
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

			userId = UserModel.checkLoginCredentialsForLogin(userModel.getPassword(), phoneNoCode, phoneNo, null, roleIds, null);
		}

		if (userId != null) {

			UserModel userInfo = UserModel.getUserActiveDeativeDetailsById(userId);

			if (!userInfo.isActive()) {

				return sendBussinessError(messageForKey("errorUserAccountDeactive", request));
			}

			if (userInfo.getUserRole().equals(UserRoles.PASSENGER_ROLE)) {

				String apiSessionKey = LoginUtils.createApiSessionKey(userId);
				int badgeCount = ApnsDeviceModel.getBadgeCount(userId);

				userInfo.setApiSessionKey(apiSessionKey);

				TourModel tour = TourModel.getCurrentTourByPassangerId(userId);

				if (tour != null) {

				//@formatter:off
				if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR) || 
				    tour.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
				//@formatter:on

						userInfo.setStatus("inprogress");

					} else if (tour.getStatus().equals(ProjectConstants.PENDING_REQUEST)) {

						userInfo.setStatus("pending");

					} else {

						userInfo.setStatus("accepted");
					}
				}

				userInfo.setBadgeCount(badgeCount);

				userInfo.setPassword("");

				AuthorizeCreditCardModel authorizeCreditCardModelDummy = AuthorizeCreditCardModel.getAuthorizeCreditCardDetails(userId);

				UserCreditCardModel userCreditCardModel = new UserCreditCardModel();

				if (authorizeCreditCardModelDummy != null) {

					userCreditCardModel.setAuthCode(null);
					userCreditCardModel.setToken(null);

					userCreditCardModel.setCardNumber(authorizeCreditCardModelDummy.getCardNumber());

					userInfo.setCardAvailable(true);

					userInfo.setCreditCardDetails(userCreditCardModel);
				} else {

					userInfo.setCardAvailable(false);
					userInfo.setCreditCardDetails(null);
				}

				return sendDataResponse(userInfo);

			} else if (userInfo.getUserRole().equals(UserRoles.DRIVER_ROLE)) {

				if (!userInfo.isApprovelStatus()) {
					return sendBussinessError(messageForKey("errorApprovalPending", request));
				}

				return loginDriver(request, userModel, userId);

			} else if (userInfo.getUserRole().equals(UserRoles.BUSINESS_OWNER_ROLE) || userInfo.getUserRole().equals(UserRoles.OPERATOR_ROLE)) {

				userInfo.setVerified(true);

				String apiSessionKey = LoginUtils.createApiSessionKey(userId);
				userInfo.setApiSessionKey(apiSessionKey);
				int badgeCount = ApnsDeviceModel.getBadgeCount(userId);
				userInfo.setBadgeCount(badgeCount);

				userInfo.setPassword("");
				return sendDataResponse(userInfo);

			} else {

				return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
			}
		}

		return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
	}

	public Response loginDriver(HttpServletRequest request, UserModel userModel, String userId) {

		DriverDutyLogsModel lastDriverDutyLogDetails = DriverDutyLogsModel.getLastDriverDutyLogDetails(userId);

		DriverDutyLogsModel driverDutyLogsModel = new DriverDutyLogsModel();
		driverDutyLogsModel.setDriverId(userId);
		driverDutyLogsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());

		TourModel driverCurrentTour = TourModel.getCurrentTourByDriverId(userId);

		userModel.setUserId(userId);

		if (driverCurrentTour != null) {
			userModel.setOnDuty(true);
			driverDutyLogsModel.setDutyStatus(true);
		} else {
			userModel.setOnDuty(false);
			driverDutyLogsModel.setDutyStatus(false);
		}

		userModel.driverOnOffDuty();

		if (lastDriverDutyLogDetails != null) {

			if (lastDriverDutyLogDetails.isDutyStatus() ^ driverDutyLogsModel.isDutyStatus()) {

				driverDutyLogsModel.addDriverDutyLogs();

				if ((!driverDutyLogsModel.isDutyStatus()) && (lastDriverDutyLogDetails.isDutyStatus())) {

					long startTimeInMilliInDefaultTimeZone = lastDriverDutyLogDetails.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;
					long endTimeInMilliInDefaultTimeZone = driverDutyLogsModel.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;

					DriverAction.calculateLoggedInTimeAndSave(userId, userId, startTimeInMilliInDefaultTimeZone, endTimeInMilliInDefaultTimeZone);
				}
			}
		}

		String apiSessionKey = LoginUtils.createApiSessionKey(userId);
		int badgeCount = ApnsDeviceModel.getBadgeCount(userId);

		DriverInfoModel driverInfo = DriverInfoModel.getDriverAccountDetailsById(userId);

		if (!driverInfo.getApplicationStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
			return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
		}

		driverInfo.setApiSessionKey(apiSessionKey);

		driverInfo.setBadgeCount(badgeCount);

		driverInfo.setPassword("");

		return sendDataResponse(driverInfo);
	}

	@Path("/vendor")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorLogin(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		UserModel userModel
		) throws IOException {
	//@formatter:on

		errorMessages = userModelValidation(userModel);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		String userId = null;

		if ((userModel.getPhoneNoCode() == null) || "".equals(userModel.getPhoneNoCode())) {

			if (userModel.getRoleId() == null || "".equalsIgnoreCase(userModel.getRoleId())) {
				userId = UserModel.checkLoginCredentialsForLogin(userModel.getPassword(), null, null, ((userModel.getEmail()).trim()).toLowerCase(), null, null);
			} else {

				List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(userModel);

				userId = UserModel.checkLoginCredentialsForLogin(userModel.getPassword(), null, null, ((userModel.getEmail()).trim()).toLowerCase(), roleIds, null);
			}

		} else {

			String phoneNoCode = "";
			String phoneNo = userModel.getEmail().trim();

			if (!(userModel.getPhoneNoCode()).contains("+")) {
				phoneNoCode = "+" + userModel.getPhoneNoCode();
			} else {
				phoneNoCode = userModel.getPhoneNoCode();
			}

			List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(userModel);

			userId = UserModel.checkLoginCredentialsForLogin(userModel.getPassword(), phoneNoCode, phoneNo, null, roleIds, null);
		}

		if (userId == null) {
			return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
		}

		UserModel userInfo = UserModel.getUserActiveDeativeDetailsById(userId);

		if (!userInfo.isActive()) {
			return sendBussinessError(messageForKey("errorUserAccountDeactive", request));
		}

		if (!UserRoleUtils.isVendorAndSubVendorRole(userInfo.getRoleId())) {
			return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
		}

		String apiSessionKey = LoginUtils.createApiSessionKey(userId);
		userInfo.setApiSessionKey(apiSessionKey);
		userInfo.setPassword("");
		return sendDataResponse(userInfo);
	}

	@Path("/owner-operator")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response ownerOperatorLogin(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			UserModel userModel
			) throws IOException {
	//@formatter:on

		errorMessages = userModelValidation(userModel);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		UserModel dummyUserModel = new UserModel();

		dummyUserModel.setEmail(userModel.getEmail());
		dummyUserModel.setPassword(userModel.getPassword());

		String userId = userModel.checkLoginCredentialsRoleWiseOwnerOperator();

		if (userId != null) {

			UserModel userInfo = UserModel.getUserAccountDetailsById(userId);

			if (userInfo.getUserRole().equals(UserRoles.BUSINESS_OWNER_ROLE) || userInfo.getUserRole().equals(UserRoles.OPERATOR_ROLE)) {

				userInfo.setVerified(true);

				String apiSessionKey = LoginUtils.createApiSessionKey(userId);
				userInfo.setApiSessionKey(apiSessionKey);
				int badgeCount = ApnsDeviceModel.getBadgeCount(userId);
				userInfo.setBadgeCount(badgeCount);

				userInfo.setPassword("");
				return sendDataResponse(userInfo);

			} else {

				return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
			}
		}

		Map<String, Object> output = new HashMap<String, Object>();

		userId = dummyUserModel.checkLoginCredentials();

		if (userId != null) {

			UserModel userInfo = UserModel.getUserAccountDetailsById(userId);

			if (userInfo != null) {
				output.put("message", "Invalid login type.");
				output.put("roleId", userInfo.getRoleId());
				output.put("statusCode", "400");

				return sendDataResponse(output);
			}
		}

		return sendBussinessError(messageForKey("errorInvalidLoginCredentials", request));
	}

	private List<String> userModelValidation(UserModel userProfileModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(PASSWORD, PASSWORD_LABEL, new RequiredValidationRule());

		if (userProfileModel.getPhoneNoCode() == null || "".equals(userProfileModel.getPhoneNoCode())) {

			validator.addValidationMapping(EMAIL, EMAIL_LABEL, new RequiredValidationRule());
			validator.addValidationMapping(EMAIL, EMAIL_LABEL, new EmailValidationRule());

			errorMessages = validator.validate(userProfileModel);
		} else {

			validator.addValidationMapping(PHONE_NO_CODE, PHONE_NO_CODE_LABEL, new RequiredValidationRule());

			errorMessages = validator.validate(userProfileModel);

			if (errorMessages.size() < 1) {

				validator.addValidationMapping(EMAIL, PHONE_NO_LABEL, new RequiredValidationRule());

				errorMessages = validator.validate(userProfileModel);

				if (errorMessages.size() < 1) {
					validator.addValidationMapping(ROLE_ID, ROLE_ID_LABEL, new RequiredValidationRule());
					errorMessages = validator.validate(userProfileModel);
				}
			}

		}

		errorMessages = validator.validate(userProfileModel);

		return errorMessages;
	}
}