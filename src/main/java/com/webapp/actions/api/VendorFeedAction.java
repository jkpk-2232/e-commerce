package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.FeedUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.VendorFeedLikeModel;
import com.webapp.models.VendorFeedModel;
import com.webapp.models.VendorFeedViewModel;
import com.webapp.models.VendorSubscriberModel;

@Path("/api/vendor-feeds")
public class VendorFeedAction extends BusinessApiAction {

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addVendorFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		VendorFeedModel vendorFeedModel
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

		errorMessages = vendorFeedModelValidation(vendorFeedModel, headerVendorId, request);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		FeedUtils.insertFeed(vendorFeedModel, loggedInuserId, loggedInuserId);

		return sendSuccessMessage(messageForKey("successVendorFeedAdded", request));
	}

	@Path("/{vendorId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorFeedsForVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
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

		List<VendorFeedModel> vendorFeedList = VendorFeedModel.getVendorFeedsByVendorIdAndVendorStoreId(vendorId, null, start, length, "%" + searchKey + "%", loggedInuserId);
		finalList.put("feedList", vendorFeedList);
		
		int count = VendorSubscriberModel.getVendorSubscribersCountByVendorId(vendorId);
		finalList.put("subscribersCount", count);
		
		return sendDataResponse(finalList);
	}

	@Path("/subscribers/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorFeedsForSubscribers(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on
		
		String loggedInuserId = sessionKeyHeader;
		
		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			loggedInuserId = checkValidSession(sessionKeyHeader);
			if (loggedInuserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, List<VendorFeedModel>> finalList = new HashMap<>();

		List<VendorFeedModel> subscriberFeedList = VendorFeedModel.getVendorFeedsBySubscriberId(loggedInuserId, start, length, "%" + searchKey + "%", ProjectConstants.NOTIFICATION_TYPE_VENDOR_FEED);
		finalList.put("feedList", subscriberFeedList);

		return sendDataResponse(finalList);
	}

//	Important NOTE -> This api is now embedded with order detail api
//	@Path("/view/{vendorFeedId}")
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response viewVendorFeed(
//		@Context HttpServletRequest request, 
//		@Context HttpServletResponse response, 
//		@HeaderParam("x-language-code") String lang,
//		@HeaderParam("x-vendor-id") String headerVendorId,
//		@HeaderParam("x-api-key") String sessionKeyHeader,
//		@PathParam("vendorFeedId") String vendorFeedId
//		) throws SQLException {
//	//@formatter:on
//
//		String loggedInuserId = checkValidSession(sessionKeyHeader);
//		if (loggedInuserId == null) {
//			return sendUnauthorizedRequestError();
//		}
//
//		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
//		if (headerVendorId == null) {
//			return sendBussinessError(messageForKey("errorInvalidVendor", request));
//		}
//
//		boolean isVendorFeedViewedByUserId = VendorFeedViewModel.isVendorFeedViewedByUserId(loggedInuserId, vendorFeedId);
//		if (isVendorFeedViewedByUserId) {
//			return sendBussinessError(messageForKey("errorVendorFeedAlreadyViewed", request));
//		}
//
//		VendorFeedModel vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);
//		if (vfm == null) {
//			return sendBussinessError(messageForKey("errorVendorFeedNotExists", request));
//		}
//
//		FeedUtils.markFeedAsViewed(vendorFeedId, loggedInuserId, vfm);
//
//		return sendSuccessMessage(messageForKey("successVendorFeedMarkedView", request));
//	}

	@Path("/like/{vendorFeedId}/{isLike}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response likeDislikeVendorFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorFeedId") String vendorFeedId,
		@PathParam("isLike") String isLike
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

		VendorFeedModel vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);
		if (vfm == null) {
			return sendBussinessError(messageForKey("errorVendorFeedNotExists", request));
		}

		boolean isLikeBoolean = Boolean.parseBoolean(isLike);
		boolean isVendorFeedLikedByUserId = VendorFeedLikeModel.isVendorFeedLikedByUserId(loggedInuserId, vendorFeedId);

		if (isLikeBoolean) {

			// like feed flow
			if (isVendorFeedLikedByUserId) {
				return sendBussinessError(messageForKey("errorVendorFeedAlreadyLiked", request));
			}

			FeedUtils.likeFeed(vendorFeedId, loggedInuserId, vfm);

			return sendSuccessMessage(messageForKey("successVendorFeedLiked", request));

		} else {

			// dislike feed flow
			if (!isVendorFeedLikedByUserId) {
				return sendBussinessError(messageForKey("errorVendorFeedNotLiked", request));
			}

			FeedUtils.disLikeFeed(vendorFeedId, loggedInuserId, vfm);

			return sendSuccessMessage(messageForKey("successVendorFeedDisLiked", request));
		}
	}

	@Path("/{vendorFeedId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response vendorFeedDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorFeedId") String vendorFeedId
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

		VendorFeedModel vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);
		if (vfm == null) {
			return sendBussinessError(messageForKey("errorVendorFeedNotExists", request));
		}

		// If the order details api is called, then mark feed as viewed if not
		// previously viewed by the user.

		boolean isVendorFeedLikedByUserId = VendorFeedLikeModel.isVendorFeedLikedByUserId(loggedInuserId, vendorFeedId);
		boolean isVendorFeedViewedByUserId = VendorFeedViewModel.isVendorFeedViewedByUserId(loggedInuserId, vendorFeedId);

		if (!isVendorFeedViewedByUserId) {

			FeedUtils.markFeedAsViewed(vendorFeedId, loggedInuserId, vfm);

			isVendorFeedViewedByUserId = true;
		}

		vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);

		vfm.setFeedLiked(isVendorFeedLikedByUserId);
		vfm.setFeedViewed(isVendorFeedViewedByUserId);

		return sendDataResponse(vfm);
	}

	@Path("/{vendorFeedId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorFeedId") String vendorFeedId
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

		VendorFeedModel vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);
		if (vfm == null) {
			return sendBussinessError(messageForKey("errorVendorFeedNotExists", request));
		}

		FeedUtils.deleteFeeds(vendorFeedId);

		return sendSuccessMessage(messageForKey("successVendorFeedDeleted", request));
	}
	
	@Path("/{vendorId}/{vendorStoreId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorFeedsForVendorStroeId(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("vendorStoreId") String vendorStoreId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on
		
		String loggedInuserId = sessionKeyHeader;
		
		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			loggedInuserId = checkValidSession(sessionKeyHeader);
			if (loggedInuserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> finalList = new HashMap<>();

		List<VendorFeedModel> vendorFeedList = VendorFeedModel.getVendorFeedsByVendorIdAndVendorStoreId(vendorId, vendorStoreId, start, length, "%" + searchKey + "%", loggedInuserId);
		finalList.put("feedList", vendorFeedList);
		
		int count = VendorSubscriberModel.getVendorSubscribersCountByVendorId(vendorId);
		finalList.put("subscribersCount", count);
		
		return sendDataResponse(finalList);
	}
	
	@Path("/new/{vendorId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorFeedsForVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("regionId") String regionId,
		@QueryParam("searchKey") String searchKey
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

		List<VendorFeedModel> vendorFeedList = VendorFeedModel.getVendorFeedsByVendorIdAndVendorStoreIdAndRegion(regionId, vendorId, null, start, length, "%" + searchKey + "%", loggedInuserId);
		finalList.put("feedList", vendorFeedList);
		
		int count = VendorSubscriberModel.getVendorSubscribersCountByVendorId(vendorId);
		finalList.put("subscribersCount", count);
		
		return sendDataResponse(finalList);
	}
	
	
	@Path("/new/subscribers/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorFeedsForSubscribersNew(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("regionId") String regionId,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on
		
		String loggedInuserId = sessionKeyHeader;
		
		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			loggedInuserId = checkValidSession(sessionKeyHeader);
			if (loggedInuserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, List<VendorFeedModel>> finalList = new HashMap<>();

		List<VendorFeedModel> subscriberFeedList = VendorFeedModel.getNewVendorFeedsBySubscriberId(regionId, loggedInuserId, start, length, "%" + searchKey + "%", ProjectConstants.NOTIFICATION_TYPE_VENDOR_FEED);
		finalList.put("feedList", subscriberFeedList);

		return sendDataResponse(finalList);
	}
	
	@Path("/new/{vendorId}/{vendorStoreId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listVendorFeedsForVendorStroeId(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorId") String vendorId,
		@PathParam("vendorStoreId") String vendorStoreId,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("regionId") String regionId,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on
		
		String loggedInuserId = sessionKeyHeader;
		
		if (!WebappPropertyUtils.GUEST_SESSION_KEY.equals(sessionKeyHeader)) {
			loggedInuserId = checkValidSession(sessionKeyHeader);
			if (loggedInuserId == null) {
				return sendUnauthorizedRequestError();
			}
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> finalList = new HashMap<>();

		List<VendorFeedModel> vendorFeedList = VendorFeedModel.getVendorFeedsByVendorIdAndVendorStoreIdAndRegion(regionId, vendorId, vendorStoreId, start, length, "%" + searchKey + "%", loggedInuserId);
		finalList.put("feedList", vendorFeedList);
		
		int count = VendorSubscriberModel.getVendorSubscribersCountByVendorId(vendorId);
		finalList.put("subscribersCount", count);
		
		return sendDataResponse(finalList);
	}
	
	@Path("/new/{vendorFeedId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response newVendorFeedDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("vendorFeedId") String vendorFeedId
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

		VendorFeedModel vfm = VendorFeedModel.getVendorFeedDetailsByFeedId(vendorFeedId);
		if (vfm == null) {
			return sendBussinessError(messageForKey("errorVendorFeedNotExists", request));
		}

		// If the order details api is called, then mark feed as viewed if not
		// previously viewed by the user.

		boolean isVendorFeedLikedByUserId = VendorFeedLikeModel.isVendorFeedLikedByUserId(loggedInuserId, vendorFeedId);
		boolean isVendorFeedViewedByUserId = VendorFeedViewModel.isVendorFeedViewedByUserId(loggedInuserId, vendorFeedId);

		if (!isVendorFeedViewedByUserId) {

			FeedUtils.markFeedAsViewed(vendorFeedId, loggedInuserId, vfm);

			isVendorFeedViewedByUserId = true;
		}

		vfm = VendorFeedModel.getNewVendorFeedDetailsByFeedId(vendorFeedId);

		vfm.setFeedLiked(isVendorFeedLikedByUserId);
		vfm.setFeedViewed(isVendorFeedViewedByUserId);

		return sendDataResponse(vfm);
	}
	

	private List<String> vendorFeedModelValidation(VendorFeedModel vendorFeedModel, String headerVendorId, HttpServletRequest request) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping("feedName", messageForKey("labelFeedName", request), new MinMaxLengthValidationRule(1, 30));
		validator.addValidationMapping("feedMessage", messageForKey("labelFeedMessage", request), new MinMaxLengthValidationRule(1, 250));

		errorMessages = validator.validate(vendorFeedModel);

		return errorMessages;
	}
}