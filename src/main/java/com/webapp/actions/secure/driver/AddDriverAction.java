package com.webapp.actions.secure.driver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.SendEmailThread;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.AlphaCharacterWithOutSpaceValidationRule;
import com.jeeutils.validator.AlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.AlphaNumericValidationRule;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.DuplicateEmailValidationRule;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MulticityRegionUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.Action;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.CarDriversModel;
import com.webapp.models.CarModel;
import com.webapp.models.DriverBankDetailsModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverTourStatusModel;
import com.webapp.models.DrivingLicenseInfoModel;
import com.webapp.models.UserAccountModel;

@Path("/add-driver")
public class AddDriverAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddDriver(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String genderOptions = DropDownUtils.getGenderOption("");
		data.put(FieldConstants.GENDER_OPTIONS, genderOptions);

		String serviceTypeOptions = DropDownUtils.getServiceTypeOptions(ProjectConstants.SERVICE_TYPE_CAR_ID);
		data.put(FieldConstants.SERVICE_TYPE_OPTIONS, serviceTypeOptions);

		if (!UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			String vendorIdOptions = DropDownUtils.getVendorListOptions("1", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(null, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String carTypeListOptions = DropDownUtils.getCarTypeListOptionsForMultiselect(Arrays.asList(ProjectConstants.Second_Vehicle_ID), false);
		data.put(FieldConstants.CAR_TYPE_LIST_OPTIONS, carTypeListOptions);

		String carOptions = DropDownUtils.getCarOption(ProjectConstants.DEFAULT_CAR_ID, Arrays.asList(ProjectConstants.Second_Vehicle_ID), UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) ? loginSessionMap.get(LoginUtils.USER_ID) : null);
		data.put(FieldConstants.CAR_OPTIONS, carOptions);

		String transmissionTypeListOptions = DropDownUtils.getTransmissionTypeListOptionsForMultiselect(new ArrayList<String>());
		data.put(FieldConstants.TRANSMISSION_TYPE_LIST_OPTIONS, transmissionTypeListOptions);

		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVING_LICENSE_PHOTO_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.SOCIAL_SECURITY_IMG_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_DRIVER_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddDriver(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.FIRST_NAME) String firstName,
		@FormParam(FieldConstants.LAST_NAME) String lastName,
		@FormParam(FieldConstants.EMAIL_ADDRESS) String emailAddress,
		@FormParam(FieldConstants.PHONE) String phone,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.GENDER) String gender,
		@FormParam(FieldConstants.DOB) String dob,
		@FormParam(FieldConstants.SOCIAL_SECURITY_NUMBER) String socialSecurityNumber,
		@FormParam(FieldConstants.AGENT_NUMBER) String agentNumber,
		@FormParam(FieldConstants.REGION_LIST) List<String> regionList,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		
		@FormParam(FieldConstants.SERVICE_TYPE) String serviceType,
		@FormParam(FieldConstants.CAR_TYPE_LIST) List<String> carTypeList,
		@FormParam(FieldConstants.CAR) String car,
		@FormParam(FieldConstants.TRANSMISSION_TYPE_LIST) List<String> transmissionTypeList,
		@FormParam(FieldConstants.DRIVING_LICENSE) String drivingLicense,
		@FormParam(FieldConstants.INSURANCE_EFFECTIVE_DATE) String insuranceEffectiveDate,
		@FormParam(FieldConstants.INSURANCE_EXPIRATION_DATE) String insuranceExpirationDate,
		@FormParam(FieldConstants.LICENSE_EXPIRATION_DATE) String licenseExpiration,
		@FormParam(FieldConstants.DRIVER_PAYABLE_PERCENTAGE) String driverPayablePercentage,
		
		@FormParam(FieldConstants.STREET_1) String street1,
		@FormParam(FieldConstants.STREET_2) String street2,
		@FormParam(FieldConstants.COUNTRY) String country,
		@FormParam(FieldConstants.STATE) String state,
		@FormParam(FieldConstants.CITY) String city,
		
		@FormParam(FieldConstants.BANK_NAME) String bankName,
		@FormParam(FieldConstants.ACCOUNT_NUMBER) String accountNumber,
		@FormParam(FieldConstants.ACCOUNT_NAME) String accountName,
		@FormParam(FieldConstants.ROUTING_NUMBER) String routingNumber,
		@FormParam(FieldConstants.TYPE) String type,

		@FormParam(FieldConstants.PROFILE_IMAGE_URL_HIDDEN) String hiddenPhotoUrl,
		@FormParam(FieldConstants.DRIVING_LICENSE_PHOTO_HIDDEN) String hiddenDriverLicenseImage,
		@FormParam(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL_HIDDEN) String hiddenDriverLicenseBackImgUrl,
		@FormParam(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL_HIDDEN) String hiddenDriverAccreditationImgUrl,
		@FormParam(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL_HIDDEN) String hiddenDriverCriminalReportImgUrl,
		@FormParam(FieldConstants.SOCIAL_SECURITY_IMG_URL_HIDDEN) String hiddenSocialSecurityPhotoUrl
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(countryCode);
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String genderOptions = DropDownUtils.getGenderOption(gender);
		data.put(FieldConstants.GENDER_OPTIONS, genderOptions);

		String serviceTypeOptions = DropDownUtils.getServiceTypeOptions(serviceType);
		data.put(FieldConstants.SERVICE_TYPE_OPTIONS, serviceTypeOptions);

		if (!UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			String vendorIdOptions = DropDownUtils.getVendorListOptions(vendorId, UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);
		}

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String carTypeListOptions = DropDownUtils.getCarTypeListOptionsForMultiselect(carTypeList, false);
		data.put(FieldConstants.CAR_TYPE_LIST_OPTIONS, carTypeListOptions);

		String carOptions = DropDownUtils.getCarOption(car, !carTypeList.isEmpty() ? carTypeList : null, UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) ? loginSessionMap.get(LoginUtils.USER_ID) : null);
		data.put(FieldConstants.CAR_OPTIONS, carOptions);

		String transmissionTypeListOptions = DropDownUtils.getTransmissionTypeListOptionsForMultiselect(transmissionTypeList);
		data.put(FieldConstants.TRANSMISSION_TYPE_LIST_OPTIONS, transmissionTypeListOptions);

		data.put(FieldConstants.REGION_LIST, "");
		data.put(FieldConstants.CAR_TYPE_LIST, "");
		data.put(FieldConstants.TRANSMISSION_TYPE_LIST, "");

		data.put(FieldConstants.FIRST_NAME, firstName);
		data.put(FieldConstants.LAST_NAME, lastName);
		data.put(FieldConstants.EMAIL_ADDRESS, emailAddress);
		data.put(FieldConstants.PHONE, phone);
		data.put(FieldConstants.GENDER, gender);
		data.put(FieldConstants.DRIVING_LICENSE, drivingLicense);
		data.put(FieldConstants.DOB, dob);
		data.put(FieldConstants.INSURANCE_EFFECTIVE_DATE, insuranceEffectiveDate);
		data.put(FieldConstants.INSURANCE_EXPIRATION_DATE, insuranceExpirationDate);

		data.put(FieldConstants.STREET_1, street1);
		data.put(FieldConstants.STREET_2, street2);
		data.put(FieldConstants.COUNTRY, country);
		data.put(FieldConstants.STATE, state);
		data.put(FieldConstants.CITY, city);
		data.put(FieldConstants.BANK_NAME, bankName);
		data.put(FieldConstants.ACCOUNT_NUMBER, accountNumber);
		data.put(FieldConstants.ACCOUNT_NAME, accountName);
		data.put(FieldConstants.ROUTING_NUMBER, routingNumber);
		data.put(FieldConstants.TYPE, type);

		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, hiddenPhotoUrl);
		data.put(FieldConstants.DRIVING_LICENSE_PHOTO_HIDDEN, hiddenDriverLicenseImage);
		data.put(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL_HIDDEN, hiddenDriverLicenseBackImgUrl);
		data.put(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL_HIDDEN, hiddenDriverAccreditationImgUrl);
		data.put(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL_HIDDEN, hiddenDriverCriminalReportImgUrl);
		data.put(FieldConstants.SOCIAL_SECURITY_IMG_URL_HIDDEN, hiddenSocialSecurityPhotoUrl);

		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenPhotoUrl) ? hiddenPhotoUrl : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVING_LICENSE_PHOTO_HIDDEN_DUMMY, StringUtils.validString(hiddenDriverLicenseImage) ? hiddenDriverLicenseImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVER_LICENSE_BACK_PHOTO_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenDriverLicenseBackImgUrl) ? hiddenDriverLicenseBackImgUrl : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVER_ACCREDIATION_PHOTO_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenDriverAccreditationImgUrl) ? hiddenDriverAccreditationImgUrl : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.DRIVER_CRIMINAL_REPORT_PHOTO_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenDriverCriminalReportImgUrl) ? hiddenDriverCriminalReportImgUrl : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.SOCIAL_SECURITY_IMG_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenSocialSecurityPhotoUrl) ? hiddenSocialSecurityPhotoUrl : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CAR, car.equals(ProjectConstants.DEFAULT_CAR_ID) ? "" : car);
		data.put(FieldConstants.LICENSE_EXPIRATION_DATE, licenseExpiration);
		data.put(FieldConstants.SOCIAL_SECURITY_NUMBER, socialSecurityNumber);
		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.DRIVER_PAYABLE_PERCENTAGE, driverPayablePercentage);
		data.put(FieldConstants.AGENT_NUMBER, agentNumber);

		if (hasErrorsForEnglish(countryCode, regionList, carTypeList, transmissionTypeList, serviceType)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_DRIVER_JSP);
		}

		DriverInfoModel driverInfoModel = new DriverInfoModel();
		CarModel carModel = new CarModel();
		DriverBankDetailsModel driverBankDetailsModel = new DriverBankDetailsModel();
		DrivingLicenseInfoModel drivingLicenseInfoModel = new DrivingLicenseInfoModel();

		String password = MyHubUtils.generatePassword();

		driverInfoModel.setPhotoUrl(hiddenPhotoUrl);
		driverInfoModel.setEmail(emailAddress);
		driverInfoModel.setFirstName(firstName);
		driverInfoModel.setLastName(lastName);
		driverInfoModel.setPassword(password);
		driverInfoModel.setPhoneNo(phone);
		driverInfoModel.setPhoneNoCode(countryCode);
		driverInfoModel.setGender(gender);
		driverInfoModel.setDrivingLicense(drivingLicense);
		driverInfoModel.setMailAddressLineOne(street1);
		driverInfoModel.setMailAddressLineTwo(street2);
		driverInfoModel.setMailCountryId(country);
		driverInfoModel.setMailStateId(state);
		driverInfoModel.setMailCityId(city);
		driverInfoModel.setMailZipCode(null);
		driverInfoModel.setBillAddressLineOne(street1);
		driverInfoModel.setBillAddressLineTwo(street2);
		driverInfoModel.setBillCountryId(country);
		driverInfoModel.setBillStateId(state);
		driverInfoModel.setBillCityId(city);
		driverInfoModel.setBillZipCode(null);
		driverInfoModel.setDriveTransmissionTypeId(EditDriverAction.getDriveTransmissionTypeFromList(transmissionTypeList));
		driverInfoModel.setDriverPayablePercentage(Double.parseDouble(driverPayablePercentage));
		driverInfoModel.setCompanyDriver(Long.parseLong(ProjectConstants.INDIVIDUAL_DRIVER_ID));
		driverInfoModel.setSameAsMailing(true);

		CarDriversModel carDriversModel = new CarDriversModel();

		if (ProjectConstants.SERVICE_TYPE_CAR_ID.equals(serviceType)) {
			carDriversModel.setCarId(car);
		} else {
			carDriversModel.setCarId(ProjectConstants.DEFAULT_CAR_ID);
		}

		driverBankDetailsModel.setBankName(bankName);
		driverBankDetailsModel.setAccountNumber(accountNumber);
		driverBankDetailsModel.setAccountName(accountName);
		driverBankDetailsModel.setRoutingNumber(routingNumber);
		driverBankDetailsModel.setType(type);

		drivingLicenseInfoModel.setfName(firstName);
		drivingLicenseInfoModel.setmName(null);
		drivingLicenseInfoModel.setlName(lastName);
		drivingLicenseInfoModel.setDriverLicenseCardNumber(drivingLicense);
		drivingLicenseInfoModel.setSocialSecurityNumber(null);
		drivingLicenseInfoModel.setDrivingLicensePhotoUrl(hiddenDriverLicenseImage);
		drivingLicenseInfoModel.setInsurancePhotoUrl(null);

		drivingLicenseInfoModel.setDob(DateUtils.getDateFromString(dob, DateUtils.DATE_FORMAT_FOR_VIEW));
		drivingLicenseInfoModel.setLicenceExpirationDate(DateUtils.getDateFromString(licenseExpiration, DateUtils.DATE_FORMAT_FOR_VIEW));

		drivingLicenseInfoModel.setInsuranceEffectiveDate(MyHubUtils.convertSecondsToMillis(insuranceEffectiveDate));
		drivingLicenseInfoModel.setInsuranceExpirationDate(MyHubUtils.convertSecondsToMillis(insuranceExpirationDate));

		drivingLicenseInfoModel.setDrivingLicenseBackPhotoUrl(hiddenDriverLicenseBackImgUrl);
		drivingLicenseInfoModel.setBirthAccreditationPassportPhotoUrl(hiddenDriverAccreditationImgUrl);
		drivingLicenseInfoModel.setCriminalHistoryPhotoUrl(hiddenDriverCriminalReportImgUrl);

		drivingLicenseInfoModel.setSocialSecurityNumber(socialSecurityNumber);
		drivingLicenseInfoModel.setSocilaSecurityPhotoUrl(hiddenSocialSecurityPhotoUrl);

		driverInfoModel.setApplicationStatus(ProjectConstants.DRIVER_APPLICATION_ACCEPTED);

		driverInfoModel.setCarModel(carModel);
		driverInfoModel.setDrivingLicenseModel(drivingLicenseInfoModel);
		driverInfoModel.setDriverBankDetails(driverBankDetailsModel);
		driverInfoModel.setCarDriversModel(carDriversModel);

		driverInfoModel.setAgentNumber(agentNumber);

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loggedInUserId;
		}

		String driverId = driverInfoModel.addDriverWeb(loggedInUserId, vendorId, false, false, false);

		long currentTime = DateUtils.nowAsGmtMillisec();

		MulticityRegionUtils.addUserRegions(regionList, loginSessionMap.get(LoginUtils.USER_ID), driverId, UserRoles.DRIVER_ROLE_ID);

		EditDriverAction.handleDriverCarType(driverId, serviceType, carTypeList, loginSessionMap.get(LoginUtils.USER_ID), car, currentTime);

		DriverTourStatusModel.createDriverTourStatusMethod(driverId);

		sendVerificationCodeAndWelcomeEmailToDriver(driverInfoModel, password, driverId);

		UserAccountModel.createUserAccountBalance(loggedInUserId, driverId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DRIVER_URL);
	}

	public static void sendVerificationCodeAndWelcomeEmailToDriver(DriverInfoModel driverInfoModel, String password, String userId) {

		driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(userId);

		String verificationCode = MyHubUtils.generateVerificationCode();
		DriverInfoModel.updateVerificationCode(userId, verificationCode);

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		String message = String.format(BusinessAction.messageForKeyAdmin("successMobileVerficationCode", adminSmsSendingModel.getLanguage()), driverInfoModel.getFullName(), verificationCode);
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, driverInfoModel.getPhoneNoCode() + driverInfoModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

		message = Action.driverApplicationAcceptMessage(MyHubUtils.formatFullName(driverInfoModel.getFirstName(), driverInfoModel.getLastName()), verificationCode, driverInfoModel.getEmail(), password);

		new SendEmailThread(driverInfoModel.getEmail(), messageForKeyAdmin("labelDriverLoginCredentials"), message);
	}

	@Path("/car-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadCarList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.CAR_ID) String carId,
		@QueryParam("carTypeIds") String carTypeIds
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		Map<String, String> outputMap = new HashMap<String, String>();

		List<String> carTypeList = new ArrayList<String>();

		if (StringUtils.validString(carTypeIds)) {

			String[] carTypesArr = MyHubUtils.splitStringByCommaArray(carTypeIds);

			for (String carTypeId : carTypesArr) {

				carTypeList.add(carTypeId);
			}
		}

		String carOptions = DropDownUtils.getCarOption(carId, carTypeList, UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) ? loginSessionMap.get(LoginUtils.USER_ID) : null);
		outputMap.put(FieldConstants.CAR_OPTIONS, carOptions);

		return sendDataResponse(outputMap);
	}

	private boolean hasErrorsForEnglish(String countryCode, List<String> regionList, List<String> carTypeList, List<String> transmissionTypeList, String serviceType) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new DuplicateEmailValidationRule(null));
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new DuplicatePhoneNumberValidationRule(countryCode, UserRoles.DRIVER_ROLE_ID, null, null));
		validator.addValidationMapping(FieldConstants.DOB, messageForKeyAdmin("labelDateOfBirth"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.SOCIAL_SECURITY_NUMBER, messageForKeyAdmin("labelVoterOrAadharIdNo"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.AGENT_NUMBER, messageForKeyAdmin("labelAgentNumber"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.REGION_LIST, messageForKeyAdmin("labelRegion"), new RequiredListValidationRule(regionList.size()));

		if (ProjectConstants.SERVICE_TYPE_CAR_ID.equals(serviceType)) {

			if (!UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

				validator.addValidationMapping(FieldConstants.CAR_TYPE_LIST, messageForKeyAdmin("labelCarType"), new RequiredListValidationRule(carTypeList.size()));
				validator.addValidationMapping(FieldConstants.CAR, messageForKeyAdmin("labelCar"), new RequiredValidationRule());
			}

		} else {
			validator.addValidationMapping(FieldConstants.TRANSMISSION_TYPE_LIST, messageForKeyAdmin("labelTransmissionType"), new RequiredListValidationRule(transmissionTypeList.size()));
		}

		validator.addValidationMapping(FieldConstants.DRIVING_LICENSE, messageForKeyAdmin("labelDrivingLicense"), new MaxLengthValidationRule(30));
		validator.addValidationMapping(FieldConstants.DRIVING_LICENSE, messageForKeyAdmin("labelDrivingLicense"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVING_LICENSE, messageForKeyAdmin("labelDrivingLicense"), new AlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.INSURANCE_EFFECTIVE_DATE, messageForKeyAdmin("labelInsuranceEffectiveDate"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.INSURANCE_EXPIRATION_DATE, messageForKeyAdmin("labelInsuranceExpiryDate"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.LICENSE_EXPIRATION_DATE, messageForKeyAdmin("labelDrivingLicenseExpiryDate"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_PAYABLE_PERCENTAGE, messageForKeyAdmin("labelRentalDriverPayablePercentage"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_PAYABLE_PERCENTAGE, messageForKeyAdmin("labelRentalDriverPayablePercentage"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.DRIVER_PAYABLE_PERCENTAGE, messageForKeyAdmin("labelRentalDriverPayablePercentage"), new MinMaxValueValidationRule(0, 100));

		validator.addValidationMapping(FieldConstants.BANK_NAME, messageForKeyAdmin("labelBankName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.BANK_NAME, messageForKeyAdmin("labelBankName"), new MaxLengthValidationRule(40));
		validator.addValidationMapping(FieldConstants.BANK_NAME, messageForKeyAdmin("labelBankName"), new AlphaCharacterWithOutSpaceValidationRule());
		validator.addValidationMapping(FieldConstants.ACCOUNT_NUMBER, messageForKeyAdmin("labelAccountNumber"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ACCOUNT_NUMBER, messageForKeyAdmin("labelAccountNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.ACCOUNT_NUMBER, messageForKeyAdmin("labelAccountNumber"), new MinMaxLengthValidationRule(4, 40));
		validator.addValidationMapping(FieldConstants.ACCOUNT_NAME, messageForKeyAdmin("labelAccountName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ACCOUNT_NAME, messageForKeyAdmin("labelAccountName"), new MaxLengthValidationRule(50));
		validator.addValidationMapping(FieldConstants.ROUTING_NUMBER, messageForKeyAdmin("labelRoutingNumber"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ROUTING_NUMBER, messageForKeyAdmin("labelRoutingNumber"), new AlphaNumericValidationRule());
		validator.addValidationMapping(FieldConstants.ROUTING_NUMBER, messageForKeyAdmin("labelRoutingNumber"), new MinMaxLengthValidationRule(11, 11));
		validator.addValidationMapping(FieldConstants.TYPE, messageForKeyAdmin("labelType"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_DRIVER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}