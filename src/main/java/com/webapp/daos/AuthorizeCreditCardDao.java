package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AuthorizeCreditCardModel;

public interface AuthorizeCreditCardDao {
	
	public int addAuthorizeCreditCardDetails(AuthorizeCreditCardModel authorizeCreditCardModel);
	
	AuthorizeCreditCardModel getAuthorizeCreditCardDetails(@Param("userId") String userId);
	
	int updateAuthorizeCreditCardDetails(AuthorizeCreditCardModel authorizeCreditCardModel);
	
	boolean isAuthorizeCreditCardDetailsPresent(@Param("userId") String userId);
	
	boolean isAuthorizeCreditCardDetailsPresentByEmail(@Param("email") String email);
	
	int updateAuthorizeCreditCardDetailsByEmail(AuthorizeCreditCardModel authorizeCreditCardModel);
	
	AuthorizeCreditCardModel getAuthorizeCreditCardDetailsByEmail(@Param("email") String email);
	
	

}
