package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.daos.DriverVendorsDao;

public class DriverVendorsModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(DriverInfoModel.class);

	private String driverVendorId;
	private String driverId;
	private String vendorId;
	private String roleId;
	private String vendorName;
	private String vendorRoleId;

	public String getDriverVendorId() {
		return driverVendorId;
	}

	public void setDriverVendorId(String driverVendorId) {
		this.driverVendorId = driverVendorId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorRoleId() {
		return vendorRoleId;
	}

	public void setVendorRoleId(String vendorRoleId) {
		this.vendorRoleId = vendorRoleId;
	}

	public static DriverVendorsModel updateDriverVendor(String driverId, String vendorId, String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverVendorsDao driverVendorsDao = session.getMapper(DriverVendorsDao.class);

		DriverVendorsModel driverVendorModel = new DriverVendorsModel();

		try {

			driverVendorModel.setDriverVendorId(UUIDGenerator.generateUUID());
			driverVendorModel.setDriverId(driverId);
			driverVendorModel.setVendorId(vendorId);
			driverVendorModel.setRoleId(UserRoles.DRIVER_ROLE_ID);
			driverVendorModel.setCreatedBy(loggedInUserId);
			driverVendorModel.setUpdatedBy(loggedInUserId);
			driverVendorModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			driverVendorModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			driverVendorsDao.deleteDriverVendorMapping(driverId);
			driverVendorsDao.addDriverVendorMapping(driverVendorModel);

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUserDetailsByRole : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverVendorModel;
	}

	public static DriverVendorsModel getDriverVendorDetailsByDriverId(String driverId) {

		DriverVendorsModel driverVendorsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverVendorsDao driverVendorsDao = session.getMapper(DriverVendorsDao.class);

		try {
			driverVendorsModel = driverVendorsDao.getDriverVendorDetailsByDriverId(driverId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDriverVendorDetailsByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return driverVendorsModel;
	}

	public static void batchInsertDefaultUserVendorMapping(List<DriverVendorsModel> list) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		DriverVendorsDao driverVendorsDao = session.getMapper(DriverVendorsDao.class);

		try {
			driverVendorsDao.batchInsertDefaultUserVendorMapping(list);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during batchInsertDefaultUserVendorMapping : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}
}