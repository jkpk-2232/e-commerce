package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.VendorProductCategoryAssocDao;

public class VendorProductCategoryAssocModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(VendorProductCategoryAssocModel.class);

	private String vendorProductCategoryAssocId;
	private String vendorId;
	private String productCategoryId;

	public String getVendorProductCategoryAssocId() {
		return vendorProductCategoryAssocId;
	}

	public void setVendorProductCategoryAssocId(String vendorProductCategoryAssocId) {
		this.vendorProductCategoryAssocId = vendorProductCategoryAssocId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public static void addVenorProductCategoryAssoc(String userId, List<String> productCategoryIdList) {

		VendorProductCategoryAssocModel VPCAssocModel = new VendorProductCategoryAssocModel();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductCategoryAssocDao VPCAssocDao = session.getMapper(VendorProductCategoryAssocDao.class);

		try {

			VPCAssocDao.deleteVendorProductCategoryAssocByUser(userId);

			VPCAssocModel.setVendorId(userId);
			VPCAssocModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			VPCAssocModel.setUpdatedBy(userId);
			VPCAssocModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			VPCAssocModel.setCreatedBy(userId);

			for (String productCategoryId : productCategoryIdList) {
				System.out.println("*** product categoryId ****" + productCategoryId);
				VPCAssocModel.setVendorProductCategoryAssocId(UUIDGenerator.generateUUID());
				VPCAssocModel.setProductCategoryId(productCategoryId);
				VPCAssocDao.addVendorProductCategoryAssoc(VPCAssocModel);
			}

			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during addVenorProductCategoryAssoc : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

	}

	public static List<VendorProductCategoryAssocModel> getVendorProductCategoryAssocByVendorId(String vendorId) {
		
		List<VendorProductCategoryAssocModel> VPCAssocList = new ArrayList<>();
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductCategoryAssocDao VPCAssocDao = session.getMapper(VendorProductCategoryAssocDao.class);
		
		try {
			VPCAssocList = VPCAssocDao.getVendorProductCategoryAssocByVendorId(vendorId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorProductCategoryAssocByVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return VPCAssocList;
	}

	public static VendorProductCategoryAssocModel getVendorProductCategoryAssocByVendorIdAndProductCategoryId(String vendorId, String productCategoryId) {
		
		VendorProductCategoryAssocModel vendorProductCategoryAssocModel = null;
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		VendorProductCategoryAssocDao VPCAssocDao = session.getMapper(VendorProductCategoryAssocDao.class);
		
		try {
			vendorProductCategoryAssocModel = VPCAssocDao.getVendorProductCategoryAssocByVendorIdAndProductCategoryId(vendorId, productCategoryId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getVendorProductCategoryAssocByVendorIdAndProductCategoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return vendorProductCategoryAssocModel;
	}

}
