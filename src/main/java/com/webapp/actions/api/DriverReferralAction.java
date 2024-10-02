package com.webapp.actions.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.TourReferrerBenefitModel;

@Path("/api/driver-referral")
public class DriverReferralAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getReferralHistory(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("start") long start,
			@PathParam("length") long length
			) {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		if (length <= 0) {
			length = ProjectConstants.LIST_LIMIT;
		}

		if (start <= 0) {
			start = 0;
		}

		Map<Object, Object> outPutMap = new HashMap<Object, Object>();

		List<Map<Object, Object>> referralListMap = new ArrayList<Map<Object, Object>>();

		double totalBenefit = TourReferrerBenefitModel.getTotalDriverBenefitByDriverId(loggedInUserId);

		List<TourReferrerBenefitModel> tourReferrerBenefitModelList = new ArrayList<TourReferrerBenefitModel>();

		tourReferrerBenefitModelList = TourReferrerBenefitModel.getDriverReferrerlListByDriverId(loggedInUserId, start, length);

		for (TourReferrerBenefitModel tourReferrerBenefitModel : tourReferrerBenefitModelList) {

			Map<Object, Object> innerMap = new HashMap<Object, Object>();

			innerMap.put("userTourId", tourReferrerBenefitModel.getUserTourId());
			innerMap.put("passengerName", tourReferrerBenefitModel.getPassengerName());
			innerMap.put("passengerPhotoUrl", tourReferrerBenefitModel.getPassengerPhotoUrl());
			innerMap.put("referrerDriverBenefit", Double.parseDouble(df_new.format(tourReferrerBenefitModel.getReferrerDriverBenefit())));
			innerMap.put("date", tourReferrerBenefitModel.getCreatedAt());

			referralListMap.add(innerMap);
		}

		outPutMap.put("referralList", referralListMap);
		outPutMap.put("totalBenefit", Double.parseDouble(df_new.format(totalBenefit)));

		return sendDataResponse(outPutMap);
	}

}