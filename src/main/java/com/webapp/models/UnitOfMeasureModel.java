package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.UnitOfMeasureDao;

public class UnitOfMeasureModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UnitOfMeasureModel.class);

	private Integer uomId;
	private String uomName;
	private String uomShortName;
	private String uomDescription;
	private boolean isActive;
	private boolean isDeleted;

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getUomShortName() {
		return uomShortName;
	}

	public void setUomShortName(String uomShortName) {
		this.uomShortName = uomShortName;
	}

	public String getUomDescription() {
		return uomDescription;
	}

	public void setUomDescription(String uomDescription) {
		this.uomDescription = uomDescription;
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

	public static boolean isUomNameExists(String uomName, Integer uomId) {
		
		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);

		try {
			isDuplicate = uomDao.isUomNameExists(uomName, uomId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isUomNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
		
	}

	public void insertUom(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		//this.uomId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = loggedInUserId;
		this.updatedBy = loggedInUserId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			uomDao.insertUom(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertUom : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static UnitOfMeasureModel getUomDetailsByUomId(Integer uomId) {
		
		UnitOfMeasureModel unitOfMeasureModel = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		try {
			unitOfMeasureModel = uomDao.getUomDetailsByUomId(uomId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUomDetailsByUomId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return unitOfMeasureModel;
	}

	public void updateUom(String loggedInUserId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = loggedInUserId;

		try {
			uomDao.updateUom(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUom : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static int getUomsCount(long startDatelong, long endDatelong) {
		
		int count =0;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		try {
			count = uomDao.getUomsCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUomsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return count;
	}

	public static List<UnitOfMeasureModel> getUomSearch(long startDatelong, long endDatelong, String searchKey, int start, int length) {
		
		List<UnitOfMeasureModel> uomList = new ArrayList<>();
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		try {
			uomList = uomDao.getUomSearch(startDatelong, endDatelong, searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUomSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return uomList;
	}

	public static int getUomSearchCount(long startDatelong, long endDatelong, String searchKey) {
		
		int count = 0;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		try {

			count = uomDao.getUomSearchCount(startDatelong, endDatelong, searchKey);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUomSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
		
	}

	public void updateUomStatus(String userId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);
		
		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			uomDao.updateUomStatus(this);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateUomStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static List<UnitOfMeasureModel> getuomList() {

		List<UnitOfMeasureModel> uomList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnitOfMeasureDao uomDao = session.getMapper(UnitOfMeasureDao.class);

		try {
			uomList = uomDao.getuomList();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getuomList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return uomList;
	}

}
