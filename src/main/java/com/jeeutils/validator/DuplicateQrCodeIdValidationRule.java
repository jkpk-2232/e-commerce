package com.jeeutils.validator;

import com.webapp.models.QrProfileModel;

public class DuplicateQrCodeIdValidationRule extends AbstractValidationRule {
	
	boolean isDuplicate = false;

	private String qrProfileId;

	public DuplicateQrCodeIdValidationRule(String qrProfileId) {
		this.qrProfileId = qrProfileId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = QrProfileModel.isQrCodeIdExists(String.valueOf(paramObject).trim(), qrProfileId);

		if (isDuplicate == false) {
			return SUCCESS;

		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}
