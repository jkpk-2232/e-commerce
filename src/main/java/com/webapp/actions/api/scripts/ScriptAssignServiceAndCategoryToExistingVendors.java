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
//import com.utils.myhub.ServiceCategoryUtils;
//import com.webapp.ProjectConstants;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.UserProfileModel;
//
//@Path("/api/script-assign-service-category-existing-vendors")
//public class ScriptAssignServiceAndCategoryToExistingVendors extends BusinessApiAction {
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
//		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(ProjectConstants.VENDOR_ROLE_ID);
//
//		for (UserProfileModel userProfileModel : vendorList) {
//			ServiceCategoryUtils.mapVendorServiceCategory("Script", userProfileModel.getUserId(), "1", "1");
//		}
//
//		return sendSuccessMessage("All existing vendors assigned to default service and category");
//	}
//}