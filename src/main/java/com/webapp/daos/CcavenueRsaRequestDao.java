package com.webapp.daos;

import com.webapp.models.CcavenueRsaRequestModel;

public interface CcavenueRsaRequestDao {
	
	int insertCcavenueRsaRequestDetails(CcavenueRsaRequestModel ccavenueRsaRequestModel);
	
	CcavenueRsaRequestModel getCcavenueRsaRequestByOrderId(String orderId);

}