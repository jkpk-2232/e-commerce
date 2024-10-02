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
import com.utils.myhub.UnifiedHistoryUtils;
import com.webapp.ProjectConstants;
import com.webapp.daos.InvoiceDao;
import com.webapp.daos.TourDao;

public class InvoiceModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(InvoiceModel.class);

	private String invoiceId;
	private String tourId;
	private long userTourId;
	private String destiAddess;
	private String sourceAddess;
	private String transactionId;
	private double initialFare;
	private double perKmFare;
	private double perMinuteFare;
	private double bookingFees;
	private double discount;
	private double minimumFare;
	private double subTotal;
	private double total;
	private double distance;
	private double duration;
	private double avgSpeed;
	private double fine;
	private double distanceFare;
	private double timeFare;
	private double baseFare;
	private double refundAmount;
	private double charges;
	private double percentage;
	private double driverAmount;
	private String paymentMode;
	private double cashCollected;
	private double cashToBeCollected;
	private boolean cashReceived;
	private String paymentStatus;
	private boolean isCard;
	private boolean isCashNotReceived;
	private boolean isRefunded;
	private String promoCodeId;
	private boolean isPromoCodeApplied;
	private double promoDiscount;
	private double waitingTime;
	private double usedCredits;
	private double finalAmountCollected;
	private double arrivedWaitingTime;
	private double tollAmount;
	private double adminSettlementAmount;
	private int monthOnly;
	private int weekOnly;
	private int dayOnly;
	private long tripCount;
	private double totalEarning;
	private double avgRating;
	private String staticMapImgUrl;
	private boolean isSurgePriceApplied;
	private String surgePriceId;
	private double surgePrice;
	private double totalWithSurge;
	private double surgeFare;

	private boolean isPaymentPaid;
	private String photoUrl;
	private String pEmail;
	private String pPhone;
	private String pPhoneCode;

	private double totalTaxAmount;
	private double arrivedWaitingTimeFare;

	private boolean isRentalBooking;
	private String rentalPackageId;
	private long rentalPackageTime;

	private double updatedAmountCollected;
	private String remark;
	private String remarkBy;

	private double fareAfterSpecificKm;
	private double kmToIncreaseFare;
	private double distanceBeforeSpecificKm;
	private double distanceAfterSpecificKm;
	private double distanceFareBeforeSpecificKm;
	private double distanceFareAfterSpecificKm;
	private double markupFare;

	private boolean isPassReleasedByAdmin;
	private long passengerReleasedAt;
	private String passengerReleasedBy;

	private String driverFullName;
	private String passengerFullName;
	private String bookingType;

	public int generateInvoice(String userId, boolean isCancelledTrip) {

		int status = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);
		TourDao tourDao = session.getMapper(TourDao.class);

		TourModel tourModel = TourModel.getTourDetailsByTourId(this.tourId);

		try {

			double totalDummy = this.getTotal();

			double driverAmountDummy = 0;

			if (totalDummy > this.getPromoDiscount()) {

				// driverAmountDummy = (tourModel.getPercentage() * (totalDummy -
				// this.getPromoDiscount() - this.bookingFees)) / 100;

				driverAmountDummy = (tourModel.getPercentage() * (totalDummy - this.getPromoDiscount())) / 100;

				if (driverAmountDummy < 0) {
					driverAmountDummy = 0;
				}

			} else {
				driverAmountDummy = 0;
				// driverAmountDummy = (tourModel.getPercentage() * (totalDummy)) / 100;
			}

			this.percentage = tourModel.getPercentage();

			tourModel.setCharges(this.getCharges());
			tourModel.setDriverAmount(driverAmountDummy);
			tourModel.setPromoDiscount(this.getPromoDiscount());
			tourModel.setTotal(this.getTotal());
			tourModel.setUsedCredits(this.getUsedCredits());
			tourModel.setPromoCodeApplied(this.isPromoCodeApplied);
			tourModel.setPromoCodeId(this.promoCodeId);
			tourModel.setDistance(this.distance);

			tourDao.updateChargesAndDriverAmount(tourModel);

			this.setDriverAmount(driverAmountDummy);

			/*
			 * adminSettlementAmount amount if positive then its receivable amount, if
			 * negative then its payable amount and if zero then No settlement
			 */
			// Calculate admin settlement amount
			if (this.paymentMode.equalsIgnoreCase(ProjectConstants.CASH)) {

				if (isCancelledTrip) {

					this.setAdminSettlementAmount(0 - this.driverAmount);

				} else {

					if (this.finalAmountCollected > this.driverAmount) {

						this.setAdminSettlementAmount(this.finalAmountCollected - this.driverAmount);

					} else {

						this.setAdminSettlementAmount(this.finalAmountCollected - this.driverAmount);
					}
				}

			} else {

				this.setAdminSettlementAmount(0 - this.driverAmount);
			}

			this.setInvoiceId(UUIDGenerator.generateUUID());
			this.setCreatedBy(userId);
			this.setUpdatedBy(userId);
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(this.getCreatedAt());

			this.bookingFees = tourModel.getBookingFees();
			this.initialFare = tourModel.getInitialFare();
			this.discount = tourModel.getDiscount();
			this.perKmFare = tourModel.getPerKmFare();
			this.perMinuteFare = tourModel.getPerMinuteFare();
			this.minimumFare = tourModel.getMinimumFare();

			this.kmToIncreaseFare = tourModel.getKmToIncreaseFare();
			this.fareAfterSpecificKm = tourModel.getFareAfterSpecificKm();

			status = invoiceDao.generateInvoice(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during generateInvoice : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateChargesAndDriverAmount(tourModel);

		return status;
	}

	public int generateInvoiceNew(String userId) {

		int status = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();

		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);
		TourDao tourDao = session.getMapper(TourDao.class);

		TourModel tourModel = TourModel.getTourDetailsByTourId(this.tourId);

		try {

			this.percentage = tourModel.getPercentage();

			tourModel.setCharges(this.getCharges());
			tourModel.setDriverAmount(this.getDriverAmount());
			tourModel.setPromoDiscount(this.getPromoDiscount());
			tourModel.setTotal(this.getTotal());
			tourModel.setUsedCredits(this.getUsedCredits());
			tourModel.setPromoCodeApplied(this.isPromoCodeApplied);
			tourModel.setPromoCodeId(this.promoCodeId);
			tourModel.setDistance(this.distance);
			tourModel.setFinalAmountCollected(this.getFinalAmountCollected());
			tourModel.setTollAmount(this.getTollAmount());

			tourDao.updateChargesAndDriverAmount(tourModel);

			// Calculate admin settlement amount
			/*
			 * adminSettlementAmount amount if positive then its receivable amount, if
			 * negative then its payable amount and if zero then No settlement
			 */

			if (this.paymentMode.equalsIgnoreCase(ProjectConstants.CASH)) {

				if (this.finalAmountCollected > this.driverAmount) {
					this.setAdminSettlementAmount(this.finalAmountCollected - this.driverAmount);
				} else {
					this.setAdminSettlementAmount(this.finalAmountCollected - this.driverAmount);
				}

			} else {

				this.setAdminSettlementAmount(0 - this.driverAmount);
			}

			this.setInvoiceId(UUIDGenerator.generateUUID());
			this.setCreatedBy(userId);
			this.setUpdatedBy(userId);
			this.setCreatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedAt(this.getCreatedAt());

			this.bookingFees = tourModel.getBookingFees();
			this.initialFare = tourModel.getInitialFare();
			this.discount = tourModel.getDiscount();
			this.perKmFare = tourModel.getPerKmFare();
			this.perMinuteFare = tourModel.getPerMinuteFare();
			this.minimumFare = tourModel.getMinimumFare();

			this.kmToIncreaseFare = tourModel.getKmToIncreaseFare();
			this.fareAfterSpecificKm = tourModel.getFareAfterSpecificKm();

			if (tourModel.isSurgePriceApplied()) {

				this.isSurgePriceApplied = true;
				this.surgePriceId = tourModel.getSurgePriceId();
				this.surgePrice = tourModel.getSurgePrice();

			} else {

				this.isSurgePriceApplied = false;
				this.surgePriceId = "-1";
				this.surgePrice = 1;
			}

			if (tourModel.isRentalBooking()) {

				this.isRentalBooking = true;
				this.rentalPackageId = tourModel.getRentalPackageId();
				this.rentalPackageTime = tourModel.getRentalPackageTime();

			} else {

				this.isRentalBooking = false;
				this.rentalPackageId = "-1";
				this.rentalPackageTime = 0;
			}

			status = invoiceDao.generateInvoice(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during generateInvoice : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateChargesAndDriverAmount(tourModel);

		return status;
	}

	public static InvoiceModel getInvoiceByTourId(String tourId) {

		InvoiceModel invoiceModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			invoiceModel = invoiceDao.getInvoiceByTourId(tourId);

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getInvoiceByTourId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		return invoiceModel;

	}

	public static int getTotalInvoicesByUserId(String userId, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("userId", userId);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			count = invoiceDao.getTotalInvoicesByUserId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getInvoiceByTourId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static int getTotalInvoicesAmountByDriverId(String userId) {

		int count = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			count = invoiceDao.getTotalInvoicesAmountByDriverId(userId);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalInvoicesAmountByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static List<InvoiceModel> getInvoiceListBySearch(int start, int length, String order, String userId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("userId", userId);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			invoiceList = invoiceDao.getInvoiceListBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return invoiceList;
	}

	public static int getTotalCountBySearch(String userId, String globalSearchString) {

		int count = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			count = invoiceDao.getTotalCountBySearch(userId, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int updateTransactionId(String transactionId, String tourId) {

		int count = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			count = invoiceDao.updateTransactionId(transactionId, tourId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTransactionId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int updateRefundAndTotalAmount(String tourId, double refundAmount, String userId) {

		int count = 0;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			count = invoiceDao.updateRefundAndTotalAmount(tourId, refundAmount, DateUtils.nowAsGmtMillisec());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRefundAndTotalAmount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static double getAllDriverTotalPayableAmount(long startDate, long endDate, String userId) {

		double count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			count = invoiceDao.getAllDriverTotalPayableAmount(startDate, endDate, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllDriverTotalPayableAmount :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public int setRefundedStatus(String userId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			count = invoiceDao.setRefundedStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during setRefundedStatus :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static double getAllRefundedTripsAmount(long startDate, long endDate, List<String> assignedRegionList) {

		double count;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			count = invoiceDao.getAllRefundedTripsAmount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getAllRefundedTripsAmount :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static int getTotalInvoicesByDate(long startDatelong, long endDatelong, List<String> assignedRegionList) {

		int count;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			count = invoiceDao.getTotalInvoicesByDate(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalInvoicesByDate :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<InvoiceModel> getRefundedIvoiceListBySearchAndDateReports(int start, int length, String order, String userId, String globalSearchString, long startDatelong, long endDatelong, List<String> assignedRegionList) {

		List<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("userId", userId);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("startDatelong", startDatelong);
		inputMap.put("endDatelong", endDatelong);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			invoiceList = invoiceDao.getRefundedIvoiceListBySearchAndDateReports(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateUser :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return invoiceList;
	}

	public static List<InvoiceModel> getMonthlyDriverDashboardSummary(int offset, int length, String driverId, long startDateLong, long endDateLong) {
		List<InvoiceModel> invoiceModelList = new ArrayList<InvoiceModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("offset", offset);
			inputMap.put("length", length);
			inputMap.put("driverId", driverId);
			inputMap.put("startDateLong", startDateLong);
			inputMap.put("endDateLong", endDateLong);

			invoiceModelList = invoiceDao.getMonthlyDriverDashboardSummary(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMonthlyDriverDashboardSummary : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return invoiceModelList;
	}

	public static List<InvoiceModel> getWeeklyDriverDashboardSummary(int offset, int length, String driverId, long startDateLong, long endDateLong) {
		List<InvoiceModel> invoiceModelList = new ArrayList<InvoiceModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("offset", offset);
			inputMap.put("length", length);
			inputMap.put("driverId", driverId);
			inputMap.put("startDateLong", startDateLong);
			inputMap.put("endDateLong", endDateLong);

			invoiceModelList = invoiceDao.getWeeklyDriverDashboardSummary(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMonthlyDriverDashboardSummary : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return invoiceModelList;
	}

	public static List<InvoiceModel> getDailyDriverDashboardSummary(int offset, int length, String driverId, long startDateLong, long endDateLong) {
		List<InvoiceModel> invoiceModelList = new ArrayList<InvoiceModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("offset", offset);
			inputMap.put("length", length);
			inputMap.put("driverId", driverId);
			inputMap.put("startDateLong", startDateLong);
			inputMap.put("endDateLong", endDateLong);

			invoiceModelList = invoiceDao.getDailyDriverDashboardSummary(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMonthlyDriverDashboardSummary : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return invoiceModelList;
	}

	public static double getTotalAdminSettlementAmount(long startDate, long endDate, String userId, List<String> assignedRegionList) {

		double totalPayOrCollectAmount = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("startDate", startDate);
		inputMap.put("endDate", endDate);
		inputMap.put("userId", userId);
		inputMap.put("assignedRegionList", assignedRegionList);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			totalPayOrCollectAmount = invoiceDao.getTotalAdminSettlementAmount(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalAdminSettlementAmount :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return totalPayOrCollectAmount;
	}

	public static int updateStaticMapImgUrlByTourId(String tourId, String staticMapImgUrl) {

		int count = -1;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {
			count = invoiceDao.updateStaticMapImgUrlByTourId(tourId, staticMapImgUrl, DateUtils.nowAsGmtMillisec());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateStaticMapImgUrlByTourId :", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static InvoiceModel getPendingPaymentTourByPassengerId(String passengerId) {

		InvoiceModel invoiceModel = null;
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			invoiceModel = invoiceDao.getPendingPaymentTourByPassengerId(passengerId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getPendingPaymentTourByPassengerId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return invoiceModel;
	}

	public static int updatePaymentPaidStatus(String tourId, boolean paymentPaidStatus, boolean isPassReleasedByAdmin, String loggedInUserId) {

		int count = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("tourId", tourId);
		inputMap.put("paymentPaidStatus", paymentPaidStatus);
		inputMap.put("updatedAt", DateUtils.nowAsGmtMillisec());
		inputMap.put("isPassReleasedByAdmin", isPassReleasedByAdmin);
		inputMap.put("passengerReleasedAt", DateUtils.nowAsGmtMillisec());
		inputMap.put("passengerReleasedBy", loggedInUserId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			count = invoiceDao.updatePaymentPaidStatus(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updatePaymentPaidStatus :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updateUpdatedAmountCollectWithRemark(String loggedInUserId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		this.remarkBy = loggedInUserId;
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {

			count = invoiceDao.updateUpdatedAmountCollectWithRemark(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateUpdatedAmountCollectWithRemark :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
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

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public String getDestiAddess() {
		return destiAddess;
	}

	public void setDestiAddess(String destiAddess) {
		this.destiAddess = destiAddess;
	}

	public String getSourceAddess() {
		return sourceAddess;
	}

	public void setSourceAddess(String sourceAddess) {
		this.sourceAddess = sourceAddess;
	}

	public long getUserTourId() {
		return userTourId;
	}

	public void setUserTourId(long userTourId) {
		this.userTourId = userTourId;
	}

	public double getFine() {
		return fine;
	}

	public void setFine(double fine) {
		this.fine = fine;
	}

	public double getDistanceFare() {
		return distanceFare;
	}

	public void setDistanceFare(double distanceFare) {
		this.distanceFare = distanceFare;
	}

	public double getTimeFare() {
		return timeFare;
	}

	public void setTimeFare(double timeFare) {
		this.timeFare = timeFare;
	}

	public double getMinimumFare() {
		return minimumFare;
	}

	public void setMinimumFare(double minimumFare) {
		this.minimumFare = minimumFare;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public boolean isRefunded() {
		return isRefunded;
	}

	public void setRefunded(boolean isRefunded) {
		this.isRefunded = isRefunded;
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

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public double getCashCollected() {
		return cashCollected;
	}

	public void setCashCollected(double cashCollected) {
		this.cashCollected = cashCollected;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public boolean isCard() {
		return isCard;
	}

	public void setCard(boolean isCard) {
		this.isCard = isCard;
	}

	public boolean isCashNotReceived() {
		return isCashNotReceived;
	}

	public void setCashNotReceived(boolean isCashNotReceived) {
		this.isCashNotReceived = isCashNotReceived;
	}

	public double getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(double promoDiscount) {
		this.promoDiscount = promoDiscount;
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

	public double getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}

	public boolean isCashReceived() {
		return cashReceived;
	}

	public void setCashReceived(boolean cashReceived) {
		this.cashReceived = cashReceived;
	}

	public double getUsedCredits() {
		return usedCredits;
	}

	public void setUsedCredits(double usedCredits) {
		this.usedCredits = usedCredits;
	}

	public double getCashToBeCollected() {
		return cashToBeCollected;
	}

	public void setCashToBeCollected(double cashToBeCollected) {
		this.cashToBeCollected = cashToBeCollected;
	}

	public double getFinalAmountCollected() {
		return finalAmountCollected;
	}

	public void setFinalAmountCollected(double finalAmountCollected) {
		this.finalAmountCollected = finalAmountCollected;
	}

	public double getArrivedWaitingTime() {
		return arrivedWaitingTime;
	}

	public void setArrivedWaitingTime(double arrivedWaitingTime) {
		this.arrivedWaitingTime = arrivedWaitingTime;
	}

	public double getTollAmount() {
		return tollAmount;
	}

	public void setTollAmount(double tollAmount) {
		this.tollAmount = tollAmount;
	}

	public double getAdminSettlementAmount() {
		return adminSettlementAmount;
	}

	public void setAdminSettlementAmount(double adminSettlementAmount) {
		this.adminSettlementAmount = adminSettlementAmount;
	}

	public int getMonthOnly() {
		return monthOnly;
	}

	public void setMonthOnly(int monthOnly) {
		this.monthOnly = monthOnly;
	}

	public int getWeekOnly() {
		return weekOnly;
	}

	public void setWeekOnly(int weekOnly) {
		this.weekOnly = weekOnly;
	}

	public int getDayOnly() {
		return dayOnly;
	}

	public void setDayOnly(int dayOnly) {
		this.dayOnly = dayOnly;
	}

	public long getTripCount() {
		return tripCount;
	}

	public void setTripCount(long tripCount) {
		this.tripCount = tripCount;
	}

	public double getTotalEarning() {
		return totalEarning;
	}

	public void setTotalEarning(double totalEarning) {
		this.totalEarning = totalEarning;
	}

	public double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(double avgRating) {
		this.avgRating = avgRating;
	}

	public String getStaticMapImgUrl() {
		return staticMapImgUrl;
	}

	public void setStaticMapImgUrl(String staticMapImgUrl) {
		this.staticMapImgUrl = staticMapImgUrl;
	}

	public double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(double baseFare) {
		this.baseFare = baseFare;
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

	public double getSurgeFare() {
		return surgeFare;
	}

	public void setSurgeFare(double surgeFare) {
		this.surgeFare = surgeFare;
	}

	public boolean isPaymentPaid() {
		return isPaymentPaid;
	}

	public void setPaymentPaid(boolean isPaymentPaid) {
		this.isPaymentPaid = isPaymentPaid;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public String getpPhoneCode() {
		return pPhoneCode;
	}

	public void setpPhoneCode(String pPhoneCode) {
		this.pPhoneCode = pPhoneCode;
	}

	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public double getArrivedWaitingTimeFare() {
		return arrivedWaitingTimeFare;
	}

	public void setArrivedWaitingTimeFare(double arrivedWaitingTimeFare) {
		this.arrivedWaitingTimeFare = arrivedWaitingTimeFare;
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

	public double getUpdatedAmountCollected() {
		return updatedAmountCollected;
	}

	public void setUpdatedAmountCollected(double updatedAmountCollected) {
		this.updatedAmountCollected = updatedAmountCollected;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkBy() {
		return remarkBy;
	}

	public void setRemarkBy(String remarkBy) {
		this.remarkBy = remarkBy;
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

	public double getDistanceBeforeSpecificKm() {
		return distanceBeforeSpecificKm;
	}

	public void setDistanceBeforeSpecificKm(double distanceBeforeSpecificKm) {
		this.distanceBeforeSpecificKm = distanceBeforeSpecificKm;
	}

	public double getDistanceAfterSpecificKm() {
		return distanceAfterSpecificKm;
	}

	public void setDistanceAfterSpecificKm(double distanceAfterSpecificKm) {
		this.distanceAfterSpecificKm = distanceAfterSpecificKm;
	}

	public double getDistanceFareBeforeSpecificKm() {
		return distanceFareBeforeSpecificKm;
	}

	public void setDistanceFareBeforeSpecificKm(double distanceFareBeforeSpecificKm) {
		this.distanceFareBeforeSpecificKm = distanceFareBeforeSpecificKm;
	}

	public double getDistanceFareAfterSpecificKm() {
		return distanceFareAfterSpecificKm;
	}

	public void setDistanceFareAfterSpecificKm(double distanceFareAfterSpecificKm) {
		this.distanceFareAfterSpecificKm = distanceFareAfterSpecificKm;
	}

	public double getMarkupFare() {
		return markupFare;
	}

	public void setMarkupFare(double markupFare) {
		this.markupFare = markupFare;
	}

	public boolean isPassReleasedByAdmin() {
		return isPassReleasedByAdmin;
	}

	public void setPassReleasedByAdmin(boolean isPassReleasedByAdmin) {
		this.isPassReleasedByAdmin = isPassReleasedByAdmin;
	}

	public long getPassengerReleasedAt() {
		return passengerReleasedAt;
	}

	public void setPassengerReleasedAt(long passengerReleasedAt) {
		this.passengerReleasedAt = passengerReleasedAt;
	}

	public String getPassengerReleasedBy() {
		return passengerReleasedBy;
	}

	public void setPassengerReleasedBy(String passengerReleasedBy) {
		this.passengerReleasedBy = passengerReleasedBy;
	}

	public String getDriverFullName() {
		return driverFullName;
	}

	public void setDriverFullName(String driverFullName) {
		this.driverFullName = driverFullName;
	}

	public String getPassengerFullName() {
		return passengerFullName;
	}

	public void setPassengerFullName(String passengerFullName) {
		this.passengerFullName = passengerFullName;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public static double getTotalEarningsPerDay(String driverId, long startOfDay, long endOfDay) {
		
		double totalAmount = 0;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		InvoiceDao invoiceDao = session.getMapper(InvoiceDao.class);

		try {

			totalAmount = invoiceDao.getTotalEarningsPerDay(driverId, startOfDay, endOfDay);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalEarningsPerDay :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return totalAmount;
	}
}