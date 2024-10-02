//package com.webapp.actions.api.scripts;
//
//import java.io.IOException;
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
//@Path("/api/script-assign-take-ride-url-to-admin-vendor-existing")
//public class ScriptAssignTakeRideUrlToAllVendorsAndAdmin extends BusinessApiAction {
//
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getCarFare(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response
//		) throws SQLException, IOException {
//	//@formatter:on
//
//		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(ProjectConstants.VENDOR_ROLE_ID);
//		List<UserProfileModel> adminList = UserProfileModel.getUserIdByRoleId(ProjectConstants.ADMIN_ROLE_ID);
//
//		MultiTenantUtils.assignTakeRideUrlToExistingUsers(vendorList);
//		MultiTenantUtils.assignTakeRideUrlToExistingUsers(adminList);
//
//		return sendSuccessMessage("All vendors and admin existing are assigned to take ride url. Please wait for 10 mins to get it executed in the backend");
//	}
//}