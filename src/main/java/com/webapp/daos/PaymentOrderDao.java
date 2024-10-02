package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.PaymentOrdersModel;

public interface PaymentOrderDao {

	void insertPaymentOrder(PaymentOrdersModel paymentOrdersModel);

	PaymentOrdersModel getPaymentDetailsByOrderId(@Param("orderId") String orderId);

	void updatePaymentStatus(PaymentOrdersModel paymentOrdersModel);

	boolean isOrderIdExists(String orderId);

}
