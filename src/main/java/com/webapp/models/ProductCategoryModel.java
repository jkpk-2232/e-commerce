package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ProductCategoryDao;

public class ProductCategoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ProductCategoryModel.class);

	private String productCategoryId;
	private String productCategoryName;
	private String productCategoryDescription;
	private boolean isActive;
	private boolean isDeleted;
	private String productCategoryImage;
	
	private List<ProductSubCategoryModel> productSubCategoryList;

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getProductCategoryDescription() {
		return productCategoryDescription;
	}

	public void setProductCategoryDescription(String productCategoryDescription) {
		this.productCategoryDescription = productCategoryDescription;
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

	public static List<ProductCategoryModel> getProductCategorySearch(long startDatelong, long endDatelong, String searchKey, int start, int length) {
		
		List<ProductCategoryModel> productCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			productCategoryList = productCategoryDao.getProductCategorySearch(startDatelong, endDatelong, searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductCategorySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productCategoryList;
	}

	public static ProductCategoryModel getProductCategoryDetailsByProductCategoryId(String productCategoryId) {
		
		ProductCategoryModel productCategoryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			productCategoryModel = productCategoryDao.getProductCategoryDetailsByProductCategoryId(productCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductCategoryDetailsByProductCategoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productCategoryModel;
	}
	
	public static List<ProductCategoryModel> getProductCategoryList( ) {

		List<ProductCategoryModel> productCategoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			productCategoryList = productCategoryDao.getProductCategoryList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductCategoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productCategoryList;
	}

	public void insertProductCategory(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		this.productCategoryId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			productCategoryDao.insertProductCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertProductCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		
	}

	public void updateProductCategory(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			productCategoryDao.updateProductCategory(this);
			VendorProductModel  vendorProductModel = new VendorProductModel();
			vendorProductModel.setProductCategory(this.getProductCategoryName());
			vendorProductModel.setProductCategoryId(this.getProductCategoryId());
			vendorProductModel.updateProductCategory(loggedInUserId);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static int getProductCategorySearchCount(long startDatelong, long endDatelong, String searchKey) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			
			count = productCategoryDao.getProductCategorySearchCount(startDatelong, endDatelong, searchKey );
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getServiceSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return count;
	}

	public void updateProductCategoryStatus(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			productCategoryDao.updateProductCategoryStatus(this);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductCategoryStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static int getProductCategoryCount(long startDatelong, long endDatelong) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			count = productCategoryDao.getProductCategoryCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductCategoryCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
		
	}

	public String getProductCategoryImage() {
		return productCategoryImage;
	}

	public void setProductCategoryImage(String productCategoryImage) {
		this.productCategoryImage = productCategoryImage;
	}

	public static ProductCategoryModel getProductCategoryDetailsByProductCategoryName(String productCategory) {
		
		ProductCategoryModel productCategoryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			productCategoryModel = productCategoryDao.getProductCategoryDetailsByProductCategoryName(productCategory);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductCategoryDetailsByProductCategoryName : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productCategoryModel;
	}

	public static boolean isProductCategoryNameExists(String productCategoryName, String productCategoryId) {
		
		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			isDuplicate = productCategoryDao.isProductCategoryNameExists(productCategoryName, productCategoryId);
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

	public static List<ProductCategoryModel> getProductCategoryListByProductCategoryId(List<String> productCategoryIdList) {
		
		List<ProductCategoryModel> productCategoryModelList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductCategoryDao productCategoryDao = session.getMapper(ProductCategoryDao.class);

		try {
			if (productCategoryIdList.size() > 0) {
				productCategoryModelList = productCategoryDao.getProductCategoryListByProductCategoryId(productCategoryIdList);
			}
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductCategoryDetailsByProductCategoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productCategoryModelList;
		
	}

	public List<ProductSubCategoryModel> getProductSubCategoryList() {
		return productSubCategoryList;
	}

	public void setProductSubCategoryList(List<ProductSubCategoryModel> productSubCategoryList) {
		this.productSubCategoryList = productSubCategoryList;
	}

}
