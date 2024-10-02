package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.AppointmentUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AppointmentModel;
import com.webapp.models.UserProfileModel;

@Path("/api/vendor-appointments")
public class VendorAppointmentAction extends BusinessApiAction {

	@Path("/{vendorId}/{type}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorAppointments(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("type") String type,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, List<AppointmentModel>> finalList = new HashMap<>();
		UserProfileModel vendorIdUserModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		List<String> vendorStoreIdList = new ArrayList<>();

		if (UserRoleUtils.isSubVendorRole(vendorIdUserModel.getRoleId())) {

			vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(vendorId, false);

			if (vendorStoreIdList.isEmpty()) {
				finalList.put("orderList", new ArrayList<>());
				return sendDataResponse(finalList);
			}

			vendorId = UserRoleUtils.getParentVendorId(vendorId);

		} else {
			vendorStoreIdList = null;
		}

		int orderShortIdSearch = MyHubUtils.searchNumericFormat(searchKey);

		List<String> orderStatus = AppointmentUtils.getAppointmentStatusListAsPerAppointmentType(type, false);

		List<AppointmentModel> appointmentList = AppointmentModel.getAppointmentsSearchAPI("%" + searchKey + "%", start, length, vendorId, orderStatus, orderShortIdSearch, vendorStoreIdList);

		finalList.put("appointmentList", appointmentList);
		return sendDataResponse(finalList);
	}
}