package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import com.webapp.models.DriverCommissionTypeModel;

public interface DriverCommissionTypeDao {

	List<DriverCommissionTypeModel> getAllDriverCommissionType() throws SQLException;
}
