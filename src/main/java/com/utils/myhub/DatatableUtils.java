package com.utils.myhub;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.webapp.ProjectConstants;

public class DatatableUtils {

	private String start;
	private String length;
	private String order;
	private String column;
	private String globalSearchString;
	private String startDate;
	private String endDate;

	private int startInt;
	private int lengthInt;

	private long startDatelong;
	private long endDatelong;

	private HttpServletRequest request;
	

	public DatatableUtils(HttpServletRequest request) {

		try {

			this.request = request;

			this.start = getTrimmedParameter(request, "iDisplayStart");
			this.length = getTrimmedParameter(request, "iDisplayLength");
			this.order = getTrimmedParameter(request, "sSortDir_0");
			this.column = getTrimmedParameter(request, "iSortCol_0");
			this.globalSearchString = getTrimmedParameter(request, "sSearch");
			this.startDate = getTrimmedParameter(request, "startDate");
			this.endDate = getTrimmedParameter(request, "endDate");

			this.startInt = Integer.parseInt(this.start);
			this.lengthInt = Integer.parseInt(this.length);

			if (StringUtils.validString(this.startDate)) {
				this.startDatelong = DateUtils.getStartOfDayDatatableUpdated(this.startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
			} else {
				this.startDatelong = 0;
			}

			if (StringUtils.validString(this.endDate)) {
				this.endDatelong = DateUtils.getEndOfDayDatatableUpdated(this.endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
			} else {
				this.endDatelong = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String setAndGetSortColumnAndDir(DatatableUtils dtu, Map<String, String> sortingMappings) {

		String columnValue;
		String orderValue = dtu.getOrder();

		if (sortingMappings.containsKey(dtu.getColumn())) {
			columnValue = sortingMappings.get(dtu.getColumn());
		} else {
			columnValue = sortingMappings.get(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
			orderValue = "DESC";
		}

		return columnValue.concat(" ").concat(orderValue);
	}

	private String getTrimmedParameter(HttpServletRequest request, String parameter) {
		String value = request.getParameter(parameter);
		return value != null ? value.trim() : "";
	}

	public String getRequestParameter(String parameter) {

		try {
			String value = this.getRequest().getParameter(parameter);
			return value != null ? value.trim() : "";
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
	

	public static void processData(JSONObject jsonObject, JSONArray outerJsonArray, int total, int filterCount) {

		try {
			jsonObject.put("iTotalRecords", total);
			jsonObject.put("iTotalDisplayRecords", filterCount);
			jsonObject.put("aaData", outerJsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	


	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getGlobalSearchString() {
		return globalSearchString;
	}

	public String getGlobalSearchStringWithPercentage() {
		return MyHubUtils.getSearchStringFormat(globalSearchString);
	}

	public void setGlobalSearchString(String globalSearchString) {
		this.globalSearchString = globalSearchString;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getStartInt() {
		return startInt;
	}

	public void setStartInt(int startInt) {
		this.startInt = startInt;
	}

	public int getLengthInt() {
		return lengthInt;
	}

	public void setLengthInt(int lengthInt) {
		this.lengthInt = lengthInt;
	}

	public long getStartDatelong() {
		return startDatelong;
	}

	public void setStartDatelong(long startDatelong) {
		this.startDatelong = startDatelong;
	}

	public long getEndDatelong() {
		return endDatelong;
	}

	public void setEndDatelong(long endDatelong) {
		this.endDatelong = endDatelong;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	    
	}

