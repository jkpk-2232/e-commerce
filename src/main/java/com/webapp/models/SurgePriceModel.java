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
import com.webapp.ProjectConstants;
import com.webapp.daos.SurgePriceDao;

public class SurgePriceModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(SurgePriceModel.class);

	private String surgePriceId;
	private String multicityCityRegionId;
	private double surgePrice;
	private long startTime;
	private long endTime;
	private boolean isActive;

	private String cityDisplayName;

	public String getSurgePriceId() {
		return surgePriceId;
	}

	public void setSurgePriceId(String surgePriceId) {
		this.surgePriceId = surgePriceId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public double getSurgePrice() {
		return surgePrice;
	}

	public void setSurgePrice(double surgePrice) {
		this.surgePrice = surgePrice;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getCityDisplayName() {
		return cityDisplayName;
	}

	public void setCityDisplayName(String cityDisplayName) {
		this.cityDisplayName = cityDisplayName;
	}

	public int addSurgePriceDetails(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			this.surgePriceId = UUIDGenerator.generateUUID();
			this.isActive = true;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;

			status = surgePriceDao.addSurgePriceDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during addSurgePriceDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int updateSurgePriceDetails(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = userId;

			status = surgePriceDao.updateSurgePriceDetails(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during updateSurgePriceDetails : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public static SurgePriceModel getSurgePriceDetailsByIdAndActiveStatus(String surgePriceId, boolean isActive) {

		SurgePriceModel surgePriceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			surgePriceModel = surgePriceDao.getSurgePriceDetailsByIdAndActiveStatus(surgePriceId, isActive);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSurgePriceDetailsByIdAndActiveStatus : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return surgePriceModel;
	}

	public static SurgePriceModel getSurgePriceDetailsById(String surgePriceId) {

		SurgePriceModel surgePriceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			surgePriceModel = surgePriceDao.getSurgePriceDetailsById(surgePriceId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSurgePriceDetailsById : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return surgePriceModel;
	}

	public static int getSurgePriceCount(double surgePriceInDouble, String multicityCityRegionId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			count = surgePriceDao.getSurgePriceCount(surgePriceInDouble, multicityCityRegionId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSurgePriceCount : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return count;
	}

	public static List<SurgePriceModel> getSurgePriceListForSearch(int start, int length, String order, String globalSearchString, double surgePriceInDouble, String multicityCityRegionId) {

		List<SurgePriceModel> surgePriceModelList = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("start", start);
		inputMap.put("length", length);
		inputMap.put("order", order);
		inputMap.put("globalSearchString", globalSearchString);
		inputMap.put("surgePriceInDouble", surgePriceInDouble);
		inputMap.put("multicityCityRegionId", multicityCityRegionId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			surgePriceModelList = surgePriceDao.getSurgePriceListForSearch(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSurgePriceListForSearch : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return surgePriceModelList;
	}

	public static List<SurgePriceModel> getSurgePriceListByMulticityCityRegionId(String multicityCityRegionId) {

		List<SurgePriceModel> surgePriceModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			surgePriceModelList = surgePriceDao.getSurgePriceListByMulticityCityRegionId(multicityCityRegionId);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSurgePriceListByMulticityCityRegionId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return surgePriceModelList;
	}

	public static SurgePriceModel getSurgePriceDetailsByRequestTimeAndRegionId(long requestTimeInMilli, String multicityCityRegionId) {

		SurgePriceModel surgePriceModel = null;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("requestTimeInMilli", requestTimeInMilli);
		inputMap.put("multicityCityRegionId", multicityCityRegionId);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			surgePriceModel = surgePriceDao.getSurgePriceDetailsByRequestTimeAndRegionId(inputMap);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during getSurgePriceDetailsByRequestTimeAndRegionId : ", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return surgePriceModel;
	}

	public int activateDeactivateSurgePrice(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);

			status = surgePriceDao.activateDeactivateSurgePrice(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during activateDeactivateSurgePrice :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

	public int deleteSurgePrice(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		SurgePriceDao surgePriceDao = session.getMapper(SurgePriceDao.class);

		try {

			this.isActive = false;
			this.recordStatus = ProjectConstants.DEACTIVATE_STATUS;
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);

			status = surgePriceDao.deleteSurgePrice(this);
			session.commit();

		} catch (Throwable t) {

			session.rollback();
			logger.error("Exception occured during deleteSurgePrice :", t);
			throw new PersistenceException(t);

		} finally {

			session.close();
		}

		return status;
	}

}
