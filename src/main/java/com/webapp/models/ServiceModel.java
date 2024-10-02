package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.ServiceDao;

public class ServiceModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(ServiceModel.class);

	private String serviceId;
	private String serviceName;
	private String serviceDescription;
	private boolean isDefault;
	private boolean isActive;
	private boolean isDeleted;
	private int servicePriority;
	private String serviceImage;

	private String serviceTypeId;
	private String serviceTypeName;

	public String insertServices(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		this.serviceId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			serviceDao.insertServices(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertServices : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.serviceId;
	}

	public void updateServices(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			serviceDao.updateServices(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateServices : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getServiceCount(long startDatelong, long endDatelong, String serviceTypeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			count = serviceDao.getServiceCount(startDatelong, endDatelong, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getServiceCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<ServiceModel> getServiceSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String displayType, String orderColumn, String serviceTypeId) {

		List<ServiceModel> serviceList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			serviceList = serviceDao.getServiceSearch(startDatelong, endDatelong, searchKey, start, length, displayType, orderColumn, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getServiceSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceList;
	}

	public static int getServiceSearchCount(long startDatelong, long endDatelong, String searchKey, String serviceTypeId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			count = serviceDao.getServiceSearchCount(startDatelong, endDatelong, searchKey, serviceTypeId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getServiceSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateServiceStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			serviceDao.updateServiceStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateServiceStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static boolean isServiceNameExists(String serviceName, String serviceId) {

		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			isDuplicate = serviceDao.isServiceNameExists(serviceName, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isServiceNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}

	public static ServiceModel getServiceDetailsByServiceId(String serviceId) {

		ServiceModel serviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			serviceModel = serviceDao.getServiceDetailsByServiceId(serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getServiceDetailsByServiceId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceModel;
	}

	public static ServiceModel getDefaultServiceModel() {

		ServiceModel serviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			serviceModel = serviceDao.getDefaultServiceModel();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDefaultServiceModel : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceModel;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
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

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

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

	public static List<ServiceModel> getAllActiveServices() {

		List<ServiceModel> serviceModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		ServiceDao serviceDao = session.getMapper(ServiceDao.class);

		try {
			serviceModel = serviceDao.getAllActiveServices();
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllActiveServices : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return serviceModel;
	}

	public int getServicePriority() {
		return servicePriority;
	}

	public void setServicePriority(int servicePriority) {
		this.servicePriority = servicePriority;
	}

	public String getServiceImage() {
		return serviceImage;
	}

	public void setServiceImage(String serviceImage) {
		this.serviceImage = serviceImage;
	}
}