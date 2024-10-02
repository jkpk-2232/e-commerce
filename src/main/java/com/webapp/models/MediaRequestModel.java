package com.webapp.models;

public class MediaRequestModel {

	private String media_title;
	private String media_type;
	private String url;
	private String format;
	private String duration;
	private String resolution_id;

	public String getMedia_title() {
		return media_title;
	}

	public void setMedia_title(String media_title) {
		this.media_title = media_title;
	}

	public String getMedia_type() {
		return media_type;
	}

	public void setMedia_type(String media_type) {
		this.media_type = media_type;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getResolution_id() {
		return resolution_id;
	}

	public void setResolution_id(String resolution_id) {
		this.resolution_id = resolution_id;
	}

}
