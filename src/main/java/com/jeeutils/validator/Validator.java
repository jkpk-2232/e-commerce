package com.jeeutils.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jeeutils.RequestUtils;

public class Validator {

	protected static final String SUCCESS = "Success";

	protected List<ValidationResult> results = null;

	protected List<ValidationMapping> mappings = new ArrayList<ValidationMapping>();

	protected ArrayList<String> errorMessages = new ArrayList<String>();

	Map<String, String> resultsValidation = new HashMap<String, String>();

	protected Map<String, List<ValidationResult>> validationResultsMap = new HashMap<String, List<ValidationResult>>();

	protected List<ValidationResult> errorMessagesArrListForAjax = new ArrayList<ValidationResult>();

	public List<ValidationResult> getErrorMessagesArrListForAjax() {
		return errorMessagesArrListForAjax;
	}

	public void setErrorMessagesArrListForAjax(List<ValidationResult> errorMessagesArrListForAjax) {
		this.errorMessagesArrListForAjax = errorMessagesArrListForAjax;
	}

	public List<ValidationResult> getResults() {
		return results;
	}

	public void setResults(List<ValidationResult> results) {
		this.results = results;
	}

	public Map<String, String> validate(Map<String, String> data) {

		Map<String, String> resultsForValidation = null;

		for (ValidationMapping mapping : mappings) {
			resultsForValidation = mapping.validate(data);
			resultsValidation.putAll(resultsForValidation);
		}

		return resultsValidation;
	}

	public ArrayList<String> validate(HttpServletRequest request) {

		for (ValidationMapping mapping : mappings) {

			results = mapping.validate(request);

			if (results != null) {

				for (ValidationResult validationResult : results) {

					if (validationResult.msgType != SUCCESS) {

						setErrorMessages(request, validationResult.paramName, validationResult.message);

						addErrorMessageToMapForAjax(validationResult);

						errorMessages.add(validationResult.message);

					}
				}

				if (results.size() == 0) {
					request.setAttribute(mapping.paramName + "Error", "");
				}

				validationResultsMap.put(mapping.paramName, results);
			}
		}

		this.setErrorMessagesArrListForAjax(errorMessagesArrListForAjax);

		return errorMessages;
	}

	public ArrayList<String> validate(Object object, Map<String, String> parameters) {

		for (ValidationMapping mapping : mappings) {

			results = mapping.validate(object, parameters);

			if (results != null) {

				for (ValidationResult validationResult : results) {

					if (validationResult.msgType != SUCCESS) {
						errorMessages.add(validationResult.message);
					}
				}

				validationResultsMap.put(mapping.paramName, results);
			}
		}

		return errorMessages;
	}

	public ArrayList<String> validate(Object object) {

		for (ValidationMapping mapping : mappings) {

			results = mapping.validate(object);

			if (results != null) {

				for (ValidationResult validationResult : results) {

					if (validationResult.msgType != SUCCESS) {
						errorMessages.add(validationResult.message);

					}
				}

				validationResultsMap.put(mapping.paramName, results);
			}
		}

		return errorMessages;
	}

	public void addValidationMapping(String paramName, String fieldName, AbstractValidationRule rule) {

		ValidationMapping validationMapping = null;

		if (!isRuleAddedForParamName(paramName)) {

			validationMapping = new ValidationMapping();
			validationMapping.paramName = paramName;
			validationMapping.fieldName = fieldName;
			validationMapping.addRuleToList(rule);
			mappings.add(validationMapping);

		} else {

			for (ValidationMapping mapping : mappings) {

				if (mapping.paramName.equalsIgnoreCase(paramName)) {
					validationMapping = mapping;
					validationMapping.addRuleToList(rule);
					break;
				}
			}
		}

	}

	private boolean isRuleAddedForParamName(String paramName) {

		boolean isRuleAdded = false;

		for (ValidationMapping mapping : mappings) {

			if (mapping.paramName.equalsIgnoreCase(paramName)) {
				isRuleAdded = true;
			}
		}

		return isRuleAdded;
	}

	public String getErrorMessageForField(String paramName) {

		String errorMessage = "";

		List<ValidationResult> validationResults = this.validationResultsMap.get(paramName);

		for (ValidationResult validationResult : validationResults) {

			if (validationResult.paramName.equalsIgnoreCase(paramName)) {

				if (validationResult.msgType != SUCCESS) {
					errorMessage = validationResult.message;
					break;
				}
			}
		}

		return errorMessage;
	}

	public String getErrorSummaryString(ArrayList<String> errorMessages) {

		String errorSummary = "<div class='error-summary'><ul>";

		for (String error : errorMessages) {
			errorSummary += "<li>" + error + "</li>";
		}

		errorSummary += "</ul></div>";

		return errorSummary;

	}

	public static void setErrorMessages(HttpServletRequest request, String paramName, String errorMessage) {

		String paramNameError = RequestUtils.requestAttributeValue(request, paramName + "Error");

		if (paramNameError.equals("") || paramNameError == null) {
			request.setAttribute(paramName + "Error", errorMessage);
		}

	}

	public void addErrorMessageToMapForAjax(ValidationResult validationResult) {
		errorMessagesArrListForAjax.add(validationResult);
	}
}