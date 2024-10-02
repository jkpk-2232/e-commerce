package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UnifiedHistoryUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.daos.CarFareDao;
import com.webapp.daos.MulticityCountryDao;
import com.webapp.daos.RentalPackageDao;
import com.webapp.daos.TourDao;

public class TourModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TourModel.class);

	private String tourId;
	private String userTourId;
	private String passengerId;
	private String driverId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNo;
	private String phoneNoCode;
	private String pFirstName;
	private String pLastName;
	private String pEmail;
	private String pPhone;
	private String pPhoneCode;
	private String pPhotoUrl;
	private String photoUrl;
	private String sourceGeolocation;
	private String destinationGeolocation;
	private String sourceAddress;
	private String destinationAddress;
	private String carTypeId;
	private String status;
	private String bookingType;
	private String sLatitude;
	private String sLongitude;
	private String dLatitude;
	private String dLongitude;
	private double freeDistance;
	private String cardOwner;
	private double distance;
	private double charges;
	private double percentage;
	private double driverAmount;
	private double invoiceTotal;
	private double driverAmountTotal;
	private double initialFare;
	private double perKmFare;
	private double perMinuteFare;
	private double bookingFees;
	private double discount;
	private double minimumFare;
	private String language;
	private String promoCodeId;
	private boolean isPromoCodeApplied;
	private double total;
	private double promoDiscount;
	private String mode;
	private boolean cardBooking;
	private double usedCredits;
	private String paymentType;
	private String carId;
	private boolean isFixedFare;
	private double finalAmountCollected;
	private double tollAmount;
	private long rideLaterPickupTime;
	private String rideLaterPickupTimeLogs;
	private boolean isRideLater;
	private long rideLaterLastNotification;
	private boolean isTourRideLater;
	private boolean isCriticalTourRideLater;
	private boolean isAcknowledged;
	private String country;
	private String multicityCityRegionId;
	private String multicityCountryId;
	private String currencySymbol;
	private String currencySymbolHtml;
	private String distanceType;
	private double distanceUnits;
	private double cancellationCharges;
	private boolean isSurgePriceApplied;
	private String surgePriceId;
	private double surgePrice;
	private double totalWithSurge;
	private double distanceLive;
	private String promoCode;
	private boolean isRentalBooking;
	private String rentalPackageId;
	private long rentalPackageTime;
	private String pickupFavouriteLocationsId;
	private String destinationFavouriteLocationsId;
	private String carType;
	private double fareAfterSpecificKm;
	private double kmToIncreaseFare;
	private String passengerComment;
	private double passengerRating;
	private String driverComment;
	private double driverRating;
	private long usageCount;
	private String usage;
	private String usageType;
	private String remarkBy;
	private String remark;
	private double updatedAmountCollected;
	private boolean isRefunded;
	private String paymentMode;
	private double fine;
	private boolean isCashNotReceived;
	private String transmissionTypeId;
	private String vendorId;
	private boolean isAirportFixedFareApplied;
	private String airportBookingType;
	private double surgeRadius;
	private String surgeType;
	private boolean airportBooking;
	private double markupFare;
	private String vendorName;
	private String tourBookedBy;

	private String passengerVendorId;
	private String driverVendorId;

	private String airportRegionId;
	private String rentalVendorId;

	private String subscriptionPackageId;

	private String vendorIdFareApplied;
	private double demandVendorPercentage;
	private double supplierVendorPercentage;
	private double demandVendorAmount;
	private double supplierVendorAmount;
	private double amountForDemandSupplier;

	private boolean isTakeRide;
	private boolean isTourTakeRide;
	// user who marked the tour as take ride
	private String markedTakeRideUserId;

	private String serviceTypeId;
	private String courierPickupAddress;
	private String courierContactPersonName;
	private String courierContactPhoneNo;
	private String courierDropAddress;
	private String courierDropContactPersonName;
	private String courierDropContactPhoneNo;
	private String courierDetails;
	private String courierOrderReceivedAgainstVendorId;

	private double cashCollected;
	private double adminSettlementAmount;
	private double totalTaxAmount;
	private String invoiceId;

	private boolean isTakeBookingByDriver;
	private String takeBookingDriverId;
	private long takeBookingByDriverTime;
	private long driverProcessingViaCronTime;

	public String createTour(String loggedInuserId) {

		String passengerVendorId = MultiTenantUtils.getVendorIdByUserId(this.getPassengerId());

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);

		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setCreatedBy(loggedInuserId);
		this.setUpdatedBy(loggedInuserId);
		this.serviceTypeId = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;

		try {

			if (this.multicityCountryId == null || "".equals(this.multicityCountryId)) {
				this.multicityCountryId = "1";
			}

			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("carTypeId", this.carTypeId);
			inputMap.put("multicityCityRegionId", this.multicityCityRegionId);
			inputMap.put("multicityCountryId", this.multicityCountryId);
			inputMap.put("serviceTypeId", this.serviceTypeId);

			CarFareModel carFareModel = carFareDao.getCarFareDetailsByRegionCountryAndId(inputMap);

			MulticityCountryModel multicityCountryModel = multicityCountryDao.getMulticityCountryIdDetailsById(this.multicityCountryId);

			if (carFareModel != null) {

				this.driverAmount = (carFareModel.getDriverPayablePercentage() * (this.total)) / 100;
				this.percentage = carFareModel.getDriverPayablePercentage();
			} else {
				this.driverAmount = 0;
				this.percentage = 0;
			}

			if (carFareModel != null) {
				this.initialFare = carFareModel.getInitialFare();
				this.perMinuteFare = carFareModel.getPerMinuteFare();
				this.perKmFare = carFareModel.getPerKmFare();
				this.minimumFare = carFareModel.getMinimumFare();
				this.discount = carFareModel.getDiscount();
				this.bookingFees = carFareModel.getBookingFees();
				this.freeDistance = carFareModel.getFreeDistance();

				this.kmToIncreaseFare = carFareModel.getKmToIncreaseFare();
				this.fareAfterSpecificKm = carFareModel.getFareAfterSpecificKm();
			} else {
				this.initialFare = 0;
				this.perMinuteFare = 0;
				this.perKmFare = 0;
				this.minimumFare = 0;
				this.discount = 0;
				this.bookingFees = 0;
				this.isFixedFare = false;
				this.freeDistance = 0;

				this.kmToIncreaseFare = 0;
				this.fareAfterSpecificKm = 0;
			}

			if (multicityCountryModel != null) {

				this.distanceType = multicityCountryModel.getDistanceType();
				this.distanceUnits = multicityCountryModel.getDistanceUnits();
				// this.cancellationCharges = multicityCountryModel.getCancellationCharges();
				this.currencySymbol = multicityCountryModel.getCurrencySymbol();
				this.currencySymbolHtml = multicityCountryModel.getCurrencySymbolHtml();
			}

			this.passengerVendorId = passengerVendorId;
			this.demandVendorPercentage = adminSettingsModel.getDemandVendorPercentage();
			this.supplierVendorPercentage = adminSettingsModel.getSupplierVendorPercentage();

			tourDao.createTour(this);
			session.commit();
		} catch (Throwable t) {
			tourId = null;
			session.rollback();
			logger.error("Exception occured during createTour : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForToursAndCouries(this.tourId);

		return this.tourId;
	}

	public String createTourV2(String loggedInuserId) {

		String passengerVendorId = MultiTenantUtils.getVendorIdByUserId(this.getPassengerId());

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setCreatedBy(loggedInuserId);
		this.setUpdatedBy(loggedInuserId);
		this.serviceTypeId = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;

		try {

			if (this.multicityCountryId == null || "".equals(this.multicityCountryId)) {

				this.multicityCountryId = "1";
			}

			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("carTypeId", this.carTypeId);
			inputMap.put("multicityCityRegionId", this.multicityCityRegionId);
			inputMap.put("multicityCountryId", this.multicityCountryId);
			inputMap.put("serviceTypeId", this.serviceTypeId);

			String airportBookingTypeFare = "";
			CarFareModel carFareModel = new CarFareModel();
			if (this.isAirportBooking()) {

				Map<String, Object> airportRegionMap = new HashMap<String, Object>();
				airportRegionMap = BusinessApiAction.airPortbooking(this.getsLatitude(), this.getsLongitude(), this.getdLatitude(), this.getdLongitude(), this.getCarTypeId());
				String latAndLong = "";
				if ((boolean) airportRegionMap.get("isAirportPickUp")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + this.getsLatitude() + " " + this.getsLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_PICKUP;
					this.setAirportBookingType(ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP);

				}
				if ((boolean) airportRegionMap.get("isAirportDrop")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + this.getdLatitude() + " " + this.getdLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_DROP;
					this.setAirportBookingType(ProjectConstants.AIRPORT_BOOKING_TYPE_DROP);
				}
				AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionContainingLatLng(latAndLong);
				carFareModel = CarFareModel.getActiveCarFareDetailsByAirportRegionId(this.getCarTypeId(), airportRegionModel.getAirportRegionId(), airportBookingTypeFare, this.serviceTypeId);

				this.setAirportBooking(true);
				this.setAirportRegionId(airportRegionModel.getAirportRegionId());
			} else {
				carFareModel = carFareDao.getCarFareDetailsByRegionCountryAndId(inputMap);
			}
			MulticityCountryModel multicityCountryModel = multicityCountryDao.getMulticityCountryIdDetailsById(this.multicityCountryId);

			if (this.isRentalBooking) {

				RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(this.rentalPackageId, this.carTypeId);
				RentalPackageModel rentalPackageModel = rentalPackageDao.getRentalPackageDetailsById(this.rentalPackageId);

				if (rentalPackageFareModel != null) {

					this.initialFare = rentalPackageFareModel.getBaseFare();
					this.rentalPackageTime = rentalPackageFareModel.getPackageTime();
					this.freeDistance = rentalPackageFareModel.getPackageDistance();
					this.perKmFare = rentalPackageFareModel.getPerKmFare();
					this.perMinuteFare = rentalPackageFareModel.getPerMinuteFare();

					this.bookingFees = 0;
					this.discount = 0;
					this.minimumFare = 0;

					this.charges = rentalPackageFareModel.getBaseFare();
					this.total = rentalPackageFareModel.getBaseFare();

					this.driverAmount = (rentalPackageFareModel.getDriverPayablePercentage() * (this.total)) / 100;
					this.percentage = rentalPackageFareModel.getDriverPayablePercentage();

					this.rentalVendorId = rentalPackageModel.getVendorId();
				} else {

					this.initialFare = 0;
					this.rentalPackageTime = 0;
					this.freeDistance = 0;
					this.perKmFare = 0;
					this.perMinuteFare = 0;

					this.bookingFees = 0;
					this.discount = 0;
					this.minimumFare = 0;

					this.charges = 0;
					this.total = 0;

					this.driverAmount = 0;
					this.percentage = 0;
				}

				this.kmToIncreaseFare = 0;
				this.fareAfterSpecificKm = 0;

			} else {

				if (carFareModel != null) {

					logger.info("===================== Airport booking confoirm Initial fare==============" + carFareModel.getInitialFare());
					this.initialFare = carFareModel.getInitialFare();
					this.perMinuteFare = carFareModel.getPerMinuteFare();
					this.perKmFare = carFareModel.getPerKmFare();
					this.minimumFare = carFareModel.getMinimumFare();
					this.discount = carFareModel.getDiscount();
					this.bookingFees = carFareModel.getBookingFees();
					this.freeDistance = carFareModel.getFreeDistance();

					this.driverAmount = (carFareModel.getDriverPayablePercentage() * (this.total)) / 100;
					this.percentage = carFareModel.getDriverPayablePercentage();

					this.kmToIncreaseFare = carFareModel.getKmToIncreaseFare();
					this.fareAfterSpecificKm = carFareModel.getFareAfterSpecificKm();

				} else {

					this.initialFare = 0;
					this.perMinuteFare = 0;
					this.perKmFare = 0;
					this.minimumFare = 0;
					this.discount = 0;
					this.bookingFees = 0;
					this.isFixedFare = false;
					this.freeDistance = 0;

					this.driverAmount = 0;
					this.percentage = 0;

					this.kmToIncreaseFare = 0;
					this.fareAfterSpecificKm = 0;
				}
			}

			if (multicityCountryModel != null) {

				this.distanceType = multicityCountryModel.getDistanceType();
				this.distanceUnits = multicityCountryModel.getDistanceUnits();
				// this.cancellationCharges = multicityCountryModel.getCancellationCharges();
				this.currencySymbol = multicityCountryModel.getCurrencySymbol();
				this.currencySymbolHtml = multicityCountryModel.getCurrencySymbolHtml();
			}

			this.passengerVendorId = passengerVendorId;
			this.demandVendorPercentage = adminSettingsModel.getDemandVendorPercentage();
			this.supplierVendorPercentage = adminSettingsModel.getSupplierVendorPercentage();

			tourDao.createTourV2(this);
			session.commit();

		} catch (Throwable t) {
			tourId = null;
			session.rollback();
			logger.error("Exception occured during createTourV2 : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForToursAndCouries(this.tourId);

		return this.tourId;
	}

	public String createTourRideLater(String loggedInuserId) {

		String passengerVendorId = MultiTenantUtils.getVendorIdByUserId(this.getPassengerId());

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);
		RentalPackageDao rentalPackageDao = session.getMapper(RentalPackageDao.class);

		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setCreatedBy(loggedInuserId);
		this.setUpdatedBy(loggedInuserId);
		this.serviceTypeId = ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID;

		try {

			if (this.multicityCountryId == null || "".equals(this.multicityCountryId)) {
				this.multicityCountryId = "1";
			}

			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("carTypeId", this.carTypeId);
			inputMap.put("multicityCityRegionId", this.multicityCityRegionId);
			inputMap.put("multicityCountryId", this.multicityCountryId);
			inputMap.put("serviceTypeId", this.serviceTypeId);

			CarFareModel carFareModel = new CarFareModel();

			if (this.isAirportBooking()) {

				Map<String, Object> airportRegionMap = new HashMap<String, Object>();
				airportRegionMap = BusinessApiAction.airPortbooking(this.getsLatitude(), this.getsLongitude(), this.getdLatitude(), this.getdLongitude(), this.getCarTypeId());
				String latAndLong = "";
				String airportBookingTypeFare = "";
				if ((boolean) airportRegionMap.get("isAirportPickUp")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + this.getsLatitude() + " " + this.getsLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_PICKUP;
					this.setAirportBookingType(ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP);
				}
				if ((boolean) airportRegionMap.get("isAirportDrop")) {
					latAndLong = "ST_Intersects (ST_GeometryFromText('POINT(" + this.getdLatitude() + " " + this.getdLongitude() + ")'), area_polygon)";
					airportBookingTypeFare = ProjectConstants.AIRPORT_DROP;
					this.setAirportBookingType(ProjectConstants.AIRPORT_BOOKING_TYPE_DROP);
				}
				AirportRegionModel airportRegionModel = AirportRegionModel.getAirportRegionContainingLatLng(latAndLong);
				carFareModel = CarFareModel.getActiveCarFareDetailsByAirportRegionId(this.getCarTypeId(), airportRegionModel.getAirportRegionId(), airportBookingTypeFare, this.serviceTypeId);

				this.setAirportBooking(true);
				this.setAirportRegionId(airportRegionModel.getAirportRegionId());
			} else {

				carFareModel = carFareDao.getCarFareDetailsByRegionCountryAndId(inputMap);
			}
			MulticityCountryModel multicityCountryModel = multicityCountryDao.getMulticityCountryIdDetailsById(this.multicityCountryId);

			if (carFareModel != null) {

				this.driverAmount = (carFareModel.getDriverPayablePercentage() * (this.total)) / 100;
				this.percentage = carFareModel.getDriverPayablePercentage();

			} else {

				this.driverAmount = 0;
				this.percentage = 0;
			}

			if (this.isRentalBooking) {

				RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(this.rentalPackageId, this.carTypeId);
				RentalPackageModel rentalPackageModel = rentalPackageDao.getRentalPackageDetailsById(this.rentalPackageId);

				if (rentalPackageFareModel != null) {

					this.initialFare = rentalPackageFareModel.getBaseFare();
					this.rentalPackageTime = rentalPackageFareModel.getPackageTime();
					this.freeDistance = rentalPackageFareModel.getPackageDistance();
					this.perKmFare = rentalPackageFareModel.getPerKmFare();
					this.perMinuteFare = rentalPackageFareModel.getPerMinuteFare();

					this.bookingFees = 0;
					this.discount = 0;
					this.minimumFare = 0;

					this.driverAmount = (rentalPackageFareModel.getDriverPayablePercentage() * (this.total)) / 100;
					this.percentage = rentalPackageFareModel.getDriverPayablePercentage();

					this.rentalVendorId = rentalPackageModel.getVendorId();
				} else {

					this.initialFare = 0;
					this.rentalPackageTime = 0;
					this.freeDistance = 0;
					this.perKmFare = 0;
					this.perMinuteFare = 0;

					this.bookingFees = 0;
					this.discount = 0;
					this.minimumFare = 0;

					this.driverAmount = 0;
					this.percentage = 0;
				}

				this.kmToIncreaseFare = 0;
				this.fareAfterSpecificKm = 0;

			} else if (this.isAirportFixedFareApplied()) {

				this.initialFare = getAirportFixedFare();
				this.perMinuteFare = 0;
				this.perKmFare = 0;
				this.minimumFare = 0;
				this.discount = 0;
				this.bookingFees = 0;
				this.freeDistance = 0;

				if (carFareModel != null) {
					this.driverAmount = (carFareModel.getDriverPayablePercentage() * (this.total)) / 100;
					this.percentage = carFareModel.getDriverPayablePercentage();
				}
				this.kmToIncreaseFare = 0;
				this.fareAfterSpecificKm = 0;

			} else {

				if (carFareModel != null) {

					this.initialFare = carFareModel.getInitialFare();
					this.perMinuteFare = carFareModel.getPerMinuteFare();
					this.perKmFare = carFareModel.getPerKmFare();
					this.minimumFare = carFareModel.getMinimumFare();
					this.discount = carFareModel.getDiscount();
					this.bookingFees = carFareModel.getBookingFees();
					this.freeDistance = carFareModel.getFreeDistance();

					this.driverAmount = (carFareModel.getDriverPayablePercentage() * (this.total)) / 100;
					this.percentage = carFareModel.getDriverPayablePercentage();

					this.kmToIncreaseFare = carFareModel.getKmToIncreaseFare();
					this.fareAfterSpecificKm = carFareModel.getFareAfterSpecificKm();

				} else {

					this.initialFare = 0;
					this.perMinuteFare = 0;
					this.perKmFare = 0;
					this.minimumFare = 0;
					this.discount = 0;
					this.bookingFees = 0;
					this.isFixedFare = false;
					this.freeDistance = 0;

					this.driverAmount = 0;
					this.percentage = 0;

					this.kmToIncreaseFare = 0;
					this.fareAfterSpecificKm = 0;
				}
			}

			if (multicityCountryModel != null) {

				this.distanceType = multicityCountryModel.getDistanceType();
				this.distanceUnits = multicityCountryModel.getDistanceUnits();
				// this.cancellationCharges = multicityCountryModel.getCancellationCharges();
				this.currencySymbol = multicityCountryModel.getCurrencySymbol();
				this.currencySymbolHtml = multicityCountryModel.getCurrencySymbolHtml();
			}

			this.passengerVendorId = passengerVendorId;
			this.demandVendorPercentage = adminSettingsModel.getDemandVendorPercentage();
			this.supplierVendorPercentage = adminSettingsModel.getSupplierVendorPercentage();

			tourDao.createTourRideLater(this);
			session.commit();

		} catch (Throwable t) {
			tourId = null;
			session.rollback();
			logger.error("Exception occured during createTourRideLater : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForToursAndCouries(this.tourId);

		return this.tourId;
	}

	public String createCourierOrder(String loggedInuserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setCreatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setCreatedBy(loggedInuserId);
		this.setUpdatedBy(loggedInuserId);
		this.serviceTypeId = ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID;

		try {
			tourDao.createCourierOrder(this);
			session.commit();
		} catch (Throwable t) {
			tourId = null;
			session.rollback();
			logger.error("Exception occured during createCourierOrder: ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForToursAndCouries(this.tourId);

		return this.tourId;
	}

	public static List<TourModel> getPassengerToursBySpecificDate(String userId, long tourStartDate, long tourEndDate) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getPassengerToursBySpecificDate(userId, tourStartDate, tourEndDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassengerToursBySpecificDate : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static TourModel getCurrentTourByDriverId(String userId) {

		TourModel tour = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tour = tourDao.getCurrentTourByDriverId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrentTourByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tour;
	}
	
	public static TourModel getCurrentTourByDriverIdForRideNowTakeBooking(String userId) {

		TourModel tour = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tour = tourDao.getCurrentTourByDriverIdForRideNowTakeBooking(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrentTourByDriverIdForRideNowTakeBooking : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tour;
	}

	public static TourModel getAdminCurrentTourByDriverId(String userId) {

		TourModel tour = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tour = tourDao.getAdminCurrentTourByDriverId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminCurrentTourByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tour;
	}

	public static TourModel getCurrentTourByPassangerId(String userId) {

		TourModel tour = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tour = tourDao.getCurrentTourByPassangerId(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			tour = null;
			logger.error("Exception occured during getCurrentTourByPassangerId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tour;
	}
	
	public static TourModel getCurrentTourByPassangerIdNew(String userId) {

		TourModel tour = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tour = tourDao.getCurrentTourByPassangerIdNew(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			tour = null;
			logger.error("Exception occured during getCurrentTourByPassangerIdNew : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tour;
	}

	public static List<TourModel> getDriverToursBySpecificDate(String userId, long startDate, long endDate, String globalSearchString, List<String> assignedRegionList) {

		List<TourModel> tourList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getDriverToursBySpecificDate(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverToursBySpecificDate : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static TourModel getTourDetailsByTourId(String tourId) {

		TourModel tourModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourModel = tourDao.getTourDetailsByTourId(tourId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTourDetailsByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourModel;
	}

	public int updateTourStatusByTourId() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {
			updated = tourDao.updateTourStatusByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourStatusByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateTourStatusByTourId(this);

		return updated;
	}

	public int updateDriverPercentageByTourId() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {
			updated = tourDao.updateDriverPercentageByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverPercentageByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public int updateTourAddress() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {
			updated = tourDao.updateTourAddress(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourAddress : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateTourAddress(this);

		return updated;
	}

	public int assignTourDriver(String loggedInuserId) {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInuserId);

		try {
			updated = tourDao.assignTourDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during assignTourDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.assignTourDriver(this);

		return updated;
	}

	public int updateTourStatusByPassenger(String loggedInuserId) {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInuserId);

		try {
			updated = tourDao.updateTourStatusByPassenger(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourStatusByPassenger : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateTourStatusByTourId(this);

		return updated;
	}

	public static int getDriverTourListBySearchByDriverIdCount(String driverId, String globalSearchString, long userTourId, long startDate, long endDate, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("userTourId", userTourId);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getDriverTourListBySearchByDriverIdCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverTourListBySearchByDriverIdCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getDriverTourListBySearchByDriverId(int start, int length, String order, String driverId, String globalSearchString, long userTourId, long startDate, long endDate, List<String> assignedRegionList) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("driverId", driverId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("userTourId", userTourId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getDriverTourListBySearchByDriverId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverTourListBySearchByDriverId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getPassengerTourListBySearchByPassengerId(int start, int length, String order, String passengerId, String globalSearchString, long userTourId, long startDate, long endDate, List<String> assignedRegionList) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("passengerId", passengerId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("userTourId", userTourId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getPassengerTourListBySearchByPassengerId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassengerTourListBySearchByPassengerId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public void updateCharges() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {
			tourDao.updateCharges(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCharges : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateCharges(this);
	}

	public static int getTotalBookingsCountByTime(long createdAt, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("createdAt", createdAt);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getTotalBookingsCountByTime(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalBookingsCountByTime :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int getAllTourListCount(String globalSearchString, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getAllTourListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getAllTourListBySearch(int start, int length, String order, String globalSearchString, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList, String vendorId, String vendorRoleId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);
		inputMap.put("vendorRoleId", vendorRoleId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getAllTourListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getAdminTourListCount(String bookingType, String globalSearchString, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList, String vendorId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("bookingType", bookingType);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getAdminTourListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getAdminTourListBySearch(int start, int length, String order, String globalSearchString, String bookingType, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList, String vendorId,
				String vendorRoleId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("bookingType", bookingType);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("vendorId", vendorId);
		inputMap.put("vendorRoleId", vendorRoleId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getAdminTourListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getDriverReportTourListByDate(long tourStartDate, long tourEndDate) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getDriverReportTourListByDate(tourStartDate, tourEndDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverReportTourListByDate :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getDriverReportTourListByDateByUserIds(long tourStartDate, long tourEndDate, ArrayList<String> userIds) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getDriverReportTourListByDateByUserIds(tourStartDate, tourEndDate, userIds);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverReportTourListByDateByUserIds :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getDriverTourCountByDriverId(long startDate, long endDate, String driverId, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getDriverTourCountByDriverId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverTourCountByDriverId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int getPassengerTourCountByPassengerId(long startDate, long endDate, String passengerId, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("passengerId", passengerId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getPassengerTourCountByPassengerId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPassengerTourCountByPassengerId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public int updateChargesAndDriverAmount() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.updateChargesAndDriverAmount(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateChargesAndDriverAmount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateChargesAndDriverAmount(this);

		return count;
	}

	public int updatePromoCodeStatus() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.updatePromoCodeStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePromoCodeStatus :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updatePromoCodeStatus(this);

		return count;
	}

	public static TourModel getCurrentEndedTourByPassangerId(String userId, String status) {

		TourModel tour = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tour = tourDao.getCurrentEndedTourByPassangerId(userId, status);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			tour = null;
			logger.error("Exception occured during getCurrentEndedTourByPassangerId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tour;
	}

	public static List<TourModel> getToursByStatus(Map<String, Object> inputMap) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getToursByStatus(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getToursByStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getToursByPassengerIdPagination(String userId, long afterTime, long start, long length) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getToursByPassengerIdPagination(userId, afterTime, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getToursByPassengerIdPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getToursByDriverIdPagination(String userId, long afterTime, long start, long length) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getToursByDriverIdPagination(userId, afterTime, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getToursByDriverIdPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public int updateTourCarIdByTourId() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.updateTourCarIdByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourCarIdByTourId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateTourCarIdByTourId(this);

		return count;
	}

	public static List<TourModel> getCronJobRideLaterTourList(Map<String, Object> inputMap) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getCronJobRideLaterTourList(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCronJobRideLaterTourList :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getCronJobRideLaterTourListForNotification(Map<String, Object> inputMap) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getCronJobRideLaterTourListForNotification(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCronJobRideLaterTourListForNotification :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public int updateTourStatusCritical() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.updateTourStatusCritical(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourStatusCritical :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateTourStatusCritical(this);

		return count;
	}

	public int updateRideLaterTourFlag() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.updateRideLaterTourFlag(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRideLaterTourFlag :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateRideLaterTourFlag(this);

		return count;
	}

	public int updateTourRideLaterLastNotification() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.updateTourRideLaterLastNotification(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourRideLaterLastNotification :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getRideLaterToursByPassengerIdPagination(String userId, long start, long length) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getRideLaterToursByPassengerIdPagination(userId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterToursByPassengerIdPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getRideLaterToursByDriverIdPagination(String userId, long start, long length) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getRideLaterToursByDriverIdPagination(userId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterToursByDriverIdPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getRideLaterPassengerDetailsBetweenTimeSlot(String userId, long beforePickupTime, long afterPickupTime) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getRideLaterPassengerDetailsBetweenTimeSlot(userId, beforePickupTime, afterPickupTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterPassengerDetailsBetweenTimeSlot :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int getRideLaterTourListCount(long startDate, long endDate, boolean isTakeRide, String serviceTypeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getRideLaterTourListCount(startDate, endDate, isTakeRide, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getRideLaterTourListBySearch(int start, int length, String order, String globalSearchString, long startDate, long endDate, boolean isTakeRide, String serviceTypeId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getRideLaterTourListBySearch(start, length, order, globalSearchString, startDate, endDate, isTakeRide, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getCriticalRideLaterTourListCount(long rideLaterVisitedTime, long startDate, long endDate) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getCriticalRideLaterTourListCount(rideLaterVisitedTime, startDate, endDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCriticalRideLaterTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getCriticalRideLaterTourListBySearch(int start, int length, String order, String globalSearchString, long rideLaterVisitedTime, long startDate, long endDate) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getCriticalRideLaterTourListBySearch(start, length, order, globalSearchString, rideLaterVisitedTime, startDate, endDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCriticalRideLaterTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getDriverAllTourListCount(String driverId, String globalSearchString, String[] statusArray, long startDate, long endDate) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("driverId", driverId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getDriverAllTourListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverAllTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getDriverAllTourListBySearch(String driverId, int start, int length, String order, String globalSearchString, String[] statusArray, long startDate, long endDate, List<String> assignedRegionList) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("driverId", driverId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getDriverAllTourListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverAllTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public int updateRideLaterTourAcknowledgeByTourId() {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		try {
			updated = tourDao.updateRideLaterTourAcknowledgeByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRideLaterTourAcknowledgeByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public int updateDistanceLiveByTourId(String loggedInUserId) {

		int updated = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(loggedInUserId);
			updated = tourDao.updateDistanceLiveByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDistanceLiveByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updated;
	}

	public static List<TourModel> getTourListForHeatMap(Map<String, Object> inputMap) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getTourListForHeatMap(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTourListForHeatMap :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static boolean checkDriverIsBusyInRideNowAndLaterTrip(String userId, long driverTripBeforeTime, long driverTripAfterTime) {

		boolean status = false;

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("userId", userId);
		inputMap.put("driverTripBeforeTime", driverTripBeforeTime);
		inputMap.put("driverTripAfterTime", driverTripAfterTime);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			status = tourDao.checkDriverIsBusyInRideNowAndLaterTrip(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkDriverIsBusyInRideNowAndLaterTrip : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int updateFavouriteLocations(String loggedInuserId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();

		try {
			count = tourDao.updateFavouriteLocations(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateFavouriteLocations :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static boolean checkPassengerCurrentTourAsFirstTour(String currentTourId, String passengerId, long currentTourTime) {

		boolean isFirstTour = false;

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("currentTourId", currentTourId);
		inputMap.put("passengerId", passengerId);
		inputMap.put("currentTourTime", currentTourTime);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			isFirstTour = tourDao.checkPassengerCurrentTourAsFirstTour(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkPassengerCurrentTourAsFirstTour : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isFirstTour;
	}

	public static List<TourModel> getTourListForBookingsExport(String searchString, String bookingType, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList, String userId, String vendorRoleId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("searchString", searchString);
		inputMap.put("bookingType", bookingType);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);
		inputMap.put("vendorRoleId", vendorRoleId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getTourListForBookingsExport(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTourListForBookingsExport :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getVendorsTotalBookingsCountByTime(long createdAt, List<String> assignedRegionList, String userId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("createdAt", createdAt);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getVendorsTotalBookingsCountByTime(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorsTotalBookingsCountByTime :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getVendorsAllTourListBySearch(int start, int length, String order, String globalSearchString, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList, String userId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getVendorsAllTourListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorsAllTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getVendorsAllTourListCount(String globalSearchString, String[] statusArray, long startDate, long endDate, double[] surgeArray, List<String> assignedRegionList, String userId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("statusArray", statusArray);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("surgeArray", surgeArray);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("userId", userId);
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getVendorsAllTourListCount(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorsAllTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<TourModel> getVendorRideLaterTourListBySearch(int start, int length, String order, String globalSearchString, long startDate, long endDate, List<String> statusList, String userId, List<String> assignedRegionList, boolean isTakeRide,
				String serviceTypeId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("statusList", statusList);
		inputMap.put("userId", userId);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("isTakeRide", isTakeRide);
		inputMap.put("serviceTypeId", serviceTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getVendorRideLaterTourListBySearch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorRideLaterTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getVendorDriverReportTourListByDateByUserIds(long tourStartDate, long tourEndDate, ArrayList<String> userIds, String loggedInUserId) {

		List<TourModel> tourList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getVendorDriverReportTourListByDateByUserIds(tourStartDate, tourEndDate, userIds, loggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorDriverReportTourListByDateByUserIds :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getVendorDriverReportTourListByDate(long tourStartDate, long tourEndDate, String loggedInUserId) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getVendorDriverReportTourListByDate(tourStartDate, tourEndDate, loggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorDriverReportTourListByDate :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getVendorCriticalRideLaterTourListBySearch(int start, int length, String order, String globalSearchString, long rideLaterVisitedTime, long startDate, long endDate, List<String> statusList, String userId, List<String> assignedRegionList) {

		List<TourModel> tourList = new ArrayList<TourModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getVendorCriticalRideLaterTourListBySearch(start, length, order, globalSearchString, rideLaterVisitedTime, startDate, endDate, statusList, userId, assignedRegionList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCriticalRideLaterTourListBySearch :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getVendorRideLaterTourListCount(long startDate, long endDate, List<String> statusList, String userId, List<String> assignedRegionList, boolean isTakeRide, String serviceTypeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getVendorRideLaterTourListCount(startDate, endDate, statusList, userId, assignedRegionList, isTakeRide, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorRideLaterTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int getVendorCriticalRideLaterTourListCount(long rideLaterVisitedTime, long startDate, long endDate, List<String> statusList, String userId, List<String> assignedRegionList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getVendorCriticalRideLaterTourListCount(rideLaterVisitedTime, startDate, endDate, statusList, userId, assignedRegionList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCriticalRideLaterTourListCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static TourModel getDriverDetailsByTourId(String tourId) {

		TourModel tourModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourModel = tourDao.getDriverDetailsByTourId(tourId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverDetailsByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourModel;
	}

	private double getAirportFixedFare() {

		Map<String, Object> ac = TourUtils.checkBookingForAirportPickupOrDrop(this.sLatitude, this.sLongitude, this.dLatitude, this.dLongitude);

		if ((boolean) ac.get("isAirportPickUp") || (boolean) ac.get("isAirportDrop")) {

			AirportRegionModel airportRegionModel = (AirportRegionModel) ac.get("airport");
			if (this.airportBookingType.equals(ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP)) {
				return airportRegionModel.getFixedFareByCarTypeAndBookingType(this.carTypeId, ProjectConstants.AIRPORT_BOOKING_TYPE_PICKUP);
			} else {
				return airportRegionModel.getFixedFareByCarTypeAndBookingType(this.carTypeId, ProjectConstants.AIRPORT_BOOKING_TYPE_DROP);
			}
		}

		return this.charges;
	}

	public static List<TourModel> getRideLaterTourListToExpire(long timeInMillies, List<String> statusList) {

		List<TourModel> tourList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("timeInMillies", timeInMillies);
		inputMap.put("statusList", statusList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getRideLaterTourListToExpire(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterTourListToExpire :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int expireToursBatch(List<String> tourIds) {

		int updatedStatus = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("tourIds", tourIds);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			updatedStatus = tourDao.expireToursBatch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during expireToursBatch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.expireToursBatch(tourIds);

		return updatedStatus;
	}

	public void updateVendorWiseCarFare() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateVendorWiseCarFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorWiseCarFare :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateDriverVendorId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateDriverVendorId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverVendorId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateDriverVendorId(this);
	}

	public void updateScriptPassengerAndDriverVendorIdByCreatedAt() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateScriptPassengerAndDriverVendorIdByCreatedAt(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateScriptPassengerAndDriverVendorIdByCreatedAt :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<TourModel> getScriptTourListByCreatedAt(long tourDate) {

		List<TourModel> tourList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getScriptTourListByCreatedAt(tourDate);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getScriptTourListByCreatedAt :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public void updateScriptPassengerAndDriverVendorIdByTourId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateScriptPassengerAndDriverVendorIdByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateScriptPassengerAndDriverVendorIdByTourId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateDriverSubscriptionAgainstTour() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateDriverSubscriptionAgainstTour(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverSubscriptionAgainstTour :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourDemandSupplierParameters() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateTourDemandSupplierParameters(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourDemandSupplierParameters :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourAsTakeRide() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateTourAsTakeRide(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourAsTakeRide :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateTourAsTakeRide(this);
	}

	public static List<TourModel> getCouriersForProcessingCronJob(List<String> courierStatus, int start, int length, String serviceTypeId, long currentTime, long nextOneHour) {

		List<TourModel> courierList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			courierList = tourDao.getCouriersForProcessingCronJob(courierStatus, start, length, serviceTypeId, currentTime, nextOneHour);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCouriersForProcessingCronJob : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return courierList;
	}

	public static List<TourModel> getToursForProcessingCronJob(List<String> tourStatus, int start, int length, String serviceTypeId, long currentTime) {

		List<TourModel> courierList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			courierList = tourDao.getToursForProcessingCronJob(tourStatus, start, length, serviceTypeId, currentTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getToursForProcessingCronJob : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return courierList;
	}

	public static List<TourModel> getAllToursDataForMigration(int start, int length) {

		List<TourModel> tourList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getAllToursDataForMigration(start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllToursDataForMigration : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getCouriersHistoryByUserId(String userId, String roleId, String serviceTypeId, int start, int length) {

		List<TourModel> tourList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getCouriersHistoryByUserId(userId, roleId, serviceTypeId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCouriersHistoryByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getRideLaterToursForTakeBookings(String driverId, List<String> regionList, List<String> carTypeList, long maxTime, int start, int length, String serviceTypeId) {

		List<TourModel> tourList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getRideLaterToursForTakeBookings(driverId, regionList, carTypeList, maxTime, start, length, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterToursForTakeBookings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static List<TourModel> getRideNowToursForTakeBookings(String driverId, List<String> regionList, List<String> carTypeList, int start, int length, String serviceTypeId, List<String> statusList, String latAndLong) {

		List<TourModel> tourList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourList = tourDao.getRideNowToursForTakeBookings(driverId, regionList, carTypeList, start, length, serviceTypeId, statusList, latAndLong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideNowToursForTakeBookings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return tourList;
	}

	public static int getTakeBookingDriverCurrentCount(String driverId, String serviceTypeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.getTakeBookingDriverCurrentCount(driverId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTakeBookingDriverCurrentCount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateTakeBookingByDriverParams() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateTakeBookingByDriverParams(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTakeBookingByDriverParams : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int checkForConflictingSlotForRideLater(String tourId, String driverId, String serviceTypeId, long beforePickupTime, long afterPickupTime) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			count = tourDao.checkForConflictingSlotForRideLater(tourId, driverId, serviceTypeId, beforePickupTime, afterPickupTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during checkForConflictingSlotForRideLater :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateDriverProcessingViaCronTime() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourDao tourDao = session.getMapper(TourDao.class);

		try {
			tourDao.updateDriverProcessingViaCronTime(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverProcessingViaCronTime : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSourceGeolocation() {
		return sourceGeolocation;
	}

	public void setSourceGeolocation(String sourceGeolocation) {
		this.sourceGeolocation = sourceGeolocation;
	}

	public String getDestinationGeolocation() {
		return destinationGeolocation;
	}

	public void setDestinationGeolocation(String destinationGeolocation) {
		this.destinationGeolocation = destinationGeolocation;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getpFirstName() {
		return pFirstName;
	}

	public void setpFirstName(String pFirstName) {
		this.pFirstName = pFirstName;
	}

	public String getpLastName() {
		return pLastName;
	}

	public void setpLastName(String pLastName) {
		this.pLastName = pLastName;
	}

	public String getpEmail() {
		return pEmail;
	}

	public void setpEmail(String pEmail) {
		this.pEmail = pEmail;
	}

	public String getpPhone() {
		return pPhone;
	}

	public void setpPhone(String pPhone) {
		this.pPhone = pPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getsLatitude() {
		return sLatitude;
	}

	public void setsLatitude(String sLatitude) {
		this.sLatitude = sLatitude;
	}

	public String getsLongitude() {
		return sLongitude;
	}

	public void setsLongitude(String sLongitude) {
		this.sLongitude = sLongitude;
	}

	public String getdLatitude() {
		return dLatitude;
	}

	public void setdLatitude(String dLatitude) {
		this.dLatitude = dLatitude;
	}

	public String getdLongitude() {
		return dLongitude;
	}

	public void setdLongitude(String dLongitude) {
		this.dLongitude = dLongitude;
	}

	public String getpPhotoUrl() {
		return pPhotoUrl;
	}

	public void setpPhotoUrl(String pPhotoUrl) {
		this.pPhotoUrl = pPhotoUrl;
	}

	public String getTourId() {
		return tourId;
	}

	public String getUserTourId() {
		return userTourId;
	}

	public void setUserTourId(String userTourId) {
		this.userTourId = userTourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getDriverAmount() {
		return driverAmount;
	}

	public void setDriverAmount(double driverAmount) {
		this.driverAmount = driverAmount;
	}

	public double getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public double getDriverAmountTotal() {
		return driverAmountTotal;
	}

	public void setDriverAmountTotal(double driverAmountTotal) {
		this.driverAmountTotal = driverAmountTotal;
	}

	public double getInitialFare() {
		return initialFare;
	}

	public void setInitialFare(double initialFare) {
		this.initialFare = initialFare;
	}

	public double getPerKmFare() {
		return perKmFare;
	}

	public void setPerKmFare(double perKmFare) {
		this.perKmFare = perKmFare;
	}

	public double getPerMinuteFare() {
		return perMinuteFare;
	}

	public void setPerMinuteFare(double perMinuteFare) {
		this.perMinuteFare = perMinuteFare;
	}

	public double getBookingFees() {
		return bookingFees;
	}

	public void setBookingFees(double bookingFees) {
		this.bookingFees = bookingFees;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getMinimumFare() {
		return minimumFare;
	}

	public void setMinimumFare(double minimumFare) {
		this.minimumFare = minimumFare;
	}

	public String getPromoCodeId() {
		return promoCodeId;
	}

	public void setPromoCodeId(String promoCodeId) {
		this.promoCodeId = promoCodeId;
	}

	public boolean isPromoCodeApplied() {
		return isPromoCodeApplied;
	}

	public void setPromoCodeApplied(boolean isPromoCodeApplied) {
		this.isPromoCodeApplied = isPromoCodeApplied;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(double promoDiscount) {
		this.promoDiscount = promoDiscount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isCardBooking() {
		return cardBooking;
	}

	public void setCardBooking(boolean cardBooking) {
		this.cardBooking = cardBooking;
	}

	public double getUsedCredits() {
		return usedCredits;
	}

	public void setUsedCredits(double usedCredits) {
		this.usedCredits = usedCredits;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getpPhoneCode() {
		return pPhoneCode;
	}

	public void setpPhoneCode(String pPhoneCode) {
		this.pPhoneCode = pPhoneCode;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public boolean isFixedFare() {
		return isFixedFare;
	}

	public void setFixedFare(boolean isFixedFare) {
		this.isFixedFare = isFixedFare;
	}

	public double getFinalAmountCollected() {
		return finalAmountCollected;
	}

	public void setFinalAmountCollected(double finalAmountCollected) {
		this.finalAmountCollected = finalAmountCollected;
	}

	public double getTollAmount() {
		return tollAmount;
	}

	public void setTollAmount(double tollAmount) {
		this.tollAmount = tollAmount;
	}

	public long getRideLaterPickupTime() {
		return rideLaterPickupTime;
	}

	public void setRideLaterPickupTime(long rideLaterPickupTime) {
		this.rideLaterPickupTime = rideLaterPickupTime;
	}

	public String getRideLaterPickupTimeLogs() {
		return rideLaterPickupTimeLogs;
	}

	public void setRideLaterPickupTimeLogs(String rideLaterPickupTimeLogs) {
		this.rideLaterPickupTimeLogs = rideLaterPickupTimeLogs;
	}

	public boolean isRideLater() {
		return isRideLater;
	}

	public void setRideLater(boolean isRideLater) {
		this.isRideLater = isRideLater;
	}

	public long getRideLaterLastNotification() {
		return rideLaterLastNotification;
	}

	public void setRideLaterLastNotification(long rideLaterLastNotification) {
		this.rideLaterLastNotification = rideLaterLastNotification;
	}

	public boolean isTourRideLater() {
		return isTourRideLater;
	}

	public void setTourRideLater(boolean isTourRideLater) {
		this.isTourRideLater = isTourRideLater;
	}

	public boolean isCriticalTourRideLater() {
		return isCriticalTourRideLater;
	}

	public void setCriticalTourRideLater(boolean isCriticalTourRideLater) {
		this.isCriticalTourRideLater = isCriticalTourRideLater;
	}

	public boolean isAcknowledged() {
		return isAcknowledged;
	}

	public void setAcknowledged(boolean isAcknowledged) {
		this.isAcknowledged = isAcknowledged;
	}

	public double getFreeDistance() {
		return freeDistance;
	}

	public void setFreeDistance(double freeDistance) {
		this.freeDistance = freeDistance;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getMulticityCountryId() {
		return multicityCountryId;
	}

	public void setMulticityCountryId(String multicityCountryId) {
		this.multicityCountryId = multicityCountryId;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbolHtml() {
		return currencySymbolHtml;
	}

	public void setCurrencySymbolHtml(String currencySymbolHtml) {
		this.currencySymbolHtml = currencySymbolHtml;
	}

	public String getDistanceType() {
		return distanceType;
	}

	public void setDistanceType(String distanceType) {
		this.distanceType = distanceType;
	}

	public double getDistanceUnits() {
		return distanceUnits;
	}

	public void setDistanceUnits(double distanceUnits) {
		this.distanceUnits = distanceUnits;
	}

	public double getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(double cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public boolean isSurgePriceApplied() {
		return isSurgePriceApplied;
	}

	public void setSurgePriceApplied(boolean isSurgePriceApplied) {
		this.isSurgePriceApplied = isSurgePriceApplied;
	}

	public String getSurgePriceId() {
		return surgePriceId;
	}

	public void setSurgePriceId(String surgePriceId) {
		this.surgePriceId = surgePriceId;
	}

	public double getSurgePrice() {
		return surgePrice;
	}

	public void setSurgePrice(double surgePrice) {
		this.surgePrice = surgePrice;
	}

	public double getTotalWithSurge() {
		return totalWithSurge;
	}

	public void setTotalWithSurge(double totalWithSurge) {
		this.totalWithSurge = totalWithSurge;
	}

	public double getDistanceLive() {
		return distanceLive;
	}

	public void setDistanceLive(double distanceLive) {
		this.distanceLive = distanceLive;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public boolean isRentalBooking() {
		return isRentalBooking;
	}

	public void setRentalBooking(boolean isRentalBooking) {
		this.isRentalBooking = isRentalBooking;
	}

	public String getRentalPackageId() {
		return rentalPackageId;
	}

	public void setRentalPackageId(String rentalPackageId) {
		this.rentalPackageId = rentalPackageId;
	}

	public long getRentalPackageTime() {
		return rentalPackageTime;
	}

	public void setRentalPackageTime(long rentalPackageTime) {
		this.rentalPackageTime = rentalPackageTime;
	}

	public String getPickupFavouriteLocationsId() {
		return pickupFavouriteLocationsId;
	}

	public void setPickupFavouriteLocationsId(String pickupFavouriteLocationsId) {
		this.pickupFavouriteLocationsId = pickupFavouriteLocationsId;
	}

	public String getDestinationFavouriteLocationsId() {
		return destinationFavouriteLocationsId;
	}

	public void setDestinationFavouriteLocationsId(String destinationFavouriteLocationsId) {
		this.destinationFavouriteLocationsId = destinationFavouriteLocationsId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public double getFareAfterSpecificKm() {
		return fareAfterSpecificKm;
	}

	public void setFareAfterSpecificKm(double fareAfterSpecificKm) {
		this.fareAfterSpecificKm = fareAfterSpecificKm;
	}

	public double getKmToIncreaseFare() {
		return kmToIncreaseFare;
	}

	public void setKmToIncreaseFare(double kmToIncreaseFare) {
		this.kmToIncreaseFare = kmToIncreaseFare;
	}

	public String getPassengerComment() {
		return passengerComment;
	}

	public void setPassengerComment(String passengerComment) {
		this.passengerComment = passengerComment;
	}

	public double getPassengerRating() {
		return passengerRating;
	}

	public void setPassengerRating(double passengerRating) {
		this.passengerRating = passengerRating;
	}

	public String getDriverComment() {
		return driverComment;
	}

	public void setDriverComment(String driverComment) {
		this.driverComment = driverComment;
	}

	public double getDriverRating() {
		return driverRating;
	}

	public void setDriverRating(double driverRating) {
		this.driverRating = driverRating;
	}

	public long getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(long usageCount) {
		this.usageCount = usageCount;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public String getRemarkBy() {
		return remarkBy;
	}

	public void setRemarkBy(String remarkBy) {
		this.remarkBy = remarkBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getUpdatedAmountCollected() {
		return updatedAmountCollected;
	}

	public void setUpdatedAmountCollected(double updatedAmountCollected) {
		this.updatedAmountCollected = updatedAmountCollected;
	}

	public boolean isRefunded() {
		return isRefunded;
	}

	public void setRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public double getFine() {
		return fine;
	}

	public void setFine(double fine) {
		this.fine = fine;
	}

	public boolean isCashNotReceived() {
		return isCashNotReceived;
	}

	public void setCashNotReceived(boolean isCashNotReceived) {
		this.isCashNotReceived = isCashNotReceived;
	}

	public String getTransmissionTypeId() {
		return transmissionTypeId;
	}

	public void setTransmissionTypeId(String transmissionTypeId) {
		this.transmissionTypeId = transmissionTypeId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isAirportFixedFareApplied() {
		return isAirportFixedFareApplied;
	}

	public void setAirportFixedFareApplied(boolean isAirportFixedFareApplied) {
		this.isAirportFixedFareApplied = isAirportFixedFareApplied;
	}

	public String getAirportBookingType() {
		return airportBookingType;
	}

	public void setAirportBookingType(String airportBookingType) {
		this.airportBookingType = airportBookingType;
	}

	public double getSurgeRadius() {
		return surgeRadius;
	}

	public void setSurgeRadius(double surgeRadius) {
		this.surgeRadius = surgeRadius;
	}

	public String getSurgeType() {
		return surgeType;
	}

	public void setSurgeType(String surgeType) {
		this.surgeType = surgeType;
	}

	public boolean isAirportBooking() {
		return airportBooking;
	}

	public void setAirportBooking(boolean airportBooking) {
		this.airportBooking = airportBooking;
	}

	public double getMarkupFare() {
		return markupFare;
	}

	public void setMarkupFare(double markupFare) {
		this.markupFare = markupFare;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getTourBookedBy() {
		return tourBookedBy;
	}

	public void setTourBookedBy(String tourBookedBy) {
		this.tourBookedBy = tourBookedBy;
	}

	public String getPassengerVendorId() {
		return passengerVendorId;
	}

	public void setPassengerVendorId(String passengerVendorId) {
		this.passengerVendorId = passengerVendorId;
	}

	public String getDriverVendorId() {
		return driverVendorId;
	}

	public void setDriverVendorId(String driverVendorId) {
		this.driverVendorId = driverVendorId;
	}

	public String getAirportRegionId() {
		return airportRegionId;
	}

	public void setAirportRegionId(String airportRegionId) {
		this.airportRegionId = airportRegionId;
	}

	public String getRentalVendorId() {
		return rentalVendorId;
	}

	public void setRentalVendorId(String rentalVendorId) {
		this.rentalVendorId = rentalVendorId;
	}

	public String getSubscriptionPackageId() {
		return subscriptionPackageId;
	}

	public void setSubscriptionPackageId(String subscriptionPackageId) {
		this.subscriptionPackageId = subscriptionPackageId;
	}

	public String getVendorIdFareApplied() {
		return vendorIdFareApplied;
	}

	public void setVendorIdFareApplied(String vendorIdFareApplied) {
		this.vendorIdFareApplied = vendorIdFareApplied;
	}

	public double getDemandVendorPercentage() {
		return demandVendorPercentage;
	}

	public void setDemandVendorPercentage(double demandVendorPercentage) {
		this.demandVendorPercentage = demandVendorPercentage;
	}

	public double getSupplierVendorPercentage() {
		return supplierVendorPercentage;
	}

	public void setSupplierVendorPercentage(double supplierVendorPercentage) {
		this.supplierVendorPercentage = supplierVendorPercentage;
	}

	public double getDemandVendorAmount() {
		return demandVendorAmount;
	}

	public void setDemandVendorAmount(double demandVendorAmount) {
		this.demandVendorAmount = demandVendorAmount;
	}

	public double getSupplierVendorAmount() {
		return supplierVendorAmount;
	}

	public void setSupplierVendorAmount(double supplierVendorAmount) {
		this.supplierVendorAmount = supplierVendorAmount;
	}

	public double getAmountForDemandSupplier() {
		return amountForDemandSupplier;
	}

	public void setAmountForDemandSupplier(double amountForDemandSupplier) {
		this.amountForDemandSupplier = amountForDemandSupplier;
	}

	public boolean isTakeRide() {
		return isTakeRide;
	}

	public void setTakeRide(boolean isTakeRide) {
		this.isTakeRide = isTakeRide;
	}

	public boolean isTourTakeRide() {
		return isTourTakeRide;
	}

	public void setTourTakeRide(boolean isTourTakeRide) {
		this.isTourTakeRide = isTourTakeRide;
	}

	public String getMarkedTakeRideUserId() {
		return markedTakeRideUserId;
	}

	public void setMarkedTakeRideUserId(String markedTakeRideUserId) {
		this.markedTakeRideUserId = markedTakeRideUserId;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getCourierPickupAddress() {
		return courierPickupAddress;
	}

	public void setCourierPickupAddress(String courierPickupAddress) {
		this.courierPickupAddress = courierPickupAddress;
	}

	public String getCourierContactPersonName() {
		return courierContactPersonName;
	}

	public void setCourierContactPersonName(String courierContactPersonName) {
		this.courierContactPersonName = courierContactPersonName;
	}

	public String getCourierContactPhoneNo() {
		return courierContactPhoneNo;
	}

	public void setCourierContactPhoneNo(String courierContactPhoneNo) {
		this.courierContactPhoneNo = courierContactPhoneNo;
	}

	public String getCourierDropAddress() {
		return courierDropAddress;
	}

	public void setCourierDropAddress(String courierDropAddress) {
		this.courierDropAddress = courierDropAddress;
	}

	public String getCourierDropContactPersonName() {
		return courierDropContactPersonName;
	}

	public void setCourierDropContactPersonName(String courierDropContactPersonName) {
		this.courierDropContactPersonName = courierDropContactPersonName;
	}

	public String getCourierDropContactPhoneNo() {
		return courierDropContactPhoneNo;
	}

	public void setCourierDropContactPhoneNo(String courierDropContactPhoneNo) {
		this.courierDropContactPhoneNo = courierDropContactPhoneNo;
	}

	public String getCourierDetails() {
		return courierDetails;
	}

	public void setCourierDetails(String courierDetails) {
		this.courierDetails = courierDetails;
	}

	public String getCourierOrderReceivedAgainstVendorId() {
		return courierOrderReceivedAgainstVendorId;
	}

	public void setCourierOrderReceivedAgainstVendorId(String courierOrderReceivedAgainstVendorId) {
		this.courierOrderReceivedAgainstVendorId = courierOrderReceivedAgainstVendorId;
	}

	public double getCashCollected() {
		return cashCollected;
	}

	public void setCashCollected(double cashCollected) {
		this.cashCollected = cashCollected;
	}

	public double getAdminSettlementAmount() {
		return adminSettlementAmount;
	}

	public void setAdminSettlementAmount(double adminSettlementAmount) {
		this.adminSettlementAmount = adminSettlementAmount;
	}

	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public boolean isTakeBookingByDriver() {
		return isTakeBookingByDriver;
	}

	public void setTakeBookingByDriver(boolean isTakeBookingByDriver) {
		this.isTakeBookingByDriver = isTakeBookingByDriver;
	}

	public String getTakeBookingDriverId() {
		return takeBookingDriverId;
	}

	public void setTakeBookingDriverId(String takeBookingDriverId) {
		this.takeBookingDriverId = takeBookingDriverId;
	}

	public long getTakeBookingByDriverTime() {
		return takeBookingByDriverTime;
	}

	public void setTakeBookingByDriverTime(long takeBookingByDriverTime) {
		this.takeBookingByDriverTime = takeBookingByDriverTime;
	}

	public long getDriverProcessingViaCronTime() {
		return driverProcessingViaCronTime;
	}

	public void setDriverProcessingViaCronTime(long driverProcessingViaCronTime) {
		this.driverProcessingViaCronTime = driverProcessingViaCronTime;
	}
}