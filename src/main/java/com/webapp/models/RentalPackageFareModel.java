package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.RentalPackageFareDao;

public class RentalPackageFareModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(RentalPackageFareModel.class);

	private String rentalPackageFareId;
	private String rentalPackageId;
	private String carTypeId;
	private double baseFare;
	private double perKmFare;
	private double perMinuteFare;
	private double driverPayablePercentage;

	private long packageTime;
	private double packageDistance;
	private String rentalPackageType;

	public String getRentalPackageFareId() {
		return rentalPackageFareId;
	}

	public void setRentalPackageFareId(String rentalPackageFareId) {
		this.rentalPackageFareId = rentalPackageFareId;
	}

	public String getRentalPackageId() {
		return rentalPackageId;
	}

	public void setRentalPackageId(String rentalPackageId) {
		this.rentalPackageId = rentalPackageId;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(double baseFare) {
		this.baseFare = baseFare;
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

	public double getDriverPayablePercentage() {
		return driverPayablePercentage;
	}

	public void setDriverPayablePercentage(double driverPayablePercentage) {
		this.driverPayablePercentage = driverPayablePercentage;
	}

	public long getPackageTime() {
		return packageTime;
	}

	public void setPackageTime(long packageTime) {
		this.packageTime = packageTime;
	}

	public double getPackageDistance() {
		return packageDistance;
	}

	public void setPackageDistance(double packageDistance) {
		this.packageDistance = packageDistance;
	}

	public String getRentalPackageType() {
		return rentalPackageType;
	}

	public void setRentalPackageType(String rentalPackageType) {
		this.rentalPackageType = rentalPackageType;
	}

	public static int insertRentalPackageFareBatch(List<RentalPackageFareModel> rentalPackageFareModelList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageFareDao rentalPackageCarTypeDao = session.getMapper(RentalPackageFareDao.class);

		try {

			count = rentalPackageCarTypeDao.insertRentalPackageFareBatch(rentalPackageFareModelList);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during insertRentalPackageFareBatch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int deleteRentalPackageFareByRentalPackageId() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageFareDao rentalPackageCarTypeDao = session.getMapper(RentalPackageFareDao.class);

		try {

			count = rentalPackageCarTypeDao.deleteRentalPackageFareByRentalPackageId(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteRentalPackageFareByRentalPackageId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<RentalPackageFareModel> getRentalPackageFareListByRentalPackageId(String rentalPackageId) {

		List<RentalPackageFareModel> rentalPackageCarTypeModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageFareDao rentalPackageCarTypeDao = session.getMapper(RentalPackageFareDao.class);

		try {

			rentalPackageCarTypeModelList = rentalPackageCarTypeDao.getRentalPackageFareListByRentalPackageId(rentalPackageId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getRentalPackageFareListByRentalPackageId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return rentalPackageCarTypeModelList;
	}

	public static RentalPackageFareModel getRentalPackageFareDetailsByRentalIdnCarType(String rentalPackageId, String carTypeId) {

		RentalPackageFareModel rentalPackageCarTypeModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("rentalPackageId", rentalPackageId);
		inputMap.put("carTypeId", carTypeId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RentalPackageFareDao rentalPackageCarTypeDao = session.getMapper(RentalPackageFareDao.class);

		try {

			rentalPackageCarTypeModel = rentalPackageCarTypeDao.getRentalPackageFareDetailsByRentalIdnCarType(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getRentalPackageFareDetailsByRentalIdnCarType : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return rentalPackageCarTypeModel;
	}

}