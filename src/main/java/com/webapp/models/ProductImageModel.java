package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ProductImageDao;

import twitter4j.internal.logging.Logger;

public class ProductImageModel extends AbstractModel {

	public static Logger logger = Logger.getLogger(ProductImageModel.class);

	private String productImageId;
	private String productImageUrl;
	private String productVariantId;
	
	private String brandName;
	private String productName;
	private String productVariantName;
	private String weight;
	private String uomName;

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	private boolean isActive;
	private boolean isDeleted;

	public String getProductImageId() {
		return productImageId;
	}

	public void setProductImageId(String productImageId) {
		this.productImageId = productImageId;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
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

	public void insertProductImage(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		this.productImageId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			productImageDao.insertProductImage(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertProductImage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static ProductImageModel getProductImageDetailsByProductImageId(String productImageId) {

		ProductImageModel productImageModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		try {
			productImageModel = productImageDao.getProductImageDetailsByProductImageId(productImageId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductImageDetailsByProductImageId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productImageModel;
	}

	public void updateProductImage(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			productImageDao.updateProductImage(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductImage : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static int getProductImageCount(long startDatelong, long endDatelong, List<String> userIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		try {
			count = productImageDao.getProductImageCount(startDatelong, endDatelong, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductImageCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<ProductImageModel> getProductImageSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, List<String> userIdList) {

		List<ProductImageModel> productImageList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		try {
			productImageList = productImageDao.getProductImageSearch(startDatelong, endDatelong, searchKey, start, length, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productImageList;

	}

	public static int getProductImageSearchCount(long startDatelong, long endDatelong, String searchKey, List<String> userIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		try {

			count = productImageDao.getProductImageSearchCount(startDatelong, endDatelong, searchKey, userIdList);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductImageSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;

	}

	public void updateProductImageStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			productImageDao.updateProductImageStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductImageStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductVariantName() {
		return productVariantName;
	}

	public void setProductVariantName(String productVariantName) {
		this.productVariantName = productVariantName;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public static List<ProductImageModel> getProductImageListByProductVariant(String productVariantId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductImageDao productImageDao = session.getMapper(ProductImageDao.class);
		
		List<ProductImageModel> productImageModels = new ArrayList<>();

		try {
			productImageModels = productImageDao.getProductImageListByProductVariant(productVariantId);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductImageStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return productImageModels;
	}
}
