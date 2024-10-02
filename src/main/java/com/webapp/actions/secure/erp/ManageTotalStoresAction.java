package com.webapp.actions.secure.erp;

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
import org.json.JSONObject;

import com.utils.myhub.DatatableUtils;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-total-stores")
public class ManageTotalStoresAction extends BusinessAction {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        // Preprocess request for the new theme
        preprocessRequestNewTheme(request, response);
        System.out.println("*** rest ****");
        // Load the JSP page
        return loadView(UrlConstants.JSP_URLS.MANAGE_TOTAL_STORES_JSP);
    }

    @Path("/list")
    @GET
    @Produces({ "application/json", "application/xml" })
    public Response getWarehouseUsersList(
        @Context HttpServletRequest request, 
        @Context HttpServletResponse response
    ) throws ServletException, IOException {

        preprocessRequestNewTheme(request, response);

        List<JSONObject> usersArray = generateUserData();

        JSONArray dtuOuterJsonArray = new JSONArray();
        for (JSONObject user : usersArray) {
            JSONArray dtuInnerJsonArray = new JSONArray();
            dtuInnerJsonArray.put(user.getString("id"));        // ID
            dtuInnerJsonArray.put(user.getInt("srNo"));         // SrNo
            dtuInnerJsonArray.put(user.getString("vendorName")); // Vendor Name
            dtuInnerJsonArray.put(user.getString("storeName"));  // Store Name
            dtuInnerJsonArray.put(user.getString("serviceType")); // Service Type

            // Using the existing outputBadge method
            dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(
                OUTPUT_BADGE_TYPES.valueOf(user.getString("status").toUpperCase()), 
                "Status - " + user.getString("status"))
            );

            // Action buttons
            StringBuilder btnGroupStr = new StringBuilder();
            btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(
                NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, 
                messageForKeyAdmin("labelViewTours"), 
                "")
            );

            dtuInnerJsonArray.put(btnGroupStr); // Adding action buttons to the last column
            dtuOuterJsonArray.put(dtuInnerJsonArray);
        }

        int total = usersArray.size();
        int filterCount = total;

        JSONObject dtuJsonObject = new JSONObject();
        DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

        return sendDataResponse(dtuJsonObject.toString());
    }

    /**
     * Generates static user data.
     */
    private List<JSONObject> generateUserData() {
        List<JSONObject> usersList = new ArrayList<>();
        usersList.add(createUser("1", 1, "Vendor 1", "Store A", "Delivery", "ACTIVE"));
        usersList.add(createUser("2", 2, "Vendor 2", "Store B", "Pickup", "DEACTIVE"));
        usersList.add(createUser("3", 3, "Vendor 3", "Store C", "Delivery", "ACTIVE"));
        usersList.add(createUser("4", 4, "Vendor 4", "Store D", "Pickup", "PENDING"));
        usersList.add(createUser("5", 5, "Vendor 5", "Store E", "Delivery", "ACTIVE"));
        return usersList;
    }

    /**
     * Creates a user JSON object.
     */
    private JSONObject createUser(String id, int srNo, String vendorName, String storeName, String serviceType, String status) {
        JSONObject user = new JSONObject();
        user.put("id", id);
        user.put("srNo", srNo);
        user.put("vendorName", vendorName);
        user.put("storeName", storeName);
        user.put("serviceType", serviceType);
        user.put("status", status);
        return user;
    }
   
    @Override
    protected String[] requiredJs() {
        List<String> requiredJS = new ArrayList<>();
        requiredJS.add("js/viewjs/erp-users/manage-total-stores.js");
        requiredJS.add("new-ui/js/moment-with-locales.min.js");
        requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

        return requiredJS.toArray(new String[requiredJS.size()]);
    }
}
