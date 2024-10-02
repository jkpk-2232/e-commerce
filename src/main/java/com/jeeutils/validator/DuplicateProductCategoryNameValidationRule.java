package com.jeeutils.validator;

import com.webapp.models.ProductCategoryModel;

public class DuplicateProductCategoryNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private String productCategoryId;

	public DuplicateProductCategoryNameValidationRule(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = ProductCategoryModel.isProductCategoryNameExists(String.valueOf(paramObject).trim(), productCategoryId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}
