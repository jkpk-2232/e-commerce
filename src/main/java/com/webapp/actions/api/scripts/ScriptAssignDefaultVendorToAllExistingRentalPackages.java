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
//import com.webapp.actions.Action;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.RentalPackageModel;
//
//@Path("/api/script-assign-default-vendor-to-rental-packages")
//public class ScriptAssignDefaultVendorToAllExistingRentalPackages extends BusinessApiAction {
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
//		// 1. Get the default vendor
//
//		// 2. Assign that default vendor to existing rental packages
//
//		List<RentalPackageModel> rentalList = RentalPackageModel.getAllRentalPackages();
////		rentalList = rentalList.stream().filter(rentalPackageModel -> rentalPackageModel.getVendorId() == null).collect(Collectors.toList());
////
////		rentalList.forEach(rpm -> {
////			rpm.setVendorId(Action.DEFAULT_VENDOR_ID);
////			rpm.setVendorId("1");
////			rpm.updateVendorIdForRentalPackages();
////		});
//
//		for (RentalPackageModel rentalPackageModel : rentalList) {
//			if (rentalPackageModel.getVendorId() == null || "".equalsIgnoreCase(rentalPackageModel.getVendorId())) {
//				rentalPackageModel.setVendorId(Action.DEFAULT_VENDOR_ID);
//				rentalPackageModel.updateVendorIdForRentalPackages();
//			}
//		}
//
//		return sendSuccessMessage("All existing rental packages assigned to the default vendor");
//	}
//}