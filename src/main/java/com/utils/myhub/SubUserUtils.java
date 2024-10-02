package com.utils.myhub;

import com.webapp.models.BusinessOperatorModel;

public class SubUserUtils {

	public static void createSubUserMapping(String mainUserId, String subUserId) {
		BusinessOperatorModel businessOperatorModel = new BusinessOperatorModel();
		businessOperatorModel.setBusinessOwnerId(mainUserId);
		businessOperatorModel.setOperatorId(subUserId);
		businessOperatorModel.assignBusinessOperatorToBusinessOwner(mainUserId);
	}
}
