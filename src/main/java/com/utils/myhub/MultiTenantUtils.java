package com.utils.myhub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AirportRegionModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverVendorsModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.TourModel;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorAirportRegionCarFareModel;
import com.webapp.models.VendorAirportRegionModel;
import com.webapp.models.VendorCarFareModel;
import com.webapp.models.VendorCarTypeModel;

public class MultiTenantUtils {

	private static Logger logger = Logger.getLogger(MultiTenantUtils.class);

	public static String validateVendor(String vendorId) {

		if (vendorId != null) {

			UserProfileModel vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

			if (vendorModel == null) {
				return null;
			}

			return vendorId;

		} else {

			UserProfileModel defaultVendorModel = UserProfileModel.getAdminUserAccountDetailsById(WebappPropertyUtils.DEFAULT_VENDOR_ID);

			if (defaultVendorModel == null) {
				return null;
			}

			return defaultVendorModel.getUserId();
		}
	}

	public static String getVendorIdByUserId(String userId) {

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(userId);

		if (userProfileModel == null) {
			return WebappPropertyUtils.DEFAULT_VENDOR_ID;
		}

		// If not driver and passenger
		if (!(userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID) || userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID))) {

			// If user is vendor, then return the vendor himself
			if (userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {
				return userId;
			}

			// If user is super admin or admin, then return the default vendor
			if (userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.SUPER_ADMIN_ROLE_ID) || userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.ADMIN_ROLE_ID)) {
				return WebappPropertyUtils.DEFAULT_VENDOR_ID;
			}
		}

		DriverVendorsModel driverVendorsModel = DriverVendorsModel.getDriverVendorDetailsByDriverId(userId);

		if (driverVendorsModel != null) {
			logger.info("\n\n\n\tgetVendorIdByUserId found vendor\t" + driverVendorsModel.getVendorId());
			return driverVendorsModel.getVendorId();
		}

		logger.info("\n\n\n\tgetVendorIdByUserId vendor not found\t");

		return WebappPropertyUtils.DEFAULT_VENDOR_ID;
	}

	public static void setVendorDriverSubscriptionAppliedInBookingFlowInputData(Map<String, Object> inputMap, String userId, long pickupTime) {

		if (userId == null) {
			return;
		}

		String vendorId = getVendorIdByUserId(userId);
		UserProfileModel vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		if (vendorModel != null && vendorModel.isVendorDriverSubscriptionAppliedInBookingFlow()) {
			inputMap.put("isVendorDriverSubscriptionAppliedInBookingFlow", "isVendorDriverSubscriptionAppliedInBookingFlow");
			inputMap.put("time", pickupTime);
			inputMap.put("vendorId", vendorId);
		} else {
			inputMap.put("isVendorDriverSubscriptionAppliedInBookingFlow", null);
		}
	}

	public static void assignDynamicCityFareToAllVendors(String multicityCityRegionId, String loggedInUserId, boolean deletePreviousFare, String serviceTypeId) {

		if (deletePreviousFare) {
			logger.info("\n\n\n\tDeleting all car fare related for all vendors of this region multicityCityRegionId\t" + multicityCityRegionId);
			VendorCarFareModel vendorCarFareModel = new VendorCarFareModel();
			vendorCarFareModel.setMulticityCityRegionId(multicityCityRegionId);
			vendorCarFareModel.setServiceTypeId(serviceTypeId);
			vendorCarFareModel.deleteRegionFare(serviceTypeId);
		}

		List<CarFareModel> carFareList = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, serviceTypeId);

		for (CarFareModel carFareModel : carFareList) {
			Runnable runnable = () -> {
				addCarFareAgainstAllVendors(carFareModel, loggedInUserId, serviceTypeId);
			};
			Thread t = new Thread(runnable);
			t.start();
		}
	}

	private static void addCarFareAgainstAllVendors(CarFareModel carFareModel, String loggedInUserId, String serviceTypeId) {

		List<VendorCarFareModel> vendorCarFareList = new ArrayList<>();
		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(UserRoles.VENDOR_ROLE_ID);

		int count = 0;

		for (UserProfileModel userProfileModel : vendorList) {

			count++;

			VendorCarFareModel vendorCarFareModel = new VendorCarFareModel();

			vendorCarFareModel.setVendorCarFareId(UUIDGenerator.generateUUID());
			vendorCarFareModel.setVendorId(userProfileModel.getUserId());

			vendorCarFareModel.setMulticityCityRegionId(carFareModel.getMulticityCityRegionId());
			vendorCarFareModel.setMulticityCountryId(carFareModel.getMulticityCountryId());
			vendorCarFareModel.setCarTypeId(carFareModel.getCarTypeId());
			vendorCarFareModel.setInitialFare(carFareModel.getInitialFare());
			vendorCarFareModel.setPerKmFare(carFareModel.getPerKmFare());
			vendorCarFareModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
			vendorCarFareModel.setDriverPayablePercentage(0);
			vendorCarFareModel.setFreeDistance(carFareModel.getFreeDistance());
			vendorCarFareModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
			vendorCarFareModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());
			vendorCarFareModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			vendorCarFareModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			vendorCarFareModel.setCreatedBy(loggedInUserId);
			vendorCarFareModel.setUpdatedBy("MultiTenantUtils car fare vendor wise");
			vendorCarFareModel.setServiceTypeId(serviceTypeId);
			vendorCarFareList.add(vendorCarFareModel);

			if (count >= 100 && vendorCarFareList.size() > 0) {
				logger.info("\n\n\n\tInserting vendorCarFareList by batch of 100");
				VendorCarFareModel.batchInsert(vendorCarFareList, serviceTypeId);

				count = 0;
				vendorCarFareList = new ArrayList<>();
			}
		}

		if (count > 0 && vendorCarFareList.size() > 0) {
			logger.info("\n\n\n\tInserting vendorCarFareList remaining");
			VendorCarFareModel.batchInsert(vendorCarFareList, serviceTypeId);
		}
	}

	public static void addCarFareAgainstVendor(CarFareModel carFareModel, String vendorId, String serviceTypeId) {

		VendorCarFareModel vendorCarFareModel = new VendorCarFareModel();

		vendorCarFareModel.setVendorCarFareId(UUIDGenerator.generateUUID());
		vendorCarFareModel.setVendorId(vendorId);

		vendorCarFareModel.setMulticityCityRegionId(carFareModel.getMulticityCityRegionId());
		vendorCarFareModel.setMulticityCountryId(carFareModel.getMulticityCountryId());
		vendorCarFareModel.setCarTypeId(carFareModel.getCarTypeId());
		vendorCarFareModel.setInitialFare(carFareModel.getInitialFare());
		vendorCarFareModel.setPerKmFare(carFareModel.getPerKmFare());
		vendorCarFareModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
		vendorCarFareModel.setDriverPayablePercentage(0);
		vendorCarFareModel.setFreeDistance(carFareModel.getFreeDistance());
		vendorCarFareModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
		vendorCarFareModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());
		vendorCarFareModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
		vendorCarFareModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		vendorCarFareModel.setCreatedBy(vendorId);
		vendorCarFareModel.setUpdatedBy("MultiTenantUtils car fare vendor wise");
		vendorCarFareModel.setServiceTypeId(serviceTypeId);

		vendorCarFareModel.addVendorCarFare(vendorId, serviceTypeId);
	}

	public static void assignVendorAllCityFare(String userId, String serviceTypeId) {

		List<MulticityCityRegionModel> cityList = MulticityCityRegionModel.getMulticityRegionDetails();

		for (MulticityCityRegionModel multicityCityRegionModel : cityList) {

			boolean isEntryExist = VendorCarFareModel.isEntryExist(multicityCityRegionModel.getMulticityCityRegionId(), userId, serviceTypeId);

			if (!isEntryExist) {

				Runnable runnable = () -> {

					CarFareModel carFareFirstVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.First_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null, serviceTypeId);
					CarFareModel carFareSecondVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Second_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null,
								serviceTypeId);
					CarFareModel carFareThirdVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Third_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null, serviceTypeId);
					CarFareModel carFareFourthVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Fourth_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null,
								serviceTypeId);
					CarFareModel carFareFifthVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Fifth_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null, serviceTypeId);
					CarFareModel carFareSixthVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Seventh_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null,
								serviceTypeId);

					if (carFareFirstVehicle != null) {
						addCarFareAgainstVendor(carFareFirstVehicle, userId, serviceTypeId);
					} else {
						carFareFirstVehicle = new CarFareModel();
						carFareFirstVehicle.setCarTypeId(ProjectConstants.First_Vehicle_ID);
						carFareFirstVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
						carFareFirstVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
						addCarFareAgainstVendor(carFareFirstVehicle, userId, serviceTypeId);
					}

					if (carFareSecondVehicle != null) {
						addCarFareAgainstVendor(carFareSecondVehicle, userId, serviceTypeId);
					} else {
						carFareSecondVehicle = new CarFareModel();
						carFareSecondVehicle.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
						carFareSecondVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
						carFareSecondVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
						addCarFareAgainstVendor(carFareSecondVehicle, userId, serviceTypeId);
					}

					if (carFareThirdVehicle != null) {
						addCarFareAgainstVendor(carFareThirdVehicle, userId, serviceTypeId);
					} else {
						carFareThirdVehicle = new CarFareModel();
						carFareThirdVehicle.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
						carFareThirdVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
						carFareThirdVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
						addCarFareAgainstVendor(carFareThirdVehicle, userId, serviceTypeId);
					}

					if (carFareFourthVehicle != null) {
						addCarFareAgainstVendor(carFareFourthVehicle, userId, serviceTypeId);
					} else {
						carFareFourthVehicle = new CarFareModel();
						carFareFourthVehicle.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
						carFareFourthVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
						carFareFourthVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
						addCarFareAgainstVendor(carFareFourthVehicle, userId, serviceTypeId);
					}

					if (carFareFifthVehicle != null) {
						addCarFareAgainstVendor(carFareFifthVehicle, userId, serviceTypeId);
					} else {
						carFareFifthVehicle = new CarFareModel();
						carFareFifthVehicle.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
						carFareFifthVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
						carFareFifthVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
						addCarFareAgainstVendor(carFareFifthVehicle, userId, serviceTypeId);
					}

					if (carFareSixthVehicle != null) {
						addCarFareAgainstVendor(carFareSixthVehicle, userId, serviceTypeId);
					} else {
						carFareSixthVehicle = new CarFareModel();
						carFareSixthVehicle.setCarTypeId(ProjectConstants.Seventh_Vehicle_ID);
						carFareSixthVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
						carFareSixthVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
						addCarFareAgainstVendor(carFareSixthVehicle, userId, serviceTypeId);
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			}
		}
	}

	public static void assignNonExistingCarTypesDefaultFareToAllCities(String userId) {

//		List<MulticityCityRegionModel> cityList = MulticityCityRegionModel.getMulticityRegionDetails();
//
//		for (MulticityCityRegionModel multicityCityRegionModel : cityList) {
//
//			Runnable runnable = () -> {
//
//				CarFareModel carFareFirstVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.First_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null);
//				CarFareModel carFareSecondVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Second_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null);
//				CarFareModel carFareThirdVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Third_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null);
//				CarFareModel carFareFourthVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Fourth_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null);
//				CarFareModel carFareFifthVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Fifth_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null);
//				CarFareModel carFareSixthVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Seventh_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null);
//
//				if (carFareFirstVehicle == null) {
//					// assign default fare as 0
//					carFareFirstVehicle = new CarFareModel();
//					carFareFirstVehicle.setCarTypeId(ProjectConstants.First_Vehicle_ID);
//					carFareFirstVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
//					carFareFirstVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
//					carFareFirstVehicle.addCarFare(userId);
//				}
//
//				if (carFareSecondVehicle == null) {
//					// assign default fare as 0
//					carFareSecondVehicle = new CarFareModel();
//					carFareSecondVehicle.setCarTypeId(ProjectConstants.Second_Vehicle_ID);
//					carFareSecondVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
//					carFareSecondVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
//					carFareSecondVehicle.addCarFare(userId);
//				}
//
//				if (carFareThirdVehicle == null) {
//					// assign default fare as 0
//					carFareThirdVehicle = new CarFareModel();
//					carFareThirdVehicle.setCarTypeId(ProjectConstants.Third_Vehicle_ID);
//					carFareThirdVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
//					carFareThirdVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
//					carFareThirdVehicle.addCarFare(userId);
//				}
//
//				if (carFareFourthVehicle == null) {
//					// assign default fare as 0
//					carFareFourthVehicle = new CarFareModel();
//					carFareFourthVehicle.setCarTypeId(ProjectConstants.Fourth_Vehicle_ID);
//					carFareFourthVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
//					carFareFourthVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
//					carFareFourthVehicle.addCarFare(userId);
//				}
//
//				if (carFareFifthVehicle == null) {
//					// assign default fare as 0
//					carFareFifthVehicle = new CarFareModel();
//					carFareFifthVehicle.setCarTypeId(ProjectConstants.Fifth_Vehicle_ID);
//					carFareFifthVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
//					carFareFifthVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
//					carFareFifthVehicle.addCarFare(userId);
//				}
//
//				if (carFareSixthVehicle == null) {
//					// assign default fare as 0
//					carFareSixthVehicle = new CarFareModel();
//					carFareSixthVehicle.setCarTypeId(ProjectConstants.Seventh_Vehicle_ID);
//					carFareSixthVehicle.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
//					carFareSixthVehicle.setMulticityCountryId(multicityCityRegionModel.getMulticityCountryId());
//					carFareSixthVehicle.addCarFare(userId);
//				}
//
//			};
//			Thread t = new Thread(runnable);
//			t.start();
//		}
	}

	public static void changeTourFareParametersWithVendorFare(String tourId, String driverId) {

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		// 1. Check if airport booking

		if (tourModel.isAirportBooking() && (tourModel.getAirportBookingType() != null || !"".equalsIgnoreCase(tourModel.getAirportBookingType()))) {

			String driverVendorId = getVendorIdByUserId(driverId);
			String vendorIdFareApplied = driverVendorId;

			if (!tourModel.getPassengerVendorId().equalsIgnoreCase(driverVendorId)) {
				// if passenger and driver vendor are different, then consider default vendor
				// fare.
				vendorIdFareApplied = WebappPropertyUtils.DEFAULT_VENDOR_ID;
			}

			CarFareModel carFareModel = MultiTenantUtils.getAirportCarFare(tourModel, vendorIdFareApplied);

			tourModel.setInitialFare(carFareModel.getInitialFare());
			tourModel.setPerKmFare(carFareModel.getPerKmFare());
			tourModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
			tourModel.setFreeDistance(carFareModel.getFreeDistance());
			tourModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
			tourModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());

			tourModel.setVendorIdFareApplied(vendorIdFareApplied);

			tourModel.updateVendorWiseCarFare();

			tourModel.setDriverVendorId(driverVendorId);
			tourModel.updateDriverVendorId();

		} else if (tourModel != null && (tourModel.getAirportBookingType() == null || "".equalsIgnoreCase(tourModel.getAirportBookingType())) && !tourModel.isAirportBooking() && !tourModel.isRentalBooking()) {

			// 3. Check if normal booking

			String driverVendorId = getVendorIdByUserId(driverId);
			String vendorIdFareApplied = driverVendorId;

			if (!tourModel.getPassengerVendorId().equalsIgnoreCase(driverVendorId)) {
				// if passenger and driver vendor are different, then consider default vendor
				// fare.
				vendorIdFareApplied = WebappPropertyUtils.DEFAULT_VENDOR_ID;
			}

			VendorCarFareModel vendorCarFareModel = VendorCarFareModel.getVendorCarFareDetailsByRegionCountryAndId(tourModel.getCarTypeId(), tourModel.getMulticityCityRegionId(), tourModel.getMulticityCountryId(), vendorIdFareApplied, tourModel.getServiceTypeId());

			if (vendorCarFareModel != null) {
				tourModel.setInitialFare(vendorCarFareModel.getInitialFare());
				tourModel.setPerKmFare(vendorCarFareModel.getPerKmFare());
				tourModel.setPerMinuteFare(vendorCarFareModel.getPerMinuteFare());
				tourModel.setFreeDistance(vendorCarFareModel.getFreeDistance());
				tourModel.setKmToIncreaseFare(vendorCarFareModel.getKmToIncreaseFare());
				tourModel.setFareAfterSpecificKm(vendorCarFareModel.getFareAfterSpecificKm());

				tourModel.setVendorIdFareApplied(vendorIdFareApplied);

				tourModel.updateVendorWiseCarFare();

				tourModel.setDriverVendorId(driverVendorId);
				tourModel.updateDriverVendorId();
			}
		} else if (tourModel.isRentalBooking()) {
			// 2. Check if rental booking

			String driverVendorId = getVendorIdByUserId(driverId);

			tourModel.setDriverVendorId(driverVendorId);
			tourModel.updateDriverVendorId();

		}

	}

	public static Map<String, Object> parseInputParameters(String queryString) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		String[] inputArray = queryString.split("&&&");
		for (String string : inputArray) {
			String[] innerArray = string.split("=");
			if (innerArray.length > 1) {
				if (innerArray[0] != null && innerArray[1] != null) {
					inputMap.put(innerArray[0], innerArray[1]);
				}
			}
		}
		return inputMap;
	}

	public static void assignNewCarTypeToExistingVendors(String carTypeId, String loggedInUserId, String serviceTypeId) {

		Runnable runnable = () -> {

			VendorCarTypeModel vendorCarTypeModel = null;
			CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(carTypeId);
			List<VendorCarTypeModel> vendorCarTypeList = new ArrayList<>();
			List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(UserRoles.VENDOR_ROLE_ID);

			int count = 0;

			for (UserProfileModel userProfileModel : vendorList) {

				count++;

				vendorCarTypeModel = new VendorCarTypeModel();
				vendorCarTypeModel.setVendorCarTypeId(UUIDGenerator.generateUUID());
				vendorCarTypeModel.setCarTypeId(carTypeModel.getCarTypeId());
				vendorCarTypeModel.setVendorId(userProfileModel.getUserId());
				vendorCarTypeModel.setActive(carTypeModel.isActive());
				vendorCarTypeModel.setDeleted(carTypeModel.isDeleted());
				vendorCarTypeModel.setPermanentDeleted(carTypeModel.isPermanentDeleted());
				vendorCarTypeModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				vendorCarTypeModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				vendorCarTypeModel.setCreatedBy("Script");
				vendorCarTypeModel.setUpdatedBy("Script");
				vendorCarTypeModel.setServiceTypeId(serviceTypeId);

				vendorCarTypeList.add(vendorCarTypeModel);

				if (count >= 100 && vendorCarTypeList.size() > 0) {
					logger.info("\n\n\n\tInserting vendorCarFareList by batch of 100");
					VendorCarTypeModel.batchInsertVendorCarType(vendorCarTypeList);

					count = 0;
					vendorCarTypeList = new ArrayList<>();
				}
			}

			if (count > 0 && vendorCarTypeList.size() > 0) {
				logger.info("\n\n\n\tInserting vendorCarTypeList remaining");
				VendorCarTypeModel.batchInsertVendorCarType(vendorCarTypeList);
			}
		};
		Thread t = new Thread(runnable);
		t.start();
	}

	public static void updateVendorCarTypeStatusByCarTypeId(String carTypeId) {

		CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(carTypeId);

		VendorCarTypeModel vendorCarTypeModel = new VendorCarTypeModel();

		vendorCarTypeModel.setCarTypeId(carTypeModel.getCarTypeId());
		vendorCarTypeModel.setActive(carTypeModel.isActive());
		vendorCarTypeModel.setDeleted(carTypeModel.isDeleted());
		vendorCarTypeModel.updateVendorCarTypeStatusByCarTypeId();
	}

	public static void handleEditCityRegionFareNoOverrideCase(List<CarFareModel> previousCarFareList, String multicityCityRegionId, String loggedInUserId, String serviceTypeId) {

		// Get previous car type list --> 1,2,4,5,7
		// Get current car type list --> 1,2,3,5,8
		// Delete the ones other then the common onces. Delete 4,7. Insert 3,8. No
		// change 1,2,5

		List<CarFareModel> currentCarFareList = CarFareModel.getCarFareListByRegionId(multicityCityRegionId, serviceTypeId);
		List<String> previousCarTyeList = new ArrayList<String>();
		List<String> currentCarTyeList = new ArrayList<String>();

		previousCarFareList.forEach(previousCarFareModel -> {
			previousCarTyeList.add(previousCarFareModel.getCarTypeId());
		});

		logger.info("\n\n\n\thandleEditCityRegionFareNoOverrideCase previousCarTyeList\t" + previousCarTyeList);

		currentCarFareList.forEach(currentCarFareModel -> {
			currentCarTyeList.add(currentCarFareModel.getCarTypeId());
		});

		logger.info("\n\n\n\thandleEditCityRegionFareNoOverrideCase currentCarTyeList\t" + currentCarTyeList);

		List<String> deleteList = new ArrayList<String>();
		List<String> insertList = new ArrayList<String>();
		List<String> noChangeList = new ArrayList<String>();

		currentCarTyeList.forEach(string -> {
			if (!previousCarTyeList.contains(string)) {
				insertList.add(string);
			} else {
				noChangeList.add(string);
			}
		});

		previousCarTyeList.forEach(string -> {
			if (!currentCarTyeList.contains(string)) {
				deleteList.add(string);
			}
		});

		logger.info("\n\n\n\thandleEditCityRegionFareNoOverrideCase noChangeList\t" + noChangeList);

		logger.info("\n\n\n\thandleEditCityRegionFareNoOverrideCase deleteList\t" + deleteList);
		if (deleteList.size() > 0) {
			VendorCarFareModel.deleteVendorCarFareByRegionIdAndCarTypeList(multicityCityRegionId, deleteList, serviceTypeId);
		}

		logger.info("\n\n\n\thandleEditCityRegionFareNoOverrideCase insertList\t" + insertList);
		for (String carTypeId : insertList) {

			List<CarFareModel> carFareList = CarFareModel.getCarFareListByRegionIdAndCarTypeId(multicityCityRegionId, carTypeId, serviceTypeId);

			for (CarFareModel carFareModel : carFareList) {
				Runnable runnable = () -> {
					addCarFareAgainstAllVendors(carFareModel, loggedInUserId, serviceTypeId);
				};
				Thread t = new Thread(runnable);
				t.start();
			}
		}
	}

	public static boolean validateVendorCarType(String carTypeId, String vendorId, String multicityCityRegionId, String serviceTypeId) {

		if (carTypeId.equalsIgnoreCase(ProjectConstants.Rental_Type_ID)) {
			logger.info("\n\n\tCar type found as Rental.\t");
			return true;
		}

		UserProfileModel user = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		if (user != null && !user.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {
			logger.info("\n\n\tvalidateVendorCarType --> vendorId is not a vendor. Hence returning true.\t");
			return true;
		}

		List<String> carType = new ArrayList<String>();
		List<String> tempCarList = new ArrayList<>();

		// Get vendor allowed car type list
		List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(vendorId, serviceTypeId);
		for (VendorCarTypeModel string : vendorCarTypeList) {
			tempCarList.add(string.getCarTypeId());
		}

		// Get vendor enabled car type for a particular region
		List<VendorCarFareModel> vendorCarFareList = VendorCarFareModel.getVendorCarFareListByRegionIdAndVendorId(multicityCityRegionId, vendorId, serviceTypeId);
		for (VendorCarFareModel vendorCarFareModel : vendorCarFareList) {
			if (tempCarList.contains(vendorCarFareModel.getCarTypeId())) {
				carType.add(vendorCarFareModel.getCarTypeId());
			}
		}

		for (String carTypeTemp : carType) {
			if (carTypeId.equalsIgnoreCase(carTypeTemp)) {
				return true;
			}
		}

		return false;
	}

	public static List<CarTypeModel> getCarTypeListOfVendor(String headerVendorId, String multicityCityRegionId, String serviceTypeId) {

		List<CarTypeModel> carTypeList = new ArrayList<CarTypeModel>();
		List<String> carType = new ArrayList<String>();
		List<String> tempCarList = new ArrayList<>();

		// Get vendor allowed car type list
		List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(headerVendorId, serviceTypeId);
		for (VendorCarTypeModel string : vendorCarTypeList) {
			tempCarList.add(string.getCarTypeId());
		}

		// Get vendor enabled car type for a particular region
		List<VendorCarFareModel> vendorCarFareList = VendorCarFareModel.getVendorCarFareListByRegionIdAndVendorId(multicityCityRegionId, headerVendorId, serviceTypeId);
		for (VendorCarFareModel vendorCarFareModel : vendorCarFareList) {
			if (tempCarList.contains(vendorCarFareModel.getCarTypeId())) {
				carType.add(vendorCarFareModel.getCarTypeId());
			}
		}

		if (carType.size() > 0) {
			carTypeList = CarTypeModel.getCarListByIds(carType);
		}

		return carTypeList;
	}

	public static boolean validateVendorCarTypeRental(String carTypeId, String vendorId, String multicityCityRegionId, String rentalPackageId) {

		UserProfileModel user = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		if (user != null && !user.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {
			logger.info("\n\n\tvalidateVendorCarType --> vendorId is not a vendor. Hence returning true.\t");
			return true;
		}

		List<String> carType = new ArrayList<String>();
		List<String> tempCarList = new ArrayList<>();

		// Get vendor allowed car type list
		List<VendorCarTypeModel> vendorCarTypeList = VendorCarTypeModel.getVendorCarTypeListByVendorId(vendorId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		for (VendorCarTypeModel string : vendorCarTypeList) {
			tempCarList.add(string.getCarTypeId());
		}

		// Get rental package details
		List<RentalPackageFareModel> rentalPackageFareModelList = RentalPackageFareModel.getRentalPackageFareListByRentalPackageId(rentalPackageId);
		for (RentalPackageFareModel rentalPackageFareModel : rentalPackageFareModelList) {
			if (tempCarList.contains(rentalPackageFareModel.getCarTypeId())) {
				carType.add(rentalPackageFareModel.getCarTypeId());
			}
		}

		for (String carTypeTemp : carType) {
			if (carTypeId.equalsIgnoreCase(carTypeTemp)) {
				return true;
			}
		}

		return false;
	}

	public static void assignAirportRegionAndCarFareToAllVendors(String airportRegionId) {

		/*
		 * Method to assign the airport region and it's car fare to all the existing
		 * vendors in the database
		 */

		AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionDetailsById(airportRegionId);
		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(UserRoles.VENDOR_ROLE_ID);
		MulticityCountryModel mcm = MulticityCountryModel.getMulticityCountryIdDetailsById(ProjectConstants.DEFAULT_COUNTRY_ID);

		vendorList.stream().forEach(vendorModel -> {
			Runnable runnable = () -> {
				mapVendorToAirportRegionAndCarFare(vendorModel.getUserId(), mcm.getMulticityCountryId(), airportRegionModel);
			};
			Thread t = new Thread(runnable);
			t.start();
		});
	}

	public static void assignVendorAllExistingAirportRegionsAndCarFare(String vendorId) {
		List<AirportRegionModel> airportRegionList = AirportRegionModel.getScriptAllExistingAirports();
		MulticityCountryModel mcm = MulticityCountryModel.getMulticityCountryIdDetailsById(ProjectConstants.DEFAULT_COUNTRY_ID);

		// Generic method to map 1 vendor to a list of airport region
		assignVendorAllExistingAirportRegionsAndCarFare(vendorId, airportRegionList, mcm.getMulticityCountryId());
	}

	private static void assignVendorAllExistingAirportRegionsAndCarFare(String vendorId, List<AirportRegionModel> airportRegionList, String multicityCountryId) {
		Runnable runnable = () -> {
			assign(vendorId, airportRegionList, multicityCountryId);
		};
		Thread t = new Thread(runnable);
		t.start();
	}

	private static void assign(String vendorId, List<AirportRegionModel> airportRegionList, String multicityCountryId) {
		airportRegionList.stream().forEach(airportRegionModel -> {
			// Generic method to map 1 vendor to one airport region
			mapVendorToAirportRegionAndCarFare(vendorId, multicityCountryId, airportRegionModel);
		});
	}

	private static void mapVendorToAirportRegionAndCarFare(String vendorId, String multicityCountryId, AirportRegionModel airportRegionModel) {

		/*
		 * Method to assign the airport region and it's car fare to a particular vendor
		 */

		VendorAirportRegionModel vendorAirportRegionModel = VendorAirportRegionModel.getVendorAirportDetailsByVendorIdAndAirportRegionId(vendorId, airportRegionModel.getAirportRegionId());

		if (vendorAirportRegionModel == null) {

			vendorAirportRegionModel = new VendorAirportRegionModel();
			vendorAirportRegionModel.setAirportRegionId(airportRegionModel.getAirportRegionId());
			vendorAirportRegionModel.setVendorId(vendorId);
			vendorAirportRegionModel.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
			vendorAirportRegionModel.setMulticityCountryId(multicityCountryId);
			String vendorAirportRegionId = vendorAirportRegionModel.addVendorAirportRegion(vendorId);

			List<VendorAirportRegionCarFareModel> pickupList = new ArrayList<>();
			List<CarFareModel> pickupCarFareList = CarFareModel.getScriptAirportRegionCarFareDetails(airportRegionModel.getAirportRegionId(), ProjectConstants.AIRPORT_PICKUP, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
			pickupCarFareList.stream().forEach(carFareModel -> {
				VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel = new VendorAirportRegionCarFareModel();
				vendorAirportRegionCarFareModel.setVendorAirportRegionCarFareId(UUIDGenerator.generateUUID());
				vendorAirportRegionCarFareModel.setVendorAirportRegionId(vendorAirportRegionId);
				vendorAirportRegionCarFareModel.setVendorId(vendorId);
				vendorAirportRegionCarFareModel.setAirportRegionId(airportRegionModel.getAirportRegionId());
				vendorAirportRegionCarFareModel.setAirportBookingType(ProjectConstants.AIRPORT_PICKUP);
				vendorAirportRegionCarFareModel.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
				vendorAirportRegionCarFareModel.setMulticityCountryId(multicityCountryId);
				vendorAirportRegionCarFareModel.setCarTypeId(carFareModel.getCarTypeId());
				vendorAirportRegionCarFareModel.setInitialFare(carFareModel.getInitialFare());
				vendorAirportRegionCarFareModel.setPerKmFare(carFareModel.getPerKmFare());
				vendorAirportRegionCarFareModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
				vendorAirportRegionCarFareModel.setFreeDistance(carFareModel.getFreeDistance());
				vendorAirportRegionCarFareModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());
				vendorAirportRegionCarFareModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
				vendorAirportRegionCarFareModel.setActive(true);
				vendorAirportRegionCarFareModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				vendorAirportRegionCarFareModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				vendorAirportRegionCarFareModel.setCreatedBy("Script");
				vendorAirportRegionCarFareModel.setUpdatedBy("Script");
				pickupList.add(vendorAirportRegionCarFareModel);
			});

			List<VendorAirportRegionCarFareModel> dropList = new ArrayList<>();
			List<CarFareModel> dropCarFareList = CarFareModel.getScriptAirportRegionCarFareDetails(airportRegionModel.getAirportRegionId(), ProjectConstants.AIRPORT_DROP, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
			dropCarFareList.stream().forEach(carFareModel -> {
				VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel = new VendorAirportRegionCarFareModel();
				vendorAirportRegionCarFareModel.setVendorAirportRegionCarFareId(UUIDGenerator.generateUUID());
				vendorAirportRegionCarFareModel.setVendorAirportRegionId(vendorAirportRegionId);
				vendorAirportRegionCarFareModel.setVendorId(vendorId);
				vendorAirportRegionCarFareModel.setAirportRegionId(airportRegionModel.getAirportRegionId());
				vendorAirportRegionCarFareModel.setAirportBookingType(ProjectConstants.AIRPORT_DROP);
				vendorAirportRegionCarFareModel.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
				vendorAirportRegionCarFareModel.setMulticityCountryId(multicityCountryId);
				vendorAirportRegionCarFareModel.setCarTypeId(carFareModel.getCarTypeId());
				vendorAirportRegionCarFareModel.setInitialFare(carFareModel.getInitialFare());
				vendorAirportRegionCarFareModel.setPerKmFare(carFareModel.getPerKmFare());
				vendorAirportRegionCarFareModel.setPerMinuteFare(carFareModel.getPerMinuteFare());
				vendorAirportRegionCarFareModel.setFreeDistance(carFareModel.getFreeDistance());
				vendorAirportRegionCarFareModel.setFareAfterSpecificKm(carFareModel.getFareAfterSpecificKm());
				vendorAirportRegionCarFareModel.setKmToIncreaseFare(carFareModel.getKmToIncreaseFare());
				vendorAirportRegionCarFareModel.setActive(true);
				vendorAirportRegionCarFareModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				vendorAirportRegionCarFareModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				vendorAirportRegionCarFareModel.setCreatedBy("Script");
				vendorAirportRegionCarFareModel.setUpdatedBy("Script");
				dropList.add(vendorAirportRegionCarFareModel);
			});

			if (pickupList.size() > 0) {
				VendorAirportRegionCarFareModel.batchInsert(pickupList);
			}

			if (dropList.size() > 0) {
				VendorAirportRegionCarFareModel.batchInsert(dropList);
			}
		}
	}

	public static void updateAllVendorAirportRegionsAndCarFareWithCityId(String airportRegionId, String multicityCityRegionId) {
		VendorAirportRegionModel vendorAirportRegionModel = new VendorAirportRegionModel();
		vendorAirportRegionModel.setAirportRegionId(airportRegionId);
		vendorAirportRegionModel.setMulticityCityRegionId(multicityCityRegionId);
		vendorAirportRegionModel.updateMulticityRegionId();

		VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel = new VendorAirportRegionCarFareModel();
		vendorAirportRegionCarFareModel.setAirportRegionId(airportRegionId);
		vendorAirportRegionCarFareModel.setMulticityCityRegionId(multicityCityRegionId);
		vendorAirportRegionCarFareModel.updateMulticityRegionId();
	}

	public static CarFareModel getAirportCarFare(TourModel tourModel, String vendorId) {

		CarFareModel carFareModel = new CarFareModel();
		String latAndLong = "";
		String airportRegionId = tourModel.getAirportRegionId();
		String airportBookingTypeFare = tourModel.getAirportBookingType().equalsIgnoreCase(ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP) ? ProjectConstants.AIRPORT_PICKUP : ProjectConstants.AIRPORT_DROP;
		String carTypeId = tourModel.getCarTypeId();

		AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionDetailsById(airportRegionId);

		if (airportRegionModel == null) {

			if (tourModel.getAirportBookingType().equalsIgnoreCase(ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP)) {
				latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + tourModel.getsLatitude() + " " + tourModel.getsLongitude() + ")'), area_polygon)";
				airportBookingTypeFare = ProjectConstants.AIRPORT_PICKUP;
			}
			if (tourModel.getAirportBookingType().equalsIgnoreCase(ProjectConstants.AIRPORT_BOOKING_TYPE_DROP)) {
				latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + tourModel.getdLatitude() + " " + tourModel.getdLongitude() + ")'), area_polygon)";
				airportBookingTypeFare = ProjectConstants.AIRPORT_DROP;
			}

			airportRegionModel = AirportRegionModel.getAirportRegionContainingLatLng(latAndLong);
		}

//		VendorAirportRegionModel vendorAirportRegionModel = VendorAirportRegionModel.getVendorAirportDetailsByVendorIdAndAirportRegionId(vendorId, airportRegionModel.getAirportRegionId());
//		VendorAirportRegionCarFareModel varcfm = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorId, airportRegionModel.getAirportRegionId(), carTypeId, airportBookingTypeFare);

//		if (varcfm == null) {
		carFareModel = CarFareModel.getActiveCarFareDetailsByAirportRegionId(carTypeId, airportRegionModel.getAirportRegionId(), airportBookingTypeFare, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		logger.info("\n\n\n\tReturning airport fare as VendorAirportRegionCarFareModel is null\t");
//		} else {
//			carFareModel.setCarTypeId(carTypeId);
//			carFareModel.setInitialFare(varcfm.getInitialFare());
//			carFareModel.setPerKmFare(varcfm.getPerKmFare());
//			carFareModel.setPerMinuteFare(varcfm.getPerMinuteFare());
//			carFareModel.setFareAfterSpecificKm(varcfm.getFareAfterSpecificKm());
//			carFareModel.setFreeDistance(varcfm.getFreeDistance());
//			carFareModel.setKmToIncreaseFare(varcfm.getKmToIncreaseFare());
//			carFareModel.setAirportBookingType(airportBookingTypeFare);
//			carFareModel.setAirportRegionId(airportRegionModel.getAirportRegionId());
//			carFareModel.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
//			carFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
//
//			logger.info("\n\n\n\tReturning airport fare of the vendor\t" + vendorAirportRegionModel.getVendorId());
//		}

		return carFareModel;
	}

	public static CarFareModel getAirportCarFare(String carTypeId, String latAndLong, String airportBookingTypeFare, String vendorId) {

		CarFareModel carFareModel = new CarFareModel();
		AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionContainingLatLng(latAndLong);
//		VendorAirportRegionModel vendorAirportRegionModel = VendorAirportRegionModel.getVendorAirportDetailsByVendorIdAndAirportRegionId(vendorId, airportRegionModel.getAirportRegionId());
//
//		if (vendorAirportRegionModel != null) {
//			VendorAirportRegionCarFareModel varcfm = VendorAirportRegionCarFareModel.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorId, airportRegionModel.getAirportRegionId(), carTypeId, airportBookingTypeFare);
//
//			if (varcfm == null) {
//				carFareModel = CarFareModel.getActiveCarFareDetailsByAirportRegionId(carTypeId, airportRegionModel.getAirportRegionId(), airportBookingTypeFare, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
//				logger.info("\n\n\n\tReturning airport fare as VendorAirportRegionCarFareModel is null\t");
//			} else {
//				carFareModel.setCarTypeId(carTypeId);
//				carFareModel.setInitialFare(varcfm.getInitialFare());
//				carFareModel.setPerKmFare(varcfm.getPerKmFare());
//				carFareModel.setPerMinuteFare(varcfm.getPerMinuteFare());
//				carFareModel.setFareAfterSpecificKm(varcfm.getFareAfterSpecificKm());
//				carFareModel.setFreeDistance(varcfm.getFreeDistance());
//				carFareModel.setKmToIncreaseFare(varcfm.getKmToIncreaseFare());
//				carFareModel.setAirportBookingType(airportBookingTypeFare);
//				carFareModel.setAirportRegionId(airportRegionModel.getAirportRegionId());
//				carFareModel.setMulticityCityRegionId(airportRegionModel.getMulticityCityRegionId());
//				carFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
//
//				logger.info("\n\n\n\tReturning airport fare of the vendor\t" + vendorAirportRegionModel.getVendorId());
//			}
//		} else {
		logger.info("\n\n\n\tReturning normal airport fare airportregionId\t" + airportRegionModel.getAirportRegionId());
		carFareModel = CarFareModel.getActiveCarFareDetailsByAirportRegionId(carTypeId, airportRegionModel.getAirportRegionId(), airportBookingTypeFare, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
//		}

		return carFareModel;
	}

	public static void processDefaultVendorForTripsAfterMultiTenantDeployment(long tourDate) {
		List<TourModel> tourList = TourModel.getScriptTourListByCreatedAt(tourDate);
		List<TourModel> dummyTourList = new ArrayList<>();
		if (tourList.size() > 0) {
			int count = 0;
			for (TourModel tour : tourList) {
				count++;
				dummyTourList.add(tour);
				if (count == 20) {
					processTour(dummyTourList);
					count = 0;
					dummyTourList = new ArrayList<TourModel>();
				}
			}

			if (dummyTourList.size() > 0) {
				processTour(dummyTourList);
			}
		}
	}

	private static void processTour(List<TourModel> dummyTourList) {
		Runnable r = () -> {
			for (TourModel tour : dummyTourList) {
				logger.info("\n\ttour.getTourId()\t" + tour.getTourId());
				boolean process = false;
				if (tour.getPassengerVendorId() == null) {
					tour.setPassengerVendorId(MultiTenantUtils.getVendorIdByUserId(tour.getPassengerId()));
					process = true;
				}

				if (!"-1".equalsIgnoreCase(tour.getDriverId())) {
					if (tour.getDriverVendorId() == null) {
						tour.setDriverVendorId(MultiTenantUtils.getVendorIdByUserId(tour.getDriverVendorId()));
						process = true;
					}
				}

				if (process) {
					tour.updateScriptPassengerAndDriverVendorIdByTourId();
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	public static void processDemandSupplierAmount(String tourId, double total, double driverAmount) {

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		double amountForDemandSupplier = total - driverAmount;

		if (tourModel.getPassengerVendorId().equalsIgnoreCase(tourModel.getDriverVendorId())) {

			// amountForDemandSupplier will go to the same vendor
			String remarkVendorDemand = String.format(BusinessAction.messageForKeyAdmin("remarkVendorDemand", tourModel.getLanguage()), tourModel.getUserTourId());
			processDemandSupplierAmount(tourModel, amountForDemandSupplier, remarkVendorDemand, tourModel.getDriverVendorId());

		} else {
			// amountForDemandSupplier will split between demand and supplier vendor
			double demandVendorAmount = (tourModel.getDemandVendorPercentage() * amountForDemandSupplier) / 100;
			double supplierVendorAmount = (tourModel.getSupplierVendorPercentage() * amountForDemandSupplier) / 100;

			tourModel.setAmountForDemandSupplier(amountForDemandSupplier);
			tourModel.setDemandVendorAmount(demandVendorAmount);
			tourModel.setSupplierVendorAmount(supplierVendorAmount);
			tourModel.updateTourDemandSupplierParameters();

			// Credit amount into demand vendor account i.e., vendor account of passenger
			String remarkVendorDemand = String.format(BusinessAction.messageForKeyAdmin("remarkVendorDemand", tourModel.getLanguage()), tourModel.getUserTourId());
			processDemandSupplierAmount(tourModel, demandVendorAmount, remarkVendorDemand, tourModel.getPassengerVendorId());

			// Credit amount into supplier vendor account i.e., vendor account of driver
			String remarkVendorSupplier = String.format(BusinessAction.messageForKeyAdmin("remarkVendorSupplier", tourModel.getLanguage()), tourModel.getUserTourId());
			processDemandSupplierAmount(tourModel, supplierVendorAmount, remarkVendorSupplier, tourModel.getDriverVendorId());
		}
	}

	private static void processDemandSupplierAmount(TourModel tourModel, double vendorAmount, String message, String vendorIdWhoseAccountIsToBeCredited) {
		UserAccountModel.updateUserAccountAndCreateLog(vendorIdWhoseAccountIsToBeCredited, vendorAmount, ProjectConstants.TRANSACTION_LOG_TYPE_CREDIT, message, message, vendorIdWhoseAccountIsToBeCredited, false);
	}

	public static void assignTakeRideUrlToExistingUsers(List<UserProfileModel> userList) throws IOException {

		Runnable r = () -> {
			List<UrlAccessesModel> urlAccessesModelList = new ArrayList<UrlAccessesModel>();
			userList.stream().forEach(userProfileModel -> {
				UrlAccessesModel urlAccessesModel = new UrlAccessesModel();
				// 35 is the take ride url id
				urlAccessesModel.setUrlId(Long.parseLong("35"));
				urlAccessesModel.setUserId(userProfileModel.getUserId());
				urlAccessesModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
				urlAccessesModel.setCreatedBy("Script");
				urlAccessesModel.setRecordStatus("A");

				urlAccessesModelList.add(urlAccessesModel);

				// insert by size of 20
				if (urlAccessesModelList.size() % 20 == 0) {
					try {
						UrlAccessesModel.addUserUrlAccessesBatch(urlAccessesModelList);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					urlAccessesModelList.clear();
				}
			});

			// insert remaining users access
			if (urlAccessesModelList.size() > 0) {
				try {
					UrlAccessesModel.addUserUrlAccessesBatch(urlAccessesModelList);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}