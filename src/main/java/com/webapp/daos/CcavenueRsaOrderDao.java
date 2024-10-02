package com.webapp.daos;

import com.webapp.models.CcavenueRsaOrderModel;

public interface CcavenueRsaOrderDao {
	
	int insertCcavenueRsaOrderDetails(CcavenueRsaOrderModel ccavenueRsaOrderModel);
	
	boolean isOrderIdExists(String orderId);

}