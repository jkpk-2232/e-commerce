package com.webapp.actions.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.validator.AlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.AlphaNumericValidationRule;
import com.jeeutils.validator.DuplicateCarPlateNumberValidationRule;
import com.jeeutils.validator.DuplicateEmailValidationRule;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MulticityRegionUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.actions.secure.driver.AddDriverAction;
import com.webapp.actions.secure.driver.EditDriverAction;
import com.webapp.models.CarModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverBankDetailsModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverTourStatusModel;
import com.webapp.models.DrivingLicenseInfoModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

@Path("/api/driver-signup")
public class DriverSignUpAction extends BusinessApiAction {

	Logger logger = Logger.getLogger(DriverSignUpAction.class);

	private static final String FIRSTNAME = "firstName";
	private static final String FIRSTNAME_LABEL = "First Name";

	private static final String LASTNAME = "lastName";
	private static final String LASTNAME_LABEL = "Last Name";

	private static final String GENDER = "gender";
	private static final String GENDER_LABEL = "Gender";

	private static final String EMAIL = "email";
	private static final String EMAIL_LABEL = "Email";

	private static final String OWNER = "owner";
	private static final String OWNER_LABEL = "Owner";

	private static final String PHONE_NO = "phoneNo";
	private static final String PHONE_NO_LABEL = "Phone Number";

	private static final String PASSWORD = "password";
	private static final String PASSWORD_LABEL = "Password";

	private static final String COLOR = "carColor";
	private static final String COLOR_LABEL = "CAR Color";

	private static final String PLATE_NO = "carPlateNo";
	private static final String PLATE_NO_LABEL = "Plate Number";

	private static final String YEAR = "carYear";
	private static final String YEAR_LABEL = "Car Year";

	private static final String NO_OF_PASSENGER = "noOfPassenger";
	private static final String NO_OF_PASSENGER_LABEL = "Number Of Customers";

	private static final String CAR_TYPE_ID = "carTypeId";
	private static final String CAR_TYPE_ID_LABEL = "Car Type Id";

	private static final String DRIVING_LICENSE_NUMBER = "driverLicenseCardNumber";
	private static final String DRIVING_LICENSE_NUMBER_LABEL = "Driver License Card Number";

	private static final String DOB = "dob";
	private static final String DOB_LABEL = "Date Of Birth";

	private static final String INSURANCE_PHOTO_URL = "insurancePhotoUrl";
	private static final String INSURANCE_PHOTO_URL_LABEL = "Insurance Photo Url";

	private static final String DRIVING_LICENSE_PHOTO_URL = "drivingLicensePhotoUrl";
	private static final String DRIVING_LICENSE_PHOTO_URL_LABEL = "Driving License Photo Url";

	private static final String SOCIAL_SECURITY_NUMBER = "socialSecurityNumber";
	private static final String SOCIAL_SECURITY_NUMBER_LABEL = "Voter Or Aadhar Id No";

	private static final String INSURANCE_EFFECTIVE_DATE = "insuranceEffectiveDate";
	private static final String INSURANCE_EFFECTIVE_DATE_LABEL = "Insurance Effective Date";

	private static final String INSURANCE_EXPIRATION_DATE = "insuranceExpirationDate";
	private static final String INSURANCE_EXPIRATION_DATE_LABEL = "Insurance Expiration Date";

	private static final String BANK_NAME = "bankName";
	private static final String BANK_NAME_LABEL = "Bank Name";

	private static final String ACCOUNT_NUMBER = "accountNumber";
	private static final String ACCOUNT_NUMBER_LABEL = "Account Number";

	private static final String ACCOUNT_NAME = "accountName";
	private static final String ACCOUNT_NAME_LABEL = "Account Name";

	private static final String ROUTING_NUMBER = "routingNumber";
	private static final String ROUTING_NUMBER_LABEL = "IFS Code";

	private static final String TYPE = "type";
	private static final String TYPE_LABEL = "Account Type";

	private final static String STREET_1 = "mailAddressLineOne";
	private final static String STREET_1_LABEL = "Mail Address Line one";

	private final static String COUNTRY = "mailCountryId";
	private final static String COUNTRY_LABEL = "Mail country Id";

	private final static String STATE = "mailStateId";
	private final static String STATE_LABEL = "Mail State Id";

	private final static String CITY = "mailCityId";
	private final static String CITY_LABEL = "Mail City Id";

	// 1. api to get region list according to vendor id
	// 2. api to get car type list according to vendor id and region id
	// 3. sign up driver

	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverSignup(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		DriverInfoModel driverInfoModel) throws IOException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		boolean createCar = false;
		if (driverInfoModel.getInputServiceType().equalsIgnoreCase(ProjectConstants.SERVICE_TYPE_CAR_ID)) {
			createCar = true;
		}

		errorMessages = driverModelValidation(driverInfoModel, headerVendorId, createCar);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		String password = driverInfoModel.getPassword();

		driverInfoModel.setApplicationStatus(ProjectConstants.DRIVER_APPLICATION_PENDING);

		driverInfoModel.getDrivingLicenseModel().setDob(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getDobString(), DateUtils.DATE_FORMAT_FOR_VIEW));
		driverInfoModel.getDrivingLicenseModel().setInsuranceEffectiveDate(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getInsuranceEffectiveDateString(), DateUtils.DATE_FORMAT_FOR_VIEW));
		driverInfoModel.getDrivingLicenseModel().setInsuranceExpirationDate(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getInsuranceExpirationDateString(), DateUtils.DATE_FORMAT_FOR_VIEW));
		driverInfoModel.getDrivingLicenseModel().setLicenceExpirationDate(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getLicenceExpirationDateString(), DateUtils.DATE_FORMAT_FOR_VIEW));

		logger.info("\n\n\tdriverInfoModel.getInputServiceType()\t" + driverInfoModel.getInputServiceType());
		logger.info("\n\n\tcreateCar\t" + createCar);

		String driverId = driverInfoModel.addDriverWeb(null, headerVendorId, createCar, true, false);

		if (driverId != null) {

			DriverInfoModel driverInfoAfterInsert = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);

			long currentTime = DateUtils.nowAsGmtMillisec();

			List<String> regionList = new ArrayList<String>();
			regionList.add(driverInfoModel.getInputRegionId());
			MulticityRegionUtils.addUserRegions(regionList, driverId, driverId, UserRoles.DRIVER_ROLE_ID);

			CarModel carModel = driverInfoAfterInsert.getCarModel();
			String carId = null;
			List<String> carTypeList = new ArrayList<String>();

			if (carModel != null) {
				carTypeList.add(carModel.getCarTypeId());
				carId = carModel.getCarId();
				logger.info("\n\n\tCarModel is present");
			} else {
				logger.info("\n\n\tCarModel is null");
			}

			logger.info("\n\n\tcarId\t" + carId);
			logger.info("\n\n\tcarTypeList\t" + carTypeList);

			EditDriverAction.handleDriverCarType(driverId, driverInfoModel.getInputServiceType(), carTypeList, driverId, carId, currentTime);

			DriverTourStatusModel.createDriverTourStatusMethod(driverId);
			AddDriverAction.sendVerificationCodeAndWelcomeEmailToDriver(driverInfoModel, password, driverId);
		}

		return sendSuccessMessage("Driver application submitted successfully and pending approval.");
	}

	@Path("/v2")
	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverSignupV2LimitedFields(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		DriverInfoModel driverInfoModel) throws IOException {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		boolean createCar = false;
		if (driverInfoModel.getInputServiceType().equalsIgnoreCase(ProjectConstants.SERVICE_TYPE_CAR_ID)) {
			createCar = true;
		}

		errorMessages = driverModelValidationV2(driverInfoModel, headerVendorId, createCar);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		overrideDriverFieldsToSetDefaultValues(driverInfoModel);

		String password = driverInfoModel.getPassword();

		// move the driver to auto accept state
		driverInfoModel.setApplicationStatus(ProjectConstants.DRIVER_APPLICATION_ACCEPTED);

		driverInfoModel.getDrivingLicenseModel().setDob(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getDobString(), DateUtils.DATE_FORMAT_FOR_VIEW));
		driverInfoModel.getDrivingLicenseModel().setInsuranceEffectiveDate(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getInsuranceEffectiveDateString(), DateUtils.DATE_FORMAT_FOR_VIEW));
		driverInfoModel.getDrivingLicenseModel().setInsuranceExpirationDate(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getInsuranceExpirationDateString(), DateUtils.DATE_FORMAT_FOR_VIEW));
		driverInfoModel.getDrivingLicenseModel().setLicenceExpirationDate(DateUtils.getDateFromString(driverInfoModel.getDrivingLicenseModel().getLicenceExpirationDateString(), DateUtils.DATE_FORMAT_FOR_VIEW));

		logger.info("\n\n\tdriverInfoModel.getInputServiceType()\t" + driverInfoModel.getInputServiceType());
		logger.info("\n\n\tcreateCar\t" + createCar);

		String driverId = driverInfoModel.addDriverWeb(null, headerVendorId, createCar, true, true);

		if (driverId != null) {

			DriverInfoModel driverInfoAfterInsert = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);

			long currentTime = DateUtils.nowAsGmtMillisec();

			List<String> regionList = new ArrayList<String>();
			regionList.add(driverInfoModel.getInputRegionId());
			MulticityRegionUtils.addUserRegions(regionList, driverId, driverId, UserRoles.DRIVER_ROLE_ID);

			CarModel carModel = driverInfoAfterInsert.getCarModel();
			String carId = null;
			List<String> carTypeList = new ArrayList<String>();

			if (carModel != null) {
				carTypeList.add(carModel.getCarTypeId());
				carId = carModel.getCarId();
				logger.info("\n\n\tCarModel is present");
			} else {
				logger.info("\n\n\tCarModel is null");
			}

			logger.info("\n\n\tcarId\t" + carId);
			logger.info("\n\n\tcarTypeList\t" + carTypeList);

			EditDriverAction.handleDriverCarType(driverId, driverInfoModel.getInputServiceType(), carTypeList, driverId, carId, currentTime);

			DriverTourStatusModel.createDriverTourStatusMethod(driverId);
			AddDriverAction.sendVerificationCodeAndWelcomeEmailToDriver(driverInfoModel, password, driverId);

			return new LoginAction().loginDriver(request, new UserModel(), driverId);
		}

		return sendBussinessError(messageForKey("errorFailedDriverRegistration", request));
	}

	private void overrideDriverFieldsToSetDefaultValues(DriverInfoModel driverInfoModel) {

		driverInfoModel.setAgentNumber(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_AGENT_NUMBER);
		driverInfoModel.setDriveTransmissionTypeId(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_TRANSMISSION_TYPE_BOTH_ID);

		DriverBankDetailsModel driverBankDetails = new DriverBankDetailsModel();
		driverBankDetails.setAccountName(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_ACCOUNT_NAME);
		driverBankDetails.setAccountNumber(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_ACCOUNT_NUMBER);
		driverBankDetails.setBankName(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_BANK_NAME);
		driverBankDetails.setRoutingNumber(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_ROUTING_NUMBER);
		driverBankDetails.setType(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_TYPE);
		driverInfoModel.setDriverBankDetails(driverBankDetails);

		DrivingLicenseInfoModel drivingLicenseInfoModel = new DrivingLicenseInfoModel();
		drivingLicenseInfoModel.setfName(driverInfoModel.getFirstName());
		drivingLicenseInfoModel.setmName(null);
		drivingLicenseInfoModel.setlName(driverInfoModel.getLastName());
		drivingLicenseInfoModel.setDriverLicenseCardNumber(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_NUMBER);
		drivingLicenseInfoModel.setSocialSecurityNumber(null);
		drivingLicenseInfoModel.setDrivingLicensePhotoUrl(null);
		drivingLicenseInfoModel.setInsurancePhotoUrl(null);
		drivingLicenseInfoModel.setDobString(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_DOB);
		drivingLicenseInfoModel.setLicenceExpirationDateString(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_LICENSE_EXPIRATION);
		drivingLicenseInfoModel.setInsuranceEffectiveDateString(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_INSURANCE_EFFECTIVE_DATE);
		drivingLicenseInfoModel.setInsuranceExpirationDateString(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_INSURANCE_EXPIRATION_DATE);
		drivingLicenseInfoModel.setDrivingLicenseBackPhotoUrl(null);
		drivingLicenseInfoModel.setBirthAccreditationPassportPhotoUrl(null);
		drivingLicenseInfoModel.setCriminalHistoryPhotoUrl(null);
		drivingLicenseInfoModel.setSocialSecurityNumber(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_NUMBER);
		drivingLicenseInfoModel.setSocilaSecurityPhotoUrl(null);
		driverInfoModel.setDrivingLicenseModel(drivingLicenseInfoModel);

		driverInfoModel.setGender(ProjectConstants.MALE);
		driverInfoModel.setMailAddressLineOne(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
		driverInfoModel.setMailAddressLineTwo(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
		driverInfoModel.setMailCityId(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
		driverInfoModel.setMailCountryId(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
		driverInfoModel.setMailStateId(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
		driverInfoModel.setMailAddressLineOne(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);

		if (driverInfoModel.getInputServiceType().equalsIgnoreCase(ProjectConstants.SERVICE_TYPE_CAR_ID)) {

			CarModel carModel = driverInfoModel.getCarModel();

			if (carModel == null) {
				carModel = new CarModel();
				carModel.setCarTypeId(ProjectConstants.CAR_TYPES.AUTO_ID);
				carModel.setOwner(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_NUMBER);
			}

			carModel.setBackImgUrl(null);
			carModel.setCarColor(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
			carModel.setCarColor(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
			carModel.setCarPlateNo(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_CAR_PLATE_NO);
			carModel.setCarYear(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_CAR_YEAR);
			carModel.setFrontImgUrl(null);
			carModel.setInspectionReportPhotoUrl(null);
			carModel.setInsurancePhotoUrl(null);
			carModel.setMake(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
			carModel.setModelName(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_STRING);
			carModel.setNoOfPassenger(ProjectConstants.DRIVER_APP_REGISTRATION_DEFAULT_VALUES.DEFAULT_NO_OF_PASSENGERS);
			carModel.setRegistrationPhotoUrl(null);
			carModel.setVehicleCommercialLicencePhotoUrl(null);
			driverInfoModel.setCarModel(carModel);
		}

		driverInfoModel.setCompanyDriver(Long.parseLong(ProjectConstants.INDIVIDUAL_DRIVER_ID));
		driverInfoModel.setSameAsMailing(true);
	}

	@Path("/region-list/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRegionList(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@PathParam("start") int start,
		@PathParam("length") int length) {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(headerVendorId);
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(userProfileModel);
		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionListByMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList, start, length);

		return sendDataResponse(multicityCityRegionModelList);
	}

	@Path("/car-type-list/{multicityCityRegionId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarTypeList(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@PathParam("multicityCityRegionId") String multicityCityRegionId) {
	//@formatter:on

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		List<CarTypeModel> carTypeList = MultiTenantUtils.getCarTypeListOfVendor(headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		return sendDataResponse(carTypeList);
	}

	private List<String> drivingLicenseInfoModelValidation(DrivingLicenseInfoModel drivingLicenseInfoModel) throws IOException {

		Validator validator = new Validator();
		List<String> errorMessages = new ArrayList<String>();
		validator.addValidationMapping(DRIVING_LICENSE_NUMBER, DRIVING_LICENSE_NUMBER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DRIVING_LICENSE_NUMBER, DRIVING_LICENSE_NUMBER_LABEL, new MaxLengthValidationRule(30));
		validator.addValidationMapping(DRIVING_LICENSE_NUMBER, DRIVING_LICENSE_NUMBER_LABEL, new AlphaNumericDashCharactersValidationRule());

		validator.addValidationMapping(DOB, DOB_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(SOCIAL_SECURITY_NUMBER, SOCIAL_SECURITY_NUMBER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(INSURANCE_EFFECTIVE_DATE, INSURANCE_EFFECTIVE_DATE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(INSURANCE_EXPIRATION_DATE, INSURANCE_EXPIRATION_DATE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DRIVING_LICENSE_PHOTO_URL, DRIVING_LICENSE_PHOTO_URL_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(drivingLicenseInfoModel);

		return errorMessages;
	}

	private List<String> carModelValidation(CarModel carModel) throws IOException {

		Validator validator = new Validator();

		List<String> errorMessages = new ArrayList<String>();

		validator.addValidationMapping(CAR_TYPE_ID, CAR_TYPE_ID_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(COLOR, COLOR_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PLATE_NO, PLATE_NO_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PLATE_NO, PLATE_NO_LABEL, new DuplicateCarPlateNumberValidationRule(null));
		validator.addValidationMapping(YEAR, YEAR_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(NO_OF_PASSENGER, NO_OF_PASSENGER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(NO_OF_PASSENGER, NO_OF_PASSENGER_LABEL, new NumericValidationRule());
		validator.addValidationMapping(OWNER, OWNER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(INSURANCE_PHOTO_URL, INSURANCE_PHOTO_URL_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(carModel);

		return errorMessages;
	}

	private List<String> driverBankDetailsValidation(DriverBankDetailsModel driverBankDetailsModel) throws IOException {

		Validator validator = new Validator();

		List<String> errorMessages = new ArrayList<String>();

		validator.addValidationMapping(BANK_NAME, BANK_NAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(BANK_NAME, BANK_NAME_LABEL, new MaxLengthValidationRule(40));
		validator.addValidationMapping(ACCOUNT_NUMBER, ACCOUNT_NUMBER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(ACCOUNT_NUMBER, ACCOUNT_NUMBER_LABEL, new NumericValidationRule());
		validator.addValidationMapping(ACCOUNT_NAME, ACCOUNT_NAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(ACCOUNT_NAME, ACCOUNT_NAME_LABEL, new MaxLengthValidationRule(50));
		validator.addValidationMapping(ROUTING_NUMBER, ROUTING_NUMBER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(ROUTING_NUMBER, ROUTING_NUMBER_LABEL, new AlphaNumericValidationRule());
		validator.addValidationMapping(ROUTING_NUMBER, ROUTING_NUMBER_LABEL, new MinMaxLengthValidationRule(11, 11));
		validator.addValidationMapping(TYPE, TYPE_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(driverBankDetailsModel);

		return errorMessages;
	}

	private List<String> driverModelValidation(DriverInfoModel driverInfoModel, String headerVendorId, boolean createCar) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new DuplicateEmailValidationRule(null));
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new EmailValidationRule());
		validator.addValidationMapping(PASSWORD, PASSWORD_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(GENDER, GENDER_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new NumericValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new DuplicatePhoneNumberValidationRule(driverInfoModel.getPhoneNoCode(), UserRoles.DRIVER_ROLE_ID, null, null));

		validator.addValidationMapping(STREET_1, STREET_1_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(COUNTRY, COUNTRY_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(STATE, STATE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CITY, CITY_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(driverInfoModel);

		if (createCar && driverInfoModel.getCarModel() != null) {
			errorMessages.addAll(carModelValidation(driverInfoModel.getCarModel()));
		}

		if (driverInfoModel.getDrivingLicenseModel() != null) {
			errorMessages.addAll(drivingLicenseInfoModelValidation(driverInfoModel.getDrivingLicenseModel()));
		}

		if (driverInfoModel.getDriverBankDetails() != null) {
			errorMessages.addAll(driverBankDetailsValidation(driverInfoModel.getDriverBankDetails()));
		}

		return errorMessages;
	}

	private List<String> driverModelValidationV2(DriverInfoModel driverInfoModel, String headerVendorId, boolean createCar) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(FIRSTNAME, FIRSTNAME_LABEL, new TaxiSolsAlphaNumericDashCharactersValidationRule());

		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new MinMaxLengthValidationRule(2, 40));
		validator.addValidationMapping(LASTNAME, LASTNAME_LABEL, new TaxiSolsAlphaNumericDashCharactersValidationRule());

		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new DuplicateEmailValidationRule(null));
		validator.addValidationMapping(EMAIL, EMAIL_LABEL, new EmailValidationRule());

		validator.addValidationMapping(PASSWORD, PASSWORD_LABEL, new RequiredValidationRule());

		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new NumericValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(PHONE_NO, PHONE_NO_LABEL, new DuplicatePhoneNumberValidationRule(driverInfoModel.getPhoneNoCode(), UserRoles.DRIVER_ROLE_ID, null, null));

		errorMessages = validator.validate(driverInfoModel);

		return errorMessages;
	}
}