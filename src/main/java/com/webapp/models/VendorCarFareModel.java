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
import com.webapp.daos.VendorCarFareDao;

public class VendorCarFareModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorCarFareModel.class);

	private String vendorCarFareId;
	private String vendorId;
	private String multicityCityRegionId;
	private String multicityCountryId;
	private String carTypeId;
	private double initialFare;
	private double perKmFare;
	private double perMinuteFare;
	private double bookingFees;
	private double minimumFare;
	private double discount;
	private double driverPayablePercentage;
	private double freeDistance;
	private double fareAfterSpecificKm;
	private double kmToIncreaseFare;
	private String airportRegionId;
	private String airportBookingType;

	private double cancellationCharges;
	private String carType;
	private boolean isFixedFare;
	private boolean isDayFare;

	private String serviceTypeId;

	// done
	public static void batchInsert(List<VendorCarFareModel> vendorCarFareList, String serviceTypeId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			vendorCarFareDao.batchInsert(vendorCarFareList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during batchInsert : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	// done
	public int addVendorCarFare(String loggedInUserId, String serviceTypeId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		long currentTime = DateUtils.nowAsGmtMillisec();

		this.vendorCarFareId = UUIDGenerator.generateUUID();
		this.setCreatedAt(currentTime);
		this.setCreatedBy(loggedInUserId);
		this.setUpdatedAt(currentTime);
		this.setUpdatedBy(loggedInUserId);

		try {
			status = vendorCarFareDao.addVendorCarFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during addVendorCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	// done
	public static VendorCarFareModel getVendorCarFareDetailsByRegionCountryAndId(String carTypeId, String multicityCityRegionId, String multicityCountryId, String vendorId, String serviceTypeId) {

		VendorCarFareModel carFareModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("multicityCityRegionId", multicityCityRegionId);
		inputMap.put("multicityCountryId", multicityCountryId);
		inputMap.put("vendorId", vendorId);
		inputMap.put("serviceTypeId", serviceTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			carFareModel = vendorCarFareDao.getVendorCarFareDetailsByRegionCountryAndId(inputMap);
			if (carFareModel != null) {
				CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();
				carFareModel.setCancellationCharges(cancellationChargeModel.getCharge());
			}
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCarFareDetailsByRegionCountryAndId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareModel;
	}

	// pending
	public int updateVendorCarFareForMultiCity(String loggedInUserId, String serviceTypeId) {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(loggedInUserId);

		try {
			status = vendorCarFareDao.updateVendorCarFareForMultiCity(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during updateVendorCarFareForMultiCity : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	// done
	public void deleteRegionFare(String serviceTypeId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			vendorCarFareDao.deleteRegionFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during deleteRegionFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	// done
	public static boolean isEntryExist(String multicityCityRegionId, String vendorId, String serviceTypeId) {

		boolean isEntryExist = false;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			isEntryExist = vendorCarFareDao.isEntryExist(multicityCityRegionId, vendorId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during isEntryExist : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isEntryExist;
	}

	// done
	public static void deleteVendorCarFareByRegionIdAndCarTypeList(String multicityCityRegionId, List<String> deleteList, String serviceTypeId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("multicityCityRegionId", multicityCityRegionId);
		inputMap.put("deleteList", deleteList);
		inputMap.put("serviceTypeId", serviceTypeId);

		try {
			vendorCarFareDao.deleteVendorCarFareByRegionIdAndCarTypeList(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during deleteVendorCarFareByRegionIdAndCarTypeList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	// done
	public static List<VendorCarFareModel> getVendorCarFareListByRegionIdAndVendorId(String multicityCityRegionId, String vendorId, String serviceTypeId) {

		List<VendorCarFareModel> carFareList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			carFareList = vendorCarFareDao.getVendorCarFareListByRegionIdAndVendorId(multicityCityRegionId, vendorId, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorCarFareListByRegionIdAndVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return carFareList;
	}

	// done
	public void deleteExistingVendorCarFare(String serviceTypeId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorCarFareDao vendorCarFareDao = session.getMapper(VendorCarFareDao.class);

		try {
			vendorCarFareDao.deleteExistingVendorCarFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("\n\n\n\n\n\n\n\tException occured during deleteExistingVendorCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorCarFareId() {
		return vendorCarFareId;
	}

	public void setVendorCarFareId(String vendorCarFareId) {
		this.vendorCarFareId = vendorCarFareId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
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

	public double getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(double cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public boolean isFixedFare() {
		return isFixedFare;
	}

	public void setFixedFare(boolean isFixedFare) {
		this.isFixedFare = isFixedFare;
	}

	public boolean isDayFare() {
		return isDayFare;
	}

	public void setDayFare(boolean isDayFare) {
		this.isDayFare = isDayFare;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
}