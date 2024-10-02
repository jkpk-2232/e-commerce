package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorStoreModel;

@Path("/api/vendor-stores")
public class VendorStoreAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoreList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
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

		Map<String, List<VendorStoreModel>> finalList = new HashMap<>();

		String vendorId = loggedInuserId;
		List<String> vendorStoreIdList = new ArrayList<>();
		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);

		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

			// by default send it all for sub vendor
			vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInuserId, false);

			if (vendorStoreIdList.isEmpty()) {
				finalList.put("vendorStoreList", new ArrayList<>());
				return sendDataResponse(finalList);
			}

			vendorId = UserRoleUtils.getParentVendorId(loggedInuserId);

		} else {
			vendorStoreIdList = null;
		}

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreListApi(vendorId, start, length, "%" + searchKey + "%", vendorStoreIdList);
		finalList.put("vendorStoreList", vendorStoreList);

		return sendDataResponse(finalList);
	}

	@Path("/products/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoreProductList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("vendorStoreId") String vendorStoreId,
		@QueryParam("productStatus") String productStatus,
		@QueryParam("searchKey") String searchKey,
		@QueryParam("filterOrder") String filterOrder
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

		if (productStatus != null && ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(productStatus)) {
			productStatus = null;
		}

		if (filterOrder != null && ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(filterOrder)) {
			filterOrder = null;
		}

		Map<String, List<VendorProductModel>> finalList = new HashMap<>();

		String vendorId = loggedInuserId;
		List<String> vendorStoreIdList = new ArrayList<>();
		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);

		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

			if (vendorStoreId != null && ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(vendorStoreId)) {

				// by default send it all for sub vendor
				vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInuserId, false);

				if (vendorStoreIdList.isEmpty()) {
					finalList.put("vendorProductList", new ArrayList<>());
					return sendDataResponse(finalList);
				}
			} else {
				vendorStoreIdList.add(vendorStoreId);
			}

			vendorId = UserRoleUtils.getParentVendorId(loggedInuserId);

		} else {

			if (vendorStoreId != null && !ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(vendorStoreId)) {
				vendorStoreIdList.add(vendorStoreId);
			} else {
				vendorStoreIdList = null;
			}
		}

		List<VendorProductModel> vendorProductList = VendorProductModel.getProductListApi(vendorId, vendorStoreIdList, start, length, productStatus, "%" + searchKey + "%", filterOrder);
		finalList.put("vendorProductList", vendorProductList);

		return sendDataResponse(finalList);
	}
	
	
	@Path("/productList")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getVendorStoreProductListWithOutPagination(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("vendorStoreId") String vendorStoreId,
		@QueryParam("productStatus") String productStatus
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

		/*
		 * if (vendorStoreId != null &&
		 * ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(vendorStoreId))
		 * { vendorStoreId = null; }
		 */

		if (productStatus != null && ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(productStatus)) {
			productStatus = null;
		}

		Map<String, List<VendorProductModel>> finalList = new HashMap<>();
		
		String vendorId = loggedInuserId;
		List<String> vendorStoreIdList = new ArrayList<>();
		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);

		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {
			
			if (vendorStoreId != null && ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(vendorStoreId)) {

				// by default send it all for sub vendor
				vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInuserId, false);
				
				if (vendorStoreIdList.isEmpty()) {
					finalList.put("vendorProductList", new ArrayList<>());
					return sendDataResponse(finalList);
				}
			} else {
				vendorStoreIdList.add(vendorStoreId);
			}

			vendorId = UserRoleUtils.getParentVendorId(loggedInuserId);

		} else {

			if (vendorStoreId != null && !ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(vendorStoreId)) {
				vendorStoreIdList.add(vendorStoreId);
			} else {
				vendorStoreIdList = null;
			}
		}

		List<VendorProductModel> vendorProductList = VendorProductModel.getProductListForStore(vendorId, vendorStoreIdList, productStatus);
		finalList.put("vendorProductList", vendorProductList);

		return sendDataResponse(finalList);
	}
	
	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTotalVendorStores(
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

		Map<String, List<VendorStoreModel>> finalList = new HashMap<>();
		
		String vendorId = loggedInuserId;
		List<String> vendorStoreIdList = new ArrayList<>();
		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);

		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

			// by default send it all for sub vendor
			vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInuserId, false);

			if (vendorStoreIdList.isEmpty()) {
				finalList.put("vendorStoreList", new ArrayList<>());
				return sendDataResponse(finalList);
			}

			vendorId = UserRoleUtils.getParentVendorId(loggedInuserId);

		} else {
			vendorStoreIdList = null;
		}

		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoreListByVendorAndSubVendor( vendorId, vendorStoreIdList);
		finalList.put("vendorStoreList", vendorStoreList);

		return sendDataResponse(finalList);
	}
	
}