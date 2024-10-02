package com.jeeutils.validator;

import com.webapp.models.ProductTemplateModel;

public class DuplicateProductNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private String productTemplateId;

	public DuplicateProductNameValidationRule(String productTemplateId) {
		this.productTemplateId = productTemplateId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = ProductTemplateModel.isProductNameExists(String.valueOf(paramObject).trim(), productTemplateId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}
