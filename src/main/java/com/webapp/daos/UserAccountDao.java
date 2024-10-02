package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UserAccountModel;

public interface UserAccountDao {

	int insertAccountBalanceDetails(UserAccountModel userAccountModel);

	int updateAccountBalanceDetails(UserAccountModel userAccountModel);

	UserAccountModel getAccountBalanceDetailsById(@Param("userAccountId") String userAccountId);

	UserAccountModel getAccountBalanceDetailsByUserId(@Param("userId") String userId);

	int getTotalUserAccountCount(Map<String, Object> inputMap);

	List<UserAccountModel> getUserAccountListForSearch(Map<String, Object> inputMap);

	int getFilteredUserAccountCount(Map<String, Object> inputMap);

	UserAccountModel getUserWithAccountDetails(Map<String, Object> inputMap);

	int getTotalVendorUserAccountCount(Map<String, Object> inputMap);

	List<UserAccountModel> getVendorUserAccountListForSearch(Map<String, Object> inputMap);

	int getFilteredVendorUserAccountCount(Map<String, Object> inputMap);

	List<UserAccountModel> getUserAccountDetailsListForExport(Map<String, Object> inputMap);

	int insertAccountBalanceDetailsBatch(@Param("userAccountModelList") List<UserAccountModel> userAccountModelList);

}