package com.webapp.daos;

import java.sql.SQLException;

import org.json.JSONObject;

import com.utils.JQTableUtils;
import com.webapp.models.ActivityLogModel;

public interface ActivityLogDao {
	
	public long addInsertLogAb(ActivityLogModel logModel) throws SQLException;

	public long addUpdateLogAb(ActivityLogModel logModel) throws SQLException;

	public long addDeleteLogAb(ActivityLogModel logModel) throws SQLException;
	
	public JSONObject getJsonActivityLogListForServerSide(JQTableUtils tableUtils, String getUserTimeZone) throws SQLException;
}
