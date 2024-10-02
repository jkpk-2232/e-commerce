package com.jeeutils.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public class ValidationMapping {

	private static Logger logger = Logger.getLogger(ValidationMapping.class);

	protected String paramName;

	protected String fieldName;

	protected List<AbstractValidationRule> rules = new ArrayList<AbstractValidationRule>();

	protected Map<String, String> validationResults = new HashMap<String, String>();

	private static final String ERROR = "Error";

	private static final String SUCCESS = "Success";

	public Map<String, String> validate(Map<String, String> data) {

		String resultStr = "";

		Object paramObject = data.get(this.paramName);
		logger.info("validatimg for this.paramName\t" + this.paramName);

		if (data.get(this.paramName).length() <= 0) {
			paramObject = null;
		}

		for (AbstractValidationRule rule : this.rules) {

			resultStr = rule.validate(paramObject, this.fieldName);

			if (resultStr.equals(SUCCESS)) {
			} else {
				validationResults.put(this.paramName + ERROR, resultStr);
			}
		}

		return validationResults;
	}

	public List<ValidationResult> validate(HttpServletRequest request) {

		List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

		String resultStr = "";

		Object paramObject = null;

		for (AbstractValidationRule rule : rules) {

			ValidationResult result = new ValidationResult();

			resultStr = rule.validate(paramObject, this.fieldName);

			result.paramName = this.paramName;

			if (resultStr.equals(SUCCESS)) {
				result.msgType = SUCCESS;
				result.message = "";
			} else {
				result.msgType = ERROR;
				result.message = resultStr;
			}

			validationResults.add(result);

		}

		return validationResults;
	}

	public List<ValidationResult> validate(Object object, Map<String, String> parameters) {

		List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

		String resultStr = "";

		Object paramObject = null;

		for (AbstractValidationRule rule : rules) {

			ValidationResult result = new ValidationResult();

			resultStr = rule.validate(paramObject, this.fieldName);

			result.paramName = this.paramName;

			if (resultStr.equals(SUCCESS)) {
				result.msgType = SUCCESS;
				result.message = "";
			} else {
				result.msgType = ERROR;
				result.message = resultStr;
			}

			validationResults.add(result);
		}

		return validationResults;
	}

	public List<ValidationResult> validate(Object object) {

		List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

		String resultStr = "";

		Object paramObject = null;

		try {
			paramObject = BeanUtils.getProperty(object, this.paramName);
		} catch (IllegalAccessException e) {
			logger.debug(this.paramName, e);
		} catch (InvocationTargetException e) {
			logger.debug(this.paramName, e);
		} catch (NoSuchMethodException e) {
			logger.debug(this.paramName, e);
		}

		for (AbstractValidationRule rule : rules) {

			ValidationResult result = new ValidationResult();

			resultStr = rule.validate(paramObject, this.fieldName);

			result.paramName = this.paramName;

			if (resultStr.equals(SUCCESS)) {
				result.msgType = SUCCESS;
				result.message = "";
			} else {
				result.msgType = ERROR;
				result.message = resultStr;
			}

			validationResults.add(result);

		}

		return validationResults;
	}

	public void addRuleToList(AbstractValidationRule validationRule) {
		this.rules.add(validationRule);
	}

}