package com.utils.view;

public class ActionButton {

	private String btnId;
	private String btnImageName;
	private String title;
	private String actionUrl;
	private String cssClass;
	private boolean hasInnerImg;

	public ActionButton(String btnId, String btnImageName, String title, String actionUrl) {
		this.btnId = btnId;
		this.btnImageName = btnImageName;
		this.title = title;
		this.actionUrl = actionUrl;
	}

	public ActionButton(String btnId, String btnImageName, String title, String actionUrl, String cssClass, boolean hasInnerImg) {
		this.btnId = btnId;
		this.btnImageName = btnImageName;
		this.title = title;
		this.actionUrl = actionUrl;
		this.cssClass = cssClass;
		this.hasInnerImg = hasInnerImg;
	}

	public String getBtnId() {
		return btnId;
	}

	public void setBtnId(String btnId) {
		this.btnId = btnId;
	}

	public String getBtnImageName() {
		return btnImageName;
	}

	public void setBtnImageName(String btnImageName) {
		this.btnImageName = btnImageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public boolean hasInnerImg() {
		return hasInnerImg;
	}

	public void setInnerImg(boolean hasInnerImg) {
		this.hasInnerImg = hasInnerImg;
	}

}