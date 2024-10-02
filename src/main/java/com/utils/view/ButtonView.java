package com.utils.view;

public class ButtonView {

	private String id;
	
	private int tabIndex;
	
	private String btnName;
	
	private boolean isAnchoreBtn;

	public ButtonView(String id, String btnName, int tabIndex, boolean isAnchoreBtn) {
		this.id = id;
		this.tabIndex = tabIndex;
		this.btnName = btnName;
		this.isAnchoreBtn = isAnchoreBtn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getBtnName() {
		return btnName;
	}

	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}

	public boolean isAnchoreBtn() {
		return isAnchoreBtn;
	}

	public void setAnchoreBtn(boolean isAnchoreBtn) {
		this.isAnchoreBtn = isAnchoreBtn;
	}

}