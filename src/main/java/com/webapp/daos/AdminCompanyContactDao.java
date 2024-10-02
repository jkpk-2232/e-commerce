package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.AdminCompanyContactModel;

public interface AdminCompanyContactDao {

	int addAdminCompanyContact(AdminCompanyContactModel adminCompanyContactModel);

	AdminCompanyContactModel getAdminCompanyContactByVendorId(@Param("vendorId") String vendorId);

	int updateAdminCompanyContact(AdminCompanyContactModel adminCompanyContactModel);
}
