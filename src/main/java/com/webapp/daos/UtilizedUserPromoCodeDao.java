package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.UtilizedUserPromoCodeModel;

public interface UtilizedUserPromoCodeDao {

	int addUtilizedUserPromoCodeList(UtilizedUserPromoCodeModel utilizedUserPromoCodeModel);

	UtilizedUserPromoCodeModel getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(@Param("userId") String userId, @Param("promoCodeId") String promoCodeId);

	int getPromoCodeUsedCount(@Param("promoCodeId") String promoCodeId);
}