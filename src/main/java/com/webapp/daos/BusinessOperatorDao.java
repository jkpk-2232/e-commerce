package com.webapp.daos;

import com.webapp.models.BusinessOperatorModel;

public interface BusinessOperatorDao {

	int assignBusinessOperatorToBusinessOwner(BusinessOperatorModel businessOperatorModel);

	String getBusinessOwnerId(String userId);

}
