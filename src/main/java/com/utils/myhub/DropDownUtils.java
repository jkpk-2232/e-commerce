package com.utils.myhub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.jeeutils.ComboUtils;
import com.jeeutils.StringUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.ADMIN_SMS_SETTINGS_ENUM;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdModel;
import com.webapp.models.AppointmentModel;
import com.webapp.models.BrandModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CarIconModel;
import com.webapp.models.CarModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.CategoryGroupModel;
import com.webapp.models.CategoryModel;
import com.webapp.models.CityModel;
import com.webapp.models.CurrencyModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.LEDDeviceModel;
import com.webapp.models.LocationModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.MulticityUserRegionModel;
import com.webapp.models.OrderModel;
import com.webapp.models.ProductCategoryModel;
import com.webapp.models.ProductSubCategoryModel;
import com.webapp.models.ProductTemplateModel;
import com.webapp.models.ProductVariantModel;
import com.webapp.models.ResolutionModel;
import com.webapp.models.RoleModel;
import com.webapp.models.ServiceModel;
import com.webapp.models.ServiceTypeModel;
import com.webapp.models.StoreCategorieModel;
import com.webapp.models.StoreModel;
import com.webapp.models.SubscriptionPackageModel;
import com.webapp.models.UnitOfMeasureModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorCarFareModel;
import com.webapp.models.VendorCarTypeModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.WhmgmtRackCategoryModel;

public class DropDownUtils {

	public static String parserForAllOptions(String key) {

		if (!StringUtils.validString(key)) {
			return null;
		}

		if (key.equalsIgnoreCase(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE)) {
			return null;
		}

		return key;
	}

	public static String parserForAllOptionsForZero(String key) {

		if (!StringUtils.validString(key)) {
			return null;
		}

		if (key.equalsIgnoreCase("0")) {
			return null;
		}

		return key;
	}

	public static String getVendorOrderManagementOptionsList(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusName.add(ProjectConstants.All);
		statusValues.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);

		statusName.add(BusinessAction.messageForKeyAdmin("labelOwnDriver"));
		statusValues.add("1");

		statusName.add(BusinessAction.messageForKeyAdmin("labelDeliveryPartnerDriver"));
		statusValues.add("2");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getDateTypeOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusName.add("Days of week");
		statusValues.add(ProjectConstants.VENDOR_STORE_TIME_DAY_OF_WEEK + "");

		statusName.add("Specific dates");
		statusValues.add(ProjectConstants.VENDOR_STORE_TIME_SPECIFIC_DATE + "");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getShiftTypeOption(int selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusName.add(ProjectConstants.DAY_SHIFT);
		statusValues.add(ProjectConstants.DAY_SHIFT_ID + "");

		statusName.add(ProjectConstants.NIGHT_SHIFT);
		statusValues.add(ProjectConstants.NIGHT_SHIFT_ID + "");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getNumberOfShiftsOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusName.add("1");
		statusValues.add("1");

		statusName.add("2");
		statusValues.add("2");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getOrderStatusForOrdersToBeChangedByAdmin(OrderModel orderModel, String currentOrderDeliveryStatus, boolean isDelieveryManagedByVendorDriver, boolean isAction) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<String> orderStatusList = OrderUtils.getOrderStatusForOrdersToBeChangedByAdmin(orderModel, currentOrderDeliveryStatus, isDelieveryManagedByVendorDriver);

		if (orderStatusList.isEmpty()) {
			return null;
		}

		for (String os : orderStatusList) {
			names.add(OrderUtils.getOrderStatusDisplayLabels(os, isAction));
			values.add(os);
		}

		return ComboUtils.getOptionArrayForListBox(names, values, "");
	}

	public static String getAppointmentStatusForOrdersToBeChangedByAdmin(AppointmentModel appointmentModel, String currentOrderDeliveryStatus, boolean isAction) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<String> orderStatusList = AppointmentUtils.getAppointmentStatusForAppointmentsToBeChangedByAdmin(appointmentModel, currentOrderDeliveryStatus);

		if (orderStatusList.isEmpty()) {
			return null;
		}

		for (String os : orderStatusList) {
			names.add(AppointmentUtils.getAppointmentStatusDisplayLabels(os, isAction));
			values.add(os);
		}

		return ComboUtils.getOptionArrayForListBox(names, values, "");
	}

	public static String getOrderStatusList(boolean showAll, String type, String orderStatus) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (showAll) {
			names.add(ProjectConstants.All);
			values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		List<String> orderStatusList = OrderUtils.getOrderStatusListAsPerOrderType(type);
		for (String os : orderStatusList) {
			names.add(OrderUtils.getOrderStatusDisplayLabels(os));
			values.add(os);
		}

		return ComboUtils.getOptionArrayForListBox(names, values, orderStatus + "");
	}

	public static String getAppointmentStatusList(boolean showAll, String type, String appointmentStatus) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (showAll) {
			names.add(ProjectConstants.All);
			values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		List<String> appointmentStatusList = AppointmentUtils.getAppointmentStatusListAsPerAppointmentType(type);
		for (String os : appointmentStatusList) {
			names.add(AppointmentUtils.getAppointmentStatusDisplayLabels(os));
			values.add(os);
		}

		return ComboUtils.getOptionArrayForListBox(names, values, appointmentStatus + "");
	}

	public static String getVendorStoreFilterListOptions(List<String> selectedIdList, String vendorId, List<String> assignedRegionList) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {
			statusValues.add(vendorStoreModel.getVendorStoreId());
			statusName.add(vendorStoreModel.getStoreName());
		}

		return ComboUtils.getOptionArrayForListBoxForMultiSelect(statusName, statusValues, selectedIdList);
	}

	public static String getVendorStoreFilterListOptions(boolean showAll, String selectedId, List<String> assignedRegionList, String vendorId) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreList(assignedRegionList, vendorId);

		if (showAll) {
			statusName.add(ProjectConstants.All);
			statusValues.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {
			statusValues.add(vendorStoreModel.getVendorStoreId());
			statusName.add(vendorStoreModel.getStoreName() != null ? vendorStoreModel.getStoreName() : ProjectConstants.NOT_AVAILABLE);
		}

		return ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedId + "");
	}

	public static String getVendorFilterListOptions(String selectedId, String roleId, List<String> assignedRegionList) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<UserProfileModel> userModelList = UserProfileModel.getVendorListByServiceTypeId(assignedRegionList, null, "ui.first_name");

		statusName.add(BusinessAction.messageForKeyAdmin("labelAll"));
		statusValues.add("0");

		for (UserProfileModel user : userModelList) {
			statusValues.add(user.getUserId());
			statusName.add(MyHubUtils.formatFullName(user.getFirstName(), user.getLastName()));
		}

		return ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedId + "");
	}

	public static String getVendorListOptions(String selectedId, String roleId, List<String> assignedRegionList) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<UserProfileModel> userModelList = UserProfileModel.getVendorListByServiceTypeId(assignedRegionList, null, "ui.first_name");

		for (UserProfileModel user : userModelList) {
			statusValues.add(user.getUserId());
			statusName.add(MyHubUtils.formatFullName(user.getFirstName(), user.getLastName()));
		}
		return ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedId + "");
	}

	public static String getVendorListByServiceTypeIdList(boolean showAll, List<String> assignedRegionList, List<String> serviceTypeId, String vendorId) {

		List<UserProfileModel> userModelList = UserProfileModel.getVendorListByServiceTypeId(assignedRegionList, serviceTypeId, "ui.vendor_brand_name");

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (showAll) {
			names.add(ProjectConstants.All);
			values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		for (UserProfileModel userProfileModel : userModelList) {
			names.add(userProfileModel.getVendorBrandName());
			values.add(userProfileModel.getUserId());
		}

		return ComboUtils.getOptionArrayForListBox(names, values, vendorId + "");
	}

	public static String getServiceTypeOption(boolean showAll, String serviceTypeId) {

		List<ServiceTypeModel> serviceTypeList = ServiceTypeModel.getServiceTypeSearch(0, 0, "%%", 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (showAll) {
			names.add(ProjectConstants.All);
			values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		for (ServiceTypeModel serviceTypeModel : serviceTypeList) {
			names.add(serviceTypeModel.getServiceTypeName());
			values.add(serviceTypeModel.getServiceTypeId());
		}

		return ComboUtils.getOptionArrayForListBox(names, values, serviceTypeId + "");
	}

	public static String getProductWeightUnitOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(StringUtils.valueOf(ProjectConstants.WeightConstants.GRAMS_ID));
		statusName.add(ProjectConstants.WeightConstants.GRAMS);

		statusValues.add(StringUtils.valueOf(ProjectConstants.WeightConstants.KILOGRAMS_ID));
		statusName.add(ProjectConstants.WeightConstants.KILOGRAMS);

		statusValues.add(StringUtils.valueOf(ProjectConstants.WeightConstants.MILLILITERS_ID));
		statusName.add(ProjectConstants.WeightConstants.MILLILITERS);

		statusValues.add(StringUtils.valueOf(ProjectConstants.WeightConstants.LITERS_ID));
		statusName.add(ProjectConstants.WeightConstants.LITERS);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getYesNoOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(true + "");
		statusName.add(ProjectConstants.YES);

		statusValues.add(false + "");
		statusName.add(ProjectConstants.NO);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	private static String getSuperServicesListPrivate(boolean showAll, String displayType, String serviceId, String serviceTypeId) {

		List<ServiceModel> serviceList = ServiceModel.getServiceSearch(0, 0, "%%", 0, 0, displayType, "s.service_priority ASC", serviceTypeId);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (showAll) {
			names.add(ProjectConstants.All);
			values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		for (ServiceModel serviceModel : serviceList) {
			names.add(serviceModel.getServiceName());
			values.add(serviceModel.getServiceId());
		}

		return ComboUtils.getOptionArrayForListBox(names, values, serviceId + "");
	}

	public static String getSuperServicesList(boolean showAll, String displayType, String serviceId, String serviceTypeId) {
		return getSuperServicesListPrivate(showAll, displayType, serviceId + "", serviceTypeId);
	}

	public static String getSuperServicesList(boolean showAll, String displayType, String serviceId) {
		return getSuperServicesListPrivate(showAll, displayType, serviceId + "", null);
	}

	public static String getCategoryList(boolean showAll, String displayType, String serviceId, String categoryId) {

		List<CategoryModel> categoryList = CategoryModel.getCategorySearch(0, 0, "%%", 0, 0, serviceId, displayType);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (showAll) {
			names.add(ProjectConstants.All);
			values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		}

		for (CategoryModel categoryModel : categoryList) {
			names.add(categoryModel.getCategoryName());
			values.add(categoryModel.getCategoryId());
		}

		return ComboUtils.getOptionArrayForListBox(names, values, categoryId + "");
	}

	public static String getLanguageStringLanguage(String selectedId, String langauge) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelEnglish", langauge));
		values.add(ProjectConstants.ENGLISH_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getDriverCompanyOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(ProjectConstants.COMPANY_DRIVER);
		values.add(ProjectConstants.COMPANY_DRIVER_ID);
		names.add(ProjectConstants.INDIVIDUAL_DRIVER);
		values.add(ProjectConstants.INDIVIDUAL_DRIVER_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getGenderOption(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Male");
		values.add("Male");
		names.add("Female");
		values.add("Female");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getEncashRequestStatusOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelEncashRequestSendPendingForApproval"));
		values.add(ProjectConstants.ENCASH_REQUEST_SEND_PENDING_FOR_APPROVAL_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelEncashRequestSendPendingForTransfer"));
		values.add(ProjectConstants.ENCASH_REQUEST_SEND_PENDING_FOR_TRANSFER_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelEncashRequestSendDirectTransfer"));
		values.add(ProjectConstants.ENCASH_REQUEST_SEND_DIRECT_TRANSFER_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getCarOption(String selectedValue, List<String> carTypeList, String vendorId) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<CarModel> carTypeModel = CarModel.getCarListByCarTypeIds(carTypeList, vendorId);

		statusValues.add("-1");
		statusName.add("Select");

		for (CarModel carTypeModel2 : carTypeModel) {

			String plateNumber = MyHubUtils.formatPlateNumber(carTypeModel2.getCarPlateNo());

			statusValues.add(carTypeModel2.getCarId());
			statusName.add(carTypeModel2.getMake() + ", " + carTypeModel2.getModelName() + ", " + plateNumber + ", " + carTypeModel2.getCarType() + ", " + carTypeModel2.getOwner());
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getGenderOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(ProjectConstants.MALE);
		values.add(ProjectConstants.MALE);
		names.add(ProjectConstants.FEMALE);
		values.add(ProjectConstants.FEMALE);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getDaysOptionRideLater(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (int i = 1; i <= 30; i++) {

			names.add(i + "");
			values.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getToutStatusFilterList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add(ProjectConstants.ALL_USERS_ID);

		names.add(ProjectConstants.TourStatusConstants.NEW_TOUR);
		values.add(ProjectConstants.TourStatusConstants.NEW_TOUR_ID);

		names.add(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);
		values.add(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR_ID);

		names.add(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
		values.add(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST_ID);

		names.add(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);
		values.add(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR_ID);

		names.add(ProjectConstants.TourStatusConstants.STARTED_TOUR);
		values.add(ProjectConstants.TourStatusConstants.STARTED_TOUR_ID);

		names.add(ProjectConstants.TourStatusConstants.ENDED_TOUR);
		values.add(ProjectConstants.TourStatusConstants.ENDED_TOUR_ID);

		names.add(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);
		values.add(ProjectConstants.TourStatusConstants.EXPIRED_TOUR_ID);

		names.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER);
		values.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER_ID);

		names.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
		values.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER_ID);

		names.add(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE);
		values.add(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getTourStatus(String selectedId, String page) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(ProjectConstants.All);
		values.add(ProjectConstants.All);

		if (page.equalsIgnoreCase("current")) {

			names.add(ProjectConstants.TourStatusConstants.NEW_TOUR);
			values.add(ProjectConstants.TourStatusConstants.NEW_TOUR);
			names.add(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);
			values.add(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);
			names.add(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
			values.add(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
			names.add(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);
			values.add(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);
			names.add(ProjectConstants.TourStatusConstants.STARTED_TOUR);
			values.add(ProjectConstants.TourStatusConstants.STARTED_TOUR);

		} else {

			names.add(ProjectConstants.TourStatusConstants.ENDED_TOUR);
			values.add(ProjectConstants.TourStatusConstants.ENDED_TOUR);
			names.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER);
			values.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER);
			names.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
			values.add(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
			names.add(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE);
			values.add(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE);
			names.add(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);
			values.add(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getMultiCityCountry(String driverId) {

		List<MulticityCountryModel> stateList = MulticityCountryModel.getMulticityCountrySearch(0, 1000000, "", "%%");

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Select Country Name");
		values.add("-1");

		for (MulticityCountryModel multicityCountryModel : stateList) {

			names.add(multicityCountryModel.getCountryName());
			values.add(multicityCountryModel.getMulticityCountryId() + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, driverId + "");

		return driverListOptions;
	}

	public static String getSurgePriceOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (double surge = 1.25; surge <= 4.0; surge = surge + 0.25) {

			names.add(surge + "");
			values.add(surge + "");
		}

		String surgePriceOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return surgePriceOptions;
	}

	public static String getSurgeRateOptionsStartWithMin(double minSurge, String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (double surge = minSurge; surge <= 4.0; surge = surge + 0.25) {

			names.add(surge + "");
			values.add(surge + "");
		}

		String surgePriceOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return surgePriceOptions;
	}

	public static String getHourOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (int hour = 0; hour <= 23; hour++) {

			names.add(hour + "");
			values.add(hour + "");
		}

		String hourOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return hourOptions;
	}

	public static String getMinuteOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (int minute = 0; minute <= 59; minute++) {

			names.add(minute + "");
			values.add(minute + "");
		}

		String minuteOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return minuteOptions;
	}

	public static String getMultiCityRegionOptions(String selectedId, String multicityCountryId) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionListByMulticityCountryId(multicityCountryId, null, 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
		}

		String multiCityRegionOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return multiCityRegionOptions;
	}

	public static String getOptionsForSurgeFilter(String selectedId, boolean is1xValueInFilter) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("All");
		values.add("-1");

		if (is1xValueInFilter) {

			for (double surge = 1; surge <= 4.0; surge = surge + 0.25) {

				names.add(surge + "x");
				values.add(surge + "");
			}

		} else {

			for (double surge = 1.25; surge <= 4.0; surge = surge + 0.25) {

				names.add(surge + "");
				values.add(surge + "");
			}
		}

		String surgePriceOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);

		return surgePriceOptions;
	}

	public static String getOptionsForegionFilter(String selectedId, String multicityCountryId) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionListByMulticityCountryId(multicityCountryId, null, 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("All");
		values.add("-1");

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {
			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId());
		}

		String multiCityRegionOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);

		return multiCityRegionOptions;
	}

	public static String getCarIconsListOptions(String selectedId, String basePath) {

		List<CarIconModel> carIconList = CarIconModel.getAllCarIcons();

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (CarIconModel carIconModel : carIconList) {

			values.add(carIconModel.getCarIconId());
			names.add("Icon " + carIconModel.getCarIconId());
		}

		String multiCityRegionOptions = ComboUtils.getOptionArrayForListBoxCarIcons(names, values, selectedId, basePath);

		return multiCityRegionOptions;
	}

	public static String getDriverListByMulticityRegionIdsForMultiselectOptions(List<String> selectedIdList, String[] multicityRegionIds) {

		List<DriverInfoModel> driverList = DriverInfoModel.getDriverListByMulticityRegionIds(UserRoles.DRIVER_ROLE_ID, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, multicityRegionIds);

		if (driverList.isEmpty()) {
			return null;
		}

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		names.add(ProjectConstants.All);
		values.add(ProjectConstants.All);

		for (DriverInfoModel driverInfoModel : driverList) {
			names.add(driverInfoModel.getFirstName() + " " + driverInfoModel.getLastName());
			values.add(driverInfoModel.getUserId());
		}

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getCarTypeListOptionsForMultiselect(List<String> selectedIdList, boolean isDriverTypeDisplay) throws SQLException {

		List<CarTypeModel> carTypeModelList = CarTypeModel.getAllCars();

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (CarTypeModel carTypeModel : carTypeModelList) {

			if (ProjectConstants.Fifth_Vehicle_ID.equals(carTypeModel.getCarTypeId())) {

				if (isDriverTypeDisplay) {

					values.add(carTypeModel.getCarTypeId());
					names.add(carTypeModel.getCarType());
				}

			} else {

				values.add(carTypeModel.getCarTypeId());
				names.add(carTypeModel.getCarType());
			}
		}

		return ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
	}

	public static String getTransmissionTypeListOptionsForMultiselect(List<String> selectedTransmissionTypeIdList) throws SQLException {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelTransmissionAutomatic"));
		values.add("1");

		names.add(BusinessAction.messageForKeyAdmin("labelTransmissionNonAutomatic"));
		values.add("2");

		return ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedTransmissionTypeIdList);
	}

	public static String getCcavenueStatusFilterList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add(ProjectConstants.ALL_USERS_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelSuccess"));
		values.add("2");

		names.add(BusinessAction.messageForKeyAdmin("labelFailure"));
		values.add("3");

		names.add(BusinessAction.messageForKeyAdmin("labelAborted"));
		values.add("4");

		names.add(BusinessAction.messageForKeyAdmin("labelInvalid"));
		values.add("5");

		names.add(BusinessAction.messageForKeyAdmin("labelInitiated"));
		values.add("6");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getPassengerTypeOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(ProjectConstants.REGISTERED_PASSENGER_ID);
		statusName.add(ProjectConstants.REGISTERED_PASSENGER);

		statusValues.add(ProjectConstants.NONREGISTERED_PASSENGER_ID);
		statusName.add(ProjectConstants.NONREGISTERED_PASSENGER);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getPassengerListOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<UserProfileModel> passengerList = UserProfileModel.getAllUsersByRoleId(UserRoles.PASSENGER_ROLE_ID);

		statusValues.add("-1");
		statusName.add("Select");

		for (UserProfileModel user : passengerList) {
			statusValues.add(user.getUserId());
			statusName.add(user.getFullName() + ", " + user.getEmail());
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getVendorPassengerListOption(String selectedValue, String vendorId, String roleId, List<String> assignedRegionList) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<UserProfileModel> passengerList = UserProfileModel.getVendorPassengerList(vendorId, roleId, assignedRegionList);

		statusValues.add("-1");
		statusName.add("Select");

		for (UserProfileModel user : passengerList) {
			statusValues.add(user.getUserId());
			statusName.add(user.getFullName() + ", " + user.getEmail());
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getUserRoleListForBroadcastByRoleIds(String selectedId, String[] roleIds) {

		List<RoleModel> roles = RoleModel.getRolesByIds(roleIds);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (RoleModel role : roles) {

			if (role.getRoleId().equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {
				names.add(BusinessAction.messageForKeyAdmin("labelPassenger").toUpperCase());
			} else if (role.getRoleId().equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID)) {
				names.add(BusinessAction.messageForKeyAdmin("labelDriver").toUpperCase());
			}

			values.add(role.getRoleId());
		}

		String userRoleListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return userRoleListOptions;
	}

	public static String getServiceTypeOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Car");
		values.add(ProjectConstants.SERVICE_TYPE_CAR_ID);

		names.add("Driver");
		values.add(ProjectConstants.SERVICE_TYPE_DRIVER_ID);

		return ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
	}

	public static String getDriverListOptionsForRideLater(String selectedValue, Map<String, Object> inputMap) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<UserProfileModel> driverList = UserProfileModel.getDriverListForManualBookingRideLater(inputMap);

		statusValues.add("-1");
		statusName.add("Assign Later");

		for (UserProfileModel user : driverList) {

			statusValues.add(user.getUserId());
			statusName.add(user.getFirstName() + " " + user.getLastName() + ", " + user.getEmail());
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getRentalPackageTypeOptions(String selectedId, boolean isAllOptionDisplay) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (isAllOptionDisplay) {

			names.add(ProjectConstants.ALL_TEXT);
			values.add("-1");
		}

		names.add(ProjectConstants.RENTAL_INTERCITY_STRING);
		values.add(ProjectConstants.RENTAL_INTERCITY_ID);

		names.add(ProjectConstants.RENTAL_OUTSTATION_STRING);
		values.add(ProjectConstants.RENTAL_OUTSTATION_ID);

		String rentalPackageTypeOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return rentalPackageTypeOptions;
	}

	public static String getAdminExportAccessListOptions(List<String> selectedIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		names.add(BusinessAction.messageForKeyAdmin("labelBookings") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_BOOKING_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelPassenger") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_PASSENGER_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverIncomeReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelRefundReports") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_REFUND_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverDutyReports") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelCcavenueLogReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_CCAVENUE_LOG_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverDriveReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverBenefitReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_BENEFIT_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverAccount") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_DRIVER_ACCOUNT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelVendorAccount") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_VENDOR_ACCOUNT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelVendorDriverAccount") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverSubscriptionReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverTransactionHistoryReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID);
		
		names.add(BusinessAction.messageForKeyAdmin("labelPhonepeLogReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_PHONEPE_LOG_REPORT_ID);
		
		names.add(BusinessAction.messageForKeyAdmin("labelWarehouseUsers") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_WAREHOUSE_ID);

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getVendorExportAccessListOptions(List<String> selectedIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelBookings") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_BOOKING_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverIncomeReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_INCOME_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverDutyReports") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DUTY_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverDriveReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_ACCESS_DRIVER_DRIVE_REPORT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverAccount") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_VENDOR_DRIVER_ACCOUNT_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverSubscriptionReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_DRIVER_SUBSCRIPTION_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDriverTransactionHistoryReport") + " " + BusinessAction.messageForKeyAdmin("labelExport"));
		values.add(ProjectConstants.EXPORT_DRIVER_TRANSACTION_HISTORY_ID);

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getAdminAccessListOptions(List<String> selectedIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelDashboard"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_DASHBOARD_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelManualBookings"));
		//values.add(ProjectConstants.MENU_CONSTANTS.MENU_MANUAL_BOOKINGS_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labellogistics"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_lOGISTICS_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelRetailSupply"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_RETAILSUPPLY_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelInventory"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_INVENTORY_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelMarketSurvey"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_MARKETSURVEY_ID + "");
		
		
		names.add(BusinessAction.messageForKeyAdmin("labelMarketShare"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_MARKET_SHARE_ID + "");
		

		names.add(BusinessAction.messageForKeyAdmin("labelMarketing"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_MARKETING_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelUsers"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_USERS_ID + "");
		
		//names.add(BusinessAction.messageForKeyAdmin("labelBookings"));
		//values.add(ProjectConstants.MENU_CONSTANTS.MENU_BOOKINGS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelAdminUser"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_ADMIN_USER_ID + "");

		//names.add(BusinessAction.messageForKeyAdmin("labelPassengers"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_CUSTOMERS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelDrivers"));
		//values.add(ProjectConstants.MENU_CONSTANTS.MENU_DRIVERS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelRideLater"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_BOOK_LATER_ID_1 + "");

		names.add(BusinessAction.messageForKeyAdmin("labelCriticalRideLater"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_CRITICAL_BOOK_LATER_ID_1 + "");

		names.add(BusinessAction.messageForKeyAdmin("labelTakeRide"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_TAKE_BOOKINGS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelCars"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_CARS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelAnnouncments"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_BROADCAST_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelRefund"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_REFUND_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelReports"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_REPORTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelSettings"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_SETTINGS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelVendor"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDORS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelSubVendors"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUB_VENDOR_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelDriverAccounts"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_DRIVER_ACCOUNTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelVendorAccounts"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_ACCOUNTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelHoldEncashRequests"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_HOLD_ENCASH_REQUESTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelApprovedEncashRequests"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_APPROVED_ENCASH_REQUESTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelTransferredEncashRequests"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_TRANSFERRED_ENCASH_REQUESTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelRejectedEncashRequests"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_REJECTED_ENCASH_REQUESTS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelSuperServices"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUPER_SERVICES_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelCategories"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_CATEGORIES_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelProductsSales"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_PRODUCTS_SALES_ID + "");//ADDED

		names.add(BusinessAction.messageForKeyAdmin("labelAppointments"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_APPOINTMENT_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelSubscribers"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUBSCRIBERS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelPromoCode"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_PROMO_CODE_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelFeeds"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_FEEDS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelProductsPrice"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_PRODUCTS_AND_PRICE_ID + "");//ADDED
		
	//	names.add(BusinessAction.messageForKeyAdmin("labelFeed2"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_FEED2_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelWarehouse"));
		values.add(ProjectConstants.MENU_CONSTANTS.WAREHOUSE_ID + "");
		
	//	names.add(BusinessAction.messageForKeyAdmin("labelBusinessUsers"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.BUSINESS_INTERESTED_USERS + "");
		
	//	names.add(BusinessAction.messageForKeyAdmin("labelWarehouseUsers"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.WAREHOUSE_USER + "");
		
	//	names.add(BusinessAction.messageForKeyAdmin("labelWarehouseUsers"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.WAREHOUSE_USER + "");
		
	//	names.add(BusinessAction.messageForKeyAdmin("labelERPUsers"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.ERP_USER + "");

//		names.add(BusinessAction.messageForKeyAdmin("labelHistory"));
//		values.add(ProjectConstants.MENU_CONSTANTS.MENU_HISTORY_ID + "");

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getVendorAccessListOptions(List<String> selectedIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelDashboard"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_DASHBOARD_ID + "");

		//names.add(BusinessAction.messageForKeyAdmin("labelManualBookings"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_MANUAL_BOOKINGS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labellogistics"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_lOGISTICS_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelRetailSupply"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_RETAILSUPPLY_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelInventory"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_INVENTORY_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelMarketSurvey"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_MARKETSURVEY_ID + "");
		
		
		names.add(BusinessAction.messageForKeyAdmin("labelMarketShare"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_MARKET_SHARE_ID + "");
		

		names.add(BusinessAction.messageForKeyAdmin("labelMarketing"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_MARKETING_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelUsers"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_USERS_ID + "");
		
		//names.add(BusinessAction.messageForKeyAdmin("labelBookings"));
		//values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_BOOKINGS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelPassengers"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_CUSTOMERS_ID + "");

		//names.add(BusinessAction.messageForKeyAdmin("labelDrivers"));
		//values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_DRIVERS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelCars"));
		//values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_CARS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelReports"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_REPORTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelRideLater"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_BOOK_LATER_ID_2 + "");

		names.add(BusinessAction.messageForKeyAdmin("labelCriticalRideLater"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_CRITICAL_BOOK_LATER_ID_2 + "");

		names.add(BusinessAction.messageForKeyAdmin("labelDriverAccounts"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_DRIVER_ACCOUNTS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelTakeRide"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_TAKE_BOOKINGS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelProductsSales"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_PRODUCTS_SALES_ID + "");//ADDED

		names.add(BusinessAction.messageForKeyAdmin("labelAppointments"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_APPOINTMENT_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelSubscribers"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUBSCRIBERS_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelPromoCode"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_PROMO_CODE_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelFeeds"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_FEEDS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelVendorStores"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_STORE_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelVendorMonthlySubscriptionHistory"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelSubVendors"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUB_VENDOR_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelProductsPrice"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_PRODUCTS_AND_PRICE_ID + "");//ADDED
		
	//	names.add(BusinessAction.messageForKeyAdmin("labelFeed2"));
	//	values.add(ProjectConstants.MENU_CONSTANTS.MENU_FEED2_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelWarehouse"));
		values.add(ProjectConstants.MENU_CONSTANTS.WAREHOUSE_ID + "");
		
		names.add(BusinessAction.messageForKeyAdmin("labelERPEmployees"));
		values.add(ProjectConstants.MENU_CONSTANTS.ERP_EMPLOYEE_ID + "");

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getSubVendorAccessListOptions(List<String> selectedIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelDashboard"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_DASHBOARD_ID + "");

	//	names.add(BusinessAction.messageForKeyAdmin("labelPassengers"));
//		values.add(ProjectConstants.MENU_CONSTANTS.MENU_CUSTOMERS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelProductsSales"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_PRODUCTS_SALES_ID + "");//ADDED

		names.add(BusinessAction.messageForKeyAdmin("labelAppointments"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_APPOINTMENT_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelSubscribers"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUBSCRIBERS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelPromoCode"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_PROMO_CODE_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelFeeds"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_FEEDS_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelVendorStores"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_STORE_ID + "");

		names.add(BusinessAction.messageForKeyAdmin("labelSubVendors"));
		values.add(ProjectConstants.MENU_CONSTANTS.MENU_SUB_VENDOR_ID + "");

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getDriverIdealTimeOptions(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = 1; i <= 60; i++) {

			if (i == 10 || i == 20 || i == 30 || i == 45) {

				statusName.add(i + " " + "min");
				statusValues.add(i + "_min");
			}
		}

		for (int i = 1; i <= 100; i++) {

			if (i == 1 || i == 2 || i == 4 || i == 8 || i == 12 || i == 24 || i == 48 || i == 72) {

				statusName.add(i + " " + "hr");
				statusValues.add(i + "_hr");
			}
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getApprovelSearchList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add("0");
		names.add(BusinessAction.messageForKeyAdmin("labelApproved"));
		values.add("true");
		names.add(BusinessAction.messageForKeyAdmin("labelNotApproved"));
		values.add("false");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getDisableRegionListByMulticityCountryIdOptions(List<String> selectedIdList, String multicityCountryId, List<String> assignedRegionList) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionListByMulticityCountryId(multicityCountryId, assignedRegionList, 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
		}

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForDisableMultiSelect(names, values, selectedIdList);
		return regionListOptions;
	}

	public static String getCarTypeListOptionsForDisableMultiselect(List<String> selectedIdList, boolean isDriverTypeDisplay) throws SQLException {

		List<CarTypeModel> carTypeModelList = CarTypeModel.getAllCars();

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (CarTypeModel carTypeModel : carTypeModelList) {

			if (ProjectConstants.Fifth_Vehicle_ID.equals(carTypeModel.getCarTypeId())) {

				if (isDriverTypeDisplay) {

					values.add(carTypeModel.getCarTypeId());
					names.add(carTypeModel.getCarType());
				}

			} else {

				values.add(carTypeModel.getCarTypeId());
				names.add(carTypeModel.getCarType());
			}
		}

		return ComboUtils.getOptionArrayForListBoxForDisableMultiSelect(names, values, selectedIdList);
	}

	public static String getDisableTransmissionTypeListOptionsForMultiselect(List<String> selectedTransmissionTypeIdList) throws SQLException {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelTransmissionAutomatic"));
		values.add("1");

		names.add(BusinessAction.messageForKeyAdmin("labelTransmissionNonAutomatic"));
		values.add("2");

		return ComboUtils.getOptionArrayForListBoxForDisableMultiSelect(names, values, selectedTransmissionTypeIdList);
	}

	public static String getDisableServiceTypeOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Car");
		values.add("1");

		names.add("Driver");
		values.add("2");

		return ComboUtils.getDisableOptionArrayForListBox(names, values, selectedId + "");
	}

	public static String getDisableCarModelOption(String selectedValue, boolean isDriverTypeDisplay, boolean isRentalTypeDisplay) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<CarTypeModel> carTypeModel = CarTypeModel.getAllCars();

		for (CarTypeModel carTypeModel2 : carTypeModel) {

			if (ProjectConstants.Fifth_Vehicle_ID.equals(carTypeModel2.getCarTypeId())) {

				if (isDriverTypeDisplay) {

					statusValues.add(carTypeModel2.getCarTypeId());
					statusName.add(carTypeModel2.getCarType());
				}

			} else {

				statusValues.add(carTypeModel2.getCarTypeId());
				statusName.add(carTypeModel2.getCarType());
			}
		}

		if (isRentalTypeDisplay) {

			statusValues.add(ProjectConstants.Rental_Type_ID);
			statusName.add("Rental");
		}

		String driverListOptions = ComboUtils.getDisableOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getDisableNumberOfPassangerOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();
		for (int i = 2; i <= 7; i++) {
			statusName.add(i + "");
			statusValues.add(i + "");
		}

		String driverListOptions = ComboUtils.getDisableOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getDisablePastFifteenYearOption(String selectedValue) {

		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		int currentYear = localCalendar.get(Calendar.YEAR);

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = currentYear; i > 1979; i--) {

			statusName.add(i + "");
			statusValues.add(i + "");
		}

		String driverListOptions = ComboUtils.getDisableOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getSubscriptionDurationInDaysOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = 7; i <= 365; i++) {

			statusName.add(i + " Days");
			statusValues.add(i + "");
		}
		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getSubscriptionPackageListOptions(String selectedId) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<SubscriptionPackageModel> vendorList = SubscriptionPackageModel.getAllActiveSubscriptionPackagesList(0, 0, null);

		for (SubscriptionPackageModel subscriptionPackageModel : vendorList) {
			statusValues.add(subscriptionPackageModel.getSubscriptionPackageId());
			CarTypeModel ctm = CarTypeModel.getCarTypeByCarTypeId(subscriptionPackageModel.getCarTypeId());
			statusName.add("Car Type: " + ctm.getCarType() + " & Name: " + subscriptionPackageModel.getPackageName() + " & Duration: " + subscriptionPackageModel.getDurationDays() + " Days & Price: " + StringUtils.valueOf(subscriptionPackageModel.getPrice()));
		}
		return ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedId + "");
	}

	public static String getSubscriptionPackageListOptions(String selectedId, String driverId) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<SubscriptionPackageModel> vendorList = SubscriptionPackageModel.getAllActiveSubscriptionPackagesListDriverCarTypeWise(0, 0, driverId);

		for (SubscriptionPackageModel subscriptionPackageModel : vendorList) {
			statusValues.add(subscriptionPackageModel.getSubscriptionPackageId());
			CarTypeModel ctm = CarTypeModel.getCarTypeByCarTypeId(subscriptionPackageModel.getCarTypeId());
			statusName.add("Car Type: " + ctm.getCarType() + " & Name: " + subscriptionPackageModel.getPackageName() + " & Duration: " + subscriptionPackageModel.getDurationDays() + " Days & Price: " + StringUtils.valueOf(subscriptionPackageModel.getPrice()));
		}
		return ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedId + "");
	}

	public static String getUserListByRoleIdOptions(List<String> selectedIdList, String roleId) {

		List<UserProfileModel> stateList = UserProfileModel.getAllUsersByRoleId(roleId);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		names.add(ProjectConstants.All);
		values.add(ProjectConstants.All);

		for (UserProfileModel userProfileModel : stateList) {
			names.add(userProfileModel.getFullName());
			values.add(userProfileModel.getUserId() + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);

		return driverListOptions;
	}

	public static String getDriverListByRoleIdOptionsAndApplicationStatus(List<String> selectedIdList, String roleId, String applicationStatus, List<String> assignedRegionList) {

		List<DriverInfoModel> stateList = DriverInfoModel.getAllDriverDetailsByRoleIdAndApplicationStatus(roleId, applicationStatus, assignedRegionList);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (DriverInfoModel userProfileModel : stateList) {
			names.add(userProfileModel.getFirstName() + " " + userProfileModel.getLastName());
			values.add(userProfileModel.getUserId() + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);

		return driverListOptions;

	}

	public static String getBusinessOwnerList(String driverId, String roleId) {

		List<UserProfileModel> stateList = UserProfileModel.getBOUsersByRoleId(roleId);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Select Company Name");
		values.add("-1");

		for (UserProfileModel userProfileModel : stateList) {
			names.add(userProfileModel.getCompanyName());
			values.add(userProfileModel.getUserId() + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, driverId + "");

		return driverListOptions;
	}

	public static String getStatusList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);

		names.add(BusinessAction.messageForKeyAdmin("labelActive"));
		values.add(ProjectConstants.ACTIVE_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDeactive"));
		values.add(ProjectConstants.DEACTIVE_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getPassengerSearchList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add("0");

		names.add(BusinessAction.messageForKeyAdmin("labelActive"));
		values.add(ProjectConstants.ACTIVE_ID);

		names.add(BusinessAction.messageForKeyAdmin("labelDeactive"));
		values.add(ProjectConstants.DEACTIVE_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getDriverOnOffSearchList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelOnOff"));
		values.add("0");
		names.add(BusinessAction.messageForKeyAdmin("labelOn"));
		values.add("1");
		names.add(BusinessAction.messageForKeyAdmin("labelOff"));
		values.add("2");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getPromoCodeUsage(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(ProjectConstants.LIMITED_TEXT);
		values.add(ProjectConstants.LIMITED_ID);
		names.add(ProjectConstants.UNLIMITED_TEXT);
		values.add(ProjectConstants.UNLIMITED_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getPromoCodeUsageType(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(ProjectConstants.ALL_TEXT);
		values.add(ProjectConstants.ALL_ID);
		names.add(ProjectConstants.INDIVIDUAL_TEXT);
		values.add(ProjectConstants.INDIVIDUAL_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getModeOption(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Percentage");
		values.add(ProjectConstants.PERCENTAGE_ID);
		names.add("Amount");
		values.add(ProjectConstants.AMOUNT_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getToutUserFilterList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add(ProjectConstants.ALL_USERS_ID);
		names.add(BusinessAction.messageForKeyAdmin("labelAdmin"));
		values.add(ProjectConstants.ADMIN_USERS_ID);
		names.add(BusinessAction.messageForKeyAdmin("labelDriver"));
		values.add(ProjectConstants.DRIVER_USERS_ID);
		names.add(BusinessAction.messageForKeyAdmin("labelPassenger"));
		values.add(ProjectConstants.PASSENGER_USERS_ID);
		names.add(BusinessAction.messageForKeyAdmin("labelVendor"));
		values.add(UserRoles.VENDOR_ROLE_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getLanguageString(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(ProjectConstants.ENGLISH);
		values.add(ProjectConstants.ENGLISH_ID);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getRadiusString(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (int i = 2; i <= 50; i++) {
			names.add(i + "");
			values.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getAdminAreaRadius(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (int i = 5; i <= 200; i = i + 5) {
			names.add(i + "");
			values.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getAdminAreaRadiusStartsWithMinRadius(long minRadius, String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (int i = (int) minRadius; i <= 100; i++) {
			names.add(i + "");
			values.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");

		return driverListOptions;
	}

	public static String getCarModelOption(String selectedValue, boolean isDriverTypeDisplay, boolean isRentalTypeDisplay) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<CarTypeModel> carTypeModel = CarTypeModel.getAllCars();

		for (CarTypeModel carTypeModel2 : carTypeModel) {

			if (ProjectConstants.Fifth_Vehicle_ID.equals(carTypeModel2.getCarTypeId())) {

				if (isDriverTypeDisplay) {

					statusValues.add(carTypeModel2.getCarTypeId());
					statusName.add(carTypeModel2.getCarType());
				}

			} else {

				statusValues.add(carTypeModel2.getCarTypeId());
				statusName.add(carTypeModel2.getCarType());
			}
		}

		if (isRentalTypeDisplay) {

			statusValues.add(ProjectConstants.Rental_Type_ID);
			statusName.add("Rental");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getDynamicCarOption(String selectedValue, String userId, String multicityRegionId, boolean isDriverTypeDisplay, boolean isRentalTypeDisplay) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(userId);

		if (userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {

			List<String> tempList = new ArrayList<>();
			List<VendorCarFareModel> carFareList = VendorCarFareModel.getVendorCarFareListByRegionIdAndVendorId(multicityRegionId, userId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

			for (VendorCarFareModel vendorCarFareModel : carFareList) {
				tempList.add(vendorCarFareModel.getCarTypeId());
			}

			BusinessAction.logger.info("\n\n\ttempList\t" + tempList);

			List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(userId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
			for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeList) {
				if (tempList.contains(vendorCarTypeModel.getCarTypeId())) {
					if (ProjectConstants.Fifth_Vehicle_ID.equals(vendorCarTypeModel.getCarTypeId())) {
						if (isDriverTypeDisplay) {
							statusValues.add(vendorCarTypeModel.getCarTypeId());
							statusName.add(vendorCarTypeModel.getCarType());
						}
					} else {
						statusValues.add(vendorCarTypeModel.getCarTypeId());
						statusName.add(vendorCarTypeModel.getCarType());
					}
				}
			}
		} else {
			BusinessAction.logger.info("\n\n\ttempList 1");
			List<CarFareModel> carFareList = CarFareModel.getCarFareListByRegionId(multicityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
			for (CarFareModel carFareModel : carFareList) {
				if (ProjectConstants.Fifth_Vehicle_ID.equals(carFareModel.getCarTypeId())) {
					if (isDriverTypeDisplay) {
						statusValues.add(carFareModel.getCarTypeId());
						statusName.add(carFareModel.getCarType());
					}
				} else {
					statusValues.add(carFareModel.getCarTypeId());
					statusName.add(carFareModel.getCarType());
				}
			}
		}

		if (isRentalTypeDisplay) {
			statusValues.add(ProjectConstants.Rental_Type_ID);
			statusName.add("Rental");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getCurrencyOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<CurrencyModel> currencyListModel = CurrencyModel.getCurrencyList();

		for (CurrencyModel currency : currencyListModel) {
			statusValues.add(currency.getCurrencyId() + "");
			statusName.add(currency.getCountry() + ", " + currency.getCurrency() + ", " + currency.getCode() + ", " + currency.getSymbol());
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getRideTypeOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(ProjectConstants.RIDE_NOW);
		statusName.add(ProjectConstants.RIDE_NOW_STRING);

		statusValues.add(ProjectConstants.RIDE_LATER);
		statusName.add(ProjectConstants.RIDE_LATER_STRING);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getDistanceTypeOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(ProjectConstants.KM);
		statusName.add(ProjectConstants.KM);

		statusValues.add(ProjectConstants.MILES);
		statusName.add(ProjectConstants.MILES);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getPaymentTypeOption(String selectedValue) throws SQLException {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(ProjectConstants.CASH_ID);
		statusName.add(ProjectConstants.C_CASH);

		// statusValues.add(ProjectConstants.CARD_ID);
		// statusName.add(ProjectConstants.C_CARD);
		// statusValues.add(ProjectConstants.WALLET_ID);
		// statusName.add(ProjectConstants.C_WALLET);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getNumberOfPassangerOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = 1; i <= 7; i++) {
			statusName.add(i + "");
			statusValues.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getNumberOfCarsOption(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = 1; i <= 5; i++) {

			statusName.add(i + "");
			statusValues.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getPastFifteenYearOption(String selectedValue) {

		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());

		int currentYear = localCalendar.get(Calendar.YEAR);

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = currentYear; i > 1979; i--) {

			statusName.add(i + "");
			statusValues.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");

		return driverListOptions;
	}

	public static String getCountryCodeOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("+1");
		names.add("+7");
		names.add("+20");
		names.add("+27");
		names.add("+30");
		names.add("+31");
		names.add("+32");
		names.add("+33");
		names.add("+34");
		names.add("+36");
		names.add("+39");
		names.add("+39");
		names.add("+40");
		names.add("+41");
		names.add("+43");
		names.add("+44");
		names.add("+45");
		names.add("+46");
		names.add("+47");
		names.add("+48");
		names.add("+49");
		names.add("+51");
		names.add("+52");
		names.add("+53");
		names.add("+54");
		names.add("+55");
		names.add("+56");
		names.add("+57");
		names.add("+58");
		names.add("+60");
		names.add("+61");
		names.add("+62");
		names.add("+63");
		names.add("+64");
		names.add("+65");
		names.add("+66");
		names.add("+81");
		names.add("+82");
		names.add("+84");
		names.add("+86");
		names.add("+90");
		names.add("+91");
		names.add("+92");
		names.add("+93");
		names.add("+94");
		names.add("+95");
		names.add("+98");
		names.add("+212");
		names.add("+213");
		names.add("+216");
		names.add("+218");
		names.add("+220");
		names.add("+221");
		names.add("+222");
		names.add("+223");
		names.add("+224");
		names.add("+225");
		names.add("+226");
		names.add("+227");
		names.add("+228");
		names.add("+229");
		names.add("+230");
		names.add("+231");
		names.add("+232");
		names.add("+233");
		names.add("+234");
		names.add("+235");
		names.add("+236");
		names.add("+237");
		names.add("+238");
		names.add("+239");
		names.add("+240");
		names.add("+241");
		names.add("+242");
		names.add("+243");
		names.add("+244");
		names.add("+245");
		names.add("+248");
		names.add("+249");
		names.add("+250");
		names.add("+251");
		names.add("+252");
		names.add("+253");
		names.add("+254");
		names.add("+255");
		names.add("+256");
		names.add("+257");
		names.add("+258");
		names.add("+260");
		names.add("+261");
		names.add("+262");
		names.add("+263");
		names.add("+264");
		names.add("+265");
		names.add("+266");
		names.add("+267");
		names.add("+268");
		names.add("+269");
		names.add("+290");
		names.add("+291");
		names.add("+297");
		names.add("+298");
		names.add("+299");
		names.add("+350");
		names.add("+351");
		names.add("+352");
		names.add("+353");
		names.add("+355");
		names.add("+356");
		names.add("+357");
		names.add("+358");
		names.add("+359");
		names.add("+370");
		names.add("+371");
		names.add("+372");
		names.add("+373");
		names.add("+374");
		names.add("+375");
		names.add("+376");
		names.add("+377");
		names.add("+378");
		names.add("+380");
		names.add("+381");
		names.add("+382");
		names.add("+385");
		names.add("+386");
		names.add("+387");
		names.add("+389");
		names.add("+420");
		names.add("+421");
		names.add("+423");
		names.add("+500");
		names.add("+501");
		names.add("+502");
		names.add("+503");
		names.add("+504");
		names.add("+505");
		names.add("+506");
		names.add("+507");
		names.add("+508");
		names.add("+509");
		names.add("+590");
		names.add("+591");
		names.add("+592");
		names.add("+593");
		names.add("+595");
		names.add("+597");
		names.add("+598");
		names.add("+670");
		names.add("+672");
		names.add("+673");
		names.add("+674");
		names.add("+675");
		names.add("+676");
		names.add("+677");
		names.add("+678");
		names.add("+679");
		names.add("+680");
		names.add("+681");
		names.add("+682");
		names.add("+683");
		names.add("+685");
		names.add("+686");
		names.add("+687");
		names.add("+688");
		names.add("+689");
		names.add("+690");
		names.add("+691");
		names.add("+692");
		names.add("+850");
		names.add("+852");
		names.add("+853");
		names.add("+855");
		names.add("+856");
		names.add("+870");
		names.add("+880");
		names.add("+886");
		names.add("+960");
		names.add("+961");
		names.add("+962");
		names.add("+963");
		names.add("+964");
		names.add("+965");
		names.add("+966");
		names.add("+967");
		names.add("+968");
		names.add("+971");
		names.add("+972");
		names.add("+973");
		names.add("+974");
		names.add("+975");
		names.add("+976");
		names.add("+977");
		names.add("+992");
		names.add("+993");
		names.add("+994");
		names.add("+995");
		names.add("+996");
		names.add("+998");
		values.add("+1");
		values.add("+7");
		values.add("+20");
		values.add("+27");
		values.add("+30");
		values.add("+31");
		values.add("+32");
		values.add("+33");
		values.add("+34");
		values.add("+36");
		values.add("+39");
		values.add("+39");
		values.add("+40");
		values.add("+41");
		values.add("+43");
		values.add("+44");
		values.add("+45");
		values.add("+46");
		values.add("+47");
		values.add("+48");
		values.add("+49");
		values.add("+51");
		values.add("+52");
		values.add("+53");
		values.add("+54");
		values.add("+55");
		values.add("+56");
		values.add("+57");
		values.add("+58");
		values.add("+60");
		values.add("+61");
		values.add("+62");
		values.add("+63");
		values.add("+64");
		values.add("+65");
		values.add("+66");
		values.add("+81");
		values.add("+82");
		values.add("+84");
		values.add("+86");
		values.add("+90");
		values.add("+91");
		values.add("+92");
		values.add("+93");
		values.add("+94");
		values.add("+95");
		values.add("+98");
		values.add("+212");
		values.add("+213");
		values.add("+216");
		values.add("+218");
		values.add("+220");
		values.add("+221");
		values.add("+222");
		values.add("+223");
		values.add("+224");
		values.add("+225");
		values.add("+226");
		values.add("+227");
		values.add("+228");
		values.add("+229");
		values.add("+230");
		values.add("+231");
		values.add("+232");
		values.add("+233");
		values.add("+234");
		values.add("+235");
		values.add("+236");
		values.add("+237");
		values.add("+238");
		values.add("+239");
		values.add("+240");
		values.add("+241");
		values.add("+242");
		values.add("+243");
		values.add("+244");
		values.add("+245");
		values.add("+248");
		values.add("+249");
		values.add("+250");
		values.add("+251");
		values.add("+252");
		values.add("+253");
		values.add("+254");
		values.add("+255");
		values.add("+256");
		values.add("+257");
		values.add("+258");
		values.add("+260");
		values.add("+261");
		values.add("+262");
		values.add("+263");
		values.add("+264");
		values.add("+265");
		values.add("+266");
		values.add("+267");
		values.add("+268");
		values.add("+269");
		values.add("+290");
		values.add("+291");
		values.add("+297");
		values.add("+298");
		values.add("+299");
		values.add("+350");
		values.add("+351");
		values.add("+352");
		values.add("+353");
		values.add("+355");
		values.add("+356");
		values.add("+357");
		values.add("+358");
		values.add("+359");
		values.add("+370");
		values.add("+371");
		values.add("+372");
		values.add("+373");
		values.add("+374");
		values.add("+375");
		values.add("+376");
		values.add("+377");
		values.add("+378");
		values.add("+380");
		values.add("+381");
		values.add("+382");
		values.add("+385");
		values.add("+386");
		values.add("+387");
		values.add("+389");
		values.add("+420");
		values.add("+421");
		values.add("+423");
		values.add("+500");
		values.add("+501");
		values.add("+502");
		values.add("+503");
		values.add("+504");
		values.add("+505");
		values.add("+506");
		values.add("+507");
		values.add("+508");
		values.add("+509");
		values.add("+590");
		values.add("+591");
		values.add("+592");
		values.add("+593");
		values.add("+595");
		values.add("+597");
		values.add("+598");
		values.add("+670");
		values.add("+672");
		values.add("+673");
		values.add("+674");
		values.add("+675");
		values.add("+676");
		values.add("+677");
		values.add("+678");
		values.add("+679");
		values.add("+680");
		values.add("+681");
		values.add("+682");
		values.add("+683");
		values.add("+685");
		values.add("+686");
		values.add("+687");
		values.add("+688");
		values.add("+689");
		values.add("+690");
		values.add("+691");
		values.add("+692");
		values.add("+850");
		values.add("+852");
		values.add("+853");
		values.add("+855");
		values.add("+856");
		values.add("+870");
		values.add("+880");
		values.add("+886");
		values.add("+960");
		values.add("+961");
		values.add("+962");
		values.add("+963");
		values.add("+964");
		values.add("+965");
		values.add("+966");
		values.add("+967");
		values.add("+968");
		values.add("+971");
		values.add("+972");
		values.add("+973");
		values.add("+974");
		values.add("+975");
		values.add("+976");
		values.add("+977");
		values.add("+992");
		values.add("+993");
		values.add("+994");
		values.add("+995");
		values.add("+996");
		values.add("+998");

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return driverListOptions;
	}

	public static String getRegionListByMulticityCountryIdOptions(List<String> selectedIdList, List<String> assignedRegionList) {

		String multicityCountryId = ProjectConstants.DEFAULT_COUNTRY_ID;

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionListByMulticityCountryId(multicityCountryId, assignedRegionList, 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
		}

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);

		return regionListOptions;
	}

	public static String getRegionListByMulticityCountryIdOptionsSingleSelect(String regionId, String multicityCountryId, List<String> assignedRegionList) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityCityRegionListByMulticityCountryId(multicityCountryId, assignedRegionList, 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, regionId + "");
		return driverListOptions;
	}

	public static String getUserAccessWiseRegionList(String regionId, String userId, String roleId, boolean allFlag) {

		String countryId = ProjectConstants.DEFAULT_COUNTRY_ID;

		List<MulticityCityRegionModel> allRegionLists;
		List<MulticityUserRegionModel> multicityRegionModels;

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (allFlag) {
			names.add("All");
			values.add("-1");
		}

		allRegionLists = MulticityCityRegionModel.getMulticityCityRegionSearch(0, 100000, "", "%%", countryId);
		if (UserRoles.SUPER_ADMIN_ROLE_ID.equals(roleId) || UserRoles.BUSINESS_OPERATOR_ROLE_ID.equals(roleId) || UserRoles.BUSINESS_OWNER_ROLE_ID.equals(roleId)) {
			for (MulticityCityRegionModel multicityCityRegionModel : allRegionLists) {
				names.add(multicityCityRegionModel.getCityDisplayName());
				values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
			}
		} else {
			multicityRegionModels = MulticityUserRegionModel.getMulticityUserRegionByUserId(userId);
			for (MulticityUserRegionModel multicityRegionModel : multicityRegionModels) {
				String regionName = "";
				for (MulticityCityRegionModel model : allRegionLists) {
					if (multicityRegionModel.getMulticityCityRegionId().equals(model.getMulticityCityRegionId())) {
						regionName = model.getCityDisplayName();
					}

				}
				names.add(regionName);
				values.add(multicityRegionModel.getMulticityCityRegionId() + "");
			}
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, regionId + "");
		return driverListOptions;
	}

	public static List<String> getUserAccessWiseRegionListDatatable(String region, String userId, String userRole) {

		String countryId = ProjectConstants.DEFAULT_COUNTRY_ID;

		List<MulticityUserRegionModel> multicityRegionModels;
		List<MulticityCityRegionModel> allRegionLists;

		ArrayList<String> regionIdsArrys = new ArrayList<String>();

		if (ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equals(region)) {

			if (UserRoles.SUPER_ADMIN_ROLE.equals(userRole) || UserRoles.BUSINESS_OWNER_ROLE.equals(userRole) || UserRoles.OPERATOR_ROLE.equals(userRole)) {

				allRegionLists = MulticityCityRegionModel.getMulticityCityRegionSearch(0, 100000, "", "%%", countryId);

				for (MulticityCityRegionModel model : allRegionLists) {
					regionIdsArrys.add(model.getMulticityCityRegionId());
				}
			} else {
				multicityRegionModels = MulticityUserRegionModel.getMulticityUserRegionByUserId(userId);

				for (MulticityUserRegionModel model : multicityRegionModels) {
					regionIdsArrys.add(model.getMulticityCityRegionId());
				}
			}
		} else {
			regionIdsArrys.add(region);
		}

		if (regionIdsArrys.size() == 0) {
			regionIdsArrys.add(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);

		}

		return regionIdsArrys;
	}

	public static String getUserAccessWiseVendorList(String vendorId, String userId, String roleId, boolean allFlag) {

		List<MulticityUserRegionModel> multicityRegionModels;
		List<UserProfileModel> allVendorLists;

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (allFlag) {
			names.add("All");
			values.add("-1");
		}

		allVendorLists = UserProfileModel.getAllUsersByRoleId(UserRoles.VENDOR_ROLE_ID);
		if (UserRoles.SUPER_ADMIN_ROLE_ID.equals(roleId) || UserRoles.BUSINESS_OPERATOR_ROLE_ID.equals(roleId) || UserRoles.BUSINESS_OWNER_ROLE_ID.equals(roleId)) {
			for (UserProfileModel userProfileModel : allVendorLists) {
				names.add(userProfileModel.getFullName());
				values.add(userProfileModel.getUserId());
			}
		} else {
			// get all the regions assigned to the userId
			multicityRegionModels = MulticityUserRegionModel.getMulticityUserRegionByUserId(userId);
			List<String> assignedRegionList = new ArrayList<String>();
			for (MulticityUserRegionModel string : multicityRegionModels) {
				assignedRegionList.add(string.getMulticityCityRegionId());
			}

			if (assignedRegionList.size() > 0) {
				allVendorLists = UserProfileModel.getVendorListByRegion(UserRoles.VENDOR_ROLE_ID, assignedRegionList);
			}

			for (UserProfileModel userProfileModel : allVendorLists) {
				names.add(userProfileModel.getFullName());
				values.add(userProfileModel.getUserId());
			}
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, vendorId + "");
		return driverListOptions;
	}

	public static String[] getUserAccessWiseVendorListDatatable(String vendorId, String userId, String userRole) {

		List<MulticityUserRegionModel> multicityRegionModels;
		List<UserProfileModel> allVendorLists = null;

		ArrayList<String> vendorIdsArrys = new ArrayList<String>();

		if (vendorId == null || "".equalsIgnoreCase(vendorId) || "-1".equals(vendorId)) {

			if (UserRoles.SUPER_ADMIN_ROLE.equals(userRole) || UserRoles.BUSINESS_OWNER_ROLE.equals(userRole) || UserRoles.OPERATOR_ROLE.equals(userRole)) {

				allVendorLists = UserProfileModel.getAllUsersByRoleId(UserRoles.VENDOR_ROLE_ID);

				for (UserProfileModel model : allVendorLists) {
					vendorIdsArrys.add(model.getUserId());
				}
			} else {
				multicityRegionModels = MulticityUserRegionModel.getMulticityUserRegionByUserId(userId);
				List<String> assignedRegionList = new ArrayList<String>();
				for (MulticityUserRegionModel string : multicityRegionModels) {
					assignedRegionList.add(string.getMulticityCityRegionId());
				}

				if (assignedRegionList.size() > 0) {
					allVendorLists = UserProfileModel.getVendorListByRegion(UserRoles.VENDOR_ROLE_ID, assignedRegionList);
					for (UserProfileModel userProfileModel : allVendorLists) {
						vendorIdsArrys.add(userProfileModel.getUserId());
					}
				}
			}
		} else {
			vendorIdsArrys.add(vendorId);
		}

		if (vendorIdsArrys.size() == 0) {
			vendorIdsArrys.add("-1");
		}

		String[] vendorIds = new String[vendorIdsArrys.size()];
		vendorIds = vendorIdsArrys.toArray(vendorIds);

		if (vendorIdsArrys.size() == 0) {
			vendorIds = null;
		}
		return vendorIds;
	}

	public static String getAllRegionListOptions(List<String> selectedIdList) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityRegionDetails();

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
		}

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);

		return regionListOptions;
	}

	public static String getAllRegionListWithDisabledOptions(List<String> selectedIdList) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = MulticityCityRegionModel.getMulticityRegionDetails();

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (MulticityCityRegionModel multicityCityRegionModel : multicityCityRegionModelList) {

			names.add(multicityCityRegionModel.getCityDisplayName());
			values.add(multicityCityRegionModel.getMulticityCityRegionId() + "");
		}

		String regionListOptions = ComboUtils.getOptionArrayForListBoxForDisableMultiSelect(names, values, selectedIdList);

		return regionListOptions;
	}

	public static String getAdminSmsSettingsPassengerOptions(List<String> selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelBookingAcceptedByDriver"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_1.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelDriverArrivedAndWaiting"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_2.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelBookingCancelledByDriver"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_3.toString());

//		names.add(BusinessAction.messageForKeyAdmin("labelBookingCancelledByAdmin"));
//		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_4.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelInvoiceAlert"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_5.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelBookedByAdmin"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_6.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelCreditRefundFromAdmin"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_7.toString());

//		names.add(BusinessAction.messageForKeyAdmin("labelCreditUpdateFromAdmin"));
//		values.add(ADMIN_SMS_SETTINGS_ENUM.PASSENGER_8.toString());

		String driverListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedId);
		return driverListOptions;
	}

	public static String getAdminSmsSettingsDriverOptions(List<String> selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelBookingRequestReceived"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.DRIVER_1.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelBookingCancelledByPassenger"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.DRIVER_2.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelPaymentReceivedForSMSSetting"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.DRIVER_3.toString());

		String driverListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedId);
		return driverListOptions;
	}

	public static String getAdminSmsSettingsAdminOptions(List<String> selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelBookingAcceptedByDriver"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.BO_1.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelDriverArrivedAndWaiting"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.BO_2.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelBookingCancelledByDriver"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.BO_3.toString());

		names.add(BusinessAction.messageForKeyAdmin("labelInvoiceAlert"));
		values.add(ADMIN_SMS_SETTINGS_ENUM.BO_4.toString());

		String driverListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedId);
		return driverListOptions;
	}

	public static String getProductCategoryOption(String selectedId) {

		List<ProductCategoryModel> productCategoryList = ProductCategoryModel.getProductCategorySearch(0, 0, "%%", 0, 0);

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (ProductCategoryModel productCategoryModel : productCategoryList) {
			names.add(productCategoryModel.getProductCategoryName());
			values.add(productCategoryModel.getProductCategoryId());
		}

		return ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
	}

	public static String getProductQuantityTypeOption(String selectedValue) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		values.add(ProjectConstants.QuantityTypeConstants.PIECES);
		names.add(ProjectConstants.QuantityTypeConstants.PIECES_OPTION);

		values.add(ProjectConstants.QuantityTypeConstants.LOOSE);
		names.add(ProjectConstants.QuantityTypeConstants.LOOSE_OPTION);

		values.add(ProjectConstants.QuantityTypeConstants.BUNDLE);
		names.add(ProjectConstants.QuantityTypeConstants.BUNDLE_OPTION);

		String driverListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedValue + "");
		return driverListOptions;
	}

	public static String getProductCategoryListOption(List<String> vPCAssocList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<ProductCategoryModel> productCategoryList = ProductCategoryModel.getProductCategorySearch(0, 0, "%%", 0, 0);

		for (ProductCategoryModel productCategoryModel : productCategoryList) {
			names.add(productCategoryModel.getProductCategoryName());
			values.add(productCategoryModel.getProductCategoryId());
		}

		String productCategoryListOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, vPCAssocList);
		return productCategoryListOptions;
	}

	public static String getProductCategoryAssocListOption(List<String> productCategoryIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Select Product Category");
		values.add("-1");

		List<ProductCategoryModel> productCategoryModels = ProductCategoryModel.getProductCategoryListByProductCategoryId(productCategoryIdList);

		for (ProductCategoryModel productCategoryModel : productCategoryModels) {
			names.add(productCategoryModel.getProductCategoryName());
			values.add(productCategoryModel.getProductCategoryId());
		}
		String productCategoryListOptions = ComboUtils.getOptionArrayForListBox(names, values, "");
		return productCategoryListOptions;
	}

	public static String getProductCategoryAssocListOption(List<String> productCategoryIdList, String selectedValue) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		if ("-1".equals(selectedValue) || selectedValue == null || selectedValue.isEmpty()) {
			names.add("Select Product Category");
			values.add("-1");
		}

		
		List<ProductCategoryModel> productCategoryModels = ProductCategoryModel.getProductCategoryListByProductCategoryId(productCategoryIdList);

		for (ProductCategoryModel productCategoryModel : productCategoryModels) {
			names.add(productCategoryModel.getProductCategoryName());
			values.add(productCategoryModel.getProductCategoryId());
		}

		String productCategoryListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedValue);
		
		return productCategoryListOptions;

	}

	public static String getProductSubCategoryListOptionsByProductCategoryId(String productCategoryId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<ProductSubCategoryModel> productSubCategoryModels = ProductSubCategoryModel.getProductSubCategoryListByProductCategoryId(productCategoryId);

		names.add("Select Product Sub Category");
		values.add("-1");

		for (ProductSubCategoryModel productSubCategoryModel : productSubCategoryModels) {
			names.add(productSubCategoryModel.getProductSubCategoryName());
			values.add(productSubCategoryModel.getProductSubCategoryId());
		}
		String productSubCategoryListOptions = ComboUtils.getOptionArrayForListBox(names, values, "");
		return productSubCategoryListOptions;

	}

	public static String getProductSubCategoryListOptions(String productCategoryId, String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<ProductSubCategoryModel> productSubCategoryModels = ProductSubCategoryModel.getProductSubCategoryListByProductCategoryId(productCategoryId);

		names.add("Select Product Sub Category");
		values.add("-1");

		for (ProductSubCategoryModel productSubCategoryModel : productSubCategoryModels) {
			names.add(productSubCategoryModel.getProductSubCategoryName());
			values.add(productSubCategoryModel.getProductSubCategoryId());
		}
		String productSubCategoryListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return productSubCategoryListOptions;

	}

	public static String getNumberOptions(String selectedValue, int minValue, int maxValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		for (int i = minValue; i <= maxValue; i++) {
			statusName.add(i + "");
			statusValues.add(i + "");
		}

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}

	public static String getTypeOption(List<String> type) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		String[] selectedValues = String.join(",", type).split(",");

		statusValues.add(StringUtils.valueOf(ProjectConstants.TypeConstants.STORE_ID));
		statusName.add(ProjectConstants.TypeConstants.STORE);

		statusValues.add(StringUtils.valueOf(ProjectConstants.TypeConstants.WAREHOUSE_ID));
		statusName.add(ProjectConstants.TypeConstants.WAREHOUSE);

		String typeListOptions = ComboUtils.getOptionArrayForListBoxForView(statusName, statusValues, selectedValues);
		return typeListOptions;
	}

	public static String getBrandFilterListOptions(List<String> userIdList, String selectedId, String approvedBrands) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<BrandModel> brandModels = BrandModel.getBrandSearch(0, 0, "%%", 0, 0, userIdList, "1", approvedBrands);

		for (BrandModel brandModel : brandModels) {
			names.add(brandModel.getBrandName());
			values.add(brandModel.getBrandId());
		}
		String brandListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return brandListOptions;
	}

	public static String getUOMListOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<UnitOfMeasureModel> uomModels = UnitOfMeasureModel.getuomList();

		for (UnitOfMeasureModel uomModel : uomModels) {
			names.add(uomModel.getUomName());
			values.add(String.valueOf(uomModel.getUomId()));
		}

		String uomListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);

		return uomListOptions;
	}
	
	public static String getUOMMultiSelectOptions(String[] selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<UnitOfMeasureModel> uomModels = UnitOfMeasureModel.getuomList();

		for (UnitOfMeasureModel uomModel : uomModels) {
			names.add(uomModel.getUomName());
			values.add(String.valueOf(uomModel.getUomId()));
		}

		String uomListOptions = ComboUtils.getOptionArrayForListBoxForView(names, values, selectedId);

		return uomListOptions;
	}

	public static String getProductCategoryListOption(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<ProductCategoryModel> productCategoryList = ProductCategoryModel.getProductCategorySearch(0, 0, "%%", 0, 0);

		for (ProductCategoryModel productCategoryModel : productCategoryList) {
			names.add(productCategoryModel.getProductCategoryName());
			values.add(productCategoryModel.getProductCategoryId());
		}

		String productCategoryListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return productCategoryListOptions;
	}

	public static String getProductTemplateFilterListOptions(List<String> userIdList, String selectedId, String brandId, String productCategoryId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<ProductTemplateModel> productTemplateModels = ProductTemplateModel.getProductTemplateList(userIdList, brandId, productCategoryId);

		for (ProductTemplateModel productTemplateModel : productTemplateModels) {

			names.add(productTemplateModel.getBrandName() + "-" + productTemplateModel.getProductName() + "-" + productTemplateModel.getUomName());
			values.add(productTemplateModel.getProductTemplateId());
		}
		String productTemplateListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return productTemplateListOptions;
	}

	public static String getProductVariantFilterListOptions(List<String> userIdList, String selectedId, String productTemplateId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		List<ProductVariantModel> productVariantModels = ProductVariantModel.getProductVariantList(userIdList, productTemplateId);

		for (ProductVariantModel productVariantModel : productVariantModels) {

			names.add(productVariantModel.getProductName() + "-" + productVariantModel.getProductVariantName() + "-" + productVariantModel.getWeight() + " " + productVariantModel.getUomName());
			values.add(productVariantModel.getProductVariantId());
		}
		String productVariantListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return productVariantListOptions;
	}

	public static String getBrandFilterListOptions(List<String> userIdList, String selectedId, List<BrandModel> brandModels) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (BrandModel brandModel : brandModels) {
			names.add(brandModel.getBrandName());
			values.add(brandModel.getBrandId());
		}
		String brandListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return brandListOptions;
	}

	public static String getProductTemplateFilterListOptions(List<String> userIdList, String selectedId, List<ProductTemplateModel> productTemplateModels) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (ProductTemplateModel productTemplateModel : productTemplateModels) {

			names.add(productTemplateModel.getBrandName() + "-" + productTemplateModel.getProductName() + "-" + productTemplateModel.getUomName());
			values.add(productTemplateModel.getProductTemplateId());
		}
		String productTemplateListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return productTemplateListOptions;
	}

	public static String getProductVariantFilterListOptionsForOldData(List<String> userIdList, String selectedId, List<ProductVariantModel> productVariantModels) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (ProductVariantModel productVariantModel : productVariantModels) {

			names.add(productVariantModel.getProductName() + "-" + productVariantModel.getProductVariantName() + "-" + productVariantModel.getWeight() + " " + productVariantModel.getUomName());
			values.add(productVariantModel.getProductVariantId());
		}
		String productVariantListOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return productVariantListOptions;
	}

	public static String getDriverProcessingViaOptions(String selectedValue) {

		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusName.add(BusinessAction.messageForKeyAdmin("labelThreadBased"));
		statusValues.add(StringUtils.valueOf(ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID));

		statusName.add(BusinessAction.messageForKeyAdmin("labelCronJobBased"));
		statusValues.add(StringUtils.valueOf(ProjectConstants.DRIVER_PROCESSING_VIA_CRON_JOB_BASED_ID));

		String driverListOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedValue + "");
		return driverListOptions;
	}
	
	public static String getCategoryGroupOptions(List<CategoryGroupModel> categoryGroups, String selectedId) {
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		for (CategoryGroupModel categoryGroupModel : categoryGroups) {
			names.add(categoryGroupModel.getGroupName());
			values.add(categoryGroupModel.get_id());
		}
		
		String categoryGorupOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return categoryGorupOptions;
	}
	
	public static String getCityOptions(List<CityModel> cityList, String selectedId, String AllCityOption) {
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		if (AllCityOption != null) {
			names.add(AllCityOption);
			values.add(AllCityOption);
		}
		
		for (CityModel cityModel : cityList) {
			names.add(cityModel.getCityName());
			values.add(cityModel.get_id());
		}
		
		String cityOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return cityOptions;
	}

	public static String getStoreCategoryListOption(List<StoreCategorieModel> storeCategoryList) {
		
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		for (StoreCategorieModel storeCategoryModel : storeCategoryList) {
			names.add(storeCategoryModel.getCategoryName());
			values.add(storeCategoryModel.get_id());
		}
		
		String storeCategoryOptions = ComboUtils.getOptionArrayForListBox(names, values, "");
		return storeCategoryOptions;
	}

	public static String getLocationListOption(List<LocationModel> locationList, boolean allOption, String selected) {
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		if (allOption) {
			names.add("All Locations");
			values.add("All Locations");
		}
		
		for (LocationModel locationModel : locationList) {
			names.add(locationModel.getLocationName());
			values.add(locationModel.get_id());
		}
		
		String locationsOptions = ComboUtils.getOptionArrayForListBox(names, values, selected);
		return locationsOptions;
	}

	public static String getResolutionOptions(List<ResolutionModel> resolutionList, String selected) {
		
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		for (ResolutionModel resolutionModel : resolutionList) {
			names.add(resolutionModel.getResolution()+"-"+resolutionModel.getAspectRatio());
			values.add(resolutionModel.get_id());
		}
		
		String locationsOptions = ComboUtils.getOptionArrayForListBox(names, values, "");
		return locationsOptions;
		
	}

	public static String getMediaTypeOptions(String selectedId) {
		
		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		statusValues.add(ProjectConstants.Image);
		statusName.add(ProjectConstants.Image);

		statusValues.add(ProjectConstants.Video);
		statusName.add(ProjectConstants.Video);

		String mediaTypeOptions = ComboUtils.getOptionArrayForListBox(statusName, statusValues, selectedId + "");
		return mediaTypeOptions;
	}
	
	public static String getAdOptions(List<AdModel> adList, List<String> selectedIdList) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		for (AdModel adModel : adList) {
			names.add(adModel.getAdTitle());
			values.add(adModel.get_id());
		}

		String locationsOptions = ComboUtils.getOptionArrayForListBoxForMultiSelect(names, values, selectedIdList);
		return locationsOptions;

	}

	public static String getTargetSpecificOptions(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("All");
		values.add("All");
		
		names.add("City");
		values.add("City");
		
		names.add("Location");
		values.add("Location");
		
		names.add("Store Category");
		values.add("Store Category");
		
		names.add("Store");
		values.add("Store");
		
		names.add("Device");
		values.add("Device");

		String targetSpecificOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return targetSpecificOptions;
	}

	public static String getStoreListOption(List<StoreModel> storeList, String allStoresOption, String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (allStoresOption != null) {
			names.add(allStoresOption);
			values.add(allStoresOption);
		}

		for (StoreModel storeModel : storeList) {
			names.add(storeModel.getStoreName());
			values.add(storeModel.get_id());
		}

		String storeOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return storeOptions;
	}

	public static String getDeviceListOption(List<LEDDeviceModel> deviceList, String allDevicesOption, String selectedId) {
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		if (allDevicesOption != null) {
			names.add(allDevicesOption);
			values.add(allDevicesOption);
		}

		for (LEDDeviceModel ledDeviceModel : deviceList) {
			names.add(ledDeviceModel.getDeviceId());
			values.add(ledDeviceModel.get_id());
		}

		String deviceOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return deviceOptions;
	}
	
	public static String getRackCategoryIdOption(String selectedId) {
		
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		List<WhmgmtRackCategoryModel> rackCategoryList = WhmgmtRackCategoryModel.getRackCategoryList();
		
		for (WhmgmtRackCategoryModel whmgmtRackCategoryModel : rackCategoryList) {
			names.add(whmgmtRackCategoryModel.getCategoryName()+" ["+whmgmtRackCategoryModel.getNoOfSlots()+"]");
			values.add(whmgmtRackCategoryModel.getCategoryId());
		}
		
		String rackCategoryOptions = ComboUtils.getOptionArrayForListBox(names, values, selectedId);
		return rackCategoryOptions;
	}
	
	public static String getPhonepeStatusFilterList(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add(BusinessAction.messageForKeyAdmin("labelAll"));
		values.add(ProjectConstants.ALL_USERS_ID);

		names.add("PAYMENT_PENDING");
		values.add("2");

		names.add("PAYMENT_SUCCESS");
		values.add("3");

		String phonepeStatusList = ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
		return phonepeStatusList;
	}

	public static String getERPBrandsFilterListOptions(List<String> selectedIdList, String vendorId, List<String> assignedRegionList) {
		
		List<String> statusName = new ArrayList<String>();
		List<String> statusValues = new ArrayList<String>();

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getERPBrandList(assignedRegionList, vendorId);

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {
			statusValues.add(vendorStoreModel.getVendorStoreId());
			statusName.add(vendorStoreModel.getStoreName());
		}

		return ComboUtils.getOptionArrayForListBoxForMultiSelect(statusName, statusValues, selectedIdList);
	}
	
	public static String getServiceTypeOptions1(String selectedId) {

		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		names.add("Car");
		values.add(ProjectConstants.SERVICE_TYPE_CAR_ID);

		names.add("Driver");
		values.add(ProjectConstants.SERVICE_TYPE_DRIVER_ID);
		
	    names.add("Courier");
		values.add(ProjectConstants.SERVICE_TYPE_COURIER_ID);
		
		names.add("Delivery");
		values.add(ProjectConstants.SERVICE_TYPE_DELIVERY_ID);
		

		return ComboUtils.getOptionArrayForListBox(names, values, selectedId + "");
	}
}
