//package com.webapp.actions.api.scripts;
//
//import java.sql.SQLException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//
//import com.jeeutils.DateUtils;
//import com.utils.multitenants.MultiTenantUtils;
//import com.webapp.actions.Action;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.TourModel;
//
//@Path("/api/script-assign-vendor-to-all-existing-trips")
//public class ScriptAssignVendorsToAllExistingTrips extends BusinessApiAction {
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
//		/*
//		 * 1. On 8th May we had handed over multi tenant to Bilwam. So the trips before
//		 * 8th May 2019 should be assigned with default vendor id for passenger and
//		 * vendor
//		 */
//
//		// 8th May 2019 00:00:00 -> 1557273600000
//		// Assign default vendor to all the trips before 8th May
//
//		long tourDate = DateUtils.getStartOfDayDatatableUpdated("08/05/2019", DateUtils.DATE_FORMAT_FOR_VIEW, "Asia/Calcutta");
//
//		TourModel tourModel = new TourModel();
//		tourModel.setPassengerVendorId(Action.DEFAULT_VENDOR_ID);
//		tourModel.setDriverVendorId(Action.DEFAULT_VENDOR_ID);
//		tourModel.setCreatedAt(tourDate);
//		tourModel.updateScriptPassengerAndDriverVendorIdByCreatedAt();
//
//		/*
//		 * 2. Fetch all the trips after 8th May 2019 and then calculate each trips
//		 * passenger vendor id and driver vendor id and assign them IF NOT ALREADY
//		 * PRESENT
//		 */
//
//		MultiTenantUtils.processDefaultVendorForTripsAfterMultiTenantDeployment(tourDate);
//
//		return sendSuccessMessage("All trips assigned with relevant vendors... thread is running in background. Wait for atleast 10 mins");
//	}
//}