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
import com.webapp.daos.TaxDao;

public class TaxModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TaxModel.class);

	private String taxId;
	private String taxName;
	private double taxPercentage;
	private boolean isActive;

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int addTaxDetails(String userId) {

		int status = 0;

		long currentTime = DateUtils.nowAsGmtMillisec();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			this.taxId = UUIDGenerator.generateUUID();
			this.isActive = true;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = currentTime;
			this.createdBy = userId;
			this.updatedAt = currentTime;
			this.updatedBy = userId;

			status = taxDao.addTaxDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addTaxDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int updateTaxDetails(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			status = taxDao.updateTaxDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateTaxDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static TaxModel getTaxDetailsById(String taxId) {

		TaxModel taxModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			taxModel = taxDao.getTaxDetailsById(taxId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTaxDetailsById : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return taxModel;
	}

	public static List<TaxModel> getActiveTaxList() {

		List<TaxModel> taxModelList = new ArrayList<TaxModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			taxModelList = taxDao.getActiveTaxList();
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getActiveTaxList :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return taxModelList;
	}

	public static int getTaxCount() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			status = taxDao.getTaxCount();
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTaxCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static List<TaxModel> getTaxListForSearch(int start, int length, String order, String globalSearchString) {

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);

		List<TaxModel> taxModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			taxModelList = taxDao.getTaxListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTaxListForSearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return taxModelList;
	}

	public static int getTotalTaxCountBySearch(String globalSearchString) {

		int status = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("globalSearchString", globalSearchString);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			status = taxDao.getTotalTaxCountBySearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTotalTaxCountBySearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int deleteTax(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);
			this.setActive(false);
			this.setRecordStatus(ProjectConstants.DEACTIVATE_STATUS);

			status = taxDao.deleteTax(this);

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteTax : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int updateTaxStatus(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TaxDao taxDao = session.getMapper(TaxDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);

			status = taxDao.updateTaxStatus(this);

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateTaxStatus : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}
}
