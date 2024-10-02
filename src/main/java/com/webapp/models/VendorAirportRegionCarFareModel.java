package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorAirportRegionCarFareDao;

public class VendorAirportRegionCarFareModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorAirportRegionCarFareModel.class);

	private String vendorAirportRegionCarFareId;
	private String vendorAirportRegionId;
	private String vendorId;
	private String airportRegionId;
	private String airportBookingType;
	private String multicityCityRegionId;
	private String multicityCountryId;

	private String carTypeId;
	private double initialFare;
	private double perKmFare;
	private double perMinuteFare;
	private double freeDistance;
	private double fareAfterSpecificKm;
	private double kmToIncreaseFare;

	private boolean isActive;

	public void addVendorAirportRegion(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionCarFareDao vendorAirportRegionCarFareDao = session.getMapper(VendorAirportRegionCarFareDao.class);

		this.vendorAirportRegionCarFareId = UUIDGenerator.generateUUID();
		this.createdBy = userId;
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.isActive = true;
		try {
			vendorAirportRegionCarFareDao.addVendorAirportRegionCarFare(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addVendorAirportRegionCarFare : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static void batchInsert(List<VendorAirportRegionCarFareModel> pickupList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionCarFareDao vendorAirportRegionCarFareDao = session.getMapper(VendorAirportRegionCarFareDao.class);

		try {
			vendorAirportRegionCarFareDao.batchInsert(pickupList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during batchInsert : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateMulticityRegionId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionCarFareDao vendorAirportRegionCarFareDao = session.getMapper(VendorAirportRegionCarFareDao.class);

		try {
			vendorAirportRegionCarFareDao.updateMulticityRegionId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateMulticityRegionId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static VendorAirportRegionCarFareModel getCarFareDetailsByVendorIdAirportIdAndCarTypeId(String vendorId, String airportId, String carTypeId, String airportBookingType) {

		VendorAirportRegionCarFareModel vendorAirportRegionCarFareModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionCarFareDao vendorAirportRegionCarFareDao = session.getMapper(VendorAirportRegionCarFareDao.class);

		try {
			vendorAirportRegionCarFareModel = vendorAirportRegionCarFareDao.getCarFareDetailsByVendorIdAirportIdAndCarTypeId(vendorId, airportId, carTypeId, airportBookingType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCarFareDetailsByVendorIdAirportIdAndCarTypeId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorAirportRegionCarFareModel;
	}

	public void deleteExistingData() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorAirportRegionCarFareDao vendorAirportRegionCarFareDao = session.getMapper(VendorAirportRegionCarFareDao.class);

		try {
			vendorAirportRegionCarFareDao.deleteExistingData(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteExistingData : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorAirportRegionCarFareId() {
		return vendorAirportRegionCarFareId;
	}

	public void setVendorAirportRegionCarFareId(String vendorAirportRegionCarFareId) {
		this.vendorAirportRegionCarFareId = vendorAirportRegionCarFareId;
	}

	public String getVendorAirportRegionId() {
		return vendorAirportRegionId;
	}

	public void setVendorAirportRegionId(String vendorAirportRegionId) {
		this.vendorAirportRegionId = vendorAirportRegionId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}