package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.CategoryDao;

public class CategoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(CategoryModel.class);

	private String categoryId;
	private String serviceId;
	private String categoryName;
	private String categoryDescription;
	private boolean isActive;
	private boolean isDeleted;

	private String serviceName;

	public String insertCategory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		this.categoryId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		this.isActive = true;
		this.isDeleted = false;

		try {
			categoryDao.insertCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.categoryId;
	}

	public void updateCategory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			categoryDao.updateCategory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCategory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getCategoryCount(long startDatelong, long endDatelong, String serviceId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		try {
			count = categoryDao.getCategoryCount(startDatelong, endDatelong, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCategoryCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<CategoryModel> getCategorySearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String serviceId, String displayType) {

		List<CategoryModel> categoryList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		try {
			categoryList = categoryDao.getCategorySearch(startDatelong, endDatelong, searchKey, start, length, serviceId, displayType);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCategorySearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return categoryList;
	}

	public static int getCategorySearchCount(long startDatelong, long endDatelong, String searchKey, String serviceId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		try {
			count = categoryDao.getCategorySearchCount(startDatelong, endDatelong, searchKey, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCategorySearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateCategoryStatus(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			categoryDao.updateCategoryStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCategoryStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static boolean isCategoryNameExists(String categoryName, String serviceId) {

		boolean isDuplicate = false;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		try {
			isDuplicate = categoryDao.isCategoryNameExists(categoryName, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during isCategoryNameExists : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return isDuplicate;
	}

	public static CategoryModel getCategoryDetailsByCategoryId(String categoryId) {

		CategoryModel categoryModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		CategoryDao categoryDao = session.getMapper(CategoryDao.class);

		try {
			categoryModel = categoryDao.getCategoryDetailsByCategoryId(categoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCategoryDetailsByCategoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return categoryModel;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}