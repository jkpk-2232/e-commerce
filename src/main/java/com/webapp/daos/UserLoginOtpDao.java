package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UserLoginOtpModel;

public interface UserLoginOtpDao {

	UserLoginOtpModel getUserDetailsByOtp(@Param("verificationCode") String verificationCode,@Param("userId") String userId);
	
	int addVerificationCode (UserLoginOtpModel userLoginOtpModel);
	
	int deleteVerificationCode (String userId);
	
	int getVerificationCodeCount (UserLoginOtpModel userLoginOtpModel);
	
	String getVerificationCodeOfUser(String userId);

	UserLoginOtpModel getUserDetails(@Param("userLoginOtpId") String userLoginOtpId);
}
