package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.PaymentTypeModel;

public interface PaymentTypeDao {

	PaymentTypeModel getPaymentTypeIdBy(@Param("paymentType") String paymentType);
}
