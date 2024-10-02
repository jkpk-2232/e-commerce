package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.MulticityCountryDao;

public class MulticityCountryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(MulticityCountryModel.class);

	private String multicityCountryId;
	private String countryName;
	private String countryShortName;
	private String currencySymbol;
	private String currencySymbolHtml;
	private boolean isActive;
	private boolean isDeleted;
	private boolean isPermanentDelete;
	private double senderBenefit;
	private double receiverBenefit;
	private String phoneNoCode;

	private String distanceType;
	private double distanceUnits;
	private double cancellationCharges;

	private double walletAmount;

	public String getCurrencySymbolHtml() {
		return currencySymbolHtml;
	}

	public void setCurrencySymbolHtml(String currencySymbolHtml) {
		this.currencySymbolHtml = currencySymbolHtml;
	}

	public String getMulticityCountryId() {
		return multicityCountryId;
	}

	public void setMulticityCountryId(String multicityCountryId) {
		this.multicityCountryId = multicityCountryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryShortName() {
		return countryShortName;
	}

	public void setCountryShortName(String countryShortName) {
		this.countryShortName = countryShortName;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public double getSenderBenefit() {
		return senderBenefit;
	}

	public void setSenderBenefit(double senderBenefit) {
		this.senderBenefit = senderBenefit;
	}

	public double getReceiverBenefit() {
		return receiverBenefit;
	}

	public void setReceiverBenefit(double receiverBenefit) {
		this.receiverBenefit = receiverBenefit;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public double getWalletAmount() {
		return walletAmount;
	}

	public void setWalletAmount(double walletAmount) {
		this.walletAmount = walletAmount;
	}

	public String getDistanceType() {
		return distanceType;
	}

	public void setDistanceType(String distanceType) {
		this.distanceType = distanceType;
	}

	public double getDistanceUnits() {
		return distanceUnits;
	}

	public void setDistanceUnits(double distanceUnits) {
		this.distanceUnits = distanceUnits;
	}

	public double getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(double cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public boolean isPermanentDelete() {
		return isPermanentDelete;
	}

	public void setPermanentDelete(boolean isPermanentDelete) {
		this.isPermanentDelete = isPermanentDelete;
	}

	public static int getMulticityCountryCount() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);

		try {
			count = multicityCountryDao.getMulticityCountryCount();
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCountryCount : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static List<MulticityCountryModel> getMulticityCountrySearch(int start, int length, String order, String globalSearchString) {

		List<MulticityCountryModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);

		try {
			multicityCountryModelList = multicityCountryDao.getMulticityCountrySearch(start, length, globalSearchString);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCountrySearch : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public static MulticityCountryModel getMulticityCountryIdDetailsById(String multicityCountryId) {

		MulticityCountryModel multicityCountryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);

		try {
			multicityCountryModel = multicityCountryDao.getMulticityCountryIdDetailsById(multicityCountryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCountryIdDetailsById : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModel;
	}

	public static MulticityCountryModel getMulticityCountryIdDetailsByCountryName(String country) {

		MulticityCountryModel multicityCountryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);

		try {
			multicityCountryModel = multicityCountryDao.getMulticityCountryIdDetailsByCountryName(country);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCountryIdDetailsByCountryName : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModel;
	}

	public static MulticityCountryModel getMulticityCountryIdDetailsByPhoneNumberCode(String phoneNoCode) {

		MulticityCountryModel multicityCountryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCountryDao multicityCountryDao = session.getMapper(MulticityCountryDao.class);

		try {
			multicityCountryModel = multicityCountryDao.getMulticityCountryIdDetailsByPhoneNumberCode(phoneNoCode);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCountryIdDetailsByPhoneNumberCode : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModel;
	}
}