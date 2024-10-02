package com.webapp.actions.secure.booking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.ComboUtils;
import com.jeeutils.DateUtils;
import com.webapp.actions.BusinessAction;
import com.webapp.models.TourModel;

@Path("/heatmap")
public class HeatMapAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadHeatMap(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res) 
			throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		String timeFilterOptions = getTimeFilterOptions("1");

		data.put("timeFilterOptions", timeFilterOptions);

		return loadView("/secure/booking/heatmap.jsp");
	}

	@Path("/coordinates-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadCoordinatesList(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@QueryParam("timeFilter") String timeFilter
			) throws ServletException, IOException {
	//@formatter:on

		if (timeFilter == null || "".equals(timeFilter)) {

			timeFilter = "1";
		}

		long startTimeInMillis = getStartTimeByTimeFilter(timeFilter);

		Map<String, Object> outputMap = new HashMap<String, Object>();

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("startTimeInMillis", startTimeInMillis);

		List<TourModel> toursList = TourModel.getTourListForHeatMap(inputMap);

		outputMap.put("coordinatesList", toursList);

		return sendDataResponse(outputMap);

	}

	private long getStartTimeByTimeFilter(String timeFilter) {

		long currentTimeInMillis = DateUtils.nowAsGmtMillisec();

		long timeInMillis = 0;

		long startTime = 0;

		if ("2".equals(timeFilter)) {

			// Last 4 hour
			timeInMillis = ((60 * 1000) * 60) * 4;
			startTime = currentTimeInMillis - timeInMillis;

		} else if ("3".equals(timeFilter)) {

			// Current day
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			startTime = calendar.getTimeInMillis();

		} else if ("4".equals(timeFilter)) {

			// Last 7 day
			timeInMillis = (((60 * 1000) * 60) * 24) * 7;
			startTime = currentTimeInMillis - timeInMillis;

		} else if ("5".equals(timeFilter)) {

			// Last 15 day
			timeInMillis = (((60 * 1000) * 60) * 24) * 15;
			startTime = currentTimeInMillis - timeInMillis;

		} else if ("6".equals(timeFilter)) {

			// Last 30 day
			timeInMillis = (((60 * 1000) * 60) * 24) * 30;
			startTime = currentTimeInMillis - timeInMillis;

		} else {

			// Last hour
			timeInMillis = (60 * 1000) * 60;
			startTime = currentTimeInMillis - timeInMillis;
		}

		return startTime;
	}

	private String getTimeFilterOptions(String selectedValue) {

		List<String> timeFilterName = new ArrayList<String>();
		List<String> timeFilterValues = new ArrayList<String>();

		timeFilterValues.add("1");
		timeFilterName.add("Last Hour");

		timeFilterValues.add("2");
		timeFilterName.add("Last 4 Hour");

		timeFilterValues.add("3");
		timeFilterName.add("Current Day");

		timeFilterValues.add("4");
		timeFilterName.add("Last 7 Days");

		timeFilterValues.add("5");
		timeFilterName.add("Last 15 Days");

		timeFilterValues.add("6");
		timeFilterName.add("Last 30 Days");

		String timeFilteOptions = ComboUtils.getOptionArrayForListBox(timeFilterName, timeFilterValues, selectedValue + "");

		return timeFilteOptions;
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/booking/heatmap.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}