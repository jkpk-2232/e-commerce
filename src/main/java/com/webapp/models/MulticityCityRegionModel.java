package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.MulticityCityRegionDao;

public class MulticityCityRegionModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(MulticityCityRegionModel.class);

	private String multicityCityRegionId;
	private String multicityCountryId;
	private String cityDisplayName;
	private String cityOriginalName;
	private String regionName;
	private String regionPlaceId;
	private String regionLatitude;
	private String regionLongitude;
	private String regionGeolocation;
	private long regionRadius;
	private boolean isActive;
	private boolean isDeleted;
	private boolean isPermanentDelete;
	private long regionRadiusInMeters;

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

	public String getCityDisplayName() {
		return cityDisplayName;
	}

	public void setCityDisplayName(String cityDisplayName) {
		this.cityDisplayName = cityDisplayName;
	}

	public String getCityOriginalName() {
		return cityOriginalName;
	}

	public void setCityOriginalName(String cityOriginalName) {
		this.cityOriginalName = cityOriginalName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionPlaceId() {
		return regionPlaceId;
	}

	public void setRegionPlaceId(String regionPlaceId) {
		this.regionPlaceId = regionPlaceId;
	}

	public String getRegionLatitude() {
		return regionLatitude;
	}

	public void setRegionLatitude(String regionLatitude) {
		this.regionLatitude = regionLatitude;
	}

	public String getRegionLongitude() {
		return regionLongitude;
	}

	public void setRegionLongitude(String regionLongitude) {
		this.regionLongitude = regionLongitude;
	}

	public String getRegionGeolocation() {
		return regionGeolocation;
	}

	public void setRegionGeolocation(String regionGeolocation) {
		this.regionGeolocation = regionGeolocation;
	}

	public long getRegionRadius() {
		return regionRadius;
	}

	public void setRegionRadius(long regionRadius) {
		this.regionRadius = regionRadius;
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

	public boolean isPermanentDelete() {
		return isPermanentDelete;
	}

	public void setPermanentDelete(boolean isPermanentDelete) {
		this.isPermanentDelete = isPermanentDelete;
	}

	public long getRegionRadiusInMeters() {
		return regionRadiusInMeters;
	}

	public void setRegionRadiusInMeters(long regionRadiusInMeters) {
		this.regionRadiusInMeters = regionRadiusInMeters;
	}

	public static int getMulticityCityRegionCount(String multicityCountryId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			count = multicityCityRegionDao.getMulticityCityRegionCount(multicityCountryId);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionCount : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return count;
	}

	public static List<MulticityCityRegionModel> getMulticityCityRegionSearch(int start, int length, String order, String globalSearchString, String multicityCountryId) {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.getMulticityCityRegionSearch(start, length, globalSearchString, multicityCountryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionSearch : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public static List<MulticityCityRegionModel> getMulticityCityRegionList(List<String> regionList, String multicityCountryId) {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.getMulticityCityRegionList(regionList, multicityCountryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionList : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public String addMulticityCityRegion(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		this.multicityCityRegionId = UUIDGenerator.generateUUID();
		this.createdBy = userId;
		this.updatedBy = userId;
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.isActive = true;
		this.isDeleted = false;

		this.setRegionGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.regionLongitude + "  " + this.regionLatitude + ")')");

		try {
			multicityCityRegionDao.addMulticityCityRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addMulticityCityRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.multicityCityRegionId;
	}

	public String editMulticityCityRegion(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		this.updatedBy = userId;
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.isActive = true;
		this.isDeleted = false;

		this.setRegionGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.regionLongitude + "  " + this.regionLatitude + ")')");

		try {
			multicityCityRegionDao.editMulticityCityRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during editMulticityCityRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return this.multicityCityRegionId;
	}

	public static List<MulticityCityRegionModel> test(Map<String, Object> inputMap) {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.test(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during test : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public static MulticityCityRegionModel getMulticityCityRegionDetailsByCityId(String multicityCityRegionId) {

		MulticityCityRegionModel multicityCountryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {

			multicityCountryModel = multicityCityRegionDao.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);

			if (multicityCountryModel != null) {

				multicityCountryModel.setRegionRadiusInMeters(multicityCountryModel.getRegionRadius() * 1000);
			}

			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionDetailsByCityId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return multicityCountryModel;
	}

	public static List<MulticityCityRegionModel> getNearByRegionList(String latAndLong, String distance) {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.getNearByRegionList(latAndLong, distance);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getNearByRegionList : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public static List<MulticityCityRegionModel> getMulticityCityRegionSearchDatatable(int start, int length, String order, String globalSearchString, String multicityCountryId) {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.getMulticityCityRegionSearchDatatable(start, length, globalSearchString, multicityCountryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionSearchDatatable : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public void permanentDeleteRegion() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();

		try {
			multicityCityRegionDao.permanentDeleteRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during permanentDeleteRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
	}

	public void deleteRegion() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();

		try {
			multicityCityRegionDao.deleteRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
	}

	public void activateRegion() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();

		try {
			multicityCityRegionDao.activateRegion(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during activateRegion : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}
	}

	public static List<MulticityCityRegionModel> getMulticityCityRegionListByMulticityCountryId(String multicityCountryId, List<String> assignedRegionList, int start, int length) {

		List<MulticityCityRegionModel> multicityCityRegionModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("multicityCountryId", multicityCountryId);
		inputMap.put("assignedRegionList", assignedRegionList);
		inputMap.put("start", start);
		inputMap.put("length", length);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCityRegionModelList = multicityCityRegionDao.getMulticityCityRegionListByMulticityCountryId(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return multicityCityRegionModelList;
	}

	public static List<MulticityCityRegionModel> getMulticityRegionDetails() {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.getMulticityCityRegionDetails();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during test : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}

	public static int getMulticityCityRegionCountByVendorId(String multicityCountryId, String vendorId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			count = multicityCityRegionDao.getMulticityCityRegionCountByVendorId(multicityCountryId, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionCountByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<MulticityCityRegionModel> getMulticityCityRegionSearchDatatableByVendorId(int start, int length, String order, String globalSearchString, String multicityCountryId, String vendorId) {

		List<MulticityCityRegionModel> multicityCountryModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		MulticityCityRegionDao multicityCityRegionDao = session.getMapper(MulticityCityRegionDao.class);

		try {
			multicityCountryModelList = multicityCityRegionDao.getMulticityCityRegionSearchDatatableByVendorId(start, length, globalSearchString, multicityCountryId, vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getMulticityCityRegionSearchDatatableByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {

			session.close();
		}

		return multicityCountryModelList;
	}
}