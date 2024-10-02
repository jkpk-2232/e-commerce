package com.webapp.actions.secure.history;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.models.OrderModel;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.viewutils.NewUiViewUtils;

@Path("/manage-history-new")
public class ManageHistoryNewAction extends BusinessAction {

	public final static String MANAGE_HISTORY_URL = "/manage-history.do";

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);
		if (userProfileModel == null) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		if (!UrlAccessesModel.hasUserUrlAccess(ManageHistoryNewAction.MANAGE_HISTORY_URL, loggedInUserId)) {
			return loadView("/error.jsp");
		}

		String serviceTypeIdOptions = DropDownUtils.getServiceTypeOption(false, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		data.put("serviceTypeIdOptions", serviceTypeIdOptions);

//		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(userProfileModel);
//
//		String vendorIdOptions = DropDownUtils.getVendorListByServiceTypeIdList(true, assignedRegionList, "-1", "-1");
//		data.put("vendorIdOptions", vendorIdOptions);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(true, null, "-1");
		data.put("serviceIdOptions", serviceIdOptions);

		data.put(ProjectConstants.STATUS_TYPE, ProjectConstants.OrderDeliveryConstants.ORDERS_NEW_TAB);

		String orderStatusFilterOptions = DropDownUtils.getOrderStatusList(true, ProjectConstants.OrderDeliveryConstants.ORDERS_NEW_TAB, "-1");
		data.put("orderStatusFilterOptions", orderStatusFilterOptions);

		return loadView("/secure/history/manage-history-new.jsp");
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response postSurgeSettings(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res
		) {
	//@formatter:on

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);

		String timeZone = TimeZoneUtils.getTimeZone();

		String sStart = req.getParameter("iDisplayStart");
		String length = req.getParameter("iDisplayLength");
		String globalSearchString = req.getParameter("sSearch");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		String vendorId = req.getParameter("vendorId");
		String serviceId = req.getParameter("serviceId");
		String categoryId = req.getParameter("categoryId");
		String type = req.getParameter(ProjectConstants.STATUS_TYPE);
		String orderStatusFilter = req.getParameter("orderStatusFilter");
		String vendorOrderManagement = req.getParameter("vendorOrderManagement");

		int lengthh = Integer.parseInt(length);
		int start = Integer.parseInt(sStart);

		long startDatelong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATE_FORMAT_FOR_VIEW, timeZone);
		long endDatelong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATE_FORMAT_FOR_VIEW, timeZone);

		globalSearchString = globalSearchString.trim();

		if (userProfileModel.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {

			vendorId = loggedInUserId;

			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);

			serviceId = vscm != null ? vscm.getServiceId() : null;
			categoryId = vscm != null ? vscm.getCategoryId() : null;

		} else {

			if (vendorId.equalsIgnoreCase("0")) {
				vendorId = null;
			}

			if (serviceId.equalsIgnoreCase("-1")) {
				serviceId = null;
			}

			categoryId = null;
		}

		if (orderStatusFilter.equalsIgnoreCase("-1")) {
			orderStatusFilter = null;
		}

		if (vendorOrderManagement.equalsIgnoreCase("-1")) {
			vendorOrderManagement = null;
		}

		int orderShortIdSearch = MyHubUtils.searchNumericFormat(globalSearchString);

		List<String> orderStatus = OrderUtils.getOrderStatusListAsPerOrderType(type);

		int total = OrderModel.getOrdersCount(startDatelong, endDatelong, vendorId, serviceId, categoryId, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, null);
		List<OrderModel> orderList = OrderModel.getOrdersSearch(startDatelong, endDatelong, "%" + globalSearchString + "%", start, lengthh, null, vendorId, serviceId, categoryId, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, null);

		JSONObject jsonObject = new JSONObject();
		JSONArray outerJsonArray = new JSONArray();
		JSONArray innerJsonArray = null;

		int count = start;

		for (OrderModel orderModel : orderList) {

			count++;

			innerJsonArray = new JSONArray();
			innerJsonArray.put(orderModel.getOrderId());
			innerJsonArray.put(count);
			innerJsonArray.put(orderModel.getOrderShortId());
			innerJsonArray.put(DateUtils.dbTimeStampToSesionDate(orderModel.getOrderCreationTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
			innerJsonArray.put(orderModel.getCustomerName());
			innerJsonArray.put(orderModel.getOrderDeliveryAddress());
			innerJsonArray.put(orderModel.getVendorName());
			innerJsonArray.put(df.format(orderModel.getOrderTotal()));
			innerJsonArray.put(df.format(orderModel.getOrderDeliveryCharges()));
			innerJsonArray.put(df.format(orderModel.getOrderCharges()));
			innerJsonArray.put(orderModel.getOrderNumberOfItems());
			innerJsonArray.put(OrderUtils.getOrderStatusDisplayLabels(orderModel.getOrderDeliveryStatus()));
			innerJsonArray.put(NewUiViewUtils.outputFormButtonLink(req.getContextPath() + "/view-order.do?orderId=" + orderModel.getOrderId() + "&type=" + type, "view"));
			outerJsonArray.put(innerJsonArray);
		}

		int filterCount = 0;

		if (globalSearchString != null && !globalSearchString.equals("")) {
			filterCount = OrderModel.getOrdersSearchCount(startDatelong, endDatelong, "%" + globalSearchString + "%", vendorId, serviceId, categoryId, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, null);
		} else {
			filterCount = total;
		}

		try {
			jsonObject.put("iTotalRecords", total);
			jsonObject.put("iTotalDisplayRecords", filterCount);
			jsonObject.put("aaData", outerJsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return sendDataResponse(jsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/history/manage-history-new.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}