package com.webapp.actions.secure.vendor.stores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.VendorStoreTimingModel;

@Path("/add-vendor-store")
public class AddVendorStoreAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddVendorStore(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.STORE_IMAGE_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		
		List<String> typeList = new ArrayList<>();
		typeList.add(String.valueOf(ProjectConstants.TypeConstants.STORE_ID));

		String typeOptions = DropDownUtils.getTypeOption(typeList);
		data.put(FieldConstants.TYPE_OPTIONS, typeOptions);

		String numberOfShiftsOptions = DropDownUtils.getNumberOfShiftsOption("1");
		data.put("numberOfShiftsOptions", numberOfShiftsOptions);

		String shiftTypeOptions = DropDownUtils.getShiftTypeOption(ProjectConstants.DAY_SHIFT_ID);
		data.put("shiftTypeOptions", shiftTypeOptions);

		String dateTypeOptions = DropDownUtils.getDateTypeOption(ProjectConstants.VENDOR_STORE_TIME_DAY_OF_WEEK + "");
		data.put("dateTypeOptions", dateTypeOptions);

		String isClosedTodayOptions = DropDownUtils.getYesNoOption(false + "");
		data.put("isClosedTodayOptions", isClosedTodayOptions);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String multicityCityRegionIdOptions = DropDownUtils.getRegionListByMulticityCountryIdOptionsSingleSelect(null, ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		String ledDeviceForStoreOptions = DropDownUtils.getYesNoOption(ProjectConstants.YES);
		data.put("ledDeviceForStoreOptions", ledDeviceForStoreOptions);
		
		String rackCategoryIdOptions = DropDownUtils.getRackCategoryIdOption("");
		data.put("rackCategoryIdOptions", rackCategoryIdOptions);
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);
		} else {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + vendorId);
		}

		return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_STORE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postAddVendorStore(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.STORE_NAME) String storeName,
		@FormParam(FieldConstants.STORE_ADDRESS) String storeAddress,
		@FormParam(FieldConstants.STORE_ADDRESS_LAT) String storeAddressLat,
		@FormParam(FieldConstants.STORE_ADDRESS_LNG) String storeAddressLng,
		@FormParam(FieldConstants.STORE_PLACE_ID) String storePlaceId,
		@FormParam(FieldConstants.STORE_IMAGE_HIDDEN) String hiddenStoreImage,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId,
		@FormParam(FieldConstants.LED_DEVICE_FOR_STORE) String ledDeviceForStore,
		@FormParam(FieldConstants.LED_DEVICE_COUNT) String ledDeviceCount,
		@FormParam(FieldConstants.TYPE) List<String> typeList,
		@FormParam(FieldConstants.NUMBER_OF_RACKS) String numberOfRacks,
		@FormParam(FieldConstants.RACK_CATEGORY_ID) String rackCategoryId,
		@FormParam(FieldConstants.PHONEPE_STORE_ID) String phonepeStoreId,
		
		@FormParam("isClosedToday") String isClosedToday,
		@FormParam("numberOfShifts") String numberOfShifts,
		@FormParam("shiftType") String shiftType,
		
		@FormParam("dateType") String dateType,
		@FormParam("fromDate") String fromDate,
		@FormParam("toDate") String toDate,
		@FormParam("dateOpeningMorningHours") String dateOpeningMorningHours,
		@FormParam("dateClosingMorningHours") String dateClosingMorningHours,
		@FormParam("dateOpeningEveningHours") String dateOpeningEveningHours,
		@FormParam("dateClosingEveningHours") String dateClosingEveningHours,
		
		@FormParam("daysOfWeek0") String daysOfWeek0,
		@FormParam("daysOfWeek1") String daysOfWeek1,
		@FormParam("daysOfWeek2") String daysOfWeek2,
		@FormParam("daysOfWeek3") String daysOfWeek3,
		@FormParam("daysOfWeek4") String daysOfWeek4,
		@FormParam("daysOfWeek5") String daysOfWeek5,
		@FormParam("daysOfWeek6") String daysOfWeek6,
		
		@FormParam("dateOpeningMorningHours0") String dateOpeningMorningHours0,
		@FormParam("dateClosingMorningHours0") String dateClosingMorningHours0,
		@FormParam("dateOpeningEveningHours0") String dateOpeningEveningHours0,
		@FormParam("dateClosingEveningHours0") String dateClosingEveningHours0,
		
		@FormParam("dateOpeningMorningHours1") String dateOpeningMorningHours1,
		@FormParam("dateClosingMorningHours1") String dateClosingMorningHours1,
		@FormParam("dateOpeningEveningHours1") String dateOpeningEveningHours1,
		@FormParam("dateClosingEveningHours1") String dateClosingEveningHours1,
		
		@FormParam("dateOpeningMorningHours2") String dateOpeningMorningHours2,
		@FormParam("dateClosingMorningHours2") String dateClosingMorningHours2,
		@FormParam("dateOpeningEveningHours2") String dateOpeningEveningHours2,
		@FormParam("dateClosingEveningHours2") String dateClosingEveningHours2,
		
		@FormParam("dateOpeningMorningHours3") String dateOpeningMorningHours3,
		@FormParam("dateClosingMorningHours3") String dateClosingMorningHours3,
		@FormParam("dateOpeningEveningHours3") String dateOpeningEveningHours3,
		@FormParam("dateClosingEveningHours3") String dateClosingEveningHours3,
		
		@FormParam("dateOpeningMorningHours4") String dateOpeningMorningHours4,
		@FormParam("dateClosingMorningHours4") String dateClosingMorningHours4,
		@FormParam("dateOpeningEveningHours4") String dateOpeningEveningHours4,
		@FormParam("dateClosingEveningHours4") String dateClosingEveningHours4,
		
		@FormParam("dateOpeningMorningHours5") String dateOpeningMorningHours5,
		@FormParam("dateClosingMorningHours5") String dateClosingMorningHours5,
		@FormParam("dateOpeningEveningHours5") String dateOpeningEveningHours5,
		@FormParam("dateClosingEveningHours5") String dateClosingEveningHours5,
		
		@FormParam("dateOpeningMorningHours6") String dateOpeningMorningHours6,
		@FormParam("dateClosingMorningHours6") String dateClosingMorningHours6,
		@FormParam("dateOpeningEveningHours6") String dateOpeningEveningHours6,
		@FormParam("dateClosingEveningHours6") String dateClosingEveningHours6
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.STORE_NAME, storeName);
		data.put(FieldConstants.STORE_ADDRESS, storeAddress);
		data.put(FieldConstants.STORE_ADDRESS_LAT, storeAddressLat);
		data.put(FieldConstants.STORE_ADDRESS_LNG, storeAddressLng);
		data.put(FieldConstants.STORE_PLACE_ID, storePlaceId);
		data.put(FieldConstants.STORE_IMAGE_HIDDEN, hiddenStoreImage);
		data.put(FieldConstants.STORE_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenStoreImage) ? hiddenStoreImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.LED_DEVICE_FOR_STORE, ledDeviceForStore);
		data.put(FieldConstants.LED_DEVICE_COUNT, ledDeviceCount);
		data.put(FieldConstants.PHONEPE_STORE_ID, phonepeStoreId);

		data.put("fromDate", fromDate);
		data.put("toDate", toDate);
		data.put("dateOpeningMorningHours", dateOpeningMorningHours);
		data.put("dateClosingMorningHours", dateClosingMorningHours);
		data.put("dateOpeningEveningHours", dateOpeningEveningHours);
		data.put("dateClosingEveningHours", dateClosingEveningHours);

		data.put("daysOfWeek0", daysOfWeek0);
		data.put("daysOfWeek1", daysOfWeek1);
		data.put("daysOfWeek2", daysOfWeek2);
		data.put("daysOfWeek3", daysOfWeek3);
		data.put("daysOfWeek4", daysOfWeek4);
		data.put("daysOfWeek5", daysOfWeek5);
		data.put("daysOfWeek6", daysOfWeek6);

		data.put("dateOpeningMorningHours0", dateOpeningMorningHours0);
		data.put("dateClosingMorningHours0", dateClosingMorningHours0);
		data.put("dateOpeningEveningHours0", dateOpeningEveningHours0);
		data.put("dateClosingEveningHours0", dateClosingEveningHours0);

		data.put("dateOpeningMorningHours1", dateOpeningMorningHours1);
		data.put("dateClosingMorningHours1", dateClosingMorningHours1);
		data.put("dateOpeningEveningHours1", dateOpeningEveningHours1);
		data.put("dateClosingEveningHours1", dateClosingEveningHours1);

		data.put("dateOpeningMorningHours2", dateOpeningMorningHours2);
		data.put("dateClosingMorningHours2", dateClosingMorningHours2);
		data.put("dateOpeningEveningHours2", dateOpeningEveningHours2);
		data.put("dateClosingEveningHours2", dateClosingEveningHours2);

		data.put("dateOpeningMorningHours3", dateOpeningMorningHours3);
		data.put("dateClosingMorningHours3", dateClosingMorningHours3);
		data.put("dateOpeningEveningHours3", dateOpeningEveningHours3);
		data.put("dateClosingEveningHours3", dateClosingEveningHours3);

		data.put("dateOpeningMorningHours4", dateOpeningMorningHours4);
		data.put("dateClosingMorningHours4", dateClosingMorningHours4);
		data.put("dateOpeningEveningHours4", dateOpeningEveningHours4);
		data.put("dateClosingEveningHours4", dateClosingEveningHours4);

		data.put("dateOpeningMorningHours5", dateOpeningMorningHours5);
		data.put("dateClosingMorningHours5", dateClosingMorningHours5);
		data.put("dateOpeningEveningHours5", dateOpeningEveningHours5);
		data.put("dateClosingEveningHours5", dateClosingEveningHours5);

		data.put("dateOpeningMorningHours6", dateOpeningMorningHours6);
		data.put("dateClosingMorningHours6", dateClosingMorningHours6);
		data.put("dateOpeningEveningHours6", dateOpeningEveningHours6);
		data.put("dateClosingEveningHours6", dateClosingEveningHours6);

		String dateTypeOptions = DropDownUtils.getDateTypeOption(dateType);
		data.put("dateTypeOptions", dateTypeOptions);

		String numberOfShiftsOptions = DropDownUtils.getNumberOfShiftsOption(numberOfShifts);
		data.put("numberOfShiftsOptions", numberOfShiftsOptions);

		int numberOfShiftsLong = Integer.parseInt(numberOfShifts);

		String shiftTypeOptions = DropDownUtils.getShiftTypeOption(Integer.parseInt(shiftType));
		data.put("shiftTypeOptions", shiftTypeOptions);

		String isClosedTodayOptions = DropDownUtils.getYesNoOption(isClosedToday);
		data.put("isClosedTodayOptions", isClosedTodayOptions);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String multicityCityRegionIdOptions = DropDownUtils.getRegionListByMulticityCountryIdOptionsSingleSelect(multicityCityRegionId, ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);
		
		String ledDeviceForStoreOptions = DropDownUtils.getYesNoOption(ledDeviceForStore);
		data.put("ledDeviceForStoreOptions", ledDeviceForStoreOptions);

		data.put(FieldConstants.TYPE, "");

		String typeOptions = DropDownUtils.getTypeOption(typeList);
		data.put(FieldConstants.TYPE_OPTIONS, typeOptions);

		data.put(FieldConstants.NUMBER_OF_RACKS, numberOfRacks);
		
		if (!rackCategoryId.isEmpty()) {
			String rackCategoryIdOptions = DropDownUtils.getRackCategoryIdOption(rackCategoryId);
			data.put("rackCategoryIdOptions", rackCategoryIdOptions);
		}

		boolean hasErrors = hasErrorsForEnglish(typeList);

		data.put("daysOfWeekOrSpecificSpan", "");
		
		if (typeList.size() > 0) {
			if (typeList.contains(String.valueOf(ProjectConstants.TypeConstants.WAREHOUSE_ID))) {
				if (numberOfRacks.isEmpty()) {
					hasErrors = true;
					data.put("numberOfRacksError", messageForKeyAdmin("errorNumberOfRacksRequired"));
				}
			}
		}

		if (dateType.equalsIgnoreCase(ProjectConstants.VENDOR_STORE_TIME_SPECIFIC_DATE + "")) {

			if (fromDate == null || "".equals(fromDate.trim()) || toDate == null || "".equals(toDate.trim())) {

				hasErrors = true;
				data.put("daysOfWeekOrSpecificSpan", messageForKeyAdmin("errorDateRangeRequired"));

			} else {

				long fromDateMillies = MyHubUtils.convertSecondsToMillis(fromDate);
				long toDateMillies = MyHubUtils.convertSecondsToMillis(toDate);

				if (toDateMillies < fromDateMillies) {

					hasErrors = true;
					data.put("daysOfWeekOrSpecificSpan", messageForKeyAdmin("errorInvalidDateRange"));
				}
			}

			if (!hasErrors) {

				String errorMessage = validateMorningEveningSlot(dateOpeningMorningHours, dateClosingMorningHours, dateOpeningEveningHours, dateClosingEveningHours, numberOfShiftsLong, shiftType);

				if ((errorMessage != null) && (!"".equals(errorMessage))) {

					hasErrors = true;
					data.put("daysOfWeekOrSpecificSpan", errorMessage);
				}
			}

		} else if (dateType.equalsIgnoreCase(ProjectConstants.VENDOR_STORE_TIME_DAY_OF_WEEK + "")) {

			boolean isAnySingleDaySelected = false;

			boolean isSunTimeInvalid = false;
			boolean isMonTimeInvalid = false;
			boolean isTueTimeInvalid = false;
			boolean isWedTimeInvalid = false;
			boolean isThuTimeInvalid = false;
			boolean isFriTimeInvalid = false;
			boolean isSatTimeInvalid = false;

			String errorMessage = null;

			if (daysOfWeek0 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours0, dateClosingMorningHours0, dateOpeningEveningHours0, dateClosingEveningHours0, numberOfShiftsLong, shiftType);
			}

			if (daysOfWeek1 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours1, dateClosingMorningHours1, dateOpeningEveningHours1, dateClosingEveningHours1, numberOfShiftsLong, shiftType);
			}

			if (daysOfWeek2 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours2, dateClosingMorningHours2, dateOpeningEveningHours2, dateClosingEveningHours2, numberOfShiftsLong, shiftType);
			}

			if (daysOfWeek3 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours3, dateClosingMorningHours3, dateOpeningEveningHours3, dateClosingEveningHours3, numberOfShiftsLong, shiftType);
			}

			if (daysOfWeek4 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours4, dateClosingMorningHours4, dateOpeningEveningHours4, dateClosingEveningHours4, numberOfShiftsLong, shiftType);
			}

			if (daysOfWeek5 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours5, dateClosingMorningHours5, dateOpeningEveningHours5, dateClosingEveningHours5, numberOfShiftsLong, shiftType);
			}

			if (daysOfWeek6 != null) {

				isAnySingleDaySelected = true;
				errorMessage = validateMorningEveningSlot(dateOpeningMorningHours6, dateClosingMorningHours6, dateOpeningEveningHours6, dateClosingEveningHours6, numberOfShiftsLong, shiftType);
			}

			if (!isAnySingleDaySelected) {

				hasErrors = true;
				data.put("daysOfWeekOrSpecificSpan", messageForKeyAdmin("errorSelectAtLeastSingleDay"));
			}

			if (isSunTimeInvalid || isMonTimeInvalid || isTueTimeInvalid || isWedTimeInvalid || isThuTimeInvalid || isFriTimeInvalid || isSatTimeInvalid) {

				hasErrors = true;
				data.put("daysOfWeekOrSpecificSpan", messageForKeyAdmin("errorClosingTimeNotLess"));
			}

			if ((errorMessage != null) && (!"".equals(errorMessage))) {

				hasErrors = true;
				data.put("daysOfWeekOrSpecificSpan", errorMessage);
			}
		}

		if (hasErrors) {

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);
			} else {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + vendorId);
			}

			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_STORE_JSP);
		}

		VendorStoreModel vsm = new VendorStoreModel();
		vsm.setVendorId(vendorId);
		vsm.setStoreName(storeName);
		vsm.setStoreAddress(storeAddress);
		vsm.setStoreAddressLat(storeAddressLat);
		vsm.setStoreAddressLng(storeAddressLng);
		vsm.setStorePlaceId(storePlaceId);
		vsm.setStoreImage(hiddenStoreImage);
		vsm.setClosedToday(Boolean.parseBoolean(isClosedToday));
		vsm.setNumberOfShifts(numberOfShiftsLong);
		vsm.setShiftType(Integer.parseInt(shiftType));
		vsm.setDateType(Integer.parseInt(dateType));
		vsm.setMulticityCityRegionId(multicityCityRegionId);
		vsm.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		vsm.setLedDeviceForStore(Boolean.parseBoolean(ledDeviceForStore));
		vsm.setLedDeviceCount(  !"".equalsIgnoreCase(ledDeviceCount) ?  Integer.parseInt(ledDeviceCount) : 0);
		vsm.setType(String.join(",", typeList));
		if (!typeList.contains(String.valueOf(ProjectConstants.TypeConstants.WAREHOUSE_ID))) {
			vsm.setNumberOfRacks(0);
		} else
			vsm.setNumberOfRacks(Integer.parseInt(numberOfRacks));
		
		vsm.setRackCategoryId(rackCategoryId);
		vsm.setPhonepeStoreId(phonepeStoreId);

		if (dateType.equalsIgnoreCase(ProjectConstants.VENDOR_STORE_TIME_SPECIFIC_DATE + "")) {

			vsm.setStartDate(MyHubUtils.convertSecondsToMillis(fromDate));
			vsm.setEndDate(MyHubUtils.convertSecondsToMillis(toDate));
			vsm.setDateOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours, timeZone));
			vsm.setDateClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours, timeZone));

			if (numberOfShiftsLong == ProjectConstants.NUMBER_OF_SHIFTS_2) {
				vsm.setDateOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours, timeZone));
				vsm.setDateClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours, timeZone));
			}
		}

		String vendorStoreId = vsm.insertVendorStore(loginSessionMap.get(LoginUtils.USER_ID));

		if (dateType.equalsIgnoreCase(ProjectConstants.VENDOR_STORE_TIME_DAY_OF_WEEK + "")) {

			VendorStoreTimingModel.deletePreviousEntries(vendorStoreId);

			VendorStoreTimingModel vstm = new VendorStoreTimingModel();

			if (daysOfWeek0 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.SUNDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours0, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours0, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours0, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours0, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}

			if (daysOfWeek1 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.MONDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours1, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours1, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours1, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours1, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}

			if (daysOfWeek2 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.TUESDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours2, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours2, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours2, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours2, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}

			if (daysOfWeek3 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.WEDNESDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours3, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours3, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours3, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours3, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}

			if (daysOfWeek4 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.THURSDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours4, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours4, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours4, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours4, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}

			if (daysOfWeek5 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.FRIDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours5, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours5, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours5, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours5, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}

			if (daysOfWeek6 != null) {

				vstm = new VendorStoreTimingModel();
				vstm.setDay(ProjectConstants.DAY_OF_WEEK.SATURDAY_NO + "");
				vstm.setOpeningMorningHours(DateUtils.getHoursIntoMillis(dateOpeningMorningHours6, timeZone));
				vstm.setClosingMorningHours(DateUtils.getHoursIntoMillis(dateClosingMorningHours6, timeZone));

				if (numberOfShifts.equalsIgnoreCase(ProjectConstants.NUMBER_OF_SHIFTS_2 + "")) {
					vstm.setOpeningEveningHours(DateUtils.getHoursIntoMillis(dateOpeningEveningHours6, timeZone));
					vstm.setClosingEveningHours(DateUtils.getHoursIntoMillis(dateClosingEveningHours6, timeZone));
				}

				vstm.addVendorStoreTiming(loginSessionMap.get(LoginUtils.USER_ID), vendorStoreId);
			}
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL + "?vendorId=" + vendorId);
		}
	}

	public static String validateMorningEveningSlot(String openingMorningTime, String closingMorningTime, String openingEveningTime, String closingEveningTime, long numberOfShifts, String shiftType) {

		String errorMessage = null;

		if ((openingMorningTime != null) && (closingMorningTime != null) && (!"".equals(openingMorningTime.trim())) && (!"".equals(closingMorningTime.trim()))) {

			// Morning slot
			int openingMorningHH = Integer.parseInt(openingMorningTime.split(":")[0]);
			int openingMorningMM = Integer.parseInt(openingMorningTime.split(":")[1]);

			int closingMorningHH = Integer.parseInt(closingMorningTime.split(":")[0]);
			int closingMorningMM = Integer.parseInt(closingMorningTime.split(":")[1]);

			if (shiftType.equalsIgnoreCase(ProjectConstants.DAY_SHIFT_ID + "")) {

				if (openingMorningHH > closingMorningHH) {

					errorMessage = messageForKeyAdmin("errorClosingTimeNotLess");

				} else {

					if (openingMorningHH == closingMorningHH) {

						if (openingMorningMM >= closingMorningMM) {
							errorMessage = messageForKeyAdmin("errorClosingTimeNotLess");
						}
					}
				}
			}

			if (numberOfShifts == 2) {

				// Evening slot
				if ((openingEveningTime != null) && (closingEveningTime != null) && (!"".equals(openingEveningTime.trim())) && (!"".equals(closingEveningTime.trim()))) {

					int openingEveningHH = Integer.parseInt(openingEveningTime.split(":")[0]);
					int openingEveningMM = Integer.parseInt(openingEveningTime.split(":")[1]);

					int closingEveningHH = Integer.parseInt(closingEveningTime.split(":")[0]);
					int closingEveningMM = Integer.parseInt(closingEveningTime.split(":")[1]);

					if (shiftType.equalsIgnoreCase(ProjectConstants.DAY_SHIFT_ID + "")) {

						// IF Evening slot intersect in morning slot
						if (openingEveningHH <= closingMorningHH) {

							if (openingEveningHH == closingMorningHH) {

								if (openingEveningMM <= closingMorningMM) {
									errorMessage = messageForKeyAdmin("errorEveningOpeningTimeNotLess");
								}

							} else {

								errorMessage = messageForKeyAdmin("errorEveningOpeningTimeNotLess");
							}
						}

						if (closingEveningHH <= closingMorningHH) {

							if (closingEveningHH == closingMorningHH) {

								if (closingEveningMM <= closingMorningMM) {
									errorMessage = messageForKeyAdmin("errorEveningClosingTimeNotLess");
								}

							} else {

								errorMessage = messageForKeyAdmin("errorEveningClosingTimeNotLess");
							}
						}

						// Validate evening slot
						if (openingEveningHH > closingEveningHH) {

							errorMessage = messageForKeyAdmin("errorClosingTimeNotLess");

						} else {

							if (openingEveningHH == closingEveningHH) {

								if (openingEveningMM >= closingEveningMM) {
									errorMessage = messageForKeyAdmin("errorClosingTimeNotLess");
								}
							}
						}
					}
				}
			}

		} else {

			errorMessage = messageForKeyAdmin("errorMorningSlotRequired");
		}

		return errorMessage;
	}

	public boolean hasErrorsForEnglish(List<String> typeList) {

		boolean hasErrors = false;

		Validator validator = new Validator();
		
		int size = 0;

		validator.addValidationMapping("storeAddress", messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping("storeName", messageForKeyAdmin("labelStoreName"), new MinMaxLengthValidationRule(1, 25));
		
		for (String string : typeList) {

			if (StringUtils.validString(string)) {
				size++;
			}

		}

		validator.addValidationMapping(FieldConstants.TYPE, BusinessAction.messageForKeyAdmin("labelType"), new RequiredListValidationRule(size));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_VENDOR_STORE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}