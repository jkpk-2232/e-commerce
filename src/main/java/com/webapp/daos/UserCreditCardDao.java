package com.webapp.daos;

import com.webapp.models.UserCreditCardModel;

public interface UserCreditCardDao {

	int addUserCreditCardDetails(UserCreditCardModel userCreditCardModel);

	UserCreditCardModel getCreditCardDetails(String userId);

	int updateUserCreditCardDetails(UserCreditCardModel userCreditCardModel);
	
	int updateUserCreditCardDetails1(UserCreditCardModel userCreditCardModel);
	
	int editUserCreditCardDetails(UserCreditCardModel userCreditCardModel);
}
