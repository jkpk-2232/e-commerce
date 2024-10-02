package com.webapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.utils.myhub.MyHubUtils;
import com.webapp.ProjectConstants;
import com.webapp.daos.VendorSubscriberDao;

public class VendorSubscriberModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorSubscriberModel.class);

	private String vendorSubscriberId;
	private String vendorId;
	private String vendorStoreId;
	private String userId;

	private String vendorName;
	private String vendorBrandName;
	private String storeName;
	private String subscriberName;

	public String insertVendorSubscriber(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		this.vendorSubscriberId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorSubscriberDao.insertVendorSubscriber(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorSubscriber : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorSubscriberId;
	}

	public static int getVendorSubscriberCount(long startDatelong, long endDatelong, String vendorId, String vendorStoreId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			count = vendorSubscriberDao.getVendorSubscriberCount(startDatelong, endDatelong, vendorId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorSubscriberCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<VendorSubscriberModel> getVendorSubscriberSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String vendorId, String vendorStoreId) {

		List<VendorSubscriberModel> vendorSubscriberList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			vendorSubscriberList = vendorSubscriberDao.getVendorSubscriberSearch(startDatelong, endDatelong, searchKey, start, length, vendorId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorSubscriberSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorSubscriberList;
	}

	public static int getVendorSubscriberSearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId, String vendorStoreId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			count = vendorSubscriberDao.getVendorSubscriberSearchCount(startDatelong, endDatelong, searchKey, vendorId, vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorSubscriberSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static boolean isUserSubscribedToVendorStore(String vendorStoreId, String userId) {

		boolean isUserSubscribedToVendor = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			isUserSubscribedToVendor = vendorSubscriberDao.isUserSubscribedToVendorStore(vendorStoreId, userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isUserSubscribedToVendorStore : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isUserSubscribedToVendor;
	}

	public void deleteVendorSubscriber() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			vendorSubscriberDao.deleteVendorSubscriber(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteVendorSubscriber : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<VendorSubscriberModel> getVendorSubscribersByVendorId(String vendorId, int start, int length) {

		List<VendorSubscriberModel> vendorSubscriberList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			vendorSubscriberList = vendorSubscriberDao.getVendorSubscribersByVendorId(vendorId, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorSubscribersByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorSubscriberList;
	}

	public static StatsModel getSubscriberStatsByTimeForVendor(String vendorId, long startTime, long endTime) {

		StatsModel statsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			statsModel = vendorSubscriberDao.getSubscriberStatsByTimeForVendor(vendorId, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getSubscriberStatsByTimeForVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return statsModel;
	}

	public String getVendorSubscriberId() {
		return vendorSubscriberId;
	}

	public void setVendorSubscriberId(String vendorSubscriberId) {
		this.vendorSubscriberId = vendorSubscriberId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorBrandName() {
		return vendorBrandName;
	}

	public void setVendorBrandName(String vendorBrandName) {
		this.vendorBrandName = vendorBrandName;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
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

	public static Map<String, Object> getLimitedData(UserProfileModel userProfileModel, boolean isVendorStoreOpened) {
		return getLimitedDataMain(userProfileModel, isVendorStoreOpened);
	}

	public static Map<String, Object> getLimitedData(UserProfileModel userProfileModel) {
		return getLimitedDataMain(userProfileModel, false);
	}

	private static Map<String, Object> getLimitedDataMain(UserProfileModel userProfileModel, boolean isVendorStoreOpen) {

		Map<String, Object> map = new HashMap<>();

		map.put("photoUrl", userProfileModel.getPhotoUrl());
		map.put("phoneNo", userProfileModel.getPhoneNo());
		map.put("phoneNoCode", userProfileModel.getPhoneNoCode());
		map.put("email", userProfileModel.getEmail());
		map.put("firstName", userProfileModel.getFirstName());
		map.put("lastName", userProfileModel.getLastName());
		map.put("companyName", userProfileModel.getCompanyName());
		map.put("companyAddress", userProfileModel.getCompanyAddress());
		map.put("distance", userProfileModel.getDistance());
		map.put("distanceKm", MyHubUtils.getKilometerString(userProfileModel.getDistance()));
		map.put("serviceName", userProfileModel.getServiceName());
		map.put("categoryName", userProfileModel.getCategoryName());
		map.put("serviceId", userProfileModel.getServiceId());
		map.put("categoryId", userProfileModel.getCategoryId());
		map.put("day", userProfileModel.getDay());
		map.put("openingMorningHours", userProfileModel.getOpeningMorningHours());
		map.put("closingMorningHours", userProfileModel.getClosingMorningHours());
		map.put("openingEveningHours", userProfileModel.getOpeningEveningHours());
		map.put("closingEveningHours", userProfileModel.getClosingEveningHours());
		map.put("isVendorStoreSubscribed", userProfileModel.isVendorStoreSubscribed());
		map.put("isVendorStoreOpen", isVendorStoreOpen);
		map.put("isClosedToday", userProfileModel.isClosedToday());
		map.put("vendorId", userProfileModel.getUserId());
		map.put("vendorStoreId", userProfileModel.getVendorStoreId());
		map.put("vendorBrandName", userProfileModel.getVendorBrandName());
		map.put("vendorBrandImage", userProfileModel.getVendorBrandImage());
		map.put("vendorBrandSearchKeywords", userProfileModel.getVendorBrandSearchKeywords());
		map.put("storeAddress", userProfileModel.getStoreAddress());
		map.put("storeAddressLat", userProfileModel.getStoreAddressLat());
		map.put("storeAddressLng", userProfileModel.getStoreAddressLng());
		map.put("storeName", userProfileModel.getStoreName() != null ? userProfileModel.getStoreName() : ProjectConstants.NOT_AVAILABLE);
		map.put("storeImage", userProfileModel.getStoreImage());

		return map;
	}

	public static int getVendorSubscribersCountByVendorId(String vendorId) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorSubscriberDao vendorSubscriberDao = session.getMapper(VendorSubscriberDao.class);

		try {
			count = vendorSubscriberDao.getVendorSubscribersCountByVendorId(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorSubscribersCountByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}
}