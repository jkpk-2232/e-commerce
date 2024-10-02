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
import com.webapp.daos.VendorStoreDao;

public class VendorStoreModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorStoreModel.class);
    
	
	private String vendorStoreId;
	private String vendorId;
	private String storeName;
	private String storeAddress;
	private String storeAddressLat;
	private String storeAddressLng;
	private String storePlaceId;
	private String storeAddressGeolocation;
	private String storeImage;
	private boolean isClosedToday;
	private boolean isActive;
	private boolean isDeleted;
	private int dateType;
	private int numberOfShifts;
	private int shiftType;
	private long startDate;
	private long endDate;
	private long dateOpeningMorningHours;
	private long dateClosingMorningHours;
	private long dateOpeningEveningHours;
	private long dateClosingEveningHours;
	private String multicityCityRegionId;
	private String multicityCountryId;
	private boolean ledDeviceForStore;
	private int ledDeviceCount;
	private String type;
	private int numberOfRacks;
	private String rackCategoryId;
	
	private String vendorName;

	private List<VendorStoreTimingModel> vendorStoreTimingList;
	
	private String phonepeStoreId;
	
	private String brandId;
	private String vendorBrandName;

	public String insertVendorStore(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		this.vendorStoreId = UUIDGenerator.generateUUID();
		this.setStoreAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getStoreAddressLng() + "  " + this.getStoreAddressLat() + ")')");
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;
		this.isActive = true;
		this.isDeleted = false;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			vendorStoreDao.insertVendorStore(this);
			try {
				if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(this.vendorId)) {
					URL url = new URL(UrlConstants.STORES_WEBHOOK_URL+"?action="+ProjectConstants.ACTION_ADD);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
					connection.setRequestProperty("Content-Type", "application/json");
					vPrintout = new DataOutputStream(connection.getOutputStream());
					ObjectMapper obj = new ObjectMapper();
					String jsonString = obj.writeValueAsString(vendorStoreDao.getVendorStoreDetailsById(this.getVendorStoreId()));
					vPrintout.writeBytes(jsonString);
					vPrintout.flush();
					vPrintout.close();
					connection.connect();
					int responseCode = connection.getResponseCode();
					logger.info("\n\n\n\n\t add store response code :\t"+responseCode);
				}

			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error sms sending ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorStore : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}

		return this.vendorStoreId;
	}

	public void updateVendorStore(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		this.setStoreAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getStoreAddressLng() + "  " + this.getStoreAddressLat() + ")')");
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			vendorStoreDao.updateVendorStore(this);
			try {
				if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(this.vendorId)) {
					URL url = new URL(UrlConstants.STORES_WEBHOOK_URL+"?action="+ProjectConstants.ACTION_EDIT);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setDoOutput(true);
					connection.setRequestProperty("Content-Type", "application/json");
					vPrintout = new DataOutputStream(connection.getOutputStream());
					ObjectMapper obj = new ObjectMapper();
					String jsonString = obj.writeValueAsString(vendorStoreDao.getVendorStoreDetailsById(this.getVendorStoreId()));
					vPrintout.writeBytes(jsonString);
					vPrintout.flush();
					vPrintout.close();
					connection.connect();
					int responseCode = connection.getResponseCode();
					logger.info("\n\n\n\n\t add store response code :\t"+responseCode);
				}

			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error sms sending ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorStore : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
	}

	public static int getVendorStoreCount(long startDatelong, long endDatelong, String vendorId, List<String> vendorStoreIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			count = vendorStoreDao.getVendorStoreCount(startDatelong, endDatelong, vendorId, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<VendorStoreModel> getVendorStoreSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String vendorId, List<String> vendorStoreIdList) {

		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoreSearch(startDatelong, endDatelong, searchKey, start, length, vendorId, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}

	public static int getVendorStoreSearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId, List<String> vendorStoreIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			count = vendorStoreDao.getVendorStoreSearchCount(startDatelong, endDatelong, searchKey, vendorId, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static VendorStoreModel getVendorStoreDetailsById(String vendorStoreId) {

		VendorStoreModel vendorStoreModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreModel = vendorStoreDao.getVendorStoreDetailsById(vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreModel;
	}

	public static List<VendorStoreModel> getVendorStoresByOpenClosedStatus(boolean isClosedToday, long todayEpoch, String currentDayOfWeekValue) {

		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoresByOpenClosedStatus(isClosedToday, todayEpoch, currentDayOfWeekValue);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoresByOpenClosedStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}

	public static void updateClosedTodayFlag(List<VendorStoreModel> updateList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreDao.updateClosedTodayFlag(updateList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateClosedTodayFlag : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateVendorStoreStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);
		
		HttpURLConnection connection = null;
		DataOutputStream vPrintout = null;

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			vendorStoreDao.updateVendorStoreStatus(this);
			try {
				if (WebappPropertyUtils.KP_MART_DEFAULT_VENDOR_ID.equals(this.vendorId)) {
					URL url = new URL(UrlConstants.STORES_WEBHOOK_URL + "?action=" + ProjectConstants.ACTION_DELETE);
					connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					vPrintout = new DataOutputStream(connection.getOutputStream());
					ObjectMapper obj = new ObjectMapper();
					String jsonString = obj.writeValueAsString(vendorStoreDao.getVendorStoreDetailsById(this.getVendorStoreId()));
					vPrintout.writeBytes(jsonString);
					vPrintout.flush();
					vPrintout.close();
					connection.connect();
					int responseCode = connection.getResponseCode();
					logger.info("\n\n\n\n\t update store response code :\t" + responseCode);
				}
			} catch (Exception e) {
				logger.error("\n\n\n\n\t ================> error sms sending ... \t" + e);
			}
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateVendorStoreStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
			if (connection != null)
				connection.disconnect();
		}
	}

	public static List<VendorStoreModel> getVendorStoreList(List<String> assignedRegionList, String vendorId) {

		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoreList(assignedRegionList, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}

	public static List<VendorStoreModel> getVendorStoreListApi(String vendorId, int start, int length, String searchKey, List<String> vendorStoreIdList) {

		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoreListApi(vendorId, start, length, searchKey, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreListApi : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreAddressLat() {
		return storeAddressLat;
	}

	public void setStoreAddressLat(String storeAddressLat) {
		this.storeAddressLat = storeAddressLat;
	}

	public String getStoreAddressLng() {
		return storeAddressLng;
	}

	public void setStoreAddressLng(String storeAddressLng) {
		this.storeAddressLng = storeAddressLng;
	}

	public String getStorePlaceId() {
		return storePlaceId;
	}

	public void setStorePlaceId(String storePlaceId) {
		this.storePlaceId = storePlaceId;
	}

	public String getStoreAddressGeolocation() {
		return storeAddressGeolocation;
	}

	public void setStoreAddressGeolocation(String storeAddressGeolocation) {
		this.storeAddressGeolocation = storeAddressGeolocation;
	}

	public String getStoreImage() {
		return storeImage;
	}

	public void setStoreImage(String storeImage) {
		this.storeImage = storeImage;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public int getDateType() {
		return dateType;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public int getNumberOfShifts() {
		return numberOfShifts;
	}

	public void setNumberOfShifts(int numberOfShifts) {
		this.numberOfShifts = numberOfShifts;
	}

	public int getShiftType() {
		return shiftType;
	}

	public void setShiftType(int shiftType) {
		this.shiftType = shiftType;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public long getDateOpeningMorningHours() {
		return dateOpeningMorningHours;
	}

	public void setDateOpeningMorningHours(long dateOpeningMorningHours) {
		this.dateOpeningMorningHours = dateOpeningMorningHours;
	}

	public long getDateClosingMorningHours() {
		return dateClosingMorningHours;
	}

	public void setDateClosingMorningHours(long dateClosingMorningHours) {
		this.dateClosingMorningHours = dateClosingMorningHours;
	}

	public long getDateOpeningEveningHours() {
		return dateOpeningEveningHours;
	}

	public void setDateOpeningEveningHours(long dateOpeningEveningHours) {
		this.dateOpeningEveningHours = dateOpeningEveningHours;
	}

	public long getDateClosingEveningHours() {
		return dateClosingEveningHours;
	}

	public void setDateClosingEveningHours(long dateClosingEveningHours) {
		this.dateClosingEveningHours = dateClosingEveningHours;
	}

	public boolean isClosedToday() {
		return isClosedToday;
	}

	public void setClosedToday(boolean isClosedToday) {
		this.isClosedToday = isClosedToday;
	}

	public List<VendorStoreTimingModel> getVendorStoreTimingList() {
		return vendorStoreTimingList;
	}

	public void setVendorStoreTimingList(List<VendorStoreTimingModel> vendorStoreTimingList) {
		this.vendorStoreTimingList = vendorStoreTimingList;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getMulticityCountryId() {
		return multicityCountryId;
	}

	public void setMulticityCountryId(String multicityCountryId) {
		this.multicityCountryId = multicityCountryId;
	}

	public boolean getLedDeviceForStore() {
		return ledDeviceForStore;
	}

	public void setLedDeviceForStore(boolean ledDeviceForStore) {
		this.ledDeviceForStore = ledDeviceForStore;
	}

	public int getLedDeviceCount() {
		return ledDeviceCount;
	}

	public void setLedDeviceCount(int ledDeviceCount) {
		this.ledDeviceCount = ledDeviceCount;
	}
	
	public static List<VendorStoreModel> getVendorStoresBasedOnService(List<String> serviceIds) {
		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoresBasedOnService(serviceIds);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoresBasedOnService : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		return vendorStoreList;
	}
	
	public static List<VendorStoreModel> getVendorStoresListInfo(String vendorId) {

		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoresInfoList(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoresListInfo : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumberOfRacks() {
		return numberOfRacks;
	}

	public void setNumberOfRacks(int numberOfRacks) {
		this.numberOfRacks = numberOfRacks;
	}

	public static List<VendorStoreModel> getVendorStoreListByVendorAndSubVendor(String vendorId, List<String> vendorStoreIdList) {
		
		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getVendorStoreListByVendorAndSubVendor(vendorId, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoreListByVendorAndSubVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}

	public String getRackCategoryId() {
		return rackCategoryId;
	}

	public void setRackCategoryId(String rackCategoryId) {
		this.rackCategoryId = rackCategoryId;
	}

	public String getPhonepeStoreId() {
		return phonepeStoreId;
	}

	public void setPhonepeStoreId(String phonepeStoreId) {
		this.phonepeStoreId = phonepeStoreId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getVendorBrandName() {
		return vendorBrandName;
	}

	public void setVendorBrandName(String vendorBrandName) {
		this.vendorBrandName = vendorBrandName;
	}

	public static List<VendorStoreModel> getERPBrandList(List<String> assignedRegionList, String vendorId) {
		
		List<VendorStoreModel> vendorStoreList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

		try {
			vendorStoreList = vendorStoreDao.getERPBrandList(assignedRegionList, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getERPBrandList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorStoreList;
	}
	
	public static List<VendorStoreModel> getVendorStoresWithRacks(String vendorId, List<String> vendorStoreIdList) {

	    List<VendorStoreModel> vendorStoreList = new ArrayList<>();

	    SqlSession session = ConnectionBuilderAction.getSqlSession();
	    VendorStoreDao vendorStoreDao = session.getMapper(VendorStoreDao.class);

	    try {
	        // Call the DAO method to get only stores with racks
	        vendorStoreList = vendorStoreDao.getVendorStoresWithRacks(vendorId, vendorStoreIdList);
	        session.commit();
	    } catch (Throwable t) {
	        session.rollback();
	        logger.error("Exception occurred during getVendorStoresWithRacks: ", t);
	        throw new PersistenceException(t);
	    } finally {
	        session.close();
	    }

	    return vendorStoreList;
	}
	public String getStoreAddr1essLat1() {
        return storeAddressLat;
    }

    public String getStoreAddressLng1() {
        return storeAddressLng;
    }



}