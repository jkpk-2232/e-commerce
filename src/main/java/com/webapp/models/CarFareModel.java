package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.CarFareDao;
import com.webapp.daos.VendorCarFareDao;

public class CarFareModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CarFareModel.class);

	private String carFareId;
	private double initialFare;
	private double perKmFare;
	private double perMinuteFare;
	private double bookingFees;
	private double minimumFare;
	private double discount;
	private String carTypeId;
	private String carType;
	private double cancellationCharges;
	private double driverPayablePercentage;
	private double freeDistance;
	private String multicityCityRegionId;
	private String multicityCountryId;

	private boolean isPaymentPaid;

	private double totalFare;

	private double fareAfterSpecificKm;
	private double kmToIncreaseFare;
	private String airportRegionId;
	private String airportBookingType;

	private String serviceTypeId;
	private String carTypeIconImage;

	public int addCarFare(String loggedInUserId, String serviceTypeId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		long currentTime = DateUtils.nowAsGmtMillisec();

		this.carFareId = UUIDGenerator.generateUUID();
		this.setCreatedAt(currentTime);
		this.setCreatedBy(loggedInUserId);
		this.setUpdatedAt(currentTime);
		this.setUpdatedBy(loggedInUserId);
		this.setServiceTypeId(serviceTypeId);

		try {
			status = carFareDao.addCarFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during addCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int updateCarFareForMultiCity(String loggedInUserId, String serviceTypeId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);
		this.setServiceTypeId(serviceTypeId);

		try {
			status = carFareDao.updateCarFareForMultiCity(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during updateCarFareForMultiCity : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int updateAirportDropCarFareForAirportRegion(String loggedInUserId, String serviceTypeId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);
		this.setServiceTypeId(serviceTypeId);

		try {
			status = carFareDao.updateAirportDropCarFareForAirportRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during updateCarFareForMultiCity : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int updateCarFareForAirportRegion(String loggedInUserId, String serviceTypeId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);
		this.setServiceTypeId(serviceTypeId);

		try {
			status = carFareDao.updateCarFareForAirportRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during updateCarFareForMultiCity : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public void deleteExistingCarFare() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {
			carFareDao.deleteExistingCarFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during deleteExistingCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	// ============================================================================

	public static List<CarFareModel> getCarFare(String serviceTypeId) {

		List<CarFareModel> carFareList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {
			carFareList = carFareDao.getCarFare(serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareList;
	}

	public static CarFareModel getById(String carTypeId, String serviceTypeId) {

		CarFareModel carFareList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {
			carFareList = carFareDao.getById(carTypeId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareList;
	}

	public static CarFareModel getCarFareDetailsByRegionCountryAndId(String carTypeId, String multicityCityRegionId, String multicityCountryId, String vendorId, String serviceTypeId) {

		CarFareModel carFareModel = null;
		VendorCarFareModel vendorCarFareModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("multicityCityRegionId", multicityCityRegionId);
		inputMap.put("multicityCountryId", multicityCountryId);
		inputMap.put("serviceTypeId", serviceTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			boolean computeCarFare = false;

			if (vendorId != null) {
				inputMap.put("vendorId", vendorId);
				vendorCarFareModel = vendorCarFareDao.getVendorCarFareDetailsByRegionCountryAndId(inputMap);

				if (vendorCarFareModel == null) {
					computeCarFare = true;
				} else {

					// Get the original car fare parameters and overrride them with vendor_car_fare
					// This is done to compensate for the airportid and booking type
					carFareModel = carFareDao.getCarFareDetailsByRegionCountryAndId(inputMap);

					if (carFareModel == null) {
						carFareModel = new CarFareModel();
					}

					carFareModel.setCarTypeId(vendorCarFareModel.getCarTypeId());
					carFareModel.setInitialFare(vendorCarFareModel.getInitialFare());
					carFareModel.setPerKmFare(vendorCarFareModel.getPerKmFare());
					carFareModel.setPerMinuteFare(vendorCarFareModel.getPerMinuteFare());
					carFareModel.setDriverPayablePercentage(vendorCarFareModel.getDriverPayablePercentage());
					carFareModel.setFreeDistance(vendorCarFareModel.getFreeDistance());
					carFareModel.setMulticityCityRegionId(vendorCarFareModel.getMulticityCityRegionId());
					carFareModel.setMulticityCountryId(vendorCarFareModel.getMulticityCountryId());
					carFareModel.setKmToIncreaseFare(vendorCarFareModel.getKmToIncreaseFare());
					carFareModel.setFareAfterSpecificKm(vendorCarFareModel.getFareAfterSpecificKm());

					CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
					carFareModel.setCancellationCharges(cancellationChargeModel.getCharge());
				}
			}

			if (vendorId == null || computeCarFare) {
				carFareModel = carFareDao.getCarFareDetailsByRegionCountryAndId(inputMap);

				if (carFareModel != null) {
					CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
					carFareModel.setCancellationCharges(cancellationChargeModel.getCharge());
				}
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCarFareDetailsByRegionCountryAndId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carFareModel;
	}

	public static CarFareModel getCarFareDetailsByAirportRegionId(String carTypeId, String airportRegionId, String serviceTypeId) {

		CarFareModel carFareModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("airportRegionId", airportRegionId);
		inputMap.put("serviceTypeId", serviceTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {

			carFareModel = carFareDao.getCarFareDetailsByAirportRegionId(inputMap);

			if (carFareModel != null) {

				CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
				carFareModel.setCancellationCharges(cancellationChargeModel.getCharge());
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCarFareDetailsByRegionCountryAndId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carFareModel;
	}

	public static CarFareModel getActiveCarFareDetailsByAirportRegionId(String carTypeId, String airportRegionId, String airportBookingTypeFare, String serviceTypeId) {

		CarFareModel carFareModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("airportRegionId", airportRegionId);
		inputMap.put("airportBookingType", airportBookingTypeFare);
		inputMap.put("serviceTypeId", serviceTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {

			carFareModel = carFareDao.getActiveCarFareDetailsByAirportRegionId(inputMap);

			if (carFareModel != null) {

				CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
				carFareModel.setCancellationCharges(cancellationChargeModel.getCharge());
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCarFareDetailsByRegionCountryAndId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carFareModel;
	}

	public static CarFareModel getAirportDropCarFareDetailsByAirportRegionId(String carTypeId, String airportRegionId, String serviceTypeId) {

		CarFareModel carFareModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("airportRegionId", airportRegionId);
		inputMap.put("serviceTypeId", serviceTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {

			carFareModel = carFareDao.getAirportDropCarFareDetailsByAirportRegionId(inputMap);

			if (carFareModel != null) {

				CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
				carFareModel.setCancellationCharges(cancellationChargeModel.getCharge());
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getCarFareDetailsByRegionCountryAndId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return carFareModel;
	}

	public static List<CarFareModel> getCarFareListByRegionId(String multicityCityRegionId, String serviceTypeId) {

		List<CarFareModel> carFareList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {
			carFareList = carFareDao.getCarFareListByRegionId(multicityCityRegionId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarFareListByRegionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareList;
	}

	public static List<CarFareModel> getCarFareListByRegionIdAndCarTypeId(String multicityCityRegionId, String carTypeId, String serviceTypeId) {

		List<CarFareModel> carFareList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {
			carFareList = carFareDao.getCarFareListByRegionIdAndCarTypeId(multicityCityRegionId, carTypeId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarFareListByRegionIdAndCarTypeId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareList;
	}

	public static List<CarFareModel> getScriptAirportRegionCarFareDetails(String airportRegionId, String airportBookingType, String serviceTypeId) {

		List<CarFareModel> carFareList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CarFareDao carFareDao = session.getMapper(CarFareDao.class);

		try {
			carFareList = carFareDao.getScriptAirportRegionCarFareDetails(airportRegionId, airportBookingType, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getScriptAirportRegionCarFareDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareList;
	}

	public String getCarFareId() {
		return carFareId;
	}

	public void setCarFareId(String carFareId) {
		this.carFareId = carFareId;
	}

	public double getInitialFare() {
		return initialFare;
	}

	public void setInitialFare(double initialFare) {
		this.initialFare = initialFare;
	}

	public double getBookingFees() {
		return bookingFees;
	}

	public void setBookingFees(double bookingFees) {
		this.bookingFees = bookingFees;
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

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public double getMinimumFare() {
		return minimumFare;
	}

	public void setMinimumFare(double minimumFare) {
		this.minimumFare = minimumFare;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(double cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public double getDriverPayablePercentage() {
		return driverPayablePercentage;
	}

	public void setDriverPayablePercentage(double driverPayablePercentage) {
		this.driverPayablePercentage = driverPayablePercentage;
	}

	public double getFreeDistance() {
		return freeDistance;
	}

	public void setFreeDistance(double freeDistance) {
		this.freeDistance = freeDistance;
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

	public boolean isPaymentPaid() {
		return isPaymentPaid;
	}

	public void setPaymentPaid(boolean isPaymentPaid) {
		this.isPaymentPaid = isPaymentPaid;
	}

	public double getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
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

	public String getAirportRegionId() {
		return airportRegionId;
	}

	public void setAirportRegionId(String airportRegionId) {
		this.airportRegionId = airportRegionId;
	}

	public String getAirportBookingType() {
		return airportBookingType;
	}

	public void setAirportBookingType(String airportBookingType) {
		this.airportBookingType = airportBookingType;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getCarTypeIconImage() {
		return carTypeIconImage;
	}

	public void setCarTypeIconImage(String carTypeIconImage) {
		this.carTypeIconImage = carTypeIconImage;
	}
}