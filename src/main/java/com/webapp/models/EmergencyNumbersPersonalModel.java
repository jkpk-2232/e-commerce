package com.webapp.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.EmergencyNumbersPersonalDao;

public class EmergencyNumbersPersonalModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(EmergencyNumbersPersonalModel.class);

	private String emergencyNumberPersonalId;
	private String userId;
	private String name;
	private String phoneNo;

	List<ContactNumberModel> contactNumberList;

	public String getEmergencyNumberPersonalId() {
		return emergencyNumberPersonalId;
	}

	public void setEmergencyNumberPersonalId(String emergencyNumberPersonalId) {
		this.emergencyNumberPersonalId = emergencyNumberPersonalId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public List<ContactNumberModel> getContactNumberList() {
		return contactNumberList;
	}

	public void setContactNumberList(List<ContactNumberModel> contactNumberList) {
		this.contactNumberList = contactNumberList;
	}

	public int addEmergencyNumbersPersonal(String userId) throws IOException {

		int status = 0;

		if (this.contactNumberList.size() > 0) {

			SqlSession session = ConnectionBuilderAction.getSqlSession();
			EmergencyNumbersPersonalDao emergencyNumbersPersonalDao = session.getMapper(EmergencyNumbersPersonalDao.class);

			long currentTime = DateUtils.nowAsGmtMillisec();

			List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalModelList = new ArrayList<EmergencyNumbersPersonalModel>();

			for (ContactNumberModel contactNumberModel : this.contactNumberList) {

				EmergencyNumbersPersonalModel emergencyNumbersPersonalModel = new EmergencyNumbersPersonalModel();

				emergencyNumbersPersonalModel.setEmergencyNumberPersonalId(UUIDGenerator.generateUUID());
				emergencyNumbersPersonalModel.setUserId(userId);
				emergencyNumbersPersonalModel.setName(contactNumberModel.getName());
				emergencyNumbersPersonalModel.setPhoneNo(contactNumberModel.getPhoneNo());
				emergencyNumbersPersonalModel.setRecordStatus("A");
				emergencyNumbersPersonalModel.setCreatedAt(currentTime);
				emergencyNumbersPersonalModel.setCreatedBy(userId);
				emergencyNumbersPersonalModel.setUpdatedAt(currentTime);
				emergencyNumbersPersonalModel.setUpdatedBy(userId);

				emergencyNumbersPersonalModelList.add(emergencyNumbersPersonalModel);
			}

			try {
				status = emergencyNumbersPersonalDao.insertEmergencyNumbersPersonalBatch(emergencyNumbersPersonalModelList);
				session.commit();
			} catch (Throwable t) {
				session.rollback();
				logger.error("Exception occured during addEmergencyNumbersPersonal : ", t);
				throw new PersistenceException(t);
			} finally {
				session.close();
			}

		}

		return status;

	}

	public static List<EmergencyNumbersPersonalModel> getEmergencyNumbersPersonalListById(String userId) {

		List<EmergencyNumbersPersonalModel> EmergencyNumbersPersonalModelList = new ArrayList<EmergencyNumbersPersonalModel>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersPersonalDao emergencyNumbersPersonalDao = session.getMapper(EmergencyNumbersPersonalDao.class);

		try {
			EmergencyNumbersPersonalModelList = emergencyNumbersPersonalDao.getEmergencyNumbersPersonalListById(userId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllEmergencyNumbers : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return EmergencyNumbersPersonalModelList;
	}

	public static int deleteEmergencyNumberPersonal(String emergencyNumberPersonalId, String userId) {

		int status = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersPersonalDao emergencyNumbersPersonalDao = session.getMapper(EmergencyNumbersPersonalDao.class);

		try {
			EmergencyNumbersPersonalModel emergencyNumbersPersonalModel = new EmergencyNumbersPersonalModel();

			emergencyNumbersPersonalModel.setEmergencyNumberPersonalId(emergencyNumberPersonalId);
			emergencyNumbersPersonalModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			emergencyNumbersPersonalModel.setUpdatedBy(userId);

			status = emergencyNumbersPersonalDao.deleteEmergencyNumberPersonal(emergencyNumbersPersonalModel);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteEmergencyNumberPersonal : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return status;
	}

	public static EmergencyNumbersPersonalModel getEmergencyNumbersPersonalDetailsById(String emergencyNumberPersonalId) {

		EmergencyNumbersPersonalModel emergencyNumberDetails = new EmergencyNumbersPersonalModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		EmergencyNumbersPersonalDao emergencyNumbersPersonalDao = session.getMapper(EmergencyNumbersPersonalDao.class);

		try {
			emergencyNumberDetails = emergencyNumbersPersonalDao.getEmergencyNumbersPersonalDetailsById(emergencyNumberPersonalId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during sendEmergencyMessageById : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return emergencyNumberDetails;
	}

}