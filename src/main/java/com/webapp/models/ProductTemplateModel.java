package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ProductTemplateDao;

public class ProductTemplateModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ProductTemplateModel.class);

	private String productTemplateId;
	private String productName;
	private String productInformation;
	private String productSpecification;
	private boolean taxApplicable;
	private String productImage;
	private boolean isProductToAll;
	private String hsnCode;
	private String brandId;
	private String productCategoryId;
	private Integer uomId;
	private boolean isActive;
	private boolean isDeleted;
	private String productSubCategoryId;
	private String prdQtyType;
	
	private String brandName;
	private String productCategory;
	private String uomName;

	public String getProductTemplateId() {
		return productTemplateId;
	}

	public void setProductTemplateId(String productTemplateId) {
		this.productTemplateId = productTemplateId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductInformation() {
		return productInformation;
	}

	public void setProductInformation(String productInformation) {
		this.productInformation = productInformation;
	}

	public String getProductSpecification() {
		return productSpecification;
	}

	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}

	public boolean isTaxApplicable() {
		return taxApplicable;
	}

	public void setTaxApplicable(boolean taxApplicable) {
		this.taxApplicable = taxApplicable;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public boolean isProductToAll() {
		return isProductToAll;
	}

	public void setProductToAll(boolean isProductToAll) {
		this.isProductToAll = isProductToAll;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
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
	
	public static boolean isProductNameExists(String productName, String productTemplateId) {

		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {
			isDuplicate = productTemplateDao.isProductNameExists(productName, productTemplateId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isProductNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}
	
	public void insertProductTemplate(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		this.productTemplateId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			productTemplateDao.insertProductTemplate(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertProductTemplate : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static ProductTemplateModel getProductTemplateDetailsByProductTemplateId(String productTemplateId) {
		
		ProductTemplateModel productTemplateModel = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);
		
		try {
			productTemplateModel = productTemplateDao.getProductTemplateDetailsByProductTemplateId(productTemplateId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductTemplateDetailsByProductTemplateId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productTemplateModel;
	}

	public void updateProductTemplate(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);
		
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			productTemplateDao.updateProductTemplate(this);
			VendorProductModel vendorProductModel = new VendorProductModel();
			vendorProductModel.setProductInformation(this.getProductInformation());
			vendorProductModel.setProductSpecification(this.getProductSpecification());
			vendorProductModel.setProductWeightUnit(this.getUomId());
			ProductCategoryModel productCategoryModel = ProductCategoryModel.getProductCategoryDetailsByProductCategoryId(this.getProductCategoryId());
			if (productCategoryModel != null) {
				vendorProductModel.setProductCategory(productCategoryModel.getProductCategoryName());
			}
			vendorProductModel.setProductCategoryId(this.getProductCategoryId());
			vendorProductModel.setProductSubCategoryId(this.getProductSubCategoryId());
			vendorProductModel.setProductTemplateId(this.getProductTemplateId());
			vendorProductModel.updateInformationAndSpecificationAndWeightUnitAndCategoryAndSubCategory(loggedInUserId);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during productTemplateDao : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static int getProductTemplateCount(long startDatelong, long endDatelong, List<String> userIdList) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {
			count = productTemplateDao.getProductTemplatesCount(startDatelong, endDatelong, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductTemplateCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
		
	}

	public static List<ProductTemplateModel> getProductTemplateSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, List<String> userIdList) {

		List<ProductTemplateModel> productTemplateList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {
			productTemplateList = productTemplateDao.getProductTemplateSearch(startDatelong, endDatelong, searchKey, start, length, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productTemplateList;
	}

	public static int getProductTemplateSearchCount(long startDatelong, long endDatelong, String searchKey, List<String> userIdList) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {

			count = productTemplateDao.getProductTemplateSearchCount(startDatelong, endDatelong, searchKey, userIdList);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductTemplateSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateProductTemplateStatus(String userId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			productTemplateDao.updateProductTemplateStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductTemplateStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static List<ProductTemplateModel> getProductTemplateList(List<String> userIdList, String brandId, String productCategoryId) {
		
		List<ProductTemplateModel> productTemplateList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {
			productTemplateList = productTemplateDao.getProductTemplateList(userIdList, brandId, productCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productTemplateList;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getProductSubCategoryId() {
		return productSubCategoryId;
	}

	public void setProductSubCategoryId(String productSubCategoryId) {
		this.productSubCategoryId = productSubCategoryId;
	}

	public String getPrdQtyType() {
		return prdQtyType;
	}

	public void setPrdQtyType(String prdQtyType) {
		this.prdQtyType = prdQtyType;
	}

	public static boolean isProductNameAndUomIdExists(String productName, int uomId, String productTemplateId) {
		
		boolean isDuplicate = false;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {
			isDuplicate = productTemplateDao.isProductNameAndUomIdExists(productName, uomId, productTemplateId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isProductNameAndUomIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return isDuplicate;
	}

	public static boolean isProductNameAndUomIdListExists(String productName, List<String> uomList) {
		
		boolean isDuplicate = false;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductTemplateDao productTemplateDao = session.getMapper(ProductTemplateDao.class);

		try {
			isDuplicate = productTemplateDao.isProductNameAndUomIdListExists(productName, uomList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isProductNameAndUomIdListExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return isDuplicate;
	}

}
