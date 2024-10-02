package com.webapp.actions.secure.vendor.account;

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

@Path("/manage-onboarding")
public class ManageOnboardingAction extends BusinessAction {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response)
            throws ServletException, IOException {

        // Preprocess request for the new theme (if necessary)
        preprocessRequestNewTheme(request, response);
        return loadView(UrlConstants.JSP_URLS.MANAGE_ONBOARDING_JSP);
    }

    @Path("/list")
    @GET
    @Produces({ "application/json", "application/xml" })
    public Response getWarehouseUsersList(@Context HttpServletRequest request, @Context HttpServletResponse response)
            throws ServletException, IOException {

        preprocessRequestNewTheme(request, response);

        List<JSONObject> usersArray = generateUserData();

        JSONArray dtuOuterJsonArray = new JSONArray();
        for (JSONObject user : usersArray) {
            JSONArray dtuInnerJsonArray = new JSONArray();
            dtuInnerJsonArray.put(user.getString("id"));        // ID
            dtuInnerJsonArray.put(user.getInt("srNo"));         // SrNo
            dtuInnerJsonArray.put(user.getString("employeeName")); // Employee Name
            dtuInnerJsonArray.put(user.getString("brandName")); // Brand Name
            dtuInnerJsonArray.put(user.getString("vendorName")); // Vendor Name
            dtuInnerJsonArray.put(user.getString("phoneNumber")); // Phone Number
            dtuInnerJsonArray.put(user.getString("emailId"));    // Email ID
            dtuInnerJsonArray.put(user.getString("storeName"));  // Store Name

            // Adding action buttons to the last column
            StringBuilder btnGroupStr = new StringBuilder();
            btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.APPROVE, messageForKeyAdmin("labelApproved"), " "));
            btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.REJECT, messageForKeyAdmin("labelRejected")," "));
            dtuInnerJsonArray.put(btnGroupStr); // Adding action buttons to the last column
            dtuOuterJsonArray.put(dtuInnerJsonArray);
        }

        int total = usersArray.size();
        int filterCount = total;

        JSONObject dtuJsonObject = new JSONObject();
        DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

        return sendDataResponse(dtuJsonObject.toString());
    }

    private List<JSONObject> generateUserData() {
        List<JSONObject> usersList = new ArrayList<>();
        
        usersList.add(createUser("1", 1, "Ravi Kumar", "BrandX", "VendorOne", "9876543210", "ravi.kumar@hyderabadmail.com", "Madhapur, Hyderabad"));
        usersList.add(createUser("2", 2, "Priya Sharma", "FreshMart", "GlobalSupply", "9845123456", "priya.sharma@hyderabadmail.com", "Gachibowli, Hyderabad"));
        usersList.add(createUser("3", 3, "Arjun Reddy", "TechWorks", "MegaDistributors", "9965234512", "arjun.reddy@hyderabadmail.com", "Banjara Hills, Hyderabad"));
        usersList.add(createUser("4", 4, "Anjali Nair", "HealthFirst", "EliteSupply", "9921345678", "anjali.nair@hyderabadmail.com", "Kondapur, Hyderabad"));
        usersList.add(createUser("5", 5, "Rohit Agarwal", "AutoMotive", "RapidLogistics", "9845671234", "rohit.agarwal@hyderabadmail.com", "Secunderabad, Hyderabad"));
        usersList.add(createUser("6", 6, "Sneha Kapoor", "StyleHub", "TopGear Supply Co.", "9834567890", "sneha.kapoor@hyderabadmail.com", "Jubilee Hills, Hyderabad"));
        usersList.add(createUser("7", 7, "Vikram Singh", "BuildPro", "SuperConnect", "9976543210", "vikram.singh@hyderabadmail.com", "Hitech City, Hyderabad"));
        usersList.add(createUser("8", 8, "Nisha Verma", "FoodNation", "PrimeVendors", "9856123456", "nisha.verma@hyderabadmail.com", "Begumpet, Hyderabad"));
        usersList.add(createUser("9", 9, "Ajay Mehra", "TechGear", "FastTrack Distributors", "9967890123", "ajay.mehra@hyderabadmail.com", "Ameerpet, Hyderabad"));
        usersList.add(createUser("10", 10, "Meera Gupta", "HomeEssentials", "VibraCorp", "9823456712", "meera.gupta@hyderabadmail.com", "Manikonda, Hyderabad"));
        usersList.add(createUser("11", 11, "Karan Patel", "Sportify", "NovaTrade", "9876523401", "karan.patel@hyderabadmail.com", "Malkajgiri, Hyderabad"));
        usersList.add(createUser("12", 12, "Swati Joshi", "EduPlus", "SkylineVendors", "9845123678", "swati.joshi@hyderabadmail.com", "Attapur, Hyderabad"));
        usersList.add(createUser("13", 13, "Rakesh Yadav", "GreenHarvest", "MetroDealers", "9876542310", "rakesh.yadav@hyderabadmail.com", "Nizampet, Hyderabad"));
        usersList.add(createUser("14", 14, "Neha Jain", "MedicoCare", "TrinitySupply", "9812345678", "neha.jain@hyderabadmail.com", "Lingampally, Hyderabad"));
        usersList.add(createUser("15", 15, "Amit Sinha", "BrightTech", "AceLogistics", "9832456710", "amit.sinha@hyderabadmail.com", "Dilsukhnagar, Hyderabad"));
        usersList.add(createUser("16", 16, "Deepika Desai", "WellnessHub", "OptimaDistribution", "9956123456", "deepika.desai@hyderabadmail.com", "Lakdikapul, Hyderabad"));
        usersList.add(createUser("17", 17, "Rajesh Menon", "AutoZone", "SuperVendors", "9876123456", "rajesh.menon@hyderabadmail.com", "Mehdipatnam, Hyderabad"));
        usersList.add(createUser("18", 18, "Aishwarya Iyer", "BookWorld", "EliteConnect", "9945234678", "aishwarya.iyer@hyderabadmail.com", "Chanda Nagar, Hyderabad"));
        usersList.add(createUser("19", 19, "Suraj Kumar", "HomeSense", "MetroSuppliers", "9871234567", "suraj.kumar@hyderabadmail.com", "Uppal, Hyderabad"));
        usersList.add(createUser("20", 20, "Shalini Mishra", "PureFresh", "TitanDealers", "9812312345", "shalini.mishra@hyderabadmail.com", "Nagole, Hyderabad"));
        
        return usersList;
    }


    private JSONObject createUser(String id, int srNo, String employeeName, String brandName, String vendorName, 
                                  String phoneNumber, String emailId, String storeName) {
        JSONObject user = new JSONObject();
        user.put("id", id);
        user.put("srNo", srNo);
        user.put("employeeName", employeeName);
        user.put("brandName", brandName);
        user.put("vendorName", vendorName);
        user.put("phoneNumber", phoneNumber);
        user.put("emailId", emailId);
        user.put("storeName", storeName);
        return user;
    }

    @Override
    protected String[] requiredJs() {
        List<String> requiredJS = new ArrayList<>();
        requiredJS.add("js/viewjs/vendor/account/manage-onboarding.js");
        requiredJS.add("new-ui/js/moment-with-locales.min.js");
        requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");
        return requiredJS.toArray(new String[requiredJS.size()]);
    }
}
