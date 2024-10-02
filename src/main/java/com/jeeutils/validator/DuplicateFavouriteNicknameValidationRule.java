package com.jeeutils.validator;

import com.webapp.models.FavouriteLocationsModel;

public class DuplicateFavouriteNicknameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	String favouriteLocationsId;

	String userId;

	public DuplicateFavouriteNicknameValidationRule(String favouriteLocationsId, String userId) {
		this.favouriteLocationsId = favouriteLocationsId;
		this.userId = userId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {

			return nullCheck;
		}

		isDuplicate = FavouriteLocationsModel.isFavouriteNicknameExists(String.valueOf(paramObject).trim(), this.favouriteLocationsId, this.userId);

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}