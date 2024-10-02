package com.webapp.daos;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.webapp.models.ReferralCodeLogsModel;

public interface ReferralCodeLogsDao {

	int addReferralCodeLogs(ReferralCodeLogsModel referralCodeLogsModel);

	ReferralCodeLogsModel getReferralCodeLogsByReceiverId(@Param("userId") String userId);

	int getTotalDriverReferralLogsCount(Map<String, Object> inputMap);

	List<ReferralCodeLogsModel> getReferralLogsListForSearch(Map<String, Object> inputMap);
}
