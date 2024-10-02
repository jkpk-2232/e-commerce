package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.CartDao;

public class CartModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CartModel.class);

	private String cartId;
	private String vendorId;
	private String vendorStoreId;
	private int quantity;
	private String userId;
	private String vendorProductId;
	private boolean isActive;
	private double totalCost;
	
	private String productName;
	private String productImage;
	private double productWeight;
	private int productWeightUnit;
	private double productActualPrice;
	private double productDiscountedPrice;
	private long productInventoryCount;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVendorProductId() {
		return vendorProductId;
	}

	public void setVendorProductId(String vendorProductId) {
		this.vendorProductId = vendorProductId;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public  static void addCart(CartModel cartModel) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);

		try {
			cartModel.setCartId(UUIDGenerator.generateUUID());
			cartModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			cartModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			cartModel.setCreatedBy(cartModel.getUserId());
			cartModel.setUpdatedBy(cartModel.getUserId());
			cartDao.addCart(cartModel);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addCart : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "CartModel [cartId=" + cartId + ", vendorId=" + vendorId + ", vendorStoreId=" + vendorStoreId + ", quantity=" + quantity + ", userId=" + userId + ", vendorProductId=" + vendorProductId + ", isActive=" + isActive + ", totalCost=" + totalCost + "]";
	}

	public static List<CartModel> getCartDetailsByUser(String loggedInuserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);
		
		List<CartModel> cartModelList = null;

		try {
			
			cartModelList = cartDao.getCartDetailsByUser(loggedInuserId);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addCart : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return cartModelList;
	}

	public static void deleteCartItemByCartId(String cartId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);
		
		try {
			
			cartDao.deleteCartItemByCartId(cartId);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteCartItemByCartId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		
	}

	public static void updateCart(CartModel cartModel) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);

		try {
			
			cartDao.updateCart(cartModel);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addCart : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static void addMultipleCartItems(List<CartModel> cartModelList) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);

		try {
			
			cartDao.addMultipleCartItems(cartModelList);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addMultipleCartItems : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public double getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(double productWeight) {
		this.productWeight = productWeight;
	}

	public int getProductWeightUnit() {
		return productWeightUnit;
	}

	public void setProductWeightUnit(int productWeightUnit) {
		this.productWeightUnit = productWeightUnit;
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

	public static void deleteTotalCartItemsByVendorAndStoreAndUser(String vendorId, String vendorStoreId, String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);
		
		try {
			
			cartDao.deleteTotalCartItemsByVendorAndStoreAndUser(vendorId, vendorStoreId, loggedInUserId);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteCartItemByCartId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public long getProductInventoryCount() {
		return productInventoryCount;
	}

	public void setProductInventoryCount(long productInventoryCount) {
		this.productInventoryCount = productInventoryCount;
	}
	
	public static List<CartModel> getNewCartDetailsByUser(String loggedInuserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CartDao cartDao = session.getMapper(CartDao.class);
		
		List<CartModel> cartModelList = null;

		try {
			
			cartModelList = cartDao.getNewCartDetailsByUser(loggedInuserId);
			session.commit();
			
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNewCartDetailsByUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return cartModelList;
	}

}
