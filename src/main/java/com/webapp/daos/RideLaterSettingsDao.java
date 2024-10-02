package com.webapp.daos;

import com.webapp.models.RideLaterSettingsModel;

public interface RideLaterSettingsDao {

	RideLaterSettingsModel getRideLaterSettingsDetails();

	int updateRideLaterSettings(RideLaterSettingsModel rideLaterSettingsModel);
}