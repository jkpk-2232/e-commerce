package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.VendorFeedCommentsUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.VendorFeedCommentModel;

@Path("/api/vendor-feed-comments")
public class VendorFeedCommentAction extends BusinessApiAction {
	
	
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addVendorFeedComment(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		VendorFeedCommentModel vendorFeedCommentModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		long timeInMillis = System.currentTimeMillis(); 
	        Timestamp specificTimestamp = new Timestamp(timeInMillis);
		vendorFeedCommentModel.setCreatedAt(specificTimestamp);
		vendorFeedCommentModel.insertVendorFeedComment(loggedInuserId);
		
		return sendSuccessMessage(messageForKey("successVendorFeedCommetAdded", request));
	}
	
	
	@Path("/{postId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorFeedDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("postId") String postId,
		@PathParam("start") int start,
		@PathParam("length") int length
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> finalList = new HashMap<>();
		
		List<VendorFeedCommentModel> vendorFeedCommentList = VendorFeedCommentsUtils.getVendorFeedCommentsByPostId(postId, start, length);
		finalList.put("feedCommentList", vendorFeedCommentList);
		
		return sendDataResponse(vendorFeedCommentList);
	}
	
	
	@Path("/{commentId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getSubCommentList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("commentId") String commentId
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> finalList = new HashMap<>();
		
		List<VendorFeedCommentModel> subCommentsList = VendorFeedCommentModel.getVendorFeedCommentsByParentComment(commentId);
		finalList.put("subCommentsList", subCommentsList);
		
		return sendDataResponse(subCommentsList);
	}
	

	

}
