package com.webapp.actions.api.scripts;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CarFareModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorCarFareModel;
import com.webapp.models.VendorCarTypeModel;

@Path("/api/script-assign-ecommerce-bike-to-all-regions-and-existing-vendors")
public class ScriptAssignEcommerceBikeToAllRegionsAndExistingVendors extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		// Step 1: Get all the regions
		// Step 2: Assign Bike fare to all the regions

		List<MulticityCityRegionModel> cityList = MulticityCityRegionModel.getMulticityRegionDetails();

		for (MulticityCityRegionModel multicityCityRegionModel : cityList) {

			CarFareModel carFareBikeVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Seventh_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(), null,
						ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);

			if (carFareBikeVehicle == null) {

				CarFareModel carFareModel = new CarFareModel();

				carFareModel.setCarTypeId(ProjectConstants.Seventh_Vehicle_ID);
				carFareModel.setMulticityCityRegionId(multicityCityRegionModel.getMulticityCityRegionId());
				carFareModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
				carFareModel.setInitialFare(0);
				carFareModel.setPerKmFare(0);
				carFareModel.setPerMinuteFare(0);
				carFareModel.setFreeDistance(0);
				carFareModel.setKmToIncreaseFare(0);
				carFareModel.setFareAfterSpecificKm(0);

				carFareModel.addCarFare("1", ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
			}
		}

		// Step 3: Get all the vendors & Assign Bike to all the vendors
		// Step 4: Assign Bike fare to all the vendors
		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(UserRoles.VENDOR_ROLE_ID);

		for (UserProfileModel userProfileModel : vendorList) {

			VendorCarTypeModel vendorCarTypeModel = VendorCarTypeModel.getVendorCarTypeListByVendorIdForBike(userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID, ProjectConstants.Seventh_Vehicle_ID);

			if (vendorCarTypeModel == null) {

				vendorCarTypeModel = new VendorCarTypeModel();
				vendorCarTypeModel.setCarTypeId(ProjectConstants.Seventh_Vehicle_ID);
				vendorCarTypeModel.setVendorId(userProfileModel.getUserId());
				vendorCarTypeModel.setServiceTypeId(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
				vendorCarTypeModel.insertVendorCarType(userProfileModel.getUserId());
			}

			for (MulticityCityRegionModel multicityCityRegionModel : cityList) {

				VendorCarFareModel vendorCarFareBikeVehicle = VendorCarFareModel.getVendorCarFareDetailsByRegionCountryAndId(ProjectConstants.Seventh_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(),
							userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);

				if (vendorCarFareBikeVehicle == null) {

					CarFareModel carFareBikeVehicle = CarFareModel.getCarFareDetailsByRegionCountryAndId(ProjectConstants.Seventh_Vehicle_ID, multicityCityRegionModel.getMulticityCityRegionId(), multicityCityRegionModel.getMulticityCountryId(),
								userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);

					MultiTenantUtils.addCarFareAgainstVendor(carFareBikeVehicle, userProfileModel.getUserId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
				}
			}
		}

		return sendSuccessMessage("All ScriptAssignEcommerceBikeToAllRegionsAndExistingVendors Done.");
	}
}