package com.webapp.daos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.DeliveryAddressModel;

public interface DeliveryAddressDao {

	void insertDeliveryAddress(DeliveryAddressModel deliveryAddressModel);

	void updateDeliveryAddress(DeliveryAddressModel deliveryAddressModel);

	DeliveryAddressModel getDeliveryAddressByAddressTypeAndUserId(@Param("userId") String userId, @Param("addressType") String addressType);

	List<DeliveryAddressModel> getDeliveryAddressList(@Param("userId") String userId, @Param("start") int start, @Param("length") int length, @Param("searchKey") String searchKey);

	void deleteDeliveryAddress(DeliveryAddressModel deliveryAddressModel);

	void updateIsDefaultByUserId(@Param("loggedInuserId") String loggedInuserId, @Param("isDefault") boolean isDefault, @Param("updatedAt") Long updatedAt, @Param("updatedBy") String updatedBy);
}