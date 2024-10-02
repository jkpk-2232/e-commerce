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
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.TourReferrerBenefitDao;

public class TourReferrerBenefitModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TourReferrerBenefitModel.class);

	private String tourReferrerBenefitId;
	private String driverReferralCodeLogId;
	private String tourId;
	private double referrerDriverPercentage;
	private double referrerDriverBenefit;
	private String tourReferrerType;

	private String userTourId;
	private String driverName;
	private String driverEmail;
	private String driverPhoneNumber;
	private String passengerName;
	private String passengerEmail;
	private String passengerPhoneNumber;

	private String driverPhotoUrl;
	private String passengerPhotoUrl;

	public String getTourReferrerBenefitId() {
		return tourReferrerBenefitId;
	}

	public void setTourReferrerBenefitId(String tourReferrerBenefitId) {
		this.tourReferrerBenefitId = tourReferrerBenefitId;
	}

	public String getDriverReferralCodeLogId() {
		return driverReferralCodeLogId;
	}

	public void setDriverReferralCodeLogId(String driverReferralCodeLogId) {
		this.driverReferralCodeLogId = driverReferralCodeLogId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public double getReferrerDriverPercentage() {
		return referrerDriverPercentage;
	}

	public void setReferrerDriverPercentage(double referrerDriverPercentage) {
		this.referrerDriverPercentage = referrerDriverPercentage;
	}

	public double getReferrerDriverBenefit() {
		return referrerDriverBenefit;
	}

	public void setReferrerDriverBenefit(double referrerDriverBenefit) {
		this.referrerDriverBenefit = referrerDriverBenefit;
	}

	public String getTourReferrerType() {
		return tourReferrerType;
	}

	public void setTourReferrerType(String tourReferrerType) {
		this.tourReferrerType = tourReferrerType;
	}

	public String getUserTourId() {
		return userTourId;
	}

	public void setUserTourId(String userTourId) {
		this.userTourId = userTourId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverEmail() {
		return driverEmail;
	}

	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}

	public String getDriverPhoneNumber() {
		return driverPhoneNumber;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		this.driverPhoneNumber = driverPhoneNumber;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getPassengerEmail() {
		return passengerEmail;
	}

	public void setPassengerEmail(String passengerEmail) {
		this.passengerEmail = passengerEmail;
	}

	public String getPassengerPhoneNumber() {
		return passengerPhoneNumber;
	}

	public void setPassengerPhoneNumber(String passengerPhoneNumber) {
		this.passengerPhoneNumber = passengerPhoneNumber;
	}

	public String getDriverPhotoUrl() {
		return driverPhotoUrl;
	}

	public void setDriverPhotoUrl(String driverPhotoUrl) {
		this.driverPhotoUrl = driverPhotoUrl;
	}

	public String getPassengerPhotoUrl() {
		return passengerPhotoUrl;
	}

	public void setPassengerPhotoUrl(String passengerPhotoUrl) {
		this.passengerPhotoUrl = passengerPhotoUrl;
	}

	public int addTourReferrerBenefit(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourReferrerBenefitDao tourReferrerBenefitDao = session.getMapper(TourReferrerBenefitDao.class);

		try {

			this.tourReferrerBenefitId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = tourReferrerBenefitDao.addTourReferrerBenefit(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addDriverReferralCodeLog : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;

	}

	public static TourReferrerBenefitModel getTourReferrerBenefitByTourId(String tourId) {

		TourReferrerBenefitModel tourReferrerBenefitModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("tourId", tourId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourReferrerBenefitDao tourReferrerBenefitDao = session.getMapper(TourReferrerBenefitDao.class);

		try {

			tourReferrerBenefitModel = tourReferrerBenefitDao.getTourReferrerBenefitByTourId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTourReferrerBenefitByTourId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return tourReferrerBenefitModel;
	}

	public static int getTotalTourReferrerBenefitCountForSearch(String driverId, long startDate, long endDate) {

		int tourReferralLogsCount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourReferrerBenefitDao tourReferrerBenefitDao = session.getMapper(TourReferrerBenefitDao.class);

		try {

			tourReferralLogsCount = tourReferrerBenefitDao.getTotalTourReferrerBenefitCountForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalTourReferrerBenefitCountForSearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return tourReferralLogsCount;
	}

	public static List<TourReferrerBenefitModel> getTourReferrerBenefitListForSearch(String driverId, int start, int length, String order, String globalSearchString, long startDate, long endDate) {

		List<TourReferrerBenefitModel> tourReferrerBenefitModelList = new ArrayList<TourReferrerBenefitModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourReferrerBenefitDao tourReferrerBenefitDao = session.getMapper(TourReferrerBenefitDao.class);

		try {

			tourReferrerBenefitModelList = tourReferrerBenefitDao.getTourReferrerBenefitListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTourReferrerBenefitListForSearch :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return tourReferrerBenefitModelList;
	}

	public static double getTotalDriverBenefitByDriverId(String driverId) {

		double totalDriverBenefit = 0.0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourReferrerBenefitDao tourReferrerBenefitDao = session.getMapper(TourReferrerBenefitDao.class);

		try {

			totalDriverBenefit = tourReferrerBenefitDao.getTotalDriverBenefitByDriverId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalDriverBenefitByDriverId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return totalDriverBenefit;
	}

	public static List<TourReferrerBenefitModel> getDriverReferrerlListByDriverId(String driverId, long start, long length) {

		List<TourReferrerBenefitModel> tourReferrerBenefitModelList = new ArrayList<TourReferrerBenefitModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("driverId", driverId);
		inputMap.put("start", start);
		inputMap.put("length", length);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourReferrerBenefitDao tourReferrerBenefitDao = session.getMapper(TourReferrerBenefitDao.class);

		try {

			tourReferrerBenefitModelList = tourReferrerBenefitDao.getDriverReferrerlListByDriverId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getDriverReferrerlListByDriverId :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return tourReferrerBenefitModelList;
	}
}