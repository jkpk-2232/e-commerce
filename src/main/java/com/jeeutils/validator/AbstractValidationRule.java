package com.jeeutils.validator;

import org.apache.log4j.Logger;

public abstract class AbstractValidationRule {

	protected static Logger logger = Logger.getLogger(ValidationMapping.class);

	protected static final String SUCCESS = "Success";

	protected String errorMessage = "";

	public abstract String validate(Object paramObject, String fieldName);

	public String validateNotNull(Object paramObject, String fieldName) {

		if (paramObject == null) {
			errorMessage = fieldName + " is required.";
			logger.debug("errorMessage" + errorMessage);
			return errorMessage;
		} else {
			return SUCCESS;
		}

	}

}