package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.ServiceTypeDao;

public class ServiceTypeModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ServiceTypeModel.class);

	private String serviceTypeId;
	private String serviceTypeName;
	private String serviceTypeDescription;
	private boolean isActive;
	private boolean isDeleted;

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getServiceTypeDescription() {
		return serviceTypeDescription;
	}

	public void setServiceTypeDescription(String serviceTypeDescription) {
		this.serviceTypeDescription = serviceTypeDescription;
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

	public static List<ServiceTypeModel> getServiceTypeSearch(int startDatelong, int endDatelong, String searchKey, int start, int length) {

		List<ServiceTypeModel> serviceTypeList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceTypeDao serviceTypeDao = session.getMapper(ServiceTypeDao.class);

		try {
			serviceTypeList = serviceTypeDao.getServiceTypeSearch(startDatelong, endDatelong, searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCategorySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceTypeList;
	}
}