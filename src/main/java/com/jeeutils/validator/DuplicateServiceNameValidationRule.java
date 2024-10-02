package com.jeeutils.validator;

import com.webapp.models.ServiceModel;

public class DuplicateServiceNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private String serviceId;

	public DuplicateServiceNameValidationRule(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = ServiceModel.isServiceNameExists(String.valueOf(paramObject).trim(), serviceId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}