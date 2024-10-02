package com.webapp.daos;

import com.webapp.models.DriverWalletSettingsModel;

public interface DriverWalletSettingsDao {

	int updateDriverWalletSettings(DriverWalletSettingsModel driverWalletSettingsModel);

	DriverWalletSettingsModel getDriverWalletSettings();

}