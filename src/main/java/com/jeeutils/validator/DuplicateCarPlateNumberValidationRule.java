package com.jeeutils.validator;

import com.webapp.models.CarModel;

public class DuplicateCarPlateNumberValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;
	String carId;

	public DuplicateCarPlateNumberValidationRule(String carId) {
		this.carId = carId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = CarModel.isCarPlateExists(String.valueOf(paramObject).trim(), this.carId);

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}