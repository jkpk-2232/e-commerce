package com.webapp.daos;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ExportAccessModel;

public interface ExportAccessDao {

	int addExportAccessDetails(ExportAccessModel exportAccessModel);

	int updateExportAccessDetails(ExportAccessModel exportAccessModel);

	ExportAccessModel getExportAccessDetailsByUserId(@Param("userId") String userId);

}