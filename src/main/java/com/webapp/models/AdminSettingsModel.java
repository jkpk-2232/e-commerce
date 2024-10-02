package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.ProjectConstants;
import com.webapp.daos.AdminSettingsDao;

public class AdminSettingsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AdminSettingsModel.class);

	private String adminSettingsId;
	private long radius;
	private double senderBenefit;
	private double receiverBenefit;
	private String radiusString;
	private String aboutUs;
	private String privacyPolicy;
	private String termsConditions;
	private String distanceType;
	private long distanceUnits;
	private String currencySymbol;
	private String currencySymbolHtml;
	private long noOfCars;
	private String countryCode;
	private String currencyId;
	private String refundPolicy;
	private double driverReferralBenefit;
	private double averageSpeed;
	private long driverIdealTime;
	private boolean isAutoAssign;
	private boolean isCarServiceAutoAssign;
	private double demandVendorPercentage;
	private double supplierVendorPercentage;
	private boolean isRestrictDriverVendorSubscriptionExpiry;
	private String driverFare;
	private int radiusDeliveryDriver;
	private int radiusSelfDelivery;
	private int radiusStoreVisibility;
	private String radiusDeliveryDriverString;
	private String radiusSelfDeliveryString;
	private String radiusStoreVisibilityString;
	private int driverProcessingVia;
	private int cronJobTripExpiryAfterXMins;

	public int getDriverProcessingVia() {
		return driverProcessingVia;
	}

	public void setDriverProcessingVia(int driverProcessingVia) {
		this.driverProcessingVia = driverProcessingVia;
	}

	public int getCronJobTripExpiryAfterXMins() {
		return cronJobTripExpiryAfterXMins;
	}

	public void setCronJobTripExpiryAfterXMins(int cronJobTripExpiryAfterXMins) {
		this.cronJobTripExpiryAfterXMins = cronJobTripExpiryAfterXMins;
	}

	public String getRadiusDeliveryDriverString() {
		return radiusDeliveryDriverString;
	}

	public void setRadiusDeliveryDriverString(String radiusDeliveryDriverString) {
		this.radiusDeliveryDriverString = radiusDeliveryDriverString;
	}

	public String getRadiusSelfDeliveryString() {
		return radiusSelfDeliveryString;
	}

	public void setRadiusSelfDeliveryString(String radiusSelfDeliveryString) {
		this.radiusSelfDeliveryString = radiusSelfDeliveryString;
	}

	public String getRadiusStoreVisibilityString() {
		return radiusStoreVisibilityString;
	}

	public void setRadiusStoreVisibilityString(String radiusStoreVisibilityString) {
		this.radiusStoreVisibilityString = radiusStoreVisibilityString;
	}

	public int getRadiusDeliveryDriver() {
		return radiusDeliveryDriver;
	}

	public void setRadiusDeliveryDriver(int radiusDeliveryDriver) {
		this.radiusDeliveryDriver = radiusDeliveryDriver;
	}

	public int getRadiusSelfDelivery() {
		return radiusSelfDelivery;
	}

	public void setRadiusSelfDelivery(int radiusSelfDelivery) {
		this.radiusSelfDelivery = radiusSelfDelivery;
	}

	public int getRadiusStoreVisibility() {
		return radiusStoreVisibility;
	}

	public void setRadiusStoreVisibility(int radiusStoreVisibility) {
		this.radiusStoreVisibility = radiusStoreVisibility;
	}

	public String getAdminSettingsId() {
		return adminSettingsId;
	}

	public void setAdminSettingsId(String adminSettingsId) {
		this.adminSettingsId = adminSettingsId;
	}

	public long getRadius() {
		return radius;
	}

	public void setRadius(long radius) {
		this.radius = radius;
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

	public String getRadiusString() {
		return radiusString;
	}

	public void setRadiusString(String radiusString) {
		this.radiusString = radiusString;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	public String getPrivacyPolicy() {
		return privacyPolicy;
	}

	public void setPrivacyPolicy(String privacyPolicy) {
		this.privacyPolicy = privacyPolicy;
	}

	public String getTermsConditions() {
		return termsConditions;
	}

	public void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public String getDistanceType() {
		return distanceType;
	}

	public void setDistanceType(String distanceType) {
		this.distanceType = distanceType;
	}

	public long getDistanceUnits() {
		return distanceUnits;
	}

	public void setDistanceUnits(long distanceUnits) {
		this.distanceUnits = distanceUnits;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbolHtml() {
		return currencySymbolHtml;
	}

	public void setCurrencySymbolHtml(String currencySymbolHtml) {
		this.currencySymbolHtml = currencySymbolHtml;
	}

	public long getNoOfCars() {
		return noOfCars;
	}

	public void setNoOfCars(long noOfCars) {
		this.noOfCars = noOfCars;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getRefundPolicy() {
		return refundPolicy;
	}

	public void setRefundPolicy(String refundPolicy) {
		this.refundPolicy = refundPolicy;
	}

	public double getDriverReferralBenefit() {
		return driverReferralBenefit;
	}

	public void setDriverReferralBenefit(double driverReferralBenefit) {
		this.driverReferralBenefit = driverReferralBenefit;
	}

	public double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public long getDriverIdealTime() {
		return driverIdealTime;
	}

	public void setDriverIdealTime(long driverIdealTime) {
		this.driverIdealTime = driverIdealTime;
	}

	public boolean isAutoAssign() {
		return isAutoAssign;
	}

	public void setAutoAssign(boolean isAutoAssign) {
		this.isAutoAssign = isAutoAssign;
	}

	public boolean isCarServiceAutoAssign() {
		return isCarServiceAutoAssign;
	}

	public void setCarServiceAutoAssign(boolean isCarServiceAutoAssign) {
		this.isCarServiceAutoAssign = isCarServiceAutoAssign;
	}

	public double getDemandVendorPercentage() {
		return demandVendorPercentage;
	}

	public void setDemandVendorPercentage(double demandVendorPercentage) {
		this.demandVendorPercentage = demandVendorPercentage;
	}

	public double getSupplierVendorPercentage() {
		return supplierVendorPercentage;
	}

	public void setSupplierVendorPercentage(double supplierVendorPercentage) {
		this.supplierVendorPercentage = supplierVendorPercentage;
	}

	public boolean isRestrictDriverVendorSubscriptionExpiry() {
		return isRestrictDriverVendorSubscriptionExpiry;
	}

	public void setRestrictDriverVendorSubscriptionExpiry(boolean isRestrictDriverVendorSubscriptionExpiry) {
		this.isRestrictDriverVendorSubscriptionExpiry = isRestrictDriverVendorSubscriptionExpiry;
	}

	public static AdminSettingsModel getAdminSettingsDetails() {

		AdminSettingsModel adminSettingsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {
			adminSettingsModel = adminSettingsDao.getAdminSettingsDetails();

			double radiusInMeters = adminSettingsModel.getDistanceUnits() * adminSettingsModel.getRadius();

			String radiusString = String.valueOf(radiusInMeters);
			adminSettingsModel.setRadiusString(radiusString);

			adminSettingsModel.setRadiusDeliveryDriverString(String.valueOf(adminSettingsModel.getDistanceUnits() * adminSettingsModel.getRadiusDeliveryDriver()));
			adminSettingsModel.setRadiusSelfDeliveryString(String.valueOf(adminSettingsModel.getDistanceUnits() * adminSettingsModel.getRadiusSelfDelivery()));
			adminSettingsModel.setRadiusStoreVisibilityString(String.valueOf(adminSettingsModel.getDistanceUnits() * adminSettingsModel.getRadiusStoreVisibility()));

			adminSettingsModel.setCurrencySymbol(adminSettingsModel.getCurrencySymbol() + " ");
			adminSettingsModel.setCurrencySymbolHtml(adminSettingsModel.getCurrencySymbolHtml() + " ");

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getAdminSettingsDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return adminSettingsModel;
	}

	public int updateAdminSettings() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updateAdminSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateAdminSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updateAboutUsAdminSettings() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updateAboutUsAdminSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateAboutUsAdminSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updatePrivacyPolicyAdminSettings() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updatePrivacyPolicyAdminSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updatePrivacyPolicyAdminSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updateRefundPolicyAdminSettings() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updateRefundPolicyAdminSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateRefundPolicyAdminSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updateTermsConditionsAdminSettings() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updateTermsConditionsAdminSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateTermsConditionsAdminSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public int updateBenefits() {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updateBenefits(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateBenefits : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static void overrideSettingForDriverIdealTime(AdminSettingsModel adminSettingsModel) {
		if (adminSettingsModel.getDriverIdealTime() <= 0) {
			adminSettingsModel.setDriverIdealTime(ProjectConstants.ONE_HOUR_MILLISECONDS_LONG * 24);
		}
	}

	public String getDriverFare() {
		return driverFare;
	}

	public void setDriverFare(String driverFare) {
		this.driverFare = driverFare;
	}
	
	public int updateDriverFareAdminSettings() {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AdminSettingsDao adminSettingsDao = session.getMapper(AdminSettingsDao.class);

		try {

			count = adminSettingsDao.updateDriverFareAdminSettings(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateDriverFareAdminSettings : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
		
	}

	@Override
	public String toString() {
		return "AdminSettingsModel [adminSettingsId=" + adminSettingsId + ", radius=" + radius + ", senderBenefit=" + senderBenefit + ", receiverBenefit=" + receiverBenefit + ", radiusString=" + radiusString + ", aboutUs=" + aboutUs + ", privacyPolicy=" + privacyPolicy
					+ ", termsConditions=" + termsConditions + ", distanceType=" + distanceType + ", distanceUnits=" + distanceUnits + ", currencySymbol=" + currencySymbol + ", currencySymbolHtml=" + currencySymbolHtml + ", noOfCars=" + noOfCars + ", countryCode="
					+ countryCode + ", currencyId=" + currencyId + ", refundPolicy=" + refundPolicy + ", driverReferralBenefit=" + driverReferralBenefit + ", averageSpeed=" + averageSpeed + ", driverIdealTime=" + driverIdealTime + ", isAutoAssign=" + isAutoAssign
					+ ", isCarServiceAutoAssign=" + isCarServiceAutoAssign + "]";
	}

	public static void main(String[] args) {
		AdminSettingsModel adminSettingsModel = getAdminSettingsDetails();
		System.out.println(adminSettingsModel);
	}
}