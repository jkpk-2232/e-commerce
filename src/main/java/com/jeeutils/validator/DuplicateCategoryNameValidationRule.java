package com.jeeutils.validator;

import com.webapp.models.CategoryModel;

public class DuplicateCategoryNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private String categoryId;

	public DuplicateCategoryNameValidationRule(String categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = CategoryModel.isCategoryNameExists(String.valueOf(paramObject).trim(), categoryId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}