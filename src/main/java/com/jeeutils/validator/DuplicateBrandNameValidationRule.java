package com.jeeutils.validator;

import com.webapp.models.BrandModel;

public class DuplicateBrandNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private String brandId;

	public DuplicateBrandNameValidationRule(String brandId) {
		this.brandId = brandId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = BrandModel.isBrandNameExists(String.valueOf(paramObject).trim(), brandId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}
