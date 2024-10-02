package com.jeeutils.validator;

public class DateCompareValidator extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {
		return fieldName + "  must be greater than start date";
	}

}