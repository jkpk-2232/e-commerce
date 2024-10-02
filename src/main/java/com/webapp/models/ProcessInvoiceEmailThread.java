package com.webapp.models;

import com.jeeutils.SendEmailThread;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.Action;
import com.webapp.actions.BusinessAction;

public class ProcessInvoiceEmailThread extends Thread {

	private String tourId;

	public ProcessInvoiceEmailThread(String tourId) {

		this.tourId = tourId;
		this.start();
	}

	@Override
	public void run() {

		try {
			sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		TourModel tourDetils = TourModel.getTourDetailsByTourId(this.tourId);

		boolean sendEmailOfOperatorToOwner = false;

		String ownerEmail = "";

		UserProfileModel userModel = UserProfileModel.getAdminUserAccountDetailsById(tourDetils.getPassengerId());

		if (userModel.getUserRole().equalsIgnoreCase(UserRoles.OPERATOR_ROLE)) {

			sendEmailOfOperatorToOwner = true;

			String ownerId = BusinessOperatorModel.getBusinessOwnerId(userModel.getUserId());

			UserProfileModel userModel1 = UserProfileModel.getAdminUserAccountDetailsById(ownerId);

			ownerEmail = userModel1.getEmail();
		}

		InvoiceModel invoiceModel = InvoiceModel.getInvoiceByTourId(tourDetils.getTourId());

		if (tourDetils.getpEmail() != null) {

			String messasge = Action.getInvoiceMessageNewTemplate(tourDetils, invoiceModel, tourDetils.getLanguage());

			new SendEmailThread(tourDetils.getpEmail(), BusinessAction.messageForKeyAdmin("labelInvoiceDetials", tourDetils.getLanguage()), messasge);

			if (sendEmailOfOperatorToOwner) {

				new SendEmailThread(ownerEmail, BusinessAction.messageForKeyAdmin("labelInvoiceDetials", tourDetils.getLanguage()), messasge);
			}
		}
	}
}