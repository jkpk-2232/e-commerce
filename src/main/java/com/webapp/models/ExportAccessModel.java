package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.ExportAccessDao;

public class ExportAccessModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ExportAccessModel.class);

	private String exportAccessId;
	private String userId;
	private boolean isBookingExport;
	private boolean isPassengerExport;
	private boolean isRideLaterExport;
	private boolean isCriticalRideLaterExport;
	private boolean isDriverIncomeReportExport;
	private boolean isRefundReportExport;
	private boolean isDriverDutyReportExport;
	private boolean isCcavenueLogReportExport;
	private boolean isDriverDriveReportExport;
	private boolean isDriverBenefitReportExport;
	private boolean isActive;
	private boolean isDriverAccountExport;
	private boolean isVendorAccountExport;
	private boolean isVendorDriverAccountExport;
	private boolean isDriverSubscriptionExport;
	private boolean isDriverTransactionHistoryExport;
	private boolean isPhonepeLogReportExport;
	private boolean isWarehouseExport;

	public String getExportAccessId() {
		return exportAccessId;
	}

	public void setExportAccessId(String exportAccessId) {
		this.exportAccessId = exportAccessId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isBookingExport() {
		return isBookingExport;
	}

	public void setBookingExport(boolean isBookingExport) {
		this.isBookingExport = isBookingExport;
	}

	public boolean isPassengerExport() {
		return isPassengerExport;
	}

	public void setPassengerExport(boolean isPassengerExport) {
		this.isPassengerExport = isPassengerExport;
	}

	public boolean isRideLaterExport() {
		return isRideLaterExport;
	}

	public void setRideLaterExport(boolean isRideLaterExport) {
		this.isRideLaterExport = isRideLaterExport;
	}

	public boolean isCriticalRideLaterExport() {
		return isCriticalRideLaterExport;
	}

	public void setCriticalRideLaterExport(boolean isCriticalRideLaterExport) {
		this.isCriticalRideLaterExport = isCriticalRideLaterExport;
	}

	public boolean isDriverIncomeReportExport() {
		return isDriverIncomeReportExport;
	}

	public void setDriverIncomeReportExport(boolean isDriverIncomeReportExport) {
		this.isDriverIncomeReportExport = isDriverIncomeReportExport;
	}

	public boolean isRefundReportExport() {
		return isRefundReportExport;
	}

	public void setRefundReportExport(boolean isRefundReportExport) {
		this.isRefundReportExport = isRefundReportExport;
	}

	public boolean isDriverDutyReportExport() {
		return isDriverDutyReportExport;
	}

	public void setDriverDutyReportExport(boolean isDriverDutyReportExport) {
		this.isDriverDutyReportExport = isDriverDutyReportExport;
	}

	public boolean isCcavenueLogReportExport() {
		return isCcavenueLogReportExport;
	}

	public void setCcavenueLogReportExport(boolean isCcavenueLogReportExport) {
		this.isCcavenueLogReportExport = isCcavenueLogReportExport;
	}

	public boolean isDriverDriveReportExport() {
		return isDriverDriveReportExport;
	}

	public void setDriverDriveReportExport(boolean isDriverDriveReportExport) {
		this.isDriverDriveReportExport = isDriverDriveReportExport;
	}

	public boolean isDriverBenefitReportExport() {
		return isDriverBenefitReportExport;
	}

	public void setDriverBenefitReportExport(boolean isDriverBenefitReportExport) {
		this.isDriverBenefitReportExport = isDriverBenefitReportExport;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDriverAccountExport() {
		return isDriverAccountExport;
	}

	public void setDriverAccountExport(boolean isDriverAccountExport) {
		this.isDriverAccountExport = isDriverAccountExport;
	}

	public boolean isVendorAccountExport() {
		return isVendorAccountExport;
	}

	public void setVendorAccountExport(boolean isVendorAccountExport) {
		this.isVendorAccountExport = isVendorAccountExport;
	}

	public boolean isVendorDriverAccountExport() {
		return isVendorDriverAccountExport;
	}

	public void setVendorDriverAccountExport(boolean isVendorDriverAccountExport) {
		this.isVendorDriverAccountExport = isVendorDriverAccountExport;
	}

	public boolean isDriverSubscriptionExport() {
		return isDriverSubscriptionExport;
	}

	public void setDriverSubscriptionExport(boolean isDriverSubscriptionExport) {
		this.isDriverSubscriptionExport = isDriverSubscriptionExport;
	}

	public boolean isDriverTransactionHistoryExport() {
		return isDriverTransactionHistoryExport;
	}

	public void setDriverTransactionHistoryExport(boolean isDriverTransactionHistoryExport) {
		this.isDriverTransactionHistoryExport = isDriverTransactionHistoryExport;
	}

	public String addExportAccessDetails(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ExportAccessDao exportAccessDao = session.getMapper(ExportAccessDao.class);

		try {

			this.exportAccessId = UUIDGenerator.generateUUID();
			this.isActive = true;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = exportAccessDao.addExportAccessDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addExportAccessDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		if (status > 0) {

			return this.exportAccessId;

		} else {

			return null;
		}
	}

	public int updateExportAccessDetails(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ExportAccessDao exportAccessDao = session.getMapper(ExportAccessDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			status = exportAccessDao.updateExportAccessDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateExportAccessDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static ExportAccessModel getExportAccessDetailsByUserId(String userId) {

		ExportAccessModel exportAccessModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ExportAccessDao exportAccessDao = session.getMapper(ExportAccessDao.class);

		try {

			exportAccessModel = exportAccessDao.getExportAccessDetailsByUserId(userId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getExportAccessDetailsByUserId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return exportAccessModel;
	}

	public boolean isPhonepeLogReportExport() {
		return isPhonepeLogReportExport;
	}

	public void setPhonepeLogReportExport(boolean isPhonepeLogReportExport) {
		this.isPhonepeLogReportExport = isPhonepeLogReportExport;
	}

	public boolean isWarehouseExport() {
		return isWarehouseExport;
	}

	public void setWarehouseExport(boolean isWarehouseExport) {
		this.isWarehouseExport = isWarehouseExport;
	}

}