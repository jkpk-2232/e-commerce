package com.webapp.daos;

import java.sql.SQLException;

import com.webapp.models.UserPaymentProfileModel;

public interface UserPaymentProfileDao {
	
	public int insertUserPaymentProfile(UserPaymentProfileModel userPaymentProfileModel) throws SQLException;
	
	public UserPaymentProfileModel getLatesetUserPaymentProfileModel(String userId) throws SQLException;
}
