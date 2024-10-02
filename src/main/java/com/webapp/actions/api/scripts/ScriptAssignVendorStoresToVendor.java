package com.webapp.actions.api.scripts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorStoreModel;

@Path("/api/script-assign-vendor-stores-to-vendor")
public class ScriptAssignVendorStoresToVendor extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		List<UserProfileModel> vendorList = UserProfileModel.getUserIdByRoleId(UserRoles.VENDOR_ROLE_ID);

		for (UserProfileModel userProfileModel : vendorList) {

			List<String> storeList = new ArrayList<>();
			List<VendorStoreModel> vendorStoresList = VendorStoreModel.getVendorStoreList(null, userProfileModel.getUserId());
			for (VendorStoreModel storeModel : vendorStoresList) {
				storeList.add(storeModel.getVendorStoreId());
			}

			if (!storeList.isEmpty()) {
				VendorStoreSubVendorUtils.mapSubVendorsToVendorStore(storeList, userProfileModel.getUserId(), userProfileModel.getUserId());
			}
		}

		return sendSuccessMessage("All ScriptAssignEcommerceBikeToAllRegionsAndExistingVendors Done.");
	}
}