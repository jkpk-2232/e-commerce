package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.PhonepePaymentModel;

public interface PhonepePaymentDao {

	void insertPhonepePayment(PhonepePaymentModel phonepePaymentModel);

	PhonepePaymentModel getPhonepePaymentDetailsByPhonepePaymentId(@Param("phonepePaymentId") String  phonepePaymentId);

	PhonepePaymentModel getPhonepePaymentDetailsByMerchantTransactionId(@Param("merchantTransactionId") String merchantTransactionId);

	void updatePhonepePaymentDetails(PhonepePaymentModel phonepePaymentModel);

	int getPhonepePaymentListCount(Map<String, Object> inputMap);

	List<PhonepePaymentModel> getPhonepePaymentListBySearch(Map<String, Object> inputMap);

	List<PhonepePaymentModel> getPhonepeLogsReport(Map<String, Object> inputMap);

}
