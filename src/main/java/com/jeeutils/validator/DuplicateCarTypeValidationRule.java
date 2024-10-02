package com.jeeutils.validator;

import com.webapp.models.CarTypeModel;

public class DuplicateCarTypeValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;
	String carTypeId;

	public DuplicateCarTypeValidationRule(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = CarTypeModel.isCarTypeNameExists(String.valueOf(paramObject).trim(), this.carTypeId);

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}