package com.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JQTableUtils {

	private static Logger logger = Logger.getLogger(JQTableUtils.class);

	private int iDisplayStart;
	private int sortColumn;//iSortCol_0
	private String sortingDirection; //sSortDir_0
	private String searchParams;
	private String deleteParams;
	private int iDisplayLength;
	private long selectRecordId;

	public JQTableUtils(HttpServletRequest request) {

		try {
			this.sortingDirection = getTrimmedParameter(request, "sSortDir_0");
			this.searchParams = getTrimmedParameter(request, "searchParams");
			this.deleteParams = getTrimmedParameter(request, "deleteParams");
			this.iDisplayStart = Integer.parseInt(getTrimmedParameter(request, "iDisplayStart"));
			this.iDisplayLength = Integer.parseInt(getTrimmedParameter(request, "iDisplayLength"));
			this.sortColumn = Integer.parseInt(getTrimmedParameter(request, "iSortCol_0"));
			if (getTrimmedParameter(request, "selectRecordId").length() > 0) {
				this.selectRecordId = Long.parseLong(getTrimmedParameter(request, "selectRecordId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public long getSelectRecordId() {
		return selectRecordId;
	}

	public void setSelectRecordId(long selectRecordId) {
		this.selectRecordId = selectRecordId;
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(int sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortingDirection() {
		return sortingDirection;
	}

	public void setSortingDirection(String sortingDirection) {
		this.sortingDirection = sortingDirection;
	}

	public String getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(String searchParams) {
		this.searchParams = searchParams;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public String getDeleteParams() {
		return deleteParams;
	}

	public void setDeleteParams(String deleteParams) {
		this.deleteParams = deleteParams;
	}

	private String getTrimmedParameter(HttpServletRequest request, String parameter) {

		String value = request.getParameter(parameter);

		if (value == null) {
			return "";
		} else {
			return value.trim();
		}
	}

	public static JSONObject getOutputJsonObject(JSONArray array, long total) {

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("status", "OK");
			jsonObject.put("iTotalRecords", total);
			jsonObject.put("iTotalDisplayRecords", total);
			jsonObject.put("aaData", array);

		} catch (JSONException e) {
			logger.error(" error in Json Object crearion" + e);
			e.printStackTrace();
		}

		return jsonObject;
	}

}