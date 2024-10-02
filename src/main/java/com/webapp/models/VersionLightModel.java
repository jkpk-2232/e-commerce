package com.webapp.models;

public class VersionLightModel {

	public VersionLightModel(VersionModel versionModel) {
		this.version = versionModel.getVersion();
		this.appLink = versionModel.getAppLink();
		this.releaseNote = versionModel.getReleaseNote();
	}

	private String resultCode;

	private String message;

	private String version;

	private String appLink;

	private String releaseNote;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAppLink() {
		return appLink;
	}

	public void setAppLink(String appLink) {
		this.appLink = appLink;
	}

	public String getReleaseNote() {
		return releaseNote;
	}

	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

}