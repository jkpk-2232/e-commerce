package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UserPromoCodeModel;

public interface UserPromoCodeDao {

	int addUserPromoCodeList(@Param("messages") List<UserPromoCodeModel> userPromoCodeList);

	UserPromoCodeModel getUserPromoCodeDetailsByPromoCodeIdAndUserId(@Param("userId") String userId, @Param("promoCodeId") String promoCodeId);
}
