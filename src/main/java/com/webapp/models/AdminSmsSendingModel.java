package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.AdminSmsSendingDao;

public class AdminSmsSendingModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminSmsSendingModel.class);

	private String adminSmsSendingId;

	private boolean pAcceptByDriver;
	private boolean pArrivedAndWaiting;
	private boolean pCancelledByDriver;
	private boolean pCancelledByBusinessO;
	private boolean pInvoice;
	private boolean pBookByOwner;
	private boolean pRefund;
	private boolean pCreditUpdateAdmin;

	private boolean dBookingRequest;
	private boolean dCancelledByPassengerBusinessO;
	private boolean dPaymentReceived;

	private boolean boAccepted;
	private boolean boArrivedAndWaiting;
	private boolean boCancelledByDriver;
	private boolean boInvoice;

	private String language;

	public String getAdminSmsSendingId() {
		return adminSmsSendingId;
	}

	public void setAdminSmsSendingId(String adminSmsSendingId) {
		this.adminSmsSendingId = adminSmsSendingId;
	}

	public boolean ispAcceptByDriver() {
		return pAcceptByDriver;
	}

	public void setpAcceptByDriver(boolean pAcceptByDriver) {
		this.pAcceptByDriver = pAcceptByDriver;
	}

	public boolean ispArrivedAndWaiting() {
		return pArrivedAndWaiting;
	}

	public void setpArrivedAndWaiting(boolean pArrivedAndWaiting) {
		this.pArrivedAndWaiting = pArrivedAndWaiting;
	}

	public boolean ispCancelledByDriver() {
		return pCancelledByDriver;
	}

	public void setpCancelledByDriver(boolean pCancelledByDriver) {
		this.pCancelledByDriver = pCancelledByDriver;
	}

	public boolean ispCancelledByBusinessO() {
		return pCancelledByBusinessO;
	}

	public void setpCancelledByBusinessO(boolean pCancelledByBusinessO) {
		this.pCancelledByBusinessO = pCancelledByBusinessO;
	}

	public boolean ispInvoice() {
		return pInvoice;
	}

	public void setpInvoice(boolean pInvoice) {
		this.pInvoice = pInvoice;
	}

	public boolean ispBookByOwner() {
		return pBookByOwner;
	}

	public void setpBookByOwner(boolean pBookByOwner) {
		this.pBookByOwner = pBookByOwner;
	}

	public boolean ispRefund() {
		return pRefund;
	}

	public void setpRefund(boolean pRefund) {
		this.pRefund = pRefund;
	}

	public boolean isdBookingRequest() {
		return dBookingRequest;
	}

	public void setdBookingRequest(boolean dBookingRequest) {
		this.dBookingRequest = dBookingRequest;
	}

	public boolean isdCancelledByPassengerBusinessO() {
		return dCancelledByPassengerBusinessO;
	}

	public void setdCancelledByPassengerBusinessO(boolean dCancelledByPassengerBusinessO) {
		this.dCancelledByPassengerBusinessO = dCancelledByPassengerBusinessO;
	}

	public boolean isdPaymentReceived() {
		return dPaymentReceived;
	}

	public void setdPaymentReceived(boolean dPaymentReceived) {
		this.dPaymentReceived = dPaymentReceived;
	}

	public boolean isBoAccepted() {
		return boAccepted;
	}

	public void setBoAccepted(boolean boAccepted) {
		this.boAccepted = boAccepted;
	}

	public boolean isBoArrivedAndWaiting() {
		return boArrivedAndWaiting;
	}

	public void setBoArrivedAndWaiting(boolean boArrivedAndWaiting) {
		this.boArrivedAndWaiting = boArrivedAndWaiting;
	}

	public boolean isBoCancelledByDriver() {
		return boCancelledByDriver;
	}

	public void setBoCancelledByDriver(boolean boCancelledByDriver) {
		this.boCancelledByDriver = boCancelledByDriver;
	}

	public boolean isBoInvoice() {
		return boInvoice;
	}

	public void setBoInvoice(boolean boInvoice) {
		this.boInvoice = boInvoice;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean ispCreditUpdateAdmin() {
		return pCreditUpdateAdmin;
	}

	public void setpCreditUpdateAdmin(boolean pCreditUpdateAdmin) {
		this.pCreditUpdateAdmin = pCreditUpdateAdmin;
	}

	public static AdminSmsSendingModel getAdminSmsSendingDetails() {

		AdminSmsSendingModel adminSmsSendingModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSmsSendingDao adminSmsSendingDao = session.getMapper(AdminSmsSendingDao.class);

		try {
			adminSmsSendingModel = adminSmsSendingDao.getAdminSmsSendingDetails();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminSmsSendingDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return adminSmsSendingModel;
	}

	public int updateAdminSmsSending() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSmsSendingDao adminSmsSendingDao = session.getMapper(AdminSmsSendingDao.class);

		try {
			count = adminSmsSendingDao.updateAdminSmsSending(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAdminSmsSending : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static int updatePreviousAdminSmsSendingEntryToFalse() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSmsSendingDao adminSmsSendingDao = session.getMapper(AdminSmsSendingDao.class);

		try {
			count = adminSmsSendingDao.updatePreviousAdminSmsSendingEntryToFalse();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePreviousAdminSmsSendingEntryToFalse : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}
}