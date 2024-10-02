package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ProductVariantDao;

public class ProductVariantModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ProductVariantModel.class);

	private String productVariantId;
	private String productVariantName;
	private String productVariantDescription;
	private double productVariantPrice;
	private String barcode;
	private double weight;
	private String color;
	private String productVariantSku;
	private boolean isActive;
	private boolean isDeleted;
	private String productTemplateId;
	private String productQuantityType;
	private boolean isNonVeg;
	
	private String brandName;
	private String productCategory;
	private String uomName;
	private String productName;

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public String getProductVariantName() {
		return productVariantName;
	}

	public void setProductVariantName(String productVariantName) {
		this.productVariantName = productVariantName;
	}

	public String getProductVariantDescription() {
		return productVariantDescription;
	}

	public void setProductVariantDescription(String productVariantDescription) {
		this.productVariantDescription = productVariantDescription;
	}

	public double getProductVariantPrice() {
		return productVariantPrice;
	}

	public void setProductVariantPrice(double productVariantPrice) {
		this.productVariantPrice = productVariantPrice;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getProductVariantSku() {
		return productVariantSku;
	}

	public void setProductVariantSku(String productVariantSku) {
		this.productVariantSku = productVariantSku;
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

	public String getProductTemplateId() {
		return productTemplateId;
	}

	public void setProductTemplateId(String productTemplateId) {
		this.productTemplateId = productTemplateId;
	}

	public void insertProductVariant(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		this.productVariantId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			variantDao.insertProductVariant(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertProductVariant : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static ProductVariantModel getProductVariantDetailsByProductVariantId(String ProductVariantId) {

		ProductVariantModel productVariantModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		try {
			productVariantModel = variantDao.getProductVariantDetailsByProductVariantId(ProductVariantId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVariantDetailsByProductVariantId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return productVariantModel;
	}

	public void updateProductVariant(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			variantDao.updateProductVariant(this);
			VendorProductModel productModel = new VendorProductModel();
			productModel.setProductName(this.getProductVariantName());
			productModel.setProductWeight(this.getWeight());
			productModel.setPrdQtyType(this.getProductQuantityType());
			productModel.setIsNonVeg(this.isNonVeg());
			productModel.setProductTemplateId(this.getProductTemplateId());
			productModel.setProductVariantId(this.getProductVariantId());
			productModel.setVendorId(loggedInUserId);
			productModel.setProductBarcode(this.getBarcode());
			productModel.updateProductNameAndWeightAndQtyTypeAndIsNonVeg(loggedInUserId);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVariant : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static int getProductVariantsCount(long startDatelong, long endDatelong, List<String> userIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		try {
			count = variantDao.getProductVariantsCount(startDatelong, endDatelong, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVariantsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;

	}

	public static List<ProductVariantModel> getProductVariantSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, List<String> userIdList) {

		List<ProductVariantModel> variantList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		try {
			variantList = variantDao.getProductVariantSearch(startDatelong, endDatelong, searchKey, start, length, userIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductVariantSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return variantList;
	}

	public static int getProductVariantSearchCount(long startDatelong, long endDatelong, String searchKey, List<String> userIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		try {

			count = variantDao.getProductVariantSearchCount(startDatelong, endDatelong, searchKey, userIdList);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductVariantSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateProductVariantStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			variantDao.updateProductVariantStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductVariantStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static List<ProductVariantModel> getProductVariantList(List<String> userIdList, String productTemplateId) {
		
		List<ProductVariantModel> variantList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ProductVariantDao variantDao = session.getMapper(ProductVariantDao.class);

		try {
			variantList = variantDao.getProductVariantList(userIdList, productTemplateId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductVariantList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return variantList;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductQuantityType() {
		return productQuantityType;
	}

	public void setProductQuantityType(String productQuantityType) {
		this.productQuantityType = productQuantityType;
	}

	public boolean isNonVeg() {
		return isNonVeg;
	}

	public void setNonVeg(boolean isNonVeg) {
		this.isNonVeg = isNonVeg;
	}

}
