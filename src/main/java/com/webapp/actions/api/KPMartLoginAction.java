package com.webapp.actions.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.LoginUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.UserLoginOtpModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

@Path("/api/kp-mart")
public class KPMartLoginAction extends BusinessApiAction {

	@Path("/login")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response userLogin(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		UserModel userModel
		) throws IOException {
	//@formatter:on	
		
		if (userModel.getPhoneNo().length() == 10 && userModel.getPhoneNo() != null) {
			
			UserModel userModelDetails = new UserModel();
			
			userModelDetails = userModelDetails.getUserAccountDetailsByPhoneNumAndRoleId(userModel.getPhoneNo(), ProjectConstants.UserRoles.PASSENGER_ROLE_ID);
			
			if (userModelDetails != null) {
				UserLoginOtpModel.deleteVerificationCode(userModelDetails.getUserId());

				UserLoginOtpModel userLoginOtpModel = new UserLoginOtpModel();
				userLoginOtpModel.setUserId(userModelDetails.getUserId());
				userLoginOtpModel.setRoleId(userModelDetails.getRoleId());
				userLoginOtpModel.setVerificationCode(MyHubUtils.generateSixDigVerificationCode(6));
				userLoginOtpModel.addVerificationCode(userModelDetails.getUserId());
				userModel.setVerificationCode(userLoginOtpModel.getVerificationCode());
			}else if (!userModel.getFirstName().isEmpty() && !userModel.getLastName().isEmpty() &&
						!userModel.getEmail().isEmpty() && !userModel.getPhoneNoCode().isEmpty()) {
				
				boolean isEmailExists = UserModel.isEmailIdExistsByRoleId(userModel.getEmail(), ProjectConstants.UserRoles.PASSENGER_ROLE_ID);
				
				
				if (isEmailExists) {
					List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(ProjectConstants.UserRoles.PASSENGER_ROLE_ID);
					UserModel exsistsUserModel = UserModel.getUserAccountDetailsByRoleIdAndEmailId(roleIds, userModel.getEmail());
					
					UserLoginOtpModel.deleteVerificationCode(exsistsUserModel.getUserId());

					UserLoginOtpModel userLoginOtpModel = new UserLoginOtpModel();
					userLoginOtpModel.setUserId(exsistsUserModel.getUserId());
					userLoginOtpModel.setRoleId(exsistsUserModel.getRoleId());
					userLoginOtpModel.setVerificationCode(MyHubUtils.generateSixDigVerificationCode(6));
					userLoginOtpModel.addVerificationCode(exsistsUserModel.getUserId());
					userModel.setVerificationCode(userLoginOtpModel.getVerificationCode());
					
					if (exsistsUserModel.getPhoneNo() == null) {
						exsistsUserModel.setPhoneNo(userModel.getPhoneNo());
						exsistsUserModel.updatePhoneNum();
					}
					
				}else {
					UserProfileModel userProfileModel = new UserProfileModel();
					userProfileModel.setPhoneNo(userModel.getPhoneNo());
					userProfileModel.setVerificationCode(MyHubUtils.generateSixDigVerificationCode(6));
					userProfileModel.setFirstName(userModel.getFirstName());
					userProfileModel.setLastName(userModel.getLastName());
					userProfileModel.setEmail(userModel.getEmail());
					userProfileModel.setPhoneNoCode(userModel.getPhoneNoCode());
					userProfileModel.setFullName(userModel.getFullName());
					userProfileModel.setGender(userModel.getGender());
					userProfileModel.setRoleId(ProjectConstants.UserRoles.PASSENGER_ROLE_ID);
					userProfileModel.addMartUser();
					userModel.setVerificationCode(userProfileModel.getVerificationCode());
					// return sendBussinessError("The email is previously registered with us.");
				}
				
			} else {
				return sendBussinessError(messageForKey("errorUserNotFound", request));
			}
			
		} else {
			return sendBussinessError(messageForKey("errorInvalidPhoneNumber", request));
		}
		
		return sendDataResponse(userModel);
	}

	@Path("/verification/{phoneNumber}/{verificationCode}")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response phoneNumVerificationWithOtp(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@PathParam(FieldConstants.PHONE_NUMBER) String phoneNumber,
		@PathParam(FieldConstants.VERIFICATION_CODE) String verificationCode
		) throws IOException {
	//@formatter:on	

		if (phoneNumber.length() == 10 && phoneNumber != null && verificationCode.length() == 6) {
			UserModel userModel = new UserModel();
			userModel = userModel.getUserAccountDetailsByPhoneNumAndRoleId(phoneNumber, ProjectConstants.UserRoles.PASSENGER_ROLE_ID);

			if (userModel != null) {

				UserLoginOtpModel verifyOtpLogsModel = UserLoginOtpModel.getUserDetailsByOtp(verificationCode, userModel.getUserId());
				if (verifyOtpLogsModel == null) {
					return sendBussinessError(messageForKey("errorInvalidVerificationCode", request));
				} else {
					String apiSessionKey = LoginUtils.createApiSessionKey(userModel.getUserId());
					userModel.setApiSessionKey(apiSessionKey);
					return sendDataResponse(userModel);
				}
			}
		} else {
			return sendBussinessError(messageForKey("errorInvalidPhoneNumberOrVerificationCode", request));
		}

		return sendBussinessError(messageForKey("errorInvalidPhoneNumberOrVerificationCode", request));
	}
}