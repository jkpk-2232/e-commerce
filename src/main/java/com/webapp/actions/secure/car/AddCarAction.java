package com.webapp.actions.secure.car;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.AlphaCharactersValidationRule;
import com.jeeutils.validator.AlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.CarModel;

@Path("/add-car")
public class AddCarAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddCar(
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
		
		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))
				|| UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
		return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
     	}
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String carTypeOptions = DropDownUtils.getCarModelOption(ProjectConstants.First_Vehicle_ID, false, false);
		data.put(FieldConstants.CAR_TYPE_OPTIONS, carTypeOptions);

		String carYearOptions = DropDownUtils.getPastFifteenYearOption(String.valueOf(DateUtils.getCurrentYear()));
		data.put(FieldConstants.CAR_YEAR_OPTIONS, carYearOptions);

		String noOfPassengerOptions = DropDownUtils.getNumberOfPassangerOption(ProjectConstants.Second_Vehicle_ID);
		data.put(FieldConstants.NO_OF_PASSENGER_OPTIONS, noOfPassengerOptions);

		String vendorIdOptions = DropDownUtils.getVendorListOptions("1", UserRoles.VENDOR_ROLE_ID, assignedRegionList);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		data.put(FieldConstants.CAR_FRONT_IMG_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.CAR_BACK_IMG_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.CAR_INSURANCE_IMG_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.CAR_INSPECTION_IMG_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.CAR_REGISTRATION_IMG_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.PAGE_TYPE, FieldConstants.PAGE_TYPE_ADD);
		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CAR_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_CAR_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddCar(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.CAR_MODEL_TYPE_ID) String carType,
		@FormParam(FieldConstants.CAR_COLOR) String carColor,
		@FormParam(FieldConstants.MODEL_NAME) String modelName,
		@FormParam(FieldConstants.MAKE) String make,
		@FormParam(FieldConstants.CAR_PLATE_NUMBER) String carPlateNo,
		@FormParam(FieldConstants.CAR_YEAR) String carYear,
		@FormParam(FieldConstants.OWNER) String owner,
		@FormParam(FieldConstants.NUMBER_OF_PASSENGER) String noOfPassenger,
		@FormParam(FieldConstants.CAR_FRONT_IMG_HIDDEN) String hiddenFrontImgUrl,
		@FormParam(FieldConstants.CAR_BACK_IMG_HIDDEN) String hiddenBackImgUrl,
		@FormParam(FieldConstants.CAR_INSURANCE_IMG_HIDDEN) String hiddenInsuranceImgUrl,
		@FormParam(FieldConstants.CAR_INSPECTION_IMG_HIDDEN) String hiddenInspectionImgUrl,
		@FormParam(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG_HIDDEN) String hiddenVehicleCommercialLicenseImgUrl,
		@FormParam(FieldConstants.CAR_REGISTRATION_IMG_HIDDEN) String hiddenVehicleRegistrationImgUrl,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException, SQLException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID)) || UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))
				|| UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
		return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
     	}

		data.put(FieldConstants.OWNER, owner);
		data.put(FieldConstants.CAR_COLOR, carColor);
		data.put(FieldConstants.CAR_PLATE_NUMBER, carPlateNo);
		data.put(FieldConstants.MODEL_NAME, modelName);
		data.put(FieldConstants.MAKE, make);
		data.put(FieldConstants.CAR_FRONT_IMG_HIDDEN, hiddenFrontImgUrl);
		data.put(FieldConstants.CAR_BACK_IMG_HIDDEN, hiddenBackImgUrl);
		data.put(FieldConstants.CAR_INSURANCE_IMG_HIDDEN, hiddenInsuranceImgUrl);
		data.put(FieldConstants.CAR_INSPECTION_IMG_HIDDEN, hiddenInspectionImgUrl);
		data.put(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG_HIDDEN, hiddenVehicleCommercialLicenseImgUrl);
		data.put(FieldConstants.CAR_REGISTRATION_IMG_HIDDEN, hiddenVehicleRegistrationImgUrl);
		data.put(FieldConstants.CAR_MODEL_TYPE_ID, carType);
		data.put(FieldConstants.NUMBER_OF_PASSENGER, noOfPassenger);
		data.put(FieldConstants.CAR_YEAR, carYear);
		data.put(FieldConstants.VENDOR_ID, vendorId);

		if (hasErrors()) {

			data.put(FieldConstants.CAR_FRONT_IMG_HIDDEN_DUMMY, StringUtils.validString(hiddenFrontImgUrl) ? hiddenFrontImgUrl : ProjectConstants.DEFAULT_IMAGE);
			data.put(FieldConstants.CAR_BACK_IMG_HIDDEN_DUMMY, StringUtils.validString(hiddenBackImgUrl) ? hiddenBackImgUrl : ProjectConstants.DEFAULT_IMAGE);
			data.put(FieldConstants.CAR_INSURANCE_IMG_HIDDEN_DUMMY, StringUtils.validString(hiddenInsuranceImgUrl) ? hiddenInsuranceImgUrl : ProjectConstants.DEFAULT_IMAGE);
			data.put(FieldConstants.CAR_INSPECTION_IMG_HIDDEN_DUMMY, StringUtils.validString(hiddenInspectionImgUrl) ? hiddenInspectionImgUrl : ProjectConstants.DEFAULT_IMAGE);
			data.put(FieldConstants.CAR_COMMERCIAL_LICENSE_IMG_HIDDEN_DUMMY, StringUtils.validString(hiddenVehicleCommercialLicenseImgUrl) ? hiddenVehicleCommercialLicenseImgUrl : ProjectConstants.DEFAULT_IMAGE);
			data.put(FieldConstants.CAR_REGISTRATION_IMG_HIDDEN_DUMMY, StringUtils.validString(hiddenVehicleRegistrationImgUrl) ? hiddenVehicleRegistrationImgUrl : ProjectConstants.DEFAULT_IMAGE);

			String carTypeOptions = DropDownUtils.getCarModelOption(carType, false, false);
			data.put(FieldConstants.CAR_TYPE_OPTIONS, carTypeOptions);

			String carYearOptions = DropDownUtils.getPastFifteenYearOption(carYear);
			data.put(FieldConstants.CAR_YEAR_OPTIONS, carYearOptions);

			String noOfPassengerOptions = DropDownUtils.getNumberOfPassangerOption(noOfPassenger);
			data.put(FieldConstants.NO_OF_PASSENGER_OPTIONS, noOfPassengerOptions);

			data.put(FieldConstants.PAGE_TYPE, FieldConstants.PAGE_TYPE_ADD);
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_CAR_URL);

			List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

			String vendorIdOptions = DropDownUtils.getVendorListOptions(vendorId, UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

			return loadView(UrlConstants.JSP_URLS.ADD_CAR_JSP);
		}

		CarModel car = new CarModel();

		car.setCarColor(carColor);
		car.setCarTypeId(carType);
		car.setModelName(modelName);
		car.setMake(make);
		car.setOwner(owner);
		car.setCarYear(Long.parseLong(carYear));
		car.setCarPlateNo(carPlateNo);
		car.setNoOfPassenger(Long.parseLong(noOfPassenger));
		car.setDriverId(null);
		car.setBackImgUrl(hiddenBackImgUrl);
		car.setFrontImgUrl(hiddenFrontImgUrl);
		car.setCarTitle(null);
		car.setInsurancePhotoUrl(hiddenInsuranceImgUrl);
		car.setInspectionReportPhotoUrl(hiddenInspectionImgUrl);
		car.setVehicleCommercialLicencePhotoUrl(hiddenVehicleCommercialLicenseImgUrl);
		car.setRegistrationPhotoUrl(hiddenVehicleRegistrationImgUrl);
		car.setApprovelStatus(true);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		car.insertCar(loginSessionMap.get(LoginUtils.USER_ID), vendorId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_CAR_URL);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.CAR_COLOR, messageForKeyAdmin("labelCarColor"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CAR_COLOR, messageForKeyAdmin("labelCarColor"), new AlphaCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.CAR_PLATE_NUMBER, messageForKeyAdmin("labelCarPlateNo"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CAR_PLATE_NUMBER, messageForKeyAdmin("labelCarPlateNo"), new AlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.OWNER, messageForKeyAdmin("labelOwnerNumber"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.MODEL_NAME, messageForKeyAdmin("labelModelName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.MAKE, messageForKeyAdmin("labelCarMake"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_CAR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}