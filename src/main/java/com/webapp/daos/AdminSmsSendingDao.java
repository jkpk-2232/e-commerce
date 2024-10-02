package com.webapp.daos;

import com.webapp.models.AdminSmsSendingModel;

public interface AdminSmsSendingDao {

	AdminSmsSendingModel getAdminSmsSendingDetails();

	int updateAdminSmsSending(AdminSmsSendingModel adminSmsSendingModel);

	int updatePreviousAdminSmsSendingEntryToFalse();
}