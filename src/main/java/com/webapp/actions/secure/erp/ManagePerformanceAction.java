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
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-performance")
public class ManagePerformanceAction extends BusinessAction {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        preprocessRequestNewTheme(request, response);
        return loadView(UrlConstants.JSP_URLS.MANAGE_PERFORMANACE_JSP);
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
          
            dtuInnerJsonArray.put(user.getString("id"));               // ID
            dtuInnerJsonArray.put(user.getInt("srNo"));                // SrNo
            dtuInnerJsonArray.put(user.getString("storeOwner"));       // Store Owner
            dtuInnerJsonArray.put(user.getString("storeName"));        // Store Name
            dtuInnerJsonArray.put(user.getString("storeLocation"));    // Store Location
            dtuInnerJsonArray.put(user.getString("emailId"));          // Email ID
            dtuInnerJsonArray.put(user.getString("phoneNumber"));      // Phone Number
            dtuInnerJsonArray.put(user.getString("employeeLocation")); // Employee Location  **NEW COLUMN**
            
            // Using the existing outputBadge method
            dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(
                OUTPUT_BADGE_TYPES.valueOf(user.getString("status").toUpperCase()), 
                "Status - " + user.getString("status"))
            );

            // Action buttons
            StringBuilder btnGroupStr = new StringBuilder();
            btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), " "));
            btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.DEACTIVATE, messageForKeyAdmin("labelDeactivate"), " "));
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
        usersList.add(createUser("1", 1, "John Doe", "Store A", "Location A", "john@storea.com", "123-456-7890", "Employee Location A", "ACTIVE"));
        usersList.add(createUser("2", 2, "Jane Smith", "Store B", "Location B", "jane@storeb.com", "234-567-8901", "Employee Location B", "ACTIVE"));
        usersList.add(createUser("3", 3, "Alex Johnson", "Store C", "Location C", "alex@storec.com", "345-678-9012", "Employee Location C", "ACTIVE"));
        usersList.add(createUser("4", 4, "Chris Lee", "Store D", "Location D", "chris@stored.com", "456-789-0123", "Employee Location D", "ACTIVE"));
        usersList.add(createUser("5", 5, "Patricia Brown", "Store E", "Location E", "patricia@storee.com", "567-890-1234", "Employee Location E", "ACTIVE"));
        return usersList;
    }

    /**
     * Creates a user JSON object.
     */
    private JSONObject createUser(String id, int srNo, String storeOwner, String storeName, String storeLocation, String emailId, String phoneNumber, String employeeLocation, String status) {
        JSONObject user = new JSONObject();
        user.put("id", id);
        user.put("srNo", srNo);
        user.put("storeOwner", storeOwner);
        user.put("storeName", storeName);
        user.put("storeLocation", storeLocation);
        user.put("emailId", emailId);
        user.put("phoneNumber", phoneNumber);
        user.put("employeeLocation", employeeLocation);  // Adding employee location data
        user.put("status", status);
        return user;
    }

    @Override
    protected String[] requiredJs() {
        return new String[] {
            "js/viewjs/erp-users/manage-performance.js",  // Correct JS path
            "new-ui/js/moment-with-locales.min.js", 
            "new-ui/js/bootstrap-material-datetimepicker.js"
        };
    }
}
