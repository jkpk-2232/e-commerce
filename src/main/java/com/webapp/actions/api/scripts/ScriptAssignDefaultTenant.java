//package com.webapp.actions.api.scripts;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Response;
//
//import com.jeeutils.DateUtils;
//import com.utils.UUIDGenerator;
//import com.webapp.ProjectConstants;
//import com.webapp.actions.BusinessApiAction;
//import com.webapp.models.CarVendorsModel;
//import com.webapp.models.DriverVendorsModel;
//import com.webapp.models.UserModel;
//
//@Path("/api/script-assign-default-vendor")
//public class ScriptAssignDefaultTenant extends BusinessApiAction {
//
//	@Path("/passenger/{defaultVendorId}")
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getCarFare(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response, 
//		@PathParam("defaultVendorId") String defaultVendorId
//		) throws SQLException {
//	//@formatter:on
//
//		/**
//		 * 1. In "driver_vendors" table, assign existing passenger to the default
//		 * vendor_id from webapp properties
//		 **/
//
//		int limit = 500;
//		for (int offset = 0; offset <= 18500; offset = offset + 500) {
//
//			List<UserModel> existingPassengerUsers = UserModel.getScriptAssignDefaultVendorUsers(offset, limit);
//			List<DriverVendorsModel> list = new ArrayList<DriverVendorsModel>();
//
//			for (UserModel userModel : existingPassengerUsers) {
//
//				DriverVendorsModel driverVendorModel = new DriverVendorsModel();
//
//				driverVendorModel.setDriverVendorId(UUIDGenerator.generateUUID());
//				driverVendorModel.setDriverId(userModel.getUserId());
//				driverVendorModel.setVendorId(defaultVendorId);
//				driverVendorModel.setCreatedBy("Script Assign Default Vendor");
//				driverVendorModel.setUpdatedBy("Script Assign Default Vendor");
//				driverVendorModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
//				driverVendorModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
//				driverVendorModel.setRoleId(ProjectConstants.PASSENGER_ROLE_ID);
//
//				list.add(driverVendorModel);
//			}
//
//			if (list.size() > 0) {
//				DriverVendorsModel.batchInsertDefaultUserVendorMapping(list);
//			}
//		}
//
//		return sendSuccessMessage("Passenger mapping for default vendor success");
//	}
//
//	@Path("/driver/{defaultVendorId}")
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getCarFare1(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response, 
//		@PathParam("defaultVendorId") String defaultVendorId
//		) throws SQLException {
//	//@formatter:on
//
//		/**
//		 * 2. In "driver_vendors" table, assign existing driver which are mapped to
//		 * vendor_id = "-1" as Admin to the default vendor_id from webapp properties.
//		 **/
//
//		DriverVendorsModel driverVendorModel = new DriverVendorsModel();
//		driverVendorModel.setVendorId(defaultVendorId);
//		driverVendorModel.setCreatedBy("Script Assign Default Vendor");
//		driverVendorModel.setUpdatedBy("Script Assign Default Vendor");
//		driverVendorModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
//		driverVendorModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
//
//		driverVendorModel.batchUpdateExistingAdminDriverMappingToVendor();
//
//		return sendSuccessMessage("Driver mapping for default vendor success");
//	}
//
//	@Path("/car/{defaultVendorId}")
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getCarFare2(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response, 
//		@PathParam("defaultVendorId") String defaultVendorId
//		) throws SQLException {
//	//@formatter:on
//
//		/**
//		 * 3. "car_vendors" assign existing car which are mapped to vendor_id = "-1" as
//		 * Admin to the default vendor_id from webapp properties.
//		 **/
//
//		CarVendorsModel carVendorsModel = new CarVendorsModel();
//		carVendorsModel.setVendorId(defaultVendorId);
//		carVendorsModel.setCreatedBy("Script Assign Default Vendor");
//		carVendorsModel.setUpdatedBy("Script Assign Default Vendor");
//		carVendorsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
//		carVendorsModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
//
//		carVendorsModel.batchUpdateExistingAdminCarMappingToVendor();
//
//		return sendSuccessMessage("Car mapping for default vendor success");
//	}
//}