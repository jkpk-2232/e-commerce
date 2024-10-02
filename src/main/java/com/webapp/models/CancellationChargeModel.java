package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.CancellationChargeDao;

public class CancellationChargeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CancellationChargeModel.class);

	private String cancellationChargesId;
	private String adminId;
	private double charge;

	public int updateAdminCancellationCharges() {

		int status = -1;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CancellationChargeDao cancellationChargeDao = session.getMapper(CancellationChargeDao.class);

		this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		this.setUpdatedBy(this.getAdminId());

		try {

			status = cancellationChargeDao.updateAdminCancellationCharges(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAdminCancellationCharges :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		
		return status;
	}

	public static CancellationChargeModel getAdminCancellationCharges() {

		CancellationChargeModel cancellationChargeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CancellationChargeDao cancellationChargeDao = session.getMapper(CancellationChargeDao.class);

		try {

			cancellationChargeModel = cancellationChargeDao.getAdminCancellationCharges();

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAdminCancellationCharges :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
		
		return cancellationChargeModel;
	}

	public String getCancellationChargesId() {
		return cancellationChargesId;
	}

	public void setCancellationChargesId(String cancellationChargesId) {
		this.cancellationChargesId = cancellationChargesId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public double getCharge() {
		return charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

}
