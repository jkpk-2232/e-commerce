package com.utils.view;

public class ButtonLink extends ButtonView {

	public ButtonLink(String id, int tabIndex, String btnName, boolean isButton) {
		super(id, btnName, tabIndex, isButton);
		this.isButton = isButton;
	}

	public ButtonLink(String id, int tabIndex, String btnName, boolean isButton, String title) {
		super(id, btnName, tabIndex, isButton);
		this.isButton = isButton;
		this.title = title;
	}

	private boolean isButton;

	private String title;

	public boolean isButton() {
		return isButton;
	}

	public void setButton(boolean isButton) {
		this.isButton = isButton;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}