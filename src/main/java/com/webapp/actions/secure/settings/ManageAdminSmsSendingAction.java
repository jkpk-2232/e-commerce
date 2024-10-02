package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.utils.myhub.DropDownUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.ADMIN_SMS_SETTINGS_ENUM;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSmsSendingModel;

@Path("/manage-admin-sms-sending")
public class ManageAdminSmsSendingAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAdminSmsSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		List<String> passengerSmsListApp = getPassengerSmsListByPlatformType(UserRoles.PASSENGER_ROLE_ID, adminSmsSendingModel);
		List<String> driverSmsListApp = getPassengerSmsListByPlatformType(UserRoles.DRIVER_ROLE_ID, adminSmsSendingModel);
		List<String> adminSmsListApp = getPassengerSmsListByPlatformType(UserRoles.ADMIN_ROLE_ID, adminSmsSendingModel);

		String passengerSmsAppOptions = DropDownUtils.getAdminSmsSettingsPassengerOptions(passengerSmsListApp);
		String driverSmsAppOptions = DropDownUtils.getAdminSmsSettingsDriverOptions(driverSmsListApp);
		String adminSmsAppOptions = DropDownUtils.getAdminSmsSettingsAdminOptions(adminSmsListApp);

		data.put(FieldConstants.PASSENGER_SMS_APP_OPTIONS, passengerSmsAppOptions);
		data.put(FieldConstants.DRIVER_SMS_APP_OPTIONS, driverSmsAppOptions);
		data.put(FieldConstants.ADMIN_SMS_APP_OPTIONS, adminSmsAppOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_SMS_SETTINGS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ADMIN_SMS_SETTINGS_JSP);
	}

	private List<String> getPassengerSmsListByPlatformType(String type, AdminSmsSendingModel adminSmsSendingModel) {

		if (type.equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {

			List<String> passengerSmsList = new ArrayList<String>();

			if (adminSmsSendingModel.ispAcceptByDriver()) {
				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_1.toString());
			}

			if (adminSmsSendingModel.ispArrivedAndWaiting()) {
				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_2.toString());
			}

			if (adminSmsSendingModel.ispCancelledByDriver()) {
				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_3.toString());
			}

//			if (adminSmsSendingModel.ispCancelledByBusinessO()) {
//				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_4.toString());
//			}

			if (adminSmsSendingModel.ispInvoice()) {
				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_5.toString());
			}

			if (adminSmsSendingModel.ispBookByOwner()) {
				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_6.toString());
			}

			if (adminSmsSendingModel.ispRefund()) {
				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_7.toString());
			}

//			if (adminSmsSendingModel.ispCreditUpdateAdmin()) {
//				passengerSmsList.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_8.toString());
//			}

			return passengerSmsList;

		} else if (type.equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID)) {

			List<String> driverSmsList = new ArrayList<String>();

			if (adminSmsSendingModel.isdBookingRequest()) {
				driverSmsList.add(ADMIN_SMS_SETTINGS_ENUM.DRIVER_1.toString());
			}

			if (adminSmsSendingModel.isdCancelledByPassengerBusinessO()) {
				driverSmsList.add(ADMIN_SMS_SETTINGS_ENUM.DRIVER_2.toString());
			}

			if (adminSmsSendingModel.isdPaymentReceived()) {
				driverSmsList.add(ADMIN_SMS_SETTINGS_ENUM.DRIVER_3.toString());
			}

			return driverSmsList;

		} else if (type.equalsIgnoreCase(UserRoles.ADMIN_ROLE_ID)) {

			List<String> adminSmsList = new ArrayList<String>();

			if (adminSmsSendingModel.isBoAccepted()) {
				adminSmsList.add(ADMIN_SMS_SETTINGS_ENUM.BO_1.toString());
			}

			if (adminSmsSendingModel.isBoArrivedAndWaiting()) {
				adminSmsList.add(ADMIN_SMS_SETTINGS_ENUM.BO_2.toString());
			}

			if (adminSmsSendingModel.isBoCancelledByDriver()) {
				adminSmsList.add(ADMIN_SMS_SETTINGS_ENUM.BO_3.toString());
			}

			if (adminSmsSendingModel.isBoInvoice()) {
				adminSmsList.add(ADMIN_SMS_SETTINGS_ENUM.BO_4.toString());
			}

			return adminSmsList;
		}

		return null;
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAdminSmsSettingsPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.PASSENGER_SMS_APP) List<String> passengerSmsApp,
		@FormParam(FieldConstants.DRIVER_SMS_APP) List<String> driverSmsApp,
		@FormParam(FieldConstants.ADMIN_SMS_APP) List<String> adminSmsApp
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SETTINGS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		updateSmsSendingParameters(passengerSmsApp, driverSmsApp, adminSmsApp);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ADMIN_SMS_SETTINGS_URL);
	}

	private void updateSmsSendingParameters(List<String> passengerSms, List<String> driverSms, List<String> adminSms) {

		ADMIN_SMS_SETTINGS_ENUM smsSettingType;

		AdminSmsSendingModel.updatePreviousAdminSmsSendingEntryToFalse();

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		adminSmsSendingModel.setLanguage(ProjectConstants.ENGLISH_ID);

		for (String string : passengerSms) {

			smsSettingType = ADMIN_SMS_SETTINGS_ENUM.valueOf(string);

			switch (smsSettingType) {

			case PASSENGER_1:

				adminSmsSendingModel.setpAcceptByDriver(true);

				break;

			case PASSENGER_2:

				adminSmsSendingModel.setpArrivedAndWaiting(true);

				break;

			case PASSENGER_3:

				adminSmsSendingModel.setpCancelledByDriver(true);

				break;

			case PASSENGER_4:

				adminSmsSendingModel.setpCancelledByBusinessO(true);

				break;

			case PASSENGER_5:

				adminSmsSendingModel.setpInvoice(true);

				break;

			case PASSENGER_6:

				adminSmsSendingModel.setpBookByOwner(true);

				break;

			case PASSENGER_7:

				adminSmsSendingModel.setpRefund(true);

				break;

			case PASSENGER_8:

				adminSmsSendingModel.setpCreditUpdateAdmin(true);

				break;

			default:
				break;
			}
		}

		for (String string : driverSms) {

			smsSettingType = ADMIN_SMS_SETTINGS_ENUM.valueOf(string);

			switch (smsSettingType) {

			case DRIVER_1:

				adminSmsSendingModel.setdBookingRequest(true);

				break;

			case DRIVER_2:

				adminSmsSendingModel.setdCancelledByPassengerBusinessO(true);

				break;

			case DRIVER_3:

				adminSmsSendingModel.setdPaymentReceived(true);

				break;

			default:
				break;
			}
		}

		for (String string : adminSms) {

			smsSettingType = ADMIN_SMS_SETTINGS_ENUM.valueOf(string);

			switch (smsSettingType) {

			case BO_1:

				adminSmsSendingModel.setBoAccepted(true);

				break;

			case BO_2:

				adminSmsSendingModel.setBoArrivedAndWaiting(true);

				break;

			case BO_3:

				adminSmsSendingModel.setBoCancelledByDriver(true);

				break;

			case BO_4:

				adminSmsSendingModel.setBoInvoice(true);

				break;

			default:
				break;
			}
		}

		adminSmsSendingModel.updateAdminSmsSending();
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ADMIN_SMS_SETTINGS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}