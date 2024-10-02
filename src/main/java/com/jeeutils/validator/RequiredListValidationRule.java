package com.jeeutils.validator;

public class RequiredListValidationRule extends AbstractValidationRule {

	private int size;

	public RequiredListValidationRule(int size) {
		this.size = size;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		if (size == 0) {
			errorMessage = "Select atleast one " + fieldName + ".";
			return errorMessage;
		} else {
			return SUCCESS;
		}

	}

}