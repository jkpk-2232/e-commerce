package com.webapp.models;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.daos.VendorProductDao;

public class VendorProductModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorProductModel.class);

	private String vendorProductId;
	private String vendorId;
	private String productName;
	private String productInformation;
	private double productActualPrice;
	private double productDiscountedPrice;
	private double productWeight;
	private int productWeightUnit;
	private String productSpecification;
	private String productImage;
	private boolean isActive;
	private boolean isDeleted;
	private boolean isPaid;
	private boolean isProductForAllVendorStores;
	private String vendorStoreId;
	private String productSku;
	private String productCategory;
	private long productInventoryCount;
	private String productBarcode;
	private String productCategoryId;
	private String prdQtyType;
	private boolean isNonVeg;
	private double gst;
	private String productSubCategoryId;

	private String vendorName;
	private String storeName;
	private String productSubCategory;
	private String nonVeg;
	
	private double storePrice;
	private String userProductSku;
	private String productTemplateId;
	private String productVariantId;
	private String uomName;
	
	private int action;
	
	private String productsInCart;
	private String cartValue;
	
	private String storeLat;
	private String storeLng;
	private String storeImage;
	private String isClosedToday;
	private String storeAddress;
	private String serviceId;

	public String insertVendorProducts(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		this.vendorProductId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		this.isActive = true;
		this.isDeleted = false;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			productDao.insertVendorProducts(this);
			try {
				URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_ADD);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				vPrintout = new DataOutputStream(connection.getOutputStream());
				ObjectMapper obj = new ObjectMapper();
				String jsonString = obj.writeValueAsString(new ArrayList<>().add( productDao.getVendorProductsDetailsByProductId(this.getVendorProductId())));
				vPrintout.writeBytes(jsonString);
				vPrintout.flush();
				vPrintout.close();
				connection.connect();
				int responseCode = connection.getResponseCode();
				logger.info("\n\n\n\n\t add product response code :\t" + responseCode);
			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error product creation ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorProducts : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}

		return this.vendorProductId;
	}

	public static void insertVendorProductsBatchInsert(List<VendorProductModel> vendorProductList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			productDao.insertVendorProductsBatchInsert(vendorProductList);
			List<String> vendorProdIdList = new ArrayList<>();
			
			for (VendorProductModel vendorProductModel : vendorProductList) {
				if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(vendorProductModel.getVendorId())) {
					vendorProdIdList.add(vendorProductModel.getVendorProductId());
				}
			}

			try {
				if (vendorProdIdList.size() > 0) {
					URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_ADD);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json");
					vPrintout = new DataOutputStream(connection.getOutputStream());
					ObjectMapper obj = new ObjectMapper();
					String jsonString = obj.writeValueAsString(productDao.getVendorProductsDetailsByProductIdsList(vendorProdIdList));
					vPrintout.writeBytes(jsonString);
					vPrintout.flush();
					vPrintout.close();
					connection.connect();
					int responseCode = connection.getResponseCode();
					logger.info("\n\n\n\n\t add product response code :\t" + responseCode);
				}
			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error product creation ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorProductsBatchInsert : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
	}

	public void updateVendorProducts(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		//this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			productDao.updateVendorProducts(this);
			try {
				URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_EDIT);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				vPrintout = new DataOutputStream(connection.getOutputStream());
				ObjectMapper obj = new ObjectMapper();
				String jsonString = obj.writeValueAsString(productDao.getVendorProductsDetailsByProductId(this.vendorProductId));
				vPrintout.writeBytes(jsonString);
				vPrintout.flush();
				vPrintout.close();
				connection.connect();
				int responseCode = connection.getResponseCode();
				logger.info("\n\n\n\n\t edit product response code :\t" + responseCode);
			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error product edit ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProducts : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
	}

	public static int getVendorProductsCount(long startDatelong, long endDatelong, String vendorId, String serviceId, String categoryId, String vendorStoreId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			count = productDao.getVendorProductsCount(startDatelong, endDatelong, vendorId, serviceId, categoryId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<VendorProductModel> getVendorProductsSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String displayType, String vendorId, String serviceId, String categoryId, String orderColumn, String vendorStoreId) {

		List<VendorProductModel> serviceList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			serviceList = productDao.getVendorProductsSearch(startDatelong, endDatelong, searchKey, start, length, displayType, vendorId, serviceId, categoryId, orderColumn, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductsSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceList;
	}

	public static int getVendorProductsSearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId, String serviceId, String categoryId, String vendorStoreId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			count = productDao.getVendorProductsSearchCount(startDatelong, endDatelong, searchKey, vendorId, serviceId, categoryId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductsSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateProductsStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			productDao.updateVendorProductsStatus(this);
			try {
				URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_DELETE);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				vPrintout = new DataOutputStream(connection.getOutputStream());
				ObjectMapper obj = new ObjectMapper();
				String jsonString = obj.writeValueAsString(productDao.getVendorProductsDetailsByProductId(this.vendorProductId));
				vPrintout.writeBytes(jsonString);
				vPrintout.flush();
				vPrintout.close();
				connection.connect();
				int responseCode = connection.getResponseCode();
				logger.info("\n\n\n\n\t    response code :\t" + responseCode);
			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error product creation ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductsStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
	}

	public static VendorProductModel getProductsDetailsByProductId(String vendorProductId) {

		VendorProductModel serviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			serviceModel = productDao.getVendorProductsDetailsByProductId(vendorProductId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductsDetailsByProductId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceModel;
	}

	public static List<VendorProductModel> getProductDetailsByVendorProductsIdsAndVendorStoreIdList(List<String> vendorProductIds, String vendorStoreId) {

		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getProductDetailsByVendorProductsIdsAndVendorStoreIdList(vendorProductIds, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductDetailsByVendorProductsIdsAndVendorStoreIdList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static List<VendorProductModel> getProductListApi(String vendorId, List<String> vendorStoreIdList, int start, int length, String productStatus, String searchKey, String filterOrder) {

		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getProductListApi(vendorId, vendorStoreIdList, start, length, productStatus, searchKey, filterOrder);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductListApi : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static List<VendorProductModel> getVendorProductListByProductSkus(List<String> productSkus, String vendorId, List<String> vendorStoreIdList) {

		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getVendorProductListByProductSkus(productSkus, vendorId, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorProductListByProductSkus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public static void updateProductInventoryCount(String userId, List<VendorProductModel> vpmList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		long updatedAt = DateUtils.nowAsGmtMillisec();
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;
		
		List<VendorProductModel> prodList = new ArrayList<>();

		for (VendorProductModel vendorProductModel : vpmList) {
			vendorProductModel.setUpdatedAt(updatedAt);
			vendorProductModel.setUpdatedBy(userId);
			if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(vendorProductModel.getVendorId())) {
				prodList.add(vendorProductModel);
			}
		}

		try {
			productDao.updateProductInventoryCount(vpmList);
			
			if (prodList.size() > 0) {
				try {
					URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_EDIT);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json");
					vPrintout = new DataOutputStream(connection.getOutputStream());
					ObjectMapper obj = new ObjectMapper();
					String jsonString = obj.writeValueAsString(prodList);
					vPrintout.writeBytes(jsonString);
					vPrintout.flush();
					vPrintout.close();
					connection.connect();
					int responseCode = connection.getResponseCode();
					logger.info("\n\n\n\n\t update product inventory response code :\t" + responseCode);
				} catch (Exception e) {
					logger.error("\n\n\n\n\t ================> error product inventory ... \t" + e);
				}
			}
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductInventoryCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public String getVendorProductId() {
		return vendorProductId;
	}

	public void setVendorProductId(String vendorProductId) {
		this.vendorProductId = vendorProductId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
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

	public double getProductActualPrice() {
		return productActualPrice;
	}

	public void setProductActualPrice(double productActualPrice) {
		this.productActualPrice = productActualPrice;
	}

	public double getProductDiscountedPrice() {
		return productDiscountedPrice;
	}

	public void setProductDiscountedPrice(double productDiscountedPrice) {
		this.productDiscountedPrice = productDiscountedPrice;
	}

	public double getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(double productWeight) {
		this.productWeight = productWeight;
	}

	public String getProductSpecification() {
		return productSpecification;
	}

	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
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

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public int getProductWeightUnit() {
		return productWeightUnit;
	}

	public void setProductWeightUnit(int productWeightUnit) {
		this.productWeightUnit = productWeightUnit;
	}

	public boolean isProductForAllVendorStores() {
		return isProductForAllVendorStores;
	}

	public void setProductForAllVendorStores(boolean isProductForAllVendorStores) {
		this.isProductForAllVendorStores = isProductForAllVendorStores;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public long getProductInventoryCount() {
		return productInventoryCount;
	}

	public void setProductInventoryCount(long productInventoryCount) {
		this.productInventoryCount = productInventoryCount;
	}

	public String getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	public static VendorProductModel getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(String vendorId, String productSku, String productName, String productCategory, String vendorStoreId, String productBarcode) {
		
		VendorProductModel productObject = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);


		try {
			productObject = productDao.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode(vendorId, productSku, productName, productCategory, vendorStoreId, productBarcode);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreIdAndProductBarcode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return productObject;

	}
	
	public static List<VendorProductModel> getProductListByVendorId(String vendorId) {
		
		List<VendorProductModel> productList = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);


		try {
			productList = productDao.getProductListByVendorId(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductListByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return productList;
	}
	
	public static List<VendorProductModel> getProductListWithOutPagination(String vendorId, String vendorStoreId, String productStatus, String productCategoryId, String searchKey, String productSubCategoryId) {
		
		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getProductListWithOutPagination(vendorId, vendorStoreId, productStatus, productCategoryId, searchKey, productSubCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductListWithOutPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
		
	}
	
	public static List<VendorProductModel> getProductDetailsByProductSkuAndVendorStoreIdList(List<String> productSkuList, String vendorStoreId) {

		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getProductDetailsByProductSkuAndVendorStoreIdList(productSkuList, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductDetailsByProductSkuAndVendorStoreIdList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public void updateVendorProductsFromCSV(String userId) {
	
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			productDao.updateVendorProductsFromCSV(this);
			try {
				URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_EDIT);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				vPrintout = new DataOutputStream(connection.getOutputStream());
				ObjectMapper obj = new ObjectMapper();
				String jsonString = obj.writeValueAsString(productDao.getVendorProductsDetailsByProductId(this.vendorProductId));
				vPrintout.writeBytes(jsonString);
				vPrintout.flush();
				vPrintout.close();
				connection.connect();
				int responseCode = connection.getResponseCode();
				logger.info("\n\n\n\n\t edit product response code :\t" + responseCode);
			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error product edit ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProducts : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
		
	}

	public static VendorProductModel getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(String vendorId, String productSku, String productName, String productCategory, String vendorStoreId) {
		
		VendorProductModel productObject = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);


		try {
			productObject = productDao.getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId(vendorId, productSku, productName, productCategory, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getvendorProductByVendorIdAndProductSkuAndProductNameAndProductCategoryAndVendorStoreId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return productObject;
	}

	public String getPrdQtyType() {
		return prdQtyType;
	}

	public void setPrdQtyType(String prdQtyType) {
		this.prdQtyType = prdQtyType;
	}

	public static boolean isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(String vendorId, String vendorStoreId, String productBarcode) {
		
		boolean isDuplicate = false;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);


		try {
			isDuplicate = productDao.isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode(vendorId, vendorStoreId, productBarcode);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isVendorProductExistsByVendorIdAndVendorStoreIdAndProductBarcode : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return isDuplicate;
	}

	public static List<VendorProductModel> getNewlyAddedProductsList(String vendorId, String vendorStoreId) {
		
		List<VendorProductModel> vendorProductList = new ArrayList<>();
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);


		try {
			vendorProductList = productDao.getNewlyAddedProductsList(vendorId, vendorStoreId);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNewlyAddedProductsList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorProductList;
		
	}

	public boolean getIsNonVeg() {
		return isNonVeg;
	}

	public void setIsNonVeg(boolean isNonVeg) {
		this.isNonVeg = isNonVeg;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public String getProductSubCategoryId() {
		return productSubCategoryId;
	}

	public void setProductSubCategoryId(String productSubCategoryId) {
		this.productSubCategoryId = productSubCategoryId;
	}

	public String getProductSubCategory() {
		return productSubCategory;
	}

	public void setProductSubCategory(String productSubCategory) {
		this.productSubCategory = productSubCategory;
	}

	public String getNonVeg() {
		return nonVeg;
	}

	public void setNonVeg(String nonVeg) {
		this.nonVeg = nonVeg;
	}
	
	public static List<VendorProductModel> getOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(String vendorId, String vendorStoreId, String productCategory) {
		
		List<VendorProductModel> vendorProductList = new ArrayList<>();
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		try {
			vendorProductList = productDao.getOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(vendorId, vendorStoreId, productCategory);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorProductList;
	}
	
	public static List<VendorProductModel> getProductListForStore(String vendorId, List<String> vendorStoreIdList, String productStatus ) {
		
		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getProductListForStore(vendorId, vendorStoreIdList, productStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductListForStore : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
		
	}
	
	public static List<VendorProductModel> getVendorProductListForKPMARTByProductSkus(List<String> productSkus, String vendorId, String vendorStoreId) {
		List<VendorProductModel> list = new ArrayList<>();
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		try {
			list = productDao.getVendorProductListForKPMARTByProductSkus(productSkus, vendorId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorProductListForKPMARTByProductSkus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return list;
	}

	public double getStorePrice() {
		return storePrice;
	}

	public void setStorePrice(double storePrice) {
		this.storePrice = storePrice;
	}

	public String getUserProductSku() {
		return userProductSku;
	}

	public void setUserProductSku(String userProductSku) {
		this.userProductSku = userProductSku;
	}

	public String getProductTemplateId() {
		return productTemplateId;
	}

	public void setProductTemplateId(String productTemplateId) {
		this.productTemplateId = productTemplateId;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}
	
	public static List<VendorProductModel> getNewProductListWithOutPagination(String vendorId, String vendorStoreId, String productStatus, String productCategoryId, String searchKey, String productSubCategoryId) {
		
		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getNewProductListWithOutPagination(vendorId, vendorStoreId, productStatus, productCategoryId, searchKey, productSubCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductListWithOutPagination : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
		
	}
	
	public static VendorProductModel getNewProductsDetailsByProductId(String vendorProductId) {

		VendorProductModel serviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			serviceModel = productDao.getNewVendorProductsDetailsByProductId(vendorProductId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNewProductsDetailsByProductId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceModel;
	}
	
	public static List<VendorProductModel> getVendorProductListForKPMARTByProductIds(List<String> productIds, String vendorId, String vendorStoreId) {
		List<VendorProductModel> list = new ArrayList<>();
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		try {
			list = productDao.getVendorProductListForKPMARTByProductSkus(productIds, vendorId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorProductListForKPMARTByProductSkus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return list;
	}
	
	public static List<VendorProductModel> getNewlyAddedProductIdsList(String vendorId, String vendorStoreId) {
		
		List<VendorProductModel> vendorProductList = new ArrayList<>();
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);


		try {
			vendorProductList = productDao.getNewlyAddedProductIdsList(vendorId, vendorStoreId);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNewlyAddedProductIdsList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorProductList;
		
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}
	
	public static List<VendorProductModel> getNewOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(String vendorId, String vendorStoreId, String productCategory) {
		
		List<VendorProductModel> vendorProductList = new ArrayList<>();
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		try {
			vendorProductList = productDao.getNewOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(vendorId, vendorStoreId, productCategory);
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNewOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorProductList;
	}

	public void updateProductNameAndWeightAndQtyTypeAndIsNonVeg(String userId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;
		List<VendorProductModel> productsList = new ArrayList<>();

		try {
			productDao.updateProductNameAndWeightAndQtyTypeAndIsNonVeg(this);
			productsList = productDao.getProductListByProductVariantIdAndProductTemplateId(this.getProductVariantId(), null);
			if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(this.getVendorId())) {
				if (productsList.size() > 0) {
					for (VendorProductModel vendorProductModel : productsList) {
						try {
							URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_EDIT);
							connection = (HttpURLConnection) url.openConnection();
							connection.setDoOutput(true);
							connection.setRequestMethod("POST");
							connection.setRequestProperty("Content-Type", "application/json");
							vPrintout = new DataOutputStream(connection.getOutputStream());
							ObjectMapper obj = new ObjectMapper();
							String jsonString = obj.writeValueAsString(productDao.getVendorProductsDetailsByProductId(vendorProductModel.getVendorProductId()));
							vPrintout.writeBytes(jsonString);
							vPrintout.flush();
							vPrintout.close();
							connection.connect();
							int responseCode = connection.getResponseCode();
							logger.info("\n\n\n\n\t edit product response code :\t" + responseCode);
						} catch (Exception e) {
							logger.error("\n\n\n\n\t ================> error product edit ... \t" + e);
						}
					}
				}
			}
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductNameAndWeightAndQtyTypeAndIsNonVeg : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
		
	}

	public void updateInformationAndSpecificationAndWeightUnitAndCategoryAndSubCategory(String userId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;
		List<VendorProductModel> productsList = new ArrayList<>();

		try {
			productDao.updateInformationAndSpecificationAndWeightUnitAndCategoryAndSubCategory(this);
			productsList = productDao.getProductListByProductVariantIdAndProductTemplateId(null, this.getProductTemplateId());
			if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(this.getVendorId())) {
				if (productsList.size() > 0) {
					for (VendorProductModel vendorProductModel : productsList) {
						try {
							URL url = new URL(UrlConstants.VENDOR_PRODUCT_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_EDIT);
							connection = (HttpURLConnection) url.openConnection();
							connection.setDoOutput(true);
							connection.setRequestMethod("POST");
							connection.setRequestProperty("Content-Type", "application/json");
							vPrintout = new DataOutputStream(connection.getOutputStream());
							ObjectMapper obj = new ObjectMapper();
							String jsonString = obj.writeValueAsString(productDao.getVendorProductsDetailsByProductId(vendorProductModel.getVendorProductId()));
							vPrintout.writeBytes(jsonString);
							vPrintout.flush();
							vPrintout.close();
							connection.connect();
							int responseCode = connection.getResponseCode();
							logger.info("\n\n\n\n\t edit product response code :\t" + responseCode);
						} catch (Exception e) {
							logger.error("\n\n\n\n\t ================> error product edit ... \t" + e);
						}
					}
				}
			}
			
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateInformationAndSpecificationAndWeightUnitAndCategoryAndSubCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
		
	}

	public void updateProductCategory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			productDao.updateProductCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateProductCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public boolean isVendorStoreIdAndProductVariantIdExists(String vendorStoreId, String productVariantId, String vendorProductId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		boolean isDuplicate = false;

		try {
			isDuplicate = productDao.isVendorStoreIdAndProductVariantIdExists(vendorStoreId, productVariantId, vendorProductId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isVendorStoreIdAndProductVariantIdExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}

	public static List<VendorProductModel> getBrandProductsListForDashboard(String brandId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		
		List<VendorProductModel> productsList = new ArrayList<>();
		
		try {
			productsList = productDao.getBrandProductsListForDashboard(brandId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBrandProductsListForDashboard : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return productsList;
	}

	public String getProductsInCart() {
		return productsInCart;
	}

	public void setProductsInCart(String productsInCart) {
		this.productsInCart = productsInCart;
	}

	public String getCartValue() {
		return cartValue;
	}

	public void setCartValue(String cartValue) {
		this.cartValue = cartValue;
	}

	public static List<String> getDistinctVendorStoreIdsBasedonBrandId(String brandId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);
		
		List<String> vendorStoreIdsList = new ArrayList<>();
		
		try {
			vendorStoreIdsList = productDao.getDistinctVendorStoreIdsBasedonBrandId(brandId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDistinctVendorStoreIdsBasedonBrandId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorStoreIdsList;
	}

	public static List<VendorProductModel> getProductListForApiSearch(int start, int length, String productStatus, String searchKey, String filterOrder) {
		
		List<VendorProductModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductDao productDao = session.getMapper(VendorProductDao.class);

		try {
			list = productDao.getProductListForApiSearch(start, length, productStatus, searchKey, filterOrder);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getProductListForApiSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}
	

	public String getStoreLat() {
		return storeLat;
	}

	public void setStoreLat(String storeLat) {
		this.storeLat = storeLat;
	}

	public String getStoreLng() {
		return storeLng;
	}

	public void setStoreLng(String storeLng) {
		this.storeLng = storeLng;
	}

	public String getStoreImage() {
		return storeImage;
	}

	public void setStoreImage(String storeImage) {
		this.storeImage = storeImage;
	}

	public String getIsClosedToday() {
		return isClosedToday;
	}

	public void setIsClosedToday(String isClosedToday) {
		this.isClosedToday = isClosedToday;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}