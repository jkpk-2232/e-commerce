package com.webapp.actions.secure.vendor.feeds;

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

@Path("/manage-custom-feeds")
public class ManageCustomFeedsAction extends BusinessAction {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getVendorcustomFeeds(
        @Context HttpServletRequest request,
        @Context HttpServletResponse response
    ) throws ServletException, IOException {
        
        LOGGER.info("Entered getVendorFeeds method");

        preprocessRequestNewTheme(request, response);

        LOGGER.info("Loading view for manage-vendor-feeds.jsp");
        return loadView(UrlConstants.JSP_URLS.MANAGE_CUSTOM_FEEDS_JSP);
    }

    @Path("/list")
    @GET
    @Produces({ "application/json", "application/xml" })
    public Response getVendorcustomFeedsList(
        @Context HttpServletRequest request, 
        @Context HttpServletResponse response
    ) throws ServletException, IOException {

        preprocessRequestNewTheme(request, response);

        // Create JSON Array with static data
        JSONArray feedsArray = new JSONArray();

        JSONObject feed1 = new JSONObject();
        feed1.put("vendorFeedId", "1");
        feed1.put("vendorName", "Vendor 1");
        feed1.put("storeName", "Store A");
        feed1.put("feedName", "Feed 1");
        feed1.put("feedMessage", "Message 1");
        feed1.put("mediaType", "Image");
        feed1.put("feedLikesCount", 100);
        feed1.put("feedViewsCount", 500);
        feed1.put("createdAt", "2024-08-29 10:00 AM");
        feedsArray.put(feed1);

        // Add more feeds similarly (for demonstration, only one feed is shown)

        // Processing the feeds into a format suitable for DataTables
        JSONArray dtuOuterJsonArray = new JSONArray();
        for (int i = 0; i < feedsArray.length(); i++) {
            JSONObject feed = feedsArray.getJSONObject(i);

            JSONArray dtuInnerJsonArray = new JSONArray();
            dtuInnerJsonArray.put(feed.getString("vendorFeedId"));
            dtuInnerJsonArray.put(i + 1); // srNo
            dtuInnerJsonArray.put(feed.getString("vendorName"));
            dtuInnerJsonArray.put(feed.getString("storeName"));
            dtuInnerJsonArray.put(feed.getString("feedName"));
            dtuInnerJsonArray.put(feed.getString("feedMessage"));
            dtuInnerJsonArray.put(feed.getString("mediaType"));
            dtuInnerJsonArray.put(feed.getInt("feedLikesCount"));
            dtuInnerJsonArray.put(feed.getInt("feedViewsCount"));
            dtuInnerJsonArray.put(feed.getString("createdAt"));

            // Action buttons HTML
            String btnGroupStr = "<div style=\"display: flex; gap: 5px;\">" +
                    "<button class=\"btn btn-sm btn-success\"><i class=\"fa-solid fa-pen-to-square\"></i></button>" +
                    "<button class=\"btn btn-sm btn-danger\"><i class=\"fa-solid fa-trash\"></i></button>" +
                    "</div>";

            dtuInnerJsonArray.put(btnGroupStr); // Adding action buttons to the last column

            dtuOuterJsonArray.put(dtuInnerJsonArray);
        }

        // Static total and filtered count
        int total = feedsArray.length();
        int filterCount = total;

        // Prepare the final JSON object
        JSONObject dtuJsonObject = new JSONObject();
        DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

        return sendDataResponse(dtuJsonObject.toString());
    }

    @Override
    protected String[] requiredJs() {
        LOGGER.info("Entered requiredJs() method of ManageCustomFeedsAction");

        List<String> requiredJS = new ArrayList<>();
        requiredJS.add("js/viewjs/vendor-feeds/manage-custom-feeds.js");
        requiredJS.add("new-ui/js/moment-with-locales.min.js");
        requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

        return requiredJS.toArray(new String[0]);
    }
}
