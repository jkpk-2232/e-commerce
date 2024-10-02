package com.jeeutils.validator;

import com.webapp.models.ProductSubCategoryModel;

public class DuplicateProductSubCategoryNameValidationRule extends AbstractValidationRule {
	
	boolean isDuplicate = false;

	private String productSubCategoryId;

	public DuplicateProductSubCategoryNameValidationRule(String productSubCategoryId) {
		this.productSubCategoryId = productSubCategoryId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = ProductSubCategoryModel.isProductSubCategoryNameExists(String.valueOf(paramObject).trim(), productSubCategoryId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}
