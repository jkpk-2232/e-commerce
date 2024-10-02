package com.webapp.actions.api;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorSubscriberModel;

@Path("/api/kp-mart/store-products")
public class KPMartVendorStoreProductsAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoreListServiceWise(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("latitude") String latitude,
		@QueryParam("longitude") String longitude,
		@QueryParam("productCategoryId") String productCategoryId,
		@QueryParam("productSubCategoryId") String productSubCategoryId,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Instant todayInstantObject = DateUtils.getNowInstant();
		String currentDayOfWeekValue = DateUtils.getDayOfWeek(todayInstantObject);

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String latAndLong = "ST_DWithin(vs.store_address_geolocation,ST_GeographyFromText('SRID=4326;POINT(" + longitude + " " + latitude + ")'),  " + adminSettingsModel.getRadiusStoreVisibility() + ")";

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(latitude, longitude);

		String considerDistance = ProjectConstants.NOT_AVAILABLE;

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		boolean isVendorStoreSubscribed = false;
		String serviceId = WebappPropertyUtils.KP_MART_SERVICE_ID;

		List<UserProfileModel> brandList = UserProfileModel.getVendorBrandStoresList(headerVendorId, serviceId, latitude, longitude, latAndLong, loggedInUserId, currentDayOfWeekValue, isVendorStoreSubscribed, considerDistance, multicityCityRegionId);
		LinkedList<Map<String, Object>> vendorMapList = new LinkedList<>();

		for (UserProfileModel userProfileModel : brandList) {

			logger.info("\nuserProfileModel.getDistance()\t" + userProfileModel.getDistance());
			logger.info("\nadminSettings.getRadius()     \t" + (adminSettings.getRadiusStoreVisibility() * ProjectConstants.KM_UNITS));

			if (userProfileModel.getDistance() <= (adminSettings.getRadiusStoreVisibility() * ProjectConstants.KM_UNITS)) {

				logger.info("\nAdding store due to distance");

				boolean isVendorStoreOpen = OrderUtils.isVendorStoreOpen(todayInstantObject, userProfileModel);

				if (isVendorStoreOpen) {
					vendorMapList.add(VendorSubscriberModel.getLimitedData(userProfileModel, isVendorStoreOpen));
				}
			} else {
				logger.info("\nIgnoring store due to distance");
			}
		}

		Map<String, List<VendorProductModel>> finalResponse = new HashMap<>();
		List<VendorProductModel> productList = new ArrayList<>();

		if (vendorMapList.size() > 0) {

			if (productCategoryId.isEmpty())
				productCategoryId = null;

			if (productSubCategoryId.isEmpty()) {
				productSubCategoryId = null;
			}

			productList = VendorProductModel.getProductListWithOutPagination(headerVendorId, vendorMapList.get(0).get("vendorStoreId").toString(), "1", productCategoryId, "%" + searchKey + "%", productSubCategoryId);
		}

		finalResponse.put("productList", productList);

		return sendDataResponse(finalResponse);
	}

}
