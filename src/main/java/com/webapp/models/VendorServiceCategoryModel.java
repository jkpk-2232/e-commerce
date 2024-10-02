package com.webapp.models;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorServiceCategoryDao;

public class VendorServiceCategoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorServiceCategoryModel.class);

	private String vendorServiceCategoryId;
	private String vendorId;
	private String serviceId;
	private String categoryId;

	private String serviceTypeId;
	private String serviceTypeName;
	private String serviceName;
	private String categoryName;

	public String insertVendorServiceCategory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorServiceCategoryDao vendorServiceCategoryDao = session.getMapper(VendorServiceCategoryDao.class);

		this.vendorServiceCategoryId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			vendorServiceCategoryDao.insertVendorServiceCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertVendorServiceCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.vendorServiceCategoryId;
	}

	public static VendorServiceCategoryModel getVendorServiceCategoryByVendorId(String vendorId) {

		VendorServiceCategoryModel vendorServiceCategoryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorServiceCategoryDao vendorServiceCategoryDao = session.getMapper(VendorServiceCategoryDao.class);

		try {
			vendorServiceCategoryModel = vendorServiceCategoryDao.getVendorServiceCategoryByVendorId(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorServiceCategoryByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return vendorServiceCategoryModel;
	}

	public void deleteVendorServiceCategoryByVendorId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorServiceCategoryDao vendorServiceCategoryDao = session.getMapper(VendorServiceCategoryDao.class);

		try {
			vendorServiceCategoryDao.deleteVendorServiceCategoryByVendorId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteVendorServiceCategoryByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public String getVendorServiceCategoryId() {
		return vendorServiceCategoryId;
	}

	public void setVendorServiceCategoryId(String vendorServiceCategoryId) {
		this.vendorServiceCategoryId = vendorServiceCategoryId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}