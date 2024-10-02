package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.VendorStoreSubVendorDao;

public class VendorStoreSubVendorModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorStoreSubVendorModel.class);

	private String vendorStoreSubVendorId;
	private String vendorStoreId;
	private String vendorId;
	private String subVendorId;

	public static void batchInsertVendorStoreSubVendorEntryOs(List<VendorStoreSubVendorModel> vendorStoreSubVendorList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreSubVendorDao vendorStoreSubVendorDao = session.getMapper(VendorStoreSubVendorDao.class);

		try {
			vendorStoreSubVendorDao.batchInsertVendorStoreSubVendorEntry(vendorStoreSubVendorList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during batchInsertVendorStoreSubVendorEntry : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<VendorStoreSubVendorModel> getVendorStoresAddedToTheSubVendor(String subVendorId, String fetchAllStores) {

		List<VendorStoreSubVendorModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreSubVendorDao vendorStoreSubVendorDao = session.getMapper(VendorStoreSubVendorDao.class);

		try {
			list = vendorStoreSubVendorDao.getVendorStoresAddedToTheSubVendor(subVendorId, fetchAllStores);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorStoresAddedToTheSubVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public void deleteMapSubVendorsToVendorStore() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreSubVendorDao vendorStoreSubVendorDao = session.getMapper(VendorStoreSubVendorDao.class);

		try {
			vendorStoreSubVendorDao.deleteMapSubVendorsToVendorStore(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteMapSubVendorsToVendorStore : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<VendorStoreSubVendorModel> getSubVendorsAllocatedToTheStore(String vendorStoreId) {

		List<VendorStoreSubVendorModel> list = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorStoreSubVendorDao vendorStoreSubVendorDao = session.getMapper(VendorStoreSubVendorDao.class);

		try {
			list = vendorStoreSubVendorDao.getSubVendorsAllocatedToTheStore(vendorStoreId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getSubVendorsAllocatedToTheStore : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return list;
	}

	public String getVendorStoreSubVendorId() {
		return vendorStoreSubVendorId;
	}

	public void setVendorStoreSubVendorId(String vendorStoreSubVendorId) {
		this.vendorStoreSubVendorId = vendorStoreSubVendorId;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSubVendorId() {
		return subVendorId;
	}

	public void setSubVendorId(String subVendorId) {
		this.subVendorId = subVendorId;
	}
}