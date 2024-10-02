package com.jeeutils.validator;

import com.webapp.models.UnitOfMeasureModel;

public class DuplicateUomNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private Integer uomId;

	public DuplicateUomNameValidationRule(Integer uomId) {
		this.uomId = uomId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = UnitOfMeasureModel.isUomNameExists(String.valueOf(paramObject).trim(), uomId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}
