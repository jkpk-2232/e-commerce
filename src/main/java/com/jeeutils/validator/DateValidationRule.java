package com.jeeutils.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidationRule extends AbstractValidationRule {

	private SimpleDateFormat dateFormat;

	public DateValidationRule(String dateFormat) {
		this.dateFormat = new SimpleDateFormat(dateFormat);
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		try {
			SimpleDateFormat sdf = dateFormat;

			Date dateToBeValidated = sdf.parse(paramObject.toString());

			if (!sdf.format(dateToBeValidated).equals(paramObject.toString())) {
				errorMessage = "Enter date in  " + dateFormat.toPattern();
				return errorMessage;
			}

		} catch (ParseException e) {
			errorMessage = "Enter date in  " + dateFormat.toPattern();
			return errorMessage;
		}

		return SUCCESS;
	}

}