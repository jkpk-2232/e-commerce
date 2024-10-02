package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.CartModel;

public interface CartDao {

	void addCart(CartModel cartModel);

	List<CartModel> getCartDetailsByUser(@Param("loggedInuserId") String loggedInuserId);

	void deleteCartItemByCartId(@Param("cartId") String cartId);

	void updateCart(CartModel cartModel);

	void addMultipleCartItems(@Param("cartModelList") List<CartModel> cartModelList);

	void deleteTotalCartItemsByVendorAndStoreAndUser(@Param("vendorId") String vendorId, @Param("vendorStoreId") String vendorStoreId, @Param("loggedInUserId") String loggedInUserId);
	
	List<CartModel> getNewCartDetailsByUser(@Param("loggedInuserId") String loggedInuserId);

}
