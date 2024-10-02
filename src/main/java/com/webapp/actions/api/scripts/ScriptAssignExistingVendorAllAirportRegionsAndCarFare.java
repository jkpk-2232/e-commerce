//package com.webapp.actions.api.scripts;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//
//import com.utils.myhub.MultiTenantUtils;
//import com.webapp.ProjectConstants;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.UserProfileModel;
//
//@Path("/api/script-assign-existing-vendor-all-airport-region-car-fare")
//public class ScriptAssignExistingVendorAllAirportRegionsAndCarFare extends BusinessApiAction {
//
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getCarFare(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response
//		) throws SQLException {
//	//@formatter:on
//
//		/**
//		 * 1. Fetch all the existing vendors
//		 **/
//
//		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(ProjectConstants.VENDOR_ROLE_ID);
//
//		/**
//		 * 2. Take a vendor at one time and assign all existing airport regions and
//		 * there car fare vendor
//		 **/
//
//		for (UserProfileModel userProfileModel : vendorList) {
//			MultiTenantUtils.assignVendorAllExistingAirportRegionsAndCarFare(userProfileModel.getUserId());
//		}
//
//		return sendSuccessMessage("All existing airports and car fare are assigned to all existing vendors.");
//	}
//}