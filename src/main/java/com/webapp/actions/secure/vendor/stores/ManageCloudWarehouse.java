package com.webapp.actions.secure.vendor.stores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.utils.myhub.DatatableUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.VendorStoreModel;

@Path("/manage-cloud-warehouse")
public class ManageCloudWarehouse extends BusinessAction {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        // Preprocess request for the new theme (if necessary)
        preprocessRequestNewTheme(request, response);
        System.out.println("*** rest ****");
        // Directly load the desired JSP page
        return loadView(UrlConstants.JSP_URLS.MANAGE_CLOUD_WAREHOUSE_JSP);
    }

    @Path("/list")
    @GET
    @Produces({ "application/json", "application/xml" })
    public Response getVendorStoresWithRacks(
        @Context HttpServletRequest request, 
        @Context HttpServletResponse response
    ) throws ServletException, IOException {

        preprocessRequestNewTheme(request, response);

        // Remove session and role checks for testing in Postman
        // Removed: loginSessionMap and UserRoleUtils validation

        DatatableUtils dtu = new DatatableUtils(request);
        String vendorId = dtu.getRequestParameter(FieldConstants.VENDOR_ID);

        // Removed: Role-based vendor ID assignment

        List<String> vendorStoreIdList = new ArrayList<>();

        // Handle sub-vendor logic (this part is optional and can also be removed if not needed for testing)
        vendorStoreIdList = null;  // Set to null to fetch all stores

        // Fetch stores with racks using the updated DAO method
        List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoresWithRacks(vendorId, vendorStoreIdList);

        dtuOuterJsonArray = new JSONArray();
        int count = dtu.getStartInt();

        for (VendorStoreModel vendorStoreModel : vendorStoreList) {
            count++;

            // Create the JSON array for map data
            dtuInnerJsonArray = new JSONArray();
            dtuInnerJsonArray.put(vendorStoreModel.getVendorStoreId());
            dtuInnerJsonArray.put(count);
            dtuInnerJsonArray.put(vendorStoreModel.getVendorName());
            dtuInnerJsonArray.put(vendorStoreModel.getStoreName() != null 
                ? vendorStoreModel.getStoreName() 
                : ProjectConstants.NOT_AVAILABLE
            );
            dtuInnerJsonArray.put(vendorStoreModel.getStoreAddress());

            // Check for presence of racks and add map data
            if (vendorStoreModel.getNumberOfRacks() > 0) {
                dtuInnerJsonArray.put(vendorStoreModel.getStoreAddressLat());  // Correct getter method
                dtuInnerJsonArray.put(vendorStoreModel.getStoreAddressLng());  // Correct getter method

                dtuOuterJsonArray.put(dtuInnerJsonArray);
            }
        }    

        DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, 0, 0);

        return sendDataResponse(dtuJsonObject.toString());
    }

    @Override
    protected String[] requiredJs() {
        List<String> requiredJS = new ArrayList<>();

        requiredJS.add("js/viewjs/vendor-stores/manage-cloud-warehouse.js");
        requiredJS.add("new-ui/js/moment-with-locales.min.js");
        requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

        return requiredJS.toArray(new String[requiredJS.size()]);
    }
}
