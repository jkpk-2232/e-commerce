package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.BrandDao;

public class BrandModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(BrandModel.class);

	private String brandId;
	private String brandName;
	private String brandDescription;
	private boolean isActive;
	private boolean isDeleted;
	private String brandImage;
	
	private String vendorName;
	private String vendorBrandName;
	
	private boolean isPublic;

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandDescription() {
		return brandDescription;
	}

	public void setBrandDescription(String brandDescription) {
		this.brandDescription = brandDescription;
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

	public String getBrandImage() {
		return brandImage;
	}

	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}

	public static boolean isBrandNameExists(String brandName, String brandId) {

		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		try {
			isDuplicate = brandDao.isBrandNameExists(brandName, brandId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isBrandNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}

	public void insertBrand(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		this.brandId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			brandDao.insertBrand(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertBrand : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static BrandModel getBrandDetailsByBrandId(String brandId) {

		BrandModel brandModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		try {
			brandModel = brandDao.getBrandDetailsByBrandId(brandId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandDetailsByBrandId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return brandModel;
	}

	public void updateBrand(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			brandDao.updateBrand(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateBrand : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static int getBrandsCount(long startDatelong, long endDatelong, List<String> userIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		try {
			count = brandDao.getBrandsCount(startDatelong, endDatelong, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<BrandModel> getBrandSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, List<String> userIdList, String brandStatus,String approvedBrands) {

		List<BrandModel> brandList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		try {
			brandList = brandDao.getBrandSearch(startDatelong, endDatelong, searchKey, start, length, userIdList, brandStatus, approvedBrands);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return brandList;
	}

	public static int getBrandSearchCount(long startDatelong, long endDatelong, String searchKey, List<String> userIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		try {

			count = brandDao.getBrandSearchCount(startDatelong, endDatelong, searchKey, userIdList);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateBrandStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			brandDao.updateBrandStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateBrandStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorBrandName() {
		return vendorBrandName;
	}

	public void setVendorBrandName(String vendorBrandName) {
		this.vendorBrandName = vendorBrandName;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void updateBrandPublicStatus(String userId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BrandDao brandDao = session.getMapper(BrandDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();

		try {
			brandDao.updateBrandPublicStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateBrandPublicStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

}
