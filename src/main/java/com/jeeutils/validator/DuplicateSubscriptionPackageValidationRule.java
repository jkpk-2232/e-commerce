package com.jeeutils.validator;

import com.webapp.models.SubscriptionPackageModel;

public class DuplicateSubscriptionPackageValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;
	String subscriptionPackageId;

	public DuplicateSubscriptionPackageValidationRule(String subscriptionPackageId) {
		this.subscriptionPackageId = subscriptionPackageId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = SubscriptionPackageModel.isSubscriptionPackageExists(String.valueOf(paramObject).trim(), this.subscriptionPackageId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}
}