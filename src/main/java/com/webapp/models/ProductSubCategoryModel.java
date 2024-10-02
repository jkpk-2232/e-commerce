package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ProductSubCategoryDao;

public class ProductSubCategoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ProductSubCategoryModel.class);

	private String productSubCategoryId;
	private String productSubCategoryName;
	private String productSubCategoryDescription;
	private boolean isActive;
	private boolean isDeleted;
	private String productSubCategoryImage;
	private String productCategoryId;
	
	private String productCategoryName;

	public String getProductSubCategoryId() {
		return productSubCategoryId;
	}

	public void setProductSubCategoryId(String productSubCategoryId) {
		this.productSubCategoryId = productSubCategoryId;
	}

	public String getProductSubCategoryName() {
		return productSubCategoryName;
	}

	public void setProductSubCategoryName(String productSubCategoryName) {
		this.productSubCategoryName = productSubCategoryName;
	}

	public String getProductSubCategoryDescription() {
		return productSubCategoryDescription;
	}

	public void setProductSubCategoryDescription(String productSubCategoryDescription) {
		this.productSubCategoryDescription = productSubCategoryDescription;
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

	public String getProductSubCategoryImage() {
		return productSubCategoryImage;
	}

	public void setProductSubCategoryImage(String productSubCategoryImage) {
		this.productSubCategoryImage = productSubCategoryImage;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public static List<ProductSubCategoryModel> getProductSubCategorySearch(long startDatelong, long endDatelong, String searchKey, int start, int length) {

		List<ProductSubCategoryModel> productSubCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			productSubCategoryList = productSubCategoryDao.getProductSubCategorySearch(startDatelong, endDatelong, searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategorySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productSubCategoryList;
	}

	public static ProductSubCategoryModel getProductSubCategoryDetailsByProductSubCategoryId(String productSubCategoryId) {

		ProductSubCategoryModel productSubCategoryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			productSubCategoryModel = productSubCategoryDao.getProductSubCategoryDetailsByProductSubCategoryId(productSubCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategoryDetailsByProductSubCategoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productSubCategoryModel;
	}
	
	public static List<ProductSubCategoryModel> getProductSubCategoryList( ) {

		List<ProductSubCategoryModel> productSubCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			productSubCategoryList = productSubCategoryDao.getProductSubCategoryList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productSubCategoryList;
	}
	
	public void insertProductSubCategory(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		this.productSubCategoryId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			productSubCategoryDao.insertProductSubCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertProductSubCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}
	
	public void updateProductSubCategory(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			productSubCategoryDao.updateProductSubCategory(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductSubCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}
	
	public static int getProductSubCategorySearchCount(long startDatelong, long endDatelong, String searchKey) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {

			count = productSubCategoryDao.getProductSubCategorySearchCount(startDatelong, endDatelong, searchKey);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategorySearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}
	
	public void updateProductSubCategoryStatus(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			productSubCategoryDao.updateProductSubCategoryStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductSubCategoryStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}
	
	public static boolean isProductSubCategoryNameExists(String productSubCategoryName, String productSubCategoryId) {

		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			isDuplicate = productSubCategoryDao.isProductSubCategoryNameExists(productSubCategoryName, productSubCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isProductCategoryNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}
	
	public static int getProductSubCategoryCount(long startDatelong, long endDatelong) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			count = productSubCategoryDao.getProductSubCategoryCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategoryCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;

	}

	public static List<ProductSubCategoryModel> getProductSubCategoryListByProductCategoryId(String productCategoryId) {
		
		List<ProductSubCategoryModel> productSubCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			productSubCategoryList = productSubCategoryDao.getProductSubCategoryListByProductCategoryId(productCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productSubCategoryList;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public static ProductSubCategoryModel getProductSubCategoryDetailsByProductCategoryIdAndProductSubCategoryName(String productCategoryId, String productSubCategoryName) {
		
		ProductSubCategoryModel productSubCategoryModel = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductSubCategoryDao productSubCategoryDao = session.getMapper(ProductSubCategoryDao.class);

		try {
			productSubCategoryModel = productSubCategoryDao.getProductSubCategoryDetailsByProductCategoryIdAndProductSubCategoryName(productCategoryId, productSubCategoryName);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductSubCategoryDetailsByProductCategoryIdAndProductSubCategoryName : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productSubCategoryModel;
	}

}
