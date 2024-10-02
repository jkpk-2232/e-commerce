package com.webapp.actions.api;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.BusinessInterestedUsersModel;
import com.webapp.models.RoleModel;

@Path("/api/business-interested-user")
public class BusinessInterestedUsersAction extends BusinessApiAction {
	
	
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoreProductListWithOutPagination(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		BusinessInterestedUsersModel businessInterestedUsersModel
		) throws SQLException {
	//@formatter:on
		try {
			businessInterestedUsersModel.insertBusinessInterestedUser(businessInterestedUsersModel);
			
			HttpURLConnection connection = null;
			try {
				URL postUrl = new URL("https://lms.kisaanparivar.com/webapi/store-leads-from-web?campaign=15");
				connection = (HttpURLConnection) postUrl.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setDoOutput(true);
				
				
				RoleModel roleModel = RoleModel.getRoleDetailsByRoleId(businessInterestedUsersModel.getRoleId());
				String businessType = "";
				String parameters = null;

				if ("erp".equals(roleModel.getRole())) {
					businessType = "brands";
				} else {
					businessType = roleModel.getRole();
				}

				try {
					parameters = "name=" + URLEncoder.encode(businessInterestedUsersModel.getName(), StandardCharsets.UTF_8.toString()) + 
						      "&email=" + URLEncoder.encode(businessInterestedUsersModel.getEmail(), StandardCharsets.UTF_8.toString()) + 
						      "&mobile_no=" + URLEncoder.encode(businessInterestedUsersModel.getPhoneNo(), StandardCharsets.UTF_8.toString()) + 
						      "&source=" + URLEncoder.encode("web", StandardCharsets.UTF_8.toString()) + 
						      "&description=" + URLEncoder.encode(businessInterestedUsersModel.getCity() + " " + businessType + " " + businessInterestedUsersModel.getVechicleType() + " " + businessInterestedUsersModel.getBusinessCategory(),
											StandardCharsets.UTF_8.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

				try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
					wr.writeBytes(parameters);
					wr.flush();
				}

				int responseCode = connection.getResponseCode();
			}
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			            
			}
		} catch (Exception e) {
			return sendBussinessError(messageForKey("errorFailedToAddBusinessUser", request));
		}
		
		
		return sendSuccessMessage(messageForKey("successAddBusinessUser", request));
	}

}
