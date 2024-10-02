package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreCategorieModel {

	private String _id;
	private String categoryName;
	private CategoryGroupModel categoryGroup;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public CategoryGroupModel getCategoryGroup() {
		return categoryGroup;
	}

	public void setCategoryGroup(CategoryGroupModel categoryGroup) {
		this.categoryGroup = categoryGroup;
	}

}
