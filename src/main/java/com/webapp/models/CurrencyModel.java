package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.CurrencyDao;

public class CurrencyModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CurrencyModel.class);

	private long currencyId;
	private String country;
	private String currency;
	private String code;
	private String symbol;

	public static List<CurrencyModel> getCurrencyList() {

		List<CurrencyModel> currencyList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CurrencyDao currencyDao = session.getMapper(CurrencyDao.class);

		try {
			currencyList = currencyDao.getCurrencyList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrencyList :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return currencyList;
	}

	public static CurrencyModel getCurrencyDetailsByCurrencyId(long currencyId) {

		CurrencyModel currencyList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CurrencyDao currencyDao = session.getMapper(CurrencyDao.class);

		try {
			currencyList = currencyDao.getCurrencyDetailsByCurrencyId(currencyId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrencyDetailsByCurrencyId :", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return currencyList;
	}

	public long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(long currencyId) {
		this.currencyId = currencyId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}