package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorServiceCategoryModel;

@Path("/api/promo-code")
public class PromoCodeAction extends BusinessApiAction {

	@Path("/{vendorId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response searchDrivers(
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

		UserProfileModel user = UserProfileModel.getUserAccountDetailsById(vendorId);
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(user);

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(vendorId);

		double maxDiscountSearch = -1;
		try {
			maxDiscountSearch = Double.parseDouble(searchKey);
		} catch (Exception e) {
			// TODO: handle exception
		}

		List<PromoCodeModel> promoCodeList = PromoCodeModel.getPromoCodeListForSearch(start, length, null, "%" + searchKey + "%", 0, 0, vscm.getServiceTypeId(), vendorId, assignedRegionList, maxDiscountSearch);

		Map<String, Object> finalOuput = new HashMap<>();
		finalOuput.put("promoCodeList", promoCodeList);

		return sendDataResponse(finalOuput);
	}
	
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getPromoCodeList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader
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
		
		UserProfileModel user = UserProfileModel.getUserAccountDetailsById(headerVendorId);
		
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(user);

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(headerVendorId);
		
		List<PromoCodeModel> promoCodeList = PromoCodeModel.getPromoCodeListByserviceTypeIdAndVendorId(vscm.getServiceTypeId(), headerVendorId, assignedRegionList);
		
		Map<String, Object> finalOuput = new HashMap<>();
		finalOuput.put("promoCodeList", promoCodeList);

		return sendDataResponse(finalOuput);
	}
}