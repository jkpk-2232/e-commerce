package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.RideLaterSettingsDao;

public class RideLaterSettingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(RideLaterSettingsModel.class);

	private String rideLaterSettingsId;
	private long minBookingTime;
	private long maxBookingTime;
	private long driverJobRequestTime;
	private long driverAllocateBeforeTime;
	private long driverAllocateAfterTime;
	private long passengerTourBeforeTime;
	private long passengerTourAfterTime;
	
	private int takeBookingForNextXDays;
	private int takeBookingMaxNumberAllowed;

	public int getTakeBookingForNextXDays() {
		return takeBookingForNextXDays;
	}

	public void setTakeBookingForNextXDays(int takeBookingForNextXDays) {
		this.takeBookingForNextXDays = takeBookingForNextXDays;
	}

	public int getTakeBookingMaxNumberAllowed() {
		return takeBookingMaxNumberAllowed;
	}

	public void setTakeBookingMaxNumberAllowed(int takeBookingMaxNumberAllowed) {
		this.takeBookingMaxNumberAllowed = takeBookingMaxNumberAllowed;
	}

	public String getRideLaterSettingsId() {
		return rideLaterSettingsId;
	}

	public void setRideLaterSettingsId(String rideLaterSettingsId) {
		this.rideLaterSettingsId = rideLaterSettingsId;
	}

	public long getMinBookingTime() {
		return minBookingTime;
	}

	public void setMinBookingTime(long minBookingTime) {
		this.minBookingTime = minBookingTime;
	}

	public long getMaxBookingTime() {
		return maxBookingTime;
	}

	public void setMaxBookingTime(long maxBookingTime) {
		this.maxBookingTime = maxBookingTime;
	}

	public long getDriverJobRequestTime() {
		return driverJobRequestTime;
	}

	public void setDriverJobRequestTime(long driverJobRequestTime) {
		this.driverJobRequestTime = driverJobRequestTime;
	}

	public long getDriverAllocateBeforeTime() {
		return driverAllocateBeforeTime;
	}

	public void setDriverAllocateBeforeTime(long driverAllocateBeforeTime) {
		this.driverAllocateBeforeTime = driverAllocateBeforeTime;
	}

	public long getDriverAllocateAfterTime() {
		return driverAllocateAfterTime;
	}

	public void setDriverAllocateAfterTime(long driverAllocateAfterTime) {
		this.driverAllocateAfterTime = driverAllocateAfterTime;
	}

	public long getPassengerTourBeforeTime() {
		return passengerTourBeforeTime;
	}

	public void setPassengerTourBeforeTime(long passengerTourBeforeTime) {
		this.passengerTourBeforeTime = passengerTourBeforeTime;
	}

	public long getPassengerTourAfterTime() {
		return passengerTourAfterTime;
	}

	public void setPassengerTourAfterTime(long passengerTourAfterTime) {
		this.passengerTourAfterTime = passengerTourAfterTime;
	}

	public static RideLaterSettingsModel getRideLaterSettingsDetails() {

		RideLaterSettingsModel rideLaterSettingsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RideLaterSettingsDao rideLaterSettingsDao = session.getMapper(RideLaterSettingsDao.class);

		try {
			rideLaterSettingsModel = rideLaterSettingsDao.getRideLaterSettingsDetails();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRideLaterSettingsDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return rideLaterSettingsModel;
	}

	public int updateRideLaterSettings() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RideLaterSettingsDao rideLaterSettingsDao = session.getMapper(RideLaterSettingsDao.class);

		try {
			status = rideLaterSettingsDao.updateRideLaterSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRideLaterSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}
}