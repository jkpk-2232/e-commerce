package com.webapp.daos;

import java.sql.SQLException;
import java.util.List;

import com.webapp.models.GeoCountryModel;

public interface GeoCountryDao {

	List<GeoCountryModel> getAllCountries() throws SQLException;
}
