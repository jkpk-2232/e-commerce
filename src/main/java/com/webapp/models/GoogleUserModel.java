package com.webapp.models;

public class GoogleUserModel {
	
	private Long id;

	private String email;

	private String googleUserId;

	private String googleDisplayName;

	private String googleprivateProfileUrl;

	private String googleprivateProfilePhotoUrl;

	private String googleAccessToken;

	private String googleRefreshToken;

	private Long googleExpiresIn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
	}

	public String getGoogleDisplayName() {
		return googleDisplayName;
	}

	public void setGoogleDisplayName(String googleDisplayName) {
		this.googleDisplayName = googleDisplayName;
	}

	public String getGoogleprivateProfileUrl() {
		return googleprivateProfileUrl;
	}

	public void setGoogleprivateProfileUrl(String googleprivateProfileUrl) {
		this.googleprivateProfileUrl = googleprivateProfileUrl;
	}

	public String getGoogleprivateProfilePhotoUrl() {
		return googleprivateProfilePhotoUrl;
	}

	public void setGoogleprivateProfilePhotoUrl(String googleprivateProfilePhotoUrl) {
		this.googleprivateProfilePhotoUrl = googleprivateProfilePhotoUrl;
	}

	public String getGoogleAccessToken() {
		return googleAccessToken;
	}

	public void setGoogleAccessToken(String googleAccessToken) {
		this.googleAccessToken = googleAccessToken;
	}

	public String getGoogleRefreshToken() {
		return googleRefreshToken;
	}

	public void setGoogleRefreshToken(String googleRefreshToken) {
		this.googleRefreshToken = googleRefreshToken;
	}

	public Long getGoogleExpiresIn() {
		return googleExpiresIn;
	}

	public void setGoogleExpiresIn(Long googleExpiresIn) {
		this.googleExpiresIn = googleExpiresIn;
	}

	@Override
	public String toString() {
		String result = "id:" + id + "\n";
		result += "email:" + email + "\n";
		result += "googleUserId:" + googleUserId + "\n";
		result += "googleDisplayName:" + googleDisplayName + "\n";
		result += "googleprivateProfileUrl:" + googleprivateProfileUrl + "\n";
		result += "googleprivateProfilePhotoUrl:" + googleprivateProfilePhotoUrl + "\n";
		result += "email:" + email + "\n";
		return result;

	}
	
}