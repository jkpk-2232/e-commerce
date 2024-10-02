package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.PromoCodeModel;

public interface PromoCodeDao {

	int addPromoCode(PromoCodeModel promoCodeModel);

	int getPromoCodeCount(@Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("serviceTypeId") String serviceTypeId, @Param("vendorId") String vendorId, @Param("assignedRegionList") List<String> assignedRegionList);

	List<PromoCodeModel> getPromoCodeListForSearch(@Param("start") int start, @Param("length") int length, @Param("order") String order, @Param("globalSearchString") String globalSearchString, @Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong,
				@Param("serviceTypeId") String serviceTypeId, @Param("vendorId") String vendorId, @Param("assignedRegionList") List<String> assignedRegionList, @Param("maxDiscountSearch") double maxDiscountSearch);

	int getTotalPromoCodeCountBySearch(@Param("globalSearchString") String globalSearchString, @Param("startDatelong") long startDatelong, @Param("endDatelong") long endDatelong, @Param("serviceTypeId") String serviceTypeId, @Param("vendorId") String vendorId,
				@Param("assignedRegionList") List<String> assignedRegionList, @Param("maxDiscountSearch") double maxDiscountSearch);

	int deletePromoCode(PromoCodeModel promoCodeModel);

	boolean isPromoCodeExists(@Param("promoCode") String promoCode);

	PromoCodeModel getPromoCodeDetailsByPromoCode(@Param("promoCode") String promoCode);

	PromoCodeModel getPromoCodeDetailsByPromoCodeId(@Param("promoCodeId") String promoCodeId);

	int updatePromoCodeCount(PromoCodeModel promoCodeModel);

	int activatePromoCode(PromoCodeModel promoCodeModel);

	int deactivatePromoCode(PromoCodeModel promoCodeModel);

	PromoCodeModel getActiveDeactivePromoCodeDetailsByPromoCodeId(@Param("promoCodeId") String promoCodeId);

	int updatePromoCodeDetails(PromoCodeModel promoCodeModel);
	
	List<PromoCodeModel> getPromoCodeListByserviceTypeIdAndVendorId(@Param("serviceTypeId")String serviceTypeId,@Param("vendorId") String vendorId,@Param("assignedRegionList") List<String> assignedRegionList);
}