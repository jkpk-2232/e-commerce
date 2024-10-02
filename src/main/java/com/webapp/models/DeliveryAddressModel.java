package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.DeliveryAddressDao;

public class DeliveryAddressModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DeliveryAddressModel.class);

	private String deliveryAddressId;
	private String userId;
	private String addressType;
	private String address;
	private String addressLat;
	private String addressLng;
	private String placeId;
	private String addressGeolocation;
	private boolean isDefault;
	private String name;
	private String phoneNum;

	public String insertDeliveryAddress(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DeliveryAddressDao deliveryAddressDao = session.getMapper(DeliveryAddressDao.class);

		this.setAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getAddressLng() + "  " + this.getAddressLat() + ")')");
		this.deliveryAddressId = UUIDGenerator.generateUUID();
		this.userId = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			deliveryAddressDao.insertDeliveryAddress(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertDeliveryAddress : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.deliveryAddressId;
	}

	public void updateDeliveryAddress(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DeliveryAddressDao deliveryAddressDao = session.getMapper(DeliveryAddressDao.class);

		this.setAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getAddressLng() + "  " + this.getAddressLat() + ")')");
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			deliveryAddressDao.updateDeliveryAddress(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDeliveryAddress : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static DeliveryAddressModel getDeliveryAddressByAddressTypeAndUserId(String userId, String addressType) {

		DeliveryAddressModel deliveryAddressModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DeliveryAddressDao deliveryAddressDao = session.getMapper(DeliveryAddressDao.class);

		try {
			deliveryAddressModel = deliveryAddressDao.getDeliveryAddressByAddressTypeAndUserId(userId, addressType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertDeliveryAddress : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return deliveryAddressModel;
	}

	public static List<DeliveryAddressModel> getDeliveryAddressList(String userId, int start, int length, String searchKey) {

		List<DeliveryAddressModel> deliveryAddressList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DeliveryAddressDao deliveryAddressDao = session.getMapper(DeliveryAddressDao.class);

		try {
			deliveryAddressList = deliveryAddressDao.getDeliveryAddressList(userId, start, length, searchKey);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDeliveryAddressList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return deliveryAddressList;
	}

	public void deleteDeliveryAddress(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DeliveryAddressDao deliveryAddressDao = session.getMapper(DeliveryAddressDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			deliveryAddressDao.deleteDeliveryAddress(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteDeliveryAddress : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getDeliveryAddressId() {
		return deliveryAddressId;
	}

	public void setDeliveryAddressId(String deliveryAddressId) {
		this.deliveryAddressId = deliveryAddressId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressLat() {
		return addressLat;
	}

	public void setAddressLat(String addressLat) {
		this.addressLat = addressLat;
	}

	public String getAddressLng() {
		return addressLng;
	}

	public void setAddressLng(String addressLng) {
		this.addressLng = addressLng;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getAddressGeolocation() {
		return addressGeolocation;
	}

	public void setAddressGeolocation(String addressGeolocation) {
		this.addressGeolocation = addressGeolocation;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public static void updateIsDefaultByUserId(String loggedInuserId, boolean isDefault) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DeliveryAddressDao deliveryAddressDao = session.getMapper(DeliveryAddressDao.class);

		Long updatedAt = DateUtils.nowAsGmtMillisec();
		String updatedBy = loggedInuserId;

		try {
			deliveryAddressDao.updateIsDefaultByUserId(loggedInuserId,isDefault,updatedAt,updatedBy);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateIsDefaultByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

}