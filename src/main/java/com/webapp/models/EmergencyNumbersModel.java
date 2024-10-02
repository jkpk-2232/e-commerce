package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.daos.EmergencyNumbersDao;

public class EmergencyNumbersModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(EmergencyNumbersModel.class);

	private String emergencyNumberId;
	private String name;
	private String phoneNo;
	private String noType;

	public String getEmergencyNumberId() {
		return emergencyNumberId;
	}

	public void setEmergencyNumberId(String emergencyNumberId) {
		this.emergencyNumberId = emergencyNumberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getNoType() {
		return noType;
	}

	public void setNoType(String noType) {
		this.noType = noType;
	}

	public static List<EmergencyNumbersModel> getEmergencyPoliceNumbers(String noType) {

		List<EmergencyNumbersModel> emergencyNumbersModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersDao emergencyNumbersDao = session.getMapper(EmergencyNumbersDao.class);

		try {
			emergencyNumbersModelList = emergencyNumbersDao.getEmergencyPoliceNumbers(noType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getEmergencyPoliceNumbers : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return emergencyNumbersModelList;
	}

	public int addEmergencyNumber(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersDao emergencyNumbersDao = session.getMapper(EmergencyNumbersDao.class);

		try {

			this.emergencyNumberId = UUIDGenerator.generateUUID();
			this.createdAt = DateUtils.nowAsGmtMillisec();
			this.updatedAt = this.createdAt;
			this.createdBy = userId;
			this.updatedBy = userId;
			status = emergencyNumbersDao.addEmergencyNumber(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addEmergencyNumber : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static int getEmergencyNumberCountByType(String noType) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersDao emergencyNumbersDao = session.getMapper(EmergencyNumbersDao.class);

		try {
			status = emergencyNumbersDao.getEmergencyNumberCountByType(noType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getEmergencyNumberCountByType : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static List<EmergencyNumbersModel> getEmergencyNumberListByTypeForSearch(int start, int length, String order, String globalSearchString, String noType) {

		List<EmergencyNumbersModel> emergencyNumbersModelList = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersDao emergencyNumbersDao = session.getMapper(EmergencyNumbersDao.class);

		try {
			emergencyNumbersModelList = emergencyNumbersDao.getEmergencyNumberListByTypeForSearch(start, length, order, globalSearchString, noType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getEmergencyNumberListByTypeForSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return emergencyNumbersModelList;
	}

	public static int getTotalEmergencyNumberListByTypeCountBySearch(String globalSearchString, String noType) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersDao emergencyNumbersDao = session.getMapper(EmergencyNumbersDao.class);

		try {
			status = emergencyNumbersDao.getTotalEmergencyNumberListByTypeCountBySearch(globalSearchString, noType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getTotalEmergencyNumberListByTypeCountBySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public int deleteEmergencyNumber(String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersDao emergencyNumbersDao = session.getMapper(EmergencyNumbersDao.class);

		try {
			this.setRecordStatus(ProjectConstants.DEACTIVATE_STATUS);
			this.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			this.setUpdatedBy(userId);
			status = emergencyNumbersDao.deleteEmergencyNumber(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteEmergencyNumber : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

}