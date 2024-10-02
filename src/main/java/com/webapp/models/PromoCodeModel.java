package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.PromoCodeDao;

public class PromoCodeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(PromoCodeModel.class);

	private String promoCodeId;
	private String promoCode;
	private String vendorId; // mapped against vendor_id
	private String usage;
	private String usageType;
	private long usageCount;
	private long usedCount;
	private String mode;
	private double discount;
	private long startDate;
	private long endDate;

	private boolean isActive;

	private String serviceTypeId;
	private double maxDiscount;

	private String serviceTypeName;
	private String vendorBrandName;

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getVendorName() {
		return vendorBrandName;
	}

	public void setVendorName(String vendorName) {
		this.vendorBrandName = vendorName;
	}

	public String getPromoCodeId() {
		return promoCodeId;
	}

	public void setPromoCodeId(String promoCodeId) {
		this.promoCodeId = promoCodeId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public long getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(long usageCount) {
		this.usageCount = usageCount;
	}

	public long getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(long usedCount) {
		this.usedCount = usedCount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public double getMaxDiscount() {
		return maxDiscount;
	}

	public void setMaxDiscount(double maxDiscount) {
		this.maxDiscount = maxDiscount;
	}

	public String addPromoCode(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {

			this.promoCodeId = UUIDGenerator.generateUUID();
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.isActive = true;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;
			promoCodeDao.addPromoCode(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addPromoCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.promoCodeId;
	}

	public static int getPromoCodeCount(long startDatelong, long endDatelong, String serviceTypeId, String vendorId, List<String> assignedRegionList) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			status = promoCodeDao.getPromoCodeCount(startDatelong, endDatelong, serviceTypeId, vendorId, assignedRegionList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPromoCodeCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<PromoCodeModel> getPromoCodeListForSearch(int start, int length, String order, String globalSearchString, long startDatelong, long endDatelong, String serviceTypeId, String vendorId, List<String> assignedRegionList, double maxDiscountSearch) {

		List<PromoCodeModel> promoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			promoCodeModel = promoCodeDao.getPromoCodeListForSearch(start, length, order, globalSearchString, startDatelong, endDatelong, serviceTypeId, vendorId, assignedRegionList, maxDiscountSearch);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPromoCodeListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return promoCodeModel;
	}

	public static int getTotalPromoCodeCountBySearch(String globalSearchString, long startDatelong, long endDatelong, String serviceTypeId, String vendorId, List<String> assignedRegionList, double maxDiscountSearch) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			status = promoCodeDao.getTotalPromoCodeCountBySearch(globalSearchString, startDatelong, endDatelong, serviceTypeId, vendorId, assignedRegionList, maxDiscountSearch);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalPromoCodeCountBySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int deletePromoCode(String loggedInUserId) {

		int status = 0;

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			status = promoCodeDao.deletePromoCode(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deletePromoCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int activatePromoCode(String loggedInUserId) {

		int status = 0;

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			status = promoCodeDao.activatePromoCode(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activatePromoCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int deactivatePromoCode(String loggedInUserId) {

		int status = 0;

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			status = promoCodeDao.deactivatePromoCode(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deactivatePromoCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static boolean isPromoCodeExists(String promoCode) {

		boolean status = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			status = promoCodeDao.isPromoCodeExists(promoCode.toUpperCase());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isPromoCodeExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static PromoCodeModel getPromoCodeDetailsByPromoCode(String promoCode) {

		PromoCodeModel promoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			promoCodeModel = promoCodeDao.getPromoCodeDetailsByPromoCode(promoCode.toUpperCase());
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPromoCodeDetailsByPromoCode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return promoCodeModel;
	}

	public static PromoCodeModel getPromoCodeDetailsByPromoCodeId(String promoCodeId) {

		PromoCodeModel promoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			promoCodeModel = promoCodeDao.getPromoCodeDetailsByPromoCodeId(promoCodeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPromoCodeDetailsByPromoCodeId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return promoCodeModel;
	}

	public int updatePromoCodeCount() {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			status = promoCodeDao.updatePromoCodeCount(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePromoCodeCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static PromoCodeModel getActiveDeactivePromoCodeDetailsByPromoCodeId(String promoCodeId) {

		PromoCodeModel promoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			promoCodeModel = promoCodeDao.getActiveDeactivePromoCodeDetailsByPromoCodeId(promoCodeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getActiveDeactivePromoCodeDetailsByPromoCodeId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return promoCodeModel;
	}

	public int updatePromoCodeDetails(String loggedInUserId) {

		int updateStatus = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = loggedInUserId;

			updateStatus = promoCodeDao.updatePromoCodeDetails(this);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePromoCodeDetails : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updateStatus;
	}
	
	public static List<PromoCodeModel> getPromoCodeListByserviceTypeIdAndVendorId(String serviceTypeId, String vendorId, List<String> assignedRegionList) {
		
		List<PromoCodeModel> promoCodeModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		PromoCodeDao promoCodeDao = session.getMapper(PromoCodeDao.class);

		try {
			promoCodeModel = promoCodeDao.getPromoCodeListByserviceTypeIdAndVendorId(serviceTypeId, vendorId, assignedRegionList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPromoCodeListForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return promoCodeModel;
	}
}