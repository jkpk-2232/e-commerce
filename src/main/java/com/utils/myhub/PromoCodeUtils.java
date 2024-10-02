package com.utils.myhub;

import java.util.Map;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AppointmentModel;
import com.webapp.models.OrderModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TourModel;
import com.webapp.models.VendorServiceCategoryModel;

public class PromoCodeUtils {

	public static void promoCodeCancel(TourModel tour, boolean updateTour) {

		if (tour.isPromoCodeApplied()) {

			PromoCodeModel promo = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tour.getPromoCodeId());

			if (promo != null) {

				long currentPromoCount = promo.getUsedCount();

				if (currentPromoCount > 0) {

					currentPromoCount = currentPromoCount - 1;

					promo.setUsedCount(currentPromoCount);

					promo.updatePromoCodeCount();
				}
			}

			if (updateTour) {

				tour.setPromoCodeApplied(false);

				tour.setPromoCodeId(null);

				tour.setPromoDiscount(0);

				tour.updatePromoCodeStatus();
			}
		}

	}

	public static boolean validatePromoCode(OrderModel orderModel, VendorServiceCategoryModel vscm, Map<String, Object> estimateFareMap) {

		if (orderModel.getPromoCode() == null || "".equalsIgnoreCase(orderModel.getPromoCode())) {
			return true;
		}

		PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(orderModel.getPromoCode());
		if (promoCode == null) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorInvalidPromoCode");
			return false;
		}

		// Check for
		// 1. All the vendors i.e., -1 OR Particular vendor
		// 2. All the service types i.e., -1 OR Vendor related service type id
		if ((promoCode.getVendorId().equalsIgnoreCase("-1") || promoCode.getVendorId().equalsIgnoreCase(orderModel.getOrderReceivedAgainstVendorId()))
					&& (promoCode.getServiceTypeId().equalsIgnoreCase("-1") || promoCode.getServiceTypeId().equalsIgnoreCase(vscm.getServiceTypeId()))) {

			long currentTime = DateUtils.nowAsGmtMillisec();
			if (currentTime < promoCode.getStartDate() || currentTime > promoCode.getEndDate()) {
				estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
				estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorPromoCodeExpired");
				return false;
			}

			orderModel.setOrderPromoCodeId(promoCode.getPromoCodeId());
			return true;
		} else {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorInvalidPromoCodeForVendorService");
			return false;
		}
	}

	public static boolean validatePromoCode(AppointmentModel appointmentModel, VendorServiceCategoryModel vscm, Map<String, Object> estimateFareMap) {

		if (appointmentModel.getPromoCode() == null || "".equalsIgnoreCase(appointmentModel.getPromoCode())) {
			return true;
		}

		PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCode(appointmentModel.getPromoCode());
		if (promoCode == null) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorInvalidPromoCode");
			return false;
		}

		// Check for
		// 1. All the vendors i.e., -1 OR Particular vendor
		// 2. All the service types i.e., -1 OR Vendor related service type id
		if ((promoCode.getVendorId().equalsIgnoreCase("-1") || promoCode.getVendorId().equalsIgnoreCase(appointmentModel.getAppointmentReceivedAgainstVendorId()))
					&& (promoCode.getServiceTypeId().equalsIgnoreCase("-1") || promoCode.getServiceTypeId().equalsIgnoreCase(vscm.getServiceTypeId()))) {

			long currentTime = DateUtils.nowAsGmtMillisec();
			if (currentTime < promoCode.getStartDate() || currentTime > promoCode.getEndDate()) {
				estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
				estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorPromoCodeExpired");
				return false;
			}

			appointmentModel.setAppointmentPromoCodeId(promoCode.getPromoCodeId());
			return true;
		} else {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorInvalidPromoCodeForVendorService");
			return false;
		}
	}

	public static PromoCodeModel applyAndCalculatePromoCode(double total, String promoCodeId) {

		PromoCodeModel tempPromoCodeReturnModel = new PromoCodeModel();
		double promoCodeDiscount = 0;
		double discountAmountTemp = 0;

		if (!StringUtils.validString(promoCodeId)) {
			tempPromoCodeReturnModel.setDiscount(0);
			tempPromoCodeReturnModel.setPromoCode(null);
			return tempPromoCodeReturnModel;
		}

		PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(promoCodeId);
		if (promoCode == null) {
			tempPromoCodeReturnModel.setDiscount(0);
			tempPromoCodeReturnModel.setPromoCode(null);
			return tempPromoCodeReturnModel;
		}

		if (promoCode.getMode().equalsIgnoreCase(ProjectConstants.PERCENTAGE_ID)) {

			discountAmountTemp = ((total * promoCode.getDiscount()) / 100);
			discountAmountTemp = Double.parseDouble(BusinessAction.df.format(discountAmountTemp));

			if (discountAmountTemp > promoCode.getMaxDiscount()) {
				discountAmountTemp = promoCode.getMaxDiscount();
			}

		} else {
			discountAmountTemp = promoCode.getDiscount();
		}

		if (discountAmountTemp > total) {
			// promo discount = 100
			// order total = 60
			// final promo discount = 60
			promoCodeDiscount = total;
		} else {
			// promo discount = 60
			// order total = 100
			// final promo discount = 60
			promoCodeDiscount = discountAmountTemp;
		}

		tempPromoCodeReturnModel.setPromoCode(promoCode.getPromoCode());
		tempPromoCodeReturnModel.setDiscount(promoCodeDiscount);

		return tempPromoCodeReturnModel;
	}
}