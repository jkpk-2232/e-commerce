package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaModel extends AbstractModel{

	private String _id;
	private String mediaTitle;
	private String mediaType;
	private String url;
	private String format;
	private ResolutionModel resolutionId;
	private String resolutionGroup;
	private String duration;
	private boolean isApproved;
	private String ad_id;
	/* private UserDetailsModel userId; */
	private String createdDate;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getMediaTitle() {
		return mediaTitle;
	}

	public void setMediaTitle(String mediaTitle) {
		this.mediaTitle = mediaTitle;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public ResolutionModel getResolutionId() {
		return resolutionId;
	}

	public void setResolutionId(ResolutionModel resolutionId) {
		this.resolutionId = resolutionId;
	}

	public String getResolutionGroup() {
		return resolutionGroup;
	}

	public void setResolutionGroup(String resolutionGroup) {
		this.resolutionGroup = resolutionGroup;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getAd_id() {
		return ad_id;
	}

	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}

	/*
	 * public UserDetailsModel getUserId() { return userId; }
	 * 
	 * public void setUserId(UserDetailsModel userId) { this.userId = userId; }
	 */

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

}
