package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.AppointmentSettingDao;

public class AppointmentSettingModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AppointmentSettingModel.class);

	private String appointmentSettingId;
	private String serviceId;
	private long minBookingTime;
	private long maxBookingTime;
	private int freeCancellationTimeMins;
	private int cronJobExpireTimeMins;

	private String serviceName;

	public AppointmentSettingModel() {

	}

	public AppointmentSettingModel(String serviceId, String userId) {
		this.appointmentSettingId = UUIDGenerator.generateUUID();
		this.serviceId = serviceId;
		this.minBookingTime = 1440; // 1 day
		this.maxBookingTime = 14400;// 10 days
		this.freeCancellationTimeMins = 1440;// 1 day
		this.cronJobExpireTimeMins = 1440;// 1 day
	}

	public String insertAppointmentSettings(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentSettingDao appointmentSettingDao = session.getMapper(AppointmentSettingDao.class);

		this.appointmentSettingId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			appointmentSettingDao.insertAppointmentSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertAppointmentSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.appointmentSettingId;
	}

	public void updateAppointmentSettings(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentSettingDao appointmentSettingDao = session.getMapper(AppointmentSettingDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			appointmentSettingDao.updateAppointmentSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAppointmentSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getAppointmentSettingCount(String serviceId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentSettingDao appointmentSettingDao = session.getMapper(AppointmentSettingDao.class);

		try {
			count = appointmentSettingDao.getAppointmentSettingCount(serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentSettingCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<AppointmentSettingModel> getAppointmentSettingSearch(String searchKey, int start, int length, String orderColumn, String serviceId) {

		List<AppointmentSettingModel> orderSettingList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentSettingDao appointmentSettingDao = session.getMapper(AppointmentSettingDao.class);

		try {
			orderSettingList = appointmentSettingDao.getAppointmentSettingSearch(searchKey, start, length, orderColumn, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentSettingSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderSettingList;
	}

	public static int getAppointmentSettingSearchCount(String searchKey, String serviceId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentSettingDao appointmentSettingDao = session.getMapper(AppointmentSettingDao.class);

		try {
			count = appointmentSettingDao.getAppointmentSettingSearchCount(searchKey, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentSettingSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static AppointmentSettingModel getAppointmentSettingDetailsByServiceId(String serviceId) {

		AppointmentSettingModel orderSettingModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentSettingDao appointmentSettingDao = session.getMapper(AppointmentSettingDao.class);

		try {
			orderSettingModel = appointmentSettingDao.getAppointmentSettingDetailsByServiceId(serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentSettingDetailsByServiceId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderSettingModel;
	}

	public String getAppointmentSettingId() {
		return appointmentSettingId;
	}

	public void setAppointmentSettingId(String appointmentSettingId) {
		this.appointmentSettingId = appointmentSettingId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public int getFreeCancellationTimeMins() {
		return freeCancellationTimeMins;
	}

	public void setFreeCancellationTimeMins(int freeCancellationTimeMins) {
		this.freeCancellationTimeMins = freeCancellationTimeMins;
	}

	public int getCronJobExpireTimeMins() {
		return cronJobExpireTimeMins;
	}

	public void setCronJobExpireTimeMins(int cronJobExpireTimeMins) {
		this.cronJobExpireTimeMins = cronJobExpireTimeMins;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}