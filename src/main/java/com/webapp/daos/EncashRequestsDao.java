package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.EncashRequestsModel;

public interface EncashRequestsDao {

	int addEncashRequest(EncashRequestsModel encashRequestsModel);

	int updateEncashRequestStatus(EncashRequestsModel encashRequestsModel);

	EncashRequestsModel getEncashRequestDetailsById(@Param("encashRequestId") String encashRequestId);

	List<EncashRequestsModel> getEncashRequestForSearchByStatus(Map<String, Object> inputMap);

	int getTotalCountOfEncashRequestForSearchByStatus(Map<String, Object> inputMap);

	int getFilteredCountOfEncashRequestForSearchByStatus(Map<String, Object> inputMap);

	List<EncashRequestsModel> getEncashRequestsListForExport(Map<String, Object> inputMap);

}