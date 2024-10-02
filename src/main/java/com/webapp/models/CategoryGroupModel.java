package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryGroupModel extends AbstractModel {

	private String _id;
	private String groupName;
	private boolean groupStatus;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(boolean groupStatus) {
		this.groupStatus = groupStatus;
	}

}
