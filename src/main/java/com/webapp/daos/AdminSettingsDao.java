package com.webapp.daos;

import com.webapp.models.AdminSettingsModel;

public interface AdminSettingsDao {

	AdminSettingsModel getAdminSettingsDetails();

	int updateAdminSettings(AdminSettingsModel adminSettingsModel);

	int updateAboutUsAdminSettings(AdminSettingsModel adminSettingsModel);

	int updatePrivacyPolicyAdminSettings(AdminSettingsModel adminSettingsModel);

	int updateRefundPolicyAdminSettings(AdminSettingsModel adminSettingsModel);

	int updateTermsConditionsAdminSettings(AdminSettingsModel adminSettingsModel);

	int updateBenefits(AdminSettingsModel adminSettingsModel);
	
	int updateDriverFareAdminSettings(AdminSettingsModel adminSettingsModel);
}