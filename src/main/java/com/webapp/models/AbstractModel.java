package com.webapp.models;

import com.utils.UUIDGenerator;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.daos.ActivityLogDao;

public class AbstractModel {

	public static final String ERROR_MESSAGE_TYPE = "ERROR";

	public static final String SUCCESS_MESSAGE_TYPE = "SUCCESS";

	public static final String ACTIVE_RECORD_STATUS = "A";

	public static final String DELETED_RECORD_STATUS = "D";

	public static final String UPDATED_RECORD_STATUS = "U";

	public static final int ACTIVE_STATUS = 1;

	public static final int DEACTIVE_STATUS = 0;

	public static final boolean IS_ACTIVE_TRUE = true;

	public static final boolean IS_NONACTIVE = false;

	protected long createdAt;

	protected String createdBy;

	protected long updatedAt;

	protected String updatedBy;

	protected String recordStatus;

	public static ActivityLogDao logDao = null;

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public ActivityLogModel getActivityLogModel(String log, String description) {
		ActivityLogModel logModel = new ActivityLogModel();
		logModel.setActivityLogId(UUIDGenerator.generateUUID());
		logModel.setLog(log);
		logModel.setDescription(description);
		logModel.setCreatedAt(updatedAt);
		logModel.setCreatedBy(updatedBy);
		return logModel;
	}

	public static String getFullImageUrl(String url) {

		String imageUrl = "";

		String baseUrl = WebappPropertyUtils.getWebAppProperty("imagePath");

		if (url != null && !"".equals(url)) {
			if (url.contains("bucketName")) {
				imageUrl = baseUrl + url;
			} else {
				imageUrl = url;
			}
		} else {
			// do nothing
		}

		return imageUrl;

	}
}
