package com.webapp.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.RechargeAmountDao;

public class RechargeAmountModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(RechargeAmountModel.class);

	private String rechargeAmountId;
	private double amount;

	public static List<RechargeAmountModel> getAllRechargeAmountList() throws SQLException {

		List<RechargeAmountModel> rechargeAmountList = new ArrayList<RechargeAmountModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RechargeAmountDao rechargeAmountDao = session.getMapper(RechargeAmountDao.class);

		try {
			rechargeAmountList = rechargeAmountDao.getAllRechargeAmountList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllRechargeAmountList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return rechargeAmountList;
	}

	public static RechargeAmountModel getRechargeAmountById(String rechargeAmountId) {

		RechargeAmountModel rechargeAmount = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		RechargeAmountDao rechargeAmountDao = session.getMapper(RechargeAmountDao.class);

		try {
			rechargeAmount = rechargeAmountDao.getRechargeAmountById(rechargeAmountId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getRechargeAmountById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return rechargeAmount;
	}

	public String getRechargeAmountId() {
		return rechargeAmountId;
	}

	public void setRechargeAmountId(String rechargeAmountId) {
		this.rechargeAmountId = rechargeAmountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}