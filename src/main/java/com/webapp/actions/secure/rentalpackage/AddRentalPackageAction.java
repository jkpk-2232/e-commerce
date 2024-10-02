package com.webapp.actions.secure.rentalpackage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.RentalPackageModel;
import com.webapp.models.VendorCarTypeModel;

@Path("/add-rental-package")
public class AddRentalPackageAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadAddRentalPackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String vendorIdOptions = DropDownUtils.getUserAccessWiseVendorList(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String rentalPackageTypeOptions = DropDownUtils.getRentalPackageTypeOptions(ProjectConstants.RENTAL_INTERCITY_ID, false);
		data.put(FieldConstants.RENTAL_PACKAGE_TYPE_OPTIONS, rentalPackageTypeOptions);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(loginSessionMap.get(LoginUtils.USER_ID), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
			String dataAvailableCarList = "";
			for (VendorCarTypeModel string : vendorCarTypeList) {
				dataAvailableCarList += string.getCarTypeId() + ",";
			}

			if (dataAvailableCarList.length() > 0) {
				data.put(FieldConstants.CAR_AVAILABLE_LIST, dataAvailableCarList.substring(0, dataAvailableCarList.length() - 1));
			}

		} else {
			data.put(FieldConstants.CAR_AVAILABLE_LIST, "");
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_URL);
		return loadView(UrlConstants.JSP_URLS.ADD_RENTAL_PACKAGE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addRentalPackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.REGION_LIST) String regionList,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.RENTAL_PACKAGE_TYPE) String rentalPackageType,
		@FormParam(FieldConstants.PACKAGE_TIME) String packageTime,
		@FormParam(FieldConstants.PACKAGE_DISTANCE) String packageDistance,
		@FormParam(FieldConstants.QUERY_STRING) String queryString,
		@FormParam(FieldConstants.CAR_AVAILABLE_LIST) String carAvailableList
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.PACKAGE_TIME, packageTime);
		data.put(FieldConstants.PACKAGE_DISTANCE, packageDistance);
		data.put(FieldConstants.QUERY_STRING, queryString);

		logger.info("\n\n\n\tqueryString\t" + queryString);

		Map<String, Object> inputMap = MultiTenantUtils.parseInputParameters(queryString);

		logger.info("\n\n\n\tinputMap\t" + inputMap);
		logger.info("\n\n\n\tcarAvailableList\t" + carAvailableList);

		String[] availableCarList = MyHubUtils.splitStringByCommaArray(carAvailableList);
		String dataAvailableCarList = "";

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable", "");
			data.put(carTypeId + "_BaseFare", inputMap.get(carTypeId + "_BaseFare") != null ? inputMap.get(carTypeId + "_BaseFare").toString() : "");
			data.put(carTypeId + "_PerKmFare", inputMap.get(carTypeId + "_PerKmFare") != null ? inputMap.get(carTypeId + "_PerKmFare").toString() : "");
			data.put(carTypeId + "_PerMinuteFare", inputMap.get(carTypeId + "_PerMinuteFare") != null ? inputMap.get(carTypeId + "_PerMinuteFare").toString() : "");
			data.put(carTypeId + "_DriverPayablePercentage", inputMap.get(carTypeId + "_DriverPayablePercentage") != null ? inputMap.get(carTypeId + "_DriverPayablePercentage").toString() : "");

			dataAvailableCarList += carTypeId + ",";
		}

		if (dataAvailableCarList.length() > 0) {
			data.put(FieldConstants.CAR_AVAILABLE_LIST, dataAvailableCarList.substring(0, dataAvailableCarList.length() - 1));
		}

		String regionListOptions = DropDownUtils.getUserAccessWiseRegionList(regionList, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String vendorIdOptions = DropDownUtils.getUserAccessWiseVendorList(vendorId, loginSessionMap.get(LoginUtils.USER_ID), loginSessionMap.get(LoginUtils.ROLE_ID), false);
		data.put(FieldConstants.VENDOR_ID_OPTIONS, vendorIdOptions);

		String rentalPackageTypeOptions = DropDownUtils.getRentalPackageTypeOptions(rentalPackageType, false);
		data.put(FieldConstants.RENTAL_PACKAGE_TYPE_OPTIONS, rentalPackageTypeOptions);

		if (hasErrors(availableCarList)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_URL);
			return loadView(UrlConstants.JSP_URLS.ADD_RENTAL_PACKAGE_JSP);
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		RentalPackageModel rentalPackageModel = new RentalPackageModel();
		rentalPackageModel.setMulticityCityRegionId(regionList);
		rentalPackageModel.setRentalPackageType(rentalPackageType);
		rentalPackageModel.setPackageTime(StringUtils.intValueOf(packageTime));
		rentalPackageModel.setPackageDistance(StringUtils.doubleValueOf(packageDistance) * adminSettingsModel.getDistanceUnits());

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			rentalPackageModel.setVendorId(loginSessionMap.get(LoginUtils.USER_ID));
		} else {
			rentalPackageModel.setVendorId(vendorId);
		}

		String rentalPackageId = rentalPackageModel.addRentalPackage(loginSessionMap.get(LoginUtils.USER_ID));

		List<RentalPackageFareModel> rentalPackageFareModelList = new ArrayList<RentalPackageFareModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable", "");

			RentalPackageFareModel rentalPackageFareModel = new RentalPackageFareModel();

			rentalPackageFareModel.setRentalPackageFareId(UUIDGenerator.generateUUID());
			rentalPackageFareModel.setRentalPackageId(rentalPackageId);
			rentalPackageFareModel.setCarTypeId(carTypeId);
			rentalPackageFareModel.setBaseFare(Double.parseDouble(inputMap.get(carTypeId + "_BaseFare").toString().trim()));
			rentalPackageFareModel.setPerKmFare(Double.parseDouble(inputMap.get(carTypeId + "_PerKmFare").toString().trim()));
			rentalPackageFareModel.setPerMinuteFare(Double.parseDouble(inputMap.get(carTypeId + "_PerMinuteFare").toString().trim()));
			rentalPackageFareModel.setDriverPayablePercentage(Double.parseDouble(inputMap.get(carTypeId + "_DriverPayablePercentage").toString().trim()));
			rentalPackageFareModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			rentalPackageFareModel.setCreatedAt(currentTime);
			rentalPackageFareModel.setCreatedBy(loginSessionMap.get(LoginUtils.USER_ID));
			rentalPackageFareModel.setUpdatedAt(currentTime);
			rentalPackageFareModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));

			rentalPackageFareModelList.add(rentalPackageFareModel);
		}

		RentalPackageFareModel.insertRentalPackageFareBatch(rentalPackageFareModelList);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_RENTAL_PACKAGE_URL);
	}

	public boolean hasErrors(String[] availableCarList) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping("packageTime", messageForKeyAdmin("labelHours"), new RequiredValidationRule());
		validator.addValidationMapping("packageTime", messageForKeyAdmin("labelHours"), new NumericValidationRule());
		validator.addValidationMapping("packageTime", messageForKeyAdmin("labelHours"), new MinMaxValueValidationRule(0, 1000));

		validator.addValidationMapping("packageDistance", messageForKeyAdmin("labelDriverKm"), new RequiredValidationRule());
		validator.addValidationMapping("packageDistance", messageForKeyAdmin("labelDriverKm"), new DecimalValidationRule());
		validator.addValidationMapping("packageDistance", messageForKeyAdmin("labelDriverKm"), new MinMaxValueValidationRule(0, 10000));

		for (String string : availableCarList) {

			String carTypeId = string.replace("_IsAvailable", "");

			validator.addValidationMapping(carTypeId + "_BaseFare", messageForKeyAdmin("labelBaseFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_BaseFare", messageForKeyAdmin("labelBaseFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_BaseFare", messageForKeyAdmin("labelBaseFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerKmFare", messageForKeyAdmin("labelRentalPerKmFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare", messageForKeyAdmin("labelRentalPerKmFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerKmFare", messageForKeyAdmin("labelRentalPerKmFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_PerMinuteFare", messageForKeyAdmin("labelRentalPerMinuteFare"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare", messageForKeyAdmin("labelRentalPerMinuteFare"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_PerMinuteFare", messageForKeyAdmin("labelRentalPerMinuteFare"), new MinMaxValueValidationRule(0, 100000));

			validator.addValidationMapping(carTypeId + "_DriverPayablePercentage", messageForKeyAdmin("labelRentalDriverPayablePercentage"), new RequiredValidationRule());
			validator.addValidationMapping(carTypeId + "_DriverPayablePercentage", messageForKeyAdmin("labelRentalDriverPayablePercentage"), new DecimalValidationRule());
			validator.addValidationMapping(carTypeId + "_DriverPayablePercentage", messageForKeyAdmin("labelRentalDriverPayablePercentage"), new MinMaxValueValidationRule(0, 100000));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_RENTAL_PACKAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}