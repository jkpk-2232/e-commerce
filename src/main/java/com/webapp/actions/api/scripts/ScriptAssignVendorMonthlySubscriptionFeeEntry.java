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
//import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
//import com.webapp.ProjectConstants;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.UserProfileModel;
//
//@Path("/api/script-assign-vendor-monthly-subscription-fee-entry")
//public class ScriptAssignVendorMonthlySubscriptionFeeEntry extends BusinessApiAction {
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
//		 * 1. Script to make an default entry in the table for vendor monthly
//		 * subsription fee
//		 **/
//
//		// Step 1: Fetch all existing vendors
//		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(ProjectConstants.VENDOR_ROLE_ID);
//
//		// Step 2: Take a vendor at one time and assign all region car fare to that
//		// vendor
//		for (UserProfileModel userProfileModel : vendorList) {
//			VendorMonthlySubscriptionHistoryUtils.addFreeVendorMonthlySubscriptionEntryViaScript(userProfileModel, "1");
//		}
//
//		return sendSuccessMessage("All vendor -> script to make an default entry in the table for vendor monthly subsription fee -> Done");
//	}
//}