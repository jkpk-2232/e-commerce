package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.TourTaxDao;

public class TourTaxModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(TaxModel.class);

	private String tourTaxId;
	private String tourId;
	private String taxId;
	private double taxPercentage;
	private double taxAmount;

	private String taxName;

	public String getTourTaxId() {
		return tourTaxId;
	}

	public void setTourTaxId(String tourTaxId) {
		this.tourTaxId = tourTaxId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public static int insertTourTaxBatch(List<TourTaxModel> tourTaxModelList) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTaxDao tourTaxDao = session.getMapper(TourTaxDao.class);

		try {

			status = tourTaxDao.insertTourTaxBatch(tourTaxModelList);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insertTourTaxBatch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static TourTaxModel getTourTaxDetailsById(String tourTaxId) {

		TourTaxModel tourTaxModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTaxDao tourTaxDao = session.getMapper(TourTaxDao.class);

		try {

			tourTaxModel = tourTaxDao.getTourTaxDetailsById(tourTaxId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTourTaxDetailsById : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return tourTaxModel;
	}

	public static List<TourTaxModel> getTourTaxListByTourId(String tourId) {

		List<TourTaxModel> tourTaxModelList = new ArrayList<TourTaxModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		TourTaxDao tourTaxDao = session.getMapper(TourTaxDao.class);

		try {

			tourTaxModelList = tourTaxDao.getTourTaxListByTourId(tourId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getTourTaxListByTourId :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return tourTaxModelList;
	}

}