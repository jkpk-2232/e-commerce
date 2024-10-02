package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.BrandDao;
import com.webapp.daos.BusinessInterestedUsersDao;

public class BusinessInterestedUsersModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(BusinessInterestedUsersModel.class);

	private String businessInterestedUserId;
	private String name;
	private String email;
	private String phoneNoCode;
	private String phoneNo;
	private String city;
	private String roleId;
	private String vechicleType;
	private String StoreName;
	private int noOfStores;
	private String businessCategory;
	private String brandName;
	private int noOfOutlets;
	private String description;
	private String remarks;
	
	private String businessType;

	public String getBusinessInterestedUserId() {
		return businessInterestedUserId;
	}

	public void setBusinessInterestedUserId(String businessInterestedUserId) {
		this.businessInterestedUserId = businessInterestedUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNoCode() {
		return phoneNoCode;
	}

	public void setPhoneNoCode(String phoneNoCode) {
		this.phoneNoCode = phoneNoCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getVechicleType() {
		return vechicleType;
	}

	public void setVechicleType(String vechicleType) {
		this.vechicleType = vechicleType;
	}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	public int getNoOfStores() {
		return noOfStores;
	}

	public void setNoOfStores(int noOfStores) {
		this.noOfStores = noOfStores;
	}

	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getNoOfOutlets() {
		return noOfOutlets;
	}

	public void setNoOfOutlets(int noOfOutlets) {
		this.noOfOutlets = noOfOutlets;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void insertBusinessInterestedUser(BusinessInterestedUsersModel businessInterestedUsersModel) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BusinessInterestedUsersDao businessInterestedUserDao = session.getMapper(BusinessInterestedUsersDao.class);

		this.businessInterestedUserId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		//this.createdBy = loggedInUserId;
		//this.updatedBy = loggedInUserId;

		try {
			businessInterestedUserDao.insertBusinessInterestedUser(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertBusinessInterestedUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
	}

	public static int getBusinessInterestedUsersCount(long startDatelong, long endDatelong) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BusinessInterestedUsersDao businessInterestedUserDao = session.getMapper(BusinessInterestedUsersDao.class);

		try {
			count = businessInterestedUserDao.getBusinessInterestedUsersCount(startDatelong, endDatelong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBusinessInterestedUsersCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<BusinessInterestedUsersModel> getBusinessInterestedUsersSearch(long startDatelong, long endDatelong, String searchKey, int start, int length) {
		
		List<BusinessInterestedUsersModel> businessUsersList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BusinessInterestedUsersDao businessInterestedUserDao = session.getMapper(BusinessInterestedUsersDao.class);

		try {
			businessUsersList = businessInterestedUserDao.getBusinessInterestedUsersSearch(startDatelong, endDatelong, searchKey, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBusinessInterestedUsersSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return businessUsersList;
	}

	public static int getBusinessUserSearchCount(long startDatelong, long endDatelong, String searchKey) {
		
		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		BusinessInterestedUsersDao businessInterestedUserDao = session.getMapper(BusinessInterestedUsersDao.class);

		try {

			count = businessInterestedUserDao.getBusinessUserSearchCount(startDatelong, endDatelong, searchKey);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getBusinessUserSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
