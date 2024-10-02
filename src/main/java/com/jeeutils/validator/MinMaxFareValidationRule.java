package com.jeeutils.validator;

public class MinMaxFareValidationRule extends AbstractValidationRule {

	private String errorMessage = "";

	private int minValue;

	private int maxValue;

	public MinMaxFareValidationRule(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		double paramValue = Double.parseDouble(paramObject.toString());

		if (!paramObject.toString().equals("")) {

			if (paramValue <= this.minValue) {
				errorMessage = fieldName + " should be greater than " + this.minValue;
				return errorMessage;
			} else if (paramValue > this.maxValue) {
				errorMessage = fieldName + " should not be greater than of value " + this.maxValue;
				return errorMessage;
			}
		}

		return SUCCESS;
	}

}